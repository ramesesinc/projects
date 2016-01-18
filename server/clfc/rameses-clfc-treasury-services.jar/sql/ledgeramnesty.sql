[getList]
SELECT a.*
FROM (
	SELECT objid
	FROM ledgeramnesty
	WHERE borrower_name LIKE $P{searchtext}
	UNION
	SELECT objid
	FROM ledgeramnesty
	WHERE loanapp_appno LIKE $P{searchtext}
) q INNER JOIN ledgeramnesty a ON q.objid = a.objid
ORDER BY a.dtcreated DESC

[getListByState]
SELECT a.*
FROM (
	SELECT objid
	FROM ledgeramnesty
	WHERE borrower_name LIKE $P{searchtext}
		AND txnstate = $P{state}
	UNION
	SELECT objid
	FROM ledgeramnesty
	WHERE loanapp_appno LIKE $P{searchtext}
		AND txnstate = $P{state}
) q INNER JOIN ledgeramnesty a ON q.objid = a.objid
ORDER BY a.dtcreated DESC

[getListByLedgerid]
SELECT a.*
FROM (
	SELECT objid
	FROM ledgeramnesty
	WHERE borrower_name LIKE $P{searchtext}
		AND ledger_objid = $P{ledgerid}
	UNION
	SELECT objid
	FROM ledgeramnesty
	WHERE loanapp_appno LIKE $P{searchtext}
		AND ledger_objid = $P{ledgerid}
) q INNER JOIN ledgeramnesty a ON q.objid = a.objid
ORDER BY a.dtcreated DESC

[getListByLedgeridAndState]
SELECT a.*
FROM (
	SELECT objid
	FROM ledgeramnesty
	WHERE borrower_name LIKE $P{searchtext}
		AND txnstate = $P{state}
		AND ledger_objid = $P{ledgerid}
	UNION
	SELECT objid
	FROM ledgeramnesty
	WHERE loanapp_appno LIKE $P{searchtext}
		AND txnstate = $P{state}
		AND ledger_objid = $P{ledgerid}
) q INNER JOIN ledgeramnesty a ON q.objid = a.objid
ORDER BY a.dtcreated DESC

[getLookupList]
SELECT a.*
FROM (
	SELECT objid
	FROM ledgeramnesty
	WHERE borrower_name LIKE $P{searchtext}
	UNION
	SELECT objid
	FROM ledgeramnesty
	WHERE loanapp_appno LIKE $P{searchtext}
) q INNER JOIN ledgeramnesty a ON q.objid = a.objid
ORDER BY a.dtcreated

[getForAvailLookupListByType]
SELECT a.*
FROM (
	SELECT a.objid
	FROM ledgeramnesty a
	INNER JOIN ledgeramnesty_detail d ON a.objid = d.parentid
	LEFT JOIN ledgeramnesty_active ac ON a.objid = ac.amnestyid
	WHERE ac.objid IS NULL
		AND a.borrower_name LIKE $P{searchtext}
		AND a.txnstate = 'RETURNED'
		AND d.amnestytype_value = $P{type}
	GROUP BY a.objid
	UNION
	SELECT a.objid
	FROM ledgeramnesty a
	INNER JOIN ledgeramnesty_detail d ON a.objid = d.parentid
	LEFT JOIN ledgeramnesty_active ac ON a.objid = ac.amnestyid
	WHERE ac.objid IS NULL
		AND a.loanapp_appno LIKE $P{searchtext}
		AND a.txnstate = 'RETURNED'
		AND d.amnestytype_value = $P{type}
	GROUP BY a.objid
	UNION
	SELECT a.objid
	FROM ledgeramnesty a
	INNER JOIN ledgeramnesty_detail d ON a.objid = d.parentid
	LEFT JOIN ledgeramnesty_active ac ON a.objid = ac.amnestyid
	WHERE ac.objid IS NULL
		AND a.refno LIKE $P{searchtext}
		AND a.txnstate = 'RETURNED'
		AND d.amnestytype_value = $P{type}
	GROUP BY a.objid
) q INNER JOIN ledgeramnesty a ON q.objid = a.objid
ORDER BY a.dtcreated

