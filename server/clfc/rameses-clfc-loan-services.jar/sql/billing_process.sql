[getList]
SELECT * FROM billing_process

[getDetailsByParentid]
SELECT * FROM billing_process_detail
WHERE parentid=$P{parentid}

[removeItems]
DELETE FROM billing_process_detail
WHERE parentid = $P{objid}