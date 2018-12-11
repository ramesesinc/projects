# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.6.40)
# Database: ovsdb
# Generation Time: 2018-12-11 03:04:04 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table ovs_payment
# ------------------------------------------------------------

CREATE TABLE `ovs_payment` (
  `objid` varchar(50) NOT NULL,
  `reftype` varchar(50) DEFAULT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `refno` varchar(50) DEFAULT NULL,
  `refdate` date DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `voided` int(11) DEFAULT NULL,
  `entityid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_refid` (`refid`),
  KEY `ix_refno` (`refno`),
  KEY `ix_refdate` (`refdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ovs_payment_item
# ------------------------------------------------------------

CREATE TABLE `ovs_payment_item` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) NOT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `reftype` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_parentid` (`parentid`),
  KEY `ix_refid` (`refid`),
  CONSTRAINT `fk_ovs_payment_item_parentid` FOREIGN KEY (`parentid`) REFERENCES `ovs_payment` (`objid`),
  CONSTRAINT `fk_ovs_payment_item_ticketentry` FOREIGN KEY (`refid`) REFERENCES `ovs_violation_ticket_entry` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ovs_vehicle
# ------------------------------------------------------------

CREATE TABLE `ovs_vehicle` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `plateno` varchar(50) DEFAULT NULL,
  `orcr` varchar(50) DEFAULT NULL,
  `owner_objid` varchar(50) DEFAULT NULL,
  `owner_name` varchar(100) DEFAULT NULL,
  `info` text,
  PRIMARY KEY (`objid`),
  KEY `ix_name` (`name`),
  KEY `ix_plateno` (`plateno`),
  KEY `ix_owner_objid` (`owner_objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ovs_violation
# ------------------------------------------------------------

CREATE TABLE `ovs_violation` (
  `objid` varchar(50) NOT NULL,
  `title` varchar(50) DEFAULT NULL,
  `section` varchar(50) DEFAULT NULL,
  `article` varchar(50) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`objid`),
  KEY `ix_title` (`title`),
  KEY `ix_section` (`section`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ovs_violation_ticket
# ------------------------------------------------------------

CREATE TABLE `ovs_violation_ticket` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `ticketno` varchar(50) DEFAULT NULL,
  `violator_objid` varchar(50) DEFAULT NULL,
  `violator_name` varchar(100) DEFAULT NULL,
  `apprehensionofficer_objid` varchar(50) DEFAULT NULL,
  `apprehensionofficer_name` varchar(100) DEFAULT NULL,
  `apprehensionaddress` text,
  `apprehensiondate` datetime DEFAULT NULL,
  `forvehicle` int(11) DEFAULT NULL,
  `vehicleid` varchar(50) DEFAULT NULL,
  `licenseno` varchar(50) DEFAULT NULL,
  `violator_address_text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_ticketno` (`ticketno`),
  KEY `ix_ticketno` (`ticketno`),
  KEY `ix_voilator_objid` (`violator_objid`),
  KEY `ix_voilator_name` (`violator_name`),
  KEY `ix_apprehensionofficer_objid` (`apprehensionofficer_objid`),
  KEY `ix_apprehensionofficer_name` (`apprehensionofficer_name`),
  KEY `ix_vehicleid` (`vehicleid`),
  KEY `ix_licenseno` (`licenseno`),
  KEY `ix_apprehensiondate` (`apprehensiondate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ovs_violation_ticket_entry
# ------------------------------------------------------------

CREATE TABLE `ovs_violation_ticket_entry` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `violationid` varchar(50) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `amtpaid` decimal(10,2) DEFAULT NULL,
  `violationcount` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_parentid` (`parentid`),
  KEY `ix_violationid` (`violationid`),
  CONSTRAINT `fk_ovs_violation_ticket_entry_parentid` FOREIGN KEY (`parentid`) REFERENCES `ovs_violation_ticket` (`objid`),
  CONSTRAINT `fk_ovs_violation_ticket_entry_violationid` FOREIGN KEY (`violationid`) REFERENCES `ovs_violation` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
