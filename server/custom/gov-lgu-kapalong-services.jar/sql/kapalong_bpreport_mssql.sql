[getList]
select 
	a.objid, a.appno, a.apptype, a.tradename, a.businessaddress, a.ownername, a.owneraddress, b.bin, b.orgtype, 
	(select top 1 permitno from business_permit where applicationid=a.objid and activeyear=a.appyear order by version desc) as permitno, 

	zinfo.intvalue as numofemployee, a.dtfiled, 
	
	isnull((
		select (case when a.apptype='NEW' then sum(decimalvalue) else 0.0 end) 
		from business_application_info where applicationid=a.objid and attribute_objid='CAPITAL' 
	),0) as capital, 
	isnull((
		select (case when a.apptype='NEW' then sum(decimalvalue) else 0.0 end) 
		from business_application a0 
			inner join business_application_info bai on a0.objid=bai.applicationid 
		where a0.parentapplicationid=a.objid and a0.state in ('RELEASE','COMPLETED') and bai.attribute_objid='CAPITAL'
	),0) as extcapital, 

	isnull((
		select (case when a.apptype='RENEW' then sum(decimalvalue) else 0.0 end) 
		from business_application_info where applicationid=a.objid and attribute_objid='GROSS' 
	),0) as gross, 
	isnull((
		select (case when a.apptype='RENEW' then sum(decimalvalue) else 0.0 end) 
		from business_application a0 
			inner join business_application_info bai on a0.objid=bai.applicationid 
		where a0.parentapplicationid=a.objid and a0.state in ('RELEASE','COMPLETED') and bai.attribute_objid='GROSS'
	),0) as extgross, 

	isnull((
		select sum(amount) from business_receivable 
		where applicationid=a.objid and taxfeetype='TAX'
	),0) as tax,
	isnull((
		select sum(r0.amount) from business_receivable r0  
			inner join business_application a0 on (r0.applicationid=a0.objid and a0.parentapplicationid=a.objid) 
		where r0.taxfeetype='TAX' 
	),0) as exttax, 

	isnull((
		select sum(amount) from business_receivable 
		where applicationid=a.objid and taxfeetype='REGFEE'
	),0) as regfee,
	isnull((
		select sum(r0.amount) from business_receivable r0  
			inner join business_application a0 on (r0.applicationid=a0.objid and a0.parentapplicationid=a.objid) 
		where r0.taxfeetype='REGFEE' 
	),0) as extregfee,

	isnull(( 
		select sum(amount) from business_receivable br 
			inner join report_itemaccount_mapping rm on br.account_objid=rm.accountid
		where br.applicationid=a.objid and rm.objid in ('PLATE','STICKER') 
	),0) as platestkr, 
	isnull(( 
		select sum(amount) from business_receivable br 
			inner join business_application ba on (br.applicationid=ba.objid and ba.parentapplicationid=a.objid) 
			inner join report_itemaccount_mapping rm on br.account_objid=rm.accountid 
		where rm.objid in ('PLATE','STICKER') 
	),0) as extplatestkr,	

	isnull(( 
		select sum(amount) from business_receivable br 
			inner join report_itemaccount_mapping rm on br.account_objid=rm.accountid
		where br.applicationid=a.objid and rm.objid='WM'
	),0) as wm, 
	isnull(( 
		select sum(amount) from business_receivable br 
			inner join business_application ba on (br.applicationid=ba.objid and ba.parentapplicationid=a.objid) 
			inner join report_itemaccount_mapping rm on br.account_objid=rm.accountid 
		where rm.objid='WM' 
	),0) as extwm,

	isnull(( 
		select sum(amount) from business_receivable br 
			inner join report_itemaccount_mapping rm on br.account_objid=rm.accountid
		where br.applicationid=a.objid and rm.objid='MP'
	),0) as mp, 
	isnull(( 
		select sum(amount) from business_receivable br 
			inner join business_application ba on (br.applicationid=ba.objid and ba.parentapplicationid=a.objid) 
			inner join report_itemaccount_mapping rm on br.account_objid=rm.accountid 
		where rm.objid='MP' 
	),0) as extmp, 

	isnull(( 
		select sum(amount) from business_receivable br 
			inner join report_itemaccount_mapping rm on br.account_objid=rm.accountid
		where br.applicationid=a.objid and rm.objid='OCF'
	),0) as ocf, 
	isnull(( 
		select sum(amount) from business_receivable br 
			inner join business_application ba on (br.applicationid=ba.objid and ba.parentapplicationid=a.objid) 
			inner join report_itemaccount_mapping rm on br.account_objid=rm.accountid 
		where rm.objid='OCF' 
	),0) as extocf, 

	isnull(( 
		select sum(amount) from business_receivable br 
			inner join report_itemaccount_mapping rm on br.account_objid=rm.accountid
		where br.applicationid=a.objid and rm.objid='GF'
	),0) as gf, 
	isnull(( 
		select sum(amount) from business_receivable br 
			inner join business_application ba on (br.applicationid=ba.objid and ba.parentapplicationid=a.objid) 
			inner join report_itemaccount_mapping rm on br.account_objid=rm.accountid 
		where rm.objid='GF' 
	),0) as extgf, 

	isnull(( 
		select sum(bpi.surcharge) from business_payment bp 
			inner join business_payment_item bpi on bp.objid=bpi.parentid 
		where bp.applicationid=a.objid and bp.voided=0 
	),0) as surcharge, 
	isnull(( 
		select sum(bpi.interest) from business_payment bp 
			inner join business_payment_item bpi on bp.objid=bpi.parentid 
		where bp.applicationid=a.objid and bp.voided=0 
	),0) as interest, 

	stuff(( 
		select ', '+refno from ( 
			select refno from business_payment where applicationid=a.objid and voided=0  
			union 
			select refno from business_payment where applicationid in (
				select objid from business_application where parentapplicationid=a.objid
			) and voided=0 
		) as c1 
		for XML PATH ('')),1,1,'') as orno, 

	stuff(( 
		select ', '+refdate from ( 
			select convert(varchar(10), getdate(), 111) as refdate from business_payment 
			where applicationid=a.objid and voided=0  
			union 
			select convert(varchar(10), getdate(), 111) as refdate from business_payment 
			where applicationid in (
				select objid from business_application where parentapplicationid=a.objid
			) and voided=0 
		) as c1 
		for XML PATH ('')),1,1,'') as ordate,  

	stuff(( 
		select ', '+name from business_active_lob where businessid=a.business_objid 
		for XML PATH ('')),1,1,'') as activelobs 

