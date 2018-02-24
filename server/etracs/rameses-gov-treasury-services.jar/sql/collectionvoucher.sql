[insertCollectionVoucherFund]
INSERT INTO collectionvoucher_fund
( objid,controlno,parentid,fund_objid,fund_title,amount,totalcash,totalcheck,totalcr,cashbreakdown )
SELECT 
CONCAT(cv.objid, '-', rf.fund_objid) AS objid, CONCAT(cv.controlno,'-',f.code) AS controlno, cv.objid AS parentid, 
rf.fund_objid, rf.fund_title, SUM(rf.amount) AS amount, SUM(rf.totalcash) AS totalcash, SUM(rf.totalcheck) AS totalcheck, 
SUM(rf.totalcr) AS totalcr, '[]' AS cashbreakdown
FROM remittance_fund rf
INNER JOIN remittance r ON rf.remittanceid = r.objid 
INNER JOIN collectionvoucher cv ON r.collectionvoucherid = cv.objid
INNER JOIN fund f ON f.objid=rf.fund_objid
WHERE cv.objid = $P{collectionvoucherid}
GROUP BY rf.fund_objid,  f.code, rf.fund_title


[postToCashTreasury]
INSERT INTO cash_ledger 
(objid,refid,refno,refdate,reftype,fundid,dr,cr)
SELECT 
    cvf.objid,
    cv.objid AS refid,
    cv.controlno AS refno, 
    cv.controldate AS refdate,
    'collectionvoucher' AS reftype,
    cvf.fund_objid AS fundid, 
    SUM(cvf.totalcash+cvf.totalcheck) AS amount,
    0 
FROM collectionvoucher_fund cvf
INNER JOIN collectionvoucher cv ON cv.objid=cvf.parentid
WHERE cvf.parentid = $P{collectionvoucherid}
GROUP BY cvf.objid,cvf.controlno,cv.controldate,cvf.fund_objid

[postToBankAccount]
INSERT INTO bankaccount_ledger
(objid,bankacctid,refid,refno,reftype,refdate,fundid,dr,cr)
SELECT  
    UUID() AS objid,
    cm.bankaccount_objid,
    ccv.objid AS refid,
    ccv.controlno AS refno,
    'collectionvoucher' AS reftype,
    ccv.controldate AS refdate,
    nc.fund_objid,
    SUM(nc.amount) AS amount,
    0
FROM 
   cashreceiptpayment_noncash nc
 INNER JOIN cashreceipt c ON nc.receiptid=c.objid
 LEFT JOIN cashreceipt_void cv ON cv.receiptid=c.objid
 INNER JOIN creditmemo cm ON cm.objid = nc.refid
 INNER JOIN remittance r ON c.remittanceid = r.objid
 INNER JOIN collectionvoucher ccv ON r.collectionvoucherid = ccv.objid 
 WHERE ccv.objid=$P{collectionvoucherid} AND cv.objid IS NULL 
 GROUP BY ccv.objid,ccv.controlno,ccv.controldate,cm.bankaccount_objid,nc.fund_objid

[postToIncome]
SELECT 
    cv.controlno AS refno,
    cv.objid AS refid,
    cv.controldate AS refdate,
    'collectionvoucher' AS reftype,
    ci.item_code AS item_code,
    ci.item_objid AS item_objid,
    ci.item_title AS item_title, 
    ci.item_fund_objid AS fund_objid,
    SUM( ci.amount ) AS amount 
FROM 
cashreceiptitem ci
INNER JOIN cashreceipt c ON ci.receiptid=c.objid
LEFT JOIN cashreceipt_void crv ON crv.receiptid=c.objid
INNER JOIN remittance r ON c.remittanceid=r.objid
INNER JOIN collectionvoucher cv ON r.collectionvoucherid=cv.objid 
WHERE cv.objid = $P{collectionvoucherid} 
AND crv.objid IS NULL
GROUP BY cv.controlno,
    cv.objid,
    cv.controldate,
    ci.item_code,
    ci.item_objid,
    ci.item_title, 
    ci.item_fund_objid

    UNION

SELECT 
    cv.controlno AS refno,
    cv.objid AS refid,
    cv.controldate AS refdate,
    'collectionvoucher' AS reftype,
    ia.code AS item_code,
    ia.objid AS item_objid,
    ia.title AS item_title, 
    ia.fund_objid AS fund_objid,
    SUM( cs.amount * -1 ) AS amount 
    
FROM cashreceipt_share cs 
INNER JOIN itemaccount ia ON cs.refacctid = ia.objid 
INNER JOIN cashreceipt c ON cs.receiptid=c.objid
LEFT JOIN cashreceipt_void crv ON crv.receiptid=c.objid
INNER JOIN remittance r ON c.remittanceid=r.objid
INNER JOIN collectionvoucher cv ON r.collectionvoucherid=cv.objid 
WHERE cv.objid = '$P{collectionvoucherid}' 
AND crv.objid IS NULL
GROUP BY cv.controlno,
    cv.objid,
    cv.controldate,
    ia.code,
    ia.objid,
    ia.title, 
    ia.fund_objid

[postToPayable]