[getList]
SELECT l.* 
FROM ( 
	SELECT objid FROM bankdeposit 
	WHERE cashier_objid LIKE $P{cashierid} 
		AND txnno LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM bankdeposit 
	WHERE cashier_objid LIKE $P{cashierid} 
		AND cashier_name LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM bankdeposit 
	WHERE cashier_objid LIKE $P{cashierid} 
		AND dtposted BETWEEN $P{startdate}  AND $P{enddate}  
	UNION 
	SELECT DISTINCT bdl.bankdepositid AS objid 
	FROM liquidation l 
		INNER JOIN liquidation_fund lcf ON l.objid=lcf.liquidationid
		INNER JOIN bankdeposit_liquidation bdl ON lcf.objid=bdl.objid 
		INNER JOIN bankdeposit bd ON bdl.bankdepositid=bd.objid 
	WHERE l.txnno LIKE $P{searchtext} 
		AND bd.cashier_objid LIKE $P{cashierid} 
)x 
	INNER JOIN bankdeposit l ON x.objid=l.objid 
ORDER BY l.dtposted desc 


[getListByLiquidationNo]
SELECT distinct b.* FROM bankdeposit b
	inner join bankdeposit_liquidation bl on bl.bankdepositid = b.objid 
	inner join liquidation_fund lcf on lcf.objid = bl.objid 
	inner join liquidation l on lcf.liquidationid = l.objid 
where b.cashier_objid like $P{cashierid} 
	and l.txnno like $P{searchtext} 
order by b.dtposted desc 


[getUndepositedLiquidation]
SELECT  
	l.objid, 
	l.txnno as liquidationno,
	min(l.dtposted) as liquidationdate,
	min(l.liquidatingofficer_objid) as liquidatingofficer_objid, 
	min(l.liquidatingofficer_name) as liquidatingofficer_name, 	
	min(l.liquidatingofficer_title) as liquidatingofficer_title, 
	sum(lcf.amount) as amount
FROM liquidation l
INNER JOIN liquidation_fund lcf ON lcf.liquidationid=l.objid
LEFT JOIN bankdeposit_liquidation bdl ON bdl.objid=lcf.objid
WHERE lcf.cashier_objid = $P{cashierid}
	and l.state = 'OPEN' 
AND bdl.objid IS NULL
group by l.objid, l.txnno 


[getUndeposited]
SELECT  lcf.objid, 
	l.txnno as liquidationno,
	l.dtposted as liquidationdate,
	lcf.fund_objid,
	lcf.fund_title,
	liquidatingofficer_objid, 
	liquidatingofficer_name, 	
	liquidatingofficer_title, 
	lcf.amount
FROM liquidation_fund lcf
INNER JOIN liquidation l ON lcf.liquidationid=l.objid
LEFT JOIN bankdeposit_liquidation bdl ON bdl.objid=lcf.objid
WHERE l.objid in ( ${liquidationids} )
	and lcf.cashier_objid = $P{cashierid}  
	and l.state = 'OPEN'
AND bdl.objid IS NULL


[getUndepositedByFund]
SELECT  
	lcf.fund_objid,
	lcf.fund_title,
	sum(lcf.amount)  as totalamount 
FROM liquidation l
INNER JOIN liquidation_fund lcf ON lcf.liquidationid=l.objid
LEFT JOIN bankdeposit_liquidation bdl ON bdl.objid=lcf.objid
WHERE l.state = 'OPEN'
  AND l.objid in ( ${liquidationids} )
 and lcf.cashier_objid = $P{cashierid}   
AND bdl.objid IS NULL 
GROUP BY lcf.fund_objid, lcf.fund_title

[findCrediteMemos]
SELECT sum(crp.amount) as creditmemo 
FROM  liquidation_noncashpayment lc 
INNER JOIN cashreceiptpayment_noncash crp ON crp.objid=lc.objid
LEFT JOIN cashreceipt_void cv ON crp.receiptid = cv.receiptid 
WHERE lc.liquidationid  in ( ${liquidationids} )
	 and crp.reftype = 'CREDITMEMO'
	 and crp.account_fund_objid=$P{fundid}
	AND cv.objid IS NULL 


[getUndepositedChecks]
SELECT DISTINCT
crp.objid, crp.refno, crp.particulars, crp.amount, crp.reftype   
FROM   liquidation l  
INNER JOIN liquidation_noncashpayment lc ON lc.liquidationid=l.objid 
INNER JOIN cashreceiptpayment_noncash crp ON crp.objid=lc.objid
LEFT JOIN bankdeposit_entry_check bec on bec.objid = crp.objid
LEFT JOIN cashreceipt_void cv ON crp.receiptid = cv.receiptid 
WHERE l.objid  in ( ${liquidationids} )
	 AND l.state = 'OPEN'
	 and bec.objid is null 
	 and crp.reftype = 'CHECK'
AND cv.objid IS NULL 

