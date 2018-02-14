[resolveMeters]
update 
	migrationmeter mm, waterworks_meter wm 
set 
	mm.sourcemeterid = wm.objid 
where mm.parentid = $P{parentid} 
	and mm.serialno = wm.serialno 

[insertMeters]
insert into waterworks_meter ( 
	objid, serialno, brand, capacity, stocktype, sizeid 
) 
select 
	mm.objid, mm.serialno, mm.brand, mm.capacity, mm.stocktype, mm.sizeid 
from migrationmeter mm 
where mm.parentid = $P{parentid} 
	and mm.sourcemeterid is null 
