[getOpenChecks]
SELECT refno, receivedfrom, amount, amtused 
FROM paymentcheck
WHERE objid IN ( 
   SELECT refid 
   FROM cashreceiptpayment_noncash npc
   INNER JOIN cashreceipt c ON c.objid=npc.receiptid
   WHERE c.remittanceid =  $P{remittanceid}
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
