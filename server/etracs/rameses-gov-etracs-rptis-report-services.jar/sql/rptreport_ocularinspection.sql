[findOcularInspectionInfo]
SELECT 
	ef.*,
	f.owner_name, f.titleno, f.fullpin,
	rp.cadastrallotno, r.totalareaha,
	rp.barangayid, b.parentid as barangay_parentid, rp.purok, rp.street
FROM examiner_finding ef 
	INNER JOIN faas f ON ef.parent_objid = f.objid 
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE ef.objid = $P{objid}