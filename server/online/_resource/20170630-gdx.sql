-- MySQL dump 10.10
--
-- Host: localhost    Database: gdx
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
-- Current Database: `gdx`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `gdx` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `gdx`;

--
-- Table structure for table `eor`
--

DROP TABLE IF EXISTS `eor`;
CREATE TABLE `eor` (
  `objid` varchar(50) NOT NULL,
  `dtposted` datetime NOT NULL,
  `txnpoid` varchar(50) DEFAULT NULL,
  `txnrefid` varchar(50) NOT NULL,
  `txnreftype` varchar(50) NOT NULL,
  `txntype` varchar(50) NOT NULL,
  `refno` varchar(50) NOT NULL,
  `refdate` datetime NOT NULL,
  `amount` decimal(16,2) NOT NULL,
  `particulars` varchar(500) DEFAULT NULL,
  `partner_objid` varchar(50) NOT NULL,
  `locationid` varchar(20) NOT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `ux_refno` (`refno`),
  UNIQUE KEY `ux_txnrefid` (`txnrefid`),
  KEY `ix_txnreftype` (`txnreftype`),
  KEY `ix_txntype` (`txntype`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `eor`
--

LOCK TABLES `eor` WRITE;
/*!40000 ALTER TABLE `eor` DISABLE KEYS */;
/*!40000 ALTER TABLE `eor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eor_forrecovery`
--

DROP TABLE IF EXISTS `eor_forrecovery`;
CREATE TABLE `eor_forrecovery` (
  `objid` varchar(50) NOT NULL,
  `dtposted` datetime NOT NULL,
  `paymentinfo` longtext NOT NULL,
  `eor` longtext NOT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `eor_forrecovery`
--

LOCK TABLES `eor_forrecovery` WRITE;
/*!40000 ALTER TABLE `eor_forrecovery` DISABLE KEYS */;
/*!40000 ALTER TABLE `eor_forrecovery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eor_forresolve`
--

DROP TABLE IF EXISTS `eor_forresolve`;
CREATE TABLE `eor_forresolve` (
  `objid` varchar(50) NOT NULL,
  `dtposted` datetime NOT NULL,
  `paymentinfo` longtext NOT NULL,
  `eor` longtext NOT NULL,
  `msg` varchar(255) NOT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `eor_forresolve`
--

LOCK TABLES `eor_forresolve` WRITE;
/*!40000 ALTER TABLE `eor_forresolve` DISABLE KEYS */;
/*!40000 ALTER TABLE `eor_forresolve` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eor_forsync`
--

DROP TABLE IF EXISTS `eor_forsync`;
CREATE TABLE `eor_forsync` (
  `objid` varchar(50) NOT NULL,
  `syncdate` datetime NOT NULL,
  `synccount` int(255) NOT NULL,
  `msg` text,
  PRIMARY KEY (`objid`),
  CONSTRAINT `FK_eor_forsync_eor` FOREIGN KEY (`objid`) REFERENCES `eor` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `eor_forsync`
--

LOCK TABLES `eor_forsync` WRITE;
/*!40000 ALTER TABLE `eor_forsync` DISABLE KEYS */;
/*!40000 ALTER TABLE `eor_forsync` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eor_paymentorder`
--

DROP TABLE IF EXISTS `eor_paymentorder`;
CREATE TABLE `eor_paymentorder` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(10) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `dtposted` datetime DEFAULT NULL,
  `traceno` varchar(25) DEFAULT NULL,
  `tracedate` datetime DEFAULT NULL,
  `billinfo` varchar(255) DEFAULT NULL,
  `paymentorder_txnid` varchar(50) DEFAULT NULL,
  `paymentorder_txndate` datetime DEFAULT NULL,
  `paymentorder_controlno` varchar(50) DEFAULT NULL,
  `paymentorder_payer_objid` varchar(50) DEFAULT NULL,
  `paymentorder_payer_name` varchar(255) DEFAULT NULL,
  `paymentorder_paidby` varchar(255) DEFAULT NULL,
  `paymentorder_paidbyaddress` varchar(255) DEFAULT NULL,
  `paymentorder_particulars` varchar(255) DEFAULT NULL,
  `paymentorder_amount` decimal(16,4) DEFAULT NULL,
  `paymentorder_expirydate` datetime DEFAULT NULL,
  `paymentorder_refno` varchar(50) DEFAULT NULL,
  `paymentorder_info` mediumtext,
  `paymentorder_txntypeid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `eor_paymentorder`
--

LOCK TABLES `eor_paymentorder` WRITE;
/*!40000 ALTER TABLE `eor_paymentorder` DISABLE KEYS */;
/*!40000 ALTER TABLE `eor_paymentorder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partner`
--

DROP TABLE IF EXISTS `partner`;
CREATE TABLE `partner` (
  `id` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `clusterid` varchar(50) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `subtype` varchar(50) DEFAULT NULL,
  `expirydate` date DEFAULT NULL,
  `state` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uix_group_name` (`name`,`clusterid`),
  KEY `fk_member_cluster` (`clusterid`),
  CONSTRAINT `fk_partner_cluster` FOREIGN KEY (`clusterid`) REFERENCES `partner_cluster` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `partner`
--

LOCK TABLES `partner` WRITE;
/*!40000 ALTER TABLE `partner` DISABLE KEYS */;
INSERT INTO `partner` VALUES ('003','ilocosnorte','Ilocos Norte','ilocosnorte','lgu','province',NULL,'ACTIVE'),('00312','marcos','Marcos','ilocosnorte','lgu','municipality',NULL,'ACTIVE'),('00314','pagudpud','Pagudpud','ilocosnorte','lgu','municipality',NULL,'ACTIVE'),('00317','piddig','Piddig','ilocosnorte','lgu','municipality',NULL,'ACTIVE'),('00319','sannicolas','San Nicolas','ilocosnorte','lgu','municipality',NULL,'ACTIVE'),('00320','sarrat','Sarrat','ilocosnorte','lgu','municipality',NULL,'ACTIVE'),('00321','solsona','Solsona','ilocosnorte','lgu','municipality',NULL,'ACTIVE'),('01205','bayombong','Bayombong','nuevavizcaya','lgu','municipality',NULL,'ACTIVE'),('01214','solano','Solano','nuevavizcaya','lgu','municipality',NULL,'ACTIVE'),('01536','sariaya','Sariaya','quezon','lgu','municipality',NULL,'ACTIVE'),('038','aklan','Aklan Province','aklan','lgu','province',NULL,'ACTIVE'),('03801','kalibo','Kalibo','aklan','lgu','municipality',NULL,'ACTIVE'),('03802','altavas','Altavas','aklan','lgu','municipality',NULL,'ACTIVE'),('04321','dalaguete','Dalaguete','cebu','lgu','municipality',NULL,'ACTIVE'),('057','lanaonorte','Lanao del Norte Province','lanaodelnorte','lgu','province',NULL,'ACTIVE'),('05701','bacolod','Bacolod','lanaodelnorte','lgu','municipality',NULL,'ACTIVE'),('05704','kapatagan','Kapatagan','lanaodelnorte','lgu','municipality',NULL,'ACTIVE'),('05707','kolambugan','Kolambugan','lanaodelnorte','lgu','municipality',NULL,'ACTIVE'),('05708','lala','Lala','lanaodelnorte','lgu','municipality',NULL,'ACTIVE'),('05717','salvador','Salvador','lanaodelnorte','lgu','municipality',NULL,'ACTIVE'),('05722','tubod','Tubod','lanaodelnorte','lgu','municipality',NULL,'ACTIVE'),('057XX','baroy','Baroy','lanaodelnorte','lgu','municipality',NULL,'ACTIVE'),('059','bukidnon','Bukidnon Province','bukidnon','lgu','province',NULL,'ACTIVE'),('05914','malitbog','Malitbog','bukidnon','lgu','municipality',NULL,'ACTIVE'),('063','davaodelnorte','Davao del Norte Province','ddn','lgu','province',NULL,'ACTIVE'),('06301','asuncion','Asuncion','ddn','lgu','municipality',NULL,'ACTIVE'),('06302','dujali','Braulio E. Dujali','ddn','lgu','municipality',NULL,'ACTIVE'),('06303','carmen','Carmen','ddn','lgu','municipality',NULL,'ACTIVE'),('06304','kapalong','Kapalong','ddn','lgu','municipality',NULL,'ACTIVE'),('06305','newcorella','New Corella','ddn','lgu','municipality',NULL,'ACTIVE'),('06306','sanisidro','San Isidro','ddn','lgu','municipality',NULL,'ACTIVE'),('06307','stotomas','Sto. Tomas','ddn','lgu','municipality',NULL,'ACTIVE'),('06308','talaingod','Talaingod','ddn','lgu','municipality',NULL,'ACTIVE'),('075','guimaras','Guimaras Province','guimaras','lgu','municipality',NULL,'ACTIVE'),('07501','buenavista','Buenavista','guimaras','lgu','municipality',NULL,'ACTIVE'),('07502','jordan','Jordan','guimaras','lgu','municipality',NULL,'ACTIVE'),('07503','nuevavalencia','Nueva Valencia','guimaras','lgu','municipality',NULL,'ACTIVE'),('07504','sanlorenzo','San Lorenzo','guimaras','lgu','municipality',NULL,'ACTIVE'),('07505','sibunag','Sibunag','guimaras','lgu','municipality',NULL,'ACTIVE'),('07909','nabunturan','Nabunturan','comval','lgu','municipality',NULL,'ACTIVE'),('134','puertoprincesa','Puerto Princesa City','palawan','lgu','city',NULL,'ACTIVE'),('135','naga','Naga City','camsur','lgu','city',NULL,'ACTIVE'),('136','iriga','Iriga City','camsur','lgu','city',NULL,'ACTIVE'),('137','legazpi','Legazpi City','albay','lgu','city',NULL,'INACTIVE'),('140','cadiz','Cadiz City','negocc','lgu','city',NULL,'ACTIVE'),('142','sancarlos','San Carlos City','negocc','lgu','city',NULL,'ACTIVE'),('154','tagbilaran','Tagbilaran City','bohol','lgu','city',NULL,'INACTIVE'),('169','iligan','Iligan City','lanaodelnorte','lgu','city',NULL,'ACTIVE'),('191','gapan','Gapan City','nuevaecija','lgu','city',NULL,'ACTIVE'),('195','panabo','Panabo City','ddn','lgu','city',NULL,'ACTIVE'),('218','bayawan','Bayawan City','negor','lgu','city',NULL,'ACTIVE'),('9999985','tabaco','Tabaco City','albay','lgu','city',NULL,'INACTIVE'),('9999986','ligao','Ligao City','albay','lgu','city',NULL,'INACTIVE'),('9999987','surigaonorte','Surigao del Norte Province','surigaodelnorte','lgu','province',NULL,'INACTIVE'),('9999988','oroquieta','Oroquieta City','misocc','lgu','city',NULL,'INACTIVE'),('9999989','misocc','Misamis Occidental','misocc','lgu','province',NULL,'INACTIVE'),('9999991','baao','Baao','camsur','lgu','municipality',NULL,'INACTIVE'),('9999994','sanandres','San Andres','catanduanes','lgu','municipality',NULL,'INACTIVE'),('9999999','masbate','Masbate City','masbate','lgu','city',NULL,'INACTIVE'),('999XX','sjdb','San Jose de Buenavista','antique','lgu','municipality',NULL,'ACTIVE');
/*!40000 ALTER TABLE `partner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partner_cluster`
--

DROP TABLE IF EXISTS `partner_cluster`;
CREATE TABLE `partner_cluster` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `partner_cluster`
--

LOCK TABLES `partner_cluster` WRITE;
/*!40000 ALTER TABLE `partner_cluster` DISABLE KEYS */;
INSERT INTO `partner_cluster` VALUES ('aklan','aklan','Aklan'),('albay','albay','Albay'),('antique','antique','Antique'),('bohol','bohol','Bohol'),('bukidnon','bukidnon','Bukidnon'),('camsur','camsur','Camarines Sur'),('catanduanes','catanduanes','Catanduanes'),('cebu','cebu','Cebu'),('comval','comval','Compostela Valley'),('ddn','ddn','Davao del Norte'),('guimaras','guimaras','Guimaras '),('ilocosnorte','ilocosnorte','Ilocos Norte'),('lanaodelnorte','lanaodelnorte','Lanao del Norte'),('masbate','masbate','Masbate'),('misocc','misocc','Misamis Occidental'),('negocc','negoc','Negros Occidental'),('negor','negor','Negros Oriental'),('nuevaecija','nuevaecija','Nueva Ecija'),('nuevavizcaya','nuevavizcaya','Nueva Vizcaya'),('palawan','palawan','Palawan'),('quezon','quezon','Quezon'),('surigaodelnorte','surigaodelnorte','Surigao Del Norte');
/*!40000 ALTER TABLE `partner_cluster` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partner_online`
--

DROP TABLE IF EXISTS `partner_online`;
CREATE TABLE `partner_online` (
  `id` varchar(50) NOT NULL,
  `dtregistered` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `partner_online`
--

LOCK TABLES `partner_online` WRITE;
/*!40000 ALTER TABLE `partner_online` DISABLE KEYS */;
INSERT INTO `partner_online` VALUES ('06306','2017-06-30 09:06:46');
/*!40000 ALTER TABLE `partner_online` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partner_payment_option`
--

DROP TABLE IF EXISTS `partner_payment_option`;
CREATE TABLE `partner_payment_option` (
  `objid` varchar(50) NOT NULL,
  `partnerid` varchar(50) DEFAULT NULL,
  `paypartnerid` varchar(50) DEFAULT NULL,
  `info` longtext,
  PRIMARY KEY (`objid`),
  KEY `fk_partner` (`partnerid`),
  KEY `fk_payment_partner` (`paypartnerid`),
  CONSTRAINT `fk_partner` FOREIGN KEY (`partnerid`) REFERENCES `partner` (`id`),
  CONSTRAINT `fk_payment_partner` FOREIGN KEY (`paypartnerid`) REFERENCES `payment_partner` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `partner_payment_option`
--

LOCK TABLES `partner_payment_option` WRITE;
/*!40000 ALTER TABLE `partner_payment_option` DISABLE KEYS */;
INSERT INTO `partner_payment_option` VALUES ('06306_DBP','06306','DBP','[terminalid:56, transactionkey:\"5f16150a98195379b69b525626cf41c41d79b33d\", \r\nactionurl:\"https://testipg.apollo.com.ph:8443/transaction/verify\", \r\ntestactionurl:\"http://test.rameses.com:8580/dbp/transaction/verify\"]'),('06306_LBP','06306','LBP','[merchantcode:\"2016120039\", successurl: \"http://localhost:8580/payoptions/landbanksuccess\", errorurl:\"http://localhost:8580/payoptions/landbankerror\"]'),('137_LBP','137','LBP','[merchantcode:\"2016120039\", successurl: \"http://localhost:8580/landbanksuccess\", errorurl:\"http://localhost:8580/landbankerror\"]'),('154_DBP','154','DBP','[terminalid:56, transactionkey:\"5f16150a98195379b69b525626cf41c41d79b33d\", \r\nactionurl:\"https://testipg.apollo.com.ph:8443/transaction/verify\", \r\ntestactionurl:\"http://test.rameses.com:8580/dbp/transaction/verify\"]'),('195_DBP','195','DBP','[terminalid:56, transactionkey:\"5f16150a98195379b69b525626cf41c41d79b33d\", \r\nactionurl:\"https://testipg.apollo.com.ph:8443/transaction/verify\", \r\ntestactionurl:\"http://test.rameses.com:8580/dbp/transaction/verify\"]'),('195_LBP','195','LBP','[merchantcode:\"2016120039\", successurl: \"http://localhost:8580/landbanksuccess\", errorurl:\"http://localhost:8580/landbankerror\"]');
/*!40000 ALTER TABLE `partner_payment_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_partner`
--

DROP TABLE IF EXISTS `payment_partner`;
CREATE TABLE `payment_partner` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `code` varchar(25) DEFAULT NULL,
  `actionurl` varchar(255) DEFAULT NULL,
  `caption` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `payment_partner`
--

LOCK TABLES `payment_partner` WRITE;
/*!40000 ALTER TABLE `payment_partner` DISABLE KEYS */;
INSERT INTO `payment_partner` VALUES ('DBP','DEVELOPMENT BANK OF THE PHILIPPINES','101','https://testipg.apollo.com.ph:8443/transaction/verify','DBP'),('LBP','LAND BANK OF THE PHILIPPINES','102','https://222.127.109.48/epp_mobile/','LANDBANK');
/*!40000 ALTER TABLE `payment_partner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_sequence`
--

DROP TABLE IF EXISTS `sys_sequence`;
CREATE TABLE `sys_sequence` (
  `objid` varchar(100) NOT NULL,
  `nextSeries` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `sys_sequence`
--

LOCK TABLES `sys_sequence` WRITE;
/*!40000 ALTER TABLE `sys_sequence` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_session`
--

DROP TABLE IF EXISTS `sys_session`;
CREATE TABLE `sys_session` (
  `sessionid` varchar(50) NOT NULL,
  `userid` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `clienttype` varchar(12) DEFAULT NULL,
  `accesstime` datetime DEFAULT NULL,
  `accessexpiry` datetime DEFAULT NULL,
  `timein` datetime DEFAULT NULL,
  PRIMARY KEY (`sessionid`),
  KEY `ix_timein` (`timein`),
  KEY `ix_userid` (`userid`),
  KEY `ix_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `sys_session`
--

LOCK TABLES `sys_session` WRITE;
/*!40000 ALTER TABLE `sys_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_session_log`
--

DROP TABLE IF EXISTS `sys_session_log`;
CREATE TABLE `sys_session_log` (
  `sessionid` varchar(50) NOT NULL,
  `userid` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `clienttype` varchar(12) DEFAULT NULL,
  `accesstime` datetime DEFAULT NULL,
  `accessexpiry` datetime DEFAULT NULL,
  `timein` datetime DEFAULT NULL,
  `timeout` datetime DEFAULT NULL,
  `state` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`sessionid`),
  KEY `ix_timein` (`timein`),
  KEY `ix_timeout` (`timeout`),
  KEY `ix_userid` (`userid`),
  KEY `ix_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `sys_session_log`
--

LOCK TABLES `sys_session_log` WRITE;
/*!40000 ALTER TABLE `sys_session_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_session_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_var`
--

DROP TABLE IF EXISTS `sys_var`;
CREATE TABLE `sys_var` (
  `name` varchar(50) NOT NULL,
  `value` longtext,
  `description` varchar(255) DEFAULT NULL,
  `datatype` varchar(15) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `sys_var`
--

LOCK TABLES `sys_var` WRITE;
/*!40000 ALTER TABLE `sys_var` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_var` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-06-30  1:47:07
