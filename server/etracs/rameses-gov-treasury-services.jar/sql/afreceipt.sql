[post]
insert into af_control_detail (
	objid, controlid, indexno, 
	refid, refno, reftype, refdate, txndate, txntype, 
	receivedstartseries, receivedendseries, beginstartseries, beginendseries, endingstartseries, endingendseries, 
	qtyreceived, qtybegin, qtyissued, qtyending, qtycancelled, remarks 
)
select 
	afc.objid, afc.objid, 1, afr.objid, afr.receiptno, 'afreceipt', afr.dtfiled, afr.dtfiled, 'RECEIPT', 
	afc.startseries, afc.endseries, afc.startseries, afc.endseries, afc.startseries, afc.endseries, 
	(afc.endseries-afc.startseries)+1 as qtyreceived, (afc.endseries-afc.startseries)+1 as qtybegin,
	0 as qtyissued, (afc.endseries-afc.startseries)+1 as qtyending, 0 as qtycancelled, 
	(case when afri.txntype = 'PURCHASE' then 'RECEIPT OF PURCHASE' else afri.txntype end) as remarks 
from afreceipt afr 
	inner join af_control afc on afc.receiptid = afr.objid 
	inner join afreceiptitem afri on (afri.parentid = afr.objid and afri.item_objid = afc.afid) 
where afr.objid = $P{objid} 

