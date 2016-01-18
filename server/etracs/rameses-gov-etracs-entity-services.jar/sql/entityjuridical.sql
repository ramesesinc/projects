[getList]
SELECT e.*, ej.* 
FROM entity e 
	INNER JOIN entityjuridical ej ON e.objid=ej.objid 
WHERE e.entityname LIKE $P{searchtext} 
ORDER BY e.entityname 


[getLookup]
SELECT e.objid, e.entityno, e.name, e.address_text, e.type, ej.orgtype 
FROM entity e 
INNER JOIN entityjuridical ej ON e.objid=ej.objid 
WHERE e.entityname LIKE $P{searchtext} 
${filter}
ORDER BY e.entityname 

[getPositionList]
SELECT DISTINCT administrator_position AS position FROM entityjuridical 
WHERE administrator_position LIKE $P{searchtext}

[getMatchList]
SELECT e.objid, e.entityno, e.name, e.address_text, e.address_objid 
FROM entityjuridical ej
INNER JOIN entity e ON ej.objid=e.objid 
WHERE e.name LIKE $P{name}
