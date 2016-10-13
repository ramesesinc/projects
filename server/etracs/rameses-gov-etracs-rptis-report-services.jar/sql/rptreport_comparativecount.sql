
#----------------------------------------------------------------------
#
# COMPARATIVE DATA ON NUMBER OF RPU
#
#----------------------------------------------------------------------
[getPreceedingComparativeRpuCount]
SELECT
	'TAXABLE' AS taxability, 
	pc.objid AS classid, 
	pc.name AS classname, 
	pc.special AS special, 
	SUM( CASE WHEN r.rputype = 'land' THEN 1.0 ELSE 0.0 END ) AS preceedinglandcount, 
	SUM( CASE WHEN r.rputype <> 'land' THEN 1.0 ELSE 0.0 END ) AS preceedingimpcount, 
	SUM( 1.0 ) AS preceedingtotal 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
WHERE ${filter}
  AND f.state = 'CURRENT'  
  AND r.taxable = 1 
GROUP BY pc.objid, pc.name, pc.special , pc.orderno 
ORDER BY pc.orderno 


[getNewDiscoveryComparativeRpuCount]
SELECT
	'TAXABLE' AS taxability, 
	pc.objid AS classid, 
	pc.name AS classname, 
	pc.special AS special, 
	SUM( CASE WHEN r.rputype = 'land' THEN 1.0 ELSE 0.0 END ) AS newdiscoverylandcount, 
	SUM( CASE WHEN r.rputype <> 'land' THEN 1.0 ELSE 0.0 END ) AS newdiscoveryimpcount, 
	SUM( 1.0 ) AS newdiscoverytotal 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
WHERE ${filter}
  AND f.state = 'CURRENT'  
  AND r.taxable = 1 
GROUP BY pc.objid, pc.name, pc.special , pc.orderno 
ORDER BY pc.orderno 


[getCancelledComparativeRpuCount]
SELECT
	'TAXABLE' AS taxability, 
	pc.objid AS classid, 
	pc.name AS classname, 
	pc.special AS special, 
	SUM( CASE WHEN r.rputype = 'land' THEN 1.0 ELSE 0.0 END ) AS cancelledlandcount, 
	SUM( CASE WHEN r.rputype <> 'land' THEN 1.0 ELSE 0.0 END ) AS cancelledimpcount, 
	SUM( 1.0 ) AS cancelledtotal 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
WHERE ${filter}
  AND f.state = 'CANCELLED'  
  AND r.taxable = 1 
GROUP BY pc.objid, pc.name, pc.special , pc.orderno 
ORDER BY pc.orderno 


[getEndingComparativeRpuCount]
SELECT
	'TAXABLE' AS taxability, 
	pc.objid AS classid, 
	pc.name AS classname, 
	pc.special AS special, 
	SUM( CASE WHEN r.rputype = 'land' THEN 1.0 ELSE 0.0 END ) AS endinglandcount, 
	SUM( CASE WHEN r.rputype <> 'land' THEN 1.0 ELSE 0.0 END ) AS endingimpcount, 
	SUM( 1.0 ) AS endingtotal 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
WHERE ${filter}
  AND f.state = 'CURRENT'  
  AND r.taxable = 1 
GROUP BY pc.objid, pc.name, pc.special , pc.orderno 
ORDER BY pc.orderno 



[getPreceedingComparativeRpuCountExempt]
SELECT 
	'EXEMPT' AS taxability,  
	e.objid AS classid,  
	e.name AS classname,  
	0 AS special,  
	SUM( CASE WHEN r.rputype = 'land' THEN 1.0 ELSE 0.0 END ) AS preceedinglandcount,  
	SUM( CASE WHEN r.rputype <> 'land' THEN 1.0 ELSE 0.0 END ) AS preceedingimpcount,  
	SUM( 1.0 ) AS preceedingtotal     
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN exemptiontype e ON r.exemptiontype_objid = e.objid   
WHERE ${filter}
  AND f.state = 'CURRENT'  
  AND r.taxable = 0
GROUP BY e.objid, e.name , e.orderno  
ORDER BY e.orderno  


[getNewDiscoveryComparativeRpuCountExempt]
SELECT 
	'EXEMPT' AS taxability,  
	e.objid AS classid,  
	e.name AS classname,  
	0 AS special,  
	SUM( CASE WHEN r.rputype = 'land' THEN 1.0 ELSE 0.0 END ) AS newdiscoverylandcount,  
	SUM( CASE WHEN r.rputype <> 'land' THEN 1.0 ELSE 0.0 END ) AS newdiscoveryimpcount,  
	SUM( 1.0 ) AS newdiscoverytotal     
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN exemptiontype e ON r.exemptiontype_objid = e.objid   
WHERE ${filter}
  AND f.state = 'CURRENT'  
  AND r.taxable = 0
GROUP BY e.objid, e.name , e.orderno  
ORDER BY e.orderno  


[getCancelledComparativeRpuCountExempt]
SELECT 
	'EXEMPT' AS taxability,  
	e.objid AS classid,  
	e.name AS classname,  
	0 AS special,  
	SUM( CASE WHEN r.rputype = 'land' THEN 1.0 ELSE 0.0 END ) AS cancelledlandcount,  
	SUM( CASE WHEN r.rputype <> 'land' THEN 1.0 ELSE 0.0 END ) AS cancelledimpcount,  
	SUM( 1.0 ) AS cancelledtotal     
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN exemptiontype e ON r.exemptiontype_objid = e.objid   
WHERE ${filter}
  AND f.state = 'CANCELLED'  
  AND r.taxable = 0
GROUP BY e.objid, e.name , e.orderno  
ORDER BY e.orderno  


[getEndingComparativeRpuCountExempt]
SELECT 
	'EXEMPT' AS taxability,  
	e.objid AS classid,  
	e.name AS classname,  
	0 AS special,  
	SUM( CASE WHEN r.rputype = 'land' THEN 1.0 ELSE 0.0 END ) AS endinglandcount,  
	SUM( CASE WHEN r.rputype <> 'land' THEN 1.0 ELSE 0.0 END ) AS endingimpcount,  
	SUM( 1.0 ) AS endingtotal     
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN exemptiontype e ON r.exemptiontype_objid = e.objid   
WHERE ${filter}
  AND f.state = 'CURRENT'  
  AND r.taxable = 0
GROUP BY e.objid, e.name , e.orderno  
ORDER BY e.orderno  

