
CREATE TABLE `cashreceiptpayment_noncash` (
  `objid` VARCHAR(50) NOT NULL,
  `receiptid` VARCHAR(50) DEFAULT NULL,
  `bank` VARCHAR(50) DEFAULT NULL,
  `refno` VARCHAR(25) DEFAULT NULL,
  `refdate` DATETIME DEFAULT NULL,
  `reftype` VARCHAR(50) DEFAULT NULL,
  `amount` DECIMAL(16,2) DEFAULT NULL,
  `particulars` VARCHAR(255) DEFAULT NULL,
  `bankid` VARCHAR(50) DEFAULT NULL,
  `deposittype` VARCHAR(50) DEFAULT NULL,
  `account_objid` VARCHAR(50) DEFAULT NULL,
  `account_code` VARCHAR(50) DEFAULT NULL,
  `account_name` VARCHAR(100) DEFAULT NULL,
  `account_fund_objid` VARCHAR(50) DEFAULT NULL,
  `account_fund_name` VARCHAR(50) DEFAULT NULL,
  `account_bank` VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `bankid` (`bankid`),
  KEY `FK_cashreceiptpayment_check` (`receiptid`),
  CONSTRAINT `cashreceiptpayment_noncash_ibfk_1` FOREIGN KEY (`receiptid`) REFERENCES `cashreceipt` (`objid`),
  CONSTRAINT `cashreceiptpayment_noncash_ibfk_2` FOREIGN KEY (`bankid`) REFERENCES `bank` (`objid`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


insert into cashreceiptpayment_noncash
( 
	objid, receiptid, bank, refno, refdate, reftype,
	amount, particulars, bankid, deposittype
)
select 
	objid, receiptid, bank, checkno, checkdate, 'CHECK'  as reftype,
	amount, particulars, bankid, deposittype
from cashreceiptpayment_check;

Alter table bankdeposit_entry_check drop foreign key bankdeposit_entry_check_ibfk_2;
Alter table liquidation_checkpayment drop foreign key liquidation_noncashpayment_ibfk_1;
drop table cashreceiptpayment_check;

RENAME TABLE remittance_checkpayment TO remittance_noncashpayment;
RENAME TABLE liquidation_checkpayment TO liquidation_noncashpayment;
