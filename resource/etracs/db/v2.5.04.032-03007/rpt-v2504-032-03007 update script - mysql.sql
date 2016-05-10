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
/* MACHRPU - ADD BLDG REFERENCE */
/*===========================================*/
alter table machrpu add bldgmaster_objid varchar(50);


/*===========================================*/
/* STRUCTURE UPDATE */
/*===========================================*/
alter table structure add showinfaas int not null;
update structure set showinfaas = 1 where showinfaas is null;



/*===========================================*/
/* STEWARDSHIP SUPPORT */
/*===========================================*/
alter table realproperty add stewardshipno varchar(3) ;
alter table faas add parentfaasid varchar(50) ;

INSERT INTO `faas_txntype` (`objid`, `name`, `newledger`, `newrpu`, `newrealproperty`, `displaycode`, `allowEditOwner`, `checkbalance`, `allowEditPin`, `allowEditPinInfo`, `allowEditAppraisal`, `opener`) 
VALUES ('ST', 'Stewardship', '1', '1', '1', 'DP', '1', '0', '0', '1', '1', '');

INSERT INTO `faas_txntype` (`objid`, `name`, `newledger`, `newrpu`, `newrealproperty`, `displaycode`, `allowEditOwner`, `checkbalance`, `allowEditPin`, `allowEditPinInfo`, `allowEditAppraisal`, `opener`) 
VALUES ('STP', 'Stewardship', '0', '1', '1', 'DP', '0', '0', '0', '0', '1', '');


create table faas_stewardship
(
	objid varchar(50),
	rpumasterid varchar(50) not null, 
	stewardrpumasterid varchar(50) not null,
	ry int not null, 
	stewardshipno int not null,
	primary key(objid)
) engine=INNODB charset=utf8;

create unique index ux_faas_stewardship on faas_stewardship(rpumasterid, stewardrpumasterid, ry, stewardshipno);
create index ix_faas_stewardship_rpumasterid on faas_stewardship(rpumasterid);
create index ix_faas_stewardship_stewardrpumasterid on faas_stewardship(stewardrpumasterid);


	