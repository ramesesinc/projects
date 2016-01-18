[getList]
SELECT qry.*
FROM (
	SELECT * FROM teller
	WHERE `name` LIKE $P{searchtext}
	UNION
	SELECT * FROM teller
	WHERE tellerno = $P{searchtext}
) qry
ORDER BY qry.tellerno

[getListByState]
SELECT qry.*
FROM (
	SELECT * FROM teller
	WHERE `name` LIKE $P{searchtext}
		AND state = $P{state}
	UNION
	SELECT * FROM teller
	WHERE tellerno = $P{searchtext}
		AND state = $P{state}
) qry
ORDER BY qry.tellerno

[getActiveList]
SELECT qry.*
FROM (
	SELECT * FROM teller
	WHERE `name` LIKE $P{searchtext}
	UNION
	SELECT * FROM teller
	WHERE tellerno = $P{searchtext}
) qry
	INNER JOIN teller_active t ON qry.objid = t.objid
ORDER BY qry.tellerno

[changeState]
UPDATE teller SET state = $P{state}
WHERE objid = $P{objid}