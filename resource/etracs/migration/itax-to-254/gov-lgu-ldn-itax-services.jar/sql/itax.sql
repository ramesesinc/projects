[findPrevious]
select 
	p.taxtrans_id as objid,
	c.taxtrans_id as rptledgerid, 
	p.taxtrans_id as previd, 
	c.cancelledtdno as tdno,
	c.prevassessedvalue as assessedvalue,
	c.prevstartyear as fromyear,
	curr.startyear - 1 as toyear
from rptcancelled c, rptassessment curr, rptassessment p
where c.cancelledtdno = p.tdno 
and c.taxtrans_id = curr.taxtrans_id 
and c.taxtrans_id = $P{taxtransid}


[getPayments]
select
	min(pd.payclassdetail_id) as objid,  
	'CAPTURE' AS type,
	p.receiptno as refno,
	p.paymentdate as refdate,
	null as payorid,
	p.paidby as paidby_name,
	'-' as paidby_address,
	p.userid as collector,
	'system' as postedby,
	'system' as postedbytitle,
	p.paymentdate as dtposted,
	min(pd.taxyear) as fromyear,
	1 as fromqtr,
	max(pd.taxyear) as toyear,
	4 as toqtr,
	sum(case when pd.itaxtype_ct= 'BSC' and pd.casetype_ct = 'REG' then pd.amount else 0 end) as basic,
	sum(case when pd.itaxtype_ct= 'BSC' and pd.casetype_ct = 'PEN' then pd.amount else 0 end) as basicint,
	sum(case when pd.itaxtype_ct= 'BSC' and pd.casetype_ct = 'DED' then pd.amount else 0 end) as basicdisc,
	0.0 as basicidle,
	sum(case when pd.itaxtype_ct= 'SEF' and pd.casetype_ct = 'REG' then pd.amount else 0 end) as sef,
	sum(case when pd.itaxtype_ct= 'SEF' and pd.casetype_ct = 'PEN' then pd.amount else 0 end) as sefint,
	sum(case when pd.itaxtype_ct= 'SEF' and pd.casetype_ct = 'DED' then pd.amount else 0 end) as sefdisc,
	0.0 as sefidle,
	0.0 as firecode,
	sum(pd.amount) as amount,
	'MTO' as collectingagency
from paymentclassdetail pd, payment p
where p.payment_id = pd.payment_id 
	and pd.taxtrans_id = $P{taxtransid}
group by 
	p.receiptno,
	p.paymentdate,
	p.paidby,
	p.userid
	