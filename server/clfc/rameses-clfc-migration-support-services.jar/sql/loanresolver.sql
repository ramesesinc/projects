[getList]
SELECT l.*, b.borrowername AS acctname,
	CASE 
		WHEN r.objid IS NULL AND e.objid IS NOT NULL THEN 'UNRESOLVED' 
		WHEN r.objid IS NOT NULL THEN 'RESOLVED' 
		ELSE 'ALS'
	END AS state
FROM (
	SELECT l.objid
	FROM loan l
	WHERE l.objid LIKE $P{searchtext}
	UNION 
	SELECT l.objid
	FROM loan l
	WHERE l.borrowerid LIKE $P{searchtext}
	UNION
	SELECT l.objid
	FROM loan l
	INNER JOIN borrower b ON l.borrowerid = b.objid
	WHERE b.borrowername LIKE $P{searchtext}
) a 
	INNER JOIN loan l ON a.objid = l.objid
	INNER JOIN borrower b ON b.objid = l.borrowerid
	LEFT JOIN loan_resolved r ON l.objid = r.objid
	LEFT JOIN loan_extinfo e ON l.objid = e.objid
ORDER BY b.borrowername, l.loandate DESC

[getListByBorrowerid]
SELECT l.*, b.borrowername AS acctname,
	CASE 
		WHEN r.objid IS NULL AND e.objid IS NOT NULL THEN 'UNRESOLVED' 
		WHEN r.objid IS NOT NULL THEN 'RESOLVED' 
		ELSE 'ALS'
	END AS state
FROM (
	SELECT l.objid
	FROM loan l
	WHERE l.objid LIKE $P{searchtext}
		AND l.borrowerid = $P{borrowerid}
	UNION 
	SELECT l.objid
	FROM loan l
	WHERE l.borrowerid LIKE $P{searchtext}
		AND l.borrowerid = $P{borrowerid}
	UNION
	SELECT l.objid
	FROM loan l
	INNER JOIN borrower b ON l.borrowerid = b.objid
	WHERE b.borrowername LIKE $P{searchtext}
		AND l.borrowerid = $P{borrowerid}
) a 
	INNER JOIN loan l ON a.objid = l.objid
	INNER JOIN borrower b ON b.objid = l.borrowerid
	LEFT JOIN loan_resolved r ON l.objid = r.objid
	LEFT JOIN loan_extinfo e ON l.objid = e.objid
ORDER BY b.borrowername, l.loandate DESC


[getResolvedList]
SELECT l.*, b.borrowername AS acctname, 'RESOLVED' AS state
FROM (
	SELECT l.objid FROM loan l
	INNER JOIN loan_resolved r ON l.objid = r.objid
	WHERE l.objid LIKE $P{searchtext}
	UNION 
	SELECT l.objid FROM loan l
	INNER JOIN loan_resolved r ON l.objid = r.objid
	WHERE l.borrowerid LIKE $P{searchtext}
	UNION
	SELECT l.objid FROM loan l
	INNER JOIN loan_resolved r ON l.objid = r.objid
	INNER JOIN borrower b ON l.borrowerid = b.objid
	WHERE b.borrowername LIKE $P{searchtext}
) a 
	INNER JOIN loan l ON a.objid = l.objid
	INNER JOIN borrower b ON b.objid = l.borrowerid
ORDER BY b.borrowername, l.loandate DESC


[getResolvedListByBorrowerid]
SELECT l.*, b.borrowername AS acctname, 'RESOLVED' AS state
FROM (
	SELECT l.objid FROM loan l
	INNER JOIN loan_resolved r ON l.objid = r.objid
	WHERE l.objid LIKE $P{searchtext}
		AND l.borrowerid = $P{borrowerid}
	UNION 
	SELECT l.objid FROM loan l
	INNER JOIN loan_resolved r ON l.objid = r.objid
	WHERE l.borrowerid LIKE $P{searchtext}
		AND l.borrowerid = $P{borrowerid}
	UNION
	SELECT l.objid FROM loan l
	INNER JOIN loan_resolved r ON l.objid = r.objid
	INNER JOIN borrower b ON l.borrowerid = b.objid
	WHERE b.borrowername LIKE $P{searchtext}
		AND l.borrowerid = $P{borrowerid}
) a 
	INNER JOIN loan l ON a.objid = l.objid
	INNER JOIN borrower b ON b.objid = l.borrowerid
