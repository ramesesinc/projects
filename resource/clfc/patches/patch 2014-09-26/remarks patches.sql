USE `clfc2`;

CREATE TABLE IF NOT EXISTS `collector_remarks` (
  `objid` varchar(50) NOT NULL,
  `ledgerid` varchar(50) default NULL,
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `txndate` date default NULL,
  `collector_objid` varchar(50) default NULL,
  `collector_name` varchar(50) default NULL,
  `remarks` varchar(255) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_collectorid` (`collector_objid`),
  KEY `fk_collector_remarks_ledger` (`ledgerid`),
  KEY `ix_txndate` (`txndate`),
  CONSTRAINT `fk_collector_remarks_ledger` FOREIGN KEY (`ledgerid`) REFERENCES `loan_ledger` (`objid`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `followup_remarks` (
  `objid` varchar(50) NOT NULL,
  PRIMARY KEY  (`objid`),
  CONSTRAINT `fk_followup_remarks` FOREIGN KEY (`objid`) REFERENCES `collector_remarks` (`objid`)
) ENGINE=InnoDB;