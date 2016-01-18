[getList]
SELECT b.*, d.borrower_name
FROM (
	SELECT b.objid
	FROM borrower_merge b
	WHERE b.author_name LIKE $P{searchtext}
	UNION
	SELECT b.objid
	FROM borrower_merge b
	INNER JOIN borrower_merge_detail d ON b.objid = d.parentid
	WHERE d.borrower_name LIKE $P{searchtext}
) a INNER JOIN borrower_merge b ON a.objid = b.objid
INNER JOIN borrower_merge_detail d ON b.objid = d.parentid
GROUP BY b.objid
HAVING b.objid IS NOT NULL
ORDER BY b.dtcreated DESC

[getListByState]
SELECT b.*, d.borrower_name
FROM (
	SELECT b.objid
	FROM borrower_merge b
	WHERE b.author_name LIKE $P{searchtext}
		AND b.state = $P{state}
	UNION
	SELECT b.objid
	FROM borrower_merge b
	INNER JOIN borrower_merge_detail d ON b.objid = d.parentid
	WHERE d.borrower_name LIKE $P{searchtext}
		AND b.state = $P{state}
) a INNER JOIN borrower_merge b ON a.objid = b.objid
INNER JOIN borrower_merge_detail d ON b.objid = d.parentid
GROUP BY b.objid
HAVING b.objid IS NOT NULL
ORDER BY b.dtcreated DESC

[getItems]
SELECT d.*
FROM borrower_merge_detail d
WHERE d.parentid = $P{objid}

[findByBorroweridAndState]
SELECT b.*
FROM borrower_merge_detail d
INNER JOIN borrower_merge b ON d.parentid = b.objid
WHERE d.borrower_objid = $P{borrowerid}
	AND b.state = $P{state}

[changeState]
UPDATE borrower_merge SET state = $P{state}
WHERE objid = $P{objid}