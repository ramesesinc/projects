USE notification_lgu;

CREATE TABLE `sms_outbox_pending` (
  `objid` varchar(50) NOT NULL,
  `dtexpiry` datetime default NULL,
  `dtretry` datetime default NULL,
  `retrycount` smallint(6) default '0',
  PRIMARY KEY  (`objid`),
  KEY `ix_dtexpiry` (`dtexpiry`),
  KEY `ix_dtretry` (`dtretry`)
) ENGINE=InnoDB;

