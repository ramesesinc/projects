[getBarangayList]
SELECT objid AS barangayid, name AS barangayname FROM barangay ORDER BY name

[getClassificationList]
SELECT objid AS classificationid, name AS classification FROM lobclassification ORDER BY name

[getLobListByAppid]
SELECT * FROM business_application_lob
WHERE businessid=$P{businessid} AND activeyear=$P{activeyear} 
ORDER BY name 

[getBPPaymentsByAppid]
SELECT p.refno, p.refdate, p.amount 
FROM business_application a 
	INNER JOIN business b ON a.business_objid=b.objid 
	INNER JOIN business_payment p ON a.objid=p.applicationid 
WHERE a.business_objid=$P{businessid} 
	AND a.appyear=$P{activeyear} 
	AND b.state NOT IN ('CANCELLED') 
	AND p.voided=0 
ORDER BY p.refno 

[getLobList]
SELECT classification_objid, name FROM lob
ORDER BY classification_objid, name

[getTaxpayerMasterList]
select xx.*, 
	(
		select max(permitno) from business_permit 
		where businessid=xx.objid and activeyear=xx.activeyear and version=xx.permitver 
	) as permitno 
from ( 
	select 
		b.objid, 
		(case when ba.apptype is null then b.apptype else ba.apptype end) as apptype, 
		b.activeyear, b.orgtype, b.tradename, baddr.barangay_name, b.address_text as businessaddress, 
		b.owner_name, b.owner_address_text as owner_address, 
		(
			select sum(decimalvalue) from business_application_info 
			where businessid=b.objid and activeyear=b.activeyear and attribute_objid='DECLARED_CAPITAL'
		) AS declaredcapital, 
		(
			select sum(decimalvalue) from business_application_info 
			where businessid=b.objid and activeyear=b.activeyear and attribute_objid='DECLARED_GROSS'
		) AS declaredgross, 
		(
			select sum(decimalvalue) from business_application_info 
			where businessid=b.objid and activeyear=b.activeyear and attribute_objid='CAPITAL'
		) AS capital, 
		(
			select sum(decimalvalue) from business_application_info 
			where businessid=b.objid and activeyear=b.activeyear and attribute_objid='GROSS'
		) AS gross, 
		(
			select max(version) from business_permit 
			where businessid=b.objid and activeyear=b.activeyear and state='ACTIVE' 
		) as permitver 
	from business_application ba 
		inner join business b on ba.business_objid=b.objid 
		left join business_address baddr on b.address_objid=baddr.objid 
	where ba.appyear=$P{year} ${filter} 
)xx 
order by xx.tradename 


[getLOBCountList]
select 
	a.appyear, lob.name, 
	SUM(CASE 
		WHEN a.apptype='NEW' THEN 1 
		WHEN a.apptype='RENEW' AND bal.assessmenttype='NEW' THEN 1 
		ELSE 0 
	END) as newcount, 
	SUM(CASE WHEN a.apptype='RENEW' AND bal.assessmenttype='RENEW' THEN 1 ELSE 0 END) AS renewcount,
	SUM(CASE WHEN a.apptype='ADDITIONAL' THEN 1 ELSE 0 END) AS addlobcount,
	SUM(CASE WHEN bal.assessmenttype='RETIRE' THEN 1 ELSE 0 END) AS retirecount	
from business_application a 
	inner join business_application_lob bal on a.objid=bal.applicationid 
	inner join lob on bal.lobid=lob.objid 
	inner join business b on a.business_objid=b.objid 
	left join business_address baddr on b.address_objid=baddr.objid 
where a.appyear=year($P{startdate}) 
	and a.dtfiled between $P{startdate} and $P{enddate}  
	and a.state='COMPLETED' ${filter} 
group by a.appyear, lob.name 


[getBusinessTopList]
select top ${topsize} 
	xx.businessid, xx.appyear, b.tradename, b.address_text as businessaddress, 
	b.owner_name as ownername, b.owner_address_text as owneraddress, xx.amount, 
	(
		select top 1 permitno from business_permit 
		where businessid=xx.businessid and activeyear=xx.appyear and state='ACTIVE' 
		order by version desc 
	) as permitno 
