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
