[getUndepositedLiquidations]
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
WHERE lcf.cashier_objid = $P{cashierid}
	and l.state = 'CAPTURE'
AND bdl.objid IS NULL

[getCashierlist]
SELECT 
	distinct lcf.cashier_objid as objid, lcf.cashier_name as name, 
	su.jobtitle as title 
FROM liquidation_fund lcf
inner join liquidation l on l.objid = lcf.liquidationid 
LEFT JOIN bankdeposit_liquidation bdl ON bdl.objid=lcf.objid
left join sys_user su on su.objid = lcf.cashier_objid 
where l.state = 'CAPTURE'
AND bdl.objid IS NULL

[getUndepositedByFund]
SELECT a.fund_objid,
       a.fund_title,
       SUM(a.amount) AS amount
FROM
( SELECT  
	lcf.fund_objid,
	lcf.fund_title,
	lcf.amount
FROM liquidation_fund lcf
INNER JOIN liquidation l ON lcf.liquidationid=l.objid
LEFT JOIN bankdeposit_liquidation bdl ON bdl.objid=lcf.objid
WHERE l.state = 'CAPTURE'
AND lcf.objid in (${cashierfundids} )
AND bdl.objid IS NULL) a
GROUP BY a.fund_objid, a.fund_title

[getUndepositedChecks]
SELECT DISTINCT
crp.objid, crp.refno, crp.particulars, crp.amount  
FROM  liquidation_fund lcf
INNER JOIN liquidation l ON lcf.liquidationid=l.objid 
INNER JOIN liquidation_noncashpayment lc ON lc.liquidationid=lcf.liquidationid
INNER JOIN cashreceiptpayment_noncash crp ON crp.objid=lc.objid
LEFT JOIN bankdeposit_entry_check bec on bec.objid = crp.objid
LEFT JOIN cashreceipt_void cv ON crp.receiptid = cv.receiptid 
WHERE lcf.objid in ( ${cashierfundids} )
	AND l.state = 'CAPTURE'
	and bec.objid is null 
AND cv.objid IS NULL 