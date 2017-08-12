[getList]
SELECT r.*,
  CASE WHEN lr.objid IS NULL THEN 0 ELSE 1 END AS liquidated 
FROM remittance r
  LEFT JOIN liquidation_remittance lr ON r.objid=lr.objid 
WHERE r.collector_objid like $P{collectorid} 
  AND r.txnno like $P{searchtext}
ORDER BY r.dtposted DESC 

[getListAll]
SELECT r.*,
  CASE WHEN lr.objid IS NULL THEN 0 ELSE 1 END AS liquidated 
FROM (
  SELECT objid FROM remittance WHERE txnno LIKE $P{searchtext} 
  UNION 
  SELECT objid FROM remittance WHERE collector_name LIKE $P{searchtext} 
)x 
  INNER JOIN remittance r ON x.objid=r.objid 
  LEFT JOIN liquidation_remittance lr ON r.objid=lr.objid 
ORDER BY r.dtposted DESC

[getListBySeries]
SELECT distinct r.*,
CASE WHEN lr.objid IS NULL THEN 0 ELSE 1 END AS liquidated 
FROM remittance r 
INNER JOIN remittance_cashreceipt rc on rc.remittanceid = r.objid
inner join cashreceipt c on c.objid = rc.objid 
LEFT JOIN liquidation_remittance lr ON r.objid=lr.objid 
WHERE r.collector_objid like $P{collectorid}  
  and c.receiptno like $P{searchtext} 
ORDER BY r.collector_name, r.txnno DESC 

[getListByCollector]
SELECT r.*,
CASE WHEN lr.objid IS NULL THEN 0 ELSE 1 END AS liquidated 
FROM remittance r 
LEFT JOIN liquidation_remittance lr ON r.objid=lr.objid 
WHERE r.collector_name like $P{searchtext} 
ORDER BY r.collector_name, r.txnno DESC 

[getUnremittedForCollector]
SELECT 
  c.formno, c.collector_objid, c.controlid, min(c.formtype) as formtype,  
  case when c.formtype='serial' then min(series) else null end as startseries, 
  case when c.formtype='serial' then max(series) else null end as endseries, 
  min( series ) as minseries, 
  sum( CASE WHEN c.state = 'POSTED' then 1 else 0 end ) as qty ,  
  sum( CASE WHEN c.state = 'CANCELLED' then 1 else 0 end ) as cqty , 
  sum( CASE WHEN bt.voided=0 THEN c.amount ELSE 0.0 END ) AS amount,
  sum( CASE WHEN bt.voided=0 THEN c.totalcash-c.cashchange ELSE 0.0 END ) AS totalcash,
  sum( CASE WHEN bt.voided=0 THEN c.totalnoncash ELSE 0.0 END ) AS totalnoncash
FROM ( 
  SELECT objid, 
    (SELECT COUNT(*) FROM cashreceipt_void WHERE receiptid=c.objid) AS voided 
  FROM cashreceipt c 
  WHERE collector_objid = $P{collectorid} 
    AND receiptdate < $P{txndate} 
    AND state IN ('POSTED', 'CANCELLED') 
    AND objid NOT IN (SELECT objid FROM remittance_cashreceipt WHERE objid=c.objid) 
)bt 
  INNER JOIN cashreceipt c ON bt.objid=c.objid 
GROUP BY c.collector_objid, c.formno, c.formtype, c.controlid 

[getUnremittedCancelSeries]
SELECT cs.*, c.series 
FROM (
  SELECT c.objid FROM cashreceipt c 
    LEFT JOIN remittance_cashreceipt r ON c.objid = r.objid 
  WHERE c.controlid = $P{controlid}  
    AND r.objid IS NULL 
    AND c.collector_objid = $P{collectorid} 
    AND c.state = 'CANCELLED' 
)bt 
  INNER JOIN cashreceipt c ON bt.objid=c.objid 
  INNER JOIN cashreceipt_cancelseries cs on cs.receiptid = c.objid

[getUnremittedTotals]
SELECT 
  COUNT(*) AS itemcount, 
  SUM( CASE WHEN v.objid IS NULL THEN c.amount ELSE 0 END ) AS totals
FROM (
  SELECT c.objid FROM cashreceipt c 
    LEFT JOIN remittance_cashreceipt r ON c.objid = r.objid 
  WHERE c.receiptdate < $P{txndate} 
    AND r.objid IS NULL 
    AND c.collector_objid = $P{collectorid} 
    AND c.state = 'POSTED'
)bt 
  INNER JOIN cashreceipt c ON bt.objid=c.objid 
  LEFT JOIN cashreceipt_void v ON c.objid=v.receiptid
GROUP by c.collector_objid

[getUnremittedReceipts]
SELECT 
  c.formno, c.receiptno, c.paidby, c.receiptdate,
  CASE WHEN v.objid IS NULL THEN c.amount ELSE 0 END AS amount,
  CASE WHEN v.objid IS NULL THEN 0 ELSE 1 END AS voided,
  c.subcollector_name
