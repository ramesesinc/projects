USE clfc2;

CREATE TABLE `change_ledger_borrower_log` (
  `objid` VARCHAR(40) NOT NULL,
  `ledgerid` VARCHAR(40) DEFAULT NULL,
  `borrowerid` VARCHAR(40) DEFAULT NULL,
  `prevborrowerid` VARCHAR(40) DEFAULT NULL,
  `dfiled` DATETIME DEFAULT NULL,
  `filedby` VARCHAR(50) DEFAULT NULL,
  `remarks` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_ledgerid` (`ledgerid`),
  KEY `ix_borrowerid` (`borrowerid`),
  KEY `ix_prevborrowerid` (`prevborrowerid`),
  KEY `ix_dtfiled` (`dfiled`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

INSERT INTO `sys_usergroup`(`objid`, `title`, `domain`, `role`, `userclass`, `orgclass`)
VALUES ('ADMIN_SUPPORT', 'ADMIN SUPPORT', 'ADMIN', 'ADMIN_SUPPORT', 'usergroup', NULL);

INSERT INTO `sys_usergroup_permission`(`objid`, `usergroup_objid`, `object`, `permission`, `title`)
VALUES ('ADMIN_SUPPORT_PT', 'ADMIN_SUPPORT', 'application', 'transfer', 'transfer payment');