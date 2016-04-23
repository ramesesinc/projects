[getListForDownload]
SELECT 
	acct.objid, acct.acctno, acct.acctname, acct.address_text AS address,
	wm.serialno, wm.sizeid AS metersize, s.objid AS sectorid, s.code AS sectorcode, 
	acct.classificationid, acct.address_barangay_objid AS barangayid,
	acct.fromperiod, acct.toperiod, acct.duedate, acct.disconnectiondate,
	acct.stuboutid, acct.stuboutindex AS sortorder, acct.currentreading as lastreading
FROM waterworks_account acct 
	INNER JOIN waterworks_meter wm ON acct.meterid = wm.objid
	INNER JOIN waterworks_stubout ws ON acct.stuboutid = ws.objid
	INNER JOIN waterworks_sector_zone wsz ON ws.zoneid = wsz.objid 
	INNER JOIN waterworks_sector_reader wsr ON wsz.readerid = wsr.objid
	INNER JOIN waterworks_sector s ON wsr.sectorid = s.objid
WHERE s.objid = $P{sectorid}
AND CURDATE() >= acct.readingdate

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

[getSectorByUser]
SELECT
s.*
FROM waterworks_sector s
	INNER JOIN waterworks_sector_reader sr ON s.objid = sr.sectorid 
WHERE sr.assignee_objid = $P{userid}

[getStuboutsBySector]
SELECT
s.*
FROM waterworks_stubout s 
	INNER JOIN waterworks_sector_zone sz ON s.zoneid = sz.objid
	INNER JOIN waterworks_sector_reader sr ON sz.readerid = sr.objid 
WHERE sz.sectorid = $P{sectorid}
AND sr.assignee_objid = $P{userid}

[getZoneBySector]
SELECT
z.*
FROM waterworks_sector_zone z 
	INNER JOIN waterworks_sector_reader r ON z.readerid = r.objid 
WHERE z.sectorid = $P{sectorid}
AND r.assignee_objid = $P{userid}