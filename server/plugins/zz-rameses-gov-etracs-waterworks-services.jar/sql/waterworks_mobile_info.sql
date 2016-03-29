[getDownloadableAccount]
SELECT 
wa.objid, wa.acctno, wa.acctname, wa.address_text as address, wa.mobileno,
wm.serialno, wm.sizeid as metersize, wr.objid as areaid, wa.lastreading, 
wa.lasttxndate, wr.title as areaname, wa.classificationid, wa.lastreadingyear,
wa.lastreadingmonth, wa.lastreadingdate, wa.address_barangay_objid as barangayid
FROM waterworks_account wa
INNER JOIN waterworks_meter wm ON wa.meterid = wm.objid
INNER JOIN waterworks_stubout_account wsa ON wsa.acctid = wa.objid
INNER JOIN waterworks_stubout ws on ws.objid = wsa.parentid
INNER JOIN waterworks_area wr on wr.objid = ws.areaid
WHERE wa.objid NOT IN (SELECT objid FROM waterworks_mobile_info)
AND wr.assignee_objid = $P{assigneeid}
AND wr.objid IN ${groupids}

[cancelDownload]
DELETE FROM waterworks_mobile_info
WHERE batchid = $P{batchid}
AND objid NOT IN ${downloadedlist}