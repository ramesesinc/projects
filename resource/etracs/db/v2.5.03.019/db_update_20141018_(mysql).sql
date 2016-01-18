ALTER TABLE `collectiontype` CHANGE `allowpos` `allowonline` INT(11) DEFAULT 1 NULL;
ALTER TABLE `collectiontype` ADD COLUMN `allowoffline` INT NULL AFTER `allowonline`; 
ALTER TABLE `collectiontype` ADD COLUMN `org_objid` VARCHAR(50) NULL AFTER `sortorder`; 
ALTER TABLE `collectiontype` ADD COLUMN `org_name` VARCHAR(50) NULL AFTER `org_objid`; 
CREATE INDEX `ix_name` ON `collectiontype` (`name`);
CREATE INDEX `ix_org_objid` ON `collectiontype` (`org_objid`); 

UPDATE collectiontype SET allowoffline=1 WHERE `name` IN ('REAL_PROPERTY_TAX','GENERAL_COLLECTION','BUSINESS_COLLECTION');


CREATE TABLE `collectiontype_account` (
  `collectiontypeid` VARCHAR(50) NOT NULL,
  `account_objid` VARCHAR(50) NOT NULL,
  `account_title` VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`collectiontypeid`,`account_objid`),
  KEY `fk_collectiontype_account_revitem` (`account_objid`),
  KEY `fk_collectiontype_account_collectiontype` (`collectiontypeid`) 
) ENGINE=INNODB DEFAULT CHARSET=latin1; 

ALTER TABLE `collectiontype_account` 
	ADD CONSTRAINT `fk_collectiontype_account_revitem` FOREIGN KEY (`account_objid`) REFERENCES `revenueitem` (`objid`); 
ALTER TABLE `collectiontype_account` 
	ADD CONSTRAINT `fk_collectiontype_account_collectiontype` FOREIGN KEY (`collectiontypeid`) REFERENCES `collectiontype` (`objid`); 
