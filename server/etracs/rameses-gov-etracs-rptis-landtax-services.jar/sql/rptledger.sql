[getList]
SELECT 
	rl.objid, rl.state, rl.faasid, rl.tdno, rl.prevtdno, rl.titleno,
	rl.taxpayer_objid, e.name AS taxpayer_name, rl.administrator_name,
	rl.fullpin, rl.cadastrallotno, rl.totalareaha, rl.classcode, rl.rputype,  
	rl.totalmv, rl.totalav, rl.lastyearpaid, rl.lastqtrpaid,
	CASE WHEN rl.faasid IS NULL THEN 'M' ELSE '' END AS type,
	b.objid AS barangay_objid, b.name AS barangay_name
FROM rptledger rl 
	INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
	INNER JOIN barangay b ON rl.barangayid = b.objid 
WHERE 1=1
${filters}	


[findById]
SELECT 
	rl.*,
	rl.totalareaha * 10000 as totalareasqm,
	e.name AS taxpayer_name, 
	e.address_text AS taxpayer_address,
	b.name AS barangay_name
FROM rptledger rl 
	INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
	INNER JOIN barangay b ON rl.barangayid = b.objid 
WHERE rl.objid = $P{objid}


	
[findApprovedLedgerByFaasId]
SELECT rl.* , r.taxable 
FROM rptledger rl 
	INNER JOIN faas f ON rl.faasid = f.objid 
	INNER JOIN rpu r ON f.rpuid = r.objid 
WHERE rl.faasid = $P{faasid} AND rl.state = 'APPROVED' 


[findLedgerByFaasId]	
SELECT * FROM rptledger WHERE faasid = $P{faasid} 


[getLedgerFaases]
SELECT rlf.*,
	pc.code AS classification_code,
	pc.name AS classification_name,
	pc1.code AS actualuse_code,
	pc1.name AS actualuse_name
FROM rptledgerfaas rlf
	INNER JOIN propertyclassification pc ON rlf.classification_objid = pc.objid 
	LEFT JOIN propertyclassification pc1 ON rlf.actualuse_objid = pc1.objid 
WHERE rlf.rptledgerid = $P{rptledgerid} 
ORDER BY rlf.fromyear DESC, rlf.tdno   DESC 

[findLedgerFaasByFaasId]
SELECT rlf.*,
	pc.code AS classification_code,
	pc.name AS classification_name,
	pc1.code AS actualuse_code,
	pc1.name AS actualuse_name
FROM rptledgerfaas rlf
	INNER JOIN propertyclassification pc ON rlf.classification_objid = pc.objid 
	LEFT JOIN propertyclassification pc1 ON rlf.actualuse_objid = pc1.objid 
WHERE rlf.rptledgerid =  $P{rptledgerid} 
  AND $P{yr} >= rlf.fromyear 
  AND $P{yr} <= (CASE WHEN rlf.toyear = 0 THEN $P{yr} ELSE rlf.toyear END)
  AND rlf.state ='APPROVED'


[closePreviousFaasToYearAndQtr]
UPDATE rptledgerfaas SET
	toyear = $P{toyear},
	toqtr  = $P{toqtr}
WHERE objid <> $P{objid} AND rptledgerid = $P{rptledgerid}	AND toyear = 0 



[clearNextBillDateByLedger]
UPDATE rptledger SET 
	nextbilldate = NULL 
WHERE objid = $P{objid} 
  AND state = 'APPROVED'

[clearNextBillDate]
UPDATE rptledger SET 
	nextbilldate = NULL 
WHERE state = 'APPROVED'



