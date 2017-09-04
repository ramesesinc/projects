[findLedgerInfo]
select
	rl.objid,
	rl.objid as rptledgerid, 
	rl.faasid, 
	rl.tdno,
	rl.owner_name,
	rl.lastyearpaid,
	rl.lastqtrpaid,
	rl.taxpayer_objid,
	rl.barangayid,
	rl.rputype,
	case when m.objid is not null then m.objid else d.parentid end as lguid,
	brgy.name as barangay
from rptledger rl 
	inner join barangay brgy on rl.barangayid = brgy.objid 
	left join municipality m on brgy.parentid = m.objid 
	left join district d  on brgy.parentid = d.objid 
where rl.objid = $P{objid}


[getItemsForPaymentByBill]
select
	rl.objid,
	rl.objid as rptledgerid, 
	rl.faasid, 
	rl.tdno,
	rl.owner_name,
	rl.lastyearpaid,
	rl.lastqtrpaid,
	rl.taxpayer_objid,
	rl.barangayid,
	rl.rputype,
	case when m.objid is not null then m.objid else d.parentid end as lguid,
	brgy.name as barangay
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
 		(exists(select * from rptledgeritem where rptledgerid = rl.objid and taxdifference=1 and fullypaid=0))

 	)

order by rl.tdno 


[getLedgerForPaymentDetail]
SELECT
	rliq.objid, 
    rl.objid as rptledgerid,
    rli.rptledgerfaasid,
	rliq.parentid as rptledgeritemid, 
	rliq.objid as rptledgeritemqtrlyid, 
    rliq.year,
    rliq.qtr,
    rliq.qtr as fromqtr,
    rliq.qtr as toqtr,
    rliq.basic - rliq.basicpaid as basic,
    rliq.basicint,
    rliq.basicdisc,
    rliq.sef - rliq.sefpaid as sef,
    rliq.sefint,
    rliq.sefdisc,
    rliq.firecode - rliq.firecodepaid as firecode,
    rliq.revperiod,
    rliq.basic - rliq.basicpaid - rliq.basicdisc + rliq.basicint as basicnet,
    rliq.sef - rliq.sefpaid - rliq.sefdisc + rliq.sefint as sefnet,
    ( rliq.basic - rliq.basicpaid - rliq.basicdisc + rliq.basicint + 
      rliq.basicidle - rliq.basicidlepaid - rliq.basicidledisc + rliq.basicidleint +
      rliq.sef - rliq.sefpaid - rliq.sefdisc + rliq.sefint +
      rliq.firecode - rliq.firecodepaid
     ) as total,
    0 as partialled,
    rliq.basicidle - rliq.basicidlepaid as basicidle,
    rliq.basicidledisc,
    rliq.basicidleint
FROM rptbill b 
    inner join rptbill_ledger bl on b.objid = bl.billid  
    inner join rptledger rl on bl.rptledgerid = rl.objid 
    INNER JOIN rptledgeritem rli ON rl.objid = rli.rptledgerid
    INNER JOIN rptledgeritem_qtrly rliq ON rli.objid = rliq.parentid 
WHERE b.objid = $P{billid}
    and rl.objid = $P{rptledgerid}
  and rl.state = 'APPROVED'
  and rliq.fullypaid = 0 
  and (rliq.year < $P{billtoyear}  or (rliq.year = $P{billtoyear} and rliq.qtr <= $P{billtoqtr}))
order by rliq.year, rliq.qtr   


[updateLedgerItemQrtrlyPayment]
update rliq set
	rliq.basicpaid = rliq.basicpaid + cro.basic,
	rliq.basicint = rliq.basicint - cro.basicint,
	rliq.basicdisc = rliq.basicdisc - cro.basicdisc,
	rliq.basicidlepaid = rliq.basicidlepaid + cro.basicidle,
	rliq.basicidledisc = rliq.basicidledisc - cro.basicidledisc,
	rliq.basicidleint = rliq.basicidleint - cro.basicidleint,
	rliq.sefpaid = rliq.sefpaid + cro.sef,
	rliq.sefint = rliq.sefint - cro.sefint,
	rliq.sefdisc = rliq.sefdisc - cro.sefdisc,
	rliq.firecodepaid = rliq.firecodepaid + cro.firecode,
	rliq.partialled = cro.partialled 
