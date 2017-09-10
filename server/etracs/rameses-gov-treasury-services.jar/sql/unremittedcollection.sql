[getList]
select * from ( 
	select 
		c.objid, c.state, c.txndate, c.receiptno, c.receiptdate, c.txnmode, c.payer_objid, c.payer_name,
		c.paidby, c.paidbyaddress, c.collector_objid, c.collector_name, c.collector_title,
		c.totalcash, c.totalnoncash, c.cashchange, c.totalcredit, c.org_objid, c.org_name, c.formno,
		c.series, c.controlid, c.collectiontype_objid, c.collectiontype_name, c.user_objid, c.user_name,
		c.remarks, c.subcollector_objid, c.subcollector_name, c.subcollector_title, c.formtype, c.stub,
		(case when xx.voided=0 then 0 else 1 end) as voided, 	
		(case when xx.voided=0 then c.amount else 0.0 end) as amount 
	from ( 
		select * from ( 
			select a.objid, a.receiptno, a.paidby, a.payer_name, 
				(select count(*) from cashreceipt_void where receiptid=a.objid) as voided 
			from cashreceipt a 
			where collector_objid = $P{collectorid} and state='POSTED' 
				and objid not in (select objid from remittance_cashreceipt where objid=a.objid) 

			union 

			select a.objid, a.receiptno, a.paidby, a.payer_name, 
				(select count(*) from cashreceipt_void where receiptid=a.objid) as voided 
			from cashreceipt a 
			where subcollector_objid = $P{collectorid} and state='DELEGATED' 
				and objid not in (select objid from remittance_cashreceipt where objid=a.objid) 
		)xx 
		where (xx.receiptno LIKE $P{searchtext} OR xx.payer_name LIKE $P{searchtext} OR xx.paidby LIKE $P{searchtext})  
	)xx 
	inner join cashreceipt c on xx.objid=c.objid 
)a 
order by a.formno, a.receiptno 
