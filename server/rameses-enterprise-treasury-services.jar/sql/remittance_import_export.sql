[findRemittance]
SELECT * FROM remittance WHERE objid = $P{remittanceid}

[getCashReceipts]
SELECT cr.* 
FROM remittance_cashreceipt remcr  
	INNER JOIN cashreceipt cr ON cr.objid = remcr.objid 
WHERE remcr.remittanceid = $P{remittanceid}	

[getCashReceiptItems]
select 
  i.*, ri.fund_code as item_fund_code, 
  ri.fund_objid as item_fund_objid, ri.fund_title as item_fund_title  
from cashreceiptitem i 
  inner join itemaccount ri on ri.objid =  i.item_objid
where i.receiptid=$P{objid}


[getCashReceiptCheckPayments]
select * from cashreceiptpayment_noncash where receiptid=$P{objid}


[findVoidedReceipt]
SELECT * from cashreceipt_void where receiptid = $P{objid}

[getRemittanceItems]
SELECT c.formno, c.collector_objid, c.controlid, min(c.formtype) as formtype,  
  case when c.formtype = 'serial' then  MIN(series) else null  end as startseries, 
  case when c.formtype = 'serial' then MAX(series) else null  end as endseries, 
  min( series ) as minseries, 
  SUM( CASE WHEN c.state = 'POSTED' then 1 else 0 end ) as qty ,  
  SUM( CASE WHEN c.state = 'CANCELLED' then 1 else 0 end ) as cqty , 
  SUM(CASE WHEN v.objid IS NULL THEN c.amount ELSE 0 END) AS amount,
  SUM(CASE WHEN v.objid IS NULL THEN c.totalcash-c.cashchange ELSE 0 END) AS totalcash,
  SUM(CASE WHEN v.objid IS NULL THEN c.totalnoncash ELSE 0 END) AS totalnoncash
FROM cashreceipt c 
  INNER JOIN remittance_cashreceipt r ON c.objid = r.objid 
  LEFT JOIN cashreceipt_void v ON c.objid=v.receiptid
where r.remittanceid=$P{remittanceid}	
GROUP BY c.collector_objid, c.formno, c.formtype, c.controlid

[getCancelledSeries]
SELECT cs.*, c.series 
FROM cashreceipt c 
   INNER JOIN remittance_cashreceipt r ON c.objid = r.objid 
   INNER JOIN cashreceipt_cancelseries cs on cs.receiptid = c.objid
where c.controlid =  $P{controlid}
	and  c.state = 'CANCELLED' 
	and r.objid =$P{remittanceid}

