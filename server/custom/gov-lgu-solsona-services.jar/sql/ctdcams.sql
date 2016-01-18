[findDelinquency]
select `last payment` as lastyearpaid 
from delinquents  
where `pin no` = $P{pinno}


[getFaasHistories]
select * 
from assessments a
	inner join assess_spans arv on a.aid = arv.aid
where a.pinno = $P{pinno}
and fromyear <= 2014;

[getPayments]
select
	acct.acctid, 
	'CAPTURE' AS type,
	p.orno as refno,
	p.date as refdate,
	null as payorid,
	concat(ifnull(o.lastname,''), ', ', ifnull(o.firstname,''), ' ', ifnull(o.middle,'')) as paidby_name,
	o.address as paidby_address,
	'-' as collector,
	'system' as postedby,
	'system' as postedbytitle,
	pd.dateposted as dtposted,
	acct.taxyear as fromyear,
	acct.taxyear as toyear,
	acct.basic,
	case when acct.discpen > 0 then round(acct.discpen / 2) else 0 end basicint,
	case when acct.discpen <= 0 then round(acct.discpen / 2) else 0 end basicdisc,
	0.0 as basicidle,
	acct.sef,
	case when acct.discpen > 0 then acct.discpen - round(acct.discpen / 2) else 0 end sefint,
	case when acct.discpen <= 0 then acct.discpen -  round(acct.discpen / 2) else 0 end sefdisc,
	0.0 as sefidle,
	0.0 as firecode,
	acct.total as amount,
	null as collectingagency
from assessments a 
	inner join accounts acct on a.avid = acct.avid 
	inner join payaccountdetails pd on acct.acctid = pd.acctid 
	inner join payments p on pd.payacctid = p.payacctid 
	inner join owners o on p.payeeid = o.ownerid
where a.pinno = $P{pinno}
order by acct.taxyear 