USE `clfc2`;

CREATE TABLE IF NOT EXISTS `encashment` (
  `objid` varchar(50) NOT NULL,
  `txnstate` varchar(25) default NULL COMMENT 'DRAFT,FOR_APPROVAL,APPROVED,DISAPPROVED',
  `txndate` date default NULL,
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `amount` decimal(10,2) default '0.00',
  `remarks` varchar(255) default NULL,
  `dtposted` datetime default NULL,
  `poster_objid` varchar(50) default NULL,
  `poster_name` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_txnstate` (`txnstate`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_dtposted` (`dtposted`),
  KEY `ix_posterid` (`poster_objid`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `encashment_cb` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `cbsno` varchar(25) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_cbsno` (`parentid`),
  CONSTRAINT `fk_encashment_cb` FOREIGN KEY (`parentid`) REFERENCES `encashment` (`objid`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `encashment_cb_detail` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `encashmentid` varchar(50) default NULL,
  `denomination` decimal(7,2) default '0.00',
  `qty` smallint(6) default '0',
  `amount` decimal(10,2) default '0.00',
  PRIMARY KEY  (`objid`),
  KEY `ix_encashmentid` (`encashmentid`),
  KEY `ix_denomination` (`denomination`),
  KEY `fk_encashment_cb_detail` (`parentid`),
  CONSTRAINT `fk_encashment_cb_detail` FOREIGN KEY (`parentid`) REFERENCES `encashment_cb` (`objid`),
  CONSTRAINT `fk_encashment_cb_detail_encashment` FOREIGN KEY (`encashmentid`) REFERENCES `encashment` (`objid`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `encashment_cbs` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `refid` varchar(50) default NULL,
  `cbsno` varchar(25) default NULL,
  `txndate` date default NULL,
  `collectiontype` varchar(25) default NULL,
  `amount` decimal(10,2) default '0.00',
  `cbsid` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `fk_encashment_cbs` (`parentid`),
  KEY `ix_refid` (`refid`),
  KEY `ix_cbsno` (`cbsno`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_collectiontype` (`collectiontype`),
  KEY `ix_cbsid` (`cbsid`),
  CONSTRAINT `fk_encashment_cbs` FOREIGN KEY (`parentid`) REFERENCES `encashment` (`objid`),
  CONSTRAINT `fk_encashment_cbs_collection_cb` FOREIGN KEY (`refid`) REFERENCES `collection_cb` (`objid`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `encashment_cbs_detail` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `denomination` decimal(7,2) default '0.00',
  `qty` smallint(6) default '0',
  `amount` decimal(10,2) default '0.00',
  PRIMARY KEY  (`objid`),
  KEY `ix_denomination` (`denomination`),
  KEY `fk_encashment_cbs_detail` (`parentid`),
  CONSTRAINT `fk_encashment_cbs_detail` FOREIGN KEY (`parentid`) REFERENCES `encashment_cbs` (`objid`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `encashment_cbs_reference` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `denomination` decimal(7,2) default '0.00',
  `qty` smallint(6) default '0',
  `amount` decimal(10,2) default '0.00',
  PRIMARY KEY  (`objid`),
  KEY `ix_denomination` (`denomination`),
  KEY `fk_encashment_cbs_reference` (`parentid`),
  CONSTRAINT `fk_encashment_cbs_reference` FOREIGN KEY (`parentid`) REFERENCES `encashment_cbs` (`objid`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `encashment_check` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `checkno` varchar(25) default NULL,
  `txndate` date default NULL,
  `amount` decimal(10,2) default '0.00',
  `bank_objid` varchar(50) default NULL,
  `passbook_objid` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_checkno` (`checkno`),
  KEY `ix_txndate` (`txndate`),
  KEY `fk_encashment_check` (`parentid`),
  KEY `fk_encashmentcheck_bank` (`bank_objid`),
  KEY `fk_encashmentcheck_passbook` (`passbook_objid`),
  CONSTRAINT `fk_encashmentcheck_bank` FOREIGN KEY (`bank_objid`) REFERENCES `bank` (`objid`),
  CONSTRAINT `fk_encashmentcheck_passbook` FOREIGN KEY (`passbook_objid`) REFERENCES `passbook` (`objid`),
  CONSTRAINT `fk_encashment_check` FOREIGN KEY (`parentid`) REFERENCES `encashment` (`objid`)
) ENGINE=InnoDB;


