
insert into etracs254_kolambugan.sys_org(
	objid,
	name,
	orgclass,
	parent_objid,
	parent_orgclass,
	code,
	root
)
select 
	'057' as objid,
	'LANAO DEL NORTE'  as name,
	'PROVINCE' as orgclass,
	null as parent_objid,
	null as parent_orgclass,
	'057' as code,
	0 as root;


insert into etracs254_kolambugan.province(
	objid,
	state,
	indexno,
	pin,
	name,
	parentid,
	address,
	fullname
)
select 
	'057' as objid,
	'DRAFT' as state,
	'057' as indexno,
	'057' as pin,
	'LANAO DEL NORTE' as name,
	null as parentid,
	'LANAO DEL NORTE' as address,
	'PROVINCE OF LANAO DEL NORTE' as fullname;

		

insert into etracs254_kolambugan.sys_org(
	objid,
	name,
	orgclass,
	parent_objid,
	parent_orgclass,
	code,
	root
)
select 
	'05707' as objid,
	'KOLAMBUGAN'  as name,
	'MUNICIPALITY' as orgclass,
	'057' as parent_objid,
	'PROVINCE' as parent_orgclass,
	'057-07' as code,
	1 as root;


insert into etracs254_kolambugan.municipality(
	objid,
	state,
	indexno,
	pin,
	name,
	parentid,
	address,
	fullname
)
select 
	'05707' as objid,
	'DRAFT' as state,
	'07' as indexno,
	'057-07' as pin,
	'KOLAMBUGAN' as name,
	'057' as parentid,
	'KOLAMBUGAN, LANAO DEL NORTE' as address,
	'MUNICIPALITY OF TUBOD' as fullname;





create table itax_kolambugan.xbarangay
(
	objid varchar(50) not null,
	indexno varchar(4),
	pin varchar(15),
	name varchar(50),
	provid varchar(50),
	muniid varchar(50),
	primary key(objid)
);



insert into itax_kolambugan.xbarangay(
	objid, indexno, pin, name, provid, muniid 
)
select distinct 
	concat(province_ct, '-', case when length(municipal_id) = 2 then municipal_id else concat('0', municipal_id) end, '-', concat('0',barangay_ct)) as objid, 
	concat('0', barangay_ct) as indexno, 
	concat(province_ct, '-', case when length(municipal_id) = 2 then municipal_id else concat('0', municipal_id) end, '-', concat('0',barangay_ct)) as pin, 
	concat(province_ct, '-', case when length(municipal_id) = 2 then municipal_id else concat('0', municipal_id) end, '-', concat('0',barangay_ct)) as name,
	p.province_ct as provid,
	concat('057', case when length(municipal_id) = 2 then municipal_id else concat('0', municipal_id) end) as muniid
from itax_kolambugan.property p 
where province_ct is not null 
 and p.municipal_id is not null 
 and p.barangay_ct is not null;



insert into etracs254_kolambugan.sys_org(
	objid,
	name,
	orgclass,
	parent_objid,
	parent_orgclass,
	code,
	root
)
select 
	objid,
	name,
	'BARANGAY' as orgclass,
	muniid as parent_objid,
	'MUNICIPALITY' as parent_orgclass,
	objid as code,
	0 as root
from itax_kolambugan.xbarangay;


insert into etracs254_kolambugan.barangay(
	objid,
	state,
	indexno,
	pin,
	name,
	parentid,
	address,
	fullname
)
select 
	objid,
	'DRAFT' as state,
	indexno,
	pin,
	name,
	muniid as parentid,
	concat('BARANGAY ', name) as address,
	concat('BARANGAY ', name) as fullname
from itax_kolambugan.xbarangay;




create index ix_rptassessment_taxtrans_id on itax_kolambugan.rptassessment(taxtrans_id);
create index ix_rptassessment_gryear on itax_kolambugan.rptassessment(gryear);
create index ix_rptassessment_tdno on itax_kolambugan.rptassessment(tdno);
create index ix_property_proptype on itax_kolambugan.property(propertykind_ct);

alter table itax_kolambugan.property add barangayid varchar(50);

update itax_kolambugan.property p, itax_kolambugan.xbarangay b set 
	p.barangayid = b.objid 
where concat('0',p.barangay_ct) = b.indexno;



