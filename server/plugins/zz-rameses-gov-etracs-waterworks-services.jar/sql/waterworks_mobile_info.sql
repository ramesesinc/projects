[insertForDownload]
INSERT INTO waterworks_mobile_info
SELECT wa.objid, $P{batchid}, 'PENDING', $P{penalty}, $P{othercharge}, $P{duemonth}, 
$P{dueyear}, $P{duedate}, $P{fromdate}, $P{todate}, $P{discodate}, $P{rundate},""
FROM waterworks_account wa
INNER JOIN waterworks_stubout_account wsa ON wsa.acctid = wa.objid
INNER JOIN waterworks_stubout ws on ws.objid = wsa.parentid
INNER JOIN waterworks_readinggroup wrg on wrg.objid = ws.readinggroup_objid
WHERE wa.objid NOT IN (SELECT objid FROM waterworks_mobile_info)
AND wrg.assignee_objid = $P{assigneeid}
AND wrg.objid IN ${groupids} 

[getListForDownload]
SELECT 
wa.objid, wa.acctno, wa.acctname, wa.address_text AS address, wa.mobileno, wa.phoneno, wa.email, 
wm.serialno, wa.classificationid, wa.lastreadingyear, wa.lastreadingmonth, wa.lastreadingdate, 
wa.lastreading, wa.prevreading,wa.balance, wd.penalty, wd.othercharge, wd.year, wd.month, wd.duedate,
wd.fromdate, wd.todate, wd.discodate, wd.rundate, wa.address_barangay_objid AS barangay, wm.sizeid AS metersize
FROM waterworks_account wa
INNER JOIN waterworks_meter wm ON wa.meterid = wm.objid
INNER JOIN waterworks_mobile_info wd ON wd.objid = wa.objid
WHERE wd.batchid = $P{batchid}

[cancelDownload]
DELETE FROM waterworks_mobile_info
WHERE batchid = $P{batchid}
AND objid NOT IN ${downloadedlist}