[getList]
select 
	alob.assessmenttype as apptype, a.state, a.appno, a.appyear, b.orgtype, ba.barangay_name, 
	b.tradename, b.address_text as businessaddress, b.owner_name, b.owner_address_text as owner_address, 
	lob.objid as lobid, lob.name as lobname, lob.classification_objid, alob.businessid, 
	case 
		when a.state='COMPLETED' then (
			select dtissued from business_permit 
			where businessid=b.objid and activeyear=a.appyear and state='ACTIVE' 
			order by version desc limit 1
		) else null 
	end as dtissued, 
	IFNULL((
		select SUM(decimalvalue) from business_application_info 
		WHERE attribute_objid='DECLARED_CAPITAL' and applicationid=a.objid and lob_objid=alob.lobid
	),0.0) AS declaredcapital,
	IFNULL((
		select SUM(decimalvalue) from business_application_info 
		WHERE attribute_objid='DECLARED_GROSS' and applicationid=a.objid and lob_objid=alob.lobid
	),0.0) AS declaredgross , 
	IFNULL((
		select SUM(decimalvalue) from business_application_info 
		WHERE attribute_objid='CAPITAL' and applicationid=a.objid and lob_objid=alob.lobid
	),0.0) AS capital,
	IFNULL((
		select SUM(decimalvalue) from business_application_info 
		WHERE attribute_objid='GROSS' and applicationid=a.objid and lob_objid=alob.lobid
	),0.0) AS gross 
from business_application_lob alob 
	inner join business_application a on a.objid=alob.applicationid 
	inner join business b on b.objid=a.business_objid 
	inner join lob on alob.lobid=lob.objid 
	left join business_address ba on ba.objid=b.address_objid 
where a.appyear=$P{year} 
	and a.dtfiled between $P{startdate} and $P{enddate}  
	${filter} 
order by a.appno, lob.name 
