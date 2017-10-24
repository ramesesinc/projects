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
  concat(replace(ld.objid, concat('-',r.ry), ''), concat('-', '2017')) as objid,
  concat(replace(ld.landrpuid, concat('-',r.ry), ''), concat('-', '2017')) as landrpuid,
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



[updateLandActualUses]
update faas f, realproperty rp, rpu r, landdetail ld, landassesslevel lal,
  (
      SELECT xal.objid, xal.code, xal.name, xal.rate, xrs.ry, xl.lguid, xal.classification_objid, xal.previd 
      FROM landrysetting xrs
        INNER JOIN rysetting_lgu xl ON xrs.objid = xl.rysettingid 
        INNER JOIN landassesslevel xal ON xrs.objid = xal.landrysettingid 
  )xx
set 
  ld.actualuse_objid = xx.objid,
  ld.assesslevel = xx.rate 
where f.realpropertyid = rp.objid
  and f.rpuid = r.objid 
  and f.rpuid = ld.landrpuid
  and ld.actualuse_objid = lal.objid
  and rp.barangayid = $P{barangayid}
  and r.rputype = 'land'
  and r.ry = $P{newry}
  and (
    xx.previd = lal.objid or (
      xx.ry = r.ry 
      and xx.lguid = rp.lguid 
      and xx.classification_objid = r.classification_objid 
      and xx.name = lal.name 
    )
  )


[updateLandSubclasses]
update faas f, realproperty rp, rpu r, landdetail ld, lcuvsubclass sub, 
    (
      SELECT xsub.*, xlspc.objid as landspecificclass_objid, xrs.ry, xl.lguid 
      FROM landrysetting xrs
        INNER JOIN rysetting_lgu xl ON xrs.objid = xl.rysettingid 
        INNER JOIN lcuvspecificclass xspc ON xrs.objid = xspc.landrysettingid 
        INNER JOIN landspecificclass xlspc ON xspc.landspecificclass_objid = xlspc.objid 
        INNER JOIN lcuvsubclass xsub ON xspc.objid = xsub.specificclass_objid 
    )xx
set 
  ld.subclass_objid = xx.objid, 
  ld.specificclass_objid = xx.specificclass_objid,
  ld.landspecificclass_objid = xx.landspecificclass_objid,
  ld.basevalue = xx.unitvalue  
where f.realpropertyid = rp.objid
  and f.rpuid = r.objid 
  and f.rpuid = ld.landrpuid
  and ld.subclass_objid = sub.objid
  and rp.barangayid = $P{barangayid}
  and r.rputype = 'land'
  and r.ry = $P{newry}
  and (
    xx.previd = sub.objid or (
      xx.ry = r.ry 
      and xx.lguid = rp.lguid 
      and xx.code = sub.code 
      and xx.landspecificclass_objid = ld.landspecificclass_objid
    )
  )






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
  concat(replace(ptd.objid, concat('-',r.ry), ''), concat('-', '2017')) as objid,
  ptd.planttreerpuid,
  concat(replace(ptd.landrpuid, concat('-',r.ry), ''), concat('-', '2017')) as objid,
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


[updatePlantTreeUnitValues]
update faas f, realproperty rp, rpu r, planttreedetail ptd, planttreeunitvalue ptuv,
  (
      SELECT xuv.*, xrs.ry, xl.lguid 
      FROM planttreerysetting xrs
        INNER JOIN rysetting_lgu xl ON xrs.objid = xl.rysettingid 
        INNER JOIN planttreeunitvalue xuv on xrs.objid = xuv.planttreerysettingid 
  )xx
set 
  ptd.planttreeunitvalue_objid = xx.objid,  
  ptd.unitvalue = xx.unitvalue 
where f.realpropertyid = rp.objid
  and f.rpuid = r.objid 
  and f.rpuid = ptd.landrpuid
  and ptd.planttreeunitvalue_objid = ptuv.objid
and rp.barangayid = $P{barangayid}
and r.rputype = 'land'
and r.ry = $P{newry}
and (
  xx.previd = ptuv.objid or (
    xx.ry = r.ry 
    and xx.lguid = rp.lguid 
    and xx.code = ptuv.code 
  )
)


[updatePlantTreeActualUses]
update faas f, realproperty rp, rpu r, planttreedetail ptd, planttreeassesslevel al, 
    (
      SELECT xal.*, xrs.ry, xl.lguid 
      FROM planttreerysetting xrs
        INNER JOIN rysetting_lgu xl ON xrs.objid = xl.rysettingid 
        INNER JOIN planttreeassesslevel xal on xrs.objid = xal.planttreerysettingid
    )xx
set
  ptd.actualuse_objid = xx.objid, 
  ptd.assesslevel = xx.rate 
where f.realpropertyid = rp.objid
  and f.rpuid = r.objid 
  and f.rpuid = ptd.landrpuid
  and ptd.actualuse_objid = al.objid
and rp.barangayid = $P{barangayid}
and r.rputype = 'land'
and r.ry = $P{newry}
and (
  xx.previd = al.objid or (
    xx.ry = r.ry 
    and xx.lguid = rp.lguid 
    and xx.code = al.code 
  )
)





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
  concat(replace(la.objid, concat('-',r.ry), ''), concat('-', '2017')) as objid,
  concat(replace(la.landrpuid, concat('-',r.ry), ''), concat('-', '2017')) as landrpuid,
  case when la.landdetailid is not null 
  then concat(replace(la.landdetailid, concat('-',r.ry), ''), concat('-', '2017')) 
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



[updatLandAdjustmentTypes]
update faas f, realproperty rp, rpu r, landadjustment la, landadjustmenttype lat, 
  (
      SELECT xlat.*, xrs.ry, xl.lguid 
      FROM landrysetting xrs
        INNER JOIN rysetting_lgu xl ON xrs.objid = xl.rysettingid 
        INNER JOIN landadjustmenttype xlat on xrs.objid = xlat.landrysettingid 
  )xx
set 
  la.adjustmenttype_objid = xx.objid, 
  la.expr = xx.expr 
where f.realpropertyid = rp.objid
  and f.rpuid = r.objid 
  and r.objid = la.landrpuid 
  and la.adjustmenttype_objid = lat.objid
and rp.barangayid = $P{barangayid}
and r.rputype = 'land'
and r.ry = $P{newry}
and (
  xx.previd = lat.objid or (
    xx.ry = r.ry 
    and xx.lguid = rp.lguid 
    and xx.code = lat.code 
  )
)



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
  concat(replace(la.objid, concat('-',r.ry), ''), concat('-', '2017')) as objid,
  concat(replace(la.landadjustmentid, concat('-',r.ry), ''), concat('-', '2017')) as objid,
  concat(replace(la.landrpuid, concat('-',r.ry), ''), concat('-', '2017')) as objid,
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

