/*
SQLyog Ultimate v9.51 
MySQL - 5.5.39 : Database - marketdb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`marketdb` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `marketdb`;

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
  `unit_objid` varchar(50) DEFAULT NULL,
  `startdate` date DEFAULT NULL,
  `totalprincipalpaid` decimal(18,2) DEFAULT NULL,
  `duedate` date DEFAULT NULL,
  `rate` decimal(16,4) DEFAULT NULL,
  `extrate` decimal(16,4) DEFAULT NULL,
  `txntype` varchar(50) DEFAULT NULL,
  `createdby_objid` varchar(50) DEFAULT NULL,
  `createdby_name` varchar(255) DEFAULT NULL,
  `lastpaymentid` varchar(50) DEFAULT NULL,
  `payfrequency` varchar(50) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `dateclosed` date DEFAULT NULL,
  `todate` date DEFAULT NULL,
  `partialbalance` decimal(18,2) DEFAULT NULL,
  `partialextbalance` decimal(18,2) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_acctno` (`acctno`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_createdby_objid` (`createdby_objid`),
  KEY `ix_owner_objid` (`owner_objid`),
  KEY `ix_unit_objid` (`unit_objid`),
  KEY `ix_startdate` (`startdate`),
  KEY `ix_lastpmtdate` (`todate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_account` */

insert  into `market_account`(`objid`,`state`,`acctno`,`acctname`,`dtcreated`,`owner_objid`,`owner_name`,`owner_address_text`,`unit_objid`,`startdate`,`totalprincipalpaid`,`duedate`,`rate`,`extrate`,`txntype`,`createdby_objid`,`createdby_name`,`lastpaymentid`,`payfrequency`,`remarks`,`dateclosed`,`todate`,`partialbalance`,`partialextbalance`) values ('MKTACCT-197a6849:15ab1114062:-7f00','ACTIVE','M1422017030001','NAZARENO, ELMO MANAGASE','2017-03-09 11:17:40','IND744f9bf7:14e802b6cb6:-506e','NAZARENO, ELMO MANAGASE','DAWIS, TALISAY CITY \nTABUNOK, TALISAY CITY\nCEBU','MKTUNT-282a3e9f:15aad0e2492:-7e8b','2016-01-01',NULL,NULL,'450.0000','120.0000','CAPTURED','USR-ADMIN','ADMIN MIDDLE NAME ADMIN',NULL,'MONTHLY','sample',NULL,'2017-01-01','0.00','0.00'),('MKTACCT-606ff128:15ad4bc3d28:-7df1','ACTIVE','999-0001','ANAZAWA, EMILY S','2017-03-16 09:38:17','E69ca62f5:13ca325e0cb:-5f61','ANAZAWA, EMILY S','SCC','MKTUNT-606ff128:15ad4bc3d28:-7d83','2017-01-01',NULL,NULL,'20.0000','15.0000','CAPTURED','USR-ADMIN','ADMIN MIDDLE NAME ADMIN',NULL,'DAILY','new stall',NULL,'2017-01-01','0.00','0.00');

/*Table structure for table `market_account_rate` */

DROP TABLE IF EXISTS `market_account_rate`;

