[getBankAccountLedgerItems]
SELECT 
  a.fundid,
  a.bankacctid,
  ba.acctid AS itemacctid,
  a.dr,
  0 AS cr,
  'bankaccount_ledger' AS _schemaname
FROM     
(SELECT 
     dvf.fundid,
	 dvf.parentid AS depositvoucherid, 
    ds.bankacctid,
    SUM(ds.amount) AS dr
FROM depositslip ds 
INNER JOIN depositvoucher_fund dvf ON ds.depositvoucherfundid = dvf.objid
WHERE dvf.parentid = $P{depositvoucherid}  
GROUP BY dvf.parentid, ds.bankacctid) a
INNER JOIN depositvoucher dv ON a.depositvoucherid = dv.objid 
INNER JOIN bankaccount ba ON a.bankacctid = ba.objid


[getCashLedgerItems]
SELECT 
dvf.fundid, 
(SELECT objid FROM itemaccount WHERE fund_objid = dvf.fundid AND TYPE = 'CASH_IN_TREASURY' LIMIT 1 ) AS itemacctid,
0 AS dr,
dvf.amount AS cr,
'cash_treasury_ledger' AS _schemaname
FROM depositvoucher_fund dvf
WHERE dvf.parentid=$P{depositvoucherid} 
