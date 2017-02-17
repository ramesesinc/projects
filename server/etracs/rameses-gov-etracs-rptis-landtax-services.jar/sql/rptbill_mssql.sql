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
		WHEN rl.nextbilldate IS NULL OR rl.nextbilldate <= GETDATE() THEN 1 
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
 AND ( rl.lastyearpaid < $P{billtoyear} OR (rl.lastyearpaid = $P{billtoyear} AND rl.lastqtrpaid < $P{billtoqtr}))
 and not exists(select * from rptledger_restriction where parentid = rl.objid )


[getIncentivesByLedgerId]
SELECT *
FROM rpttaxincentive_item
WHERE rptledgerid = $P{rptledgerid}


[getItemsForTaxComputation]
select 
	rli.objid, 
	rli.rptledgerid, 
	rli.rptledgerfaasid, 
	rli.objid AS rptledgeritemid, 
	null as rptledgeritemqtrlyid,
	rli.year, 
	1 as qtr, 
	rli.av,
	rli.av AS originalav, 
	rli.basicav, 
	rli.sefav, 
	rl.txntype_objid,
	rli.classification_objid,
	rli.actualuse_objid,
	lf.backtax,
	lf.reclassed,
	lf.idleland,
	0.0 as basic,
	0.0 as basicint,
	0.0 as basicdisc,
	0.0 as basicidle,
	0.0 as basicidleint,
	0.0 as basicidledisc,
	0.0 as sef,
	0.0 as sefint,
	0.0 as sefdisc,
	0.0 as firecode,
	0 as partialled,
	rli.year AS effectiveyear,
	rli.taxdifference ,
	(select effectivityyear from faas where objid = rl.faasid)  as effectivityyear,
	0 as qtrly,
	0 as qtrlycomputed
from rptledger rl 
	inner join rptledgeritem rli on rl.objid = rli.rptledgerid
	inner join rptledgerfaas lf on rli.rptledgerfaasid = lf.objid 
where rl.objid = $P{rptledgerid}
and rli.fullypaid = 0


[getItemsForPenaltyDiscountComputation]
select x.* 
from (
	select 
		rli.objid, 
		rli.rptledgerid, 
		rli.rptledgerfaasid, 
		rli.objid AS rptledgeritemid, 
		null as rptledgeritemqtrlyid,
		rli.year, 
		1 as qtr, 
		rli.av,
		rli.av AS originalav, 
		rli.basicav, 
		rli.sefav, 
		rl.txntype_objid,
		rli.classification_objid,
		rli.actualuse_objid,
		lf.backtax,
		lf.reclassed,
		lf.idleland,
		rli.basic,
		0.0 as basicint,
		0.0 as basicdisc,
		0.0 as basicidle,
		0.0 as basicidleint,
		0.0 as basicidledisc,
		rli.sef, 
		0.0 as sefint,
		0.0 as sefdisc,
		0.0 as firecode,
		0 as partialled,
		rli.year AS effectiveyear,
		rli.taxdifference ,
		(select effectivityyear from faas where objid = rl.faasid)  as effectivityyear,
		rli.qtrly,
		rli.qtrly as qtrlycomputed
	from rptledger rl 
		inner join rptledgeritem rli on rl.objid = rli.rptledgerid
		inner join rptledgerfaas lf on rli.rptledgerfaasid = lf.objid 
	where rl.objid = $P{rptledgerid}
		and rli.qtrly = 0 
		and rli.fullypaid = 0 

	UNION all 

	select 
		rliq.objid, 
		rli.rptledgerid, 
		rli.rptledgerfaasid, 
		rli.objid AS rptledgeritemid, 
		rliq.objid as rptledgeritemqtrlyid,
		rli.year, 
		rliq.qtr, 
		rliq.av,
		rliq.av AS originalav, 
		rliq.basicav, 
		rliq.sefav, 
		rl.txntype_objid,
		rli.classification_objid,
		rli.actualuse_objid,
		lf.backtax,
		lf.reclassed,
		lf.idleland,
		rliq.basic,
		0.0 as basicint,
		0.0 as basicdisc,
		0.0 as basicidle,
		0.0 as basicidleint,
		0.0 as basicidledisc,
		rliq.sef,
		0.0 as sefint,
		0.0 as sefdisc,
		0.0 as firecode,
		0 as partialled,
		rli.year AS effectiveyear,
		rli.taxdifference ,
		(select effectivityyear from faas where objid = rl.faasid)  as effectivityyear,
		rli.qtrly,
		rli.qtrly as qtrlycomputed
	from rptledger rl 
		inner join rptledgeritem rli on rl.objid = rli.rptledgerid
		inner join rptledgeritem_qtrly rliq on rli.objid = rliq.parentid 
		inner join rptledgerfaas lf on rli.rptledgerfaasid = lf.objid 
	where rl.objid = $P{rptledgerid}
		and rli.qtrly = 1
		and rliq.fullypaid = 0 
) x
order by x.year, x.qtr 


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


