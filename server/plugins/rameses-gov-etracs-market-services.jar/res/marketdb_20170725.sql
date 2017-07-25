CREATE TABLE `market_electricity` (
  `objid` varchar(50) CHARACTER SET latin1 NOT NULL,
  `acctid` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `state` varchar(10) CHARACTER SET latin1 DEFAULT NULL,
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
);

CREATE TABLE `market_water` (
  `objid` varchar(50) CHARACTER SET latin1 NOT NULL,
  `acctid` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
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
) ;

ALTER TABLE market_account ADD COLUMN electricityreading INT;
ALTER TABLE market_account ADD COLUMN waterreading INT;
