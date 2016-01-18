[getList]
SELECT a.*
FROM (
	SELECT a.*
	FROM ledgeramnesty_fix a
	WHERE a.borrower_name LIKE $P{searchtext}
	UNION
	SELECT a.*
	FROM ledgeramnesty_fix a
	WHERE a.loanapp_appno LIKE $P{searchtext}
) q INNER JOIN ledgeramnesty_fix a ON q.objid = a.objid
ORDER BY a.dtcreated DESC, a.borrower_name

[getListByState]
SELECT a.*
FROM (
	SELECT a.*
	FROM ledgeramnesty_fix a
	WHERE a.borrower_name LIKE $P{searchtext}
		AND a.txnstate = $P{state}
	UNION
	SELECT a.*
	FROM ledgeramnesty_fix a
	WHERE a.loanapp_appno LIKE $P{searchtext}
		AND a.txnstate = $P{state}
) q INNER JOIN ledgeramnesty_fix a ON q.objid = a.objid
ORDER BY a.dtcreated DESC, a.borrower_name

[getItems]
SELECT i.*
FROM ledgeramnesty_fix_item i
WHERE i.parentid = $P{objid}
ORDER BY i.index

[getLookupList]
SELECT d.*
FROM ledgeramnesty a
INNER JOIN ledgeramnesty_detail d ON a.objid = d.parentid
WHERE a.txnstate = 'RETURNED'
	AND d.state = 'APPROVED'
	AND d.amnestytype_value = 'FIX'

[getLookupListByAmnestyid]
SELECT d.*
FROM ledgeramnesty a
INNER JOIN ledgeramnesty_detail d ON a.objid = d.parentid
WHERE a.txnstate = 'RETURNED'
	AND d.state = 'APPROVED'
	AND d.amnestytype_value = 'FIX'
	AND a.objid = $P{amnestyid}

[changeState]
UPDATE ledgeramnesty_fix SET txnstate = $P{txnstate}
WHERE objid = $P{objid}