[getFaasesForRevision]
SELECT TOP ${count}
  f.*,
  pc.code AS classification_code, 
  pc.code AS classcode, 
  pc.name AS classification_name, 
  pc.name AS classname, 
  r.ry, r.realpropertyid, r.rputype, r.fullpin, r.totalmv, r.totalav,
  r.totalareasqm, r.totalareaha, 
  rp.barangayid, rp.cadastrallotno, rp.blockno, rp.surveyno, rp.lgutype, rp.pintype, 
  b.name AS barangay_name
FROM faas f 
    INNER JOIN rpu r ON f.rpuid = r.objid 
    INNER JOIN realproperty rp ON r.realpropertyid = rp.objid
    INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
    INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE rp.barangayid = $P{barangayid}
  AND r.rputype = $P{rputype}
  AND r.ry < $P{ry}
  AND f.state = 'CURRENT'
  AND NOT EXISTS(
    SELECT * FROM faas fx 
    INNER JOIN rpu rx ON fx.rpuid = rx.objid 
    WHERE fx.prevtdno = f.tdno AND rx.ry = $P{ry}
  )  
  AND NOT EXISTS(SELECT * FROM batchgrerror WHERE faasid = f.objid)
ORDER BY rp.pin, r.suffix 



  
[insertBatchGRError]  
INSERT INTO batchgrerror(faasid, ry, msg)
VALUES($P{faasid}, $P{ry}, $P{msg})


[deleteBatchGRError]
DELETE FROM batchgrerror WHERE faasid = $P{faasid} 



[findRYSetting_land]
SELECT * FROM landrysetting  where ry = $P{ry}

[findRYSetting_bldg]
SELECT * FROM bldgrysetting  where ry = $P{ry}

[findRYSetting_mach]
SELECT * FROM machrysetting  where ry = $P{ry}

[findRYSetting_planttree]
SELECT * FROM planttreerysetting  where ry = $P{ry}

[findRYSetting_misc]
SELECT * FROM miscrysetting  where ry = $P{ry}


[getRYSettings]
SELECT * FROM ${setting}



[getLandRYSettings] 
SELECT * FROM landrysetting ORDER BY ry 

[getBldgRYSettings] 
SELECT * FROM bldgrysetting ORDER BY ry 

[getMachRYSettings] 
SELECT * FROM machrysetting ORDER BY ry 

[getPlantTreeRYSettings] 
SELECT * FROM planttreerysetting ORDER BY ry 

[getMiscRYSettings] 
SELECT * FROM miscrysetting ORDER BY ry 



