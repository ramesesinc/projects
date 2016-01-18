USE clfc2;

CREATE TABLE IF NOT EXISTS bank ( 
	`objid` VARCHAR(50) NOT NULL, 
	`code` VARCHAR(50), 
	`name` VARCHAR(150), 
	`address` VARCHAR(255), 
	`branchname` VARCHAR(150), 
	`manager` VARCHAR(150), 
	PRIMARY KEY (`objid`), 
	UNIQUE INDEX `uix_code_branchname` (`code`, `branchname`), 
	INDEX `ix_name` (`name`), 
	INDEX `ix_branchname` (`branchname`), 
	INDEX `ix_manager` (`manager`) 
); 

INSERT IGNORE INTO sys_usergroup_permission 
	(objid, usergroup_objid, object, permission, title) 
VALUES 
	('DATAMGMT_BANK_CREATE', 'LOAN_DATAMGMT_AUTHOR', 'bank', 'create', 'create'), 
	('DATAMGMT_BANK_READ', 'LOAN_DATAMGMT_AUTHOR', 'bank', 'read', 'read'),
	('DATAMGMT_BANK_EDIT', 'LOAN_DATAMGMT_AUTHOR', 'bank', 'edit', 'edit'),
	('DATAMGMT_BANK_DELETE', 'LOAN_DATAMGMT_AUTHOR', 'bank', 'delete', 'delete'),
	('DATAMGMT_BANK_APPROVE', 'LOAN_DATAMGMT_AUTHOR', 'bank', 'approve', 'approve'); 

CREATE TABLE IF NOT EXISTS note ( 
	`objid` VARCHAR(50) NOT NULL, 
	`state` VARCHAR(25), 
	`dtcreated` DATETIME, 
	`createdby` VARCHAR(50), 
	`ledger_objid` VARCHAR(50), 
	`route_code` VARCHAR(50), 
	`loanapp_objid` VARCHAR(50), 
	`loanapp_appno` VARCHAR(50), 
	`borrower_objid` VARCHAR(50), 
	`borrower_name` VARCHAR(255), 
	`borrower_address` VARCHAR(255), 
	`dtstart` DATE, 
	`dtend` DATE, 
	`text` TEXT, 
	`dtapproved` DATETIME, 
	`approvedby` VARCHAR(50), 
	`approvedremarks` VARCHAR(255), 
	PRIMARY KEY (`objid`), 
	INDEX `ix_dtcreatedby` (`dtcreated`, `createdby`), 
	INDEX `ix_ledger_objid` (`ledger_objid`), 
	INDEX `ix_route_code` (`route_code`), 
	INDEX `ix_loanapp_objid` (`loanapp_objid`), 
	INDEX `ix_loanapp_appno` (`loanapp_appno`), 
	INDEX `ix_borrower_name` (`borrower_name`), 
	INDEX `ix_dtstart` (`dtstart`), 
	INDEX `ix_dtend` (`dtend`), 
	INDEX `ix_dtapprovedby` (`dtapproved`, `approvedby`) 
); 

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
  KEY `ix_dtpostedby` (`dtposted`,`postedby`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;