[getLedgerCredits]
SELECT t.*
FROM (
	SELECT 
		objid AS rptreceiptid,
		refno,
		refdate ,
		paidby_name,
		fromyear,
		fromqtr,
		toyear, 
		toqtr,
		basic,
		basicint,
		basicdisc,
		basicidle,
		sef,
		sefint,
		sefdisc,
		firecode,
		amount,
		type AS txnmode,
		0 as partialled
	FROM rptledger_credit rc 
	WHERE rptledgerid = $P{rptledgerid}

	UNION ALL
	
	SELECT 
		cr.objid AS rptreceiptid, 
		cr.receiptno AS refno,
		cr.receiptdate AS refdate,
		cr.paidby AS paidby_name,
		cri.year AS fromyear,
		case when cri.qtr = 0 then cri.fromqtr else cri.qtr end AS fromqtr,
		cri.year AS toyear,
		case when cri.qtr = 0 then cri.toqtr else cri.qtr end AS toqtr,
		cri.basic AS basic,
		cri.basicint AS basicint,
		cri.basicdisc AS basicdisc,
		cri.basicidle- cri.basicidledisc + cri.basicidleint AS basicidle,
		cri.sef AS sef,
		cri.sefint AS sefint,
		cri.sefdisc AS sefdisc,
		cri.firecode AS firecode,
		cri.basic+ cri.basicint - cri.basicdisc + cri.basicidle - cri.basicidledisc + cri.basicidleint +
				cri.sef + cri.sefint - cri.sefdisc + cri.firecode AS amount,
		crr.txntype AS txnmode,
		cri.partialled
	FROM cashreceipt_rpt crr
		INNER JOIN cashreceipt cr ON crr.objid = cr.objid 
		INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid 	
		LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
	WHERE cri.rptledgerid = $P{rptledgerid}
		AND cv.objid IS NULL 	
    AND cri.partialled = 1 

	UNION ALL 

	select 				
			x.rptreceiptid, 
				x.refno,
				x.refdate,
				x.paidby_name,
				x.fromyear,
				(select min(fromqtr) from cashreceiptitem_rpt_online where rptledgerid = $P{rptledgerid} and rptreceiptid = x.rptreceiptid and year = x.fromyear and partialled = 0) as fromqtr,
				x.toyear,
				(select max(toqtr) from cashreceiptitem_rpt_online where rptledgerid = $P{rptledgerid} and  rptreceiptid = x.rptreceiptid and year = x.toyear and partialled = 0) as toqtr, 
				x.basic,
				x.basicint,
				x.basicdisc,
				x.basicidle,
				x.sef,
				x.sefint,
				x.sefdisc,
				x.firecode,
				x.amount,
				x.txnmode,
				x.partialled
	from (
		SELECT 
				cr.objid AS rptreceiptid, 
				cr.receiptno AS refno,
				cr.receiptdate AS refdate,
				cr.paidby AS paidby_name,
				min(cri.year) AS fromyear,
				0 AS fromqtr,
				max(cri.year) AS toyear,
				0 as toqtr,
				sum(cri.basic) AS basic,
				sum(cri.basicint) AS basicint,
				sum(cri.basicdisc) AS basicdisc,
				sum(cri.basicidle- cri.basicidledisc + cri.basicidleint) AS basicidle,
				sum(cri.sef) AS sef,
				sum(cri.sefint) AS sefint,
				sum(cri.sefdisc) AS sefdisc,
				sum(cri.firecode) AS firecode,
				sum(cri.basic+ cri.basicint - cri.basicdisc + cri.basicidle - cri.basicidledisc + cri.basicidleint +
						cri.sef + cri.sefint - cri.sefdisc + cri.firecode) AS amount,
				crr.txntype AS txnmode,
				cri.partialled
			FROM cashreceipt_rpt crr
				INNER JOIN cashreceipt cr ON crr.objid = cr.objid 
				INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid 	
				LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
			WHERE cri.rptledgerid = $P{rptledgerid}
				AND cv.objid IS NULL
				AND cri.partialled = 0 	
		group by 
			cr.objid,
			cr.receiptno,
			cr.receiptdate,
			cr.paidby,
			crr.txntype,
			cri.year, 
			cri.partialled		
	) x 

) t
ORDER BY t.refdate desc, t.fromyear desc, t.fromqtr desc 






[approveLedgerFaas]
UPDATE rptledgerfaas SET state = 'APPROVED' WHERE objid = $P{objid}


[updateLastYearQtrPaid]
UPDATE rptledger SET 
	lastyearpaid = $P{toyear}, lastqtrpaid = $P{toqtr}, nextbilldate = null 
WHERE objid = $P{rptledgerid}


[updateState]
UPDATE rptledger SET state = $P{state} WHERE objid = $P{objid}



[fixLedgerInfo]
UPDATE rptledger SET 
	taxpayer_objid = $P{taxpayerid},
	owner_name = $P{taxpayername},
	tdno = $P{tdno},
	lastyearpaid = $P{lastyearpaid}, 
	lastqtrpaid = $P{lastqtrpaid},
	taxable = $P{taxable},
	nextbilldate = null
