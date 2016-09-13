[getList]
SELECT * FROM landrysetting 
${filter}
ORDER BY ry 


[deleteRySettingLgu]
DELETE FROM rysetting_lgu WHERE rysettingid = $P{objid}

[deleteRangesBySettingId]
DELETE FROM landassesslevelrange WHERE landrysettingid = $P{objid}

[deleteAssessLevelsBySettingId]
DELETE FROM landassesslevel WHERE landrysettingid = $P{objid}

[deleteSubClassesBySettingId]
DELETE FROM lcuvsubclass WHERE landrysettingid = $P{objid}

[deleteSpecificClassesBySettingId]
DELETE FROM lcuvspecificclass WHERE landrysettingid = $P{objid}

[deleteStrippingsBySettingId]
DELETE FROM lcuvstripping WHERE landrysettingid = $P{objid}

[deleteLandAdjustmentTypesBySettingId]
DELETE FROM landadjustmenttype WHERE landrysettingid = $P{objid}

[deleteLandAdjustmentTypeClassificationBySettingId]
DELETE FROM landadjustmenttype_classification WHERE landrysettingid = $P{objid}



#===================================================================
# ASSESSLEVEL SUPPORT
#===================================================================
[getAssessLevels]
SELECT 
	lal.*,
	pc.code AS classification_code,
	pc.name AS classification_name 
FROM landassesslevel lal
	LEFT JOIN propertyclassification pc ON lal.classification_objid = pc.objid 
WHERE lal.landrysettingid = $P{landrysettingid} 
ORDER BY pc.orderno


[getAssessLevelRanges]
SELECT * FROM landassesslevelrange 
WHERE landassesslevelid = $P{landassesslevelid} 
ORDER BY mvfrom 


[deleteAssessLevel]
DELETE FROM landassesslevel WHERE objid = $P{objid}


[deleteRangesByAssessLevelId]
DELETE FROM landassesslevelrange WHERE landassesslevelid = $P{landassesslevelid}


#===================================================================
# SPECIFICCLASS SUPPORT
#===================================================================
[getSpecificClasses]
SELECT spc.*, 
	lspc.code, lspc.name, 
	lspc.code as landspecificclass_code, lspc.name as landspecificclass_name  
FROM lcuvspecificclass spc 
	inner join landspecificclass lspc on spc.landspecificclass_objid = lspc.objid 
WHERE landrysettingid = $P{landrysettingid}
  AND classification_objid LIKE $P{classification_objid} 

ORDER BY code, name 


[deleteSpecificClass]
DELETE FROM lcuvspecificclass 
WHERE landrysettingid = $P{landrysettingid} AND classification_objid = $P{classification_objid}



#===================================================================
# SUBCLASS SUPPORT
#===================================================================
[getSubClasses]
SELECT * 
FROM lcuvsubclass 
WHERE specificclass_objid = $P{specificclass_objid} ORDER BY code, name

[deleteSubclasses]
DELETE FROM lcuvsubclass WHERE specificclass_objid = $P{specificclass_objid}

#===================================================================
# STRIPPING SUPPORT
#===================================================================
[getStrippings]
SELECT * FROM lcuvstripping
WHERE landrysettingid = $P{landrysettingid}
  AND classification_objid LIKE $P{classification_objid} 
ORDER BY striplevel


#===================================================================
# LANDADJUSTMENTTYPE SUPPORT
#===================================================================
[getLandAdjustmentTypes]
SELECT * FROM landadjustmenttype WHERE landrysettingid = $P{landrysettingid} ORDER BY code


[getLandAdjustmentTypeClassifications]
SELECT lc.*, pc.code AS classification_code, pc.name AS classification_name
FROM landadjustmenttype_classification lc
	INNER JOIN propertyclassification pc ON lc.classification_objid = pc.objid 
WHERE lc.landadjustmenttypeid = $P{landadjustmenttypeid}


[deleteLandAdjustmentType]
DELETE FROM landadjustmenttype WHERE objid = $P{objid}


[deleteLandAdjustmentTypeClassifications]
DELETE FROM landadjustmenttype_classification WHERE landadjustmenttypeid = $P{landadjustmenttypeid}


[insertLandAdjustmentTypeClassification]
INSERT INTO landadjustmenttype_classification 
	(landadjustmenttypeid, classification_objid, landrysettingid)
VALUES	
	($P{landadjustmenttypeid}, $P{classification_objid}, $P{landrysettingid})


[getIdByRy]
SELECT objid FROM landrysetting WHERE ry = $P{ry} 

[findIdByRy]
SELECT objid FROM landrysetting WHERE ry = $P{ry} 

[getSettingsByRy]
SELECT objid FROM landrysetting WHERE ry = $P{ry} 


[getAdjustmentTypeClassifications]  
SELECT pc.objid, pc.code, pc.name 
FROM landadjustmenttype_classification lc
	INNER JOIN propertyclassification pc ON lc.classification_objid = pc.objid 
WHERE lc.landadjustmenttypeid = $P{landadjustmenttypeid}


[findDuplicate]
SELECT objid 
FROM landrysetting 
WHERE ry = $P{ry} 
AND previd = $P{previd}



[getSettingsByLguIdRy]
SELECT s.*
FROM landrysetting s
	INNER JOIN rysetting_lgu l ON s.objid = l.rysettingid
WHERE l.lguid = $P{lguid}
  AND s.ry = $P{ry}