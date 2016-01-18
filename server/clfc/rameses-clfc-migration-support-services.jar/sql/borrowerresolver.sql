[getList]
SELECT b.*,
	CASE
		WHEN r.objid IS NULL AND e.objid IS NOT NULL THEN 'UNRESOLVED' 
		WHEN r.objid IS NOT NULL THEN 'RESOLVED'
		ELSE 'ALS'
	END AS state
FROM (
	SELECT b.objid FROM borrower b
	WHERE b.borrowername LIKE $P{searchtext}	
	UNION
	SELECT b.objid FROM borrower b
	WHERE b.objid LIKE $P{searchtext}
) a 
	INNER JOIN borrower b ON a.objid = b.objid
	LEFT JOIN borrower_resolved r ON b.objid = r.objid
	LEFT JOIN borrower_extinfo e ON b.objid = e.objid
ORDER BY b.borrowername

[getResolvedList]
SELECT b.*, 'RESOLVED' AS state, e.lastname, e.firstname, e.middlename
FROM (
	SELECT b.objid FROM borrower b
	INNER JOIN borrower_resolved r ON b.objid = r.objid
	WHERE b.borrowername LIKE $P{searchtext}	
	UNION
	SELECT b.objid FROM borrower b
	INNER JOIN borrower_resolved r ON b.objid = r.objid
	WHERE b.objid LIKE $P{searchtext}
) a 
	INNER JOIN borrower b ON a.objid = b.objid
	INNER JOIN borrower_extinfo e ON b.objid = e.objid
ORDER BY b.borrowername

[getUnresolvedList]
SELECT b.*, 'UNRESOLVED' AS state
FROM (
	SELECT b.objid FROM borrower b
	INNER JOIN borrower_extinfo e ON e.objid = b.objid
	LEFT JOIN borrower_resolved r ON b.objid = r.objid
	WHERE r.objid IS NULL
		AND b.borrowername LIKE $P{searchtext}
	UNION
	SELECT b.objid FROM borrower b
	INNER JOIN borrower_extinfo e ON e.objid = b.objid
	LEFT JOIN borrower_resolved r ON b.objid = r.objid
	WHERE r.objid IS NULL
		AND b.objid LIKE $P{searchtext}
) a 
	INNER JOIN borrower b ON a.objid = b.objid
ORDER BY b.borrowername

[getALSList]
SELECT b.*, 'ALS' AS state
FROM (
	SELECT b.objid FROM borrower b
	LEFT JOIN borrower_extinfo e ON e.objid = b.objid
	WHERE e.objid IS NULL
		AND b.borrowername LIKE $P{searchtext}
	UNION
	SELECT b.objid FROM borrower b
	LEFT JOIN borrower_extinfo e ON e.objid = b.objid
	WHERE e.objid IS NULL
		AND b.objid LIKE $P{searchtext}
) a 
	INNER JOIN borrower b ON a.objid = b.objid
ORDER BY b.borrowername

[getLookupList]
SELECT b.*,
	CASE
		WHEN r.objid IS NULL AND e.objid IS NOT NULL THEN 'UNRESOLVED' 
		WHEN r.objid IS NOT NULL THEN 'RESOLVED'
		ELSE 'ALS'
	END AS state,
	e.lastname, e.firstname, e.middlename
FROM (
	SELECT b.objid FROM borrower b
	WHERE b.borrowername LIKE $P{searchtext}	
	UNION
	SELECT b.objid FROM borrower b
	WHERE b.objid LIKE $P{searchtext}
) a 
	INNER JOIN borrower b ON a.objid = b.objid
	LEFT JOIN borrower_resolved r ON b.objid = r.objid
	LEFT JOIN borrower_extinfo e ON b.objid = e.objid
ORDER BY b.borrowername

[getForMigrationList]
SELECT b.*
FROM (
	SELECT DISTINCT br.objid
	FROM loan_resolved lr
	INNER JOIN loan l ON lr.objid = l.objid
	INNER JOIN borrower_resolved br ON l.borrowerid = br.objid
	WHERE lr.taskkey = $P{taskkey}
) a INNER JOIN borrower b ON a.objid = b.objid
ORDER BY b.borrowername

[updateResolvedBorrowerKey]
UPDATE borrower_resolved SET taskkey = $P{taskkey}
WHERE taskkey IS NULL
LIMIT 15

[getResolvedBorrowersWithoutKey]
SELECT objid FROM borrower_resolved
WHERE taskkey IS NULL
