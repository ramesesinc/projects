[getList]
SELECT l.*
FROM (
	SELECT l.objid
	FROM ledgeramnesty_baddebt l
	WHERE l.borrower_name LIKE $P{searchtext}
	UNION
	SELECT l.objid
	FROM ledgeramnesty_baddebt l
	WHERE l.loanapp_appno LIKE $P{searchtext}
) q INNER JOIN ledgeramnesty_baddebt l ON q.objid = l.objid
ORDER BY l.dtcreated DESC

[getListByState]
SELECT l.*
FROM (
	SELECT l.objid
	FROM ledgeramnesty_baddebt l
	WHERE l.borrower_name LIKE $P{searchtext}
		AND l.txnstate = $P{state}
	UNION
	SELECT l.objid
	FROM ledgeramnesty_baddebt l
	WHERE l.loanapp_appno LIKE $P{searchtext}
		AND l.txnstate = $P{state}
) q INNER JOIN ledgeramnesty_baddebt l ON q.objid = l.objid
ORDER BY l.dtcreated DESC

[changeState]
UPDATE ledgeramnesty_baddebt SET txnstate = $P{txnstate}
WHERE objid = $P{objid}