from ( 
	select  
		ba.business_objid as businessid, ba.appyear, sum(decimalvalue) as amount 
	from business_application ba 
		inner join business_application_info bai on bai.applicationid=ba.objid 
		inner join lob on lob.objid=bai.lob_objid 
		inner join business b on b.objid=ba.business_objid 
	where ba.appyear=$P{year} and ba.state in ('COMPLETED','RELEASE','PAYMENT') 
		and (select count(*) from business_payment where applicationid=ba.objid and voided=0)>0  
		and bai.attribute_objid in ('CAPITAL','GROSS')  
		${filter} 
	group by ba.business_objid, ba.appyear 
)xx 
	inner join business b on b.objid=xx.businessid 
order by xx.amount desc, b.tradename  


[getBusinessPermitSummary]
select 
	refyear as activeyear, refmonth as imonth, 
	case 
		when refmonth between 1 and 3 then 1 
		when refmonth between 4 and 6 then 2 
		when refmonth between 7 and 9 then 3 
		when refmonth between 10 and 12 then 4 
	end as iqtr,	
	case  
		when refmonth=1 then 'JANUARY'
		when refmonth=2 then 'FEBRUARY'
		when refmonth=3 then 'MARCH'
		when refmonth=4 then 'APRIL'
		when refmonth=5 then 'MAY'
		when refmonth=6 then 'JUNE'
		when refmonth=7 then 'JULY'
		when refmonth=8 then 'AUGUST'
		when refmonth=9 then 'SEPTEMBER'
		when refmonth=10 then 'OCTOBER'
		when refmonth=11 then 'NOVEMBER'
		when refmonth=12 then 'DECEMBER' 
	end as strmonth, 
	newcount, newamount, 
	renewcount, renewamount,
	retirecount, retireamount, total 
from ( 
	select 
		refyear, refmonth, 
		sum(newcount) as newcount, sum(newamount) as newamount, 
		sum(renewcount) as renewcount, sum(renewamount) as renewamount, 
		sum(retirecount) as retirecount, sum(retireamount) as retireamount,  
		sum(newamount + renewamount + retireamount) as total 
	from ( 
		select 
			(case when ba.parentapplicationid is null then ba.objid else ba.parentapplicationid end) as mainappid, 
			pay.applicationid, pay.businessid, pay.appyear as refyear, month(pay.refdate) as refmonth, pay.amount, 
			(case when ba.apptype IN ('NEW','ADDITIONAL') then 1 else 0 end) as newcount, 
			(case when ba.apptype IN ('NEW','ADDITIONAL') then pay.amount else 0.0 end) as newamount, 
			(case when ba.apptype IN ('RETIRE', 'RETIRELOB') then 1 else 0 end) as retirecount, 
			(case when ba.apptype IN ('RETIRE', 'RETIRELOB') then pay.amount else 0.0 end) as retireamount, 
			(case when ba.apptype = 'RENEW' then 1 else 0 end) as renewcount, 
			(case when ba.apptype = 'RENEW' then pay.amount else 0.0 end) as renewamount, 
			(
				select count(*) from business_permit 
				where businessid=pay.businessid and activeyear=pay.appyear and state='ACTIVE' 
			) as haspermit  
		from business_payment pay 
			inner join business_application ba on pay.applicationid=ba.objid 
			inner join business b on ba.business_objid=b.objid 
		where pay.appyear=$P{year} and pay.voided=0 
			and ba.state='COMPLETED' and b.permittype=$P{permittypeid} 
			${filter} 
	)tmp1 
	where haspermit > 0 
	group by refyear, refmonth 
)tmp2 
order by refyear, refmonth


