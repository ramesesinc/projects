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
		) AS capital,
		(
			select sum(decimalvalue) from business_application_info 
			where businessid=b.objid and activeyear=b.activeyear and attribute_objid='DECLARED_GROSS'
		) AS gross, 
		(
			select max(version) from business_permit 
			where businessid=b.objid and activeyear=b.activeyear
		) as permitver 
	from business b 
		left join business_application ba on b.currentapplicationid=ba.objid 
		left join business_address baddr on b.address_objid=baddr.objid 
	where b.activeyear=$P{year} ${filter} 
)xx 
order by xx.tradename 


[getPermitListByYear]
SELECT 
	bp.*, b.owner_name, b.owner_address_text as owner_address, 
	b.tradename, b.objid as businessid 
FROM ( 
	SELECT businessid, activeyear, MAX(version) AS version 
	FROM business_permit WHERE activeyear=$P{year}  
	GROUP BY businessid, activeyear 
)xx  
	INNER JOIN business_permit bp ON xx.businessid=bp.businessid 
	INNER JOIN business b ON bp.businessid=b.objid 
	LEFT JOIN business_address addr ON b.address_objid=addr.objid  
WHERE bp.activeyear=xx.activeyear and bp.version=xx.version  
	and ISNULL(addr.barangay_objid,'') LIKE $P{barangayid} 
	${filter} 
ORDER BY b.owner_name 


[getLOBCountList]
select 
	a.appyear, lob.name, 
	SUM(CASE 
		WHEN a.apptype='NEW' THEN 1 
		WHEN a.apptype='RENEW' AND bal.assessmenttype='NEW' THEN 1 
		ELSE 0 
	END) as newcount, 
	SUM(CASE WHEN a.apptype='RENEW' AND bal.assessmenttype='RENEW' THEN 1 ELSE 0 END) AS renewcount,
	SUM(CASE WHEN a.apptype='ADDITIONAL' AND bal.assessmenttype='NEW' THEN 1 ELSE 0 END) AS addlobcount,
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
	businessid, appyear, tradename, businessaddress, 
	ownername, owneraddress, sum(decimalvalue) as amount, 
	(
		select top 1 permitno from business_permit 
		where businessid=xx.businessid and activeyear=xx.appyear 
		order by version desc 
	) as permitno 
from ( 
	select 
		bal.businessid, ba.appyear, ba.tradename, ba.businessaddress, 
		ba.ownername, ba.owneraddress, 
		(
			select sum(decimalvalue) from business_application_info 
			where applicationid=bal.applicationid and lob_objid=bal.lobid 
				and attribute_objid in ('CAPITAL','GROSS') 
		) as decimalvalue 
	from ( 
		select 
			bal.businessid, bal.activeyear, bal.lobid, max(ba.txndate) as txndate  
		from business_application ba 
			inner join business_application_lob bal on ba.objid=bal.applicationid 
			inner join business b on ba.business_objid=b.objid 
			inner join lob on bal.lobid=lob.objid 			
		where ba.appyear=$P{year} and ba.state in ('RELEASE','COMPLETED') ${filter} 
		group by bal.businessid, bal.activeyear, bal.lobid 
	)xx 
		inner join business_application_lob bal on xx.businessid=bal.businessid 
		inner join business_application ba on bal.applicationid=ba.objid 
	where bal.activeyear=xx.activeyear and bal.lobid=xx.lobid and ba.txndate=xx.txndate 
)xx 
group by businessid, appyear, tradename, businessaddress, ownername, owneraddress
order by sum(decimalvalue) desc, tradename 


[getBusinessPermitSummary]
select 
	a.activeyear, a.iqtr, a.imonth, a.strmonth,
	SUM(a.newcount) AS newcount,
	SUM(a.newamount) AS newamount,
	SUM(a.renewcount) AS renewcount,
	SUM(a.renewamount) AS renewamount,
	SUM(a.retirecount) AS retirecount,
	SUM(a.retireamount) AS retireamount,
	SUM(a.amount) AS total 
