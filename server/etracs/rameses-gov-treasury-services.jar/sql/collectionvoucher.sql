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
