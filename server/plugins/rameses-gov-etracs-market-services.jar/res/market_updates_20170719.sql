ALTER TABLE `marketdb`.`market_account` 
DROP COLUMN `extrate`, 
DROP COLUMN `totalprincipalpaid`, 
DROP COLUMN `partialextbalance`,
DROP COLUMN duedate,
ADD COLUMN `unitno` VARCHAR(100) , 
ADD COLUMN `sectionid` VARCHAR(50) NULL , 
ADD COLUMN `rate` DECIMAL(16,2) NULL,
ADD COLUMN ratetype VARCHAR(50),
ADD COLUMN clusterid VARCHAR(50); 


UPDATE market_account m, market_rentalunit mru
SET m.unitno=mru.code, m.sectionid=mru.sectionid, m.rate=mru.rate, m.ratetype=mru.`ratetype`,
m.clusterid=mru.`clusterid` 
WHERE m.unit_objid=mru.objid; 

CREATE TABLE market_account_unit( 
  `objid` VARCHAR(50) NOT NULL, 
  `acctid` VARCHAR(50), 
  `unit_objid` VARCHAR(50), 
  `usage` INT, 
PRIMARY KEY (`objid`) );

INSERT INTO market_account_unit 
SELECT CONCAT( acctno, '-', unit_objid), objid, unit_objid,100 FROM market_account 

ALTER TABLE market_account DROP COLUMN unit_objid;

ALTER TABLE market_rentalunit DROP COLUMN currentacctid;
UPDATE market_rentalunit SET `usage`=100;


ALTER TABLE market_account
ADD COLUMN `business_objid` VARCHAR(50), 
ADD COLUMN `business_bin` VARCHAR(50) NULL, 
ADD COLUMN `business_tradename` VARCHAR(255) NULL, 
ADD COLUMN `business_ownername` VARCHAR(255) NULL; 