#----------------------------------------
# PlantTreeRySetting
#----------------------------------------
[getList]
SELECT * FROM planttreerysetting 
${filter}
ORDER BY ry 


[deleteAssessLevels]
DELETE FROM planttreeassesslevel WHERE planttreerysettingid = $P{objid}


[deleteUnitValues]
DELETE FROM planttreeunitvalue WHERE planttreerysettingid = $P{objid}


[deleteRySettingLgus]
DELETE FROM rysetting_lgu WHERE rysettingid = $P{objid} 



[getAssessLevels]
SELECT pal.*, pc.code AS classification_code, pc.name AS classification_name 
FROM planttreeassesslevel pal 
	LEFT JOIN propertyclassification pc on pal.classification_objid = pc.objid 
WHERE pal.planttreerysettingid = $P{planttreerysettingid}


[getUnitValues]
SELECT
	puv.*,
	pt.code AS planttree_code,
	pt.name AS planttree_name
FROM planttreeunitvalue puv
	INNER JOIN planttree pt ON puv.planttree_objid = pt.objid
WHERE puv.planttreerysettingid  = $P{planttreerysettingid}
  AND puv.planttree_objid LIKE $P{planttree_objid}
ORDER BY  puv.code 



[findIdByRy]
SELECT objid FROM planttreerysetting WHERE ry = $P{ry} 

[getSettingsByRy]
SELECT objid FROM planttreerysetting WHERE ry = $P{ry} 

[findDuplicate]
SELECT objid 
FROM planttreerysetting 
WHERE ry = $P{ry} 
AND previd = $P{previd} 


[getSettingsByLguIdRy]
SELECT s.*
FROM planttreerysetting s
	INNER JOIN rysetting_lgu l ON s.objid = l.rysettingid
WHERE l.lguid = $P{lguid}
  AND s.ry = $P{ry}