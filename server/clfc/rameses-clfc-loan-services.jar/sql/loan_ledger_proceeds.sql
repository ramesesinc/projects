[getList]
SELECT a.*, b.acctid AS borrower_objid, b.acctname AS borrower_name
FROM loan_ledger_proceeds a
	INNER JOIN loan_ledger b ON a.parentid = b.objid

[getListByState]
SELECT a.*, b.acctid AS borrower_objid, b.acctname AS borrower_name
FROM loan_ledger_proceeds a
	INNER JOIN loan_ledger b ON a.parentid = b.objid
WHERE a.txnstate = $P{state}

[getListByLedgerid]
SELECT a.* FROM loan_ledger_proceeds a
WHERE a.parentid = $P{ledgerid}

[getListByLedgeridAndState]
SELECT a.* FROM loan_ledger_proceeds a
WHERE a.parentid = $P{ledgerid}
	AND a.txnstate = $P{state}

[findByRefid]
SELECT a.* FROM loan_ledger_proceeds a
WHERE a.refid = $P{refid}

[findCollectionProceedByRefid]
SELECT p.* FROM collection_proceed p
WHERE p.refid = $P{refid}

[findByIdWithInfo]
SELECT p.*, l.acctid AS borrower_objid, l.acctname AS borrower_name,
	a.objid AS loanapp_objid, a.appno AS loanapp_appno, r.code AS route_code,
	r.description AS route_description, r.area AS route_area, l.paymentmethod
FROM loan_ledger_proceeds p
INNER JOIN loan_ledger l ON p.parentid = l.objid
INNER JOIN loanapp a ON l.appid = a.objid
INNER JOIN loan_route r ON a.route_code = r.code
WHERE p.objid = $P{objid}

[changeState]
UPDATE loan_ledger_proceeds SET txnstate = $P{txnstate}
WHERE objid = $P{objid}