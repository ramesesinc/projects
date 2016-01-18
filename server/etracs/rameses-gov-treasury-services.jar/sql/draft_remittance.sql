[findHeader]
select * from draft_remittance where collector_objid=$P{collectorid} and remittancedate=$P{remittancedate}  

[removeHeader]
delete from draft_remittance where collector_objid=$P{collectorid} 

[removeItems]
delete from draft_remittance_cashreceipt where parentid in ( 
	select objid from draft_remittance where collector_objid = $P{collectorid} 
) 

[removeHeaderByPrimary]
delete from draft_remittance where objid=$P{objid} 

[removeItemsByParentid]
delete from draft_remittance_cashreceipt where parentid=$P{parentid}  

[insertItems]
insert into draft_remittance_cashreceipt (
	objid, parentid, controlid, batchid 
) 
select 
	xx.receiptid as objid, $P{parentid} as parentid, 
	xx.controlid, xx.objid as batchid 
from ( 
	select xx.*, c.controlid, c.collector_objid, c.series, 
		(case when xx.formtype='cashticket' then null else c.receiptno end) as sseries, 
		(case when xx.formtype='cashticket' and xx.voided>0 then 0 else 1 end) as allowed, 
		(case when xx.voided=0 then c.totalcash-c.cashchange else 0.0 end) as totalcash, 
		(case when xx.voided=0 then c.totalnoncash else 0.0 end) as totalnoncash, 
		(case when xx.voided=0 then c.amount else 0.0 end) as amount 
	from ( 
		select bc.objid, bce.objid as receiptid, cr.formtype, cr.formno, 
			(select count(*) from cashreceipt_void where receiptid=bce.objid) as voided 
		from batchcapture_collection bc 
			inner join batchcapture_collection_entry bce on bc.objid = bce.parentid 
			inner join cashreceipt cr on bce.objid = cr.objid 
		where bc.collector_objid = $P{collectorid} and bc.state = 'POSTED' 
			and cr.receiptdate <= $P{remittancedate} and cr.state = 'CAPTURED'  
			and cr.objid not in (select objid from remittance_cashreceipt where objid=cr.objid) 
			and cr.controlid in ( ${controlids} ) 
	)xx inner join cashreceipt c on xx.receiptid=c.objid 
)xx 
where xx.allowed=1 
