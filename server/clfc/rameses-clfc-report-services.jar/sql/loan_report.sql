[getLoansByRoute]
SELECT q.*,
	CASE WHEN lb.objid IS NOT NULL THEN
		CONCAT(q.bname, ' AND ', lb.borrowername)
	ELSE
		q.bname
	END AS borrower_name
FROM (
	SELECT a.appno AS loanapp_appno, a.apptype AS loanapp_apptype, a.loanamount AS loanapp_loanamount, a.loantype AS loanapp_loantype,
		a.borrower_objid AS borrower_objid, a.borrower_name AS bname, a.objid AS loanapp_objid, ac.dtreleased AS loanapp_dtreleased, 
		l.dtmatured AS loanapp_dtmatured, l.objid AS loanapp_ledgerid, r.code AS route_code, r.description AS route_description, 
		r.area AS route_area, l.balance AS loanapp_balance, b.address AS loanapp_address,
		(SELECT objid FROM loanapp_borrower WHERE parentid = l.appid AND `type` = 'JOINT' LIMIT 1) AS jointid
	FROM loanapp a
	INNER JOIN loanapp_capture ac ON a.objid = ac.objid
	INNER JOIN loan_ledger l ON a.objid = l.appid
	INNER JOIN loan_route r ON a.route_code = r.code
	INNER JOIN loan_ledger_segregation s ON l.objid = s.refid
	INNER JOIN borrower b ON a.borrower_objid = b.objid
	WHERE l.state = 'OPEN'
		AND r.code = $P{routecode}
	ORDER BY b.address, a.borrower_name, ac.dtreleased DESC
) q LEFT JOIN loanapp_borrower lb ON q.jointid = lb.objid

[xgetLoansByRoute]
SELECT a.appno AS loanapp_appno, a.apptype AS loanapp_apptype, a.loanamount AS loanapp_loanamount, a.loantype AS loanapp_loantype,
	a.borrower_objid AS borrower_objid, a.borrower_name AS borrower_name, a.objid AS loanapp_objid, ac.dtreleased AS loanapp_dtreleased, 
	l.dtmatured AS loanapp_dtmatured, l.objid AS loanapp_ledgerid, r.code AS route_code, r.description AS route_description, 
	r.area AS route_area, l.balance AS loanapp_balance, b.address AS loanapp_address
FROM loanapp a
INNER JOIN loanapp_capture ac ON a.objid = ac.objid
INNER JOIN loan_ledger l ON a.objid = l.appid
INNER JOIN loan_route r ON a.route_code = r.code
INNER JOIN loan_ledger_segregation s ON l.objid = s.refid
INNER JOIN borrower b ON a.borrower_objid = b.objid
WHERE l.state = 'OPEN'
	AND r.code = $P{routecode}
ORDER BY b.address, a.borrower_name, ac.dtreleased DESC

[getLoansByRouteAndSegregationType]
SELECT q.*,
	CASE WHEN lb.objid IS NOT NULL THEN
		CONCAT(q.bname, ' AND ', lb.borrowername)
	ELSE
		q.bname
	END AS borrower_name