[updateLedgerItemQtrlyData]
update rptledgeritem_qtrly set 
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
where rli.rptledgerid = $P{rptledgerid}
 and rli.qtrly = 1 
group by rliq.parentid, rliq.revperiod 


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
	rliq.basicint - rliq.basicintpaid as basicint,
	rliq.basicdisc - rliq.basicdisctaken as basicdisc,
	rliq.basicidle - rliq.basicidlepaid as basicidle,
	rliq.basicidledisc - rliq.basicidledisctaken as basicidledisc,
	rliq.basicidleint - rliq.basicidleintpaid as basicidleint,
	rliq.sef - rliq.sefpaid as sef,
	rliq.sefint - rliq.sefintpaid as sefint,
	rliq.sefdisc - rliq.sefdisctaken as sefdisc,
	rliq.firecode - rliq.firecodepaid as firecode,
	
	(	rliq.basic - rliq.basicpaid + rliq.basicint - rliq.basicintpaid - (rliq.basicdisc - rliq.basicdisctaken) +
		rliq.basicidle - rliq.basicidlepaid + rliq.basicidleint - rliq.basicidleintpaid - (rliq.basicidledisc - rliq.basicidledisctaken)
	) as basicnet,
	( rliq.sef - rliq.sefpaid + rliq.sefint - rliq.sefintpaid - (rliq.sefdisc - rliq.sefdisctaken)) as sefnet,
	
	(	rliq.basic - rliq.basicpaid + rliq.basicint - rliq.basicintpaid - (rliq.basicdisc - rliq.basicdisctaken) +
		rliq.basicidle - rliq.basicidlepaid + rliq.basicidleint - rliq.basicidleintpaid - (rliq.basicidledisc - rliq.basicidledisctaken) +
		rliq.sef - rliq.sefpaid + rliq.sefint - rliq.sefintpaid - (rliq.sefdisc - rliq.sefdisctaken) +
		rliq.firecode - rliq.firecodepaid
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


[findBrgyTaxAccountMapping]
SELECT 
	bam.*, 
	prior.title AS basicprioracct_title, 
	priorint.title AS basicpriorintacct_title,
	prev.title AS basicprevacct_title, 
	prevint.title AS basicprevintacct_title,
	curr.title AS basiccurracct_title, 
	currint.title AS basiccurrintacct_title,
	adv.title AS basicadvacct_title
FROM brgy_taxaccount_mapping bam 
	INNER JOIN itemaccount prior ON bam.basicprioracct_objid = prior.objid 
	INNER JOIN itemaccount priorint ON bam.basicpriorintacct_objid = priorint.objid 
	INNER JOIN itemaccount prev ON bam.basicprevacct_objid = prev.objid 
	INNER JOIN itemaccount prevint ON bam.basicprevintacct_objid = prevint.objid 
	INNER JOIN itemaccount curr ON bam.basiccurracct_objid = curr.objid 
	INNER JOIN itemaccount currint ON bam.basiccurrintacct_objid = currint.objid 
	INNER JOIN itemaccount adv ON bam.basicadvacct_objid = adv.objid 
WHERE bam.barangayid = $P{barangayid}


[findMunicipalityTaxAccountMapping]
SELECT 
	mm.*,
	m.name,
	m.indexno,
	basicprior.title AS basicprioracct_title, 
	basicpriorint.title AS basicpriorintacct_title,
	basicprev.title AS basicprevacct_title, 
	basicprevint.title AS basicprevintacct_title,
	basiccurr.title AS basiccurracct_title, 
	basiccurrint.title AS basiccurrintacct_title,
	basicadv.title AS basicadvacct_title,
	basicidlecurr.title AS basicidlecurracct_title, 
	basicidlecurrint.title AS basicidlecurrintacct_title,
	basicidleprev.title AS basicidleprevacct_title, 
	basicidleprevint.title AS basicidleprevintacct_title,
	basicidleadv.title AS basicidleadvacct_title,

	sefprior.title AS sefprioracct_title, 
	sefpriorint.title AS sefpriorintacct_title,
	sefprev.title AS sefprevacct_title, 
	sefprevint.title AS sefprevintacct_title,
	sefcurr.title AS sefcurracct_title, 
	sefcurrint.title AS sefcurrintacct_title,
	sefadv.title AS sefadvacct_title
FROM municipality_taxaccount_mapping mm
	LEFT JOIN municipality m ON mm.lguid = m.objid 
	LEFT JOIN itemaccount basicprior ON mm.basicprioracct_objid = basicprior.objid 
	LEFT JOIN itemaccount basicpriorint ON mm.basicpriorintacct_objid = basicpriorint.objid 
	LEFT JOIN itemaccount basicprev ON mm.basicprevacct_objid = basicprev.objid 
	LEFT JOIN itemaccount basicprevint ON mm.basicprevintacct_objid = basicprevint.objid 
	LEFT JOIN itemaccount basiccurr ON mm.basiccurracct_objid = basiccurr.objid 
	LEFT JOIN itemaccount basiccurrint ON mm.basiccurrintacct_objid = basiccurrint.objid 
	LEFT JOIN itemaccount basicadv ON mm.basicadvacct_objid = basicadv.objid 
	LEFT JOIN itemaccount basicidlecurr ON mm.basicidlecurracct_objid = basicidlecurr.objid 
	LEFT JOIN itemaccount basicidlecurrint ON mm.basicidlecurrintacct_objid = basicidlecurrint.objid 
	LEFT JOIN itemaccount basicidleprev ON mm.basicidleprevacct_objid = basicidleprev.objid 
	LEFT JOIN itemaccount basicidleprevint ON mm.basicidleprevintacct_objid = basicidleprevint.objid 
	LEFT JOIN itemaccount basicidleadv ON mm.basicidleadvacct_objid = basicidleadv.objid 
	
	LEFT JOIN itemaccount sefprior ON mm.sefprioracct_objid = sefprior.objid 
	LEFT JOIN itemaccount sefpriorint ON mm.sefpriorintacct_objid = sefpriorint.objid 
	LEFT JOIN itemaccount sefprev ON mm.sefprevacct_objid = sefprev.objid 
	LEFT JOIN itemaccount sefprevint ON mm.sefprevintacct_objid = sefprevint.objid 
	LEFT JOIN itemaccount sefcurr ON mm.sefcurracct_objid = sefcurr.objid 
	LEFT JOIN itemaccount sefcurrint ON mm.sefcurrintacct_objid = sefcurrint.objid 
	LEFT JOIN itemaccount sefadv ON mm.sefadvacct_objid = sefadv.objid 
WHERE mm.lguid = $P{lguid}


[findProvinceTaxAccountMapping]
SELECT 
	mm.*,
	basicprior.title AS basicprioracct_title, 
	basicpriorint.title AS basicpriorintacct_title,
	basicprev.title AS basicprevacct_title, 
	basicprevint.title AS basicprevintacct_title,
	basiccurr.title AS basiccurracct_title, 
	basiccurrint.title AS basiccurrintacct_title,
	basicadv.title AS basicadvacct_title,
	basicidlecurr.title AS basicidlecurracct_title, 
	basicidlecurrint.title AS basicidlecurrintacct_title,
	basicidleprev.title AS basicidleprevacct_title, 
	basicidleprevint.title AS basicidleprevintacct_title,
	basicidleadv.title AS basicidleadvacct_title,

	sefprior.title AS sefprioracct_title, 
	sefpriorint.title AS sefpriorintacct_title,
	sefprev.title AS sefprevacct_title, 
	sefprevint.title AS sefprevintacct_title,
	sefcurr.title AS sefcurracct_title, 
	sefcurrint.title AS sefcurrintacct_title,
	sefadv.title AS sefadvacct_title
FROM province_taxaccount_mapping mm
	LEFT JOIN itemaccount basicprior ON mm.basicprioracct_objid = basicprior.objid 
	LEFT JOIN itemaccount basicpriorint ON mm.basicpriorintacct_objid = basicpriorint.objid 
	LEFT JOIN itemaccount basicprev ON mm.basicprevacct_objid = basicprev.objid 
	LEFT JOIN itemaccount basicprevint ON mm.basicprevintacct_objid = basicprevint.objid 
	LEFT JOIN itemaccount basiccurr ON mm.basiccurracct_objid = basiccurr.objid 
	LEFT JOIN itemaccount basiccurrint ON mm.basiccurrintacct_objid = basiccurrint.objid 
	LEFT JOIN itemaccount basicadv ON mm.basicadvacct_objid = basicadv.objid 
	LEFT JOIN itemaccount basicidlecurr ON mm.basicidlecurracct_objid = basicidlecurr.objid 
	LEFT JOIN itemaccount basicidlecurrint ON mm.basicidlecurrintacct_objid = basicidlecurrint.objid 
	LEFT JOIN itemaccount basicidleprev ON mm.basicidleprevacct_objid = basicidleprev.objid 
	LEFT JOIN itemaccount basicidleprevint ON mm.basicidleprevintacct_objid = basicidleprevint.objid 
	LEFT JOIN itemaccount basicidleadv ON mm.basicidleadvacct_objid = basicidleadv.objid 
	
	LEFT JOIN itemaccount sefprior ON mm.sefprioracct_objid = sefprior.objid 
	LEFT JOIN itemaccount sefpriorint ON mm.sefpriorintacct_objid = sefpriorint.objid 
	LEFT JOIN itemaccount sefprev ON mm.sefprevacct_objid = sefprev.objid 
	LEFT JOIN itemaccount sefprevint ON mm.sefprevintacct_objid = sefprevint.objid 
	LEFT JOIN itemaccount sefcurr ON mm.sefcurracct_objid = sefcurr.objid 
	LEFT JOIN itemaccount sefcurrint ON mm.sefcurrintacct_objid = sefcurrint.objid 
	LEFT JOIN itemaccount sefadv ON mm.sefadvacct_objid = sefadv.objid 


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
and not exists(select * from rptledger_restriction where parentid = rl.objid )
ORDER BY rl.tdno  


[deleteRptBillLedgerItem]
DELETE FROM rptbill_ledger_item 
WHERE rptledgerid = $P{rptledgerid}
 AND billid = $P{objid}


[deleteRptBillLedgerAccount]
DELETE FROM rptbill_ledger_account 
WHERE rptledgerid = $P{rptledgerid}
AND billid = $P{objid}



[findBillByBarcode]
SELECT * FROM rptbill  WHERE barcode = $P{barcodeid}

[getBillLedgers]  
SELECT * FROM rptbill_ledger rbl 
WHERE billid = $P{objid}
and not exists(select * from rptledger_restriction where parentid = rbl.rptledgerid)


[getBillLedgerAccounts]
SELECT * FROM rptbill_ledger_account 
WHERE rptledgerid = $P{rptledgerid}
AND billid = $P{objid}






















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





[getLandDetails]
select pc.objid as classification_objid, ld.area
from faas f 
	inner join landdetail ld on f.rpuid = ld.landrpuid 
	inner join landassesslevel lal on ld.actualuse_objid = lal.objid 
	inner join propertyclassification pc on lal.classification_objid = pc.objid 
where f.objid = $P{faasid}



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
	select max(receiptdate) as receiptdate 
	from cashreceipt c
		inner join cashreceipt_rpt cr on c.objid = cr.objid 
		inner join cashreceiptitem_rpt_online cro on c.objid = cro.rptreceiptid 
	where cro.rptledgerid = $P{objid}
    and (cro.year = $P{cy} and cro.fromqtr = 1)

	union 

	select max(refdate) as receiptdate 
	from rptledger_credit cr 
	where cr.rptledgerid = $P{objid}
	 and ((cr.fromyear = $P{cy} and cr.fromqtr = 1) 
				or (cr.toyear = $P{cy} and cr.toqtr >= 1)
				or ($P{cy} > cr.fromyear and $P{cy} < cr.toyear)
		)
)x





