[getBuildReceiptSummary]
select 
  collector_objid, formno, controlid, formtype, denomination, 
  (case when formtype='serial' then min(series) else null end) as startseries, 
  (case when formtype='serial' then max(series) else null end) as endseries, 
  min( series ) as minseries, sum( qty ) as qty , sum( cqty ) as cqty, 
  sum( case when voided=0 then amount else 0.0 end ) as amount,
  sum( case when voided=0 then totalcash else 0.0 end ) as totalcash,
  sum( case when voided=0 then totalnoncash else 0.0 end ) as totalnoncash, 
  afstartseries, afendseries, afnextseries 
from ( 
  select 
    c.objid, c.formno, af.formtype, c.collector_objid, c.controlid, c.series,
    (select count(*) from cashreceipt_void where receiptid=c.objid) as voided, 
    (case when c.state='CANCELLED' then 0 else 1 end) as qty, 
    (case when c.state='CANCELLED' then 1 else 0 end) as cqty, 
    c.amount, c.totalnoncash, (c.amount - c.totalnoncash) as totalcash, 
    afc.startseries as afstartseries, afc.endseries as afendseries, 
    afc.currentseries as afnextseries, af.denomination 
  from cashreceipt c 
    inner join af_control afc on c.controlid=afc.objid 
    inner join af on afc.afid=af.objid 
  where c.remittanceid = $P{remittanceid} 
)tmp1  
group by 
  collector_objid, formno, controlid, formtype, 
  denomination, afstartseries, afendseries, afnextseries 


[getBuildCancelSeries]
select cs.*, c.series 
from cashreceipt c 
  inner join cashreceipt_cancelseries cs on cs.receiptid = c.objid
where c.remittanceid = $P{remittanceid} 
  and c.state = 'CANCELLED' 


[getNonCashPayments]
select 
  nc.objid, nc.refno, nc.particulars, nc.reftype, nc.amount,
  0 as voided, c.subcollector_name
from remittance_noncashpayment remnc 
  inner join cashreceiptpayment_noncash nc on nc.objid=remnc.objid 
  inner join cashreceipt c on nc.receiptid=c.objid 
where remnc.remittanceid = $P{remittanceid} 


[getRemittedCashTickets]
select 
  remittanceid, controlid, formtype, stubno, 
  denomination, startseries, endseries, sum(amount) as amount 
from ( 
  select 
    (select remittanceid from remittance_cashreceipt where objid=c.objid) as remittanceid, 
    c.controlid, af.formtype, afc.stubno, af.denomination, c.amount, afc.startseries, afc.endseries 
  from cashreceipt c
    inner join af_control afc on c.controlid=afc.objid
    inner join af on afc.afid=af.objid 
  where c.controlid = $P{controlid} 
    and c.objid in (select objid from remittance_cashreceipt where objid=c.objid) 
    and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
    and c.objid not in (select receiptid from cashreceipt_cancelseries where receiptid=c.objid) 
    and af.formtype='cashticket' 
)tmp1 
group by  
  remittanceid, controlid, formtype, stubno, 
  denomination, startseries, endseries 


[updateSerialReceipts]
update 
  cashreceipt c, af_control afc, af 
set 
  c.remittanceid = $P{remittanceid} 
where c.remittanceid is null 
  and c.collector_objid = $P{collectorid} 
  and c.receiptdate < $P{txndate} 
  and c.state in ('POSTED','CANCELLED') 
  and c.controlid = afc.objid 
  and afc.afid = af.objid 
  and af.formtype = 'serial' 


[updateNonSerialReceipts]
update 
  cashreceipt c, af_control afc, af 
set 
  c.remittanceid = $P{remittanceid} 
where c.remittanceid is null 
  and c.collector_objid = $P{collectorid} 
  and c.receiptdate < $P{txndate} 
  and c.state = 'POSTED'  
  and c.controlid = afc.objid 
  and afc.afid = af.objid 
  and af.formtype = 'cashticket' 
  and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
  and c.objid not in (select receiptid from cashreceipt_cancelseries where receiptid=c.objid) 


[insertNonCashPayment]
insert into remittance_noncashpayment (
  objid, remittanceid, amount, voided 
) 
select 
  nc.objid, c.remittanceid, nc.amount, 0 as voided 
from cashreceipt c 
  inner join cashreceiptpayment_noncash nc on nc.receiptid=c.objid 
where c.remittanceid = $P{remittanceid} 
  and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
  and c.state not in ('CANCELLED') 


[updateNonCashReceiptPayment]
update 
  remittance_noncashpayment remnc, 
  cashreceiptpayment_noncash nc, 
  remittance_fund remf  
set 
  nc.remittancefundid = remf.objid 
where remnc.remittanceid = $P{remittanceid} 
  and remnc.objid = nc.objid 
  and remf.remittanceid = remnc.remittanceid 
  and remf.fund_objid = nc.fund_objid 


[insertFundSummary] 
insert into remittance_fund ( 
   objid, remittanceid, fund_objid, fund_title, totalcash, totalnoncash, totalcr, amount, cashbreakdown 
) 
SELECT 
    CONCAT(remittanceid, fund_objid) AS objid, remittanceid, fund_objid, fund.title as fund_title,  
    SUM(totalcash) AS totalcash, SUM(totalnoncash) AS totalnoncash, SUM(totalcr) AS totalcr, 
    SUM( totalcash + totalnoncash + totalcr ) as amount, '[]' as cashbreakdown  
FROM ( 
  SELECT 
     c.remittanceid, ia.fund_objid, SUM(ci.amount) AS totalcash, 0.0 AS totalnoncash, 0.0 AS totalcr 
  FROM cashreceipt c 
    INNER JOIN cashreceiptitem ci ON ci.receiptid=c.objid 
    INNER JOIN itemaccount ia ON ci.item_objid=ia.objid 
  WHERE c.remittanceid = $P{remittanceid} 
    AND c.objid NOT IN (SELECT receiptid FROM cashreceipt_void WHERE receiptid=c.objid) 
    AND c.objid NOT IN (SELECT receiptid FROM cashreceiptpayment_noncash WHERE receiptid=c.objid) 
    AND c.state <> 'CANCELLED' 
  GROUP BY ia.fund_objid 
  UNION ALL 
  SELECT 
    c.remittanceid, ci.fund_objid, 0.0 AS totalcash, 0.0 AS totalnoncash, SUM(ci.amount) AS totalcr 
  FROM cashreceipt c 
    INNER JOIN cashreceiptpayment_noncash ci ON ci.receiptid=c.objid 
  WHERE c.remittanceid = $P{remittanceid} 
    AND c.objid NOT IN (SELECT receiptid FROM cashreceipt_void WHERE receiptid=c.objid) 
    AND c.state <> 'CANCELLED' 
    AND ci.reftype = 'CREDITMEMO' 
  GROUP BY ci.fund_objid 
  UNION ALL 
  SELECT 
    c.remittanceid, nc.fund_objid, 0.0 AS totalcash, SUM(nc.amount) AS totalnoncash, 0.0 AS totalcr  
  FROM cashreceipt c 
    INNER JOIN cashreceiptpayment_noncash nc ON nc.receiptid=c.objid 
  WHERE c.remittanceid = $P{remittanceid} 
    AND c.objid NOT IN (SELECT receiptid FROM cashreceipt_void WHERE receiptid=c.objid) 
    AND c.state <> 'CANCELLED' 
    AND nc.reftype <> 'CREDITMEMO' 
  GROUP BY nc.fund_objid 
)tmp1, fund 
where fund.objid=tmp1.fund_objid  
GROUP BY remittanceid, fund_objid, fund.title  

