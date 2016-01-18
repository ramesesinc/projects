[getList]
SELECT DISTINCT lob.* FROM 
(SELECT lob.*, lc.name AS classification_name 
FROM lob lob 
INNER JOIN lobclassification lc ON lob.classification_objid = lc.objid
WHERE lob.name LIKE $P{searchtext}
UNION
SELECT lob.*, lc.name AS classification_name 
FROM lob lob 
INNER JOIN lobclassification lc ON lob.classification_objid = lc.objid
WHERE lc.name LIKE $P{searchtext}) lob
ORDER BY lob.name 

[getListByAttribute]
SELECT lob.*, lc.name AS classification_name 
FROM lob lob 
INNER JOIN lobclassification lc ON lob.classification_objid = lc.objid
INNER JOIN lob_lobattribute lla ON lla.lobid = lob.objid
INNER JOIN lobattribute la ON lla.lobattributeid=la.objid
WHERE lob.name LIKE $P{searchtext}
AND la.objid = $P{attributeid}
ORDER BY lob.name 

[removeAttributes]
DELETE FROM lob_lobattribute
WHERE lobid = $P{lobid}

[findInfo]
SELECT name, classification_objid  FROM lob WHERE objid=$P{objid}

[getAttributes]
SELECT lla.*, la.name 
FROM lob_lobattribute lla
INNER JOIN lobattribute la
ON lla.lobattributeid = la.objid
WHERE lla.lobid = $P{lobid}

[getLookup]
SELECT lob.*, lc.name AS classification_name 
FROM lob lob 
INNER JOIN lobclassification lc ON lob.classification_objid = lc.objid
WHERE lob.name LIKE $P{searchtext}
ORDER BY lob.name

[findInfo]
SELECT 
	l.objid, l.name, lc.name as classification, lc.objid as classificationid
FROM lob l
INNER JOIN lobclassification lc ON l.classification_objid=lc.objid
WHERE l.objid = $P{lobid}

[findAttr]
SELECT la.objid, la.name  
FROM lob_lobattribute llb 
INNER JOIN lobattribute la ON la.objid=llb.lobattributeid
WHERE llb.lobid=$P{lobid}

[approve]
UPDATE lob SET state='APPROVED' WHERE objid=$P{objid}