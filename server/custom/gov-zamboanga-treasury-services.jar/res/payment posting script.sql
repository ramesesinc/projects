drop view vw_etracs_payment_detail
go 

create view vw_etracs_payment_detail
as 
select 
	i.tdn,
	null as receiptid,
	a.orno as receiptno,
	a.ordate as receiptdate,
	isnull(a.payee, a.paidby) as paidby_name,
	'-' as paidby_address,
	isnull(a.usercode, 'system') as postedby,
	isnull(a.usercode, 'system') as postedbytitle,
	a.ordate as dtposted,
	b.calendaryear as year, 
	b.calendaryear as fromyear,
	case 
		when b.qtrind in (1, 5, 6, 10, 15) then 1
		when b.qtrind in (2, 7, 8) then 2
		when b.qtrind in ( 3, 9) then 3
		when b.qtrind = 4 then 4
		else 1 
	end as fromqtr,
	b.calendaryear as toyear,
	case 
		when b.qtrind in (1) then 1
		when b.qtrind in (2, 5) then 2
		when b.qtrind in (3, 6, 7) then 3
		when b.qtrind in (4, 8, 9, 10, 15) then 4
		else 1 
	end as toqtr,
	basic,
	basicpen,
	(discount / 2) as basicdisc,
	sef,
	sefpen,
	(discount / 2) as sefdisc,
	idle,
	idlepen,
	discount,
	basic + basicpen + sef + sefpen + idle + idlepen - discount as amount,
	'cto' as collectingagency,
	0 as voided,
	case 
		when b.calendaryear < year(a.ordate) then 0
		when b.calendaryear = year(a.ordate) then 1
		else 2
	end as revperiod
from LANDTAX..af56ora a 
inner join LANDTAX..af56orb b on a.orno = b.orno 
inner join landtax..tdinfo i on b.tdn = i.tdn 
where a.datecanceled is null 
go 

create index ix_tdn on LANDTAX..tdinfo(tdn)
go 



create table zz_etracs_afor56_forposting (
	orno varchar(25) not null,
	ordate datetime not null,
	tdn varchar(25) not null 
)
go 

create index ix_orno on zz_etracs_afor56_forposting(orno)
go 

create index ix_tdn on zz_etracs_afor56_forposting(tdn)
go 


create table zz_etracs_afor56_posted (
	orno varchar(25) not null,
	ordate datetime not null,
	tdn varchar(25) not null,
	remarks text
)
go 

create index ix_orno on zz_etracs_afor56_posted(orno)
go 

create index ix_tdn on zz_etracs_afor56_posted(tdn)
go 




insert into zz_etracs_afor56_forposting(orno, ordate, tdn)
select distinct a.orno, a.ordate, b.tdn, b.calendaryear
from AF56ORA a  
inner join AF56ORB b on a.orno = b.orno 
where a.ordate >= '2019-11-29' and a.ordate <= '2019-11-30'
and a.orstatus <> 'c'
order by a.ordate, a.orno, b.calendaryear
go 