from ( 
	select 
		bp.activeyear, MONTH(bp.dtissued) as imonth,
		CASE
			WHEN MONTH(bp.dtissued) BETWEEN 1 and 3 then 1 
			WHEN MONTH(bp.dtissued) BETWEEN 4 and 6 then 2 
			WHEN MONTH(bp.dtissued) BETWEEN 7 and 9 then 3 
			WHEN MONTH(bp.dtissued) BETWEEN 10 and 12 then 4 
		END AS iqtr, 
		CASE 
			WHEN MONTH(bp.dtissued) = 1 THEN 'JANUARY'
			WHEN MONTH(bp.dtissued) = 2 THEN 'FEBRUARY'
			WHEN MONTH(bp.dtissued) = 3 THEN 'MARCH'
			WHEN MONTH(bp.dtissued) = 4 THEN 'APRIL'
			WHEN MONTH(bp.dtissued) = 5 THEN 'MAY'
			WHEN MONTH(bp.dtissued) = 6 THEN 'JUNE'
			WHEN MONTH(bp.dtissued) = 7 THEN 'JULY'
			WHEN MONTH(bp.dtissued) = 8 THEN 'AUGUST'
			WHEN MONTH(bp.dtissued) = 9 THEN 'SEPTEMBER'
			WHEN MONTH(bp.dtissued) = 10 THEN 'OCTOBER'
			WHEN MONTH(bp.dtissued) = 11 THEN 'NOVEMBER'
			WHEN MONTH(bp.dtissued) = 12 THEN 'DECEMBER'
		END AS strmonth, 
		(CASE WHEN ba.apptype IN ('NEW','ADDITIONAL') THEN 1 ELSE 0 END) AS newcount,
		(CASE WHEN ba.apptype IN ('NEW', 'ADDITIONAL') THEN bpy.amount ELSE 0.0 END) AS newamount,
		(CASE WHEN ba.apptype = 'RENEW' THEN 1 ELSE 0 END) AS renewcount,
		(CASE WHEN ba.apptype = 'RENEW' THEN bpy.amount ELSE 0.0 END) AS renewamount,
		(CASE WHEN ba.apptype IN ('RETIRE', 'RETIRELOB') THEN 1 ELSE 0 END) AS retirecount,
		(CASE WHEN ba.apptype IN ('RETIRE', 'RETIRELOB') THEN bpy.amount ELSE 0.0 END) AS retireamount,
		bpy.amount 
	from ( 
		select bpmin.businessid, bpmin.applicationid, bpmax.objid as permitid 
		from ( 
			select 
				businessid, activeyear, 
				min(version) as minver, 
				max(version) as maxver 
			from business_permit 
			where activeyear=$P{year} 
			group by businessid, activeyear
		)xx 
			inner join business_permit bpmin on (xx.businessid=bpmin.businessid and xx.minver=bpmin.version)  
			inner join business_permit bpmax on (xx.businessid=bpmax.businessid and xx.maxver=bpmax.version) 
			inner join business b on bpmin.businessid=b.objid 
		where bpmin.activeyear=xx.activeyear and bpmax.activeyear=xx.activeyear and b.permittype=$P{permittypeid} 
	)xx 
		inner join business_permit bp on xx.permitid=bp.objid 
		inner join business_application ba on xx.applicationid=ba.objid 
		inner join business_payment bpy on xx.businessid=bpy.businessid 
	where bpy.voided=0 
)a  
group by a.activeyear, a.iqtr, a.imonth, a.strmonth
order by a.activeyear, a.iqtr, a.imonth 


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
order by xx.bin 


[getEmployerList]
select xx.*, (xx.numfemale + xx.nummale) as numemployee
from ( 
	select 
		b.objid, b.bin, b.tradename, b.address_text as businessaddress, b.owner_name, 
		ba.objid as appid, ba.apptype, ba.appno, ba.state as appstate, 
		(select top 1 permitno from business_permit where businessid=b.objid order by version desc) as permitno, 
		(select sum(intvalue) from business_active_info where businessid=b.objid and attribute_objid='NUM_EMPLOYEE_FEMALE') AS numfemale,
		(select sum(intvalue) from business_active_info where businessid=b.objid and attribute_objid='NUM_EMPLOYEE_MALE') AS nummale,
		(select sum(intvalue) from business_active_info where businessid=b.objid and attribute_objid='NUM_EMPLOYEE_RESIDENT') AS numresident,
		(select count(objid) from entityindividual where objid=b.owner_objid and gender='M') as malecount, 
		(select count(objid) from entityindividual where objid=b.owner_objid and gender='F') as femalecount, 		
		case 
			when b.orgtype='SING' then (select tin from entityindividual WHERE objid=b.owner_objid) 
			else (select tin FROM entityjuridical WHERE objid=b.owner_objid) 
		end as tin, '' as sss 
	from business b 
		inner join business_application ba on b.currentapplicationid=ba.objid 
	where b.activeyear=$P{year} and b.state='ACTIVE' ${filter} 
	union all 
	select 
		b.objid, b.bin, b.tradename, b.address_text AS businessaddress, b.owner_name, 
		ba.objid AS appid, ba.apptype, ba.appno, ba.state as appstate, null as permitno, 
		(select sum(intvalue) from business_application_info where applicationid=ba.objid and attribute_objid='NUM_EMPLOYEE_FEMALE') AS numfemale,
		(select sum(intvalue) from business_application_info where applicationid=ba.objid and attribute_objid='NUM_EMPLOYEE_MALE') AS nummale,
		(select sum(intvalue) from business_application_info where applicationid=ba.objid and attribute_objid='NUM_EMPLOYEE_RESIDENT') AS numresident,
		(select count(objid) from entityindividual where objid=b.owner_objid and gender='M') as malecount, 
		(select count(objid) from entityindividual where objid=b.owner_objid and gender='F') as femalecount, 		
		case 
			when b.orgtype='SING' then (select tin from entityindividual WHERE objid=b.owner_objid) 
			else (select tin FROM entityjuridical WHERE objid=b.owner_objid) 
		end as tin, '' as sss 
	from business b 
		inner join business_application ba on b.currentapplicationid=ba.objid 
	where b.activeyear=$P{year} and b.state='PROCESSING' ${filter} 
)xx 
order by xx.tradename 


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
select 
	a.activeyear, a.iqtr, a.imonth, a.strmonth,
	SUM(a.newcount) AS newcount,
	SUM(a.newamount) AS newamount,
	SUM(a.renewcount) AS renewcount,
	SUM(a.renewamount) AS renewamount,
	SUM(a.retirecount) AS retirecount,
	SUM(a.retireamount) AS retireamount,
	SUM(a.amount) AS total 
