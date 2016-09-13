[getList]
SELECT * 
FROM landspecificclass 
WHERE ( code LIKE $P{searchtext} OR name LIKE $P{searchtext} )
ORDER BY code, name 


[getSpecificClasses]
SELECT *
FROM landspecificclass
WHERE state LIKE 'APPROVED'
  AND ( code LIKE $P{searchtext} OR name LIKE $P{searchtext}  )
ORDER BY code

[approve]
UPDATE landspecificclass SET state = 'APPROVED' WHERE objid = $P{objid}


