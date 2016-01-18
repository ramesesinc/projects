
alter table consolidationaffectedrpu alter column newsuffix int null
go

ALTER TABLE consolidation_task DROP CONSTRAINT consolidation_task_ibfk_1
go

ALTER TABLE subdivision_task DROP CONSTRAINT subdivision_task_ibfk_1
go



alter table rptledger add taxpayer_objid varchar(50)
go 
alter table rptledger add fullpin varchar(25)
go 
alter table rptledger add tdno varchar(25)
go 
alter table rptledger add cadastrallotno varchar(50)
go 
alter table rptledger add rputype varchar(15)
go 
alter table rptledger add txntype_objid varchar(5)
go 
alter table rptledger add classcode varchar(10)
go 
alter table rptledger add totalav decimal(16,2)
go 
alter table rptledger add totalmv decimal(16,2)
go 
alter table rptledger add totalareaha decimal(16,6)
go 
alter table rptledger add taxable decimal(16,2)
go 
alter table rptledger add owner_name varchar(50)
go 
alter table rptledger add prevtdno varchar(1000)
go 
alter table rptledger add classification_objid varchar(50)
go 
alter table rptledger add titleno varchar(30)
go 
alter table rptledger add undercompromise int
go 




INSERT INTO sys_usergroup (objid, title, domain, userclass, orgclass, role) VALUES ('LANDTAX.ADMIN', 'ADMIN', 'LANDTAX', NULL, NULL, 'ADMIN')
go 
INSERT INTO sys_usergroup (objid, title, domain, userclass, orgclass, role) VALUES ('LANDTAX.LANDTAX', 'LANDTAX', 'LANDTAX', NULL, NULL, 'LANDTAX')
go 
INSERT INTO sys_usergroup (objid, title, domain, userclass, orgclass, role) VALUES ('LANDTAX.LANDTAX_SHARED', 'SHARED', 'LANDTAX', NULL, NULL, 'SHARED')
go 
INSERT INTO sys_usergroup (objid, title, domain, userclass, orgclass, role) VALUES ('LANDTAX.REPORT', 'REPORT', 'LANDTAX', NULL, NULL, 'REPORT')
go 



INSERT INTO sys_usergroup_member 
  (objid, usergroup_objid, user_objid, user_username, user_firstname, user_lastname)
select distinct 
  (u.objid + '-' + ug.role) as objid,
  ug.objid, u.objid, u.username, u.firstname, u.lastname
from sys_user u 
  inner join sys_usergroup_member um ON u.objid = um.user_objid,
  sys_usergroup ug 
where um.usergroup_objid like 'rpt.landtax'
  and ug.domain = 'landtax' and ug.role='landtax'
  and not exists(select * from sys_usergroup_member where objid = (u.objid + '-' + ug.role))
go   




CREATE TABLE rptledger_credit (
  objid nvarchar(50) NOT NULL,
  rptledgerid nvarchar(50) NOT NULL,
  type nvarchar(20) NOT NULL,
  refno nvarchar(50) NOT NULL,
  refdate date NOT NULL,
  payorid nvarchar(50) default NULL,
  paidby_name text NOT NULL,
  paidby_address nvarchar(150) NOT NULL,
  collector nvarchar(80) NOT NULL,
  postedby nvarchar(100) NOT NULL,
  postedbytitle nvarchar(50) NOT NULL,
  dtposted datetime NOT NULL,
  fromyear int NOT NULL,
  fromqtr int NOT NULL,
  toyear int NOT NULL,
  toqtr int NOT NULL,
  basic decimal(12,2) NOT NULL,
  basicint decimal(12,2) NOT NULL,
  basicdisc decimal(12,2) NOT NULL,
  basicidle decimal(12,2) NOT NULL,
  sef decimal(12,2) NOT NULL,
  sefint decimal(12,2) NOT NULL,
  sefdisc decimal(12,2) NOT NULL,
  firecode decimal(12,2) NOT NULL,
  amount decimal(12,2) NOT NULL,
  collectingagency nvarchar(50) default NULL,
  PRIMARY KEY  (objid),
  CONSTRAINT rptledger_credit_ibfk_1 FOREIGN KEY (rptledgerid) REFERENCES rptledger (objid)
)
go

create index ix_rptreceipt_payorid on rptledger_credit (payorid)
go 
create index ix_rptreceipt_receiptno on rptledger_credit (refno)
go 
create index FK_rptledgercredit_rptledger on rptledger_credit (rptledgerid)
go 






alter table landadjustmentparameter add param_objid nvarchar(50) null
GO
alter table landadjustmentparameter alter column parameter_objid nvarchar(50) null
go 




/* add year, qtr, month, day on faas for reporting support */
alter table faas add year int
go 
alter table faas add qtr int
go 
alter table faas add month int
go 
alter table faas add day int
go 

drop index faas.ix_faas_txntimestamp
go 

alter table faas alter column  txntimestamp nvarchar(15) null
go 

create index ix_faas_year on faas(year)
go 
create index ix_faas_year_qtr on faas(year,qtr)
go 
create index ix_faas_year_qtr_month on faas(year, qtr, month)
go 
create index ix_faas_year_qtr_month_day on faas(year, qtr, month, day)
go 


update faas set 
  year = substring(txntimestamp,1,4), 
  qtr = substring(txntimestamp,5,1), 
  month = substring(txntimestamp,6,2), 
  day = substring(txntimestamp,8,2)
go 

