USE clfc2;

ALTER TABLE `collection_cb`
ADD COLUMN `state` VARCHAR(25) COMMENT 'FOR_VERIFICATION,VERIFIED,DEPOSITED' AFTER `objid`,
ADD COLUMN `dtverified` DATETIME AFTER `cbsno`,
ADD COLUMN `verifier_objid` VARCHAR(50) AFTER `dtverified`,
ADD COLUMN `verifier_name` VARCHAR(50) AFTER `verifier_objid`;

CREATE INDEX `ix_state` ON `collection_cb`(`state`);

CREATE INDEX `ix_dtverified` ON `collection_cb`(`dtverified`);

CREATE INDEX `ix_verifierid` ON `collection_cb`(`verifier_objid`);

CREATE TABLE `collection_cb_forverification` (
  `objid` varchar(50) NOT NULL,
  PRIMARY KEY  (`objid`),
  CONSTRAINT `fk_collection_cb_forverification` FOREIGN KEY (`objid`) REFERENCES `collection_cb` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `collection_cb_active` (
  `objid` varchar(50) NOT NULL,
  PRIMARY KEY  (`objid`),
  CONSTRAINT `fk_collection_cb-active` FOREIGN KEY (`objid`) REFERENCES `collection_cb` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `depositslip_cbs` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `cbsid` varchar(50) default NULL,
  `cbsno` varchar(25) default NULL,
  `txndate` date default NULL,
  `amount` decimal(8,2) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_cbsid` (`cbsid`),
  KEY `ix_cbsno` (`cbsno`),
  KEY `ix_txndate` (`txndate`),
  KEY `fk_depositslip_cbs` (`parentid`),
  CONSTRAINT `fk_depositslip_cbs` FOREIGN KEY (`parentid`) REFERENCES `depositslip` (`objid`),
  CONSTRAINT `fk_depositslip_cbs_collection_cb` FOREIGN KEY (`cbsid`) REFERENCES `collection_cb` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;