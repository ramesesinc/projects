[getItemsForPayment]
SELECT 
	t.rptledgerid, t.tdno, t.owner_name,
	t.lastyearpaid, t.lastqtrpaid,
	t.fromyear, 
	(SELECT MIN(qtr) FROM rptbill_ledger_item WHERE billid = $P{billid} and rptledgerid = t.rptledgerid AND year = t.fromyear) AS fromqtr,
	t.toyear AS toyear,
	(SELECT MAX(qtr) FROM rptbill_ledger_item WHERE billid = $P{billid} and rptledgerid = t.rptledgerid AND year = t.toyear) AS toqtr,
	SUM(t.basic) AS totalbasic,
	SUM(t.sef) AS totalsef,
	SUM(t.firecode) AS totalfirecode,
	SUM(t.basic + t.firecode + t.basicidle) AS totalgeneral,
	SUM(t.basic + t.sef + t.firecode + t.basicidle) AS amount,
  	t.partialled 
FROM (
	SELECT
		rl.objid AS rptledgerid, 
		rl.lastyearpaid,
		rl.lastqtrpaid,
		rl.tdno, 
		rl.owner_name,
		MAX(bi.partialled) AS partialled,
		MIN(bi.year) AS fromyear,
		MAX(bi.year) AS toyear,
		SUM(bi.basic - bi.basicdisc + bi.basicint) AS basic,
		SUM(bi.sef - bi.sefdisc + sefint) AS sef,
		SUM(bi.basicidle - bi.basicidledisc + bi.basicidleint) AS basicidle, 
		SUM(bi.firecode) AS firecode 
	FROM rptledger rl
		INNER JOIN rptbill_ledger_item bi ON rl.objid = bi.rptledgerid
	WHERE rl.objid like $P{rptledgerid}
	  and rl.state = 'APPROVED'
	  and bi.billid = $P{billid}
	GROUP BY rl.objid, rl.owner_name, rl.tdno, rl.lastyearpaid, rl.lastqtrpaid
	${mysqlcountfilter}
) t
GROUP BY t.rptledgerid, t.owner_name, t.lastyearpaid, t.lastqtrpaid, t.tdno, t.fromyear, t.toyear, t.partialled


[findPaidItemTotals]
select sum(amount - discount) as amount 
from rptbill_ledger_account 
{ledgersfilter}


[insertPaidOnlineItems]
INSERT INTO cashreceiptitem_rpt_online
(
	objid,
	rptreceiptid,
	rptledgerid,
	rptledgerfaasid,
	rptledgeritemid,
	rptledgeritemqtrlyid,
	year,
	qtr,
	fromqtr,
	toqtr,
	basic,
	basicint,
	basicdisc,
	basicidle,
	basicidledisc,
	basicidleint,
	sef,
	sefint,
	sefdisc,
	firecode,
	basicnet,
	sefnet,
	total,
	revperiod,
	partialled
)
SELECT
	bi.objid,
	$P{rptreceiptid},
	bi.rptledgerid,
	bi.rptledgerfaasid,
	bi.rptledgeritemid,
	bi.rptledgeritemqtrlyid,
	bi.year,
	bi.qtr as qtr,
	bi.qtr,
	bi.qtr,
	bi.basic,
	bi.basicint,
	bi.basicdisc,
	bi.basicidle,
	bi.basicidledisc,
	bi.basicidleint,
	bi.sef,
	bi.sefint,
	bi.sefdisc,
	bi.firecode,
	bi.basicnet,
	bi.sefnet,
	bi.total,
	bi.revperiod,
	bi.partialled
FROM rptledger rl
		INNER JOIN rptbill_ledger_item bi ON rl.objid = bi.rptledgerid
		INNER JOIN rptledgeritem rli on bi.rptledgeritemid = rli.objid 
WHERE rl.objid = $P{rptledgerid}
	and bi.billid = $P{billid}
  


