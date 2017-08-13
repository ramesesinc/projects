CREATE TABLE IF NOT EXISTS `market_electricity` (
  `objid` varchar(50) NOT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  `state` varchar(10) DEFAULT NULL,
  `createdby_objid` varchar(50) DEFAULT NULL,
  `createdby_name` varchar(155) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `readingdate` date DEFAULT NULL,
  `prevreading` int(11) DEFAULT NULL,
  `reading` int(11) DEFAULT NULL,
  `rate` decimal(16,4) DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `amtpaid` decimal(16,4) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX ix_acctid ON market_electricity (acctid);
CREATE INDEX ix_createdby_objid ON market_electricity (createdby_objid);
CREATE INDEX ix_createdby_name ON market_electricity (createdby_name);
CREATE INDEX ix_dtcreated ON market_electricity (dtcreated);
CREATE INDEX ix_year ON market_electricity (year);
CREATE INDEX ix_month ON market_electricity (month);

CREATE TABLE IF NOT EXISTS `market_water` (
  `objid` varchar(50) NOT NULL,
  `acctid` varchar(50) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `createdby_objid` varchar(50) DEFAULT NULL,
  `createdby_name` varchar(255) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `readingdate` date DEFAULT NULL,
  `prevreading` int(11) DEFAULT NULL,
  `reading` int(11) DEFAULT NULL,
  `rate` decimal(16,4) DEFAULT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `amtpaid` decimal(16,4) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX ix_acctid ON market_water (acctid);
CREATE INDEX ix_createdby_objid ON market_water (createdby_objid);
CREATE INDEX ix_createdby_name ON market_water (createdby_name);
CREATE INDEX ix_dtcreated ON market_water (dtcreated);
CREATE INDEX ix_year ON market_water (year);
CREATE INDEX ix_month ON market_water (month);

ALTER TABLE market_account ADD electricityreading INT;
ALTER TABLE market_account ADD waterreading INT;