WHERE objid = $P{rptledgerid}


[setLedgerItemFullyPaidFlag]
update rptledgeritem set 
	fullypaid = 1
where rptledgerid = $P{rptledgerid}
  and ( 
  		year < $P{lastyearpaid} or 
  		(year = $P{lastyearpaid} and 4 = $P{lastqtrpaid})
  	)

[setQtrlyItemFullyPaidFlag]
update rptledgeritem_qtrly set 
	fullypaid = 1,
	partialled = 0 
where rptledgerid = $P{rptledgerid}
  and (
  		year < $P{lastyearpaid} or 
  		(year = $P{lastyearpaid} and qtr <= $P{lastqtrpaid})
  	)



[resetLedgerItemFullyPaidFlag]
update rptledgeritem set 
	fullypaid = 0,
	basicpaid = 0.0,
	basicintpaid = 0.0,
	basicdisctaken = 0.0,
	basicidlepaid = 0.0,
	basicidledisctaken = 0.0,
	basicidleintpaid = 0.0,
	sefpaid = 0.0,
	sefintpaid = 0.0,
	sefdisctaken = 0.0,
	firecodepaid = 0.0
where rptledgerid = $P{rptledgerid}
  and (
  		year > $P{lastyearpaid} or 
  		(year = $P{lastyearpaid} and $P{lastqtrpaid} <> 4)
  	)

[resetQtrlyItemFullyPaidFlag]
update rptledgeritem_qtrly set 
	fullypaid = 0,
	partialled = 0,
	basicpaid = 0.0,
	basicintpaid = 0.0,
	basicdisctaken = 0.0,
	basicidlepaid = 0.0,
	basicidledisctaken = 0.0,
	basicidleintpaid = 0.0,
	sefpaid = 0.0,
	sefintpaid = 0.0,
	sefdisctaken = 0.0,
	firecodepaid = 0.0
where rptledgerid = $P{rptledgerid}
  and ( 
  		year > $P{lastyearpaid} or 
  		(year = $P{lastyearpaid} and qtr > $P{lastqtrpaid})
  	)



[getLedgerItemQtrlyAggregates]
select 
 	rli.objid, 
 	rli.basic,
	sum(rliq.basic) as basicpaid,
 	rli.basicint as basicint,
	sum(rliq.basicint) as basicintpaid,
 	rli.basicdisc as basicdisc,
	sum(rliq.basicdisc) as basicdisctaken,
 	rli.basicidle as basicidle,
	sum(rliq.basicidle) as basicidlepaid,
 	rli.basicidledisc as basicidledisc,
	sum(rliq.basicidledisc) as basicidledisctaken,
 	rli.basicidleint as basicidleint,
	sum(rliq.basicidleint) as basicidleintpaid,
 	rli.sef,
	sum(rliq.sef) as sefpaid,
 	rli.sefint as sefint,
	sum(rliq.sefint) as sefintpaid,
 	rli.sefdisc as sefdisc,
	sum(rliq.sefdisc) as sefdisctaken,
 	rli.firecode as firecode,
	sum(rliq.firecode) as firecodepaid,
	rliq.revperiod
 from rptledgeritem rli
 	inner join rptledgeritem_qtrly rliq on rli.objid = rliq.parentid 
 where rli.rptledgerid = $P{rptledgerid}
and rliq.fullypaid = 1
and rli.qtrly = 0
 group by rli.objid, rliq.revperiod, 
 	rli.basic, rli.basicint, rli.basicdisc, 
 	rli.basicidle, rli.basicidleint, rli.basicidledisc, 
 	rli.sef, rli.sefint, rli.sefdisc, 
 	rli.firecode



[closeFullyPaidQtrlyItems]
update rptledgeritem_qtrly set 
	basicpaid = basic,
	basicintpaid = basicint,
	basicdisctaken = basicdisc,
	basicidlepaid = basicidle,
	basicidledisctaken = basicidledisc,
	basicidleintpaid = basicidleint,
	sefpaid = sef,
	sefintpaid = sefint,
	sefdisctaken = sefdisc,
	firecodepaid = firecode
where rptledgerid = $P{rptledgerid}	
and fullypaid = 1