from ( 
	select 
		bp.appyear AS activeyear, MONTH(bp.refdate) AS imonth, 
		CASE
			WHEN MONTH(bp.refdate) >= 1 AND MONTH(bp.refdate) <= 3 THEN 1
			WHEN MONTH(bp.refdate) >= 4 AND MONTH(bp.refdate) <= 6 THEN 2
			WHEN MONTH(bp.refdate) >= 7 AND MONTH(bp.refdate) <= 9 THEN 3
			WHEN MONTH(bp.refdate) >= 10 AND MONTH(bp.refdate) <= 12 THEN 4
		END AS iqtr, 
		CASE 
			WHEN MONTH(bp.refdate) = 1 THEN 'JANUARY'
			WHEN MONTH(bp.refdate) = 2 THEN 'FEBRUARY'
			WHEN MONTH(bp.refdate) = 3 THEN 'MARCH'
			WHEN MONTH(bp.refdate) = 4 THEN 'APRIL'
			WHEN MONTH(bp.refdate) = 5 THEN 'MAY'
			WHEN MONTH(bp.refdate) = 6 THEN 'JUNE'
			WHEN MONTH(bp.refdate) = 7 THEN 'JULY'
			WHEN MONTH(bp.refdate) = 8 THEN 'AUGUST'
			WHEN MONTH(bp.refdate) = 9 THEN 'SEPTEMBER'
			WHEN MONTH(bp.refdate) = 10 THEN 'OCTOBER'
			WHEN MONTH(bp.refdate) = 11 THEN 'NOVEMBER'
			WHEN MONTH(bp.refdate) = 12 THEN 'DECEMBER'
		END AS strmonth, 
		(CASE WHEN ba.apptype IN ('NEW','ADDITIONAL') THEN 1 ELSE 0 END) AS newcount,
		(CASE WHEN ba.apptype IN ('NEW', 'ADDITIONAL') THEN bp.amount ELSE 0.0 END) AS newamount,
		(CASE WHEN ba.apptype = 'RENEW' THEN 1 ELSE 0 END) AS renewcount,
		(CASE WHEN ba.apptype = 'RENEW' THEN bp.amount ELSE 0.0 END) AS renewamount,
		(CASE WHEN ba.apptype IN ('RETIRE', 'RETIRELOB') THEN 1 ELSE 0 END) AS retirecount,
		(CASE WHEN ba.apptype IN ('RETIRE', 'RETIRELOB') THEN bp.amount ELSE 0.0 END) AS retireamount, 
		bp.amount 
	from business_payment bp 
		inner join business_application ba on bp.applicationid=ba.objid 
		inner join business b on ba.business_objid=b.objid 
	where bp.appyear=$P{year} and bp.voided=0 ${filter} 
)a 
group by a.activeyear, a.iqtr, a.imonth, a.strmonth 
order by a.activeyear, a.iqtr, a.imonth


[getBPTaxFeeTopList]
select top ${topsize} 
	businessid, appyear, tradename, businessaddress, ownername, owneraddress, 
	sum(tax) as tax, sum(regfee) as regfee, sum(othercharge) as othercharge,
	sum(tax + regfee + othercharge) as total, 
	(
		select top 1 permitno from business_permit 
		where businessid=xx.businessid and activeyear=xx.appyear 
		order by version desc 
	) as permitno 
from ( 
	select 
		ba.business_objid as businessid, ba.appyear, ba.tradename, 
		ba.businessaddress, ba.ownername, ba.owneraddress, 
		(case when br.taxfeetype='TAX' then br.amount else 0.0 end) as tax, 
		(case when br.taxfeetype='REGFEE' then br.amount else 0.0 end) as regfee,
		(case when br.taxfeetype='OTHERCHARGE' then br.amount else 0.0 end) as othercharge 
	from business b 
		inner join business_application ba on b.currentapplicationid=ba.objid 
		inner join business_receivable br on br.businessid=b.objid 
	where ba.appyear=$P{year} and ba.state in ('RELEASE','COMPLETED')  
		and br.iyear=ba.appyear ${filter} 
)xx 
group by businessid, appyear, tradename, businessaddress, ownername, owneraddress 
order by sum(tax + regfee + othercharge) desc, tradename 
