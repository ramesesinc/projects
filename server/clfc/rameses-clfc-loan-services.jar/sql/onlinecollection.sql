[getCollectors]
SELECT * FROM onlinecollection_collector

[getCollectorListByDate]
SELECT DISTINCT o.collector_objid AS objid, o.collector_name AS name
FROM onlinecollection o
WHERE o.txndate = $P{date}

[getCollectionByDateAndCollector]
SELECT * FROM onlinecollection
WHERE txndate = $P{txndate}
	AND collector_objid = $P{collectorid}
ORDER BY dtfiled

[getCollectionDateByCollector]
SELECT c.objid, c.txndate 
FROM onlinecollection c
LEFT JOIN collection_remittance cr ON c.objid = cr.collection_objid
WHERE c.collector_objid = $P{collectorid}
	AND cr.objid IS NULL
UNION 
SELECT c.objid,  c.txndate 
FROM onlinecollection c
LEFT JOIN collection_remittance cr ON c.objid = cr.collection_objid
WHERE c.collector_objid = $P{collectorid}
	AND cr.state = 'FOR_POSTING'
ORDER BY txndate

[getPayments]
SELECT d.*, CASE WHEN p.objid IS NULL THEN 0 ELSE 1 END AS isproceedcollection,
	p.proceedid, rd.objid AS remittancedetailid
FROM onlinecollection_detail d 
LEFT JOIN collection_proceed p ON d.objid = p.refid
LEFT JOIN collection_remittance_detail rd ON d.objid = rd.refid
WHERE d.parentid = $P{objid}
ORDER BY d.route_description, d.borrower_name, d.dtpaid

[getOnlineCollectors]
SELECT DISTINCT collector_objid AS objid, collector_name AS name
FROM onlinecollection
WHERE txndate = $P{txndate}

[getOnlineCollections]
SELECT ocd.* 
FROM onlinecollection_detail ocd
INNER JOIN onlinecollection oc ON ocd.parentid = oc.objid
WHERE oc.txndate = $P{txndate}
	AND oc.collector_objid = $P{collectorid}
ORDER BY ocd.borrower_name

[getOnlineCollectionsByCollectionid]
SELECT ocd.* 
FROM onlinecollection_detail ocd
INNER JOIN onlinecollection oc ON ocd.parentid = oc.objid
WHERE oc.txndate = $P{txndate}
	AND oc.collector_objid = $P{collectorid}
	AND oc.objid = $P{collectionid}
ORDER BY ocd.borrower_name

[getCollectionByCollectoridAndDate]
SELECT o.*
FROM onlinecollection o
WHERE o.txndate = $P{txndate}
	AND o.collector_objid = $P{collectorid}

[findCollectionByDateAndCollector]
SELECT * FROM onlinecollection
WHERE txndate = $P{txndate}
	AND collector_objid = $P{collectorid}

[findCollectionByDateAndCollectorAndState]
SELECT * FROM onlinecollection
WHERE txndate = $P{txndate}
	AND collector_objid = $P{collectorid}
	AND state = $P{state}

[findDraftCollectionByDateAndCollector]
SELECT * 
FROM onlinecollection
WHERE state = 'DRAFT'
	AND txndate = $P{txndate}
	AND collector_objid = $P{collectorid}
LIMIT 1

[findRemittanceById]
SELECT * FROM collection_remittance
WHERE collection_objid = $P{objid}

[findDetailByProceedidAndCollectionid]
SELECT d.*
FROM onlinecollection_detail d
INNER JOIN collection_proceed p ON d.objid = p.refid
WHERE p.proceedid = $P{proceedid}
	AND d.parentid = $P{collectionid}

[findDetailByNoncashidAndCollectionid]
SELECT d.*
FROM onlinecollection_detail d
INNER JOIN collection_noncash n ON d.objid = n.refid
WHERE n.noncashid = $P{noncashid}
	AND d.parentid = $P{collectionid}

[changeState]
UPDATE onlinecollection SET state = $P{state}
WHERE objid = $P{objid}