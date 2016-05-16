/* v2.5.04.032-03004 */
create table faas_list(
	objid varchar(50) primary key, 
	state varchar(30) not null, 
	rpuid varchar(50) not null, 
	realpropertyid varchar(50) not null, 
	datacapture int not null,
	ry int not null, 
	txntype_objid varchar(50) not null, 
	tdno varchar(25), 
	utdno varchar(25) not null, 
	prevtdno text, 
	displaypin varchar(35) not null, 
	pin varchar(35) not null, 
	taxpayer_objid varchar(50) , 
	owner_name text , 
	owner_address varchar(150) , 
	administrator_name varchar(150), 
	administrator_address varchar(150), 
	rputype varchar(10) not null, 
	barangayid varchar(50) not null, 
	barangay varchar(75) not null, 
	classification_objid varchar(50) , 
	classcode varchar(20), 
	cadastrallotno text, 
	blockno varchar(100), 
	surveyno varchar(255), 
	titleno varchar(50), 
	totalareaha decimal(16,6) not null , 
	totalareasqm decimal(16,6) not null, 
	totalmv decimal(16,2) not null, 
	totalav decimal(16,2) not null, 
	effectivityyear int not null, 
	effectivityqtr int not null, 
	cancelreason varchar(15), 
	cancelledbytdnos text, 
	lguid varchar(50) not null, 
	originlguid varchar(50) not null, 
	yearissued int,
	taskid varchar(50),
	taskstate varchar(50),
	assignee_objid varchar(50),
	trackingno varchar(20)
);


create index ix_faaslist_state on faas_list(state);
create index ix_faaslist_rpuid on faas_list(rpuid);
create index ix_faaslist_realpropertyid on faas_list(realpropertyid);
create index ix_faaslist_ry on faas_list(ry);
create index ix_faaslist_tdno on faas_list(tdno);
create index ix_faaslist_utdno on faas_list(utdno);
create index ix_faaslist_prevtdno on faas_list(prevtdno(500));
create index ix_faaslist_pin on faas_list(pin);
create index ix_faaslist_taxpayer_objid on faas_list(taxpayer_objid);
create index ix_faaslist_owner_name on faas_list(owner_name(100));
create index ix_faaslist_administrator_name on faas_list(administrator_name(100));
create index ix_faaslist_rputype on faas_list(rputype);
create index ix_faaslist_barangayid on faas_list(barangayid);
create index ix_faaslist_barangay on faas_list(barangay);
create index ix_faaslist_classification_objid on faas_list(classification_objid);
create index ix_faaslist_classcode on faas_list(classcode);
create index ix_faaslist_cadastrallotno on faas_list(cadastrallotno(255));
create index ix_faaslist_blockno on faas_list(blockno);
create index ix_faaslist_surveyno on faas_list(surveyno);
create index ix_faaslist_titleno on faas_list(titleno);
create index ix_faaslist_lguid on faas_list(lguid);
create index ix_faaslist_originlguid on faas_list(originlguid);
create index ix_faaslist_taskstate on faas_list(taskstate);
create index ix_faaslist_trackingno on faas_list(trackingno);
create index ix_faaslist_assigneeid on faas_list(assignee_objid);



alter table realproperty 
	drop index ix_realproperty_brgysection,
	drop index ix_realproperty_brgystate;


insert into faas_list(
	objid,
	state,
	datacapture,
	rpuid,
	realpropertyid,
	ry,
	txntype_objid,
	tdno,
	utdno,
	prevtdno,
	displaypin,
	pin,
	taxpayer_objid,
	owner_name,
	owner_address,
	administrator_name,
	administrator_address,
	rputype,
	barangayid,
	barangay,
	classification_objid,
	classcode,
	cadastrallotno,
	blockno,
	surveyno,
	titleno,
	totalareaha,
	totalareasqm,
	totalmv,
	totalav,
	effectivityyear,
	effectivityqtr,
	cancelreason,
	cancelledbytdnos,
	lguid,
	originlguid,
	yearissued,
	taskid,
	taskstate,
	assignee_objid,
	trackingno
)
select 
	f.objid,
	f.state,
	f.datacapture, 
	f.rpuid,
	f.realpropertyid,
	r.ry,
	f.txntype_objid,
	f.tdno,
	f.utdno,
	f.prevtdno,
	f.fullpin as displaypin,
	case when r.rputype = 'land' then rp.pin else concat(rp.pin, '-', r.suffix) end as pin,
	f.taxpayer_objid,
	f.owner_name,
	f.owner_address,
	f.administrator_name,
	f.administrator_address,
	r.rputype,
	rp.barangayid,
	(select name from barangay where objid = rp.barangayid) as barangay,
	r.classification_objid,
	pc.code as classcode,
	rp.cadastrallotno,
	rp.blockno,
	rp.surveyno,
	f.titleno,
	r.totalareaha,
	r.totalareasqm,
	r.totalmv,
	r.totalav,
	f.effectivityyear,
	f.effectivityqtr,
	f.cancelreason,
	f.cancelledbytdnos,
	f.lguid,
	f.originlguid,
	f.year as yearissued,
	(select objid from faas_task where refid = f.objid and enddate is null limit 1) as taskid,
	(select state from faas_task where refid = f.objid and enddate is null limit 1) as taskstate,
	(select assignee_objid from faas_task where refid = f.objid and enddate is null limit 1) as assignee_objid,
	(select trackingno from rpttracking where objid = f.objid) as trackingno
from faas f 
	inner join rpu r on f.rpuid = r.objid 
	inner join realproperty rp on f.realpropertyid = rp.objid 
	inner join propertyclassification pc on r.classification_objid = pc.objid 
where not exists(select * from faas_list where objid = f.objid);



		