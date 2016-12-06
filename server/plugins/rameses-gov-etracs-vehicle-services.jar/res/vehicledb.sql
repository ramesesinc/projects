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
  `appno` varchar(50) DEFAULT NULL,
  `appdate` date DEFAULT NULL,
  `apptype` varchar(50) DEFAULT NULL,
  `appyear` int(11) DEFAULT NULL,
  `vehicletype` varchar(50) DEFAULT NULL,
  `vehicleid` varchar(50) DEFAULT NULL,
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
  PRIMARY KEY (`objid`)
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
  `boolvalue` int(11) DEFAULT NULL,
  `lov_objid` varchar(50) DEFAULT NULL,
  `lookup_objid` varchar(50) DEFAULT NULL,
  `lookup_title` varchar(50) DEFAULT NULL,
  `objvalue` mediumtext,
  `sortorder` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_application_tricycle_task` */

DROP TABLE IF EXISTS `vehicle_application_tricycle_task`;

CREATE TABLE `vehicle_application_tricycle_task` (
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
  KEY `FK_business_application_task_business_application` (`refid`),
  CONSTRAINT `fx_vehicle_application_task` FOREIGN KEY (`refid`) REFERENCES `vehicle_application` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_boat` */

DROP TABLE IF EXISTS `vehicle_boat`;

