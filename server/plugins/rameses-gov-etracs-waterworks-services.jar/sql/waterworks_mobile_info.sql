[getListForDownload]
SELECT 
	acct.objid, acct.acctno, acct.acctname, acct.address_text AS address,
	wm.serialno, wm.sizeid AS metersize, s.objid AS sectorid, s.code AS sectorcode, 
	acct.classificationid, acct.address_barangay_objid AS barangayid,
	wbc.fromperiod, wbc.toperiod, wbc.duedate, wbc.disconnectiondate,
	wsn.stuboutid, wsn.indexno AS sortorder, acct.currentreading as lastreading
FROM waterworks_account acct 
	INNER JOIN waterworks_billing_cycle wbc on acct.billingcycleid=wbc.objid  
	INNER JOIN waterworks_meter wm ON acct.meterid = wm.objid
	INNER JOIN waterworks_sector s ON acct.sectorid = s.objid 
	INNER JOIN waterworks_sector_zone wsz ON acct.zoneid = wsz.objid 
	INNER JOIN waterworks_sector_reader wsr ON wsz.readerid = wsr.objid
	INNER JOIN waterworks_stubout ws ON acct.stuboutid = ws.objid 
	INNER JOIN waterworks_stubout_node wsn on acct.stuboutnodeid = wsn.objid 
WHERE acct.sectorid = $P{sectorid} 
	AND ( acct.billingcycleid IS NULL OR acct.billingcycleid < $P{billingcycleid}) 
	AND wsr.assignee_objid = $P{assigneeid} 
	AND acct.objid NOT IN (  
		SELECT objid FROM waterworks_mobile_info 
		WHERE objid=acct.objid 
	) 
