[getList]
SELECT r.* 
FROM remittance r 
	LEFT JOIN liquidation_remittance lr ON r.objid=lr.objid 
WHERE lr.objid IS NULL 
	AND r.liquidatingofficer_objid = $P{userid} 
	AND r.state = $P{state} 
	${filter} 


[getListByCollector]
SELECT r.* 
FROM remittance r 
	LEFT JOIN liquidation_remittance lr ON r.objid=lr.objid 
WHERE lr.objid IS NULL 
	AND r.collector_objid = $P{collectorid} 
	${filter} 
