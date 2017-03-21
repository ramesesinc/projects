ALTER TABLE market_account CHANGE COLUMN `paymentterm` `term` VARCHAR(50) NOT NULL;
ALTER TABLE market_account ADD COLUMN `lastledgerdate` DATE AFTER `term`;
ALTER TABLE market_rentalunit CHANGE COLUMN `paymentterm` `term` VARCHAR(50) NOT NULL;

CREATE TABLE `market_ledger` (
  `objid` varchar(50) NOT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `duedate` date DEFAULT NULL,
  `amount` decimal(16,2) DEFAULT NULL,
  `amtpaid` decimal(16,2) DEFAULT NULL,
  `surcharge` decimal(16,2) DEFAULT NULL,
  `interest` decimal(16,2) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `market_ledger_market_account` (`acctid`),
  CONSTRAINT `market_ledger_market_account` FOREIGN KEY (`acctid`) REFERENCES `market_account` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