[resetLastBilledInfo]
UPDATE rptledger SET 
	nextbilldate = null
WHERE objid = $P{rptledgerid}



[findLedgerbyTdNo]
SELECT objid 
FROM rptledger 
WHERE tdno = $P{tdno} 

[findLedgerbyPrevTdNo]
SELECT objid 
FROM rptledger 
WHERE tdno = $P{prevtdno} 

[findLedgerByFullPin]
SELECT objid 
FROM rptledger 
WHERE fullpin = $P{fullpin} 


[updateOnlineLedgerInfo]
UPDATE rptledger r, faas f, rpu rpu, realproperty rp, propertyclassification pc   SET
	r.tdno = f.tdno,
	r.fullpin = f.fullpin,
	r.rputype = rpu.rputype,
	r.cadastrallotno = rp.cadastrallotno,
	r.totalmv = rpu.totalmv, 
	r.totalav = rpu.totalav, 
	r.totalareaha = rpu.totalareaha,
	r.classcode = pc.code,
	r.taxpayer_objid = f.taxpayer_objid,
	r.txntype_objid = f.txntype_objid,
	r.taxable = rpu.taxable,
	r.owner_name = f.owner_name,
	r.prevtdno = f.prevtdno,
	r.titleno = f.titleno 
WHERE r.objid = $P{objid}
  AND r.faasid = f.objid 
  AND f.rpuid = rpu.objid 
  AND f.realpropertyid = rp.objid
  AND rpu.classification_objid = pc.objid;

[updateLedgerFaasId]  
UPDATE rptledger SET 
	faasid = $P{faasid}
WHERE objid = $P{ledgerid}	


[findSubLedgerById]
SELECT sl.*, p.fullpin AS parent_fullpin, p.tdno AS parent_tdno
FROM rptledger_subledger sl 
	INNER JOIN rptledger p ON sl.parent_objid = p.objid 
WHERE sl.objid = $P{objid}


[getPreviousFaas]
SELECT prevfaasid 
FROM previousfaas pf
WHERE faasid = $P{faasid}



[getCreditedLedgerItems]
select objid, year
from rptledgeritem 
where rptledgerid = $P{rptledgerid}
  and year <= $P{toyear}

[updateLedgerItemPaidInfo]  
update rptledgeritem set 
	fullypaid = $P{paid}
where objid = $P{objid}	
 and taxdifference = 0


[findLatestLedgerItem]
SELECT objid, year, paid
FROM rptledgeritem 
WHERE rptledgerid = $P{objid}
 AND taxdifference = 0 
 and fullypaid = 0
ORDER BY year desc 

[findLedgerItemByYears]
select *
from rptledgeritem 
where rptledgerid = $P{objid}
and year = $P{year}
and actualuse_objid = $P{actualuseid}
and taxdifference = 0


[getLedgerItemsWithTaxDifference]
select objid, rptledgerid, year, av, $P{currentav} - av AS avdifference
from rptledgeritem 
where rptledgerid = $P{rptledgerid} 
  and year >= $P{effectivityyear}
  and fullypaid = 1
  and av < $P{currentav}



[deleteUnpaidLedgerItemsAboveEffectivityYear]
delete from rptledgeritem 
where rptledgerid = $P{rptledgerid}	
  and year >= ${effectivityyear}
  and fullypaid = 0

[deleteBillLedgerItems]
delete from rptbill_ledger_item 
where rptledgeritemid in (
	select objid from rptledgeritem 
	where rptledgerid = $P{rptledgerid}	
		and year >= ${effectivityyear}
		and fullypaid = 0
)  


[getLedgerItems]
select x.*, x.total- x.amtpaid as amtdue
from (
	select rli.*,
		pc.code AS classification_code,
		au.code AS actualuse_code,
		(rli.basic + rli.basicint - rli.basicdisc + 
		rli.basicidle - rli.basicidledisc + rli.basicidleint + 
		rli.sef + rli.sefint - rli.sefdisc + rli.firecode 
		) as total,
		(rli.basicpaid + rli.basicintpaid - rli.basicdisctaken + 
		rli.basicidlepaid + rli.basicidleintpaid - rli.basicidledisctaken + 
		rli.sefpaid + rli.sefintpaid - rli.sefdisctaken + rli.firecodepaid) as amtpaid
	from rptledgeritem rli
		inner join propertyclassification pc on rli.classification_objid = pc.objid
		left join propertyclassification au on rli.actualuse_objid = au.objid 
	where rptledgerid = $P{rptledgerid}
) x
order by year desc 



