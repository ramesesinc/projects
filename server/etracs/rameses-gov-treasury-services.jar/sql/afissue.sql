[post]
insert into af_control_detail (
	objid, controlid, indexno, 
	refid, refno, reftype, refdate, txndate, txntype, 
	receivedstartseries, receivedendseries, beginstartseries, beginendseries, endingstartseries, endingendseries, 
	qtyreceived, qtybegin, qtyissued, qtyending, qtycancelled, remarks 
)
select 
	$P{detailid}, a.objid, a.currentindexno+1, b.objid, b.controlno, 'afissue', b.dtfiled, b.txndate, $P{txntype}, 
	a.currentseries, a.endseries, a.currentseries, a.endseries, a.currentseries, a.endseries, 
	(a.endseries-a.currentseries)+1 as qtyreceived, (a.endseries-a.currentseries)+1 as qtybegin, 
	0 as qtyissued, (a.endseries-a.currentseries)+1 as qtyending, 0 as qtycancelled, $P{remarks} 
from af_control a, ( 
		select objid, controlno, dtfiled, txndate  
		from afissue where objid = $P{objid} 
	)b 
where a.objid = $P{controlid} 
	and b.objid is not null 
