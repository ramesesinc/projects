--
-- Table structure for table `waterworks_account`
--

DROP TABLE IF EXISTS `waterworks_account`;

CREATE TABLE `waterworks_account` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) NULL,
  `dtstarted` date NULL,
  `acctno` varchar(50) NULL,
  `applicationid` varchar(50) NULL,
  `acctname` varchar(255) NULL,
  `owner_objid` varchar(50) NULL,
  `owner_name` varchar(255) NULL,
  `address_type` varchar(50) NULL,
  `address_barangay_objid` varchar(50) NULL,
  `address_barangay_name` varchar(50) NULL,
  `address_text` varchar(255) NULL,
  `address_city` varchar(50) NULL,
  `address_province` varchar(50) NULL,
  `address_municipality` varchar(50) NULL,
  `address_unitno` varchar(50) NULL,
  `address_street` varchar(50) NULL,
  `address_subdivision` varchar(50) NULL,
  `address_bldgno` varchar(50) NULL,
  `address_bldgname` varchar(50) NULL,
  `address_pin` varchar(50) NULL,
  `meterid` varchar(50) NULL,
  `classificationid` varchar(50) NULL,
  `lastdateread` date NULL,
  `currentreading` int(11) NULL,
  `lastdatepaid` datetime NULL,
  `billingcycleid` varchar(50) NULL,
  `zoneid` varchar(50) NULL,
  `stuboutnodeid` varchar(50) NULL,
  `lastbilldate` date NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `fk_waterworks_account_meter` (`meterid`),
  KEY `fk_waterworks_account_classification` (`classificationid`),
  KEY `ix_acctno` (`acctno`),
  KEY `ix_applicationid` (`applicationid`),
  KEY `ix_billingcycleid` (`billingcycleid`),
  KEY `ix_dtstarted` (`dtstarted`),
  KEY `ix_acctname` (`acctname`),
  KEY `ix_owner_objid` (`owner_objid`),
  KEY `ix_meterid` (`meterid`),
  KEY `ix_zoneid` (`zoneid`),
  KEY `ix_stuboutnodeid` (`stuboutnodeid`),
  KEY `ix_lastbilldate` (`lastbilldate`),
  CONSTRAINT `waterworks_account_ibfk_1` FOREIGN KEY (`applicationid`) REFERENCES `waterworks_application` (`objid`),
  CONSTRAINT `waterworks_account_ibfk_2` FOREIGN KEY (`billingcycleid`) REFERENCES `waterworks_billing_cycle` (`objid`),
  CONSTRAINT `waterworks_account_ibfk_3` FOREIGN KEY (`classificationid`) REFERENCES `waterworks_classification` (`objid`),
  CONSTRAINT `waterworks_account_ibfk_4` FOREIGN KEY (`meterid`) REFERENCES `waterworks_meter` (`objid`),
  CONSTRAINT `waterworks_account_ibfk_8` FOREIGN KEY (`stuboutnodeid`) REFERENCES `waterworks_stubout_node` (`objid`),
  CONSTRAINT `waterworks_account_ibfk_9` FOREIGN KEY (`zoneid`) REFERENCES `waterworks_zone` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_account_attribute`
--

DROP TABLE IF EXISTS `waterworks_account_attribute`;

CREATE TABLE `waterworks_account_attribute` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) NULL,
  `attribute_name` varchar(50) NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_account_ledger`
--

DROP TABLE IF EXISTS `waterworks_account_ledger`;

