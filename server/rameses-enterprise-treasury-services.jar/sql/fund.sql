[getList]
SELECT * FROM fund WHERE parentid IS NULL ORDER BY code 

[findAllSubAcct]
SELECT * FROM fund WHERE parentid = $P{objid}

[getLookup]
SELECT DISTINCT f.* FROM 
(SELECT * FROM fund WHERE code LIKE $P{searchtext}
UNION 
SELECT * FROM fund WHERE title LIKE $P{searchtext}
) f
ORDER BY f.code

[approve]
UPDATE fund SET state='APPROVED' WHERE objid=$P{objid} 

[getAll]
SELECT * FROM fund ORDER BY parentid, title 
