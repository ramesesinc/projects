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


[findPendingByCollector]
select * 
from ( 
	select 
		year(r.remittancedate) as iyear, 
		month(r.remittancedate) as imonth, 
		count(*) as icount 
	from remittance r 
		left join liquidation_remittance lr on r.objid=lr.objid 
	where lr.objid is null 
		and r.collector_objid = $P{collectorid} 
	group by year(r.remittancedate), month(r.remittancedate) 
)tmp1 
where iyear=$P{year} and imonth=$P{month} 
