[getList]
SELECT e.*, e.name AS fullname 
FROM entitymultiple emu 
	INNER JOIN entity e ON emu.objid=e.objid 
WHERE e.entityname LIKE $P{searchtext} 
ORDER BY e.entityname 

[getMembers]
SELECT * FROM entitymember WHERE entityid=$P{objid} ORDER BY itemno 

[removeMembers]
DELETE FROM entitymember WHERE entityid=$P{objid} 
