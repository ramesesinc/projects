drop table if exists `cashreceiptpayment_eor`;
drop table if exists `eor_paymentorder`;
drop table if exists `paymentorder_item`;
drop table if exists `paymentorder_type`;
drop table if exists `paymentorder`;
drop table if exists `payment_partner`;


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



CREATE TABLE `paymentorder_type` (
  `objid` varchar(50) NOT NULL,
  `title` varchar(150) DEFAULT NULL,
  `collectiontype_objid` varchar(50) DEFAULT NULL,
  `queuesection` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_paymentorder_type_collectiontype` (`collectiontype_objid`),
  CONSTRAINT `fk_paymentorder_type_collectiontype` FOREIGN KEY (`collectiontype_objid`) REFERENCES `collectiontype` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `payment_partner` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `eor_paymentorder` (
  `objid` varchar(50) NOT NULL,
  `traceno` varchar(50) DEFAULT NULL,
  `tracedate` datetime DEFAULT NULL,
  `dtposted` datetime DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `paymentorder_txnid` varchar(50) NOT NULL DEFAULT '',
  `paymentorder_txndate` datetime DEFAULT NULL,
  `paymentorder_payer_objid` varchar(50) DEFAULT NULL,
  `paymentorder_payer_name` text,
  `paymentorder_paidby` text,
  `paymentorder_paidbyaddress` varchar(150) DEFAULT NULL,
  `paymentorder_particulars` varchar(500) DEFAULT NULL,
  `paymentorder_amount` decimal(16,2) DEFAULT NULL,
  `paymentorder_txntypeid` varchar(50) DEFAULT NULL,
  `paymentorder_expirydate` date DEFAULT NULL,
  `paymentorder_refid` varchar(50) DEFAULT NULL,
  `paymentorder_refno` varchar(50) DEFAULT NULL,
  `paymentorder_info` text,
  PRIMARY KEY (`objid`),
  KEY `ix_traceno` (`traceno`),
  KEY `ix_paymentorder_txnid` (`paymentorder_txnid`),
  KEY `ix_paymentorder_refid` (`paymentorder_refid`),
  KEY `ix_paymentorder_refno` (`paymentorder_refno`),
  KEY `ix_paymentorder_txntypeid` (`paymentorder_txntypeid`)
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


INSERT INTO `payment_partner` (`objid`, `name`) VALUES ('DBP', 'DEVELOPMENT BANK OF THE PHILIPPINES');
INSERT INTO `payment_partner` (`objid`, `name`) VALUES ('LBP', 'LAND BANK OF THE PHILIPPINES');


INSERT INTO `paymentorder_type` (`objid`, `title`, `collectiontype_objid`, `queuesection`) VALUES ('rptcol', 'Real Property Tax', 'RPT_COL', NULL);

 INSERT INTO af ( objid, title, usetype, serieslength,  system,  denomination,  formtype)
 VALUES ('EOR', 'EOR', 'collection', 12,  1, 0.00,  'serial' );