CREATE TABLE `vehicle_boat` (
  `objid` varchar(50) NOT NULL,
  `vesselname` varchar(255) DEFAULT NULL,
  `homeport_id` varchar(50) DEFAULT NULL,
  `length` decimal(10,2) DEFAULT NULL,
  `breadth` decimal(10,2) DEFAULT NULL,
  `depth` decimal(10,2) DEFAULT NULL,
  `grosstonnage` decimal(10,2) DEFAULT NULL,
  `nettonnage` decimal(10,2) DEFAULT NULL,
  `buildername` varchar(255) DEFAULT NULL,
  `builderplace` varchar(255) DEFAULT NULL,
  `buildyear` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `vehicle_boat_engine` */

DROP TABLE IF EXISTS `vehicle_boat_engine`;

CREATE TABLE `vehicle_boat_engine` (
  `objid` varchar(50) NOT NULL,
  `acctid` varchar(50) DEFAULT NULL,
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
  `controlno` varchar(50) DEFAULT NULL,
  `vehicletype` varchar(50) DEFAULT NULL,
  `expirydate` date DEFAULT NULL,
  `appid` varchar(50) DEFAULT NULL,
  `dtregistered` date DEFAULT NULL,
  PRIMARY KEY (`objid`)
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
  PRIMARY KEY (`objid`),
  KEY `fk_applicationfee_application` (`parentid`),
  CONSTRAINT `fk_franchise_fee` FOREIGN KEY (`parentid`) REFERENCES `vehicle_franchise` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_payment` */

DROP TABLE IF EXISTS `vehicle_payment`;

CREATE TABLE `vehicle_payment` (
  `objid` varchar(50) NOT NULL,
  `appid` varchar(50) DEFAULT NULL,
  `vehicleid` varchar(50) DEFAULT NULL,
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
  KEY `fk_vehicle_payment` (`vehicleid`),
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

/*Table structure for table `vehicle_tricycle` */

DROP TABLE IF EXISTS `vehicle_tricycle`;

CREATE TABLE `vehicle_tricycle` (
  `objid` varchar(50) NOT NULL,
  `plateno` varchar(50) DEFAULT NULL,
  `engineno` varchar(50) DEFAULT NULL,
  `bodyno` varchar(50) DEFAULT NULL,
  `sidecarno` varchar(50) DEFAULT NULL,
  `make` varchar(50) DEFAULT NULL,
  `model` varchar(50) DEFAULT NULL,
  `color` varchar(50) DEFAULT NULL,
  `createdby_objid` varchar(50) DEFAULT NULL,
  `createdby_name` varchar(255) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `controlid` varchar(50) DEFAULT NULL,
  `appid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_variable` */

DROP TABLE IF EXISTS `vehicle_variable`;

CREATE TABLE `vehicle_variable` (
  `name` varchar(50) NOT NULL,
  `caption` varchar(100) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  `system` int(11) DEFAULT '0',
  `arrayvalues` text,
  PRIMARY KEY (`name`)
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
  `issued` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_application_tricycle` */

DROP TABLE IF EXISTS `vehicle_application_tricycle`;

/*!50001 DROP VIEW IF EXISTS `vehicle_application_tricycle` */;
/*!50001 DROP TABLE IF EXISTS `vehicle_application_tricycle` */;

/*!50001 CREATE TABLE  `vehicle_application_tricycle`(
 `objid` varchar(50) ,
 `appno` varchar(50) ,
 `appdate` date ,
 `apptype` varchar(50) ,
 `appyear` int(11) ,
 `vehicletype` varchar(50) ,
 `vehicleid` varchar(50) ,
 `controlid` varchar(50) ,
 `dtfiled` datetime ,
 `filedby_objid` varchar(50) ,
 `filedby_name` varchar(255) ,
 `owner_objid` varchar(50) ,
 `owner_name` varchar(255) ,
 `owner_address_objid` varchar(50) ,
 `owner_address_text` varchar(255) ,
 `particulars` varchar(150) ,
 `barangay_objid` varchar(50) ,
 `barangay_name` varchar(155) ,
 `billexpirydate` date ,
 `vehicle_plateno` varchar(50) ,
 `vehicle_engineno` varchar(50) ,
 `vehicle_bodyno` varchar(50) ,
 `vehicle_sidecarno` varchar(50) ,
 `vehicle_make` varchar(50) ,
 `vehicle_model` varchar(50) ,
 `vehicle_color` varchar(50) ,
 `taskid` varchar(50) ,
 `state` varchar(50) ,
 `franchise_controlno` varchar(50) 
)*/;

/*Table structure for table `vehicle_fee_tricycle` */

DROP TABLE IF EXISTS `vehicle_fee_tricycle`;

/*!50001 DROP VIEW IF EXISTS `vehicle_fee_tricycle` */;
/*!50001 DROP TABLE IF EXISTS `vehicle_fee_tricycle` */;

/*!50001 CREATE TABLE  `vehicle_fee_tricycle`(
 `objid` varchar(50) ,
 `appid` varchar(50) ,
 `controlid` varchar(50) ,
 `item_objid` varchar(50) ,
 `item_code` varchar(50) ,
 `item_title` varchar(255) ,
 `amount` decimal(16,4) ,
 `amtpaid` decimal(16,4) ,
 `txntype` varchar(50) ,
 `sortorder` int(11) ,
 `ledgertype` varchar(11) 
)*/;

/*Table structure for table `vehicle_franchise_tricycle` */

DROP TABLE IF EXISTS `vehicle_franchise_tricycle`;

/*!50001 DROP VIEW IF EXISTS `vehicle_franchise_tricycle` */;
/*!50001 DROP TABLE IF EXISTS `vehicle_franchise_tricycle` */;

/*!50001 CREATE TABLE  `vehicle_franchise_tricycle`(
 `objid` varchar(50) ,
 `state` varchar(50) ,
 `controlno` varchar(50) ,
 `vehicletype` varchar(50) ,
 `expirydate` date ,
 `appid` varchar(50) ,
 `dtregistered` date ,
 `activeyear` int(11) ,
 `owner_objid` varchar(50) ,
 `owner_name` varchar(255) ,
 `owner_address_objid` varchar(50) ,
 `owner_address_text` varchar(255) ,
 `barangay_objid` varchar(50) ,
 `barangay_name` varchar(155) ,
 `vehicleid` varchar(50) ,
 `vehicle_objid` varchar(50) ,
 `vehicle_plateno` varchar(50) ,
 `vehicle_engineno` varchar(50) ,
 `vehicle_bodyno` varchar(50) ,
 `vehicle_sidecarno` varchar(50) ,
 `vehicle_make` varchar(50) ,
 `vehicle_model` varchar(50) ,
 `vehicle_color` varchar(50) 
)*/;

/*View structure for view vehicle_application_tricycle */

/*!50001 DROP TABLE IF EXISTS `vehicle_application_tricycle` */;
/*!50001 DROP VIEW IF EXISTS `vehicle_application_tricycle` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vehicle_application_tricycle` AS select `va`.`objid` AS `objid`,`va`.`appno` AS `appno`,`va`.`appdate` AS `appdate`,`va`.`apptype` AS `apptype`,`va`.`appyear` AS `appyear`,`va`.`vehicletype` AS `vehicletype`,`va`.`vehicleid` AS `vehicleid`,`va`.`controlid` AS `controlid`,`va`.`dtfiled` AS `dtfiled`,`va`.`filedby_objid` AS `filedby_objid`,`va`.`filedby_name` AS `filedby_name`,`va`.`owner_objid` AS `owner_objid`,`va`.`owner_name` AS `owner_name`,`va`.`owner_address_objid` AS `owner_address_objid`,`va`.`owner_address_text` AS `owner_address_text`,`va`.`particulars` AS `particulars`,`va`.`barangay_objid` AS `barangay_objid`,`va`.`barangay_name` AS `barangay_name`,`va`.`billexpirydate` AS `billexpirydate`,`t`.`plateno` AS `vehicle_plateno`,`t`.`engineno` AS `vehicle_engineno`,`t`.`bodyno` AS `vehicle_bodyno`,`t`.`sidecarno` AS `vehicle_sidecarno`,`t`.`make` AS `vehicle_make`,`t`.`model` AS `vehicle_model`,`t`.`color` AS `vehicle_color`,`vt`.`taskid` AS `taskid`,`vt`.`state` AS `state`,`vf`.`controlno` AS `franchise_controlno` from (((`vehicle_application` `va` join `vehicle_tricycle` `t` on((`va`.`vehicleid` = `t`.`objid`))) join `vehicle_application_tricycle_task` `vt` on((`vt`.`refid` = `va`.`objid`))) left join `vehicle_franchise` `vf` on((`va`.`controlid` = `vf`.`objid`))) where (isnull(`vt`.`enddate`) and (`vt`.`state` <> 'start')) */;

/*View structure for view vehicle_fee_tricycle */

/*!50001 DROP TABLE IF EXISTS `vehicle_fee_tricycle` */;
/*!50001 DROP VIEW IF EXISTS `vehicle_fee_tricycle` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vehicle_fee_tricycle` AS select `vf`.`objid` AS `objid`,`vf`.`appid` AS `appid`,`va`.`controlid` AS `controlid`,`vf`.`item_objid` AS `item_objid`,`vf`.`item_code` AS `item_code`,`vf`.`item_title` AS `item_title`,`vf`.`amount` AS `amount`,`vf`.`amtpaid` AS `amtpaid`,`vf`.`txntype` AS `txntype`,`vf`.`sortorder` AS `sortorder`,'application' AS `ledgertype` from (`vehicle_application_fee` `vf` join `vehicle_application` `va` on(((`va`.`objid` = `vf`.`appid`) and (`va`.`vehicletype` = 'tricycle')))) union select `vf`.`objid` AS `objid`,NULL AS `appid`,`va`.`objid` AS `controlid`,`vf`.`item_objid` AS `item_objid`,`vf`.`item_code` AS `item_code`,`vf`.`item_title` AS `item_title`,`vf`.`amount` AS `amount`,`vf`.`amtpaid` AS `amtpaid`,`vf`.`txntype` AS `txntype`,`vf`.`sortorder` AS `sortorder`,'franchise' AS `ledgertype` from (`vehicle_franchise_fee` `vf` join `vehicle_franchise` `va` on(((`va`.`objid` = `vf`.`parentid`) and (`va`.`vehicletype` = 'tricycle')))) */;

/*View structure for view vehicle_franchise_tricycle */

/*!50001 DROP TABLE IF EXISTS `vehicle_franchise_tricycle` */;
/*!50001 DROP VIEW IF EXISTS `vehicle_franchise_tricycle` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vehicle_franchise_tricycle` AS select `vf`.`objid` AS `objid`,`vf`.`state` AS `state`,`vf`.`controlno` AS `controlno`,`vf`.`vehicletype` AS `vehicletype`,`vf`.`expirydate` AS `expirydate`,`vf`.`appid` AS `appid`,`vf`.`dtregistered` AS `dtregistered`,`a`.`appyear` AS `activeyear`,`a`.`owner_objid` AS `owner_objid`,`a`.`owner_name` AS `owner_name`,`a`.`owner_address_objid` AS `owner_address_objid`,`a`.`owner_address_text` AS `owner_address_text`,`a`.`barangay_objid` AS `barangay_objid`,`a`.`barangay_name` AS `barangay_name`,`t`.`objid` AS `vehicleid`,`t`.`objid` AS `vehicle_objid`,`t`.`plateno` AS `vehicle_plateno`,`t`.`engineno` AS `vehicle_engineno`,`t`.`bodyno` AS `vehicle_bodyno`,`t`.`sidecarno` AS `vehicle_sidecarno`,`t`.`make` AS `vehicle_make`,`t`.`model` AS `vehicle_model`,`t`.`color` AS `vehicle_color` from ((`vehicle_franchise` `vf` left join `vehicle_application` `a` on((`vf`.`appid` = `a`.`objid`))) left join `vehicle_tricycle` `t` on((`a`.`vehicleid` = `t`.`objid`))) where (`vf`.`vehicletype` = 'tricycle') */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
