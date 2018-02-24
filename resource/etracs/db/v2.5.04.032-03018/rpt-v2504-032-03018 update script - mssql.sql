/* 254032-03018*/

alter table faasbacktax alter column tdno varchar(25) null
go 



alter table landdetail alter column subclass_objid varchar(50) null
go 
alter table landdetail alter column specificclass_objid varchar(50) null
go 
alter table landdetail alter column actualuse_objid varchar(50) null
go 
alter table landdetail alter column landspecificclass_objid varchar(50) null
go 




/* RYSETTING ORDINANCE INFO */
alter table landrysetting add ordinanceno varchar(25)
go 
alter table landrysetting add ordinancedate date
go 


alter table bldgrysetting add ordinanceno varchar(25)
go 
alter table bldgrysetting add ordinancedate date
go 


alter table machrysetting add ordinanceno varchar(25)
go 
alter table machrysetting add ordinancedate date
go 


alter table miscrysetting add ordinanceno varchar(25)
go 
alter table miscrysetting add ordinancedate date
go 


alter table planttreerysetting add ordinanceno varchar(25)
go 
alter table planttreerysetting add ordinancedate date
go 


delete from sys_var where name in ('gr_ordinance_date','gr_ordinance_no')
go





drop TABLE bldgrpu_land
go 

create table bldgrpu_land (
  objid varchar(50) not null,
  rpu_objid varchar(50) not null,
  landfaas_objid varchar(50)not null,
  landrpumaster_objid varchar(50),
  primary key (objid)
)
go 


create index ix_bldgrpu_land_bldgrpuid on bldgrpu_land(rpu_objid)
go 
create index ix_bldgrpu_land_landfaasid on bldgrpu_land(landfaas_objid)
go 
create index ix_bldgrpu_land_landrpumasterid on bldgrpu_land(landrpumaster_objid)
go 


alter table bldgrpu_land 
  add constraint fk_bldgrpu_land_bldgrpu foreign key (rpu_objid) 
  references bldgrpu (objid)
go  

alter table bldgrpu_land 
  add constraint fk_bldgrpu_land_rpumaster foreign key (landrpumaster_objid) 
  references rpumaster objid)
go 

alter table bldgrpu_land 
  add constraint fk_bldgrpu_land_landfaas foreign key (landfaas_objid) 
  references faas(objid)
go 




create table batchgr_log (
  objid varchar(50) not null,
  primary key (objid)
)
go 



alter table bldgrpu_structuraltype alter column bldgkindbucc_objid nvarchar(50) null
go 



alter table bldgadditionalitem add idx int
go 

update bldgadditionalitem set idx = 0 where idx is null
go 





/*================================================= 
*
*  PROVINCE-MUNI LEDGER SYNCHRONIZATION SUPPORT 
*
====================================================*/
CREATE TABLE rptledger_remote (
  objid nvarchar(50) NOT NULL,
  remote_objid nvarchar(50) NOT NULL,
  createdby_name nvarchar(255) NOT NULL,
  createdby_title nvarchar(100) DEFAULT NULL,
  dtcreated datetime NOT NULL,
  PRIMARY KEY (objid)
)
go 

alter table rptledger_remote 
add CONSTRAINT FK_rptledgerremote_rptledger FOREIGN KEY (objid) REFERENCES rptledger (objid)
go 



/*======================================
* AUTOMATIC MACH AV RECALC SUPPORT
=======================================*/
INSERT INTO rptparameter (objid, state, name, caption, description, paramtype, minvalue, maxvalue) 
VALUES ('TOTAL_VALUE', 'APPROVED', 'TOTAL_VALUE', 'TOTAL VALUE', '', 'decimal', '0', '0')
GO 



/* BATCH GR ADDITIONAL SUPPORT */
alter table batchgr_items_forrevision add section varchar(3)
go 
alter table batchgr_items_forrevision add classification_objid varchar(50)
go 

/* 254032-03018 */

alter table faasbacktax alter column tdno varchar(25) null
go 



/*===============================================================
* realty tax account mapping  update 
*==============================================================*/

create table landtax_lgu_account_mapping (
  objid varchar(50) not null,
  lgu_objid varchar(50) not null,
  revtype varchar(50) not null,
  revperiod varchar(50) not null,
  item_objid varchar(50) not null
) 
go 

create index ix_objid on landtax_lgu_account_mapping(objid)
go 
create index fk_landtaxlguaccountmapping_sysorg on landtax_lgu_account_mapping(lgu_objid)
go 
create index fk_landtaxlguaccountmapping_itemaccount on landtax_lgu_account_mapping(item_objid)
go 
create index ix_revtype on landtax_lgu_account_mapping(revtype)
go 


 alter table landtax_lgu_account_mapping 
    add constraint fk_landtaxlguaccountmapping_itemaccount 
    foreign key (item_objid) references itemaccount (objid)
   go 

 alter table landtax_lgu_account_mapping 
    add constraint fk_landtaxlguaccountmapping_sysorg 
    foreign key (lgu_objid) references sys_org (objid)
   go 


delete from sys_rulegroup where ruleset = 'rptbilling' and name in ('before-misc-comp','misc-comp')
go 




/*======================================================
* rptledger payment
*=====================================================*/ 
create proc usp_droptable(
	@tablename varchar(50)
)
as 
if (exists(select * from sysobjects where id = object_id(@tablename)))
begin 
	exec ('drop table ' + @tablename)
end 
go 

exec usp_droptable 'cashreceiptitem_rpt_noledger'
go 

