[getJAT]
SELECT 
	b.name AS barangay, f.dtapproved AS issuedate, f.tdno, r.fullpin, f.titleno,
	f.txntype_objid AS txntype, f.owner_name, f.administrator_name, r.rputype, pc.code AS classcode, 
	r.totalareaha, r.totalmv, r.totalav, f.state, 
	rp.ry, rp.section, rp.cadastrallotno
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE rp.barangayid = $P{barangayid} 
  AND rp.ry = $P{ry}
  AND f.dtapproved >= $P{startdate} AND f.dtapproved < $P{enddate}
  AND f.state IN ('CURRENT', 'CANCELLED')
  AND rp.section LIKE $P{section}
ORDER BY f.dtapproved, tdno 
