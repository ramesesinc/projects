[getList]
SELECT objid, entityno, 
entityname AS name,
entityaddress AS address,
'INDIVIDUAL' AS type 
FROM entity 
WHERE entitytype = 'individual'
AND entityname LIKE $P{searchtext}




