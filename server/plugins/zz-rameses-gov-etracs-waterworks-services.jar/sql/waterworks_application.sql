[assignMeter]
UPDATE waterworks_application SET 
	meterid = $P{meterid}, 
	initialreading = $P{initialreading}, 
	installer_name = $P{installername}, 
	installer_objid = $P{installerid}, 
	dtinstalled = $P{dtinstalled} 
WHERE 
	objid=$P{objid} 
