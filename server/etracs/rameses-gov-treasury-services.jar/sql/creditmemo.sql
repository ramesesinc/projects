[getBankAccountLedgerItems]
SELECT  
  ba.fund_objid AS fundid,
  ba.objid AS bankacctid,
  ba.acctid AS itemacctid,
  ia.code as itemacctcode, 
  ia.title as itemacctname, 
  SUM(cm.amount) AS dr,
  0 AS cr,
  'bankaccount_ledger' AS _schemaname 
FROM creditmemo cm
INNER JOIN bankaccount ba ON cm.bankaccount_objid = ba.objid
INNER JOIN itemaccount ia ON ba.acctid = ia.objid 
WHERE cm.objid = $P{objid}

[getIncomeLedgerItems]
SELECT 
  ba.fund_objid AS fundid, 
  ia.objid AS itemacctid, 
  ia.code AS itemacctcode, 
  ia.title AS itemacctname, 
  0 AS dr, 
  cmi.amount AS cr, 
  'income_ledger' AS _schemaname  
FROM creditmemoitem cmi 
INNER JOIN creditmemo cm ON cm.objid = cmi.parentid
INNER JOIN bankaccount ba ON cm.bankaccount_objid = ba.objid
INNER JOIN itemaccount ia ON cmi.item_objid = ia.objid
WHERE cm.objid = $P{objid}