alter table etracs254_kolambugan.realproperty modify column north varchar(500);
alter table etracs254_kolambugan.realproperty modify column south varchar(500);
alter table etracs254_kolambugan.realproperty modify column east varchar(500);
alter table etracs254_kolambugan.realproperty modify column west varchar(500);
alter table etracs254_kolambugan.realproperty modify column surveyno varchar(255);


insert into etracs254_kolambugan.realproperty(
	objid,
	state,
	autonumber,
	pintype,
	pin,
	ry,
	claimno,
	section,
	parcel,
	cadastrallotno,
	blockno,
	surveyno,
	street,
	purok,
	north,
	south,
	east,
	west,
	barangayid,
	lgutype,
	previd,
	lguid
)
select 
	a.taxtrans_id as objid,
	'INTERIM' as state,
	0 as autonumber,
	'new' as pintype,
	p.pinno as pin,
	a.gryear as ry,
	null as claimno,
	substring(p.pinno, 13, 3) as section,
	substring(p.pinno, 17, 2) as parcel,
	p.cadastrallotno,
	p.blkno as blockno,
	p.surveyno,
	null as street,
	null as purok,
	p.boundarynorth as north,
	p.boundarysouth as south,
	p.boundaryeast as east,
	p.boundarywest as west,
	p.barangayid,
	'MUNICIPALITY' as lgutype,
	null as previd,
	replace(substring(p.pinno, 1, 6), '-','') as lguid
from itax_kolambugan.rptassessment a
	inner join itax_kolambugan.property p on a.prop_id = p.prop_id 
where a.gryear = 2013
and p.propertykind_ct = 'L';



insert into etracs254_kolambugan.rpumaster(
	objid,
	currentfaasid,
	currentrpuid
)
select 
	a.taxtrans_id as objid,
	a.taxtrans_id as currentfaasid,
	a.taxtrans_id as currentrpuid
from itax_kolambugan.rptassessment a
	inner join itax_kolambugan.property p on a.prop_id = p.prop_id 
where a.tdno like 'h%';





alter table itax_kolambugan.rptassessment add classid varchar(50);
update itax_kolambugan.rptassessment a set classid  = (select objid from etracs254_kolambugan.propertyclassification where code = a.predomclasscode_ct)

alter table itax_kolambugan.property 
	add suffix int,
	add csuffix varchar(10),
	add landpin varchar(50);


update itax_kolambugan.rptassessment a, itax_kolambugan.property p  set 
	p.csuffix = 0,
	p.suffix = 0
where a.prop_id = p.prop_id 
 and a.tdno like 'h%'
 and p.propertykind_ct = 'L';


update itax_kolambugan.rptassessment a, itax_kolambugan.property p  set 
	p.csuffix = SUBSTRING_INDEX(pinno,'-',-1)
where a.prop_id = p.prop_id 
 and a.tdno like 'h%'
 and p.propertykind_ct <> 'L';


update itax_kolambugan.property set 
	suffix = csuffix 
where csuffix not like 'B%' and length(csuffix) = 4;


update rptassessment a, property p  set 
	landpin = replace(replace(SUBSTRING_INDEX(pinno,'-',5), '(', ''), ')','') 
where a.tdno like 'h%'
 and  a.prop_id = p.prop_id 
 and p.propertykind_ct <> 'L';


update rptassessment a, property p set
 landpin = pinno 
where a.tdno like 'h%'
 and  a.prop_id = p.prop_id 
 and p.propertykind_ct = 'L';




insert into etracs254_kolambugan.rpu(
	objid,
	state,
	realpropertyid,
	rputype,
	ry,
	fullpin,
	suffix,
	subsuffix,
	classification_objid,
	exemptiontype_objid,
	taxable,
	totalareaha,
	totalareasqm,
	totalbmv,
	totalmv,
	totalav,
	hasswornamount,
	swornamount,
	useswornamount,
	previd,
	rpumasterid,
	reclassed
)
select 
	a.taxtrans_id as objid,
	'INTERIM' as state,
	a.taxtrans_id as realpropertyid,
	case 
		when p.propertykind_ct = 'L' then 'land' 
		when p.propertykind_ct = 'B' then 'bldg' 
		when p.propertykind_ct = 'M' then 'mach' 
		else 'misc'
	end as rputype,
	a.gryear as ry,
	p.pinno,
	p.suffix, 
	null as subsuffix,
	a.classid as classification_objid,
	null as exemptiontype_objid,
	a.taxability_bv as taxable,
	0 as totalareaha,
	0 as totalareasqm,
	0 as totalbmv,
	0 as totalmv,
	0 as totalav,
	0 as hasswornamount,
	0 as swornamount,
	0 as useswornamount,
	null as previd,
	a.taxtrans_id as rpumasterid,
	0 as reclassed
