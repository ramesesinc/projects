[getList]
SELECT * FROM collection_cb
WHERE cbsno LIKE $P{searchtext}
ORDER BY txndate DESC

[getListByState]
SELECT * FROM collection_cb
WHERE cbsno LIKE $P{searchtext}
	AND state = $P{state}
ORDER BY txndate DESC

[getListByTxndateAndState]
SELECT c.* FROM collection_cb c
WHERE c.txndate = $P{txndate}
	AND c.state = $P{state}

[getListByTxndateStateNotEncashed]
SELECT c.* 
FROM collection_cb c
LEFT JOIN collection_cb_encash e ON c.objid = e.objid
WHERE e.objid IS NULL
	AND c.txndate = $P{txndate}
	AND c.state = $P{state}

[getLookupList]
SELECT c.*, (SELECT SUM(amount) FROM collection_cb_detail WHERE parentid = c.objid) AS amount,
	CASE WHEN e.objid IS NULL THEN 0 ELSE 1 END AS isencashed
FROM collection_cb c
LEFT JOIN collection_cb_encash e ON c.objid = e.objid
WHERE c.cbsno LIKE $P{searchtext}
ORDER BY c.txndate DESC, c.cbsno

[getLookupListByState]
SELECT c.*, (SELECT SUM(amount) FROM collection_cb_detail WHERE parentid = c.objid) AS amount,
	CASE WHEN e.objid IS NULL THEN 0 ELSE 1 END AS isencashed
FROM collection_cb c
LEFT JOIN collection_cb_encash e ON c.objid = e.objid
WHERE c.cbsno LIKE $P{searchtext}
	AND c.state = $P{state}
ORDER BY c.txndate DESC, c.cbsno

[getListForVerification]
SELECT c.* 
FROM collection_cb c
INNER JOIN collection_cb_forverification f ON c.objid = f.objid
WHERE c.cbsno LIKE $P{searchtext}
ORDER BY c.txndate DESC, c.cbsno

[getListVerified]
SELECT c.* 
FROM collection_cb c
INNER JOIN collection_cb_active f ON c.objid = f.objid
WHERE c.cbsno LIKE $P{searchtext}
ORDER BY c.txndate DESC, c.cbsno

[getListDeposited]
SELECT c.*
FROM collection_cb c
INNER JOIN collection_cb_deposited d ON c.objid = d.objid
WHERE c.cbsno LIKE $P{searchtext}
ORDER BY c.txndate DESC, c.cbsno

[getLookupListActive]
SELECT c.*, (SELECT SUM(amount) FROM collection_cb_detail WHERE parentid = c.objid) AS amount,
	CASE WHEN e.objid IS NULL THEN 0 ELSE 1 END AS isencashed
FROM collection_cb c
INNER JOIN collection_cb_active f ON c.objid = f.objid
LEFT JOIN collection_cb_encash e ON c.objid = e.objid
WHERE c.cbsno LIKE $P{searchtext}
ORDER BY c.txndate DESC, c.cbsno

[getLookupListForDepositSlip]
SELECT c.*, (SELECT SUM(amount) FROM collection_cb_detail WHERE parentid = c.objid) AS amount,
	CASE WHEN e.objid IS NULL THEN 0 ELSE 1 END AS isencashed
FROM (
	SELECT c.objid
	FROM collection_cb c
	LEFT JOIN (
		SELECT dc.cbsid, dc.objid
		FROM depositslip d
		INNER JOIN depositslip_cbs dc ON d.objid = dc.parentid
	) q ON c.objid = q.cbsid
	WHERE q.objid IS NULL
) q INNER JOIN collection_cb c ON q.objid = c.objid
INNER JOIN collection_cb_active f ON c.objid = f.objid
LEFT JOIN collection_cb_encash e ON e.objid = c.objid
WHERE e.objid IS NULL
	AND c.cbsno LIKE $P{searchtext}
ORDER BY c.txndate DESC, c.cbsno

