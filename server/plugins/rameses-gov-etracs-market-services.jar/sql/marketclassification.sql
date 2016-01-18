[getList]
SELECT * FROM marketclassification

[approve]
UPDATE marketclassification SET state='APPROVED' WHERE objid=$P{objid}