from itax_kolambugan.rptassessment a
	inner join itax_kolambugan.property p on a.prop_id = p.prop_id 
where a.tdno like 'h%'
and p.suffix is not null;



update etracs254_kolambugan.rpu r set 
	r.realpropertyid = (
				select max(objid) 
				from etracs254_kolambugan.realproperty r
				where replace(replace(r.fullpin, '(',''), ')','') like concat(pin, '%')
)
where r.rputype <> 'land' ;




insert into etracs254_kolambugan.landrpu(
	objid,
	idleland,
	totallandbmv,
	totallandmv,
	totallandav,
	totalplanttreebmv,
	totalplanttreemv,
	totalplanttreeadjustment,
	totalplanttreeav,
	landvalueadjustment
)
select 
	a.taxtrans_id as objid,
	0 as idleland,
	0 as totallandbmv,
	0 as totallandmv,
	0 as totallandav,
	0 as totalplanttreebmv,
	0 as totalplanttreemv,
	0 as totalplanttreeadjustment,
	0 as totalplanttreeav,
	0 as landvalueadjustment
from itax_kolambugan.rptassessment a
	inner join itax_kolambugan.property p on a.prop_id = p.prop_id 
where a.tdno like 'h%'
and p.propertykind_ct = 'L'
and p.suffix is not null
and exists(select * from etracs254_kolambugan.rpu where objid = a.taxtrans_id);


insert into etracs254_kolambugan.landdetail(
	objid,
	landrpuid,
	subclass_objid,
	specificclass_objid,
	actualuse_objid,
	stripping_objid,
	striprate,
	areatype,
	addlinfo,
	area,
	areasqm,
	areaha,
	basevalue,
	unitvalue,
	taxable,
	basemarketvalue,
	adjustment,
	landvalueadjustment,
	actualuseadjustment,
	marketvalue,
	assesslevel,
	assessedvalue
)
select
	la.landappraisal_id as objid,
	a.taxtrans_id as landrpuid,
	sub.objid as subclass_objid,
	spc.objid as specificclass_objid,
	lvl.objid as actualuse_objid,
	null as stripping_objid,
	0 as striprate,
	case when la.classcode_ct = 'A' then 'HA' else 'SQM' end as  areatype,
	null as addlinfo,
	case when la.classcode_ct = 'A' then la.area/10000.0 else la.area end as area,
	la.area as areasqm,
	la.area / 10000 as areaha,
	la.unitvalue as basevalue,
	la.unitvalue asunitvalue,
	1 as taxable,
	(la.unitvalue * la.area) basemarketvalue,
	0 as adjustment,
	0 as landvalueadjustment,
	0 as actualuseadjustment,
	la.marketvalue,
	d.assessmentlevel as assesslevel,
	d.assessedvalue as assessedvalue
from 	itax_kolambugan.rptassessment a 
	inner join itax_kolambugan.rptlandappraisal la on a.taxtrans_id = la.taxtrans_id
	inner join etracs254_kolambugan.landassesslevel lvl on la.actualuse_ct = lvl.code 
	inner join etracs254_kolambugan.lcuvsubclass sub on sub.objid  like concat('%',la.sfmvland_id) 
	inner join itax_kolambugan.rptassessmentdetail d on a.taxtrans_id = d.taxtrans_id and la.taxdecdetail_id = d.taxdecdetail_id
inner join etracs254_kolambugan.lcuvspecificclass spc on sub.specificclass_objid = spc.objid 
where a.gryear = 2013
 and d.propertykind_ct = 'L';



 insert into etracs254_kolambugan.rpu_assessment
(
	objid,
	rpuid,
	classification_objid,
	actualuse_objid,
	areasqm,
	areaha,
	marketvalue,
	assesslevel,
	assessedvalue,
	rputype
)
select
	la.landappraisal_id as objid,
	a.taxtrans_id as rpuid,
	r.classification_objid,
	lvl.objid as actualuse_objid,
	la.area as  areasqm,
	la.area/10000 as areaha,
	d.marketvalue as  marketvalue,
	d.assessmentlevel * 100 as assesslevel,
	d.assessedvalue,
	case 
	when d.propertykind_ct = 'L' then 'land'
	when d.propertykind_ct = 'B' then 'bldg'
	when d.propertykind_ct = 'M' then 'mach'
	when d.propertykind_ct = 'P' then 'planttree'
	else 'misc'
	end as rputype
