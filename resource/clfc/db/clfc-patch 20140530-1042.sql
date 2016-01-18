USE clfc2;

CREATE TABLE IF NOT EXISTS `entity_address` ( 
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `type` varchar(50) default NULL,
  `addresstype` varchar(50) default NULL,
  `barangay_objid` varchar(50) default NULL,
  `barangay_name` varchar(150) default NULL,
  `city` varchar(50) default NULL,
  `municipality` varchar(50) default NULL,
  `province` varchar(50) default NULL,
  `bldgno` varchar(50) default NULL,
  `bldgname` varchar(50) default NULL,
  `unitno` varchar(25) default NULL,
  `street` varchar(50) default NULL,
  `subdivision` varchar(50) default NULL,
  `pin` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_parentid` (`parentid`),
  KEY `ix_barangay_objid` (`barangay_objid`),
  KEY `ix_barangay_name` (`barangay_name`),
  KEY `ix_city` (`city`),
  KEY `ix_municipality` (`municipality`),
  KEY `ix_province` (`province`),
  KEY `ix_pin` (`pin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `entity` 
	ADD COLUMN `address_objid` VARCHAR(50) NULL AFTER `name`, 
	CHANGE `address` `address_text` VARCHAR(255) CHARSET utf8 COLLATE utf8_general_ci NOT NULL, 
	ADD COLUMN `mobileno` VARCHAR(50) NULL AFTER `remarks`, 
	ADD COLUMN `phoneno` VARCHAR(50) NULL AFTER `mobileno`, 
	ADD COLUMN `email` VARCHAR(50) NULL AFTER `phoneno`, 
	ADD INDEX `ix_address_objid` (`address_objid`); 

ALTER TABLE `entityindividual` 
	DROP COLUMN `mobileno`, 
	DROP COLUMN `phoneno`, 
	DROP COLUMN `email`, 
	CHANGE `profession` `profession` VARCHAR(150) CHARSET utf8 COLLATE utf8_general_ci NULL, 
	ADD COLUMN `tin` VARCHAR(50) NULL AFTER `thumbnail`, 
	ADD COLUMN `sss` VARCHAR(50) NULL AFTER `tin`, 
	ADD INDEX `ix_tin` (`tin`), 
	ADD INDEX `ix_sss` (`sss`); 

ALTER TABLE `customer` 
	CHANGE `address` `address_text` VARCHAR(255) CHARSET latin1 COLLATE latin1_swedish_ci NULL; 


	