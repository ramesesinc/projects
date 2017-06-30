
DROP TABLE if exists `paymentorder_item`;
DROP TABLE if exists `paymentorder`;

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
  `controlno` varchar(50) DEFAULT NULL,
  `locationid` varchar(25) DEFAULT NULL,
  `receiptid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`txnid`),
  KEY `FK_paymentorder_cashreceipt` (`receiptid`),
  CONSTRAINT `FK_paymentorder_cashreceipt` FOREIGN KEY (`receiptid`) REFERENCES `cashreceipt` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


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



CREATE TABLE `cashreceiptpayment_eor` (
  `objid` varchar(50) NOT NULL,
  `dtposted` datetime NOT NULL,
  `receiptid` varchar(50) DEFAULT NULL,
  `partner_objid` varchar(50) DEFAULT NULL,
  `txnrefid` varchar(50) DEFAULT NULL,
  `txnreftype` varchar(50) DEFAULT NULL,
  `refno` varchar(100) DEFAULT NULL,
  `refdate` datetime DEFAULT NULL,
  `amount` decimal(16,2) DEFAULT NULL,
  `txntype` varchar(50) DEFAULT NULL,
  `particulars` varchar(255) DEFAULT NULL,
  `controlno` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_receiptid` (`receiptid`),
  KEY `ix_account_objid` (`partner_objid`),
  KEY `ix_refno` (`refno`),
  KEY `ix_refdate` (`refdate`),
  CONSTRAINT `fk_payment_partner_eor` FOREIGN KEY (`partner_objid`) REFERENCES `payment_partner` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;