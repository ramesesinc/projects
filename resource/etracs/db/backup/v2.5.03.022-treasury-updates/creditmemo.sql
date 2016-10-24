
CREATE TABLE `cashreceiptpayment_creditmemo` (
  `objid` VARCHAR(50) NOT NULL,
  `receiptid` VARCHAR(50) DEFAULT NULL,
  `account_objid` VARCHAR(50) DEFAULT NULL,
  `account_code` VARCHAR(100) DEFAULT NULL,
  `account_fund_name` VARCHAR(50) DEFAULT NULL,
  `account_fund_objid` VARCHAR(50) DEFAULT NULL,
  `account_bank` VARCHAR(50) DEFAULT NULL,
  `refno` VARCHAR(25) DEFAULT NULL,
  `refdate` DATETIME DEFAULT NULL,
  `amount` DECIMAL(16,2) DEFAULT NULL,
  `particulars` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `FK_cashreceiptpayment_creditmemo` (`receiptid`)
) ENGINE=INNODB; 

CREATE TABLE `remittance_creditmemopayment` (
  `objid` VARCHAR(50) NOT NULL,
  `remittanceid` VARCHAR(50) DEFAULT NULL,
  `amount` DECIMAL(16,2) DEFAULT NULL,
  `voided` INT(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `FK_remittance_creditmemo_remittance` (`remittanceid`),
  CONSTRAINT `fk_remittance_creditmemo_creditmemopayment` FOREIGN KEY (`objid`) REFERENCES `cashreceiptpayment_creditmemo` (`objid`)
) ENGINE=INNODB ;

CREATE TABLE `liquidation_creditmemopayment` (
  `objid` VARCHAR(50) NOT NULL,
  `liquidationid` VARCHAR(50) NOT NULL,
  `liquidationfundid` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=INNODB ;

CREATE TABLE `bankdeposit_entry_creditmemo` (
  `objid` VARCHAR(50) NOT NULL,
  `parentid` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `FK_bankdeposit_creditmemo` (`parentid`)
) ENGINE=INNODB;