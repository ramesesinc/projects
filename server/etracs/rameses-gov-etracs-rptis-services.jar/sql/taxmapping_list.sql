[getBarangayLastParcelInfo]
SELECT 
	r.ry, 
	r.section,
	MAX(r.parcel) AS lastparcel
FROM faas f
	INNER JOIN realproperty r ON f.realpropertyid = r.objid 
	INNER JOIN barangay b ON r.barangayid = b.objid  
WHERE r.barangayid = $P{objid}
  AND r.ry = $P{ry} 
  AND f.state <> 'CANCELLED'
GROUP BY r.ry, r.section	
ORDER BY r.section, r.ry


[getRevisionYears]
SELECT ry FROM landrysetting ORDER BY ry 