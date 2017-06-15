/*
SQLyog Ultimate v10.00 Beta1
MySQL - 5.1.73-community-log : Database - skilldb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`skilldb` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `skilldb`;

/*Table structure for table `address_abroad` */

CREATE TABLE `address_abroad` (
  `name` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `address_local` */

CREATE TABLE `address_local` (
  `name` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `course` */

CREATE TABLE `course` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dataentry` */

CREATE TABLE `dataentry` (
  `objid` varchar(50) NOT NULL,
  `dtcreated` date DEFAULT NULL,
  `data` longtext,
  `createdby_objid` varchar(50) DEFAULT NULL,
  `createdby_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `educationallevel` */

CREATE TABLE `educationallevel` (
  `level` varchar(50) DEFAULT NULL,
  `index` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `entity_education` */

CREATE TABLE `entity_education` (
  `objid` varchar(50) NOT NULL,
  `entityid` varchar(50) DEFAULT NULL,
  `schoolname` text,
  `educationlevel` varchar(50) DEFAULT NULL,
  `course_objid` varchar(50) DEFAULT NULL,
  `course_name` varchar(50) DEFAULT NULL,
  `fromyear` int(11) DEFAULT NULL,
  `toyear` int(11) DEFAULT NULL,
  `awards` text,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `entity_eligibility` */

CREATE TABLE `entity_eligibility` (
  `objid` varchar(50) NOT NULL,
  `entityid` varchar(50) DEFAULT NULL,
  `name` text,
  `licenseno` varchar(50) DEFAULT NULL,
  `expirydate` date DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `entity_jobpreference_occupation` */

CREATE TABLE `entity_jobpreference_occupation` (
  `objid` varchar(50) NOT NULL,
  `entityid` varchar(50) DEFAULT NULL,
  `occupation` text,
  `industry` text,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `entity_jobpreference_worklocation` */

CREATE TABLE `entity_jobpreference_worklocation` (
  `objid` varchar(50) NOT NULL,
  `entityid` varchar(50) DEFAULT NULL,
  `local` tinyint(1) DEFAULT NULL,
  `location` text,
  `index` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `entity_languageproficiency` */

CREATE TABLE `entity_languageproficiency` (
  `objid` varchar(50) NOT NULL,
  `entityid` varchar(50) DEFAULT NULL,
  `language` text,
  `read` tinyint(1) DEFAULT NULL,
  `write` tinyint(1) DEFAULT NULL,
  `speak` tinyint(1) DEFAULT NULL,
  `understand` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `entity_skill` */

CREATE TABLE `entity_skill` (
  `objid` varchar(50) NOT NULL,
  `entityid` varchar(50) DEFAULT NULL,
  `name` text,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `entity_training` */

CREATE TABLE `entity_training` (
  `objid` varchar(50) NOT NULL,
  `entityid` varchar(50) DEFAULT NULL,
  `training` text,
  `fromdate` date DEFAULT NULL,
  `todate` date DEFAULT NULL,
  `institution` text,
  `certificates` text,
  `completed` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `entity_workexperience` */

CREATE TABLE `entity_workexperience` (
  `objid` varchar(50) NOT NULL,
  `entityid` varchar(50) DEFAULT NULL,
  `companyname` text,
  `address` text,
  `fromdate` date DEFAULT NULL,
  `todate` date DEFAULT NULL,
  `appointmentstatus` text,
  `jobtitle_objid` varchar(50) DEFAULT NULL,
  `jobtitle_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `jobtitle` */

CREATE TABLE `jobtitle` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`objid`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `language` */

CREATE TABLE `language` (
  `name` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `address_abroad` */

insert  into `address_abroad`(`name`) values ('USA'),('CANADA'),('UNITED KINGDOM'),('JAPAN'),('HONG KONG'),('SOUTH KOREA'),('DUBAI'),('CHINA'),('RUSSIA'),('AUSTRALIA'),('FRANCE');

/*Data for the table `address_local` */

insert  into `address_local`(`name`) values ('CEBU'),('DAVAO'),('MAKATI'),('TAGUIG'),('ILOILO');

/*Data for the table `educationallevel` */

insert  into `educationallevel`(`level`,`index`) values ('NO FORMAL EDUCATION',0),('INCOMPLETE ELEMENTARY LEVEL',1),('ELEMENTARY GRADUATE',2),('INCOMPLETE HIGH SCHOOL LEVEL',3),('HIGH SCHOOL GRADUATE',4),('INCOMPLETE COLLEGE LEVEL',5),('COLLEGE GRADUATE',6),('TECHNICAL-VOCATIONAL GRADUATE',7),('POST GRADUATE',8);

/*Data for the table `language` */

insert  into `language`(`name`) values ('ENGLISH'),('BISAYA'),('TAGALOG'),('ILOKO'),('ILONGGO'),('AKLANON'),('SPANISH'),('MANDARIN'),('ITALIAN'),('PORTUGESE');
