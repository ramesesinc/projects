[buildConsumptions]
INSERT INTO waterworks_consumption ( 
	objid, state, acctid, batchid, prevreading, reading, readingmethod, 
	reader_objid, reader_name, volume, amount, amtpaid, month, year, readingdate  
) 
SELECT 
	CONCAT(a.objid,'-',br.year,br.month) AS objid, 'DRAFT', a.objid, br.objid, 0, 0, 'PROCESSING', 
	br.reader_objid, br.reader_name, 0, 0.0, 0.0, br.month, br.year, br.readingdate   
FROM waterworks_batchreading br 
	INNER JOIN waterworks_stubout so ON so.zoneid = br.zoneid 
	INNER JOIN waterworks_account a ON a.stuboutid = so.objid  
	LEFT JOIN waterworks_consumption c ON (c.acctid = a.objid AND c.year = br.year AND c.month = br.month) 
WHERE br.objid = $P{batchid} 
	AND a.meterid IS NOT NULL 
	AND c.objid IS NULL 
