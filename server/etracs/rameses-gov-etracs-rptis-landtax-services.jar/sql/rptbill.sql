[findOpenLedgerById]
SELECT 
	rl.objid,
	rl.lastyearpaid,
	rl.lastqtrpaid,
	rl.undercompromise,
	rl.faasid, 
	rl.tdno,
	rl.taxpayer_objid, 
	e.name AS taxpayer_name, 
	e.address_text AS taxpayer_address, 
	rl.owner_name,
	null as administrator_name,
	rl.rputype,
	rl.fullpin,
	rl.totalareaha,
	rl.totalareaha * 10000 AS totalareasqm,
	rl.totalav,
	rl.taxable,
	b.name AS barangay,
	b.objid AS barangayid,
	rl.cadastrallotno,
	rl.barangayid,
	rl.classcode,
	CASE 
		WHEN rl.nextbilldate IS NULL OR rl.nextbilldate <= NOW() THEN 1 
		WHEN rl.forcerecalcbill = 1 OR rl.forcerecalcbill is null then 1 
		ELSE 0
	END AS recalcbill,
	rl.nextbilldate AS expirydate,
	rl.updateflag
FROM rptledger rl 
	INNER JOIN barangay b ON rl.barangayid = b.objid 
	INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
WHERE rl.objid = $P{rptledgerid}
 AND rl.state = 'APPROVED'
 AND (
 		( rl.lastyearpaid < $P{billtoyear} OR (rl.lastyearpaid = $P{billtoyear} AND rl.lastqtrpaid < $P{billtoqtr}))
 		or 
 		(exists(select * from rptledgeritem where rptledgerid = rl.objid and taxdifference=1 and fullypaid=0))

 	)


[getIncentivesByLedgerId]
SELECT ri.*
FROM rpttaxincentive r
inner join rpttaxincentive_item ri on r.objid = ri.rpttaxincentiveid 
WHERE ri.rptledgerid = $P{rptledgerid}
and r.state = 'APPROVED'


[getItemsForTaxComputation]
select 
	rli.objid, 
	rli.year, 
	1 as qtr, 
	rli.av,
	rli.basicav, 
	rli.sefav, 
	rl.txntype_objid,
	rli.classification_objid,
	rli.actualuse_objid,
	lf.backtax,
	lf.reclassed,
	lf.idleland,
	rli.taxdifference,
	(select effectivityyear from faas where objid = rl.faasid)  as effectivityyear
from rptledger rl 
	inner join rptledgeritem rli on rl.objid = rli.rptledgerid
	inner join rptledgerfaas lf on rli.rptledgerfaasid = lf.objid 
where rl.objid = $P{rptledgerid}
and rli.av > 0.0
and rli.fullypaid = 0


[getItemsForPenaltyDiscount]
select * 
from (
	select 
		rli.objid,
		null as parentid,
		rli.year, 
		(select min(qtr) from rptledgeritem_qtrly where parentid = rli.objid and fullypaid = 0) as qtr, 
		rli.av,
		rli.basicav, 
		rli.sefav, 
		rl.txntype_objid,
		rli.classification_objid,
		rli.actualuse_objid,
		lf.backtax,
		lf.reclassed,
		lf.idleland,
		rli.basic - rli.basicpaid as basic,
		rli.basicidle - rli.basicidlepaid as basicidle,
		rli.sef - rli.sefpaid as sef,
		rli.firecode - rli.firecodepaid as firecode,
		rli.sh - rli.shpaid as sh,
		rli.taxdifference,
		(select effectivityyear from faas where objid = rl.faasid)  as effectivityyear
	from rptledger rl 
		inner join rptledgeritem rli on rl.objid = rli.rptledgerid
		inner join rptledgerfaas lf on rli.rptledgerfaasid = lf.objid 
	where rl.objid = $P{rptledgerid}
	and rli.av > 0.0
	and rli.fullypaid = 0
	and (rli.basicpaid + rli.sefpaid + rli.basicidlepaid + rli.firecodepaid + rli.shpaid) = 0 

	union 

	select 
		rliq.objid,
		rli.objid as parentid,
		rli.year, 
		rliq.qtr, 
		rliq.av,
		rliq.basicav, 
		rliq.sefav, 
		rl.txntype_objid,
		rli.classification_objid,
		rli.actualuse_objid,
		lf.backtax,
		lf.reclassed,
		lf.idleland,
		rliq.basic - rliq.basicpaid as basic,
		rliq.basicidle - rliq.basicidlepaid as basicidle,
		rliq.sef - rliq.sefpaid as sef,
		rliq.firecode - rliq.firecodepaid as firecode,
		rliq.sh - rliq.shpaid as sh,
		rli.taxdifference,
		(select effectivityyear from faas where objid = rl.faasid)  as effectivityyear
	from rptledger rl 
		inner join rptledgeritem rli on rl.objid = rli.rptledgerid
		inner join rptledgeritem_qtrly rliq on rli.objid = rliq.parentid 
		inner join rptledgerfaas lf on rli.rptledgerfaasid = lf.objid 
	where rl.objid = $P{rptledgerid}
	and rliq.av > 0.0
	and rliq.fullypaid = 0
	and (rli.basicpaid + rli.sefpaid + rli.basicidlepaid + rli.firecodepaid + rli.shpaid) <> 0 
)x


