[getList]
SELECT * 
FROM propertyclassification 
WHERE ( code LIKE $P{searchtext} OR name LIKE $P{searchtext} )
ORDER BY orderno

[getClassifications]
SELECT *
FROM propertyclassification
WHERE state = 'APPROVED'
  AND ( code LIKE $P{searchtext} OR name LIKE $P{searchtext} )
ORDER BY orderno


[findById]
SELECT * FROM propertyclassification WHERE objid = $P{objid}

[approve]
UPDATE propertyclassification SET state = 'APPROVED' WHERE objid = $P{objid}

