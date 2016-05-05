/* v2.5.04.032-03007 */
alter table subdivisionaffectedrpu modify column prevfaasid varchar(50) null;
alter table subdivisionaffectedrpu modify column prevtdno varchar(50) null;
alter table subdivisionaffectedrpu modify column prevpin varchar(50) null;


/*===========================================*/
/* SUBDIVISION - CANCEL IMPROVEMENTS SUPPORT */
/*===========================================*/
create table subdivision_cancelledimprovement(
	objid varchar(50) not null,
	parentid varchar(50) not null,
	faasid varchar(50),
	remarks varchar(1000),
	primary key (objid)
) engine=innodb default charset=utf8;

alter table subdivision_cancelledimprovement 
	add constraint FK_subdivision_cancelledimprovement_subdivision
	foreign key (parentid) references subdivision(objid);

alter table subdivision_cancelledimprovement 
	add constraint FK_subdivision_cancelledimprovement_faas
	foreign key (faasid) references faas(objid);


/*===========================================*/
/* MACHRPU - ADD BLDG REFERENCE 
/*===========================================*/
alter table machrpu add bldgmaster_objid varchar(50);


/*===========================================*/
/* STRUCTURE UPDATE
/*===========================================*/
alter table structure add showinfaas int not null;
update structure set showinfaas = 1 where showinfaas is null;