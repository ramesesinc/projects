/*
SQLyog Ultimate v9.51 
MySQL - 5.5.39 : Database - market
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `market` */

CREATE TABLE `market` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(20) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `address_type` varchar(50) DEFAULT NULL,
  `address_barangay_objid` varchar(50) DEFAULT NULL,
  `address_barangay_name` varchar(50) DEFAULT NULL,
  `address_text` varchar(255) DEFAULT NULL,
  `address_city` varchar(50) DEFAULT NULL,
  `address_municipality` varchar(50) DEFAULT NULL,
  `address_province` varchar(50) DEFAULT NULL,
  `address_street` varchar(50) DEFAULT NULL,
  `address_subdivision` varchar(50) DEFAULT NULL,
  `address_unitno` varchar(50) DEFAULT NULL,
  `address_bldgno` varchar(25) DEFAULT NULL,
  `address_bldgname` varchar(50) DEFAULT NULL,
  `address_pin` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `market_account` */

CREATE TABLE `market_account` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `acctno` varchar(50) DEFAULT NULL,
  `acctname` varchar(255) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `owner_objid` varchar(50) DEFAULT NULL,
  `owner_name` varchar(50) DEFAULT NULL,
  `owner_address_text` varchar(50) DEFAULT NULL,
  `unit_objid` varchar(50) DEFAULT NULL,
  `startdate` date DEFAULT NULL,
  `lastpmtdate` date DEFAULT NULL,
  `totalprincipalpaid` decimal(18,2) DEFAULT NULL,
  `duedate` date DEFAULT NULL,
  `padlockdate` date DEFAULT NULL,
  `rate` decimal(10,0) DEFAULT NULL,
  `paymentterm` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `market_application` */

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
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `market_application_task` */

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
  CONSTRAINT `fx_market_application_task` FOREIGN KEY (`refid`) REFERENCES `market_application` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `market_compromise` */

CREATE TABLE `market_compromise` (
  `objid` varchar(50) NOT NULL,
  `refno` varchar(50) DEFAULT NULL,
  `refdate` date DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `todate` date DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `market_payment` */

CREATE TABLE `market_payment` (
  `objid` varchar(50) NOT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  `txnno` varchar(50) DEFAULT NULL,
  `txndate` date DEFAULT NULL,
  `particulars` varchar(255) DEFAULT NULL,
  `amount` decimal(18,2) DEFAULT NULL,
  `voided` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `acctid` (`acctid`),
  CONSTRAINT `market_payment_ibfk_1` FOREIGN KEY (`acctid`) REFERENCES `market_account` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `market_payment_item` */

CREATE TABLE `market_payment_item` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `amtpaid` decimal(18,2) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `imonth` int(11) DEFAULT NULL,
  `iyear` int(11) DEFAULT NULL,
  `surchargepaid` decimal(18,2) DEFAULT NULL,
  `interestpaid` decimal(18,2) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `parentid` (`parentid`),
  CONSTRAINT `market_payment_item_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `market_payment` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `market_rentalunit` */

CREATE TABLE `market_rentalunit` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(20) DEFAULT NULL,
  `marketid` varchar(50) DEFAULT NULL,
  `unittype` varchar(20) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `sectionid` varchar(50) DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  `lng` decimal(18,10) DEFAULT NULL,
  `lat` decimal(18,10) DEFAULT NULL,
  `paymentterm` varchar(50) DEFAULT NULL,
  `rate` decimal(10,2) DEFAULT NULL,
  `currentacctid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_market_rentalunit_code` (`code`),
  KEY `section_objid` (`sectionid`),
  CONSTRAINT `fk_market_rentalunit_section` FOREIGN KEY (`sectionid`) REFERENCES `market_section` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `market_section` */

CREATE TABLE `market_section` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `system` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
