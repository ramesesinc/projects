[getListForDownload]
select 
	acct.objid, acct.acctno, acct.acctname, acct.address_text as address, acct.mobileno,
	wm.serialno, wm.sizeid as metersize, wa.objid as areaid, acct.lastreading, 
	acct.lasttxndate, wa.title as areaname, acct.classificationid, acct.lastreadingyear,
	acct.lastreadingmonth, acct.lastreadingdate, acct.address_barangay_objid as barangayid,
	acct.stuboutid, acct.stuboutindex as sortorder 
from waterworks_area_zone waz 
	inner join waterworks_area wa on wa.objid=waz.areaid 
	inner join waterworks_sector_zone wsz on (wsz.objid=waz.zoneid and wsz.sectorid=wa.sectorid)
	inner join waterworks_sector ws on ws.objid=wsz.sectorid 
	inner join waterworks_stubout wst on wst.zoneid=wsz.objid 
	inner join waterworks_account acct on acct.stuboutid=wst.objid 
	inner join waterworks_meter wm on acct.meterid=wm.objid 
where waz.areaid=$P{areaid} 
	and acct.objid not in ( 
		select objid from waterworks_mobile_info 
		where objid=acct.objid 
	) and ( 
		acct.lastreadingdate is null or 
		YEAR(acct.lastreadingdate) <= $P{year} and 
		MONTH(acct.lastreadingdate) < $P{month} 
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
	