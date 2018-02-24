[insertRevisedLandRpus]
insert into landrpu(
  objid,
  idleland,
  totallandbmv,
  totallandmv,
  totallandav,
  totalplanttreebmv,
  totalplanttreemv,
  totalplanttreeadjustment,
  totalplanttreeav,
  landvalueadjustment,
  publicland,
  distanceawr,
  distanceltc
)
select 
  concat(replace(lr.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  lr.idleland,
  lr.totallandbmv,
  lr.totallandmv,
  lr.totallandav,
  lr.totalplanttreebmv,
  lr.totalplanttreemv,
  lr.totalplanttreeadjustment,
  lr.totalplanttreeav,
  lr.landvalueadjustment,
  lr.publicland,
  lr.distanceawr,
  lr.distanceltc
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join landrpu lr on f.rpuid = lr.objid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'land'
and r.ry < $P{newry}
${filter}


[insertRevisedLandDetails]
insert into landdetail(
  objid,
  landrpuid,
  subclass_objid,
  specificclass_objid,
  actualuse_objid,
  stripping_objid,
  striprate,
  areatype,
  addlinfo,
  area,
  areasqm,
  areaha,
  basevalue,
  unitvalue,
  taxable,
  basemarketvalue,
  adjustment,
  landvalueadjustment,
  actualuseadjustment,
  marketvalue,
  assesslevel,
  assessedvalue,
  landspecificclass_objid
)
select 
  concat(replace(ld.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  concat(replace(ld.landrpuid, concat('-',r.ry), ''), concat('-', $P{newry})) as landrpuid,
  ld.subclass_objid,
  ld.specificclass_objid,
  ld.actualuse_objid,
  ld.stripping_objid,
  ld.striprate,
  ld.areatype,
  ld.addlinfo,
  ld.area,
  ld.areasqm,
  ld.areaha,
  ld.basevalue,
  ld.unitvalue,
  ld.taxable,
  ld.basemarketvalue,
  ld.adjustment,
  ld.landvalueadjustment,
  ld.actualuseadjustment,
  ld.marketvalue,
  ld.assesslevel,
  ld.assessedvalue,
  ld.landspecificclass_objid
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join landdetail ld on f.rpuid = ld.landrpuid
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'land'
and r.ry < $P{newry}
${filter}


[insertRevisedPlantTreeDetails]
insert into planttreedetail(
  objid,
  planttreerpuid,
  landrpuid,
  planttreeunitvalue_objid,
  planttree_objid,
  actualuse_objid,
  productive,
  nonproductive,
  nonproductiveage,
  unitvalue,
  basemarketvalue,
  adjustment,
  adjustmentrate,
  marketvalue,
  assesslevel,
  assessedvalue,
  areacovered
)
select 
  concat(replace(ptd.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  ptd.planttreerpuid,
  concat(replace(ptd.landrpuid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  ptd.planttreeunitvalue_objid,
  ptd.planttree_objid,
  ptd.actualuse_objid,
  ptd.productive,
  ptd.nonproductive,
  ptd.nonproductiveage,
  ptd.unitvalue,
  ptd.basemarketvalue,
  ptd.adjustment,
  ptd.adjustmentrate,
  ptd.marketvalue,
  ptd.assesslevel,
  ptd.assessedvalue,
  ptd.areacovered
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join planttreedetail ptd on f.rpuid = ptd.landrpuid
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'land'
and r.ry < $P{newry}
${filter}


[insertRevisedLandAdjustments]
insert into landadjustment(
  objid,
  landrpuid,
  landdetailid,
  adjustmenttype_objid,
  expr,
  adjustment,
  type,
  basemarketvalue,
  marketvalue
)
select 
  concat(replace(la.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  concat(replace(la.landrpuid, concat('-',r.ry), ''), concat('-', $P{newry})) as landrpuid,
  case when la.landdetailid is not null 
  then concat(replace(la.landdetailid, concat('-',r.ry), ''), concat('-', $P{newry})) 
  else null end as landdetailid,
  la.adjustmenttype_objid,
  la.expr,
  la.adjustment,
  la.type,
  la.basemarketvalue,
  la.marketvalue
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join landadjustment la on f.rpuid = la.landrpuid
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'land'
and r.ry < $P{newry}
${filter}



[insertRevisedLandAdjustmentParameters]
insert into landadjustmentparameter(
  objid,
  landadjustmentid,
  landrpuid,
  parameter_objid,
  value,
  param_objid
)
select 
  concat(replace(la.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  concat(replace(la.landadjustmentid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  concat(replace(la.landrpuid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  la.parameter_objid,
  la.value,
  la.param_objid
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join landadjustmentparameter la on f.rpuid = la.landrpuid
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'land'
and r.ry < $P{newry}
${filter}

