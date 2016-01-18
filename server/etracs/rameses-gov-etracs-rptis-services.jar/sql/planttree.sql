[getList]
SELECT * 
FROM planttree 
WHERE code LIKE $P{searchtext} OR name LIKE $P{searchtext}
ORDER BY code


[getPlantTrees]
SELECT *
FROM planttree
WHERE state = 'APPROVED'
  AND ( code LIKE $P{searchtext} OR name LIKE $P{searchtext}  )
ORDER BY code


[approve]
UPDATE planttree SET state = 'APPROVED' WHERE objid = $P{objid}




