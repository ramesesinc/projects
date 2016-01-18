CREATE TABLE `income_summary` (
  `refid` VARCHAR(50) NOT NULL,
  `refdate` DATE DEFAULT NULL,
  `acctid` VARCHAR(50) NOT NULL,
  `fundid` VARCHAR(50) DEFAULT NULL,
  `amount` DECIMAL(16,2) DEFAULT NULL,
  `refno` VARCHAR(50) DEFAULT NULL,
  `reftype` VARCHAR(50) DEFAULT NULL,
  `orgid` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`refid`,`acctid`),
  KEY `ix_income_summary_refid` (`refid`)
) ENGINE=INNODB ;