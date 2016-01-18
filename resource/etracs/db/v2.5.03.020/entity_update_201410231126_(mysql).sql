
ALTER TABLE `entity` CHANGE `entityname` `entityname` VARCHAR(255) NULL; 
ALTER TABLE `entity` ADD INDEX `ix_entityname` (`entityname`); 

ALTER TABLE `entityindividual` CHANGE `lastname` `lastname` VARCHAR(50) NOT NULL;
ALTER TABLE `entityindividual` CHANGE `firstname` `firstname` VARCHAR(50) NOT NULL; 
ALTER TABLE `entityindividual` CHANGE `middlename` `middlename` VARCHAR(50) NULL;
CREATE INDEX `ix_lfname` ON `entityindividual` (`lastname`, `firstname`); 
