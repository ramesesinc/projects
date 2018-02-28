[findLguStats]
select count(*) as totalcount from sys_org where orgclass = $P{lgutype}


[buildBrgyItemAccounts]
insert into itemaccount (
	objid, state, code, title, description, 
	type, fund_objid, fund_code, fund_title, 
	defaultvalue, valuetype, org_objid, org_name, parentid 
)
select * from ( 
	select 
		concat(xx.objid,':',l.objid) as objid, 'APPROVED' as state, '-' as code, 
		concat('BRGY. ', l.name , ' ' , ia.title) as title, 
		concat('BRGY. ', l.name , ' ' , ia.title) as description, ia.type, 
		ia.fund_objid, ia.fund_code, ia.fund_title, ia.defaultvalue, ia.valuetype, 
		ia.org_objid, ia.org_name, ia.objid as parentid 
	from ( 
		select 'basiccurrent' as objid, 'basic' as revtype, 'current' as revperiod, acctid as item_objid from itemaccount_tag where tag='RPT_BASIC_CURRENT'
		union 
		select 'basicintcurrent' as objid, 'basicint' as revtype, 'current' as revperiod, acctid as item_objid from itemaccount_tag where tag='RPT_BASIC_CURRENT_PENALTY'
		union 
		select 'basicprevious' as objid, 'basic' as revtype, 'previous' as revperiod, acctid as item_objid from itemaccount_tag where tag='RPT_BASIC_PREVIOUS'
		union 
		select 'basicintprevious' as objid, 'basicint' as revtype, 'previous' as revperiod, acctid as item_objid from itemaccount_tag where tag='RPT_BASIC_PREVIOUS_PENALTY'
		union 
		select 'basicprior' as objid, 'basic' as revtype, 'prior' as revperiod, acctid as item_objid from itemaccount_tag where tag='RPT_BASIC_PRIOR' 
		union 
		select 'basicintprior' as objid, 'basicint' as revtype, 'prior' as revperiod, acctid as item_objid from itemaccount_tag where tag='RPT_BASIC_PRIOR_PENALTY' 
		union 
		select 'basicadvance' as objid, 'basic' as revtype, 'advance' as revperiod, acctid as item_objid from itemaccount_tag where tag='RPT_BASIC_ADVANCE' 
	)xx 
		inner join itemaccount ia on xx.item_objid=ia.objid, barangay l
)xx 
where objid not in (select objid from itemaccount) 


[getCityItemAccounts]
select * from ( 
	select 
		concat(xx.objid,':',l.objid) as objid, 'APPROVED' as state, '-' as code, 
		concat(l.name , ' CITY ' , ia.title) as title, 
		concat(l.name , ' CITY ' , ia.title) as description, ia.type, 
		ia.fund_objid, ia.fund_code, ia.fund_title, ia.defaultvalue, ia.valuetype, 
		ia.org_objid, ia.org_name, ia.objid as parentid 
	from ( 
		${itemaccountsql}
	)xx 
		inner join itemaccount ia on xx.item_objid=ia.objid, city l 
)xx 
where not exists(select * from itemaccount where objid = xx.objid )



[getProvinceItemAccounts]
select * from ( 
	select 
		concat(xx.objid,':',l.objid) as objid, 'APPROVED' as state, '-' as code, 
		concat(l.name , ' PROVINCE ' , ia.title) as title, 
		concat(l.name , ' PROVINCE ' , ia.title) as description, ia.type, 
		ia.fund_objid, ia.fund_code, ia.fund_title, ia.defaultvalue, ia.valuetype, 
		ia.org_objid, ia.org_name, ia.objid as parentid 
	from ( 
		${itemaccountsql}
	)xx 
		inner join itemaccount ia on xx.item_objid=ia.objid, province l 
)xx 
where objid not in (select objid from itemaccount) 


