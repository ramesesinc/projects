ALTER TABLE business_redflag 
ADD COLUMN `blockaction` VARCHAR(50) NULL, 
ADD COLUMN `effectivedate` DATE NULL, 
ADD COLUMN `resolvedby_objid` VARCHAR(50) NULL, 
ADD COLUMN `resolvedby_name` VARCHAR(100),
ADD COLUMN `dtresolved` DATETIME,
CHANGE `dtposted` `dtfiled` DATETIME NULL, 
CHANGE `postedby_objid` `filedby_objid` VARCHAR(50) NULL, 
CHANGE `postedby_name` `filedby_name` VARCHAR(255) NULL, 
ADD COLUMN `caseno` VARCHAR(50) NULL; 

ALTER TABLE business_redflag ADD UNIQUE INDEX `uix_caseno` (`caseno`);
