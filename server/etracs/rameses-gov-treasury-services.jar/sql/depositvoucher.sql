[getCollectionVoucherFund]
select 
  fund.depositoryfundid as fundid, 
  sum(cv.totalcash + cv.totalcheck) as amount 
from collectionvoucher v 
  inner join collectionvoucher_fund cv on cv.parentid = v.objid 
  inner join fund on fund.objid = cv.fund_objid 
where v.depositvoucherid = $P{depositvoucherid}
group by fund.depositoryfundid 


[findCollectionVoucherWithoutDepositoryFund]
select distinct 
  fund.objid as fundid, fund.code as fundcode, fund.title as fundtitle
from collectionvoucher v 
  inner join collectionvoucher_fund cv on cv.parentid = v.objid 
  inner join fund on fund.objid = cv.fund_objid 
where v.depositvoucherid = $P{depositvoucherid} 
  and fund.depositoryfundid is null 


[updateChecksForDeposit]
update checkpayment cp set 
    cp.depositvoucherid = $P{depositvoucherid}, 
    cp.fundid = (
        select fund.depositoryfundid from cashreceiptpayment_noncash a, fund 
        where a.refid = cp.objid and a.amount = cp.amount and fund.objid = a.fund_objid  
        limit 1 
    ) 
where cp.objid in (
  select nc.refid from cashreceiptpayment_noncash nc 
      inner join cashreceipt c on c.objid = nc.receiptid 
      inner join remittance r on r.objid = c.remittanceid 
      inner join collectionvoucher cv on cv.objid = r.collectionvoucherid 
      left join cashreceipt_void v on v.receiptid = c.objid
  where cv.depositvoucherid = $P{depositvoucherid}  and v.objid is null 
) 


[getBankAccountLedgerItems]
SELECT 
  a.fundid, a.bankacctid,
  ba.acctid AS itemacctid, ia.code as itemacctcode, ia.title as itemacctname,
  a.dr, 0.0 AS cr, 'bankaccount_ledger' AS _schemaname, ba.acctid, ia.title as acctname 
FROM (
  SELECT 
    dvf.fundid, dvf.parentid AS depositvoucherid, ds.bankacctid, SUM(ds.amount) AS dr
  FROM depositslip ds 
    INNER JOIN depositvoucher_fund dvf ON ds.depositvoucherfundid = dvf.objid
  WHERE dvf.parentid = $P{depositvoucherid} 
  GROUP BY dvf.fundid, dvf.parentid, ds.bankacctid
)a 
  INNER JOIN depositvoucher dv ON a.depositvoucherid = dv.objid 
  INNER JOIN bankaccount ba ON a.bankacctid = ba.objid
  INNER JOIN itemaccount ia on ia.objid = ba.acctid


[getCashLedgerItems]
SELECT tmp.*, ia.code as itemacctcode, ia.title as itemacctname  
FROM (
  SELECT 
    dvf.fundid, ( 
      SELECT a.objid FROM itemaccount a 
      WHERE a.fund_objid = dvf.fundid 
        AND a.type = 'CASH_IN_TREASURY' 
      LIMIT 1 
    ) AS itemacctid,
    0.0 AS dr, dvf.amount AS cr, 
    'cash_treasury_ledger' AS _schemaname
  FROM depositvoucher_fund dvf
  WHERE dvf.parentid = $P{depositvoucherid}  
) tmp
  LEFT JOIN itemaccount ia on ia.objid = tmp.itemacctid  


[getOutgoingItems]
SELECT 
  frdvf.fundid,
  CONCAT(frdvf.fundid, '-TO-', tofund.objid ) AS item_objid,
  CONCAT('DUE TO ', tofund.title ) AS item_title,
  tofund.objid AS item_fund_objid,
  tofund.code AS item_fund_code,
  tofund.title AS item_fund_title,
  'OFT' AS item_type,
  0 AS dr, dft.amount AS cr,
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