FROM (
  SELECT c.objid FROM cashreceipt c 
    LEFT JOIN remittance_cashreceipt r ON c.objid = r.objid 
  WHERE c.txndate < $P{txndate} 
    AND r.objid IS NULL 
    AND c.collector_objid = $P{collectorid} 
    AND c.state = 'POSTED'
)bt 
  INNER JOIN cashreceipt c ON bt.objid=c.objid 
  LEFT JOIN cashreceipt_void v ON c.objid=v.receiptid
ORDER BY c.receiptno

[getUnremittedChecks]
SELECT a.* FROM 
(
  SELECT 
    crp.objid, crp.refno, crp.particulars, crp.reftype, 
    CASE WHEN cv.objid IS NULL THEN crp.amount ELSE 0 END AS amount,
    CASE WHEN cv.objid IS NULL THEN 0 ELSE 1 END AS voided,
    cash.subcollector_name
  FROM (
    SELECT c.objid FROM cashreceipt c 
      LEFT JOIN remittance_cashreceipt r ON c.objid = r.objid 
    WHERE c.receiptdate < $P{txndate} 
      AND r.objid IS NULL 
      AND c.collector_objid = $P{collectorid} 
      AND c.state = 'POSTED'
  )bt 
    INNER JOIN cashreceipt cash ON bt.objid=cash.objid 
    INNER JOIN cashreceiptpayment_noncash crp ON crp.receiptid=cash.objid 
    LEFT JOIN cashreceipt_void cv ON crp.receiptid = cv.receiptid 
) a 


[insertSerialReceipts]
insert into remittance_cashreceipt ( objid, remittanceid )
select c.objid, $P{remittanceid} as remittanceid 
from cashreceipt c 
  inner join af_control afc on c.controlid=afc.objid 
  inner join af on afc.afid=af.objid 
where c.collector_objid = $P{collectorid} 
  and c.receiptdate < $P{txndate} 
  and c.state in ('POSTED','CANCELLED') 
  and af.formtype='serial' 
  and c.objid not in (select objid from remittance_cashreceipt where objid=c.objid) 


[insertCashTickets]
insert into remittance_cashreceipt ( objid, remittanceid )
select c.objid, $P{remittanceid} as remittanceid 
from cashreceipt c 
  inner join af_control afc on c.controlid=afc.objid 
  inner join af on afc.afid=af.objid 
where c.collector_objid = $P{collectorid} 
  and c.receiptdate < $P{txndate} 
  and c.state = 'POSTED' 
  and af.formtype='cashticket' 
  and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
  and c.objid not in (select objid from remittance_cashreceipt where objid=c.objid) 


[insertNonCashPayment]
insert into remittance_noncashpayment (
  objid, remittanceid, amount, voided 
) 
select 
  nc.objid, remc.remittanceid, nc.amount, 0 as voided 
from remittance_cashreceipt remc 
  inner join cashreceipt c on remc.objid=c.objid 
  inner join cashreceiptpayment_noncash nc on nc.receiptid=c.objid 
where remc.remittanceid = $P{remittanceid} 
  and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
  and c.state not in ('CANCELLED') 


[getNonCashPayments]
select 
  nc.objid, nc.refno, nc.particulars, nc.reftype, nc.amount,
  0 as voided, c.subcollector_name
from remittance_noncashpayment remnc 
  inner join cashreceiptpayment_noncash nc on nc.objid=remnc.objid 
  inner join cashreceipt c on nc.receiptid=c.objid 
where remnc.remittanceid = $P{remittanceid} 
  and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
  and c.state <> 'CANCELLED' 


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
    remc.objid, c.formno, af.formtype, c.collector_objid, c.controlid, c.series,
    (select count(*) from cashreceipt_void where receiptid=remc.objid) as voided, 
    (case when c.state='CANCELLED' then 0 else 1 end) as qty, 
    (case when c.state='CANCELLED' then 1 else 0 end) as cqty, 
    c.amount, c.totalnoncash, (c.totalcash-c.cashchange) as totalcash, 
    afc.startseries as afstartseries, afc.endseries as afendseries, 
    afc.currentseries as afnextseries, af.denomination 
  from remittance_cashreceipt remc 
    inner join cashreceipt c on remc.objid=c.objid 
    inner join af_control afc on c.controlid=afc.objid 
    inner join af on afc.afid=af.objid 
  where remc.remittanceid = $P{remittanceid} 
)tmp1  
group by 
  collector_objid, formno, controlid, formtype, 
  denomination, afstartseries, afendseries, afnextseries 


[xgetBuildFundSummary]
select 
  remc.remittanceid, fund.objid as fund_objid, fund.title as fund_title, sum(ci.amount) as amount  
