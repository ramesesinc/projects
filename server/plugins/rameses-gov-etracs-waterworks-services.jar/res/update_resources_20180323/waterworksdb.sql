/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.1.73-community : Database - icws_waterworks
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `waterworks_account` */

DROP TABLE IF EXISTS `waterworks_account`;

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
  `meterid` varchar(50) DEFAULT NULL,
  `classificationid` varchar(50) DEFAULT NULL,
  `lastdatepaid` datetime DEFAULT NULL,
  `billingcycleid` varchar(50) DEFAULT NULL,
  `stuboutnodeid` varchar(50) DEFAULT NULL,
  `lastbilldate` date DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `fk_waterworks_account_meter` (`meterid`),
  KEY `fk_waterworks_account_classification` (`classificationid`),
  KEY `ix_acctno` (`acctno`),
  KEY `ix_applicationid` (`applicationid`),
  KEY `ix_billingcycleid` (`billingcycleid`),
  KEY `ix_dtstarted` (`dtstarted`),
  KEY `ix_acctname` (`acctname`),
  KEY `ix_owner_objid` (`owner_objid`),
  KEY `ix_meterid` (`meterid`),
  KEY `ix_stuboutnodeid` (`stuboutnodeid`),
  KEY `ix_lastbilldate` (`lastbilldate`),
  CONSTRAINT `waterworks_account_ibfk_1` FOREIGN KEY (`applicationid`) REFERENCES `waterworks_application` (`objid`),
  CONSTRAINT `waterworks_account_ibfk_2` FOREIGN KEY (`billingcycleid`) REFERENCES `waterworks_billing_cycle` (`objid`),
  CONSTRAINT `waterworks_account_ibfk_3` FOREIGN KEY (`classificationid`) REFERENCES `waterworks_classification` (`objid`),
  CONSTRAINT `waterworks_account_ibfk_4` FOREIGN KEY (`meterid`) REFERENCES `waterworks_meter` (`objid`),
  CONSTRAINT `waterworks_account_ibfk_8` FOREIGN KEY (`stuboutnodeid`) REFERENCES `waterworks_stubout_node` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_account_attribute` */

DROP TABLE IF EXISTS `waterworks_account_attribute`;

CREATE TABLE `waterworks_account_attribute` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `attribute_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_attribute_parentid` (`parentid`,`attribute_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_application` */

DROP TABLE IF EXISTS `waterworks_application`;

CREATE TABLE `waterworks_application` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `dtfiled` date DEFAULT NULL,
  `appno` varchar(50) DEFAULT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  `classificationid` varchar(50) DEFAULT NULL,
  `acctname` varchar(50) DEFAULT NULL,
  `owner_objid` varchar(50) DEFAULT NULL,
  `owner_name` varchar(255) DEFAULT NULL,
  `address_type` varchar(50) DEFAULT NULL,
  `address_barangay_objid` varchar(50) DEFAULT NULL,
  `address_barangay_name` varchar(50) DEFAULT NULL,
  `address_text` varchar(50) DEFAULT NULL,
  `address_city` varchar(50) DEFAULT NULL,
  `address_province` varchar(50) DEFAULT NULL,
  `address_municipality` varchar(50) DEFAULT NULL,
  `address_unitno` varchar(50) DEFAULT NULL,
  `address_street` varchar(50) DEFAULT NULL,
  `address_subdivision` varchar(50) DEFAULT NULL,
  `address_bldgno` varchar(50) DEFAULT NULL,
  `address_bldgname` varchar(50) DEFAULT NULL,
  `address_pin` varchar(50) DEFAULT NULL,
  `stuboutid` varchar(50) DEFAULT NULL,
  `stuboutindex` int(11) DEFAULT NULL,
  `meterid` varchar(50) DEFAULT NULL,
  `initialreading` int(11) DEFAULT NULL,
  `installer_name` varchar(255) DEFAULT NULL,
  `installer_objid` varchar(50) DEFAULT NULL,
  `dtinstalled` date DEFAULT NULL,
  `signature` longtext,
  `meterprovider` varchar(255) DEFAULT NULL,
  `stuboutnodeid` varchar(50) DEFAULT NULL,
  `metersizeid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_classificationid` (`classificationid`),
  KEY `ix_acctname` (`acctname`),
  KEY `ix_owner_objid` (`owner_objid`),
  KEY `ix_address_barangay_objid` (`address_barangay_objid`),
  KEY `ix_stuboutid` (`stuboutid`),
  KEY `ix_meterid` (`meterid`),
  KEY `ix_installer_objid` (`installer_objid`),
  KEY `ix_dtinstalled` (`dtinstalled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_application_bom` */

DROP TABLE IF EXISTS `waterworks_application_bom`;

CREATE TABLE `waterworks_application_bom` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `state` varchar(10) DEFAULT NULL,
  `item_objid` varchar(50) DEFAULT NULL,
  `item_code` varchar(50) DEFAULT NULL,
  `item_title` varchar(255) DEFAULT NULL,
  `item_unit` varchar(10) DEFAULT NULL,
  `qty` int(11) DEFAULT NULL,
  `qtyissued` int(11) DEFAULT NULL,
  `item_unitprice` decimal(10,2) DEFAULT NULL,
  `cwdsupplied` int(11) DEFAULT NULL,
  `remarks` varchar(50) DEFAULT NULL,
  KEY `fk_application_bom` (`parentid`),
  CONSTRAINT `waterworks_application_bom_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `waterworks_application` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_application_fee` */

DROP TABLE IF EXISTS `waterworks_application_fee`;

CREATE TABLE `waterworks_application_fee` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `duedate` date DEFAULT NULL,
  `txntype` varchar(10) DEFAULT NULL,
  `item_objid` varchar(50) DEFAULT NULL,
  `item_code` varchar(50) DEFAULT NULL,
  `item_title` varchar(255) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `amount` decimal(16,2) DEFAULT NULL,
  `amtpaid` decimal(16,2) DEFAULT NULL,
  `installmentid` varchar(50) DEFAULT NULL,
  KEY `fk_application_fee_application` (`parentid`),
  CONSTRAINT `waterworks_application_fee_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `waterworks_application` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_application_requirement` */

DROP TABLE IF EXISTS `waterworks_application_requirement`;

CREATE TABLE `waterworks_application_requirement` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `refno` varchar(50) DEFAULT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `sortorder` int(11) DEFAULT NULL,
  `required` tinyint(4) DEFAULT NULL,
  `complied` tinyint(4) DEFAULT NULL,
  `dtcomplied` datetime DEFAULT NULL,
  `verifier_objid` varchar(50) DEFAULT NULL,
  `verifier_name` varchar(255) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `info` longtext,
  `dtissued` date DEFAULT NULL,
  `expirydate` date DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_application_task` */

