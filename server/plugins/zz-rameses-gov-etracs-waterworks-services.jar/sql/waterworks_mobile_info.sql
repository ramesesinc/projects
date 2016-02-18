[insertForDownload]
INSERT INTO waterworks_mobile_info
SELECT wa.objid, $P{batchid}, 'PENDING', $P{penalty}, $P{othercharge}, $P{duemonth}, 
$P{dueyear}, $P{duedate}, $P{fromdate}, $P{todate}, $P{discodate}, $P{rundate},""
FROM waterworks_account wa
WHERE wa.objid NOT IN (SELECT objid FROM waterworks_mobile_info)
AND wa.areaid  IN (${areaids})
AND lastreadingyear = $P{year}
AND lastreadingmonth = $P{month}

[getListForDownload]
SELECT 
wa.objid, wa.acctno, wa.acctname, wa.address_text AS address, wa.mobileno, wa.phoneno, wa.email, wm.serialno, wa.areaid,
war.name AS areaname, wa.classificationid, wa.lastreadingyear, wa.lastreadingmonth, wa.lastreadingdate, wa.lastreading, 
wa.prevreading,wa.balance, wd.penalty, wd.othercharge, wd.year, wd.month, wd.duedate, wd.fromdate, wd.todate, wd.discodate,
wd.rundate
FROM waterworks_account wa
INNER JOIN waterworks_meter wm ON wa.meterid = wm.objid
INNER JOIN waterworks_area war ON wa.areaid = war.objid
INNER JOIN waterworks_mobile_info wd ON wd.objid = wa.objid
WHERE wd.batchid = $P{batchid}