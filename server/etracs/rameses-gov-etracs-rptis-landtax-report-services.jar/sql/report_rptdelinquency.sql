[getBarangayList]
SELECT objid, name FROM barangay ORDER BY pin 


[getOpenLedgersByBarangay]
SELECT 
	rl.objid, rl.barangayid, rl.tdno
FROM rptledger rl
	LEFT JOIN faas f ON rl.faasid = f.objid 
WHERE barangayid = $P{barangayid}
  AND rl.state = 'APPROVED'
  AND (rl.lastyearpaid < $P{cy}  
  	     OR (rl.lastyearpaid = $P{cy} AND rl.lastqtrpaid < 4)
  )
  AND rl.totalav > 0 
  

[cleanup]
DELETE FROM report_rptdelinquency 
WHERE barangayid LIKE $P{barangayid} 


[getDelinquentLedgers]
SELECT 
	e.name AS taxpayername,
	e.address_text AS taxpayeraddress,
	rl.administrator_name, 
	rl.fullpin AS pin,
	rl.tdno,
	rl.classcode,
	rl.cadastrallotno,
	rl.rputype,
	rl.totalav, 
	rl.totalareaha,
	rl.totalareaha * 10000 as totalareasqm,
	b.name AS barangay, b.objid, 
	x.* 
FROM ( 
	SELECT 
		rptledgerid, MAX(dtgenerated) AS dtgenerated, 
		MIN(year) AS startyearpaid, MIN(qtr) as startqtrpaid, 
		MAX(year) AS lastyearpaid, MAX(4) AS lastqtrpaid,  
		SUM(basic - basicdisc) AS basic, SUM(sef - sefdisc) AS sef, 
		SUM(basicint) AS basicint, SUM(sefint) AS sefint, 
		SUM(basic - basicdisc + basicint) AS basicnet,
		SUM(sef - sefdisc + sefint) AS sefnet,
		SUM(basic - basicdisc + basicint  + sef - sefdisc + sefint ) AS total
	FROM report_rptdelinquency r
	WHERE barangayid LIKE $P{barangayid} 
	AND NOT EXISTS(select * from rptledger_restriction where parentid = r.rptledgerid )
	${filter} 
	GROUP BY rptledgerid 
)x 
	INNER JOIN rptledger rl ON x.rptledgerid = rl.objid 
	INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
	INNER JOIN barangay b ON rl.barangayid = b.objid 
ORDER BY ${orderby} 


[getDelinquentLedgers2]
SELECT 
	e.name AS taxpayername,
	e.address_text AS taxpayeraddress,
	rl.administrator_name, 
	rl.fullpin AS pin,
	rl.tdno,
	rl.classcode,
	rl.cadastrallotno,
	rl.rputype,
	rl.totalav, 
	rl.totalareaha,
	rl.totalareaha * 10000 as totalareasqm,
	b.name AS barangay, b.objid, 
	x.endyear - x.startyear + 1 as numyears,
	x.* 
FROM ( 
	SELECT 
		rptledgerid, 
		MAX(dtgenerated) AS dtgenerated, 
		case when year = $P{year} then 'A. CURRENT' else 'B. PREVIOUS' end as revperiod, 
		MIN(year) AS startyear, 
		MAX(year) AS endyear,
		SUM(basic) AS basic, SUM(sef) AS sef, 
		SUM(basicint) AS basicint, SUM(sefint) AS sefint, 
		SUM(basicdisc) as basicdisc, SUM(sefdisc) as sefdisc,
		SUM(basic - basicdisc + basicint  + sef - sefdisc + sefint ) AS total
	FROM report_rptdelinquency r
	WHERE barangayid LIKE $P{barangayid} 
	AND NOT EXISTS(select * from rptledger_restriction where parentid = r.rptledgerid )
	${filter} 
	GROUP BY rptledgerid, case when year = $P{year} then 'A. CURRENT' else 'B. PREVIOUS' end
)x 
	INNER JOIN rptledger rl ON x.rptledgerid = rl.objid 
	INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
	INNER JOIN barangay b ON rl.barangayid = b.objid
ORDER BY b.name, x.revperiod, (x.endyear - x.startyear + 1) desc 



[getDelinquentLedgers3]
select 
	e.name as taxpayer_name, 
	t.* 
