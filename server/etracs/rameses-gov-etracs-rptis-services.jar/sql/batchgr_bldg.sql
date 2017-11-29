[insertRevisedBldgRpus]
insert into bldgrpu(
  objid,
  landrpuid,
  houseno,
  psic,
  permitno,
  permitdate,
  permitissuedby,
  bldgtype_objid,
  bldgkindbucc_objid,
  basevalue,
  dtcompleted,
  dtoccupied,
  floorcount,
  depreciation,
  depreciationvalue,
  totaladjustment,
  additionalinfo,
  bldgage,
  percentcompleted,
  bldgassesslevel_objid,
  assesslevel,
  condominium,
  bldgclass,
  predominant,
  effectiveage,
  condocerttitle,
  dtcertcompletion,
  dtcertoccupancy
)
select
  concat(replace(br.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  (select objid from landrpu where objid = concat(replace(br.landrpuid, concat('-',r.ry), ''), concat('-', $P{newry}))) as landrpuid,
  br.houseno,
  br.psic,
  br.permitno,
  br.permitdate,
  br.permitissuedby,
  br.bldgtype_objid,
  br.bldgkindbucc_objid,
  br.basevalue,
  br.dtcompleted,
  br.dtoccupied,
  br.floorcount,
  br.depreciation,
  br.depreciationvalue,
  br.totaladjustment,
  br.additionalinfo,
  br.bldgage,
  br.percentcompleted,
  br.bldgassesslevel_objid,
  br.assesslevel,
  br.condominium,
  br.bldgclass,
  br.predominant,
  br.effectiveage,
  br.condocerttitle,
  br.dtcertcompletion,
  br.dtcertoccupancy
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join bldgrpu br on f.rpuid = br.objid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'bldg'
and r.ry < $P{newry} 
${filter}


[insertRevisedBldgStructures]
insert into bldgstructure(
  objid,
  bldgrpuid,
  structure_objid,
  material_objid,
  floor
)
select
  concat(replace(bs.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  concat(replace(bs.bldgrpuid, concat('-',r.ry), ''), concat('-', $P{newry})) as bldgrpuid,
  bs.structure_objid,
  bs.material_objid,
  bs.floor
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join bldgstructure bs on f.rpuid = bs.bldgrpuid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'bldg'
and r.ry < $P{newry} 
${filter}



[insertRevisedBldgStructuralTypes]
insert into bldgrpu_structuraltype(
  objid,
  bldgrpuid,
  bldgtype_objid,
  bldgkindbucc_objid,
  floorcount,
  basefloorarea,
  totalfloorarea,
  basevalue,
  unitvalue,
  classification_objid
)
select
  concat(replace(bs.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  concat(replace(bs.bldgrpuid, concat('-',r.ry), ''), concat('-', $P{newry})) as bldgrpuid,
  bs.bldgtype_objid,
  bs.bldgkindbucc_objid,
  bs.floorcount,
  bs.basefloorarea,
  bs.totalfloorarea,
  bs.basevalue,
  bs.unitvalue,
  bs.classification_objid
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join bldgrpu_structuraltype bs on f.rpuid = bs.bldgrpuid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'bldg'
and r.ry < $P{newry} 
${filter}


[insertRevisedBldgUses]
insert into bldguse(
  objid,
  bldgrpuid,
  structuraltype_objid,
  actualuse_objid,
  basevalue,
  area,
  basemarketvalue,
  depreciationvalue,
  adjustment,
  marketvalue,
  assesslevel,
  assessedvalue,
  addlinfo,
  taxable,
  adjfordepreciation
)
select
  concat(replace(bu.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  concat(replace(bu.bldgrpuid, concat('-',r.ry), ''), concat('-', $P{newry})) as bldgrpuid,
  concat(replace(bu.structuraltype_objid, concat('-',r.ry), ''), concat('-', $P{newry})) as bldgrpuid,
  bu.actualuse_objid,
  bu.basevalue,
  bu.area,
  bu.basemarketvalue,
  bu.depreciationvalue,
  bu.adjustment,
  bu.marketvalue,
  bu.assesslevel,
  bu.assessedvalue,
  bu.addlinfo,
  bu.taxable,
  bu.adjfordepreciation
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join bldguse bu on f.rpuid = bu.bldgrpuid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'bldg'
and r.ry < $P{newry} 
${filter}


[insertRevisedBldgFloors]
insert into bldgfloor(
  objid,
  bldguseid,
  bldgrpuid,
  floorno,
  area,
  storeyrate,
  basevalue,
  unitvalue,
  basemarketvalue,
  adjustment,
  marketvalue
)
select
  concat(replace(bf.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  concat(replace(bf.bldguseid, concat('-',r.ry), ''), concat('-', $P{newry})) as bldguseid,
  concat(replace(bf.bldgrpuid, concat('-',r.ry), ''), concat('-', $P{newry})) as bldgrpuid,
  bf.floorno,
  bf.area,
  bf.storeyrate,
  bf.basevalue,
  bf.unitvalue,
  bf.basemarketvalue,
  bf.adjustment,
  bf.marketvalue
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join bldgfloor bf on f.rpuid = bf.bldgrpuid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'bldg'
and r.ry < $P{newry} 
${filter}



[insertRevisedBldgAdditionalItems]
insert into bldgflooradditional(
  objid,
  bldgfloorid,
  bldgrpuid,
  additionalitem_objid,
  amount,
  expr,
  depreciate
)
select
  concat(replace(bfa.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  concat(replace(bfa.bldgfloorid, concat('-',r.ry), ''), concat('-', $P{newry})) as bldgfloorid,
  concat(replace(bfa.bldgrpuid, concat('-',r.ry), ''), concat('-', $P{newry})) as bldgrpuid,
  bfa.additionalitem_objid,
  bfa.amount,
  bfa.expr,
  bfa.depreciate
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join bldgflooradditional bfa on f.rpuid = bfa.bldgrpuid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'bldg'
and r.ry < $P{newry} 
${filter}



[insertRevisedBldgAdditionalItemParams]
insert into bldgflooradditionalparam(
  objid,
  bldgflooradditionalid,
  bldgrpuid,
  param_objid,
  intvalue,
  decimalvalue
)
select
  concat(replace(bfa.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  concat(replace(bfa.bldgflooradditionalid, concat('-',r.ry), ''), concat('-', $P{newry})) as bldgflooradditionalid,
  concat(replace(bfa.bldgrpuid, concat('-',r.ry), ''), concat('-', $P{newry})) as bldgrpuid,
  bfa.param_objid,
  bfa.intvalue,
  bfa.decimalvalue
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join bldgflooradditionalparam bfa on f.rpuid = bfa.bldgrpuid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'bldg'
and r.ry < $P{newry} 
${filter}

