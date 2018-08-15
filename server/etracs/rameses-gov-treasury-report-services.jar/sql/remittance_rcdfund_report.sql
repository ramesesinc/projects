[getCollectionTypes]
select 
	formtypeindex, controlid, formno, formtype, stubno, 
	min(series) as startseries, max(series) as endseries, 
	min(receiptno) as fromseries, max(receiptno) as toseries, 
	(sum(amount)-sum(voidamt)) as amount 
from ( 
	select tmp1.*, 
		(case when tmp1.voided > 0 then tmp1.amount else 0.0 end) as voidamt 
	from ( 
		select c.controlid, afc.afid as formno, af.formtype, afc.stubno,  
			(case when af.formtype = 'serial' then c.series else null end) as series, 
			(case when af.formtype = 'serial' then c.receiptno else null end) as receiptno, 
			(case when af.formtype = 'serial' then 1 else 2 end) as formtypeindex, bt1.amount, 
			(select count(*) from cashreceipt_void where receiptid=c.objid) as voided 
		from ( 
			select ci.receiptid, sum(ci.amount) as amount 
			from cashreceipt c 
				inner join cashreceiptitem ci on ci.receiptid = c.objid 
			where c.remittanceid = $P{remittanceid} 
				and ci.item_fund_objid = $P{fundid} 
			group by ci.receiptid 
		)bt1 
			inner join cashreceipt c on c.objid = bt1.receiptid 
			inner join af_control afc on afc.objid = c.controlid 
			inner join af on af.objid = afc.afid 
	)tmp1 
)tmp2
group by formtypeindex, controlid, formno, formtype, stubno 
order by formtypeindex, formno, min(receiptno) 


[getCollectionSummaries]
select 
	formindex, formno, collectiontypetitle, fundtitle, sum(amount) as amount 
from ( 
	select 
		(case when af.formtype = 'serial' then 1 else 2 end) as formindex, 
		c.formno, ct.title as collectiontypetitle, fund.title as fundtitle, ci.amount  
	from cashreceipt c 
		inner join collectiontype ct on ct.objid = c.collectiontype_objid 
		inner join cashreceiptitem ci on ci.receiptid = c.objid 
		inner join fund on fund.objid = ci.item_fund_objid 
		inner join af on af.objid = c.formno  
	where c.remittanceid = $P{remittanceid} 
		and ci.item_fund_objid = $P{fundid} 
		and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
)tmp1
group by formindex, formno, collectiontypetitle, fundtitle 
order by formindex, formno, collectiontypetitle 


[getOtherPayments]
select 
	pc.bank_name, nc.reftype, nc.particulars, 
	sum(nc.amount) as amount, min(nc.refdate) as refdate  
from cashreceipt c 
	inner join cashreceiptpayment_noncash nc on nc.receiptid = c.objid 
	left join checkpayment pc on (pc.objid = nc.refid and nc.reftype='CHECK') 
where c.remittanceid = $P{remittanceid} 
	and nc.fund_objid = $P{fundid} 
	and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
group by pc.bank_name, nc.reftype, nc.particulars 
order by pc.bank_name, min(nc.refdate), sum(nc.amount) 


[getRemittedAFs]
select tmp1.* 
from ( 
	select remaf.*, 
		af.formtype, afc.afid as formno, af.serieslength, af.denomination, afc.stubno, 
		afc.prefix, afc.suffix, afc.startseries, afc.endseries, afc.endseries+1 as nextseries, 
		(case when af.formtype = 'serial' then 0 else 1 end) as formindex 
	from remittance_af remaf 
		inner join af_control afc on afc.objid = remaf.controlid 
		inner join af on af.objid = afc.afid 
	where remaf.remittanceid = $P{remittanceid} 
)tmp1
order by tmp1.formindex, tmp1.formno, tmp1.startseries 
