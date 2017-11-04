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
select  
  collector_objid, controlid, formno, formtype,  
  case when formtype='serial' then min(series) else null end as startseries, 
  case when formtype='serial' then max(series) else null end as endseries, 
  sum( qty ) as qty, sum( cqty ) as cqty, 
  sum( case when voided=0 then amount else 0.0 end ) AS amount,
  sum( case when voided=0 then totalcash-cashchange else 0.0 end ) AS totalcash,
  sum( case when voided=0 then totalnoncash else 0.0 end ) AS totalnoncash
from ( 
  select 
    c.objid, c.controlid, c.collector_objid, af.formtype, af.objid as formno, 0 as formindex, 
    c.series, c.amount, c.totalcash, c.totalnoncash, c.cashchange, 
    (case when c.state = 'CANCELLED' then 0 else 1 end) as qty, 
    (case when c.state = 'CANCELLED' then 1 else 0 end) as cqty, 
    (select count(*) from cashreceipt_void where receiptid=c.objid) as voided 
  from cashreceipt c 
    inner join af_control afc on afc.objid = c.controlid 
    inner join af on (af.objid = afc.afid and af.formtype='serial') 
  where c.collector_objid = $P{collectorid} 
    and c.receiptdate < $P{txndate} 
    and c.state in ($P{txnstate}, 'CANCELLED') 
    and c.objid not in (select objid from remittance_cashreceipt where objid=c.objid) 

  union all 

  select 
    c.objid, c.controlid, c.collector_objid, af.formtype, af.objid as formno, 1 as formindex,  
    c.series, c.amount, c.totalcash, c.totalnoncash, c.cashchange, 
    convert((c.amount/af.denomination), signed) as qty, 0 as cqty, 0 as voided 
  from cashreceipt c 
    inner join af_control afc on afc.objid = c.controlid 
    inner join af on (af.objid = afc.afid and af.formtype <> 'serial') 
  where c.collector_objid = $P{collectorid} 
    and c.receiptdate < $P{txndate} 
    and c.state = $P{txnstate}  
    and c.objid not in (select objid from remittance_cashreceipt where objid=c.objid) 
    and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 

)tmp1 
group by collector_objid, controlid, formno, formtype 


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


[collectReceipts]
INSERT INTO remittance_cashreceipt ( objid, remittanceid )
SELECT c.objid, $P{remittanceid} AS remittanceid 
FROM (
  SELECT c.objid FROM cashreceipt c 
    LEFT JOIN remittance_cashreceipt r ON c.objid = r.objid 
  WHERE c.receiptdate < $P{txndate} 
    AND r.objid IS NULL 
    AND c.collector_objid = $P{collectorid} 
    AND c.state IN ('POSTED', 'CANCELLED') 
)bt 
  INNER JOIN cashreceipt c ON bt.objid=c.objid 


[collectNoncash]
INSERT INTO remittance_noncashpayment ( 
  objid, remittanceid, amount, voided 
) 
SELECT 
  crp.objid, $P{remittanceid} as remittanceid, crp.amount,    
  CASE WHEN cv.objid IS NULL THEN 0 ELSE 1 END AS voided 
FROM remittance_cashreceipt rc 
  INNER JOIN cashreceipt cash ON rc.objid=cash.objid 
  INNER JOIN cashreceiptpayment_noncash crp ON crp.receiptid=cash.objid
  LEFT JOIN cashreceipt_void cv ON crp.receiptid = cv.receiptid
WHERE rc.remittanceid = $P{remittanceid} 
  AND cash.state = 'POSTED' 
  AND cash.receiptdate < $P{txndate} 
  AND cv.objid IS NULL 

[getRemittedFundTotals]
SELECT ri.fund_objid, ri.fund_title, SUM(chi.amount) AS amount
FROM remittance_cashreceipt c
  INNER JOIN cashreceipt ch on c.objid = ch.objid 
  INNER JOIN cashreceiptitem chi on chi.receiptid = ch.objid
  INNER JOIN itemaccount ri on ri.objid = chi.item_objid
  LEFT JOIN cashreceipt_void cv ON c.objid = cv.receiptid 
WHERE c.remittanceid = $P{remittanceid}
  AND cv.objid IS NULL 
  AND ch.receiptdate < $P{txndate}
GROUP BY c.remittanceid, ri.fund_objid, ri.fund_title

[getRemittedChecks]
SELECT 
   crpc.refno, crpc.particulars, crpc.reftype, 
   CASE WHEN rc.voided = 1 THEN 0 ELSE rc.amount END AS amount, 
   rc.voided
FROM remittance_noncashpayment rc
  INNER JOIN cashreceiptpayment_noncash crpc ON crpc.objid=rc.objid
WHERE rc.remittanceid  = $P{objid}

[getRemittedReceipts]
SELECT 
  c.formno, c.receiptno, c.paidby, c.receiptdate,
  CASE WHEN v.objid IS NULL THEN c.amount ELSE 0 END AS amount,
  CASE WHEN v.objid IS NULL THEN 0 ELSE 1 END AS voided,
  c.subcollector_name
FROM remittance_cashreceipt r 
  INNER JOIN cashreceipt c ON r.objid=c.objid 
  LEFT JOIN cashreceipt_void v ON c.objid=v.receiptid
WHERE r.remittanceid = $P{objid}
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
