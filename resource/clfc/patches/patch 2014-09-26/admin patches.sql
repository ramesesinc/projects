USE `clfc2`;

SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE IF NOT EXISTS `loan_ledger_segregationtype` (
  `objid` VARCHAR(50) NOT NULL,
  `state` VARCHAR(25) DEFAULT NULL,
  `dtcreated` DATETIME DEFAULT NULL,
  `author_objid` VARCHAR(50) DEFAULT NULL,
  `author_name` VARCHAR(50) DEFAULT NULL,
  `name` VARCHAR(50) DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_state` (`state`)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `loan_ledger_segregation` (
  `objid` VARCHAR(50) NOT NULL,
  `refid` VARCHAR(50) DEFAULT NULL,
  `typeid` VARCHAR(50) DEFAULT NULL,
  `date` DATE DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_refid_typeid` (`refid`,`typeid`),
  KEY `ix_date` (`date`),
  KEY `fk_ledger_segregation_segregationtype` (`typeid`),
  CONSTRAINT `fk_ledger_segregation` FOREIGN KEY (`refid`) REFERENCES `loan_ledger` (`objid`),
  CONSTRAINT `fk_ledger_segregation_segregationtype` FOREIGN KEY (`typeid`) REFERENCES `loan_ledger_segregationtype` (`objid`)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `accounttype` (
  `objid` VARCHAR(50) NOT NULL,
  `txnstate` VARCHAR(25) DEFAULT NULL,
  `dtcreated` DATETIME DEFAULT NULL,
  `author_objid` VARCHAR(50) DEFAULT NULL,
  `author_name` VARCHAR(50) DEFAULT NULL,
  `code` VARCHAR(150) DEFAULT NULL,
  `name` VARCHAR(150) DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_code` (`code`),
  KEY `ix_txnstate` (`txnstate`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `currencytype` (
  `objid` VARCHAR(50) NOT NULL,
  `txnstate` VARCHAR(25) DEFAULT NULL,
  `dtcreated` DATETIME DEFAULT NULL,
  `author_objid` VARCHAR(50) DEFAULT NULL,
  `author_name` VARCHAR(50) DEFAULT NULL,
  `code` VARCHAR(150) DEFAULT NULL,
  `name` VARCHAR(150) DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_code` (`code`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_txnstate` (`txnstate`)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `deposittype` (
  `objid` VARCHAR(50) NOT NULL,
  `txnstate` VARCHAR(25) DEFAULT NULL,
  `dtcreated` DATETIME DEFAULT NULL,
  `author_objid` VARCHAR(50) DEFAULT NULL,
  `author_name` VARCHAR(50) DEFAULT NULL,
  `code` VARCHAR(150) DEFAULT NULL,
  `name` VARCHAR(150) DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_code` (`code`),
  KEY `ix_txnstate` (`txnstate`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `loan_ledger_adjustment` (
  `objid` VARCHAR(50) NOT NULL,
  `txnstate` VARCHAR(25) DEFAULT NULL,
  `dtcreated` DATETIME DEFAULT NULL,
  `author_objid` VARCHAR(50) DEFAULT NULL,
  `author_name` VARCHAR(50) DEFAULT NULL,
  `ledgerid` VARCHAR(50) DEFAULT NULL,
  `borrower_objid` VARCHAR(50) DEFAULT NULL,
  `borrower_name` VARCHAR(50) DEFAULT NULL,
  `txndate` DATE DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT '0.00',
  `dtmodified` DATETIME DEFAULT NULL,
  `modifiedby_objid` VARCHAR(50) DEFAULT NULL,
  `modifiedby_name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_txnstate` (`txnstate`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_ledgerid` (`ledgerid`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_borrowerid` (`borrower_objid`),
  KEY `ix_borrowername` (`borrower_name`),
  KEY `ix_dtmodified` (`dtmodified`),
  KEY `ix_modifiedbyid` (`modifiedby_objid`),
  CONSTRAINT `fk_loan_ledger_adjustment` FOREIGN KEY (`ledgerid`) REFERENCES `loan_ledger` (`objid`)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `loan_ledger_proceeds` (
  `objid` VARCHAR(50) NOT NULL,
  `parentid` VARCHAR(50) DEFAULT NULL,
  `txnstate` VARCHAR(25) DEFAULT NULL COMMENT 'DRAFT,FOR_SELLING,SOLD',
  `txntype` VARCHAR(25) DEFAULT NULL COMMENT 'ONLINE,CAPTURE',
  `dtcreated` DATETIME DEFAULT NULL,
  `author_objid` VARCHAR(50) DEFAULT NULL,
  `author_name` VARCHAR(50) DEFAULT NULL,
  `description` TEXT,
  `borrower_objid` VARCHAR(50) DEFAULT NULL,
  `borrower_name` VARCHAR(50) DEFAULT NULL,
  `borrower_address` VARCHAR(255) DEFAULT NULL,
  `refid` VARCHAR(50) DEFAULT NULL,
  `refno` VARCHAR(25) DEFAULT NULL,
  `txndate` DATE DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT '0.00',
  `dtsold` DATETIME DEFAULT NULL,
  `soldby_objid` VARCHAR(50) DEFAULT NULL,
  `soldby_name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_txnstate` (`txnstate`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_refid` (`refid`),
  KEY `ix_dtsold` (`dtsold`),
  KEY `ix_soldbyid` (`soldby_objid`),
  KEY `fk_loan_ledger_proceeds` (`parentid`),
  KEY `ix_refno` (`refno`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_borrowerid` (`borrower_objid`),
  KEY `ix_borrowername` (`borrower_name`),
  KEY `ix_txntype` (`txntype`),
  CONSTRAINT `fk_loanledger_proceed_payment` FOREIGN KEY (`refid`) REFERENCES `loan_ledger_payment` (`objid`),
  CONSTRAINT `fk_loan_ledger_proceeds` FOREIGN KEY (`parentid`) REFERENCES `loan_ledger` (`objid`)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `ledger_detail_state_type` (
  `name` VARCHAR(40) NOT NULL,
  `level` SMALLINT(6) DEFAULT NULL,
  PRIMARY KEY  (`name`),
  KEY `ix_level` (`level`)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `collection_cb_encash` (
  `objid` VARCHAR(50) NOT NULL,
  PRIMARY KEY  (`objid`),
  CONSTRAINT `fk_collection_cb_encash` FOREIGN KEY (`objid`) REFERENCES `collection_cb` (`objid`)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `collection_cb_forencashment` (
  `objid` VARCHAR(50) NOT NULL,
  PRIMARY KEY  (`objid`),
  CONSTRAINT `fk_collection_cb_forencashment` FOREIGN KEY (`objid`) REFERENCES `collection_cb` (`objid`)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `collection_cb_reconciliation` (
  `objid` VARCHAR(50) NOT NULL,
  `state` VARCHAR(25) DEFAULT NULL COMMENT 'DRAFT,RECONCILED',
  `refid` VARCHAR(50) DEFAULT NULL,
  `dtcreated` DATETIME DEFAULT NULL,
  `author_objid` VARCHAR(50) DEFAULT NULL,
  `author_name` VARCHAR(50) DEFAULT NULL,
  `reason` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_state` (`state`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`),
  KEY `fk_collection_cb_reconciliation` (`refid`),
  CONSTRAINT `fk_collection_cb_reconciliation` FOREIGN KEY (`refid`) REFERENCES `collection_cb` (`objid`)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `collection_cb_reconcile` (
  `objid` VARCHAR(50) NOT NULL,
  `reconciliationid` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_reconciliationid` (`reconciliationid`),
  CONSTRAINT `fk_collection_cb_reconcile` FOREIGN KEY (`objid`) REFERENCES `collection_cb` (`objid`),
  CONSTRAINT `fk_reconcile_reconciliation` FOREIGN KEY (`reconciliationid`) REFERENCES `collection_cb_reconciliation` (`objid`)
) ENGINE=INNODB;

SET FOREIGN_KEY_CHECKS=1;

INSERT IGNORE INTO `sys_var` 
	(`name`, `value`, `description`, `datatype`, `category`)
VALUES 
	('allow_sunday_billing', 'false', 'Allow sunday billing', NULL, 'BRANCH'),
	('allow_holiday_billing', 'false', 'Allow holiday billing', NULL, 'BRANCH'),
	('enable_segregation_task', 'true', 'Enable segregation task', NULL, 'SYSTEM');


INSERT IGNORE INTO `sys_usergroup_permission` 
	(`objid`, `usergroup_objid`, `object`, `permission`, `title`) 
VALUES  
	('DATAMGMT_CURRENCYTYPE_C', 'LOAN_DATAMGMT_AUTHOR', 'currencytype', 'create', 'create'), 
	('DATAMGMT_CURRENCYTYPE_D', 'LOAN_DATAMGMT_AUTHOR', 'currencytype', 'delete', 'delete'), 
	('DATAMGMT_CURRENCYTYPE_R', 'LOAN_DATAMGMT_AUTHOR', 'currencytype', 'read', 'read'), 
	('DATAMGMT_CURRENCYTYPE_U', 'LOAN_DATAMGMT_AUTHOR', 'currencytype', 'edit', 'edit'), 
	('DATAMGMT_ACCOUNTTYPE_C', 'LOAN_DATAMGMT_AUTHOR', 'accounttype', 'create', 'create'), 
	('DATAMGMT_ACCOUNTTYPE_D', 'LOAN_DATAMGMT_AUTHOR', 'accounttype', 'delete', 'delete'), 
	('DATAMGMT_ACCOUNTTYPE_R', 'LOAN_DATAMGMT_AUTHOR', 'accounttype', 'read', 'read'), 
	('DATAMGMT_ACCOUNTTYPE_U', 'LOAN_DATAMGMT_AUTHOR', 'accounttype', 'edit', 'edit'), 
	('DATAMGMT_DEPOSITTYPE_C', 'LOAN_DATAMGMT_AUTHOR', 'deposittype', 'create', 'create'), 
	('DATAMGMT_DEPOSITTYPE_D', 'LOAN_DATAMGMT_AUTHOR', 'deposittype', 'delete', 'delete'), 
	('DATAMGMT_DEPOSITTYPE_R', 'LOAN_DATAMGMT_AUTHOR', 'deposittype', 'read', 'read'), 
	('DATAMGMT_DEPOSITTYPE_U', 'LOAN_DATAMGMT_AUTHOR', 'deposittype', 'edit', 'edit'); 


ALTER TABLE `depositslip`
ADD COLUMN `currencytype_objid` VARCHAR(50) AFTER `reftype`,
ADD COLUMN `currencytype_name` VARCHAR(150) AFTER `currencytype_objid`;

ALTER TABLE `depositslip`
ADD CONSTRAINT fk_depositslip_currencytpe FOREIGN KEY (`currencytype_objid`) REFERENCES `currencytype`(`objid`);

ALTER TABLE `depositslip`
  ADD COLUMN `accounttype_objid` VARCHAR(50) AFTER `currencytype_name`,
  ADD COLUMN `accounttype_name` VARCHAR(150) AFTER `accounttype_objid`;

ALTER TABLE `depositslip`
  ADD CONSTRAINT fk_depositslip_accounttype FOREIGN KEY (`accounttype_objid`) REFERENCES `accounttype`(`objid`);

ALTER TABLE `depositslip`
  ADD COLUMN `deposittype_objid` VARCHAR(50) AFTER `accounttype_name`,
  ADD COLUMN `deposittype_name` VARCHAR(150) AFTER `deposittype_objid`;

ALTER TABLE `depositslip`
  ADD CONSTRAINT fk_depositslip_deposittype FOREIGN KEY (`deposittype_objid`) REFERENCES `deposittype`(`objid`);

INSERT IGNORE INTO `sys_usergroup` (`objid`, `title`, `domain`, `role`, `userclass`, `orgclass`) 
VALUES 
('TREASURY_APPROVER', 'APPROVER', 'TREASURY', 'APPROVER', 'usergroup', NULL),
('LOAN_RULE_AUTHOR', 'RULE AUTHOR', 'LOAN', 'RULE_AUTHOR', 'usergroup', NULL),
('TREASURY_FLA_OFFICER', 'FLA OFFICER', 'TREASURY', 'FLA_OFFICER', 'usergroup', NULL);

ALTER TABLE `overage`
  ADD COLUMN `balance` DECIMAL(10,2) DEFAULT 0.00 AFTER `amount`;

UPDATE `overage` SET balance = amount
WHERE balance = 0;

ALTER TABLE `ledger_billing_detail`
  ADD COLUMN `dtreleased` DATE DEFAULT NULL AFTER `txndate`;

INSERT IGNORE INTO `ledger_detail_state_type`(`name`, `level`)
VALUES ('RECEIVED', 1), ('OFFSET', 2), ('ADJUSTMENT', 3), ('PROCEED', 4), ('AMNESTY', 5);

ALTER TABLE `loan_ledger_detail`
  ADD COLUMN `amnestyid` VARCHAR(50) DEFAULT NULL,
  ADD COLUMN `remarks` TEXT;

ALTER TABLE `loanapp_business` 
DROP INDEX `uix_tradename`;

ALTER TABLE `loanapp_business` 
ADD UNIQUE INDEX `uix_tradename_loanapp` (`tradename`, `parentid`);