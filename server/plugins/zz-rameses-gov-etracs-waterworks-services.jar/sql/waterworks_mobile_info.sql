[getListForDownload]
SELECT 
	wa.objid, wa.acctno, wa.acctname, wa.address_text as address, wa.mobileno,
	wm.serialno, wm.sizeid as metersize, wr.objid as areaid, wa.lastreading, 
	wa.lasttxndate, wr.title as areaname, wa.classificationid, wa.lastreadingyear,
	wa.lastreadingmonth, wa.lastreadingdate, wa.address_barangay_objid as barangayid,
	wsa.parentid as stuboutid, wsa.sortorder
FROM waterworks_account wa
	INNER JOIN waterworks_meter wm ON wa.meterid = wm.objid
	INNER JOIN waterworks_stubout_account wsa ON wsa.acctid = wa.objid
	INNER JOIN waterworks_stubout ws on ws.objid = wsa.parentid
	INNER JOIN waterworks_area wr on wr.objid = ws.areaid
WHERE wa.objid NOT IN (SELECT objid FROM waterworks_mobile_info)
	AND wr.objid = $P{areaid} 
	AND (
		wa.lastreadingdate IS NULL OR 
		YEAR(wa.lastreadingdate) <= $P{year} AND 
		MONTH(wa.lastreadingdate) < $P{month} 
	) 

[getListForDownload2]
SELECT  
	wa.objid, wa.acctno, wa.acctname, wa.address_text as address, wa.mobileno,
	wm.serialno, wm.sizeid as metersize, wr.objid as areaid, wa.lastreading, 
	wa.lasttxndate, wr.title as areaname, wa.classificationid, wa.lastreadingyear,
	wa.lastreadingmonth, wa.lastreadingdate, wa.address_barangay_objid as barangayid,
	wsoa.sortorder, wso.objid AS stuboutid, 
	wso.title AS stubout_title, wso.zoneid AS stubout_zone 
FROM waterworks_account wa 
	INNER JOIN waterworks_meter wm ON wa.meterid = wm.objid
	INNER JOIN waterworks_stubout_account wsoa ON wa.objid=wsoa.acctid
	INNER JOIN waterworks_stubout wso ON wso.objid=wsoa.parentid
	INNER JOIN waterworks_area_zone waz ON wso.zoneid=waz.zoneid
	INNER JOIN waterworks_area wr ON wso.zoneid=waz.zoneid
WHERE wa.objid NOT IN (SELECT objid FROM waterworks_mobile_info)
	AND waz.areaid = $P{areaid}
	AND (
		wa.lastreadingdate IS NULL OR 
		YEAR(wa.lastreadingdate) <= $P{year} AND 
		MONTH(wa.lastreadingdate) < $P{month} 
	) 


[cancelDownload]
DELETE FROM waterworks_mobile_info
WHERE batchid = $P{batchid}
AND objid NOT IN ${downloadedlist}

[findConsumptionAccount]
SELECT ia.* 
FROM collectiontype ct, collectiontype_account cta, itemaccount ia 
WHERE ct.handler='waterworks' and cta.tag='WFEE' 
	AND ct.objid=cta.collectiontypeid 
	AND cta.account_objid=ia.objid 
	