[getBusinessPermitSummaryB]
select tmp2.*, 
case  
		when tmp2.imonth=1 then 'JANUARY'
		when tmp2.imonth=2 then 'FEBRUARY'
		when tmp2.imonth=3 then 'MARCH'
		when tmp2.imonth=4 then 'APRIL'
		when tmp2.imonth=5 then 'MAY'
		when tmp2.imonth=6 then 'JUNE'
		when tmp2.imonth=7 then 'JULY'
		when tmp2.imonth=8 then 'AUGUST'
		when tmp2.imonth=9 then 'SEPTEMBER'
		when tmp2.imonth=10 then 'OCTOBER'
		when tmp2.imonth=11 then 'NOVEMBER'
		when tmp2.imonth=12 then 'DECEMBER' 
	end as strmonth,
	case 
		when tmp2.imonth between 1 and 3 then 1 
		when tmp2.imonth between 4 and 6 then 2 
		when tmp2.imonth between 7 and 9 then 3 
		when tmp2.imonth between 10 and 12 then 4 
	end as iqtr 
from ( 
	select 
		refyear as activeyear, refmonth as imonth, 
		sum(amount) as sales, sum(capital) as capital, sum(gross) as gross, 
		sum(newcount) as newcount, sum(newamount) as newamount, 
		sum(renewcount) as renewcount, sum(renewamount) as renewamount, 
		sum(retirecount) as retirecount, sum(retireamount) as retireamount, 
		sum(tax) as tax, sum(fee) as fee, sum(othercharge) as othercharge 
	from ( 
			select 
				(case when ba.parentapplicationid is null then ba.objid else ba.parentapplicationid end) as mainappid, 
				pay.applicationid, pay.businessid, pay.appyear as refyear, month(pay.refdate) as refmonth, pay.amount, 
				(case when ba.apptype IN ('NEW','ADDITIONAL') then 1 else 0 end) as newcount, 
				(case when ba.apptype IN ('NEW','ADDITIONAL') then pay.amount else 0.0 end) as newamount, 
				(case when ba.apptype IN ('RETIRE', 'RETIRELOB') then 1 else 0 end) as retirecount, 
				(case when ba.apptype IN ('RETIRE', 'RETIRELOB') then pay.amount else 0.0 end) as retireamount, 
				(case when ba.apptype = 'RENEW' then 1 else 0 end) as renewcount, 
				(case when ba.apptype = 'RENEW' then pay.amount else 0.0 end) as renewamount, 
				(
					select count(*) from business_permit 
					where businessid=pay.businessid and activeyear=pay.appyear and state='ACTIVE' 
				) as haspermit, 
				(select sum(decimalvalue) from business_application_info where applicationid=ba.objid and attribute_objid='GROSS') as gross, 
				(select sum(decimalvalue) from business_application_info where applicationid=ba.objid and attribute_objid='CAPITAL') as capital, 
				( 
					select sum(bpi.amount) from business_payment_item bpi 
						inner join business_receivable br on bpi.receivableid=br.objid  
					where bpi.parentid=pay.objid 
						and br.taxfeetype='TAX' 
				) as tax, 
				( 
					select sum(bpi.amount) from business_payment_item bpi 
						inner join business_receivable br on bpi.receivableid=br.objid  
					where bpi.parentid=pay.objid 
						and br.taxfeetype='REGFEE' 
				) as fee, 
				( 
					select sum(bpi.amount) from business_payment_item bpi 
						inner join business_receivable br on bpi.receivableid=br.objid  
					where bpi.parentid=pay.objid 
						and br.taxfeetype not in ('TAX','REGFEE')
				) as othercharge  
			from business_payment pay 
				inner join business_application ba on pay.applicationid=ba.objid 
				inner join business b on ba.business_objid=b.objid 
			where pay.appyear=$P{year} and pay.voided=0 
				and ba.state='COMPLETED' and b.permittype=$P{permittypeid} 
				${filter} 
	)tmp1 
	where haspermit > 0 
	group by refyear, refmonth 
)tmp2 
order by activeyear, imonth 


[getQtrlyPaidBusinessList]
select 
	xx.bin, xx.businessname, xx.businessaddress, 
	sum(case when xx.q1 > 0.0 then xx.q1 else 0.0 end) as q1,
	sum(case when xx.q2 > 0.0 then xx.q2 else 0.0 end) as q2,
	sum(case when xx.q3 > 0.0 then xx.q3 else 0.0 end) as q3, 
	sum(case when xx.q4 > 0.0 then xx.q4 else 0.0 end) as q4, 
	sum(xx.balance) as balance 
