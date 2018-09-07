[getList]
select 
	b.objid as businessid, tmp2.applicationid, a.apptype, a.appno, a.appyear, 
	b.orgtype, b.tradename, b.address_text as businessaddress, addr.barangay_name, 
	b.owner_name, b.owner_address_text as owner_address, 
	lob.objid as lobid, lob.name as lobname, lob.classification_objid, 
	tmp2.declaredcapital, tmp2.declaredgross, tmp2.capital, tmp2.gross, 
	case 
		when a.state='COMPLETED' then (
			select top 1 dtissued from business_permit 
			where businessid=b.objid and activeyear=a.appyear and state='ACTIVE' 
			order by version desc 
		) else null 
	end as dtissued  
from ( 
	select 
		applicationid, lobid, 
		isnull(sum(declaredcapital), 0) as declaredcapital,
		isnull(sum(declaredgross), 0) as declaredgross,
		isnull(sum(capital), 0) as capital,
		isnull(sum(gross), 0) as gross 
	from ( 
		select 
			ba.objid as applicationid, bal.lobid,  
			(select sum(decimalvalue) from business_application_info where applicationid=ba.objid and lob_objid=bal.lobid and attribute_objid='DECLARED_CAPITAL') as declaredcapital, 
			(select sum(decimalvalue) from business_application_info where applicationid=ba.objid and lob_objid=bal.lobid and attribute_objid='DECLARED_GROSS') as declaredgross,  
			(select sum(decimalvalue) from business_application_info where applicationid=ba.objid and lob_objid=bal.lobid and attribute_objid='CAPITAL') as capital, 
			(select sum(decimalvalue) from business_application_info where applicationid=ba.objid and lob_objid=bal.lobid and attribute_objid='GROSS') as gross 
		from business_application ba 
			inner join business b on ba.business_objid=b.objid 
			inner join business_application_lob bal on bal.applicationid=ba.objid 
		where ba.appyear in (YEAR($P{startdate}), YEAR($P{enddate}))
			and ba.dtfiled >= $P{startdate} 
			and ba.dtfiled <  $P{enddate}   
			and ba.apptype in ( ${apptypefilter} ) 
			and ba.state in ( ${appstatefilter} ) 
			and b.permittype = $P{permittypeid} 
	)tmp1 
	group by applicationid, lobid 
)tmp2 
	inner join business_application a on a.objid=tmp2.applicationid 
	inner join business b on a.business_objid=b.objid 
	inner join lob on lob.objid=tmp2.lobid 
	left join business_address addr on b.address_objid=addr.objid 
where 1=1 ${filter} 
order by b.tradename, a.appno, lob.name  