CREATE TABLE `market_account_rate` (
  `objid` varchar(50) DEFAULT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  `fromdate` date DEFAULT NULL,
  `todate` date DEFAULT NULL,
  `rate` decimal(16,4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_account_rate` */

/*Table structure for table `market_application` */

DROP TABLE IF EXISTS `market_application`;

CREATE TABLE `market_application` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `appno` varchar(50) DEFAULT NULL,
  `acctname` varchar(255) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `createdby_objid` varchar(50) DEFAULT NULL,
  `createdby_name` varchar(255) DEFAULT NULL,
  `owner_objid` varchar(50) DEFAULT NULL,
  `owner_name` varchar(50) DEFAULT NULL,
  `owner_address_text` varchar(50) DEFAULT NULL,
  `unit_objid` varchar(50) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `ix_appno` (`appno`),
  KEY `ix_state` (`state`),
  KEY `ix_acctname` (`acctname`),
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

insert  into `market_cluster`(`objid`,`market_objid`,`market_name`,`market_barangay_objid`,`market_barangay_name`,`name`,`description`) values ('MKTCLU-282a3e9f:15aad0e2492:-7e9f','BUSBLDG-247c5aec:156044008e9:-75d6','PREMIUM MARKET','195-00-0004','STO. NIÑO','C2','WEST WING'),('MKTCLU-282a3e9f:15aad0e2492:-7eb0','BUSBLDG-247c5aec:156044008e9:-75d6','PREMIUM MARKET','195-00-0004','STO. NIÑO','C1','EAST WING');

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

/*Table structure for table `market_duedate` */

DROP TABLE IF EXISTS `market_duedate`;

CREATE TABLE `market_duedate` (
  `year` int(11) NOT NULL,
  `month` int(11) NOT NULL,
  `duedate` date DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`year`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_duedate` */

/*Table structure for table `market_payment` */

DROP TABLE IF EXISTS `market_payment`;

CREATE TABLE `market_payment` (
  `objid` varchar(50) NOT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  `txndate` datetime DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `voided` int(11) DEFAULT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `reftype` varchar(50) DEFAULT NULL,
  `refno` varchar(50) DEFAULT NULL,
  `refdate` date DEFAULT NULL,
  `txnmode` varchar(50) DEFAULT NULL,
  `fromdate` date DEFAULT NULL,
  `todate` date DEFAULT NULL,
  `prevlastdatepaid` date DEFAULT NULL,
  `prevpartialbalance` decimal(16,4) DEFAULT NULL,
  `prevpartialextbalance` decimal(16,4) DEFAULT NULL,
  `prevpaymentid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `acctid` (`acctid`),
  KEY `ix_refid` (`refid`),
  KEY `ix_refno` (`refno`),
  KEY `ix_refdate` (`refdate`),
  KEY `ix_fromdate` (`fromdate`),
  KEY `ix_todate` (`todate`),
  CONSTRAINT `market_payment_ibfk_1` FOREIGN KEY (`acctid`) REFERENCES `market_account` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_payment` */

/*Table structure for table `market_payment_item` */

DROP TABLE IF EXISTS `market_payment_item`;

CREATE TABLE `market_payment_item` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `fromdate` date DEFAULT NULL,
  `todate` date DEFAULT NULL,
  `fromday` int(11) DEFAULT NULL,
  `today` int(11) DEFAULT NULL,
  `days` int(11) DEFAULT NULL,
  `rate` decimal(16,4) DEFAULT NULL,
  `extrate` decimal(16,4) DEFAULT NULL,
  `surcharge` decimal(16,4) DEFAULT NULL,
  `interest` decimal(16,4) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `parentid` (`parentid`),
  CONSTRAINT `market_payment_item_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `market_payment` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_payment_item` */

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
  `rate` decimal(10,2) DEFAULT NULL,
  `currentacctid` varchar(50) DEFAULT NULL,
  `areasqm` decimal(16,2) DEFAULT NULL,
  `clusterid` varchar(50) DEFAULT NULL,
  `payfrequency` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_market_rentalunit_code` (`code`),
  KEY `section_objid` (`sectionid`),
  KEY `ix_clusterid` (`clusterid`),
  KEY `ix_currentacctid` (`currentacctid`),
  CONSTRAINT `market_rentalunit_ibfk_1` FOREIGN KEY (`sectionid`) REFERENCES `market_section` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `market_rentalunit` */

insert  into `market_rentalunit`(`objid`,`state`,`unittype`,`code`,`description`,`sectionid`,`location`,`lng`,`lat`,`rate`,`currentacctid`,`areasqm`,`clusterid`,`payfrequency`) values ('MKTUNT-282a3e9f:15aad0e2492:-7e8b','ACTIVE','TABLE','BM-001','BM-001','MKTSECT-1d39827d:15a929c7241:-7e9f',NULL,NULL,NULL,'450.00','MKTACCT-197a6849:15ab1114062:-7f00',NULL,'MKTCLU-282a3e9f:15aad0e2492:-7eb0','MONTHLY'),('MKTUNT-606ff128:15ad4bc3d28:-7d83','ACTIVE','STALL','CM-001','STOCK 2','MKTSECT-1d39827d:15a929c7241:-7e8d',NULL,NULL,NULL,'20.00','MKTACCT-606ff128:15ad4bc3d28:-7df1',NULL,'MKTCLU-282a3e9f:15aad0e2492:-7eb0','DAILY');

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

insert  into `market_section`(`objid`,`state`,`name`,`description`,`system`) values ('MKTSECT-1d39827d:15a929c7241:-7e7b','ACTIVE','COCO_GRINDER','COCO GRINDER',NULL),('MKTSECT-1d39827d:15a929c7241:-7e84','ACTIVE','FRUITS_AND_VEGETABLES','FRUITS AND VEGETABLES',NULL),('MKTSECT-1d39827d:15a929c7241:-7e8d','ACTIVE','FISH_VENDOR_SECTION','FISH VENDOR SECTION',NULL),('MKTSECT-1d39827d:15a929c7241:-7e96','ACTIVE','MEAT','MEAT SECTION',NULL),('MKTSECT-1d39827d:15a929c7241:-7e9f','ACTIVE','CARINDERIA','CARINDERIA',NULL),('MKTSECT-1d39827d:15a929c7241:-7ea8','ACTIVE','DRY_GOODS','DRY GOODS',NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
