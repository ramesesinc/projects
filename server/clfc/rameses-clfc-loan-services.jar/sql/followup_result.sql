[getList]
SELECT f.*
FROM followup_result f
WHERE f.borrower_name LIKE $P{searchtext}
ORDER BY f.dtcreated DESC

[getListByState]
SELECT f.*
FROM followup_result f
WHERE f.borrower_name LIKE $P{searchtext}
	AND f.txnstate = $P{state}
ORDER BY f.dtcreated DESC

[getLookupBorrowers]
SELECT b.*, a.borrower_objid, a.borrower_name, a.objid AS loanapp_objid,
	a.appno AS loanapp_appno, d.objid
FROM (
	SELECT b.billdate, i.objid AS itemid, s.txntype, 
		s.collector_objid, s.collector_name
	FROM fieldcollection b
	INNER JOIN ledger_billing_item i ON b.objid = i.parentid
	INNER JOIN specialcollection s ON i.objid = s.objid
	INNER JOIN followupcollection f ON s.objid = f.objid
) b 
INNER JOIN ledger_billing_detail d ON b.itemid = d.parentid
INNER JOIN loanapp a ON d.loanappid = a.objid
WHERE a.borrower_name LIKE $P{searchtext}
ORDER BY b.billdate DESC, a.borrower_name

[getLookupBorrowersByDate]
SELECT b.*, a.borrower_objid, a.borrower_name, a.objid AS loanapp_objid,
	a.appno AS loanapp_appno, d.objid
FROM (
	SELECT b.billdate, i.objid AS itemid, s.txntype, 
		s.collector_objid, s.collector_name
	FROM fieldcollection b
	INNER JOIN ledger_billing_item i ON b.objid = i.parentid
	INNER JOIN specialcollection s ON i.objid = s.objid
	INNER JOIN followupcollection f ON s.objid = f.objid
	WHERE b.billdate = $P{date}
) b 
INNER JOIN ledger_billing_detail d ON b.itemid = d.parentid
INNER JOIN loanapp a ON d.loanappid = a.objid
WHERE a.borrower_name LIKE $P{searchtext}
ORDER BY b.billdate DESC, a.borrower_name

[findFieldCollectionLoanWithNoFollowupResultByItemid]
SELECT l.*
FROM fieldcollection_loan l 
LEFT JOIN followup_result f ON l.objid = f.refid
WHERE f.objid IS NULL
	AND l.parentid = $P{itemid}	
	
[findBillingDetailWithFollowupResultByItemid]
SELECT i.*
FROM ledger_billing_detail i 
INNER JOIN followup_result f ON i.objid = f.refid
WHERE i.parentid = $P{itemid}
	AND f.txnstate = 'CONFIRMED'
