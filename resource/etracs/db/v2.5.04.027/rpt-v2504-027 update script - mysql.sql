/*==================================
** V2.5.04.027
==================================*/
update planttreeassesslevel set code = 'AA' where code = 'AA (PLANTS)';


update rptledger rl, faas f, rpu r  set 
	rl.txntype_objid = f.txntype_objid,
	rl.classification_objid = r.classification_objid
where rl.faasid = f.objid 
and f.rpuid = r.objid 
and (rl.txntype_objid is null or rl.classification_objid is null);


create table faas_txntype_attribute_type(
	attribute varchar(50) not null,
	primary key(attribute)
);


create table faas_txntype_attribute(
	txntype_objid varchar(50) not null, 
	attribute varchar(50) not null,
	idx int not null, 
	primary key (txntype_objid, attribute)
);

alter table faas_txntype_attribute 
add constraint FK_faas_txntype_attribute foreign key (txntype_objid)
references faas_txntype (objid);


alter table faas_txntype_attribute 
add constraint FK_faas_txntype_attribute_type foreign key (attribute)
references faas_txntype_attribute_type (attribute);



create table rpt_redflag
(
	objid varchar(50) not null,
	refid varchar(50) not null,
	caseno varchar(25) not null,
	message text not null,
	dtfiled datetime not null,
	filedby_objid varchar(50) not null,
	filedby_name varchar(150) not null,
	resolved int not null,
	action varchar(50) not null,
	opener varchar(50) not null,
	resolvedby_objid varchar(50) not null,
	resolvedby_name varchar(150) not null,
	dtresolved datetime,
	primary key (objid)
);

alter table rpt_redflag add constraint ux_rptredflag_caseno unique(caseno);

create index ix_rptredflag_refid on rpt_redflag(refid);

	


alter table faas add originlguid varchar(50);

update faas set originlguid = lguid;	

	

alter table subdivision add originlguid varchar(50);

update subdivision set originlguid = lguid;	


	

alter table consolidation add originlguid varchar(50);

update consolidation set originlguid = lguid;		