FROM (
	SELECT q.*, p.objid AS lastpayment_objid, p.amount AS lastpayment_amount, p.txndate AS lastpayment_txndate, p.refno AS lastpayment_refno
	FROM (
		SELECT a.appno AS loanapp_appno, a.apptype AS loanapp_apptype, a.loanamount AS loanapp_loanamount, a.loantype AS loanapp_loantype,
			a.borrower_objid AS borrower_objid, a.borrower_name AS bname, a.objid AS loanapp_objid, ac.dtreleased AS loanapp_dtreleased, 
			l.dtmatured AS loanapp_dtmatured, l.objid AS loanapp_ledgerid, r.code AS route_code, r.description AS route_description, 
			r.area AS route_area, l.balance AS loanapp_balance, b.address AS loanapp_address,
			(SELECT objid FROM loanapp_borrower WHERE parentid = l.appid AND `type` = 'JOINT' LIMIT 1) AS jointid,
			(SELECT objid FROM loan_ledger_payment WHERE parentid = l.objid ORDER BY txndate DESC LIMIT 1) AS lastpaymentid
		FROM loanapp a
		INNER JOIN loanapp_capture ac ON a.objid = ac.objid
		INNER JOIN loan_ledger l ON a.objid = l.appid
		INNER JOIN loan_route r ON a.route_code = r.code
		INNER JOIN loan_ledger_segregation s ON l.objid = s.refid
		INNER JOIN borrower b ON a.borrower_objid = b.objid
		WHERE l.state = 'OPEN'
			AND r.code = $P{routecode}
			AND s.typeid = $P{typeid}
		ORDER BY b.address, a.borrower_name, ac.dtreleased DESC
	) q LEFT JOIN loan_ledger_payment p ON q.lastpaymentid = p.objid
) q LEFT JOIN loanapp_borrower lb ON q.jointid = lb.objid

[xxgetLoansByRouteAndSegregationType]
SELECT q.*,
	CASE WHEN lb.objid IS NOT NULL THEN
		CONCAT(q.bname, ' AND ', lb.borrowername)
	ELSE
		q.bname
	END AS borrower_name
FROM (
	SELECT a.appno AS loanapp_appno, a.apptype AS loanapp_apptype, a.loanamount AS loanapp_loanamount, a.loantype AS loanapp_loantype,
		a.borrower_objid AS borrower_objid, a.borrower_name AS bname, a.objid AS loanapp_objid, ac.dtreleased AS loanapp_dtreleased, 
		l.dtmatured AS loanapp_dtmatured, l.objid AS loanapp_ledgerid, r.code AS route_code, r.description AS route_description, 
		r.area AS route_area, l.balance AS loanapp_balance, b.address AS loanapp_address,
		(SELECT objid FROM loanapp_borrower WHERE parentid = l.appid AND `type` = 'JOINT' LIMIT 1) AS jointid
	FROM loanapp a
	INNER JOIN loanapp_capture ac ON a.objid = ac.objid
	INNER JOIN loan_ledger l ON a.objid = l.appid
	INNER JOIN loan_route r ON a.route_code = r.code
	INNER JOIN loan_ledger_segregation s ON l.objid = s.refid
	INNER JOIN borrower b ON a.borrower_objid = b.objid
	WHERE l.state = 'OPEN'
		AND r.code = $P{routecode}
		AND s.typeid = $P{typeid}
	ORDER BY b.address, a.borrower_name, ac.dtreleased DESC
) q LEFT JOIN loanapp_borrower lb ON q.jointid = lb.objid

[xgetLoansByRouteAndSegregationType]
SELECT a.appno AS loanapp_appno, a.apptype AS loanapp_apptype, a.loanamount AS loanapp_loanamount, a.loantype AS loanapp_loantype,
	a.borrower_objid AS borrower_objid, a.borrower_name AS borrower_name, a.objid AS loanapp_objid, ac.dtreleased AS loanapp_dtreleased, 
	l.dtmatured AS loanapp_dtmatured, l.objid AS loanapp_ledgerid, r.code AS route_code, r.description AS route_description, 
	r.area AS route_area, l.balance AS loanapp_balance, b.address AS loanapp_address
FROM loanapp a
INNER JOIN loanapp_capture ac ON a.objid = ac.objid
INNER JOIN loan_ledger l ON a.objid = l.appid
INNER JOIN loan_route r ON a.route_code = r.code
INNER JOIN loan_ledger_segregation s ON l.objid = s.refid
INNER JOIN borrower b ON a.borrower_objid = b.objid
WHERE l.state = 'OPEN'
	AND r.code = $P{routecode}
	AND s.typeid = $P{typeid}
ORDER BY b.address, a.borrower_name, ac.dtreleased DESC