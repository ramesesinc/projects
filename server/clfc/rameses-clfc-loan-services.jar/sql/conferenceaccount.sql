[getList]
SELECT c.*
FROM conferenceaccount c
WHERE c.refno LIKE $P{searchtext}
ORDER BY c.dtcreated DESC

[getListByState]
SELECT c.*
FROM conferenceaccount c
WHERE c.refno LIKE $P{searchtext}
	AND c.txnstate = $P{state}
ORDER BY c.dtcreated DESC

[getForConferenceLedgerLookupList]
SELECT l.objid, a.borrower_objid, a.borrower_name, b.address AS borrower_address, a.objid AS loanapp_objid, 
	a.loanamount AS loanapp_amount, a.appno AS loanapp_appno, ac.dtreleased, l.dtmatured
FROM loan_ledger l
INNER JOIN loanapp a ON l.appid = a.objid
INNER JOIN borrower b ON a.borrower_objid = b.objid
LEFT JOIN loanapp_capture ac ON a.objid = ac.objid
WHERE l.state = 'OPEN'
	AND a.borrower_name LIKE $P{searchtext}
ORDER BY a.borrower_name, ac.dtreleased

[getDetails]
SELECT d.*
FROM conferenceaccount_detail d
WHERE d.parentid = $P{objid}

[changeState]
UPDATE conferenceaccount SET txnstate = $P{txnstate}
WHERE objid = $P{objid}
