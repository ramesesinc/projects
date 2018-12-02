[getList]
select 
	c.objid, c.state, c.txndate, c.receiptno, c.receiptdate, c.txnmode, c.paidby, c.paidbyaddress, c.amount, 
	c.collector_objid, c.collector_name, c.collectiontype_objid, c.collectiontype_name, 
	c.controlid, c.series, c.formno, c.formtype, 
	(case when v.objid is null then 0 else 1 end) as voided, 
	(case when r.objid is null then 0 else 1 end) as remitted, 
	(case when r.collectionvoucherid is null then 0 else 1 end) as liquidated 
from ( 
	select c.objid 
	from cashreceipt c 
	where ${filter} 
	limit 500 
)t1 
	inner join cashreceipt c on c.objid = t1.objid 
	left join cashreceipt_void v on v.receiptid = c.objid 
	left join remittance r on r.objid = c.remittanceid 
order by ${orderby} 
