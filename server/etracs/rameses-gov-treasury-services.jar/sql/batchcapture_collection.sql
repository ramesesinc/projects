[findBatchControlByState]
select objid from batchcapture_collection 
where controlid=$P{controlid} and state NOT IN ('POSTED','CLOSED')

[updateBatchCaptureState]
update batchcapture_collection set state=$P{state} 
where objid=$P{objid} 

[deleteBatchEntryItem]
delete from batchcapture_collection_entry_item where parentid=$P{objid}

[deleteBatchEntryItemByRootid]
delete from batchcapture_collection_entry_item  
where parentid in (select objid from  batchcapture_collection_entry where parentid=$P{objid}  ) 		

[deleteBatchEntry]
delete from batchcapture_collection_entry where parentid=$P{objid} 


[findBatchEntry]
select objid from batchcapture_collection_entry where objid=$P{objid} 

[post]	
update batchcapture_collection set 
	state=$P{state}, 
	postedby_objid=$P{postedby_objid},
	postedby_name=$P{postedby_name},
	postedby_date=$P{postedby_date}
where objid=$P{objid}

[getList]
select 
	objid, state, formno, collector_name as collectorname, startseries, endseries,
	totalamount, capturedby_name as capturedbyname
from batchcapture_collection 
where startseries like $P{searchtext} 
${filter} 

[getBatchEntry]
select * from batchcapture_collection_entry where parentid=$P{objid} order by series  

[getBatchEntryItems]
select bcei.*, 
	case 
		when cta.account_objid is null then ia.valuetype 
		else cta.valuetype 
	end as valuetype 
from batchcapture_collection_entry bce 
	inner join batchcapture_collection_entry_item bcei on bce.objid=bcei.parentid 
	inner join batchcapture_collection bc on bce.parentid=bc.objid 
	inner join itemaccount ia on bcei.item_objid = ia.objid 
	left join collectiontype_account cta on (bcei.item_objid=cta.account_objid and cta.collectiontypeid=bc.collectiontype_objid)
where bce.objid=$P{objid} 

[updateCashReceiptState]
update cashreceipt set state='POSTED' where objid=$P{objid} 

#
# added scripts 
#
[findBatchSummary]
select 
	sum(case when voided > 0 then 0.0 else totalcash end) as totalcash, 
	sum(case when voided > 0 then 0.0 else totalnoncash end) as totalnoncash, 
	sum(case when voided > 0 then 0.0 else amount end) as totalamount 
from batchcapture_collection_entry 
where parentid=$P{objid} 


[updateBatchSummary]
update batchcapture_collection set 
	totalamount = $P{totalamount}, 
	totalcash = $P{totalcash}, 
	totalnoncash = $P{totalnoncash} 
where 
	objid=$P{objid}


[getBatchEntries]
select * from batchcapture_collection_entry where parentid=$P{objid} order by series 

[deleteBatchEntryItems]
delete from batchcapture_collection_entry_item where parentid=$P{objid}
