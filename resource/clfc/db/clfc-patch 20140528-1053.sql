USE clfc2;

CREATE TABLE IF NOT EXISTS `exemptiontype` 
(
  `objid` varchar(50) NOT NULL,
  `txnstate` varchar(25) default NULL,
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_username` varchar(50) default NULL,
  `code` varchar(50) default NULL,
  `name` varchar(50) default NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_code` (`code`),
  KEY `ix_name` (`name`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_author_objid` (`author_objid`),
  KEY `ix_author_username` (`author_username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `checkpayment` 
(
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL,
  `dtcreated` datetime default NULL,
  `refid` varchar(50) default NULL,
  `refno` varchar(25) default NULL,
  `dtpaid` datetime default NULL,
  `paidby` varchar(150) default NULL,
  `amount` decimal(8,2) default '0.00',
  `checkno` varchar(25) default NULL,
  `checkdate` date default NULL,
  `bank_objid` varchar(50) default NULL,
  `collector_objid` varchar(50) default NULL,
  `collector_name` varchar(150) default NULL,
  `payor_objid` varchar(50) default NULL,
  `payor_name` varchar(150) default NULL,
  `posting_date` datetime default NULL,
  `posting_userid` varchar(50) default NULL,
  `posting_username` varchar(50) default NULL,
  `posting_remarks` varchar(255) default NULL,
  `passbook_objid` varchar(50) default NULL,
  `dtresolved` date default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_refno` (`refno`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_dtpaid` (`dtpaid`),
  KEY `ix_checkno` (`checkno`),
  KEY `ix_checkdate` (`checkdate`),
  KEY `ix_bank_objid` (`bank_objid`),
  KEY `ix_collector_objid` (`collector_objid`),
  KEY `ix_payor_objid` (`payor_objid`),
  KEY `ix_payor_name` (`payor_name`),
  KEY `ix_refid` (`refid`),
  KEY `ix_postingdate` (`posting_date`),
  KEY `ix_postinguserid` (`posting_userid`),
  KEY `ix_postingusername` (`posting_username`),
  KEY `ix_passbook_objid` (`passbook_objid`),
  KEY `ix_dtresolved` (`dtresolved`),
  CONSTRAINT `fk_checkpayment_passbook_objid_objid` FOREIGN KEY (`passbook_objid`) REFERENCES `passbook` (`objid`),
  CONSTRAINT `fk_checkpayment_bank_objid_objid` FOREIGN KEY (`bank_objid`) REFERENCES `bank` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `checkpayment_active` 
(
  `objid` varchar(50) NOT NULL,
  PRIMARY KEY  (`objid`),
  CONSTRAINT `fk_checkpayment_active_objid_objid` FOREIGN KEY (`objid`) REFERENCES `checkpayment` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

