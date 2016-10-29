/*==================================
** V2.5.04.029
==================================*/
DROP  TABLE `rpt_redflag`;

CREATE TABLE `rpt_redflag` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) NOT NULL,
  `state` varchar(30) NOT NULL,
  `refid` varchar(50) NOT NULL,
  `refno` varchar(15) NOT NULL,
  `caseno` varchar(25) NOT NULL,
  `message` text NOT NULL,
  `filedby_date` datetime NOT NULL,
  `filedby_objid` varchar(50) NOT NULL,
  `filedby_name` varchar(150) NOT NULL,
  `action` varchar(50) NOT NULL,
  `resolvedby_objid` varchar(50) default NULL,
  `resolvedby_name` varchar(150) default NULL,
  `resolvedby_date` datetime default NULL,
  `lguid` varchar(15) NOT NULL,
  `dtclosed` datetime default NULL,
  `remarks` text,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `ux_rptredflag_caseno` (`caseno`),
  KEY `ix_rptredflag_refid` (`refid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


alter table rpt_sms modify column traceid varchar(50) null;

INSERT INTO `sys_var` (`name`, `value`, `description`, `datatype`, `category`) 
VALUES ('show_interim_watermark', '0', 'Display an INTERIM wartermark for FAAS and TD', 'checkbox', 'ASSESSOR');