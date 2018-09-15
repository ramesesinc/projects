[getBankAccountLedgerItems]
SELECT 
  a.fundid,
  a.bankacctid,
  ba.acctid AS itemacctid,
  ia.code as itemacctcode, 
  ia.title as itemacctname,
  a.dr,
  0 AS cr,
  'bankaccount_ledger' AS _schemaname, 
  ba.acctid, ia.title as acctname 
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
INNER JOIN itemaccount ia on ia.objid = ba.acctid

[getCashLedgerItems]
SELECT tmp.*, ia.code as itemacctcode, ia.title as itemacctname  
FROM 
  (
   SELECT 
   dvf.fundid, 
   (SELECT objid FROM itemaccount WHERE fund_objid = dvf.fundid AND TYPE = 'CASH_IN_TREASURY' LIMIT 1 ) AS itemacctid,
   0 AS dr,
   dvf.amount AS cr,
   'cash_treasury_ledger' AS _schemaname
   FROM depositvoucher_fund dvf
   WHERE dvf.parentid=$P{depositvoucherid} 
  ) tmp
LEFT JOIN  itemaccount ia on ia.objid = tmp.itemacctid  

[getOutgoingItems]
SELECT 
    frdvf.fundid,
    CONCAT(frdvf.fundid,'-TO-',tofund.objid ) AS item_objid,
    CONCAT('DUE TO ', tofund.title ) AS item_title,
    tofund.objid AS item_fund_objid,
    tofund.code AS item_fund_code,
    tofund.title AS item_fund_title,
    'OFT' AS item_type,
    0 AS dr,
    dft.amount AS cr,
    'interfund_transfer_ledger' AS _schemaname
FROM deposit_fund_transfer dft
INNER JOIN depositvoucher_fund todvf ON dft.todepositvoucherfundid = todvf.objid
INNER JOIN fund tofund ON todvf.fundid = tofund.objid
INNER JOIN depositvoucher_fund frdvf ON dft.fromdepositvoucherfundid = frdvf.objid
WHERE frdvf.parentid = $P{depositvoucherid}

[getIncomingItems]
SELECT 
    todvf.fundid,
    CONCAT(todvf.fundid,'-FROM-',fromfund.objid ) AS item_objid,
    CONCAT('DUE FROM ', fromfund.title ) AS item_title,
    fromfund.objid AS item_fund_objid,
    fromfund.code AS item_fund_code,
    fromfund.title AS item_fund_title,
    'IFT' AS item_type,
    dft.amount AS dr,
    0 AS cr,
    'interfund_transfer_ledger' AS _schemaname
FROM deposit_fund_transfer dft
INNER JOIN depositvoucher_fund fromdvf ON dft.fromdepositvoucherfundid = fromdvf.objid
INNER JOIN fund fromfund ON fromdvf.fundid = fromfund.objid
INNER JOIN depositvoucher_fund todvf ON dft.todepositvoucherfundid = todvf.objid
WHERE todvf.parentid = $P{depositvoucherid}


[getCashLedgerItem]
SELECT tmp.*, 
	ia.objid as acctid, ia.title as acctname 
FROM ( 
	SELECT 
		dv.fundid,
		(SELECT objid FROM itemaccount WHERE fund_objid = dv.fundid AND TYPE = 'CASH_IN_TREASURY' LIMIT 1 ) AS itemacctid,  
		0 AS dr,
		a.cr,
		'cash_treasury_ledger' AS _schemaname
	FROM     
	(SELECT 
		 ds.depositvoucherid,
			SUM(ds.amount) AS cr
	FROM depositslip ds 
	WHERE ds.depositvoucherid = $P{depositvoucherid} 
	GROUP BY ds.depositvoucherid) a
	INNER JOIN depositvoucher dv ON a.depositvoucherid = dv.objid 
)tmp 
	left join itemaccount ia on ia.objid = tmp.itemacctid 
