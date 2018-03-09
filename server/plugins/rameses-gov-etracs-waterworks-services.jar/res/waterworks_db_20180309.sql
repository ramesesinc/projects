-- MySQL dump 10.13  Distrib 5.1.73, for Win64 (unknown)
--
-- Host: localhost    Database: icws_waterworks
-- ------------------------------------------------------
-- Server version	5.1.73-community

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
-- Table structure for table `waterworks_account`
--

DROP TABLE IF EXISTS `waterworks_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `lastdateread` date DEFAULT NULL,
  `currentreading` int(11) DEFAULT NULL,
  `lastdatepaid` datetime DEFAULT NULL,
  `billingcycleid` varchar(50) DEFAULT NULL,
  `zoneid` varchar(50) DEFAULT NULL,
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
  KEY `ix_zoneid` (`zoneid`),
  KEY `ix_stuboutnodeid` (`stuboutnodeid`),
  KEY `ix_lastbilldate` (`lastbilldate`),
  CONSTRAINT `waterworks_account_ibfk_1` FOREIGN KEY (`applicationid`) REFERENCES `waterworks_application` (`objid`),
  CONSTRAINT `waterworks_account_ibfk_2` FOREIGN KEY (`billingcycleid`) REFERENCES `waterworks_billing_cycle` (`objid`),
  CONSTRAINT `waterworks_account_ibfk_3` FOREIGN KEY (`classificationid`) REFERENCES `waterworks_classification` (`objid`),
  CONSTRAINT `waterworks_account_ibfk_4` FOREIGN KEY (`meterid`) REFERENCES `waterworks_meter` (`objid`),
  CONSTRAINT `waterworks_account_ibfk_8` FOREIGN KEY (`stuboutnodeid`) REFERENCES `waterworks_stubout_node` (`objid`),
  CONSTRAINT `waterworks_account_ibfk_9` FOREIGN KEY (`zoneid`) REFERENCES `waterworks_zone` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_account_attribute`
--

