[getCollectionTypes]
select 
	v.formtypeindexno, v.formno, v.formtype, v.controlid, v.stubno, 
	min(v.series) as minseries, max(v.series) as maxseries, 
	case when v.formtype = 'serial' then min(v.receiptno) else null end as fromseries, 
	case when v.formtype = 'serial' then max(v.receiptno) else null end as toseries, 
	sum(v.amount)-sum(v.voidamount) as amount 
from vw_remittance_cashreceipt v
where v.remittanceid = $P{remittanceid} 
group by v.formtypeindexno, v.formno, v.formtype, v.controlid, v.stubno 
order by v.formtypeindexno, v.formno, min(v.series) 


[getCollectionSummaries]
select 
	v.formtypeindex as formindex, v.formno, 
	v.collectiontype_objid, v.collectiontype_name as collectiontypetitle,
	v.fundid, fund.title as fundtitle, sum(v.amount) as amount 
from vw_remittance_cashreceiptitem v 
	inner join fund on fund.objid = v.fundid 
where v.remittanceid = $P{remittanceid} 
group by 
	v.formtypeindex, v.formno, v.collectiontype_objid, 
	v.collectiontype_name, v.fundid, fund.title 
order by v.formtypeindex, v.formno, v.collectiontype_name 


[getOtherPayments]
select * from ( 
	select 
		bankid, bank_name, reftype, particulars, 
		sum(amount)-sum(voidamount) as amount, min(refdate) as refdate 
	from vw_remittance_cashreceiptpayment_noncash 
	where remittanceid = $P{remittanceid} 
	group by bankid, bank_name, reftype, particulars 
)t1 
where amount > 0 
order by bank_name, refdate, amount 


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
