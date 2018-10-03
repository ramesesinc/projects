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
update cp set 
    cp.depositvoucherid = $P{depositvoucherid}, 
    cp.fundid = (
        select top 1 fund.depositoryfundid from cashreceiptpayment_noncash a, fund 
        where a.refid = cp.objid and a.amount = cp.amount and fund.objid = a.fund_objid  
    ) 
from checkpayment cp  
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
