[getList]
SELECT r.*
FROM close_loanapp r
WHERE r.author_name LIKE $P{searchtext}
ORDER BY r.dtcreated DESC

[getListByState]
SELECT r.*
FROM close_loanapp r
WHERE r.author_name LIKE $P{searchtext}
	AND r.txnstate = $P{state}
ORDER BY r.dtcreated DESC

[getItems]
SELECT d.* FROM close_loanapp_detail d
WHERE d.parentid = $P{objid}

[getForCloseLoan]
SELECT a.borrower_objid, a.borrower_name, a.objid AS loanapp_objid, a.appno AS loanapp_appno,
	a.loanamount, DATE_SUB(l.dtstarted, INTERVAL 1 DAY) AS dtreleased, l.dtmatured
FROM loanapp a
INNER JOIN loan_ledger l ON a.objid = l.appid
WHERE l.state = 'OPEN'
	AND a.borrower_name LIKE $P{searchtext}
ORDER BY a.borrower_name, l.dtstarted DESC

[changeState]
UPDATE close_loanapp SET txnstate = $P{txnstate}
WHERE objid = $P{objid}