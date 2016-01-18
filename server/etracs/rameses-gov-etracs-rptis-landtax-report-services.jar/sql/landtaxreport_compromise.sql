    
[findLedgerInfo]  
SELECT 
    rl.tdno, rl.fullpin, rl.cadastrallotno, b.name AS barangay, 
    rl.totalmv, rl.totalav AS assessedvalue, rl.totalareaha,
    e.name AS taxpayer_name, e.address_text AS taxpayer_address
FROM rptledger rl 
    INNER JOIN barangay b ON rl.barangayid = b.objid 
    INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
WHERE rl.objid = $P{objid}

[getCredits]
select 
	'downpayment' as type,
	'Downpayment' as particular,
	cr.receiptno, 
	cr.receiptdate, 
	max(cro.year) as year, 
	sum(cro.basic - cro.basicdisc) as basic,
	sum(cro.sef - cro.sefdisc) as sef, 
	sum(cro.basicint + cro.sefint) as penalty 
from rptledger_compromise rc 
	inner join cashreceipt cr on rc.downpaymentreceiptid = cr.objid
	inner join cashreceiptitem_rpt_online cro on cr.objid = cro.rptreceiptid 
where rc.objid = $P{objid}
group by cr.receiptno, cr.receiptdate

union all 

select 
	'cypayment' as type,
	'Current Year Payment' as particular,
	cr.receiptno, 
	cr.receiptdate, 
	max(cro.year) as year, 
	sum(cro.basic - cro.basicdisc) as basic,
	sum(cro.sef - cro.sefdisc) as sef, 
	sum(cro.basicint + cro.sefint) as penalty 
from rptledger_compromise rc 
	inner join cashreceipt cr on rc.cypaymentreceiptid = cr.objid
	inner join cashreceiptitem_rpt_online cro on cr.objid = cro.rptreceiptid 
where rc.objid = $P{objid}
group by cr.receiptno, cr.receiptdate 