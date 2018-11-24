ALTER TABLE waterworks_account_ledger ADD COLUMN `month` int;
ALTER TABLE waterworks_account_ledger ADD COLUMN `year` int;
ALTER TABLE waterworks_account_ledger ADD COLUMN `surcharge` decimal(16,2);
ALTER TABLE waterworks_account_ledger ADD COLUMN `interest` decimal(16,2);

ALTER TABLE `waterworks4`.`waterworks_account_ledger` ADD UNIQUE INDEX `uix_parentid_year_month_itemid` (`parentid`, `item_objid`, `month`, `year`); 


ALTER TABLE waterworks_account_consumption ADD COLUMN `rate` decimal(16,2);
ALTER TABLE waterworks_account_consumption ADD COLUMN `ledgerid` varchar(50);

ALTER TABLE waterworks_payment ADD COLUMN `txnmode` varchar(50);


