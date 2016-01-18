USE clfc2;

ALTER TABLE entityindividual 
	DROP COLUMN `tin`, 
	DROP COLUMN `sss`, 
	DROP COLUMN `height`, 
	DROP COLUMN `weight`, 
	ADD COLUMN `religion` VARCHAR(50) NULL AFTER `profession`; 

CREATE TABLE IF NOT EXISTS entityindividual_physical ( 
	`objid` VARCHAR(50) NOT NULL, 
	`height` VARCHAR(25), 
	`weight` VARCHAR(25), 
	`eyecolor` VARCHAR(25), 
	`haircolor` VARCHAR(25), 
	`photo_objid` VARCHAR(50), 
	`fingerprint_objid` VARCHAR(50), 
	`signature_objid` VARCHAR(50), 
	PRIMARY KEY (`objid`) 
); 	

ALTER TABLE entityindividual_physical 
	ADD INDEX `ix_photo_objid` (`photo_objid`), 
	ADD INDEX `ix_fingerprint_objid` (`fingerprint_objid`), 
	ADD INDEX `ix_signature_objid` (`signature_objid`); 

CREATE TABLE IF NOT EXISTS entity_photo ( 
	`objid` VARCHAR(50) NOT NULL, 
	`dtfiled` DATETIME, 
	`entityid` VARCHAR(50), 
	`image` MEDIUMBLOB, 
	PRIMARY KEY (`objid`), 
	INDEX `ix_dtfiled` (`dtfiled`), 
	INDEX `ix_entityid` (`entityid`) 
); 

CREATE TABLE IF NOT EXISTS entity_fingerprint ( 
	`objid` VARCHAR(50) NOT NULL, 
	`dtfiled` DATETIME, 
	`entityid` VARCHAR(50), 
	`leftthumb_image` MEDIUMBLOB, 
	`leftthumb_fmd` MEDIUMBLOB, 
	`rightthumb_image` MEDIUMBLOB, 
	`rightthumb_fmd` MEDIUMBLOB, 
	PRIMARY KEY (`objid`), 
	INDEX `ix_dtfiled` (`dtfiled`), 
	INDEX `ix_entityid` (`entityid`) 
); 

CREATE TABLE IF NOT EXISTS entityid ( 
	`objid` VARCHAR(50) NOT NULL, 
	`entityid` VARCHAR(50), 
	`idtype` VARCHAR(50), 
	`idno` VARCHAR(50), 
	`dtissued` DATE, 
	`dtexpiry` DATE, 
	PRIMARY KEY (`objid`), 
	INDEX `ix_entityid` (`entityid`), 
	INDEX `ix_idtype` (`idtype`), 
	INDEX `ix_idno` (`idno`), 
	INDEX `ix_dtissued` (`dtissued`), 
	INDEX `ix_dtexpiry` (`dtexpiry`) 
); 

ALTER TABLE entity 
	ADD COLUMN `entityname` VARCHAR(160) NULL AFTER `entityno`, 
	ADD INDEX `ix_entityname` (`entityname`); 

UPDATE entity SET entityname=`name` WHERE entityname IS NULL; 

 