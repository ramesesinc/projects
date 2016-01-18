[getList]
SELECT b.*
FROM (
	SELECT * FROM overage_voidrequest
	WHERE overage_refno LIKE $P{searchtext}
	UNION
	SELECT * FROM overage_voidrequest
	WHERE overage_collectorname LIKE $P{searchtext}
) b INNER JOIN overage_voidrequest a ON b.objid = a.objid
ORDER BY b.dtcreated DESC

[getListByState]
SELECT b.*
FROM (
	SELECT * FROM overage_voidrequest
	WHERE overage_refno LIKE $P{searchtext}
		AND txnstate = $P{state}
	UNION
	SELECT * FROM overage_voidrequest
	WHERE overage_collectorname LIKE $P{searchtext}
		AND txnstate = $P{state}
) b INNER JOIN overage_voidrequest a ON b.objid = a.objid
ORDER BY b.dtcreated DESC

[findPendingRequestByOverageid]
SELECT s.* FROM overage_voidrequest s 
WHERE s.overage_objid = $P{overageid}
	AND s.txnstate IN ('DRAFT', 'FOR_APPROVAL')

[findRequestByOverageidAndState]
SELECT s.* FROM overage_voidrequest s
WHERE s.overage_objid = $P{overageid}
	AND s.txnstate = $P{txnstate}

[findPendingRequestByRemittanceid]
SELECT vr.*
FROM overage_voidrequest vr
INNER JOIN overage o ON vr.overage_objid = o.objid
WHERE o.remittanceid = $P{remittanceid}
	AND vr.txnstate = 'FOR_APPROVAL'

[changeState]
UPDATE overage_voidrequest SET txnstate = $P{txnstate}
WHERE objid = $P{objid}