DROP VIEW IF EXISTS unremitted_cashreceipt_af_summary;
CREATE VIEW unremitted_cashreceipt_af_summary AS 
SELECT 
CONCAT( cr.collector_objid, afc.objid ) AS objid,
cr.collector_objid,afc.objid AS afcontrolid, afc.stubno,cr.formno,  MIN(cr.series) AS fromseries, MAX(cr.series) AS toseries, 
COUNT(*) AS qty, SUM( CASE WHEN cv.objid IS NULL THEN cr.amount ELSE 0 END ) AS amount  
FROM cashreceipt cr
INNER JOIN af_control afc ON cr.controlid=afc.objid 
LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid
WHERE  cr.remittanceid IS NULL 
GROUP BY cr.collector_objid, afc.objid, afc.stubno,cr.formno;