[getFundlist]
select 
  distinct lcf.fund_objid as objid , lcf.fund_title as title 
from bankdeposit bd 
  inner join bankdeposit_liquidation bl on bd.objid = bl.bankdepositid 
  inner join liquidation_fund lcf on lcf.objid = bl.objid  
where bd.objid = $P{bankdepositid}


[getReportOfCollectionsByFund]
SELECT 
  bd.cashier_name, bd.cashier_title, bd.dtposted, bd.txnno,
  cr.collector_name, rem.txnno AS remittanceno,
  ri.fund_title,
  SUM(cri.amount) AS amount 
FROM bankdeposit bd 
  INNER JOIN bankdeposit_liquidation bl ON bd.objid = bl.bankdepositid
  INNER JOIN liquidation_fund lf ON bl.objid = lf.objid 
  INNER JOIN liquidation_remittance lr ON lf.liquidationid = lr.liquidationid
  INNER JOIN remittance rem ON lr.objid = rem.objid 
  INNER JOIN remittance_cashreceipt rc ON rem.objid = rc.remittanceid
  INNER JOIN cashreceipt cr ON rc.objid = cr.objid 
  INNER JOIN cashreceiptitem cri ON cr.objid = cri.receiptid
  INNER JOIN itemaccount ri ON cri.item_objid = ri.objid AND ri.fund_objid = lf.fund_objid
  LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid
WHERE bd.objid = $P{bankdepositid}
  AND ri.fund_objid = $P{fundid}
  AND cv.objid IS NULL
GROUP BY 
  bd.cashier_name, bd.dtposted, bd.txnno,
  cr.collector_name, rem.txnno, ri.fund_title
ORDER BY cr.collector_name, rem.txnno 


[getReportOfDepositsByFund]
select 
  bd.cashier_name, bd.cashier_title, bd.dtposted, bd.txnno, f.title AS fund_title,
  CONCAT(ba.bank_code, ' - Cash D/S: Account ', be.bankaccount_code ) as depositref, 
  be.totalcash AS amount,
  CONCAT(ba.bank_code, ' - ', b.branchname) AS bankacctname
from bankdeposit bd 
 inner join bankdeposit_entry be on bd.objid = be.parentid 
 inner join bankaccount ba on ba.objid = be.bankaccount_objid 
 inner join bank b on ba.bank_objid = b.objid 
 inner join fund f on ba.fund_objid = f.objid
where bd.objid= $P{bankdepositid} 
  and f.objid = $P{fundid}
  and be.totalcash > 0.0 

UNION ALL 

select 
  bd.cashier_name, bd.cashier_title, bd.dtposted, bd.txnno, f.title AS fund_title,
  CONCAT(ba.bank_code, ' - Check D/S: Account ', be.bankaccount_code ) as depositref, 
  be.totalnoncash AS amount,
  CONCAT(ba.bank_code, ' - ', b.branchname) AS bankacctname
from bankdeposit bd 
 inner join bankdeposit_entry be on bd.objid = be.parentid 
 inner join bankaccount ba on ba.objid = be.bankaccount_objid 
 inner join bank b on ba.bank_objid = b.objid 
 inner join fund f on ba.fund_objid = f.objid
where bd.objid= $P{bankdepositid} 
  and f.objid = $P{fundid}
  and be.totalnoncash > 0.0


[getJevEntries]
SELECT 
  ria.code AS account_code,
  ria.title AS account_title,
  0.0 AS debit, 
  SUM(cri.amount) AS credit
FROM bankdeposit bd 
  INNER JOIN bankdeposit_liquidation bl ON bd.objid = bl.bankdepositid
  INNER JOIN liquidation_fund lf ON bl.objid = lf.objid AND lf.fund_objid = $P{fundid}
  INNER JOIN liquidation_remittance lr ON lf.liquidationid = lr.liquidationid
  INNER JOIN remittance_cashreceipt rc ON lr.objid = rc.remittanceid
  INNER JOIN cashreceipt cr ON rc.objid = cr.objid 
  INNER JOIN cashreceiptitem cri ON cr.objid = cri.receiptid
  LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid
  INNER JOIN itemaccount ri ON cri.item_objid = ri.objid AND ri.fund_objid = $P{fundid}
  LEFT JOIN ngas_revenue_mapping rm ON ri.objid = rm.itemaccountid 
  LEFT JOIN ngasaccount ria ON ria.objid=rm.acctid
WHERE bd.objid = $P{bankdepositid} 
  AND cr.formno <> '56'
  AND cv.objid IS NULL 