from ( 
	select 
		b.bin, b.businessname, b.address_text AS businessaddress, a.objid as applicationid, 
		(select sum(amount) from business_payment where applicationid=a.objid and DATEPART(QUARTER,refdate)=1 and voided=0 group by applicationid) AS q1,
		(select sum(amount) from business_payment where applicationid=a.objid and DATEPART(QUARTER,refdate)=2 and voided=0 group by applicationid) AS q2,
		(select sum(amount) from business_payment where applicationid=a.objid and DATEPART(QUARTER,refdate)=3 and voided=0 group by applicationid) AS q3,
		(select sum(amount) from business_payment where applicationid=a.objid and DATEPART(QUARTER,refdate)=4 and voided=0 group by applicationid) AS q4, 
		( 
			select 
				sum(case when amount=amtpaid then 0.0 else amount-(amtpaid+discount) end)  
			from business_receivable 
			where applicationid=a.objid 
		) as balance 
	from business_application a 
		inner join business b on a.business_objid=b.objid 
	where a.appyear=$P{year} ${filter} 
)xx 
group by xx.bin, xx.businessname, xx.businessaddress 
having sum(ISNULL(xx.q1,0)+ISNULL(xx.q2,0)+ISNULL(xx.q3,0)+ISNULL(xx.q4,0)) > 0 
order by xx.bin 


[getEmployerList]
select 
	b.objid, b.bin, b.tradename, b.address_text as businessaddress, b.owner_name, b.owner_objid, 
	tmp2.numfemale, tmp2.nummale, tmp2.numresident, tmp2.numemployee, 
	(case when ei.gender='M' then 1 else 0 end) as malecount,
	(case when ei.gender='F' then 1 else 0 end) as femalecount,  
	case 
		when b.orgtype='SING' then (select tin from entityindividual WHERE objid=b.owner_objid) 
		else (select tin FROM entityjuridical WHERE objid=b.owner_objid) 
	end as tin, '' as sss, 
	case 
		when b.state='ACTIVE' then (
			select top 1 permitno from business_permit 
			where businessid=b.objid and activeyear=tmp2.appyear and state='ACTIVE' 
			order by version desc 
		) 
		else null 
	end as permitno 
from ( 
	select 
		businessid, appyear, 
		isnull(sum(nummale),0) as nummale, 
		isnull(sum(numfemale),0) as numfemale, 
		isnull(sum(numresident),0) as numresident,  
		(isnull(sum(nummale),0) + isnull(sum(numfemale),0)) as numemployee 
	from ( 
		select 
			ba.business_objid as businessid, ba.appyear, 
			(select sum(intvalue) from business_application_info where applicationid=ba.objid and attribute_objid='NUM_EMPLOYEE_MALE') as nummale, 
			(select sum(intvalue) from business_application_info where applicationid=ba.objid and attribute_objid='NUM_EMPLOYEE_FEMALE') as numfemale, 
			(select sum(intvalue) from business_application_info where applicationid=ba.objid and attribute_objid='NUM_EMPLOYEE_RESIDENT') as numresident 
		from business_application ba 
			inner join business b on ba.business_objid=b.objid 
		where ba.appyear = $P{year} 
			and ba.apptype in ( ${apptypefilter} ) 
			and ba.state in ( ${appstatefilter} ) 
			and b.permittype = $P{permittypeid} 
	)tmp1 
	group by businessid, appyear 
)tmp2 
	inner join business b on b.objid=tmp2.businessid 
	left join entityindividual ei on b.owner_objid=ei.objid 
order by tradename 


[getBusinessesByBarangay]
SELECT 
	b.objid as businessid, b.owner_objid, b.owner_name, b.owner_address_text as owner_address,
	b.tradename, b.address_text as businessaddress, a.appno, a.apptype, a.appyear
FROM business b
INNER JOIN business_application a ON a.business_objid = b.objid
LEFT JOIN business_address ba ON b.address_objid=ba.objid
WHERE 
  ba.barangay_objid LIKE $P{barangayid}
