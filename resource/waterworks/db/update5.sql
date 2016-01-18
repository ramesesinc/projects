/*
SQLyog Ultimate v11.33 (32 bit)
MySQL - 5.0.27-community-nt : Database - waterworksdb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`waterworksdb` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `waterworksdb`;

/*Table structure for table `waterworks_account` */

DROP TABLE IF EXISTS `waterworks_account`;

CREATE TABLE `waterworks_account` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) default NULL,
  `acctno` varchar(50) default NULL,
  `dtstarted` datetime default NULL,
  `meterid` varchar(50) default NULL,
  `accounttype` varchar(50) default NULL,
  `basicrate` decimal(10,2) default NULL,
  `currentreadingid` varchar(50) default NULL,
  `entity_objid` varchar(50) default NULL,
  `entity_name` text,
  `entity_address` text,
  PRIMARY KEY  (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `waterworks_account` */

/*Table structure for table `waterworks_account_payment` */

DROP TABLE IF EXISTS `waterworks_account_payment`;

CREATE TABLE `waterworks_account_payment` (
  `paymentid` varchar(50) default NULL,
  `acctid` varchar(50) default NULL,
  KEY `paymentid_fk` (`paymentid`),
  CONSTRAINT `paymentid_fk` FOREIGN KEY (`paymentid`) REFERENCES `waterworks_payment` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `waterworks_account_payment` */

/*Table structure for table `waterworks_account_route` */

DROP TABLE IF EXISTS `waterworks_account_route`;

CREATE TABLE `waterworks_account_route` (
  `routeid` varchar(50) NOT NULL,
  `acctid` varchar(50) NOT NULL,
  KEY `routeid_fk` (`routeid`),
  KEY `acctid_fk` (`acctid`),
  CONSTRAINT `acctid_fk` FOREIGN KEY (`acctid`) REFERENCES `waterworks_account` (`objid`),
  CONSTRAINT `routeid_fk` FOREIGN KEY (`routeid`) REFERENCES `waterworks_route` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `waterworks_account_route` */

/*Table structure for table `waterworks_meter` */

DROP TABLE IF EXISTS `waterworks_meter`;

CREATE TABLE `waterworks_meter` (
  `objid` varchar(50) NOT NULL,
  `serialno` varchar(50) default NULL,
  `brand` varchar(50) default NULL,
  `capacity` varchar(50) default NULL,
  `size` varchar(50) default NULL,
  PRIMARY KEY  (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `waterworks_meter` */

/*Table structure for table `waterworks_meter_reading` */

DROP TABLE IF EXISTS `waterworks_meter_reading`;

CREATE TABLE `waterworks_meter_reading` (
  `objid` varchar(50) NOT NULL,
  `acctid` varchar(50) default NULL,
  `meterid` varchar(50) default NULL,
  `dtread` datetime default NULL,
  `year` int(11) default NULL,
  `month` int(11) default NULL,
  `previousreading` decimal(10,2) default NULL,
  `currentreading` decimal(10,2) default NULL,
  `consumed` decimal(10,2) default NULL,
  `amount` decimal(10,2) default NULL,
  `method` varchar(50) default NULL,
  `reader_objid` varchar(50) default NULL,
  `reader_name` text,
  PRIMARY KEY  (`objid`),
  KEY `meterid_fk` (`meterid`),
  CONSTRAINT `meterid_fk` FOREIGN KEY (`meterid`) REFERENCES `waterworks_meter` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `waterworks_meter_reading` */

/*Table structure for table `waterworks_payment` */

DROP TABLE IF EXISTS `waterworks_payment`;

CREATE TABLE `waterworks_payment` (
  `objid` varchar(50) NOT NULL,
  `refid` varchar(50) default NULL,
  `refno` varchar(50) default NULL,
  `refdate` datetime default NULL,
  `reftype` varchar(50) default NULL,
  `amount` decimal(10,2) default NULL,
  `remarks` text,
  PRIMARY KEY  (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `waterworks_payment` */

/*Table structure for table `waterworks_route` */

DROP TABLE IF EXISTS `waterworks_route`;

CREATE TABLE `waterworks_route` (
  `objid` varchar(50) NOT NULL,
  `code` varchar(50) default NULL,
  `name` varchar(50) default NULL,
  `description` text,
  PRIMARY KEY  (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `waterworks_route` */

/*Table structure for table `waterworks_schedule` */

DROP TABLE IF EXISTS `waterworks_schedule`;

CREATE TABLE `waterworks_schedule` (
  `objid` varchar(50) NOT NULL,
  `month` int(11) default NULL,
  `year` int(11) default NULL,
  `duedate` date default NULL,
  `cuttingdate` date default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  PRIMARY KEY  (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `waterworks_schedule` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
