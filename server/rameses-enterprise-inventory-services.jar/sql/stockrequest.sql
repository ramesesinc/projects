[getList]
SELECT r.* 
FROM stockrequest r WHERE state =$P{state}

[getItems]
SELECT sri.*, si.itemclass AS item_itemclass, si.type as item_type  
FROM stockrequestitem sri
INNER JOIN stockitem si ON sri.item_objid = si.objid
WHERE sri.parentid=$P{objid}


[deleteRequest]
update stockrequest set state = 'DELETED' where objid =$P{objid} 