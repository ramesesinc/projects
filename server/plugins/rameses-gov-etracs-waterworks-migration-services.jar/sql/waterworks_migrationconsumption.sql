[insertData]
insert into migrationconsumption ( 
	objid, parentid, acctid, prevreading, reading, volume, 
	readingmethod, billingcycleid, amount, amtpaid, uacctid, 
	allowed  
) 
select 
	objid, parentid, acctid, prevreading, reading, 
	(reading - prevreading) as volume, 
	readingmethod, billingcycleid, amount, amtpaid, uacctid, 
	1 as allowed  
from ( 
	select 
		concat('MC',UUID()) as objid, 
		mi.parentid, mi.objid as acctid, 
		(case when mi.prevreading is null then 0 else mi.prevreading end) as prevreading, 
		(case when mi.currentreading is null then 0 else mi.currentreading end) as reading, 
		'CAPTURE' as readingmethod, mi.billingcycleid, 0.0 as amount, 0.0 as amtpaid, 
		(case when mi.sourceacctid is null then mi.objid else mi.sourceacctid end) as uacctid 
	from migrationitem mi 
		inner join waterworks_billing_cycle wbc on wbc.objid = mi.billingcycleid 
	where mi.parentid = $P{parentid} 
)tmp1 
where (reading + prevreading) > 0  


[resolveAllowed]
update 
	migrationconsumption mc, waterworks_account wa, waterworks_consumption wc 
set 
	mc.allowed = 0 
where mc.parentid = $P{parentid} 
	and mc.uacctid = wa.objid 
	and mc.uacctid = wc.acctid 
	and mc.billingcycleid = wc.billingcycleid 


[insertConsumptions]
insert into waterworks_consumption ( 
	objid, acctid, prevreading, reading, volume, 
	readingmethod, billingcycleid, amount, amtpaid 
) 
select 
	objid, uacctid, prevreading, reading, volume, 
	readingmethod, billingcycleid, amount, amtpaid 
from migrationconsumption mc 
where mc.parentid = $P{parentid} 
	and mc.allowed = 1 
