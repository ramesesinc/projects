[lookupAssessLevels]
SELECT lal.*, l.barangayid 
FROM landrysetting rs
	INNER JOIN rysetting_lgu l ON rs.objid = l.rysettingid 
	INNER JOIN landassesslevel lal ON rs.objid = lal.landrysettingid 
WHERE rs.ry = $P{ry}
  AND l.lguid = $P{lguid}
  AND (lal.code LIKE $P{searchtext} OR lal.name LIKE $P{searchtext})	
ORDER BY code 


[lookupAssessLevelByPrevId]
SELECT lal.* 
FROM landassesslevel lal
WHERE lal.previd = $P{previd}


[lookupSubclasses]  
SELECT sub.*, l.barangayid,
	spc.code AS specificclass_code,
	spc.name AS specificclass_name,
	spc.areatype AS specificclass_areatype,
	spc.classification_objid as specificclass_classification_objid,
	spc.classification_objid,
	pc.code AS classification_code,
	pc.name AS classification_name
FROM landrysetting rs
	INNER JOIN rysetting_lgu l ON rs.objid = l.rysettingid 
	INNER JOIN lcuvspecificclass spc ON rs.objid = spc.landrysettingid 
	INNER JOIN lcuvsubclass sub ON spc.objid = sub.specificclass_objid 
	INNER JOIN propertyclassification pc ON spc.classification_objid = pc.objid 
WHERE rs.ry = $P{ry}
  AND l.lguid = $P{lguid}
  AND (sub.code LIKE $P{searchtext} OR sub.name LIKE $P{searchtext} OR spc.name LIKE $P{searchtext})	
ORDER BY spc.code, sub.code   


[lookupSubclassByPrevId]  
SELECT sub.*,
	spc.code AS specificclass_code,
	spc.name AS specificclass_name,
	spc.areatype AS specificclass_areatype,
	spc.classification_objid,
	pc.code AS classification_code,
	pc.name AS classification_name
FROM lcuvsubclass sub 
	INNER JOIN lcuvspecificclass spc ON sub.specificclass_objid = spc.objid  
	INNER JOIN propertyclassification pc ON spc.classification_objid = pc.objid 
WHERE sub.previd = $P{previd}


[lookupStrippings]
SELECT st.*, l.barangayid 
FROM landrysetting rs
    INNER JOIN rysetting_lgu l ON rs.objid = l.rysettingid 
	INNER JOIN lcuvstripping st ON rs.objid = st.landrysettingid 
WHERE rs.ry = $P{ry}
  AND l.lguid = $P{lguid}
  AND st.classification_objid = $P{classificationid} 
  AND st.striplevel LIKE $P{searchtext} 
ORDER BY st.striplevel

[lookupStrippingByPrevId]
SELECT st.* 
FROM lcuvstripping st 
WHERE st.previd = $P{previd}


[lookupAdjustmentTypes]
SELECT DISTINCT lat.*, l.barangayid
FROM landadjustmenttype lat 
	INNER JOIN landadjustmenttype_classification lc ON lat.objid = lc.landadjustmenttypeid
	INNER JOIN landrysetting rs ON lat.landrysettingid = rs.objid 
	INNER JOIN rysetting_lgu l ON rs.objid = l.rysettingid 
WHERE rs.ry = $P{ry}
  AND l.lguid = $P{lguid}
  AND (l.barangayid = $P{barangayid} OR l.barangayid IS NULL)
  AND lc.classification_objid LIKE $P{classificationid}
  AND (lat.code  LIKE $P{searchtext} OR lat.name LIKE $P{searchtext})


[lookupAdjustmentTypeByPrevId]
SELECT lat.*
FROM landadjustmenttype lat 
WHERE lat.previd = $P{previd}
