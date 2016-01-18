/*============================================
*
* SUBLEDGER SUPPORT 
*
*=============================================*/

create table rptledger_subledger
(
	objid varchar(50) not null primary key,
	parent_objid varchar(50) not null, 
	subacctno varchar(10) not null,
	constraint ux_parentid_subacctno unique (parent_objid, subacctno)
) engine=innodb;


alter table rptledger_subledger 
add constraint FK_rptledger_subledger_rptldger foreign key(parent_objid)
references rptledger(objid);


/*============================================
*
* BLDG_LAND (bldg rpu located in multiple lands)
*
*=============================================*/
CREATE TABLE `bldgrpu_land` (
  `objid` varchar(50) NOT NULL,
  `bldgrpuid` varchar(50) NOT NULL,
  `landfaas_objid` varchar(50) NOT NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `ux_bldgrpu_land_bldgrpu_landfaas` USING BTREE (`bldgrpuid`,`landfaas_objid`),
  KEY `ix_bldgrpu_land_bldgrpuid` (`bldgrpuid`),
  KEY `ix_bldgrpu_land_landfaasid` (`landfaas_objid`)
) ENGINE=InnoDB;

ALTER TABLE `bldgrpu_land` 
	ADD CONSTRAINT `FK_bldgrpu_land_bldgrpu` 
	FOREIGN KEY (`bldgrpuid`) 
	REFERENCES `bldgrpu` (`objid`);

ALTER TABLE `bldgrpu_land` 
	ADD CONSTRAINT `FK_bldgrpu_land_landfaas` 
	FOREIGN KEY (`landfaas_objid`) 
	REFERENCES `faas` (`objid`);

	