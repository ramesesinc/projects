[getList]
SELECT * 
FROM rptparameter 
WHERE name LIKE $P{searchtext}
ORDER BY name


[getRPTParameters]
SELECT *, paramtype as type
FROM rptparameter
WHERE state = 'APPROVED'
  AND name LIKE $P{searchtext} 
ORDER BY name


[approve]
UPDATE rptparameter SET state = 'APPROVED' WHERE objid = $P{objid}



