[getLOBs]
SELECT alob.*, 
	a.apptype, a.txndate, a.dtfiled, a.appno, 
	(
		select sum(decimalvalue) from business_application_info 
		where applicationid=a.objid and lob_objid=alob.lobid and attribute_objid='CAPITAL' 
	) as capital, 
	(
		select sum(decimalvalue) from business_application_info 
		where applicationid=a.objid and lob_objid=alob.lobid and attribute_objid='GROSS' 
	) as gross 	
from business_permit p 
	inner join business_application pa on pa.objid=p.applicationid 
	inner join business_application a on (a.business_objid=pa.business_objid and a.appyear=pa.appyear)
	inner join business_application_lob alob on alob.applicationid=a.objid 
where p.objid = $P{permitid} 
	and a.state='COMPLETED' 
	and a.txndate <= pa.txndate 
order by a.txndate 


[getPayments]
select bpay.refno   
from business_permit p 
	inner join business_application pa on pa.objid=p.applicationid 
	inner join business_application a on (a.business_objid=pa.business_objid and a.appyear=pa.appyear )
	inner join business_payment bpay on (bpay.applicationid=a.objid and bpay.voided=0) 
where p.objid = $P{permitid}  
	and a.state='COMPLETED' 
	and a.txndate <= pa.txndate 
order by bpay.refdate, bpay.refno 


[getList]
select * from ( 
	select 
		tmp2.*, p.permitno, p.dtissued, ba.apptype, 
		ba.ownername as owner_name, ba.owneraddress as owner_address, 
		ba.tradename, ba.businessaddress as businessaddress, b.orgtype, idtin.idno as tin, 
		(
			select sum( ai.intvalue ) from business_application a 
				inner join business_application_info ai on ai.applicationid=a.objid 
			where a.business_objid = p.businessid 
				and a.appyear = ba.appyear 
				and a.state = 'COMPLETED' 			
				and a.apptype in ('NEW','RENEW')
				and ai.attribute_objid = 'NUM_EMPLOYEE' 
		) as numemployee, 
		(
			select sum( ai.intvalue ) from business_application a 
				inner join business_application_info ai on ai.applicationid=a.objid 
			where a.business_objid = p.businessid 
				and a.appyear = ba.appyear 
				and a.state = 'COMPLETED' 			
				and a.apptype in ('NEW','RENEW')
				and ai.attribute_objid='NUM_EMPLOYEE_MALE'
		) as nummale, 
		(
			select sum( ai.intvalue ) from business_application a 
				inner join business_application_info ai on ai.applicationid=a.objid 
			where a.business_objid = p.businessid 
				and a.appyear = ba.appyear 
				and a.state = 'COMPLETED' 			
				and a.apptype in ('NEW','RENEW')
				and ai.attribute_objid='NUM_EMPLOYEE_FEMALE'
		) as numfemale  
	from ( 
		select 
			tmp1.mainappid, tmp1.businessid, tmp1.activeyear, max(tmp1.version) as permitver, 
			(	
				select objid from business_permit 
				where businessid=tmp1.businessid 
					and activeyear=tmp1.activeyear 
					and version=max(tmp1.version) 
			) as permitid 
		from ( 
			select 
				(case when a.parentapplicationid is null then a.objid else a.parentapplicationid end) as mainappid, 
				p.businessid, p.applicationid, p.dtissued, p.activeyear, p.permitno, p.version 
			from business_permit p 
				inner join business_application a on a.objid = p.applicationid 
			where p.activeyear = $P{year} 
				and p.dtissued >= $P{startdate} 
				and p.dtissued <  $P{enddate} 
				and p.state = 'ACTIVE' 
		)tmp1
		group by mainappid, businessid, activeyear 
	)tmp2 
		inner join business_permit p on p.objid = tmp2.permitid 
		inner join business_application ba on ba.objid = tmp2.mainappid 
		inner join business b on b.objid = ba.business_objid 
		left join entityid idtin on (idtin.entityid = b.owner_objid and idtin.idtype = 'TIN') 	
		left join business_address addr on addr.objid = b.address_objid 
	where ba.apptype in ('NEW','RENEW') ${filter} 
)tmp3 
${orderbyfilter} 


[getList_bak1]
select * from ( 
	select tmp2.*, 
		b.owner_name, b.owner_address_text as owner_address, 
		b.tradename, b.address_text as businessaddress, 
		b.objid as business_objid, idtin.idno as tin, b.orgtype, 
	 	(
			select sum( ai.intvalue ) from business_application a 
				inner join business_application_info ai on ai.applicationid=a.objid 
			where a.business_objid=tmp2.businessid 
				and a.appyear=tmp2.activeyear 
				and a.apptype in ('NEW','RENEW')
				and a.state='COMPLETED' 			
				and ai.attribute_objid='NUM_EMPLOYEE' 
		) as numemployee, 
		(
			select sum( ai.intvalue ) from business_application a 
				inner join business_application_info ai on ai.applicationid=a.objid 
			where a.business_objid=tmp2.businessid 
				and a.appyear=tmp2.activeyear 
				and a.apptype in ('NEW','RENEW')
				and a.state='COMPLETED' 			
				and ai.attribute_objid='NUM_EMPLOYEE_MALE'
		) as nummale, 
		(
			select sum( ai.intvalue ) from business_application a 
				inner join business_application_info ai on ai.applicationid=a.objid 
			where a.business_objid=tmp2.businessid 
				and a.appyear=tmp2.activeyear 
				and a.apptype in ('NEW','RENEW')
				and a.state='COMPLETED' 			
				and ai.attribute_objid='NUM_EMPLOYEE_FEMALE' 
		) as numfemale    
	from ( 
		select tmp1.*, 
			p.objid as permitid, p.permitno, p.dtissued  
		from ( 
			select 
				businessid, activeyear, max(version) as maxver 
			from business_permit 
			where activeyear = $P{year} 
				and dtissued >= $P{startdate} 
				and dtissued <  $P{enddate} 
				and state = 'ACTIVE' 
			group by businessid, activeyear 
		)tmp1, business_permit p 
		where p.businessid=tmp1.businessid 
			and p.activeyear=tmp1.activeyear 
			and p.version=tmp1.maxver 
	)tmp2 
		inner join business_application ba on (ba.business_objid=tmp2.businessid and ba.appyear=tmp2.activeyear) 
		inner join business b on ba.business_objid=b.objid 
		left join entityid idtin on (idtin.entityid=b.owner_objid and idtin.idtype='TIN') 	
		left join business_address addr ON b.address_objid=addr.objid  
	where ba.parentapplicationid is null 
		and ba.apptype in ('NEW','RENEW') 
		${filter}  
)tmp3 
${orderbyfilter} 