[getLookupListForEncashment]
SELECT c.*, (SELECT SUM(amount) FROM collection_cb_detail WHERE parentid = c.objid) AS amount
FROM (
	SELECT c.objid
	FROM collection_cb c
	LEFT JOIN depositslip_cbs dc ON c.objid = dc.cbsid
	WHERE dc.objid IS NULL 
		AND c.txndate = CURDATE()
		AND c.cbsno LIKE $P{searchtext}
) q INNER JOIN collection_cb c ON q.objid = c.objid
INNER JOIN collection_cb_forencashment f ON c.objid = f.objid
ORDER BY c.txndate DESC, c.cbsno

[getLookupListForEncashmentByDate]
SELECT c.*, (SELECT SUM(amount) FROM collection_cb_detail WHERE parentid = c.objid) AS amount
FROM (
	SELECT c.objid
	FROM collection_cb c
	LEFT JOIN depositslip_cbs dc ON c.objid = dc.cbsid
	WHERE dc.objid IS NULL 
		AND c.txndate = $P{txndate}
		AND c.cbsno LIKE $P{searchtext}
) q INNER JOIN collection_cb c ON q.objid = c.objid
INNER JOIN collection_cb_forencashment f ON c.objid = f.objid
ORDER BY c.txndate DESC, c.cbsno

[xxgetLookupListForEncashment]
SELECT c.*, (SELECT SUM(amount) FROM collection_cb_detail WHERE parentid = c.objid) AS amount,
	CASE WHEN e.objid IS NULL THEN 0 ELSE 1 END AS isencashed
FROM (
	SELECT c.objid
	FROM collection_cb c
	LEFT JOIN (
		SELECT dc.cbsid, dc.objid
		FROM depositslip d
		INNER JOIN depositslip_cbs dc ON d.objid = dc.parentid
		WHERE d.txndate = CURDATE()
	) q ON c.objid = q.cbsid
	WHERE q.objid IS NULL AND c.txndate = CURDATE()
) q INNER JOIN collection_cb c ON q.objid = c.objid
INNER JOIN collection_cb_forencashment f ON c.objid = f.objid
LEFT JOIN collection_cb_encash e ON e.objid = c.objid
WHERE c.cbsno LIKE $P{searchtext}
ORDER BY c.txndate DESC, c.cbsno

[xxgetLookupListForEncashmentByDate]
SELECT c.*, (SELECT SUM(amount) FROM collection_cb_detail WHERE parentid = c.objid) AS amount,
	CASE WHEN e.objid IS NULL THEN 0 ELSE 1 END AS isencashed
FROM (
	SELECT c.objid
	FROM collection_cb c
	LEFT JOIN (
		SELECT dc.cbsid, dc.objid
		FROM depositslip d
		INNER JOIN depositslip_cbs dc ON d.objid = dc.parentid
	) q ON c.objid = q.cbsid
	WHERE q.objid IS NULL AND c.txndate = $P{txndate}
) q INNER JOIN collection_cb c ON q.objid = c.objid
INNER JOIN collection_cb_forencashment f ON c.objid = f.objid
LEFT JOIN collection_cb_encash e ON e.objid = c.objid
WHERE c.cbsno LIKE $P{searchtext}
ORDER BY c.txndate DESC, c.cbsno

[xgetLookupListForEncashment]
SELECT c.*, (SELECT SUM(amount) FROM collection_cb_detail WHERE parentid = c.objid) AS amount,
	CASE WHEN e.objid IS NULL THEN 0 ELSE 1 END AS isencashed
FROM collection_cb c
INNER JOIN collection_cb_forencashment f ON c.objid = f.objid
LEFT JOIN collection_cb_encash e ON e.objid = c.objid
WHERE c.cbsno LIKE $P{searchtext}
ORDER BY c.txndate DESC, c.cbsno

[getLookupListForDailycollection]
SELECT c.*, (SELECT SUM(amount) FROM collection_cb_detail WHERE parentid = c.objid) AS amount,
	CASE WHEN e.objid IS NULL THEN 0 ELSE 1 END AS isencashed
FROM (
	SELECT b.* FROM collection_cb b 
	INNER JOIN collection_cb_active a ON b.objid = a.objid
	WHERE b.cbsno LIKE $P{searchtext}
	UNION
	SELECT b.* FROM collection_cb b 
	INNER JOIN collection_cb_deposited a ON b.objid = a.objid
	WHERE b.cbsno LIKE $P{searchtext}
) q INNER JOIN collection_cb c ON q.objid = c.objid
LEFT JOIN collection_cb_encash e ON e.objid = c.objid
ORDER BY c.txndate DESC, c.cbsno