[getMunicipalityItemAccounts]
select * from ( 
	select 
		concat(xx.objid,':',l.objid) as objid, 'APPROVED' as state, '-' as code, 
		concat('MUNI. ', l.name , ' ' , ia.title) as title, 
		concat('MUNI. ', l.name , ' ' , ia.title) as description, ia.type, 
		ia.fund_objid, ia.fund_code, ia.fund_title, ia.defaultvalue, ia.valuetype, 
		ia.org_objid, ia.org_name, ia.objid as parentid 
	from ( 
		${itemaccountsql}
	)xx 
		inner join itemaccount ia on xx.item_objid=ia.objid, municipality l 
)xx 
where not exists(select * from itemaccount where objid = xx.objid )



[removeAccountMappings]
delete from landtax_lgu_account_mapping 
where lgu_objid in(
	select objid from sys_org where orgclass = $P{lgutype}
)


[getAccountsByOrg]
select ia.objid, ia.objid as item_objid, l.objid as lgu_objid 
from itemaccount ia, sys_org l
where ia.objid like $P{itemid}
and l.orgclass = $P{orgclass}
and l.objid = $P{lguid}


[findBrgyMappingCount]
select count(*) as count from brgy_taxaccount_mapping

[findProvinceMappingCount]
select count(*) as count from province_taxaccount_mapping

[findMunicipalityMappingCount]
select count(*) as count from municipality_taxaccount_mapping





[migrateBarangayMappings]
insert into landtax_lgu_account_mapping(
	objid,
	lgu_objid,
	revperiod,
	revtype,
	item_objid
)
select distinct 
	x.objid,
	x.lgu_objid,
	x.revperiod,
	x.revtype,
	x.item_objid
from (
	select 
		basicadvacct_objid as objid,
		barangayid as lgu_objid,
		'advance' as revperiod,
		'basic' as revtype,
		basicadvacct_objid as item_objid
	from brgy_taxaccount_mapping
	union 
	select 
		basicprevacct_objid as objid,
		barangayid as lgu_objid,
		'previous' as revperiod,
		'basic' as revtype,
		basicprevacct_objid as item_objid
	from brgy_taxaccount_mapping
	union 
	select 
		basicprevintacct_objid as objid,
		barangayid as lgu_objid,
		'previous' as revperiod,
		'basicint' as revtype,
		basicprevintacct_objid as item_objid
	from brgy_taxaccount_mapping
	union 
	select 
		basicprioracct_objid as objid,
		barangayid as lgu_objid,
		'prior' as revperiod,
		'basic' as revtype,
		basicprioracct_objid as item_objid
	from brgy_taxaccount_mapping
	union 
	select 
		basicpriorintacct_objid as objid,
		barangayid as lgu_objid,
		'prior' as revperiod,
		'basicint' as revtype,
		basicpriorintacct_objid as item_objid
	from brgy_taxaccount_mapping
	union 
	select 
		basiccurracct_objid as objid,
		barangayid as lgu_objid,
		'current' as revperiod,
		'basic' as revtype,
		basiccurracct_objid as item_objid
	from brgy_taxaccount_mapping
	union 
	select 
		basiccurrintacct_objid as objid,
		barangayid as lgu_objid,
		'current' as revperiod,
		'basicint' as revtype,
		basiccurrintacct_objid as item_objid
	from brgy_taxaccount_mapping

)x
where x.item_objid is not null 
and exists(select * from sys_org where objid = x.lgu_objid)


[migrateProvinceMappings]
insert into landtax_lgu_account_mapping(
	objid,
	lgu_objid,
	revperiod,
	revtype,
	item_objid
)
select distinct 
	x.objid,
	x.lgu_objid,
	x.revperiod,
	x.revtype,
	x.item_objid
