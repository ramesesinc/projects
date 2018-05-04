[getOpenChecks]
SELECT refno, receivedfrom, amount, amtused 
FROM paymentcheck
WHERE objid IN ( 
   SELECT refid 
   FROM cashreceiptpayment_noncash npc
   INNER JOIN cashreceipt c ON c.objid=npc.receiptid
   LEFT JOIN cashreceipt_void cv ON cv.receiptid = c.objid 
   WHERE c.remittanceid =  $P{remittanceid} AND cv.objid IS NULL
)   
AND (amount - amtused) > 0 

[insertRemittanceFund]
INSERT INTO remittance_fund ( 
	objid, remittanceid, controlno, fund_objid, fund_title, 
	amount, totalcash, totalcheck, totalcr, cashbreakdown 
)
SELECT 
	objid, remittanceid, controlno, fund_objid, fund_title, 
	amount, 0, 0, 0, '[]' 
FROM cashreceipt_fund_summary 
WHERE remittanceid =  $P{remittanceid} 


[getCashReceiptsForRemittance]
SELECT 
CONCAT( cr.collector_objid, cr.remittanceid, afc.objid ) AS objid, af.formtype, cr.remittanceid,  
cr.collector_objid,afc.objid AS afcontrolid, afc.stubno,cr.formno,  
MIN(cr.series) AS fromseries, MAX(cr.series) AS toseries, afc.endseries,
COUNT(*) AS qty, SUM( CASE WHEN cv.objid IS NULL THEN cr.amount ELSE 0 END ) AS amount  
FROM
( SELECT * FROM cashreceipt WHERE collector_objid = $P{collectorid} AND remittanceid IS NULL AND receiptdate<= $P{remdate} ) cr
INNER JOIN af_control afc ON cr.controlid=afc.objid 
INNER JOIN af ON afc.afid = af.objid 
LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid
GROUP BY cr.collector_objid, afc.objid, afc.stubno,cr.formno, af.formtype, cr.remittanceid