ORDER BY b.borrowername, l.loandate DESC

[getUnresolvedList]
SELECT l.*, b.borrowername AS acctname, 'UNRESOLVED' AS state
FROM (
	SELECT l.objid FROM loan l
	INNER JOIN loan_extinfo e ON l.objid = e.objid
	LEFT JOIN loan_resolved r ON l.objid = r.objid
	WHERE l.objid LIKE $P{searchtext}
		AND r.objid IS NULL
	UNION 
	SELECT l.objid FROM loan l
	INNER JOIN loan_extinfo e ON l.objid = e.objid
	LEFT JOIN loan_resolved r ON l.objid = r.objid
	WHERE l.borrowerid LIKE $P{searchtext}
		AND r.objid IS NULL
	UNION
	SELECT l.objid FROM loan l
	INNER JOIN loan_extinfo e ON l.objid = e.objid
	LEFT JOIN loan_resolved r ON l.objid = r.objid
	INNER JOIN borrower b ON l.borrowerid = b.objid
	WHERE b.borrowername LIKE $P{searchtext}
		AND r.objid IS NULL
) a 
	INNER JOIN loan l ON a.objid = l.objid
	INNER JOIN borrower b ON b.objid = l.borrowerid
ORDER BY b.borrowername, l.loandate DESC


[getUnresolvedListByBorrowerid]
SELECT l.*, b.borrowername AS acctname, 'UNRESOLVED' AS state
FROM (
	SELECT l.objid FROM loan l
	INNER JOIN loan_extinfo e ON l.objid = e.objid
	LEFT JOIN loan_resolved r ON l.objid = r.objid
	WHERE l.objid LIKE $P{searchtext}
		AND r.objid IS NULL
		AND l.borrowerid = $P{borrowerid}
	UNION 
	SELECT l.objid FROM loan l
	INNER JOIN loan_extinfo e ON l.objid = e.objid
	LEFT JOIN loan_resolved r ON l.objid = r.objid
	WHERE l.borrowerid LIKE $P{searchtext}
		AND r.objid IS NULL
		AND l.borrowerid = $P{borrowerid}
	UNION
	SELECT l.objid FROM loan l
	INNER JOIN loan_extinfo e ON l.objid = e.objid
	LEFT JOIN loan_resolved r ON l.objid = r.objid
	INNER JOIN borrower b ON l.borrowerid = b.objid
	WHERE b.borrowername LIKE $P{searchtext}
		AND r.objid IS NULL
		AND l.borrowerid = $P{borrowerid}
) a 
	INNER JOIN loan l ON a.objid = l.objid
	INNER JOIN borrower b ON b.objid = l.borrowerid
ORDER BY b.borrowername, l.loandate DESC

[getALSList]
SELECT l.*, b.borrowername AS acctname, 'ALS' AS state
FROM (
	SELECT l.objid FROM loan l
	LEFT JOIN loan_extinfo e ON l.objid = e.objid
	WHERE l.objid LIKE $P{searchtext}
		AND e.objid IS NULL 
	UNION 
	SELECT l.objid FROM loan l
	LEFT JOIN loan_extinfo e ON l.objid = e.objid
	WHERE l.borrowerid LIKE $P{searchtext}
		AND e.objid IS NULL 
	UNION
	SELECT l.objid FROM loan l
	INNER JOIN borrower b ON l.borrowerid = b.objid
	LEFT JOIN loan_extinfo e ON l.objid = e.objid
	WHERE b.borrowername LIKE $P{searchtext}
		AND e.objid IS NULL 
) a 
	INNER JOIN loan l ON a.objid = l.objid
	INNER JOIN borrower b ON b.objid = l.borrowerid
ORDER BY b.borrowername, l.loandate DESC


