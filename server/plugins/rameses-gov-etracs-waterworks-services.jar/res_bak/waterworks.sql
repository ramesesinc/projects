
create database cwd_waterworks; 
use cwd_waterworks; 

set foreign_key_checks=0;

CREATE TABLE `waterworks_account` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `dtstarted` date DEFAULT NULL,
  `acctno` varchar(50) DEFAULT NULL,
  `applicationid` varchar(50) DEFAULT NULL,
  `acctname` varchar(255) DEFAULT NULL,
  `owner_objid` varchar(50) DEFAULT NULL,
  `owner_name` varchar(255) DEFAULT NULL,
  `address_type` varchar(50) DEFAULT NULL,
  `address_barangay_objid` varchar(50) DEFAULT NULL,
  `address_barangay_name` varchar(50) DEFAULT NULL,
  `address_text` varchar(255) DEFAULT NULL,
  `address_city` varchar(50) DEFAULT NULL,
  `address_province` varchar(50) DEFAULT NULL,
  `address_municipality` varchar(50) DEFAULT NULL,
  `address_unitno` varchar(50) DEFAULT NULL,
  `address_street` varchar(50) DEFAULT NULL,
  `address_subdivision` varchar(50) DEFAULT NULL,
  `address_bldgno` varchar(50) DEFAULT NULL,
  `address_bldgname` varchar(50) DEFAULT NULL,
  `address_pin` varchar(50) DEFAULT NULL,
  `mobileno` varchar(50) DEFAULT NULL,
  `phoneno` varchar(50) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `meterid` varchar(50) DEFAULT NULL,
  `classificationid` varchar(50) DEFAULT NULL,
  `balance` decimal(10,2) DEFAULT NULL,
  `lastreadingdate` datetime DEFAULT NULL,
  `lastreadingmonth` int(11) DEFAULT NULL,
  `lastreadingyear` int(11) DEFAULT NULL,
  `lasttxndate` datetime DEFAULT NULL,
  `lastreading` int(11) DEFAULT NULL,
  `prevreading` int(11) DEFAULT NULL,
  `disconnectiondate` date DEFAULT NULL,
  `paymentvaliditydate` date DEFAULT NULL,
  `stuboutid` varchar(50) DEFAULT NULL,
  `stuboutindex` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `fk_waterworks_account_meter` (`meterid`),
  KEY `fk_waterworks_account_classification` (`classificationid`),
  KEY `ix_stuboutid` (`stuboutid`),
  CONSTRAINT `fk_waterworks_account_classification` FOREIGN KEY (`classificationid`) REFERENCES `waterworks_classification` (`objid`),
  CONSTRAINT `fk_waterworks_account_meter` FOREIGN KEY (`meterid`) REFERENCES `waterworks_meter` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `waterworks_account_consumption` (
  `objid` varchar(50) NOT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  `dtreading` datetime DEFAULT NULL,
  `year` int(4) DEFAULT NULL,
  `month` int(2) DEFAULT NULL,
  `prevreading` int(11) DEFAULT NULL,
  `reading` int(10) DEFAULT NULL,
  `readingmethod` varchar(50) DEFAULT NULL,
  `reader_objid` varchar(50) DEFAULT NULL,
  `reader_name` varchar(255) DEFAULT NULL,
  `volume` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_month_year` (`year`,`month`,`acctid`),
  KEY `fks_waterworks_account_reading` (`acctid`),
  CONSTRAINT `fks_waterworks_account_reading` FOREIGN KEY (`acctid`) REFERENCES `waterworks_account` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `waterworks_account_ledger` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `txntype` varchar(10) DEFAULT NULL,
  `duedate` date DEFAULT NULL,
  `item_objid` varchar(50) DEFAULT NULL,
  `item_code` varchar(50) DEFAULT NULL,
  `item_title` varchar(255) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `amount` decimal(16,2) DEFAULT NULL,
  `amtpaid` decimal(16,2) DEFAULT NULL,
  `installmentid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_parentid` (`parentid`),
  KEY `ix_duedate` (`duedate`),
  KEY `ix_item_objid` (`item_objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `waterworks_application` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `dtfiled` date DEFAULT NULL,
  `appno` varchar(50) DEFAULT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `waterworks_application_task` (
  `taskid` varchar(50) NOT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `parentprocessid` varchar(50) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `startdate` datetime DEFAULT NULL,
  `enddate` datetime DEFAULT NULL,
  `assignee_objid` varchar(50) DEFAULT NULL,
  `assignee_name` varchar(100) DEFAULT NULL,
  `actor_objid` varchar(50) DEFAULT NULL,
  `actor_name` varchar(100) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `prevtaskid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`taskid`),
  KEY `FK_business_application_task_business_application` (`refid`),
  CONSTRAINT `fx_waterworks_application_task` FOREIGN KEY (`refid`) REFERENCES `waterworks_application` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `waterworks_area` (
  `objid` varchar(50) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `assignee_objid` varchar(50) DEFAULT NULL,
  `assignee_name` varchar(255) DEFAULT NULL,
  `sectorid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_sectorid` (`sectorid`),
  KEY `ix_assignee_objid` (`assignee_objid`),
  CONSTRAINT `fk_waterworks_area_sectorid` FOREIGN KEY (`sectorid`) REFERENCES `waterworks_sector` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `waterworks_area_zone` (
  `objid` varchar(50) NOT NULL,
  `areaid` varchar(50) DEFAULT NULL,
  `zoneid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_area_zone` (`areaid`,`zoneid`),
  KEY `ix_areaid` (`areaid`),
  KEY `ix_zoned` (`zoneid`),
  CONSTRAINT `fk_waterworks_area_zone_areaid` FOREIGN KEY (`areaid`) REFERENCES `waterworks_area` (`objid`),
  CONSTRAINT `fk_waterworks_area_zone_zoneid` FOREIGN KEY (`zoneid`) REFERENCES `waterworks_sector_zone` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `waterworks_classification` (
  `objid` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `waterworks_formula` (
  `classificationid` varchar(100) NOT NULL,
  `varname` varchar(50) DEFAULT NULL,
  `expr` mediumtext,
  PRIMARY KEY (`classificationid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `waterworks_installment` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(10) DEFAULT NULL,
  `controlno` varchar(20) DEFAULT NULL,
  `doctype` varchar(10) DEFAULT NULL,
  `dtfiled` datetime DEFAULT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  `particulars` varchar(255) DEFAULT NULL,
  `startdate` date DEFAULT NULL,
  `enddate` date DEFAULT NULL,
  `term` int(11) DEFAULT NULL,
  `amount` decimal(16,2) DEFAULT NULL,
  `amtpaid` decimal(16,2) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `waterworks_meter` (
  `objid` varchar(50) NOT NULL,
  `serialno` varchar(50) DEFAULT NULL,
  `brand` varchar(50) DEFAULT NULL,
  `sizeid` varchar(50) DEFAULT NULL,
  `capacity` varchar(50) DEFAULT NULL,
  `currentacctid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_serialno` (`serialno`),
  KEY `ix_brand` (`brand`),
  KEY `fk_sizeid` (`sizeid`),
  CONSTRAINT `fk_sizeid` FOREIGN KEY (`sizeid`) REFERENCES `waterworks_metersize` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `waterworks_metersize` (
  `objid` varchar(50) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `waterworks_mobile_info` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `acctno` varchar(50) DEFAULT NULL,
  `acctname` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `mobileno` varchar(50) DEFAULT NULL,
  `serialno` varchar(50) DEFAULT NULL,
  `areaid` varchar(50) DEFAULT NULL,
  `lastreading` int(11) DEFAULT NULL,
  `lasttxndate` date DEFAULT NULL,
  `areaname` varchar(50) DEFAULT NULL,
  `classificationid` varchar(50) DEFAULT NULL,
  `lastreadingyear` int(11) DEFAULT NULL,
  `lastreadingmonth` int(11) DEFAULT NULL,
  `lastreadingdate` datetime DEFAULT NULL,
  `barcode` varchar(50) DEFAULT NULL,
  `batchid` varchar(50) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `period` varchar(50) DEFAULT NULL,
  `duedate` date DEFAULT NULL,
  `discodate` date DEFAULT NULL,
  `rundate` datetime DEFAULT NULL,
  `items` text,
  `info` text,
  `itemaccountid` varchar(50) DEFAULT NULL,
  `stuboutid` varchar(50) DEFAULT NULL,
  `sortorder` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_itemaccountid` (`itemaccountid`),
  KEY `ix_batchid` (`batchid`),
  KEY `ix_classificationid` (`classificationid`),
  KEY `ix_stuboutid` (`stuboutid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `waterworks_payment` (
  `objid` varchar(50) NOT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  `refno` varchar(50) DEFAULT NULL,
  `reftype` varchar(50) DEFAULT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `refdate` date DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `voided` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_waterworks_payment_account` (`acctid`),
  CONSTRAINT `fk_waterworks_payment_account` FOREIGN KEY (`acctid`) REFERENCES `waterworks_account` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `waterworks_payment_item` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `txnrefid` varchar(50) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  `surcharge` decimal(10,2) DEFAULT NULL,
  `interest` decimal(10,2) DEFAULT NULL,
  `remarks` varchar(50) DEFAULT NULL,
  `item_objid` varchar(50) DEFAULT NULL,
  `item_title` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_waterworks_payment_parent` (`parentid`),
  KEY `fk_waterworks_payment_item_txnref` (`txnrefid`),
  CONSTRAINT `fk_waterworks_payment_item_txnref` FOREIGN KEY (`txnrefid`) REFERENCES `waterworks_account_ledger` (`objid`),
  CONSTRAINT `fk_waterworks_payment_parent` FOREIGN KEY (`parentid`) REFERENCES `waterworks_payment` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `waterworks_sector` (
  `objid` varchar(50) NOT NULL,
  `code` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `waterworks_sector_zone` (
  `objid` varchar(50) NOT NULL,
  `sectorid` varchar(50) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_waterworks_zone_sector` (`sectorid`,`code`),
  CONSTRAINT `fk_waterworks_zone_sector` FOREIGN KEY (`sectorid`) REFERENCES `waterworks_sector` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `waterworks_stubout` (
  `objid` varchar(50) NOT NULL,
  `code` varchar(50) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `zoneid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_zoneid` (`zoneid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE VIEW `vw_waterworks_account_notin_stubout` 
AS 
select `wa`.`objid` AS `objid`,`wa`.`acctno` AS `acctno`,`wa`.`acctname` AS `acctname`,`wa`.`owner_name` AS `owner_name`,`wa`.`address_text` AS `address_text`,`wm`.`serialno` AS `meter_serialno` from (`waterworks_account` `wa` join `waterworks_meter` `wm` on((`wa`.`meterid` = `wm`.`objid`))) where (not(`wa`.`stuboutid` in (select `waterworks_stubout`.`objid` from `waterworks_stubout` where (`waterworks_stubout`.`objid` = `wa`.`stuboutid`)))) ;

CREATE VIEW `vw_waterworks_meter` 
AS 
select `wm`.`objid` AS `objid`,`wm`.`serialno` AS `serialno`,`wm`.`brand` AS `brand`,`wm`.`capacity` AS `capacity`,`wms`.`title` AS `size_title`,`wa`.`objid` AS `account_objid`,`wa`.`acctname` AS `account_acctname`,`wa`.`address_text` AS `account_address_text` from ((`waterworks_meter` `wm` join `waterworks_metersize` `wms` on((`wms`.`objid` = `wm`.`sizeid`))) left join `waterworks_account` `wa` on((`wa`.`meterid` = `wm`.`objid`))) order by `wm`.`serialno` ;

CREATE VIEW `vw_waterworks_sector_zone` 
AS 
select `wsz`.`objid` AS `objid`,`wsz`.`code` AS `code`,`wsz`.`description` AS `description`,`ws`.`objid` AS `sector_objid`,`ws`.`code` AS `sector_code` from (`waterworks_sector_zone` `wsz` join `waterworks_sector` `ws` on((`ws`.`objid` = `wsz`.`sectorid`))) order by `ws`.`code`,`wsz`.`code` ;

CREATE VIEW `vw_waterworks_stubout` 
AS 
select `wst`.`objid` AS `objid`,`wst`.`code` AS `code`,`wst`.`description` AS `description`,`wsz`.`objid` AS `zone_objid`,`wsz`.`code` AS `zone_code`,`wsz`.`description` AS `zone_description`,`ws`.`objid` AS `sector_objid`,`ws`.`code` AS `sector_code`,`wa`.`objid` AS `area_objid`,`wa`.`title` AS `area_title`,`wa`.`assignee_objid` AS `area_assignee_objid`,`wa`.`assignee_name` AS `area_assignee_name` from ((((`waterworks_area_zone` `waz` join `waterworks_area` `wa` on((`wa`.`objid` = `waz`.`areaid`))) join `waterworks_sector_zone` `wsz` on(((`wsz`.`objid` = `waz`.`zoneid`) and (`wsz`.`sectorid` = `wa`.`sectorid`)))) join `waterworks_sector` `ws` on((`ws`.`objid` = `wsz`.`sectorid`))) join `waterworks_stubout` `wst` on((`wst`.`zoneid` = `wsz`.`objid`))) ;

CREATE VIEW `vw_waterworks_stubout_account` 
AS 
select `wa`.`objid` AS `objid`,`wa`.`stuboutid` AS `stuboutid`,`wa`.`stuboutindex` AS `stuboutindex`,`wa`.`acctno` AS `account_acctno`,`wa`.`acctname` AS `account_acctname`,`wa`.`address_text` AS `account_address_text`,`wm`.`serialno` AS `account_meter_serialno` from ((`waterworks_stubout` `ws` join `waterworks_account` `wa` on((`wa`.`stuboutid` = `ws`.`objid`))) join `waterworks_meter` `wm` on((`wm`.`objid` = `wa`.`meterid`))) ;
