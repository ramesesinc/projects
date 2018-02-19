[insertDepositFund]
INSERT INTO deposit_fund 
( objid,depositid,state,controlno,controldate,fundid,totalcash,totalcheck, totalcashdeposited, totalcheckdeposited )
SELECT 
CONCAT('DEPFUND', UUID()), a.depositid,  'OPEN', CONCAT(a.controlno, a.fundcode), a.controldate, a.fundid,  
totalcash, 0, 0, 0 
FROM (
	SELECT l.depositid, d.controlno, f.code AS fundcode, d.controldate, lf.fund_objid AS fundid, SUM(lf.totalcash) AS totalcash 
	FROM liquidation_fund lf
	INNER JOIN liquidation l ON lf.liquidationid = l.objid 
	INNER JOIN deposit d ON l.depositid = d.objid
	INNER JOIN fund f ON lf.fund_objid = f.objid 
	WHERE l.depositid = $P{depositid}
	AND  ( lf.totalcheck + lf.totalcash > 0 )
	GROUP BY l.depositid, d.controlno, f.code, d.controldate, lf.fund_objid
) a 

[updateLiquidationFundDepositFund]
UPDATE liquidation_fund 
SET depositfundid = (
	SELECT df.objid 
	FROM deposit_fund df
	INNER JOIN deposit d ON df.depositid = d.objid 
	INNER JOIN liquidation l ON l.depositid = d.objid 
	WHERE df.depositid  =  $P{depositid}
	AND df.fundid = liquidation_fund.fund_objid 
)
WHERE liquidationid = (
	SELECT objid FROM liquidation WHERE depositid = $P{depositid}
) 

[updatePaymentCheckDepositId]
UPDATE paymentcheck
SET depositid = $P{depositid}
WHERE objid IN 
(
	SELECT DISTINCT nc.refid 
	FROM cashreceiptpayment_noncash nc 
    INNER JOIN liquidation_fund lf ON nc.liquidationfundid=lf.objid 
    INNER JOIN deposit_fund df ON lf.depositfundid = df.objid 
    WHERE df.depositid = $P{depositid} 
)


[updatePaymentCheckDepositFundId]
UPDATE paymentcheck
SET depositfundid = (
    SELECT df.objid  
    FROM  cashreceiptpayment_noncash nc 
    INNER JOIN liquidation_fund lf ON nc.liquidationfundid=lf.objid 
    INNER JOIN deposit_fund df ON lf.depositfundid = df.objid 
    WHERE nc.refid = paymentcheck.objid AND nc.amount = paymentcheck.amount 
)
WHERE depositid = $P{depositid}


[updateCheckByFundTotals]
UPDATE deposit_fund 
SET totalcheck = (
    SELECT SUM(pc.amount) 
    FROM paymentcheck pc
    WHERE pc.depositfundid = deposit_fund.objid 
)
WHERE depositid = $P{depositid}