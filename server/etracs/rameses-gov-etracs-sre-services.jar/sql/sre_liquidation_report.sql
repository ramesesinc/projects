[getSummaryReport]
SELECT rem.objid AS remittanceid, cr.receiptdate, cr.receiptno, cr.paidby,
 na.code AS code, na.title AS title, CASE WHEN crv.objid IS NULL THEN cri.amount ELSE 0 END AS amount,
 CASE WHEN crv.objid IS NULL THEN 0 ELSE 1 END AS voided, 
ri.fund_objid, ri.fund_title
FROM cashreceiptitem cri
INNER JOIN itemaccount ri ON cri.item_objid=ri.objid
INNER JOIN cashreceipt cr ON cr.objid=cri.receiptid
INNER JOIN remittance_cashreceipt rem ON cri.receiptid=rem.objid
INNER JOIN liquidation_remittance lr ON lr.objid=rem.remittanceid
LEFT JOIN cashreceipt_void crv on crv.receiptid = cr.objid 
LEFT JOIN sre_revenue_mapping nrm ON nrm.revenueitemid=cri.item_objid
LEFT JOIN sreaccount na ON na.objid=nrm.acctid
WHERE lr.liquidationid=$P{liquidationid} AND crv.objid IS NULL
${filter}
ORDER BY cr.receiptno, na.code


[getAbstractReport]
SELECT 
   CASE WHEN na.objid IS NULL THEN 'UNMAPPED' ELSE na.code END AS `code`, 
   na.title AS title, 
   cri.amount,
   ri.fund_objid, 
   ri.fund_title,
   na.objid AS acctid,
   na.parentid, na.type 
FROM cashreceiptitem cri
INNER JOIN itemaccount ri ON cri.item_objid=ri.objid
INNER JOIN cashreceipt cr ON cr.objid=cri.receiptid
INNER JOIN remittance_cashreceipt rem ON cri.receiptid=rem.objid
INNER JOIN liquidation_remittance lr ON lr.objid=rem.remittanceid
LEFT JOIN cashreceipt_void crv ON crv.receiptid = cr.objid 
LEFT JOIN sre_revenue_mapping nrm ON nrm.revenueitemid=cri.item_objid
LEFT JOIN sreaccount na ON na.objid=nrm.acctid
WHERE lr.liquidationid=$P{liquidationid} AND crv.objid IS NULL
${filter}
ORDER BY ri.fund_title 

