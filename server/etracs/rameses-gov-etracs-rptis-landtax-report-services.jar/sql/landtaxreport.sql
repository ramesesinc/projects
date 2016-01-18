[generateDelinquencyReport]
SELECT
	f.taxpayer_name,
	f.taxpayer_address,
	r.fullpin AS pin,
	f.tdno,
	pc.code AS classcode,
	r.rputype,
	b.name AS barangay,
	(SELECT SUM(basic + basicint - basicdisc - basicpaid) FROM rptledgeritem WHERE rptledgerid = rl.objid AND state = 'OPEN' AND year <= $P{currentyear}) AS basicnet,
	(SELECT SUM(sef + sefint - sefdisc - sefpaid) FROM rptledgeritem WHERE rptledgerid = rl.objid AND state = 'OPEN' AND year <= $P{currentyear}) AS sefnet,
	(SELECT SUM(basic + basicint - basicdisc - basicpaid + sef + sefint - sefdisc - sefpaid) FROM rptledgeritem WHERE rptledgerid = rl.objid AND state = 'OPEN' AND year <= $P{currentyear}) AS total,
	rl.lastyearpaid,
	rl.lastqtrpaid,
	b.objid
FROM realproperty rp 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
	INNER JOIN rpu r ON rp.objid = r.realpropertyid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN faas f ON r.objid = f.rpuid 
	INNER JOIN rptledger rl ON f.objid = rl.faasid 	
WHERE rp.barangayid = $P{barangayid}
  AND rl.state = 'APPROVED' 
  AND (rl.lastyearpaid < $P{currentyear} OR (rl.lastyearpaid = $P{lastyearpaid} AND rl.lastqtrpaid < 4 ))


[generateDelinquencyReportOldFormat]
SELECT
	f.taxpayer_name,
	f.taxpayer_address,
	r.fullpin AS pin,
	f.tdno,
	pc.code AS classcode,
	r.rputype,
	b.name AS barangay,
	rl.lastyearpaid,
	rl.lastqtrpaid,
	CASE WHEN lastyearpaid + 1 = $P{currentyear} THEN 'I. CURRENT DELINQUENCY' ELSE 'II. DELINQUENT' END AS delinquenttype, 
	$P{currentyear} - lastyearpaid AS yearsdelinquent,
	(SELECT SUM(basic - basicpaid) FROM rptledgeritem WHERE rptledgerid = rl.objid AND state = 'OPEN' AND year <= $P{currentyear}) AS basic,
	(SELECT SUM(basicint) FROM rptledgeritem WHERE rptledgerid = rl.objid AND state = 'OPEN' AND year <= $P{currentyear}) AS basicint,
	(SELECT SUM(basicdisc) FROM rptledgeritem WHERE rptledgerid = rl.objid AND state = 'OPEN' AND year <= $P{currentyear}) AS basicdisc,
	(SELECT SUM(sef - sefpaid) FROM rptledgeritem WHERE rptledgerid = rl.objid AND state = 'OPEN' AND year <= $P{currentyear}) AS sef,
	(SELECT SUM(sefint) FROM rptledgeritem WHERE rptledgerid = rl.objid AND state = 'OPEN' AND year <= $P{currentyear}) AS sefint,
	(SELECT SUM(sefdisc) FROM rptledgeritem WHERE rptledgerid = rl.objid AND state = 'OPEN' AND year <= $P{currentyear}) AS sefdisc,
	(SELECT SUM(basic + basicint - basicdisc - basicpaid + sef + sefint - sefdisc - sefpaid) FROM rptledgeritem WHERE rptledgerid = rl.objid AND state = 'OPEN' AND year <= $P{currentyear}) AS total,
	xr.receiptno AS orno,
	xr.txndate AS ordate
FROM realproperty rp 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
	INNER JOIN rpu r ON rp.objid = r.realpropertyid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN faas f ON r.objid = f.rpuid 
	INNER JOIN rptledger rl ON f.objid = rl.faasid 	
	LEFT JOIN xreceipt xr ON rl.lastreceiptid = xr.objid 
WHERE rp.barangayid = $P{barangayid}
  AND rl.state = 'APPROVED' 
  AND (rl.lastyearpaid < $P{currentyear} OR (rl.lastyearpaid = $P{currentyear} AND rl.lastqtrpaid < 4 ))



