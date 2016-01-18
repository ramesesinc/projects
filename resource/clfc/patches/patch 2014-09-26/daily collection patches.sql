USE `clfc2`;

CREATE TABLE IF NOT EXISTS `dailycollection` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL COMMENT 'DRAFT,SEND_BACK,FOR_VERIFICATION,VERIFIED',
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `txndate` date default NULL,
  `totalcollection` decimal(10,2) default '0.00',
  `dtverified` datetime default NULL,
  `verifier_objid` varchar(50) default NULL,
  `verifier_name` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_txndate` (`txndate`),
  KEY `ix_state` (`state`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_dtverified` (`dtverified`),
  KEY `ix_verifierid` (`verifier_objid`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `dailycollection_cbs` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `refid` varchar(50) default NULL,
  `txndate` date default NULL,
  `cbsno` varchar(25) default NULL,
  `amount` decimal(10,2) default '0.00',
  `isencashed` smallint(3) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_cbsno` (`cbsno`),
  KEY `ix_refid` (`refid`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_isencashed` (`isencashed`),
  KEY `fk_dailycollection_cbs` (`parentid`),
  CONSTRAINT `fk_dailycollection_cbs` FOREIGN KEY (`parentid`) REFERENCES `dailycollection` (`objid`),
  CONSTRAINT `fk_dailycollection_cbs_collection_cb` FOREIGN KEY (`refid`) REFERENCES `collection_cb` (`objid`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `dailycollection_depositslip` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `refid` varchar(50) default NULL,
  `txndate` date default NULL,
  `controlno` varchar(25) default NULL,
  `amount` decimal(10,2) default '0.00',
  `passbook_objid` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_refid` (`refid`),
  UNIQUE KEY `uix_controlno` (`controlno`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_passbookid` (`passbook_objid`),
  KEY `fk_dailycollection_depositslip` (`parentid`),
  CONSTRAINT `fk_dailycollection_depositslip` FOREIGN KEY (`parentid`) REFERENCES `dailycollection` (`objid`),
  CONSTRAINT `fk_dailycollection_depositslip_depositslip` FOREIGN KEY (`refid`) REFERENCES `depositslip` (`objid`),
  CONSTRAINT `fk_dailycollection_depositslip_passbook` FOREIGN KEY (`passbook_objid`) REFERENCES `passbook` (`objid`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `dailycollection_encashment` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `refid` varchar(50) default NULL,
  `txndate` date default NULL,
  `amount` decimal(7,2) default '0.00',
  `checkdate` date default NULL,
  `checkno` varchar(25) default NULL,
  `bank_objid` varchar(50) default NULL,
  `passbook_objid` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_refid` (`refid`),
  UNIQUE KEY `uix_checkno` (`checkno`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_checkdate` (`checkdate`),
  KEY `ix_bankid` (`bank_objid`),
  KEY `ix_passbookid` (`passbook_objid`),
  KEY `fk_dailycollection_encashment` (`parentid`),
  CONSTRAINT `fk_dailycollection_encashment` FOREIGN KEY (`parentid`) REFERENCES `dailycollection` (`objid`),
  CONSTRAINT `fk_dailycollection_encashment_bank` FOREIGN KEY (`bank_objid`) REFERENCES `bank` (`objid`),
  CONSTRAINT `fk_dailycollection_encashment_encashment` FOREIGN KEY (`refid`) REFERENCES `encashment` (`objid`),
  CONSTRAINT `fk_dailycollection_encashment_passbook` FOREIGN KEY (`passbook_objid`) REFERENCES `passbook` (`objid`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `dailycollection_overage` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `refid` varchar(50) default NULL,
  `refno` varchar(25) default NULL,
  `txndate` date default NULL,
  `collector_objid` varchar(50) default NULL,
  `collector_name` varchar(50) default NULL,
  `amount` decimal(7,2) default '0.00',
  `remarks` varchar(255) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_refid` (`refid`),
  UNIQUE KEY `uix_refno` (`refno`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_collectorid` (`collector_objid`),
  KEY `fk_dailycollection_overage` (`parentid`),
  CONSTRAINT `fk_dailycollection_overage` FOREIGN KEY (`parentid`) REFERENCES `dailycollection` (`objid`),
  CONSTRAINT `fk_dailycollection_overage_overage` FOREIGN KEY (`refid`) REFERENCES `overage` (`objid`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `dailycollection_shortage` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `refid` varchar(50) default NULL,
  `refno` varchar(25) default NULL,
  `txndate` date default NULL,
  `cbsno` varchar(25) default NULL,
  `collector_objid` varchar(50) default NULL,
  `collector_name` varchar(50) default NULL,
  `amount` decimal(7,2) default '0.00',
  `remarks` varchar(255) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_refid` (`refid`),
  UNIQUE KEY `uix_cbsno` (`cbsno`),
  UNIQUE KEY `uix_refno` (`refno`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_collectorid` (`collector_objid`),
  KEY `fk_dailycollection_shortage` (`parentid`),
  CONSTRAINT `fk_dailycollection_shortage` FOREIGN KEY (`parentid`) REFERENCES `dailycollection` (`objid`),
  CONSTRAINT `fk_dailycollection_shortage_shortage` FOREIGN KEY (`refid`) REFERENCES `shortage` (`objid`)
) ENGINE=InnoDB;







