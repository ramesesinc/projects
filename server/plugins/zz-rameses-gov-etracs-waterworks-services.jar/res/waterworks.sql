/*
SQLyog Ultimate v9.51 
MySQL - 5.5.39 : Database - waterworks
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `waterworks_account` */

CREATE TABLE `waterworks_account` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `dtstarted` datetime DEFAULT NULL,
  `acctno` varchar(50) DEFAULT NULL,
  `applicationid` varchar(50) DEFAULT NULL,
  `acctname` varchar(255) DEFAULT NULL,
  `owner_objid` varchar(50) DEFAULT NULL,
  `owner_name` varchar(255) DEFAULT NULL,
  `address_objid` varchar(50) DEFAULT NULL,
  `address_text` varchar(255) DEFAULT NULL,
  `mobileno` varchar(50) DEFAULT NULL,
  `phoneno` varchar(50) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `meterid` varchar(50) DEFAULT NULL,
  `classificationid` varchar(50) DEFAULT NULL,
  `areaid` varchar(50) DEFAULT NULL,
  `lastreading` int(11) DEFAULT NULL,
  `balance` decimal(10,2) DEFAULT NULL,
  `lastreadingdate` datetime DEFAULT NULL,
  `lastreadingmonth` int(11) DEFAULT NULL,
  `lastreadingyear` int(11) DEFAULT NULL,
  `lasttxndate` date DEFAULT NULL,
  `prevreading` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_waterworks_account_meter` (`meterid`),
  KEY `fk_waterworks_account_classification` (`classificationid`),
  KEY `fk_waterworks_account_aread` (`areaid`),
  CONSTRAINT `fk_waterworks_account_aread` FOREIGN KEY (`areaid`) REFERENCES `waterworks_area` (`objid`),
  CONSTRAINT `fk_waterworks_account_classification` FOREIGN KEY (`classificationid`) REFERENCES `waterworks_classification` (`objid`),
  CONSTRAINT `fk_waterworks_account_meter` FOREIGN KEY (`meterid`) REFERENCES `waterworks_meter` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `waterworks_account_consumption` */

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
  `amount` decimal(10,2) DEFAULT NULL,
  `amtpaid` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_month_year` (`year`,`month`,`acctid`),
  KEY `fks_waterworks_account_reading` (`acctid`),
  CONSTRAINT `fks_waterworks_account_reading` FOREIGN KEY (`acctid`) REFERENCES `waterworks_account` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `waterworks_area` */

CREATE TABLE `waterworks_area` (
  `objid` varchar(50) NOT NULL,
  `name` text,
  `description` text,
  `assignee_objid` varchar(50) DEFAULT NULL,
  `assignee_name` text,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `waterworks_classification` */

CREATE TABLE `waterworks_classification` (
  `objid` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `waterworks_formula` */

CREATE TABLE `waterworks_formula` (
  `classificationid` varchar(100) NOT NULL,
  `varname` varchar(50) DEFAULT NULL,
  `expr` mediumtext,
  PRIMARY KEY (`classificationid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `waterworks_meter` */

CREATE TABLE `waterworks_meter` (
  `objid` varchar(50) NOT NULL,
  `serialno` varchar(50) DEFAULT NULL,
  `brand` varchar(50) DEFAULT NULL,
  `size` varchar(50) DEFAULT NULL,
  `capacity` varchar(50) DEFAULT NULL,
  `currentacctid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_serialno` (`serialno`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `waterworks_payment` */

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `waterworks_payment_item` */

CREATE TABLE `waterworks_payment_item` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `txnrefid` varchar(50) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  `surcharge` decimal(10,2) DEFAULT NULL,
  `interest` decimal(10,2) DEFAULT NULL,
  `remarks` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_waterworks_payment_parent` (`parentid`),
  KEY `fk_waterworks_payment_item_txnref` (`txnrefid`),
  CONSTRAINT `fk_waterworks_payment_item_txnref` FOREIGN KEY (`txnrefid`) REFERENCES `waterworks_account_consumption` (`objid`),
  CONSTRAINT `fk_waterworks_payment_parent` FOREIGN KEY (`parentid`) REFERENCES `waterworks_payment` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
