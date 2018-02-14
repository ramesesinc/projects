[getUnmappedClassifications]
select distinct 
	accttype as value 
from migrationitem 
where parentid = $P{parentid} 
	and classificationid is null 
order by accttype

[getUnmappedMeters]
select distinct 
	meterno as value 
from migrationitem 
where parentid = $P{parentid} 
	and meterno is not null 
	and meterid is null
order by meterno

[getUnmappedMeterSizes]
select distinct 
	metersize as value 
from migrationitem 
where parentid = $P{parentid} 
	and meterno is not null 
	and metersizeid is null
order by metersize 

[getUnmappedSectors]
select distinct 
	sectorcode as value 
from migrationitem 
where parentid = $P{parentid} 
	and sectorid is null
order by sectorcode 

[getUnmappedSectorZones]
select distinct 
	sectorcode, zonecode  
from migrationitem 
where parentid = $P{parentid} 
	and zoneid is null 
order by sectorcode, zonecode 

[resolveAccountTypes]
update 
	migrationitem a, migrationmapping_accttype b 
set 
	a.classificationid = b.refid 
where a.parentid = $P{parentid} 
	and a.accttype = b.objid 
	and a.classificationid is null 

[resolveAccounts]
update 
	migrationitem a, waterworks_account b 
set 
	a.sourceacctid = b.objid 
where a.parentid = $P{migrationid} 
	and a.acctno = b.acctno 

[insertAccounts]
insert into waterworks_account ( 
	objid, state, dtstarted, acctno, acctname, address_text, 
	classificationid, lastdateread, currentreading, 
	sectorid, zoneid, stuboutid, stuboutnodeid, 
	billingcycleid, meterid, metersizeid 
) 
select distinct 
	objid, state, dtstarted, acctno, acctname, address_text, 
	classificationid, lastdateread, currentreading, 
	sectorid, zoneid, stuboutid, stuboutnodeid, billingcycleid, meterid, 
	(case when meterid is null then null else metersizeid end) as metersizeid 
from ( 
	select 
		mi.objid, 'ACTIVE' as state, mi.dtstarted, mi.acctno, mi.acctname, mi.address_text, 
		case 
			when wa.objid is not null then null 
			when mi.meterid is null then mi.meterid 
			else null 
		end as meterid, 
		mi.classificationid, mi.dtreading as lastdateread, mi.currentreading, 
		mi.sectorid, mi.zoneid, mi.metersizeid, mi.stuboutid, mi.stuboutnodeid, 
		mi.billingcycleid  
	from migrationitem mi 
		left join waterworks_account wa on wa.meterid = mi.meterid 	
	where mi.parentid = $P{parentid} 
		and mi.sourceacctid is null 
)tmp1 

[resolveBillingCycles]
update 
	migrationitem a, migrationbillingcycle b 
set 
	a.billingcycleid = b.billingcycleid 
where a.parentid = $P{parentid} 
	and a.parentid = b.parentid 
	and a.sectorid = b.sectorid 
	and a.dtreading = b.dtreading 