from (
	select 
		basicadvacct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'advance' as revperiod,
		'basic' as revtype,
		basicadvacct_objid as item_objid
	from province_taxaccount_mapping
	union 
	select 
		basicprevacct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'previous' as revperiod,
		'basic' as revtype,
		basicprevacct_objid as item_objid
	from province_taxaccount_mapping
	union 
	select 
		basicprevintacct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'previous' as revperiod,
		'basicint' as revtype,
		basicprevintacct_objid as item_objid
	from province_taxaccount_mapping
	union 
	select 
		basicprioracct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'prior' as revperiod,
		'basic' as revtype,
		basicprioracct_objid as item_objid
	from province_taxaccount_mapping
	union 
	select 
		basicpriorintacct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'prior' as revperiod,
		'basicint' as revtype,
		basicpriorintacct_objid as item_objid
	from province_taxaccount_mapping
	union 
	select 
		basiccurracct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'current' as revperiod,
		'basic' as revtype,
		basiccurracct_objid as item_objid
	from province_taxaccount_mapping
	union 
	select 
		basiccurrintacct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'current' as revperiod,
		'basicint' as revtype,
		basiccurrintacct_objid as item_objid
	from province_taxaccount_mapping
	union 
	select 
		basicidlecurracct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'current' as revperiod,
		'basicidle' as revtype,
		basicidlecurracct_objid as item_objid
	from province_taxaccount_mapping
	union 
	select 
		basicidlecurrintacct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'current' as revperiod,
		'basicidleint' as revtype,
		basicidlecurrintacct_objid as item_objid
	from province_taxaccount_mapping
	union 
	select 
		basicidleprevacct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'previous' as revperiod,
		'basicidle' as revtype,
		basicidleprevacct_objid as item_objid
	from province_taxaccount_mapping
	union 
	select 
		basicidleprevintacct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'previous' as revperiod,
		'basicidleint' as revtype,
		basicidleprevintacct_objid as item_objid
	from province_taxaccount_mapping
	union 
	select 
		basicidleadvacct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'advance' as revperiod,
		'basicidle' as revtype,
		basicidleadvacct_objid as item_objid
	from province_taxaccount_mapping
	union 
	select 
		sefadvacct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'advance' as revperiod,
		'sef' as revtype,
		sefadvacct_objid as item_objid
	from province_taxaccount_mapping
	union 
	select 
		sefprevacct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'previous' as revperiod,
		'sef' as revtype,
		sefprevacct_objid as item_objid
	from province_taxaccount_mapping
	union 
	select 
		sefprevintacct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'previous' as revperiod,
		'sefint' as revtype,
		sefprevintacct_objid as item_objid
	from province_taxaccount_mapping
	union 
	select 
		sefprioracct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'prior' as revperiod,
		'sef' as revtype,
		sefprioracct_objid as item_objid
	from province_taxaccount_mapping
	union 
	select 
		sefpriorintacct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'prior' as revperiod,
		'sefint' as revtype,
		sefpriorintacct_objid as item_objid
	from province_taxaccount_mapping
	union 
	select 
		sefcurracct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'current' as revperiod,
		'sef' as revtype,
		sefcurracct_objid as item_objid
	from province_taxaccount_mapping
	union 
	select 
		sefcurrintacct_objid as objid,
		(select objid from sys_org where orgclass = 'province') as lgu_objid,
		'current' as revperiod,
		'sefint' as revtype,
		sefcurrintacct_objid as item_objid
	from province_taxaccount_mapping
)x
where x.item_objid is not null 
and exists(select * from sys_org where objid = x.lgu_objid)



[migrateMunicipalityMappings]
insert into landtax_lgu_account_mapping(
	objid,
	lgu_objid,
	revperiod,
	revtype,
	item_objid
)
select distinct 
	x.objid,
	x.lgu_objid,
	x.revperiod,
	x.revtype,
	x.item_objid
