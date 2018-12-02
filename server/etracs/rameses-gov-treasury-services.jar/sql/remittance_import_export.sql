[findRemittanceByid]
SELECT * FROM remittance WHERE objid = $P{remittanceid}

[getCashReceipts]
SELECT cr.* 
FROM remittance r 
	INNER JOIN cashreceipt cr ON cr.remittanceid = r.objid 
WHERE cr.remittanceid = $P{remittanceid}	

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
  INNER JOIN remittance r on r.objid = c.remittanceid 
  LEFT JOIN cashreceipt_void v ON c.objid=v.receiptid
where c.remittanceid = $P{remittanceid}	
GROUP BY c.collector_objid, c.formno, c.formtype, c.controlid

[getCancelledSeries]
SELECT cs.*, c.series 
FROM cashreceipt c 
   INNER JOIN remittance r on r.objid = c.remittanceid 
   INNER JOIN cashreceipt_cancelseries cs on cs.receiptid = c.objid
where r.objid = $P{remittanceid} 
  and c.controlid = $P{controlid}
	and c.state = 'CANCELLED' 