[getDetails]
SELECT d.*
FROM ledgeramnesty_detail d
WHERE d.parentid = $P{objid}

[getPostingDetail]
SELECT d.* 
FROM ledgeramnesty_postingdetail d
INNER JOIN ledger_detail_state_type s ON d.state = s.name 
WHERE d.parentid = $P{objid}
ORDER BY d.day, d.dtpaid, d.refno, s.level, d.txndate

[getActiveAmnestyByEndDateAndType]
SELECT a.* FROM ledgeramnesty_active a 
WHERE a.dtended = $P{enddate}
	AND a.type = $P{type}

[findCounterPostingDetail]
SELECT COUNT(d.objid) AS counter 
FROM ledgeramnesty_postingdetail d
WHERE d.parentid = $P{objid}

[findDetail]
SELECT d.* 
FROM ledgeramnesty_detail d
WHERE d.parentid = $P{objid}

[findDetailByState]
SELECT d.* 
FROM ledgeramnesty_detail d
WHERE d.parentid = $P{objid}
	AND d.state = $P{state}

[findSendBackByRefid]
SELECT s.*
FROM ledgeramnesty_sendback s
WHERE s.refid = $P{refid}
ORDER BY s.dtcreated DESC

[findSendBackByRefidAndState]
SELECT s.*
FROM ledgeramnesty_sendback s
WHERE s.refid = $P{refid}
	AND s.state = $P{state}
ORDER BY s.dtcreated DESC

[findActiveByAmnestyid]
SELECT a.* 
FROM ledgeramnesty_active a
WHERE a.amnestyid = $P{amnestyid}
ORDER BY a.dtfiled DESC

[findActiveByAmnestyidAndRefid]
SELECT a.* 
FROM ledgeramnesty_active a
WHERE a.amnestyid = $P{amnestyid}
	AND a.refid = $P{refid}
ORDER BY a.dtfiled DESC

[findActiveByDate]
SELECT a.*
FROM (
	SELECT ac.objid,
		CASE WHEN ac.dtended IS NULL THEN CURDATE() ELSE ac.dtended END AS dateended
	FROM ledgeramnesty a
	INNER JOIN ledgeramnesty_active ac ON a.objid = ac.amnestyid
) q INNER JOIN ledgeramnesty_active a ON q.objid = a.objid
WHERE $P{date} BETWEEN a.dtstarted AND q.dateended
ORDER BY a.dtfiled DESC

[xfindActiveByDate]
SELECT a.* 
FROM ledgeramnesty_active a
WHERE $P{date} BETWEEN a.dtstarted AND a.dtended
ORDER BY a.dtfiled DESC

[findActiveByDateAndLedgerid]
SELECT a.*
FROM (
	SELECT ac.objid,
		CASE WHEN ac.dtended IS NULL THEN CURDATE() ELSE ac.dtended END AS dateended
	FROM ledgeramnesty a
	INNER JOIN ledgeramnesty_active ac ON a.objid = ac.amnestyid
	WHERE a.ledger_objid = $P{ledgerid}
) q INNER JOIN ledgeramnesty_active a ON q.objid = a.objid
WHERE $P{date} BETWEEN a.dtstarted AND q.dateended
ORDER BY a.dtfiled DESC

[xfindActiveByDateAndLedgerid]
SELECT ac.* 
FROM ledgeramnesty_active ac
INNER JOIN ledgeramnesty a ON ac.amnestyid = a.objid
WHERE $P{date} BETWEEN ac.dtstarted AND ac.dtended
	AND a.ledger_objid = $P{ledgerid}
ORDER BY ac.dtfiled DESC

[changeState]
UPDATE ledgeramnesty SET txnstate = $P{txnstate}
WHERE objid = $P{objid}

[removePostingDetail]
DELETE FROM ledgeramnesty_postingdetail WHERE parentid = $P{objid}

[xgetList]
SELECT a.* 
FROM ledgeramnesty a
WHERE a.borrower_name LIKE $P{searchtext}
ORDER BY a.dtcreated DESC

[xgetListByState]
SELECT a.* 
FROM ledgeramnesty a
WHERE a.borrower_name LIKE $P{searchtext}
	AND a.txnstate = $P{state}
ORDER BY a.dtcreated DESC