ORDER BY a.appyear, b.owner_name


[getBPCollectionSummary]
select tmp2.*, 
	case 
		when imonth between 1 and 3 then 1 
		when imonth between 4 and 6 then 2 
		when imonth between 7 and 9 then 3 
		when imonth between 10 and 12 then 4 
	end as iqtr,	
	case  
		when imonth=1 then 'JANUARY'
		when imonth=2 then 'FEBRUARY'
		when imonth=3 then 'MARCH'
		when imonth=4 then 'APRIL'
		when imonth=5 then 'MAY'
		when imonth=6 then 'JUNE'
		when imonth=7 then 'JULY'
		when imonth=8 then 'AUGUST'
		when imonth=9 then 'SEPTEMBER'
		when imonth=10 then 'OCTOBER'
		when imonth=11 then 'NOVEMBER'
		when imonth=12 then 'DECEMBER' 
	end as strmonth 
from ( 
	select 
		refyear as activeyear, refmonth as imonth, 
		sum(newcount) as newcount, sum(newamount) as newamount, 
		sum(renewcount) as renewcount, sum(renewamount) as renewamount, 
		sum(retirecount) as retirecount, sum(retireamount) as retireamount,  
		sum(newamount + renewamount + retireamount) as total 
	from ( 
		select 
			(case when ba.parentapplicationid is null then ba.objid else ba.parentapplicationid end) as mainappid, 
			pay.applicationid, pay.businessid, pay.appyear as refyear, month(pay.refdate) as refmonth, pay.amount, 
			(case when ba.apptype IN ('NEW','ADDITIONAL') then 1 else 0 end) as newcount, 
			(case when ba.apptype IN ('NEW','ADDITIONAL') then pay.amount else 0.0 end) as newamount, 
			(case when ba.apptype IN ('RETIRE', 'RETIRELOB') then 1 else 0 end) as retirecount, 
			(case when ba.apptype IN ('RETIRE', 'RETIRELOB') then pay.amount else 0.0 end) as retireamount, 
			(case when ba.apptype = 'RENEW' then 1 else 0 end) as renewcount, 
			(case when ba.apptype = 'RENEW' then pay.amount else 0.0 end) as renewamount 
		from business_payment pay 
			inner join business_application ba on pay.applicationid=ba.objid 
			inner join business b on ba.business_objid=b.objid 
		where pay.appyear=$P{year} and pay.voided=0 
			and ba.state in ('COMPLETED','RELEASE','PAYMENT') 
			and b.permittype=$P{permittypeid} 
			${filter} 
	)tmp1 
	group by refyear, refmonth 
)tmp2 
order by activeyear, imonth


[getBPTaxFeeTopList]
select top ${topsize} 
	businessid, appyear, tradename, businessaddress, ownername, owneraddress, 
	sum(tax) as tax, sum(regfee) as regfee, sum(othercharge) as othercharge,
	sum(tax + regfee + othercharge) as total, 
	(
		select top 1 permitno from business_permit 
		where businessid=xx.businessid and activeyear=xx.appyear and state='ACTIVE' 
		order by version desc 
	) as permitno 
from ( 
	select 
		ba.business_objid as businessid, ba.appyear, ba.tradename, 
		ba.businessaddress, ba.ownername, ba.owneraddress, 
		(case when br.taxfeetype='TAX' then br.amount else 0.0 end) as tax, 
		(case when br.taxfeetype='REGFEE' then br.amount else 0.0 end) as regfee,
		(case when br.taxfeetype='OTHERCHARGE' then br.amount else 0.0 end) as othercharge 
	from business_application ba  
		inner join business b on ba.business_objid=b.objid 
		inner join business_receivable br on br.applicationid=ba.objid 
	where ba.appyear=$P{year} and ba.state in ('RELEASE','COMPLETED')  
		and br.iyear=ba.appyear ${filter} 
)xx 
group by businessid, appyear, tradename, businessaddress, ownername, owneraddress 
order by sum(tax + regfee + othercharge) desc, tradename 
