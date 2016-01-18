USE clfc2;

CREATE TABLE IF NOT EXISTS `branchfund` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL,
  `amtbalance` decimal(10,2) default '0.00',
  `amtuse` decimal(10,2) default '0.00',
  `refid` varchar(50) default NULL,
  `fundid` varchar(50) default NULL,
  `lockid` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_fundid` (`fundid`),
  KEY `ix_refid` (`refid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `branchfund_detail` (
  `objid` varchar(50) NOT NULL,
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_username` varchar(50) default NULL,
  `amount` decimal(10,2) default '0.00',
  `remarks` text,
  `fundid` varchar(50) default NULL,
  `refid` varchar(50) default NULL,
  `txntype` varchar(25) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_author_objid` (`author_objid`),
  KEY `ix_author_username` (`author_username`),
  KEY `ix_refid` (`refid`),
  KEY `ix_fundid` (`fundid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `branchfund_collector` (
  `objid` varchar(50) NOT NULL,
  `fundid` varchar(50) default NULL,
  `collector_objid` varchar(50) default NULL,
  `collector_username` varchar(50) default NULL,
  `threshold` decimal(8,2) default '0.00',
  `amtuse` decimal(8,2) default '0.00',
  `lockid` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_fundid_collectorid` (`fundid`,`collector_objid`),
  KEY `ix_collector_username` (`collector_username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `fundrequest` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL,
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_username` varchar(50) default NULL,
  `reqno` varchar(25) default NULL,
  `amount` decimal(10,2) default '0.00',
  `remarks` text,
  `data` longtext,
  `posting_date` datetime default NULL,
  `posting_userid` varchar(50) default NULL,
  `posting_username` varchar(50) default NULL,
  `posting_remarks` text,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_reqno` (`reqno`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_author_objid` (`author_objid`),
  KEY `ix_author_username` (`author_username`),
  KEY `ix_posting_date` (`posting_date`),
  KEY `ix_posting_userid` (`posting_userid`),
  KEY `ix_posting_username` (`posting_username`),
  KEY `ix_state` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `fundrequest_pending` (
  `objid` varchar(50) NOT NULL,
  `dtcreated` datetime default NULL,
  PRIMARY KEY  (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `fundrequest_series` (
  `objid` varchar(50) NOT NULL,
  `prefix` varchar(10) NOT NULL,
  `seriesno` bigint(20) default '1',
  `lockid` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_prefix` (`prefix`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1; 


INSERT IGNORE INTO sys_usergroup ( 
  objid, title, domain, role, userclass, orgclass
) 
VALUES 
('TREASURY_ACCT_ASSISTANT', 'ACCOUNTING ASSISTANT', 'TREASURY', 'ACCT_ASSISTANT', 'usergroup', NULL), 
('TREASURY_BRANCH_MANAGER', 'BRANCH MANAGER', 'TREASURY', 'BRANCH_MANAGER', 'usergroup', NULL), 
('TREASURY_ASST_BRANCH_MANAGER', 'ASST. BRANCH MANAGER', 'TREASURY', 'ASST_BRANCH_MANAGER', 'usergroup', NULL), 
('TREASURY_CASHIER', 'CASHIER', 'TREASURY', 'CASHIER', 'usergroup', NULL);

INSERT IGNORE INTO sys_usergroup_permission ( 
  objid, usergroup_objid, object, permission, title 
) 
VALUES 
('TREASURY_CASHIER_A','TREASURY_CASHIER','bank','approve','approve'), 
('TREASURY_CASHIER_C','TREASURY_CASHIER','bank','create','create'), 
('TREASURY_CASHIER_D','TREASURY_CASHIER','bank','delete','delete'), 
('TREASURY_CASHIER_E','TREASURY_CASHIER','bank','edit','edit'), 
('TREASURY_CASHIER_R','TREASURY_CASHIER','bank','read','read'), 
('TREASURY_CASHIER_PBKA','TREASURY_CASHIER','passbook','approve','approve'), 
('TREASURY_CASHIER_PBKC','TREASURY_CASHIER','passbook','create','create'), 
('TREASURY_CASHIER_PBKD','TREASURY_CASHIER','passbook','delete','delete'), 
('TREASURY_CASHIER_PBKE','TREASURY_CASHIER','passbook','edit','edit'), 
('TREASURY_CASHIER_PBKR','TREASURY_CASHIER','passbook','read','read'); 

