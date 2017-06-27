SET FOREIGN_KEY_CHECKS=0; 

CREATE TABLE IF NOT EXISTS `ngas_revenue` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) NOT NULL,
  `type` varchar(50) NOT NULL,
  `txndate` datetime NOT NULL,
  `dtposted` datetime NOT NULL,
  `postedby_objid` varchar(50) NOT NULL,
  `postedby_name` varchar(100) NOT NULL,
  `postedby_title` varchar(50) NOT NULL,
  `amount` decimal(16,2) NOT NULL,
  PRIMARY KEY  (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `ngas_revenue_deposit` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) NOT NULL,
  PRIMARY KEY  (`objid`),
  KEY `parentid` (`parentid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `ngas_revenue_mapping` (
  `objid` varchar(50) NOT NULL,
  `version` varchar(10) default NULL,
  `revenueitemid` varchar(50) character set latin1 default NULL,
  `acctid` varchar(50) NOT NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_revenueitem_ngasacctid` (`version`,`revenueitemid`,`acctid`),
  KEY `fk_revenue_mapping_ngasaccount` (`acctid`),
  KEY `fk_revenue_mapping_revenueitemid` (`revenueitemid`) 
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `ngas_revenue_remittance` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) NOT NULL,
  PRIMARY KEY  (`objid`),
  KEY `parentid` (`parentid`) 
) ENGINE=InnoDB DEFAULT CHARSET=latin1; 


CREATE TABLE IF NOT EXISTS `ngas_revenueitem` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) NOT NULL,
  `refid` varchar(50) NOT NULL,
  `refsource` varchar(50) NOT NULL,
  `refno` varchar(25) NOT NULL,
  `refdate` datetime NOT NULL,
  `item_objid` varchar(50) NOT NULL,
  `fund_objid` varchar(50) NOT NULL,
  `acct_objid` varchar(50) default NULL,
  `subacct_objid` varchar(50) default NULL,
  `collectiontype_objid` varchar(50) NOT NULL,
  `amount` decimal(16,2) NOT NULL,
  `remittanceid` varchar(50) default NULL,
  `liquidationid` varchar(50) default NULL,
  `depositid` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `collectiontype_objid` (`collectiontype_objid`),
  KEY `depositid` (`depositid`),
  KEY `fund_objid` (`fund_objid`),
  KEY `liquidationid` (`liquidationid`),
  KEY `parentid` (`parentid`),
  KEY `item_objid` (`item_objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `ngasaccount` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `state` varchar(10) default NULL,
  `chartid` varchar(50) default NULL,
  `code` varchar(50) default NULL,
  `title` varchar(255) default NULL,
  `type` varchar(20) default NULL,
  `acctgroup` varchar(50) default NULL,
  `target` decimal(12,2) default NULL,
  PRIMARY KEY  (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `sre_revenue_mapping` (
  `objid` varchar(50) NOT NULL,
  `version` varchar(10) default NULL,
  `revenueitemid` varchar(50) NOT NULL,
  `acctid` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1; 


CREATE TABLE IF NOT EXISTS `sreaccount` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `state` varchar(10) default NULL,
  `chartid` varchar(50) default NULL,
  `code` varchar(50) default NULL,
  `title` varchar(255) default NULL,
  `type` varchar(20) default NULL,
  `acctgroup` varchar(50) default NULL,
  PRIMARY KEY  (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `sreaccount_incometarget` (
  `objid` varchar(50) NOT NULL,
  `year` int(11) NOT NULL,
  `target` decimal(18,2) default NULL,
  PRIMARY KEY  (`objid`,`year`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


ALTER TABLE `ngas_revenue_deposit` 
  ADD CONSTRAINT `ngas_revenue_deposit_ibfk_1` FOREIGN KEY (`objid`) REFERENCES `bankdeposit` (`objid`); 
ALTER TABLE `ngas_revenue_deposit` 
  ADD CONSTRAINT `ngas_revenue_deposit_ibfk_2` FOREIGN KEY (`parentid`) REFERENCES `ngas_revenue` (`objid`);

ALTER TABLE `ngas_revenue_mapping` 
  ADD CONSTRAINT `fk_revenue_mapping_ngasaccount` FOREIGN KEY (`acctid`) REFERENCES `ngasaccount` (`objid`); 
ALTER TABLE `ngas_revenue_mapping` 
  ADD CONSTRAINT `fk_revenue_mapping_revenueitemid` FOREIGN KEY (`revenueitemid`) REFERENCES `revenueitem` (`objid`); 
  
ALTER TABLE `ngas_revenue_remittance` 
  ADD CONSTRAINT `ngas_revenue_remittance_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `ngas_revenue` (`objid`); 

ALTER TABLE `ngas_revenueitem` 
  ADD CONSTRAINT `ngas_revenueitem_ibfk_1` FOREIGN KEY (`collectiontype_objid`) REFERENCES `collectiontype` (`objid`); 
ALTER TABLE `ngas_revenueitem` 
  ADD CONSTRAINT `ngas_revenueitem_ibfk_2` FOREIGN KEY (`depositid`) REFERENCES `bankdeposit` (`objid`); 
ALTER TABLE `ngas_revenueitem` 
  ADD CONSTRAINT `ngas_revenueitem_ibfk_3` FOREIGN KEY (`fund_objid`) REFERENCES `fund` (`objid`); 
ALTER TABLE `ngas_revenueitem` 
  ADD CONSTRAINT `ngas_revenueitem_ibfk_4` FOREIGN KEY (`liquidationid`) REFERENCES `liquidation` (`objid`); 
ALTER TABLE `ngas_revenueitem` 
  ADD CONSTRAINT `ngas_revenueitem_ibfk_5` FOREIGN KEY (`parentid`) REFERENCES `ngas_revenue` (`objid`); 
ALTER TABLE `ngas_revenueitem` 
  ADD CONSTRAINT `ngas_revenueitem_ibfk_6` FOREIGN KEY (`item_objid`) REFERENCES `revenueitem` (`objid`); 


SET FOREIGN_KEY_CHECKS=1; 

