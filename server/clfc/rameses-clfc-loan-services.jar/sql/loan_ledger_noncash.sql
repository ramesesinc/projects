[getList]
SELECT n.*
FROM (
	SELECT n.objid
	FROM loan_ledger_noncash n
	WHERE n.borrower_name LIKE $P{searchtext}
		AND n.tag IS NULL
	UNION
	SELECT n.objid
	FROM loan_ledger_noncash n
	WHERE n.refno LIKE $P{searchtext}
		AND n.tag IS NULL
) a INNER JOIN loan_ledger_noncash n ON a.objid = n.objid
ORDER BY n.dtcreated DESC

[xgetList]
SELECT n.*
FROM (
	SELECT n.objid
	FROM loan_ledger_noncash n
	WHERE n.borrower_name LIKE $P{searchtext}
	UNION
	SELECT n.objid
	FROM loan_ledger_noncash n
	WHERE n.refno LIKE $P{searchtext}
) a INNER JOIN loan_ledger_noncash n ON a.objid = n.objid
ORDER BY n.dtcreated DESC

[getListByState]
SELECT n.*
FROM (
	SELECT n.objid
	FROM loan_ledger_noncash n
	WHERE n.borrower_name LIKE $P{searchtext}
		AND n.txnstate = $P{state}
		AND n.tag IS NULL
	UNION
	SELECT n.objid
	FROM loan_ledger_noncash n
	WHERE n.refno LIKE $P{searchtext}
		AND n.txnstate = $P{state}
		AND n.tag IS NULL
) a INNER JOIN loan_ledger_noncash n ON a.objid = n.objid
ORDER BY n.dtcreated DESC

[findByRefid]
SELECT a.* FROM loan_ledger_noncash a
WHERE a.refid = $P{refid}

[findCollectionNoncashByRefid]
SELECT n.* FROM collection_noncash n
WHERE n.refid = $P{refid}

[findByIdWithInfo]
SELECT p.*, l.acctid AS borrower_objid, l.acctname AS borrower_name,
	a.objid AS loanapp_objid, a.appno AS loanapp_appno, r.code AS route_code,
	r.description AS route_description, r.area AS route_area, l.paymentmethod
FROM loan_ledger_noncash p
INNER JOIN loan_ledger l ON p.parentid = l.objid
INNER JOIN loanapp a ON l.appid = a.objid
INNER JOIN loan_route r ON a.route_code = r.code
WHERE p.objid = $P{objid}

[changeState]
UPDATE loan_ledger_noncash SET txnstate = $P{txnstate}
WHERE objid = $P{objid}