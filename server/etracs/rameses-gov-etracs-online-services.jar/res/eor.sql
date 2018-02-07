CREATE TABLE `eor` (
  `objid` varchar(50) CHARACTER SET utf8 NOT NULL,
  `receiptno` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `receiptdate` date DEFAULT NULL,
  `txndate` datetime DEFAULT NULL,
  `state` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `partnerid` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `txntype` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `traceid` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `tracedate` datetime DEFAULT NULL,
  `refid` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `paidby` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `paidbyaddress` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `payer_objid` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `paymethod` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `paymentrefid` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `remittanceid` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `remarks` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_eor_receiptno` (`receiptno`)
) ENGINE=InnoDB ;


CREATE TABLE `eor_item` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `item_objid` varchar(50) DEFAULT NULL,
  `item_code` varchar(100) DEFAULT NULL,
  `item_title` varchar(100) DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `item_fund_objid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_eoritem_eor` (`parentid`),
  CONSTRAINT `fk_eoritem_eor` FOREIGN KEY (`parentid`) REFERENCES `eor` (`objid`)
) ENGINE=InnoDB;

eor_share CREATE TABLE `eor_share` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `itemaccount_objid` varchar(50) DEFAULT NULL,
  `itemaccount_code` varchar(50) DEFAULT NULL,
  `itemaccount_title` varchar(100) DEFAULT NULL,
  `shareaccount_objid` varchar(50) DEFAULT NULL,
  `shareaccount_code` varchar(50) DEFAULT NULL,
  `shareaccount_title` varchar(100) DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `share` decimal(16,2) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_eorshare_eor` (`parentid`),
  CONSTRAINT `fk_eorshare_eor` FOREIGN KEY (`parentid`) REFERENCES `eor_share` (`objid`)
) ENGINE=InnoDB; 