[getAffectedLedgerItemsByLedgerFaas]
select *
from rptledgeritem 
where rptledgerid = $P{rptledgerid}
  and year >= $P{fromyear} 
  and year <= $P{toyear}
  and fullypaid = 0
 
[updateLedgerItemInfo] 
update rptledgeritem set 
	av = $P{av},
	basicav = $P{basicav},
	sefav = $P{sefav}
where objid = $P{objid}	


[fixLedgerItemUnpaidInfo]
UPDATE rptledgeritem SET
	fullypaid = 0,
WHERE rptledgerid = $P{rptledgerid}
 AND year > $P{lastyearpaid}




[getSubLedgers]
select 
	sl.*,
	rl.state,
	e.name AS taxpayer_name, 
	rl.totalareaha, 
	rl.totalareaha * 10000 as totalareasqm,
	rl.totalmv,
	rl.totalav, 
	rl.lastyearpaid, rl.lastqtrpaid 
from rptledger_subledger sl
	inner join rptledger rl on sl.objid= rl.objid 
	inner join entity e on rl.taxpayer_objid = e.objid 
where sl.parent_objid = $P{objid}
order by sl.subacctno 


[deleteSubledgerFaas]
DELETE FROM rptledgerfaas WHERE rptledgerid =$P{objid}


[deleteRptBillLedgerItems]
delete from rptbill_ledger_item 
where rptledgerid =$P{objid}


[deleteRptLedgerItems]
delete from rptledgeritem 
where rptledgerid = $P{objid}
  and fullypaid = 0
  and taxdifference = 0
  and not exists(
		select *
		from cashreceiptitem_rpt_online 
		where rptledgerid = $P{objid}
		and rptledgeritemid = rptledgeritem.objid 
	);

[deleteLedgerItemQtrly]  
delete from rptledgeritem_qtrly where parentid = $P{objid}



[updateLedgerInfoFromNewRevision]
update rptledger set 
	fullpin = $P{fullpin},
	tdno = $P{tdno},
	txntype_objid = $P{txntype_objid},
	classification_objid = $P{classification_objid},
	classcode = $P{classcode},
	totalav = $P{totalav},
	taxable = $P{taxable}
where objid = $P{objid}




[deleteRptBillLedger]
delete from rptbill_ledger where rptledgerid = $P{objid}

[deleteRptBillLedgerItem]
delete from rptbill_ledger_item where rptledgerid = $P{objid}

[deleteRptBillLedgerAccount]
delete from rptbill_ledger_account where rptledgerid = $P{objid}

[deleteRptLedgerItem]
delete from rptledgeritem where rptledgerid = $P{objid}

[deleteRptLedgerItemQtrly]
delete from rptledgeritem_qtrly where rptledgerid = $P{objid}

[deleteLedgerFaases]
DELETE FROM rptledgerfaas WHERE rptledgerid = $P{objid} 

[deleteLedgerCredits]
delete from rptledger_credit where rptledgerid = $P{objid} 
	
[deleteLedger]
DELETE FROM rptledger WHERE objid = $P{objid}


[cancelCurrentLedgerFaasReference]
update rptledgerfaas set 
	state = 'CANCELLD'
where rptledgerid = $P{objid} 
  and faasid = $P{faasid} 


[deleteLedgerItemByFaasReference]  
delete from rptledgeritem where rptledgerfaasid in (
	select objid from rptledgerfaas where faasid = $P{faasid}
)


[updateLedgerNextBillDate]
UPDATE rptledger SET
	nextbilldate = $P{nextbilldate}
WHERE objid = $P{rptledgerid}


[updateLedgerItemAvByLedgerFaas]
update rptledgeritem rli, rptledgerfaas rlf set 
	av = rlf.assessedvalue,
	basicav = rlf.assessedvalue,
	sefav = rlf.assessedvalue
where rli.rptledgerfaasid = rlf.objid 
  and rlf.objid = $P{objid}	
  and rli.fullypaid = 0

