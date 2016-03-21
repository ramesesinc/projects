
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for area
-- ----------------------------
DROP TABLE IF EXISTS `area`;
CREATE TABLE `area` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `region_objid` varchar(50) DEFAULT NULL,
  `info` text,
  PRIMARY KEY (`objid`),
  KEY `ix_name` (`name`),
  KEY `ix_region_objid` (`region_objid`),
  CONSTRAINT `fk_area_region_objid` FOREIGN KEY (`region_objid`) REFERENCES `region` (`objid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of area
-- ----------------------------

-- ----------------------------
-- Table structure for branch
-- ----------------------------
DROP TABLE IF EXISTS `branch`;
CREATE TABLE `branch` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(32) DEFAULT NULL,
  `code` varchar(25) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `contactno` varchar(100) DEFAULT NULL,
  `info` text,
  `area_objid` varchar(50) DEFAULT NULL,
  `debit` decimal(16,2) DEFAULT NULL,
  `credit` decimal(16,2) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_parentid_code` (`code`),
  KEY `ix_name` (`name`),
  KEY `ix_fullname` (`fullname`),
  KEY `ix_area_objid` (`area_objid`),
  CONSTRAINT `fk_branch_area_objid` FOREIGN KEY (`area_objid`) REFERENCES `area` (`objid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of branch
-- ----------------------------
INSERT INTO `branch` VALUES ('BR-442cf56e:15364b20825:-7fe8', 'ACTIVE', '002', 'FUENTE', 'FUENT 1 BRANCH', 'XXX', '111222333', '[manager:\"CECILIA\",businesshours:\"DDD\"]', null, '162500.00', '3500.00');

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) DEFAULT NULL,
  `dtfiled` datetime DEFAULT NULL,
  `branchid` varchar(50) DEFAULT NULL,
  `name` varchar(160) DEFAULT NULL,
  `addresstext` varchar(255) DEFAULT NULL,
  `lastname` varchar(50) DEFAULT NULL,
  `firstname` varchar(50) DEFAULT NULL,
  `middlename` varchar(50) DEFAULT NULL,
  `birthdate` date DEFAULT NULL,
  `gender` varchar(6) DEFAULT NULL,
  `mobileno` varchar(11) DEFAULT NULL,
  `phoneno` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer
-- ----------------------------

-- ----------------------------
-- Table structure for entity
-- ----------------------------
DROP TABLE IF EXISTS `entity`;
CREATE TABLE `entity` (
  `objid` varchar(50) NOT NULL,
  `entityno` varchar(50) NOT NULL,
  `name` longtext NOT NULL,
  `address_text` varchar(255) NOT NULL DEFAULT '',
  `mailingaddress` varchar(255) DEFAULT NULL,
  `type` varchar(25) NOT NULL,
  `sys_lastupdate` varchar(25) DEFAULT NULL,
  `sys_lastupdateby` varchar(50) DEFAULT NULL,
  `remarks` text,
  `entityname` varchar(300) DEFAULT NULL,
  `address_objid` varchar(50) DEFAULT NULL,
  `mobileno` varchar(25) DEFAULT NULL,
  `phoneno` varchar(25) DEFAULT NULL,
  `email` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_entityno` (`entityno`),
  KEY `ix_entityname` (`entityname`(255)),
  KEY `ix_address_objid` (`address_objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of entity
-- ----------------------------
INSERT INTO `entity` VALUES ('IND-61cb5e2d:15388b28cf2:-7fb0', 'I000019', 'FLORES, WORGIE', 'ST 1, BRGY 1, CEBU, CEBU', null, 'INDIVIDUAL', null, null, null, 'FLORES, WORGIE', 'A327ff406:15388b2704e:-7ffa', null, null, null);

-- ----------------------------
-- Table structure for entitycontact
-- ----------------------------
DROP TABLE IF EXISTS `entitycontact`;
CREATE TABLE `entitycontact` (
  `objid` varchar(50) NOT NULL,
  `entityid` varchar(50) NOT NULL,
  `contacttype` varchar(25) NOT NULL,
  `contact` varchar(50) NOT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_entityid` (`entityid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of entitycontact
-- ----------------------------

-- ----------------------------
-- Table structure for entityid
-- ----------------------------
DROP TABLE IF EXISTS `entityid`;
CREATE TABLE `entityid` (
  `objid` varchar(50) NOT NULL,
  `entityid` varchar(50) NOT NULL,
  `idtype` varchar(50) NOT NULL,
  `idno` varchar(25) NOT NULL,
  `dtissued` date DEFAULT NULL,
  `dtexpiry` date DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_entityid` (`entityid`),
  KEY `ix_idtype` (`idtype`),
  KEY `ix_idno` (`idno`),
  KEY `ix_dtexpiry` (`dtexpiry`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of entityid
-- ----------------------------

-- ----------------------------
-- Table structure for entityindividual
-- ----------------------------
DROP TABLE IF EXISTS `entityindividual`;
CREATE TABLE `entityindividual` (
  `objid` varchar(50) NOT NULL,
  `lastname` varchar(100) DEFAULT NULL,
  `firstname` varchar(100) DEFAULT NULL,
  `middlename` varchar(100) DEFAULT NULL,
  `birthdate` date DEFAULT NULL,
  `birthplace` varchar(160) DEFAULT NULL,
  `citizenship` varchar(50) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `civilstatus` varchar(15) DEFAULT NULL,
  `profession` varchar(50) DEFAULT NULL,
  `tin` varchar(25) DEFAULT NULL,
  `sss` varchar(25) DEFAULT NULL,
  `height` varchar(10) DEFAULT NULL,
  `weight` varchar(10) DEFAULT NULL,
  `acr` varchar(50) DEFAULT NULL,
  `religion` varchar(50) DEFAULT NULL,
  `photo` mediumblob,
  `thumbnail` blob,
  PRIMARY KEY (`objid`),
  KEY `ix_lfname` (`lastname`,`firstname`),
  KEY `ix_fname` (`firstname`),
  KEY `ix_tin` (`tin`),
  KEY `ix_ss` (`sss`),
  CONSTRAINT `entityindividual_ibfk_1` FOREIGN KEY (`objid`) REFERENCES `entity` (`objid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of entityindividual
-- ----------------------------
INSERT INTO `entityindividual` VALUES ('IND-61cb5e2d:15388b28cf2:-7fb0', 'FLORES', 'WORGIE', null, null, null, null, 'M', null, null, null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for entity_address
-- ----------------------------
DROP TABLE IF EXISTS `entity_address`;
CREATE TABLE `entity_address` (
  `objid` varchar(50) NOT NULL DEFAULT '',
  `parentid` varchar(50) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `addresstype` varchar(50) DEFAULT NULL,
  `barangay_objid` varchar(50) DEFAULT NULL,
  `barangay_name` varchar(100) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `province` varchar(50) DEFAULT NULL,
  `municipality` varchar(50) DEFAULT NULL,
  `bldgno` varchar(50) DEFAULT NULL,
  `bldgname` varchar(50) DEFAULT NULL,
  `unitno` varchar(50) DEFAULT NULL,
  `street` varchar(100) DEFAULT NULL,
  `subdivision` varchar(100) DEFAULT NULL,
  `pin` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_parentid` (`parentid`),
  KEY `ix_barangay_objid` (`barangay_objid`),
  CONSTRAINT `entity_address_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `entity` (`objid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of entity_address
-- ----------------------------
INSERT INTO `entity_address` VALUES ('A327ff406:15388b2704e:-7ffa', 'IND-61cb5e2d:15388b28cf2:-7fb0', 'nonlocal', 'CITY', null, 'BRGY 1', 'CEBU', 'CEBU', '', null, null, null, 'ST 1', null, null);

-- ----------------------------
-- Table structure for entity_relation
-- ----------------------------
DROP TABLE IF EXISTS `entity_relation`;
CREATE TABLE `entity_relation` (
  `objid` varchar(50) NOT NULL,
  `entity_objid` varchar(50) DEFAULT NULL,
  `relateto_objid` varchar(50) DEFAULT NULL,
  `relation` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_sender_receiver` (`entity_objid`,`relateto_objid`),
  KEY `ix_sender_objid` (`entity_objid`) USING BTREE,
  KEY `ix_receiver_objid` (`relateto_objid`) USING BTREE,
  CONSTRAINT `fk_entityrelation_entity_objid` FOREIGN KEY (`entity_objid`) REFERENCES `entity` (`objid`),
  CONSTRAINT `fk_entityrelation_relateto_objid` FOREIGN KEY (`relateto_objid`) REFERENCES `entity` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of entity_relation
-- ----------------------------

-- ----------------------------
-- Table structure for formcontrol
-- ----------------------------
DROP TABLE IF EXISTS `formcontrol`;
CREATE TABLE `formcontrol` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) DEFAULT NULL,
  `dtfiled` datetime DEFAULT NULL,
  `formtype` varchar(25) DEFAULT NULL,
  `prefix` varchar(10) DEFAULT NULL,
  `branchid` varchar(50) DEFAULT NULL,
  `terminalid` varchar(50) DEFAULT NULL,
  `startseries` int(11) DEFAULT NULL,
  `endseries` int(11) DEFAULT NULL,
  `currentseries` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_formtype_prefix_branchid_terminalid` (`formtype`,`prefix`,`branchid`,`terminalid`) USING BTREE,
  KEY `ix_branchid` (`branchid`),
  KEY `ix_terminalid` (`terminalid`),
  KEY `ix_dtfiled` (`dtfiled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of formcontrol
-- ----------------------------

-- ----------------------------
-- Table structure for payout
-- ----------------------------
DROP TABLE IF EXISTS `payout`;
CREATE TABLE `payout` (
  `objid` varchar(50) NOT NULL,
  `dtfiled` datetime DEFAULT NULL,
  `controlid` varchar(50) DEFAULT NULL,
  `controlno` varchar(32) DEFAULT NULL,
  `partnerid` varchar(50) DEFAULT NULL,
  `terminalid` varchar(50) DEFAULT NULL,
  `branch_objid` varchar(50) DEFAULT NULL,
  `user_objid` varchar(50) DEFAULT NULL,
  `sendout_objid` varchar(50) DEFAULT NULL,
  `currency` varchar(5) DEFAULT NULL,
  `amount` decimal(16,2) DEFAULT NULL,
  `info` text,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_sendoutid` (`sendout_objid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_controlid` (`controlid`),
  KEY `ix_controlno` (`controlno`),
  KEY `ix_branchid` (`branch_objid`),
  KEY `ix_operatorid` (`user_objid`),
  KEY `ix_sendoutid` (`sendout_objid`),
  CONSTRAINT `fk_payout_sendout_objid` FOREIGN KEY (`sendout_objid`) REFERENCES `sendout` (`objid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of payout
-- ----------------------------

-- ----------------------------
-- Table structure for payout_daily
-- ----------------------------
DROP TABLE IF EXISTS `payout_daily`;
CREATE TABLE `payout_daily` (
  `receiverid` varchar(50) NOT NULL,
  `year` int(11) NOT NULL,
  `month` int(11) NOT NULL,
  `day` int(11) NOT NULL,
  `currency` varchar(10) NOT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`receiverid`,`year`,`month`,`day`,`currency`),
  KEY `ix_year_month_day` (`year`,`month`,`day`),
  KEY `ix_date` (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of payout_daily
-- ----------------------------

-- ----------------------------
-- Table structure for region
-- ----------------------------
DROP TABLE IF EXISTS `region`;
CREATE TABLE `region` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `zone_objid` varchar(50) DEFAULT NULL,
  `info` text,
  PRIMARY KEY (`objid`),
  KEY `ix_name` (`name`),
  KEY `ix_zone_objid` (`zone_objid`),
  CONSTRAINT `fk_region_zone_objid` FOREIGN KEY (`zone_objid`) REFERENCES `zone` (`objid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of region
-- ----------------------------

-- ----------------------------
-- Table structure for sendout
-- ----------------------------
DROP TABLE IF EXISTS `sendout`;
CREATE TABLE `sendout` (
  `objid` varchar(50) NOT NULL,
  `dtfiled` datetime DEFAULT NULL,
  `controlid` varchar(50) DEFAULT NULL,
  `controlno` varchar(32) DEFAULT NULL,
  `txnno` varchar(32) DEFAULT NULL,
  `txnrefid` varchar(50) DEFAULT NULL,
  `sendouttype` varchar(50) DEFAULT NULL,
  `partnerid` varchar(50) DEFAULT NULL,
  `branch_objid` varchar(50) DEFAULT NULL,
  `user_objid` varchar(50) DEFAULT NULL,
  `terminalid` varchar(50) DEFAULT NULL,
  `sender_objid` varchar(50) DEFAULT NULL,
  `sender_name` varchar(160) DEFAULT NULL,
  `sender_addresstext` varchar(255) DEFAULT NULL,
  `receiver_objid` varchar(50) DEFAULT NULL,
  `receiver_name` varchar(160) DEFAULT NULL,
  `receiver_addresstext` varchar(255) DEFAULT NULL,
  `purpose` varchar(255) DEFAULT NULL,
  `messagetoreceiver` varchar(255) DEFAULT NULL,
  `principal` decimal(16,2) DEFAULT NULL,
  `charge` decimal(10,2) DEFAULT NULL,
  `othercharge` decimal(10,2) DEFAULT NULL,
  `currency` varchar(10) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  `info` text,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_controlno` (`controlno`) USING BTREE,
  UNIQUE KEY `uix_txnno` (`txnno`) USING BTREE,
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_controlid` (`controlid`),
  KEY `ix_txnrefid` (`txnrefid`),
  KEY `ix_branchid` (`branch_objid`),
  KEY `ix_operatorid` (`user_objid`),
  KEY `ix_sender_objid` (`sender_objid`),
  KEY `ix_sender_name` (`sender_name`),
  KEY `ix_receiver_objid` (`receiver_objid`),
  KEY `ix_receiver_name` (`receiver_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sendout
-- ----------------------------

-- ----------------------------
-- Table structure for sendout_daily
-- ----------------------------
DROP TABLE IF EXISTS `sendout_daily`;
CREATE TABLE `sendout_daily` (
  `senderid` varchar(50) NOT NULL,
  `year` int(11) NOT NULL,
  `month` int(11) NOT NULL,
  `day` int(11) NOT NULL,
  `currency` varchar(10) NOT NULL,
  `amount` decimal(16,4) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`senderid`,`year`,`month`,`day`,`currency`),
  KEY `ix_year_month_day` (`year`,`month`,`day`),
  KEY `ix_date` (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sendout_daily
-- ----------------------------

-- ----------------------------
-- Table structure for sendout_unclaim
-- ----------------------------
DROP TABLE IF EXISTS `sendout_unclaim`;
CREATE TABLE `sendout_unclaim` (
  `objid` varchar(50) NOT NULL,
  PRIMARY KEY (`objid`),
  CONSTRAINT `sendout_unclaim_ibfk_1` FOREIGN KEY (`objid`) REFERENCES `sendout` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sendout_unclaim
-- ----------------------------

-- ----------------------------
-- Table structure for signatory
-- ----------------------------
DROP TABLE IF EXISTS `signatory`;
CREATE TABLE `signatory` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(10) NOT NULL,
  `doctype` varchar(50) NOT NULL,
  `indexno` int(11) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `middlename` varchar(50) DEFAULT NULL,
  `name` varchar(150) DEFAULT NULL,
  `title` varchar(50) NOT NULL,
  `department` varchar(50) DEFAULT NULL,
  `personnelid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_signatory_doctype` (`doctype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of signatory
-- ----------------------------

-- ----------------------------
-- Table structure for sms_inbox
-- ----------------------------
DROP TABLE IF EXISTS `sms_inbox`;
CREATE TABLE `sms_inbox` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) DEFAULT NULL,
  `dtfiled` datetime DEFAULT NULL,
  `channel` varchar(25) DEFAULT NULL,
  `keyword` varchar(50) DEFAULT NULL,
  `phoneno` varchar(15) DEFAULT NULL,
  `message` varchar(160) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_phoneno` (`phoneno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sms_inbox
-- ----------------------------

-- ----------------------------
-- Table structure for sms_inbox_pending
-- ----------------------------
DROP TABLE IF EXISTS `sms_inbox_pending`;
CREATE TABLE `sms_inbox_pending` (
  `objid` varchar(50) NOT NULL,
  `dtexpiry` datetime DEFAULT NULL,
  `dtretry` datetime DEFAULT NULL,
  `retrycount` smallint(6) DEFAULT '0',
  PRIMARY KEY (`objid`),
  KEY `ix_dtexpiry` (`dtexpiry`),
  KEY `ix_dtretry` (`dtretry`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sms_inbox_pending
-- ----------------------------

-- ----------------------------
-- Table structure for sms_outbox
-- ----------------------------
DROP TABLE IF EXISTS `sms_outbox`;
CREATE TABLE `sms_outbox` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) DEFAULT NULL,
  `dtfiled` datetime DEFAULT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `phoneno` varchar(15) DEFAULT NULL,
  `message` text,
  `creditcount` smallint(6) DEFAULT '0',
  `remarks` varchar(160) DEFAULT NULL,
  `dtsend` datetime DEFAULT NULL,
  `traceid` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_phoneno` (`phoneno`),
  KEY `ix_dtsend` (`dtsend`),
  KEY `ix_refid` (`refid`),
  KEY `ix_traceid` (`traceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sms_outbox
-- ----------------------------

-- ----------------------------
-- Table structure for sms_outbox_pending
-- ----------------------------
DROP TABLE IF EXISTS `sms_outbox_pending`;
CREATE TABLE `sms_outbox_pending` (
  `objid` varchar(50) NOT NULL,
  `dtexpiry` datetime DEFAULT NULL,
  `dtretry` datetime DEFAULT NULL,
  `retrycount` smallint(6) DEFAULT '0',
  PRIMARY KEY (`objid`),
  KEY `ix_dtexpiry` (`dtexpiry`),
  KEY `ix_dtretry` (`dtretry`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sms_outbox_pending
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dataset
-- ----------------------------
DROP TABLE IF EXISTS `sys_dataset`;
CREATE TABLE `sys_dataset` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `input` longtext,
  `output` longtext,
  `statement` varchar(50) DEFAULT NULL,
  `datasource` varchar(50) DEFAULT NULL,
  `servicename` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_dataset
-- ----------------------------

-- ----------------------------
-- Table structure for sys_notification
-- ----------------------------
DROP TABLE IF EXISTS `sys_notification`;
CREATE TABLE `sys_notification` (
  `notificationid` varchar(50) NOT NULL,
  `objid` varchar(50) DEFAULT NULL,
  `dtfiled` datetime DEFAULT NULL,
  `sender` varchar(160) DEFAULT NULL,
  `senderid` varchar(50) DEFAULT NULL,
  `recipientid` varchar(50) DEFAULT NULL,
  `recipienttype` varchar(50) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `filetype` varchar(50) DEFAULT NULL,
  `data` longtext,
  PRIMARY KEY (`notificationid`),
  KEY `ix_dtfiled` (`dtfiled`) USING BTREE,
  KEY `ix_senderid` (`senderid`) USING BTREE,
  KEY `ix_objid` (`objid`) USING BTREE,
  KEY `ix_recipientid` (`recipientid`) USING BTREE,
  KEY `ix_recipienttype` (`recipienttype`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_notification
-- ----------------------------

-- ----------------------------
-- Table structure for sys_org
-- ----------------------------
DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `orgclass` varchar(50) DEFAULT NULL,
  `parent_objid` varchar(50) DEFAULT NULL,
  `parent_orgclass` varchar(50) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `root` int(11) NOT NULL,
  PRIMARY KEY (`objid`),
  KEY `FK_sys_org` (`parent_orgclass`),
  KEY `FK_sys_org_orgclass` (`orgclass`),
  KEY `parent_objid` (`parent_objid`),
  CONSTRAINT `sys_org_ibfk_1` FOREIGN KEY (`parent_orgclass`) REFERENCES `sys_orgclass` (`name`) ON UPDATE CASCADE,
  CONSTRAINT `sys_org_ibfk_2` FOREIGN KEY (`orgclass`) REFERENCES `sys_orgclass` (`name`) ON UPDATE CASCADE,
  CONSTRAINT `sys_org_ibfk_3` FOREIGN KEY (`parent_objid`) REFERENCES `sys_org` (`objid`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_org
-- ----------------------------
INSERT INTO `sys_org` VALUES ('BR-442cf56e:15364b20825:-7fe8', 'FUENTE', 'BRANCH', 'JULIES', 'JULIES', '002', '0');
INSERT INTO `sys_org` VALUES ('JULIES', 'JULIES BAKESHOP', 'JULIES', null, null, 'JULIES', '1');

-- ----------------------------
-- Table structure for sys_orgclass
-- ----------------------------
DROP TABLE IF EXISTS `sys_orgclass`;
CREATE TABLE `sys_orgclass` (
  `name` varchar(50) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `parentclass` varchar(255) DEFAULT NULL,
  `handler` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_orgclass
-- ----------------------------
INSERT INTO `sys_orgclass` VALUES ('BRANCH', 'Branch', 'JULIES', 'branch');
INSERT INTO `sys_orgclass` VALUES ('JULIES', 'Julies Bakeshop', null, null);

-- ----------------------------
-- Table structure for sys_quarter
-- ----------------------------
DROP TABLE IF EXISTS `sys_quarter`;
CREATE TABLE `sys_quarter` (
  `qtrid` int(11) NOT NULL,
  PRIMARY KEY (`qtrid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_quarter
-- ----------------------------

-- ----------------------------
-- Table structure for sys_report
-- ----------------------------
DROP TABLE IF EXISTS `sys_report`;
CREATE TABLE `sys_report` (
  `objid` varchar(50) NOT NULL,
  `reportfolderid` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `filetype` varchar(25) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `datasetid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `FK_sys_report_dataset` (`datasetid`),
  KEY `FK_sys_report_entry_folder` (`reportfolderid`),
  CONSTRAINT `sys_report_ibfk_1` FOREIGN KEY (`datasetid`) REFERENCES `sys_dataset` (`objid`),
  CONSTRAINT `sys_report_ibfk_2` FOREIGN KEY (`reportfolderid`) REFERENCES `sys_report_folder` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_report
-- ----------------------------

-- ----------------------------
-- Table structure for sys_report_admin
-- ----------------------------
DROP TABLE IF EXISTS `sys_report_admin`;
CREATE TABLE `sys_report_admin` (
  `objid` varchar(50) NOT NULL,
  `userid` varchar(50) DEFAULT NULL,
  `reportfolderid` varchar(50) DEFAULT NULL,
  `exclude` longtext,
  PRIMARY KEY (`objid`),
  KEY `FK_sys_report_admin_folder` (`reportfolderid`),
  KEY `FK_sys_report_admin_user` (`userid`),
  CONSTRAINT `sys_report_admin_ibfk_1` FOREIGN KEY (`reportfolderid`) REFERENCES `sys_report_folder` (`objid`),
  CONSTRAINT `sys_report_admin_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `sys_user` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_report_admin
-- ----------------------------

-- ----------------------------
-- Table structure for sys_report_folder
-- ----------------------------
DROP TABLE IF EXISTS `sys_report_folder`;
CREATE TABLE `sys_report_folder` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_report_folder
-- ----------------------------

-- ----------------------------
-- Table structure for sys_report_member
-- ----------------------------
DROP TABLE IF EXISTS `sys_report_member`;
CREATE TABLE `sys_report_member` (
  `objid` varchar(50) NOT NULL,
  `reportfolderid` varchar(50) DEFAULT NULL,
  `userid` varchar(50) DEFAULT NULL,
  `usergroupid` varchar(50) DEFAULT NULL,
  `exclude` longtext,
  PRIMARY KEY (`objid`),
  KEY `FK_sys_report_member_folder` (`reportfolderid`),
  KEY `FK_sys_report_member_user` (`userid`),
  KEY `FK_sys_report_member_usergroup` (`usergroupid`),
  CONSTRAINT `sys_report_member_ibfk_1` FOREIGN KEY (`reportfolderid`) REFERENCES `sys_report_folder` (`objid`),
  CONSTRAINT `sys_report_member_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `sys_user` (`objid`),
  CONSTRAINT `sys_report_member_ibfk_3` FOREIGN KEY (`usergroupid`) REFERENCES `sys_usergroup` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_report_member
-- ----------------------------

-- ----------------------------
-- Table structure for sys_rule
-- ----------------------------
DROP TABLE IF EXISTS `sys_rule`;
CREATE TABLE `sys_rule` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(25) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `ruleset` varchar(50) NOT NULL,
  `rulegroup` varchar(50) DEFAULT NULL,
  `title` varchar(250) DEFAULT NULL,
  `description` longtext,
  `salience` int(11) DEFAULT NULL,
  `effectivefrom` date DEFAULT NULL,
  `effectiveto` date DEFAULT NULL,
  `dtfiled` datetime DEFAULT NULL,
  `user_objid` varchar(50) DEFAULT NULL,
  `user_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `rulegroup` (`rulegroup`,`ruleset`),
  KEY `ruleset` (`ruleset`),
  CONSTRAINT `sys_rule_ibfk_1` FOREIGN KEY (`rulegroup`, `ruleset`) REFERENCES `sys_rulegroup` (`name`, `ruleset`),
  CONSTRAINT `sys_rule_ibfk_2` FOREIGN KEY (`ruleset`) REFERENCES `sys_ruleset` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rule
-- ----------------------------
INSERT INTO `sys_rule` VALUES ('RUL-5c396351:15397edc18a:-7f0e', 'DEPLOYED', 'AML_SENDOUT_LIMIT_50000', 'aml', 'aml-initial', 'AML_SENDOUT_LIMIT_50000', null, '50000', null, null, '2016-03-21 14:51:41', 'USR-2160bfa3:15383af7f25:-7f5c', 'FUENT1');
INSERT INTO `sys_rule` VALUES ('RUL47131029:153973c4a2c:-7f77', 'DEPLOYED', 'CALCULATE_CHARGE', 'sendout', 'sendout-initial', 'CALCULATE CHARGE', null, '50000', null, null, '2016-03-21 11:44:27', 'USR-ADMIN', 'ADMIN');

-- ----------------------------
-- Table structure for sys_rulegroup
-- ----------------------------
DROP TABLE IF EXISTS `sys_rulegroup`;
CREATE TABLE `sys_rulegroup` (
  `name` varchar(50) NOT NULL,
  `ruleset` varchar(50) NOT NULL,
  `title` varchar(160) DEFAULT NULL,
  `sortorder` int(11) DEFAULT NULL,
  PRIMARY KEY (`name`,`ruleset`),
  KEY `ruleset` (`ruleset`),
  CONSTRAINT `sys_rulegroup_ibfk_1` FOREIGN KEY (`ruleset`) REFERENCES `sys_ruleset` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rulegroup
-- ----------------------------
INSERT INTO `sys_rulegroup` VALUES ('aml-initial', 'aml', 'Initial', '0');
INSERT INTO `sys_rulegroup` VALUES ('sendout-initial', 'sendout', 'Initial', '0');

-- ----------------------------
-- Table structure for sys_ruleset
-- ----------------------------
DROP TABLE IF EXISTS `sys_ruleset`;
CREATE TABLE `sys_ruleset` (
  `name` varchar(50) NOT NULL,
  `title` varchar(160) DEFAULT NULL,
  `packagename` varchar(50) DEFAULT NULL,
  `domain` varchar(50) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  `permission` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_ruleset
-- ----------------------------
INSERT INTO `sys_ruleset` VALUES ('aml', 'AML', 'mt.aml', 'AML', null, null);
INSERT INTO `sys_ruleset` VALUES ('sendout', 'Sendout', 'mt.sendout', 'MT', null, null);

-- ----------------------------
-- Table structure for sys_ruleset_actiondef
-- ----------------------------
DROP TABLE IF EXISTS `sys_ruleset_actiondef`;
CREATE TABLE `sys_ruleset_actiondef` (
  `ruleset` varchar(50) NOT NULL,
  `actiondef` varchar(50) NOT NULL,
  PRIMARY KEY (`ruleset`,`actiondef`),
  KEY `actiondef` (`actiondef`),
  CONSTRAINT `sys_ruleset_actiondef_ibfk_2` FOREIGN KEY (`ruleset`) REFERENCES `sys_ruleset` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_ruleset_actiondef
-- ----------------------------
INSERT INTO `sys_ruleset_actiondef` VALUES ('sendout', 'RULADEF-1b73a44f:15397014839:-7d3a');
INSERT INTO `sys_ruleset_actiondef` VALUES ('aml', 'RULADEF-5c396351:15397edc18a:-7f46');

-- ----------------------------
-- Table structure for sys_ruleset_fact
-- ----------------------------
DROP TABLE IF EXISTS `sys_ruleset_fact`;
CREATE TABLE `sys_ruleset_fact` (
  `ruleset` varchar(50) NOT NULL,
  `rulefact` varchar(50) NOT NULL,
  PRIMARY KEY (`ruleset`,`rulefact`),
  KEY `rulefact` (`rulefact`),
  CONSTRAINT `sys_ruleset_fact_ibfk_2` FOREIGN KEY (`ruleset`) REFERENCES `sys_ruleset` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_ruleset_fact
-- ----------------------------
INSERT INTO `sys_ruleset_fact` VALUES ('sendout', 'RULFACT-1b73a44f:15397014839:-7e92');
INSERT INTO `sys_ruleset_fact` VALUES ('sendout', 'RULFACT-1b73a44f:15397014839:-7ef5');
INSERT INTO `sys_ruleset_fact` VALUES ('sendout', 'RULFACT-1b73a44f:15397014839:-7fe5');
INSERT INTO `sys_ruleset_fact` VALUES ('aml', 'RULFACT-5c396351:15397edc18a:-7fa6');

-- ----------------------------
-- Table structure for sys_rule_action
-- ----------------------------
DROP TABLE IF EXISTS `sys_rule_action`;
CREATE TABLE `sys_rule_action` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `actiondef_objid` varchar(50) DEFAULT NULL,
  `actiondef_name` varchar(50) DEFAULT NULL,
  `pos` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `parentid` (`parentid`),
  CONSTRAINT `sys_rule_action_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `sys_rule` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rule_action
-- ----------------------------
INSERT INTO `sys_rule_action` VALUES ('RACT-5c396351:15397edc18a:-7eb2', 'RUL-5c396351:15397edc18a:-7f0e', 'RULADEF-5c396351:15397edc18a:-7f46', 'collectexception', '0');
INSERT INTO `sys_rule_action` VALUES ('RACT47131029:153973c4a2c:-7e11', 'RUL47131029:153973c4a2c:-7f77', 'RULADEF-1b73a44f:15397014839:-7d3a', 'calculatecharge', '0');

-- ----------------------------
-- Table structure for sys_rule_actiondef
-- ----------------------------
DROP TABLE IF EXISTS `sys_rule_actiondef`;
CREATE TABLE `sys_rule_actiondef` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `title` varchar(250) DEFAULT NULL,
  `sortorder` int(11) DEFAULT NULL,
  `actionname` varchar(50) DEFAULT NULL,
  `domain` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rule_actiondef
-- ----------------------------
INSERT INTO `sys_rule_actiondef` VALUES ('RULADEF-1b73a44f:15397014839:-7d3a', 'calculatecharge', 'Calculate Charge', '0', 'calculatecharge', null);
INSERT INTO `sys_rule_actiondef` VALUES ('RULADEF-5c396351:15397edc18a:-7f46', 'collectexception', 'Collect Exception', '0', 'collectexception', null);

-- ----------------------------
-- Table structure for sys_rule_actiondef_param
-- ----------------------------
DROP TABLE IF EXISTS `sys_rule_actiondef_param`;
CREATE TABLE `sys_rule_actiondef_param` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `sortorder` int(11) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `datatype` varchar(50) DEFAULT NULL,
  `handler` varchar(50) DEFAULT NULL,
  `lookuphandler` varchar(50) DEFAULT NULL,
  `lookupkey` varchar(50) DEFAULT NULL,
  `lookupvalue` varchar(50) DEFAULT NULL,
  `vardatatype` varchar(50) DEFAULT NULL,
  `lovname` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `parentid` (`parentid`),
  CONSTRAINT `sys_rule_actiondef_param_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `sys_rule_actiondef` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rule_actiondef_param
-- ----------------------------
INSERT INTO `sys_rule_actiondef_param` VALUES ('ACTPARAM-1b73a44f:15397014839:-7d0c', 'RULADEF-1b73a44f:15397014839:-7d3a', 'amount', '1', 'Amount', null, 'expression', null, null, null, null, null);
INSERT INTO `sys_rule_actiondef_param` VALUES ('ACTPARAM-5c396351:15397edc18a:-7f24', 'RULADEF-5c396351:15397edc18a:-7f46', 'message', '1', 'Message', null, 'expression', null, null, null, null, null);

-- ----------------------------
-- Table structure for sys_rule_action_param
-- ----------------------------
DROP TABLE IF EXISTS `sys_rule_action_param`;
CREATE TABLE `sys_rule_action_param` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `actiondefparam_objid` varchar(50) DEFAULT NULL,
  `stringvalue` varchar(255) DEFAULT NULL,
  `booleanvalue` int(11) DEFAULT NULL,
  `var_objid` varchar(50) DEFAULT NULL,
  `var_name` varchar(50) DEFAULT NULL,
  `expr` longtext,
  `exprtype` varchar(25) DEFAULT NULL,
  `pos` int(11) DEFAULT NULL,
  `obj_key` varchar(50) DEFAULT NULL,
  `obj_value` varchar(255) DEFAULT NULL,
  `listvalue` longtext,
  `lov` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `parentid` (`parentid`),
  KEY `var_objid` (`var_objid`),
  CONSTRAINT `sys_rule_action_param_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `sys_rule_action` (`objid`),
  CONSTRAINT `sys_rule_action_param_ibfk_2` FOREIGN KEY (`var_objid`) REFERENCES `sys_rule_condition_var` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rule_action_param
-- ----------------------------
INSERT INTO `sys_rule_action_param` VALUES ('RULACT-5c396351:15397edc18a:-7eb1', 'RACT-5c396351:15397edc18a:-7eb2', 'ACTPARAM-5c396351:15397edc18a:-7f24', null, null, null, null, '\'Amount must not be greater than 50,000.00\'', 'expression', null, null, null, null, null);
INSERT INTO `sys_rule_action_param` VALUES ('RULACT47131029:153973c4a2c:-7e10', 'RACT47131029:153973c4a2c:-7e11', 'ACTPARAM-1b73a44f:15397014839:-7d0c', null, null, 'RCONST47131029:153973c4a2c:-7f00', 'AMT', '<table border=\"1\" cellpadding=\"1\" cellspacing=\"0\">\n    <tr>\n        <td colspan=\"3\">Use Variable: AMT</td>\n    </tr>\n    <tr>\n        <th>Greater than or equal to</th>\n        <th>Less than</th>\n        <th>Formula</th>\n    </tr>\n    \n        <tr>\n            <td></td>\n            <td>300.00</td>\n            <td>5</td>\n        </tr>\n    \n        <tr>\n            <td>300.00</td>\n            <td>500.00</td>\n            <td>8</td>\n        </tr>\n    \n        <tr>\n            <td>500.00</td>\n            <td>700.00</td>\n            <td>10</td>\n        </tr>\n    \n        <tr>\n            <td>700.00</td>\n            <td>900.00</td>\n            <td>12</td>\n        </tr>\n    \n        <tr>\n            <td>900.00</td>\n            <td>1000.00</td>\n            <td>15</td>\n        </tr>\n    \n        <tr>\n            <td>1000.00</td>\n            <td>1500.00</td>\n            <td>20</td>\n        </tr>\n    \n        <tr>\n            <td>1500.00</td>\n            <td>2000.00</td>\n            <td>30</td>\n        </tr>\n    \n        <tr>\n            <td>2000.00</td>\n            <td>2500.00</td>\n            <td>40</td>\n        </tr>\n    \n        <tr>\n            <td>2500.00</td>\n            <td>3000.00</td>\n            <td>50</td>\n        </tr>\n    \n        <tr>\n            <td>3000.00</td>\n            <td>3500.00</td>\n            <td>60</td>\n        </tr>\n    \n        <tr>\n            <td>3500.00</td>\n            <td>4000.00</td>\n            <td>70</td>\n        </tr>\n    \n        <tr>\n            <td>4000.00</td>\n            <td>5000.00</td>\n            <td>90</td>\n        </tr>\n    \n        <tr>\n            <td>5000.00</td>\n            <td>7000.00</td>\n            <td>115</td>\n        </tr>\n    \n        <tr>\n            <td>7000.00</td>\n            <td>9500.00</td>\n            <td>125</td>\n        </tr>\n    \n        <tr>\n            <td>9500.00</td>\n            <td>10000.00</td>\n            <td>140</td>\n        </tr>\n    \n        <tr>\n            <td>10000.00</td>\n            <td>14000.00</td>\n            <td>210</td>\n        </tr>\n    \n        <tr>\n            <td>14000.00</td>\n            <td>15000.00</td>\n            <td>220</td>\n        </tr>\n    \n        <tr>\n            <td>15000.00</td>\n            <td>20000.00</td>\n            <td>250</td>\n        </tr>\n    \n        <tr>\n            <td>20000.00</td>\n            <td>30000.00</td>\n            <td>290</td>\n        </tr>\n    \n        <tr>\n            <td>30000.00</td>\n            <td>40000.00</td>\n            <td>320</td>\n        </tr>\n    \n        <tr>\n            <td>40000.00</td>\n            <td>50000.00</td>\n            <td>345</td>\n        </tr>\n    \n</table>', 'range', null, null, null, '[[to:300.00,value:\"5\",from:0],[to:500.00,value:\"8\",from:300.00],[to:700.00,value:\"10\",from:500.00],[to:900.00,value:\"12\",from:700.00],[to:1000.00,value:\"15\",from:900.00],[to:1500.00,value:\"20\",from:1000.00],[to:2000.00,value:\"30\",from:1500.00],[to:2500.00,value:\"40\",from:2000.00],[to:3000.00,value:\"50\",from:2500.00],[to:3500.00,value:\"60\",from:3000.00],[to:4000.00,value:\"70\",from:3500.00],[to:5000.00,value:\"90\",from:4000.00],[to:7000.00,value:\"115\",from:5000.00],[to:9500.00,value:\"125\",from:7000.00],[to:10000.00,value:\"140\",from:9500.00],[to:14000.00,value:\"210\",from:10000.00],[to:15000.00,value:\"220\",from:14000.00],[to:20000.00,value:\"250\",from:15000.00],[to:30000.00,value:\"290\",from:20000.00],[to:40000.00,value:\"320\",from:30000.00],[to:50000.00,value:\"345\",from:40000.00]]', null);

-- ----------------------------
-- Table structure for sys_rule_condition
-- ----------------------------
DROP TABLE IF EXISTS `sys_rule_condition`;
CREATE TABLE `sys_rule_condition` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `fact_name` varchar(50) DEFAULT NULL,
  `fact_objid` varchar(50) DEFAULT NULL,
  `varname` varchar(50) DEFAULT NULL,
  `pos` int(11) DEFAULT NULL,
  `ruletext` longtext,
  `displaytext` longtext,
  `dynamic_datatype` varchar(50) DEFAULT NULL,
  `dynamic_key` varchar(50) DEFAULT NULL,
  `dynamic_value` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `fact_objid` (`fact_objid`),
  KEY `parentid` (`parentid`),
  CONSTRAINT `sys_rule_condition_ibfk_1` FOREIGN KEY (`fact_objid`) REFERENCES `sys_rule_fact` (`objid`),
  CONSTRAINT `sys_rule_condition_ibfk_2` FOREIGN KEY (`parentid`) REFERENCES `sys_rule` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rule_condition
-- ----------------------------
INSERT INTO `sys_rule_condition` VALUES ('RCOND-5c396351:15397edc18a:-7ef3', 'RUL-5c396351:15397edc18a:-7f0e', 'sendoutdaily', 'RULFACT-5c396351:15397edc18a:-7fa6', null, '0', null, null, null, null, null);
INSERT INTO `sys_rule_condition` VALUES ('RCOND47131029:153973c4a2c:-7f3a', 'RUL47131029:153973c4a2c:-7f77', 'sendout', 'RULFACT-1b73a44f:15397014839:-7fe5', null, '0', null, null, null, null, null);

-- ----------------------------
-- Table structure for sys_rule_condition_constraint
-- ----------------------------
DROP TABLE IF EXISTS `sys_rule_condition_constraint`;
CREATE TABLE `sys_rule_condition_constraint` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `field_objid` varchar(50) DEFAULT NULL,
  `fieldname` varchar(50) DEFAULT NULL,
  `varname` varchar(50) DEFAULT NULL,
  `operator_caption` varchar(50) DEFAULT NULL,
  `operator_symbol` varchar(50) DEFAULT NULL,
  `usevar` int(11) DEFAULT NULL,
  `var_objid` varchar(50) DEFAULT NULL,
  `var_name` varchar(50) DEFAULT NULL,
  `decimalvalue` decimal(16,2) DEFAULT NULL,
  `intvalue` int(11) DEFAULT NULL,
  `stringvalue` varchar(255) DEFAULT NULL,
  `listvalue` longtext,
  `datevalue` date DEFAULT NULL,
  `pos` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `parentid` (`parentid`),
  KEY `var_objid` (`var_objid`),
  CONSTRAINT `sys_rule_condition_constraint_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `sys_rule_condition` (`objid`),
  CONSTRAINT `sys_rule_condition_constraint_ibfk_2` FOREIGN KEY (`var_objid`) REFERENCES `sys_rule_condition_var` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rule_condition_constraint
-- ----------------------------
INSERT INTO `sys_rule_condition_constraint` VALUES ('RCONST-5c396351:15397edc18a:-7ed2', 'RCOND-5c396351:15397edc18a:-7ef3', 'FACTFLD-5c396351:15397edc18a:-7f79', 'amount', 'AMT', 'greater than', '>', null, null, null, '50000.00', null, null, null, null, '0');
INSERT INTO `sys_rule_condition_constraint` VALUES ('RCONST47131029:153973c4a2c:-7f00', 'RCOND47131029:153973c4a2c:-7f3a', 'FACTFLD-1b73a44f:15397014839:-7f50', 'amount', 'AMT', null, null, null, null, null, null, null, null, null, null, '0');

-- ----------------------------
-- Table structure for sys_rule_condition_var
-- ----------------------------
DROP TABLE IF EXISTS `sys_rule_condition_var`;
CREATE TABLE `sys_rule_condition_var` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `ruleid` varchar(50) DEFAULT NULL,
  `varname` varchar(50) DEFAULT NULL,
  `datatype` varchar(50) DEFAULT NULL,
  `pos` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `parentid` (`parentid`),
  CONSTRAINT `sys_rule_condition_var_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `sys_rule_condition` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rule_condition_var
-- ----------------------------
INSERT INTO `sys_rule_condition_var` VALUES ('RCONST-5c396351:15397edc18a:-7ed2', 'RCOND-5c396351:15397edc18a:-7ef3', 'RUL-5c396351:15397edc18a:-7f0e', 'AMT', 'decimal', '0');
INSERT INTO `sys_rule_condition_var` VALUES ('RCONST47131029:153973c4a2c:-7f00', 'RCOND47131029:153973c4a2c:-7f3a', 'RUL47131029:153973c4a2c:-7f77', 'AMT', 'decimal', '0');

-- ----------------------------
-- Table structure for sys_rule_deployed
-- ----------------------------
DROP TABLE IF EXISTS `sys_rule_deployed`;
CREATE TABLE `sys_rule_deployed` (
  `objid` varchar(50) NOT NULL,
  `ruletext` longtext,
  PRIMARY KEY (`objid`),
  CONSTRAINT `sys_rule_deployed_ibfk_1` FOREIGN KEY (`objid`) REFERENCES `sys_rule` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rule_deployed
-- ----------------------------
INSERT INTO `sys_rule_deployed` VALUES ('RUL-5c396351:15397edc18a:-7f0e', '\npackage aml.AML_SENDOUT_LIMIT_50000;\nimport aml.*;\nimport java.util.*;\nimport com.rameses.rules.common.*;\n\nglobal RuleAction action;\n\nrule \"AML_SENDOUT_LIMIT_50000\"\n	agenda-group \"aml-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		\n		 aml.facts.SendoutDaily (  AMT:amount > 50000.00 )\n		\n	then\n		Map bindings = new HashMap();\n		\n		bindings.put(\"AMT\", AMT );\n		\n	Map _p0 = new HashMap();\r\n_p0.put( \"message\", (new ActionExpression(\"\'Amount must not be greater than 50,000.00\'\", bindings)) );\r\naction.execute( \"collectexception\",_p0,drools);\r\n\nend\n\n\n	');
INSERT INTO `sys_rule_deployed` VALUES ('RUL47131029:153973c4a2c:-7f77', '\npackage sendout.CALCULATE_CHARGE;\nimport sendout.*;\nimport java.util.*;\nimport com.rameses.rules.common.*;\n\nglobal RuleAction action;\n\nrule \"CALCULATE_CHARGE\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		\n		 mt.facts.Sendout (  AMT:amount )\n		\n	then\n		Map bindings = new HashMap();\n		\n		bindings.put(\"AMT\", AMT );\n		\n	RangeEntry re0 = new RangeEntry(\"CALCULATE_CHARGE\");\r\nre0.setBindings(bindings);\r\nre0.setDecimalvalue(AMT);\r\nre0.getParams().put( \"amount\", 0.0 );\r\ninsert(re0);\r\n\nend\n\n\n	\nrule \"calculatecharge_0_0\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue < 300.00 )\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"5\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_1\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 300.00, decimalvalue < 500.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"8\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_2\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 500.00, decimalvalue < 700.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"10\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_3\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 700.00, decimalvalue < 900.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"12\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_4\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 900.00, decimalvalue < 1000.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"15\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_5\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 1000.00, decimalvalue < 1500.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"20\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_6\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 1500.00, decimalvalue < 2000.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"30\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_7\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 2000.00, decimalvalue < 2500.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"40\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_8\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 2500.00, decimalvalue < 3000.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"50\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_9\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 3000.00, decimalvalue < 3500.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"60\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_10\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 3500.00, decimalvalue < 4000.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"70\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_11\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 4000.00, decimalvalue < 5000.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"90\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_12\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 5000.00, decimalvalue < 7000.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"115\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_13\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 7000.00, decimalvalue < 9500.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"125\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_14\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 9500.00, decimalvalue < 10000.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"140\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_15\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 10000.00, decimalvalue < 14000.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"210\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_16\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 14000.00, decimalvalue < 15000.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"220\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_17\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 15000.00, decimalvalue < 20000.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"250\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_18\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 20000.00, decimalvalue < 30000.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"290\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_19\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 30000.00, decimalvalue < 40000.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"320\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	\nrule \"calculatecharge_0_20\"\n	agenda-group \"sendout-initial\"\n	salience 50000\n	no-loop\n	when\n		\n		rv: RangeEntry( id==\"CALCULATE_CHARGE\", decimalvalue >= 40000.00, decimalvalue < 50000.00 )\n		\n		\n		\n	then\n		Map bindings = rv.getBindings();\n		Map params = rv.getParams();\n		params.put( \"amount\", (new ActionExpression(\"345\", bindings)) );	\n		 \n		action.execute( \"calculatecharge\",params, drools);\nend\n\n\n	');

-- ----------------------------
-- Table structure for sys_rule_fact
-- ----------------------------
DROP TABLE IF EXISTS `sys_rule_fact`;
CREATE TABLE `sys_rule_fact` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `title` varchar(160) DEFAULT NULL,
  `factclass` varchar(50) DEFAULT NULL,
  `sortorder` int(11) DEFAULT NULL,
  `handler` varchar(50) DEFAULT NULL,
  `defaultvarname` varchar(25) DEFAULT NULL,
  `dynamic` int(11) DEFAULT NULL,
  `lookuphandler` varchar(50) DEFAULT NULL,
  `lookupkey` varchar(50) DEFAULT NULL,
  `lookupvalue` varchar(50) DEFAULT NULL,
  `lookupdatatype` varchar(50) DEFAULT NULL,
  `dynamicfieldname` varchar(50) DEFAULT NULL,
  `builtinconstraints` varchar(50) DEFAULT NULL,
  `domain` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rule_fact
-- ----------------------------
INSERT INTO `sys_rule_fact` VALUES ('RULFACT-1b73a44f:15397014839:-7e92', 'partner', 'Partner', 'mt.sendout.Partner', '2', null, null, null, null, null, null, null, null, null, 'mt');
INSERT INTO `sys_rule_fact` VALUES ('RULFACT-1b73a44f:15397014839:-7ef5', 'sender', 'Sender', 'mt.sendout.Sender', '1', null, null, null, null, null, null, null, null, null, 'mt');
INSERT INTO `sys_rule_fact` VALUES ('RULFACT-1b73a44f:15397014839:-7fe5', 'sendout', 'Sendout', 'mt.facts.Sendout', '0', null, null, null, null, null, null, null, null, null, 'mt');
INSERT INTO `sys_rule_fact` VALUES ('RULFACT-5c396351:15397edc18a:-7fa6', 'sendoutdaily', 'Sendout Daily', 'aml.facts.SendoutDaily', '0', null, null, null, null, null, null, null, null, null, 'aml');

-- ----------------------------
-- Table structure for sys_rule_fact_field
-- ----------------------------
DROP TABLE IF EXISTS `sys_rule_fact_field`;
CREATE TABLE `sys_rule_fact_field` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `title` varchar(160) DEFAULT NULL,
  `datatype` varchar(50) DEFAULT NULL,
  `sortorder` int(11) DEFAULT NULL,
  `handler` varchar(50) DEFAULT NULL,
  `lookuphandler` varchar(50) DEFAULT NULL,
  `lookupkey` varchar(50) DEFAULT NULL,
  `lookupvalue` varchar(50) DEFAULT NULL,
  `lookupdatatype` varchar(50) DEFAULT NULL,
  `multivalued` int(11) DEFAULT NULL,
  `required` int(11) DEFAULT NULL,
  `vardatatype` varchar(50) DEFAULT NULL,
  `lovname` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `parentid` (`parentid`),
  CONSTRAINT `sys_rule_fact_field_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `sys_rule_fact` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rule_fact_field
-- ----------------------------
INSERT INTO `sys_rule_fact_field` VALUES ('FACTFLD-1b73a44f:15397014839:-7e75', 'RULFACT-1b73a44f:15397014839:-7e92', 'type', 'Type', 'string', '1', 'string', null, null, null, null, null, null, 'string', null);
INSERT INTO `sys_rule_fact_field` VALUES ('FACTFLD-1b73a44f:15397014839:-7ec5', 'RULFACT-1b73a44f:15397014839:-7ef5', 'credits', 'Credits', 'decimal', '2', 'decimal', null, null, null, null, null, null, 'decimal', null);
INSERT INTO `sys_rule_fact_field` VALUES ('FACTFLD-1b73a44f:15397014839:-7ed9', 'RULFACT-1b73a44f:15397014839:-7ef5', 'senior', 'Senior', 'boolean', '1', 'boolean', null, null, null, null, null, null, 'boolean', null);
INSERT INTO `sys_rule_fact_field` VALUES ('FACTFLD-1b73a44f:15397014839:-7f3e', 'RULFACT-1b73a44f:15397014839:-7fe5', 'total', 'Total', 'decimal', '8', 'decimal', null, null, null, null, null, null, 'decimal', null);
INSERT INTO `sys_rule_fact_field` VALUES ('FACTFLD-1b73a44f:15397014839:-7f47', 'RULFACT-1b73a44f:15397014839:-7fe5', 'charge', 'Charge', 'decimal', '7', 'decimal', null, null, null, null, null, null, 'decimal', null);
INSERT INTO `sys_rule_fact_field` VALUES ('FACTFLD-1b73a44f:15397014839:-7f50', 'RULFACT-1b73a44f:15397014839:-7fe5', 'amount', 'Amount', 'decimal', '6', 'decimal', null, null, null, null, null, null, 'decimal', null);
INSERT INTO `sys_rule_fact_field` VALUES ('FACTFLD-1b73a44f:15397014839:-7f59', 'RULFACT-1b73a44f:15397014839:-7fe5', 'currency', 'Currency', 'string', '5', 'lov', null, null, null, null, null, null, 'string', 'CURRENCY_TYPES');
INSERT INTO `sys_rule_fact_field` VALUES ('FACTFLD-1b73a44f:15397014839:-7f6a', 'RULFACT-1b73a44f:15397014839:-7fe5', 'region', 'Region', 'string', '4', 'lookup', 'region:lookup', 'objid', 'name', null, null, null, 'string', null);
INSERT INTO `sys_rule_fact_field` VALUES ('FACTFLD-1b73a44f:15397014839:-7fa6', 'RULFACT-1b73a44f:15397014839:-7fe5', 'area', 'Area', 'string', '3', 'lookup', 'area:lookup', 'objid', 'name', null, null, null, 'string', null);
INSERT INTO `sys_rule_fact_field` VALUES ('FACTFLD-1b73a44f:15397014839:-7fb7', 'RULFACT-1b73a44f:15397014839:-7fe5', 'zone', 'Zone', 'string', '2', 'lookup', 'zone:lookup', 'objid', 'name', null, null, null, 'string', null);
INSERT INTO `sys_rule_fact_field` VALUES ('FACTFLD-1b73a44f:15397014839:-7fc9', 'RULFACT-1b73a44f:15397014839:-7fe5', 'branch', 'Branch', 'string', '1', 'lookup', 'branch:lookup', 'objid', 'name', null, null, null, 'string', null);
INSERT INTO `sys_rule_fact_field` VALUES ('FACTFLD-5c396351:15397edc18a:-7f79', 'RULFACT-5c396351:15397edc18a:-7fa6', 'amount', 'Amount', 'decimal', '3', 'decimal', null, null, null, null, null, null, 'decimal', null);
INSERT INTO `sys_rule_fact_field` VALUES ('FACTFLD-5c396351:15397edc18a:-7f82', 'RULFACT-5c396351:15397edc18a:-7fa6', 'currency', 'Currency', 'string', '2', 'string', null, null, null, null, null, null, 'string', null);
INSERT INTO `sys_rule_fact_field` VALUES ('FACTFLD-5c396351:15397edc18a:-7f8c', 'RULFACT-5c396351:15397edc18a:-7fa6', 'senderid', 'Sender', 'string', '1', 'string', null, null, null, null, null, null, 'string', null);

-- ----------------------------
-- Table structure for sys_script
-- ----------------------------
DROP TABLE IF EXISTS `sys_script`;
CREATE TABLE `sys_script` (
  `name` varchar(50) NOT NULL,
  `title` longblob,
  `content` longtext,
  `category` varchar(20) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_script
-- ----------------------------

-- ----------------------------
-- Table structure for sys_securitygroup
-- ----------------------------
DROP TABLE IF EXISTS `sys_securitygroup`;
CREATE TABLE `sys_securitygroup` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `usergroup_objid` varchar(50) DEFAULT NULL,
  `exclude` longtext,
  PRIMARY KEY (`objid`),
  KEY `FK_sys_securitygroup_usergroup` (`usergroup_objid`),
  CONSTRAINT `sys_securitygroup_ibfk_1` FOREIGN KEY (`usergroup_objid`) REFERENCES `sys_usergroup` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_securitygroup
-- ----------------------------

-- ----------------------------
-- Table structure for sys_sequence
-- ----------------------------
DROP TABLE IF EXISTS `sys_sequence`;
CREATE TABLE `sys_sequence` (
  `objid` varchar(100) NOT NULL,
  `nextSeries` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_sequence
-- ----------------------------
INSERT INTO `sys_sequence` VALUES ('INDIVIDUAL_ENTITY', '23');

-- ----------------------------
-- Table structure for sys_session
-- ----------------------------
DROP TABLE IF EXISTS `sys_session`;
CREATE TABLE `sys_session` (
  `sessionid` varchar(50) NOT NULL,
  `userid` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `clienttype` varchar(12) DEFAULT NULL,
  `accesstime` datetime DEFAULT NULL,
  `accessexpiry` datetime DEFAULT NULL,
  `timein` datetime DEFAULT NULL,
  PRIMARY KEY (`sessionid`),
  KEY `ix_timein` (`timein`),
  KEY `ix_userid` (`userid`),
  KEY `ix_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for sys_session_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_session_log`;
CREATE TABLE `sys_session_log` (
  `sessionid` varchar(50) NOT NULL,
  `userid` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `clienttype` varchar(12) DEFAULT NULL,
  `accesstime` datetime DEFAULT NULL,
  `accessexpiry` datetime DEFAULT NULL,
  `timein` datetime DEFAULT NULL,
  `timeout` datetime DEFAULT NULL,
  `state` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`sessionid`),
  KEY `ix_timein` (`timein`),
  KEY `ix_timeout` (`timeout`),
  KEY `ix_userid` (`userid`),
  KEY `ix_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for sys_terminal
-- ----------------------------
DROP TABLE IF EXISTS `sys_terminal`;
CREATE TABLE `sys_terminal` (
  `terminalid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `parentcode` varchar(50) DEFAULT NULL,
  `parenttype` varchar(50) DEFAULT NULL,
  `macaddress` varchar(50) DEFAULT NULL,
  `dtregistered` datetime DEFAULT NULL,
  `registeredby` varchar(50) DEFAULT NULL,
  `info` longtext,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`terminalid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_terminal
-- ----------------------------
INSERT INTO `sys_terminal` (terminalid) VALUES ('T001');
INSERT INTO `sys_terminal` (terminalid) VALUES ('T002');
INSERT INTO `sys_terminal` (terminalid) VALUES ('T003');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(15) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `pwd` varchar(50) DEFAULT NULL,
  `firstname` varchar(50) DEFAULT NULL,
  `lastname` varchar(50) DEFAULT NULL,
  `middlename` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `jobtitle` varchar(50) DEFAULT NULL,
  `pwdlogincount` int(11) DEFAULT NULL,
  `pwdexpirydate` datetime DEFAULT NULL,
  `usedpwds` longtext,
  `lockid` varchar(32) DEFAULT NULL,
  `txncode` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_username` (`username`),
  KEY `ix_lastname_firstname` (`lastname`,`firstname`),
  KEY `ix_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('USR-2160bfa3:15383af7f25:-7f5c', null, null, null, 'FUENT1', 'cb5d8fdbdecbb1e18233619e3999b49d', 'USER', 'FUENT1', null, 'FUENT1, USER', 'COLLECTOR', '0', '2046-08-17 16:32:38', 'cb5d8fdbdecbb1e18233619e3999b49d', null, 'FU');
INSERT INTO `sys_user` VALUES ('USR-ADMIN', null, null, null, 'ADMIN', '557a4295dcca1a044b690f8b6486f33d', 'ADMIN', 'ADMIN', '.', 'ADMIN, ADMIN .', 'ADMIN', '0', '2045-09-16 22:30:54', '557a4295dcca1a044b690f8b6486f33d', null, null);

-- ----------------------------
-- Table structure for sys_usergroup
-- ----------------------------
DROP TABLE IF EXISTS `sys_usergroup`;
CREATE TABLE `sys_usergroup` (
  `objid` varchar(50) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `domain` varchar(25) DEFAULT NULL,
  `userclass` varchar(25) DEFAULT NULL,
  `orgclass` varchar(50) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_usergroup
-- ----------------------------
INSERT INTO `sys_usergroup` VALUES ('ADMIN.SYSADMIN', 'SYSTEM ADMINISTRATOR', 'ADMIN', 'usergroup', null, 'SYSADMIN');
INSERT INTO `sys_usergroup` VALUES ('DEVELOPER.ADMIN', 'DEVELOPER ADMIN', 'DEVELOPER', null, null, 'ADMIN');
INSERT INTO `sys_usergroup` VALUES ('DEVELOPER.REPORT', 'SYSTEM ADMINISTRATOR', 'DEVELOPER', 'usergroup', null, 'REPORT');
INSERT INTO `sys_usergroup` VALUES ('ENTITY.ADMIN', 'ENTITY ADMIN', 'ENTITY', null, null, 'ADMIN');
INSERT INTO `sys_usergroup` VALUES ('ENTITY.APPROVER', 'ENTITY APPROVER', 'ENTITY', null, null, 'APPROVER');
INSERT INTO `sys_usergroup` VALUES ('ENTITY.MASTER', 'ENTITY MASTER', 'ENTITY', null, null, 'MASTER');

-- ----------------------------
-- Table structure for sys_usergroup_admin
-- ----------------------------
DROP TABLE IF EXISTS `sys_usergroup_admin`;
CREATE TABLE `sys_usergroup_admin` (
  `objid` varchar(50) NOT NULL,
  `usergroupid` varchar(50) DEFAULT NULL,
  `user_objid` varchar(50) DEFAULT NULL,
  `user_username` varchar(50) DEFAULT NULL,
  `user_firstname` varchar(50) NOT NULL,
  `user_lastname` varchar(50) DEFAULT NULL,
  `exclude` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `usergroupid` (`usergroupid`),
  KEY `FK_sys_usergroup_admin` (`user_objid`),
  CONSTRAINT `sys_usergroup_admin_ibfk_1` FOREIGN KEY (`user_objid`) REFERENCES `sys_user` (`objid`),
  CONSTRAINT `sys_usergroup_admin_ibfk_2` FOREIGN KEY (`usergroupid`) REFERENCES `sys_usergroup` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_usergroup_admin
-- ----------------------------

-- ----------------------------
-- Table structure for sys_usergroup_member
-- ----------------------------
DROP TABLE IF EXISTS `sys_usergroup_member`;
CREATE TABLE `sys_usergroup_member` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(10) DEFAULT NULL,
  `usergroup_objid` varchar(50) DEFAULT NULL,
  `user_objid` varchar(50) NOT NULL,
  `user_username` varchar(50) DEFAULT NULL,
  `user_firstname` varchar(50) NOT NULL,
  `user_lastname` varchar(50) NOT NULL,
  `org_objid` varchar(50) DEFAULT NULL,
  `org_name` varchar(50) DEFAULT NULL,
  `org_orgclass` varchar(50) DEFAULT NULL,
  `securitygroup_objid` varchar(50) DEFAULT NULL,
  `exclude` varchar(255) DEFAULT NULL,
  `displayname` varchar(50) DEFAULT NULL,
  `jobtitle` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `usergroup_objid` (`usergroup_objid`),
  KEY `FK_sys_usergroup_member` (`user_objid`),
  KEY `FK_sys_usergroup_member_org` (`org_objid`),
  KEY `FK_sys_usergroup_member_securitygorup` (`securitygroup_objid`),
  KEY `ix_user_firstname` (`user_firstname`),
  KEY `ix_user_lastname_firstname` (`user_lastname`,`user_firstname`),
  KEY `ix_username` (`user_username`),
  CONSTRAINT `sys_usergroup_member_ibfk_1` FOREIGN KEY (`user_objid`) REFERENCES `sys_user` (`objid`),
  CONSTRAINT `sys_usergroup_member_ibfk_2` FOREIGN KEY (`org_objid`) REFERENCES `sys_org` (`objid`),
  CONSTRAINT `sys_usergroup_member_ibfk_3` FOREIGN KEY (`securitygroup_objid`) REFERENCES `sys_securitygroup` (`objid`),
  CONSTRAINT `sys_usergroup_member_ibfk_4` FOREIGN KEY (`usergroup_objid`) REFERENCES `sys_usergroup` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_usergroup_member
-- ----------------------------
INSERT INTO `sys_usergroup_member` VALUES ('UGM-1314279d:153819f12bf:-7f51', null, 'DEVELOPER.REPORT', 'USR-ADMIN', 'ADMIN', 'ADMIN', 'ADMIN', null, null, null, null, null, null, null);
INSERT INTO `sys_usergroup_member` VALUES ('UGM-1314279d:153819f12bf:-7faa', null, 'DEVELOPER.ADMIN', 'USR-ADMIN', 'ADMIN', 'ADMIN', 'ADMIN', null, null, null, null, null, null, null);
INSERT INTO `sys_usergroup_member` VALUES ('UGM-2160bfa3:15383af7f25:-7f2f', null, 'ENTITY.MASTER', 'USR-2160bfa3:15383af7f25:-7f5c', 'FUENT1', 'USER', 'FUENT1', 'BR-442cf56e:15364b20825:-7fe8', 'FUENTE', 'BRANCH', null, null, null, null);
INSERT INTO `sys_usergroup_member` VALUES ('UGM-ADMIN-SYS-0001', null, 'ADMIN.SYSADMIN', 'USR-ADMIN', 'ADMIN', 'ADMIN', 'ADMIN', null, null, null, null, null, null, null);
INSERT INTO `sys_usergroup_member` VALUES ('UGM5dd335f3:153776444ed:-7f17', null, 'ENTITY.MASTER', 'USR-ADMIN', 'ADMIN', 'ADMIN', 'ADMIN', null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for sys_usergroup_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_usergroup_permission`;
CREATE TABLE `sys_usergroup_permission` (
  `objid` varchar(50) NOT NULL,
  `usergroup_objid` varchar(50) DEFAULT NULL,
  `object` varchar(25) DEFAULT NULL,
  `permission` varchar(25) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `FK_sys_usergroup_permission_usergroup` (`usergroup_objid`),
  CONSTRAINT `sys_usergroup_permission_ibfk_1` FOREIGN KEY (`usergroup_objid`) REFERENCES `sys_usergroup` (`objid`),
  CONSTRAINT `sys_usergroup_permission_ibfk_2` FOREIGN KEY (`usergroup_objid`) REFERENCES `sys_usergroup` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_usergroup_permission
-- ----------------------------

-- ----------------------------
-- Table structure for sys_var
-- ----------------------------
DROP TABLE IF EXISTS `sys_var`;
CREATE TABLE `sys_var` (
  `name` varchar(50) NOT NULL,
  `value` longtext,
  `description` varchar(255) DEFAULT NULL,
  `datatype` varchar(15) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_var
-- ----------------------------
INSERT INTO `sys_var` VALUES ('invalid_login_access_date_interval', '1m', 'number of hours/days access date is moved when failed_login_max_retries is reached( d=days, h=hours)', null, null);
INSERT INTO `sys_var` VALUES ('invalid_login_action', '1', '0 - suspend 1-move access to a later date', null, null);
INSERT INTO `sys_var` VALUES ('invalid_login_max_retries', '5', 'No. of times a user can retry login before disabling account', null, null);
INSERT INTO `sys_var` VALUES ('pwd_change_cycle', '1', 'No. of times the user cannot use a repeating password', null, 'SYSTEM');
INSERT INTO `sys_var` VALUES ('pwd_change_date_interval', '365M', 'No. of days/months before a user is required to change their password (d=days, M=months)', null, 'SYSTEM');
INSERT INTO `sys_var` VALUES ('pwd_change_login_count', '0', 'No. of times a user has successfully logged in before changing their password. (0=no limit) ', null, 'SYSTEM');
INSERT INTO `sys_var` VALUES ('sa_pwd', 'ba4d084b31b41fbe55302c0429d93c81', null, null, null);
INSERT INTO `sys_var` VALUES ('server_timezone', 'Asia/Shanghai', 'this must be matched with default timezone of server', null, null);
INSERT INTO `sys_var` VALUES ('session_timeout_interval', '1d ', 'expiry dates of inactive session (d=days, h=hours)', null, 'SYSTEM');
INSERT INTO `sys_var` VALUES ('system_pwd', '1234', 'system password', null, 'SYSTEM');

-- ----------------------------
-- Table structure for sys_wf
-- ----------------------------
DROP TABLE IF EXISTS `sys_wf`;
CREATE TABLE `sys_wf` (
  `name` varchar(50) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `domain` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_wf
-- ----------------------------

-- ----------------------------
-- Table structure for sys_wf_assignee
-- ----------------------------
DROP TABLE IF EXISTS `sys_wf_assignee`;
CREATE TABLE `sys_wf_assignee` (
  `objid` varchar(50) NOT NULL,
  `processname` varchar(50) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `domain` varchar(50) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  `user_objid` varchar(50) DEFAULT NULL,
  `expr` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_wf_assignee
-- ----------------------------

-- ----------------------------
-- Table structure for sys_wf_node
-- ----------------------------
DROP TABLE IF EXISTS `sys_wf_node`;
CREATE TABLE `sys_wf_node` (
  `name` varchar(50) NOT NULL,
  `processname` varchar(50) NOT NULL DEFAULT '',
  `title` varchar(100) DEFAULT NULL,
  `nodetype` varchar(10) DEFAULT NULL,
  `idx` int(11) DEFAULT NULL,
  `salience` int(11) DEFAULT NULL,
  `domain` varchar(50) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name`,`processname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_wf_node
-- ----------------------------

-- ----------------------------
-- Table structure for sys_wf_subtask
-- ----------------------------
DROP TABLE IF EXISTS `sys_wf_subtask`;
CREATE TABLE `sys_wf_subtask` (
  `objid` varchar(50) NOT NULL DEFAULT '',
  `taskid` varchar(50) DEFAULT NULL,
  `requester_objid` varchar(50) DEFAULT NULL,
  `requester_name` varchar(100) DEFAULT NULL,
  `requestdate` datetime DEFAULT NULL,
  `startdate` datetime DEFAULT NULL,
  `enddate` datetime DEFAULT NULL,
  `assignee_objid` varchar(50) DEFAULT NULL,
  `assignee_name` varchar(100) DEFAULT NULL,
  `actor_objid` varchar(50) DEFAULT NULL,
  `actor_name` varchar(100) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `action` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `FK_sys_wf_subtask_sys_wf_task` (`taskid`),
  CONSTRAINT `FK_sys_wf_subtask_sys_wf_task` FOREIGN KEY (`taskid`) REFERENCES `sys_wf_task` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_wf_subtask
-- ----------------------------

-- ----------------------------
-- Table structure for sys_wf_task
-- ----------------------------
DROP TABLE IF EXISTS `sys_wf_task`;
CREATE TABLE `sys_wf_task` (
  `objid` varchar(50) NOT NULL DEFAULT '',
  `refid` varchar(50) DEFAULT NULL,
  `parentprocessid` varchar(50) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `startdate` datetime DEFAULT NULL,
  `enddate` datetime DEFAULT NULL,
  `assignee_objid` varchar(50) DEFAULT NULL,
  `assignee_name` varchar(100) DEFAULT NULL,
  `actor_objid` varchar(50) DEFAULT NULL,
  `actor_name` varchar(100) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_wf_task
-- ----------------------------

-- ----------------------------
-- Table structure for sys_wf_transition
-- ----------------------------
DROP TABLE IF EXISTS `sys_wf_transition`;
CREATE TABLE `sys_wf_transition` (
  `parentid` varchar(50) NOT NULL DEFAULT '',
  `processname` varchar(50) NOT NULL DEFAULT '',
  `action` varchar(50) DEFAULT NULL,
  `to` varchar(50) NOT NULL,
  `idx` int(11) DEFAULT NULL,
  `eval` mediumtext,
  `properties` varchar(255) DEFAULT NULL,
  `permission` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`parentid`,`processname`,`to`),
  CONSTRAINT `FK_sys_wf_transition_wf_node` FOREIGN KEY (`parentid`, `processname`) REFERENCES `sys_wf_node` (`name`, `processname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_wf_transition
-- ----------------------------

-- ----------------------------
-- Table structure for sys_wf_workitemtype
-- ----------------------------
DROP TABLE IF EXISTS `sys_wf_workitemtype`;
CREATE TABLE `sys_wf_workitemtype` (
  `objid` varchar(50) NOT NULL DEFAULT '',
  `name` varchar(50) NOT NULL,
  `state` varchar(50) NOT NULL DEFAULT '',
  `processname` varchar(50) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `action` varchar(50) DEFAULT NULL,
  `domain` varchar(50) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  `expr` varchar(255) DEFAULT NULL,
  `handler` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`name`,`state`,`processname`),
  KEY `FK_sys_wf_subtasktype_node` (`state`,`processname`),
  CONSTRAINT `FK_sys_wf_subtasktype_node` FOREIGN KEY (`state`, `processname`) REFERENCES `sys_wf_node` (`name`, `processname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_wf_workitemtype
-- ----------------------------

-- ----------------------------
-- Table structure for txnlog
-- ----------------------------
DROP TABLE IF EXISTS `txnlog`;
CREATE TABLE `txnlog` (
  `objid` varchar(50) NOT NULL,
  `ref` varchar(100) NOT NULL,
  `refid` text NOT NULL,
  `txndate` datetime NOT NULL,
  `action` varchar(50) NOT NULL,
  `userid` varchar(50) NOT NULL,
  `remarks` text,
  `diff` longtext,
  `username` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_txnlog_action` (`action`),
  KEY `ix_txnlog_ref` (`ref`),
  KEY `ix_txnlog_userid` (`userid`),
  KEY `ix_txnlog_useridaction` (`userid`,`action`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for txnref
-- ----------------------------
DROP TABLE IF EXISTS `txnref`;
CREATE TABLE `txnref` (
  `oid` int(11) NOT NULL AUTO_INCREMENT,
  `objid` varchar(50) NOT NULL,
  `refid` varchar(50) NOT NULL,
  `msg` varchar(255) NOT NULL,
  PRIMARY KEY (`oid`),
  KEY `ix_txnref_refid` (`refid`),
  KEY `ix_txnref_objid` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of txnref
-- ----------------------------

-- ----------------------------
-- Table structure for txnsignatory
-- ----------------------------
DROP TABLE IF EXISTS `txnsignatory`;
CREATE TABLE `txnsignatory` (
  `objid` varchar(50) NOT NULL,
  `refid` varchar(50) NOT NULL,
  `personnelid` varchar(50) DEFAULT NULL,
  `type` varchar(25) NOT NULL,
  `caption` varchar(25) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `dtsigned` datetime DEFAULT NULL,
  `seqno` int(11) NOT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_signatory_refid` (`refid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of txnsignatory
-- ----------------------------

-- ----------------------------
-- Table structure for workflowstate
-- ----------------------------
DROP TABLE IF EXISTS `workflowstate`;
CREATE TABLE `workflowstate` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) NOT NULL,
  `txndate` datetime DEFAULT NULL,
  `userid` varchar(50) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`objid`,`state`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_userid` (`userid`),
  KEY `ix_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of workflowstate
-- ----------------------------

-- ----------------------------
-- Table structure for zone
-- ----------------------------
DROP TABLE IF EXISTS `zone`;
CREATE TABLE `zone` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `info` text,
  PRIMARY KEY (`objid`),
  KEY `ix_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zone
-- ----------------------------

-- ----------------------------
-- View structure for vw_entityindividual_lookup
-- ----------------------------
DROP VIEW IF EXISTS `vw_entityindividual_lookup`;
CREATE VIEW `vw_entityindividual_lookup` AS select 
	e.objid, e.entityno, e.name, e.address_text as addresstext, 
	e.type, ei.lastname, ei.firstname, ei.middlename, ei.gender, ei.birthdate, 
	e.mobileno, e.phoneno 
from entity e 
	inner join entityindividual ei on ei.objid=e.objid ;

-- ----------------------------
-- View structure for vw_entityrelation_lookup
-- ----------------------------
DROP VIEW IF EXISTS `vw_entityrelation_lookup`;
CREATE VIEW `vw_entityrelation_lookup` AS select er.*, 
	e.entityno, e.name, e.address_text as addresstext, e.type, 
	ei.lastname, ei.firstname, ei.middlename, ei.gender, ei.birthdate, 
	e.mobileno, e.phoneno 
from entity_relation er  
  inner join entityindividual ei on ei.objid=er.relateto_objid 
	inner join entity e on e.objid=ei.objid ;

-- ----------------------------
-- View structure for vw_sendout_unclaim
-- ----------------------------
DROP VIEW IF EXISTS `vw_sendout_unclaim`;
CREATE VIEW `vw_sendout_unclaim` AS select 
	s.objid, s.dtfiled, s.controlno, s.txnno, 
	s.sender_objid, s.sender_name, s.receiver_objid, 
	s.receiver_name, s.principal, s.currency
from sendout_unclaim u 
	inner join sendout s on s.objid=u.objid ;
