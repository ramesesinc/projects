/*
SQLyog Ultimate v9.51 
MySQL - 5.5.39 : Database - vehicledb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`vehicledb` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `vehicledb`;

/*Table structure for table `vehicle_application` */

DROP TABLE IF EXISTS `vehicle_application`;

CREATE TABLE `vehicle_application` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `appno` varchar(50) DEFAULT NULL,
  `appdate` date DEFAULT NULL,
  `apptype` varchar(50) DEFAULT NULL,
  `txnmode` varchar(50) DEFAULT NULL,
  `appyear` int(11) DEFAULT NULL,
  `vehicletype` varchar(50) DEFAULT NULL,
  `controlid` varchar(50) DEFAULT NULL,
  `dtfiled` datetime DEFAULT NULL,
  `filedby_objid` varchar(50) DEFAULT NULL,
  `filedby_name` varchar(255) DEFAULT NULL,
  `owner_objid` varchar(50) DEFAULT NULL,
  `owner_name` varchar(255) DEFAULT NULL,
  `owner_address_objid` varchar(50) DEFAULT NULL,
  `owner_address_text` varchar(255) DEFAULT NULL,
  `particulars` varchar(150) DEFAULT NULL,
  `barangay_objid` varchar(50) DEFAULT NULL,
  `barangay_name` varchar(155) DEFAULT NULL,
  `billexpirydate` date DEFAULT NULL,
  `taskid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_vehicle_application_task` (`taskid`),
  CONSTRAINT `fk_vehicle_application_task` FOREIGN KEY (`taskid`) REFERENCES `vehicle_application_task` (`taskid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_application_fee` */

DROP TABLE IF EXISTS `vehicle_application_fee`;

CREATE TABLE `vehicle_application_fee` (
  `objid` varchar(50) NOT NULL,
  `appid` varchar(50) DEFAULT NULL,
  `item_objid` varchar(50) DEFAULT NULL,
  `item_code` varchar(50) DEFAULT NULL,
  `item_title` varchar(255) DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `amtpaid` decimal(16,4) DEFAULT NULL,
  `txntype` varchar(50) DEFAULT NULL,
  `sortorder` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_applicationfee_application` (`appid`),
  CONSTRAINT `fk_applicationfee_application` FOREIGN KEY (`appid`) REFERENCES `vehicle_application` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_application_fishboat` */

DROP TABLE IF EXISTS `vehicle_application_fishboat`;

CREATE TABLE `vehicle_application_fishboat` (
  `objid` varchar(50) NOT NULL,
  `vesselname` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `length` decimal(10,2) DEFAULT NULL,
  `breadth` decimal(10,2) DEFAULT NULL,
  `depth` decimal(10,2) DEFAULT NULL,
  `grosstonnage` decimal(10,2) DEFAULT NULL,
  `nettonnage` decimal(10,2) DEFAULT NULL,
  `buildername` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `builderplace` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `buildyear` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  CONSTRAINT `fk_application_fishboat` FOREIGN KEY (`objid`) REFERENCES `vehicle_application` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_application_info` */

DROP TABLE IF EXISTS `vehicle_application_info`;

CREATE TABLE `vehicle_application_info` (
  `objid` varchar(50) NOT NULL,
  `appid` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `stringvalue` varchar(255) DEFAULT NULL,
  `decimalvalue` decimal(18,2) DEFAULT NULL,
  `intvalue` int(11) DEFAULT NULL,
  `datevalue` date DEFAULT NULL,
  `booleanvalue` int(11) DEFAULT NULL,
  `lov_objid` varchar(50) DEFAULT NULL,
  `lookup_objid` varchar(50) DEFAULT NULL,
  `lookup_title` varchar(50) DEFAULT NULL,
  `objvalue` mediumtext,
  `sortorder` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_application_pedicab` */

DROP TABLE IF EXISTS `vehicle_application_pedicab`;

CREATE TABLE `vehicle_application_pedicab` (
  `objid` varchar(50) NOT NULL,
  `plateno` varchar(50) NOT NULL,
  `color` varchar(50) DEFAULT NULL,
  `bodyno` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_application_task` */

DROP TABLE IF EXISTS `vehicle_application_task`;

CREATE TABLE `vehicle_application_task` (
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
  KEY `FK_business_application_task_business_application` (`refid`),
  CONSTRAINT `FK_business_application_task_vehicle_application` FOREIGN KEY (`refid`) REFERENCES `vehicle_application` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_application_tricycle` */

DROP TABLE IF EXISTS `vehicle_application_tricycle`;

CREATE TABLE `vehicle_application_tricycle` (
  `objid` varchar(50) NOT NULL,
  `plateno` varchar(50) NOT NULL,
  `engineno` varchar(50) DEFAULT NULL,
  `chassisno` varchar(50) DEFAULT NULL,
  `make` varchar(50) DEFAULT NULL,
  `model` varchar(50) DEFAULT NULL,
  `color` varchar(50) DEFAULT NULL,
  `sidecarno` varchar(50) DEFAULT NULL,
  `bodyno` varchar(50) DEFAULT NULL,
  `sidecarcolor` varchar(50) DEFAULT NULL,
  `crname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  CONSTRAINT `fk_tricycle_application_vehicle` FOREIGN KEY (`objid`) REFERENCES `vehicle_application` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_fishboat_engine` */

DROP TABLE IF EXISTS `vehicle_fishboat_engine`;

CREATE TABLE `vehicle_fishboat_engine` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `make` varchar(50) DEFAULT NULL,
  `serialno` varchar(50) DEFAULT NULL,
  `horsepower` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `vehicle_franchise` */

DROP TABLE IF EXISTS `vehicle_franchise`;

CREATE TABLE `vehicle_franchise` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `clusterid` varchar(50) DEFAULT NULL,
  `controlno` varchar(50) DEFAULT NULL,
  `vehicletype` varchar(50) DEFAULT NULL,
  `expirydate` date DEFAULT NULL,
  `appid` varchar(50) DEFAULT NULL,
  `activeyear` int(11) DEFAULT NULL,
  `startdate` date DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_vehicle_franchise_clusterid` (`clusterid`),
  CONSTRAINT `fk_vehicle_franchise_clusterid` FOREIGN KEY (`clusterid`) REFERENCES `vehicletype_cluster` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_franchise_fee` */

DROP TABLE IF EXISTS `vehicle_franchise_fee`;

CREATE TABLE `vehicle_franchise_fee` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `item_objid` varchar(50) DEFAULT NULL,
  `item_code` varchar(50) DEFAULT NULL,
  `item_title` varchar(255) DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `amtpaid` decimal(16,4) DEFAULT NULL,
  `txntype` varchar(50) DEFAULT NULL,
  `sortorder` int(11) DEFAULT NULL,
  `remarks` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_applicationfee_application` (`parentid`),
  CONSTRAINT `fk_franchise_fee` FOREIGN KEY (`parentid`) REFERENCES `vehicle_franchise` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_payment` */

DROP TABLE IF EXISTS `vehicle_payment`;

CREATE TABLE `vehicle_payment` (
  `objid` varchar(50) NOT NULL,
  `appid` varchar(50) DEFAULT NULL,
  `franchiseid` varchar(50) DEFAULT NULL,
  `txndate` datetime DEFAULT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `reftype` varchar(50) DEFAULT NULL,
  `refno` varchar(50) DEFAULT NULL,
  `refdate` date DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `voided` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `appid` (`appid`),
  KEY `ix_refid` (`refid`),
  KEY `ix_refno` (`refno`),
  KEY `ix_refdate` (`refdate`),
  KEY `fk_vehicle_payment` (`franchiseid`),
  CONSTRAINT `vehcile_application_payment_ibfk_1` FOREIGN KEY (`appid`) REFERENCES `vehicle_application` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_payment_item` */

DROP TABLE IF EXISTS `vehicle_payment_item`;

CREATE TABLE `vehicle_payment_item` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `apprefid` varchar(50) DEFAULT NULL,
  `franchiserefid` varchar(50) DEFAULT NULL,
  `item_objid` varchar(50) DEFAULT NULL,
  `item_code` varchar(50) DEFAULT NULL,
  `item_title` varchar(255) DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `surcharge` decimal(16,4) DEFAULT NULL,
  `interest` decimal(16,4) DEFAULT NULL,
  `discount` decimal(16,4) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `parentid` (`parentid`),
  KEY `fk_vehicle_application_fee_payment` (`apprefid`),
  KEY `fk_franchise_fee_paymentitem` (`franchiserefid`),
  CONSTRAINT `fk_franchise_fee_paymentitem` FOREIGN KEY (`franchiserefid`) REFERENCES `vehicle_franchise` (`objid`),
  CONSTRAINT `fk_vehicle_application_fee_payment` FOREIGN KEY (`apprefid`) REFERENCES `vehicle_application_fee` (`objid`),
  CONSTRAINT `vehcile_application_payment_item_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `vehicle_payment` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_permit` */

DROP TABLE IF EXISTS `vehicle_permit`;

CREATE TABLE `vehicle_permit` (
  `objid` varchar(50) NOT NULL,
  `appid` varchar(50) DEFAULT NULL,
  `permitno` varchar(50) DEFAULT NULL,
  `permittype` varchar(50) DEFAULT NULL,
  `dtissued` date DEFAULT NULL,
  `issuedby_objid` varchar(50) DEFAULT NULL,
  `issuedby_name` varchar(50) DEFAULT NULL,
  `expirydate` date DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_variable` */

DROP TABLE IF EXISTS `vehicle_variable`;

CREATE TABLE `vehicle_variable` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `caption` varchar(100) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `datatype` varchar(50) DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  `sortorder` int(11) DEFAULT NULL,
  `system` int(11) DEFAULT '0',
  `arrayvalues` text,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicletype` */

DROP TABLE IF EXISTS `vehicletype`;

CREATE TABLE `vehicletype` (
  `objid` varchar(50) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `controlpattern` varchar(50) DEFAULT NULL,
  `pattern_appno` varchar(50) DEFAULT NULL,
  `pattern_acctno` varchar(50) DEFAULT NULL,
  `indexno` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicletype_cluster` */

DROP TABLE IF EXISTS `vehicletype_cluster`;

CREATE TABLE `vehicletype_cluster` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(5) DEFAULT NULL,
  `vehicletype` varchar(50) DEFAULT NULL,
  `issued` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_fee` */

DROP TABLE IF EXISTS `vehicle_fee`;

/*!50001 DROP VIEW IF EXISTS `vehicle_fee` */;
/*!50001 DROP TABLE IF EXISTS `vehicle_fee` */;

/*!50001 CREATE TABLE  `vehicle_fee`(
 `objid` varchar(50) ,
 `vehicletype` varchar(50) ,
 `appno` varchar(50) ,
 `controlno` varchar(50) ,
 `appid` varchar(50) ,
 `controlid` varchar(50) ,
 `item_objid` varchar(50) ,
 `item_code` varchar(50) ,
 `item_title` varchar(255) ,
 `amount` decimal(16,4) ,
 `amtpaid` decimal(16,4) ,
 `txntype` varchar(50) ,
 `sortorder` int(11) ,
 `ledgertype` varchar(11) ,
 `remarks` varchar(50) 
)*/;

/*Table structure for table `vehicle_franchise_tricycle` */

DROP TABLE IF EXISTS `vehicle_franchise_tricycle`;

/*!50001 DROP VIEW IF EXISTS `vehicle_franchise_tricycle` */;
/*!50001 DROP TABLE IF EXISTS `vehicle_franchise_tricycle` */;

/*!50001 CREATE TABLE  `vehicle_franchise_tricycle`(
 `objid` varchar(50) ,
 `state` varchar(50) ,
 `clusterid` varchar(50) ,
 `controlno` varchar(50) ,
 `vehicletype` varchar(50) ,
 `expirydate` date ,
 `appid` varchar(50) ,
 `activeyear` int(11) ,
 `startdate` date ,
 `plateno` varchar(50) ,
 `engineno` varchar(50) ,
 `chassisno` varchar(50) ,
 `make` varchar(50) ,
 `model` varchar(50) ,
 `color` varchar(50) ,
 `sidecarno` varchar(50) ,
 `bodyno` varchar(50) ,
 `sidecarcolor` varchar(50) ,
 `crname` varchar(255) ,
 `owner_objid` varchar(50) ,
 `owner_name` varchar(255) ,
 `owner_address_objid` varchar(50) ,
 `owner_address_text` varchar(255) ,
 `barangay_objid` varchar(50) ,
 `barangay_name` varchar(155) ,
 `billexpirydate` date 
)*/;

/*View structure for view vehicle_fee */

/*!50001 DROP TABLE IF EXISTS `vehicle_fee` */;
/*!50001 DROP VIEW IF EXISTS `vehicle_fee` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vehicle_fee` AS select `vf`.`objid` AS `objid`,`va`.`vehicletype` AS `vehicletype`,`va`.`appno` AS `appno`,`f`.`controlno` AS `controlno`,`vf`.`appid` AS `appid`,`va`.`controlid` AS `controlid`,`vf`.`item_objid` AS `item_objid`,`vf`.`item_code` AS `item_code`,`vf`.`item_title` AS `item_title`,`vf`.`amount` AS `amount`,`vf`.`amtpaid` AS `amtpaid`,`vf`.`txntype` AS `txntype`,`vf`.`sortorder` AS `sortorder`,'application' AS `ledgertype`,'' AS `remarks` from ((`vehicle_application_fee` `vf` join `vehicle_application` `va` on((`va`.`objid` = `vf`.`appid`))) join `vehicle_franchise` `f` on((`f`.`objid` = `va`.`controlid`))) union select `vf`.`objid` AS `objid`,`va`.`vehicletype` AS `vehicletype`,NULL AS `appno`,`va`.`controlno` AS `controlno`,NULL AS `appid`,`va`.`objid` AS `controlid`,`vf`.`item_objid` AS `item_objid`,`vf`.`item_code` AS `item_code`,`vf`.`item_title` AS `item_title`,`vf`.`amount` AS `amount`,`vf`.`amtpaid` AS `amtpaid`,`vf`.`txntype` AS `txntype`,`vf`.`sortorder` AS `sortorder`,'franchise' AS `ledgertype`,`vf`.`remarks` AS `remarks` from (`vehicle_franchise_fee` `vf` join `vehicle_franchise` `va` on((`va`.`objid` = `vf`.`parentid`))) */;

/*View structure for view vehicle_franchise_tricycle */

/*!50001 DROP TABLE IF EXISTS `vehicle_franchise_tricycle` */;
/*!50001 DROP VIEW IF EXISTS `vehicle_franchise_tricycle` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vehicle_franchise_tricycle` AS select `vf`.`objid` AS `objid`,`vf`.`state` AS `state`,`vf`.`clusterid` AS `clusterid`,`vf`.`controlno` AS `controlno`,`vf`.`vehicletype` AS `vehicletype`,`vf`.`expirydate` AS `expirydate`,`vf`.`appid` AS `appid`,`vf`.`activeyear` AS `activeyear`,`vf`.`startdate` AS `startdate`,`vat`.`plateno` AS `plateno`,`vat`.`engineno` AS `engineno`,`vat`.`chassisno` AS `chassisno`,`vat`.`make` AS `make`,`vat`.`model` AS `model`,`vat`.`color` AS `color`,`vat`.`sidecarno` AS `sidecarno`,`vat`.`bodyno` AS `bodyno`,`vat`.`sidecarcolor` AS `sidecarcolor`,`vat`.`crname` AS `crname`,`va`.`owner_objid` AS `owner_objid`,`va`.`owner_name` AS `owner_name`,`va`.`owner_address_objid` AS `owner_address_objid`,`va`.`owner_address_text` AS `owner_address_text`,`va`.`barangay_objid` AS `barangay_objid`,`va`.`barangay_name` AS `barangay_name`,`va`.`billexpirydate` AS `billexpirydate` from ((`vehicle_franchise` `vf` left join `vehicle_application_tricycle` `vat` on((`vf`.`appid` = `vat`.`objid`))) left join `vehicle_application` `va` on((`vat`.`objid` = `va`.`objid`))) */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
