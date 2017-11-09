[getPaymentInfo]
select * 
from (
	SELECT 
		rl.tdno, 
		1 as idx, 
		'BASIC' as particulars,
		xr.receiptno AS orno,
		xr.txndate AS ordate,
		SUM(ri.basic + ri.basicint - ri.basicdisc) AS oramount,
		CASE WHEN (MIN(ri.qtr) = 1 AND MAX(ri.qtr) = 4) OR ((MIN(ri.qtr) = 0 AND MAX(ri.qtr) = 0))
			THEN  'FULL'
			ELSE MIN(ri.qtr) + 'Q'+ ' - ' + MAX(ri.qtr) + 'Q'
		END AS term,
		ri.year as taxyear 
	FROM rptcertificationitem rci 
		INNER JOIN rptledger rl ON rci.refid = rl.objid 
		INNER JOIN cashreceiptitem_rpt_online  ri ON rl.objid = ri.rptledgerid
		INNER JOIN cashreceipt xr ON ri.rptreceiptid = xr.objid 
		LEFT JOIN cashreceipt_void cv ON xr.objid = cv.receiptid  
	WHERE rci.rptcertificationid = $P{rptcertificationid}
		AND rl.objid = $P{rptledgerid}
	  AND (ri.year = $P{year} AND ri.qtr <= $P{qtr}) 
	  AND cv.objid IS NULL 
	GROUP BY rl.tdno, xr.receiptno, xr.txndate, ri.year

	UNION ALL

	SELECT 
		rl.tdno, 
		2 as idx, 
		'SEF' as particulars,
		xr.receiptno AS orno,
		xr.txndate AS ordate,
		SUM(ri.sef + ri.sefint - ri.sefdisc) AS oramount,
		CASE WHEN (MIN(ri.qtr) = 1 AND MAX(ri.qtr) = 4) OR ((MIN(ri.qtr) = 0 AND MAX(ri.qtr) = 0))
			THEN  'FULL'
			ELSE MIN(ri.qtr) + 'Q'+ ' - ' + MAX(ri.qtr) + 'Q'
		END AS term,
		ri.year as taxyear 
	FROM rptcertificationitem rci 
		INNER JOIN rptledger rl ON rci.refid = rl.objid 
		INNER JOIN cashreceiptitem_rpt_online  ri ON rl.objid = ri.rptledgerid
		INNER JOIN cashreceipt xr ON ri.rptreceiptid = xr.objid 
		LEFT JOIN cashreceipt_void cv ON xr.objid = cv.receiptid  
	WHERE rci.rptcertificationid = $P{rptcertificationid}
		AND rl.objid = $P{rptledgerid}
	  AND (ri.year = $P{year} AND ri.qtr <= $P{qtr}) 
	  AND cv.objid IS NULL 
	GROUP BY rl.tdno, xr.receiptno, xr.txndate, ri.year

) x 
order by x.tdno, x.idx