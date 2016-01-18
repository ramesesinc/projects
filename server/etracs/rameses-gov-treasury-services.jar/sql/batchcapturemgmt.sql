[getAFSerialList]
SELECT a.objid AS formno, a.* FROM af a where formtype = 'serial' ORDER BY a.objid  


[getSubmittedBatchCapturedReceipts]
SELECT t.*
FROM (
	SELECT 
		bc.objid, 
		bc.state,
		bc.formno,
		bc.collector_name,
		bc.capturedby_name,
		bc.collectiontype_name,
		MIN(bce.series) AS issuedstartseries,
		MAX(bce.series) AS issuedendseries,
		SUM(CASE WHEN bce.voided = 0 THEN bce.totalcash ELSE 0 END ) AS totalcash,
		SUM(CASE WHEN bce.voided = 0 THEN bce.totalnoncash ELSE 0 END ) AS totalnoncash,
		SUM(CASE WHEN bce.voided = 0 THEN bce.amount ELSE 0 END ) AS amount,
		SUM(CASE WHEN bce.voided = 0 THEN 0 ELSE 1 END) AS voidcount
	FROM batchcapture_collection bc
		INNER JOIN batchcapture_collection_entry bce ON bc.objid = bce.parentid
	WHERE bc.collector_objid = $P{collectorid} 
	   AND bc.startseries like $P{startseries}  
	  AND bc.state IN ('FORPOSTING', 'POSTED')
	GROUP BY 
		bc.objid,
		bc.state,
		bc.formno,
		bc.collector_name,
		bc.capturedby_name,
		bc.collectiontype_name
	) t
ORDER BY t.objid, t.state, t.formno, t.issuedstartseries 		 