[getAbstractCollectionBASIC]
SELECT  
	xr.receiptno AS orno, 
	xr.txndate AS ordate, 	
	'BASIC' AS `type`, 
	CASE 
		WHEN v.objid IS NOT NULL  THEN ''
		WHEN MIN(ri.year) = MAX(ri.year) AND MIN(ri.qtr) = 1 AND MAX(ri.qtr) = 4
			THEN CONCAT('FULL ', MIN(ri.year))
		WHEN MIN(ri.year) = MAX(ri.year) AND MIN(ri.qtr) = MAX(ri.qtr)
			THEN CONCAT('FULL ', MIN(ri.qtr), 'Q,', MIN(ri.year))
		WHEN MIN(ri.year) = MAX(ri.year)
			THEN CONCAT(MIN(ri.qtr), 'Q-', MAX(ri.qtr), 'Q, ', MIN(ri.year) )
		ELSE 
			CONCAT(  MIN(CONCAT(ri.qtr, 'Q,', ri.year)), ' - ', MAX(CONCAT(ri.qtr, 'Q,', ri.year)) )
	END AS period,
	CASE WHEN v.objid IS NULL THEN f.taxpayer_name ELSE '*** VOIDED ***' END AS taxpayername, 
	CASE WHEN v.objid IS NULL THEN f.tdno ELSE '' END AS tdno, 
	CASE WHEN v.objid IS NULL THEN b.name ELSE '' END AS barangay, 
	CASE WHEN v.objid IS NULL THEN pc.code ELSE '' END AS classification, 
	SUM(CASE WHEN v.objid IS NULL AND ri.revtype IN ('current', 'advance') THEN ri.basic ELSE 0.0 END) AS currentyear,
	SUM(CASE WHEN v.objid IS NULL AND ri.revtype IN ('previous','prior') THEN ri.basic ELSE 0.0 END) AS previousyear,
	SUM(CASE WHEN v.objid IS NULL AND ri.revtype IN ('current', 'advance') THEN ri.basicdisc ELSE 0.0 END) AS discount,
	SUM(CASE WHEN v.objid IS NULL AND ri.revtype IN ('current', 'advance') THEN ri.basicint ELSE 0.0 END) AS penaltycurrent,
	SUM(CASE WHEN v.objid IS NULL AND ri.revtype IN ('previous','prior') THEN ri.basicint ELSE 0.0 END) AS penaltyprevious
FROM cashreceipt xr
	INNER JOIN cashreceipt_rpt rr ON xr.objid = rr.objid 
	INNER JOIN cashreceipt_rpt_item ri ON rr.objid = ri.rptreceiptid 
	INNER JOIN rptledger rl ON ri.rptledgerid = rl.objid 
	INNER JOIN faas f ON rl.faasid = f.objid 
	INNER JOIN rpu r ON f.rpuid = r.objid
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rl.barangayid = b.objid 
	LEFT JOIN cashreceipt_void v ON xr.objid = v.receiptid
${whereclause}
GROUP BY xr.receiptno, xr.txndate, f.taxpayer_name, f.tdno, b.name, pc.code 	
ORDER BY xr.receiptno;


[getAbstractCollectionSEF]
SELECT  
	xr.receiptno AS orno, 
	xr.txndate AS ordate, 	
	'SEF' AS `type`, 
	CASE 
		WHEN v.objid IS NOT NULL  THEN ''
		WHEN MIN(ri.year) = MAX(ri.year) AND MIN(ri.qtr) = 1 AND MAX(ri.qtr) = 4
			THEN CONCAT('FULL ', MIN(ri.year))
		WHEN MIN(ri.year) = MAX(ri.year) AND MIN(ri.qtr) = MAX(ri.qtr)
			THEN CONCAT('FULL ', MIN(ri.qtr), 'Q,', MIN(ri.year))
		WHEN MIN(ri.year) = MAX(ri.year)
			THEN CONCAT(MIN(ri.qtr), 'Q-', MAX(ri.qtr), 'Q, ', MIN(ri.year) )
		ELSE 
			CONCAT(  MIN(CONCAT(ri.qtr, 'Q,', ri.year)), ' - ', MAX(CONCAT(ri.qtr, 'Q,', ri.year)) )
	END AS period,
	CASE WHEN v.objid IS NULL THEN f.taxpayer_name ELSE '*** VOIDED ***' END AS taxpayername, 
	CASE WHEN v.objid IS NULL THEN f.tdno ELSE '' END AS tdno, 
	CASE WHEN v.objid IS NULL THEN b.name ELSE '' END AS barangay, 
	CASE WHEN v.objid IS NULL THEN pc.code ELSE '' END AS classification, 
	SUM(CASE WHEN v.objid IS NULL AND ri.revtype IN ('current', 'advance') THEN ri.sef ELSE 0.0 END) AS currentyear,
	SUM(CASE WHEN v.objid IS NULL AND ri.revtype IN ('previous','prior') THEN ri.sef ELSE 0.0 END) AS previousyear,
	SUM(CASE WHEN v.objid IS NULL AND ri.revtype IN ('current', 'advance') THEN ri.sefdisc ELSE 0.0 END) AS discount,
	SUM(CASE WHEN v.objid IS NULL AND ri.revtype IN ('current', 'advance') THEN ri.sefint ELSE 0.0 END) AS penaltycurrent,
	SUM(CASE WHEN v.objid IS NULL AND ri.revtype IN ('previous','prior') THEN ri.sefint ELSE 0.0 END) AS penaltyprevious
