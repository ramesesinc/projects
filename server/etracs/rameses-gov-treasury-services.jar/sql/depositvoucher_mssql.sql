[insertDepositFund]
INSERT INTO depositvoucher_fund (
	objid, parentid, state, controlno, fundid,
	totalcash, totalcheck, cashtodeposit, checktodeposit, amount 
)
SELECT 
	( a.objid + a.fundid ), a.objid, 'OPEN', 
	(a.controlno + a.fundcode ), a.fundid, 
	0, 0, 0, 0, a.amount
FROM  (
	SELECT 
		dv.objid, dv.controlno, dv.controldate, 
		f.code AS fundcode, f.objid AS fundid, 
		SUM( cvf.amount) AS amount 
	FROM collectionvoucher_fund cvf
		INNER JOIN fund f ON cvf.fund_objid = f.objid 
		INNER JOIN collectionvoucher cv ON cvf.parentid = cv.objid 
		LEFT JOIN depositvoucher dv ON cv.depositvoucherid = dv.objid
	WHERE dv.objid = $P{depositvoucherid} 
		AND  ( cvf.totalcheck + cvf.totalcash > 0 )
	GROUP BY dv.objid, dv.controlno, dv.controldate, f.code, f.objid
)a 

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

[updateCheckPaymentDefaultFund]
UPDATE checkpayment 
SET fundid = (
     SELECT nc.fund_objid 
     FROM cashreceiptpayment_noncash nc
     WHERE nc.refid = checkpayment.objid AND nc.amount = checkpayment.amount 
)
WHERE depositvoucherid = $P{depositvoucherid}


[updateFundCheckTotal]
UPDATE depositvoucher_fund
    SET checktodeposit = (
		SELECT SUM(pc.amount) 
		FROM checkpayment pc
        WHERE pc.depositvoucherid = depositvoucher_fund.parentid 
        AND pc.fundid = depositvoucher_fund.fundid
	)
WHERE parentid = $P{depositvoucherid}

[cleanUpNullTotals]
UPDATE depositvoucher_fund SET 
    checktodeposit = CASE WHEN checktodeposit IS NULL THEN 0 ELSE checktodeposit END
WHERE parentid = $P{depositvoucherid}


[getBankAccountLedgerItems]
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
GROUP BY ds.fundid,ds.bankacctid,ba.acctid


[getCashLedgerItems]
SELECT 
  cv.fundid,
  (SELECT top 1 objid FROM itemaccount WHERE fund_objid = cv.fundid AND TYPE = 'CASH_IN_TREASURY' ) AS itemacctid,
  0 AS dr,
  (cv.totalcash + cv.totalcheck) AS cr,
  'cash_treasury_ledger' AS _schemaname
FROM depositvoucher_fund cv 
WHERE cv.parentid = $P{depositvoucherid} 
