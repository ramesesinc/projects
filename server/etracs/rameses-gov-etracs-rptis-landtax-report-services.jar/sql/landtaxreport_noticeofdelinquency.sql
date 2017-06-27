[getDelinquentLedgers]
SELECT
	t.totalav,
	t.fromyear, 
	t.fromqtr,
	t.toyear,
	t.toqtr, 
	rl.tdno,
	rl.rputype,
	rl.fullpin,
	rl.cadastrallotno, 
	rl.totalareaha,
	rl.totalareaha * 10000.0 AS totalareasqm,
	pc.code AS classcode, 
	b.name AS barangay,
	SUM(t.basic) as basic, 
	SUM(t.basicdisc) as basicdisc, 
	SUM(t.basicint) as basicint, 
	SUM(t.basicidle) as basicidle, 
	SUM(t.basicidledisc) as basicidledisc, 
	SUM(t.basicidleint) as basicint, 
	SUM(t.sef) as sef, 
	SUM(t.sefdisc) as sefdisc, 
	SUM(t.sefint) as sefint, 
	SUM(t.firecode) as firecode, 
	SUM(t.amtdue) AS amtdue
FROM rptledger rl 
	INNER JOIN propertyclassification pc ON rl.classification_objid = pc.objid 
	INNER JOIN barangay b ON rl.barangayid = b.objid,
	(
		SELECT 
			x.*,
			(SELECT MIN(qtr) FROM rptbill_ledger_item WHERE rptledgerid = x.objid AND year = x.fromyear) AS fromqtr,
			(SELECT MAX(qtr) FROM rptbill_ledger_item WHERE rptledgerid = x.objid AND year = x.fromyear) AS toqtr
		FROM (
			SELECT
				rl.objid,
				rlf.assessedvalue as totalav, 
				MIN(bi.year) AS  fromyear,
				MAX(bi.year) AS toyear,
				SUM(bi.basic) as basic, 
				SUM(bi.basicdisc) as basicdisc, 
				SUM(bi.basicint) as basicint, 
				SUM(bi.basicidle) as basicidle, 
				SUM(bi.basicidledisc) as basicidledisc, 
				SUM(bi.basicidleint) as basicidleint, 
				SUM(bi.sef) as sef, 
				SUM(bi.sefdisc) as sefdisc, 
				SUM(bi.sefint) as sefint, 
				SUM(bi.firecode) as firecode, 
				SUM(bi.basic + bi.basicint - bi.basicdisc + bi.sef + bi.sefint - bi.sefdisc + bi.firecode + bi.basicidle - bi.basicidledisc + bi.basicidleint ) AS amtdue
			FROM rptledger rl
				INNER JOIN rptbill_ledger_item bi ON rl.objid = bi.rptledgerid
				INNER JOIN rptledgerfaas rlf ON bi.rptledgerfaasid = rlf.objid 
		WHERE ${filters}
			  AND bi.billid = $P{billid}
			  AND rl.state = 'APPROVED'
			  AND bi.year <= $P{cy}
			GROUP BY rl.objid, rlf.assessedvalue
		)x	
	) t
WHERE rl.objid = t.objid
GROUP BY 
	t.objid,
	t.totalav,
	t.fromyear,
	t.fromqtr,
	t.toyear,
	t.toqtr,
	t.amtdue,
	rl.objid,
	rl.tdno,
	rl.rputype,
	rl.fullpin,
	rl.cadastrallotno, 
	rl.totalareaha,
	pc.code, 
	b.name
ORDER BY t.fromyear, t.toyear 


[findBillByTaxpayer]
select objid from rptbill where taxpayer_objid = $P{taxpayerid} order by barcode desc 


[findBillByLedgerId]
select distinct b.objid, b.barcode from rptbill b
	inner join rptbill_ledger bl on b.objid = bl.billid
where bl.rptledgerid =  $P{rptledgerid} 
order by b.barcode desc 
