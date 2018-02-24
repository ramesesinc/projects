/* 254032-03018 */

alter table faasbacktax modify column tdno varchar(25) null;


alter table landdetail modify column subclass_objid varchar(50) null;
alter table landdetail modify column specificclass_objid varchar(50) null;
alter table landdetail modify column actualuse_objid varchar(50) null;
alter table landdetail modify column landspecificclass_objid varchar(50) null;



/* RYSETTING ORDINANCE INFO */
alter table landrysetting 
  add ordinanceno varchar(25),
  add ordinancedate date;


alter table bldgrysetting 
  add ordinanceno varchar(25),
  add ordinancedate date;


alter table machrysetting 
  add ordinanceno varchar(25),
  add ordinancedate date;


alter table miscrysetting 
  add ordinanceno varchar(25),
  add ordinancedate date;


alter table planttreerysetting 
  add ordinanceno varchar(25),
  add ordinancedate date;


delete from sys_var where name in ('gr_ordinance_date','gr_ordinance_no');

  



drop TABLE if exists `bldgrpu_land`;

CREATE TABLE `bldgrpu_land` (
  `objid` varchar(50) NOT NULL DEFAULT '',
  `rpu_objid` varchar(50) NOT NULL DEFAULT '',
  `landfaas_objid` varchar(50) DEFAULT NULL,
  `landrpumaster_objid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_bldgrpu_land_bldgrpuid` (`rpu_objid`),
  KEY `ix_bldgrpu_land_landfaasid` (`landfaas_objid`),
  KEY `ix_bldgrpu_land_landrpumasterid` (`landrpumaster_objid`),
  CONSTRAINT `FK_bldgrpu_land_bldgrpu` FOREIGN KEY (`rpu_objid`) REFERENCES `bldgrpu` (`objid`),
  CONSTRAINT `FK_bldgrpu_land_rpumaster` FOREIGN KEY (`landrpumaster_objid`) REFERENCES `rpumaster` (`objid`),
  CONSTRAINT `FK_bldgrpu_land_landfaas` FOREIGN KEY (`landfaas_objid`) REFERENCES `faas` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  

create table batchgr_log (
  objid varchar(50) not null,
  primary key (objid)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


alter table bldgrpu_structuraltype modify column bldgkindbucc_objid nvarchar(50) null;



alter table bldgadditionalitem add idx int;
update bldgadditionalitem set idx = 0 where idx is null;

  



/*================================================= 
*
*  PROVINCE-MUNI LEDGER SYNCHRONIZATION SUPPORT 
*
====================================================*/
CREATE TABLE `rptledger_remote` (
  `objid` varchar(50) NOT NULL,
  `remote_objid` varchar(50) NOT NULL,
  `createdby_name` varchar(255) NOT NULL,
  `createdby_title` varchar(100) DEFAULT NULL,
  `dtcreated` datetime NOT NULL,
  PRIMARY KEY (`objid`),
  CONSTRAINT `FK_rptledgerremote_rptledger` FOREIGN KEY (`objid`) REFERENCES `rptledger` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/*======================================
* AUTOMATIC MACH AV RECALC SUPPORT
=======================================*/
INSERT INTO `rptparameter` (`objid`, `state`, `name`, `caption`, `description`, `paramtype`, `minvalue`, `maxvalue`) 
VALUES ('TOTAL_VALUE', 'APPROVED', 'TOTAL_VALUE', 'TOTAL VALUE', '', 'decimal', '0', '0');




/* BATCH GR ADDITIONAL SUPPORT */
alter table batchgr_items_forrevision add section varchar(3);
alter table batchgr_items_forrevision add classification_objid varchar(50);


/* 254032-03018 */

alter table faasbacktax modify column tdno varchar(25) null;



/*===============================================================
* REALTY TAX ACCOUNT MAPPING  UPDATE 
*==============================================================*/

CREATE TABLE `landtax_lgu_account_mapping` (
  `objid` varchar(50) NOT NULL,
  `lgu_objid` varchar(50) NOT NULL,
  `revtype` varchar(50) NOT NULL,
  `revperiod` varchar(50) NOT NULL,
  `item_objid` varchar(50) NOT NULL,
  KEY `FK_landtaxlguaccountmapping_sysorg` (`lgu_objid`),
  KEY `FK_landtaxlguaccountmapping_itemaccount` (`item_objid`),
  KEY `ix_revtype` (`revtype`),
  KEY `ix_objid` (`objid`),
  CONSTRAINT `fk_landtaxlguaccountmapping_itemaccount` FOREIGN KEY (`item_objid`) REFERENCES `itemaccount` (`objid`),
  CONSTRAINT `fk_landtaxlguaccountmapping_sysorg` FOREIGN KEY (`lgu_objid`) REFERENCES `sys_org` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create index FK_landtaxlguaccountmapping_sysorg on landtax_lgu_account_mapping(lgu_objid);
create index FK_landtaxlguaccountmapping_itemaccount on landtax_lgu_account_mapping(item_objid);
create index ix_revtype on landtax_lgu_account_mapping(revtype);


 alter table landtax_lgu_account_mapping 
    add constraint fk_landtaxlguaccountmapping_itemaccount 
    foreign key (item_objid) references itemaccount (objid);
 alter table landtax_lgu_account_mapping 
    add constraint fk_landtaxlguaccountmapping_sysorg 
    foreign key (lgu_objid) references sys_org (objid);


delete from sys_rulegroup where ruleset = 'rptbilling' and name in ('BEFORE-MISC-COMP','MISC-COMP');





/*======================================================
* RPTLEDGER PAYMENT
*=====================================================*/ 
drop table if exists cashreceiptitem_rpt_noledger;
drop table if exists cashreceiptitem_rpt;
drop table if exists rptledger_payment_share; 
drop table if exists rptledger_payment_item; 
drop table if exists rptledger_payment;


CREATE TABLE `rptledger_payment` (
  `objid` varchar(100) NOT NULL,
  `rptledgerid` varchar(50) NOT NULL,
  `type` varchar(20) NOT NULL,
  `receiptid` varchar(50) NULL,
  `receiptno` varchar(50) NOT NULL,
  `receiptdate` date NOT NULL,
  `paidby_name` longtext NOT NULL,
  `paidby_address` varchar(150) NOT NULL,
  `postedby` varchar(100) NOT NULL,
  `postedbytitle` varchar(50) NOT NULL,
  `dtposted` datetime NOT NULL,
  `fromyear` int(11) NOT NULL,
  `fromqtr` int(11) NOT NULL,
  `toyear` int(11) NOT NULL,
  `toqtr` int(11) NOT NULL,
  `amount` decimal(12,2) NOT NULL,
  `collectingagency` varchar(50) DEFAULT NULL,
  `voided` int(11) NOT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create index `fk_rptledger_payment_rptledger` on rptledger_payment(`rptledgerid`) using btree;
create index `fk_rptledger_payment_cashreceipt` on rptledger_payment(`receiptid`) using btree;
create index `ix_receiptno` on rptledger_payment(`receiptno`) using btree;

alter table rptledger_payment 
add constraint `fk_rptledger_payment_cashreceipt` foreign key (`receiptid`) references `cashreceipt` (`objid`);

alter table rptledger_payment 
add constraint `fk_rptledger_payment_rptledger` foreign key (`rptledgerid`) references `rptledger` (`objid`);


CREATE TABLE `rptledger_payment_item` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(100) NOT NULL,
  `rptledgerfaasid` varchar(50) DEFAULT NULL,
  `rptledgeritemid` varchar(50) DEFAULT NULL,
  `rptledgeritemqtrlyid` varchar(50) DEFAULT NULL,
  `year` int(11) NOT NULL,
  `qtr` int(11) NOT NULL,
  `basic` decimal(16,2) NOT NULL,
  `basicint` decimal(16,2) NOT NULL,
  `basicdisc` decimal(16,2) NOT NULL,
  `basicidle` decimal(16,2) NOT NULL,
  `basicidledisc` decimal(16,2) DEFAULT NULL,
  `basicidleint` decimal(16,2) DEFAULT NULL,
  `sef` decimal(16,2) NOT NULL,
  `sefint` decimal(16,2) NOT NULL,
  `sefdisc` decimal(16,2) NOT NULL,
  `firecode` decimal(10,2) DEFAULT NULL,
  `sh` decimal(16,2) NOT NULL,
  `shint` decimal(16,2) NOT NULL,
  `shdisc` decimal(16,2) NOT NULL,
  `total` decimal(16,2) DEFAULT NULL,
  `revperiod` varchar(25) DEFAULT NULL,
  `partialled` int(11) NOT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create index `FK_rptledger_payment_item_parentid` on rptledger_payment_item(`parentid`) using btree;
create index `FK_rptledger_payment_item_rptledgerfaasid` on rptledger_payment_item(`rptledgerfaasid`) using btree;
create index `ix_rptledgeritemid` on rptledger_payment_item(`rptledgeritemid`) using btree;
create index `ix_rptledgeritemqtrlyid` on rptledger_payment_item(`rptledgeritemqtrlyid`) using btree;


alter table rptledger_payment_item 
  add constraint `fk_rptledger_payment_item_parentid` 
  foreign key (`parentid`) references `rptledger_payment` (`objid`);
alter table rptledger_payment_item 
  add constraint `fk_rptledger_payment_item_rptledgerfaasid` 
  foreign key (`rptledgerfaasid`) references `rptledgerfaas` (`objid`);


create table rptledger_payment_share (
  objid varchar(50) not null,
  parentid varchar(100) null,
  revperiod varchar(25) not null,
  revtype varchar(25) not null,
  item_objid varchar(50) not null,
  amount decimal(16,4) not null,
  sharetype varchar(25) not null,
  discount decimal(16,4) null,
  primary key (objid)
) engine=innodb charset=utf8;

alter table rptledger_payment_share
  add constraint FK_rptledger_payment_share_parentid foreign key (parentid) 
  references rptledger_payment(objid);

alter table rptledger_payment_share
  add constraint FK_rptledger_payment_share_itemaccount foreign key (item_objid) 
  references itemaccount(objid);

create index FK_parentid on rptledger_payment_share(parentid) using btree;
create index FK_item_objid on rptledger_payment_share(item_objid) using btree;



insert into rptledger_payment(
  objid,
  rptledgerid,
  type,
  receiptid,
  receiptno,
  receiptdate,
  paidby_name,
  paidby_address,
  postedby,
  postedbytitle,
  dtposted,
  fromyear,
  fromqtr,
  toyear,
  toqtr,
  amount,
  collectingagency,
  voided 
)
select 
  x.objid,
  x.rptledgerid,
  x.type,
  x.receiptid,
  x.receiptno,
  x.receiptdate,
  x.paidby_name,
  x.paidby_address,
  x.postedby,
  x.postedbytitle,
  x.dtposted,
  x.fromyear,
  (select min(qtr) from cashreceiptitem_rpt_online 
    where rptledgerid = x.rptledgerid and rptreceiptid = x.receiptid and year = x.fromyear) as fromqtr,
  x.toyear,
  (select max(qtr) from cashreceiptitem_rpt_online 
    where rptledgerid = x.rptledgerid and rptreceiptid = x.receiptid and year = x.toyear) as toqtr,
  x.amount,
  x.collectingagency,
  0 as voided
from (
  select
    concat(cro.rptledgerid, '-', cr.objid) as objid,
    cro.rptledgerid,
    cr.txntype as type,
    cr.objid as receiptid,
    c.receiptno as receiptno,
    c.receiptdate as receiptdate,
    c.paidby as paidby_name,
    c.paidbyaddress as paidby_address,
    c.collector_name as postedby,
    c.collector_title as postedbytitle,
    c.txndate as dtposted,
    min(cro.year) as fromyear,
    max(cro.year) as toyear,
    sum(
      cro.basic + cro.basicint - cro.basicdisc + cro.sef + cro.sefint - cro.sefdisc + cro.firecode +
      cro.basicidle + cro.basicidleint - cro.basicidledisc
    ) as amount,
    null as collectingagency
  from cashreceipt_rpt cr 
  inner join cashreceipt c on cr.objid = c.objid 
  inner join cashreceiptitem_rpt_online cro on c.objid = cro.rptreceiptid
  left join cashreceipt_void cv on c.objid = cv.receiptid 
  where cv.objid is null 
  group by 
    cr.objid,
    cro.rptledgerid,
    cr.txntype,
    c.receiptno,
    c.receiptdate,
    c.paidby,
    c.paidbyaddress,
    c.collector_name,
    c.collector_title,
    c.txndate 
)x;


insert into rptledger_payment_item(
  objid,
  parentid,
  rptledgerfaasid,
  rptledgeritemid,
  rptledgeritemqtrlyid,
  year,
  qtr,
  basic,
  basicint,
  basicdisc,
  basicidle,
  basicidledisc,
  basicidleint,
  sef,
  sefint,
  sefdisc,
  firecode,
  sh,
  shint,
  shdisc,
  total,
  revperiod,
  partialled
)
select
  cro.objid,
  concat(cro.rptledgerid, '-', cro.rptreceiptid) as parentid,
  cro.rptledgerfaasid,
  cro.rptledgeritemid,
  cro.rptledgeritemqtrlyid,
  cro.year,
  cro.qtr,
  cro.basic,
  cro.basicint,
  cro.basicdisc,
  cro.basicidle,
  cro.basicidledisc,
  cro.basicidleint,
  cro.sef,
  cro.sefint,
  cro.sefdisc,
  cro.firecode,
  0 as sh,
  0 as shint,
  0 as shdisc,
  cro.total,
  cro.revperiod,
  cro.partialled
from cashreceipt_rpt cr 
inner join cashreceipt c on cr.objid = c.objid 
inner join cashreceiptitem_rpt_online cro on c.objid = cro.rptreceiptid 
left join cashreceipt_void cv on c.objid = cv.receiptid 
where cv.objid is null ;



insert into rptledger_payment_share(
  objid,
  parentid,
  revperiod,
  revtype,
  item_objid,
  amount,
  sharetype,
  discount
)
select 
  x.objid,
  x.parentid,
  x.revperiod,
  x.revtype,
  x.item_objid,
  x.amount,
  x.sharetype,
  x.discount
from (
  select
    cra.objid,
    concat(cra.rptledgerid, '-', cra.rptreceiptid) as parentid,
    cra.revperiod,
    cra.revtype,
    cra.item_objid,
    cra.amount,
    cra.sharetype,
    cra.discount
  from cashreceipt_rpt cr 
  inner join cashreceipt c on cr.objid = c.objid 
  inner join cashreceiptitem_rpt_account cra on c.objid = cra.rptreceiptid 
  left join cashreceipt_void cv on c.objid = cv.receiptid 
  where cv.objid is null 
    and cra.rptledgerid is not null 
    and exists(select * from rptledger where objid = cra.rptledgerid)
    and not exists(select * from rptledger_payment_share where objid = cra.objid)
) x 
where exists(select * from rptledger_payment where objid= x.parentid);


insert into rptledger_payment(
  objid,
  rptledgerid,
  type,
  receiptid,
  receiptno,
  receiptdate,
  paidby_name,
  paidby_address,
  postedby,
  postedbytitle,
  dtposted,
  fromyear,
  fromqtr,
  toyear,
  toqtr,
  amount,
  collectingagency,
  voided 
)
select 
  objid,
  rptledgerid,
  type,
  null as receiptid,
  refno as receiptno,
  refdate,
  paidby_name,
  paidby_address,
  postedby,
  postedbytitle,
  dtposted,
  fromyear,
  fromqtr,
  toyear,
  toqtr,
  (basic + basicint - basicdisc + sef + sefint - sefdisc + basicidle + firecode) as amount,
  collectingagency,
  0 as voided 
from rptledger_credit;



alter table rptledgeritem 
  add sh decimal(16,2),
  add shdisc decimal(16,2),
  add shpaid decimal(16,2),
  add shint decimal(16,2);

update rptledgeritem set 
    sh = 0, shdisc=0, shpaid = 0, shint = 0
where sh is null ;


alter table rptledgeritem_qtrly 
  add sh decimal(16,2),
  add shdisc decimal(16,2),
  add shpaid decimal(16,2),
  add shint decimal(16,2);

update rptledgeritem_qtrly set 
    sh = 0, shdisc = 0, shpaid = 0, shint = 0
where sh is null ;



alter table rptledger_compromise_item add sh decimal(16,2);
alter table rptledger_compromise_item add shpaid decimal(16,2);
alter table rptledger_compromise_item add shint decimal(16,2);
alter table rptledger_compromise_item add shintpaid decimal(16,2);

update rptledger_compromise_item set 
    sh = 0, shpaid = 0, shint = 0, shintpaid = 0
where sh is null ;


alter table rptledger_compromise_item_credit add sh decimal(16,2);
alter table rptledger_compromise_item_credit add shint decimal(16,2);

update rptledger_compromise_item_credit set 
    sh = 0, shint = 0
where sh is null ;


