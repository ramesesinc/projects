[getBreakdownDetail]
SELECT ccd.*
FROM collection_cashbreakdown cc
INNER JOIN collection_cashbreakdown_detail ccd ON cc.objid=ccd.parentid
WHERE cc.collection_objid=$P{collectionid}
	AND cc.group_objid=$P{groupid}
	AND cc.group_type=$P{grouptype}
ORDER BY ccd.denomination DESC

[findByCollectionid]
SELECT * FROM collection_cashbreakdown
WHERE collection_objid=$P{collectionid}
	AND group_objid=$P{groupid}
LIMIT 1

[getConsolidatedBreakdown]
SELECT ccd.denomination, SUM(ccd.qty) AS qty,
	SUM(ccd.amount) AS amount
FROM collection_cashbreakdown cc
INNER JOIN collection_cashbreakdown_detail ccd ON cc.objid=ccd.parentid
WHERE cc.dtfiled LIKE $P{billdate}
GROUP BY ccd.denomination
ORDER BY ccd.denomination DESC

[getOnlineBreakdown]
SELECT ccd.denomination, SUM(ccd.qty) AS qty, SUM(ccd.amount) AS amount
FROM online_collection oc
INNER JOIN collection_cashbreakdown cc ON oc.objid=cc.collection_objid
INNER JOIN collection_cashbreakdown_detail ccd ON cc.objid=ccd.parentid
WHERE oc.collector_objid=$P{collectorid}
	AND oc.txndate=$P{billdate}
GROUP BY ccd.denomination

[getPerRouteBreakdown]
SELECT ccd.denomination, SUM(ccd.qty) AS qty, SUM(ccd.amount) AS amount
FROM field_collection fc
INNER JOIN collection_cashbreakdown cc ON fc.objid=cc.collection_objid
INNER JOIN collection_cashbreakdown_detail ccd ON cc.objid=ccd.parentid
WHERE fc.objid=$P{collectionid}
	AND fc.collector_objid=$P{collectorid}
	AND fc.billdate=$P{billdate}
	AND cc.group_objid=$P{routecode}
GROUP BY ccd.denomination
ORDER BY ccd.denomination DESC

[getFollowupBreakdown]
SELECT ccd.denomination, SUM(ccd.qty) AS qty, SUM(ccd.amount) AS amount
FROM field_collection fc
INNER JOIN special_collection sc ON fc.objid=sc.billingid
INNER JOIN collection_cashbreakdown cc ON sc.objid=cc.group_objid
INNER JOIN collection_cashbreakdown_detail ccd ON cc.objid=ccd.parentid
WHERE fc.billdate = $P{billdate}
	AND fc.collector_objid = $P{collectorid}
	AND cc.group_objid = $P{collectionid}
	AND cc.group_type = 'followup'
GROUP BY ccd.denomination
ORDER BY ccd.denomination DESC

[getSpecialBreakdown]
SELECT ccd.denomination, SUM(ccd.qty) AS qty, SUM(ccd.amount) AS amount
FROM field_collection fc
INNER JOIN special_collection sc ON fc.objid=sc.billingid
INNER JOIN collection_cashbreakdown cc ON sc.objid=cc.group_objid
INNER JOIN collection_cashbreakdown_detail ccd ON cc.objid=ccd.parentid
WHERE fc.billdate = $P{billdate}
	AND fc.collector_objid = $P{collectorid}
	AND cc.group_objid = $P{collectionid}
	AND cc.group_type = 'special'
GROUP BY ccd.denomination
ORDER BY ccd.denomination DESC
