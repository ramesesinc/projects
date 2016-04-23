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
  AND NOT EXISTS(select * from rptledger_restriction where parentid = rl.objid )
  

[cleanup]
DELETE FROM report_rptdelinquency 
WHERE barangayid LIKE $P{barangayid} 


[getDelinquentLedgers]
SELECT 
	e.name AS taxpayername,
	e.address_text AS taxpayeraddress,
	rl.fullpin AS pin,
	rl.tdno,
	rl.classcode,
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
		MAX(year) AS lastyearpaid, MAX(qtr) AS lastqtrpaid,  
		SUM(basic - basicdisc) AS basic, SUM(sef - sefdisc) AS sef, 
		SUM(basicint) AS basicint, SUM(sefint) AS sefint, 
		SUM(basic - basicdisc + basicint) AS basicnet,
		SUM(sef - sefdisc + sefint) AS sefnet,
		SUM(basic - basicdisc + basicint  + sef - sefdisc + sefint ) AS total
	FROM report_rptdelinquency 
	WHERE barangayid LIKE $P{barangayid} ${filter} 
	GROUP BY rptledgerid 
)x 
	INNER JOIN rptledger rl ON x.rptledgerid = rl.objid 
	INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
	INNER JOIN barangay b ON rl.barangayid = b.objid 
ORDER BY ${orderby} 


[getDelinquentLedgersSummary]
SELECT 
	dtgenerated, barangayid, barangay_name, barangay_pin, 
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
	GROUP BY barangayid, year  
)x 
WHERE year < $P{year} 
GROUP BY dtgenerated, barangayid, barangay_name, barangay_pin 
ORDER BY barangay_pin  
