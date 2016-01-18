[findPrevious]
select * 
from previousib 
where taxdecnum = $P{taxdecnum}


[getFaasHistories]
select 
	p.taxdecnum as objid,
	'PENDING' as state,
	'' as rptledgerid,
	null as faasid,
	p.taxdecnum as tdno,
	'GR' as txntype_objid,
	null as classification_objid,
	null as actualuse_objid,
	1 as taxable,
	0 as backtax,
	(select distinct year(effectivitydate) from tdmainib where taxdecnum = $P{taxdecnum} ) as fromyear,
	1 as fromqtr,
	max(y.toyear) as toyear,
	4 as toqtr, 
	( ifnull(p.assessedvalue,0) + ifnull(p.improvement,0) ) as assessedvalue,
	0 as systemcreated,
	0 as reclassed,
	0 as idleland
from previousib p
	inner join tdcodeyear y  on substring(p.taxdecnum,1,1) = y.tdcode
where p.taxdecnum = $P{taxdecnum}
group by p.taxdecnum 
order by p.taxdecnum desc 


[getPayments]
select
	min(p.oid) as objid,  
	'CAPTURE' AS type,
	r.ornumber as refno,
	r.ordate as refdate,
	null as payorid,
	r.payor as paidby_name,
	'-' as paidby_address,
	r.collectorsname as collector,
	'system' as postedby,
	'system' as postedbytitle,
	r.ordate as dtposted,
	p.fromyear as fromyear,
	p.fromqtr,
	p.toyear as toyear,
	p.toqtr, 
	sum(case when p.taxcode = 'B' then p.principal else 0 end) as basic,
	sum(case when p.taxcode = 'B' and p.penaltydiscount >= 0 then p.penaltydiscount else 0 end) as basicint,
	sum(case when p.taxcode = 'B' and p.penaltydiscount < 0 then p.penaltydiscount else 0 end) as basicdisc,
	0.0 as basicidle,
	sum(case when p.taxcode = 'S' then p.principal else 0 end) as sef,
	sum(case when p.taxcode = 'S' and p.penaltydiscount >= 0 then p.penaltydiscount else 0 end) as sefint,
	sum(case when p.taxcode = 'S' and p.penaltydiscount < 0 then p.penaltydiscount else 0 end) as sefdisc,
	0.0 as sefidle,
	0.0 as firecode,
	sum(p.principal + p.penaltydiscount) as amount,
	'MTO' as collectingagency
from ormainib r, orpymtib p
where r.ornumber = p.ornumber
  and p.taxdecnum = $P{taxdecnum}
group by 
	r.ornumber,
	r.ordate,
	r.payor,
	r.collectorsname,
	r.ordate,
	p.fromyear,
	p.toyear
order by p.fromyear, p.fromqtr 	