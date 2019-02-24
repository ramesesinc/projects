-- MySQL dump 10.13  Distrib 5.5.39, for Win64 (x86)
--
-- Host: localhost    Database: ovs
-- ------------------------------------------------------
-- Server version	5.5.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `ovs`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `ovs` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `ovs`;

--
-- Table structure for table `ovs_payment`
--

DROP TABLE IF EXISTS `ovs_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ovs_payment` (
  `objid` varchar(50) NOT NULL,
  `reftype` varchar(50) DEFAULT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `refno` varchar(50) DEFAULT NULL,
  `refdate` date DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `voided` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_refid` (`refid`),
  KEY `ix_refno` (`refno`),
  KEY `ix_refdate` (`refdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ovs_payment_item`
--

DROP TABLE IF EXISTS `ovs_payment_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ovs_payment_item` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) NOT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_parentid` (`parentid`),
  KEY `ix_refid` (`refid`),
  CONSTRAINT `fk_ovs_payment_item_parentid` FOREIGN KEY (`parentid`) REFERENCES `ovs_payment` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ovs_vehicle`
--

DROP TABLE IF EXISTS `ovs_vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ovs_violation`
--

DROP TABLE IF EXISTS `ovs_violation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ovs_violation_ticket`
--

DROP TABLE IF EXISTS `ovs_violation_ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ovs_violation_ticket` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `ticketno` varchar(50) DEFAULT NULL,
  `violator_objid` varchar(50) DEFAULT NULL,
  `violator_name` varchar(100) DEFAULT NULL,
  `violator_address_text` varchar(255) DEFAULT NULL,
  `apprehensionofficer_objid` varchar(50) DEFAULT NULL,
  `apprehensionofficer_name` varchar(100) DEFAULT NULL,
  `apprehensionaddress` text,
  `apprehensiondate` datetime DEFAULT NULL,
  `forvehicle` int(11) DEFAULT NULL,
  `vehicleid` varchar(50) DEFAULT NULL,
  `licenseno` varchar(50) DEFAULT NULL,
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ovs_violation_ticket_entry`
--

DROP TABLE IF EXISTS `ovs_violation_ticket_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-03-06 16:15:43
