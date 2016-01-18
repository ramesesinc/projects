[getList]
SELECT i.* 
FROM customer c
INNER JOIN customerindividual i ON c.objid = i.objid
WHERE c.name LIKE $P{searchtext}