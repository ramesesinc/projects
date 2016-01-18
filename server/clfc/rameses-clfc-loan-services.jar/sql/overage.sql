[getList]
SELECT * FROM overage
ORDER BY dtfiled DESC

[getListByState]
SELECT * FROM overage
WHERE state = $P{state}
ORDER BY dtfiled DESC

[getListByRemittanceid]
SELECT * FROM overage
WHERE remittanceid = $P{remittanceid}
ORDER BY dtfiled DESC

[getListByRemittanceidAndState]
SELECT * FROM overage
WHERE remittanceid = $P{remittanceid}
	AND state = $P{state}
ORDER BY dtfiled DESC

[getListByTxndate]
SELECT o.* FROM overage o
WHERE o.txndate = $P{txndate}

[getListByTxndateAndState]
SELECT o.* FROM overage o
WHERE o.txndate = $P{txndate}

[getLookupList]
SELECT * FROM overage
ORDER BY dtfiled DESC

[getLookupListByState]
SELECT * FROM overage
WHERE state = $P{state}
ORDER BY dtfiled DESC

[getLookupListByStateWithBalance]
SELECT * FROM overage
WHERE state = $P{state}
	AND balance > 0
ORDER BY dtfiled DESC

[getOveragesByStartdateAndEnddate]
SELECT o.collector_name AS collector, o.txndate, o.refno, o.amount, o.balance,
	CASE 
		WHEN c.group_type = 'route' THEN CONCAT(r.description, ' - ', r.area)
		WHEN c.group_type = 'online' THEN "DIRECT"
		ELSE UCASE(group_type)
	END  AS route
FROM overage o
	INNER JOIN collection_remittance c ON o.remittanceid = c.objid
	LEFT JOIN loan_route r ON c.group_objid = r.code
WHERE o.txndate BETWEEN $P{startdate} AND $P{enddate}
	AND o.state IN ('APPROVED', 'NOTED')

[findUnapprovedOverageByRemittanceid]
SELECT * FROM overage
WHERE state IN ('DRAFT', 'FOR_APPROVAL')
	AND remittanceid = $P{remittanceid}

[findUnnotedOverageByRemittanceid]
SELECT * FROM overage
WHERE state IN ('DRAFT', 'FOR_SIGNATORY')
	AND remittanceid = $P{remittanceid}

[findOverageByRefno]
SELECT o.*,
	CASE
		WHEN c.group_type = 'route' THEN CONCAT(r.description, " - ", r.area)
		WHEN c.group_type = 'online' THEN "DIRECT"
		ELSE UCASE(c.group_type)
	END AS route
FROM overage o
	INNER JOIN collection_remittance c ON o.remittanceid = c.objid
	LEFT JOIN loan_route r ON c.group_objid = r.code
WHERE o.refno = $P{refno}
LIMIT 1

[changeState]
UPDATE overage SET state = $P{state}
WHERE objid = $P{objid}