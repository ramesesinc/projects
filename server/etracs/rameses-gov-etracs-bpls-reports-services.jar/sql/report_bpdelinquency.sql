[getBarangays]
select * from barangay order by indexno 


[clearItems]
delete from report_bpdelinquency_item where parentid=$P{reportid} 


[build]
insert into report_bpdelinquency_item ( 
	objid, parentid, applicationid 
) 
select 
	UUID() as objid, $P{reportid} as parentid, tmpa.applicationid 
from ( 
	select applicationid, sum(amount-amtpaid) as balance 
	from business_receivable  
	group by applicationid 
	having sum(amount-amtpaid) > 0 
)tmpa 
	inner join business_application ba on ba.objid=tmpa.applicationid 
where ba.state in ('RELEASE','COMPLETED') 


[updateHeader]
update report_bpdelinquency set 
	processedcount = 0, 
	totalcount = (select count(*) from report_bpdelinquency_item where parentid=$P{reportid}) 
where objid=$P{reportid} 
	and state='FOR-PROCESS' 


[getForUpdateBuildItems]
select * 
from ( 
	select 
		rpt.*, ba.appno, ba.apptype, ba.appyear, ba.tradename,  
		addr.barangay_objid as barangayid, 
		addr.barangay_name as barangayname 
	from report_bpdelinquency_item rpt 
		inner join report_bpdelinquency rptp on rpt.parentid=rptp.objid 
		inner join business_application ba on ba.objid=rpt.applicationid 
		inner join business b on ba.business_objid=b.objid 
		left join business_address addr on b.address_objid=addr.objid 
	where rpt.parentid=$P{reportid} 
		and rptp.state='FOR-UPDATE' 
		and ba.state in ('RELEASE','COMPLETED') 
		and ba.apptype in ('NEW','RENEW') 
		and b.state in ('ACTIVE','PROCESSING') 
		and rpt.total is null 
)tmpa 
where 1=1 ${filter} 
order by barangayname, appyear, appno 


[findLedger]
select 
	tmpa.applicationid, b.appyear, b.appno, b.tradename, b.apptype, 
	ifnull(tmpa.amount,0.0) as amount, ifnull(tmpa.amtpaid,0.0) as amtpaid, 
	ifnull(tmpa.tax,0.0) as tax, ifnull(tmpa.regfee,0.0) as regfee, 
	ifnull(tmpa.othercharge,0.0) as othercharge 
from ( 
	select 
		ba.objid as applicationid, 
		(select sum(amount) from business_receivable where applicationid=ba.objid) as amount, 
		(select sum(amtpaid) from business_receivable where applicationid=ba.objid) as amtpaid,
		(select sum(amount-amtpaid) from business_receivable where applicationid=ba.objid and taxfeetype='TAX') as tax,
		(select sum(amount-amtpaid) from business_receivable where applicationid=ba.objid and taxfeetype='REGFEE') as regfee, 
		(select sum(amount-amtpaid) from business_receivable where applicationid=ba.objid and taxfeetype='OTHERCHARGE') as othercharge  
	from business_application ba  
	where ba.objid=$P{applicationid} 
)tmpa, business_application b   
where b.objid=tmpa.applicationid 
 
