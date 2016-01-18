[getList]
SELECT l.* 
FROM ( 
	SELECT objid FROM liquidation 
	WHERE liquidatingofficer_objid LIKE $P{liquidationofficerid}  
		AND txnno LIKE $P{txnno} 
	UNION 
	SELECT objid FROM liquidation 
	WHERE liquidatingofficer_objid LIKE $P{liquidationofficerid} 
		AND liquidatingofficer_name LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM liquidation 
	WHERE liquidatingofficer_objid LIKE $P{liquidationofficerid} 
		AND dtposted BETWEEN $P{startdate} AND $P{enddate} 
)x 
	INNER JOIN liquidation l ON x.objid=l.objid 
ORDER BY l.dtposted DESC

[getUnliquidatedRemittanceList]
SELECT r.* FROM remittance r
LEFT JOIN liquidation_remittance lr ON r.objid=lr.objid
WHERE lr.objid IS NULL AND state=$P{state} AND liquidatingofficer_objid=$P{userid}

[getUndepositedLiquidations]
select * from liquidation l 
	inner join liquidation_cashier_fund lcf on lcf.liquidationid = l.objid 
	left join bankdeposit_liquidation bl on bl.objid = lcf.objid
where l.liquidatingofficer_objid = $P{liquidatingofficerid} 
	and bl.objid is null 


[getUnliquidatedRemittances]
SELECT 
	r.objid, 
	r.txnno AS remittanceno,
	r.remittancedate AS remittancedate,
	r.collector_name,
	r.totalcash,
	r.totalnoncash,
	r.amount 
FROM remittance r
	LEFT JOIN liquidation_remittance lr ON r.objid=lr.objid
WHERE r.liquidatingofficer_objid = $P{liquidatingofficerid}  
	AND r.state = 'APPROVED'
	AND lr.objid IS NULL 


[getUnliquidatedChecks]
SELECT a.* FROM (
	SELECT 
		pc.objid, pc.refno, pc.refdate, pc.particulars,
		CASE WHEN cv.objid IS NULL THEN pc.amount ELSE 0 END AS amount,
		CASE WHEN cv.objid IS NULL THEN 0 ELSE 1 END AS voided, pc.reftype 
	FROM remittance_noncashpayment rcp 
		INNER JOIN cashreceiptpayment_noncash pc ON rcp.objid=pc.objid
		INNER JOIN remittance r ON rcp.remittanceid=r.objid
		LEFT JOIN cashreceipt_void cv ON cv.receiptid=pc.receiptid
		LEFT JOIN liquidation_remittance lr ON r.objid=lr.objid
	WHERE r.liquidatingofficer_objid = $P{liquidatingofficerid} 
		AND r.state = 'APPROVED'
		AND lr.objid IS NULL
)a 


[getUnliquidatedFundSummary]
SELECT a.* 
FROM (
	SELECT 
		rf.fund_objid, f.code as fund_code, 
		rf.fund_title, SUM(rf.amount) AS amount 
	FROM remittance r 
		inner join remittance_fund rf on rf.remittanceid = r.objid 
		inner join fund f on f.objid = rf.fund_objid 
		LEFT JOIN liquidation_remittance lr ON r.objid=lr.objid
	WHERE r.liquidatingofficer_objid = $P{liquidatingofficerid}  
		AND r.state = 'APPROVED'
		AND lr.objid IS NULL 
	GROUP BY rf.fund_objid, rf.fund_title, f.code  
)a 
ORDER BY a.fund_code 


[postLiquidateRemittance]
INSERT INTO liquidation_remittance (objid, liquidationid)
SELECT r.objid, $P{liquidationid}
FROM remittance r WHERE r.objid IN (${ids})


[postLiquidateNoncash]
INSERT INTO liquidation_noncashpayment (objid, liquidationid  )
SELECT crp.objid, $P{liquidationid} 
FROM cashreceiptpayment_noncash crp 
	INNER JOIN remittance_cashreceipt rc ON rc.objid=crp.receiptid
WHERE rc.remittanceid IN (${ids})


[getLiquidatedNoncash]
SELECT a.* 
FROM (
	SELECT 
		crpc.refno, crpc.particulars, crpc.reftype, 
		CASE WHEN cv.objid IS NULL THEN crpc.amount ELSE 0 END AS amount, 
		CASE WHEN cv.objid IS NULL THEN 0 ELSE 1 END AS voided
	FROM liquidation_noncashpayment rc
		INNER JOIN cashreceiptpayment_noncash crpc ON crpc.objid=rc.objid
		LEFT JOIN cashreceipt_void cv ON crpc.receiptid=cv.receiptid
	WHERE rc.liquidationid = $P{objid}  
)a 


[getFundSummaries]
SELECT lcf.*, f.code as fund_code 
FROM liquidation_cashier_fund lcf
	inner join fund f on f.objid = lcf.fund_objid 
WHERE lcf.liquidationid = $P{liquidationid} 


[updatedPosted]
UPDATE liquidation SET posted=1 WHERE objid=$P{objid}


[updateRemittanceState]
UPDATE remittance SET state=$P{state} WHERE objid=$P{objid}

