[getSplitChecks]
SELECT 
pc.objid AS check_objid, 
pc.refno AS check_refno, 
pc.refdate AS check_refdate,
pc.receivedfrom AS check_receivedfrom,
pc.bank_name AS check_bank, 
pc.amount AS check_amount,
nc.fund_objid, 
f.code AS fund_code,
F.title AS fund_title,
nc.amount AS fund_amount,
lf.objid AS fund_depositfundid

FROM cashreceiptpayment_noncash nc
INNER JOIN liquidation_fund lf ON lf.objid = nc.liquidationfundid
INNER JOIN fund f ON lf.fund_objid = f.objid 
LEFT JOIN paymentcheck pc ON nc.refid = pc.objid 
WHERE  pc.amount <> nc.amount
AND lf.liquidationid = $P{liquidationid}

[insertDepositFund]
INSERT INTO deposit_fund 
( objid,state,refno,refdate,reftype,fundid,totalcash,totalcheck )
SELECT 
lf.objid, 'OPEN', lf.controlno, l.controldate, 'liquidation_fund', lf.fund_objid, lf.totalcash, lf.totalcheck 
FROM liquidation_fund lf
INNER JOIN liquidation l ON lf.liquidationid = l.objid 
WHERE l.objid = $P{liquidationid}
AND  ( lf.totalcheck + lf.totalcash > 0 )

[insertDepositFundCheck]
INSERT INTO deposit_fund_check
(objid, depositfundid, fundid ) 
SELECT 
pc.objid, nc.liquidationfundid AS depositfundid, nc.fund_objid AS fundid 
FROM cashreceiptpayment_noncash nc
INNER JOIN liquidation_fund lf ON lf.objid = nc.liquidationfundid
LEFT JOIN paymentcheck pc ON nc.refid = pc.objid 
WHERE lf.liquidationid = $P{liquidationid}
AND pc.amount = nc.amount

[insertForDepositFundTransfer]
INSERT INTO deposit_fund_transfer 
(objid, depositfundid, fromfundid, tofundid, amount )
SELECT a.* FROM 
(SELECT 
UUID() AS objid, nc.liquidationfundid AS depositfundid, df.fundid AS fromfundid, nc.fund_objid AS tofundid, nc.amount   
FROM cashreceiptpayment_noncash nc
INNER JOIN liquidation_fund lf ON lf.objid = nc.liquidationfundid
LEFT JOIN deposit_fund_check df ON nc.refid=df.objid
WHERE lf.liquidationid= $P{liquidationid} ) a 
WHERE ((a.fromfundid IS NULL) OR NOT( a.fromfundid = a.tofundid ))