FROM cashreceipt xr
	INNER JOIN cashreceipt_rpt rr ON xr.objid = rr.objid 
	INNER JOIN cashreceipt_rpt_item ri ON rr.objid = ri.rptreceiptid 
	INNER JOIN rptledger rl ON ri.rptledgerid = rl.objid 
	INNER JOIN faas f ON rl.faasid = f.objid 
	INNER JOIN rpu r ON f.rpuid = r.objid
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rl.barangayid = b.objid 
	LEFT JOIN cashreceipt_void v ON xr.objid = v.receiptid
${whereclause}
GROUP BY xr.receiptno, xr.txndate, f.taxpayer_name, f.tdno, b.name, pc.code 	
ORDER BY xr.receiptno;


[generateRPTCollectionReport_standard]
select 
	t.classname,  min(t.special) as special , 
	sum( basiccurrent ) as basiccurrent, sum( basicdisc ) as basicdisc, sum( basicprev ) as basicprev, 
	sum( basiccurrentint ) as basiccurrentint, sum( basicprevint ) as basicprevint, sum( basicnet ) as basicnet, 
	sum( sefcurrent ) as sefcurrent, sum( sefdisc ) as sefdisc, sum( sefprev ) as sefprev, 
	sum( sefcurrentint ) as sefcurrentint, sum( sefprevint ) as sefprevint, sum( sefnet ) as sefnet, 
	sum( netgrandtotal ) as netgrandtotal, sum( idlecurrent ) as idlecurrent, sum( idleprev ) as idleprev, 
	sum( idledisc ) as idledisc, sum( idleint ) as idleint, sum( idlenet ) as idlenet,sum( levynet ) as levynet
