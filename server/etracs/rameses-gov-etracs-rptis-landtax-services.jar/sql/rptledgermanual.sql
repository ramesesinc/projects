[getList]
SELECT rl.*, e.name AS taxpayer_name, e.address_text AS taxpayer_address 
FROM rptledger rl 
	INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
WHERE faasid IS NULL 
${filters}	


[findById]
SELECT 
	rl.*,
  	e.name AS taxpayer_name,
  	e.address_text AS taxpayer_address,
	b.name AS barangay_name
FROM rptledger rl 
	INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
	INNER JOIN barangay b ON rl.barangayid = b.objid 
WHERE rl.objid = $P{objid}
ORDER BY rl.state, rl.tdno


[findSubLedgerById]
SELECT sl.*, p.fullpin AS parent_fullpin, p.tdno AS parent_tdno
FROM rptledger_subledger sl 
	INNER JOIN rptledger p ON sl.parent_objid = p.objid 
WHERE sl.objid = $P{objid}

