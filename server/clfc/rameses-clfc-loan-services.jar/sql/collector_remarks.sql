[getList]
SELECT c.*
FROM collector_remarks c
LEFT OUTER JOIN followup_remarks f ON c.objid = f.objid

[getPreviousList]
SELECT c.*
FROM collector_remarks c
LEFT OUTER JOIN followup_remarks f ON c.objid = f.objid
WHERE c.txndate < $P{txndate}

[getListWithFollowupTagAndLedgerid]
SELECT c.*, CASE WHEN f.objid IS NULL THEN 0 ELSE 1 END AS isfollowup
FROM collector_remarks c
LEFT JOIN followup_remarks f ON c.objid = f.objid
WHERE c.ledgerid = $P{ledgerid}

[getPreviousListWithFollowupTagAndLedgerid]
SELECT c.*, CASE WHEN f.objid IS NULL THEN 0 ELSE 1 END AS isfollowup
FROM collector_remarks c
LEFT JOIN followup_remarks f ON c.objid = f.objid
WHERE c.ledgerid = $P{ledgerid}
	AND c.txndate < $P{txndate}

[getPreviousCollectorRemarksListByLedgeridAndTxndate]
SELECT r.* 
FROM collector_remarks r
LEFT JOIN followup_remarks f ON r.objid = f.objid
WHERE f.objid IS NULL
	AND r.ledgerid = $P{ledgerid}
	AND r.txndate < $P{txndate}
ORDER BY r.txndate DESC
LIMIT 5

[getPreviousFollowupRemarksListByLedgeridAndTxndate]
SELECT r.*
FROM collector_remarks r
INNER JOIN followup_remarks f ON r.objid = f.objid
WHERE r.ledgerid = $P{ledgerid}
	AND r.txndate < $P{txndate}
ORDER BY r.txndate DESC
LIMIT 5

[getCollectorRemarksByLedgerid]
SELECT r.*
FROM collector_remarks r
LEFT JOIN followup_remarks f ON r.objid = f.objid
WHERE f.objid IS NULL
	AND r.ledgerid = $P{ledgerid}
ORDER BY r.txndate DESC

[getFollowupRemarksByLedgerid]
SELECT r.*
FROM collector_remarks r
INNER JOIN followup_remarks f ON r.objid = f.objid
WHERE r.ledgerid = $P{ledgerid}
ORDER BY r.txndate DESC


[findByLedgeridAndTxndate]
SELECT c.* FROM collector_remarks c 
WHERE c.ledgerid = $P{ledgerid} AND c.txndate = $P{txndate}
LIMIT 1
