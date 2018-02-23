[updateCheckTotal]
UPDATE depositslip
    SET totalcheck = (
		SELECT SUM(pc.amount) 
		FROM paymentcheck pc
        WHERE pc.depositslipid = depositslip.objid   
	)
WHERE objid = $P{depositslipid}

[cleanUpNullTotals]
UPDATE depositslip 
SET 
    totalcheck = CASE WHEN totalcheck IS NULL THEN 0 ELSE totalcheck END
WHERE objid = $P{depositslipid}

[updateFundCheckTotals]
UPDATE depositvoucher_fund 
   SET totalcheck = (
      SELECT SUM( ds.totalcheck )
      FROM depositslip ds 
      WHERE ds.depositvoucherid = depositvoucher_fund.parentid 
      AND ds.fundid = depositvoucher_fund.fundid 
   )
WHERE parentid=$P{depositvoucherid} AND fundid=$P{fundid}  

[updateFundCashTotals]
UPDATE depositvoucher_fund 
   SET totalcash = (
   		SELECT SUM( ds.totalcash )
   		FROM depositslip ds 
   		WHERE ds.depositvoucherid = depositvoucher_fund.parentid 
   		AND ds.fundid = depositvoucher_fund.fundid 
   )
WHERE parentid=$P{depositvoucherid} AND fundid=$P{fundid}  

[updateFundCashTotals]
UPDATE depositvoucher_fund 
   SET totalcash = (
      SELECT SUM( ds.totalcash )
      FROM depositslip ds 
      WHERE ds.depositvoucherid = depositvoucher_fund.parentid 
      AND ds.fundid = depositvoucher_fund.fundid 
   )
WHERE parentid=$P{depositvoucherid} AND fundid=$P{fundid}  

[cleanUpNullFundTotals]
UPDATE depositvoucher_fund 
SET 
    totalcheck = CASE WHEN totalcheck IS NULL THEN 0 ELSE totalcheck END,
    totalcash = CASE WHEN totalcash IS NULL THEN 0 ELSE totalcash END
WHERE parentid=$P{depositvoucherid} AND fundid=$P{fundid}