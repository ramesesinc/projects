[getList]
SELECT o.*
FROM (
	SELECT o.objid
	FROM overagefund_detail o
	WHERE o.refno LIKE $P{searchtext}
	UNION
	SELECT o.objid
	FROM overagefund_detail o
	WHERE o.loanapp_appno LIKE $P{searchtext}
	UNION
	SELECT o.objid 
	FROM overagefund_detail o 
	WHERE o.borrower_name LIKE $P{searchtext}
) q INNER JOIN overagefund_detail o ON q.objid = o.objid
ORDER BY o.txndate DESC, o.dtcreated DESC

[getListByState]
SELECT o.*
FROM (
	SELECT o.objid
	FROM overagefund_detail o
	WHERE o.refno LIKE $P{searchtext}
		AND o.txnstate = $P{state}
	UNION
	SELECT o.objid
	FROM overagefund_detail o
	WHERE o.loanapp_appno LIKE $P{searchtext}
		AND o.txnstate = $P{state}
	UNION
	SELECT o.objid 
	FROM overagefund_detail o 
	WHERE o.borrower_name LIKE $P{searchtext}
		AND o.txnstate = $P{state}
) q INNER JOIN overagefund_detail o ON q.objid = o.objid
ORDER BY o.txndate DESC, o.dtcreated DESC

[xgetList]
SELECT o.*
FROM overagefund_detail o
ORDER BY txndate DESC, dtcreated DESC

[findActiveFund]
SELECT * FROM overagefund
WHERE state = 'ACTIVE'

[findActiveFundByYear]
SELECT o.* FROM overagefund o
WHERE o.state = 'ACTIVE'
	AND o.year = $P{year}

[getActiveFunds]
SELECT o.* FROM overagefund o
WHERE o.state = 'ACTIVE'

[getOveragesByStartdateAndEnddate]
SELECT o.* FROM overage o
WHERE o.txndate BETWEEN $P{startdate} AND $P{enddate}

[changeDetailState]
UPDATE overagefund_detail SET txnstate = $P{txnstate}
WHERE objid = $P{objid}