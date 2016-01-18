USE clfc2;

CREATE TABLE IF NOT EXISTS `loan_ledger_payment_transfer` (
  `objid` varchar(40) NOT NULL,
  `dtfiled` datetime default NULL,
  `author_objid` varchar(40) default NULL,
  `author_name` varchar(25) default NULL,
  `ledgerid` varchar(40) default NULL,
  `prevledgerid` varchar(40) default NULL,
  `fromdate` date default NULL,
  `todate` date default NULL,
  `remarks` varchar(255) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_ledgerid` (`ledgerid`),
  KEY `ix_prevledgerid` (`prevledgerid`),
  KEY `ix_fromdate` (`fromdate`),
  KEY `ix_todate` (`todate`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_authorid` (`author_objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `note` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL,
  `dtcreated` datetime default NULL,
  `createdby` varchar(50) default NULL,
  `ledger_objid` varchar(50) default NULL,
  `route_code` varchar(50) default NULL,
  `loanapp_objid` varchar(50) default NULL,
  `loanapp_appno` varchar(50) default NULL,
  `borrower_objid` varchar(50) default NULL,
  `borrower_name` varchar(255) default NULL,
  `borrower_address` varchar(255) default NULL,
  `dtstart` date default NULL,
  `dtend` date default NULL,
  `text` text,
  `dtapproved` datetime default NULL,
  `approvedby` varchar(50) default NULL,
  `approvedremarks` varchar(255) default NULL,
  `dtposted` datetime default NULL,
  `postedby` varchar(50) default NULL,
  `postedremarks` varchar(255) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtcreatedby` (`dtcreated`,`createdby`),
  KEY `ix_ledger_objid` (`ledger_objid`),
  KEY `ix_route_code` (`route_code`),
  KEY `ix_loanapp_objid` (`loanapp_objid`),
  KEY `ix_loanapp_appno` (`loanapp_appno`),
  KEY `ix_borrower_name` (`borrower_name`),
  KEY `ix_dtstart` (`dtstart`),
  KEY `ix_dtend` (`dtend`),
  KEY `ix_dtapprovedby` (`dtapproved`,`approvedby`),
  KEY `ix_dtpostedby` (`dtposted`,`postedby`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `note_active` (
  `objid` varchar(50) NOT NULL,
  `loanapp_appno` varchar(50) default NULL,
  `borrower_name` varchar(255) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_loanapp_appno` (`loanapp_appno`),
  KEY `ix_borrower_name` (`borrower_name`),
  CONSTRAINT `fk_note_objid` FOREIGN KEY (`objid`) REFERENCES `note` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `note_log` (
  `objid` varchar(50) NOT NULL,
  `noteid` varchar(50) default NULL,
  `dtfiled` datetime default NULL,
  `filedby` varchar(50) default NULL,
  `action` varchar(50) default NULL,
  `remarks` varchar(255) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_noteid` (`noteid`),
  KEY `ix_dtfiledby` (`dtfiled`,`filedby`),
  KEY `ix_action` (`action`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

