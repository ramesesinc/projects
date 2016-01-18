[getReportListing]
select 
	fundtitle, formno, controlid, series, receiptno, receiptdate, amount, payorname, 
	itemid, itemcode, itemtitle, sum(itemamount) as itemamount, 
	collector_objid, collector_name, fundsortorder  
from ( 
	select 
		cr.formno, cr.controlid, cr.series, cr.receiptno, cr.receiptdate, cr.amount, 
		case 
			when xx.voided <> 0 then '***VOIDED***' 
			when cr.payer_name is null then cr.paidby 
			else cr.payer_name 
		end as payorname, 
		case when xx.voided=0 then cri.item_objid else '***VOIDED***' end as itemid, 
		case when xx.voided=0 then cri.item_code else '' end as itemcode,
		case when xx.voided=0 then cri.item_title else '***VOIDED***' end as itemtitle,
		case when xx.voided=0 then cri.amount else 0.00 end as itemamount,
		ia.fund_title as fundtitle, 
		case 
			when ia.fund_objid='GENERAL' then 1 
			when ia.fund_objid='SEF' then 2 
			when ia.fund_objid='TRUST' then 3 
			else 4 
		end as fundsortorder, 
		cr.collector_objid, cr.collector_name 
	from ( 
		select objid, 
			(select count(*) from cashreceipt_void where receiptid=a.objid) as voided 
		from cashreceipt a 
		where receiptdate between $P{startdate} and $P{enddate} 
			and collector_objid like $P{collectorid} 
			and objid in (select objid from remittance_cashreceipt where objid=a.objid) 
	)xx inner join cashreceipt cr on xx.objid=cr.objid 
		inner join cashreceiptitem cri on cr.objid=cri.receiptid 
		left join itemaccount ia on cri.item_objid = ia.objid 
)xx 
group by 
	formno, controlid, series, receiptno, receiptdate, amount, payorname, 
	itemid, itemcode, itemtitle, collector_objid, collector_name, fundtitle, fundsortorder 
order by 
	collector_name, formno, fundsortorder, fundtitle, receiptdate, series, itemcode   
