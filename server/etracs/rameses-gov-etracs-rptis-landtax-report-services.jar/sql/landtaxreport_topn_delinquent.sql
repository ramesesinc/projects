[getTopNDelinquentAmounts]
SELECT distinct 
	amount 
from (
	SELECT 
		taxpayer_objid,
		SUM(total) AS amount 
	FROM ( 
		SELECT 
			rl.taxpayer_objid,
			SUM(basic - basicdisc + basicint  + sef - sefdisc + sefint ) AS total
		FROM report_rptdelinquency rr 
			inner join rptledger rl on rr.rptledgerid = rl.objid 
		WHERE rr.year <= $P{year} 
			AND NOT EXISTS(select * from rptledger_restriction where parentid = rr.rptledgerid )
		GROUP BY rl.taxpayer_objid
	)x 
	GROUP BY taxpayer_objid
	order by amount desc
)t
order by amount desc


[getTopNDelinquentTaxpayers]
SELECT 
	taxpayer_name,
	tdno, 
	totalav,
	maxyear - minyear + 1 as yearsdelinquent,
	amount 
FROM ( 
	SELECT 
		rl.taxpayer_objid,
		e.name as taxpayer_name, 
		rl.tdno, 
		rl.totalav,
		min(year) as minyear,
		max(year) as maxyear, 
		SUM(basic - basicdisc + basicint  + sef - sefdisc + sefint ) AS amount 
	FROM report_rptdelinquency rr 
		inner join rptledger rl on rr.rptledgerid = rl.objid 
		inner join entity e on rl.taxpayer_objid = e.objid 
	WHERE NOT EXISTS(select * from rptledger_restriction where parentid = rr.rptledgerid )
	GROUP BY rl.taxpayer_objid, e.name, rl.tdno, rl.totalav 
)x 
where amount = $P{amount}
order by taxpayer_name