[getList]
SELECT r.* 
FROM stockreceipt r WHERE state =$P{state}

[getItems]
SELECT s.*, af.formtype as aftype FROM stockreceiptitem s 
	inner join af on af.objid = s.item_objid 
WHERE parentid=$P{objid}


[closeRequest]
UPDATE stockrequest SET state='CLOSED' WHERE objid=$P{objid}


