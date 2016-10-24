

CREATE TABLE `form60setup` (
  `objid` varchar(50) NOT NULL,
  `type` varchar(20) DEFAULT NULL,
  `code` varchar(10) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `nationalrate` decimal(10,2) DEFAULT '0.00',
  `provgenrate` decimal(10,2) DEFAULT '0.00',
  `provsefrate` decimal(10,2) DEFAULT '0.00',
  `provtrustrate` decimal(10,2) DEFAULT '0.00',
  `munigenrate` decimal(10,2) DEFAULT '0.00',
  `munisefrate` decimal(10,2) DEFAULT '0.00',
  `munitrustrate` decimal(10,2) DEFAULT '0.00',
  `barangayrate` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`objid`),
  KEY `ix_parentid` (`parentid`),
  KEY `ix_type` (`type`)
) ENGINE=InnoDB ;


CREATE TABLE `form60account` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) NOT NULL,
  `revenueitemid` varchar(50) NOT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_form60account_parentid` (`parentid`),
  KEY `ix_form60account_acctid` (`revenueitemid`)
) ENGINE=InnoDB;