from (
	select 
		rd.dtgenerated,
		b.name as barangay, 
		rl.taxpayer_objid, 
		rl.objid as rptledgerid,
		rl.rputype, 
		rlf.tdno, 
		pc.code as classcode, 
		rlf.assessedvalue as av, 
		min(rlf.fromyear) as fromyear, 
		max(case when rlf.toyear = 0 then $P{toyear} else rlf.toyear end) as toyear,
		sum(basic) as basic, 
		sum(basicint) as basicint, 
		sum(basic + basicint) as basicnet,
		sum(sef) as sef, 
		sum(sefint) as sefint,
		sum(sef + sefint) as sefnet 
	from report_rptdelinquency rd 
		inner join rptledger rl on rd.rptledgerid = rl.objid 
		inner join barangay b on rl.barangayid = b.objid 
		inner join rptledgerfaas rlf on rd.rptledgerid = rlf.rptledgerid
		inner join propertyclassification pc on rlf.classification_objid = pc.objid 
	where rd.barangayid LIKE $P{barangayid} 
	 and not exists(select * from rptledger_restriction where parentid = rd.rptledgerid )
	 and rd.year >= rlf.fromyear 
	 and (rd.year <= rlf.toyear or rlf.toyear = 0 )
	 and rlf.state = 'APPROVED' 
	 ${filter} 
	group by 
		rd.dtgenerated,
		b.name, 
		rl.taxpayer_objid, 
		rl.objid,
		rl.rputype, 
		rlf.tdno, 
		pc.code,
		rlf.assessedvalue
) t 
	inner join entity e on t.taxpayer_objid = e.objid 
order by 
	e.name, t.fromyear 


[getDelinquentLedgersSummary]
SELECT 
	dtgenerated, barangayid, barangay_name, barangay_pin, 
	SUM(basic) as basic, SUM(sef) as sef, 
	SUM(basicint) AS basicint, SUM(sefint) AS sefint, 
	SUM(basicnet) AS basicnet, SUM(sefnet) AS sefnet, 
	SUM(total) AS total 
FROM ( 
	SELECT 
		barangayid, year, MAX(dtgenerated) AS dtgenerated, 
		SUM(basic - basicdisc) AS basic, SUM(sef - sefdisc) AS sef, 
		SUM(basicint) AS basicint, SUM(sefint) AS sefint, 
		SUM(basic - basicdisc + basicint) AS basicnet, 
		SUM(sef - sefdisc + sefint) AS sefnet, 
		SUM(basic - basicdisc + basicint  + sef - sefdisc + sefint ) AS total, 
		(select name from barangay where objid=rr.barangayid) as barangay_name, 
		(select pin from barangay where objid=rr.barangayid) as barangay_pin 
	FROM report_rptdelinquency rr 
	WHERE NOT EXISTS(select * from rptledger_restriction where parentid = rr.rptledgerid )
	GROUP BY barangayid, year  
)x 
WHERE year < $P{year} 
GROUP BY dtgenerated, barangayid, barangay_name, barangay_pin 
ORDER BY barangay_pin  


[getDelinquentLedgersByClassification]
SELECT 
	max(x.dtgenerated) as dtgenerated, 
	x.barangayid, 
	x.barangay_name,
	x.classification,
	x.idx,
	sum(amount) as amount 
FROM ( 
	SELECT 
		rr.barangayid, 
		(select name from barangay where objid=rr.barangayid) as barangay_name, 
		case when pc.special = 0 then pc.name else 'SPECIAL' end as classification,
		case when pc.special = 0 then pc.orderno else 1000 end as idx,
		year, dtgenerated,
		(basic - basicdisc + basicint  + sef - sefdisc + sefint) AS amount
	FROM report_rptdelinquency rr 
		inner join rptledger rl on rr.rptledgerid = rl.objid 
		inner join propertyclassification pc on rl.classification_objid = pc.objid 
	where NOT EXISTS(select * from rptledger_restriction where parentid = rr.rptledgerid )
)x 
WHERE year < $P{year} 
GROUP BY dtgenerated, barangayid, barangay_name, classification, idx 
ORDER BY barangay_name, idx 


[findLatestPayment]
select x.*
from (
	select c.receiptno, c.receiptdate
	from cashreceipt c 
		inner join cashreceiptitem_rpt_online cro on c.objid = cro.rptreceiptid
		left join cashreceipt_void cv on c.objid = cv.receiptid
	where cro.rptledgerid = $P{rptledgerid}
	and cv.objid is null 
	and c.receiptdate < $P{dtgenerated}

	union 

	select refno as receiptno, refdate as receiptdate
	from rptledger_credit 
	where rptledgerid = $P{rptledgerid}
	and refdate < $P{dtgenerated}
)x
order by x.receiptdate desc 