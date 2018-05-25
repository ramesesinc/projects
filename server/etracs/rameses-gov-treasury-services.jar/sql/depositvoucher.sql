[getOpenSplitChecks]
SELECT * FROM paymentcheck 
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
	AND NOT( nc.amount = paymentcheck.amount )
	GROUP BY nc.refid
)
ORDER BY refno

[updateCheckForDeposit]
UPDATE paymentcheck 
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
UPDATE paymentcheck 
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
	AND nc.amount = paymentcheck.amount
	GROUP BY nc.refid
)


[updatePaymentCheckDepositId]
UPDATE paymentcheck
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
    ds.fundid,  
    ds.bankacctid,
    ba.acctid AS itemacctid,
     SUM(ds.amount) AS dr,
     0 AS cr,
     'bankaccount_ledger' AS _schemaname 
FROM depositslip ds
INNER JOIN bankaccount ba ON ba.objid = ds.bankacctid 
WHERE ds.depositvoucherid = $P{depositvoucherid}
AND ds.fundid = $P{fundid}

[getCashLedgerItem]
SELECT 
  cv.fundid,
  (SELECT objid FROM itemaccount WHERE fund_objid = cv.fundid AND TYPE = 'CASH_IN_TREASURY' LIMIT 1 ) AS itemacctid,
  0 AS dr,
  (cv.totalcash + cv.totalcheck) AS cr,
  'cash_treasury_ledger' AS _schemaname
FROM depositvoucher_fund cv 
WHERE cv.parentid = $P{depositvoucherid} AND cv.fundid=$P{fundid}
