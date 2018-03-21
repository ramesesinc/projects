/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.1.73-community : Database - icws_etracs25
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `sys_report` */

DROP TABLE IF EXISTS `sys_report`;

CREATE TABLE `sys_report` (
  `objid` varchar(50) NOT NULL,
  `folderid` varchar(50) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `filetype` varchar(25) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `createdby_objid` varchar(50) DEFAULT NULL,
  `createdby_name` varchar(255) DEFAULT NULL,
  `datasetid` varchar(50) DEFAULT NULL,
  `template` mediumtext,
  `outputtype` varchar(50) DEFAULT NULL,
  `system` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `FK_sys_report_dataset` (`datasetid`),
  KEY `FK_sys_report_entry_folder` (`folderid`),
  CONSTRAINT `sys_report_ibfk_1` FOREIGN KEY (`datasetid`) REFERENCES `sys_dataset` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_report` */

insert  into `sys_report`(`objid`,`folderid`,`title`,`filetype`,`dtcreated`,`createdby_objid`,`createdby_name`,`datasetid`,`template`,`outputtype`,`system`) values ('waterworks_billing',NULL,'Waterworks Billing ',NULL,NULL,NULL,NULL,NULL,'<% def dmf = new java.text.SimpleDateFormat(\'MMM\'); def YMD = new java.text.SimpleDateFormat(\'yyyy-MM-dd\'); %>\r\n\r\n        ${o.account.acctname}\r\n	${o.account.address?.text}\r\n          ${o.account.acctno}             ${o.blockseqno}           ${o.classification}\r\n\r\n\r\n                                   ${o.readingdate ? YMD.format(o.readingdate) : \"\"}\r\n                                   ${o.reading}\r\n                                   ${o.prevreadingdate ? YMD.format(o.prevreadingdate) : \"\"}\r\n                                   ${o.prevreading}\r\n                                   ${o.volume}\r\n				   ${o.amount}\r\n                                   ${o.readingdate ? dmf.format(o.readingdate) : \"\"}\r\n\r\n				   ${(o.arrears==0 && o.discdate) ? YMD.format(o.discdate) : \"-\"}\r\n                                   ${(o.arrears==0) ? o.amount*(1-0.05) : \"-\"}\r\n\r\n                                   ${o.arrears+o.surcharge+o.interest+o.otherfees}\r\n                                   ${o.amount+o.arrears+o.surcharge+o.interest+o.otherfees}\r\n\r\n                                   ${o.amount * 0.14}\r\n                                   ${o.duedate ? YMD.format(o.duedate) : \"\"}\r\n                                   ${o.amount*1.14}\r\n                                   ${(o.amount*1.14)+o.arrears+o.surcharge+o.interest+o.otherfees}\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n','text',1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
