#254032-03014

CREATE TABLE `payment_partner` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`)
); 

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
  PRIMARY KEY (`objid`),
  KEY `ix_receiptid` (`receiptid`),
  KEY `ix_account_objid` (`partner_objid`),
  KEY `ix_refno` (`refno`),
  KEY `ix_refdate` (`refdate`),
  CONSTRAINT `fk_payment_partner_eor` FOREIGN KEY (`partner_objid`) REFERENCES `payment_partner` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO payment_partner SELECT 'DBP', 'DEVELOPMENT BANK OF THE PHILIPPINES';
INSERT INTO payment_partner SELECT 'LBP', 'LAND BANK OF THE PHILIPPINES';

 INSERT INTO af ( objid, title, usetype, serieslength,  system,  denomination,  formtype)
 VALUES ('EOR', 'EOR', 'collection', 12,  1, 0.00,  'serial' );



INSERT INTO `sys_user` (`objid`, `state`, `dtcreated`, `createdby`, `username`, `pwd`, `firstname`, `lastname`, `middlename`, `name`, `jobtitle`, `pwdlogincount`, `pwdexpirydate`, `usedpwds`, `lockid`, `txncode`) VALUES ('EOR', NULL, NULL, NULL, 'EOR', NULL, 'EOR', 'EOR', NULL, 'EOR', 'EOR', '0', NULL, NULL, '', '');



drop table if exists eor_paymentorder;
drop table if exists paymentorder;

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
  PRIMARY KEY (`txnid`)
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