exec usp_droptable 'cashreceiptitem_rpt'
go 

exec usp_droptable 'rptledger_payment_share'
go 

exec usp_droptable 'rptledger_payment_item'
go 

exec usp_droptable 'rptledger_payment'
go 



create table rptledger_payment (
  objid varchar(100) not null,
  rptledgerid varchar(50) not null,
  type varchar(20) not null,
  receiptid varchar(50) null,
  receiptno varchar(50) not null,
  receiptdate date not null,
  paidby_name text not null,
  paidby_address varchar(150) not null,
  postedby varchar(100) not null,
  postedbytitle varchar(50) not null,
  dtposted datetime not null,
  fromyear int not null,
  fromqtr int not null,
  toyear int not null,
  toqtr int not null,
  amount decimal(12,2) not null,
  collectingagency varchar(50) default null,
  voided int not null,
  primary key (objid)
) 
go 


create index fk_rptledger_payment_rptledger on rptledger_payment(rptledgerid)
go 
create index fk_rptledger_payment_cashreceipt on rptledger_payment(receiptid)
go 
create index ix_receiptno on rptledger_payment(receiptno)
go 

alter table rptledger_payment 
add constraint fk_rptledger_payment_cashreceipt foreign key (receiptid) references cashreceipt (objid)
go 

alter table rptledger_payment 
add constraint fk_rptledger_payment_rptledger foreign key (rptledgerid) references rptledger (objid)
go 


create table rptledger_payment_item (
  objid varchar(50) not null,
  parentid varchar(100) not null,
  rptledgerfaasid varchar(50) default null,
  rptledgeritemid varchar(50) default null,
  rptledgeritemqtrlyid varchar(50) default null,
  year int not null,
  qtr int not null,
  basic decimal(16,2) not null,
  basicint decimal(16,2) not null,
  basicdisc decimal(16,2) not null,
  basicidle decimal(16,2) not null,
  basicidledisc decimal(16,2) default null,
  basicidleint decimal(16,2) default null,
  sef decimal(16,2) not null,
  sefint decimal(16,2) not null,
  sefdisc decimal(16,2) not null,
  firecode decimal(10,2) default null,
  sh decimal(16,2) not null,
  shint decimal(16,2) not null,
  shdisc decimal(16,2) not null,
  total decimal(16,2) default null,
  revperiod varchar(25) default null,
  partialled int not null,
  primary key (objid)
) 
go 

create index fk_rptledger_payment_item_parentid on rptledger_payment_item(parentid)
go 
create index fk_rptledger_payment_item_rptledgerfaasid on rptledger_payment_item(rptledgerfaasid)
go 
create index ix_rptledgeritemid on rptledger_payment_item(rptledgeritemid)
go 
create index ix_rptledgeritemqtrlyid on rptledger_payment_item(rptledgeritemqtrlyid)
go 


alter table rptledger_payment_item 
  add constraint fk_rptledger_payment_item_parentid 
  foreign key (parentid) references rptledger_payment (objid)
 go 
alter table rptledger_payment_item 
  add constraint fk_rptledger_payment_item_rptledgerfaasid 
  foreign key (rptledgerfaasid) references rptledgerfaas (objid)
 go 


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
)
go 

alter table rptledger_payment_share
  add constraint fk_rptledger_payment_share_parentid foreign key (parentid) 
  references rptledger_payment(objid)
 go 

alter table rptledger_payment_share
  add constraint fk_rptledger_payment_share_itemaccount foreign key (item_objid) 
  references itemaccount(objid)
 go 

create index fk_parentid on rptledger_payment_share(parentid)
go 
create index fk_item_objid on rptledger_payment_share(item_objid)
go 



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
    (cro.rptledgerid + '-' + cr.objid) as objid,
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
)x
go 


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
  (cro.rptledgerid + '-' + cro.rptreceiptid) as parentid,
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
where cv.objid is null 
go 





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
  cra.objid,
  (cra.rptledgerid + '-' + cra.rptreceiptid) as parentid,
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
go 




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
from rptledger_credit
go 


alter table rptledgeritem add sh decimal(16,2)
go 
alter table rptledgeritem add shdisc decimal(16,2)
go 
alter table rptledgeritem add shpaid decimal(16,2)
go 
alter table rptledgeritem add shint decimal(16,2)
go 

update rptledgeritem set 
    sh = 0, shdisc=0, shpaid = 0, shint = 0
where sh is null 
go 



alter table rptledgeritem_qtrly add sh decimal(16,2)
go 
alter table rptledgeritem_qtrly add shdisc decimal(16,2)
go 
alter table rptledgeritem_qtrly add shpaid decimal(16,2)
go 
alter table rptledgeritem_qtrly add shint decimal(16,2)
go 

update rptledgeritem_qtrly set 
    sh = 0, shdisc=0, shpaid = 0, shint = 0
where sh is null 
go 




alter table rptledger_compromise_item add sh decimal(16,2)
go 
alter table rptledger_compromise_item add shpaid decimal(16,2)
go 
alter table rptledger_compromise_item add shint decimal(16,2)
go 
alter table rptledger_compromise_item add shintpaid decimal(16,2)
go 

update rptledger_compromise_item set 
    sh = 0, shpaid = 0, shint = 0, shintpaid = 0
where sh is null 
go 


alter table rptledger_compromise_item_credit add sh decimal(16,2)
go 
alter table rptledger_compromise_item_credit add shint decimal(16,2)
go 

update rptledger_compromise_item_credit set 
    sh = 0, shint = 0
where sh is null
go 


drop proc usp_droptable
go 
