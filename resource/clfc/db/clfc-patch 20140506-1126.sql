USE clfc2;

CREATE TABLE `change_loanapp_detail_log` (
  `objid` VARCHAR(40) NOT NULL,
  `loanappid` VARCHAR(40) DEFAULT NULL,
  `dtfiled` DATETIME DEFAULT NULL,
  `author_objid` VARCHAR(40) DEFAULT NULL,
  `author_name` VARCHAR(40) DEFAULT NULL,
  `remarks` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_loanappid` (`loanappid`),
  KEY `ix_authorid` (`author_objid`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;


INSERT IGNORE INTO `sys_usergroup_permission`(`objid`, `usergroup_objid`, `object`, `permission`, `title`)
VALUES ('ADMIN_SUPPORT_CLD', 'ADMIN_SUPPORT', 'application', 'changeloandetail', 'change loan detail');


CREATE TABLE IF NOT EXISTS `txnlog` (
  `objid` VARCHAR(50) NOT NULL,
  `txndate` DATETIME DEFAULT NULL,
  `txnid` VARCHAR(50) DEFAULT NULL,
  `user_objid` VARCHAR(50) DEFAULT NULL,
  `user_username` VARCHAR(50) DEFAULT NULL,
  `action` VARCHAR(50) DEFAULT NULL,
  `remarks` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_user_objid` (`user_objid`),
  KEY `ix_user_username` (`user_username`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_txnid` (`txnid`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `exemptiontype` (
  `objid` VARCHAR(50) NOT NULL,
  `dtcreated` DATETIME DEFAULT NULL,
  `author_objid` VARCHAR(50) DEFAULT NULL,
  `author_username` VARCHAR(50) DEFAULT NULL,
  `code` VARCHAR(50) DEFAULT NULL,
  `name` VARCHAR(50) DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_code` (`code`),
  KEY `ix_name` (`name`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_author_objid` (`author_objid`),
  KEY `ix_author_username` (`author_username`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

INSERT IGNORE INTO sys_usergroup_permission 
	(objid, usergroup_objid, object, permission, title) 
VALUES  
	('DATAMGMT_EXEMPTIONTYPE_C', 'LOAN_DATAMGMT_AUTHOR', 'exemptiontype', 'create', 'create'), 
	('DATAMGMT_EXEMPTIONTYPE_D', 'LOAN_DATAMGMT_AUTHOR', 'exemptiontype', 'delete', 'delete'), 
	('DATAMGMT_EXEMPTIONTYPE_R', 'LOAN_DATAMGMT_AUTHOR', 'exemptiontype', 'read', 'read'), 
	('DATAMGMT_EXEMPTIONTYPE_U', 'LOAN_DATAMGMT_AUTHOR', 'exemptiontype', 'edit', 'edit'); 
	
ALTER TABLE `clfc2`.`entityindividual` 
ADD COLUMN `mobileno` VARCHAR(50) NULL AFTER `religion`, 
ADD COLUMN `phoneno` VARCHAR(50) NULL AFTER `mobileno`, 
ADD COLUMN `email` VARCHAR(50) NULL AFTER `phoneno`, 
ADD COLUMN `photo` MEDIUMBLOB NULL AFTER `email`, 
ADD COLUMN `thumbnail` BLOB NULL AFTER `photo`; 
