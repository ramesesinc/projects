[findMachInfoById]
select mr.*
from machrpu mr 
where mr.objid = $P{objid}	


[findBldgInfoByPin]
select 
	f.owner_name as bldgownername,
	r.fullpin as bldgpin
from realproperty rp 
	inner join faas f on rp.objid = f.realpropertyid 
	inner join rpu r on f.rpuid = r.objid 
where rp.pin = $P{pin}
  and r.rputype = 'bldg'


[getMachDetails]
select
	m.name as machinename,
	md.brand,
	md.model, 
	md.capacity,
	md.yearacquired,
	md.estimatedlife,
	md.remaininglife,
	md.yearinstalled,
	md.operationyear
from machdetail md 	
	inner join machine m on md.machine_objid = m.objid 
where md.machrpuid = $P{objid}


[getAppraisals]
select 
	md.*,
	m.code as machine_code,
	m.name as machine_name 
from machdetail md 
	inner join machine m on md.machine_objid = m.objid 	
where md.machrpuid = $P{objid}


[getAssessments]
SELECT 
	lal.code as classcode,
	lal.name as classname, 
	lal.code AS actualuse,
	lal.name AS actualusename,
	ra.marketvalue,
	ra.assesslevel / 100 AS assesslevel,
	ra.assesslevel AS assesslevelrate,
	ra.assessedvalue AS assessedvalue 
FROM rpu_assessment ra 
	INNER JOIN machassesslevel lal ON ra.actualuse_objid = lal.objid 
WHERE ra.rpuid = $P{objid}	


