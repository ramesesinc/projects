[buildBillings]
INSERT INTO waterworks_billing ( 
	objid, state, acctid, batchid, 
	prevreadingdate, prevreading, readingdate, reading, readingmethod, 
	reader_objid, reader_name, volume, amount, month, year,
	discrate, surcharge, interest, otherfees, credits, 
	arrears, unpaidmonths, billed 
) 
SELECT 
	CONCAT(a.objid,'-',br.year,br.month) AS objid, 'DRAFT', a.objid, br.objid, 
	wm.lastreadingdate, CASE WHEN wm.lastreading >= 0 THEN wm.lastreading ELSE 0 END, 
	br.readingdate, 0, 'PROCESSING', br.reader_objid, br.reader_name, 0, 0.0, br.month, br.year,
	0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0  
FROM waterworks_batch_billing br 
	INNER JOIN vw_waterworks_stubout_node wsn ON wsn.zone_objid = br.zoneid 
	INNER JOIN waterworks_account a ON a.stuboutnodeid = wsn.objid 
	LEFT JOIN waterworks_meter wm ON wm.objid = a.meterid 	
	LEFT JOIN waterworks_consumption c ON (c.acctid = a.objid AND c.year = br.year AND c.month = br.month) 
WHERE br.objid = $P{batchid} 
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
	objid, acctid, batchid, meterid, duedate, discdate, 
	prevreading, reading, readingmethod, reader_objid, reader_name, 
	volume, amount, amtpaid, MONTH, YEAR, readingdate, state 
) 
SELECT 
	wb.objid, wb.acctid, wb.batchid, wa.meterid, bb.duedate, bb.discdate, 
	wb.prevreading, wb.reading, wb.readingmethod, wb.reader_objid, wb.reader_name, 
	wb.volume, wb.amount, 0.0 AS amtpaid, wb.month, wb.year, bb.readingdate, 'POSTED' AS state 
FROM waterworks_batch_billing bb 
	INNER JOIN waterworks_billing wb ON wb.batchid = bb.objid 
	INNER JOIN waterworks_account wa on wa.objid = wb.acctid 
WHERE bb.objid = $P{batchid} 

[findAverageConsumption]
SELECT AVG(a.volume)  AS avgcon
FROM
( SELECT volume FROM waterworks_consumption 
WHERE  acctid = $P{acctid}
AND ((year*12)+month) < (($P{year}*12)+$P{month})
ORDER BY ((year*12)+month) DESC
LIMIT $P{months} ) a


[postMeterReading]
UPDATE 
	waterworks_meter wm, waterworks_account wa, 
	waterworks_billing wb, waterworks_batch_billing wbb 
SET 
	wm.lastreadingdate = wbb.readingdate, 
	wm.lastreading = wb.reading 
WHERE wbb.objid = $P{batchid} 
	and wb.batchid = wbb.objid 
	and wa.objid = wb.acctid 
	and wm.objid = wa.meterid 
