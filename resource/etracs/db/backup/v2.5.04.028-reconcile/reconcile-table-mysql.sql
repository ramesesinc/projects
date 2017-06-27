CREATE TABLE `entity_reconciled` (
  `objid` varchar(50) NOT NULL,
  `info` mediumtext,
  `masterid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_entity_reconciled_master` (`masterid`),
  CONSTRAINT `fk_entity_reconciled_master` FOREIGN KEY (`masterid`) REFERENCES `entity` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `entity_reconciled_txn` (
  `objid` varchar(50) NOT NULL,
  `reftype` varchar(50) NOT NULL,
  `refid` varchar(50) NOT NULL,
  `tag` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`,`reftype`,`refid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8




