[getLookupList]
SELECT p.*, a.objid AS loanapp_objid, a.appno AS loanapp_appno
FROM (
	SELECT a.objid
	FROM loan_ledger_proceeds a
	WHERE a.refno LIKE $P{searchtext}
	UNION
	SELECT a.objid
	FROM loan_ledger_proceeds a
	WHERE a.borrower_name LIKE $P{searchtext}
) b INNER JOIN loan_ledger_proceeds p ON b.objid = p.objid
INNER JOIN loan_ledger l ON p.parentid = l.objid
INNER JOIN loanapp a ON l.appid = a.objid
ORDER BY p.dtcreated DESC

[getLookupListByState]
SELECT p.*, a.objid AS loanapp_objid, a.appno AS loanapp_appno
FROM (
	SELECT a.objid
	FROM loan_ledger_proceeds a
	WHERE a.refno LIKE $P{searchtext}
		AND a.txnstate = $P{state}
	UNION
	SELECT a.objid
	FROM loan_ledger_proceeds a
	WHERE a.borrower_name LIKE $P{searchtext}
		AND a.txnstate = $P{state}
) b INNER JOIN loan_ledger_proceeds p ON b.objid = p.objid
INNER JOIN loan_ledger l ON p.parentid = l.objid
INNER JOIN loanapp a ON l.appid = a.objid
ORDER BY p.dtcreated DESC

[getLookupListByTxntype]
SELECT p.*, a.objid AS loanapp_objid, a.appno AS loanapp_appno
FROM (
	SELECT a.objid
	FROM loan_ledger_proceeds a
	WHERE a.refno LIKE $P{searchtext}
		AND a.txntype = $P{txntype}
	UNION
	SELECT a.objid
	FROM loan_ledger_proceeds a
	WHERE a.borrower_name LIKE $P{searchtext}
		AND a.txntype = $P{txntype}
) b INNER JOIN loan_ledger_proceeds p ON b.objid = p.objid
INNER JOIN loan_ledger l ON p.parentid = l.objid
INNER JOIN loanapp a ON l.appid = a.objid
ORDER BY p.dtcreated DESC

[getLookupListByStateAndTxntype]
SELECT p.*, a.objid AS loanapp_objid, a.appno AS loanapp_appno
FROM (
	SELECT a.objid
	FROM loan_ledger_proceeds a
	WHERE a.refno LIKE $P{searchtext}
		AND a.txnstate = $P{state}
		AND a.txntype = $P{txntype}
	UNION
	SELECT a.objid
	FROM loan_ledger_proceeds a
	WHERE a.borrower_name LIKE $P{searchtext}
		AND a.txnstate = $P{state}
		AND a.txntype = $P{txntype}
) b INNER JOIN loan_ledger_proceeds p ON b.objid = p.objid
INNER JOIN loan_ledger l ON p.parentid = l.objid
INNER JOIN loanapp a ON l.appid = a.objid
ORDER BY p.dtcreated DESC