CREATE TABLE `waterworks_account_ledger` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) NULL,
  `txntype` varchar(10) NULL,
  `item_objid` varchar(50) NULL,
  `item_code` varchar(50) NULL,
  `item_title` varchar(255) NULL,
  `remarks` varchar(255) NULL,
  `amount` decimal(16,2) NULL,
  `amtpaid` decimal(16,2) NULL,
  `installmentid` varchar(50) NULL,
  `billingcycleid` varchar(50) NULL,
  `month` int(11) NULL,
  `year` int(11) NULL,
  `surcharge` decimal(16,2) NULL,
  `interest` decimal(16,2) NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_parent_item_billingcycle` (`parentid`,`item_objid`,`billingcycleid`),
  UNIQUE KEY `uix_parentid_year_month_itemid` (`parentid`,`item_objid`,`month`,`year`),
  KEY `ix_parentid` (`parentid`),
  KEY `ix_item_objid` (`item_objid`),
  KEY `ix_billingcycleid` (`billingcycleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_application`
--

DROP TABLE IF EXISTS `waterworks_application`;

CREATE TABLE `waterworks_application` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) NULL,
  `dtfiled` date NULL,
  `appno` varchar(50) NULL,
  `acctid` varchar(50) NULL,
  `classificationid` varchar(50) NULL,
  `acctname` varchar(50) NULL,
  `owner_objid` varchar(50) NULL,
  `owner_name` varchar(255) NULL,
  `address_type` varchar(50) NULL,
  `address_barangay_objid` varchar(50) NULL,
  `address_barangay_name` varchar(50) NULL,
  `address_text` varchar(50) NULL,
  `address_city` varchar(50) NULL,
  `address_province` varchar(50) NULL,
  `address_municipality` varchar(50) NULL,
  `address_unitno` varchar(50) NULL,
  `address_street` varchar(50) NULL,
  `address_subdivision` varchar(50) NULL,
  `address_bldgno` varchar(50) NULL,
  `address_bldgname` varchar(50) NULL,
  `address_pin` varchar(50) NULL,
  `stuboutid` varchar(50) NULL,
  `stuboutindex` int(11) NULL,
  `meterid` varchar(50) NULL,
  `initialreading` int(11) NULL,
  `installer_name` varchar(255) NULL,
  `installer_objid` varchar(50) NULL,
  `dtinstalled` date NULL,
  `signature` longtext,
  `meterprovider` varchar(255) NULL,
  `stuboutnodeid` varchar(50) NULL,
  `metersizeid` varchar(50) NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_classificationid` (`classificationid`),
  KEY `ix_acctname` (`acctname`),
  KEY `ix_owner_objid` (`owner_objid`),
  KEY `ix_address_barangay_objid` (`address_barangay_objid`),
  KEY `ix_stuboutid` (`stuboutid`),
  KEY `ix_meterid` (`meterid`),
  KEY `ix_installer_objid` (`installer_objid`),
  KEY `ix_dtinstalled` (`dtinstalled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_application_bom`
--

DROP TABLE IF EXISTS `waterworks_application_bom`;

CREATE TABLE `waterworks_application_bom` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) NULL,
  `state` varchar(10) NULL,
  `item_objid` varchar(50) NULL,
  `item_code` varchar(50) NULL,
  `item_title` varchar(255) NULL,
  `item_unit` varchar(10) NULL,
  `qty` int(11) NULL,
  `qtyissued` int(11) NULL,
  `item_unitprice` decimal(10,2) NULL,
  `cwdsupplied` int(11) NULL,
  `remarks` varchar(50) NULL,
  KEY `fk_application_bom` (`parentid`),
  CONSTRAINT `waterworks_application_bom_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `waterworks_application` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_application_fee`
--

DROP TABLE IF EXISTS `waterworks_application_fee`;

CREATE TABLE `waterworks_application_fee` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) NULL,
  `duedate` date NULL,
  `txntype` varchar(10) NULL,
  `item_objid` varchar(50) NULL,
  `item_code` varchar(50) NULL,
  `item_title` varchar(255) NULL,
  `remarks` varchar(255) NULL,
  `amount` decimal(16,2) NULL,
  `amtpaid` decimal(16,2) NULL,
  `installmentid` varchar(50) NULL,
  KEY `fk_application_fee_application` (`parentid`),
  CONSTRAINT `waterworks_application_fee_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `waterworks_application` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_application_requirement`
--

DROP TABLE IF EXISTS `waterworks_application_requirement`;