[getDetails]
SELECT * FROM collection_cb_detail
WHERE parentid = $P{objid}
ORDER BY denomination DESC

[getConsolidatedBreakdown]
SELECT cd.denomination, SUM(cd.qty) AS qty,
	SUM(cd.amount) AS amount
FROM collection_cb c
INNER JOIN collection_cb_detail cd ON c.objid = cd.parentid
WHERE c.txndate = $P{billdate}
	AND group_type NOT IN('deposit')
GROUP BY cd.denomination
ORDER BY cd.denomination DESC

[getCashBreakdownByStartdateAndEnddate]
SELECT c.txndate, c.state, c.collector_name AS collector, c.cbsno,
	CASE 
		WHEN c.group_type = 'route' THEN CONCAT(r.description, ' - ', r.area)
		WHEN c.group_type = 'online' THEN "DIRECT"
		ELSE UCASE(group_type)
	END  AS route,
	(SELECT SUM(amount) FROM collection_cb_detail WHERE parentid = c.objid) AS amount,
	CASE WHEN e.objid IS NULL THEN FALSE ELSE TRUE END AS isencashed
FROM collection_cb c
LEFT JOIN collection_cb_encash e ON c.objid = e.objid
LEFT JOIN loan_route r ON c.group_objid = r.code
WHERE c.txndate BETWEEN $P{startdate} AND $P{enddate} AND c.group_type <> 'deposit'
ORDER BY route, c.txndate

[getLookupListForCashExchange]
SELECT c.*, (SELECT SUM(amount) FROM collection_cb_detail WHERE parentid = c.objid) AS amount
FROM (
	SELECT c.objid
	FROM collection_cb c
	LEFT JOIN depositslip_cbs dc ON c.objid = dc.cbsid
	WHERE dc.objid IS NULL 
		AND c.cbsno LIKE $P{searchtext}
) q INNER JOIN collection_cb c ON q.objid = c.objid
INNER JOIN collection_cb_active a ON c.objid = a.objid
ORDER BY c.txndate DESC, c.cbsno

[getLookupListForCashExchangeByDate]
SELECT c.*, (SELECT SUM(amount) FROM collection_cb_detail WHERE parentid = c.objid) AS amount
FROM (
	SELECT c.objid
	FROM collection_cb c
	LEFT JOIN depositslip_cbs dc ON c.objid = dc.cbsid
	WHERE dc.objid IS NULL 
		AND c.txndate = $P{date}
		AND c.cbsno LIKE $P{searchtext}
) q INNER JOIN collection_cb c ON q.objid = c.objid
INNER JOIN collection_cb_active a ON c.objid = a.objid
ORDER BY c.txndate DESC, c.cbsno

[findUnverifiedCashBreakdownByDate]
SELECT c.*
FROM (
	SELECT c.objid
	FROM collection_cb c
	WHERE c.state = 'FOR_VERIFICATION'
		AND c.txndate = $P{date}
	UNION
	SELECT c.objid
	FROM collection_cb c
	INNER JOIN collection_cb_sendback b ON c.objid = b.parentid
	WHERE b.state = 'DRAFT'
		AND c.txndate = $P{date}
) q INNER JOIN collection_cb c ON q.objid = c.objid

[findCashbreakdown]
SELECT *
FROM collection_cb
WHERE collection_objid = $P{collectionid}
	AND group_objid = $P{groupid}
	AND group_type = $P{grouptype}

[findCashbreakdownByCbsno]
SELECT * FROM collection_cb
WHERE cbsno = $P{cbsno}

[findCurrentSendback]
SELECT c.*
FROM collection_cb_sendback c
WHERE c.parentid = $P{objid}
ORDER BY c.dtcreated DESC

[findCurrentSendbackByState]
SELECT c.*
FROM collection_cb_sendback c
WHERE c.parentid = $P{objid}
	AND c.state = $P{state}
ORDER BY c.dtcreated DESC


[changeState]
UPDATE collection_cb SET state = $P{state}
WHERE objid = $P{objid}