from (
	select 
		basicadvacct_objid as objid,
		lguid as lgu_objid,
		'advance' as revperiod,
		'basic' as revtype,
		basicadvacct_objid as item_objid
	from municipality_taxaccount_mapping
	union 
	select 
		basicprevacct_objid as objid,
		lguid as lgu_objid,
		'previous' as revperiod,
		'basic' as revtype,
		basicprevacct_objid as item_objid
	from municipality_taxaccount_mapping
	union 
	select 
		basicprevintacct_objid as objid,
		lguid as lgu_objid,
		'previous' as revperiod,
		'basicint' as revtype,
		basicprevintacct_objid as item_objid
	from municipality_taxaccount_mapping
	union 
	select 
		basicprioracct_objid as objid,
		lguid as lgu_objid,
		'prior' as revperiod,
		'basic' as revtype,
		basicprioracct_objid as item_objid
	from municipality_taxaccount_mapping
	union 
	select 
		basicpriorintacct_objid as objid,
		lguid as lgu_objid,
		'prior' as revperiod,
		'basicint' as revtype,
		basicpriorintacct_objid as item_objid
	from municipality_taxaccount_mapping
	union 
	select 
		basiccurracct_objid as objid,
		lguid as lgu_objid,
		'current' as revperiod,
		'basic' as revtype,
		basiccurracct_objid as item_objid
	from municipality_taxaccount_mapping
	union 
	select 
		basiccurrintacct_objid as objid,
		lguid as lgu_objid,
		'current' as revperiod,
		'basicint' as revtype,
		basiccurrintacct_objid as item_objid
	from municipality_taxaccount_mapping
	union 
	select 
		basicidlecurracct_objid as objid,
		lguid as lgu_objid,
		'current' as revperiod,
		'basicidle' as revtype,
		basicidlecurracct_objid as item_objid
	from municipality_taxaccount_mapping
	union 
	select 
		basicidlecurrintacct_objid as objid,
		lguid as lgu_objid,
		'current' as revperiod,
		'basicidleint' as revtype,
		basicidlecurrintacct_objid as item_objid
	from municipality_taxaccount_mapping
	union 
	select 
		basicidleprevacct_objid as objid,
		lguid as lgu_objid,
		'previous' as revperiod,
		'basicidle' as revtype,
		basicidleprevacct_objid as item_objid
	from municipality_taxaccount_mapping
	union 
	select 
		basicidleprevintacct_objid as objid,
		lguid as lgu_objid,
		'previous' as revperiod,
		'basicidleint' as revtype,
		basicidleprevintacct_objid as item_objid
	from municipality_taxaccount_mapping
	union 
	select 
		basicidleadvacct_objid as objid,
		lguid as lgu_objid,
		'advance' as revperiod,
		'basicidle' as revtype,
		basicidleadvacct_objid as item_objid
	from municipality_taxaccount_mapping
	union 
	select 
		sefadvacct_objid as objid,
		lguid as lgu_objid,
		'advance' as revperiod,
		'sef' as revtype,
		sefadvacct_objid as item_objid
	from municipality_taxaccount_mapping
	union 
	select 
		sefprevacct_objid as objid,
		lguid as lgu_objid,
		'previous' as revperiod,
		'sef' as revtype,
		sefprevacct_objid as item_objid
	from municipality_taxaccount_mapping
	union 
	select 
		sefprevintacct_objid as objid,
		lguid as lgu_objid,
		'previous' as revperiod,
		'sefint' as revtype,
		sefprevintacct_objid as item_objid
	from municipality_taxaccount_mapping
	union 
	select 
		sefprioracct_objid as objid,
		lguid as lgu_objid,
		'prior' as revperiod,
		'sef' as revtype,
		sefprioracct_objid as item_objid
	from municipality_taxaccount_mapping
	union 
	select 
		sefpriorintacct_objid as objid,
		lguid as lgu_objid,
		'prior' as revperiod,
		'sefint' as revtype,
		sefpriorintacct_objid as item_objid
	from municipality_taxaccount_mapping
	union 
	select 
		sefcurracct_objid as objid,
		lguid as lgu_objid,
		'current' as revperiod,
		'sef' as revtype,
		sefcurracct_objid as item_objid
	from municipality_taxaccount_mapping
	union 
	select 
		sefcurrintacct_objid as objid,
		lguid as lgu_objid,
		'current' as revperiod,
		'sefint' as revtype,
		sefcurrintacct_objid as item_objid
	from municipality_taxaccount_mapping
)x
where x.item_objid is not null 
and exists(select * from sys_org where objid = x.lgu_objid)


[getLgus]
select o.objid, o.code, o.name, o.orgclass 
from sys_org o 
where o.orgclass in ('city', 'province', 'municipality', 'barangay')
order by o.code 