DROP TABLE IF EXISTS `waterworks_account_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `waterworks_account_attribute` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `attribute_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_account_ledger`
--

DROP TABLE IF EXISTS `waterworks_account_ledger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `waterworks_account_ledger` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `txntype` varchar(10) DEFAULT NULL,
  `item_objid` varchar(50) DEFAULT NULL,
  `item_code` varchar(50) DEFAULT NULL,
  `item_title` varchar(255) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `amount` decimal(16,2) DEFAULT NULL,
  `amtpaid` decimal(16,2) DEFAULT NULL,
  `installmentid` varchar(50) DEFAULT NULL,
  `billingcycleid` varchar(50) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `surcharge` decimal(16,2) DEFAULT NULL,
  `interest` decimal(16,2) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_parent_item_billingcycle` (`parentid`,`item_objid`,`billingcycleid`),
  UNIQUE KEY `uix_parentid_year_month_itemid` (`parentid`,`item_objid`,`month`,`year`),
  KEY `ix_parentid` (`parentid`),
  KEY `ix_item_objid` (`item_objid`),
  KEY `ix_billingcycleid` (`billingcycleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_application`
--

DROP TABLE IF EXISTS `waterworks_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_application_bom`
--

DROP TABLE IF EXISTS `waterworks_application_bom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_application_fee`
--

DROP TABLE IF EXISTS `waterworks_application_fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_application_requirement`
--

DROP TABLE IF EXISTS `waterworks_application_requirement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_application_task`
--

DROP TABLE IF EXISTS `waterworks_application_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  KEY `FK_business_application_task_business_application` (`refid`),
  CONSTRAINT `waterworks_application_task_ibfk_1` FOREIGN KEY (`refid`) REFERENCES `waterworks_application` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_attribute`
--

DROP TABLE IF EXISTS `waterworks_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `waterworks_attribute` (
  `name` varchar(50) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_batchreading`
--

DROP TABLE IF EXISTS `waterworks_batchreading`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_billing`
--

DROP TABLE IF EXISTS `waterworks_billing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `othercharge` decimal(16,2) DEFAULT NULL,
  `advance` decimal(16,2) DEFAULT NULL,
  `unpaidamt` decimal(16,4) DEFAULT NULL,
  `unpaidmonths` int(11) DEFAULT NULL,
  `volume` int(11) DEFAULT NULL,
  `amount` decimal(16,2) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `readingmethod` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_billing_account_parent_acctid` (`batchid`,`acctid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_billing_batch`
--

DROP TABLE IF EXISTS `waterworks_billing_batch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `waterworks_billing_batch` (
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
  `discountdate` date DEFAULT NULL,
  `reader_objid` varchar(50) DEFAULT NULL,
  `reader_name` varchar(255) DEFAULT NULL,
  `readingdate` date DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_billing_cycle`
--

DROP TABLE IF EXISTS `waterworks_billing_cycle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_classification`
--

DROP TABLE IF EXISTS `waterworks_classification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `waterworks_classification` (
  `objid` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_consumption`
--

DROP TABLE IF EXISTS `waterworks_consumption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `waterworks_consumption` (
  `objid` varchar(50) NOT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  `batchid` varchar(50) DEFAULT NULL,
  `prevreading` int(11) DEFAULT NULL,
  `reading` int(11) DEFAULT NULL,
  `readingmethod` varchar(50) DEFAULT NULL,
  `reader_objid` varchar(50) DEFAULT NULL,
  `reader_name` varchar(255) DEFAULT NULL,
  `volume` int(11) DEFAULT NULL,
  `rate` decimal(16,2) DEFAULT NULL,
  `ledgerid` varchar(50) DEFAULT NULL,
  `billingcycleid` varchar(50) DEFAULT NULL,
  `amount` decimal(16,2) DEFAULT NULL,
  `amtpaid` decimal(16,2) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `readingdate` date DEFAULT NULL,
  `state` varchar(32) NOT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_waterworks_consumption_year_month` (`acctid`,`month`,`year`),
  KEY `fks_waterworks_account_reading` (`acctid`),
  KEY `ix_ledgerid` (`ledgerid`),
  KEY `ix_year` (`year`),
  KEY `ix_month` (`month`),
  CONSTRAINT `waterworks_consumption_ibfk_1` FOREIGN KEY (`acctid`) REFERENCES `waterworks_account` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_credit`
--

DROP TABLE IF EXISTS `waterworks_credit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_formula`
--

DROP TABLE IF EXISTS `waterworks_formula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `waterworks_formula` (
  `classificationid` varchar(100) NOT NULL,
  `varname` varchar(50) DEFAULT NULL,
  `expr` longtext,
  PRIMARY KEY (`classificationid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_installment`
--

DROP TABLE IF EXISTS `waterworks_installment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_material`
--

DROP TABLE IF EXISTS `waterworks_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_meter`
--

DROP TABLE IF EXISTS `waterworks_meter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `waterworks_meter` (
  `objid` varchar(50) NOT NULL,
  `serialno` varchar(50) DEFAULT NULL,
  `brand` varchar(50) DEFAULT NULL,
  `sizeid` varchar(50) DEFAULT NULL,
  `capacity` varchar(50) DEFAULT NULL,
  `currentacctid` varchar(50) DEFAULT NULL,
  `stocktype` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_serialno` (`serialno`),
  KEY `ix_brand` (`brand`),
  KEY `fk_sizeid` (`sizeid`),
  KEY `ix_currentacctid` (`currentacctid`),
  CONSTRAINT `waterworks_meter_ibfk_1` FOREIGN KEY (`currentacctid`) REFERENCES `waterworks_account` (`objid`),
  CONSTRAINT `waterworks_meter_ibfk_2` FOREIGN KEY (`sizeid`) REFERENCES `waterworks_metersize` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_metersize`
--

DROP TABLE IF EXISTS `waterworks_metersize`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `waterworks_metersize` (
  `objid` varchar(50) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_mobile_header`
--

DROP TABLE IF EXISTS `waterworks_mobile_header`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `waterworks_mobile_header` (
  `batchid` varchar(50) NOT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`batchid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_mobile_info`
--

DROP TABLE IF EXISTS `waterworks_mobile_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_payment`
--

DROP TABLE IF EXISTS `waterworks_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_payment_item`
--

DROP TABLE IF EXISTS `waterworks_payment_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_sector`
--

DROP TABLE IF EXISTS `waterworks_sector`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `waterworks_sector` (
  `objid` varchar(50) NOT NULL,
  `code` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_stubout`
--

DROP TABLE IF EXISTS `waterworks_stubout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_stubout_node`
--

DROP TABLE IF EXISTS `waterworks_stubout_node`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `waterworks_stubout_node` (
  `objid` varchar(50) NOT NULL,
  `stuboutid` varchar(50) DEFAULT NULL,
  `indexno` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_stuboutid` (`stuboutid`),
  CONSTRAINT `waterworks_stubout_node_ibfk_2` FOREIGN KEY (`stuboutid`) REFERENCES `waterworks_stubout` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_txntype`
--

DROP TABLE IF EXISTS `waterworks_txntype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waterworks_zone`
--

DROP TABLE IF EXISTS `waterworks_zone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `waterworks_zone` (
  `objid` varchar(50) NOT NULL,
  `sectorid` varchar(50) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_waterworks_zone_sector` (`sectorid`,`code`),
  CONSTRAINT `waterworks_zone_ibfk_2` FOREIGN KEY (`sectorid`) REFERENCES `waterworks_sector` (`objid`)
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

-- Dump completed on 2018-03-09 15:17:51
