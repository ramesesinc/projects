
CREATE TABLE `sms_outbox_pending` (
  `objid` VARCHAR(50) NOT NULL,
  `dtexpiry` DATETIME DEFAULT NULL,
  `dtretry` DATETIME DEFAULT NULL,
  `retrycount` SMALLINT(6) DEFAULT '0',
  PRIMARY KEY  (`objid`),
  KEY `ix_dtexpiry` (`dtexpiry`),
  KEY `ix_dtretry` (`dtretry`)
) ENGINE=INNODB;