-- select *
from 	itax_kolambugan.rptassessment a 
	inner join etracs254_kolambugan.rpu r on a.taxtrans_id = r.objid 
	inner join itax_kolambugan.rptlandappraisal la on a.taxtrans_id = la.taxtrans_id
	inner join etracs254_kolambugan.landassesslevel lvl on la.actualuse_ct = lvl.code 
	inner join itax_kolambugan.rptassessmentdetail d on a.taxtrans_id = d.taxtrans_id and la.taxdecdetail_id = d.taxdecdetail_id
where a.gryear = 2013;


create table etracs254_kolambugan.xrputotal
(
	objid varchar(50),
	areaha decimal(18,6),
	areasqm decimal(18,6),
	totalbmv decimal(18,2),
	totalmv decimal(18,2),
	totalav decimal(18,2),
	primary key(objid)
);


insert into etracs254_kolambugan.xrputotal(
	objid, areaha, areasqm, totalbmv, totalmv, totalav 
)
select 
		landrpuid as objid, 
		sum(areaha) as areaha,
		sum(areasqm) as areasqm,
		sum(basemarketvalue) as totalbmv,
		sum(marketvalue) as totalmv,
		sum(assessedvalue) as totalav
	from etracs254_kolambugan.landdetail 
	group by landrpuid ;



update etracs254_kolambugan.rpu r, etracs254_kolambugan.xrputotal x set 
	r.totalareaha = x.areaha,
	r.totalareasqm = x.areasqm,
	r.totalbmv = x.totalbmv,
	r.totalmv = x.totalmv,
	r.totalav = x.totalav
where r.objid = x.objid
  and r.rputype = 'land';


drop table etracs254_kolambugan.xrputotal;







insert into etracs254_kolambugan.bldgrpu(
	objid,
	landrpuid,
	houseno,
	psic,
	permitno,
	permitdate,
	permitissuedby,
	bldgtype_objid,
	bldgkindbucc_objid,
	basevalue,
	dtcompleted,
	dtoccupied,
	floorcount,
	depreciation,
	depreciationvalue,
	totaladjustment,
	additionalinfo,
	bldgage,
	percentcompleted,
	bldgassesslevel_objid,
	assesslevel,
	condominium,
	bldgclass,
	predominant,
	effectiveage,
	condocerttitle,
	dtcertcompletion,
	dtcertoccupancy
)
select 
	a.taxtrans_id as objid,
	(select max(objid) from etracs254_kolambugan.rpu xr where p.landpin = xr.fullpin and xr.rputype = 'land') AS landrpuid,
	null as houseno,
	null as psic,
	null as permitno,
	null as permitdate,
	null as permitissuedby,
	null as bldgtype_objid,
	null as bldgkindbucc_objid,
	0.0  as basevalue,
	null as dtcompleted,
	null as dtoccupied,
	1 as floorcount,
	0.0  as depreciation,
	0.0  as depreciationvalue,
	0.0  as totaladjustment,
	null as additionalinfo,
	0 as bldgage,
	100 as percentcompleted,
	null as bldgassesslevel_objid,
	0 as assesslevel,
	0 as condominium,
	null as bldgclass,
	0 as predominant,
	0 as effectiveage,
	null as condocerttitle,
	null as dtcertcompletion,
	null as dtcertoccupancy
from itax_kolambugan.rptassessment a
	inner join itax_kolambugan.property p on a.prop_id = p.prop_id 
where a.tdno like 'h%'
and p.propertykind_ct = 'B'
and p.suffix is not null
and exists(select * from etracs254_kolambugan.rpu where objid = a.taxtrans_id);



update etracs254_kolambugan.bldgrpu b, itax_kolambugan.rptbldginfo bi  set
	b.permitno = bi.permitno,
	b.permitdate = bi.permitdate,
	b.dtcompleted = case 
		when bi.yearcompleted is not null then concat(bi.yearcompleted, '-01-01') 
		when bi.yearoccupied is not null then concat(bi.yearoccupied, '-01-01') 
		when bi.yearconstructed is not null then concat(bi.yearconstructed, '-01-01') 
	end , 
	b.dtoccupied = case 
		when bi.yearoccupied is not null then concat(bi.yearoccupied, '-01-01') 
		when bi.yearconstructed is not null then concat(bi.yearconstructed, '-01-01') 
	end , 
	b.floorcount = ifnull(bi.numstorey,1), 
	b.bldgage = bi.bldgage, 
	b.effectiveage = bi.bldgage, 
	b.condocerttitle = bi.cct, 
	b.dtcertcompletion = bi.cctcompissued, 
	b.dtcertoccupancy = bi.cctoccissued 
