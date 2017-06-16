

#----------------------------------------------------------------------
#
# COMPARATIVE DATA ON MV
#
#----------------------------------------------------------------------
[getStartComparativeMV]
SELECT
	'TAXABLE' AS taxability, 
	pc.objid AS classid, 
	pc.name AS classname, 
	pc.special AS special, 
	SUM( CASE WHEN r.rputype = 'land' THEN r.totalmv ELSE 0.0 END ) AS startlandmv, 
	SUM( CASE WHEN r.rputype <> 'land' THEN r.totalmv ELSE 0.0 END ) AS startimpmv, 
	SUM( r.totalmv ) AS starttotal  
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
GROUP BY pc.objid, pc.name, pc.special  , pc.orderno  
ORDER BY pc.orderno  


[getEndComparativeMV]
SELECT
	'TAXABLE' AS taxability, 
	pc.objid AS classid, 
	pc.name AS classname, 
	pc.special AS special, 
	SUM( CASE WHEN r.rputype = 'land' THEN r.totalmv ELSE 0.0 END ) AS endlandmv, 
	SUM( CASE WHEN r.rputype <> 'land' THEN r.totalmv ELSE 0.0 END ) AS endimpmv, 
	SUM( r.totalmv ) AS endtotal  
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


[getStartComparativeMVExempt]
SELECT 
	'EXEMPT' AS taxability,  
	e.objid AS classid,  
	e.name AS classname,  
	0 AS special,  
	SUM( CASE WHEN r.rputype = 'land' THEN r.totalmv ELSE 0.0 END ) AS startlandmv,  
	SUM( CASE WHEN r.rputype <> 'land' THEN r.totalmv ELSE 0.0 END ) AS startimpmv,  
	SUM( r.totalmv ) AS starttotal  
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
GROUP BY e.objid, e.name, e.orderno  
ORDER BY e.orderno  


[getEndComparativeMVExempt]
SELECT 
	'EXEMPT' AS taxability,  
	e.objid AS classid,  
	e.name AS classname,  
	0 AS special,  
	SUM( CASE WHEN r.rputype = 'land' THEN r.totalmv ELSE 0.0 END ) AS endlandmv,  
	SUM( CASE WHEN r.rputype <> 'land' THEN r.totalmv ELSE 0.0 END ) AS endimpmv,  
	SUM( r.totalmv ) AS endtotal  
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
GROUP BY e.objid, e.name, e.orderno  
ORDER BY e.orderno  



