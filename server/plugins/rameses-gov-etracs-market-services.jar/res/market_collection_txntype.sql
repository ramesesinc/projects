/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.1.73-community : Database - marketdb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`marketdb` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `marketdb`;

/*Table structure for table `market_collection_txntype` */

DROP TABLE IF EXISTS `market_collection_txntype`;

CREATE TABLE `market_collection_txntype` (
  `objid` varchar(50) NOT NULL,
  `caption` varchar(100) DEFAULT NULL,
  `txntype` varchar(100) DEFAULT NULL,
  `sortorder` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `market_collection_txntype` */

insert  into `market_collection_txntype`(`objid`,`caption`,`txntype`,`sortorder`) values ('COMPROMISE','Compromise','COMPROMISE',3),('ELECTRICITY','Electricity','ELECTRICITY',1),('MARKETRENTAL','Stall Rental',NULL,0),('WATER','Water','WATER',2);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