[insertPaidOnlineAccounts]
INSERT INTO cashreceiptitem_rpt_account
(
	objid,
	rptreceiptid,
	rptledgerid,
	revperiod,
	revtype,
	item_objid,
	amount,
	discount,
	sharetype
)
SELECT
	ba.objid,
	$P{rptreceiptid},
	ba.rptledgerid,
	ba.revperiod,
	ba.revtype,
	ba.item_objid,
	ba.amount,
	ba.discount,
	ba.sharetype
FROM rptledger rl
		INNER JOIN rptbill_ledger_account ba ON rl.objid = ba.rptledgerid
WHERE rl.objid = $P{rptledgerid}  
  and ba.billid = $P{billid}


[deletePartialledItems]
delete from rptledgeritem_qtrly_partial where rptledgerid = $P{rptledgerid}


[insertPartialledItems]
insert into rptledgeritem_qtrly_partial(
    objid,
    rptledgerid,
    year,
    qtr,
    basicpaid,
    basicintpaid,
    basicdisctaken,
    basicidlepaid,
    basicidleintpaid,
    basicidledisctaken,
    sefpaid,
    sefintpaid,
    sefdisctaken,
    firecodepaid
)
select
    bi.objid,
    bi.rptledgerid,
    bi.year,
    bi.qtr,
    bi.basic as basicpaid,
    bi.basicint as basicintpaid,
    bi.basicdisc as basicdisctaken,
    bi.basicidle as basicidlepaid,
    bi.basicidleint as basicidleintpaid,
    bi.basicidledisc as basicidledisctaken,
    bi.sef as sefpaid,
    bi.sefint as sefintpaid,
    bi.sefdisc as sefdisctaken,
    bi.firecode as firecodepaid
FROM rptledger rl
        INNER JOIN rptbill_ledger_item bi ON rl.objid = bi.rptledgerid
        INNER JOIN rptledgeritem rli on bi.rptledgeritemid = rli.objid 
WHERE rl.objid = $P{rptledgerid}
    and bi.billid = $P{billid}
    and bi.partialled = 1 
      



[deletePaidOnlineItems]  
DELETE FROM rptbill_ledger_item 
WHERE rptledgerid = $P{rptledgerid}


[deletePaidOnlineAccounts]  
DELETE FROM rptbill_ledger_account 
WHERE rptledgerid = $P{rptledgerid}


[deleteRptBillLedgers]
DELETE FROM rptbill_ledger WHERE billid = $P{billid}


[deleteRptBill]
DELETE FROM rptbill WHERE objid = $P{billid}



[updateLedgerYearQtrPaid]
UPDATE rptledger SET 
	lastyearpaid = $P{lastyearpaid},
	lastqtrpaid = $P{lastqtrpaid},
	updateflag = $P{updateflag}
WHERE objid = $P{rptledgerid}	


[updateLedgerItemQrtrlyPayment]
update rptledgeritem_qtrly rliq, cashreceiptitem_rpt_online cro set
	rliq.basicpaid = rliq.basicpaid + cro.basic,
	rliq.basicintpaid = rliq.basicintpaid + cro.basicint,
	rliq.basicdisctaken = rliq.basicdisctaken + cro.basicdisc,
	rliq.basicidlepaid = rliq.basicidlepaid + cro.basicidle,
	rliq.basicidledisctaken = rliq.basicidledisctaken + cro.basicidledisc,
	rliq.basicidleintpaid = rliq.basicidleintpaid + cro.basicidleint,
	rliq.sefpaid = rliq.sefpaid + cro.sef,
	rliq.sefintpaid = rliq.sefintpaid + cro.sefint,
	rliq.sefdisctaken = rliq.sefdisctaken + cro.sefdisc,
	rliq.firecodepaid = rliq.firecodepaid + cro.firecode,
	rliq.partialled = cro.partialled 
where cro.rptreceiptid = $P{rptreceiptid}
  and rliq.objid = cro.rptledgeritemqtrlyid 
  and rliq.rptledgerid = cro.rptledgerid 
  and rliq.fullypaid = 0


