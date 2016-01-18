USE clfc2;

CREATE TABLE IF NOT EXISTS `collection_cashbreakdown` (
  `objid` VARCHAR(40) NOT NULL,
  `dtfiled` DATETIME DEFAULT NULL,
  `filedby` VARCHAR(100) DEFAULT NULL,
  `collection_objid` VARCHAR(40) DEFAULT NULL,
  `collection_type` VARCHAR(25) DEFAULT NULL COMMENT 'FIELD,ONLINE',
  `group_objid` VARCHAR(40) DEFAULT NULL,
  `group_type` VARCHAR(25) DEFAULT NULL COMMENT 'route,followup,special,online',
  PRIMARY KEY  (`objid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_collectionid` (`collection_objid`),
  KEY `ix_collectiontype` (`collection_type`),
  KEY `ix_groupid` (`group_objid`),
  KEY `ix_grouptype` (`group_type`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS  `collection_cashbreakdown_detail` (
  `objid` VARCHAR(40) NOT NULL,
  `parentid` VARCHAR(40) DEFAULT NULL,
  `denomination` DECIMAL(7,2) DEFAULT NULL,
  `qty` SMALLINT(6) DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_qty` (`qty`),
  KEY `ix_denomination` (`denomination`),
  KEY `ix_amount` (`amount`),
  KEY `ix_parentid` (`parentid`),
  CONSTRAINT `FK_detail_cashbreakdown` FOREIGN KEY (`parentid`) REFERENCES `collection_cashbreakdown` (`objid`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS  `loan_ledger_payment` (
  `objid` VARCHAR(40) NOT NULL,
  `parentid` VARCHAR(40) DEFAULT NULL,
  `refno` VARCHAR(25) DEFAULT NULL,
  `txndate` DATE DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_refno` (`refno`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_parentid` (`parentid`),
  CONSTRAINT `FK_payment_ledger` FOREIGN KEY (`parentid`) REFERENCES `loan_ledger` (`objid`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS  `followup_collection` (
  `objid` VARCHAR(40) NOT NULL,
  PRIMARY KEY  (`objid`),
  CONSTRAINT `FK_followupcollection_specialcollection` FOREIGN KEY (`objid`) REFERENCES `special_collection` (`objid`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;