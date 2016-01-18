[getList]
SELECT * FROM miscrysetting 
${filter}
ORDER BY ry 


#------------------------------------------
# DELETE SUPPORT
#------------------------------------------
[deleteRangesBySettingId]
DELETE FROM miscassesslevelrange WHERE miscrysettingid = $P{objid} 


[deleteAssessLevelBySettingId]
DELETE FROM miscassesslevel WHERE miscrysettingid = $P{objid} 


[deleteItemValueBySettingId]
DELETE FROM miscitemvalue WHERE miscrysettingid = $P{objid} 




#------------------------------------------
# ASSESSLEVEL SUPPORT
#------------------------------------------

[getAssessLevels]
SELECT 
	mal.*,
	pc.code AS classification_code,
	pc.name AS classification_name
FROM miscassesslevel mal 
	INNER JOIN propertyclassification pc ON mal.classification_objid = pc.objid 
WHERE mal.miscrysettingid = $P{miscrysettingid}	
ORDER BY pc.orderno


[getRanges]
SELECT * FROM miscassesslevelrange 
WHERE miscassesslevelid = $P{miscassesslevelid}
ORDER BY mvfrom 


[deleteRangesByAssessLevelId]
DELETE FROM miscassesslevelrange WHERE miscassesslevelid = $P{miscassesslevelid}




#------------------------------------------
# MISCITEMVALUE SUPPORT
#------------------------------------------
[getMiscItemValues]
SELECT 
	miv.*,
	mi.code AS miscitem_code,
	mi.name AS miscitem_name
FROM miscitemvalue miv
	INNER JOIN miscitem mi ON miv.miscitem_objid = mi.objid
WHERE miscrysettingid = $P{miscrysettingid} 
ORDER BY mi.code 



[getIdByRy]
SELECT objid FROM miscrysetting WHERE ry = $P{ry} 

[getSettingsByRy]
SELECT objid FROM miscrysetting WHERE ry = $P{ry} 

[findDuplicate]
SELECT objid 
FROM miscrysetting 
WHERE ry = $P{ry} 
AND previd = $P{previd} 



[getSettingsByLguIdRy]
SELECT s.*
FROM miscrysetting s
	INNER JOIN rysetting_lgu l ON s.objid = l.rysettingid
WHERE l.lguid = $P{lguid}
  AND s.ry = $P{ry}