[getAffectedQtrlyItemsByLedgerFaas]
select rli.*, rli.objid as parentid, rliq.qtr, rliq.partialled  
from rptledgeritem rli
	inner join rptledgeritem_qtrly rliq on rli.objid = rliq.parentid 
where rli.rptledgerfaasid = $P{objid}
  and ( (rliq.year > $P{fromyear} or rliq.year < $P{toyear}) or 
  		(rliq.year = $P{fromyear} and rliq.qtr >= $P{toqtr}) or 
  		(rliq.year = $P{toyear} and rliq.qtr <= $P{toqtr}) 
  	)
  and rliq.fullypaid = 0


[findPartialPayment]
select 
	basicpaid, basicdisctaken, basicintpaid,
	sefpaid, sefdisctaken, sefintpaid
from rptledgeritem_qtrly
where rptledgerid = $P{objid}	
and partialled = 1


[findPartialledItem]
select parentid as rptledgeritemid 
from rptledgeritem_qtrly
where rptledgerid = $P{objid}	
and year = $P{partialledyear}
and qtr = $P{partialledqtr}
and fullypaid = 0

[updatePartialledQtrlyItem]
update rptledgeritem_qtrly set 
	basicpaid = $P{basicpaid},
	basicintpaid = $P{basicintpaid},
	basicdisctaken = $P{basicdisctaken},
	sefpaid = $P{sefpaid},
	sefintpaid = $P{sefintpaid},
	sefdisctaken = $P{sefdisctaken},
	partialled = 1
where rptledgerid = $P{objid}	
and year = $P{partialledyear}
and qtr = $P{partialledqtr}
and fullypaid = 0

[addPartialToLedgerItemPayment]
update rptledgeritem set 
	basicpaid = basicpaid + $P{basicpaid},
	basicintpaid = basicintpaid + $P{basicintpaid},
	basicdisctaken = basicdisctaken + $P{basicdisctaken},
	sefpaid = sefpaid + $P{sefpaid},
	sefintpaid = sefintpaid + $P{sefintpaid},
	sefdisctaken = sefdisctaken + $P{sefdisctaken}
where objid = $P{rptledgeritemid}


[setQtrlyItemFullyPaidFlagByYear]
update rptledgeritem_qtrly set 
	fullypaid = 1,
	partialled = 0 
where rptledgerid = $P{rptledgerid}
  and parentid = $P{rptledgeritemid}
  and  year = $P{lastyearpaid} and qtr <= $P{lastqtrpaid}


[resetQtrlyItemFullyPaidFlagByYear]
update rptledgeritem_qtrly set 
	fullypaid = 0,
	partialled = 0,
	basicpaid = 0.0,
	basicintpaid = 0.0,
	basicdisctaken = 0.0,
	basicidlepaid = 0.0,
	basicidledisctaken = 0.0,
	basicidleintpaid = 0.0,
	sefpaid = 0.0,
	sefintpaid = 0.0,
	sefdisctaken = 0.0,
	firecodepaid = 0.0
where rptledgerid = $P{rptledgerid}
  and year = $P{lastyearpaid}


[resetItemFullyPaidFlagByYear]
update rptledgeritem  set 
	fullypaid = 0,
	basicpaid = 0.0,
	basicintpaid = 0.0,
	basicdisctaken = 0.0,
	basicidlepaid = 0.0,
	basicidledisctaken = 0.0,
	basicidleintpaid = 0.0,
	sefpaid = 0.0,
	sefintpaid = 0.0,
	sefdisctaken = 0.0,
	firecodepaid = 0.0
where rptledgerid = $P{rptledgerid}
  and year = $P{lastyearpaid}  
  	

[findQtrlyItemCount]  	
select count(*) as count from rptledgeritem_qtrly where parentid = $P{objid}


[findFaasFromSubdividedLand]
select objid from subdividedland where newfaasid = $P{objid}



[fixLedgerDeleteBillLedgerItems]
delete from rptbill_ledger_item 
where rptledgerid = $P{rptledgerid}


[fixLedgerDeleteQtrlyItems]  
delete from rptledgeritem_qtrly 
where rptledgerid = $P{rptledgerid}
and  year > $P{lastyearpaid}
and exists(select * from rptledgeritem where objid = rptledgeritem_qtrly.parentid and taxdifference = 0)


