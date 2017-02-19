[getTopNPayments]
select distinct 
	amount
from (
	select 
		case when c.payer_name = 'UNKNOWN' then c.paidby else c.payer_objid end as payer_objid, 
		min(cro.year) as fromyear, 
		max(cro.year) as toyear, 
		sum(basic + basicint - basicdisc + 
		sef + sefint - sefdisc + firecode +
		basicidle + basicidleint - basicidledisc) as amount 
	from cashreceipt c
		inner join cashreceipt_rpt cr on c.objid = cr.objid 
		inner join cashreceiptitem_rpt_online cro on cr.objid = cro.rptreceiptid
		inner join rptledger rl on cro.rptledgerid = rl.objid 
		inner join remittance_cashreceipt rc on c.objid = rc.objid
		inner join liquidation_remittance lr on rc.remittanceid = lr.objid
		inner join liquidation l on lr.liquidationid = l.objid 
		left join cashreceipt_void cv on c.objid = cv.receiptid
	where year(l.dtposted) = $P{year}
		and rl.rputype like $P{type}
		and cv.objid is null 
	group by case when c.payer_name = 'UNKNOWN' then c.paidby else c.payer_objid end
)x
order by x.amount desc 


[getTopNTaxpayerPayments]
select 
	x.payer_objid,
	x.payer_name, 
	x.amount,
	x.fromyear,
	x.toyear
from (
	select 
		case when c.payer_name = 'UNKNOWN' then c.paidby else c.payer_objid end as payer_objid,
		case when c.payer_name = 'UNKNOWN' then c.paidby else c.payer_name end as payer_name,
		min(cro.year) as fromyear, 
		max(cro.year) as toyear, 
		sum(basic + basicint - basicdisc + 
		sef + sefint - sefdisc + firecode +
		basicidle + basicidleint - basicidledisc) as amount 
	from cashreceipt c
		inner join cashreceipt_rpt cr on c.objid = cr.objid
		inner join cashreceiptitem_rpt_online cro on cr.objid = cro.rptreceiptid
		inner join rptledger rl on cro.rptledgerid = rl.objid 
		inner join remittance_cashreceipt rc on c.objid = rc.objid
		inner join liquidation_remittance lr on rc.remittanceid = lr.objid
		inner join liquidation l on lr.liquidationid = l.objid 
		left join cashreceipt_void cv on c.objid = cv.receiptid
	where year(l.dtposted) = $P{year}
		and rl.rputype like $P{type}
		and cv.objid is null 
	group by 
		case when c.payer_name = 'UNKNOWN' then c.paidby else c.payer_objid end,
		case when c.payer_name = 'UNKNOWN' then c.paidby else c.payer_name end
)x
where x.amount = $P{amount}
order by x.payer_objid, x.payer_name



