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
    rliq.basicidle - rliq.basicidlepaid as basicidle,
    rliq.basicidleint,
    rliq.basicidledisc,
    rliq.firecode - rliq.firecodepaid as firecode,
    rliq.sh - rliq.shpaid as sh,
    rliq.shint,
    rliq.shdisc,
    rliq.revperiod,
    rliq.basic - rliq.basicpaid - rliq.basicdisc + rliq.basicint as basicnet,
    rliq.sef - rliq.sefpaid - rliq.sefdisc + rliq.sefint as sefnet,
    rliq.basicidle - rliq.basicidlepaid - rliq.basicidledisc + rliq.basicidleint as basicidlenet,
    rliq.sh - rliq.shpaid - rliq.shdisc + rliq.shint as shnet,
    ( rliq.basic - rliq.basicpaid - rliq.basicdisc + rliq.basicint + 
      rliq.basicidle - rliq.basicidlepaid - rliq.basicidledisc + rliq.basicidleint +
      rliq.sef - rliq.sefpaid - rliq.sefdisc + rliq.sefint +
      rliq.firecode - rliq.firecodepaid +
      rliq.sh - rliq.shpaid - rliq.shdisc + rliq.shint 
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
	rliq.basicpaid = rliq.basicpaid + rpi.basic,
	rliq.basicint = rliq.basicint - rpi.basicint,
	rliq.basicdisc = rliq.basicdisc - rpi.basicdisc,
	rliq.basicidlepaid = rliq.basicidlepaid + rpi.basicidle,
	rliq.basicidledisc = rliq.basicidledisc - rpi.basicidledisc,
	rliq.basicidleint = rliq.basicidleint - rpi.basicidleint,
	rliq.sefpaid = rliq.sefpaid + rpi.sef,
	rliq.sefint = rliq.sefint - rpi.sefint,
	rliq.sefdisc = rliq.sefdisc - rpi.sefdisc,
	rliq.firecodepaid = rliq.firecodepaid + rpi.firecode,
	rliq.shpaid = rliq.shpaid + rpi.sh,
	rliq.shint = rliq.shint - rpi.shint,
	rliq.shdisc = rliq.shdisc - rpi.shdisc,
	rliq.partialled = rpi.partialled 
from rptledgeritem_qtrly rliq, rptledger_payment rp, rptledger_payment_item rpi 
where rp.receiptid = $P{rptreceiptid}
  and rp.objid = rpi.parentid 
  and rliq.objid = rpi.rptledgeritemqtrlyid 
  and rliq.rptledgerid = rp.rptledgerid 
  and rliq.fullypaid = 0
  and rliq.rptledgerid = $P{rptledgerid}

[fullyPaidQtrlyItems]
update rliq set
	rliq.basicpaid = rliq.basic,
	rliq.basicidlepaid = rliq.basicidle,
	rliq.sefpaid = rliq.sef,
	rliq.firecodepaid = rliq.firecode,
	rliq.shpaid = rliq.sh,
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
	rli.firecodepaid = x.firecodepaid,
	rli.shpaid = x.shpaid,
	rli.shint = x.shint,
	rli.shdisc = x.shdisc
from rptledgeritem rli, 
	(	select 
			parentid, 
			sum(basicpaid) as basicpaid,
			sum(basicint) as basicint,
			sum(basicdisc) as basicdisc,
			sum(basicidlepaid) as basicidlepaid,
			sum(basicidledisc) as basicidledisc,
			sum(basicidleint) as basicidleint,
			sum(sefpaid) as sefpaid,
			sum(sefint) as sefint,
			sum(sefdisc) as sefdisc,
			sum(firecodepaid) as firecodepaid,
			sum(shpaid) as shpaid,
			sum(shint) as shint,
			sum(shdisc) as shdisc
		from rptledgeritem_qtrly
		where rptledgerid = $P{rptledgerid}
		group by parentid 
	)x 
where rli.rptledgerid = $P{rptledgerid}
and rli.objid = x.parentid 



[updateLedgerItemQrtrlyFullyPaidFlag]  
update rliq set
	rliq.fullypaid  = case 
		when 
			rliq.basic <= rliq.basicpaid and 
			rliq.basicidle <= rliq.basicidlepaid and 
			rliq.sef <= rliq.sefpaid and 
			rliq.firecode <=  rliq.firecodepaid  and
			rliq.sh <= rliq.shpaid 
		then 1 
		else 0
		end 
from rptledgeritem_qtrly rliq
	inner join rptledger_payment_item rpi on rliq.objid = rpi.rptledgeritemqtrlyid 
	inner join rptledger_payment rp on rpi.parentid = rp.objid 
where rp.receiptid = $P{rptreceiptid}
  and rliq.rptledgerid = $P{rptledgerid}
  and rliq.rptledgerid = rp.rptledgerid 



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
	SUM(t.sh) AS sh,
	SUM(t.amount) AS amount,
	t.fromyear,
	t.toyear,
	t.partialled,
	(SELECT MIN(fromqtr)  
		FROM rptledger_payment rp 
		inner join rptledger_payment_item rpi on rp.objid = rpi.parentid 
		WHERE rp.receiptid = t.rptreceiptid AND rp.rptledgerid = t.rptledgerid AND rpi.year = t.fromyear ) AS fromqtr,
	(SELECT MAX(toqtr) 
		FROM rptledger_payment rp
		inner join rptledger_payment_item rpi on rp.objid = rpi.parentid
		WHERE rp.receiptid = t.rptreceiptid AND  rp.rptledgerid = t.rptledgerid AND rpi.year = t.toyear) AS toqtr
FROM ( 
	SELECT
		rp.rptledgerid,
		rp.receiptid as rptreceiptid,
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
		MIN(rpi.year) AS fromyear,
		MAX(rpi.year) AS toyear,
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
		SUM(sh + shint - shdisc) AS sh,
		SUM(basic + basicint - basicdisc + sef + sefint - sefdisc ) AS amount,
		MAX(rpi.partialled) AS partialled 
	FROM rptledger_payment rp 
		inner join rptledger_payment_item rpi on rp.objid = rpi.parentid 
		INNER JOIN rptledger rl ON rp.rptledgerid = rl.objid 
		INNER JOIN sys_org b ON rl.barangayid = b.objid
		inner join sys_org md on md.objid = b.parent_objid 
		inner join sys_org pct on pct.objid = md.parent_objid
  WHERE rp.receiptid = $P{rptreceiptid}

	GROUP BY 
		rp.receiptid,
		rp.rptledgerid, 
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
	(SELECT MIN(CASE WHEN qtr = 0 THEN fromqtr ELSE qtr END)  
		FROM rptledger_payment rp 
		inner join rptledger_payment_item rpi on rp.objid = rpi.parentid 
		WHERE rp.receiptid = t.rptreceiptid AND rp.rptledgerid = t.rptledgerid AND rpi.year = t.fromyear ) AS fromqtr,
	(SELECT MAX(CASE WHEN qtr = 0 THEN toqtr ELSE qtr END) 
		FROM rptledger_payment rp 
		inner join rptledger_payment_item rpi on rp.objid = rpi.parentid 
		WHERE rp.receiptid = t.rptreceiptid AND  rp.rptledgerid = t.rptledgerid AND rpi.year = t.toyear) AS toqtr
FROM (
	SELECT
		rl.objid AS rptledgerid, 
		rp.receiptid as rptreceiptid,
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
		MIN(rpi.year) AS fromyear,
		MAX(rpi.year) AS toyear,
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
		SUM(sh + shint - shdisc) AS shnet,
		SUM(basic + basicint- basicdisc + sef + sefint - sefdisc) AS amount,
		MAX(rpi.partialled) AS partialled 
	FROM cashreceipt cr
		INNER JOIN rptledger_payment rp on cr.objid = rp.receiptid 
		INNER JOIN rptledger_payment_item rpi ON rp.objid = rpi.parentid 
		INNER JOIN rptledger rl ON rp.rptledgerid = rl.objid 
		INNER JOIN sys_org b ON rl.barangayid = b.objid
		inner join sys_org md on md.objid = b.parent_objid 
		inner join sys_org pc on pc.objid = md.parent_objid 
	WHERE cr.objid = $P{objid}
	GROUP BY 
		rp.receiptid,
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
select rp.rptledgerid, min(rpi.year) as fromyear, max(rpi.year) as toyear,
	min(rpi.year - 1) as lastyearpaid, 4 as lastqtrpaid 
from rptledger_payment rp
	inner join rptledger_payment_item rpi on rp.objid = rpi.parentid 
where rp.receiptid = $P{rptreceiptid}
group by rptledgerid


[findPreviousReceipt]
SELECT TOP 1 cr.objid AS rptreceiptid, cr.txndate 
FROM cashreceipt cr 
	LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
	INNER JOIN rptledger_payment rp ON cr.objid = rp.receiptid 
	INNER JOIN rptledger_payment_item rpi ON rp.objid = rpi.parentid 
WHERE rp.rptledgerid = $P{rptledgerid}
	AND cr.txndate < $P{txndate}
	AND cv.objid IS NULL 
ORDER BY cr.objid, cr.txndate DESC 

