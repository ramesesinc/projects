[getList]
select 
	b.tradename, b.businessname, b.address_text, b.owner_name, b.orgtype, b.yearstarted, 
	(
		select GROUP_CONCAT(concat(' ', classification_objid))
		from business_application a, business_application_lob al, lob 
		where a.business_objid = t1.business_objid 
			and a.appyear = t1.appyear 
			and a.apptype in ('NEW','RENEW') 
			and a.state = 'COMPLETED' 
			and al.applicationid = a.objid 
			and lob.objid = al.lobid 
	) as businessnature, 
	(
		select permitno from business_permit 
		where businessid = t1.business_objid 
			and activeyear = t1.appyear 
			and state = 'ACTIVE' 
		order by dtissued desc, version desc 
		limit 1 
	) as permitno 
from ( 
		select distinct business_objid, appyear  
		from business_application 
		where appyear = ${year} 
			and apptype in ('NEW','RENEW') 
			and state in ('COMPLETED') 
	)t1
	inner join business b on b.objid = t1.business_objid 
where b.permittype = $P{permittypeid} 
order by b.businessname 
