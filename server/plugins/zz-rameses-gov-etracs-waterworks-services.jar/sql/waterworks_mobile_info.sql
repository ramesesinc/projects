[insertForDownload]
INSERT INTO waterworks_mobile_info
SELECT wa.objid, $P{batchid}, 'PENDING' FROM waterworks_account wa
WHERE wa.objid NOT IN (SELECT objid FROM waterworks_mobile_info)
AND wa.areaid IN (${areaids})
AND lastreadingyear = $P{year}
AND lastreadingmonth = $P{month}

[findBatchCount]
SELECT count(*) as batchcount
FROM waterworks_mobile_info
WHERE batchid = $P{batchid}

[getListForDownload]
SELECT 
wa.objid, wa.acctno, wa.acctname, wa.address_text AS address, wa.mobileno, wa.phoneno, wa.email, wm.serialno, wa.areaid,
war.name AS areaname, wa.classificationid, wa.lastreadingyear, wa.lastreadingmonth, wa.lastreadingdate, wa.lastreading, wa.prevreading,
wa.balance
FROM waterworks_account wa
INNER JOIN waterworks_meter wm ON wa.meterid = wm.objid
INNER JOIN waterworks_area war ON wa.areaid = war.objid
INNER JOIN waterworks_mobile_info wd ON wd.objid = wa.objid
WHERE wd.batchid = $P{batchid}

[confirmDownload]
UPDATE waterworks_mobile_info 
SET state = 'DOWNLOADED'
WHERE batchid = $P{batchid}

[deleteDownload]
DELETE FROM waterworks_mobile_info
WHERE batchid = $P{batchid}
AND objid = $P{acctid}
AND state = 'DOWNLOADED'