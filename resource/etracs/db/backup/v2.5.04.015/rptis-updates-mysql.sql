CREATE TABLE `rpttransmittal` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) NOT NULL,
  `type` varchar(15) NOT NULL,
  `txnno` varchar(15) NOT NULL,
  `txndate` datetime NOT NULL,
  `lgu_objid` varchar(50) NOT NULL,
  `lgu_name` varchar(50) NOT NULL,
  `lgu_type` varchar(50) NOT NULL,
  `tolgu_objid` varchar(50) NOT NULL,
  `tolgu_name` varchar(50) NOT NULL,
  `tolgu_type` varchar(50) NOT NULL,
  `createdby_objid` varchar(50) NOT NULL,
  `createdby_name` varchar(100) NOT NULL,
  `createdby_title` varchar(50) NOT NULL,
  `remarks` varchar(500) NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `ux_txnno` (`txnno`),
  KEY `ix_state` (`state`),
  KEY `ix_createdby_name` (`createdby_name`),
  KEY `ix_lguname` (`lgu_name`)
) ENGINE=InnoDB;


CREATE TABLE `rpttransmittal_item` (
  `objid` varchar(50) NOT NULL,
  `transmittalid` varchar(50) NOT NULL,
  `state` varchar(50) NOT NULL,
  `refid` varchar(50) NOT NULL,
  `filetype` varchar(50) NOT NULL,
  `txntype_objid` varchar(50) NOT NULL,
  `tdno` varchar(50) NOT NULL,
  `owner_name` varchar(1000) NOT NULL,
  `owner_address` varchar(100) NOT NULL,
  `fullpin` varchar(50) NOT NULL,
  `cadastrallotno` varchar(500) NOT NULL,
  `totalareaha` decimal(16,6) NOT NULL,
  `totalareasqm` decimal(16,2) NOT NULL,
  `totalmv` decimal(16,2) NOT NULL,
  `totalav` decimal(16,2) NOT NULL,
  `remarks` text,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `ux_transmittalid_refid` (`transmittalid`,`refid`),
  KEY `ix_refid` (`refid`),
  KEY `FK_rpttransmittal_item_rpttransmittal` (`transmittalid`),
  KEY `FK_rpttransmittal_item_data_rpttransmittal` (`transmittalid`),
  CONSTRAINT `FK_rpttransmittal_item_rpttransmittal` FOREIGN KEY (`transmittalid`) REFERENCES `rpttransmittal` (`objid`)
) ENGINE=InnoDB;


CREATE TABLE `rpttransmittal_item_data` (
  `objid` varchar(50) NOT NULL,
  `transmittalid` varchar(50) NOT NULL,
  `parentid` varchar(50) NOT NULL,
  `itemno` int(11) NOT NULL,
  `itemtype` varchar(50) NOT NULL,
  `data` text NOT NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `ux_rpttransmittal_itemno` (`parentid`,`itemno`),
  KEY `FK_rpttransmittal_item_data_rpttransmittal` (`transmittalid`),
  KEY `FK_rpttransmittal_item_data_rpttransmittalitem` (`parentid`),
  CONSTRAINT `FK_rpttransmittal_item_data_rpttransmittal` FOREIGN KEY (`transmittalid`) REFERENCES `rpttransmittal` (`objid`),
  CONSTRAINT `FK_rpttransmittal_item_data_rpttransmittalitem` FOREIGN KEY (`parentid`) REFERENCES `rpttransmittal_item` (`objid`)
) ENGINE=InnoDB;


CREATE TABLE `rpttransmittal_log` (
  `objid` varchar(50) NOT NULL,
  `transmittalid` varchar(50) NOT NULL,
  `refid` varchar(50) NOT NULL,
  `txndate` datetime NOT NULL,
  `error` text,
  `remarks` varchar(500) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `FK_rpttransmittal_log_rpttransmittal` (`transmittalid`),
  KEY `ix_refid` (`refid`),
  CONSTRAINT `FK_rpttransmittal_log_rpttransmittal` FOREIGN KEY (`transmittalid`) REFERENCES `rpttransmittal` (`objid`)
) ENGINE=InnoDB;


alter table rptledgeritem add classification_objid varchar(50);
alter table rptledgeritem add actualuse_objid varchar(50);
alter table rptledgeritem add basicav decimal(16,2) null;
alter table rptledgeritem add sefav decimal(16,2) null;

update rptledgeritem set basicav = av, sefav = av where basicav is null;


update rptledgeritem rli, rptledgerfaas rlf set
  rli.classification_objid = rlf.classification_objid,
  rli.actualuse_objid = rlf.actualuse_objid
where rli.rptledgerfaasid = rlf.objid;


alter table cashreceiptitem_rpt_account add discount decimal(16,2);
update cashreceiptitem_rpt_account  set discount = 0 where discount is null;



