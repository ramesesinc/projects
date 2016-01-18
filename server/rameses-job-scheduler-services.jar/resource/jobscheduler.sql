/*
SQLyog Ultimate v9.51 
MySQL - 5.5.39 : Database - jobscheduler
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `sys_scheduled_job` */

CREATE TABLE `sys_scheduled_job` (
  `objid` varchar(50) NOT NULL,
  `interval` int(11) DEFAULT NULL,
  `timeunit` varchar(50) DEFAULT NULL,
  `expirydate` datetime DEFAULT NULL,
  `startdate` datetime DEFAULT NULL,
  `enddate` datetime DEFAULT NULL,
  `jobtype` varchar(50) DEFAULT NULL,
  `sql` varchar(255) DEFAULT NULL,
  `adapter` varchar(50) DEFAULT NULL,
  `template` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `sys_scheduled_job` */

insert  into `sys_scheduled_job`(`objid`,`interval`,`timeunit`,`expirydate`,`startdate`,`enddate`,`jobtype`,`sql`,`adapter`,`template`) values ('T1',NULL,NULL,'2015-05-31 15:51:00',NULL,NULL,'sms','SELECT code,name FROM bank','main',NULL);

/*Table structure for table `sys_scheduled_job_completed` */

CREATE TABLE `sys_scheduled_job_completed` (
  `objid` varchar(50) NOT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `sys_scheduled_job_completed` */

insert  into `sys_scheduled_job_completed`(`objid`) values ('T1');

/*Table structure for table `sys_scheduled_job_processing` */

CREATE TABLE `sys_scheduled_job_processing` (
  `objid` varchar(50) NOT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `sys_scheduled_job_processing` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
