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
