[getORF]  
SELECT
	b.name AS barangay, rp.cadastrallotno, pc.code AS classcode, r.fullpin, 
	f.prevtdno, f.taxpayer_address, f.taxpayer_name, f.tdno, 
	r.totalareasqm, r.totalareaha, r.totalav, f.txntype_objid AS txntype
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE f.taxpayer_objid = $P{taxpayerid} 
  AND f.state = 'CURRENT'  
ORDER BY r.fullpin   
