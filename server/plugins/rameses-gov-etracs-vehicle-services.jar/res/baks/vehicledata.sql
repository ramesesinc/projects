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

/*Data for the table `vehicletype` */

insert  into `vehicletype`(`objid`,`title`,`controlpattern`,`pattern_appno`,`pattern_acctno`,`indexno`,`issued`) values ('boat','BOAT',NULL,'boat',NULL,0,0),('pedicab','PEDICAB',NULL,'pedicab',NULL,2,0),('tricycle','TRICYCLE','MTOP-[org]-[%05d]','mtop',NULL,1,10);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
