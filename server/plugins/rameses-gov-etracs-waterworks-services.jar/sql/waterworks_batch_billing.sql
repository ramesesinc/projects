[buildBillings]
INSERT INTO waterworks_consumption ( 
	objid, state, acctid, batchid, 
	prevreadingdate, prevreading, readingdate, reading, readingmethod, 
	reader_objid, reader_name, volume, amount, amtpaid, month, year, 
	duedate, discdate, discrate, surcharge, interest, otherfees, credits, 
	arrears, unpaidmonths, billed, meterid 
) 
SELECT 
	CONCAT(a.objid,'-',br.year,br.month) AS objid, 'DRAFT' as state, a.objid as acctid, br.objid as batchid, 
	wm.lastreadingdate as prevreadingdate, (CASE WHEN wm.lastreading >= 0 THEN wm.lastreading ELSE 0 END) as prevreading, 
	br.readingdate, 0 as reading, 'PROCESSING' as readingmethod, br.reader_objid, br.reader_name, 
	0 as volume, 0.0 as amount, 0.0 as amtpaid, br.month, br.year, br.duedate, br.discdate, 
	0.0 as discrate, 0.0 as surcharge, 0.0 as interest, 0.0 as otherfees, 0.0 as credits, 
	0.0 as arrears, 0 as unpaidmonths, 0 as billed, a.meterid   
FROM waterworks_batch_billing br 
	INNER JOIN vw_waterworks_stubout_node wsn ON wsn.zone_objid = br.zoneid 
	INNER JOIN waterworks_account a ON (a.objid = wsn.acctid AND a.stuboutnodeid = wsn.objid) 
	LEFT JOIN waterworks_meter wm ON wm.objid = a.meterid 	
	LEFT JOIN waterworks_consumption c ON (c.acctid = a.objid AND c.year = br.year AND c.month = br.month) 
WHERE br.objid = $P{batchid} 
	AND c.objid IS NULL 

[findBilledStatus]
select tmp1.*, (totalcount-billedcount) as balance 
from ( 
	select 
		(select count(*) from waterworks_consumption where batchid = $P{batchid}) as totalcount, 
		(select count(*) from waterworks_consumption where batchid = $P{batchid} and billed=1) as billedcount  
)tmp1 


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
	waterworks_consumption wb, waterworks_batch_billing wbb 
SET 
	wm.lastreadingdate = wbb.readingdate, 
	wm.lastreading = wb.reading 
WHERE wbb.objid = $P{batchid} 
	and wb.batchid = wbb.objid 
	and wa.objid = wb.acctid 
	and wm.objid = wa.meterid 
