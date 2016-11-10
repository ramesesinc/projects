ALTER TABLE businessrequirementtype ADD COLUMN `verifier` VARCHAR(255) NULL AFTER `sortindex`;

ALTER TABLE entityindividual CHANGE `lastname` `lastname` VARCHAR(100) NOT NULL;
ALTER TABLE entityindividual CHANGE `firstname` `firstname` VARCHAR(100) NOT NULL;
ALTER TABLE entityindividual CHANGE `middlename` `middlename` VARCHAR(100) NULL; 

CREATE INDEX `ix_entityindividual_lastname` ON `entityindividual` (`lastname`);
CREATE INDEX `ix_lfname` ON `entityindividual` (`lastname`,`firstname`);


ALTER TABLE collectiontype_account ADD COLUMN tag VARCHAR(50) NULL; 
ALTER TABLE collectiontype_account ADD COLUMN defaultvalue DECIMAL(16,2) NULL; 
ALTER TABLE collectiontype_account ADD COLUMN valuetype VARCHAR(20) NULL; 
ALTER TABLE collectiontype_account ADD COLUMN sortorder INT(11) NULL; 
