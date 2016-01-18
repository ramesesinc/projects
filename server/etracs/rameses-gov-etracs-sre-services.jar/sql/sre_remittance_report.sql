[getSummaryReport]
SELECT cr.receiptdate, cr.receiptno, cr.paidby,
 na.code AS code, na.title AS title, CASE WHEN crv.objid IS NULL THEN cri.amount ELSE 0 END AS amount,
 CASE WHEN crv.objid IS NULL THEN 0 ELSE 1 END AS voided, 
ri.fund_objid, ri.fund_title
FROM cashreceiptitem cri
INNER JOIN itemaccount ri ON cri.item_objid=ri.objid
INNER JOIN cashreceipt cr ON cr.objid=cri.receiptid
INNER JOIN remittance_cashreceipt rem ON cri.receiptid=rem.objid
LEFT JOIN cashreceipt_void crv on crv.receiptid = cr.objid 
LEFT JOIN sre_revenue_mapping nrm ON nrm.revenueitemid=cri.item_objid
LEFT JOIN sreaccount na ON na.objid=nrm.acctid
WHERE rem.remittanceid = $P{remittanceid} 
${filter}
ORDER BY cr.receiptno, na.code

[getAccountGroups]
SELECT 
  objid,
  CASE WHEN parentid IS NULL THEN 'ROOT' ELSE parentid END AS parentid,
  code,
  title,
  type,
  0 AS amount 
FROM sreaccount 
${filter}
ORDER BY code

[getRemittanceItems]
SELECT a.* FROM 
( 
  SELECT 
	ri.objid,
	CASE WHEN nrm.objid IS NULL THEN 'UNMAPPED' ELSE nrm.acctid END AS parentid, 
	ri.code, 
	ri.title,
	'item' AS type,
	SUM(cri.amount)  AS amount,
	ri.fund_objid,
	ri.fund_title
FROM cashreceiptitem cri
INNER JOIN itemaccount ri ON cri.item_objid=ri.objid
INNER JOIN cashreceipt cr ON cr.objid=cri.receiptid
INNER JOIN remittance_cashreceipt rem ON cri.receiptid=rem.objid
LEFT JOIN cashreceipt_void crv ON crv.receiptid = cr.objid 
LEFT JOIN sre_revenue_mapping nrm ON nrm.revenueitemid=cri.item_objid
LEFT JOIN sreaccount na ON na.objid=nrm.acctid
WHERE rem.remittanceid = $P{remittanceid} AND crv.objid IS NULL
${filter}
GROUP BY ri.objid, nrm.acctid, ri.code, ri.title) a
ORDER BY a.code 


