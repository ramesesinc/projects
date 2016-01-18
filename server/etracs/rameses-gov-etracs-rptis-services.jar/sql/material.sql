[getList]
SELECT * 
FROM material 
WHERE (code LIKE $P{searchtext} OR name LIKE $P{searchtext})
ORDER BY code


[getMaterials]
SELECT *
FROM material
WHERE state = 'APPROVED'
  AND (code LIKE $P{searchtext} OR name LIKE $P{searchtext})
ORDER BY code


[approve]
UPDATE material SET state = 'APPROVED' WHERE objid = $P{objid}



