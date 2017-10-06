[postTransfer]
insert into af_control_detail (
  objid, controlid, indexno, 
  refid, refno, reftype, refdate, txndate, txntype, 
  receivedstartseries, receivedendseries, beginstartseries, beginendseries, endingstartseries, endingendseries, 
  qtyreceived, qtybegin, qtyissued, qtyending, qtycancelled, remarks, issuedto_objid, issuedto_name 
)
select 
  b.objid, a.objid, a.currentindexno+1, b.objid, b.controlno, 'aftransaction', b.dtfiled, b.txndate, b.txntype, 
  a.currentseries, a.endseries, a.currentseries, a.endseries, a.currentseries, a.endseries, 
  (a.endseries-a.currentseries)+1 as qtyreceived, (a.endseries-a.currentseries)+1 as qtybegin, 
  0 as qtyissued, (a.endseries-a.currentseries)+1 as qtyending, 0 as qtycancelled, 
  concat('TRANSFERRED TO ', b.collector_name) as remarks, b.collector_objid, b.collector_name  
from af_control a, ( 
    select 
      objid, controlno, dtfiled, txndate, txntype, collector_objid, collector_name    
    from aftransaction 
    where objid = $P{objid} 
  )b 
where a.objid = $P{controlid} 
  and b.objid is not null 


[postCancel]
insert into af_control_detail (
  objid, controlid, indexno, 
  refid, refno, reftype, refdate, txndate, txntype, 
  beginstartseries, beginendseries, endingstartseries, endingendseries, 
  qtyreceived, qtybegin, qtyissued, qtyending, qtycancelled, 
  remarks, issuedto_objid, issuedto_name 
)
select 
  b.objid, a.objid, a.currentindexno+1, 
  b.objid, b.controlno, 'aftransaction', b.dtfiled, b.txndate, b.txntype, 
  a.currentseries, a.endseries, a.currentseries, a.endseries, 
  0 as qtyreceived, 0 as qtybegin, 0 as qtyissued, 0 as qtyending, 0 as qtycancelled, 
  'CANCELLED' as remarks, a.owner_objid, a.owner_name 
from af_control a, ( 
    select 
      objid, controlno, dtfiled, txndate, txntype 
    from aftransaction 
    where objid = $P{objid} 
  )b 
where a.objid = $P{controlid} 
  and b.objid is not null 


[postReturn]
insert into af_control_detail (
  objid, controlid, indexno, 
  refid, refno, reftype, refdate, txndate, txntype, 
  receivedstartseries, receivedendseries, beginstartseries, beginendseries, endingstartseries, endingendseries, 
  qtyreceived, qtybegin, qtyissued, qtyending, qtycancelled, remarks, issuedto_objid, issuedto_name 
)
select 
  b.objid, a.objid, a.currentindexno+1, b.objid, b.controlno, 'aftransaction', b.dtfiled, b.txndate, b.txntype, 
  a.currentseries, a.endseries, a.currentseries, a.endseries, a.currentseries, a.endseries, 
  (a.endseries-a.currentseries)+1 as qtyreceived, (a.endseries-a.currentseries)+1 as qtybegin, 
  0 as qtyissued, (a.endseries-a.currentseries)+1 as qtyending, 0 as qtycancelled, 
  'RETURNED' as remarks, null, null 
from af_control a, ( 
    select 
      objid, controlno, dtfiled, txndate, txntype 
    from aftransaction 
    where objid = $P{objid} 
  )b 
where a.objid = $P{controlid} 
  and b.objid is not null 


[postForwardAF]
insert into af_control_detail (
  objid, controlid, indexno, 
  refid, refno, reftype, refdate, txndate, txntype, 
  beginstartseries, beginendseries, endingstartseries, endingendseries, 
  qtyreceived, qtybegin, qtyissued, qtyending, qtycancelled, 
  remarks, issuedto_objid, issuedto_name 
)
select 
  a.objid, a.objid, 1,  
  'afforward', 'afforward', 'afforward', $P{txndate}, $P{txndate}, 'COLLECTOR BEG.BAL.', 
  a.currentseries, a.endseries, a.currentseries, a.endseries, 
  0 as qtyreceived, (a.endseries-a.currentseries)+1 as qtybegin, 
  0 as qtyissued, (a.endseries-a.currentseries)+1 as qtyending, 
  0 as qtycancelled, 'FORWARD BALANCE', a.owner_objid, a.owner_name 
from af_control a 
where a.objid = $P{controlid} 
