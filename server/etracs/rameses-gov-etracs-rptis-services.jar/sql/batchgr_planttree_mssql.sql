[insertRevisedPlantTreeRpus]
insert into planttreerpu (
  objid,
  landrpuid,
  productive,
  nonproductive
)
select
  replace(p.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})) as objid,
  replace(p.landrpuid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})) as landrpuid,
  p.productive,
  p.nonproductive
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join planttreerpu p on f.rpuid = p.objid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'planttree'
and r.ry < $P{newry}  
and not exists(select objid from planttreerpu where objid = replace(p.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})))



[insertRevisedPlantTreeDetails]
insert into planttreedetail (
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
  replace(p.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})) as objid,
  replace(p.planttreerpuid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})) as planttreerpuid,
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
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join planttreedetail p on f.rpuid = p.planttreerpuid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'planttree'
and r.ry < $P{newry}  
and not exists(select objid from planttreedetail where objid = replace(p.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})))