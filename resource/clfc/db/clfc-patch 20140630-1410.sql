USE clfc2;

INSERT IGNORE INTO `sys_usergroup_permission`(objid, usergroup_objid, object, permission, title) VALUES
('TREASURY_CASHIER_TLRC', 'TREASURY_CASHIER', 'teller', 'create', 'create'),
('TREASURY_CASHIER_TLRE', 'TREASURY_CASHIER', 'teller', 'edit', 'edit'),
('TREASURY_CASHIER_TLRD', 'TREASURY_CASHIER', 'teller', 'delete', 'delete'),
('TREASURY_CASHIER_TLRR', 'TREASURY_CASHIER', 'teller', 'read', 'read'),
('TREASURY_CASHIER_TLRA', 'TREASURY_CASHIER', 'teller', 'approve', 'approve');

INSERT IGNORE INTO `sys_usergroup_permission`(objid, usergroup_objid, object, permission, title) VALUES
('TREASURY_CASHIER_DSC', 'TREASURY_CASHIER', 'depositslip', 'create', 'create'),
('TREASURY_CASHIER_DSE', 'TREASURY_CASHIER', 'depositslip', 'edit', 'edit'),
('TREASURY_CASHIER_DSD', 'TREASURY_CASHIER', 'depositslip', 'delete', 'delete'),
('TREASURY_CASHIER_DSR', 'TREASURY_CASHIER', 'depositslip', 'read', 'read'),
('TREASURY_CASHIER_DSA', 'TREASURY_CASHIER', 'depositslip', 'approve', 'approve');

INSERT IGNORE INTO `sys_usergroup_permission`(objid, usergroup_objid, object, permission, title) VALUES
('DATAMGMT_SIGNATORY_C', 'LOAN_DATAMGMT_AUTHOR', 'signatory', 'create', 'create'),
('DATAMGMT_SIGNATORY_E', 'LOAN_DATAMGMT_AUTHOR', 'signatory', 'edit', 'edit'),
('DATAMGMT_SIGNATORY_D', 'LOAN_DATAMGMT_AUTHOR', 'signatory', 'delete', 'delete'),
('DATAMGMT_SIGNATORY_R', 'LOAN_DATAMGMT_AUTHOR', 'signatory', 'read', 'read'),
('DATAMGMT_SIGNATORY_A', 'LOAN_DATAMGMT_AUTHOR', 'signatory', 'approve', 'approve');