[updateLedgerItemPayment]
update rptledgeritem rli, 
	(	select 
			parentid as rptledgeritemid, 
			sum(basicpaid) as basicpaid,
			sum(basicintpaid) as basicintpaid,
			sum(basicdisctaken) as basicdisctaken,
			sum(basicidlepaid) as basicidlepaid,
			sum(basicidledisctaken) as basicidledisctaken,
			sum(basicidleintpaid) as basicidleintpaid,
			sum(sefpaid) as sefpaid,
			sum(sefintpaid) as sefintpaid,
			sum(sefdisctaken) as sefdisctaken,
			sum(firecodepaid) as firecodepaid
		from rptledgeritem_qtrly
		where rptledgerid = $P{rptledgerid}
		and year >= $P{fromyear} and year <= $P{toyear}
		group by parentid 
	)x 
set
	rli.basicpaid = x.basicpaid,
	rli.basicintpaid = x.basicintpaid,
	rli.basicdisctaken = x.basicdisctaken,
	rli.basicidlepaid = x.basicidlepaid,
	rli.basicidledisctaken = x.basicidledisctaken,
	rli.basicidleintpaid = x.basicidleintpaid,
	rli.sefpaid = x.sefpaid,
	rli.sefintpaid = x.sefintpaid,
	rli.sefdisctaken = x.sefdisctaken,
	rli.firecodepaid = x.firecodepaid
where rli.rptledgerid = $P{rptledgerid}
  and rli.year >= $P{fromyear} and rli.year <= $P{toyear}
  and rli.objid = x.rptledgeritemid 


[updateLedgerItemQrtrlyFullyPaidFlag]  
update rptledgeritem_qtrly rliq, cashreceiptitem_rpt_online cro set
	rliq.fullypaid  = case 
		when rliq.partialled = 1 then 0 
		when 
			rliq.basic <= rliq.basicpaid and 
			rliq.basicint <= rliq.basicintpaid and 
			rliq.basicdisc <= rliq.basicdisctaken and 
			rliq.basicidle <= rliq.basicidlepaid and 
			rliq.basicidledisc <= rliq.basicidledisctaken and 
			rliq.basicidleint <= rliq.basicidleintpaid and 
			rliq.sef <= rliq.sefpaid and 
			rliq.sefint <= rliq.sefintpaid and 
			rliq.sefdisc <= rliq.sefdisctaken and 
			rliq.firecode <=  rliq.firecodepaid 
		then 1 
		else 0
		end 
where cro.rptreceiptid = $P{rptreceiptid}
  and rliq.objid = cro.rptledgeritemqtrlyid 
  and rliq.rptledgerid = cro.rptledgerid 


[updateLedgerItemFullyPaidFlag]
update rptledgeritem rli set 
	rli.fullypaid  = 
		case when (select count(*) from rptledgeritem_qtrly 
			       where parentid = rli.objid 
			         and fullypaid = 0) = 0
			then 1 
			else 0
		end 
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
		



[getNoLedgerItemsForPrinting]	
SELECT
	t.*,
	CONCAT(
		(SELECT MIN(CASE WHEN qtr = 0 THEN fromqtr ELSE qtr END) FROM cashreceiptitem_rpt_online WHERE rptreceiptid = t.rptreceiptid AND year = t.minyear),
		'Q,', t.minyear, ' - ',
		(SELECT MAX(CASE WHEN qtr = 0 THEN toqtr ELSE qtr END) FROM cashreceiptitem_rpt_online WHERE rptreceiptid = t.rptreceiptid AND year = t.maxyear), 
		'Q,', t.maxyear
	) AS	period 
