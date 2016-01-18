/* INDEXES */
create index ix_tdmain_taxdecnum on lgsp_tubod.tdmainibib (taxdecnum);



alter table lgsp_tubod.tdmainib
	add rputype varchar(50),
	add state varchar(50), 
	add fullpin varchar(50),
	add suffix long,
	add barangayid varchar(50);

create index ix_tdmain_rputype on lgsp_tubod.tdmainib(rputype);
create index ix_tdmain_state on lgsp_tubod.tdmainib(state);



update lgsp_tubod.tdmainib set rputype = 'land' where PINEXTN is null or LENGTH(TRIM(PINEXTN)) = 0;
update lgsp_tubod.tdmainib set rputype = 'bldg' where PINEXTN like 'B%' and rputype is null;
update lgsp_tubod.tdmainib set rputype = 'bldg' where PINEXTN like '1%' and rputype is null;
update lgsp_tubod.tdmainib set rputype = 'mach' where PINEXTN like 'M%' and rputype is null;
update lgsp_tubod.tdmainib set rputype = 'mach' where PINEXTN like '2%' and rputype is null;



update lgsp_tubod.tdmainib set state = 'CURRENT' where TAXDECNUM like 'H%';
update lgsp_tubod.tdmainib set state = 'CANCELLED' where TAXDECNUM not like 'H%';


update lgsp_tubod.tdmainib t set barangayid = null;

update lgsp_tubod.tdmainib t set 
	t.barangayid = (select objid from etracs254_tubod.barangay where name = t.BARANGAY);

-- kakai-renabor
update lgsp_tubod.tdmainib t set 
	t.barangayid = (select objid from etracs254_tubod.barangay where name = 'KAKAI-RENABOR')
where t.barangayid is null 
  and t.barangay = 'KAKAI RENABOR';



-- SANTO NIÑO
update lgsp_tubod.tdmainib t set 
	t.barangayid = (select objid from etracs254_tubod.barangay where name = 'SANTO NIÑO')
where t.barangayid is null 
  and t.barangay = 'SANTO NI¥O';




update lgsp_tubod.tdmainib t set 
	t.fullpin = t.PROPERTYINDEX,
	t.suffix = 0
where state = 'current' and length(PROPERTYINDEX) = 18;


update lgsp_tubod.tdmainib t set 
	t.fullpin = concat(t.barangayid, '-', substring(t.PROPERTYINDEX, 12)),
	t.suffix = 0
where state = 'current' and length(PROPERTYINDEX) < 18;


update lgsp_tubod.tdmainib t set 
	t.fullpin = concat(fullpin, '-', PINEXTN),
	t.suffix = t.PINEXTN
where state = 'current' and rputype = 'bldg' and PINEXTN like '1%' and length(PINEXTN) = 4;

update lgsp_tubod.tdmainib t set 
	t.fullpin = concat(fullpin, '-', replace(PINEXTN, 'B0', '1')),
	t.suffix = replace(PINEXTN, 'B0', '1')
where state = 'current' and rputype = 'bldg' and PINEXTN like 'b%';




update lgsp_tubod.tdmainib t set 
	t.fullpin = concat(fullpin, '-', PINEXTN),
	t.suffix = t.PINEXTN
where state = 'current' and rputype = 'mach' and PINEXTN like '2%' and length(PINEXTN) = 4;

update lgsp_tubod.tdmainib t set 
	t.fullpin = concat(fullpin, '-', replace(PINEXTN, 'M0', '1')),
	t.suffix = replace(PINEXTN, 'M0', '1')
where state = 'current' and rputype = 'mach' and PINEXTN like 'M%';




