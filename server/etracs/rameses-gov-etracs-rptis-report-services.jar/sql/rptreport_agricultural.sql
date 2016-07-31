[getActualUseSummaryByArea]
select 
	b.name as barangay,
	lspc.name as specificclass, 
	sum(ld.areaha) as area
from faas f 
	inner join rpu r on f.rpuid = r.objid 
	inner join landdetail ld on r.objid = ld.landrpuid 
	inner join lcuvspecificclass spc on ld.specificclass_objid = spc.objid 
	inner join propertyclassification pc on spc.classification_objid = pc.objid 
	inner join landspecificclass lspc on ld.landspecificclass_objid = lspc.objid 
	inner join realproperty rp on f.realpropertyid = rp.objid 
	inner join barangay b on rp.barangayid = b.objid 
where f.lguid = $P{lguid}
  and f.state = 'current' 
  and pc.name = 'AGRICULTURAL' 
  and r.taxable = $P{taxable}
group by b.name, lspc.name 
order by b.name, lspc.name 