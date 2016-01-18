#----------------------------------------------------------------------
#
# Report on Real Property Assessments  
#
#----------------------------------------------------------------------
[getReportOnRPATaxable]
SELECT 
	pc.objid AS classid,
	pc.name AS classname, 
	COUNT( 1 ) AS rpucount,
	SUM(CASE WHEN r.rputype = 'land' THEN r.totalareasqm ELSE 0.0 END ) AS areasqm, 
	SUM(CASE WHEN r.rputype = 'land' THEN r.totalareaha ELSE 0.0 END ) AS areaha, 

	SUM( CASE WHEN r.rputype = 'land' THEN r.totalmv ELSE 0.0 END ) AS landmv,
	SUM( CASE WHEN r.rputype = 'bldg' AND r.totalmv <= 175000 THEN r.totalmv ELSE 0.0 END ) AS bldgmv150less,
	SUM( CASE WHEN r.rputype = 'bldg' AND r.totalmv > 175000 THEN r.totalmv ELSE 0.0 END ) AS bldgmvover150,
	SUM( CASE WHEN r.rputype = 'mach' THEN r.totalmv ELSE 0.0 END ) AS machmv,
	SUM( CASE WHEN rputype NOT IN( 'land', 'bldg', 'mach') THEN r.totalmv ELSE 0.0 END ) AS othermv, 
	SUM( r.totalmv ) AS totalmv,
	
	SUM( CASE WHEN r.rputype = 'land' THEN r.totalav ELSE 0.0 END ) AS landav,
	SUM( CASE WHEN r.rputype = 'bldg' AND r.totalmv <= 175000 THEN r.totalav ELSE 0.0 END ) AS bldgav150less,
	SUM( CASE WHEN r.rputype = 'bldg' AND r.totalmv > 175000 THEN r.totalav ELSE 0.0 END ) AS bldgavover150,
	SUM( CASE WHEN r.rputype = 'mach' THEN r.totalav ELSE 0.0 END ) AS machav,
	SUM( CASE WHEN rputype NOT IN( 'land', 'bldg', 'mach') THEN r.totalav ELSE 0.0 END ) AS otherav, 
	SUM( r.totalav ) AS totalav,
	
	SUM( CASE WHEN f.restrictionid = 'CARP' THEN r.totalav ELSE 0.0 END ) AS carpav,
	SUM( CASE WHEN f.restrictionid = 'UNDER_LITIGATION' THEN r.totalav ELSE 0.0 END ) AS litigationav,
	SUM( CASE WHEN f.restrictionid = 'OTHER' THEN r.totalav ELSE 0.0 END ) AS otherrestrictionav,
	SUM( CASE WHEN f.restrictionid IS NOT NULL THEN r.totalav ELSE 0.0 END ) AS totalrestriction 

FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
WHERE ${filter}
  AND f.state = 'CURRENT' 
  AND r.taxable = 1 
GROUP BY pc.objid, pc.name, pc.orderno
ORDER BY pc.orderno  


[getReportOnRPAExempt]
SELECT 
	e.objid AS classid,
	e.name AS classname, 
	COUNT( 1 ) AS rpucount,
	SUM(CASE WHEN r.rputype = 'land' THEN r.totalareasqm ELSE 0.0 END ) AS areasqm, 
	SUM(CASE WHEN r.rputype = 'land' THEN r.totalareaha ELSE 0.0 END ) AS areaha, 
	
	SUM( CASE WHEN r.rputype = 'land' THEN r.totalmv ELSE 0.0 END ) AS landmv,
	SUM( CASE WHEN r.rputype = 'bldg' AND r.totalmv <= 175000 THEN r.totalmv ELSE 0.0 END ) AS bldgmv150less,
	SUM( CASE WHEN r.rputype = 'bldg' AND r.totalmv > 175000 THEN r.totalmv ELSE 0.0 END ) AS bldgmvover150,
	SUM( CASE WHEN r.rputype = 'mach' THEN r.totalmv ELSE 0.0 END ) AS machmv,
	SUM( CASE WHEN rputype NOT IN( 'land', 'bldg', 'mach') THEN r.totalmv ELSE 0.0 END ) AS othermv, 
	SUM( r.totalmv ) AS totalmv,
	
	SUM( CASE WHEN r.rputype = 'land' THEN r.totalav ELSE 0.0 END ) AS landav,
	SUM( CASE WHEN r.rputype = 'bldg' AND r.totalmv <= 175000 THEN r.totalav ELSE 0.0 END ) AS bldgav150less,
	SUM( CASE WHEN r.rputype = 'bldg' AND r.totalmv > 175000 THEN r.totalav ELSE 0.0 END ) AS bldgavover150,
	SUM( CASE WHEN r.rputype = 'mach' THEN r.totalav ELSE 0.0 END ) AS machav,
	SUM( CASE WHEN rputype NOT IN( 'land', 'bldg', 'mach') THEN r.totalav ELSE 0.0 END ) AS otherav, 
	SUM( r.totalav ) AS totalav,
	
	SUM( CASE WHEN f.restrictionid = 'CARP' THEN r.totalav ELSE 0.0 END ) AS carpav,
	SUM( CASE WHEN f.restrictionid = 'UNDER_LITIGATION' THEN r.totalav ELSE 0.0 END ) AS litigationav,
	SUM( CASE WHEN f.restrictionid = 'OTHER' THEN r.totalav ELSE 0.0 END ) AS otherrestrictionav,
	SUM( CASE WHEN f.restrictionid IS NOT NULL THEN r.totalav ELSE 0.0 END ) AS totalrestriction 

FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN exemptiontype e ON r.exemptiontype_objid = e.objid 
WHERE ${filter}
  AND f.state = 'CURRENT' 
  AND r.taxable = 0
GROUP BY e.objid, e.name, e.orderno 
ORDER BY e.orderno  


