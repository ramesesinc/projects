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


[getUnliquidatedRemittances]
select r.*, r.txnno as remittanceno 
from remittance r
	left join liquidation_remittance lr ON r.objid=lr.objid
where lr.objid IS NULL 
	and r.liquidatingofficer_objid = $P{liquidatingofficerid}  
	and r.state = 'APPROVED' 
	${filter} 


[getUnliquidatedFundSummary]
select a.* 
from (
	select 
		rf.fund_objid, f.code as fund_code, 
		rf.fund_title, SUM(rf.amount) AS amount 
	from remittance r 
		inner join remittance_fund rf on rf.remittanceid = r.objid 
		inner join fund f on f.objid = rf.fund_objid 
		left join liquidation_remittance lr ON lr.objid = r.objid 
	where lr.objid IS NULL 
		and r.liquidatingofficer_objid = $P{liquidatingofficerid} 
		and r.state = 'APPROVED' 
		${filter} 
	group by rf.fund_objid, rf.fund_title, f.code 
)a 
order by a.fund_code, a.fund_title 


[getUnliquidatedChecks]
select a.* 
from (
	select 
		pc.objid, pc.refno, pc.refdate, pc.particulars,
		(case when cv.objid IS NULL then pc.amount else 0 end) as amount,
		(case when cv.objid IS NULL then 0 else 1 end ) as voided, pc.reftype 
	from remittance r 
		inner join remittance_noncashpayment rcp on rcp.remittanceid = r.objid 
		inner join cashreceiptpayment_noncash pc ON pc.objid = rcp.objid 
		left join cashreceipt_void cv ON cv.receiptid = pc.receiptid 
		left join liquidation_remittance lr ON lr.objid = r.objid 
	WHERE lr.objid IS NULL 
		AND r.liquidatingofficer_objid = $P{liquidatingofficerid} 
		AND r.state = 'APPROVED' 
		${filter} 
)a 
order by a.refdate, a.refno 


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


[getFundsForCashBook]
select 
  	l.dtposted as refdate, l.objid as refid, l.txnno as refno, 
  	lcf.fund_objid, lcf.fund_title, fund.code as fund_code, lcf.amount, 
  	l.liquidatingofficer_objid as subacct_objid, 
	l.liquidatingofficer_name as subacct_name 
from liquidation_cashier_fund lcf 
	inner join liquidation l on lcf.liquidationid=l.objid 
	left join fund on lcf.fund_objid=fund.objid 
where lcf.liquidationid=$P{liquidationid} 


[getUndepositedLiquidations]
select * from liquidation l 
	inner join liquidation_cashier_fund lcf on lcf.liquidationid = l.objid 
	left join bankdeposit_liquidation bl on bl.objid = lcf.objid
where l.liquidatingofficer_objid = $P{liquidatingofficerid} 
	and bl.objid is null 

