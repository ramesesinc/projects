[getList]
SELECT 
    ${columns}
FROM rptledger rl 
    INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
    INNER JOIN barangay b ON rl.barangayid = b.objid 
WHERE 1=1
${fixfilters}
${filters}
${orderby}

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
        collector as collector_name, 
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
    and rc.type <> 'COMPROMISE'

    UNION ALL
    
    SELECT 
        cr.objid AS rptreceiptid, 
        cr.receiptno AS refno,
        cr.receiptdate AS refdate,
        cr.collector_name,
        cr.paidby AS paidby_name,
        cri.year AS fromyear,
        rp.fromqtr,
        cri.year AS toyear,
        rp.toqtr,
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
        inner join rptledger_payment rp on cr.objid = rp.receiptid 
        inner join rptledger_payment_item cri on rp.objid = cri.parentid
        LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
    WHERE rp.rptledgerid = $P{rptledgerid}
        AND cv.objid IS NULL    
    AND cri.partialled = 1 

    UNION ALL 

    SELECT 
        cr.objid AS rptreceiptid, 
        cr.receiptno AS refno,
        cr.receiptdate AS refdate,
        cr.collector_name,
        cr.paidby AS paidby_name,
        min(cri.year) AS fromyear,
        min(rp.fromqtr) AS fromqtr,
        max(cri.year) AS toyear,
        min(rp.toqtr) as toqtr,
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
        inner join rptledger_payment rp on cr.objid = rp.receiptid 
        inner join rptledger_payment_item cri on rp.objid = cri.parentid
        LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
    WHERE rp.rptledgerid = $P{rptledgerid}
        AND cv.objid IS NULL
        AND cri.partialled = 0  
    group by 
        cr.objid,
        cr.receiptno,
        cr.receiptdate,
        cr.collector_name,
        cr.paidby,
        crr.txntype,
        cri.year, 
        cri.partialled      
) t
ORDER BY t.fromyear desc, t.fromqtr desc 



[approveLedgerFaas]
UPDATE rptledgerfaas SET state = 'APPROVED' WHERE objid = $P{objid}


[updateLastYearQtrPaid]
UPDATE rptledger SET 
	lastyearpaid = $P{toyear}, lastqtrpaid = $P{toqtr}, nextbilldate = null 
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



[resetLastBilledInfo]
UPDATE rptledger SET 
	nextbilldate = null
WHERE objid = $P{rptledgerid}


[findLedgerByFullPin]
SELECT objid 
FROM rptledger 
WHERE fullpin = $P{fullpin} 


[updateOnlineLedgerInfo]
UPDATE r SET
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
from rptledger r, faas f, rpu rpu, realproperty rp, propertyclassification pc   
WHERE r.objid = $P{objid}
  AND r.faasid = f.objid 
  AND f.rpuid = rpu.objid 
  AND f.realpropertyid = rp.objid
  AND rpu.classification_objid = pc.objid


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
FROM faas_previous pf
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


[getLedgerItems]
select rli.*,
	pc.code AS classification_code,
	au.code AS actualuse_code,
	(rli.basic + rli.basicint - rli.basicdisc + 
	rli.basicidle - rli.basicidledisc + rli.basicidleint + 
	rli.sef + rli.sefint - rli.sefdisc + rli.firecode 
	) as total,
	(rli.basicpaid + rli.basicidlepaid + rli.sefpaid + rli.firecodepaid) as amtpaid
from rptledgeritem rli
	inner join propertyclassification pc on rli.classification_objid = pc.objid
	left join propertyclassification au on rli.actualuse_objid = au.objid 
where rptledgerid = $P{rptledgerid}
and rli.fullypaid = 0 
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


[deleteRptLedgerItems]
delete from rptledgeritem 
where rptledgerid = $P{objid}
  and fullypaid = 0
  and taxdifference = 0
  and not exists(
		select *
    from  rptledger_payment rp 
    inner join rptledger_payment_item rpi on rp.objid = rpi.parentid
    where rp.rptledgerid = $P{objid}
    and rpi.rptledgeritemid = rptledgeritem.objid 
	)


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


[findPartialledItem]
select parentid as rptledgeritemid 
from rptledgeritem_qtrly
where rptledgerid = $P{objid}	
and year = $P{partialledyear}
and qtr = $P{partialledqtr}
and fullypaid = 0


[setQtrlyItemFullyPaidFlagByYear]
update rptledgeritem_qtrly set 
	fullypaid = 1,
	partialled = 0 
where rptledgerid = $P{rptledgerid}
  and parentid = $P{rptledgeritemid}
  and  year = $P{lastyearpaid} and qtr <= $P{lastqtrpaid}


[resetQtrlyItemFullyPaidFlagByYear]
update rptledgeritem_qtrly rliq, rptledgeritem rli set 
	rliq.fullypaid = 0,
	rliq.partialled = 0,
	rliq.basicpaid = 0.0,
	rliq.basicidlepaid = 0.0,
	rliq.sefpaid = 0.0,
	rliq.firecodepaid = 0.0
where rliq.parentid = rli.objid 
  and rliq.rptledgerid = $P{rptledgerid}
  and rliq.year > $P{lastyearpaid}
  and rli.taxdifference  like $P{taxdifference}
  	

[resetItemFullyPaidFlagByYear]
update rptledgeritem  set 
	fullypaid = 0,
	basicpaid = 0.0,
	basicidlepaid = 0.0,
	sefpaid = 0.0,
	firecodepaid = 0.0
where rptledgerid = $P{rptledgerid}
  and year > $P{lastyearpaid}  
  and taxdifference like $P{taxdifference}


