[getList]
SELECT * 
FROM bldgkind 
WHERE ( code LIKE $P{searchtext} OR name LIKE $P{searchtext} )
ORDER BY code


[getBldgKinds]
SELECT *
FROM bldgkind
WHERE state LIKE 'APPROVED'
  AND ( code LIKE $P{searchtext} OR name LIKE $P{searchtext}  )
ORDER BY code

[approve]
UPDATE bldgkind SET state = 'APPROVED' WHERE objid = $P{objid}


