
#----------------------------------------------------------------------
#
# COMPARATIVE DATA ON AV
#
#----------------------------------------------------------------------
[getPreceedingComparativeAV]
SELECT
	'TAXABLE' AS taxability, 
	pc.objid AS classid, 
	pc.name AS classname, 
	pc.special AS special, 
	SUM( CASE WHEN r.rputype = 'land' THEN totalav ELSE 0.0 END ) AS preceedinglandav, 
	SUM( CASE WHEN r.rputype <> 'land' THEN totalav ELSE 0.0 END ) AS preceedingimpav, 
	SUM( r.totalav ) AS preceedingtotal 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp on f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
WHERE (
	(f.dtapproved < $P{startdate} AND f.state = 'CURRENT' ) OR 
	(f.dtapproved < $P{startdate} and f.canceldate >= $P{startdate} AND f.state = 'CANCELLED' )
	)
  AND r.taxable = 1 
  ${filter}
GROUP BY pc.objid, pc.name, pc.special , pc.orderno 
ORDER BY pc.orderno 


[getCurrentComparativeAV]
SELECT
	'TAXABLE' AS taxability, 
	pc.objid AS classid, 
	pc.name AS classname, 
	pc.special AS special, 
	SUM( CASE WHEN r.rputype = 'land' THEN totalav ELSE 0.0 END ) AS currentlandav, 
	SUM( CASE WHEN r.rputype <> 'land' THEN totalav ELSE 0.0 END ) AS currentimpav, 
	SUM( r.totalav ) AS currenttotal 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp on f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
WHERE (
	(f.dtapproved >= $P{startdate} and f.dtapproved < $P{enddate} AND f.state = 'CURRENT' ) OR 
	(f.dtapproved >= $P{startdate} and f.dtapproved < $P{enddate} AND f.canceldate >= $P{startdate} AND f.state = 'CANCELLED' )
)
AND r.taxable = 1 
${filter}
GROUP BY pc.objid, pc.name, pc.special , pc.orderno 
ORDER BY pc.orderno 


[getCancelledComparativeAV]
SELECT
	'TAXABLE' AS taxability, 
	pc.objid AS classid, 
	pc.name AS classname, 
	pc.special AS special, 
	SUM( CASE WHEN r.rputype = 'land' THEN totalav ELSE 0.0 END ) AS cancelledlandav, 
	SUM( CASE WHEN r.rputype <> 'land' THEN totalav ELSE 0.0 END ) AS cancelledimpav, 
	SUM( r.totalav ) AS cancelledtotal 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp on f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
WHERE f.state = 'CANCELLED'  
  AND r.taxable = 1 
  and f.canceldate >= $P{startdate} AND  f.canceldate < $P{enddate}
  ${filter}
GROUP BY pc.objid, pc.name, pc.special , pc.orderno 
ORDER BY pc.orderno 


[getEndingComparativeAV]
SELECT
	'TAXABLE' AS taxability, 
	pc.objid AS classid, 
	pc.name AS classname, 
	pc.special AS special, 
	SUM( CASE WHEN r.rputype = 'land' THEN totalav ELSE 0.0 END ) AS endinglandav, 
	SUM( CASE WHEN r.rputype <> 'land' THEN totalav ELSE 0.0 END ) AS endingimpav, 
	SUM( r.totalav ) AS endingtotal 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp on f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
WHERE (
	(f.dtapproved < $P{enddate} AND f.state = 'CURRENT' ) OR 
	(f.canceldate >= $P{enddate} AND f.state = 'CANCELLED' )
)
AND r.taxable = 1 
${filter}
GROUP BY pc.objid, pc.name, pc.special , pc.orderno 
ORDER BY pc.orderno 



[getPreceedingComparativeAVExempt]
SELECT 
	'EXEMPT' AS taxability,  
	e.objid AS classid,  
	e.name AS classname,  
	0 AS special,  
	SUM( CASE WHEN r.rputype = 'land' THEN r.totalav ELSE 0.0 END ) AS preceedinglandav,  
	SUM( CASE WHEN r.rputype <> 'land' THEN r.totalav ELSE 0.0 END ) AS preceedingimpav,  
	SUM( r.totalav ) AS preceedingtotal  
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp on f.realpropertyid = rp.objid 
	INNER JOIN exemptiontype e ON r.exemptiontype_objid = e.objid 
WHERE (
	(f.dtapproved < $P{startdate} AND f.state = 'CURRENT' ) OR 
	(f.dtapproved < $P{startdate} and f.canceldate >= $P{startdate} AND f.state = 'CANCELLED' )
)
AND r.taxable = 0 
${filter}
GROUP BY e.objid, e.name , e.orderno
ORDER BY e.orderno


[getCurrentComparativeAVExempt]
SELECT 
	'EXEMPT' AS taxability,  
	e.objid AS classid,  
	e.name AS classname,  
	0 AS special,  
	SUM( CASE WHEN r.rputype = 'land' THEN r.totalav ELSE 0.0 END ) AS currentlandav,  
	SUM( CASE WHEN r.rputype <> 'land' THEN r.totalav ELSE 0.0 END ) AS currentimpav,  
	SUM( r.totalav ) AS currenttotal  
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp on f.realpropertyid = rp.objid 
	INNER JOIN exemptiontype e ON r.exemptiontype_objid = e.objid 
WHERE (
	(f.dtapproved >= $P{startdate} and f.dtapproved < $P{enddate} AND f.state = 'CURRENT' ) OR 
	(f.dtapproved >= $P{startdate} and f.dtapproved < $P{enddate} AND f.canceldate >= $P{startdate} AND f.state = 'CANCELLED' )
)
AND r.taxable = 0 
${filter}
GROUP BY e.objid, e.name , e.orderno
ORDER BY e.orderno


[getCancelledComparativeAVExempt]
SELECT 
	'EXEMPT' AS taxability,  
	e.objid AS classid,  
	e.name AS classname,  
	0 AS special,  
	SUM( CASE WHEN r.rputype = 'land' THEN r.totalav ELSE 0.0 END ) AS cancelledlandav,  
	SUM( CASE WHEN r.rputype <> 'land' THEN r.totalav ELSE 0.0 END ) AS cancelledimpav,  
	SUM( r.totalav ) AS cancelledtotal  
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp on f.realpropertyid = rp.objid 
	INNER JOIN exemptiontype e ON r.exemptiontype_objid = e.objid 
WHERE f.state = 'CANCELLED'   
and f.canceldate >= $P{startdate} AND  f.canceldate < $P{enddate}
AND r.taxable = 0 
${filter}
GROUP BY e.objid, e.name , e.orderno
ORDER BY e.orderno


[getEndingComparativeAVExempt]
SELECT 
	'EXEMPT' AS taxability,  
	e.objid AS classid,  
	e.name AS classname,  
	0 AS special,  
	SUM( CASE WHEN r.rputype = 'land' THEN r.totalav ELSE 0.0 END ) AS endinglandav,  
	SUM( CASE WHEN r.rputype <> 'land' THEN r.totalav ELSE 0.0 END ) AS endingimpav,  
	SUM( r.totalav ) AS endingtotal  
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp on f.realpropertyid = rp.objid 
	INNER JOIN exemptiontype e ON r.exemptiontype_objid = e.objid 
WHERE (
	(f.dtapproved < $P{enddate} AND f.state = 'CURRENT' ) OR 
	(f.canceldate >= $P{enddate} AND f.state = 'CANCELLED' )
)
AND r.taxable = 0 
${filter}
GROUP BY e.objid, e.name , e.orderno
ORDER BY e.orderno

