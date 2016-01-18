ALTER TABLE collectiontype ADD COLUMN `fund_objid` VARCHAR(50) NULL;
ALTER TABLE collectiontype ADD COLUMN `fund_title` VARCHAR(100) NULL; 

DROP TABLE collectiontype_fund;

