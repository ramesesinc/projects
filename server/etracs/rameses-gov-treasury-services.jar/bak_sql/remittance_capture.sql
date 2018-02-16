[getList]
SELECT r.*, 
	(SELECT COUNT(*) FROM liquidation_remittance WHERE objid=r.objid) as liquidated 
FROM remittance r
WHERE r.txnno LIKE $P{txnno} 
	AND r.state = 'CAPTURE' 
ORDER BY r.collector_name, r.txnno DESC 


[getCapturedCollections]
select 
	xx.formno, xx.controlid, xx.collector_objid, 
	min(xx.series) as startseries, max(xx.series) as endseries, 
	min(xx.sseries) as sstartseries, max(xx.sseries) as sendseries, 
	sum(xx.amount) as amount, sum(xx.totalcash) as totalcash, 
	sum(xx.totalnoncash) as totalnoncash 
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
	)xx inner join cashreceipt c on xx.receiptid=c.objid 
)xx 
where xx.allowed=1 
group by xx.formno, xx.controlid, xx.collector_objid 
order by xx.formno, min(xx.series) 


[collectReceipts]
insert into remittance_cashreceipt ( 
	objid, remittanceid 
) 
select 	
	cr.objid, $P{remittanceid} 
from draft_remittance drem 
	inner join draft_remittance_cashreceipt dremc on drem.objid=dremc.parentid 
	inner join cashreceipt cr on dremc.objid = cr.objid 
where drem.objid = $P{draftid}  


[collectChecks]
INSERT INTO remittance_noncashpayment ( 
	objid, remittanceid, amount, voided 
) 
select 	
	crp.objid, $P{remittanceid}, crp.amount, 
	(select count(*) from cashreceipt_void where receiptid=cr.objid) as voided 
from draft_remittance drem 
	inner join draft_remittance_cashreceipt dremc on drem.objid=dremc.parentid 
	inner join cashreceiptpayment_noncash crp ON dremc.objid=crp.receiptid 	
	inner join cashreceipt cr on dremc.objid=cr.objid 
where drem.objid = $P{draftid}  


[getUnremittedReceipts]
select 
	xx.formno, xx.controlid, xx.receiptno, xx.paidby, xx.receiptdate, xx.subcollector_name, 
	(case when xx.voided=0 then xx.amount else 0.0 end) as amount, 
	(case when xx.voided=0 then 0 else 1 end) as voided 
from ( 
	select 	
		cr.formno, cr.receiptno, cr.paidby, cr.receiptdate, cr.series, 
		cr.user_name as subcollector_name, cr.amount, cr.controlid, 
		(select count(*) from cashreceipt_void where receiptid=dremc.objid) as voided 
	from draft_remittance drem 
		inner join draft_remittance_cashreceipt dremc on drem.objid=dremc.parentid 
		inner join cashreceipt cr on dremc.objid = cr.objid 
	where drem.objid = $P{draftid}  
)xx 
order by xx.formno, xx.controlid, xx.series 


[getUnremittedChecks]
select 
	xx.objid, xx.checkno, xx.particulars, xx.subcollector_name, 
	(case when xx.voided=0 then xx.amount else 0.0 end) as amount, 
	(case when xx.voided=0 then 0 else 1 end) as voided 
from ( 
	select 	
		crp.objid, crp.refno as checkno, crp.particulars, 
		cr.user_name as subcollector_name, crp.amount, 
		(select count(*) from cashreceipt_void where receiptid=dremc.objid) as voided 
	from draft_remittance drem 
		inner join draft_remittance_cashreceipt dremc on drem.objid=dremc.parentid 
		inner join cashreceiptpayment_noncash crp on dremc.objid = crp.receiptid 
		inner join cashreceipt cr on dremc.objid = cr.objid 
	where drem.objid = $P{draftid} 
)xx 


[findUnremittedSummary]
select 
	xx.objid, xx.remittancedate, xx.collector_objid, 
	sum(xx.totalcash) as totalcash, 
	sum(xx.totalnoncash) as totalnoncash, 
	sum(xx.totalamount) as totalamount
from ( 
	select 
		xx.objid, xx.remittancedate, xx.collector_objid, 
		(case when xx.voided=0 then xx.totalcash else 0.0 end) as totalcash, 
		(case when xx.voided=0 then xx.totalnoncash else 0.0 end) as totalnoncash, 
		(case when xx.voided=0 then xx.totalcash+xx.totalnoncash else 0.0 end) as totalamount  
	from ( 
		select 
			drem.objid, drem.remittancedate, drem.collector_objid, 
			(cr.totalcash-cr.cashchange) as totalcash, cr.totalnoncash,  
			(select count(*) from cashreceipt_void where receiptid=dremc.objid) as voided 
		from draft_remittance drem 
			inner join draft_remittance_cashreceipt dremc on drem.objid=dremc.parentid 
			inner join cashreceipt cr on dremc.objid = cr.objid 
		where drem.objid = $P{draftid}  
	)xx 
)xx 
group by xx.objid, xx.remittancedate, xx.collector_objid 


[getUnremittedBatchCollections] 
select distinct dremc.batchid 
from draft_remittance drem 
	inner join draft_remittance_cashreceipt dremc on drem.objid=dremc.parentid 
	inner join cashreceipt cr on dremc.objid = cr.objid 
where drem.objid = $P{draftid}  


[getUnremittedFundTotals]
select 
	ia.fund_objid, ia.fund_title, 
	sum( cri.amount ) AS amount		
from draft_remittance drem 
	inner join draft_remittance_cashreceipt dremc on drem.objid=dremc.parentid 
	inner join cashreceipt cr on dremc.objid = cr.objid 
	inner join cashreceiptitem cri on cr.objid = cri.receiptid 
	inner join itemaccount ia on cri.item_objid = ia.objid 
where drem.objid = $P{draftid}  
	and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
group by ia.fund_objid, ia.fund_title 


[getCollectors]
select 
	u.objid, u.name, u.jobtitle as title  
from ( 
	select distinct collector_objid   
	from batchcapture_collection 
	where state = 'POSTED' 
)xx inner join sys_user u on xx.collector_objid=u.objid 


[updateRemittanceAF]
INSERT INTO remittance_af (  
	objid, remittanceid  
) 
SELECT ad.objid, $P{remittanceid} 
FROM af_inventory_detail ad 
	INNER JOIN af_inventory ai ON ai.objid=ad.controlid 
	LEFT JOIN remittance_af af ON af.objid=ad.objid 
WHERE ai.objid = $P{controlid} 
	AND af.objid IS NULL 


[closeBatchCollections]
update batchcapture_collection set state='CLOSED' where objid in ( 
	select distinct dremc.batchid from draft_remittance drem 
		inner join draft_remittance_cashreceipt dremc on drem.objid=dremc.parentid 
	where drem.objid = $P{draftid} 
) and state='POSTED' 
