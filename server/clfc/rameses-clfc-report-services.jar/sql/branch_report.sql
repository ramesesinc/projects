[findReportDataByDate]
SELECT b.*
FROM branch_report b
WHERE b.txndate = $P{txndate}
	AND b.reporttype = $P{reporttype}
ORDER BY b.dtcreated DESC

[findReportData]
SELECT r.*
FROM (
	SELECT b.objid
	FROM branch_report b
	WHERE b.txndate = $P{txndate}
	ORDER BY b.dtcreated DESC
) a
INNER JOIN branch_report r ON a.objid = r.objid
WHERE r.criteria_code IS NULL
	AND r.criteria_value IS NULL
	AND r.reporttype = $P{reporttype}
GROUP BY r.txndate
HAVING r.objid IS NOT NULL

[getReportData]
SELECT r.*
FROM (
	SELECT b.objid
	FROM branch_report b
	WHERE b.txndate BETWEEN $P{startdate} AND $P{enddate}
		AND b.criteria_code IS NULL
		AND b.criteria_value IS NULL
		AND b.reporttype = $P{reporttype}
	ORDER BY b.dtcreated DESC
) a INNER JOIN branch_report r ON a.objid = r.objid
GROUP BY r.txndate
HAVING r.objid IS NOT NULL

[getLoanOutstanding]
SELECT a.borrower_name AS borrower, a.appno, c.dtreleased, a.loanamount, a.apptype,
	l.balance, l.dtmatured, r.code AS route_code, r.description AS route_description,
	CURDATE() AS txndate, b.address
FROM loan_ledger l
INNER JOIN loanapp a ON l.appid = a.objid
INNER JOIN loan_route r ON a.route_code = r.code
INNER JOIN loanapp_capture c ON a.objid = c.objid
INNER JOIN borrower b ON a.borrower_objid = b.objid
WHERE l.state = 'OPEN'
ORDER BY r.description, a.borrower_name

[getOutstanding]
SELECT q.*,
	CASE WHEN lb.objid IS NOT NULL THEN
		CONCAT(q.bname, ' AND ', lb.borrowername)
	ELSE
		q.bname
	END AS borrower
FROM (
	SELECT a.borrower_name AS bname, a.appno, a.objid AS appid, c.dtreleased, a.loanamount, a.apptype,
		l.balance, l.dtmatured, r.code AS route_code, r.description AS route_description, CURDATE() AS txndate,
		(SELECT objid FROM loanapp_borrower WHERE parentid = a.objid AND `type` = 'JOINT' LIMIT 1) AS jointid
	FROM loan_ledger l
	INNER JOIN loanapp a ON l.appid = a.objid
	INNER JOIN loan_route r ON a.route_code = r.code
	INNER JOIN loanapp_capture c ON a.objid = c.objid
	WHERE l.state = 'OPEN'
	ORDER BY r.description, a.borrower_name
) q LEFT JOIN loanapp_borrower lb ON q.jointid = lb.objid

[xgetOutstanding]
SELECT a.borrower_name AS borrower, a.appno, c.dtreleased, a.loanamount, a.apptype,
	l.balance, l.dtmatured, r.code AS route_code, r.description AS route_description,
	CURDATE() AS txndate
FROM loan_ledger l
INNER JOIN loanapp a ON l.appid = a.objid
INNER JOIN loan_route r ON a.route_code = r.code
INNER JOIN loanapp_capture c ON a.objid = c.objid
WHERE l.state = 'OPEN'
ORDER BY r.description, a.borrower_name

[getPastDue]
SELECT q.*,
	CASE WHEN lb.objid IS NOT NULL THEN
		CONCAT(q.bname, ' AND ', lb.borrowername)
	ELSE
		q.bname
	END AS borrower
FROM (
	SELECT a.borrower_name AS bname, a.appno, a.objid AS appid, c.dtreleased, a.loanamount, a.apptype,
		l.balance, l.dtmatured, r.code AS route_code, r.description AS route_description, CURDATE() AS txndate,
		(SELECT objid FROM loanapp_borrower WHERE parentid = a.objid AND `type` = 'JOINT' LIMIT 1) AS jointid
	FROM loan_ledger l
	INNER JOIN loanapp a ON l.appid = a.objid
	INNER JOIN loan_route r ON a.route_code = r.code
	INNER JOIN loanapp_capture c ON a.objid = c.objid
	WHERE l.state = 'OPEN'
		AND CURDATE() > l.dtmatured
	ORDER BY r.description, a.borrower_name
) q LEFT JOIN loanapp_borrower lb ON q.jointid = lb.objid

[xgetPastDue]
SELECT a.borrower_name AS borrower, a.appno, c.dtreleased, a.loanamount, a.apptype,
	l.balance, l.dtmatured, r.code AS route_code, r.description AS route_description,
	CURDATE() AS txndate
FROM loan_ledger l
INNER JOIN loanapp a ON l.appid = a.objid
INNER JOIN loan_route r ON a.route_code = r.code
INNER JOIN loanapp_capture c ON a.objid = c.objid
WHERE l.state = 'OPEN'
	AND CURDATE() > l.dtmatured
ORDER BY r.description, a.borrower_name

[getLoanRelease]
SELECT a.borrower_name AS borrower, a.appno, c.dtreleased, a.loanamount, a.apptype,
	r.code AS route_code, r.description AS route_description, CURDATE() AS txndate
FROM loanapp a
INNER JOIN loan_route r ON a.route_code = r.code
INNER JOIN loanapp_capture c ON a.objid = c.objid
WHERE c.dtreleased = CURDATE()
ORDER BY r.description, a.borrower_name

[getLoanReleaseByStartDateAndEndDate]
SELECT q.*,
	CASE WHEN lb.objid IS NOT NULL THEN
		CONCAT(q.bname, ' AND ', lb.borrowername)
	ELSE
		q.bname
	END AS borrower
FROM (
	SELECT a.borrower_name AS bname, a.appno, a.objid AS appid, c.dtreleased, a.loanamount, a.apptype,
		r.code AS route_code, r.description AS route_description, c.dtreleased AS txndate,
		(SELECT objid FROM loanapp_borrower WHERE parentid = a.objid AND `type` = 'JOINT' LIMIT 1) AS jointid
	FROM loanapp a
	INNER JOIN loan_route r ON a.route_code = r.code
	INNER JOIN loanapp_capture c ON a.objid = c.objid
	WHERE c.dtreleased BETWEEN $P{startdate} AND $P{enddate}
	ORDER BY r.description, a.borrower_name
) q LEFT JOIN loanapp_borrower lb ON q.jointid = lb.objid

[xgetLoanReleaseByStartDateAndEndDate]
SELECT a.borrower_name AS borrower, a.appno, c.dtreleased, a.loanamount, a.apptype,
	r.code AS route_code, r.description AS route_description, c.dtreleased AS txndate
FROM loanapp a
INNER JOIN loan_route r ON a.route_code = r.code
INNER JOIN loanapp_capture c ON a.objid = c.objid
WHERE c.dtreleased BETWEEN $P{startdate} AND $P{enddate}
ORDER BY r.description, a.borrower_name

[findLastReport]
SELECT * FROM branch_report 
WHERE reporttype = $P{reporttype}
ORDER BY dtcreated DESC

[findByDatecreated]
SELECT * FROM branch_report 
WHERE dtcreated = $P{dtcreated}
	AND reporttype = $P{reporttype}