from rptledgeritem_qtrly rliq, cashreceiptitem_rpt_online cro 
where cro.rptreceiptid = $P{rptreceiptid}
  and rliq.objid = cro.rptledgeritemqtrlyid 
  and rliq.rptledgerid = cro.rptledgerid 
  and rliq.fullypaid = 0
  and rliq.rptledgerid = $P{rptledgerid}

[fullyPaidQtrlyItems]
update rliq set
	rliq.basicpaid = rliq.basic,
	rliq.basicidlepaid = rliq.basicidle,
	rliq.sefpaid = rliq.sef,
	rliq.firecodepaid = rliq.firecode,
	rliq.partialled = 0,
	rliq.fullypaid = 1
from rptledgeritem_qtrly rliq	
where rptledgerid = $P{rptledgerid}
and year = $P{year}
and qtr < $P{qtr}


[updateLedgerItemPayment]
update rli set
	rli.basicpaid = x.basicpaid,
	rli.basicint = x.basicint,
	rli.basicdisc = x.basicdisc,
	rli.basicidlepaid = x.basicidlepaid,
	rli.basicidledisc = x.basicidledisc,
	rli.basicidleint = x.basicidleint,
	rli.sefpaid = x.sefpaid,
	rli.sefint = x.sefint,
	rli.sefdisc = x.sefdisc,
	rli.firecodepaid = x.firecodepaid
from rptledgeritem rli, 
	(	select 
			parentid as rptledgeritemid, 
			sum(basicpaid) as basicpaid,
			sum(basicint) as basicint,
			sum(basicdisc) as basicdisc,
			sum(basicidlepaid) as basicidlepaid,
			sum(basicidledisc) as basicidledisc,
			sum(basicidleint) as basicidleint,
			sum(sefpaid) as sefpaid,
			sum(sefint) as sefint,
			sum(sefdisc) as sefdisc,
			sum(firecodepaid) as firecodepaid
		from rptledgeritem_qtrly
		where parentid = $P{parentid}
		group by parentid 
	)x 
where rli.objid = x.rptledgeritemid 


[updateLedgerItemQrtrlyFullyPaidFlag]  
update rliq set
	rliq.fullypaid  = case 
		when 
			rliq.basic <= rliq.basicpaid and 
			rliq.basicidle <= rliq.basicidlepaid and 
			rliq.sef <= rliq.sefpaid and 
			rliq.firecode <=  rliq.firecodepaid 
		then 1 
		else 0
		end 
from rptledgeritem_qtrly rliq
	inner join cashreceiptitem_rpt_online cro on rliq.objid = cro.rptledgeritemqtrlyid 
where cro.rptreceiptid = $P{rptreceiptid}
  and rliq.rptledgerid = $P{rptledgerid}
  and rliq.rptledgerid = cro.rptledgerid 



[updateLedgerItemFullyPaidFlag]
update rli set 
	rli.fullypaid  = 
		case when (select count(*) from rptledgeritem_qtrly 
			       where parentid = rli.objid 
			         and fullypaid = 0) = 0
			then 1 
			else 0
		end 
from rptledgeritem rli 
where rli.rptledgerid = $P{rptledgerid}
  

[getCollectionsByCount]
SELECT 
	cr.receiptno, 
	CASE WHEN cv.objid IS NULL THEN cr.amount  ELSE 0.0 END AS amount,
	CASE WHEN cv.objid IS NULL THEN 0  ELSE 1 END AS voided
FROM cashreceipt cr 
	LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
	LEFT JOIN remittance_cashreceipt rc ON cr.objid = rc.objid 
WHERE cr.collector_objid = $P{userid}
  AND rc.objid IS NULL 
ORDER BY cr.txndate DESC   



