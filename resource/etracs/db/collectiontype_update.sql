
CREATE TABLE `collectiontype` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(10) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `formno` varchar(10) DEFAULT NULL,
  `handler` varchar(25) DEFAULT NULL,
  `allowbatch` int(11) DEFAULT NULL,
  `barcodekey` varchar(100) DEFAULT NULL,
  `allowonline` int(11) DEFAULT '1',
  `allowoffline` int(11) DEFAULT NULL,
  `sortorder` int(11) DEFAULT '0',
  `org_objid` varchar(50) DEFAULT NULL,
  `org_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `collectiontype_account` (
  `collectiontypeid` varchar(50) NOT NULL,
  `account_objid` varchar(50) NOT NULL,
  `account_title` varchar(100) DEFAULT NULL,
  `tag` varchar(50) DEFAULT NULL,
  `defaultvalue` decimal(16,2) DEFAULT NULL,
  `valuetype` varchar(20) DEFAULT NULL,
  `sortorder` int(11) DEFAULT NULL,
  PRIMARY KEY (`collectiontypeid`,`account_objid`),
  KEY `fk_collectiontype_account_revitem` (`account_objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1; 

ALTER TABLE `collectiontype_account`
  ADD CONSTRAINT `fk_collectiontype_account_collectiontype` FOREIGN KEY (`collectiontypeid`) REFERENCES `collectiontype` (`objid`); 
ALTER TABLE `collectiontype_account`
  ADD CONSTRAINT `fk_collectiontype_account_revitem` FOREIGN KEY (`account_objid`) REFERENCES `revenueitem` (`objid`);


CREATE TABLE `collectiontype_fund` (
  `collectiontypeid` varchar(50) NOT NULL,
  `fund_objid` varchar(50) NOT NULL,
  `fund_title` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`collectiontypeid`,`fund_objid`),
  KEY `fk_collectiontype_fund_fund` (`fund_objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `collectiontype_fund` 
  ADD CONSTRAINT `fk_collectiontype_fund_collectiontype` FOREIGN KEY (`collectiontypeid`) REFERENCES `collectiontype` (`objid`); 
ALTER TABLE `collectiontype_fund`   
  ADD CONSTRAINT `fk_collectiontype_fund_fund` FOREIGN KEY (`fund_objid`) REFERENCES `fund` (`objid`)


