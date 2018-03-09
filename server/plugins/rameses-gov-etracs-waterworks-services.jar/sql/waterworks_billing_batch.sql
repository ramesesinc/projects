[buildBillings]
INSERT INTO waterworks_billing ( 
	objid, state, acctid, batchid, 
	prevreadingdate, prevreading, readingdate, reading, readingmethod, 
	reader_objid, reader_name, volume, amount, month, year,
	discrate, surcharge, interest, othercharge, advance, unpaidamt, unpaidmonths 
) 
SELECT 
	CONCAT(a.objid,'-',br.year,br.month) AS objid, 'DRAFT', a.objid, br.objid, 
	a.lastdateread, a.currentreading, br.readingdate, 0, 'PROCESSING', 
	br.reader_objid, br.reader_name, 0, 0.0, br.month, br.year,
	0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0  
FROM waterworks_billing_batch br 
	INNER JOIN waterworks_account a ON a.zoneid = br.zoneid  
	LEFT JOIN waterworks_consumption c ON (c.acctid = a.objid AND c.year = br.year AND c.month = br.month) 
WHERE br.objid = $P{batchid} 
	AND a.meterid IS NOT NULL 
	AND c.objid IS NULL 
