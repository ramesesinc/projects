/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.1.73-community : Database - market
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `market_account` */

DROP TABLE IF EXISTS `market_account`;

CREATE TABLE `market_account` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `acctno` varchar(50) DEFAULT NULL,
  `acctname` varchar(255) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `owner_objid` varchar(50) DEFAULT NULL,
  `owner_name` varchar(50) DEFAULT NULL,
  `owner_address_text` varchar(255) DEFAULT NULL,
  `unitid` varchar(50) DEFAULT NULL,
  `dtstarted` date DEFAULT NULL,
  `totalprincipalpaid` decimal(18,2) DEFAULT NULL,
  `duedate` date DEFAULT NULL,
  `extrate` decimal(16,4) DEFAULT NULL,
  `txntype` varchar(50) DEFAULT NULL,
  `createdby_objid` varchar(50) DEFAULT NULL,
  `createdby_name` varchar(255) DEFAULT NULL,
  `lastpaymentid` varchar(50) DEFAULT NULL,
  `payfrequency` varchar(50) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `dateclosed` date DEFAULT NULL,
  `partialbalance` decimal(18,2) DEFAULT NULL,
  `unitno` varchar(50) DEFAULT NULL,
  `extarea` varchar(50) DEFAULT NULL,
  `business_objid` varchar(50) DEFAULT NULL,
  `business_bin` varchar(50) DEFAULT NULL,
  `business_tradename` varchar(255) DEFAULT NULL,
  `business_owner_name` varchar(255) DEFAULT NULL,
  `lastdatepaid` date DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_acctno` (`acctno`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_createdby_objid` (`createdby_objid`),
  KEY `ix_owner_objid` (`owner_objid`),
  KEY `ix_unit_objid` (`unitid`),
  KEY `ix_startdate` (`dtstarted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_account` */

/*Table structure for table `market_application` */

DROP TABLE IF EXISTS `market_application`;

CREATE TABLE `market_application` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `appno` varchar(50) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `createdby_objid` varchar(50) DEFAULT NULL,
  `createdby_name` varchar(255) DEFAULT NULL,
  `owner_objid` varchar(50) DEFAULT NULL,
  `owner_name` varchar(50) DEFAULT NULL,
  `owner_address_text` varchar(50) DEFAULT NULL,
  `unit_objid` varchar(50) DEFAULT NULL,
  `sectionid` varchar(50) DEFAULT NULL,
  `clusterid` varchar(50) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `ix_appno` (`appno`),
  KEY `ix_state` (`state`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_createdby_objid` (`createdby_objid`),
  KEY `ix_owner_objid` (`owner_objid`),
  KEY `ix_unit_objid` (`unit_objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_application` */

/*Table structure for table `market_application_task` */

DROP TABLE IF EXISTS `market_application_task`;

CREATE TABLE `market_application_task` (
  `taskid` varchar(50) NOT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `parentprocessid` varchar(50) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `startdate` datetime DEFAULT NULL,
  `enddate` datetime DEFAULT NULL,
  `assignee_objid` varchar(50) DEFAULT NULL,
  `assignee_name` varchar(100) DEFAULT NULL,
  `actor_objid` varchar(50) DEFAULT NULL,
  `actor_name` varchar(100) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `prevtaskid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`taskid`),
  KEY `fx_market_application_task` (`refid`),
  KEY `ix_refid` (`refid`),
  KEY `ix_parentprocessid` (`parentprocessid`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_startdate` (`startdate`),
  KEY `ix_enddate` (`enddate`),
  KEY `ix_assignee_objid` (`assignee_objid`),
  KEY `ix_actor_objid` (`actor_objid`),
  CONSTRAINT `fk_market_application_task_refid` FOREIGN KEY (`refid`) REFERENCES `market_application` (`objid`),
  CONSTRAINT `market_application_task_ibfk_1` FOREIGN KEY (`refid`) REFERENCES `market_application` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_application_task` */

/*Table structure for table `market_attribute` */

DROP TABLE IF EXISTS `market_attribute`;

CREATE TABLE `market_attribute` (
  `name` varchar(50) NOT NULL,
  `title` varchar(155) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_attribute` */

/*Table structure for table `market_billitem_txntype` */

DROP TABLE IF EXISTS `market_billitem_txntype`;

CREATE TABLE `market_billitem_txntype` (
  `objid` varchar(50) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_billitem_txntype` */

insert  into `market_billitem_txntype`(`objid`,`title`) values ('ELECTRICITY','ELECTRICITY'),('EXT','EXTENSION FEE'),('FEE','FEE'),('INTEREST','INTEREST'),('MARKETRENTAL','MARKET RENTAL'),('SURCHARGE','SURCHARGE'),('WATER','WATER');

/*Table structure for table `market_cluster` */

DROP TABLE IF EXISTS `market_cluster`;

CREATE TABLE `market_cluster` (
  `objid` varchar(50) NOT NULL,
  `market_objid` varchar(50) DEFAULT NULL,
  `market_name` varchar(255) DEFAULT NULL,
  `market_barangay_objid` varchar(50) DEFAULT NULL,
  `market_barangay_name` varchar(255) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_marketid` (`market_objid`),
  KEY `ix_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_cluster` */

/*Table structure for table `market_compromise` */

DROP TABLE IF EXISTS `market_compromise`;

CREATE TABLE `market_compromise` (
  `objid` varchar(50) NOT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `fromdate` date DEFAULT NULL,
  `todate` date DEFAULT NULL,
  `downpayment` decimal(16,4) DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `amtpaid` decimal(16,4) DEFAULT NULL,
  `expirydate` date DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_todate` (`todate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_compromise` */

/*Table structure for table `market_delinquency_header` */

DROP TABLE IF EXISTS `market_delinquency_header`;

CREATE TABLE `market_delinquency_header` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(10) DEFAULT NULL,
  `period` date DEFAULT NULL,
  `totalcount` int(11) DEFAULT NULL,
  `recordsprocessed` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_period` (`period`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_delinquency_header` */

/*Table structure for table `market_delinquency_item` */

DROP TABLE IF EXISTS `market_delinquency_item`;

CREATE TABLE `market_delinquency_item` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  `months` int(11) DEFAULT NULL,
  `days` int(11) DEFAULT NULL,
  `rate` decimal(18,4) DEFAULT NULL,
  `extrate` decimal(18,4) DEFAULT NULL,
  `surcharge` decimal(18,4) DEFAULT NULL,
  `interest` decimal(18,4) DEFAULT NULL,
  `lastdatepaid` date DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_parentid_acctid` (`parentid`,`acctid`),
  KEY `ix_parentid` (`parentid`),
  KEY `ix_acctid` (`acctid`),
  KEY `ix_lastpmtdate` (`lastdatepaid`),
  CONSTRAINT `market_delinquency_item_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `market_delinquency_header` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_delinquency_item` */

/*Table structure for table `market_installment` */

DROP TABLE IF EXISTS `market_installment`;

CREATE TABLE `market_installment` (
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

/*Data for the table `market_installment` */

/*Table structure for table `market_otherfee_ledger` */

DROP TABLE IF EXISTS `market_otherfee_ledger`;

CREATE TABLE `market_otherfee_ledger` (
  `objid` varchar(50) NOT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  `txntypeid` varchar(50) DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `amtpaid` decimal(16,4) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `createdby_objid` varchar(50) DEFAULT NULL,
  `createdby_name` varchar(155) DEFAULT NULL,
  `dtcreated` date DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `remarks` varchar(155) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_otherfee_ledger` */

/*Table structure for table `market_payment` */

DROP TABLE IF EXISTS `market_payment`;

CREATE TABLE `market_payment` (
  `objid` varchar(50) NOT NULL,
  `txndate` datetime DEFAULT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `voided` int(11) DEFAULT NULL,
  `reftype` varchar(50) DEFAULT NULL,
  `refno` varchar(50) DEFAULT NULL,
  `refdate` date DEFAULT NULL,
  `txnmode` varchar(50) DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_refid` (`refid`),
  KEY `ix_refno` (`refno`),
  KEY `ix_refdate` (`refdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_payment` */

insert  into `market_payment`(`objid`,`txndate`,`refid`,`voided`,`reftype`,`refno`,`refdate`,`txnmode`,`amount`) values ('MKTPMT79bf7c51:15f476a8c8b:-7fe7','2017-10-23 13:22:33','RCT79bf7c51:15f476a8c8b:-7fee',0,'cashreceipt','TEST5190030','2017-10-23','ONLINE',2312.6900);

/*Table structure for table `market_payment_otherfee` */

DROP TABLE IF EXISTS `market_payment_otherfee`;

CREATE TABLE `market_payment_otherfee` (
  `objid` varchar(50) NOT NULL,
  `paymentid` varchar(50) DEFAULT NULL,
  `ledgerid` varchar(50) DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `surcharge` decimal(16,4) DEFAULT NULL,
  `interest` decimal(16,4) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_payment_otherfee` */

/*Table structure for table `market_payment_rental` */

DROP TABLE IF EXISTS `market_payment_rental`;

CREATE TABLE `market_payment_rental` (
  `objid` varchar(50) NOT NULL,
  `paymentid` varchar(50) DEFAULT NULL,
  `ledgerid` varchar(50) DEFAULT NULL,
  `fromdate` date DEFAULT NULL,
  `todate` date DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `surcharge` decimal(16,4) DEFAULT NULL,
  `interest` decimal(16,4) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `parentid` (`paymentid`),
  CONSTRAINT `fk_market_payment_rental_payment` FOREIGN KEY (`paymentid`) REFERENCES `market_payment` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_payment_rental` */

/*Table structure for table `market_payment_utility` */

DROP TABLE IF EXISTS `market_payment_utility`;

CREATE TABLE `market_payment_utility` (
  `objid` varchar(50) NOT NULL,
  `paymentid` varchar(50) DEFAULT NULL,
  `ledgerid` varchar(50) DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `surcharge` decimal(16,4) DEFAULT NULL,
  `interest` decimal(16,4) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_payment_utility` */

/*Table structure for table `market_property` */

DROP TABLE IF EXISTS `market_property`;

CREATE TABLE `market_property` (
  `objid` varchar(50) NOT NULL,
  `bldgno` varchar(50) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `street` varchar(100) DEFAULT NULL,
  `subdivision` varchar(100) DEFAULT NULL,
  `barangay_objid` varchar(50) DEFAULT NULL,
  `barangay_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_bldgname` (`name`),
  KEY `ix_barangay_objid` (`barangay_objid`),
  KEY `ix_barangay_name` (`barangay_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_property` */

/*Table structure for table `market_rental_ledger` */

DROP TABLE IF EXISTS `market_rental_ledger`;

CREATE TABLE `market_rental_ledger` (
  `objid` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `acctid` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `days` int(11) DEFAULT NULL,
  `fromdate` date DEFAULT NULL,
  `todate` date DEFAULT NULL,
  `currdate` date DEFAULT NULL,
  `rate` decimal(16,4) DEFAULT NULL,
  `ratetype` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `amtpaid` decimal(16,4) DEFAULT NULL,
  UNIQUE KEY `uix_acct_year_month` (`acctid`,`year`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `market_rental_ledger` */

/*Table structure for table `market_rentalunit` */

DROP TABLE IF EXISTS `market_rentalunit`;

CREATE TABLE `market_rentalunit` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(20) DEFAULT NULL,
  `unittype` varchar(20) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `sectionid` varchar(50) DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  `lng` decimal(18,10) DEFAULT NULL,
  `lat` decimal(18,10) DEFAULT NULL,
  `currentacctid` varchar(50) DEFAULT NULL,
  `areasqm` decimal(16,2) DEFAULT NULL,
  `clusterid` varchar(50) DEFAULT NULL,
  `indexno` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_market_rentalunit_code` (`code`),
  KEY `section_objid` (`sectionid`),
  KEY `ix_clusterid` (`clusterid`),
  KEY `ix_currentacctid` (`currentacctid`),
  CONSTRAINT `market_rentalunit_ibfk_1` FOREIGN KEY (`sectionid`) REFERENCES `market_section` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_rentalunit` */

/*Table structure for table `market_rentalunit_attribute` */

DROP TABLE IF EXISTS `market_rentalunit_attribute`;

CREATE TABLE `market_rentalunit_attribute` (
  `objid` varchar(50) NOT NULL,
  `unitid` varchar(50) DEFAULT NULL,
  `attributeid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_marketunitattr_unit` (`unitid`),
  KEY `fk_marketunitattr_attr` (`attributeid`),
  CONSTRAINT `fk_marketunitattr_attr` FOREIGN KEY (`attributeid`) REFERENCES `market_attribute` (`name`),
  CONSTRAINT `fk_marketunitattr_unit` FOREIGN KEY (`unitid`) REFERENCES `market_rentalunit` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_rentalunit_attribute` */

/*Table structure for table `market_section` */

DROP TABLE IF EXISTS `market_section`;

CREATE TABLE `market_section` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `system` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `uix_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_section` */

/*Table structure for table `market_transfer_application` */

DROP TABLE IF EXISTS `market_transfer_application`;

CREATE TABLE `market_transfer_application` (
  `objid` varchar(50) NOT NULL,
  `appno` varchar(50) DEFAULT NULL,
  `appdate` date DEFAULT NULL,
  `createdby_objid` varchar(50) DEFAULT NULL,
  `createdby_name` varchar(155) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `prevacctid` varchar(50) DEFAULT NULL,
  `prevacctname` varchar(255) DEFAULT NULL,
  `prevowner_objid` varchar(255) DEFAULT NULL,
  `prevowner_name` varchar(50) DEFAULT NULL,
  `newowner_objid` varchar(50) DEFAULT NULL,
  `newowner_name` varchar(255) DEFAULT NULL,
  `newowner_address_text` varchar(255) DEFAULT NULL,
  `relationship` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `market_transfer_application` */

/*Table structure for table `market_utility` */

DROP TABLE IF EXISTS `market_utility`;

CREATE TABLE `market_utility` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  `currentreading` int(11) DEFAULT NULL,
  `nextreadingdate` date DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_acctid` (`acctid`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `market_utility` */

/*Table structure for table `market_utility_ledger` */

DROP TABLE IF EXISTS `market_utility_ledger`;

CREATE TABLE `market_utility_ledger` (
  `objid` varchar(50) CHARACTER SET latin1 NOT NULL,
  `parentid` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `state` varchar(10) CHARACTER SET latin1 DEFAULT NULL,
  `createdby_objid` varchar(50) DEFAULT NULL,
  `createdby_name` varchar(155) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `txntypeid` varchar(50) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `readingdate` date DEFAULT NULL,
  `prevreading` int(11) DEFAULT NULL,
  `reading` int(11) DEFAULT NULL,
  `rate` decimal(16,4) DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `amtpaid` decimal(16,4) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_acct_year_month` (`parentid`,`year`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_utility_ledger` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
