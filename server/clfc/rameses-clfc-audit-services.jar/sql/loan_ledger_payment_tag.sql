[getPayments]
SELECT p.*
FROM loan_ledger_payment p
WHERE p.parentid = $P{objid}
ORDER BY p.txndate DESC, p.refno

[getNoncashPayments]
SELECT p.*, CASE WHEN nc.objid IS NULL THEN FALSE ELSE TRUE END AS hastag
FROM (
	SELECT p.objid
	FROM loan_ledger_payment p
	LEFT JOIN loan_ledger_proceeds pr ON p.objid = pr.refid
	WHERE pr.objid IS NULL 
		AND p.isonline = 1
		${filter}
) a 
	INNER JOIN loan_ledger_payment p ON a.objid = p.objid
	LEFT JOIN loan_ledger_noncash nc ON p.objid = nc.refid
ORDER BY p.txndate DESC, p.refno

[getProceedsPayments]
SELECT p.*, CASE WHEN pr.objid IS NULL THEN FALSE ELSE TRUE END AS hastag
FROM (
	SELECT p.objid
	FROM loan_ledger_payment p
	LEFT JOIN loan_ledger_noncash nc ON p.objid = nc.refid
	WHERE nc.objid IS NULL
		AND p.isonline = 1
		${filter}
) a
	INNER JOIN loan_ledger_payment p ON a.objid = p.objid
	LEFT JOIN loan_ledger_proceeds pr ON p.objid = pr.refid
ORDER BY p.txndate DESC, p.refno

[getLedgers]
SELECT l.objid, l.acctid AS borrower_objid, l.acctname AS borrower_name, ap.appno,
	ac.dtreleased, l.dtmatured
FROM (
	SELECT l.objid
	FROM loan_ledger l
	WHERE l.acctname LIKE $P{searchtext}
	UNION
	SELECT l.objid
	FROM loan_ledger l
	INNER JOIN loanapp a ON l.appid = a.objid
	WHERE a.appno LIKE $P{searchtext}
) a 
	INNER JOIN loan_ledger l ON a.objid = l.objid
	INNER JOIN loanapp ap ON l.appid = ap.objid
	INNER JOIN loanapp_capture ac ON ap.objid = ac.objid
ORDER BY l.acctname, ap.appno DESC