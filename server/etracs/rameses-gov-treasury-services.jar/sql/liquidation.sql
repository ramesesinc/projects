[insertLiquidationFund]
INSERT INTO liquidation_fund
( objid,controlno,liquidationid,fund_objid,fund_title,amount,totalcash,totalcheck,totalcr,cashbreakdown )
SELECT 
CONCAT(l.objid, '-', rf.fund_objid) AS objid, CONCAT(l.controlno,'-',f.code) AS controlno, l.objid AS liquidationid, 
rf.fund_objid, rf.fund_title, SUM(rf.amount) AS amount, SUM(rf.totalcash) AS totalcash, SUM(rf.totalcheck) AS totalcheck, SUM(rf.totalcr) AS totalcr, '[]' AS cashbreakdown
FROM remittance_fund rf
INNER JOIN remittance r ON rf.remittanceid = r.objid 
INNER JOIN liquidation l ON r.liquidationid = l.objid
INNER JOIN fund f ON f.objid=rf.fund_objid
WHERE l.objid = $P{liquidationid}
GROUP BY rf.fund_objid,  f.code, rf.fund_title


[updateRemittanceFundLiquidationId]
UPDATE remittance_fund
SET liquidationfundid = (
    SELECT lf.objid FROM liquidation_fund lf 
  INNER JOIN remittance r ON r.liquidationid=lf.liquidationid
  WHERE r.liquidationid = $P{liquidationid}
  AND lf.fund_objid = remittance_fund.fund_objid
)
WHERE remittanceid IN 
( SELECT objid FROM remittance WHERE liquidationid = $P{liquidationid} )


[updateNoncashPaymentLiquidationId]
UPDATE cashreceiptpayment_noncash 
SET liquidationfundid = ( 
    SELECT lf.objid  FROM liquidation_fund lf
    INNER JOIN remittance_fund rf ON rf.liquidationfundid=lf.objid
  INNER JOIN cashreceipt cr ON cr.remittanceid = rf.remittanceid  
    WHERE lf.liquidationid = $P{liquidationid}
    AND cr.objid = cashreceiptpayment_noncash.receiptid 
    AND lf.fund_objid = cashreceiptpayment_noncash.fund_objid
)
WHERE receiptid IN ( 
  SELECT cr.objid FROM cashreceipt cr 
  INNER JOIN remittance r ON cr.remittanceid=r.objid 
  WHERE r.liquidationid = $P{liquidationid}
)