from business_application a 
	inner join business b on a.business_objid=b.objid 
	left join (
		select objid, sum(isnull(intvalue,0)) as intvalue    
		from ( 
			select a.objid, ai.intvalue from business_application a 
				inner join business_application_info ai on a.objid=ai.applicationid 
			where ai.attribute_objid='NUM_EMPLOYEE' and ai.activeyear=$P{year} and a.parentapplicationid is null 
			union all 
			select a.parentapplicationid as objid, ai.intvalue from business_application a 
				inner join business_application_info ai on a.objid=ai.applicationid 
			where ai.attribute_objid='NUM_EMPLOYEE' and ai.activeyear=$P{year} and a.parentapplicationid is not null 
		)xx 
		group by objid
	)zinfo on a.objid=zinfo.objid 	
where a.appyear=$P{year} and a.state='COMPLETED' 
	and a.apptype IN ('NEW','RENEW') and a.parentapplicationid is null 
	and a.objid in (select applicationid from business_permit where applicationid=a.objid) 
	and a.objid in (
		select objid from business_application where objid=a.objid 
			and month(dtfiled) between $P{startmonth} and $P{endmonth} 
	) 
	and b.address_objid in (
		select b.address_objid as objid where '%'=$P{barangayid} 
		union 
		select objid from business_address  
		where objid=b.address_objid 
			and barangay_objid like $P{barangayid} 
			and not('%'=$P{barangayid}) 
	) 
