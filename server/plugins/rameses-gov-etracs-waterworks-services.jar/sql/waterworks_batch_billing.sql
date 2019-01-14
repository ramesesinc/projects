[buildConsumptions]
INSERT INTO waterworks_consumption ( 
	objid,state,acctid,batchid,txnmode,prevreading,reading,
	volume,rate,amount,amtpaid,meterid,scheduleid, hold 
) 
SELECT 
	CONCAT(a.objid,'-',br.scheduleid) AS objid, 
	'DRAFT' as state, 
	a.objid as acctid, 
	br.objid as batchid, 
	'ONLINE' as txnmode,
	(CASE WHEN wm.lastreading >= 0 THEN wm.lastreading ELSE 0 END) AS prevreading,
	(CASE WHEN wm.lastreading >= 0 THEN wm.lastreading ELSE 0 END) AS reading,  
	0 AS volume, 0 AS rate, 0 AS amount, 0 AS amtpaid, a.meterid, br.scheduleid, 0 	
FROM waterworks_batch_billing br 
	INNER JOIN vw_waterworks_stubout_node wsn ON wsn.zone_objid = br.zoneid 
	INNER JOIN waterworks_account a ON (a.objid = wsn.currentacctid AND a.stuboutnodeid = wsn.objid) 
	LEFT JOIN waterworks_meter wm ON wm.objid = a.meterid 	
	LEFT JOIN waterworks_consumption c ON (c.acctid = a.objid AND c.scheduleid = br.scheduleid) 
WHERE br.objid = $P{batchid}
	AND c.objid IS NULL

[findBilledStatus]
select tmp1.*, (totalcount-billedcount) as balance 
from ( 
	select 
		(select count(*) from waterworks_billing where batchid = $P{batchid} ) as totalcount, 
		(select count(*) from waterworks_billing where batchid = $P{batchid} and billed=1) as billedcount  
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


[findLastBillByZone]
select 
	bb.objid, bb.scheduleid, bs.year, bs.month, 
	bb.zoneid, z.schedule_objid as zonescheduleid 
from waterworks_batch_billing bb 
	inner join waterworks_billing_schedule bs on bs.objid = bb.scheduleid 
	inner join waterworks_zone z on z.objid = bb.zoneid 
where bb.zoneid = $P{zoneid} ${filter} 
order by bs.year desc, bs.month desc 


[findCurrentConsumption]
select c.*, bs.year, bs.month 
from waterworks_consumption c 
	inner join waterworks_billing_schedule bs on bs.objid = c.scheduleid 
where c.acctid = $P{acctid} 
	and c.state in ('POSTED', 'COMPLETED')
order by bs.year desc, bs.month desc 
