USE clfc2;

CREATE TABLE IF NOT EXISTS `inbox` 
(
  `objid` varchar(50) NOT NULL,
  `dtcreated` datetime default NULL,
  `refid` varchar(50) default NULL,
  `filetype` varchar(50) default NULL,
  `message` text,
  `senderid` varchar(50) default NULL,
  `sendername` varchar(150) default NULL,
  `recipientid` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_refid` (`refid`),
  KEY `ix_senderid` (`senderid`),
  KEY `ix_sendername` (`sendername`),
  KEY `ix_recipientid` (`recipientid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