insert into etracs254_tubod.realproperty(
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
	t.TAXDECNUM as objid,
	t.state as state,
	0 as autonumber,
	'new' as pintype,
	t.fullpin as pin,
	2014 as ry,
	null as claimno,
	substring(t.fullpin, 13, 3) as section,
	substring(t.fullpin, 17, 2) as parcel,
	t.LOTNO as  cadastrallotno,
	t.BLKNO as blockno,
	t.SURVEYNO as surveyno,
	null as street,
	null as purok,
	t.NORTH as north,
	t.SOUTH as south,
	t.EAST as east,
	t.WEST as west,
	t.barangayid,
	'MUNICIPALITY' as lgutype,
	null as previd,
	replace(substring(t.fullpin, 1, 6), '-','') as lguid
from lgsp_tubod.tdmainib t 
where t.rputype = 'land' 
and t.state = 'current' 
and t.fullpin is not null;







alter table lgsp_tubod.genclasslk
	add classid varchar(50),
	add exemptid varchar(50);


update lgsp_tubod.genclasslk g, etracs254_tubod. propertyclassification pc set 
	g.classid = pc.objid 
where g.GENCLASSDESCRIPTION like concat('%', pc.name, '%');


update lgsp_tubod.genclasslk g, etracs254_tubod. exemptiontype et set 
	g.exemptid = et.objid 
where g.GENCLASSDESCRIPTION like concat('%', et.name, '%');


update lgsp_tubod.genclasslk g set 
	g.classid = (select objid from etracs254_tubod.propertyclassification where name like 'TIMBERLAND')
where g.genclassid = 'LT';

insert into etracs254_tubod.rpumaster(
	objid,
	currentfaasid,
	currentrpuid
)
select t.TAXDECNUM, t.TAXDECNUM, t.TAXDECNUM
from lgsp_tubod.tdmainib t 
where t.state = 'current';





insert into etracs254_tubod.rpu(
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
	t.TAXDECNUM as objid,
	'INTERIM' as state,
	t.TAXDECNUM as realpropertyid,
	t.rputype,
	2014 as ry,
	t.fullpin,
	ifnull(t.suffix,0),
	null as subsuffix,
	gc.classid as classification_objid,
	null as exemptiontype_objid,
	1 as taxable,
	0 as totalareaha,
	0 as totalareasqm,
	0 as totalbmv,
	0 as totalmv,
	0 as totalav,
	0 as hasswornamount,
	0 as swornamount,
	0 as useswornamount,
	null as previd,
	t.TAXDECNUM as rpumasterid,
	0 as reclassed
from lgsp_tubod.tdmainib t
	inner join lgsp_tubod.genclasslk gc on t.GENCLASS = gc.genclassid 
where t.state = 'current' and rputype is not null;





update etracs254_tubod.rpu r set 
	r.realpropertyid = (select max(objid)  from etracs254_tubod.realproperty where r.fullpin like concat(pin, '%'))
where r.rputype <> 'land' ;





insert into etracs254_tubod.landrpu(
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
	t.TAXDECNUM as objid,
	0 as idleland,
	0 as totallandbmv,
	0 as totallandmv,
	0 as totallandav,
	0 as totalplanttreebmv,
	0 as totalplanttreemv,
	0 as totalplanttreeadjustment,
	0 as totalplanttreeav,
	0 as landvalueadjustment
from lgsp_tubod.tdmainib t
	inner join lgsp_tubod.genclasslk gc on t.GENCLASS = gc.genclassid 
where t.rputype = 'land'
and exists(select * from etracs254_tubod.rpu where objid = t.TAXDECNUM);





alter table lgsp_tubod.tdmainib add column pin varchar(50);

create index ix_tdmain_fullpin on lgsp_tubod.tdmainib(fullpin);
create index ix_tdmain_pin on lgsp_tubod.tdmainib(pin);

update lgsp_tubod.tdmainib  t set 
	pin = substring(fullpin, 1, 18)
where state = 'current';




insert into etracs254_tubod.bldgrpu(
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
	t.TAXDECNUM as objid,
	(select max(objid) from etracs254_tubod.rpu xr where t.pin = xr.fullpin and xr.rputype = 'land') AS landrpuid,
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
from lgsp_tubod.tdmainib t
	inner join lgsp_tubod.genclasslk gc on t.GENCLASS = gc.genclassid 
where t.rputype = 'bldg'
and exists(select * from etracs254_tubod.rpu where objid = t.TAXDECNUM);


insert into etracs254_tubod.machrpu(
	objid,
	landrpuid
)
select 
	t.TAXDECNUM as objid,
	(select max(objid) from etracs254_tubod.rpu xr where t.pin = xr.fullpin and xr.rputype = 'land') AS landrpuid
from lgsp_tubod.tdmainib t
	inner join lgsp_tubod.genclasslk gc on t.GENCLASS = gc.genclassid 
where t.rputype = 'mach'
and exists(select * from etracs254_tubod.rpu where objid = t.TAXDECNUM);




insert into etracs254_tubod.faas(
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
	t.TAXDECNUM as objid,
	'INTERIM' as state,
	t.TAXDECNUM as rpuid,
	1 as datacapture,
	0 as autonumber,
	t.TAXDECNUM as utdno,
	t.TAXDECNUM as tdno,
	'GR' as txntype_objid,
	year(t.EFFECTIVITYDATE) as effectivityyear,
	quarter(t.EFFECTIVITYDATE) as effectivityqtr,
	null as titletype,
	t.TCT as titleno,
	null as titledate,
	null as taxpayer_objid,
	null as taxpayer_name,
	null as taxpayer_address,
	t.OWNER  as owner_name,
	t.OWNERADDRESS as owner_address,
	null as administrator_objid,
	t.ADMINISTRATOR as administrator_name,
	t.ADMINADDRESS as administrator_address,
	null as beneficiary_objid,
	null as beneficiary_name,
	null as beneficiary_address,
	'-' as memoranda,
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
	'05722' as lguid,
	null as txntimestamp,
	null as cancelledtimestamp,
	substring(t.OWNER,50) as name,
	t.ASSESSMENTDATE as dtapproved,
	xr.realpropertyid as realpropertyid,
	'MUNICIPALITY' as lgutype,
	'[]' as signatories,
	null as ryordinanceno,
	null as ryordinancedate,
	0 as prevareaha,
	0 as prevareasqm,
	t.fullpin,
	0 as preveffectivity,
	year(t.ASSESSMENTDATE) as year,
	quarter(t.ASSESSMENTDATE) as  qtr,
	month(t.ASSESSMENTDATE) as  month,
	day(t.ASSESSMENTDATE) as day,
	null as cancelledyear,
	null as cancelledqtr,
	null as cancelledmonth,
	null as cancelledday,
	null as prevadministrator,
	'05722' as originlguid
from lgsp_tubod.tdmainib t
	inner join etracs254_tubod.rpu xr on t.TAXDECNUM = xr.objid 
where t.state = 'current' 
and exists(select * from etracs254_tubod.rpu where objid = t.TAXDECNUM);


update etracs254_tubod.faas f, lgsp_tubod.previousib p set 
	f.prevtdno = p.PREVIOUSTD,
	f.prevpin = null, 
	f.prevowner = p.PREVIOUSOWNER,
	f.prevav = ifnull(p.ASSESSEDVALUE,0), 
	f.prevmv = 0.0, 
	f.prevareaha = p.PREVLANDAREA, 
	f.prevareasqm = p.PREVLANDAREA,
	f.preveffectivity = null,
	f.prevadministrator = null 
where f.objid = p.TAXDECNUM;






/* update memoranda */

create index ix_tdmemo_taxdecnum on lgsp_tubod.tdmemoib(taxdecnum);

create table lgsp_tubod.xmemo(
	taxdecnum varchar(50),
	memo varchar(3000),
	primary key(taxdecnum)
);

SET SESSION group_concat_max_len = 1000000;

insert into lgsp_tubod.xmemo(
	taxdecnum, memo 
)
select taxdecnum, memo from (
	select m.taxdecnum, group_concat(TAXDECMEMO) as memo, count(*) 
	from  lgsp_tubod.tdmainib tm
		inner join lgsp_tubod.tdmemoib m on tm.taxdecnum = m.taxdecnum 
	where tm.state = 'current' 
	group by TAXDECNUM 
) x;


update etracs254_tubod.faas f, lgsp_tubod.xmemo m set 
	f.memoranda = m.memo 
where f.objid = m.taxdecnum;



alter table lgsp_tubod.land1assib add classid varchar(50);
alter table lgsp_tubod.land1assib add subclassid varchar(50);

alter table lgsp_tubod.land2assib add classid varchar(50);
alter table lgsp_tubod.land2assib add subclassid varchar(50);



update lgsp_tubod.land1assib l  set classid = null;
update lgsp_tubod.land2assib l  set classid = null;



update lgsp_tubod.land1assib l set 
	l.classid = (select objid from etracs254_tubod.propertyclassification where name = l.classification)
where l.taxdecnum like 'h%'
and l.classid is null;


update lgsp_tubod.land1assib l set 
	l.classid = (select objid from etracs254_tubod.propertyclassification where name = l.kind)
where l.taxdecnum like 'h%'
and l.classid is null;


update lgsp_tubod.land2assib l set 
	l.classid = (select objid from etracs254_tubod.propertyclassification where name = l.classification)
where l.taxdecnum like 'h%'
and l.classid is null;


update lgsp_tubod.land2assib l set 
	l.classid = (select objid from etracs254_tubod.propertyclassification where name = l.kind)
where l.taxdecnum like 'h%'
and l.classid is null;



update lgsp_tubod.land1assib l set 
	l.subclassid = (
		select min(sub.objid)
		from etracs254_tubod.lcuvsubclass sub, etracs254_tubod.lcuvspecificclass spc  
		where sub.specificclass_objid = spc.objid 
		  and spc.classification_objid = l.classid 
	)
where l.taxdecnum like 'h%'
and l.subclassid is null;


update lgsp_tubod.land2assib l set 
	l.subclassid = (
		select min(sub.objid)
		from etracs254_tubod.lcuvsubclass sub, etracs254_tubod.lcuvspecificclass spc  
		where sub.specificclass_objid = spc.objid 
		  and spc.classification_objid = l.classid 
	)
where l.taxdecnum like 'h%'
and l.subclassid is null;




alter table lgsp_tubod.taxvalueib add actualuseid varchar(50);
alter table lgsp_tubod.taxvalueib add classid varchar(50);


update lgsp_tubod.taxvalueib v set 
	v.classid = (select objid from etracs254_tubod.propertyclassification where code = v.actualuse);




update lgsp_tubod.taxvalueib v set 
	v.actualuseid = (select objid from etracs254_tubod.landassesslevel 
									  where classification_objid = v.classid 
                      and concat('A',v.actualuse) = code)
where v.kindproperty = 'L';




update lgsp_tubod.taxvalueib v set 
	v.actualuseid = (select objid from etracs254_tubod.bldgassesslevel 
									  where classification_objid = v.classid 
                      and concat('A',v.actualuse) = code)
where v.kindproperty = 'B';


update lgsp_tubod.taxvalueib v set 
	v.actualuseid = (select objid from etracs254_tubod.machassesslevel 
									  where classification_objid = v.classid 
                      and concat('A',v.actualuse) = code)
where v.kindproperty = 'M';


ALTER TABLE lgsp_tubod.land2assib
ADD COLUMN oid  int NOT NULL AUTO_INCREMENT AFTER subclassid,
ADD PRIMARY KEY (oid);

ALTER TABLE lgsp_tubod.land1assib
ADD COLUMN oid  int NOT NULL AUTO_INCREMENT AFTER subclassid,
ADD PRIMARY KEY (oid);



ALTER TABLE lgsp_tubod.land1assib
ADD COLUMN oid  int NULL AUTO_INCREMENT AFTER subclassid;

alter table etracs254_tubod.landdetail modify column actualuse_objid varchar(50) null;


insert into etracs254_tubod.landdetail(
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
	concat('LD', l.oid) as objid,
	l.taxdecnum as landrpuid,
	l.subclassid as subclass_objid,
	sub.specificclass_objid,
	null as actualuse_objid,
	null as stripping_objid,
	0 as striprate,
	case when l.classification = 'AGRICULTURAL' then 'HA' else 'SQM' end as  areatype,
	null as addlinfo,
	l.area,
	l.area as areasqm,
	l.area / 10000 as areaha,
	l.unitvalue as basevalue,
	l.unitvalue as unitvalue,
	1 as taxable,
	l.marketvalue as basemarketvalue,
	ifnull(l.adjustment,0) as adjustment,
	0 as landvalueadjustment,
	0 as actualuseadjustment,
	l.marketvalue,
	0 as assesslevel,
	0 as assessedvalue 
from lgsp_tubod.land2assib l, 
	etracs254_tubod.lcuvsubclass sub
where l.taxdecnum like 'h%'
 and l.subclassid = sub.objid
 and exists(select * from etracs254_tubod.rpu where objid = l.taxdecnum);






insert into etracs254_tubod.landdetail(
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
	concat('LDA', l.oid) as objid,
	l.taxdecnum as landrpuid,
	l.subclassid as subclass_objid,
	sub.specificclass_objid,
	null as actualuse_objid,
	null as stripping_objid,
	0 as striprate,
	case when l.classification = 'AGRICULTURAL' then 'HA' else 'SQM' end as  areatype,
	null as addlinfo,
	l.area,
	l.area * 10000 as areasqm,
	l.area as areaha,
	l.unitvalue as basevalue,
	l.unitvalue as unitvalue,
	1 as taxable,
	l.marketvalue as basemarketvalue,
	0 as adjustment,
	0 as landvalueadjustment,
	0 as actualuseadjustment,
	l.marketvalue,
	0 as assesslevel,
	0 as assessedvalue 
from lgsp_tubod.land1assib l, 
	etracs254_tubod.lcuvsubclass sub
where l.taxdecnum like 'h%'
 and l.subclassid = sub.objid
 and exists(select * from etracs254_tubod.rpu where objid = l.taxdecnum);





update etracs254_tubod.landdetail ld, 
	lgsp_tubod.taxvalueib v 
set
	ld.actualuse_objid = v.actualuseid
where ld.landrpuid = v.taxdecnum;



update etracs254_tubod.rpu set ry = 2013;
update etracs254_tubod.realproperty set ry = 2013;





insert into etracs254_tubod.faas_signatory
	(objid)
select objid from faas;


update etracs254_tubod.faas_signatory set 
	taxmapper_name = 'FRANCISCO M. DIACOR' ,
	taxmapper_title = 'ASSESSMENT CLERK III' ,
	taxmapper_dtsigned = '2013-09-04',
	appraiser_name = 'JOHARI B. PITI-ILAN',
	appraiser_title = 'ASSESSMENT CLERK II',
	appraiser_dtsigned = '2013-09-04',
	recommender_name = 'MARGARITO D. BETE, JR.',
	recommender_title = 'MUNICIPAL ASSESSOR',
	recommender_dtsigned = '2014-02-03',
	approver_name = 'ROGELIO T. AGUAVIVA',
	approver_title = 'PROVINCIAL ASSESSOR',
	approver_dtsigned = '2014-02-10';