[getItemsForPrinting]
SELECT
	t.rptledgerid,
	t.tdno,
	t.owner_name, 
	t.rputype,
	t.totalav, t.fullpin,
	t.cadastrallotno,
	t.classcode,
	t.totalareasqm,
	t.barangay, 
	t.munidistrict,
	t.provcity, 
	SUM(t.basic) AS basic, 
	SUM(t.basicdisc) AS basicdisc, 
	SUM(t.basicint) AS basicint, 
	SUM(t.basicdp) AS basicdp, 
	SUM(t.basicnet) AS basicnet,
	SUM(t.basicidle) AS basicidle,
	SUM(t.sef) AS sef,  
	SUM(t.sefdisc) AS sefdisc, 
	SUM(t.sefint) AS sefint, 
	SUM(t.sefdp) AS sefdp, 
	SUM(t.sefnet) AS sefnet,
	SUM(t.firecode) AS firecode,
	SUM(t.amount) AS amount,
	t.fromyear,
	t.toyear,
	t.partialled,
	(SELECT MIN(fromqtr)  FROM cashreceiptitem_rpt_online WHERE rptreceiptid = t.rptreceiptid AND rptledgerid = t.rptledgerid AND YEAR = t.fromyear ) AS fromqtr,
	(SELECT MAX(toqtr) FROM cashreceiptitem_rpt_online WHERE rptreceiptid = t.rptreceiptid AND  rptledgerid = t.rptledgerid AND YEAR = t.toyear) AS toqtr
FROM ( 
	SELECT
		cri.rptledgerid,
		cri.rptreceiptid,
		rl.owner_name, 
		rl.faasid,
		rl.tdno,
		rl.rputype,
		rl.totalav, rl.fullpin,
		rl.totalareaha * 10000 AS  totalareasqm,
		rl.cadastrallotno,
		rl.classcode,
		b.name AS barangay,
		md.name as munidistrict,
		pct.name as provcity, 
		MIN(cri.year) AS fromyear,
		MAX(cri.year) AS toyear,
		SUM(basic) AS basic,
		SUM(basicint) AS basicint,
		SUM(basicdisc) AS basicdisc,
		SUM(basicint - basicdisc) AS basicdp,
		SUM(basic + basicint - basicdisc) AS basicnet,
		SUM(basicidle  - basicidledisc + basicidleint) AS basicidle,
		SUM(sef) AS sef,
		SUM(sefint) AS sefint,
		SUM(sefdisc) AS sefdisc,
		SUM(sefint - sefdisc) AS sefdp,
		SUM(sef + sefint - sefdisc) AS sefnet,
		SUM(firecode) AS firecode,
		SUM(basic + basicint - basicdisc + sef + sefint - sefdisc ) AS amount,
		MAX(cri.partialled) AS partialled 
	FROM cashreceiptitem_rpt_online cri
		INNER JOIN rptledger rl ON cri.rptledgerid = rl.objid 
		INNER JOIN sys_org b ON rl.barangayid = b.objid
		inner join sys_org md on md.objid = b.parent_objid 
		inner join sys_org pct on pct.objid = md.parent_objid
  WHERE cri.rptreceiptid = $P{rptreceiptid}

	GROUP BY 
		cri.rptreceiptid,
		cri.rptledgerid, 
		rl.owner_name, 
		rl.faasid,
		rl.tdno, 
		rl.rputype, rl.totalav, rl.fullpin, rl.totalareaha,
		rl.cadastrallotno,
		rl.classcode, b.name,
		md.name,
		pct.name
	) t
GROUP BY 
	t.rptreceiptid, 
	t.rptledgerid,
	t.owner_name, 
	t.faasid, 
	t.tdno,
	t.rputype,
	t.totalav, t.fullpin,
	t.cadastrallotno, 
	t.classcode,
	t.barangay,
	t.munidistrict,
	t.provcity, 
	t.totalareasqm,
	t.partialled,
	t.fromyear,
	t.toyear
ORDER BY t.fromyear 	
		

[getManualItemsForPrinting]	
SELECT
	t.*,
	(SELECT MIN(CASE WHEN qtr = 0 THEN fromqtr ELSE qtr END)  FROM cashreceiptitem_rpt_online WHERE rptreceiptid = t.rptreceiptid AND rptledgerid = t.rptledgerid AND YEAR = t.fromyear ) AS fromqtr,
	(SELECT MAX(CASE WHEN qtr = 0 THEN toqtr ELSE qtr END) FROM cashreceiptitem_rpt_online WHERE rptreceiptid = t.rptreceiptid AND  rptledgerid = t.rptledgerid AND YEAR = t.toyear) AS toqtr