[deleteUnpartialledItem]  
delete from rptbill_ledger_item
where objid = $P{objid}
	


[getLedgerBillItemsForPartial]
select * from rptbill_ledger_item
where billid = $P{objid}
and rptledgerid = $P{rptledgerid}
order by year, rptledgeritemid, qtr

	
[updateLedgerBillPartialledItemData]
update rptbill_ledger_item set 
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
        basicnet = $P{basicnet},
        sefnet = $P{sefnet},
        total = $P{total},
        revperiod = $P{revperiod},
        partialled = 1
where objid = $P{objid}      


[updateLedgerItemQtrlyFlag]
update rptledgeritem set qtrly = $P{qtrly} where objid = $P{objid}

[resetLedgerItemQtrlyFlagByLedger]	
update rptledgeritem set qtrly = 0 where rptledgerid = $P{rptledgerid} and fullypaid = 0 


[getBillItems]
select * 
from rptbill_ledger_item 
where billid = $P{objid}
 and rptledgerid = $P{rptledgerid}
order by year, qtr  




[updateLedgerItemFromQtrlyAggregates]
update rli set
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
from rptledgeritem rli, 
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
		where rptledgerid = $P{rptledgerid}
		and parentid = $P{rptledgeritemid}
		group by parentid 
	)x 
where rli.rptledgerid = $P{rptledgerid}
  and rli.objid = x.rptledgeritemid 



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


[getLedgerQtrlyItems]
select year, qtr, av, basicav, sefav from rptledgeritem_qtrly where parentid = $P{parentid} 
