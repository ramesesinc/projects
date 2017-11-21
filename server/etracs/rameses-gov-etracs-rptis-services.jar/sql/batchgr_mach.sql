[insertRevisedMachRpus]
insert into machrpu(
  objid,
  landrpuid,
  bldgmaster_objid
)
select
  concat(replace(m.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  (select objid from landrpu where objid = concat(replace(m.landrpuid, concat('-',r.ry), ''), concat('-', $P{newry}))) as landrpuid,
  m.bldgmaster_objid
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join machrpu m on f.rpuid = m.objid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'mach'
and r.ry < $P{newry} 
and not exists(select * from machrpu where objid = concat(replace(m.objid, concat('-',r.ry), ''), concat('-', $P{newry})))



[insertRevisedMachUses]
insert into machuse(
  objid,
  machrpuid,
  actualuse_objid,
  basemarketvalue,
  marketvalue,
  assesslevel,
  assessedvalue
)
select
  concat(replace(m.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  concat(replace(m.machrpuid, concat('-',r.ry), ''), concat('-', $P{newry})) as machrpuid,
  m.actualuse_objid,
  m.basemarketvalue,
  m.marketvalue,
  m.assesslevel,
  m.assessedvalue
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join machuse m on f.rpuid = m.machrpuid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'mach'
and r.ry < $P{newry} 
and not exists(select * from machuse where objid = concat(replace(m.objid, concat('-',r.ry), ''), concat('-', $P{newry})))


[insertRevisedMachDetails]
insert into machdetail(
  objid,
  machuseid,
  machrpuid,
  machine_objid,
  operationyear,
  replacementcost,
  depreciation,
  depreciationvalue,
  basemarketvalue,
  marketvalue,
  assesslevel,
  assessedvalue,
  brand,
  capacity,
  model,
  serialno,
  status,
  yearacquired,
  estimatedlife,
  remaininglife,
  yearinstalled,
  yearsused,
  originalcost,
  freightcost,
  insurancecost,
  installationcost,
  brokeragecost,
  arrastrecost,
  othercost,
  acquisitioncost,
  feracid,
  ferac,
  forexid,
  forex,
  residualrate,
  conversionfactor,
  swornamount,
  useswornamount,
  imported,
  newlyinstalled,
  autodepreciate,
  taxable
)
select
  concat(replace(m.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  concat(replace(m.machuseid, concat('-',r.ry), ''), concat('-', $P{newry})) as machuseid,
  concat(replace(m.machrpuid, concat('-',r.ry), ''), concat('-', $P{newry})) as machrpuid,
  m.machine_objid,
  m.operationyear,
  m.replacementcost,
  m.depreciation,
  m.depreciationvalue,
  m.basemarketvalue,
  m.marketvalue,
  m.assesslevel,
  m.assessedvalue,
  m.brand,
  m.capacity,
  m.model,
  m.serialno,
  m.status,
  m.yearacquired,
  m.estimatedlife,
  m.remaininglife,
  m.yearinstalled,
  m.yearsused,
  m.originalcost,
  m.freightcost,
  m.insurancecost,
  m.installationcost,
  m.brokeragecost,
  m.arrastrecost,
  m.othercost,
  m.acquisitioncost,
  m.feracid,
  m.ferac,
  m.forexid,
  m.forex,
  m.residualrate,
  m.conversionfactor,
  m.swornamount,
  m.useswornamount,
  m.imported,
  m.newlyinstalled,
  m.autodepreciate,
  m.taxable
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join machdetail m on f.rpuid = m.machrpuid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'mach'
and r.ry < $P{newry} 
and not exists(select * from machdetail where objid = concat(replace(m.objid, concat('-',r.ry), ''), concat('-', $P{newry})))
