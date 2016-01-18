[getList]
SELECT s.*
FROM branch_loan_settings s
WHERE s.yearstarted LIKE $P{searchtext}

[getListByState]
SELECT s.*
FROM branch_loan_settings s
WHERE s.yearstarted LIKE $P{searchtext}
	AND s.txnstate = $P{state}

[findActiveSettingByYear]
SELECT s.* FROM branch_loan_settings s
WHERE $P{year} BETWEEN s.yearstarted AND s.yearended
	AND s.txnstate = 'ACTIVE'

[findActiveOverlapping]
SELECT b.*
FROM (
	SELECT s.objid, s.yearstarted AS startyear, CASE WHEN s.yearended IS NULL THEN YEAR(CURDATE()) ELSE s.yearended END AS endyear
	FROM branch_loan_settings s
	WHERE s.txnstate = 'ACTIVE'
	GROUP BY s.objid
	HAVING $P{yearstarted} BETWEEN startyear AND endyear
	UNION	
	SELECT s.objid, s.yearstarted AS startyear, CASE WHEN s.yearended IS NULL THEN YEAR(CURDATE()) ELSE s.yearended END AS endyear
	FROM branch_loan_settings s
	WHERE s.txnstate = 'ACTIVE'
	GROUP BY s.objid
	HAVING $P{yearended} BETWEEN startyear AND endyear
) a INNER JOIN branch_loan_settings b ON a.objid = b.objid

[changeState]
UPDATE branch_loan_settings SET txnstate = $P{txnstate}
WHERE objid = $P{objid}