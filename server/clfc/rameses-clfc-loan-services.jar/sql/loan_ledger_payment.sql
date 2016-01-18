[getList]
SELECT p.*, CASE WHEN s.objid IS NOT NULL THEN 1 ELSE 0 END AS isproceedcollection,
	CASE WHEN n.objid IS NOT NULL THEN 1 ELSE 0 END AS isnoncashcollection
FROM loan_ledger_payment p
	LEFT JOIN loan_ledger_proceeds s ON p.objid = s.refid
	LEFT JOIN loan_ledger_noncash n ON p.objid = n.refid
WHERE p.parentid = $P{objid}
ORDER BY p.txndate, p.refno

[getPaymentsByParentidAndTxndate]
SELECT p.*
FROM loan_ledger_payment p
WHERE p.parentid = $P{parentid}
	AND p.txndate = $P{txndate}

[findByParentidAndRefno]
SELECT p.*
FROM loan_ledger_payment p
WHERE p.parentid = $P{parentid}
	AND p.refno = $P{refno}

[findByParentidAndRefnoAndTxndate]
SELECT p.*
FROM loan_ledger_payment p
WHERE p.parentid = $P{parentid}
	AND p.refno = $P{refno}
	AND p.txndate = $P{txndate}

[findByParentid]
SELECT p.* FROM loan_ledger_payment p
WHERE p.parentid = $P{parentid}

[findLastPayment]
SELECT p.* FROM loan_ledger_payment p
WHERE p.parentid = $P{parentid}
ORDER BY p.txndate DESC