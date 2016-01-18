[getList]
SELECT r.*, a.objid AS loanapp_objid, a.appno AS loanapp_appno,
		a.borrower_objid, a.borrower_name
FROM loanapp_receivable r
INNER JOIN loanapp a ON r.loanappid = a.objid
WHERE a.borrower_name LIKE $P{searchtext}
ORDER BY r.dtcreated DESC

[getListByState]
SELECT r.*, a.objid AS loanapp_objid, a.appno AS loanapp_appno,
		a.borrower_objid, a.borrower_name
FROM loanapp_receivable r
INNER JOIN loanapp a ON r.loanappid = a.objid
WHERE a.borrower_name LIKE $P{searchtext}
	AND r.txnstate = $P{txnstate}
ORDER BY r.dtcreated DESC

[getCaptureList]
SELECT r.*, a.objid AS loanapp_objid, a.appno AS loanapp_appno,
		a.borrower_objid, a.borrower_name
FROM loanapp_receivable r
INNER JOIN loanapp a ON r.loanappid = a.objid
WHERE a.borrower_name LIKE $P{searchtext}
	AND r.txntype = 'CAPTURE'
ORDER BY r.dtcreated DESC

[getCaptureListByState]
SELECT r.*, a.objid AS loanapp_objid, a.appno AS loanapp_appno,
		a.borrower_objid, a.borrower_name
FROM loanapp_receivable r
INNER JOIN loanapp a ON r.loanappid = a.objid
WHERE a.borrower_name LIKE $P{searchtext}
	AND r.txnstate = $P{txnstate}
	AND r.txntype = 'CAPTURE'
ORDER BY r.dtcreated DESC

[getVoidRequestList]
SELECT v.*, a.objid AS loanapp_objid, a.appno AS loanapp_appno,
		a.borrower_objid, a.borrower_name
FROM loanapp_receivable_voidrequest v
INNER JOIN loanapp_receivable r ON v.receivableid = r.objid
INNER JOIN loanapp a ON r.loanappid = a.objid
WHERE a.borrower_name LIKE $P{searchtext}
ORDER BY v.dtcreated DESC

[getVoidRequestListByState]
SELECT v.*, a.objid AS loanapp_objid, a.appno AS loanapp_appno,
		a.borrower_objid, a.borrower_name
FROM loanapp_receivable_voidrequest v
INNER JOIN loanapp_receivable r ON v.receivableid = r.objid
INNER JOIN loanapp a ON r.loanappid = a.objid
WHERE a.borrower_name LIKE $P{searchtext}
	AND v.txnstate = $P{txnstate}
ORDER BY v.dtcreated DESC

[changeState]
UPDATE loanapp_receivable SET txnstate = $P{txnstate}
WHERE objid = $P{objid}