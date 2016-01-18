[getList]
SELECT * 
FROM miscitem 
WHERE code LIKE $P{searchtext} OR name LIKE $P{searchtext}
ORDER BY code


[getMiscItems]
SELECT *
FROM miscitem
WHERE state = 'APPROVED'
  AND ( code LIKE $P{searchtext} OR name LIKE $P{searchtext}  )
ORDER BY code


[approve]
UPDATE miscitem SET state = 'APPROVED' WHERE objid = $P{objid}