CREATE TABLE `waterworks_application_requirement` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) NULL,
  `code` varchar(50) NULL,
  `title` varchar(255) NULL,
  `refno` varchar(50) NULL,
  `refid` varchar(50) NULL,
  `sortorder` int(11) NULL,
  `required` tinyint(4) NULL,
  `complied` tinyint(4) NULL,
  `dtcomplied` datetime NULL,
  `verifier_objid` varchar(50) NULL,
  `verifier_name` varchar(255) NULL,
  `remarks` varchar(255) NULL,
  `info` longtext,
  `dtissued` date NULL,
  `expirydate` date NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_application_task`
--

DROP TABLE IF EXISTS `waterworks_application_task`;

CREATE TABLE `waterworks_application_task` (
  `taskid` varchar(50) NOT NULL,
  `refid` varchar(50) NULL,
  `parentprocessid` varchar(50) NULL,
  `state` varchar(50) NULL,
  `startdate` datetime NULL,
  `enddate` datetime NULL,
  `assignee_objid` varchar(50) NULL,
  `assignee_name` varchar(100) NULL,
  `actor_objid` varchar(50) NULL,
  `actor_name` varchar(100) NULL,
  `message` varchar(255) NULL,
  `prevtaskid` varchar(50) NULL,
  `dtcreated` datetime NULL,
  PRIMARY KEY (`taskid`),
  KEY `FK_business_application_task_business_application` (`refid`),
  CONSTRAINT `waterworks_application_task_ibfk_1` FOREIGN KEY (`refid`) REFERENCES `waterworks_application` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_attribute`
--

DROP TABLE IF EXISTS `waterworks_attribute`;

