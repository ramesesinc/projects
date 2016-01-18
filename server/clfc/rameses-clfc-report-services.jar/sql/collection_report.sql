[getListByStartdateAndEnddate]
SELECT r.*, CONCAT(lr.description, " - ", lr.area) AS description
FROM collection_remittance r
LEFT JOIN loan_route lr ON r.group_objid = lr.code
WHERE r.txndate BETWEEN $P{startdate} AND $P{enddate}

[getReportData]
SELECT r.*, 
	CASE 
		WHEN r.group_type = 'route' THEN CONCAT(lr.description, " - ", lr.area)
		WHEN r.group_type = 'online' THEN 'DIRECT'
		ELSE r.group_type
 	END AS description
FROM collection_remittance r
LEFT JOIN loan_route lr ON r.group_objid = lr.code
${filter}

[findTotalAccountsCollected]
SELECT COUNT(DISTINCT d.loanapp_objid) AS `count`
FROM collection_remittance_detail d
WHERE d.parentid = $P{objid}

[findTotalAmountCollected]
SELECT SUM(d.amount) AS totalamount
FROM collection_remittance_detail d
WHERE d.parentid = $P{objid}

[findToBeCollectedForRouteType]
SELECT COUNT(DISTINCT d.ledgerid) AS `count`, SUM(d.dailydue) AS totalamount
FROM collection_remittance r
INNER JOIN ledger_billing_item i ON r.collection_objid = i.parentid AND r.group_objid = i.item_objid
INNER JOIN ledger_billing_detail d ON i.objid = d.parentid
WHERE r.objid = $P{objid}

[findToBeCollectedForOnlineType]
SELECT COUNT(DISTINCT d.loanapp_objid) AS `count`, SUM(l.dailydue) AS totalamount
FROM collection_remittance r
INNER JOIN onlinecollection o ON r.collection_objid = o.objid
INNER JOIN onlinecollection_detail d ON o.objid = d.parentid
INNER JOIN loan_ledger l ON d.loanapp_objid = l.appid
WHERE r.objid = $P{objid}

[findToBeCollectedForFollowupAndSpecialType]
SELECT COUNT(DISTINCT d.ledgerid) AS `count`, SUM(d.dailydue) AS totalamount
FROM collection_remittance r
INNER JOIN ledger_billing_item i ON r.collection_objid = i.parentid AND r.group_objid = i.item_objid
INNER JOIN ledger_billing_detail d ON i.objid = d.parentid
WHERE r.objid = $P{objid}

