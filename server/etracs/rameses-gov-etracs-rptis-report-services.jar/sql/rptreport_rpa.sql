#----------------------------------------------------------------------
#
# Report on Real Property Assessments  
#
#----------------------------------------------------------------------
[getTaxables]
SELECT 
	pc.objid AS classid,
	pc.name AS classname, 
	SUM( case when rp.claimno is null then 1 else 0 end) AS rpucount,
	SUM(CASE WHEN r.rputype = 'land' and rp.claimno is null THEN r.totalareasqm ELSE 0.0 END ) AS areasqm, 
	SUM(CASE WHEN r.rputype = 'land' and rp.claimno is null THEN r.totalareaha ELSE 0.0 END ) AS areaha, 

	SUM( CASE WHEN r.rputype = 'land' THEN r.totalmv ELSE 0.0 END ) AS landmv,
	SUM( CASE WHEN r.rputype = 'bldg' AND r.totalmv <= 175000 THEN r.totalmv ELSE 0.0 END ) AS bldgmv175less,
	SUM( CASE WHEN r.rputype = 'bldg' AND r.totalmv > 175000 THEN r.totalmv ELSE 0.0 END ) AS bldgmvover175,
	SUM( CASE WHEN r.rputype = 'mach' THEN r.totalmv ELSE 0.0 END ) AS machmv,
	SUM( CASE WHEN rputype NOT IN( 'land', 'bldg', 'mach') THEN r.totalmv ELSE 0.0 END ) AS othermv, 
	SUM( r.totalmv ) AS totalmv,
	
	SUM( CASE WHEN r.rputype = 'land' THEN r.totalav ELSE 0.0 END ) AS landav,
	SUM( CASE WHEN r.rputype = 'bldg' AND r.totalmv <= 175000 THEN r.totalav ELSE 0.0 END ) AS bldgav175less,
	SUM( CASE WHEN r.rputype = 'bldg' AND r.totalmv > 175000 THEN r.totalav ELSE 0.0 END ) AS bldgavover175,
	SUM( CASE WHEN r.rputype = 'mach' THEN r.totalav ELSE 0.0 END ) AS machav,
	SUM( CASE WHEN rputype NOT IN( 'land', 'bldg', 'mach') THEN r.totalav ELSE 0.0 END ) AS otherav, 
	SUM( r.totalav ) AS totalav,
	
	SUM( CASE WHEN exists(select * from faas_restriction where parent_objid = f.objid and state = 'ACTIVE' and restrictiontype_objid = 'CARP' ) THEN r.totalav ELSE 0.0 END ) AS carpav,
	SUM( CASE WHEN exists(select * from faas_restriction where parent_objid = f.objid and state = 'ACTIVE' and restrictiontype_objid = 'UNDER_LITIGATION' ) THEN r.totalav ELSE 0.0 END ) AS litigationav,
	SUM( CASE WHEN exists(select * from faas_restriction where parent_objid = f.objid and state = 'ACTIVE' and restrictiontype_objid  NOT IN ('CARP', 'UNDER_LITIGATION')) THEN r.totalav ELSE 0.0 END ) AS otherrestrictionav,
	SUM( CASE WHEN exists(select * from faas_restriction where parent_objid = f.objid and state = 'ACTIVE') THEN r.totalav ELSE 0.0 END ) AS totalrestrictionav

FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
WHERE r.taxable = 1 
  AND (
		(f.dtapproved <= $P{enddate} AND f.state = 'CURRENT' ) OR 
		(f.canceldate > $P{enddate} AND f.state = 'CANCELLED' )
  )
  ${filter}
GROUP BY pc.objid, pc.name, pc.orderno
ORDER BY pc.orderno  


[getExempts]
SELECT 
	e.objid AS classid,
	e.name AS classname, 
	SUM( case when rp.claimno is null then 1 else 0 end) AS rpucount,
	SUM(CASE WHEN r.rputype = 'land' and rp.claimno is null THEN r.totalareasqm ELSE 0.0 END ) AS areasqm, 
	SUM(CASE WHEN r.rputype = 'land' and rp.claimno is null THEN r.totalareaha ELSE 0.0 END ) AS areaha, 

	SUM( CASE WHEN r.rputype = 'land' THEN r.totalmv ELSE 0.0 END ) AS landmv,
	SUM( CASE WHEN r.rputype = 'bldg' AND r.totalmv <= 175000 THEN r.totalmv ELSE 0.0 END ) AS bldgmv175less,
	SUM( CASE WHEN r.rputype = 'bldg' AND r.totalmv > 175000 THEN r.totalmv ELSE 0.0 END ) AS bldgmvover175,
	SUM( CASE WHEN r.rputype = 'mach' THEN r.totalmv ELSE 0.0 END ) AS machmv,
	SUM( CASE WHEN rputype NOT IN( 'land', 'bldg', 'mach') THEN r.totalmv ELSE 0.0 END ) AS othermv, 
	SUM( r.totalmv ) AS totalmv,
	
	SUM( CASE WHEN r.rputype = 'land' THEN r.totalav ELSE 0.0 END ) AS landav,
	SUM( CASE WHEN r.rputype = 'bldg' AND r.totalmv <= 175000 THEN r.totalav ELSE 0.0 END ) AS bldgav175less,
	SUM( CASE WHEN r.rputype = 'bldg' AND r.totalmv > 175000 THEN r.totalav ELSE 0.0 END ) AS bldgavover175,
	SUM( CASE WHEN r.rputype = 'mach' THEN r.totalav ELSE 0.0 END ) AS machav,
	SUM( CASE WHEN rputype NOT IN( 'land', 'bldg', 'mach') THEN r.totalav ELSE 0.0 END ) AS otherav, 
	SUM( r.totalav ) AS totalav,
	
	SUM( CASE WHEN exists(select * from faas_restriction where parent_objid = f.objid and state = 'ACTIVE' and restrictiontype_objid = 'CARP' ) THEN r.totalav ELSE 0.0 END ) AS carpav,
	SUM( CASE WHEN exists(select * from faas_restriction where parent_objid = f.objid and state = 'ACTIVE' and restrictiontype_objid = 'UNDER_LITIGATION' ) THEN r.totalav ELSE 0.0 END ) AS litigationav,
	SUM( CASE WHEN exists(select * from faas_restriction where parent_objid = f.objid and state = 'ACTIVE' and restrictiontype_objid  NOT IN ('CARP', 'UNDER_LITIGATION')) THEN r.totalav ELSE 0.0 END ) AS otherrestrictionav,
	SUM( CASE WHEN exists(select * from faas_restriction where parent_objid = f.objid and state = 'ACTIVE') THEN r.totalav ELSE 0.0 END ) AS totalrestrictionav

FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN exemptiontype e ON r.exemptiontype_objid = e.objid 
WHERE r.taxable = 0
  AND (
		(f.dtapproved <= $P{enddate} AND f.state = 'CURRENT' ) OR 
		(f.canceldate > $P{enddate} AND f.state = 'CANCELLED' )
  )
  ${filter}
GROUP BY e.objid, e.name, e.orderno 
ORDER BY e.orderno  


