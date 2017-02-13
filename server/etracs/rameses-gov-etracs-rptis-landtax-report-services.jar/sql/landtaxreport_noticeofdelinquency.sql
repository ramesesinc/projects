[getDelinquentLedgers]
SELECT
	t.*,
	rl.tdno,
	rl.rputype,
	rl.fullpin,
	rl.cadastrallotno, 
	rl.totalareaha,
	rl.totalareaha * 10000.0 AS totalareasqm,
	rl.totalav,
	rl.administrator_name,
	pc.code AS classcode, 
	b.name AS barangay,
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
				MIN(bi.year) AS  fromyear,
				MAX(bi.year) AS toyear,
				SUM(bi.basic + bi.basicint - bi.basicdisc + bi.sef + bi.sefint - bi.sefdisc + bi.firecode + bi.basicidle - bi.basicidledisc + bi.basicidleint ) AS amtdue
			FROM rptledger rl
				INNER JOIN rptbill_ledger_item bi ON rl.objid = bi.rptledgerid
			WHERE ${filters}
			  AND bi.billid = $P{billid}
			  AND rl.state = 'APPROVED'
			  AND bi.year <= $P{cy}
			GROUP BY rl.objid
		)x	
	) t
WHERE rl.objid = t.objid
GROUP BY 
	t.objid,
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
	rl.totalav,
	rl.administrator_name,
	pc.code, 
	b.name
ORDER BY rl.tdno 



[findBillByTaxpayer]
select objid from rptbill where taxpayer_objid = $P{taxpayerid} order by barcode desc 


[findBillByLedgerId]
select distinct b.objid, b.barcode from rptbill b
	inner join rptbill_ledger bl on b.objid = bl.billid
where bl.rptledgerid =  $P{rptledgerid} 
order by b.barcode desc 
