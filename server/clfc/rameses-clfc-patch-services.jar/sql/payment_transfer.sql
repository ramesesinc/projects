[getList]
SELECT ll.*, ll.acctid AS borrower_objid, ll.acctname AS borrower_name,
	l.objid AS loanapp_objid, l.appno AS loanapp_appno, l.loanamount AS loanapp_loanamount,
	lr.code AS route_code, lr.description AS route_description, lr.area AS route_area
FROM loan_ledger ll
INNER JOIN loanapp l ON ll.appid = l.objid
INNER JOIN loan_route lr ON l.route_code = lr.code
WHERE ll.acctname LIKE $P{searchtext}
ORDER BY ll.acctname, ll.dtstarted DESC

[getListByBorrowerid]
SELECT ll.*, ll.acctid AS borrower_objid, ll.acctname AS borrower_name,
	l.objid AS loanapp_objid, l.appno AS loanapp_appno, l.loanamount AS loanapp_loanamount,
	lr.code AS route_code, lr.description AS route_description, lr.area AS route_area
FROM loan_ledger ll
INNER JOIN loanapp l ON ll.appid = l.objid
INNER JOIN loan_route lr ON l.route_code = lr.code
WHERE ll.acctname LIKE $P{searchtext}
	AND ll.acctid = $P{borrowerid}
ORDER BY ll.acctname, ll.dtstarted DESC

[getPaymentsByParentidBetweenFromdateAndTodate]
SELECT *
FROM loan_ledger_payment
WHERE parentid = $P{objid}
	AND txndate BETWEEN $P{fromdate} AND $P{todate}
ORDER BY txndate