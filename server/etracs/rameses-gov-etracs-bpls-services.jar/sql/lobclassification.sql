[getList]
SELECT *
FROM lobclassification
WHERE name LIKE $P{searchtext}
ORDER BY name

[approve]
UPDATE lobclassification SET state = 'APPROVED'
WHERE objid = $P{objid}