[getBillLedgerItems]
select 
	rliq.rptledgerid,
	rli.rptledgerfaasid,
	rli.objid as rptledgeritemid,
	rliq.objid as rptledgeritemqtrlyid, 
	rliq.av,
	rliq.basicav,
	rliq.sefav,
	rliq.year,
	rliq.qtr,
	rliq.basic - rliq.basicpaid as basic,
	rliq.basicint,
	rliq.basicdisc,
	rliq.basicidle - rliq.basicidlepaid as basicidle,
	rliq.basicidledisc,
	rliq.basicidleint,
	rliq.sef - rliq.sefpaid as sef,
	rliq.sefint,
	rliq.sefdisc,
	rliq.firecode - rliq.firecodepaid as firecode,
	rliq.sh - rliq.shpaid as sh,
	rliq.shint,
	rliq.shdisc,
	
	(	rliq.basic - rliq.basicpaid + rliq.basicint - rliq.basicdisc +
		rliq.basicidle - rliq.basicidlepaid + rliq.basicidleint - rliq.basicidledisc
	) as basicnet,
	( rliq.sef - rliq.sefpaid + rliq.sefint - rliq.sefdisc ) as sefnet,
	
	(	rliq.basic - rliq.basicpaid + rliq.basicint - rliq.basicdisc +
		rliq.basicidle - rliq.basicidlepaid + rliq.basicidleint - rliq.basicidledisc +
		rliq.sef - rliq.sefpaid + rliq.sefint - rliq.sefdisc + rliq.firecode - rliq.firecodepaid +
		rliq.sh - rliq.shpaid + rliq.shint - rliq.shdisc 
	) as total,
	rliq.revperiod,
	0 as partialled,
	rli.taxdifference
from rptledgeritem rli 
	inner join rptledgeritem_qtrly rliq on rli.objid = rliq.parentid 
where rli.rptledgerid = $P{rptledgerid}
 and ( rliq.year < $P{billtoyear} or (rliq.year = $P{billtoyear} and rliq.qtr <= $P{billtoqtr}))
 and rli.fullypaid = 0
 and rliq.fullypaid = 0
order by rliq.year, rliq.qtr 



[findExpiry]
SELECT expirydate,expirytype 
FROM rptexpiry 
WHERE iqtr=$P{qtr} AND iyear=$P{year}
AND expirydate >= $P{date}
ORDER BY expirydate ASC


[getBilledLedgers]
SELECT 
	rl.objid,
	rl.lastyearpaid,
	rl.lastqtrpaid,
	rl.faasid, 
	rl.nextbilldate,
	rl.tdno,
	rl.rputype,
	rl.fullpin,
	rl.totalareaha,
	rl.totalareaha * 10000 AS totalareasqm,
	rl.totalav,
	rl.owner_name, 
	b.name AS barangay,
	rl.cadastrallotno,
	rl.barangayid,
	rl.classcode
FROM rptledger rl 
  INNER JOIN barangay b ON rl.barangayid = b.objid 
  INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
WHERE rl.objid IN (
	SELECT rl.objid 
	FROM rptledger rl 
	WHERE ${filters}
	 AND rl.state = 'APPROVED'
	 AND rl.taxable = 1 
	 AND (rl.lastyearpaid < $P{billtoyear} 
		  OR ( rl.lastyearpaid = $P{billtoyear} AND rl.lastqtrpaid < $P{billtoqtr})
	 )

	UNION 

	SELECT rl.objid 
	FROM propertypayer pp
		inner join propertypayer_item ppi on pp.objid = ppi.parentid
		inner join rptledger rl on ppi.rptledger_objid = rl.objid 
	WHERE ${ppfilters}
	 AND rl.state = 'APPROVED'
	 AND rl.taxable = 1 
	 AND (rl.lastyearpaid < $P{billtoyear} 
			OR ( rl.lastyearpaid = $P{billtoyear} AND rl.lastqtrpaid < $P{billtoqtr})
	 )
)
and rl.totalav > 0 
and not exists(select * from faas_restriction where ledger_objid = rl.objid and state='ACTIVE')
ORDER BY rl.tdno  


