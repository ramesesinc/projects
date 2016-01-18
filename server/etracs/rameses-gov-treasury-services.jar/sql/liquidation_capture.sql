[getList]
SELECT * FROM liquidation 
where txnno like $P{txnno} and state ='CAPTURE'

[getUnliquidatedRemittances]
SELECT 
	r.objid, 
	r.txnno AS remittanceno,
	r.dtposted AS remittancedate,
	r.collector_name,
	r.totalcash,
	r.totalnoncash,
	r.amount 
FROM remittance r
LEFT JOIN liquidation_remittance lr ON r.objid=lr.objid
WHERE r.liquidatingofficer_objid  = $P{liquidatingofficerid}
	and r.state = 'CAPTURE'
	AND lr.objid IS NULL 

[getLiquidatingOfficers]
select 
	distinct r.liquidatingofficer_objid as objid,
	r.liquidatingofficer_name as name, r.liquidatingofficer_title as title 
from remittance r 
	left join liquidation_remittance lr on lr.objid = r.objid 
where lr.objid is null 
   and r.state = 'CAPTURE'

[getUnliquidatedFundSummary]  
SELECT a.fund_objid, a.fund_code, a.fund_title, 
SUM(a.amount) AS amount
FROM 
(SELECT 
	rf.fund_objid,	
    f.code as fund_code,
    rf.fund_title,
    rf.amount AS amount
FROM remittance r
inner join remittance_fund rf on rf.remittanceid = r.objid 
inner join fund f on f.objid = rf.fund_objid 
LEFT JOIN liquidation_remittance lr ON r.objid=lr.objid
WHERE r.objid in ( ${remittances} )  
AND lr.objid IS NULL ) a
GROUP BY a.fund_objid, a.fund_code, a.fund_title  


[getUnliquidatedChecks]
SELECT pc.objid, pc.refno, pc.refdate, pc.particulars,
CASE WHEN cv.objid IS NULL THEN pc.amount ELSE 0 END AS amount,
CASE WHEN cv.objid IS NULL THEN 0 ELSE 1 END AS voided
FROM remittance_noncashpayment rcp 
INNER JOIN cashreceiptpayment_noncash pc ON rcp.objid=pc.objid
INNER JOIN remittance r ON rcp.remittanceid=r.objid
LEFT JOIN cashreceipt_void cv ON cv.receiptid=pc.receiptid
LEFT JOIN liquidation_remittance lr ON r.objid=lr.objid
WHERE r.objid in ( ${remittances} ) 
	and r.state = 'CAPTURE'
	AND lr.objid IS NULL 