CREATE TABLE IF NOT EXISTS `capture_payment` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL COMMENT 'PENDING,CLOSED',
  `fieldcollectionid` varchar(50) default NULL,
  `specialcollectionid` varchar(50) default NULL,
  `trackerid` varchar(50) default NULL,
  `txndate` date default NULL,
  `dtfiled` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `collector_objid` varchar(50) default NULL,
  `collector_name` varchar(50) default NULL,
  `cbsno` varchar(25) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_state` (`state`),
  KEY `ix_fieldcollectionid` (`fieldcollectionid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_collectorid` (`collector_objid`),
  KEY `ix_txndate` (`txndate`),
  KEY `fk_capturepayment_specialcollection` (`specialcollectionid`),
  KEY `ix_cbsno` (`cbsno`),
  KEY `ix_trackerid` (`trackerid`),
  CONSTRAINT `fk_capturepayment_specialcollection` FOREIGN KEY (`specialcollectionid`) REFERENCES `specialcollection` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `capture_payment_detail` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `scdetailid` varchar(50) default NULL,
  `ledgerid` varchar(50) default NULL,
  `txnmode` varchar(25) default NULL COMMENT 'ONLINE_MOBILE,ONLINE_WIFI',
  `dtpaid` datetime default NULL,
  `borrowername` varchar(255) default NULL,
  `paidby` varchar(255) default NULL,
  `amount` decimal(10,2) default '0.00',
  `payoption` varchar(25) default NULL COMMENT 'cash,check',
  `bank_objid` varchar(50) default NULL,
  `bank_name` varchar(50) default NULL,
  `check_no` varchar(25) default NULL,
  `check_date` date default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_borrowername` (`borrowername`),
  KEY `ix_payoption` (`payoption`),
  KEY `ix_bankid` (`bank_objid`),
  KEY `ix_checkno` (`check_no`),
  KEY `ix_checkdate` (`check_date`),
  KEY `fk_detail_paymentcapture` (`parentid`),
  KEY `ix_dtpaid` (`dtpaid`),
  KEY `ix_scdetailid` (`scdetailid`),
  KEY `ix_ledgerid` (`ledgerid`),
  KEY `ix_txnmode` (`txnmode`),
  CONSTRAINT `fk_detail_paymentcapture` FOREIGN KEY (`parentid`) REFERENCES `capture_payment` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `capture_payment_pending` (
  `objid` varchar(50) NOT NULL,
  PRIMARY KEY  (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `collection_cb` (
  `objid` varchar(50) NOT NULL,
  `dtfiled` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `txndate` date default NULL,
  `collector_objid` varchar(50) default NULL,
  `collector_name` varchar(50) default NULL,
  `collection_objid` varchar(50) default NULL,
  `collection_type` varchar(25) default NULL COMMENT 'FIELD,ONLINE',
  `group_objid` varchar(50) default NULL,
  `group_type` varchar(25) default NULL COMMENT 'route,followup,specila,online',
  `cbsno` varchar(25) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_collectorid` (`collector_objid`),
  KEY `ix_collectionid` (`collection_objid`),
  KEY `ix_collectiontype` (`collection_type`),
  KEY `ix_groupid` (`group_objid`),
  KEY `ix_grouptype` (`group_type`),
  KEY `ix_cbsno` (`cbsno`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `collection_cb_detail` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `denomination` decimal(7,2) default '0.00',
  `qty` smallint(6) default '0',
  `amount` decimal(10,2) default '0.00',
  PRIMARY KEY  (`objid`),
  KEY `ix_denomination` (`denomination`),
  KEY `ix_qty` (`qty`),
  KEY `fk_detail_cb` (`parentid`),
  CONSTRAINT `fk_detail_cb` FOREIGN KEY (`parentid`) REFERENCES `collection_cb` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `collection_remittance` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL COMMENT 'DRAFT,FOR_POSTING,POSTED',
  `dtfiled` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `txndate` date default NULL,
  `collector_objid` varchar(50) default NULL,
  `collector_name` varchar(50) default NULL,
  `collection_objid` varchar(50) default NULL,
  `collection_type` varchar(25) default NULL COMMENT 'FIELD,ONLINE',
  `group_objid` varchar(50) default NULL,
  `group_type` varchar(25) default NULL COMMENT 'route,special,followup,online',
  `totalacctscollected` smallint(6) default '0',
  `totalamount` decimal(16,2) default '0.00',
  `cbsno` varchar(50) default NULL,
  `dtposted` datetime default NULL,
  `poster_objid` varchar(50) default NULL,
  `poster_name` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_state` (`state`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_collectorid` (`collector_objid`),
  KEY `ix_collectionid` (`collection_objid`),
  KEY `ix_colllectiontype` (`collection_type`),
  KEY `ix_groupid` (`group_objid`),
  KEY `ix_grouptype` (`group_type`),
  KEY `ix_cbsno` (`cbsno`),
  KEY `ix_dtposted` (`dtposted`),
  KEY `ix_posterid` (`poster_objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `collection_remittance_detail` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `refid` varchar(50) default NULL,
  `borrower_objid` varchar(50) default NULL,
  `borrower_name` varchar(50) default NULL,
  `loanapp_objid` varchar(50) default NULL,
  `loanapp_appno` varchar(25) default NULL,
  `routecode` varchar(50) default NULL,
  `refno` varchar(25) default NULL,
  `amount` decimal(10,2) default NULL,
  `paytype` varchar(25) default NULL COMMENT 'schedule,over',
  `payoption` varchar(25) default NULL COMMENT 'cash,check',
  `dtpaid` datetime default NULL,
  `paidby` varchar(255) default NULL,
  `bank_objid` varchar(50) default NULL,
  `bank_name` varchar(50) default NULL,
  `check_no` varchar(25) default NULL,
  `check_date` date default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_refno` (`refno`),
  KEY `ix_refid` (`refid`),
  KEY `ix_borrowerid` (`borrower_objid`),
  KEY `ix_loanappid` (`loanapp_objid`),
  KEY `ix_loanappno` (`loanapp_appno`),
  KEY `ix_paytype` (`paytype`),
  KEY `ix_payoption` (`payoption`),
  KEY `ix_bankid` (`bank_objid`),
  KEY `ix_checkno` (`check_no`),
  KEY `ix_checkdate` (`check_date`),
  KEY `fk_detail_remittance` (`parentid`),
  KEY `ix_routecode` (`routecode`),
  CONSTRAINT `fk_detail_remittance` FOREIGN KEY (`parentid`) REFERENCES `collection_remittance` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `collection_remittance_other` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `refid` varchar(50) default NULL,
  `txntype` varchar(25) default NULL COMMENT 'SHORTAGE,OVERAGE',
  `amount` decimal(7,2) default '0.00',
  PRIMARY KEY  (`objid`),
  KEY `ix_refid` (`refid`),
  KEY `fk_other_remittance` (`parentid`),
  KEY `ix_txntype` (`txntype`),
  CONSTRAINT `fk_other_remittance` FOREIGN KEY (`parentid`) REFERENCES `collection_remittance` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `collection_remittance_pending` (
  `objid` varchar(50) NOT NULL,
  PRIMARY KEY  (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `collection_remittance_sendback` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL COMMENT 'DRAFT,ACCEPTED',
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `remittanceid` varchar(50) default NULL,
  `remarks` varchar(255) default NULL,
  `reply` varchar(255) default NULL,
  `dtaccepted` datetime default NULL,
  `acceptedby_objid` varchar(50) default NULL,
  `acceptedby_name` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_state` (`state`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_remittanceid` (`remittanceid`),
  KEY `ix_dtaccepted` (`dtaccepted`),
  KEY `ix_acceptedbyid` (`acceptedby_objid`),
  CONSTRAINT `fk_sendback_remittance` FOREIGN KEY (`remittanceid`) REFERENCES `collection_remittance` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `deposit` (
  `objid` varchar(50) NOT NULL,
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `txndate` date default NULL,
  `txntype` varchar(25) default NULL COMMENT 'vault,bank',
  `representative1_objid` varchar(50) default NULL,
  `representative1_name` varchar(50) default NULL,
  `representative2_objid` varchar(50) default NULL,
  `representative2_name` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_txntype` (`txntype`),
  KEY `ix_representative1id` (`representative1_objid`),
  KEY `ix_representative2id` (`representative2_objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `deposit_detail` (
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
  KEY `ix_parentid` (`parentid`),
  KEY `ix_dscontrolno` (`depositslip_controlno`),
  KEY `ix_dsacctno` (`depositslip_acctno`),
  CONSTRAINT `fk_depositdetail_depositslip` FOREIGN KEY (`refid`) REFERENCES `depositslip` (`objid`),
  CONSTRAINT `fk_deposit_detail` FOREIGN KEY (`parentid`) REFERENCES `deposit` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `depositslip` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL COMMENT 'DRAFT,APPROVED,CLOSED',
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `txndate` date default NULL,
  `controlno` varchar(25) default NULL,
  `passbook_objid` varchar(50) default NULL,
  `passbook_passbookno` varchar(25) default NULL,
  `passbook_acctno` varchar(25) default NULL,
  `passbook_acctname` varchar(250) default NULL,
  `amount` decimal(8,2) default '0.00',
  `type` varchar(25) default NULL COMMENT 'cash,check',
  `reftype` varchar(25) default NULL COMMENT 'SAFE_KEEP,DEPOSIT',
  PRIMARY KEY  (`objid`),
  KEY `ix_state` (`state`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_passbookid` (`passbook_objid`),
  KEY `ix_passbookno` (`passbook_passbookno`),
  KEY `ix_passbookacctno` (`passbook_acctno`),
  KEY `ix_controlno` (`controlno`),
  KEY `ix_type` (`type`),
  KEY `ix_reftype` (`reftype`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `depositslip_check` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `refid` varchar(50) default NULL,
  `checkno` varchar(25) default NULL,
  `checkdate` date default NULL,
  `amount` decimal(10,2) default '0.00',
  `bank_objid` varchar(50) default NULL,
  `bank_name` varchar(255) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_refid` (`refid`),
  KEY `ix_checkno` (`checkno`),
  KEY `ix_checkdate` (`checkdate`),
  KEY `ix_bankid` (`bank_objid`),
  KEY `fk_depositslip_check` (`parentid`),
  CONSTRAINT `fk_depositslip_check` FOREIGN KEY (`parentid`) REFERENCES `depositslip` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `depositslip_checkout` (
  `objid` varchar(50) NOT NULL,
  `dtcheckedout` datetime default NULL,
  `representative1_objid` varchar(50) default NULL,
  `representative1_name` varchar(50) default NULL,
  `representative2_objid` varchar(50) default NULL,
  `representative2_name` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtcheckedout` (`dtcheckedout`),
  KEY `ix_representative1id` (`representative1_objid`),
  KEY `ix_representative2id` (`representative2_objid`),
  CONSTRAINT `fk_depositslip_checkout` FOREIGN KEY (`objid`) REFERENCES `depositslip` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `fieldcollection` (
  `objid` varchar(50) NOT NULL,
  `dtfiled` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `branchid` varchar(50) default NULL,
  `billdate` date default NULL,
  `collector_objid` varchar(50) default NULL,
  `collector_name` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_branchid` (`branchid`),
  KEY `ix_billdate` (`billdate`),
  KEY `ix_collectorid` (`collector_objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `fieldcollection_item` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `state` varchar(25) default NULL COMMENT 'DRAFT,REMITTED,CLOSED',
  `dtfiled` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `item_objid` varchar(50) default NULL,
  `item_type` varchar(25) default NULL COMMENT 'route,special,followup',
  `trackerid` varchar(50) default NULL,
  `totalcount` smallint(5) default '0',
  `totalamount` decimal(16,2) default '0.00',
  `cbsno` varchar(40) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_state` (`state`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_itemid` (`item_objid`),
  KEY `ix_trackerid` (`trackerid`),
  KEY `ix_cbsno` (`cbsno`),
  KEY `fk_item_fieldcollection` (`parentid`),
  KEY `ix_itemtype` (`item_type`),
  CONSTRAINT `fk_item_fieldcollection` FOREIGN KEY (`parentid`) REFERENCES `fieldcollection` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `fieldcollection_loan` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `fieldcollectionid` varchar(50) default NULL,
  `loanapp_objid` varchar(50) default NULL,
  `loanapp_appno` varchar(25) default NULL,
  `borrower_objid` varchar(50) default NULL,
  `borrower_name` varchar(50) default NULL,
  `routecode` varchar(50) default NULL,
  `noofpayments` smallint(3) default NULL,
  `remarks` varchar(255) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_loanappid` (`loanapp_objid`),
  KEY `ix_borrowerid` (`borrower_objid`),
  KEY `ix_routecode` (`routecode`),
  KEY `fk_detail_fieldcollectionitem` (`parentid`),
  KEY `fk_detail_fieldcollection` (`fieldcollectionid`),
  CONSTRAINT `fk_detail_fieldcollection` FOREIGN KEY (`fieldcollectionid`) REFERENCES `fieldcollection` (`objid`),
  CONSTRAINT `fk_detail_fieldcollectionitem` FOREIGN KEY (`parentid`) REFERENCES `fieldcollection_item` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `fieldcollection_payment` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `itemid` varchar(50) default NULL,
  `fieldcollectionid` varchar(50) default NULL,
  `dtfiled` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `txnmode` varchar(25) default NULL COMMENT 'ONLINE_MOBILE,ONLINE_WIFI',
  `dtpaid` datetime default NULL,
  `refno` varchar(25) default NULL,
  `paytype` varchar(15) default NULL COMMENT 'schedule,over',
  `amount` decimal(10,2) default NULL,
  `paidby` varchar(255) default NULL,
  `payoption` varchar(15) default NULL COMMENT 'cash,check',
  `bank_objid` varchar(50) default NULL,
  `bank_name` varchar(50) default NULL,
  `check_no` varchar(50) default NULL,
  `check_date` date default NULL,
  `version` smallint(3) default '1',
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_refno` (`refno`,`version`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_dtpaid` (`dtpaid`),
  KEY `ix_paytype` (`paytype`),
  KEY `ix_payoption` (`payoption`),
  KEY `ix_bankid` (`bank_objid`),
  KEY `ix_checkno` (`check_no`),
  KEY `ix_checkdate` (`check_date`),
  KEY `fk_payment_fcloan` (`parentid`),
  KEY `fk_payment_fcitem` (`itemid`),
  KEY `fk_payment_fc` (`fieldcollectionid`),
  KEY `ix_txnmode` (`txnmode`),
  KEY `ix_refno` (`refno`),
  CONSTRAINT `fk_payment_fc` FOREIGN KEY (`fieldcollectionid`) REFERENCES `fieldcollection` (`objid`),
  CONSTRAINT `fk_payment_fcitem` FOREIGN KEY (`itemid`) REFERENCES `fieldcollection_item` (`objid`),
  CONSTRAINT `fk_payment_fcloan` FOREIGN KEY (`parentid`) REFERENCES `fieldcollection_loan` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `followupcollection` (
  `objid` varchar(50) NOT NULL,
  PRIMARY KEY  (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `ledger_billing` (
  `objid` varchar(50) NOT NULL,
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `collector_objid` varchar(50) default NULL,
  `collector_name` varchar(50) default NULL,
  `billdate` date default NULL,
  `branchid` varchar(50) default NULL,
  `totalfordownload` smallint(3) default '0',
  `totaldownloaded` smallint(3) default '0',
  `totalposted` smallint(3) default '0',
  `remarks` varchar(255) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_collectorid` (`collector_objid`),
  KEY `ix_billdate` (`billdate`),
  KEY `ix_branchid` (`branchid`),
  KEY `ix_totalfordownload` (`totalfordownload`),
  KEY `ix_totaldownloaded` (`totaldownloaded`),
  KEY `ix_totalposted` (`totalposted`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `ledger_billing_assist` (
  `objid` varchar(50) NOT NULL,
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `dtmodified` datetime default NULL,
  `modifiedby_objid` varchar(50) default NULL,
  `modifiedby_name` varchar(50) default NULL,
  `prevbillingid` varchar(50) default NULL,
  `prevcollector_objid` varchar(50) default NULL,
  `prevcollector_name` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_prevbillingid` (`prevbillingid`),
  KEY `ix_prevcollectorid` (`prevcollector_objid`),
  KEY `ix_dtmodified` (`dtmodified`),
  KEY `ix_modifiedbyid` (`modifiedby_objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `ledger_billing_detail` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `billingid` varchar(50) default NULL,
  `ledgerid` varchar(50) default NULL,
  `route_code` varchar(50) default NULL,
  `acctid` varchar(50) default NULL,
  `acctname` varchar(50) default NULL,
  `loanamount` decimal(16,2) default '0.00',
  `appno` varchar(25) default NULL,
  `dailydue` decimal(7,2) default '0.00',
  `amountdue` decimal(10,2) default '0.00',
  `overpaymentamount` decimal(7,2) default '0.00',
  `balance` decimal(16,2) default '0.00',
  `refno` varchar(25) default NULL,
  `txndate` date default NULL,
  `dtmatured` date default NULL,
  `isfirstbill` smallint(3) default '0',
  `paymentmethod` varchar(15) default NULL,
  `loandate` date default NULL,
  `term` smallint(3) default NULL,
  `loanappid` varchar(50) default NULL,
  `homeaddress` varchar(255) default NULL,
  `collectionaddress` varchar(255) default NULL,
  `penalty` decimal(16,2) default '0.00',
  `interest` decimal(7,2) default '0.00',
  `others` decimal(7,2) default '0.00',
  PRIMARY KEY  (`objid`),
  KEY `fk_detail_billingitem` (`parentid`),
  KEY `fk_detail_billing` (`billingid`),
  KEY `ix_ledgerid` (`ledgerid`),
  KEY `ix_routecode` (`route_code`),
  KEY `ix_acctid` (`acctid`),
  KEY `ix_acctname` (`acctname`),
  KEY `ix_appno` (`appno`),
  KEY `ix_refno` (`refno`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_isfirstbill` (`isfirstbill`),
  KEY `ix_paymentmethod` (`paymentmethod`),
  KEY `ix_loanappid` (`loanappid`),
  CONSTRAINT `fk_detail_billing` FOREIGN KEY (`billingid`) REFERENCES `ledger_billing` (`objid`),
  CONSTRAINT `fk_detail_billingitem` FOREIGN KEY (`parentid`) REFERENCES `ledger_billing_item` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `ledger_billing_item` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `state` varchar(25) default NULL COMMENT 'FOR_DOWNLOAD,DOWNLOADED,REMITTED,POSTED,CANCELLED,CLOSED',
  `dtmodified` datetime default NULL,
  `modifiedby_objid` varchar(50) default NULL,
  `modifiedby_name` varchar(50) default NULL,
  `item_objid` varchar(50) default NULL,
  `item_type` varchar(25) default NULL COMMENT 'route,special,followup',
  `dtcancelled` datetime default NULL,
  `cancelledby_objid` varchar(50) default NULL,
  `cancelledby_name` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_state` (`state`),
  KEY `ix_dtmodified` (`dtmodified`),
  KEY `ix_modifiedbyid` (`modifiedby_objid`),
  KEY `ix_itemid` (`item_objid`),
  KEY `ix_itemtype` (`item_type`),
  KEY `ix_dtcancelled` (`dtcancelled`),
  KEY `ix_cancelledbyid` (`cancelledby_objid`),
  KEY `fk_item_billing` (`parentid`),
  CONSTRAINT `fk_item_billing` FOREIGN KEY (`parentid`) REFERENCES `ledger_billing` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `loanpayment` (
  `objid` varchar(50) NOT NULL,
  `dtfiled` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `txndate` date default NULL,
  `totalacctscollected` smallint(3) default '0',
  `totalamount` decimal(16,2) default '0.00',
  PRIMARY KEY  (`objid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_txndate` (`txndate`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `loanpayment_detail` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `borrower_objid` varchar(50) default NULL,
  `borrower_name` varchar(50) default NULL,
  `loanapp_objid` varchar(50) default NULL,
  `loanapp_appno` varchar(25) default NULL,
  `routecode` varchar(50) default NULL,
  `refno` varchar(25) default NULL,
  `amount` decimal(10,2) default NULL,
  `paytype` varchar(25) default NULL COMMENT 'schedule,over',
  `paidby` varchar(255) default NULL,
  `control_objid` varchar(50) default NULL,
  `control_series` varchar(50) default NULL,
  `control_seriesno` int(11) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_refno` (`refno`),
  KEY `ix_borrowerid` (`borrower_objid`),
  KEY `ix_loanappid` (`loanapp_objid`),
  KEY `ix_loanappno` (`loanapp_appno`),
  KEY `ix_routecode` (`routecode`),
  KEY `ix_paytype` (`paytype`),
  KEY `ix_controlid` (`control_objid`),
  KEY `ix_controlseries` (`control_series`),
  KEY `ix_controlseriesno` (`control_seriesno`),
  KEY `fk_detail_loanpayment` (`parentid`),
  CONSTRAINT `fk_detail_loanpayment` FOREIGN KEY (`parentid`) REFERENCES `loanpayment` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `onlinecollection` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL COMMENT 'DRAFT,REMITTED',
  `txndate` date default NULL,
  `dtfiled` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `collector_objid` varchar(50) default NULL,
  `collector_name` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_state` (`state`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_collectorid` (`collector_objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `onlinecollection_collector` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(50) default NULL,
  PRIMARY KEY  (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `onlinecollection_detail` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `loanapp_objid` varchar(50) default NULL,
  `loanapp_appno` varchar(25) default NULL,
  `borrower_objid` varchar(50) default NULL,
  `borrower_name` varchar(50) default NULL,
  `route_code` varchar(50) default NULL,
  `route_description` varchar(100) default NULL,
  `route_area` varchar(255) default NULL,
  `refno` varchar(25) default NULL,
  `paytype` varchar(25) default NULL COMMENT 'schedule,over',
  `payoption` varchar(25) default NULL COMMENT 'cash,check',
  `amount` decimal(16,2) default NULL,
  `bank_objid` varchar(50) default NULL,
  `bank_name` varchar(50) default NULL,
  `check_no` varchar(25) default NULL,
  `check_date` date default NULL,
  `dtpaid` datetime default NULL,
  `paidby` varchar(255) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_refno` (`refno`),
  KEY `ix_loanappid` (`loanapp_objid`),
  KEY `ix_loanappno` (`loanapp_appno`),
  KEY `ix_borrowerid` (`borrower_objid`),
  KEY `ix_dtpaid` (`dtpaid`),
  KEY `ix_paytype` (`paytype`),
  KEY `ix_routecode` (`route_code`),
  KEY `fk_detail_onlinecollection` (`parentid`),
  KEY `ix_bankid` (`bank_objid`),
  KEY `ix_checkno` (`check_no`),
  KEY `ix_checkdate` (`check_date`),
  KEY `ix_payoption` (`payoption`),
  CONSTRAINT `fk_detail_onlinecollection` FOREIGN KEY (`parentid`) REFERENCES `onlinecollection` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `overage` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL COMMENT 'DRAFT,APPROVED,DISAPPROVED',
  `dtfiled` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `refno` varchar(25) default NULL,
  `txndate` date default NULL,
  `remittanceid` varchar(50) default NULL,
  `collector_objid` varchar(50) default NULL,
  `collector_name` varchar(50) default NULL,
  `amount` decimal(7,2) default '0.00',
  `remarks` varchar(255) default NULL,
  `dtposted` datetime default NULL,
  `poster_objid` varchar(50) default NULL,
  `poster_name` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_state` (`state`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_remittanceid` (`remittanceid`),
  KEY `ix_collectorid` (`collector_objid`),
  KEY `ix_posterid` (`poster_objid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_dtposted` (`dtposted`),
  KEY `ix_refno` (`refno`),
  CONSTRAINT `fk_overage_remittance` FOREIGN KEY (`remittanceid`) REFERENCES `collection_remittance` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `shortage` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL COMMENT 'DRAFT,APPROVED,DISAPPROVED',
  `dtfiled` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `refno` varchar(25) default NULL,
  `txndate` date default NULL,
  `remittanceid` varchar(50) default NULL,
  `collector_objid` varchar(50) default NULL,
  `collector_name` varchar(50) default NULL,
  `amount` decimal(7,2) default '0.00',
  `remarks` varchar(255) default NULL,
  `cbsno` varchar(25) default NULL,
  `dtposted` datetime default NULL,
  `poster_objid` varchar(50) default NULL,
  `poster_name` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_state` (`state`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_remittanceid` (`remittanceid`),
  KEY `ix_collectorid` (`collector_objid`),
  KEY `ix_cbsno` (`cbsno`),
  KEY `ix_dtposted` (`dtposted`),
  KEY `ix_posterid` (`poster_objid`),
  KEY `ix_refno` (`refno`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `shortage_fundrequest` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL COMMENT 'FOR_APPROVAL,APPROVED,REJECTED',
  `remittanceid` varchar(50) default NULL,
  `dtrequested` datetime default NULL,
  `requester_objid` varchar(50) default NULL,
  `requester_name` varchar(50) default NULL,
  `collector_objid` varchar(50) default NULL,
  `collector_name` varchar(50) default NULL,
  `amount` decimal(8,2) default '0.00',
  `remarks` varchar(255) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_remittanceid` (`remittanceid`),
  KEY `ix_dtrequested` (`dtrequested`),
  KEY `ix_requesterid` (`requester_objid`),
  KEY `ix_collectorid` (`collector_objid`),
  KEY `ix_state` (`state`),
  CONSTRAINT `fk_shortagerequest_remittance` FOREIGN KEY (`remittanceid`) REFERENCES `collection_remittance` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `specialcollection` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL COMMENT 'PENDING,CANCELLED,FOR_DOWNLOAD,DOWNLOADED',
  `billingid` varchar(50) default NULL,
  `txntype` varchar(25) default NULL COMMENT 'REQUEST,ONLINE,CAPTURE',
  `txndate` date default NULL,
  `dtfiled` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `dtrequested` datetime default NULL,
  `requester_objid` varchar(50) default NULL,
  `requester_name` varchar(50) default NULL,
  `collector_objid` varchar(50) default NULL,
  `collector_name` varchar(50) default NULL,
  `remarks` varchar(255) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_state` (`state`),
  KEY `ix_billingid` (`billingid`),
  KEY `ix_txntype` (`txntype`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_dtrequested` (`dtrequested`),
  KEY `ix_requesterid` (`requester_objid`),
  KEY `ix_collectorid` (`collector_objid`),
  KEY `ix_txndate` (`txndate`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `specialcollection_detail` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `billingdetailid` varchar(50) default NULL,
  `routecode` varchar(50) default NULL,
  `borrower_objid` varchar(50) default NULL,
  `borrower_name` varchar(50) default NULL,
  `loanapp_objid` varchar(50) default NULL,
  `loanapp_appno` varchar(25) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_billingdetailid` (`billingdetailid`),
  KEY `ix_routecode` (`routecode`),
  KEY `ix_borrowerid` (`borrower_objid`),
  KEY `ix_loanappid` (`loanapp_objid`),
  KEY `ix_loanappno` (`loanapp_appno`),
  KEY `fk_detail_specialcollection` (`parentid`),
  CONSTRAINT `fk_detail_specialcollection` FOREIGN KEY (`parentid`) REFERENCES `specialcollection` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `teller` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL COMMENT 'ACTIVE, DEACTIVATE',
  `mode` varchar(25) default NULL COMMENT 'ONLINE, CAPTURE',
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `dtmodified` datetime default NULL,
  `modifiedby_objid` varchar(50) default NULL,
  `modifiedby_name` varchar(50) default NULL,
  `tellerno` varchar(50) default NULL,
  `branchid` varchar(50) default NULL,
  `name` varchar(255) default NULL,
  `firstname` varchar(255) default NULL,
  `lastname` varchar(255) default NULL,
  `middlename` varchar(255) default NULL,
  `connectedto` varchar(255) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_state` (`state`),
  KEY `ix_mode` (`mode`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_dtmodified` (`dtmodified`),
  KEY `ix_modifiedbyid` (`modifiedby_objid`),
  KEY `ix_tellerno` (`tellerno`),
  KEY `ix_branchid` (`branchid`),
  KEY `ix_name` (`name`),
  KEY `ix_firstname` (`firstname`),
  KEY `ix_lastname` (`lastname`),
  KEY `ix_middlename` (`middlename`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `teller_active` (
  `objid` varchar(50) NOT NULL,
  PRIMARY KEY  (`objid`),
  CONSTRAINT `fk_teller_active` FOREIGN KEY (`objid`) REFERENCES `teller` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `voidpayment` (
  `objid` varchar(50) NOT NULL,
  `dtfiled` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `borrower_objid` varchar(50) default NULL,
  `borrower_name` varchar(50) default NULL,
  `collector_objid` varchar(50) default NULL,
  `collector_name` varchar(50) default NULL,
  `routecode` varchar(50) default NULL,
  `paymentid` varchar(50) default NULL,
  `dtpaid` datetime default NULL,
  `refno` varchar(25) default NULL,
  `amount` decimal(10,2) default '0.00',
  `paidby` varchar(255) default NULL,
  `paytype` varchar(25) default NULL COMMENT 'schedule,over',
  `payoption` varchar(25) default NULL COMMENT 'cash,check',
  `bank_objid` varchar(50) default NULL,
  `bank_name` varchar(50) default NULL,
  `check_no` varchar(25) default NULL,
  `check_date` date default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_borrowerid` (`borrower_objid`),
  KEY `ix_collectorid` (`collector_objid`),
  KEY `ix_borrowername` (`borrower_name`),
  KEY `ix_routecode` (`routecode`),
  KEY `ix_paymentid` (`paymentid`),
  KEY `ix_dtpaid` (`dtpaid`),
  KEY `ix_refno` (`refno`),
  KEY `ix_paytype` (`paytype`),
  KEY `ix_payoption` (`payoption`),
  KEY `ix_bankid` (`bank_objid`),
  KEY `ix_checkno` (`check_no`),
  KEY `ix_checkdate` (`check_date`),
  CONSTRAINT `fk_voidpayment_request` FOREIGN KEY (`objid`) REFERENCES `voidrequest` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `voidrequest` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) default NULL COMMENT 'FOR_APPROVAL,APPROVED,DISAPPROVED',
  `dtfiled` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `txncode` varchar(25) default NULL COMMENT 'FIELD,ONLINE',
  `paymentid` varchar(50) default NULL,
  `routecode` varchar(50) default NULL,
  `collectionid` varchar(50) default NULL,
  `loanapp_objid` varchar(50) default NULL,
  `loanapp_appno` varchar(25) default NULL,
  `borrower_objid` varchar(50) default NULL,
  `borrower_name` varchar(50) default NULL,
  `collector_objid` varchar(50) default NULL,
  `collector_name` varchar(50) default NULL,
  `reason` varchar(255) default NULL,
  `dtposted` datetime default NULL,
  `poster_objid` varchar(50) default NULL,
  `poster_name` varchar(50) default NULL,
  `posterremarks` varchar(255) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_state` (`state`),
  KEY `ix_authorid` (`author_objid`),
  KEY `ix_txncode` (`txncode`),
  KEY `ix_paymentid` (`paymentid`),
  KEY `ix_routecode` (`routecode`),
  KEY `ix_collectionid` (`collectionid`),
  KEY `ix_loanappid` (`loanapp_objid`),
  KEY `ix_loanappno` (`loanapp_appno`),
  KEY `ix_collectorid` (`collector_objid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_dtposted` (`dtposted`),
  KEY `ix_posterid` (`poster_objid`),
  KEY `ix_borrowerid` (`borrower_objid`),
  KEY `ix_borrowername` (`borrower_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/******************
* rule
******************/

CREATE TABLE IF NOT EXISTS `sys_rule` (
  `objid` varchar(50) NOT NULL default '',
  `state` varchar(25) default 'L',
  `name` varchar(50) NOT NULL default '',
  `ruleset` varchar(50) NOT NULL,
  `rulegroup` varchar(50) default 'L',
  `title` varchar(250) default 'L',
  `description` text,
  `salience` int(10) default NULL,
  `effectivefrom` date default NULL,
  `effectiveto` date default NULL,
  `dtfiled` timestamp NULL default NULL,
  `user_objid` varchar(50) default 'L',
  `user_name` varchar(100) default 'L',
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `PK__sys_rule__530D6FE4157B1701` (`objid`),
  UNIQUE KEY `uid_ruleset_name` (`name`,`ruleset`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sys_rule_action` (
  `objid` varchar(50) NOT NULL default '',
  `parentid` varchar(50) default 'L',
  `actiondef_objid` varchar(50) default 'L',
  `actiondef_name` varchar(50) default 'L',
  `pos` int(10) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `PK__sys_rule__530D6FE47B4643B2` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sys_rule_action_param` (
  `objid` varchar(50) NOT NULL default '',
  `parentid` varchar(50) default 'L',
  `actiondefparam_objid` varchar(50) default 'L',
  `stringvalue` varchar(255) default 'L',
  `booleanvalue` int(10) default NULL,
  `var_objid` varchar(50) default 'L',
  `var_name` varchar(50) default 'L',
  `expr` text,
  `exprtype` varchar(25) default 'L',
  `pos` int(10) default NULL,
  `obj_key` varchar(50) default 'L',
  `obj_value` varchar(255) default 'L',
  `listvalue` text,
  `lov` varchar(50) default 'L',
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `PK__sys_rule__530D6FE442CCE065` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sys_rule_actiondef` (
  `objid` varchar(50) NOT NULL default '',
  `name` varchar(50) NOT NULL default '',
  `ruleset` varchar(50) NOT NULL default '',
  `title` varchar(250) default 'L',
  `sortorder` int(10) default NULL,
  `actionname` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `PK__sys_rule__530D6FE43C69FB99` (`objid`),
  UNIQUE KEY `uid_ruleset_actiondef` (`name`,`ruleset`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sys_rule_actiondef_param` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default 'L',
  `name` varchar(50) NOT NULL default '',
  `sortorder` int(10) default NULL,
  `title` varchar(50) default 'L',
  `datatype` varchar(50) default 'L',
  `handler` varchar(50) default 'L',
  `lookuphandler` varchar(50) default 'L',
  `lookupkey` varchar(50) default 'L',
  `lookupvalue` varchar(50) default 'L',
  `vardatatype` varchar(50) default 'L',
  `lovname` varchar(50) default 'L',
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `PK__sys_rule__530D6FE44BCC3ABA` (`objid`),
  UNIQUE KEY `uid_ruleset_actiondef_name` (`name`,`parentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sys_rule_condition` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default 'L',
  `fact_name` varchar(50) default 'L',
  `fact_objid` varchar(50) default 'L',
  `varname` varchar(50) default 'L',
  `pos` int(10) default NULL,
  `ruletext` text,
  `displaytext` text,
  `dynamic_datatype` varchar(50) default NULL,
  `dynamic_key` varchar(50) default NULL,
  `dynamic_value` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `PK__sys_rule__530D6FE47F16D496` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sys_rule_condition_constraint` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default 'L',
  `field_objid` varchar(50) default 'L',
  `fieldname` varchar(50) default 'L',
  `varname` varchar(50) default 'L',
  `operator_caption` varchar(50) default 'L',
  `operator_symbol` varchar(50) default 'L',
  `usevar` int(10) default NULL,
  `var_objid` varchar(50) default 'L',
  `var_name` varchar(50) default 'L',
  `decimalvalue` decimal(16,2) default NULL,
  `intvalue` int(10) default NULL,
  `stringvalue` varchar(255) default 'L',
  `listvalue` text,
  `datevalue` date default NULL,
  `pos` int(10) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `PK__sys_rule__530D6FE473B00EE2` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sys_rule_condition_var` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default 'L',
  `ruleid` varchar(50) default 'L',
  `varname` varchar(50) default 'L',
  `datatype` varchar(50) default 'L',
  `pos` int(10) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `PK__sys_rule__530D6FE403DB89B3` (`objid`),
  UNIQUE KEY `uix_rule_var` (`varname`,`ruleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sys_rule_deployed` (
  `objid` varchar(50) NOT NULL default '',
  `ruletext` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sys_rule_fact` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL default '',
  `ruleset` varchar(50) NOT NULL default '',
  `title` varchar(160) default 'L',
  `factclass` varchar(50) default 'L',
  `sortorder` int(10) default NULL,
  `handler` varchar(50) default 'L',
  `defaultvarname` varchar(25) default 'L',
  `dynamic` int(10) default NULL,
  `lookuphandler` varchar(50) default NULL,
  `lookupkey` varchar(50) default NULL,
  `lookupvalue` varchar(50) default NULL,
  `lookupdatatype` varchar(50) default NULL,
  `dynamicfieldname` varchar(50) default NULL,
  `builtinconstraints` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `PK__sys_rule__530D6FE47F60ED59` (`objid`),
  UNIQUE KEY `uid_ruleset_fact` (`name`,`ruleset`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sys_rule_fact_field` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default 'L',
  `name` varchar(50) NOT NULL default '',
  `title` varchar(160) default 'L',
  `datatype` varchar(50) default 'L',
  `sortorder` int(10) default NULL,
  `handler` varchar(50) default 'L',
  `lookuphandler` varchar(50) default 'L',
  `lookupkey` varchar(50) default 'L',
  `lookupvalue` varchar(50) default 'L',
  `lookupdatatype` varchar(50) default 'L',
  `multivalued` int(10) default NULL,
  `required` int(10) default NULL,
  `vardatatype` varchar(50) default 'L',
  `lovname` varchar(50) default 'L',
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `PK__sys_rule__530D6FE408D548FA` (`objid`),
  UNIQUE KEY `uid_ruleset_fact_name` (`name`,`parentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sys_rulegroup` (
  `name` varchar(50) NOT NULL,
  `ruleset` varchar(50) NOT NULL,
  `title` varchar(160) default 'L',
  `sortorder` int(10) default NULL,
  PRIMARY KEY  (`name`,`ruleset`),
  UNIQUE KEY `PK__sys_rule__AC050F2A0504B816` (`name`,`ruleset`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sys_ruleset` (
  `name` varchar(50) NOT NULL,
  `title` varchar(160) default 'L',
  `packagename` varchar(50) default 'L',
  `domain` varchar(50) default 'L',
  `role` varchar(50) default 'L',
  `permission` varchar(50) default 'L',
  PRIMARY KEY  (`name`),
  UNIQUE KEY `PK__sys_rule__72E12F1A1BFD2C07` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

