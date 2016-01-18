[getList]
SELECT * FROM branchfund 

[getDetails]
SELECT * FROM branchfund_detail 
WHERE fundid=$P{fundid} 
ORDER BY dtcreated DESC 

[findByCollector]
SELECT * FROM branchfund_collector 
WHERE fundid=$P{fundid} AND collector_objid=$P{collectorid} 

[getCollectors]
SELECT c.*, 
	u.lastname AS collector_lastname, 
	u.firstname AS collector_firstname,
	u.middlename AS collector_middlename
FROM branchfund_collector c 
	INNER JOIN sys_user u ON c.collector_objid=u.objid 
WHERE 
	c.fundid=$P{fundid} 
