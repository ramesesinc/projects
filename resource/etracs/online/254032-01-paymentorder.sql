drop TABLE if exists `paymentorder`;

CREATE TABLE `paymentorder` (
  `txnid` varchar(50) NOT NULL DEFAULT '',
  `txndate` datetime DEFAULT NULL,
  `payer_objid` varchar(50) DEFAULT NULL,
  `payer_name` text,
  `paidby` text,
  `paidbyaddress` varchar(150) DEFAULT NULL,
  `particulars` varchar(500) DEFAULT NULL,
  `amount` decimal(16,2) DEFAULT NULL,
  `txntypeid` varchar(50) DEFAULT NULL,
  `expirydate` date DEFAULT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `refno` varchar(50) DEFAULT NULL,
  `info` text,
  `origin` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`txnid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


drop table if exists `paymentorder_item`;

CREATE TABLE `paymentorder_item` (
  `objid` varchar(50) NOT NULL,
  `parent_objid` varchar(50) DEFAULT NULL,
  `item_objid` varchar(50) DEFAULT NULL,
  `item_code` varchar(100) DEFAULT NULL,
  `item_title` varchar(100) DEFAULT NULL,
  `amount` decimal(16,4) NOT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `FK_paymentorderitem_paymentorder` (`parent_objid`),
  KEY `FK_paymentorderitem_itemaccount` (`item_objid`),
  CONSTRAINT `FK_paymentorderitem_itemaccount` FOREIGN KEY (`item_objid`) REFERENCES `itemaccount` (`objid`),
  CONSTRAINT `FK_paymentorderitem_paymentorder` FOREIGN KEY (`parent_objid`) REFERENCES `paymentorder` (`txnid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

