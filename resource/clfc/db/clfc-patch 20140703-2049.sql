USE clfc2;

CREATE TABLE IF NOT EXISTS `loan_signatory` (
  `objid` VARCHAR(50) NOT NULL,
  `state` VARCHAR(25) DEFAULT NULL,
  `dtcreated` DATETIME DEFAULT NULL,
  `author_objid` VARCHAR(50) DEFAULT NULL,
  `author_name` VARCHAR(50) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `position` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_state` (`state`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;