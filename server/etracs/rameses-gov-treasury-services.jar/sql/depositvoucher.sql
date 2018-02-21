[insertDepositFund]
INSERT INTO depositvoucher_fund 
	(objid,parentid,state,
	controlno,fundid,
	totalcash,totalcheck,cashtodeposit,checktodeposit,amount)
SELECT 
	CONCAT( a.objid, a.fundid ), a.objid, 'OPEN', 
	CONCAT(a.controlno, a.fundcode ), a.fundid, 
	0, 0, 0, 0, a.amount
FROM  (
	SELECT dv.objid, dv.controlno, dv.controldate, f.code AS fundcode, f.objid AS fundid, SUM( cvf.amount) AS amount 
	FROM collectionvoucher_fund cvf
	INNER JOIN fund f ON cvf.fund_objid = f.objid 
	INNER JOIN collectionvoucher cv ON cvf.parentid = cv.objid 
	LEFT JOIN depositvoucher dv ON cv.depositvoucherid = dv.objid
	WHERE dv.objid = $P{depositvoucherid}
	AND  ( cvf.totalcheck + cvf.totalcash > 0 )
	GROUP BY dv.objid, dv.controlno, dv.controldate, f.code, f.objid
) a

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

[updatePaymentCheckDefaultFund]
UPDATE paymentcheck 
SET fundid = (
     SELECT nc.fund_objid 
     FROM cashreceiptpayment_noncash nc
     WHERE nc.refid = paymentcheck.objid AND nc.amount = paymentcheck.amount 
)
WHERE depositvoucherid = $P{depositvoucherid}


[updateFundCheckTotal]
UPDATE depositvoucher_fund
    SET totalcheck = (
		SELECT SUM(pc.amount) 
		FROM paymentcheck pc
        WHERE pc.depositvoucherid = depositvoucher_fund.parentid 
        AND pc.fundid = depositvoucher_fund.fundid
	)
WHERE parentid = $P{depositvoucherid}

[cleanUpNullTotals]
UPDATE depositvoucher_fund SET 
    totalcheck = CASE WHEN totalcheck IS NULL THEN 0 ELSE totalcheck END
WHERE parentid = $P{depositvoucherid}