FROM (
	SELECT
		rl.objid AS rptledgerid, 
		cri.rptreceiptid,
		rl.tdno,
		rl.owner_name, 
		rl.rputype,
		rl.totalav,
		rl.fullpin ,
		rl.cadastrallotno AS cadastrallotno,
		rl.classcode,
		b.name AS barangay,
		md.name as munidistrict,
		pc.name as provcity, 
		MIN(cri.year) AS fromyear,
		MAX(cri.year) AS toyear,
		SUM(basic) AS basic, 
		SUM(basicdisc) AS basicdisc, 
		SUM(basicint) AS basicint, 
		SUM(basicint - basicdisc) AS basicdp, 
		SUM(basic + basicint - basicdisc) AS basicnet,
		SUM(basicidle  - basicidledisc + basicidleint) AS basicidle,
		SUM(sef) AS sef,  
		SUM(sefdisc) AS sefdisc, 
		SUM(sefint) AS sefint, 
		SUM(sefint - sefdisc) AS sefdp, 
		SUM(sef + sefint - sefdisc) AS sefnet,
		SUM(firecode) AS firecode,
		SUM(basic + basicint- basicdisc + sef + sefint - sefdisc + firecode) AS amount,
		MAX(cri.partialled) AS partialled 
	FROM cashreceipt cr
		INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid
		INNER JOIN rptledger rl ON cri.rptledgerid = rl.objid 
		INNER JOIN sys_org b ON rl.barangayid = b.objid
		inner join sys_org md on md.objid = b.parent_objid 
		inner join sys_org pc on pc.objid = md.parent_objid 
	WHERE cr.objid = $P{objid}
	GROUP BY 
		cri.rptreceiptid,
		rl.objid, 
		rl.owner_name,
		rl.tdno,
		rl.classcode, 
		rl.rputype,
		rl.totalav,
		rl.fullpin,
		rl.cadastrallotno,
		rl.classification_objid ,
		b.name,
		md.name,
		pc.name
) t



[clearFaasRestrictions]
update f set 
	f.restrictionid = null
from faas f, rptledger rl 
where rl.objid = $P{rptledgerid}	
  and rl.faasid = f.objid 

  

[getUnpaidPropertiesForPayment]
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
	b.name AS barangay,
	rl.cadastrallotno,
	rl.barangayid,
	rl.classcode
FROM rptledger rl 
  INNER JOIN barangay b ON rl.barangayid = b.objid 
  INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
  LEFT JOIN rptledger_compromise rc ON rl.objid = rc.rptledgerid 
WHERE rl.objid IN (
	SELECT rl.objid 
	FROM rptledger rl 
	WHERE ${filters}
	 AND rl.state = 'APPROVED'
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
	 AND (rl.lastyearpaid < $P{billtoyear} 
				OR ( rl.lastyearpaid = $P{billtoyear} AND rl.lastqtrpaid < $P{billtoqtr})
	 )
)
AND ( rc.objid IS NULL OR rc.state <> 'APPROVED' ) 
AND rl.totalav > 0
ORDER BY rl.tdno  
${mssqlcountfilter}


[getPaidLedgersByReceipt]
select rptledgerid, min(year) as fromyear, max(year) as toyear,
	min(year - 1) as lastyearpaid, 4 as lastqtrpaid 
from cashreceiptitem_rpt_online
where rptreceiptid = $P{rptreceiptid}
group by rptledgerid


[findPreviousReceipt]
SELECT TOP 1 cr.objid AS rptreceiptid, cr.txndate 
FROM cashreceipt cr 
	LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
	INNER JOIN cashreceiptitem_rpt_online cro ON cr.objid = cro.rptreceiptid 
WHERE cro.rptledgerid = $P{rptledgerid}
	AND cr.txndate < $P{txndate}
	AND cv.objid IS NULL 
ORDER BY cr.objid, cr.txndate DESC 

