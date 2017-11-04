[getORF]  
SELECT
	b.name AS barangay, rp.cadastrallotno, pc.code AS classcode, r.fullpin, 
	f.prevtdno, e.name as taxpayer_address, e.address_text as taxpayer_name, f.tdno, 
	r.totalareasqm, r.totalareaha, r.totalav, f.txntype_objid AS txntype,
	rp.street, rp.purok, rp.blockno,
	f.titleno, f.administrator_name, f.administrator_address 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
	INNER JOIN entity e on f.taxpayer_objid = e.objid 
WHERE f.taxpayer_objid = $P{taxpayerid} 
  AND f.state = 'CURRENT'  
ORDER BY r.fullpin   
