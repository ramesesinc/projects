[findInfo]
SELECT * FROM rpttaxclearance WHERE objid = $P{objid}

[insertTaxClearance]
INSERT INTO rpttaxclearance 
	(objid, year, qtr)
VALUES 
	($P{objid}, $P{year}, $P{qtr})


[getItems]
SELECT 
	rci.*,
	rl.objid as rptledgerid, 
	rl.tdno,
	rl.rputype,
	rl.fullpin ,
	rl.totalareaha,
	rl.totalareaha * 10000 as totalareasqm,
	rl.totalmv,
	rl.totalav,
	rl.cadastrallotno,
	rl.blockno,
	rl.administrator_name,
	f.administrator_address, 
	case when m.objid is not null then m.name else c.name end as lguname, 
	b.name AS barangay,
	rl.classcode,
	pc.name as classification, 
	rl.titleno,
	rp.surveyno
FROM rptcertificationitem rci 
	INNER JOIN rptledger rl ON rci.refid = rl.objid 
	INNER JOIN barangay b ON rl.barangayid = b.objid
	left JOIN propertyclassification pc ON rl.classification_objid = pc.objid  
	LEFT JOIN municipality m on b.parentid = m.objid
	LEFT JOIN district d on b.parentid = d.objid 
	LEFT JOIN city c on d.parentid = c.objid 
	LEFT JOIN faas f on rl.faasid = f.objid 
	LEFT JOIN realproperty rp on f.realpropertyid = rp.objid 
WHERE rci.rptcertificationid = $P{rptcertificationid}



[getClearedLedgers]
SELECT 
	rl.objid AS refid,
	rl.lastyearpaid,
	rl.lastqtrpaid,
	rl.tdno,
	rl.rputype,
	rl.fullpin ,
	rl.taxable,
	rl.totalareaha,
	rl.totalmv,
	rl.totalav,
	rl.cadastrallotno,
	b.name AS barangay,
	rl.classcode,
	rl.titleno
FROM rptledger rl
	INNER JOIN barangay b ON rl.barangayid = b.objid 
WHERE rl.state = 'APPROVED'
  AND rl.taxpayer_objid = $P{taxpayerid}
  AND ( rl.lastyearpaid > $P{year} OR (rl.lastyearpaid = $P{year} AND rl.lastqtrpaid >= $P{qtr}))



[getPaymentInfo]
SELECT 
	rl.objid as rptledgerid, 
	xr.receiptno AS orno,
	xr.txndate AS ordate,
	SUM(ri.basic + ri.basicint - ri.basicdisc + ri.sef + ri.sefint - ri.sefdisc) AS oramount,
	SUM(ri.basic) AS basic,
	SUM(ri.basicdisc) AS basicdisc,
	SUM(ri.basicint) as basicint,
	SUM(ri.sef) AS sef,
	SUM(ri.sefdisc) AS sefdisc,
  SUM(ri.sefint) AS sefint,  
	CASE WHEN (MIN(ri.qtr) = 1 AND MAX(ri.qtr) = 4) OR ((MIN(ri.qtr) = 0 AND MAX(ri.qtr) = 0))
		THEN  CONCAT('FULL ', ri.year)
		ELSE
			CONCAT(MIN(ri.qtr), 'Q,', ri.year, ' - ', MAX(ri.qtr), 'Q,', ri.year) 
	END AS period
FROM rptcertificationitem rci 
	INNER JOIN rptledger rl ON rci.refid = rl.objid 
	INNER JOIN cashreceiptitem_rpt_online  ri ON rl.objid = ri.rptledgerid
	INNER JOIN cashreceipt xr ON ri.rptreceiptid = xr.objid 
	LEFT JOIN cashreceipt_void cv ON xr.objid = cv.receiptid 
WHERE rci.rptcertificationid = $P{rptcertificationid}
	AND rl.objid = $P{rptledgerid}
   AND (ri.year = $P{year} AND ri.qtr <= $P{qtr})
  AND cv.objid IS NULL 
GROUP BY rl.objid, xr.receiptno, xr.txndate, ri.year

UNION ALL

SELECT 
	rl.objid as rptledgerid,
	rc.refno AS orno,
	rc.refdate AS ordate,
	SUM(rc.basic + rc.basicint - rc.basicdisc + rc.sef + rc.sefint - rc.sefdisc ) AS oramount,
	SUM(rc.basic) AS basic,
	SUM(rc.basicdisc) AS basicdisc,
	SUM(rc.basicint) as basicint,
	SUM(rc.sef) AS sef,
	SUM(rc.sefdisc) AS sefdisc,
  	SUM(rc.sefint) AS sefint,  
	CASE WHEN MIN(rc.fromqtr) = 1 AND MAX(rc.toqtr) = 4
		THEN  CONCAT('FULL ', rc.toyear)
		ELSE
			CONCAT(MIN(rc.fromqtr), 'Q,', rc.fromyear,  ' - ', MAX(rc.toqtr), 'Q,', rc.toyear) 
	END AS period
FROM rptcertificationitem rci 
	INNER JOIN rptledger rl ON rci.refid = rl.objid 
	INNER JOIN rptledger_credit rc on rl.objid = rc.rptledgerid
WHERE rci.rptcertificationid = $P{rptcertificationid}
	AND rl.objid = $P{rptledgerid}
  AND ( ( $P{year} > rc.fromyear AND $P{year} < rc.toyear)  or (($P{year} = rc.fromyear or $P{year} = rc.toyear) and  rc.toqtr <= $P{qtr}))
GROUP BY rl.objid, rc.refno, rc.refdate, rc.fromyear, rc.toyear 