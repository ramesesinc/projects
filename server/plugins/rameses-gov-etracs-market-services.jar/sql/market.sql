[getList]
SELECT b.* FROM 
(SELECT m.*, 
	t.title AS classification_title, 
	t.name AS classification_name
FROM market m
INNER JOIN marketclassification t ON m.classification_objid=t.objid) b

[approve]
UPDATE market SET state='APPROVED' WHERE objid=$P{objid}

[findMarketName]
SELECT name FROM market WHERE objid=$P{objid}