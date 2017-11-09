[insertRevisedMiscRpus]
insert into miscrpu (
  objid,
  landrpuid,
  actualuse_objid
)
select
  concat(replace(m.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  concat(replace(m.landrpuid, concat('-',r.ry), ''), concat('-', $P{newry})) as landrpuid,
  m.actualuse_objid
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join miscrpu m on f.rpuid = m.objid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'misc'
and r.ry < $P{newry}  



[insertRevisedMiscRpuItems]
insert into miscrpuitem (
  objid,
  miscrpuid,
  miv_objid,
  miscitem_objid,
  expr,
  depreciation,
  depreciatedvalue,
  basemarketvalue,
  marketvalue,
  assesslevel,
  assessedvalue
)
select
  concat(replace(m.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  concat(replace(m.miscrpuid, concat('-',r.ry), ''), concat('-', $P{newry})) as miscrpuid,
  m.miv_objid,
  m.miscitem_objid,
  m.expr,
  m.depreciation,
  m.depreciatedvalue,
  m.basemarketvalue,
  m.marketvalue,
  m.assesslevel,
  m.assessedvalue
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join miscrpuitem m on f.rpuid = m.miscrpuid  
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'misc'
and r.ry < $P{newry}  


[insertRevisedMiscRpuItemParams]
insert into miscrpuitem_rptparameter (
  miscrpuitemid,
  miscrpuid,
  param_objid,
  intvalue,
  decimalvalue
)
select
  concat(replace(m.miscrpuitemid, concat('-',r.ry), ''), concat('-', $P{newry})) as miscrpuitemid,
  concat(replace(m.miscrpuid, concat('-',r.ry), ''), concat('-', $P{newry})) as miscrpuid,
  m.param_objid,
  m.intvalue,
  m.decimalvalue
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join miscrpuitem_rptparameter m on f.rpuid = m.miscrpuid  
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'misc'
and r.ry < $P{newry}  