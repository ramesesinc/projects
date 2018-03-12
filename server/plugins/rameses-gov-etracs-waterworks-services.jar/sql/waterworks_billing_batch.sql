[buildBillings]
INSERT INTO waterworks_billing ( 
	objid, state, acctid, batchid, 
	prevreadingdate, prevreading, readingdate, reading, readingmethod, 
	reader_objid, reader_name, volume, amount, month, year,
	discrate, surcharge, interest, otherfees, advance, 
	arrears, unpaidmonths, billed 
) 
SELECT 
	CONCAT(a.objid,'-',br.year,br.month) AS objid, 'DRAFT', a.objid, br.objid, 
	a.lastdateread, a.currentreading, br.readingdate, 0, 'PROCESSING', 
	br.reader_objid, br.reader_name, 0, 0.0, br.month, br.year,
	0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0  
FROM waterworks_billing_batch br 
	INNER JOIN vw_waterworks_stubout_node wsn ON wsn.zone_objid = br.zoneid 
	INNER JOIN waterworks_account a ON a.objid = wsn.acctid 
	LEFT JOIN waterworks_consumption c ON (c.acctid = a.objid AND c.year = br.year AND c.month = br.month) 
WHERE br.objid = $P{batchid} 
	AND a.meterid IS NOT NULL 
	AND c.objid IS NULL 

[findBilledStatus]
select tmp1.*, (totalcount-billedcount) as balance 
from ( 
	select 
		(select count(*) from waterworks_billing where batchid = $P{batchid}) as totalcount, 
		(select count(*) from waterworks_billing where batchid = $P{batchid} and billed=1) as billedcount  
)tmp1 


[postConsumption]
INSERT INTO waterworks_consumption ( 
	objid, acctid, batchid, duedate, discdate, 
	prevreading, reading, readingmethod, reader_objid, reader_name, 
	volume, amount, amtpaid, MONTH, YEAR, readingdate, state 
) 
SELECT 
	wb.objid, wb.acctid, wb.batchid, bb.duedate, bb.discdate, 
	wb.prevreading, wb.reading, wb.readingmethod, wb.reader_objid, wb.reader_name, 
	wb.volume, wb.amount, 0.0 AS amtpaid, wb.month, wb.year, bb.readingdate, 'POSTED' AS state 
FROM waterworks_billing_batch bb 
	INNER JOIN waterworks_billing wb ON wb.batchid = bb.objid 
WHERE bb.objid = $P{batchid} 

[updateAccountReading]
UPDATE waterworks_account, waterworks_billing wb, waterworks_billing_batch wbb 
SET lastdateread=wbb.readingdate, currentreading=wb.reading
WHERE objid = wb.acctid AND wb.batchid = wbb.objid AND wbb.objid = $P{batchid}