from remittance_cashreceipt remc 
  inner join cashreceipt c on remc.objid=c.objid 
  inner join cashreceiptitem ci on ci.receiptid=c.objid 
  inner join itemaccount ia on ci.item_objid=ia.objid 
  inner join fund on ia.fund_objid=fund.objid 
where remc.remittanceid = $P{remittanceid} 
  and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
  and c.state not in ('CANCELLED') 
group by remc.remittanceid, fund.objid, fund.title 


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
     remc.remittanceid, ia.fund_objid, SUM(ci.amount) AS totalcash, 0.0 AS totalnoncash, 0.0 AS totalcr 
  FROM remittance_cashreceipt remc 
    INNER JOIN cashreceipt c ON c.objid=remc.objid 
    INNER JOIN cashreceiptitem ci ON ci.receiptid=c.objid 
    INNER JOIN itemaccount ia ON ci.item_objid=ia.objid 
  WHERE remc.remittanceid=$P{remittanceid} 
    AND c.objid NOT IN (SELECT receiptid FROM cashreceipt_void WHERE receiptid=c.objid) 
    AND c.objid NOT IN (SELECT receiptid FROM cashreceiptpayment_noncash WHERE receiptid=c.objid) 
  GROUP BY ia.fund_objid 
  UNION ALL 
  SELECT 
    remc.remittanceid, ci.fund_objid, 0.0 AS totalcash, 0.0 AS totalnoncash, SUM(ci.amount) AS totalcr 
  FROM remittance_cashreceipt remc 
    INNER JOIN cashreceipt c ON c.objid=remc.objid 
    INNER JOIN cashreceiptpayment_noncash ci ON ci.receiptid=c.objid 
  WHERE remc.remittanceid=$P{remittanceid} 
    AND c.objid NOT IN (SELECT receiptid FROM cashreceipt_void WHERE receiptid=c.objid) 
    AND ci.reftype = 'CREDITMEMO' 
  GROUP BY ci.fund_objid 
  UNION ALL 
  SELECT 
    remc.remittanceid, nc.fund_objid, 0.0 AS totalcash, SUM(nc.amount) AS totalnoncash, 0.0 AS totalcr  
  FROM remittance_cashreceipt remc 
    INNER JOIN cashreceipt c ON c.objid=remc.objid 
    INNER JOIN cashreceiptpayment_noncash nc ON nc.receiptid=c.objid 
  WHERE remc.remittanceid=$P{remittanceid} 
    AND c.objid NOT IN (SELECT receiptid FROM cashreceipt_void WHERE receiptid=c.objid) 
    AND nc.reftype <> 'CREDITMEMO' 
  GROUP BY nc.fund_objid 
)tmp1, fund 
where fund.objid=tmp1.fund_objid  
GROUP BY remittanceid, fund_objid, fund.title  


[getBuildCancelSeries]
select cs.*, c.series 
from remittance_cashreceipt remc 
  inner join cashreceipt c on remc.objid=c.objid 
  inner join cashreceipt_cancelseries cs on cs.receiptid = c.objid
where remc.remittanceid = $P{remittanceid} 
  and c.state='CANCELLED' 


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


[getRemittedChecks]
SELECT 
   crpc.refno, crpc.particulars, crpc.reftype, 
   CASE WHEN rc.voided = 1 THEN 0 ELSE rc.amount END AS amount, 
   rc.voided
FROM remittance_noncashpayment rc
  INNER JOIN cashreceiptpayment_noncash crpc ON crpc.objid=rc.objid
WHERE rc.remittanceid  = $P{remittanceid}


[getRemittedReceipts]
SELECT 
  c.formno, c.receiptno, c.paidby, c.receiptdate,
  CASE WHEN v.objid IS NULL THEN c.amount ELSE 0 END AS amount,
  CASE WHEN v.objid IS NULL THEN 0 ELSE 1 END AS voided,
  c.subcollector_name
FROM remittance_cashreceipt r 
  INNER JOIN cashreceipt c ON r.objid=c.objid 
  LEFT JOIN cashreceipt_void v ON c.objid=v.receiptid
WHERE r.remittanceid = $P{remittanceid}
ORDER BY c.receiptno


[getRemittedFundsForCashBook]
select 
  rem.dtposted as refdate, rem.objid as refid, rem.txnno as refno, 
  remf.fund_objid, remf.fund_title, remf.amount, 
  rem.collector_objid as subacct_objid, 
  rem.collector_name as subacct_name 
from remittance_fund remf 
  inner join remittance rem on remf.remittanceid=rem.objid 
where remf.remittanceid=$P{remittanceid} 


[findRemittedReceiptSummary]
select 
  af.formtype, c.formno, c.controlid, 
  min(c.series) as minseries, max(c.series) as maxseries, 
  sum(c.amount) as amount, af.denomination 
from cashreceipt c 
  inner join remittance_cashreceipt remc on c.objid=remc.objid 
  inner join af on c.formno=af.objid 
where c.controlid = $P{controlid} 
group by af.formtype, c.formno, c.controlid, af.denomination 
