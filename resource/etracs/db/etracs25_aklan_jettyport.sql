-- MySQL dump 10.13  Distrib 5.5.39, for Win64 (x86)
--
-- Host: localhost    Database: etracs25_aklan_jettyport
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
-- Current Database: `etracs25_aklan_jettyport`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `etracs25_aklan_jettyport` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `etracs25_aklan_jettyport`;

--
-- Table structure for table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ticket` (
  `objid` varchar(50) NOT NULL,
  `barcode` varchar(20) DEFAULT NULL,
  `dtfiled` datetime DEFAULT NULL,
  `dtused` datetime DEFAULT NULL,
  `guesttype` varchar(1) DEFAULT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `reftype` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `idx_barcode` (`barcode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket`
--

LOCK TABLES `ticket` WRITE;
/*!40000 ALTER TABLE `ticket` DISABLE KEYS */;
INSERT INTO `ticket` VALUES ('TKT-3217c0:1492750ff0b:-7fea','-1587847843','2014-10-19 15:32:00',NULL,'C','RCT-3217c0:1492750ff0b:-7ff3','cashreceipt'),('TKT-3217c0:1492750ff0b:-7feb','-1857204131','2014-10-19 15:32:00',NULL,'A','RCT-3217c0:1492750ff0b:-7ff3','cashreceipt'),('TKT-3217c0:1492750ff0b:-7fec','-1964617901','2014-10-19 15:32:00',NULL,'A','RCT-3217c0:1492750ff0b:-7ff3','cashreceipt'),('TKT-3217c0:1492750ff0b:-7fed','-2125374031','2014-10-19 15:32:00',NULL,'A','RCT-3217c0:1492750ff0b:-7ff3','cashreceipt');
/*!40000 ALTER TABLE `ticket` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-10-19 17:05:55
