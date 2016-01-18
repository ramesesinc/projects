[getList]
SELECT s.*
FROM (
	SELECT s.objid
	FROM specialbillingaccount s
	WHERE s.borrower_name LIKE $P{searchtext}
	UNION
	SELECT s.objid
	FROM specialbillingaccount s
	WHERE s.loanapp_appno LIKE $P{searchtext}
) q INNER JOIN specialbillingaccount s ON q.objid = s.objid
ORDER BY s.borrower_name, s.dtcreated DESC

[getListByState]
SELECT s.*
FROM (
	SELECT s.objid
	FROM specialbillingaccount s
	WHERE s.borrower_name LIKE $P{searchtext}
		AND s.txnstate = $P{state}
	UNION
	SELECT s.objid
	FROM specialbillingaccount s
	WHERE s.loanapp_appno LIKE $P{searchtext}
		AND s.txnstate = $P{state}
) q INNER JOIN specialbillingaccount s ON q.objid = s.objid
ORDER BY s.borrower_name, s.dtcreated DESC

[getForSpecialBillingLedgerLookupList]
SELECT l.objid, a.borrower_objid, a.borrower_name, b.address AS borrower_address, a.objid AS loanapp_objid, 
	a.loanamount AS loanapp_amount, a.appno AS loanapp_appno, ac.dtreleased, l.dtmatured
FROM loan_ledger l
INNER JOIN loanapp a ON l.appid = a.objid
INNER JOIN borrower b ON a.borrower_objid = b.objid
LEFT JOIN loanapp_capture ac ON a.objid = ac.objid
WHERE l.state = 'OPEN'
	AND a.borrower_name LIKE $P{searchtext}
ORDER BY a.borrower_name, ac.dtreleased

[findByLedgeridAndState]
SELECT s.*
FROM specialbillingaccount s
WHERE s.txnstate = $P{state}
	AND s.ledger_objid = $P{ledgerid}
ORDER BY s.dtcreated DESC

[changeState]
UPDATE specialbillingaccount SET txnstate = $P{txnstate}
WHERE objid = $P{objid}