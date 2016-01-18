[getList]
SELECT a.*
FROM (
	SELECT objid FROM shortage_voidrequest
	WHERE shortage_refno LIKE $P{searchtext}
	UNION
	SELECT objid FROM shortage_voidrequest
	WHERE shortage_cbsno LIKE $P{searchtext}
	UNION
	SELECT objid FROM shortage_voidrequest
	WHERE shortage_collectorname LIKE $P{searchtext}
) b INNER JOIN shortage_voidrequest a ON b.objid = a.objid
ORDER BY a.dtcreated DESC

[getListByState]
SELECT a.*
FROM (
	SELECT objid FROM shortage_voidrequest
	WHERE shortage_refno LIKE $P{searchtext}
		AND txnstate = $P{state}
	UNION
	SELECT objid FROM shortage_voidrequest
	WHERE shortage_cbsno LIKE $P{searchtext}
		AND txnstate = $P{state}
	UNION
	SELECT objid FROM shortage_voidrequest
	WHERE shortage_collectorname LIKE $P{searchtext}
		AND txnstate = $P{state}
) b INNER JOIN shortage_voidrequest a ON b.objid = a.objid
ORDER BY a.dtcreated DESC

[findPendingRequestByShortageid]
SELECT s.* FROM shortage_voidrequest s 
WHERE s.shortage_objid = $P{shortageid}
	AND s.txnstate = 'FOR_APPROVAL'

[findPendingRequestByRemittanceid]
SELECT vr.*
FROM shortage_voidrequest vr
INNER JOIN shortage s ON vr.shortage_objid = s.objid
WHERE s.remittanceid = $P{remittanceid}
	AND vr.txnstate = 'FOR_APPROVAL'

[findRequestByShortageidAndState]
SELECT s.* FROM shortage_voidrequest s
WHERE s.shortage_objid = $P{shortageid}
	AND s.txnstate = $P{txnstate}

[findRequestByRemittanceidAndState]
SELECT vr.*
FROM shortage_voidrequest vr
INNER JOIN shortage s ON vr.shortage_objid = s.objid
WHERE s.remittanceid = $P{remittanceid}
	AND vr.txnstate = $P{state}

[changeState]
UPDATE shortage_voidrequest SET txnstate = $P{txnstate}
WHERE objid = $P{objid}