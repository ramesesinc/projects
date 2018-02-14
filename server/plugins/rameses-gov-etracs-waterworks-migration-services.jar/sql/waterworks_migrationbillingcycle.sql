[getBuildBillingCycle]
select 
	tmp2.parentid, tmp2.sectorid, 
	tmp2.dtreading, wbc.objid as billingcycleid
from ( 
	select 
		tmp1.parentid, tmp1.sectorid, tmp1.dtreading, 
		max(wbc.readingdate) as readingdate 
	from ( 
		select parentid, sectorid, dtreading 
		from migrationitem 
		where parentid = $P{parentid} 
			and sectorid is not null 
		group by parentid, sectorid, dtreading 
	)tmp1, waterworks_billing_cycle wbc 
	where wbc.sectorid = tmp1.sectorid 
		and wbc.readingdate <= tmp1.dtreading 
	group by tmp1.parentid, tmp1.sectorid, tmp1.dtreading 
)tmp2, waterworks_billing_cycle wbc 
where wbc.sectorid = tmp2.sectorid 
	and wbc.readingdate = tmp2.readingdate 

