CREATE TABLE `ovs_vehicle` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `plateno` varchar(50) DEFAULT NULL,
  `orcr` varchar(50) DEFAULT NULL,
  `owner_objid` varchar(50) DEFAULT NULL,
  `owner_name` varchar(100) DEFAULT NULL,
  `info` text,
  PRIMARY KEY (`objid`),
  KEY `ix_name` (`name`),
  KEY `ix_plateno` (`plateno`),
  KEY `ix_owner_objid` (`owner_objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ovs_violation` (
  `objid` varchar(50) NOT NULL,
  `title` varchar(50) DEFAULT NULL,
  `section` varchar(50) DEFAULT NULL,
  `article` varchar(50) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`objid`),
  KEY `ix_title` (`title`),
  KEY `ix_section` (`section`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `ovs_violation_ticket` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  `ticketno` varchar(50) DEFAULT NULL,
  `violator_objid` varchar(50) DEFAULT NULL,
  `violator_name` varchar(100) DEFAULT NULL,
  `apprehensionofficer_objid` varchar(50) DEFAULT NULL,
  `apprehensionofficer_name` varchar(100) DEFAULT NULL,
  `apprehensionaddress` text,
  `apprehensiondate` datetime DEFAULT NULL,
  `forvehicle` int(11) DEFAULT NULL,
  `vehicleid` varchar(50) DEFAULT NULL,
  `licenseno` varchar(50) DEFAULT NULL,
  `violator_address_text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_ticketno` (`ticketno`),
  KEY `ix_ticketno` (`ticketno`),
  KEY `ix_voilator_objid` (`violator_objid`),
  KEY `ix_voilator_name` (`violator_name`),
  KEY `ix_apprehensionofficer_objid` (`apprehensionofficer_objid`),
  KEY `ix_apprehensionofficer_name` (`apprehensionofficer_name`),
  KEY `ix_vehicleid` (`vehicleid`),
  KEY `ix_licenseno` (`licenseno`),
  KEY `ix_apprehensiondate` (`apprehensiondate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `ovs_violation_ticket_entry` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) DEFAULT NULL,
  `violationid` varchar(50) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `amtpaid` decimal(10,2) DEFAULT NULL,
  `violationcount` int(11) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_parentid` (`parentid`),
  KEY `ix_violationid` (`violationid`),
  CONSTRAINT `fk_ovs_violation_ticket_entry_parentid` FOREIGN KEY (`parentid`) REFERENCES `ovs_violation_ticket` (`objid`),
  CONSTRAINT `fk_ovs_violation_ticket_entry_violationid` FOREIGN KEY (`violationid`) REFERENCES `ovs_violation` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `ovs_payment` (
  `objid` varchar(50) NOT NULL,
  `reftype` varchar(50) DEFAULT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `refno` varchar(50) DEFAULT NULL,
  `refdate` date DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `voided` int(11) DEFAULT NULL,
  `entityid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_refid` (`refid`),
  KEY `ix_refno` (`refno`),
  KEY `ix_refdate` (`refdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ovs_payment_item` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) NOT NULL,
  `refid` varchar(50) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `reftype` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_parentid` (`parentid`),
  KEY `ix_refid` (`refid`),
  CONSTRAINT `fk_ovs_payment_item_parentid` FOREIGN KEY (`parentid`) REFERENCES `ovs_payment` (`objid`),
  CONSTRAINT `fk_ovs_payment_item_ticketentry` FOREIGN KEY (`refid`) REFERENCES `ovs_violation_ticket_entry` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#ADD ROLES
INSERT INTO sys_usergroup (objid,title,domain,userclass,orgclass,role)
VALUES ('OVS.INFO', 'OVS INFO', 'OVS', 'usergroup', NULL, 'INFO'),
('OVS.MASTER', 'OVS MASTER', 'OVS', 'usergroup', NULL, 'MASTER'),
('OVS.REPORT', 'OVS REPORT', 'OVS', 'usergroup', NULL, 'REPORT');