where b.objid = bi.taxtrans_id;



insert into etracs254_kolambugan.bldgstructure 
(
	objid,
	bldgrpuid,
	structure_objid,
	material_objid,
	floor
)
select 
	concat(br.objid, '-', st.code) objid,
	br.objid as bldgrpuid,
	st.objid as structure_objid,
	null as material_objid,
	1 as floor
from etracs254_kolambugan.bldgrpu br, etracs254_kolambugan.structure st ;





alter table itax_kolambugan.rptbldgfloor add bldgtypeid varchar(50);

update itax_kolambugan.rptbldgfloor bf,
	itax_kolambugan.t_bldgclass bc
set 
	bldgtypeid = concat('BLDG-05707-2013-BT', bf.structuretype_ct, '-', replace(bc.description, 'CLASS ', ''))
where bf.bldgclass_ct = bc.code;





insert into etracs254_kolambugan.bldgrpu_structuraltype
(
	objid,
	bldgrpuid,
	bldgtype_objid,
	bldgkindbucc_objid,
	floorcount,
	basefloorarea,
	totalfloorarea,
	basevalue,
	unitvalue,
	classification_objid
)
select
	bf.floor_id as objid,
	a.taxtrans_id as bldgrpuid,
	bf.bldgtypeid as bldgtype_objid,
	bf.sfmvbldgcost_id as bldgkindbucc_objid,
	1 as floorcount,
	bf.area as basefloorarea,
	bf.area as totalfloorarea,
	bf.unitvalue as basevalue,
	bf.unitvalue as unitvalue,
	a.classid as classification_objid
from itax_kolambugan.rptassessment a
	inner join itax_kolambugan.rptbldgfloor bf on a.taxtrans_id = bf.taxtrans_id
	inner join itax_kolambugan.t_classification c on bf.classcode_ct = c.code 
	inner join etracs254_kolambugan.bldgrpu br on a.taxtrans_id = br.objid 
	inner join etracs254_kolambugan.bldgkindbucc bucc on bf.sfmvbldgcost_id = bucc.objid;



insert into etracs254_kolambugan.bldguse 
(
	objid,
	bldgrpuid,
	structuraltype_objid,
	actualuse_objid,
	basevalue,
	area,
	basemarketvalue,
	depreciationvalue,
	adjustment,
	marketvalue,
	assesslevel,
	assessedvalue
)
select
	bf.floor_id as objid,
	a.taxtrans_id as bldgrpuid,
	bf.floor_id as  structuraltype_objid,
	concat('BLDG-05707-2013-', bf.actualuse_ct) as actualuse_objid,
	bf.unitvalue as basevalue,
	bf.area,
	bf.unitvalue * bf.area as basemarketvalue,
	0 as depreciationvalue,
	0 as adjustment,
	bf.marketvalue,
	dd.assessmentlevel * 100 as assesslevel,
	dd.assessedvalue
from itax_kolambugan.rptassessment a
	inner join itax_kolambugan.rptbldgfloor bf on a.taxtrans_id = bf.taxtrans_id
	inner join etracs254_kolambugan.bldgrpu br on a.taxtrans_id = br.objid 
	inner join itax_kolambugan.rptassessmentdetail dd on a.taxtrans_id = dd.taxtrans_id
	inner join etracs254_kolambugan.bldgrpu_structuraltype st on bf.floor_id = st.objid 
	inner join etracs254_kolambugan.bldgassesslevel lvl on bf.actualuse_ct = lvl.code 
where bf.taxdecdetail_id = dd.taxdecdetail_id;



insert into etracs254_kolambugan.bldgfloor 
(
	objid,
	bldguseid,
	bldgrpuid,
	floorno,
	area,
	storeyrate,
	basevalue,
	unitvalue,
	basemarketvalue,
	adjustment,
	marketvalue
)
select
	bf.floor_id as objid,
	bf.floor_id as objid,
	a.taxtrans_id as bldgrpuid,
	ifnull(bf.floornumber,1) as floorno,
	bf.area as area,
	0 as storeyrate,
	bf.unitvalue as basevalue,
	bf.unitvalue asunitvalue,
	bf.area * bf.unitvalue as basemarketvalue,
	0 as adjustment,
	bf.marketvalue
