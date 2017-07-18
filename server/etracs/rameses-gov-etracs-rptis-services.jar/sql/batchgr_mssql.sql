[insertItemsForRevision]
insert into batchgr_items_forrevision(
  objid,
  rpuid,
  realpropertyid,
  barangayid,
  rputype,
  tdno,
  fullpin,
  pin,
  suffix
)
SELECT 
  f.objid,
  f.rpuid,
  f.realpropertyid,
  rp.barangayid,
  r.rputype,
  f.tdno,
  f.fullpin,
  rp.pin,
  r.suffix
FROM faas f 
    INNER JOIN rpu r ON f.rpuid = r.objid 
    INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
    INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
    INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE rp.barangayid = $P{barangayid}
  AND r.rputype LIKE $P{rputype}
  AND r.ry < $P{newry}
  AND f.state = 'CURRENT'
  AND NOT EXISTS(
    SELECT * FROM faas fx 
    INNER JOIN rpu rx ON fx.rpuid = rx.objid 
    WHERE fx.prevtdno = f.tdno AND rx.ry = $P{newry}
  )  
  AND NOT EXISTS(SELECT * FROM batchgr_error WHERE objid = f.objid)
ORDER BY rp.pin, r.suffix 

[findFaasForRevision]
SELECT 
  f.*,
  pc.code AS classification_code, 
  pc.code AS classcode, 
  pc.name AS classification_name, 
  pc.name AS classname, 
  r.ry, f.realpropertyid, r.rputype, r.fullpin, r.totalmv, r.totalav,
  r.totalareasqm, r.totalareaha, 
  rp.barangayid, rp.cadastrallotno, rp.blockno, rp.surveyno, rp.lgutype, rp.pintype, 
  b.name AS barangay_name
  FROM faas f 
    INNER JOIN rpu r ON f.rpuid = r.objid 
    INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
    INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
    INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE f.objid = $P{objid}

