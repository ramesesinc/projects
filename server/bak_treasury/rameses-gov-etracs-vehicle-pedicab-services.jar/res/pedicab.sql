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

/*Table structure for table `vehicle_application_pedicab` */

DROP TABLE IF EXISTS `vehicle_application_pedicab`;

CREATE TABLE `vehicle_application_pedicab` (
  `objid` varchar(50) NOT NULL,
  `plateno` varchar(50) NOT NULL,
  `color` varchar(50) DEFAULT NULL,
  `bodyno` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_application_pedicab_task` */

DROP TABLE IF EXISTS `vehicle_application_pedicab_task`;

CREATE TABLE `vehicle_application_pedicab_task` (
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
  `dtcreated` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vehicle_franchise_pedicab` */

DROP TABLE IF EXISTS `vehicle_franchise_pedicab`;

CREATE TABLE `vehicle_franchise_pedicab` (
  `objid` varchar(50) NOT NULL,
  `plateno` varchar(50) DEFAULT NULL,
  `color` varchar(50) DEFAULT NULL,
  `bodyno` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


INSERT INTO vehicletype
(objid,title,controlpattern,pattern_appno,indexno)
VALUES
('pedicab', 'PEDICAB', 'PED-[org][cluster][%05d]', 'ped',2)
