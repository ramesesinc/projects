[getList]
SELECT a.*
FROM amnesty_update a
WHERE a.amnesty_refno LIKE $P{searchtext}
ORDER BY a.dtcreated DESC

[getListByState]
SELECT a.*
FROM amnesty_update a
WHERE a.amnesty_refno LIKE $P{searchtext}
	AND a.txnstate = $P{state}
ORDER BY a.dtcreated DESC

[getActiveAmnesty]
SELECT am.*
FROM (
	SELECT a.objid
	FROM amnesty a
	INNER JOIN amnesty_active ac ON a.objid = ac.objid
	WHERE a.refno LIKE $P{searchtext}
	UNION
	SELECT a.objid
	FROM amnesty a
	INNER JOIN amnesty_active ac ON a.objid = ac.objid
	WHERE a.borrower_name LIKE $P{searchtext}
) a INNER JOIN amnesty am ON a.objid = am.objid
ORDER BY am.borrower_name, am.dtstarted DESC

[getActiveAmnestyByMode
SELECT am.*
FROM (
	SELECT a.objid
	FROM amnesty a
	INNER JOIN amnesty_active ac ON a.objid = ac.objid
	WHERE a.refno LIKE $P{searchtext}
	UNION
	SELECT a.objid
	FROM amnesty a
	INNER JOIN amnesty_active ac ON a.objid = ac.objid
	WHERE a.borrower_name LIKE $P{searchtext}
) a INNER JOIN amnesty am ON a.objid = am.objid
WHERE am.txnmode = $P{mode}
ORDER BY am.borrower_name, am.dtstarted DESC