/* 254032-03018 */

alter table faasbacktax modify column tdno varchar(25) null;


alter table landdetail modify column subclass_objid varchar(50) null;
alter table landdetail modify column specificclass_objid varchar(50) null;
alter table landdetail modify column actualuse_objid varchar(50) null;
alter table landdetail modify column landspecificclass_objid varchar(50) null;



/* RYSETTING ORDINANCE INFO */
alter table landrysetting 
	add ordinanceno varchar(25),
	add ordinancedate date;


alter table bldgrysetting 
	add ordinanceno varchar(25),
	add ordinancedate date;


alter table machrysetting 
	add ordinanceno varchar(25),
	add ordinancedate date;


alter table miscrysetting 
	add ordinanceno varchar(25),
	add ordinancedate date;


alter table planttreerysetting 
	add ordinanceno varchar(25),
	add ordinancedate date;


delete from sys_var where name in ('gr_ordinance_date','gr_ordinance_no');

	



drop TABLE if exists `bldgrpu_land`;

CREATE TABLE `bldgrpu_land` (
  `objid` varchar(50) NOT NULL DEFAULT '',
  `rpu_objid` varchar(50) NOT NULL DEFAULT '',
  `landfaas_objid` varchar(50) DEFAULT NULL,
  `landrpumaster_objid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_bldgrpu_land_bldgrpuid` (`rpu_objid`),
  KEY `ix_bldgrpu_land_landfaasid` (`landfaas_objid`),
	KEY `ix_bldgrpu_land_landrpumasterid` (`landrpumaster_objid`),
  CONSTRAINT `FK_bldgrpu_land_bldgrpu` FOREIGN KEY (`rpu_objid`) REFERENCES `bldgrpu` (`objid`),
  CONSTRAINT `FK_bldgrpu_land_rpumaster` FOREIGN KEY (`landrpumaster_objid`) REFERENCES `rpumaster` (`objid`),
  CONSTRAINT `FK_bldgrpu_land_landfaas` FOREIGN KEY (`landfaas_objid`) REFERENCES `faas` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

	

create table batchgr_log (
	objid varchar(50) not null,
	primary key (objid)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


alter table bldgrpu_structuraltype modify column bldgkindbucc_objid nvarchar(50) null;



alter table bldgadditionalitem add idx int;
update bldgadditionalitem set idx = 0 where idx is null;

	



/*=================================================	
*
*  PROVINCE-MUNI LEDGER SYNCHRONIZATION SUPPORT 
*
====================================================*/
CREATE TABLE `rptledger_remote` (
  `objid` varchar(50) NOT NULL,
  `remote_objid` varchar(50) NOT NULL,
  `createdby_name` varchar(255) NOT NULL,
  `createdby_title` varchar(100) DEFAULT NULL,
  `dtcreated` datetime NOT NULL,
  PRIMARY KEY (`objid`),
  CONSTRAINT `FK_rptledgerremote_rptledger` FOREIGN KEY (`objid`) REFERENCES `rptledger` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/*======================================
* AUTOMATIC MACH AV RECALC SUPPORT
=======================================*/
INSERT INTO `rptparameter` (`objid`, `state`, `name`, `caption`, `description`, `paramtype`, `minvalue`, `maxvalue`) 
VALUES ('TOTAL_VALUE', 'APPROVED', 'TOTAL_VALUE', 'TOTAL VALUE', '', 'decimal', '0', '0');




/* BATCH GR ADDITIONAL SUPPORT */
alter table batchgr_items_forrevision add section varchar(3);
alter table batchgr_items_forrevision add classification_objid varchar(50);