FROM (
	SELECT
		cri.rptreceiptid,
		nl.tdno,
		nl.owner_name, 
		nl.rputype,
		nl.originalav  AS totalav,
		nl.pin AS fullpin,
		nl.cadastrallotno AS cadastrallotno,
		nl.classification_objid AS classcode,
		b.name AS barangay,
		md.name as munidistrict,
		pc.name as provcity, 
		MIN(cri.year) AS minyear,
		MAX(cri.year) AS maxyear,
		SUM(basic) AS basic, 
		SUM(basicdisc) AS basicdisc, 
		SUM(basicint) AS basicint, 
		SUM(basicint - basicdisc) AS basicdp, 
		SUM(basic + basicint - basicdisc) AS basicnet,
		SUM(basicidle) AS basicidle,
		SUM(sef) AS sef,  
		SUM(sefdisc) AS sefdisc, 
		SUM(sefint) AS sefint, 
		SUM(sefint - sefdisc) AS sefdp, 
		SUM(sef + sefint - sefdisc) AS sefnet,
		SUM(firecode) AS firecode,
		SUM(basic + basicint- basicdisc + sef + sefint - sefdisc + firecode) AS amount
	FROM cashreceipt cr
		INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid
		INNER JOIN cashreceiptitem_rpt_noledger nl ON cri.objid = nl.objid 
		INNER JOIN sys_org b ON nl.barangay_objid = b.objid
		inner join sys_org md on md.objid = b.parent_objid 
		inner join sys_org pc on pc.objid = md.parent_objid 
	WHERE cr.objid = $P{objid}
	GROUP BY 
		cri.rptreceiptid,
		nl.owner_name,
		nl.tdno,
		nl.rputype,
		nl.originalav,
		nl.pin,
		nl.cadastrallotno,
		nl.classification_objid ,
		b.name,
		md.name,
		pc.name
) t




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
		rl.owner_name,
		rl.tdno,
		rl.rputype,
		rl.totalav,
		rl.fullpin,
		rl.cadastrallotno,
		rl.classification_objid ,
		b.name,
		md.name,
		pc.name
) t






[getLedgersMinPaidYearAndQtr] 
SELECT 
	x.*,
	CASE WHEN x.qtr = 0 THEN x.fromqtr ELSE x.qtr END AS minqtr
FROM
(
	SELECT 
		t.*,
		MIN(ri.qtr) AS qtr,
		MIN(ri.fromqtr) AS fromqtr,
		MAX(ri.toqtr) AS toqtr
	FROM (
		SELECT 
			cr.rptreceiptid, 
			cr.rptledgerid, 
			c.txndate, 
			MIN(cr.year) AS minyear
		FROM cashreceiptitem_rpt_online cr 
			INNER JOIN cashreceipt c ON cr.rptreceiptid = c.objid 
		WHERE cr.rptreceiptid = $P{rptreceiptid}
		GROUP BY cr.rptreceiptid, cr.rptledgerid, c.txndate
	)t
	INNER JOIN cashreceiptitem_rpt_online ri 
		ON t.rptledgerid = ri.rptledgerid AND t.rptreceiptid = ri.rptreceiptid AND t.minyear = ri.year 
	GROUP BY t.rptreceiptid, t.rptledgerid, t.minyear, t.txndate
) x	


[findLedgerPartialInfo]
SELECT * FROM rptledger WHERE objid = $P{rptledgerid}


[findPaidYears]
SELECT 
	MIN(year) AS minyear,
	MAX(year) AS maxyear 
FROM cashreceiptitem_rpt_online cr
WHERE cr.rptreceiptid = $P{rptreceiptid}

[findPreviousReceipt]
SELECT cr.objid AS rptreceiptid, cr.txndate 
FROM cashreceipt cr 
	LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
	INNER JOIN cashreceiptitem_rpt_online cro ON cr.objid = cro.rptreceiptid 
WHERE cro.rptledgerid = $P{rptledgerid}
	AND cr.txndate < $P{txndate}
	AND cv.objid IS NULL 
ORDER BY cr.objid, cr.txndate DESC 
LIMIT 1





[resetVoidedLedgerInfo]
UPDATE rptledger SET 
	nextbilldate = null,
	lastyearpaid = $P{lastyearpaid},
	lastqtrpaid = $P{lastqtrpaid}
WHERE objid = $P{rptledgerid}	



[getSummarizedCashReceiptItems]
SELECT
	rb.objid AS item_objid,
	rb.code AS item_code, 
	rb.title AS item_title,
	rb.fund_objid AS item_fund_objid, rb.fund_code AS item_fund_code, rb.fund_title AS item_fund_title,
	SUM(ba.amount) AS amount,
	SUM(ba.discount) AS discount
FROM rptledger rl
	INNER JOIN rptbill_ledger_account ba ON rl.objid = ba.rptledgerid
	INNER JOIN itemaccount rb ON ba.item_objid = rb.objid 