[fixLedgerDeleteLedgerItems]
delete from rptledgeritem  
where rptledgerid = $P{rptledgerid}
and year > $P{lastyearpaid} 
and taxdifference = 0 


[fixLedgerSetQtrlyItemFullyPaid]
update rptledgeritem_qtrly set 
	fullypaid = 1,
	basicpaid = basic,
	basicintpaid = basicint,
	basicdisctaken = basicdisc,
	basicidlepaid = basicidle,
	basicidledisctaken = basicidledisc,
	basicidleintpaid = basicidleint,
	sefpaid = sef,
	sefintpaid = sefint,
	sefdisctaken = sefdisc,
	firecodepaid = firecode,
	partialled = 0 
where rptledgerid = $P{rptledgerid}
  and ( year < $P{lastyearpaid} or ( year = $P{lastyearpaid} and qtr <= $P{lastqtrpaid}))


[fixLedgerSetItemFullyPaid]
update rptledgeritem rli, 
	( 
		select 
			parentid, 
			sum(basicpaid) as basicpaid, 
			sum(basicintpaid) as basicintpaid, 
			sum(basicdisctaken) as basicdisctaken, 
			sum(basicidlepaid) as basicidlepaid, 
			sum(basicidledisctaken) as basicidledisctaken, 
			sum(basicidleintpaid) as basicidleintpaid, 
			sum(sefpaid) as sefpaid, 
			sum(sefintpaid) as sefintpaid, 
			sum(sefdisctaken) as sefdisctaken, 
			sum(firecodepaid) as firecodepaid,
			max(qtr) as paidqtr 
		from rptledgeritem_qtrly 
		where rptledgerid = $P{rptledgerid}
  		and year <= $P{lastyearpaid} 
  		and fullypaid = 1 
  		group by parentid 
	) rliq 
set 
	rli.fullypaid = case when rliq.paidqtr = 4 then 1 else 0 end,
	rli.basicpaid = rliq.basicpaid,
	rli.basicintpaid = rliq.basicintpaid,
	rli.basicdisctaken = rliq.basicdisctaken,
	rli.basicidlepaid = rliq.basicidlepaid,
	rli.basicidledisctaken = rliq.basicidledisctaken,
	rli.basicidleintpaid = rliq.basicidleintpaid,
	rli.sefpaid = rliq.sefpaid,
	rli.sefintpaid = rliq.sefintpaid,
	rli.sefdisctaken = rliq.sefdisctaken,
	rli.firecodepaid = rliq.firecodepaid
where rli.objid = rliq.parentid 


[updateLedgerItemAvByQtrly]
update rptledgeritem rli, 
	( 
		select 
			parentid, 
			sum(av) as av, 
			sum(basicav) as basicav, 
			sum(sefav) as sefav 
		from rptledgeritem_qtrly 
		where parentid = $P{objid}
  		group by parentid 
	) rliq 
set 
	rli.av = rliq.av,
	rli.basicav = rliq.basicav,
	rli.sefav = rliq.sefav
where rli.objid = $P{objid}
  and rli.objid = rliq.parentid 



[updateLedgerItemFromQtrlyAggregates]
update rptledgeritem rli, 
	(	select 
			parentid as rptledgeritemid, 
			sum(basic) as basic,
			sum(basicint) as basicint,
			sum(basicdisc) as basicdisc,
			sum(basicidle) as basicidle,
			sum(basicidledisc) as basicidledisc,
			sum(basicidleint) as basicidleint,
			sum(sef) as sef,
			sum(sefint) as sefint,
			sum(sefdisc) as sefdisc,
			sum(firecode) as firecode
		from rptledgeritem_qtrly
		where rptledgerid = $P{objid}
		and year = $P{lastyearpaid}
		group by parentid 
	)x 
set
	rli.basic = x.basic,
	rli.basicint = x.basicint,
	rli.basicdisc = x.basicdisc,
	rli.basicidle = x.basicidle,
	rli.basicidledisc = x.basicidledisc,
	rli.basicidleint = x.basicidleint,
	rli.sef = x.sef,
	rli.sefint = x.sefint,
	rli.sefdisc = x.sefdisc,
	rli.firecode = x.firecode
where rli.rptledgerid = $P{rptledgerid}
  and rli.objid = x.rptledgeritemid 