[getUndepositedChecksByFund]
SELECT DISTINCT
crp.objid, crp.refno, crp.particulars, crp.amount, crp.reftype   
FROM  liquidation_fund lcf  
INNER JOIN liquidation_noncashpayment lc ON lc.liquidationfundid=lcf.objid
inner join liquidation l on l.objid = lcf.liquidationid 
INNER JOIN cashreceiptpayment_noncash crp ON crp.objid=lc.objid 
LEFT JOIN bankdeposit_entry_check bec on bec.objid = crp.objid
LEFT JOIN cashreceipt_void cv ON crp.receiptid = cv.receiptid 
WHERE lcf.cashier_objid=$P{cashierid}
	AND lcf.fund_objid in ( ${fundids} )
	and l.state = 'OPEN'
	and bec.objid is null 
	and crp.reftype = 'CHECK' 
AND cv.objid IS NULL 

[getDepositSummaries]
SELECT 
	 be.*,ba.fund_objid, ba.fund_code, ba.fund_title,
	 ba.bank_code, ba.bank_name, ba.bank_objid, b.branchname, ba.accttype, 
	 ba.cashreport, ba.cashbreakdownreport, ba.checkreport, ba.checkbreakdownreport
FROM bankdeposit_entry be
	INNER JOIN bankaccount ba ON be.bankaccount_objid = ba.objid
	left join bank b on b.objid = ba.bank_objid 
WHERE be.parentid = $P{objid}
ORDER BY ba.fund_title 

[getDepositedChecks]
select 
	crp.* 
from bankdeposit_entry_check bec
  inner join cashreceiptpayment_noncash crp on crp.objid = bec.objid 
where bec.parentid=$P{objid} 
order by crp.bank 


[getFundSummaries]
SELECT  
	lcf.fund_objid,
	lcf.fund_title,
	sum(lcf.amount) as totalamount 
FROM bankdeposit_liquidation bdl 
	INNER JOIN liquidation_fund lcf ON bdl.objid=lcf.objid
	INNER JOIN liquidation l ON lcf.liquidationid=l.objid
WHERE bdl.bankdepositid = $P{objid} 
GROUP BY lcf.fund_objid,lcf.fund_title



[getPostedLiquidations]
SELECT  lcf.objid, 
	l.objid as liquidationid, 
	l.txnno as liquidationno,
	l.dtposted as liquidationdate,
	lcf.fund_objid,
	lcf.fund_title,
	l.liquidatingofficer_objid, 
	l.liquidatingofficer_name, 	
	l.liquidatingofficer_title, 
	lcf.amount
FROM bankdeposit_liquidation bdl 
INNER join liquidation_fund lcf on lcf.objid = bdl.objid 
INNER JOIN liquidation l ON lcf.liquidationid=l.objid
WHERE bdl.bankdepositid=$P{objid}



[findFundByBankEntryId]
SELECT ba.fund_objid AS objid, ba.fund_title AS title
FROM bankdeposit_entry be
	INNER JOIN bankaccount ba ON be.bankaccount_objid = ba.objid 
WHERE be.objid = $P{objid}	


[getBankAccountsByFund]
SELECT * FROM bankaccount WHERE fund_objid = $P{objid}


[validateBankEntry]
update bankdeposit_entry set 
	state='CLOSED',  
	validationno=$P{validationno},
	validationdate=$P{validationdate}
where objid=$P{objid} 

[findOpenBankEntry]
select objid from bankdeposit_entry where parentid=$P{objid} and state='OPEN'

[closeDeposit]
update bankdeposit set state='CLOSED' WHERE objid=$P{objid} 

[getDepositsForCashBook]
select 
	bd.txnno, bd.dtposted as refdate, bde.objid as refid, bd.txnno as refno,  
	ba.fund_objid, ba.fund_title, fund.code as fund_code, bde.amount, 
	bd.cashier_objid as subacct_objid, bd.cashier_name as subacct_name 
from bankdeposit bd 
	inner join bankdeposit_entry bde on bd.objid=bde.parentid 
	inner join bankaccount ba on bde.bankaccount_objid=ba.objid 
	left join fund on ba.fund_objid=fund.objid 
where bd.objid=$P{bankdepositid} 

[getFundsForCashBook]
select 
	bd.dtposted as refdate, bd.objid as refid, bd.txnno as refno, 
	lcf.fund_objid, lcf.fund_title, fund.code as fund_code, lcf.amount, 
	l.liquidatingofficer_objid as subacct_objid, 
	l.liquidatingofficer_name as subacct_name, 
	bd.cashier_objid, bd.cashier_name 
from bankdeposit bd 
	inner join bankdeposit_liquidation bdl on bd.objid=bdl.bankdepositid 
	inner join liquidation_fund lcf on bdl.objid=lcf.objid 
	inner join liquidation l on lcf.liquidationid=l.objid 
	left join fund on lcf.fund_objid=fund.objid 
where bd.objid=$P{bankdepositid}  
