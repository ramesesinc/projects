USE clfc2;

RENAME TABLE `txnlog` TO `datatxnlog`; 

CREATE TABLE IF NOT EXISTS `txnlog` 
(
  `objid` varchar(50) NOT NULL,
  `ref` varchar(100) NOT NULL,
  `refid` text NOT NULL,
  `txndate` datetime NOT NULL,
  `action` varchar(50) NOT NULL,
  `userid` varchar(50) NOT NULL,
  `remarks` text,
  `diff` longtext,
  `username` varchar(150) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_txnlog_action` (`action`),
  KEY `ix_txnlog_ref` (`ref`),
  KEY `ix_txnlog_userid` (`userid`),
  KEY `ix_txnlog_useridaction` (`userid`,`action`)
) ENGINE=InnoDB;
