USE `clfc2`;

CREATE TABLE IF NOT EXISTS `checkaccount` (
  `objid` varchar(50) NOT NULL,
  `reftype` varchar(25) default NULL COMMENT 'COLLECTION,ENCASHMENT',
  `refid` varchar(50) default NULL,
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `checkno` varchar(25) default NULL,
  `txndate` date default NULL,
  `amount` decimal(10,2) default '0.00',
  `bank_objid` varchar(50) default NULL,
  `passbook_objid` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_checkno` (`checkno`),
  KEY `ix_reftype` (`reftype`),
  KEY `ix_refid` (`refid`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_txndate` (`txndate`),
  KEY `fk_checkaccount_bank` (`bank_objid`),
  KEY `fk_checkaccount_passbook` (`passbook_objid`),
  CONSTRAINT `fk_checkaccount_bank` FOREIGN KEY (`bank_objid`) REFERENCES `bank` (`objid`),
  CONSTRAINT `fk_checkaccount_passbook` FOREIGN KEY (`passbook_objid`) REFERENCES `passbook` (`objid`)
) ENGINE=InnoDB;