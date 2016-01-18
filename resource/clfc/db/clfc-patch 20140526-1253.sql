USE clfc2;

CREATE TABLE IF NOT EXISTS `loan_exemption` 
(
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL,
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_username` varchar(50) default NULL,
  `ledger_objid` varchar(50) default NULL,
  `route_code` varchar(50) default NULL,
  `loanapp_objid` varchar(50) default NULL,
  `loanapp_appno` varchar(50) default NULL,
  `borrower_objid` varchar(50) default NULL,
  `borrower_name` varchar(255) default NULL,
  `borrower_address` varchar(255) default NULL,
  `type_objid` varchar(50) default NULL,
  `dtstart` date default NULL,
  `dtend` date default NULL,
  `reason` text,
  `dtapproved` datetime default NULL,
  `approvedby` varchar(50) default NULL,
  `approvedremarks` varchar(255) default NULL,
  `dtposted` datetime default NULL,
  `postedby` varchar(50) default NULL,
  `postedremarks` varchar(255) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_ledger_objid` (`ledger_objid`),
  KEY `ix_route_code` (`route_code`),
  KEY `ix_loanapp_objid` (`loanapp_objid`),
  KEY `ix_loanapp_appno` (`loanapp_appno`),
  KEY `ix_borrower_name` (`borrower_name`),
  KEY `ix_type_objid` (`type_objid`),
  KEY `ix_dtstart` (`dtstart`),
  KEY `ix_dtend` (`dtend`),
  KEY `ix_dtapprovedby` (`dtapproved`,`approvedby`),
  KEY `ix_dtpostedby` (`dtposted`,`postedby`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_author_objid` (`author_objid`),
  KEY `ix_author_username` (`author_username`),
  CONSTRAINT `fk_loan_ledger_objid` FOREIGN KEY (`ledger_objid`) REFERENCES `loan_ledger` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `loan_exemption_active` 
(
  `objid` varchar(50) NOT NULL,
  `loanapp_appno` varchar(50) default NULL,
  `borrower_name` varchar(255) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_loanapp_appno` (`loanapp_appno`),
  KEY `ix_borrower_name` (`borrower_name`),
  CONSTRAINT `fk_loan_exemption_objid_objid` FOREIGN KEY (`objid`) REFERENCES `loan_exemption` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT IGNORE INTO sys_usergroup 
  (objid, title, domain, role, userclass, orgclass) 
VALUES 
  ('TREASURY_CASHIER', 'CASHIER', 'TREASURY', 'CASHIER', 'usergroup', NULL); 
  
INSERT IGNORE INTO sys_usergroup_permission 
  (objid, usergroup_objid, object, permission, title) 
VALUES 
  ('TREASURY_CASHIER_A', 'TREASURY_CASHIER', 'bank', 'approve', 'approve'),  
  ('TREASURY_CASHIER_C', 'TREASURY_CASHIER', 'bank', 'create', 'create'),  
  ('TREASURY_CASHIER_D', 'TREASURY_CASHIER', 'bank', 'delete', 'delete'),  
  ('TREASURY_CASHIER_E', 'TREASURY_CASHIER', 'bank', 'edit', 'edit'),  
  ('TREASURY_CASHIER_R', 'TREASURY_CASHIER', 'bank', 'read', 'read'); 

DELETE FROM sys_usergroup_permission WHERE usergroup_objid='LOAN_DATAMGMT_AUTHOR' AND object='bank';

CREATE TABLE IF NOT EXISTS `passbook` 
(
  `objid` varchar(50) NOT NULL,
  `txnstate` varchar(25) default NULL,
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_username` varchar(50) default NULL,
  `bank_objid` varchar(50) default NULL,
  `acctno` varchar(25) default NULL,
  `acctname` varchar(150) default NULL,
  `passbookno` varchar(25) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_acctno` (`acctno`),
  UNIQUE KEY `uix_passbookno` (`passbookno`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_author_objid` (`author_objid`),
  KEY `ix_author_username` (`author_username`),
  KEY `ix_bank_objid` (`bank_objid`),
  CONSTRAINT `fk_passbook_bank_objid_objid` FOREIGN KEY (`bank_objid`) REFERENCES `bank` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT IGNORE INTO sys_usergroup_permission 
  (objid, usergroup_objid, object, permission, title) 
VALUES 
  ('TREASURY_CASHIER_PBKA', 'TREASURY_CASHIER', 'passbook', 'approve', 'approve'),  
  ('TREASURY_CASHIER_PBKC', 'TREASURY_CASHIER', 'passbook', 'create', 'create'),  
  ('TREASURY_CASHIER_PBKD', 'TREASURY_CASHIER', 'passbook', 'delete', 'delete'),  
  ('TREASURY_CASHIER_PBKE', 'TREASURY_CASHIER', 'passbook', 'edit', 'edit'),  
  ('TREASURY_CASHIER_PBKR', 'TREASURY_CASHIER', 'passbook', 'read', 'read'); 


