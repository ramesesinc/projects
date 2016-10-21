[getRDAPRPTA100]
SELECT 
	b.pin,
	b.name AS barangay,
	b.indexno, 
	SUM( CASE WHEN r.taxable = 1 THEN 1.0 ELSE 0.0 END ) AS landtaxablecount,
	SUM( CASE WHEN r.taxable = 0 THEN 1.0 ELSE 0.0 END ) AS landexemptcount,
	SUM( CASE WHEN r.taxable = 1 THEN r.totalareaha ELSE 0.0 END ) AS landareataxable,
	SUM( CASE WHEN r.taxable = 0 THEN r.totalareaha ELSE 0.0 END ) AS landareaexempt,
	SUM( r.totalareaha ) AS landareatotal,
	SUM( CASE WHEN r.taxable = 1 THEN 1 ELSE 0.0 END ) AS tdtaxablecount,
	SUM( CASE WHEN r.taxable = 0 THEN 1 ELSE 0.0 END ) AS tdexemptcount,
	SUM( CASE WHEN f.objid IS NULL THEN 0 ELSE 1 END ) AS tdcount,
	SUM( CASE WHEN r.rputype = 'land' THEN r.totalav ELSE 0.0 END ) AS landavtotal,
	SUM( CASE WHEN r.rputype <> 'land' THEN r.totalav ELSE 0.0 END ) AS improvavtotal,
	SUM( r.totalav ) AS avtotal,
	SUM( CASE WHEN r.taxable = 1 THEN r.totalav ELSE 0.0 END ) AS avtaxable,
	SUM( CASE WHEN r.taxable = 0 THEN r.totalav ELSE 0.0 END ) AS avexempt
FROM faas f 
	INNER JOIN realproperty rp ON rp.objid = f.realpropertyid
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid
WHERE ${filter}
  AND f.state = 'CURRENT'
GROUP BY b.pin, b.indexno, b.name 
ORDER BY b.pin 


  