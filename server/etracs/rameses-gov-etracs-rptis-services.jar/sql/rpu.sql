[createRpuMaster]
INSERT INTO rpumaster(objid, currentfaasid, currentrpuid) 
VALUES($P{objid}, $P{currentfaasid}, $P{currentrpuid})


[deleteRpuMaster]
DELETE FROM rpumaster WHERE objid = $P{objid}

[findRpuMasterById]
SELECT * FROM rpumaster WHERE objid = $P{objid}


[findDuplicateFullPin]
SELECT objid   
FROM rpu r 
WHERE objid <> $P{objid} AND ry = $P{ry} AND fullpin = $P{fullpin} 
AND r.state <> 'CANCELLED'
AND EXISTS(SELECT * FROM faas WHERE rpuid = r.objid  )


[deleteRpu]
DELETE FROM rpu WHERE objid = $P{objid} AND state NOT IN ('CURRENT', 'CANCELLED')


[deleteAllAssessments]
DELETE FROM rpu_assessment WHERE rpuid = $P{objid}


[findRpuInfoById]
SELECT * FROM rpu WHERE objid = $P{objid}


[findLandRpuById]
SELECT *
FROM rpu 
WHERE objid = $P{objid}


[findLandRpuByRealPropertyId]
SELECT rpu.*
FROM rpu rpu
  INNER JOIN realproperty rp ON rpu.realpropertyid = rp.objid 
WHERE rpu.realpropertyid = $P{realpropertyid} 
  AND rpu.rputype = 'land' 


[updateRpuState]
UPDATE rpu SET state = $P{state} WHERE objid = $P{objid}


[getNextSuffixes]
SELECT 
  rputype,
  MAX(suffix +1) AS nextsuffix 
FROM rpu 
WHERE realpropertyid = $P{realpropertyid}
AND rputype <> 'land'
GROUP BY rputype 


[updateSuffix]
UPDATE rpu SET suffix = $P{suffix}, fullpin = $P{fullpin} WHERE objid = $P{objid}


[getLandImprovementsRpuByRealPropertyId]
SELECT f.objid as faasid, rpu.objid, rpu.suffix, f.fullpin 
FROM rpu rpu
  INNER JOIN realproperty rp ON rpu.realpropertyid = rp.objid 
  INNER JOIN faas f ON rpu.objid = f.rpuid 
WHERE f.realpropertyid = $P{realpropertyid} 
  AND rpu.rputype != 'land'   


[findLandRySetting]
select objid from landrysetting where ry = $P{ry}