WHERE ${filter}
  and ba.billid = $P{billid}
GROUP BY 
	rb.objid, rb.code, rb.title, rb.fund_objid,
	rb.fund_code, rb.fund_title


[findRevenueItemById]
SELECT 
	objid, code, title, 
	fund_objid, fund_code, fund_title
FROM itemaccount 
WHERE objid = $P{objid}




[getOnlinePaidItems]
select 
	cro.rptledgeritemid, cro.year, cro.fromqtr, cro.toqtr, cro.partialled  
from cashreceiptitem_rpt_online cro 
where cro.rptreceiptid = $P{rptreceiptid}
  and cro.rptledgerid = $P{rptledgerid}
order by cro.year, cro.toqtr 



[findLedgerItemByYear]
SELECT objid 
FROM rptledgeritem 
WHERE rptledgerid = $P{rptledgerid}
  and year = $P{year} 


[clearFaasRestrictions]
update faas f, rptledger rl set 
	f.restrictionid = null
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
${mysqlcountfilter}


[findModifiedLedgerUpdateFlag]
select rl.tdno 
from rptbill_ledger rbl 
	inner join rptledger rl on rbl.rptledgerid = rl.objid 
where rbl.billid = $P{billid}
  and rbl.updateflag <> rl.updateflag

[findLastPaidLedgerItemQtrly]
select rliq.year, rliq.qtr 
from rptledgeritem_qtrly rliq
where rliq.rptledgerid = $P{rptledgerid}
and rliq.fullypaid = 1 
order by rliq.year desc, rliq.qtr desc 


[findFirstLedgerItemQtrly]
select 
	case when qtr = 1 then year - 1 else year end as year,
	case when qtr = 1 then 4 else qtr - 1 end as qtr
from rptledgeritem_qtrly rliq
where rptledgerid = $P{rptledgerid}
order by year, qtr


[getPaidLedgersByReceipt]
select rptledgerid, min(year) as fromyear, max(year) as toyear,
	min(year - 1) as lastyearpaid, 4 as lastqtrpaid 
from cashreceiptitem_rpt_online
where rptreceiptid = $P{rptreceiptid}
group by rptledgerid



[revertLedgerItemQtrlyPayment]
update rptledgeritem_qtrly rliq, cashreceiptitem_rpt_online cro set
	rliq.basicpaid = rliq.basicpaid - cro.basic,
	rliq.basicintpaid = rliq.basicintpaid - cro.basicint,
	rliq.basicdisctaken = rliq.basicdisctaken - cro.basicdisc,
	rliq.basicidlepaid = rliq.basicidlepaid - cro.basicidle,
	rliq.basicidledisctaken = rliq.basicidledisctaken - cro.basicidledisc,
	rliq.basicidleintpaid = rliq.basicidleintpaid - cro.basicidleint,
	rliq.sefpaid = rliq.sefpaid - cro.sef,
	rliq.sefintpaid = rliq.sefintpaid - cro.sefint,
	rliq.sefdisctaken = rliq.sefdisctaken - cro.sefdisc,
	rliq.firecodepaid = rliq.firecodepaid - cro.firecode,
	rliq.partialled = case when rliq.basicpaid - cro.basic = 0 then 0 else 1 end,
	rliq.fullypaid = 0
where cro.rptreceiptid = $P{rptreceiptid}
  and cro.rptledgerid = $P{rptledgerid}
  and rliq.objid = cro.rptledgeritemqtrlyid 
  and rliq.rptledgerid = cro.rptledgerid 


[deleteLedgerQtrlyItemFullyPaid]
delete from rptledgeritem_qtrly 
where rptledgerid = $P{rptledgerid} and fullypaid = 1
	


[deleteLedgerItemFullyPaid]
delete from rptledgeritem
where rptledgerid = $P{rptledgerid}
	and fullypaid = 1 


[findRPTReceiptItemTotal]
select sum(total) as total from cashreceiptitem_rpt_online where rptreceiptid = $P{objid}

[findRPTReceiptAcctTotal]
select sum(amount) as total from cashreceiptitem_rpt_account where rptreceiptid = $P{objid}