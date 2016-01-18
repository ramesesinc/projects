[getSearchIndexByAppid]
SELECT s.*
FROM loanapp_search_index s
WHERE s.appid = $P{appid}

[getLookupList]
SELECT a.*, b.address AS borrower_address, ac.dtreleased, l.dtmatured
FROM (
	SELECT a.objid, 0 AS payments
	FROM (
		SELECT a.*
		FROM loanapp a
		WHERE a.borrower_name LIKE $P{searchtext}
			AND a.state = 'RELEASED'
		UNION
		SELECT a.* 
		FROM loanapp a
		WHERE a.appno LIKE $P{searchtext}
			AND a.state = 'RELEASED'
	) a
	LEFT JOIN loan_ledger l ON a.objid = l.appid
	WHERE l.objid IS NULL
	UNION
	SELECT a.objid, (SELECT COUNT(objid) FROM loan_ledger_payment WHERE parentid = l.objid LIMIT 1) AS payments
	FROM (
		SELECT a.*
		FROM loanapp a
		WHERE a.borrower_name LIKE $P{searchtext}
			AND a.state = 'RELEASED'
		UNION
		SELECT a.* 
		FROM loanapp a
		WHERE a.appno LIKE $P{searchtext}
			AND a.state = 'RELEASED'
	) a
	INNER JOIN loan_ledger l ON a.objid = l.appid
	WHERE l.state = 'OPEN'
	GROUP BY a.objid
	HAVING payments = 0
) q 
INNER JOIN loanapp a ON q.objid = a.objid
INNER JOIN borrower b ON a.borrower_objid = b.objid
LEFT JOIN loanapp_capture ac ON a.objid = ac.objid
LEFT JOIN loan_ledger l ON a.objid = l.appid
ORDER BY a.borrower_name, ac.dtreleased

[removeSegregationByLedgerid]
DELETE FROM loan_ledger_segregation WHERE refid = $P{ledgerid}