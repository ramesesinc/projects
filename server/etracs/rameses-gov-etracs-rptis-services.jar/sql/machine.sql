[getList]
SELECT * 
FROM machine 
WHERE code = $P{searchtext} OR name LIKE $P{searchtext}
ORDER BY code


[getMachines]
SELECT *
FROM machine
WHERE state = 'APPROVED'
  AND (code = $P{searchtext} OR name LIKE $P{searchtext})
ORDER BY code

[approve]
UPDATE machine SET state = 'APPROVED' WHERE objid = $P{objid}