from ( 
	SELECT  
		pc.name as classname, pc.orderno, pc.special,  
		case when ri.revperiod='current' then ri.basic else 0.0 end  as basiccurrent,
		case when ri.revperiod='current' then ri.basicdisc else 0.0 end  as basicdisc,
		case when ri.revperiod in ('previous', 'prior') then ri.basic else 0.0 end  as basicprev,
		case when ri.revperiod='current' then ri.basicint else 0.0 end  as basiccurrentint,
		case when ri.revperiod in ('previous', 'prior') then ri.basicint else 0.0 end  as basicprevint,
		case when ri.revperiod in ('current', 'previous', 'prior') then (ri.basic - ri.basicdisc + ri.basicint) else 0.0 end as basicnet, 
		case when ri.revperiod='current' then ri.sef else 0.0 end  as sefcurrent,
		case when ri.revperiod='current' then ri.sefdisc else 0.0 end  as sefdisc,
		case when ri.revperiod in ('previous', 'prior') then ri.sef else 0.0 end  as sefprev,
		case when ri.revperiod='current' then ri.sefint else 0.0 end  as sefcurrentint,
		case when ri.revperiod in ('previous', 'prior') then ri.sefint else 0.0 end as sefprevint,
		case when ri.revperiod in ('current', 'previous', 'prior') then (ri.sef - ri.sefdisc + ri.sefint) else 0.0 end as sefnet,  
		case when ri.revperiod in ('current', 'previous', 'prior') then (ri.basic - ri.basicdisc + ri.basicint + ri.sef - ri.sefdisc + ri.sefint ) else 0.0 end as netgrandtotal, 
		case when ri.revperiod='current' then ri.basicidle else 0.0 end  as idlecurrent,
		case when ri.revperiod in ('previous', 'prior') then ri.basicidle else 0.0 end  as idleprev,
		0.0 as idledisc, 0.0 as idleint, 
		case when ri.revperiod in ('current', 'previous', 'prior') then ri.basicidle else 0.0 end as idlenet,
		 0.0 as levynet
	FROM cashreceipt cr 
	    INNER JOIN cashreceiptitem_rpt_online ri ON cr.objid = ri.rptreceiptid 
	    INNER JOIN rptledger rl ON ri.rptledgerid = rl.objid  
        INNER JOIN propertyclassification pc ON rl.classification_objid = pc.objid 
        INNER JOIN remittance_cashreceipt rc ON cr.objid = rc.objid
	    LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid
	where cr.receiptdate BETWEEN $P{fromdate} AND $P{todate} 
		 and cv.objid is null  

	union all 

	SELECT  
		pc.name as classname, pc.orderno, pc.special,  
		case when ri.revperiod='current' then ri.basic else 0.0 end  as basiccurrent,
		case when ri.revperiod='current' then ri.basicdisc else 0.0 end  as basicdisc,
		case when ri.revperiod in ('previous', 'prior') then ri.basic else 0.0 end  as basicprev,
		case when ri.revperiod='current' then ri.basicint else 0.0 end  as basiccurrentint,
		case when ri.revperiod in ('previous', 'prior') then ri.basicint else 0.0 end  as basicprevint,
		case when ri.revperiod in ('current', 'previous', 'prior') then (ri.basic - ri.basicdisc + ri.basicint) else 0.0 end as basicnet, 
		case when ri.revperiod='current' then ri.sef else 0.0 end  as sefcurrent,
		case when ri.revperiod='current' then ri.sefdisc else 0.0 end  as sefdisc,
		case when ri.revperiod in ('previous', 'prior') then ri.sef else 0.0 end  as sefprev,
		case when ri.revperiod='current' then ri.sefint else 0.0 end  as sefcurrentint,
		case when ri.revperiod in ('previous', 'prior') then ri.sefint else 0.0 end as sefprevint,
		case when ri.revperiod in ('current', 'previous', 'prior') then (ri.sef - ri.sefdisc + ri.sefint) else 0.0 end as sefnet,  
		case when ri.revperiod in ('current', 'previous', 'prior') then (ri.basic - ri.basicdisc + ri.basicint + ri.sef - ri.sefdisc + ri.sefint ) else 0.0 end as netgrandtotal, 
		case when ri.revperiod='current' then ri.basicidle else 0.0 end  as idlecurrent,
		case when ri.revperiod in ('previous', 'prior') then ri.basicidle else 0.0 end  as idleprev,
		0.0 as idledisc, 0.0 as idleint, 
		case when ri.revperiod in ('current', 'previous', 'prior') then ri.basicidle else 0.0 end as idlenet,
		0.0 as levynet
	FROM cashreceipt cr 
	    INNER JOIN cashreceiptitem_rpt_online ri ON cr.objid = ri.rptreceiptid 
		INNER JOIN cashreceiptitem_rpt_noledger ril on ril.objid = ri.objid 
		INNER JOIN propertyclassification pc ON ril.classification_objid = pc.objid
        INNER JOIN remittance_cashreceipt rc ON cr.objid = rc.objid
	    LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid
	where cr.receiptdate BETWEEN $P{fromdate} AND $P{todate} 
		 and cv.objid is null   
) t 	
group by t.classname
ORDER BY min(t.orderno)  


[generateRPTCollectionReport_advance]
select 
	t.classname,  min(t.special) as special , 
	sum( basic)  as basic, sum( basicdisc ) as basicdisc, 
	sum( sef)  as sef, sum( sefdisc ) as sefdisc, 
	sum(basicnet) as basicnet, sum(sefnet ) as sefnet,
	sum( netgrandtotal ) as netgrandtotal, sum(idle) as idle 
from ( 
	SELECT  
		pc.name as classname, pc.orderno, pc.special,  
		ri.basic, ri.basicdisc, ( ri.basic - ri.basicdisc ) as basicnet,
		ri.sef, ri.sefdisc, (ri.sef - ri.sefdisc ) as sefnet,
		( ri.basic - ri.basicdisc + ri.sef - ri.sefdisc + ri.basicidle ) as netgrandtotal , ri.basicidle as idle 
	FROM cashreceipt cr 
	    INNER JOIN cashreceiptitem_rpt_online ri ON cr.objid = ri.rptreceiptid 
	    INNER JOIN rptledger rl ON ri.rptledgerid = rl.objid  
        INNER JOIN propertyclassification pc ON rl.classification_objid = pc.objid 
        INNER JOIN remittance_cashreceipt rc ON cr.objid = rc.objid
	    LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid
	where cr.receiptdate BETWEEN $P{fromdate} AND $P{todate} 
		 and ri.revperiod = 'advance'
		 and cv.objid is null   
		
	union all 

	SELECT  
		pc.name as classname, pc.orderno, pc.special,  
		ri.basic, ri.basicdisc, ( ri.basic - ri.basicdisc ) as basicnet,
		ri.sef, ri.sefdisc, (ri.sef - ri.sefdisc ) as sefnet,
		( ri.basic - ri.basicdisc + ri.sef - ri.sefdisc + ri.basicidle) as netgrandtotal , ri.basicidle as idle 
	FROM cashreceipt cr 
	    INNER JOIN cashreceiptitem_rpt_online ri ON cr.objid = ri.rptreceiptid 
		INNER JOIN cashreceiptitem_rpt_noledger ril on ril.objid = ri.objid 
		INNER JOIN propertyclassification pc ON ril.classification_objid = pc.objid
        INNER JOIN remittance_cashreceipt rc ON cr.objid = rc.objid
	    LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid
	where cr.receiptdate BETWEEN $P{fromdate} AND $P{todate} 
		and ri.revperiod = 'advance'
		and cv.objid is null   
	 
 ) t 	
