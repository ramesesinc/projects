[getList]
SELECT r.*
FROM subcollector_remittance r
where subcollector_objid like $P{subcollectorid}
order by r.dtposted desc 

[getCollectors]
SELECT 
   DISTINCT   
   cr.collector_name AS name,
   cr.collector_title AS title,
   cr.collector_objid AS objid
FROM cashreceipt cr
WHERE cr.state = 'DELEGATED'
AND cr.subcollector_objid=$P{subcollectorid}

[getUremittedCollectionSummary]
SELECT 
   cr.stub,   
   MIN(cr.receiptno) AS startno,
   MAX(cr.receiptno) AS endno,
   SUM( CASE WHEN cv.objid IS NULL THEN cr.amount ELSE 0 END ) AS amount
FROM cashreceipt cr
LEFT JOIN cashreceipt_void cv ON cr.objid=cv.receiptid
WHERE cr.state = 'DELEGATED'
AND cr.collector_objid = $P{collectorid}
AND cr.subcollector_objid=$P{subcollectorid}
GROUP BY cr.stub

[getItemsRemittance]
SELECT 
   cr.stub,   
   MIN(cr.receiptno) AS startno,
   MAX(cr.receiptno) AS endno,
   SUM( CASE WHEN cv.objid IS NULL THEN cr.amount ELSE 0 END ) AS amount
FROM subcollector_remittance_cashreceipt c 
inner join cashreceipt cr on c.objid = cr.objid 
LEFT JOIN cashreceipt_void cv ON cr.objid=cv.receiptid
WHERE c.remittanceid=$P{objid}
GROUP BY cr.stub

[findSummaryTotals]
select 
  sum(itemcount) as itemcount, 
  sum(case when voided>0 then 0 else amount end) as amount, 
  sum(case when voided>0 then 0 else totalcash end) as totalcash, 
  sum(case 
    when voided>0 then 0 
    when checked>0 then totalnoncash 
    else 0 
    end 
  ) as totalnoncash 
from ( 
  SELECT 
    (select count(*) from cashreceipt_void where receiptid=cr.objid) as voided, 
    (select count(*) from cashreceiptpayment_noncash where receiptid=cr.objid) as checked, 
    1 as itemcount, cr.amount, cr.totalnoncash, (cr.totalcash-cr.cashchange) as totalcash 
  FROM cashreceipt cr
  WHERE cr.state = 'DELEGATED'
    AND cr.collector_objid = $P{collectorid} 
    AND cr.subcollector_objid = $P{subcollectorid} 
)xx 
  
[collectReceipts]
INSERT INTO subcollector_remittance_cashreceipt (objid, remittanceid)
SELECT cr.objid, $P{remittanceid} 
FROM cashreceipt cr  
WHERE cr.state = 'DELEGATED'
AND cr.collector_objid = $P{collectorid}
AND cr.subcollector_objid=$P{subcollectorid}

[updateCashReceiptState]
UPDATE cr  
SET cr.state = 'POSTED'
from  cashreceipt cr
WHERE EXISTS (
  SELECT csr.objid 
  FROM subcollector_remittance_cashreceipt csr
  WHERE csr.remittanceid = $P{remittanceid} AND csr.objid=cr.objid
)

[getCheckPaymentByRemittanceId]
select ch.* from subcollector_remittance_cashreceipt rc 
  inner join cashreceiptpayment_noncash ch on ch.receiptid = rc.objid 
  left join cashreceipt_void cv on ch.receiptid = cv.receiptid 
where rc.remittanceid=$P{remittanceid} 
   and cv.objid is null 

[getCheckPaymentBySubcollector]
select  ch.* 
from cashreceipt c 
  inner join cashreceiptpayment_noncash ch on ch.receiptid = c.objid 
 left join cashreceipt_void cv on ch.receiptid = cv.receiptid 
where c.state='DELEGATED' 
   and c.collector_objid=$P{collectorid}
   and c.subcollector_objid = $P{subcollectorid} 
    and cv.objid is null 


[getCollectionSummaries]    
SELECT 
  x.formno,
  CASE WHEN x.issuedstartseries IS NULL THEN x.receivedstartseries ELSE x.issuedstartseries END AS receivedstartseries,
  x.receivedendseries,
  x.issuedstartseries,
  x.issuedendseries,
  CASE WHEN x.issuedstartseries IS NULL THEN x.endingstartseries ELSE x.issuedendseries + 1 END AS endingstartseries,
  x.endingendseries,
  x.amount
FROM (
  SELECT 
    ai.afid AS formno,
    ai.currentseries AS receivedstartseries,
    ai.endseries AS receivedendseries,
    (SELECT MIN(series) FROM cashreceipt 
     WHERE controlid = ai.objid AND subcollector_objid = ac.assignee_objid AND state = 'DELEGATED') AS issuedstartseries,
    (SELECT MAX(series) FROM cashreceipt 
     WHERE controlid = ai.objid AND subcollector_objid = ac.assignee_objid AND state = 'DELEGATED') AS issuedendseries,
    ai.currentseries AS endingstartseries,
    ai.endseries AS endingendseries,
    (SELECT SUM(c.amount) FROM cashreceipt c LEFT JOIN cashreceipt_void cv ON c.objid = cv.receiptid 
     WHERE c.controlid = ai.objid AND c.subcollector_objid = ac.assignee_objid AND c.state = 'DELEGATED' AND cv.objid IS NULL ) AS amount
  FROM af_inventory ai
    INNER JOIN af_control ac ON ai.objid = ac.objid 
  WHERE ac.assignee_objid = $P{subcollectorid}
) x
WHERE x.amount > 0.0 