DROP TABLE IF EXISTS `waterworks_application_task`;

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
  `dtcreated` datetime DEFAULT NULL,
  PRIMARY KEY (`taskid`),
  KEY `FK_business_application_task_business_application` (`refid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_attribute` */

DROP TABLE IF EXISTS `waterworks_attribute`;

CREATE TABLE `waterworks_attribute` (
  `name` varchar(50) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_batch_billing` */

DROP TABLE IF EXISTS `waterworks_batch_billing`;

CREATE TABLE `waterworks_batch_billing` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `createdby_objid` varchar(50) DEFAULT NULL,
  `createdby_name` varchar(255) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `zoneid` varchar(50) DEFAULT NULL,
  `dtposted` datetime DEFAULT NULL,
  `postedby_objid` varchar(50) DEFAULT NULL,
  `postedby_name` varchar(255) DEFAULT NULL,
  `fromperiod` date DEFAULT NULL,
  `toperiod` date DEFAULT NULL,
  `discdate` date DEFAULT NULL,
  `reader_objid` varchar(50) DEFAULT NULL,
  `reader_name` varchar(255) DEFAULT NULL,
  `readingdate` date DEFAULT NULL,
  `schedule_objid` varchar(50) DEFAULT NULL,
  `duedate` date DEFAULT NULL,
  `readingduedate` date DEFAULT NULL,
  `billingduedate` date DEFAULT NULL,
  `taskid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_taskid` (`taskid`),
  CONSTRAINT `fk_waterworks_batch_billing_task` FOREIGN KEY (`taskid`) REFERENCES `waterworks_batch_billing_task` (`taskid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_batch_billing_task` */

DROP TABLE IF EXISTS `waterworks_batch_billing_task`;

CREATE TABLE `waterworks_batch_billing_task` (
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
  `dtcreated` datetime DEFAULT NULL,
  `prevtaskid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`taskid`),
  KEY `fk_waterworks_batch_billing_ref` (`refid`),
  CONSTRAINT `fk_waterworks_batch_billing_ref` FOREIGN KEY (`refid`) REFERENCES `waterworks_batch_billing` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_batchreading` */

DROP TABLE IF EXISTS `waterworks_batchreading`;

CREATE TABLE `waterworks_batchreading` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `readingdate` date DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `sectorid` varchar(50) DEFAULT NULL,
  `zoneid` varchar(50) DEFAULT NULL,
  `reader_objid` varchar(50) DEFAULT NULL,
  `reader_name` varchar(255) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `createdby_objid` varchar(50) DEFAULT NULL,
  `createdby_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_zoneid` (`zoneid`),
  KEY `ix_year` (`year`),
  KEY `ix_month` (`month`),
  KEY `ix_sectorid` (`sectorid`),
  KEY `ix_reader_objid` (`reader_objid`),
  KEY `uix_batchreading_year_month` (`month`,`year`,`zoneid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_billing` */

DROP TABLE IF EXISTS `waterworks_billing`;

CREATE TABLE `waterworks_billing` (
  `objid` varchar(50) NOT NULL,
  `batchid` varchar(50) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  `prevreadingdate` datetime DEFAULT NULL,
  `prevreading` int(11) DEFAULT NULL,
  `readingdate` datetime DEFAULT NULL,
  `reading` int(11) DEFAULT NULL,
  `reader_objid` varchar(50) DEFAULT NULL,
  `reader_name` varchar(255) DEFAULT NULL,
  `discrate` decimal(16,2) DEFAULT NULL,
  `surcharge` decimal(16,2) DEFAULT NULL,
  `interest` decimal(16,2) DEFAULT NULL,
  `otherfees` decimal(16,2) DEFAULT NULL,
  `credits` decimal(16,2) DEFAULT NULL,
  `arrears` decimal(16,4) DEFAULT NULL,
  `unpaidmonths` int(11) DEFAULT NULL,
  `volume` int(11) DEFAULT NULL,
  `amount` decimal(16,2) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `readingmethod` varchar(255) DEFAULT NULL,
  `billed` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_billing_account_parent_acctid` (`batchid`,`acctid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_billing_cycle` */

DROP TABLE IF EXISTS `waterworks_billing_cycle`;

CREATE TABLE `waterworks_billing_cycle` (
  `objid` varchar(50) NOT NULL,
  `sectorid` varchar(50) NOT NULL,
  `month` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `fromperiod` date DEFAULT NULL,
  `toperiod` date DEFAULT NULL,
  `readingdate` date DEFAULT NULL,
  `duedate` date DEFAULT NULL,
  `disconnectiondate` date DEFAULT NULL,
  `billdate` date DEFAULT NULL,
  `oldid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_sectorid` (`sectorid`),
  KEY `ix_readingdate` (`readingdate`),
  CONSTRAINT `waterworks_billing_cycle_ibfk_1` FOREIGN KEY (`sectorid`) REFERENCES `waterworks_sector` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_block_schedule` */

DROP TABLE IF EXISTS `waterworks_block_schedule`;

CREATE TABLE `waterworks_block_schedule` (
  `objid` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_changelog` */

DROP TABLE IF EXISTS `waterworks_changelog`;

CREATE TABLE `waterworks_changelog` (
  `objid` varchar(50) NOT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `reftype` varchar(50) DEFAULT NULL,
  `action` varchar(50) DEFAULT NULL,
  `fieldnames` varchar(50) DEFAULT NULL,
  `oldvalue` text,
  `newvalue` text,
  `details` text,
  `remarks` text,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_classification` */

DROP TABLE IF EXISTS `waterworks_classification`;

CREATE TABLE `waterworks_classification` (
  `objid` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_consumption` */

DROP TABLE IF EXISTS `waterworks_consumption`;

CREATE TABLE `waterworks_consumption` (
  `objid` varchar(50) NOT NULL,
  `acctid` varchar(50) NOT NULL,
  `batchid` varchar(50) DEFAULT NULL,
  `meterid` varchar(50) DEFAULT NULL,
  `duedate` date DEFAULT NULL,
  `discdate` date DEFAULT NULL,
  `prevreading` int(11) DEFAULT NULL,
  `reading` int(11) DEFAULT NULL,
  `readingmethod` varchar(50) DEFAULT NULL,
  `reader_objid` varchar(50) DEFAULT NULL,
  `reader_name` varchar(255) DEFAULT NULL,
  `volume` int(11) DEFAULT NULL,
  `rate` decimal(16,2) DEFAULT NULL,
  `amount` decimal(16,2) DEFAULT NULL,
  `amtpaid` decimal(16,2) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `readingdate` date DEFAULT NULL,
  `state` varchar(32) NOT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_waterworks_consumption_year_month` (`acctid`,`month`,`year`),
  KEY `fks_waterworks_account_reading` (`acctid`),
  KEY `ix_year` (`year`),
  KEY `ix_month` (`month`),
  CONSTRAINT `waterworks_consumption_ibfk_1` FOREIGN KEY (`acctid`) REFERENCES `waterworks_account` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_credit` */

DROP TABLE IF EXISTS `waterworks_credit`;

CREATE TABLE `waterworks_credit` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `rootid` varchar(50) DEFAULT NULL,
  `refno` varchar(50) DEFAULT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `reftype` varchar(50) DEFAULT NULL,
  `refdate` date DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `amtpaid` decimal(16,4) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_waterworks_credit_refid` (`refid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_formula` */

DROP TABLE IF EXISTS `waterworks_formula`;

CREATE TABLE `waterworks_formula` (
  `classificationid` varchar(100) NOT NULL,
  `varname` varchar(50) DEFAULT NULL,
  `expr` longtext,
  PRIMARY KEY (`classificationid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_installment` */

DROP TABLE IF EXISTS `waterworks_installment`;

CREATE TABLE `waterworks_installment` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(10) DEFAULT NULL,
  `controlno` varchar(100) DEFAULT NULL,
  `doctype` varchar(10) DEFAULT NULL,
  `dtfiled` datetime DEFAULT NULL,
  `particulars` varchar(255) DEFAULT NULL,
  `term` int(11) DEFAULT NULL,
  `downpayment` decimal(16,2) DEFAULT NULL,
  `enddate` date DEFAULT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  `applicationid` varchar(50) DEFAULT NULL,
  `installmentamount` decimal(16,2) DEFAULT NULL,
  `lastbilldate` date DEFAULT NULL,
  `amtbilled` decimal(16,2) DEFAULT NULL,
  `amount` decimal(16,2) DEFAULT NULL,
  `amtpaid` decimal(16,2) DEFAULT NULL,
  `txntype_objid` varchar(50) DEFAULT NULL,
  `startyear` int(11) DEFAULT NULL,
  `startmonth` int(11) DEFAULT NULL,
  `txntypeid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_acctid` (`acctid`),
  KEY `ix_applicationid` (`applicationid`),
  KEY `ix_enddate` (`enddate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_material` */

DROP TABLE IF EXISTS `waterworks_material`;

CREATE TABLE `waterworks_material` (
  `objid` varchar(50) NOT NULL,
  `code` varchar(10) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `unitprice` decimal(16,2) DEFAULT NULL,
  `unit` varchar(10) DEFAULT NULL,
  `installmentprice` decimal(16,2) DEFAULT NULL,
  `unitcost` decimal(16,2) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_waterworks_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_meter` */

DROP TABLE IF EXISTS `waterworks_meter`;

CREATE TABLE `waterworks_meter` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `serialno` varchar(50) DEFAULT NULL,
  `brand` varchar(50) DEFAULT NULL,
  `sizeid` varchar(50) DEFAULT NULL,
  `capacity` int(11) DEFAULT NULL,
  `stocktype` varchar(50) DEFAULT NULL,
  `lastreading` int(11) DEFAULT NULL,
  `lastreadingdate` date DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_serialno` (`serialno`),
  KEY `ix_brand` (`brand`),
  KEY `fk_sizeid` (`sizeid`),
  CONSTRAINT `waterworks_meter_ibfk_2` FOREIGN KEY (`sizeid`) REFERENCES `waterworks_metersize` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_metersize` */

DROP TABLE IF EXISTS `waterworks_metersize`;

CREATE TABLE `waterworks_metersize` (
  `objid` varchar(50) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_mobile_header` */

DROP TABLE IF EXISTS `waterworks_mobile_header`;

CREATE TABLE `waterworks_mobile_header` (
  `batchid` varchar(50) NOT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`batchid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_mobile_info` */

DROP TABLE IF EXISTS `waterworks_mobile_info`;

CREATE TABLE `waterworks_mobile_info` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `acctno` varchar(50) DEFAULT NULL,
  `acctname` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `serialno` varchar(50) DEFAULT NULL,
  `sectorid` varchar(50) DEFAULT NULL,
  `sectorcode` varchar(50) DEFAULT NULL,
  `lastreading` int(11) DEFAULT NULL,
  `classificationid` varchar(50) DEFAULT NULL,
  `barcode` varchar(50) DEFAULT NULL,
  `batchid` varchar(50) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `period` varchar(50) DEFAULT NULL,
  `duedate` date DEFAULT NULL,
  `discodate` date DEFAULT NULL,
  `rundate` datetime DEFAULT NULL,
  `items` longtext,
  `info` longtext,
  `itemaccountid` varchar(50) DEFAULT NULL,
  `stuboutid` varchar(50) DEFAULT NULL,
  `sortorder` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_itemaccountid` (`itemaccountid`),
  KEY `ix_batchid` (`batchid`),
  KEY `ix_classificationid` (`classificationid`),
  KEY `ix_stuboutid` (`stuboutid`),
  KEY `ix_sectorid` (`sectorid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_otherfee` */

DROP TABLE IF EXISTS `waterworks_otherfee`;

CREATE TABLE `waterworks_otherfee` (
  `objid` varchar(50) NOT NULL,
  `acctid` varchar(50) NOT NULL,
  `item_objid` varchar(50) DEFAULT NULL,
  `item_code` varchar(50) DEFAULT NULL,
  `item_title` varchar(255) DEFAULT NULL,
  `amount` decimal(16,2) DEFAULT NULL,
  `amtpaid` decimal(16,2) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_waterworks_consumption_year_month` (`acctid`,`month`,`year`),
  KEY `ix_year` (`year`),
  KEY `ix_month` (`month`),
  KEY `fks_waterworks_otherfee_acctid` (`acctid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_payment` */

DROP TABLE IF EXISTS `waterworks_payment`;

CREATE TABLE `waterworks_payment` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `rootid` varchar(50) DEFAULT NULL,
  `refno` varchar(50) DEFAULT NULL,
  `reftype` varchar(50) DEFAULT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `refdate` date DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `voided` int(11) DEFAULT NULL,
  `txnmode` varchar(50) DEFAULT NULL,
  `parentschemaname` varchar(50) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_waterworks_payment_account` (`parentid`),
  KEY `fk_waterworks_payment_application` (`rootid`),
  KEY `ix_acctid` (`parentid`),
  KEY `ix_refid` (`refid`),
  KEY `ix_refno` (`refno`),
  KEY `ix_refdate` (`refdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_payment_item` */

DROP TABLE IF EXISTS `waterworks_payment_item`;

CREATE TABLE `waterworks_payment_item` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `reftype` varchar(50) DEFAULT NULL,
  `remarks` varchar(50) DEFAULT NULL,
  `item_objid` varchar(50) DEFAULT NULL,
  `item_title` varchar(50) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT '0.00',
  `discount` decimal(10,2) DEFAULT '0.00',
  `surcharge` decimal(10,2) DEFAULT '0.00',
  `interest` decimal(10,2) DEFAULT '0.00',
  `txntype` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_waterworks_payment_parent` (`parentid`),
  KEY `ix_parentid` (`parentid`),
  KEY `ix_item_objid` (`item_objid`),
  CONSTRAINT `waterworks_payment_item_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `waterworks_payment` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_sector` */

DROP TABLE IF EXISTS `waterworks_sector`;

CREATE TABLE `waterworks_sector` (
  `objid` varchar(50) NOT NULL,
  `code` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_stubout` */

DROP TABLE IF EXISTS `waterworks_stubout`;

CREATE TABLE `waterworks_stubout` (
  `objid` varchar(50) NOT NULL,
  `code` varchar(50) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `zoneid` varchar(50) DEFAULT NULL,
  `barangay_objid` varchar(50) DEFAULT NULL,
  `barangay_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_zoneid` (`zoneid`),
  KEY `ix_barangay_objid` (`barangay_objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_stubout_node` */

DROP TABLE IF EXISTS `waterworks_stubout_node`;

CREATE TABLE `waterworks_stubout_node` (
  `objid` varchar(50) NOT NULL,
  `stuboutid` varchar(50) DEFAULT NULL,
  `indexno` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_stuboutid` (`stuboutid`),
  CONSTRAINT `waterworks_stubout_node_ibfk_2` FOREIGN KEY (`stuboutid`) REFERENCES `waterworks_stubout` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_txntype` */

DROP TABLE IF EXISTS `waterworks_txntype`;

CREATE TABLE `waterworks_txntype` (
  `objid` varchar(50) NOT NULL,
  `title` varchar(50) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `item_objid` varchar(50) DEFAULT NULL,
  `item_code` varchar(50) DEFAULT NULL,
  `item_title` varchar(255) DEFAULT NULL,
  `item_fund_objid` varchar(50) DEFAULT NULL,
  `item_fund_code` varchar(50) DEFAULT NULL,
  `item_fund_title` varchar(255) DEFAULT NULL,
  `ledgertype` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `waterworks_zone` */

DROP TABLE IF EXISTS `waterworks_zone`;

CREATE TABLE `waterworks_zone` (
  `objid` varchar(50) NOT NULL,
  `sectorid` varchar(50) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `schedule_objid` varchar(50) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_waterworks_zone_sector` (`sectorid`,`code`),
  KEY `waterworks_zone_schedule` (`schedule_objid`),
  CONSTRAINT `waterworks_zone_ibfk_2` FOREIGN KEY (`sectorid`) REFERENCES `waterworks_sector` (`objid`),
  CONSTRAINT `waterworks_zone_schedule` FOREIGN KEY (`schedule_objid`) REFERENCES `waterworks_block_schedule` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vw_waterworks_stubout_node` */

DROP TABLE IF EXISTS `vw_waterworks_stubout_node`;

/*!50001 DROP VIEW IF EXISTS `vw_waterworks_stubout_node` */;
/*!50001 DROP TABLE IF EXISTS `vw_waterworks_stubout_node` */;

/*!50001 CREATE TABLE  `vw_waterworks_stubout_node`(
 `objid` varchar(50) ,
 `indexno` int(11) ,
 `stubout_objid` varchar(50) ,
 `stubout_code` varchar(50) ,
 `zone_objid` varchar(50) ,
 `zone_code` varchar(50) ,
 `sector_objid` varchar(50) ,
 `sector_code` varchar(50) ,
 `barangay_objid` varchar(50) ,
 `barangay_name` varchar(100) ,
 `schedule_objid` varchar(50) ,
 `acctid` varchar(50) 
)*/;

/*View structure for view vw_waterworks_stubout_node */

/*!50001 DROP TABLE IF EXISTS `vw_waterworks_stubout_node` */;
/*!50001 DROP VIEW IF EXISTS `vw_waterworks_stubout_node` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vw_waterworks_stubout_node` AS select `son`.`objid` AS `objid`,`son`.`indexno` AS `indexno`,`so`.`objid` AS `stubout_objid`,`so`.`code` AS `stubout_code`,`zon`.`objid` AS `zone_objid`,`zon`.`code` AS `zone_code`,`sec`.`objid` AS `sector_objid`,`sec`.`code` AS `sector_code`,`so`.`barangay_objid` AS `barangay_objid`,`so`.`barangay_name` AS `barangay_name`,`sked`.`objid` AS `schedule_objid`,`wa`.`objid` AS `acctid` from (((((`waterworks_stubout_node` `son` join `waterworks_stubout` `so` on((`son`.`stuboutid` = `so`.`objid`))) join `waterworks_zone` `zon` on((`so`.`zoneid` = `zon`.`objid`))) join `waterworks_sector` `sec` on((`zon`.`sectorid` = `sec`.`objid`))) left join `waterworks_block_schedule` `sked` on((`zon`.`schedule_objid` = `sked`.`objid`))) left join `waterworks_account` `wa` on((`wa`.`stuboutnodeid` = `son`.`objid`))) */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