from itax_kolambugan.rptassessment a
	inner join itax_kolambugan.rptbldgfloor bf on a.taxtrans_id = bf.taxtrans_id
	inner join etracs254_kolambugan.bldgrpu br on a.taxtrans_id = br.objid 
	inner join itax_kolambugan.rptassessmentdetail dd on a.taxtrans_id = dd.taxtrans_id
	inner join etracs254_kolambugan.bldgrpu_structuraltype st on bf.floor_id = st.objid 
	inner join etracs254_kolambugan.bldgassesslevel lvl on bf.actualuse_ct = lvl.code 
where bf.taxdecdetail_id = dd.taxdecdetail_id;





 insert into etracs254_kolambugan.rpu_assessment
(
	objid,
	rpuid,
	classification_objid,
	actualuse_objid,
	areasqm,
	areaha,
	marketvalue,
	assesslevel,
	assessedvalue,
	rputype
)
select
	concat(a.taxtrans_id, '-', d.actualuse_ct) as objid,
	a.taxtrans_id as rpuid,
	ifnull(a.classid, 'RESIDENTIAL') as classification_objid,
	lvl.objid as actualuse_objid,
	0 as  areasqm,
	0 as areaha,
	d.marketvalue as  marketvalue,
	d.assessmentlevel * 100 as assesslevel,
	d.assessedvalue,
	'bldg' as rputype
-- select *
from itax_kolambugan.rptassessment a
	inner join etracs254_kolambugan.bldgrpu br on a.taxtrans_id = br.objid 
	inner join itax_kolambugan.rptassessmentdetail d on a.taxtrans_id = d.taxtrans_id 
	inner join etracs254_kolambugan.bldgassesslevel lvl on d.actualuse_ct = lvl.code;




create table etracs254_kolambugan.xrputotal
(
	objid varchar(50),
	areaha decimal(18,6),
	areasqm decimal(18,6),
	totalbmv decimal(18,2),
	totalmv decimal(18,2),
	totalav decimal(18,2),
	primary key(objid)
);


insert into etracs254_kolambugan.xrputotal(
	objid, areaha, areasqm, totalbmv, totalmv, totalav 
)
select 
		bldgrpuid  as objid, 
		sum(area/10000) as areaha,
		sum(area) as areasqm,
		sum(basemarketvalue) as totalbmv,
		sum(marketvalue) as totalmv,
		sum(assessedvalue) as totalav
	from etracs254_kolambugan.bldguse
	group by bldgrpuid  ;





update etracs254_kolambugan.rpu r, etracs254_kolambugan.xrputotal x set 
	r.totalareaha = x.areaha,
	r.totalareasqm = x.areasqm,
	r.totalbmv = x.totalbmv,
	r.totalmv = x.totalmv,
	r.totalav = x.totalav
where r.objid = x.objid
  and r.rputype = 'bldg';


drop table etracs254_kolambugan.xrputotal;









insert into etracs254_kolambugan.machrpu(
	objid,
	landrpuid
)
select 
	a.taxtrans_id as objid,
	(select max(objid) from etracs254_kolambugan.rpu xr where p.landpin = xr.fullpin and xr.rputype = 'land') AS landrpuid
from itax_kolambugan.rptassessment a
	inner join itax_kolambugan.property p on a.prop_id = p.prop_id 
where a.tdno like 'h%'
and p.propertykind_ct = 'M'
and p.suffix is not null
and exists(select * from etracs254_kolambugan.rpu where objid = a.taxtrans_id);





create table itax_kolambugan.xduplicate_tdno
(
	tdno varchar(50),
	icount int, 
	primary key(tdno)
);

insert into itax_kolambugan.xduplicate_tdno
	(tdno, icount) 
select 
	a.tdno,
	count(*) as icount 
from itax_kolambugan.rptassessment a
where a.tdno like 'h%'
group by a.tdno 
having count(*)> 1;




