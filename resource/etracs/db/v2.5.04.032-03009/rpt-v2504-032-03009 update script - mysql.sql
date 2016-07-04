/* v2.5.04.032-03009 */

/*============================================
**
** RPT TRANSMITTAL UPDATES 
**
============================================*/
drop table if exists rpttransmittal_item_data;
drop table if exists rpttransmittal_item;
drop table if exists rpttransmittal_log;
drop table if exists rpttransmittal;

CREATE TABLE `rpttransmittal` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) NOT NULL,
  `type` varchar(15) NOT NULL,
  `filetype` varchar(50) not null,
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
  `remarks` varchar(500) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `ux_txnno` (`txnno`),
  KEY `ix_state` (`state`),
  KEY `ix_createdby_name` (`createdby_name`),
  KEY `ix_lguname` (`lgu_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `rpttransmittal_item` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) NOT NULL,
  `state` varchar(50) NOT NULL,
  `filetype` varchar(50) NOT NULL,
  `refid` varchar(50) NOT NULL,
  `refno` varchar(150) NOT NULL,
  `message` varchar(350),
  `remarks` varchar(350),
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `ux_parentid_refid` (`parentid`,`refid`),
  KEY `ix_refid` (`refid`),
  KEY `FK_rpttransmittal_item_rpttransmittal` (`parentid`)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table rpttransmittal_item 
add CONSTRAINT `FK_rpttransmittal_item_rpttransmittal` 
FOREIGN KEY (`parentid`) REFERENCES `rpttransmittal` (`objid`);

