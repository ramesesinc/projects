[getUnassignSectorZonesLookup]
select wsz.* 
from waterworks_sector_zone wsz 
where wsz.sectorid=$P{sectorid} 
	and wsz.objid not in (
		select zoneid from waterworks_area_zone 
		where areaid=$P{areaid} and zoneid=wsz.objid 
	)
