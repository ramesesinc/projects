
ALTER TABLE `business_requirement` ADD COLUMN `expirydate` DATE NULL; 
ALTER TABLE `businessrequirementtype` ADD COLUMN `agency` VARCHAR(50);
ALTER TABLE `businessrequirementtype` ADD COLUMN `sortindex` INT NULL;  

