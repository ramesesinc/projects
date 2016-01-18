USE clfc2;

-- Field collection cash breakdown
	
INSERT INTO `clfc2`.`collection_cb`
            (`objid`,
             `dtfiled`,
             `author_objid`,
             `author_name`,
             `txndate`,
             `collector_objid`,
             `collector_name`,
             `collection_objid`,
             `collection_type`,
             `group_objid`,
             `group_type`,
             `cbsno`)
SELECT c.objid, c.dtfiled, '' AS author_objid, c.filedby AS author_name, f.billdate, f.collector_objid, 
	f.collector_name, c.collection_objid, c.collection_type, c.group_objid, c.group_type, NULL AS cbsno
FROM collection_cashbreakdown c
		LEFT JOIN field_collection f ON c.collection_objid = f.objid;
		
INSERT INTO `clfc2`.`collection_cb_detail`
            (`objid`,
             `parentid`,
             `denomination`,
             `qty`,
             `amount`)
SELECT cd.objid, cd.parentid, cd.denomination, cd.qty, cd.amount
FROM collection_cashbreakdown c
	INNER JOIN collection_cashbreakdown_detail cd ON c.objid = cd.parentid;