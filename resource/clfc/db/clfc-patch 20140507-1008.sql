USE clfc2;

DROP TABLE IF EXISTS `note_log`; 

INSERT IGNORE INTO sys_usergroup 
  (objid, title, domain, role, userclass, orgclass) 
VALUES 
  ('BRANCH_MANAGER', 'BRANCH MANAGER', 'LOAN', 'BRANCH_MANAGER', 'usergroup', NULL);  

ALTER TABLE `note` 
	CHANGE `createdby` `author_objid` VARCHAR(50) CHARSET latin1 COLLATE latin1_swedish_ci NULL, 
	ADD COLUMN `author_username` VARCHAR(50) NULL AFTER `author_objid`, 
	DROP INDEX `ix_dtcreatedby`, 
	ADD INDEX `ix_dtcreated` (`dtcreated`), 
	ADD INDEX `ix_author_objid` (`author_objid`), 
	ADD INDEX `ix_author_username` (`author_username`); 

ALTER TABLE `bank` 
	ADD COLUMN `txnstate` VARCHAR(25) NULL AFTER `objid`,  
	ADD COLUMN `dtcreated` DATETIME NULL AFTER `txnstate`, 
	ADD COLUMN `author_objid` VARCHAR(50) NULL AFTER `dtcreated`, 
	ADD COLUMN `author_username` VARCHAR(50) NULL AFTER `author_objid`, 
	ADD INDEX `ix_author_objid` (`author_objid`), 
	ADD INDEX `ix_author_username` (`author_username`); 

ALTER TABLE `bank` ENGINE=INNODB;

ALTER TABLE `txnlog` 
	CHANGE `remarks` `remarks` TEXT CHARSET latin1 COLLATE latin1_swedish_ci NULL; 

ALTER TABLE `exemptiontype` 
	ADD COLUMN `txnstate` VARCHAR(25) NULL AFTER `objid`; 
	