[deleteRptBillLedger]
DELETE FROM rptbill_ledger 
WHERE billid = $P{objid}
and rptledgerid = $P{rptledgerid}

[deleteRptBill]
DELETE FROM rptbill 
WHERE objid = $P{objid}
and not exists(select * from rptbill_ledger where billid = rptbill.objid )


[getLedgerFaases]
SELECT rlf.* 
FROM rptledger rl
	INNER JOIN rptledgerfaas rlf ON rl.objid = rlf.rptledgerid
WHERE rl.objid =  $P{rptledgerid}
  AND rlf.state = 'APPROVED' 
ORDER BY rlf.fromyear   


[findCollectionTypeByBarcodeKey]
SELECT * FROM collectiontype WHERE barcodekey = $P{barcodekey}
  

[updateLedgerNextBillDate]
UPDATE rptledger SET
	nextbilldate = $P{nextbilldate},
	forcerecalcbill = $P{forcerecalcbill}
WHERE objid = $P{rptledgerid}


[getLedgerFaasesByYear]
select fromyear, fromqtr, toyear, toqtr, assessedvalue 
from rptledgerfaas 
where rptledgerid = $P{rptledgerid}
 and $P{fromyear} >= fromyear 
 and ($P{toyear} <= toyear  or toyear = 0)
 and state = 'APPROVED'


[findCompromiseByLedger]
select objid, state, txnno
from rptledger_compromise 
where rptledgerid = $P{objid}


[findLatestPayment]
select max(x.receiptdate) as receiptdate
from (
    select max(c.receiptdate) as receiptdate 
    from cashreceipt c
        inner join cashreceipt_rpt cr on c.objid = cr.objid 
        inner join rptledger_payment rp on c.objid = rp.receiptid 
        inner join rptledger_payment_item cro on rp.objid = cro.parentid
    where rp.rptledgerid = $P{objid}
    and (cro.year = $P{cy} and cro.qtr = 1)

    union 

    select max(refdate) as receiptdate 
    from rptledger_credit cr 
    where cr.rptledgerid = $P{objid}
     and ((cr.fromyear = $P{cy} and cr.fromqtr = 1) 
                or (cr.toyear = $P{cy} and cr.toqtr >= 1)
                or ($P{cy} > cr.fromyear and $P{cy} < cr.toyear)
        )
)x



[mergeBillLedger]
insert into rptbill_ledger(
	rptledgerid, billid, updateflag
)
select 
	rptledgerid, $P{billid}, updateflag
from rptbill_ledger rbl 
where billid = $P{newbillid}
and not exists(
	select * from rptbill_ledger 
	where billid = $P{billid}
	and rptledgerid = rbl.rptledgerid 
)


[getPaidLedgerBills]
select b.objid, bl.rptledgerid
from rptbill b 
inner join rptbill_ledger bl on b.objid = bl.billid 
where bl.rptledgerid = $P{objid}



[deleteEmptyBills]
delete from rptbill where not exists(select * from rptbill_ledger where billid = rptbill.objid)


[getCurrentYearCredits]	
select x.* 
from (
    select c.receiptdate, min(cro.qtr) as fromqtr, max(cro.qtr) as toqtr
    from cashreceipt c 
    inner join rptledger_payment rp on c.objid = rp.receiptid 
    inner join rptledger_payment_item cro on rp.objid = cro.parentid
    left join cashreceipt_void cv on c.objid = cv.receiptid
    where rp.rptledgerid = $P{objid}
    and cro.year = $P{cy}
    and cv.objid is null 
    group by c.receiptdate, cro.year 

    union 

    select 
        rc.refdate as receiptdate, 
        case when 2017 = rc.fromyear then rc.fromqtr else 1 end as fromqtr,
        case when 2017 = rc.toyear then rc.toqtr else 4 end as toqtr
    from rptledger_credit rc
    where rc.rptledgerid = $P{objid}
    and $P{cy} >= rc.fromyear and $P{cy} <= rc.toyear 
)x 
order by x.fromqtr 
