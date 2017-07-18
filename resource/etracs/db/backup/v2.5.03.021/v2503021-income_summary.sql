

CREATE TABLE `income_summary` (
  `liquidationno` VARCHAR(50) DEFAULT NULL,
  `remittanceno` VARCHAR(50) NOT NULL,
  `receiptdate` DATE NOT NULL,
  `acctid` VARCHAR(50) NOT NULL,
  `fundid` VARCHAR(50) NOT NULL,
  `liquidationdate` DATE DEFAULT NULL,
  `remittancedate` DATE DEFAULT NULL,
  `amount` DECIMAL(16,2) DEFAULT NULL,
  `reftype` VARCHAR(50) DEFAULT NULL,
  `refid` VARCHAR(50) DEFAULT NULL,
  `orgid` VARCHAR(50) DEFAULT NULL,
  `depositno` VARCHAR(50) CHARACTER SET latin1 DEFAULT NULL,
  `depositdate` DATE DEFAULT NULL,
  PRIMARY KEY (`remittanceno`,`receiptdate`,`acctid`,`fundid`),
  KEY `ix_income_summary_liquidationid` (`liquidationno`),
  KEY `ix_income_summary_remittanceid` (`remittanceno`),
  KEY `ix_income_summary_receipdate` (`receiptdate`),
  KEY `ix_income_summary_liquidationdate` (`liquidationdate`),
  KEY `ix_income_summary_remittancedate` (`remittancedate`),
  KEY `fk_income_summary_account` (`acctid`),
  CONSTRAINT `fk_income_summary_account` FOREIGN KEY (`acctid`) REFERENCES `revenueitem` (`objid`)
) ENGINE=INNODB DEFAULT CHARSET=utf8
