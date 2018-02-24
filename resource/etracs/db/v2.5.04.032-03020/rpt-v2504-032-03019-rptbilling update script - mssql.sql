
/*============================================================
*
* 254032-03020
*
=============================================================*/


alter table rptledger add lguid varchar(50)
go 


update rl set 
  rl.lguid = m.objid 
from rptledger rl, barangay b, sys_org m
where rl.barangayid = b.objid 
and b.parentid = m.objid 
and m.orgclass = 'municipality'
go 


update rl set 
  rl.lguid = c.objid
from rptledger rl, barangay b, sys_org d, sys_org c
where rl.barangayid = b.objid 
and b.parentid = d.objid 
and d.parent_objid = c.objid 
and d.orgclass = 'district'
go 



create table rptpayment (
  objid nvarchar(100) not null,
  type nvarchar(50) default null,
  refid nvarchar(50) not null,
  reftype nvarchar(50) not null,
  receiptid nvarchar(50) default null,
  receiptno nvarchar(50) not null,
  receiptdate date not null,
  paidby_name text not null,
  paidby_address nvarchar(150) not null,
  postedby nvarchar(100) not null,
  postedbytitle nvarchar(50) not null,
  dtposted datetime not null,
  fromyear int not null,
  fromqtr int not null,
  toyear int not null,
  toqtr int not null,
  amount decimal(12,2) not null,
  collectingagency nvarchar(50) default null,
  voided int not null,
  primary key(objid)
) 
go 

create index fk_rptpayment_rptledger on rptpayment(refid)
go 
create index fk_rptpayment_cashreceipt on rptpayment(receiptid)
go 
create index ix_receiptno on rptpayment(receiptno)
go 

alter table rptpayment 
  add constraint rptpayment_cashreceipt 
  foreign key (receiptid) references cashreceipt (objid)
go 





create table rptpayment_item (
  objid nvarchar(50) not null,
  parentid nvarchar(100) not null,
  rptledgerfaasid nvarchar(50) default null,
  year int not null,
  qtr int default null,
  revtype nvarchar(50) not null,
  revperiod nvarchar(25) default null,
  amount decimal(16,2) not null,
  interest decimal(16,2) not null,
  discount decimal(16,2) not null,
  partialled int not null,
  primary key (objid)
) 
go

create index fk_rptpayment_item_parentid on rptpayment_item (parentid)
go  
create index fk_rptpayment_item_rptledgerfaasid on rptpayment_item (rptledgerfaasid)
go  

alter table rptpayment_item
  add constraint rptpayment_item_rptledgerfaas foreign key (rptledgerfaasid) 
  references rptledgerfaas (objid)
go  

alter table rptpayment_item
  add constraint rptpayment_item_rptpayment foreign key (parentid) 
  references rptpayment (objid)
go  






create table rptpayment_share (
  objid nvarchar(50) not null,
  parentid nvarchar(100) default null,
  revperiod nvarchar(25) not null,
  revtype nvarchar(25) not null,
  sharetype nvarchar(25) not null,
  item_objid nvarchar(50) not null,
  amount decimal(16,4) not null,
  discount decimal(16,4) default null,
  primary key (objid)
) 
go 

create index fk_rptpayment_share_parentid on rptpayment_share(parentid)
go   
create index fk_rptpayment_share_item_objid on  rptpayment_share(item_objid)
go   

alter table rptpayment_share add constraint rptpayment_share_itemaccount 
  foreign key (item_objid) references itemaccount (objid)
go   

alter table rptpayment_share add constraint rptpayment_share_rptpayment 
  foreign key (parentid) references rptpayment (objid)
go   





