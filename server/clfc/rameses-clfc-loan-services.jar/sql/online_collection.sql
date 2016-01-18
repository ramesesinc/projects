[getCollectors]
SELECT * FROM online_collection_collector

[findCollectionForPostingByTxndateAndCollector]
SELECT * 
FROM online_collection
WHERE state='FOR_POSTING'
	AND txndate=$P{txndate}
	AND collector_objid=$P{collectorid}

[findPaymentById]
SELECT ocd.*, oc.txndate
FROM online_collection_detail ocd
INNER JOIN online_collection oc ON ocd.parentid=oc.objid
WHERE ocd.objid=$P{objid}

[findRouteByOnlinecollectionidAndRoute]
SELECT * FROM online_collection_route
WHERE onlinecollectionid=$P{onlinecollectionid}
	AND routecode=$P{routecode}

[findCollectionByCollectorAndTxndate]
SELECT objid FROM online_collection
WHERE collector_objid = $P{collectorid}
	AND txndate = $P{txndate}

[getCollectionDateByCollector]
SELECT txndate 
FROM online_collection
WHERE collector_objid=$P{collectorid}
	AND state='FOR_POSTING'
ORDER BY txndate

[getRoutesByCollectorAndCollectionDate]
SELECT lr.*
FROM online_collection oc
INNER JOIN online_collection_route ocr ON oc.objid=ocr.onlinecollectionid
INNER JOIN loan_route lr ON ocr.routecode=lr.code
WHERE oc.collector_objid=$P{collectorid}
	AND oc.state='FOR_POSTING'
	AND oc.txndate=$P{txndate}

[getPayments]
SELECT * FROM online_collection_detail
WHERE parentid=$P{parentid}
ORDER BY route_description, borrower_name 

[getPaymentsByRoutecode]
SELECT * FROM online_collection_detail
WHERE parentid=$P{parentid}
	AND route_code=$P{routecode}
ORDER BY borrower_name

[getCashBreakdown]
SELECT * FROM online_collection_cashbreakdown
WHERE parentid=$P{parentid}
	AND routecode=$P{routecode}
ORDER BY denomination DESC

[getConsolidatedBreakdownByTxndate]
SELECT occ.*, SUM(qty) AS totalqty, SUM(amount) AS totalamount
FROM online_collection oc
INNER JOIN online_collection_cashbreakdown occ ON oc.objid=occ.parentid
WHERE oc.txndate=$P{billdate}
GROUP BY occ.denomination
ORDER BY occ.denomination DESC

[getPercollectorBreakdownByBilldate]
SELECT occ.*, SUM(qty) AS totalqty, SUM(amount) AS totalamount
FROM online_collection oc
INNER JOIN online_collection_cashbreakdown occ ON oc.objid=occ.parentid
WHERE oc.txndate=$P{billdate}
	AND oc.collector_objid=$P{collectorid}
	AND occ.routecode=$P{routecode}
GROUP BY occ.denomination
ORDER BY occ.denomination DESC

[getRoutesByCollectoridAndTxndate]
SELECT DISTINCT lr.*
FROM online_collection oc
INNER JOIN online_collection_detail ocd ON oc.objid=ocd.parentid
INNER JOIN loan_route lr ON ocd.route_code=lr.code
WHERE oc.collector_objid=$P{collectorid}
	AND oc.txndate=$P{billdate}

[getOnlineCollectors]
SELECT DISTINCT collector_objid AS objid, collector_name AS name
FROM online_collection
WHERE txndate = $P{txndate}

[getOnlineCollections]
SELECT ocd.* 
FROM online_collection_detail ocd
INNER JOIN online_collection oc ON ocd.parentid=oc.objid
WHERE oc.txndate = $P{txndate}
	AND oc.collector_objid = $P{collectorid}
ORDER BY borrower_name

[changeState]
UPDATE online_collection SET state=$P{state}
WHERE objid=$P{objid}
