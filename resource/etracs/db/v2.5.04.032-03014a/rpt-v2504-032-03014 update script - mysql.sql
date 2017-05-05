#254032-03014a

CREATE TABLE `rptledgeritem_qtrly_partial` (
  `objid` varchar(50) NOT NULL,
  `rptledgerid` varchar(50) NOT NULL,
  `year` int(11) NOT NULL,
  `qtr` int(11) NOT NULL,
  `basicpaid` decimal(16,2) NOT NULL,
  `basicintpaid` decimal(16,2) NOT NULL,
  `basicdisctaken` decimal(16,2) NOT NULL,
  `basicidlepaid` decimal(16,2) NOT NULL,
  `basicidleintpaid` decimal(16,2) NOT NULL,
  `basicidledisctaken` decimal(16,2) NOT NULL,
  `sefpaid` decimal(16,2) NOT NULL,
  `sefintpaid` decimal(16,2) NOT NULL,
  `sefdisctaken` decimal(16,2) NOT NULL,
  `firecodepaid` decimal(16,2) NOT NULL,
  PRIMARY KEY (`objid`),
  KEY `FK_rptledgeritemqtrlypartial_rptledger` (`rptledgerid`)
);

alter table rptledgeritem_qtrly_partial 
add  CONSTRAINT `FK_rptledgeritemqtrlypartial_rptledger` 
FOREIGN KEY (`rptledgerid`) REFERENCES `rptledger` (`objid`);


alter table faas 
  drop column taxpayer_name,
  drop column taxpayer_address;


CREATE TABLE `faas_restriction` (
  `objid` varchar(50) NOT NULL,
  `parent_objid` varchar(50) NOT NULL,
  `ledger_objid` varchar(50) NULL,
  `state` varchar(25) NOT NULL,
  `restrictiontype_objid` varchar(50) NOT NULL,
  `txndate` date NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `receipt_objid` varchar(50) DEFAULT NULL,
  `receipt_receiptno` varchar(15) DEFAULT NULL,
  `receipt_receiptdate` datetime DEFAULT NULL,
  `receipt_amount` decimal(16,2) DEFAULT NULL,
  `receipt_lastyearpaid` int(11) DEFAULT NULL,
  `receipt_lastqtrpaid` int(11) DEFAULT NULL,
  `createdby_objid` varchar(50) DEFAULT NULL,
  `createdby_name` varchar(150) DEFAULT NULL,
  `dtcreated` datetime DEFAULT NULL,
  PRIMARY KEY (`objid`)
);  

alter table faas_restriction 
add  CONSTRAINT `FK_faas_restriction_faas` 
FOREIGN KEY (`parent_objid`) REFERENCES `faas` (`objid`);



create index ix_parent_objid on faas_restriction(parent_objid);
create index ix_ledger_objid on faas_restriction(ledger_objid);
create index ix_state on faas_restriction(state);
create index ix_receiptno on faas_restriction(receipt_receiptno);
create index ix_txndate on faas_restriction(txndate);
create index ix_restrictiontype_objid on faas_restriction(restrictiontype_objid);

replace into faas_restriction(
  objid,
  parent_objid,
  ledger_objid,
  state,
  restrictiontype_objid,
  txndate,
  remarks,
  receipt_objid,
  receipt_receiptno,
  receipt_receiptdate,
  createdby_objid,
  createdby_name,
  dtcreated
)
select
  concat(rl.objid, rlr.restrictionid) as objid,
  rl.faasid as parent_objid,
  rl.objid as ledger_objid,
  'ACTIVE' as state,
  rlr.restrictionid as restrictiontype_objid,
  f.dtapproved as txndate,
  null as remarks,
  null as receipt_objid,
  null as receipt_receiptno,
  null as receipt_receiptdate,
  null as createdby_objid,
  null as createdby_name,
  null as dtcreated
from faas f 
  inner join rptledger rl on f.objid = rl.faasid 
  inner join rptledger_restriction rlr on rl.objid = rlr.parentid;



replace into faas_restriction(
  objid,
  parent_objid,
  ledger_objid,
  state,
  restrictiontype_objid,
  txndate,
  remarks,
  receipt_objid,
  receipt_receiptno,
  receipt_receiptdate,
  createdby_objid,
  createdby_name,
  dtcreated
)
select
  concat(f.objid, f.restrictionid) as objid,
  f.objid as parent_objid,
  (select distinct objid from rptledger where faasid = f.objid) as ledger_objid,
  'ACTIVE' as state,
  f.restrictionid as restrictiontype_objid,
  f.dtapproved as txndate,
  null as remarks,
  null as receipt_objid,
  null as receipt_receiptno,
  null as receipt_receiptdate,
  null as createdby_objid,
  null as createdby_name,
  null as dtcreated
from faas f 
where restrictionid is not null 
and not exists(select * from rptledger rl 
  inner join rptledger_restriction rlr on rl.objid = rlr.parentid
  where rl.faasid = f.objid 
   and f.restrictionid = rlr.restrictionid);


INSERT INTO `sys_usergroup` (`objid`, `title`, `domain`, `userclass`, `orgclass`, `role`) VALUES ('LANDTAX.RECORD', 'RECORD', 'LANDTAX', NULL, NULL, 'RECORD');
INSERT INTO `sys_usergroup` (`objid`, `title`, `domain`, `userclass`, `orgclass`, `role`) VALUES ('LANDTAX.RECORD_APPROVER', 'RECORD APPROVER', 'LANDTAX', NULL, NULL, 'RECORD_APPROVER');