insert into etracs254_kolambugan.faas(
	objid,
	state,
	rpuid,
	datacapture,
	autonumber,
	utdno,
	tdno,
	txntype_objid,
	effectivityyear,
	effectivityqtr,
	titletype,
	titleno,
	titledate,
	taxpayer_objid,
	taxpayer_name,
	taxpayer_address,
	owner_name,
	owner_address,
	administrator_objid,
	administrator_name,
	administrator_address,
	beneficiary_objid,
	beneficiary_name,
	beneficiary_address,
	memoranda,
	cancelnote,
	restrictionid,
	backtaxyrs,
	prevtdno,
	prevpin,
	prevowner,
	prevav,
	prevmv,
	cancelreason,
	canceldate,
	cancelledbytdnos,
	lguid,
	txntimestamp,
	cancelledtimestamp,
	name,
	dtapproved,
	realpropertyid,
	lgutype,
	signatories,
	ryordinanceno,
	ryordinancedate,
	prevareaha,
	prevareasqm,
	fullpin,
	preveffectivity,
	year,
	qtr,
	month,
	day,
	cancelledyear,
	cancelledqtr,
	cancelledmonth,
	cancelledday,
	prevadministrator,
	originlguid
)
select 
	a.taxtrans_id as objid,
	'INTERIM' as state,
	a.taxtrans_id as rpuid,
	1 as datacapture,
	0 as autonumber,
	a.tdno as utdno,
	a.tdno as tdno,
	'GR' as txntype_objid,
	a.startyear as effectivityyear,
	a.startquarter as effectivityqtr,
	null as titletype,
	p.certificatetitleno as titleno,
	null as titledate,
	null as taxpayer_objid,
	null as taxpayer_name,
	null as taxpayer_address,
	t.ownername  as owner_name,
	t.owneraddress as owner_address,
	null as administrator_objid,
	null as administrator_name,
	null as administrator_address,
	null as beneficiary_objid,
	null as beneficiary_name,
	null as beneficiary_address,
	a.memoranda as memoranda,
	null as cancelnote,
	null as restrictionid,
	0 as backtaxyrs,
	null as prevtdno,
	null as prevpin,
	null as prevowner,
	0 as prevav,
	0 as prevmv,
	null as cancelreason,
	null as canceldate,
	null as cancelledbytdnos,
	'05707' as lguid,
	null as txntimestamp,
	null as cancelledtimestamp,
	substring(t.ownername,1, 50) as name,
	a.approveddate as dtapproved,
	xr.realpropertyid as realpropertyid,
	'MUNICIPALITY' as lgutype,
	'[]' as signatories,
	null as ryordinanceno,
	null as ryordinancedate,
	0 as prevareaha,
	0 as prevareasqm,
	p.pinno as fullpin, 
	0 as preveffectivity,
	year(a.approveddate) as year,
	quarter(a.approveddate) as  qtr,
	month(a.approveddate) as  month,
	day(a.approveddate) as day,
	null as cancelledyear,
	null as cancelledqtr,
	null as cancelledmonth,
	null as cancelledday,
	null as prevadministrator,
	'05707' as originlguid
from itax_kolambugan.rptassessment a
	inner join itax_kolambugan.property p on a.prop_id = p.prop_id 
	inner join itax_kolambugan.propertyowner po on p.prop_id = po.prop_id
	inner join itax_kolambugan.taxpayer t on po.local_tin = t.local_tin
	inner join etracs254_kolambugan.rpu xr on a.taxtrans_id = xr.objid 
where a.tdno like 'h%'
and p.suffix is not null
and exists(select * from etracs254_kolambugan.rpu where objid = a.taxtrans_id)
and not exists(select * from itax_kolambugan.xduplicate_tdno where tdno = a.tdno);





update etracs254_kolambugan.faas f, itax_kolambugan.rptcancelled p set 
	f.prevtdno = p.cancelledtdno,
	f.prevpin = p.cancelledpinno, 
	f.prevowner = p.prevowner,
	f.prevav = ifnull(p.prevassessedvalue,0),
	f.prevmv = 0.0, 
	f.prevareaha = 0.0,
	f.prevareasqm = 0.0,
	f.preveffectivity = p.prevstartyear,
	f.prevadministrator = null 
where f.objid = p.taxtrans_id;





insert into etracs254_kolambugan.faas_signatory(objid)
select objid from etracs254_kolambugan.faas;



update etracs254_kolambugan.faas_signatory f, itax_kolambugan.rptassessment a set 
	f.approver_name = a.approvedby,
	f.approver_title = '',
	f.approver_dtsigned = a.approveddate
where f.objid = a.taxtrans_id ;



update etracs254_kolambugan.faas_signatory f, itax_kolambugan.rptassessment a set 
	f.recommender_name = a.recomapprovedby,
	f.recommender_title = '',
	f.recommender_dtsigned = a.recomapproveddate
