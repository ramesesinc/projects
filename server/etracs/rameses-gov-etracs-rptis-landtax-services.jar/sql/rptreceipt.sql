[getItemsForPayment]
select rl.objid
from rptbill b 
	inner join rptbill_ledger bl on b.objid = bl.billid 
	inner join rptledger rl on bl.rptledgerid = rl.objid 
	inner join barangay brgy on rl.barangayid = brgy.objid 
	left join municipality m on brgy.parentid = m.objid 
	left join district d  on brgy.parentid = d.objid 
where b.objid = $P{objid}	
and rl.objid like $P{rptledgerid}
and (
 		( rl.lastyearpaid < $P{billtoyear} OR (rl.lastyearpaid = $P{billtoyear} AND rl.lastqtrpaid < $P{billtoqtr}))
 		or 
 		(exists(select * from rptledger_item where parentid = rl.objid))
 	)
order by rl.tdno 

[getItemsForPaymentByLedger]
select rl.objid
from rptledger rl
	inner join barangay brgy on rl.barangayid = brgy.objid 
	left join municipality m on brgy.parentid = m.objid 
	left join district d  on brgy.parentid = d.objid 
where rl.objid = $P{rptledgerid}
and (
 		( rl.lastyearpaid < $P{billtoyear} OR (rl.lastyearpaid = $P{billtoyear} AND rl.lastqtrpaid < $P{billtoqtr}))
 		or 
 		(exists(select * from rptledger_item where parentid = rl.objid))
 	)
order by rl.tdno 

[getItemsForPrinting]
SELECT
	rl.owner_name, 
	rl.tdno,
	rl.rputype,
	rl.totalav, 
	rl.fullpin,
	rl.totalareaha * 10000 AS  totalareasqm,
	rl.cadastrallotno,
	rl.classcode,
	b.name AS barangay,
	md.name as munidistrict,
	pct.name as provcity, 
	rp.fromyear, 
	rp.fromqtr, 
	rp.toyear,
	rp.toqtr,
	SUM(rpi.basic) AS basic,
	SUM(rpi.basicint) AS basicint,
	SUM(rpi.basicdisc) AS basicdisc,
	SUM(rpi.basicdp) AS basicdp,
	SUM(rpi.basicnet) AS basicnet,
	SUM(rpi.basicidle) AS basicidle,
	SUM(rpi.sef) AS sef,
	SUM(rpi.sefint) AS sefint,
	SUM(rpi.sefdisc) AS sefdisc,
	SUM(rpi.sefdp) AS sefdp,
	SUM(rpi.sefnet) AS sefnet,
	SUM(rpi.firecode) AS firecode,
	SUM(rpi.sh) AS sh,
	SUM(rpi.amount) AS amount,
	MAX(rpi.partialled) AS partialled 
FROM rptpayment rp 
	inner join vw_rptpayment_item rpi on rp.objid = rpi.parentid
	INNER JOIN rptledger rl ON rp.refid = rl.objid 
	INNER JOIN sys_org b ON rl.barangayid = b.objid
	inner join sys_org md on md.objid = b.parent_objid 
	inner join sys_org pct on pct.objid = md.parent_objid
WHERE rp.receiptid = $P{objid}
GROUP BY 
	rl.owner_name, 
	rl.tdno,
	rl.rputype,
	rl.totalav, 
	rl.fullpin,
	rl.totalareaha,
	rl.cadastrallotno,
	rl.classcode,
	b.name,
	md.name,
	pct.name,
	rp.fromyear, 
	rp.fromqtr, 
	rp.toyear,
	rp.toqtr
ORDER BY rp.fromyear 	
