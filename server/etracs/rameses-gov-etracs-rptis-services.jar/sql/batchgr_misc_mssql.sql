[insertRevisedMiscRpus]
insert into miscrpu (
  objid,
  landrpuid,
  actualuse_objid
)
select
  replace(m.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})) as objid,
  replace(m.landrpuid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})) as landrpuid,
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
and not exists(select objid from miscrpu where objid = replace(m.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})))



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
  replace(m.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})) as objid,
  replace(m.miscrpuid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})) as miscrpuid,
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
and not exists(select objid from miscrpuitem where objid = replace(m.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})))



[insertRevisedMiscRpuItemParams]
insert into miscrpuitem_rptparameter (
  miscrpuitemid,
  miscrpuid,
  param_objid,
  intvalue,
  decimalvalue
)
select
  replace(m.miscrpuitemid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})) as miscrpuitemid,
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
and not exists(select * from miscrpuitem_rptparameter where miscrpuitemid = replace(m.miscrpuitemid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})))