group by t.classname
ORDER BY min(t.orderno)  


[findCollectionDisposition_standard]
select 
	sum( provcitybasicshare ) as provcitybasicshare,
	sum( munibasicshare ) as munibasicshare,
	sum( brgybasicshare ) as brgybasicshare,
	sum( provcitysefshare ) as provcitysefshare,
	sum( munisefshare ) as munisefshare,
	sum( brgysefshare ) as brgysefshare
from ( 
	SELECT  
		case when ri.revtype in ('basic', 'basicint', 'basicidle') and ri.sharetype in ('province', 'city') then ri.amount else 0.0 end as provcitybasicshare,
		case when ri.revtype in ('basic', 'basicint', 'basicidle')  and ri.sharetype in ('municipality') then ri.amount else 0.0 end as munibasicshare,
		case when ri.revtype in ('basic', 'basicint', 'basicidle')  and ri.sharetype in ('barangay') then ri.amount else 0.0 end as brgybasicshare,
		case when ri.revtype in ('sef', 'sefint') and ri.sharetype in ('province', 'city') then ri.amount else 0.0 end as provcitysefshare,
		case when ri.revtype in ('sef', 'sefint') and ri.sharetype in ('municipality') then ri.amount else 0.0 end as munisefshare,
		case when ri.revtype in ('sef', 'sefint') and ri.sharetype in ('barangay') then ri.amount else 0.0 end as brgysefshare 
	FROM cashreceipt cr 
		INNER JOIN cashreceiptitem_rpt_account ri ON cr.objid = ri.rptreceiptid 
	    INNER JOIN remittance_cashreceipt rc ON cr.objid = rc.objid
	    LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid
	where cr.receiptdate BETWEEN $P{fromdate} AND $P{todate} 
		 and ri.revperiod != 'advance' 
		 and cv.objid is null  
   ) t 


[findCollectionDisposition_advance]
select 
	sum( provcitybasicshare ) as provcitybasicshare,
	sum( munibasicshare ) as munibasicshare,
	sum( brgybasicshare ) as brgybasicshare,
	sum( provcitysefshare ) as provcitysefshare,
	sum( munisefshare ) as munisefshare,
	sum( brgysefshare ) as brgysefshare
from ( 
	SELECT  
		case when ri.revtype in ('basic', 'basicint', 'basicidle') and ri.sharetype in ('province', 'city') then ri.amount else 0.0 end as provcitybasicshare,
		case when ri.revtype in ('basic', 'basicint', 'basicidle')  and ri.sharetype in ('municipality') then ri.amount else 0.0 end as munibasicshare,
		case when ri.revtype in ('basic', 'basicint', 'basicidle')  and ri.sharetype in ('barangay') then ri.amount else 0.0 end as brgybasicshare,
		case when ri.revtype in ('sef', 'sefint') and ri.sharetype in ('province', 'city') then ri.amount else 0.0 end as provcitysefshare,
		case when ri.revtype in ('sef', 'sefint') and ri.sharetype in ('municipality') then ri.amount else 0.0 end as munisefshare,
		case when ri.revtype in ('sef', 'sefint') and ri.sharetype in ('barangay') then ri.amount else 0.0 end as brgysefshare 
	FROM cashreceipt cr 
		INNER JOIN cashreceiptitem_rpt_account ri ON cr.objid = ri.rptreceiptid 
	    INNER JOIN remittance_cashreceipt rc ON cr.objid = rc.objid
	    LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid
	where cr.receiptdate BETWEEN $P{fromdate} AND $P{todate} 
		 and ri.revperiod = 'advance' 
		 and cv.objid is null  
   ) t 

