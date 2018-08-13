[getOpenSplitChecks]
SELECT * FROM checkpayment 
WHERE depositvoucherid IS NULL
AND objid IN 
( 
	SELECT nc.refid 
	FROM cashreceiptpayment_noncash nc 
	INNER JOIN cashreceipt c ON nc.receiptid = c.objid 
	INNER JOIN remittance r ON c.remittanceid = r.objid 
	INNER JOIN collectionvoucher cv ON r.collectionvoucherid = cv.objid 
	INNER JOIN collectionvoucher_fund cvf ON cvf.parentid = cv.objid 
	WHERE cvf.objid IN ${ids}
	AND nc.fund_objid = cvf.fund_objid 
	AND NOT( nc.amount = checkpayment.amount )
	GROUP BY nc.refid
)
ORDER BY refno

[updateCheckForDeposit]
UPDATE checkpayment 
SET state = 'FOR-DEPOSIT'
WHERE objid IN 
( 
	SELECT nc.refid 
	FROM cashreceiptpayment_noncash nc 
	INNER JOIN cashreceipt c ON nc.receiptid = c.objid 
	INNER JOIN remittance r ON c.remittanceid = r.objid 
	INNER JOIN collectionvoucher cv ON r.collectionvoucherid = cv.objid 
	INNER JOIN collectionvoucher_fund cvf ON cvf.parentid = cv.objid 
	WHERE cvf.depositvoucherid = $P{depositvoucherid}
	AND nc.fund_objid = cvf.fund_objid 
	GROUP BY nc.refid
)

[updateCheckDepositVoucherId]
UPDATE checkpayment 
SET depositvoucherid = $P{depositvoucherid}
WHERE depositvoucherid IS NULL 
AND objid IN 
( 
	SELECT nc.refid 
	FROM cashreceiptpayment_noncash nc 
	INNER JOIN cashreceipt c ON nc.receiptid = c.objid 
	INNER JOIN remittance r ON c.remittanceid = r.objid 
	INNER JOIN collectionvoucher cv ON r.collectionvoucherid = cv.objid 
	INNER JOIN collectionvoucher_fund cvf ON cvf.parentid = cv.objid 
	WHERE cvf.depositvoucherid = $P{depositvoucherid}
	AND nc.fund_objid = cvf.fund_objid 
	AND nc.amount = checkpayment.amount
	GROUP BY nc.refid
)


[updateCheckPaymentDepositId]
UPDATE checkpayment
SET depositvoucherid = $P{depositvoucherid}
WHERE objid IN 
(
	SELECT DISTINCT nc.refid 
	FROM cashreceiptpayment_noncash nc 
	INNER JOIN cashreceipt cr ON nc.receiptid=cr.objid 	
	INNER JOIN remittance r ON cr.remittanceid = r.objid 
	INNER JOIN collectionvoucher cv ON r.collectionvoucherid=cv.objid 	
    WHERE cv.depositvoucherid  = $P{depositvoucherid}
)


[getBankAccountLedgerItem]
SELECT 
  dv.fundid,
  a.bankacctid,
  ba.acctid AS itemacctid,
  a.dr,
  0 AS cr,
  'bankaccount_ledger' AS _schemaname
FROM     
(SELECT 
	 ds.depositvoucherid, 
    ds.bankacctid,
    SUM(ds.amount) AS dr
FROM depositslip ds 
WHERE ds.depositvoucherid = $P{depositvoucherid} 
GROUP BY ds.depositvoucherid, ds.bankacctid) a
INNER JOIN depositvoucher dv ON a.depositvoucherid = dv.objid 
INNER JOIN bankaccount ba ON a.bankacctid = ba.objid

[getCashLedgerItem]
SELECT 
  dv.fundid,
  (SELECT objid FROM itemaccount WHERE fund_objid = dv.fundid AND TYPE = 'CASH_IN_TREASURY' LIMIT 1 ) AS itemacctid,  
  0 AS dr,
  a.cr,
  'cash_treasury_ledger' AS _schemaname
FROM     
(SELECT 
	 ds.depositvoucherid,
    SUM(ds.amount) AS cr
FROM depositslip ds 
WHERE ds.depositvoucherid = $P{depositvoucherid}  
GROUP BY ds.depositvoucherid) a
INNER JOIN depositvoucher dv ON a.depositvoucherid = dv.objid 

