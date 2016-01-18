[getList]
SELECT * FROM shortage
ORDER BY txndate DESC, cbsno DESC

[getListByState]
SELECT * FROM shortage
WHERE state = $P{state}
ORDER BY txndate DESC, cbsno DESC

[getListByRemittanceid]
SELECT * FROM shortage
WHERE remittanceid = $P{remittanceid}
ORDER BY txndate DESC, cbsno DESC

[getListByRemittanceidAndState]
SELECT * FROM shortage
WHERE remittanceid = $P{remittanceid}
	AND state = $P{state}
ORDER BY txndate DESC, cbsno DESC

[getLookupList]
SELECT * FROM shortage
ORDER BY txndate DESC, cbsno DESC

[getLookupListByState]
SELECT * FROM shortage
WHERE state = $P{state}
ORDER BY txndate DESC, cbsno DESC

[getShortagesByStartAndEnddate]
SELECT s.collector_name AS collector, s.txndate, s.refno, s.amount,
	CASE 
		WHEN c.group_type = 'route' THEN CONCAT(r.description, ' - ', r.area)
		WHEN c.group_type = 'online' THEN "DIRECT"
		ELSE UCASE(group_type)
	END  AS route
FROM shortage s
	INNER JOIN collection_remittance c ON s.remittanceid = c.objid
	LEFT JOIN loan_route r ON c.group_objid = r.code
WHERE s.txndate BETWEEN $P{startdate} AND $P{enddate}
	AND s.state IN ('APPROVED', 'NOTED')

[getListByTxndate]
SELECT s.* FROM shortage s
WHERE s.txndate = $P{txndate}

[getShortagesByTxndateAndCollectorid]
SELECT * FROM shortage
WHERE txndate = $P{txndate}
	AND collector_objid = $P{collectorid}

[getShortagesWithCBSNoByTxndateAndCollectorid]
SELECT s.*, c.cbsno
FROM shortage s
INNER JOIN collection_cb c ON s.objid = c.collection_objid AND s.objid = c.group_objid
WHERE s.txndate = $P{txndate}
	AND s.collector_objid = $P{collectorid}

[findShortageByRemittanceidAndState]
SELECT s.* FROM shortage s
WHERE s.remittanceid = $P{remittanceid}
	AND s.state = $P{state}

[findUnapprovedShortageByRemittanceid]
SELECT * FROM shortage
WHERE state IN ('DRAFT', 'FOR_APPROVAL')
	AND remittanceid = $P{remittanceid}

[findUnnotedShortageByRemittanceid]
SELECT * FROM shortage
WHERE state IN ('DRAFT', 'FOR_SIGNATORY')
	AND remittanceid = $P{remittanceid}

[findApprovedShortageWithoutBreakdownByRemittanceid]
SELECT s.*, c.objid
FROM shortage s
LEFT JOIN collection_cb c 
ON c.collection_objid = s.objid 
	AND c.group_objid = s.objid 
	AND c.group_type = 'shortage'
WHERE s.remittanceid = $P{remittanceid}
	AND s.state = 'APPROVED'
	AND c.objid IS NULL
LIMIT 1

[findShortageByRefno]
SELECT s.*,
	CASE
		WHEN c.group_type = 'route' THEN CONCAT(r.description, " - ", r.area)
		WHEN c.group_type = 'online' THEN "DIRECT"
		ELSE UCASE(c.group_type)
	END AS route
FROM shortage s
	INNER JOIN collection_remittance c ON s.remittanceid = c.objid
	LEFT JOIN loan_route r ON c.group_objid = r.code
WHERE s.refno = $P{refno}

[changeState]
UPDATE shortage SET state = $P{state}
WHERE objid = $P{objid}

[closeShortageByRemittanceid]
UPDATE shortage SET state = 'CLOSED'
WHERE remittanceid = $P{remittanceid}