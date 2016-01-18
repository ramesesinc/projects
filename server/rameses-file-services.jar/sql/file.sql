[findDetail]
SELECT * FROM sys_file_detail WHERE objid=$P{objid} 

[getCount]
SELECT COUNT(objid) as totalcount FROM sys_file_detail WHERE parentid=$P{objid} 

[updateHeaderStatus]
UPDATE sys_file_header SET state=$P{state} WHERE objid=$P{objid} 

[removeItems]
DELETE FROM sys_file_detail WHERE parentid=$P{parentid} 

[getItems]
SELECT * FROM sys_file_detail 
WHERE parentid=$P{parentid}  
	AND indexno BETWEEN $P{startindexno} AND $P{endindexno} 
ORDER BY indexno  
