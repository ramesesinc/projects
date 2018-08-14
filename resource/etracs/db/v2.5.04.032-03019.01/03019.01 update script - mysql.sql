/* 254032-03019.01 */

/*==================================================
*
*  BATCH GR UPDATES
*
=====================================================*/
drop table batchgr_error;
drop table batchgr_items_forrevision;
drop table batchgr_log;
drop view vw_batchgr_error;

CREATE TABLE `batchgr` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) NOT NULL,
  `ry` int(255) NOT NULL,
  `lgu_objid` varchar(50) NOT NULL,
  `barangay_objid` varchar(50) NOT NULL,
  `rputype` varchar(15) DEFAULT NULL,
  `classification_objid` varchar(50) DEFAULT NULL,
  `section` varchar(10) DEFAULT NULL,
  `count` int(255) NOT NULL,
  `completed` int(255) NOT NULL,
  `error` int(255) NOT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create index `ix_barangay_objid` on batchgr(`barangay_objid`);
create index `ix_state` on batchgr(`state`);
create index `fk_lgu_objid` on batchgr(`lgu_objid`);

alter table batchgr add constraint `fk_barangay_objid` 
  foreign key (`barangay_objid`) references `barangay` (`objid`);
  
alter table batchgr add constraint `fk_lgu_objid` 
  foreign key (`lgu_objid`) references `sys_org` (`objid`);



CREATE TABLE `batchgr_item` (
  `objid` varchar(50) NOT NULL,
  `parent_objid` varchar(50) NOT NULL,
  `state` varchar(50) NOT NULL,
  `rputype` varchar(15) NOT NULL,
  `tdno` varchar(50) NOT NULL,
  `fullpin` varchar(50) NOT NULL,
  `pin` varchar(50) NOT NULL,
  `suffix` int(255) NOT NULL,
  `newfaasid` varchar(50) DEFAULT NULL,
  `error` text,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create index key `fk_batchgr_item_batchgr` on batchgr_item (`parent_objid`);
create index key `fk_batchgr_item_newfaasid` on batchgr_item (`newfaasid`);
create index key `fk_batchgr_item_tdno` on batchgr_item (`tdno`);
create index key `fk_batchgr_item_pin` on batchgr_item (`pin`);


alter table batchgr_item add constraint `fk_batchgr_item_objid` 
	foreign key (`objid`) references `faas` (`objid`);

alter table batchgr_item add constraint `fk_batchgr_item_batchgr` 
	foreign key (`parent_objid`) references `batchgr` (`objid`);

alter table batchgr_item add constraint `fk_batchgr_item_newfaasid` 
	foreign key (`newfaasid`) references `faas` (`objid`);


CREATE TABLE `batchgr_forprocess` (
  `objid` varchar(50) NOT NULL,
  `parent_objid` varchar(50) NOT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create index key `fk_batchgr_forprocess_parentid` on batchgr_forprocess(`parent_objid`);

alter table batchgr_forprocess add constraint `fk_batchgr_forprocess_parentid` 
	foreign key (`parent_objid`) references `batchgr` (`objid`);

alter table batchgr_forprocess add constraint `fk_batchgr_forprocess_objid` 
	foreign key (`objid`) references `batchgr_item` (`objid`);

	

