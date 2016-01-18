USE `clfc2`;

CREATE TABLE IF NOT EXISTS `checkout` (
  `objid` varchar(50) NOT NULL,
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `txndate` date default NULL,
  `representative1_objid` varchar(50) default NULL,
  `representative1_name` varchar(50) default NULL,
  `representative2_objid` varchar(50) default NULL,
  `representative2_name` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_representative1id` (`representative1_objid`),
  KEY `ix_representative2id` (`representative2_objid`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `checkout_detail` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `refid` varchar(50) default NULL,
  `txndate` date default NULL,
  `depositslip_controlno` varchar(25) default NULL,
  `depositslip_acctno` varchar(25) default NULL,
  `depositslip_acctname` varchar(50) default NULL,
  `depositslip_amount` decimal(10,2) default '0.00',
  PRIMARY KEY  (`objid`),
  KEY `ix_refid` (`refid`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_depositslipcontrolno` (`depositslip_controlno`),
  KEY `ix_depositslipacctno` (`depositslip_acctno`),
  KEY `fk_checkout_detail` (`parentid`),
  CONSTRAINT `fk_checkout_detail` FOREIGN KEY (`parentid`) REFERENCES `checkout` (`objid`),
  CONSTRAINT `fk_checkout_detail_depositslip` FOREIGN KEY (`refid`) REFERENCES `depositslip` (`objid`)
) ENGINE=InnoDB;