GROUP BY ria.code, ria.title
ORDER BY ria.code


[findRPTBasicDiscount]
SELECT SUM(ro.basicdisc) AS discount 
FROM bankdeposit bd 
  INNER JOIN bankdeposit_liquidation bl ON bd.objid = bl.bankdepositid
  INNER JOIN liquidation_fund lf ON bl.objid = lf.objid AND lf.fund_objid = $P{fundid}
  INNER JOIN liquidation_remittance lr ON lf.liquidationid = lr.liquidationid
  INNER JOIN remittance_cashreceipt rc ON lr.objid = rc.remittanceid
  INNER JOIN cashreceiptitem_rpt_online ro ON rc.objid = ro.rptreceiptid
  LEFT JOIN cashreceipt_void cv ON rc.objid = cv.receiptid
where bd.objid = $P{bankdepositid} 
  AND cv.objid IS NULL 


[findRPTReceivables]
SELECT 
  '127' AS account_code,
  '      RPT RECEIVABLE' AS account_title,
  0.0 AS debit,
  SUM(cri.amount) AS credit
FROM bankdeposit bd 
  INNER JOIN bankdeposit_liquidation bl ON bd.objid = bl.bankdepositid
  INNER JOIN liquidation_fund lf ON bl.objid = lf.objid AND lf.fund_objid = $P{fundid}
  INNER JOIN liquidation_remittance lr ON lf.liquidationid = lr.liquidationid
  INNER JOIN remittance_cashreceipt rc ON lr.objid = rc.remittanceid
  INNER JOIN cashreceipt cr ON rc.objid = cr.objid 
  INNER JOIN cashreceiptitem_rpt_account cri ON cr.objid = cri.rptreceiptid
  INNER JOIN itemaccount ri ON cri.item_objid = ri.objid AND ri.fund_objid = $P{fundid}
  LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid
WHERE bd.objid = $P{bankdepositid} 
  AND cr.formno = '56'
  AND cri.revtype LIKE $P{revtype}
  AND cv.objid IS NULL




[getRPTIncomes]
SELECT 
  ria.code AS account_code,
  ria.title AS account_title,
  0.0 AS debit,
  SUM(cri.amount) AS credit
FROM bankdeposit bd 
  INNER JOIN bankdeposit_liquidation bl ON bd.objid = bl.bankdepositid
  INNER JOIN liquidation_fund lf ON bl.objid = lf.objid AND lf.fund_objid = $P{fundid}
  INNER JOIN liquidation_remittance lr ON lf.liquidationid = lr.liquidationid
  INNER JOIN remittance_cashreceipt rc ON lr.objid = rc.remittanceid
  INNER JOIN cashreceipt cr ON rc.objid = cr.objid 
  INNER JOIN cashreceiptitem cri ON cr.objid = cri.receiptid
  LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid
  INNER JOIN itemaccount ri ON cri.item_objid = ri.objid AND ri.fund_objid = $P{fundid}
  LEFT JOIN ngas_revenue_mapping rm ON ri.objid = rm.revenueitemid
  LEFT JOIN ngasaccount ria ON ria.objid=rm.acctid 
WHERE bd.objid = $P{bankdepositid} 
  AND cr.formno = '56'
  AND cv.objid IS NULL
GROUP BY ria.code, ria.title
ORDER BY ria.code


[getRPTShares]
SELECT 
  sharetype, SUM(cri.amount) AS share
FROM bankdeposit bd 
  INNER JOIN bankdeposit_liquidation bl ON bd.objid = bl.bankdepositid
  INNER JOIN liquidation_fund lf ON bl.objid = lf.objid AND lf.fund_objid = $P{fundid}
  INNER JOIN liquidation_remittance lr ON lf.liquidationid = lr.liquidationid
  INNER JOIN remittance_cashreceipt rc ON lr.objid = rc.remittanceid
  INNER JOIN cashreceipt cr ON rc.objid = cr.objid 
  INNER JOIN cashreceiptitem_rpt_account cri ON cr.objid = cri.rptreceiptid
  INNER JOIN itemaccount ri ON cri.item_objid = ri.objid AND ri.fund_objid = $P{fundid}
  LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid
WHERE bd.objid = $P{bankdepositid}
  AND cr.formno = '56'
  AND cv.objid IS NULL
  AND cri.revtype LIKE $P{revtype}
  AND cri.sharetype <> $P{orgtype}
GROUP BY sharetype