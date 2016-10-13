[getTopNPayments]
select distinct 
	amount
from (
	select 
		c.payer_objid, 
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
	group by c.payer_objid
)x
order by x.amount desc 


[getTopNTaxpayerPayments]
select 
	x.payer_name, 
	x.tdno, x.totalav, 
	x.amount,
	x.fromyear,
	x.toyear
from (
	select 
		c.payer_objid, 
		c.payer_name,
		rl.tdno, rl.totalav,
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
	group by c.payer_objid, c.payer_name, rl.tdno, rl.totalav
)x
where x.amount = $P{amount}
order by x.payer_name, x.tdno