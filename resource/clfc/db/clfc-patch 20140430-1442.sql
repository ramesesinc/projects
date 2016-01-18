USE clfc2;

CREATE TABLE `loan_ledger_close` (
  `objid` VARCHAR(40) NOT NULL,
  `dtclosed` DATETIME DEFAULT NULL,
  `closedby` VARCHAR(40) DEFAULT NULL,
  `remarks` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtclosed` (`dtclosed`),
  KEY `ix_closedby` (`closedby`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;