CREATE TABLE `waterworks_attribute` (
  `name` varchar(50) NOT NULL,
  `title` varchar(100) NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_batchreading`
--

DROP TABLE IF EXISTS `waterworks_batchreading`;

CREATE TABLE `waterworks_batchreading` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) NULL,
  `readingdate` date NULL,
  `month` int(11) NULL,
  `year` int(11) NULL,
  `sectorid` varchar(50) NULL,
  `zoneid` varchar(50) NULL,
  `reader_objid` varchar(50) NULL,
  `reader_name` varchar(255) NULL,
  `dtcreated` datetime NULL,
  `createdby_objid` varchar(50) NULL,
  `createdby_name` varchar(255) NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_zoneid` (`zoneid`),
  KEY `ix_year` (`year`),
  KEY `ix_month` (`month`),
  KEY `ix_sectorid` (`sectorid`),
  KEY `ix_reader_objid` (`reader_objid`),
  KEY `uix_batchreading_year_month` (`month`,`year`,`zoneid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_billing`
--

DROP TABLE IF EXISTS `waterworks_billing`;

CREATE TABLE `waterworks_billing` (
  `objid` varchar(50) NOT NULL,
  `batchid` varchar(50) NULL,
  `state` varchar(50) NULL,
  `acctid` varchar(50) NULL,
  `prevreadingdate` datetime NULL,
  `prevreading` int(11) NULL,
  `readingdate` datetime NULL,
  `reading` int(11) NULL,
  `reader_objid` varchar(50) NULL,
  `reader_name` varchar(255) NULL,
  `discrate` decimal(16,2) NULL,
  `surcharge` decimal(16,2) NULL,
  `interest` decimal(16,2) NULL,
  `othercharge` decimal(16,2) NULL,
  `credits` decimal(16,2) NULL,
  `arrears` decimal(16,4) NULL,
  `unpaidmonths` int(11) NULL,
  `volume` int(11) NULL,
  `amount` decimal(16,2) NULL,
  `month` int(11) NULL,
  `year` int(11) NULL,
  `readingmethod` varchar(255) NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_billing_account_parent_acctid` (`batchid`,`acctid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_batch_billing`
--

DROP TABLE IF EXISTS `waterworks_batch_billing`;

CREATE TABLE `waterworks_batch_billing` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) NULL,
  `dtcreated` datetime NULL,
  `createdby_objid` varchar(50) NULL,
  `createdby_name` varchar(255) NULL,
  `year` int(11) NULL,
  `month` int(11) NULL,
  `zoneid` varchar(50) NULL,
  `dtposted` datetime NULL,
  `postedby_objid` varchar(50) NULL,
  `postedby_name` varchar(255) NULL,
  `fromperiod` date NULL,
  `toperiod` date NULL,
  `discountdate` date NULL,
  `reader_objid` varchar(50) NULL,
  `reader_name` varchar(255) NULL,
  `readingdate` date NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_billing_cycle`
--

DROP TABLE IF EXISTS `waterworks_billing_cycle`;

CREATE TABLE `waterworks_billing_cycle` (
  `objid` varchar(50) NOT NULL,
  `sectorid` varchar(50) NOT NULL,
  `month` int(11) NULL,
  `year` int(11) NULL,
  `fromperiod` date NULL,
  `toperiod` date NULL,
  `readingdate` date NULL,
  `duedate` date NULL,
  `disconnectiondate` date NULL,
  `billdate` date NULL,
  `oldid` varchar(50) NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_sectorid` (`sectorid`),
  KEY `ix_readingdate` (`readingdate`),
  CONSTRAINT `waterworks_billing_cycle_ibfk_1` FOREIGN KEY (`sectorid`) REFERENCES `waterworks_sector` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_classification`
--

DROP TABLE IF EXISTS `waterworks_classification`;

CREATE TABLE `waterworks_classification` (
  `objid` varchar(50) NOT NULL,
  `description` varchar(255) NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_consumption`
--

DROP TABLE IF EXISTS `waterworks_consumption`;

CREATE TABLE `waterworks_consumption` (
  `objid` varchar(50) NOT NULL,
  `acctid` varchar(50) NULL,
  `batchid` varchar(50) NULL,
  `prevreading` int(11) NULL,
  `reading` int(11) NULL,
  `readingmethod` varchar(50) NULL,
  `reader_objid` varchar(50) NULL,
  `reader_name` varchar(255) NULL,
  `volume` int(11) NULL,
  `rate` decimal(16,2) NULL,
  `ledgerid` varchar(50) NULL,
  `billingcycleid` varchar(50) NULL,
  `amount` decimal(16,2) NULL,
  `amtpaid` decimal(16,2) NULL,
  `month` int(11) NULL,
  `year` int(11) NULL,
  `readingdate` date NULL,
  `state` varchar(32) NOT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_waterworks_consumption_year_month` (`acctid`,`month`,`year`),
  KEY `fks_waterworks_account_reading` (`acctid`),
  KEY `ix_ledgerid` (`ledgerid`),
  KEY `ix_year` (`year`),
  KEY `ix_month` (`month`),
  CONSTRAINT `waterworks_consumption_ibfk_1` FOREIGN KEY (`acctid`) REFERENCES `waterworks_account` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_credit`
--

DROP TABLE IF EXISTS `waterworks_credit`;

CREATE TABLE `waterworks_credit` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) NULL,
  `rootid` varchar(50) NULL,
  `refno` varchar(50) NULL,
  `refid` varchar(50) NULL,
  `reftype` varchar(50) NULL,
  `refdate` date NULL,
  `amount` decimal(16,4) NULL,
  `amtpaid` decimal(16,4) NULL,
  `remarks` varchar(255) NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_waterworks_credit_refid` (`refid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_formula`
--

DROP TABLE IF EXISTS `waterworks_formula`;

CREATE TABLE `waterworks_formula` (
  `classificationid` varchar(100) NOT NULL,
  `varname` varchar(50) NULL,
  `expr` longtext,
  PRIMARY KEY (`classificationid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_installment`
--

DROP TABLE IF EXISTS `waterworks_installment`;

CREATE TABLE `waterworks_installment` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(10) NULL,
  `controlno` varchar(100) NULL,
  `doctype` varchar(10) NULL,
  `dtfiled` datetime NULL,
  `particulars` varchar(255) NULL,
  `term` int(11) NULL,
  `downpayment` decimal(16,2) NULL,
  `enddate` date NULL,
  `acctid` varchar(50) NULL,
  `applicationid` varchar(50) NULL,
  `installmentamount` decimal(16,2) NULL,
  `lastbilldate` date NULL,
  `amtbilled` decimal(16,2) NULL,
  `amount` decimal(16,2) NULL,
  `amtpaid` decimal(16,2) NULL,
  `txntype_objid` varchar(50) NULL,
  `startyear` int(11) NULL,
  `startmonth` int(11) NULL,
  `txntypeid` varchar(50) NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_acctid` (`acctid`),
  KEY `ix_applicationid` (`applicationid`),
  KEY `ix_enddate` (`enddate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_material`
--

DROP TABLE IF EXISTS `waterworks_material`;

CREATE TABLE `waterworks_material` (
  `objid` varchar(50) NOT NULL,
  `code` varchar(10) NULL,
  `title` varchar(255) NULL,
  `unitprice` decimal(16,2) NULL,
  `unit` varchar(10) NULL,
  `installmentprice` decimal(16,2) NULL,
  `unitcost` decimal(16,2) NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_waterworks_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_meter`
--

DROP TABLE IF EXISTS `waterworks_meter`;

CREATE TABLE `waterworks_meter` (
  `objid` varchar(50) NOT NULL,
  `serialno` varchar(50) NULL,
  `brand` varchar(50) NULL,
  `sizeid` varchar(50) NULL,
  `capacity` varchar(50) NULL,
  `currentacctid` varchar(50) NULL,
  `stocktype` varchar(50) NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_serialno` (`serialno`),
  KEY `ix_brand` (`brand`),
  KEY `fk_sizeid` (`sizeid`),
  KEY `ix_currentacctid` (`currentacctid`),
  CONSTRAINT `waterworks_meter_ibfk_1` FOREIGN KEY (`currentacctid`) REFERENCES `waterworks_account` (`objid`),
  CONSTRAINT `waterworks_meter_ibfk_2` FOREIGN KEY (`sizeid`) REFERENCES `waterworks_metersize` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_metersize`
--

DROP TABLE IF EXISTS `waterworks_metersize`;

CREATE TABLE `waterworks_metersize` (
  `objid` varchar(50) NOT NULL,
  `title` varchar(255) NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_mobile_header`
--

DROP TABLE IF EXISTS `waterworks_mobile_header`;

CREATE TABLE `waterworks_mobile_header` (
  `batchid` varchar(50) NOT NULL,
  `state` int(11) NULL,
  PRIMARY KEY (`batchid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_mobile_info`
--

DROP TABLE IF EXISTS `waterworks_mobile_info`;

CREATE TABLE `waterworks_mobile_info` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) NULL,
  `acctno` varchar(50) NULL,
  `acctname` varchar(255) NULL,
  `address` varchar(255) NULL,
  `serialno` varchar(50) NULL,
  `sectorid` varchar(50) NULL,
  `sectorcode` varchar(50) NULL,
  `lastreading` int(11) NULL,
  `classificationid` varchar(50) NULL,
  `barcode` varchar(50) NULL,
  `batchid` varchar(50) NULL,
  `month` int(11) NULL,
  `year` int(11) NULL,
  `period` varchar(50) NULL,
  `duedate` date NULL,
  `discodate` date NULL,
  `rundate` datetime NULL,
  `items` longtext,
  `info` longtext,
  `itemaccountid` varchar(50) NULL,
  `stuboutid` varchar(50) NULL,
  `sortorder` int(11) NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_itemaccountid` (`itemaccountid`),
  KEY `ix_batchid` (`batchid`),
  KEY `ix_classificationid` (`classificationid`),
  KEY `ix_stuboutid` (`stuboutid`),
  KEY `ix_sectorid` (`sectorid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_payment`
--

DROP TABLE IF EXISTS `waterworks_payment`;

CREATE TABLE `waterworks_payment` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) NULL,
  `rootid` varchar(50) NULL,
  `refno` varchar(50) NULL,
  `reftype` varchar(50) NULL,
  `refid` varchar(50) NULL,
  `refdate` date NULL,
  `amount` decimal(10,2) NULL,
  `voided` int(11) NULL,
  `txnmode` varchar(50) NULL,
  `parentschemaname` varchar(50) NULL,
  `remarks` varchar(255) NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_waterworks_payment_account` (`parentid`),
  KEY `fk_waterworks_payment_application` (`rootid`),
  KEY `ix_acctid` (`parentid`),
  KEY `ix_refid` (`refid`),
  KEY `ix_refno` (`refno`),
  KEY `ix_refdate` (`refdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_payment_item`
--

DROP TABLE IF EXISTS `waterworks_payment_item`;

CREATE TABLE `waterworks_payment_item` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) NULL,
  `refid` varchar(50) NULL,
  `reftype` varchar(50) NULL,
  `remarks` varchar(50) NULL,
  `item_objid` varchar(50) NULL,
  `item_title` varchar(50) NULL,
  `year` int(11) NULL,
  `month` int(11) NULL,
  `amount` decimal(10,2) DEFAULT '0.00',
  `discount` decimal(10,2) DEFAULT '0.00',
  `surcharge` decimal(10,2) DEFAULT '0.00',
  `interest` decimal(10,2) DEFAULT '0.00',
  `txntype` varchar(50) NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_waterworks_payment_parent` (`parentid`),
  KEY `ix_parentid` (`parentid`),
  KEY `ix_item_objid` (`item_objid`),
  CONSTRAINT `waterworks_payment_item_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `waterworks_payment` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_sector`
--

DROP TABLE IF EXISTS `waterworks_sector`;

CREATE TABLE `waterworks_sector` (
  `objid` varchar(50) NOT NULL,
  `code` varchar(50) NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_stubout`
--

DROP TABLE IF EXISTS `waterworks_stubout`;

CREATE TABLE `waterworks_stubout` (
  `objid` varchar(50) NOT NULL,
  `code` varchar(50) NULL,
  `description` varchar(255) NULL,
  `zoneid` varchar(50) NULL,
  `barangay_objid` varchar(50) NULL,
  `barangay_name` varchar(100) NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_zoneid` (`zoneid`),
  KEY `ix_barangay_objid` (`barangay_objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_stubout_node`
--

DROP TABLE IF EXISTS `waterworks_stubout_node`;

CREATE TABLE `waterworks_stubout_node` (
  `objid` varchar(50) NOT NULL,
  `stuboutid` varchar(50) NULL,
  `indexno` int(11) NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_stuboutid` (`stuboutid`),
  CONSTRAINT `waterworks_stubout_node_ibfk_2` FOREIGN KEY (`stuboutid`) REFERENCES `waterworks_stubout` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_txntype`
--

DROP TABLE IF EXISTS `waterworks_txntype`;

CREATE TABLE `waterworks_txntype` (
  `objid` varchar(50) NOT NULL,
  `title` varchar(50) NULL,
  `priority` int(11) NULL,
  `item_objid` varchar(50) NULL,
  `item_code` varchar(50) NULL,
  `item_title` varchar(255) NULL,
  `item_fund_objid` varchar(50) NULL,
  `item_fund_code` varchar(50) NULL,
  `item_fund_title` varchar(255) NULL,
  `ledgertype` varchar(50) NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `waterworks_zone`
--

DROP TABLE IF EXISTS `waterworks_zone`;

CREATE TABLE `waterworks_zone` (
  `objid` varchar(50) NOT NULL,
  `sectorid` varchar(50) NULL,
  `code` varchar(50) NULL,
  `description` varchar(255) NULL,
  PRIMARY KEY (`objid`),
  KEY `fk_waterworks_zone_sector` (`sectorid`,`code`),
  CONSTRAINT `waterworks_zone_ibfk_2` FOREIGN KEY (`sectorid`) REFERENCES `waterworks_sector` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
