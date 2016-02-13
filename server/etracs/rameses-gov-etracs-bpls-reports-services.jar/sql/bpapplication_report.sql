[getList]
select xx.*, 
	(case when xx.lobid=xx.baselobid then 1 else 0 end) as businesscount 
from ( 
	select 
		alob.assessmenttype as apptype, a.state, a.appno, a.appyear, b.orgtype, ba.barangay_name, 
		b.tradename, b.address_text as businessaddress, b.owner_name, b.owner_address_text as owner_address, 
		alob.lobid, alob.name AS lobname, lob.classification_objid,  
		(SELECT dtissued FROM business_permit WHERE businessid=b.objid and activeyear=a.appyear ORDER BY version DESC LIMIT 1) AS dtissued, 
		(SELECT lobid FROM business_application_lob WHERE applicationid=a.objid LIMIT 1) AS baselobid, 
		IFNULL((
			SELECT SUM(decimalvalue) FROM business_application_info 
			WHERE attribute_objid='DECLARED_CAPITAL' AND applicationid=a.objid AND lob_objid=alob.lobid
		),0.0) AS capital,
		IFNULL((
			SELECT SUM(decimalvalue) FROM business_application_info 
			WHERE attribute_objid='DECLARED_GROSS' AND applicationid=a.objid AND lob_objid=alob.lobid
		),0.0) AS gross 
	from business_application a 
		inner join business b on a.business_objid=b.objid 
		inner join business_application_lob alob on a.objid=alob.applicationid 
		inner join lob on alob.lobid = lob.objid 
		left join business_address ba on b.address_objid = ba.objid 
	where $P{showcompleted}=1 and a.appyear=$P{year} 
		and a.dtfiled between $P{startdate} and $P{enddate}  
		and a.parentapplicationid is null ${otherfilter} 
		and a.state='COMPLETED' 

	union all
	 
	select 
		alob.assessmenttype as apptype, a.state, a.appno, a.appyear, b.orgtype, ba.barangay_name, 
		b.tradename, b.address_text as businessaddress, b.owner_name, b.owner_address_text as owner_address, 
		alob.lobid, alob.name AS lobname, lob.classification_objid,  null AS dtissued, 
		(SELECT lobid FROM business_application_lob WHERE applicationid=a.objid LIMIT 1) AS baselobid, 
		IFNULL((
			SELECT SUM(decimalvalue) FROM business_application_info 
			WHERE attribute_objid='DECLARED_CAPITAL' AND applicationid=a.objid AND lob_objid=alob.lobid
		),0.0) AS capital,
		IFNULL((
			SELECT SUM(decimalvalue) FROM business_application_info 
			WHERE attribute_objid='DECLARED_GROSS' AND applicationid=a.objid AND lob_objid=alob.lobid
		),0.0) AS gross 
	from business_application a 
		inner join business b on a.business_objid=b.objid 
		inner join business_application_lob alob on a.objid=alob.applicationid 
		inner join lob on alob.lobid = lob.objid 
		left join business_address ba on b.address_objid = ba.objid 
	where $P{showprocessing}=1 and a.appyear=$P{year}  
		and a.dtfiled between $P{startdate} and $P{enddate}  
		and a.parentapplicationid is null ${statefilter} ${otherfilter}  
		and a.state <> 'COMPLETED' 
)xx 
order by appno, lobname 