[findQtrlyItemCount]  	
select count(*) as count from rptledgeritem_qtrly where parentid = $P{objid}


[findLedgerFaasById]
select objid from rptledgerfaas where objid = $P{objid}


[findLedgerFaasByTdno]
select objid from rptledgerfaas where tdno = $P{tdno}

[getRestrictions]
select * from faas_restriction_type order by idx



[findLedgerItemQtrlyByYearQtr]
select * from rptledgeritem_qtrly 
where rptledgerid = $P{rptledgerid}
and year = $P{year}
and qtr = $P{qtr}
and fullypaid = 0 



[getLedgerItemQtrlyAggregates]
select 
	rliq.parentid as objid, 
	sum(rliq.basic) as basic,
	sum(rliq.basicint) as basicint,
	sum(rliq.basicdisc) as basicdisc,
	sum(rliq.basicidle) as basicidle,
	sum(rliq.basicidledisc) as basicidledisc,
	sum(rliq.basicidleint) as basicidleint,
	sum(rliq.sef) as sef,
	sum(rliq.sefint) as sefint,
	sum(rliq.sefdisc) as sefdisc,
	sum(rliq.firecode) as firecode,
	rliq.revperiod
from rptledgeritem rli
	inner join rptledgeritem_qtrly rliq on rli.objid = rliq.parentid 
where rli.rptledgerid = $P{objid}
 and rli.fullypaid = 0 
group by rliq.parentid, rliq.revperiod 


[updateLedgerItemData]
update rptledgeritem set 
		basic = $P{basic}, 
        basicint = $P{basicint}, 
        basicdisc = $P{basicdisc}, 
        basicidle = $P{basicidle}, 
        basicidledisc = $P{basicidledisc}, 
        basicidleint = $P{basicidleint}, 
        sef = $P{sef}, 
        sefint = $P{sefint}, 
        sefdisc = $P{sefdisc}, 
        firecode = $P{firecode}, 
        revperiod = $P{revperiod}
where objid = $P{objid}    

[getCreditsByReceipt]
SELECT t.*
FROM (
	SELECT 
		cr.objid,
		cr.objid AS rptreceiptid, 
		cri.rptledgerid, 
		o.parent_objid as lguid, 
		rl.barangayid,
		rl.lastyearpaid,
		rl.lastqtrpaid,
		cr.receiptno AS refno,
		cr.receiptdate AS refdate,
		cr.collector_name,
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
		cri.partialled,
		'online' as type, 
		cr.paidbyaddress as paidby_address, 
		'sytem' as postedby,
		'system' as postedbytitle,
		cr.receiptdate as dtposted
	FROM cashreceipt_rpt crr
		INNER JOIN cashreceipt cr ON crr.objid = cr.objid 
		INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid 	
		INNER JOIN rptledger rl on cri.rptledgerid = rl.objid 
		inner join rptledger_remote rm on rl.objid = rm.objid 
		inner join sys_org o on rl.barangayid = o.objid 
		LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
	WHERE crr.objid = $P{objid}
		AND cv.objid IS NULL 	
	  AND cri.partialled = 1 

	UNION ALL 

	select 				
				x.objid, 
				x.rptreceiptid, 
				x.rptledgerid,
				x.lguid, 
				x.barangayid,
				x.lastyearpaid,
				x.lastqtrpaid,
				x.refno,
				x.refdate,
				x.collector_name,
				x.paidby_name,
				x.fromyear,
				(select min(fromqtr) from cashreceiptitem_rpt_online where rptledgerid =  x.rptledgerid and rptreceiptid = x.rptreceiptid and year = x.fromyear and partialled = 0) as fromqtr,
				x.toyear,
				(select max(toqtr) from cashreceiptitem_rpt_online where rptledgerid =  x.rptledgerid and  rptreceiptid = x.rptreceiptid and year = x.toyear and partialled = 0) as toqtr, 
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
				x.partialled,
				x.type,
				x.paidby_address,
				x.postedby,
				x.postedbytitle,
				x.dtposted
	from (
		SELECT 
			cr.objid, 
				cr.objid AS rptreceiptid, 
				cr.receiptno AS refno,
				cr.receiptdate AS refdate,
				cr.collector_name,
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
			  cri.rptledgerid,
			  	o.parent_objid as lguid,  
			  	rl.barangayid,
			  	rl.lastyearpaid, 
			  	rl.lastqtrpaid,
				cri.partialled,
				'online' as type, 
				cr.paidbyaddress as paidby_address, 
				'sytem' as postedby,
				'system' as postedbytitle,
				cr.receiptdate as dtposted
			FROM cashreceipt_rpt crr
				INNER JOIN cashreceipt cr ON crr.objid = cr.objid 
				INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid 	
				INNER JOIN rptledger rl on cri.rptledgerid = rl.objid 
				inner join rptledger_remote rm on rl.objid = rm.objid 
				inner join sys_org o on rl.barangayid = o.objid 
				LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
			WHERE crr.objid = $P{objid} 
				AND cv.objid IS NULL
				AND cri.partialled = 0 	
		group by 
			cr.objid,	
			cr.receiptno,
			cr.receiptdate,
			cr.collector_name,
			cr.paidby,
			crr.txntype,
			cri.rptledgerid, 
			o.parent_objid,
			rl.barangayid,
			rl.lastyearpaid,
			rl.lastqtrpaid,
			cri.year, 
			cri.partialled,
			cr.paidbyaddress,
			cr.receiptdate
	) x 
) t
