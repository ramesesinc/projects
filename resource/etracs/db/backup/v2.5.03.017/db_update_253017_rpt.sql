alter table rptledger add column titleno varchar(30);

INSERT INTO `sys_usergroup` (`objid`, `title`, `domain`, `userclass`, `orgclass`, `role`) 
VALUES ('LANDTAX.LANDTAX', 'LANDTAX', 'LANDTAX', NULL, NULL, 'LANDTAX');

INSERT INTO `sys_usergroup_member` 
	(objid, usergroup_objid, user_objid, user_username, user_firstname, user_lastname)
select distinct 
	concat(u.objid,'-', ug.role) as objid,
	ug.objid, u.objid, u.username, u.firstname, u.lastname
from sys_user u 
	inner join sys_usergroup_member um ON u.objid = um.user_objid,
	sys_usergroup ug 
where um.usergroup_objid like 'rpt.landtax'
	and ug.domain = 'landtax' and ug.role='landtax' ;



SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE `rptledger_credit` (
  `objid` varchar(100) NOT NULL,
  `rptledgerid` varchar(50) NOT NULL,
  `type` varchar(20) NOT NULL,
  `refno` varchar(50) NOT NULL,
  `refdate` date NOT NULL,
  `payorid` varchar(50) default NULL,
  `paidby_name` longtext NOT NULL,
  `paidby_address` varchar(150) NOT NULL,
  `collector` varchar(80) NOT NULL,
  `postedby` varchar(100) NOT NULL,
  `postedbytitle` varchar(50) NOT NULL,
  `dtposted` datetime NOT NULL,
  `fromyear` int(11) NOT NULL,
  `fromqtr` int(11) NOT NULL,
  `toyear` int(11) NOT NULL,
  `toqtr` int(11) NOT NULL,
  `basic` decimal(12,2) NOT NULL,
  `basicint` decimal(12,2) NOT NULL,
  `basicdisc` decimal(12,2) NOT NULL,
  `basicidle` decimal(12,2) NOT NULL,
  `sef` decimal(12,2) NOT NULL,
  `sefint` decimal(12,2) NOT NULL,
  `sefdisc` decimal(12,2) NOT NULL,
  `firecode` decimal(12,2) NOT NULL,
  `amount` decimal(12,2) NOT NULL,
  `collectingagency` varchar(50) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_rptreceipt_payorid` USING BTREE (`payorid`),
  KEY `ix_rptreceipt_receiptno` USING BTREE (`refno`),
  KEY `FK_rptledgercredit_rptledger` USING BTREE (`rptledgerid`),
  CONSTRAINT `rptledger_credit_ibfk_1` FOREIGN KEY (`rptledgerid`) REFERENCES `rptledger` (`objid`)
) ENGINE=InnoDB;

SET FOREIGN_KEY_CHECKS=1;




alter table landadjustmentparameter add column param_objid varchar(50) null;
alter table landadjustmentparameter modify column parameter_objid varchar(50) null;







/* add year, qtr, month, day on faas for reporting support */
alter table faas add column year int;
alter table faas add column qtr int;
alter table faas add column month int;
alter table faas add column day int;

alter table faas modify txntimestamp varchar(15) null;

create index ix_faas_year on faas(year);
create index ix_faas_year_qtr on faas(year,qtr);
create index ix_faas_year_qtr_month on faas(year, qtr, month);
create index ix_faas_year_qtr_month_day on faas(year, qtr, month, day);


update faas set 
  year = substring(txntimestamp,1,4), 
  qtr = substring(txntimestamp,5,1), 
  month = substring(txntimestamp,6,2), 
  day = substring(txntimestamp,8,2);







