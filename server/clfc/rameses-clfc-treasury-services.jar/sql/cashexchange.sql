[getList]
SELECT c.*
FROM (
	SELECT c.objid
	FROM cashexchange c
	WHERE c.cbs_cbsno LIKE $P{searchtext}
	UNION
	SELECT c.objid
	FROM cashexchange c
	WHERE c.cbs_collector_name LIKE $P{searchtext}
) q INNER JOIN cashexchange c ON q.objid = c.objid
ORDER BY c.dtcreated DESC

[getListByState]
SELECT c.*
FROM (
	SELECT c.objid
	FROM cashexchange c
	WHERE c.cbs_cbsno LIKE $P{searchtext}
		AND c.txnstate = $P{state}
	UNION
	SELECT c.objid
	FROM cashexchange c
	WHERE c.cbs_collector_name LIKE $P{searchtext}
		AND c.txnstate = $P{state}
) q INNER JOIN cashexchange c ON q.objid = c.objid
ORDER BY c.dtcreated DESC

[findByCbsidAndState]
SELECT c.*
FROM cashexchange c
WHERE c.txnstate = $P{state}
	AND c.cbs_objid = $P{cbsid}

[findExistingRequestByCbsid]
SELECT c.*
FROM cashexchange c
WHERE c.cbs_objid = $P{cbsid}
	AND c.txnstate IN ('DRAFT', 'FOR_APPROVAL')

[changeState]
UPDATE cashexchange SET txnstate = $P{txnstate}
WHERE objid = $P{objid}