[getALSListByBorrowerid]
SELECT l.*, b.borrowername AS acctname, 'ALS' AS state
FROM (
	SELECT l.objid FROM loan l
	LEFT JOIN loan_extinfo e ON l.objid = e.objid
	WHERE l.objid LIKE $P{searchtext}
		AND e.objid IS NULL 
		AND l.borrowerid = $P{borrowerid}
	UNION 
	SELECT l.objid FROM loan l
	LEFT JOIN loan_extinfo e ON l.objid = e.objid
	WHERE l.borrowerid LIKE $P{searchtext}
		AND e.objid IS NULL 
		AND l.borrowerid = $P{borrowerid}
	UNION
	SELECT l.objid FROM loan l
	INNER JOIN borrower b ON l.borrowerid = b.objid
	LEFT JOIN loan_extinfo e ON l.objid = e.objid
	WHERE b.borrowername LIKE $P{searchtext}
		AND e.objid IS NULL 
		AND l.borrowerid = $P{borrowerid}
) a 
	INNER JOIN loan l ON a.objid = l.objid
	INNER JOIN borrower b ON b.objid = l.borrowerid
ORDER BY b.borrowername, l.loandate DESC


[getLookupList]
SELECT l.*, b.borrowername,
	CASE 
		WHEN r.objid IS NULL AND e.objid IS NOT NULL THEN 'UNRESOLVED' 
		WHEN r.objid IS NOT NULL THEN 'RESOLVED' 
		ELSE 'ALS'
	END AS state
FROM (
	SELECT l.objid
	FROM loan l
	WHERE l.objid LIKE $P{searchtext}
	UNION 
	SELECT l.objid
	FROM loan l
	WHERE l.borrowerid LIKE $P{searchtext}
	UNION
	SELECT l.objid
	FROM loan l
	INNER JOIN borrower b ON l.borrowerid = b.objid
	WHERE b.borrowername LIKE $P{searchtext}
) a 
	INNER JOIN loan l ON a.objid = l.objid
	INNER JOIN borrower b ON b.objid = l.borrowerid
	LEFT JOIN loan_resolved r ON l.objid = r.objid
	LEFT JOIN loan_extinfo e ON l.objid = e.objid
ORDER BY b.borrowername, l.loandate DESC

[findLoanBorrowerByLoanid]
SELECT b.*
FROM borrower b
INNER JOIN borrower_loan_resolved r ON b.objid = r.borrowerid
WHERE r.loanid = $P{loanid}

[findLoanBorrowerConnectionByLoanid]
SELECT r.*
FROM borrower_loan_resolved r
WHERE r.loanid = $P{loanid}

[getBorrowerResolvedLoans]
SELECT l.*
FROM borrower_loan_resolved a
INNER JOIN loan l ON a.loanid = l.objid
INNER JOIN loan_resolved r ON l.objid = r.objid
WHERE a.borrowerid = $P{borrowerid}

[removeBorrowerResolvedLoanByLoanidAndBorrowerid]
DELETE FROM borrower_loan_resolved
WHERE loanid = $P{loanid}
	AND borrowerid = $P{borrowerid}

[getForMigrationList]
SELECT l.*
FROM loan l
INNER JOIN loan_resolved r ON l.objid = r.objid
WHERE l.borrowerid = $P{borrowerid}
ORDER BY l.objid

[getRemainingLoansForMigration]
SELECT l.*
FROM (
	SELECT l.objid
	FROM loan l
	LEFT JOIN loan_process p ON l.objid = p.objid
	WHERE p.objid IS NULL
		AND l.borrowerid = $P{borrowerid}
	UNION
	SELECT l.objid
	FROM loan l
	INNER JOIN loan_process p ON l.objid = p.objid
	WHERE p.key IS NULL AND p.isprocessed = 0
		AND l.borrowerid = $P{borrowerid}
) a INNER JOIN loan l ON a.objid = l.objid
ORDER BY l.objid

[updateResolvedLoanKey]
UPDATE loan_resolved SET taskkey = $P{taskkey}
WHERE taskkey IS NULL
LIMIT 15

[getResolvedLoansWithoutKey]
SELECT objid FROM loan_resolved
WHERE taskkey IS NULL