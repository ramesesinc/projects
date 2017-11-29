[insertRevisedPlantTreeRpus]
insert into planttreerpu (
  objid,
  landrpuid,
  productive,
  nonproductive
)
select
  concat(replace(p.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  concat(replace(p.landrpuid, concat('-',r.ry), ''), concat('-', $P{newry})) as landrpuid,
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
and not exists(select * from planttreerpu where objid = concat(replace(p.objid, concat('-',r.ry), ''), concat('-', $P{newry})))


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
  concat(replace(p.objid, concat('-',r.ry), ''), concat('-', $P{newry})) as objid,
  concat(replace(p.planttreerpuid, concat('-',r.ry), ''), concat('-', $P{newry})) as planttreerpuid,
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
and not exists(select * from planttreedetail where objid = concat(replace(p.objid, concat('-',r.ry), ''), concat('-', $P{newry})))