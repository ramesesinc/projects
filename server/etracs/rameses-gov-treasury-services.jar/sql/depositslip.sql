[initDepositSlipCheck]
INSERT INTO depositslip_check
(objid, depositslipid, checkid, amount )
SELECT 
MAX(ncp.refid) AS objid, ds.objid AS depositslipid, ncp.refid AS checkid, SUM(ncp.amount) AS amount
FROM cashreceiptpayment_noncash ncp
INNER JOIN cashreceipt cr ON cr.objid=ncp.receiptid
INNER JOIN remittance r ON cr.remittanceid = r.objid 
INNER JOIN collectionvoucher_fund cvf ON cvf.parentid=r.collectionvoucherid
INNER JOIN depositslip ds ON ds.depositvoucherid = cvf.depositvoucherid 
WHERE ds.objid=$P{depositslipid} AND ncp.fund_objid=$P{fundid}
AND cr.objid NOT IN (SELECT receiptid FROM cashreceipt_void WHERE receiptid = cr.objid )
AND ncp.reftype = 'CHECK' AND NOT(ncp.refid IS NULL)
GROUP BY ds.objid, ncp.refid

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
UPDATE depositvoucher 
   SET totalcheck = (
      SELECT SUM( ds.totalcheck )
      FROM depositslip ds 
      WHERE ds.depositvoucherid = depositvoucher.objid 
   )
WHERE objid=$P{depositvoucherid}   

[updateFundCashTotals]
UPDATE depositvoucher
   SET totalcash = (
   		SELECT SUM( ds.totalcash )
   		FROM depositslip ds 
   		WHERE ds.depositvoucherid = depositvoucher.objid 
   )
WHERE objid=$P{depositvoucherid}   

[updateFundCashTotals]
UPDATE depositvoucher 
   SET totalcash = (
      SELECT SUM( ds.totalcash )
      FROM depositslip ds 
      WHERE ds.depositvoucherid = depositvoucher.objid 
   )
WHERE objid=$P{depositvoucherid} 

[cleanUpNullFundTotals]
UPDATE depositvoucher 
SET 
    totalcheck = CASE WHEN totalcheck IS NULL THEN 0 ELSE totalcheck END,
    totalcash = CASE WHEN totalcash IS NULL THEN 0 ELSE totalcash END
WHERE objid=$P{depositvoucherid} 