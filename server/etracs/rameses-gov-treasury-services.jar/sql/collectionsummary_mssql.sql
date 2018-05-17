[getCollectionsByCount]
SELECT 
	TOP ${receiptcount}   
	cr.receiptno, 
	CASE WHEN cv.objid IS NULL THEN cr.amount  ELSE 0.0 END AS amount,
	CASE WHEN cv.objid IS NULL THEN 0  ELSE 1 END AS voided
FROM cashreceipt cr 
	LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
WHERE cr.collector_objid = $P{userid} 
  AND cr.remittanceid IS NULL 
  and cv.objid IS NULL 
ORDER BY cr.txndate DESC   