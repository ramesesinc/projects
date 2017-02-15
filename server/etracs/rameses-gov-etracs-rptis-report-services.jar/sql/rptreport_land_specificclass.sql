[getRevisionYears]
select distinct ry from landrysetting

[getSpecificClasses]
select * 
from landspecificclass 
where state  = 'APPROVED'
order by name 


[getLandSpecificClasses]
select 
	o.name as lguname, b.name as barangay, 
	f.tdno, f.fullpin, f.owner_name, f.owner_address, f.administrator_name, f.titleno, 
	rp.cadastrallotno, rp.blockno, rp.surveyno,
	lspc.name as specificclass, spc.areatype, 
	sum(ld.areasqm) as areasqm, sum(ld.areaha) as areaha, 
	sum(ld.marketvalue) as mv, sum(ld.assessedvalue) as av
from faas f
	inner join rpu r on f.rpuid = r.objid 
	inner join landdetail ld on r.objid = ld.landrpuid 
	inner join landspecificclass lspc on ld.landspecificclass_objid = lspc.objid 
	inner join lcuvspecificclass spc on ld.specificclass_objid = spc.objid 
	inner join realproperty rp on f.realpropertyid = rp.objid 
	inner join barangay b on rp.barangayid = b.objid 
	inner join sys_org o on f.lguid = o.objid 
where f.lguid like $P{lguid}
  and rp.barangayid like $P{barangayid}
  and spc.classification_objid like $P{classificationid}
  and f.state = 'CURRENT'
  and lspc.objid = $P{landspecificclassid}
group by 
	o.name, b.name, 
	f.tdno, f.fullpin, f.owner_name, f.owner_address, f.administrator_name,
	rp.cadastrallotno, rp.blockno, rp.surveyno,
	lspc.name, spc.areatype
order by o.name, b.name, rp.pin, r.suffix 
