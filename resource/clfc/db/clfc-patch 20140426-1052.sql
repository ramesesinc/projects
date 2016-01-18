USE clfc2;

CREATE TABLE `mobile_tracker_route` (
  `objid` VARCHAR(40) NOT NULL,
  `parentid` VARCHAR(40) NOT NULL,
  `routecode` VARCHAR(40) NOT NULL,
  PRIMARY KEY  (`objid`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

ALTER TABLE `field_collection_route`
ADD COLUMN `posted` SMALLINT(3) DEFAULT 0;

ALTER TABLE `field_collection_route`
ADD INDEX `ix_posted`(`posted`);

ALTER TABLE `field_collection_payment`
ADD COLUMN `payoption` VARCHAR(15) COMMENT 'cash,check',
ADD COLUMN `bank_objid` VARCHAR(40),
ADD COLUMN `bank_name` VARCHAR(40),
ADD COLUMN `check_date` DATE,
ADD COLUMN `check_no` VARCHAR(40);

ALTER TABLE `field_collection_payment`
ADD INDEX `ix_payoption`(`payoption`);

ALTER TABLE `field_collection_payment`
ADD INDEX `ix_bankid`(`bank_objid`);

ALTER TABLE `field_collection_payment`
ADD INDEX `ix_checkdate`(`check_date`);

ALTER TABLE `field_collection_payment`
ADD INDEX `ix_checkno`(`check_no`);

ALTER TABLE `special_collection`
ADD COLUMN `posted` SMALLINT(3) DEFAULT 0;

ALTER TABLE `special_collection`
ADD INDEX `ix_posted`(`posted`);