where f.objid = a.taxtrans_id ;







/* =========================================== */
delete from etracs254_kolambugan.machine;


insert into etracs254_kolambugan.machine (
	objid,
	state,
	code,
	name
)
select 
	code, 'DRAFT', code, description 
from itax_kolambugan.t_machinetype;


set FOREIGN_key_checks = 0;

update itax_kolambugan.rptmachappraisal set actualuse_ct = 'AC' where actualuse_ct = 'ACC';

set FOREIGN_key_checks = 1;



insert into etracs254_kolambugan.machuse(
	objid,
	machrpuid,
	actualuse_objid,
	basemarketvalue,
	marketvalue,
	assesslevel,
	assessedvalue
)
select distinct 
	concat(a.taxtrans_id, '-', ma.actualuse_ct) as objid,
	a.taxtrans_id as machrpuid,
	concat('MACH-05707-2013-', ma.actualuse_ct) as  actualuse_objid,
	0 as basemarketvalue,
	0 as marketvalue,
	0 as assesslevel,
	0 as assessedvalue
from 	itax_kolambugan.rptassessment a
	inner join itax_kolambugan.rptmachappraisal ma on a.taxtrans_id = ma.taxtrans_id
	inner join etracs254_kolambugan.machrpu mr on a.taxtrans_id = mr.objid 
where a.gryear = 2013 
  and ma.actualuse_ct <> 'MS';


insert into etracs254_kolambugan.machdetail(
	objid,
	machuseid,
	machrpuid,
	machine_objid,
	operationyear,
	replacementcost,
	depreciation,
	depreciationvalue,
	basemarketvalue,
	marketvalue,
	assesslevel,
	assessedvalue,
	brand,
	capacity,
	model,
	serialno,
	status,
	yearacquired,
	estimatedlife,
	remaininglife,
	yearinstalled,
	yearsused,
	originalcost,
	freightcost,
	insurancecost,
	installationcost,
	brokeragecost,
	arrastrecost,
	othercost,
	acquisitioncost,
	feracid,
	ferac,
	forexid,
	forex,
	residualrate,
	conversionfactor,
	swornamount,
	useswornamount,
	imported,
	newlyinstalled,
	autodepreciate
)

select 	
	ma.machappraisal_id as objid,
	concat(a.taxtrans_id, '-', ma.actualuse_ct) as machuseid,
	a.taxtrans_id as machrpuid,
	ma.machtype_ct as machine_objid,
	ma.yearoperation as operationyear,
	ma.marketvalue as replacementcost,
	ma.depreciation * 100 as depreciation,
	ma.depmarketvalue as depreciationvalue,
	ma.acquisitioncost as basemarketvalue,
	ma.marketvalue,
	lvl.rate as assesslevel,
	ad.assessedvalue,
	ma.brand,
	ma.capacity,
	ma.model,
	null as serialno,
	null as status,
	ma.yearacquired,
	ma.estimatedecolife as estimatedlife,
	ma.remainingecolife as remaininglife,
	ma.yearinstallation as yearinstalled,
	0 as yearsused,
	ma.acquisitioncost as originalcost,
	ifnull(ma.freightcost, 0) as  freightcost,
	ifnull(ma.insurancecost, 0) as  insurancecost,
	ifnull(ma.installationcost,0) as installationcost,
	0 as brokeragecost,
	0 as arrastrecost,
	ifnull(ma.othercost, 0) as othercost,
	ma.acquisitioncost,
	null as feracid,
	0 as ferac,
	null as forexid,
	0 as forex,
	0 as residualrate,
	1 as conversionfactor,
	0 as swornamount,
	0 as useswornamount,
	ma.imported_bv as imported,
	0 as newlyinstalled,
	0 as autodepreciate
from itax_kolambugan.rptassessment a
	inner join itax_kolambugan.rptassessmentdetail ad on a.taxtrans_id = ad.taxtrans_id 
	inner join itax_kolambugan.rptmachappraisal ma on a.taxtrans_id = ma.taxtrans_id and ad.taxdecdetail_id = ma.taxdecdetail_id 
	inner join etracs254_kolambugan.machrpu mr on a.taxtrans_id = mr.objid 
	inner join etracs254_kolambugan.machassesslevel lvl on concat('MACH-05707-2013-', ma.actualuse_ct) = lvl.objid 
where a.gryear = 2013 
  and ma.actualuse_ct <> 'MS';







