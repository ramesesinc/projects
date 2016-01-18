
CREATE DATABASE IF NOT EXISTS `clfc2_notification`;

USE `clfc2_notification`;
SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `async_notification`;

CREATE TABLE `async_notification` (
  `objid` varchar(50) NOT NULL,
  `dtfiled` datetime default NULL,
  `messagetype` varchar(50) default NULL,
  `data` longtext,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtfiled` (`dtfiled`)
) ENGINE=InnoDB;


DROP TABLE IF EXISTS `async_notification_delivered`;

CREATE TABLE `async_notification_delivered` (
  `objid` varchar(50) NOT NULL,
  `dtfiled` datetime default NULL,
  `refid` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_refid` (`refid`)
) ENGINE=InnoDB;


DROP TABLE IF EXISTS `async_notification_failed`;

CREATE TABLE `async_notification_failed` (
  `objid` varchar(50) NOT NULL,
  `dtfiled` datetime default NULL,
  `refid` varchar(50) default NULL,
  `errormessage` text,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_refid` (`refid`)
) ENGINE=InnoDB;


DROP TABLE IF EXISTS `async_notification_pending`;

CREATE TABLE `async_notification_pending` (
  `objid` varchar(50) NOT NULL,
  `dtretry` datetime default NULL,
  `retrycount` smallint(6) default '0',
  PRIMARY KEY  (`objid`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `async_notification_processing`;

CREATE TABLE `async_notification_processing` (
  `objid` varchar(50) NOT NULL,
  `dtfiled` datetime default NULL,
  PRIMARY KEY  (`objid`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `cloud_notification`;

CREATE TABLE `cloud_notification` (
  `objid` varchar(50) character set latin1 NOT NULL,
  `dtfiled` datetime default NULL,
  `sender` varchar(160) default NULL,
  `senderid` varchar(50) character set latin1 default NULL,
  `groupid` varchar(32) character set latin1 default NULL,
  `message` varchar(255) character set latin1 default NULL,
  `messagetype` varchar(50) default NULL,
  `filetype` varchar(50) character set latin1 default NULL,
  `channel` varchar(50) default NULL,
  `channelgroup` varchar(50) default NULL,
  `origin` varchar(50) default NULL,
  `data` longtext,
  `attachmentcount` smallint(6) default '0',
  PRIMARY KEY  (`objid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_groupid` (`groupid`),
  KEY `ix_senderid` (`senderid`),
  KEY `ix_objid` (`objid`),
  KEY `ix_origin` (`origin`)
) ENGINE=InnoDB;


DROP TABLE IF EXISTS `cloud_notification_attachment`;

CREATE TABLE `cloud_notification_attachment` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) NOT NULL,
  `dtfiled` datetime default NULL,
  `indexno` smallint(6) default NULL,
  `name` varchar(50) default NULL,
  `type` varchar(50) default NULL,
  `description` varchar(255) default NULL,
  `fileid` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_parentid` (`parentid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_name` (`name`),
  KEY `ix_fileid` (`fileid`)
) ENGINE=InnoDB;


DROP TABLE IF EXISTS `cloud_notification_delivered`;

CREATE TABLE `cloud_notification_delivered` (
  `objid` varchar(50) character set latin1 NOT NULL,
  `dtfiled` datetime default NULL,
  `traceid` varchar(50) default NULL,
  `tracetime` datetime default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_traceid` (`traceid`),
  KEY `ix_tracetime` (`tracetime`)
) ENGINE=InnoDB;


DROP TABLE IF EXISTS `cloud_notification_failed`;

CREATE TABLE `cloud_notification_failed` (
  `objid` varchar(50) character set latin1 NOT NULL,
  `dtfiled` datetime default NULL,
  `refid` varchar(50) default NULL,
  `reftype` varchar(25) default NULL,
  `errormessage` text,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_refid` (`refid`)
) ENGINE=InnoDB;


DROP TABLE IF EXISTS `cloud_notification_pending`;

CREATE TABLE `cloud_notification_pending` (
  `objid` varchar(50) NOT NULL,
  `dtfiled` datetime default NULL,
  `dtexpiry` datetime default NULL,
  `dtretry` datetime default NULL,
  `type` varchar(25) default NULL COMMENT 'HEADER,ATTACHMENT',
  PRIMARY KEY  (`objid`),
  KEY `ix_dtexpiry` (`dtexpiry`),
  KEY `ix_dtretry` (`dtretry`),
  KEY `ix_dtfiled` (`dtfiled`)
) ENGINE=InnoDB;


DROP TABLE IF EXISTS `cloud_notification_received`;

CREATE TABLE `cloud_notification_received` (
  `objid` varchar(50) character set latin1 NOT NULL,
  `dtfiled` datetime default NULL,
  `traceid` varchar(50) default NULL,
  `tracetime` datetime default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_traceid` (`traceid`),
  KEY `ix_tracetime` (`tracetime`)
) ENGINE=InnoDB;


DROP TABLE IF EXISTS `sms_inbox`;

CREATE TABLE `sms_inbox` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL,
  `dtfiled` datetime default NULL,
  `channel` varchar(25) default NULL,
  `keyword` varchar(50) default NULL,
  `phoneno` varchar(15) default NULL,
  `message` varchar(160) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_phoneno` (`phoneno`)
) ENGINE=InnoDB;


DROP TABLE IF EXISTS `sms_inbox_pending`;

CREATE TABLE `sms_inbox_pending` (
  `objid` varchar(50) NOT NULL,
  `dtexpiry` datetime default NULL,
  `dtretry` datetime default NULL,
  `retrycount` smallint(6) default '0',
  PRIMARY KEY  (`objid`),
  KEY `ix_dtexpiry` (`dtexpiry`),
  KEY `ix_dtretry` (`dtretry`)
) ENGINE=InnoDB;


DROP TABLE IF EXISTS `sms_outbox`;

CREATE TABLE `sms_outbox` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL,
  `dtfiled` datetime default NULL,
  `refid` varchar(50) default NULL,
  `phoneno` varchar(15) default NULL,
  `message` text,
  `creditcount` smallint(6) default '0',
  `remarks` varchar(160) default NULL,
  `dtsend` datetime default NULL,
  `traceid` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_phoneno` (`phoneno`),
  KEY `ix_dtsend` (`dtsend`),
  KEY `ix_refid` (`refid`),
  KEY `ix_traceid` (`traceid`)
) ENGINE=InnoDB;


DROP TABLE IF EXISTS `sys_notification`;

CREATE TABLE `sys_notification` (
  `notificationid` varchar(50) NOT NULL,
  `objid` varchar(50) character set latin1 default NULL,
  `dtfiled` datetime default NULL,
  `sender` varchar(160) default NULL,
  `senderid` varchar(50) character set latin1 default NULL,
  `recipientid` varchar(50) default NULL,
  `recipienttype` varchar(50) default NULL,
  `message` varchar(255) character set latin1 default NULL,
  `filetype` varchar(50) character set latin1 default NULL,
  `data` longtext,
  PRIMARY KEY  (`notificationid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_senderid` (`senderid`),
  KEY `ix_objid` (`objid`),
  KEY `ix_recipientid` (`recipientid`),
  KEY `ix_recipienttype` (`recipienttype`)
) ENGINE=InnoDB;

SET FOREIGN_KEY_CHECKS=1;