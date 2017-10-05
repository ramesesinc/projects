[post]
insert into af_control_detail (
	objid, controlid, indexno, 
	refid, refno, reftype, refdate, txndate, txntype, 
	receivedstartseries, receivedendseries, beginstartseries, beginendseries, endingstartseries, endingendseries, 
	qtyreceived, qtybegin, qtyissued, qtyending, qtycancelled, remarks 
)
select 
	afc.objid, afc.objid, 1, afr.objid, afr.controlno, 'afreceipt', afr.dtfiled, afr.txndate, 'RECEIPT', 
	afc.startseries, afc.endseries, afc.startseries, afc.endseries, afc.startseries, afc.endseries, 
	(afc.endseries-afc.startseries)+1 as qtyreceived, (afc.endseries-afc.startseries)+1 as qtybegin,
	0 as qtyissued, (afc.endseries-afc.startseries)+1 as qtyending, 0 as qtycancelled, 
	(case when afr.txntype = 'PURCHASE' then 'RECEIPT OF PURCHASE' else afr.txntype end) as remarks 
from af_control afc, ( 
	select objid, controlno, dtfiled, txndate, $P{txntype} as txntype 
	from afreceipt 
	where objid = $P{receiptid}  
)afr 
where afc.objid = $P{controlid}


[postDraftToOpen]
update af_control a, af_control_detail b set 
	a.state = 'OPEN' 
where a.objid = b.controlid 
	and b.refid = $P{refid} 
	and a.state = 'DRAFT' 


[removeAFControlDetailByBatch]
delete from af_control_detail where controlid in ( 
	select objid from af_control 
	where batchno = $P{batchno} 
		and objid = af_control_detail.controlid 
)