insert into rptpayment(
  objid,
  type,
  refid,
  reftype,
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
  type, 
  rptledgerid as refid,
  'rptledger' as reftype,
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
from rptledger_payment
go 


insert into rptpayment_item(
  objid,
  parentid,
  rptledgerfaasid,
  year,
  qtr,
  revtype,
  revperiod,
  amount,
  interest,
  discount,
  partialled
)
select
  concat(objid, '-basic') as objid,
  parentid,
  rptledgerfaasid,
  year,
  qtr,
  'basic' as revtype,
  revperiod,
  basic as amount,
  basicint as interest,
  basicdisc as discount,
  partialled
from rptledger_payment_item
go 





insert into rptpayment_item(
  objid,
  parentid,
  rptledgerfaasid,
  year,
  qtr,
  revtype,
  revperiod,
  amount,
  interest,
  discount,
  partialled
)
select
  concat(objid, '-sef') as objid,
  parentid,
  rptledgerfaasid,
  year,
  qtr,
  'sef' as revtype,
  revperiod,
  sef as amount,
  sefint as interest,
  sefdisc as discount,
  partialled
from rptledger_payment_item
go 


insert into rptpayment_item(
  objid,
  parentid,
  rptledgerfaasid,
  year,
  qtr,
  revtype,
  revperiod,
  amount,
  interest,
  discount,
  partialled
)
select
  concat(objid, '-sh') as objid,
  parentid,
  rptledgerfaasid,
  year,
  qtr,
  'sh' as revtype,
  revperiod,
  sh as amount,
  shint as interest,
  shdisc as discount,
  partialled
from rptledger_payment_item
where sh > 0
go 




insert into rptpayment_item(
  objid,
  parentid,
  rptledgerfaasid,
  year,
  qtr,
  revtype,
  revperiod,
  amount,
  interest,
  discount,
  partialled
)
select
  concat(objid, '-firecode') as objid,
  parentid,
  rptledgerfaasid,
  year,
  qtr,
  'firecode' as revtype,
  revperiod,
  firecode as amount,
  0 as interest,
  0 as discount,
  partialled
from rptledger_payment_item
where firecode > 0

go 



insert into rptpayment_item(
  objid,
  parentid,
  rptledgerfaasid,
  year,
  qtr,
  revtype,
  revperiod,
  amount,
  interest,
  discount,
  partialled
)
select
  concat(objid, '-basicidle') as objid,
  parentid,
  rptledgerfaasid,
  year,
  qtr,
  'basicidle' as revtype,
  revperiod,
  basicidle as amount,
  basicidleint as interest,
  basicidledisc as discount,
  partialled
from rptledger_payment_item
where basicidle > 0
go 




update cashreceipt_rpt set txntype = 'online' where txntype = 'rptonline'
go 
update cashreceipt_rpt set txntype = 'manual' where txntype = 'rptmanual'
go 
update cashreceipt_rpt set txntype = 'compromise' where txntype = 'rptcompromise'
go 

update rptpayment set type = 'online' where type = 'rptonline'
go 
update rptpayment set type = 'manual' where type = 'rptmanual'
go 
update rptpayment set type = 'compromise' where type = 'rptcompromise'
go 







  
create table landtax_report_rptdelinquency (
  objid nvarchar(50) not null,
  rptledgerid nvarchar(50) not null,
  barangayid nvarchar(50) not null,
  year int not null,
  qtr int null,
  revtype nvarchar(50) not null,
  amount decimal(16,2) not null,
  interest decimal(16,2) not null,
  discount decimal(16,2) not null,
  dtgenerated datetime not null, 
  generatedby_name nvarchar(255) not null,
  generatedby_title nvarchar(100) not null,
  primary key (objid)
)
go 




create view vw_rptpayment_item_detail as
select
  objid,
  parentid,
  rptledgerfaasid,
  year,
  qtr,
  revperiod, 
  case when rpi.revtype = 'basic' then rpi.amount else 0 end as basic,
  case when rpi.revtype = 'basic' then rpi.interest else 0 end as basicint,
  case when rpi.revtype = 'basic' then rpi.discount else 0 end as basicdisc,
  case when rpi.revtype = 'basic' then rpi.interest - rpi.discount else 0 end as basicdp,
  case when rpi.revtype = 'basic' then rpi.amount + rpi.interest - rpi.discount else 0 end as basicnet,
  case when rpi.revtype = 'basicidle' then rpi.amount + rpi.interest - rpi.discount else 0 end as basicidle,
  case when rpi.revtype = 'basicidle' then rpi.interest else 0 end as basicidleint,
  case when rpi.revtype = 'basicidle' then rpi.discount else 0 end as basicidledisc,
  case when rpi.revtype = 'basicidle' then rpi.interest - rpi.discount else 0 end as basicidledp,
  case when rpi.revtype = 'sef' then rpi.amount else 0 end as sef,
  case when rpi.revtype = 'sef' then rpi.interest else 0 end as sefint,
  case when rpi.revtype = 'sef' then rpi.discount else 0 end as sefdisc,
  case when rpi.revtype = 'sef' then rpi.interest - rpi.discount else 0 end as sefdp,
  case when rpi.revtype = 'sef' then rpi.amount + rpi.interest - rpi.discount else 0 end as sefnet,
  case when rpi.revtype = 'firecode' then rpi.amount + rpi.interest - rpi.discount else 0 end as firecode,
  case when rpi.revtype = 'sh' then rpi.amount + rpi.interest - rpi.discount else 0 end as sh,
  case when rpi.revtype = 'sh' then rpi.interest else 0 end as shint,
  case when rpi.revtype = 'sh' then rpi.discount else 0 end as shdisc,
  case when rpi.revtype = 'sh' then rpi.interest - rpi.discount else 0 end as shdp,
  rpi.amount + rpi.interest - rpi.discount as amount,
  rpi.partialled as partialled 
from rptpayment_item rpi
go




create view vw_rptpayment_item as
select 
    x.parentid,
    x.rptledgerfaasid,
    x.year,
    x.qtr,
    x.revperiod,
    sum(x.basic) as basic,
    sum(x.basicint) as basicint,
    sum(x.basicdisc) as basicdisc,
    sum(x.basicdp) as basicdp,
    sum(x.basicnet) as basicnet,
    sum(x.basicidle) as basicidle,
    sum(x.basicidleint) as basicidleint,
    sum(x.basicidledisc) as basicidledisc,
    sum(x.basicidledp) as basicidledp,
    sum(x.sef) as sef,
    sum(x.sefint) as sefint,
    sum(x.sefdisc) as sefdisc,
    sum(x.sefdp) as sefdp,
    sum(x.sefnet) as sefnet,
    sum(x.firecode) as firecode,
    sum(x.sh) as sh,
    sum(x.shint) as shint,
    sum(x.shdisc) as shdisc,
    sum(x.shdp) as shdp,
    sum(x.amount) as amount,
    max(x.partialled) as partialled
from vw_rptpayment_item_detail x
group by 
    x.parentid,
    x.rptledgerfaasid,
    x.year,
    x.qtr,
    x.revperiod
go


create view vw_landtax_report_rptdelinquency_detail 
as
select
  objid,
  rptledgerid,
  barangayid,
  year,
  qtr,
  dtgenerated,
  generatedby_name,
  generatedby_title,
  case when revtype = 'basic' then amount else 0 end as basic,
  case when revtype = 'basic' then interest else 0 end as basicint,
  case when revtype = 'basic' then discount else 0 end as basicdisc,
  case when revtype = 'basic' then interest - discount else 0 end as basicdp,
  case when revtype = 'basic' then amount + interest - discount else 0 end as basicnet,
  case when revtype = 'basicidle' then amount else 0 end as basicidle,
  case when revtype = 'basicidle' then interest else 0 end as basicidleint,
  case when revtype = 'basicidle' then discount else 0 end as basicidledisc,
  case when revtype = 'basicidle' then interest - discount else 0 end as basicidledp,
  case when revtype = 'basicidle' then amount + interest - discount else 0 end as basicidlenet,
  case when revtype = 'sef' then amount else 0 end as sef,
  case when revtype = 'sef' then interest else 0 end as sefint,
  case when revtype = 'sef' then discount else 0 end as sefdisc,
  case when revtype = 'sef' then interest - discount else 0 end as sefdp,
  case when revtype = 'sef' then amount + interest - discount else 0 end as sefnet,
  case when revtype = 'firecode' then amount else 0 end as firecode,
  case when revtype = 'firecode' then interest else 0 end as firecodeint,
  case when revtype = 'firecode' then discount else 0 end as firecodedisc,
  case when revtype = 'firecode' then interest - discount else 0 end as firecodedp,
  case when revtype = 'firecode' then amount + interest - discount else 0 end as firecodenet,
  case when revtype = 'sh' then amount else 0 end as sh,
  case when revtype = 'sh' then interest else 0 end as shint,
  case when revtype = 'sh' then discount else 0 end as shdisc,
  case when revtype = 'sh' then interest - discount else 0 end as shdp,
  case when revtype = 'sh' then amount + interest - discount else 0 end as shnet,
  amount + interest - discount as total
from landtax_report_rptdelinquency
go



create view vw_landtax_report_rptdelinquency
as
select
  rptledgerid,
  barangayid,
  year,
  qtr,
  dtgenerated,
  generatedby_name,
  generatedby_title,
  sum(basic) as basic,
  sum(basicint) as basicint,
  sum(basicdisc) as basicdisc,
  sum(basicdp) as basicdp,
  sum(basicnet) as basicnet,
  sum(basicidle) as basicidle,
  sum(basicidleint) as basicidleint,
  sum(basicidledisc) as basicidledisc,
  sum(basicidledp) as basicidledp,
  sum(basicidlenet) as basicidlenet,
  sum(sef) as sef,
  sum(sefint) as sefint,
  sum(sefdisc) as sefdisc,
  sum(sefdp) as sefdp,
  sum(sefnet) as sefnet,
  sum(firecode) as firecode,
  sum(firecodeint) as firecodeint,
  sum(firecodedisc) as firecodedisc,
  sum(firecodedp) as firecodedp,
  sum(firecodenet) as firecodenet,
  sum(sh) as sh,
  sum(shint) as shint,
  sum(shdisc) as shdisc,
  sum(shdp) as shdp,
  sum(shnet) as shnet,
  sum(total) as total
from vw_landtax_report_rptdelinquency_detail
group by 
  rptledgerid,
  barangayid,
  year,
  qtr,
  dtgenerated,
  generatedby_name,
  generatedby_title
go





create table rptledger_item (
  objid nvarchar(50) not null,
  parentid nvarchar(50) not null,
  rptledgerfaasid nvarchar(50) default null,
  remarks nvarchar(100) default null,
  basicav decimal(16,2) not null,
  sefav decimal(16,2) not null,
  av decimal(16,2) not null,
  revtype nvarchar(50) not null,
  year int not null,
  amount decimal(16,2) not null,
  amtpaid decimal(16,2) not null,
  priority int not null,
  taxdifference int not null,
  system int not null,
  primary key (objid)
) 
go 

create index fk_rptledger_item_rptledger on rptledger_item (parentid)
go  

alter table rptledger_item 
  add constraint fk_rptledger_item_rptledger foreign key (parentid) 
  references rptledger (objid)
go 



insert into rptledger_item (
  objid,
  parentid,
  rptledgerfaasid,
  remarks,
  basicav,
  sefav,
  av,
  revtype,
  year,
  amount,
  amtpaid,
  priority,
  taxdifference,
  system
)
select 
  concat(rli.objid, '-basic') as objid,
  rli.rptledgerid as parentid,
  rli.rptledgerfaasid,
  rli.remarks,
  isnull(rli.basicav, rli.av),
  isnull(rli.sefav, rli.av),
  rli.av,
  'basic' as revtype,
  rli.year,
  rli.basic as amount,
  rli.basicpaid as amtpaid,
  10000 as priority,
  rli.taxdifference,
  0 as system
from rptledgeritem rli 
  inner join rptledger rl on rli.rptledgerid = rl.objid 
where rl.state = 'APPROVED' 
and rli.basic > 0 
and rli.basicpaid < rli.basic
go 




insert into rptledger_item (
  objid,
  parentid,
  rptledgerfaasid,
  remarks,
  basicav,
  sefav,
  av,
  revtype,
  year,
  amount,
  amtpaid,
  priority,
  taxdifference,
  system
)
select 
  concat(rli.objid, '-sef') as objid,
  rli.rptledgerid as parentid,
  rli.rptledgerfaasid,
  rli.remarks,
  isnull(rli.basicav, rli.av),
  isnull(rli.sefav, rli.av),
  rli.av,
  'sef' as revtype,
  rli.year,
  rli.sef as amount,
  rli.sefpaid as amtpaid,
  10000 as priority,
  rli.taxdifference,
  0 as system
from rptledgeritem rli 
  inner join rptledger rl on rli.rptledgerid = rl.objid 
where rl.state = 'APPROVED' 
and rli.sef > 0 
and rli.sefpaid < rli.sef
go 




insert into rptledger_item (
  objid,
  parentid,
  rptledgerfaasid,
  remarks,
  basicav,
  sefav,
  av,
  revtype,
  year,
  amount,
  amtpaid,
  priority,
  taxdifference,
  system
)
select 
  concat(rli.objid, '-firecode') as objid,
  rli.rptledgerid as parentid,
  rli.rptledgerfaasid,
  rli.remarks,
  isnull(rli.basicav, rli.av),
  isnull(rli.sefav, rli.av),
  rli.av,
  'firecode' as revtype,
  rli.year,
  rli.firecode as amount,
  rli.firecodepaid as amtpaid,
  1 as priority,
  rli.taxdifference,
  0 as system
from rptledgeritem rli 
  inner join rptledger rl on rli.rptledgerid = rl.objid 
where rl.state = 'APPROVED' 
and rli.firecode > 0 
and rli.firecodepaid < rli.firecode
go 



insert into rptledger_item (
  objid,
  parentid,
  rptledgerfaasid,
  remarks,
  basicav,
  sefav,
  av,
  revtype,
  year,
  amount,
  amtpaid,
  priority,
  taxdifference,
  system
)
select 
  concat(rli.objid, '-basicidle') as objid,
  rli.rptledgerid as parentid,
  rli.rptledgerfaasid,
  rli.remarks,
  isnull(rli.basicav, rli.av),
  isnull(rli.sefav, rli.av),
  rli.av,
  'basicidle' as revtype,
  rli.year,
  rli.basicidle as amount,
  rli.basicidlepaid as amtpaid,
  5 as priority,
  rli.taxdifference,
  0 as system
from rptledgeritem rli 
  inner join rptledger rl on rli.rptledgerid = rl.objid 
where rl.state = 'APPROVED' 
and rli.basicidle > 0 
and rli.basicidlepaid < rli.basicidle
go 


insert into rptledger_item (
  objid,
  parentid,
  rptledgerfaasid,
  remarks,
  basicav,
  sefav,
  av,
  revtype,
  year,
  amount,
  amtpaid,
  priority,
  taxdifference,
  system
)
select 
  concat(rli.objid, '-sh') as objid,
  rli.rptledgerid as parentid,
  rli.rptledgerfaasid,
  rli.remarks,
  isnull(rli.basicav, rli.av),
  isnull(rli.sefav, rli.av),
  rli.av,
  'sh' as revtype,
  rli.year,
  rli.sh as amount,
  rli.shpaid as amtpaid,
  10 as priority,
  rli.taxdifference,
  0 as system
from rptledgeritem rli 
  inner join rptledger rl on rli.rptledgerid = rl.objid 
where rl.state = 'APPROVED' 
and rli.sh > 0 
and rli.shpaid < rli.sh
go 








/*====================================================================================
*
* RPTLEDGER AND RPTBILLING RULE SUPPORT 
*
======================================================================================*/

INSERT INTO sys_ruleset (name, title, packagename, domain, role, permission) 
VALUES ('rptledger', 'Ledger Billing Rules', 'rptledger', 'LANDTAX', 'RULE_AUTHOR', NULL)
go 

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('INIT', 'rptledger', 'Init', '0')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('LEDGER_ITEM', 'rptledger', 'Ledger Item Posting', '1')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('TAX', 'rptledger', 'Tax Computation', '2')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER_TAX', 'rptledger', 'Post Tax Computation', '3')
go 






declare  @ruleset varchar(50)
select  @ruleset = 'rptledger' 

delete from sys_rule_action_param where parentid in ( 
  select ra.objid 
  from sys_rule r, sys_rule_action ra 
  where r.ruleset=@ruleset and ra.parentid=r.objid 
)

delete from sys_rule_action where parentid in ( 
  select objid from sys_rule 
  where ruleset=@ruleset 
)

delete from sys_rule_condition_constraint where parentid in ( 
  select rc.objid 
  from sys_rule r, sys_rule_condition rc 
  where r.ruleset=@ruleset and rc.parentid=r.objid 
)

delete from sys_rule_condition_var where parentid in ( 
  select rc.objid 
  from sys_rule r, sys_rule_condition rc 
  where r.ruleset=@ruleset and rc.parentid=r.objid 
)

delete from sys_rule_condition where parentid in ( 
  select objid from sys_rule where ruleset=@ruleset 
)

delete from sys_rule_deployed where objid in ( 
  select objid from sys_rule where ruleset=@ruleset 
)

delete from sys_rule where ruleset=@ruleset 
go 





declare @ruleset varchar(50)
set @ruleset = 'rptbilling' 


delete from sys_rule_action_param where parentid in ( 
  select ra.objid 
  from sys_rule r, sys_rule_action ra 
  where r.ruleset=@ruleset and ra.parentid=r.objid 
)

delete from sys_rule_action where parentid in ( 
  select objid from sys_rule 
  where ruleset=@ruleset 
)

delete from sys_rule_condition_constraint where parentid in ( 
  select rc.objid 
  from sys_rule r, sys_rule_condition rc 
  where r.ruleset=@ruleset and rc.parentid=r.objid 
)

delete from sys_rule_condition_var where parentid in ( 
  select rc.objid 
  from sys_rule r, sys_rule_condition rc 
  where r.ruleset=@ruleset and rc.parentid=r.objid 
)

delete from sys_rule_condition where parentid in ( 
  select objid from sys_rule where ruleset=@ruleset 
)

delete from sys_rule_deployed where objid in ( 
  select objid from sys_rule where ruleset=@ruleset 
)

delete from sys_rule where ruleset=@ruleset 
go 







/* REMOVE rptledger and rptbilling RULE DEFS */
declare @ruleset varchar(50)
select @ruleset='rptledger'


delete from sys_rule_actiondef_param where parentid in ( 
  select ra.objid from sys_ruleset_actiondef rsa 
    inner join sys_rule_actiondef ra on ra.objid=rsa.actiondef 
  where rsa.ruleset=@ruleset
)
delete from sys_rule_actiondef where objid in ( 
  select actiondef from sys_ruleset_actiondef where ruleset=@ruleset 
)

delete from sys_rule_fact_field where parentid in ( 
  select rf.objid from sys_ruleset_fact rsf 
    inner join sys_rule_fact rf on rf.objid=rsf.rulefact 
  where rsf.ruleset=@ruleset 
)
delete from sys_rule_fact where objid in ( 
  select rulefact from sys_ruleset_fact where ruleset=@ruleset 
)

delete from sys_ruleset_fact where ruleset=@ruleset
delete from sys_ruleset_actiondef where ruleset=@ruleset
delete from sys_rulegroup where ruleset=@ruleset
delete from sys_ruleset where name=@ruleset
go 





declare @ruleset varchar(50)
select  @ruleset='rptbilling'

delete from sys_rule_actiondef_param where parentid in ( 
  select ra.objid from sys_ruleset_actiondef rsa 
    inner join sys_rule_actiondef ra on ra.objid=rsa.actiondef 
  where rsa.ruleset=@ruleset
)
delete from sys_rule_actiondef where objid in ( 
  select actiondef from sys_ruleset_actiondef where ruleset=@ruleset 
)

delete from sys_rule_fact_field where parentid in ( 
  select rf.objid from sys_ruleset_fact rsf 
    inner join sys_rule_fact rf on rf.objid=rsf.rulefact 
  where rsf.ruleset=@ruleset 
)
delete from sys_rule_fact where objid in ( 
  select rulefact from sys_ruleset_fact where ruleset=@ruleset 
)

delete from sys_ruleset_fact where ruleset=@ruleset
delete from sys_ruleset_actiondef where ruleset=@ruleset
delete from sys_rulegroup where ruleset=@ruleset
delete from sys_ruleset where name=@ruleset
go 



INSERT INTO sys_ruleset (name, title, packagename, domain, role, permission) VALUES ('rptbilling', 'RPT Billing Rules', 'rptbilling', 'LANDTAX', 'RULE_AUTHOR', NULL)
go 
INSERT INTO sys_ruleset (name, title, packagename, domain, role, permission) VALUES ('rptledger', 'Ledger Billing Rules', 'rptledger', 'LANDTAX', 'RULE_AUTHOR', NULL)
go 

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER_TAX', 'rptledger', 'Post Tax Computation', '3')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('LEDGER_ITEM', 'rptledger', 'Ledger Item Posting', '1')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('TAX', 'rptledger', 'Tax Computation', '2')
go 



INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER_DISCOUNT', 'rptbilling', 'After Discount Computation', '10')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER_PENALTY', 'rptbilling', 'After Penalty Computation', '8')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER_SUMMARY', 'rptbilling', 'After Summary', '21')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('BEFORE_SUMMARY', 'rptbilling', 'Before Summary ', '19')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('BRGY_SHARE', 'rptbilling', 'Barangay Share Computation', '25')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('DISCOUNT', 'rptbilling', 'Discount Computation', '9')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('INIT', 'rptbilling', 'Init', '0')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('LGU_SHARE', 'rptbilling', 'LGU Share Computation', '26')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('PENALTY', 'rptbilling', 'Penalty Computation', '7')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('PROV_SHARE', 'rptbilling', 'Province Share Computation', '27')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('SUMMARY', 'rptbilling', 'Summary', '20')
go 
