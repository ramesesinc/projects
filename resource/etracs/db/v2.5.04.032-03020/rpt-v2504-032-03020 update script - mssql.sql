
/*============================================================
*
* 254032-03020
*
=============================================================*/
update cashreceiptitem_rpt_account set discount= 0 where discount is null
go 

alter table rptledger add lguid nvarchar(50)
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

create index fk_rptpayment_cashreceipt on rptpayment(receiptid)
go 
create index ix_refid on rptpayment(refid)
go 
create index ix_receiptno on rptpayment(receiptno)
go 

alter table rptpayment 
  add constraint fk_rptpayment_cashreceipt 
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
  priority int not null,
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
  partialled,
  priority
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
  partialled,
  10000 as priority
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
  partialled,
  priority
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
  partialled,
  10000 as priority
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
  partialled,
  priority
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
  partialled,
  100 as priority
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
  partialled,
  priority
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
  partialled,
  50 as priority
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
  partialled,
  priority
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
  partialled,
  200 as priority
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

declare @ruleset varchar(50)
select @ruleset = 'rptledger' 


delete from sys_rule_action_param where parentid in ( 
  select ra.objid 
  from sys_rule r, sys_rule_action ra 
  where r.ruleset=@ruleset and ra.parentid=r.objid 
)

delete from sys_rule_actiondef_param where parentid in ( 
  select ra.objid from sys_ruleset_actiondef rsa 
    inner join sys_rule_actiondef ra on ra.objid=rsa.actiondef 
  where rsa.ruleset=@ruleset
)

delete from sys_rule_actiondef where objid in ( 
  select actiondef from sys_ruleset_actiondef where ruleset=@ruleset 
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

delete from sys_ruleset_fact where ruleset=@ruleset

delete from sys_ruleset_actiondef where ruleset=@ruleset

delete from sys_rulegroup where ruleset=@ruleset 

delete from sys_ruleset where name=@ruleset 
go 


declare @ruleset varchar(50)
select @ruleset = 'rptbilling' 


delete from sys_rule_action_param where parentid in ( 
  select ra.objid 
  from sys_rule r, sys_rule_action ra 
  where r.ruleset=@ruleset and ra.parentid=r.objid 
)

delete from sys_rule_actiondef_param where parentid in ( 
  select ra.objid from sys_ruleset_actiondef rsa 
    inner join sys_rule_actiondef ra on ra.objid=rsa.actiondef 
  where rsa.ruleset=@ruleset
)

delete from sys_rule_actiondef where objid in ( 
  select actiondef from sys_ruleset_actiondef where ruleset=@ruleset 
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

delete from sys_ruleset_fact where ruleset=@ruleset

delete from sys_ruleset_actiondef where ruleset=@ruleset

delete from sys_rulegroup where ruleset=@ruleset 

delete from sys_ruleset where name=@ruleset 
go 





INSERT INTO sys_ruleset (name, title, packagename, domain, role, permission) VALUES ('rptbilling', 'RPT Billing Rules', 'rptbilling', 'LANDTAX', 'RULE_AUTHOR', NULL)
go 
INSERT INTO sys_ruleset (name, title, packagename, domain, role, permission) VALUES ('rptledger', 'Ledger Billing Rules', 'rptledger', 'LANDTAX', 'RULE_AUTHOR', NULL)
go 


INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('LEDGER_ITEM', 'rptledger', 'Ledger Item Posting', '1')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('TAX', 'rptledger', 'Tax Computation', '2')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER_TAX', 'rptledger', 'Post Tax Computation', '3')
go 


INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('INIT', 'rptbilling', 'Init', '0')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('DISCOUNT', 'rptbilling', 'Discount Computation', '9')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER_DISCOUNT', 'rptbilling', 'After Discount Computation', '10')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('PENALTY', 'rptbilling', 'Penalty Computation', '7')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER_PENALTY', 'rptbilling', 'After Penalty Computation', '8')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('BEFORE_SUMMARY', 'rptbilling', 'Before Summary ', '19')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('SUMMARY', 'rptbilling', 'Summary', '20')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER_SUMMARY', 'rptbilling', 'After Summary', '21')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('BRGY_SHARE', 'rptbilling', 'Barangay Share Computation', '25')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('PROV_SHARE', 'rptbilling', 'Province Share Computation', '27')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('LGU_SHARE', 'rptbilling', 'LGU Share Computation', '26')
go 




create view vw_landtax_lgu_account_mapping
as 
select 
  ia.org_objid as org_objid,
  ia.org_name as org_name, 
  o.orgclass as org_class, 
  p.objid as parent_objid,
  p.code as parent_code,
  p.title as parent_title,
  ia.objid as item_objid,
  ia.code as item_code,
  ia.title as item_title,
  ia.fund_objid as item_fund_objid, 
  ia.fund_code as item_fund_code,
  ia.fund_title as item_fund_title,
  ia.type as item_type,
  pt.tag as item_tag
from itemaccount ia
inner join itemaccount p on ia.parentid = p.objid 
inner join itemaccount_tag pt on p.objid = pt.acctid
inner join sys_org o on ia.org_objid = o.objid 
where p.state = 'APPROVED'
go 



/*=============================================================
*
* COMPROMISE UPDATE 
*
==============================================================*/


CREATE TABLE rptcompromise (
  objid nvarchar(50) NOT NULL,
  state nvarchar(25) NOT NULL,
  txnno nvarchar(25) NOT NULL,
  txndate date NOT NULL,
  faasid nvarchar(50) DEFAULT NULL,
  rptledgerid nvarchar(50) NOT NULL,
  lastyearpaid int NOT NULL,
  lastqtrpaid int NOT NULL,
  startyear int NOT NULL,
  startqtr int NOT NULL,
  endyear int NOT NULL,
  endqtr int NOT NULL,
  enddate date NOT NULL,
  cypaymentrequired int DEFAULT NULL,
  cypaymentorno nvarchar(10) DEFAULT NULL,
  cypaymentordate date DEFAULT NULL,
  cypaymentoramount decimal(10,2) DEFAULT NULL,
  downpaymentrequired int NOT NULL,
  downpaymentrate decimal(10,0) NOT NULL,
  downpayment decimal(10,2) NOT NULL,
  downpaymentorno nvarchar(50) DEFAULT NULL,
  downpaymentordate date DEFAULT NULL,
  term int NOT NULL,
  numofinstallment int NOT NULL,
  amount decimal(16,2) NOT NULL,
  amtforinstallment decimal(16,2) NOT NULL,
  amtpaid decimal(16,2) NOT NULL,
  firstpartyname nvarchar(100) NOT NULL,
  firstpartytitle nvarchar(50) NOT NULL,
  firstpartyaddress nvarchar(100) NOT NULL,
  firstpartyctcno nvarchar(15) NOT NULL,
  firstpartyctcissued nvarchar(100) NOT NULL,
  firstpartyctcdate date NOT NULL,
  firstpartynationality nvarchar(50) NOT NULL,
  firstpartystatus nvarchar(50) NOT NULL,
  firstpartygender nvarchar(10) NOT NULL,
  secondpartyrepresentative nvarchar(100) NOT NULL,
  secondpartyname nvarchar(100) NOT NULL,
  secondpartyaddress nvarchar(100) NOT NULL,
  secondpartyctcno nvarchar(15) NOT NULL,
  secondpartyctcissued nvarchar(100) NOT NULL,
  secondpartyctcdate date NOT NULL,
  secondpartynationality nvarchar(50) NOT NULL,
  secondpartystatus nvarchar(50) NOT NULL,
  secondpartygender nvarchar(10) NOT NULL,
  dtsigned date DEFAULT NULL,
  notarizeddate date DEFAULT NULL,
  notarizedby nvarchar(100) DEFAULT NULL,
  notarizedbytitle nvarchar(50) DEFAULT NULL,
  signatories nvarchar(1000) NOT NULL,
  manualdiff decimal(16,2) NOT NULL DEFAULT '0.00',
  cypaymentreceiptid nvarchar(50) DEFAULT NULL,
  downpaymentreceiptid nvarchar(50) DEFAULT NULL,
  PRIMARY KEY (objid)
)
go 

create index ix_rptcompromise_faasid on rptcompromise(faasid)
go 

create index ix_rptcompromise_ledgerid on rptcompromise(rptledgerid)
go 

alter table rptcompromise add CONSTRAINT fk_rptcompromise_faas 
  FOREIGN KEY (faasid) REFERENCES faas (objid)
go 

alter table rptcompromise add CONSTRAINT fk_rptcompromise_rptledger 
  FOREIGN KEY (rptledgerid) REFERENCES rptledger (objid)
go 




CREATE TABLE rptcompromise_installment (
  objid nvarchar(50) NOT NULL,
  parentid nvarchar(50) NOT NULL,
  installmentno int NOT NULL,
  duedate date NOT NULL,
  amount decimal(16,2) NOT NULL,
  amtpaid decimal(16,2) NOT NULL,
  fullypaid int NOT NULL,
  PRIMARY KEY (objid)
)
go 


create index ix_rptcompromise_installment_rptcompromiseid on rptcompromise_installment(parentid)
go 

alter table rptcompromise_installment 
  add CONSTRAINT fk_rptcompromise_installment_rptcompromise 
  FOREIGN KEY (parentid) REFERENCES rptcompromise (objid)
go 



  CREATE TABLE rptcompromise_credit (
  objid nvarchar(50) NOT NULL,
  parentid nvarchar(50) NOT NULL,
  receiptid nvarchar(50) DEFAULT NULL,
  installmentid nvarchar(50) DEFAULT NULL,
  collector_name nvarchar(100) NOT NULL,
  collector_title nvarchar(50) NOT NULL,
  orno nvarchar(10) NOT NULL,
  ordate date NOT NULL,
  oramount decimal(16,2) NOT NULL,
  amount decimal(16,2) NOT NULL,
  mode nvarchar(50) NOT NULL,
  paidby nvarchar(150) NOT NULL,
  paidbyaddress nvarchar(100) NOT NULL,
  partial int DEFAULT NULL,
  remarks nvarchar(100) DEFAULT NULL,
  PRIMARY KEY (objid)
)
go  

create index ix_rptcompromise_credit_parentid on rptcompromise_credit(parentid)
go 

create index ix_rptcompromise_credit_receiptid on rptcompromise_credit(receiptid)
go 

create index ix_rptcompromise_credit_installmentid on rptcompromise_credit(installmentid)
go 


alter table rptcompromise_credit 
  add CONSTRAINT fk_rptcompromise_credit_rptcompromise_installment 
  FOREIGN KEY (installmentid) REFERENCES rptcompromise_installment (objid)
go   

alter table rptcompromise_credit 
  add CONSTRAINT fk_rptcompromise_credit_cashreceipt 
  FOREIGN KEY (receiptid) REFERENCES cashreceipt (objid)
go   

alter table rptcompromise_credit 
  add CONSTRAINT fk_rptcompromise_credit_rptcompromise 
  FOREIGN KEY (parentid) REFERENCES rptcompromise (objid)
go   



CREATE TABLE rptcompromise_item (
  objid nvarchar(50) NOT NULL,
  parentid nvarchar(50) NOT NULL,
  rptledgerfaasid nvarchar(50) NOT NULL,
  revtype nvarchar(50) NOT NULL,
  revperiod nvarchar(50) NOT NULL,
  year int NOT NULL,
  amount decimal(16,2) NOT NULL,
  amtpaid decimal(16,2) NOT NULL,
  interest decimal(16,2) NOT NULL,
  interestpaid decimal(16,2) NOT NULL,
  priority int DEFAULT NULL,
  taxdifference int DEFAULT NULL,
  PRIMARY KEY (objid)
)
go 

create index ix_rptcompromise_item_rptcompromise on rptcompromise_item (parentid)
go   
create index ix_rptcompromise_item_rptledgerfaas on rptcompromise_item (rptledgerfaasid)
go   

alter table rptcompromise_item 
  add CONSTRAINT fk_rptcompromise_item_rptcompromise 
  FOREIGN KEY (parentid) REFERENCES rptcompromise (objid)
go   

alter table rptcompromise_item 
  add CONSTRAINT fk_rptcompromise_item_rptledgerfaas 
  FOREIGN KEY (rptledgerfaasid) REFERENCES rptledgerfaas (objid)
go   




/*=============================================================
*
* MIGRATE COMPROMISE RECORDS 
*
==============================================================*/
insert into rptcompromise(
    objid,
    state,
    txnno,
    txndate,
    faasid,
    rptledgerid,
    lastyearpaid,
    lastqtrpaid,
    startyear,
    startqtr,
    endyear,
    endqtr,
    enddate,
    cypaymentrequired,
    cypaymentorno,
    cypaymentordate,
    cypaymentoramount,
    downpaymentrequired,
    downpaymentrate,
    downpayment,
    downpaymentorno,
    downpaymentordate,
    term,
    numofinstallment,
    amount,
    amtforinstallment,
    amtpaid,
    firstpartyname,
    firstpartytitle,
    firstpartyaddress,
    firstpartyctcno,
    firstpartyctcissued,
    firstpartyctcdate,
    firstpartynationality,
    firstpartystatus,
    firstpartygender,
    secondpartyrepresentative,
    secondpartyname,
    secondpartyaddress,
    secondpartyctcno,
    secondpartyctcissued,
    secondpartyctcdate,
    secondpartynationality,
    secondpartystatus,
    secondpartygender,
    dtsigned,
    notarizeddate,
    notarizedby,
    notarizedbytitle,
    signatories,
    manualdiff,
    cypaymentreceiptid,
    downpaymentreceiptid
)
select 
    objid,
    state,
    txnno,
    txndate,
    faasid,
    rptledgerid,
    lastyearpaid,
    lastqtrpaid,
    startyear,
    startqtr,
    endyear,
    endqtr,
    enddate,
    cypaymentrequired,
    cypaymentorno,
    cypaymentordate,
    cypaymentoramount,
    downpaymentrequired,
    downpaymentrate,
    downpayment,
    downpaymentorno,
    downpaymentordate,
    term,
    numofinstallment,
    amount,
    amtforinstallment,
    amtpaid,
    firstpartyname,
    firstpartytitle,
    firstpartyaddress,
    firstpartyctcno,
    firstpartyctcissued,
    firstpartyctcdate,
    firstpartynationality,
    firstpartystatus,
    firstpartygender,
    secondpartyrepresentative,
    secondpartyname,
    secondpartyaddress,
    secondpartyctcno,
    secondpartyctcissued,
    secondpartyctcdate,
    secondpartynationality,
    secondpartystatus,
    secondpartygender,
    dtsigned,
    notarizeddate,
    notarizedby,
    notarizedbytitle,
    signatories,
    manualdiff,
    cypaymentreceiptid,
    downpaymentreceiptid
from rptledger_compromise
go 


insert into rptcompromise_installment(
    objid,
    parentid,
    installmentno,
    duedate,
    amount,
    amtpaid,
    fullypaid
)
select 
    objid,
    rptcompromiseid,
    installmentno,
    duedate,
    amount,
    amtpaid,
    fullypaid
from rptledger_compromise_installment    
go 


insert into rptcompromise_credit(
    objid,
    parentid,
    receiptid,
    installmentid,
    collector_name,
    collector_title,
    orno,
    ordate,
    oramount,
    amount, 
    mode,
    paidby,
    paidbyaddress,
    partial,
    remarks
)
select 
    objid,
    rptcompromiseid as parentid,
    rptreceiptid,
    installmentid,
    collector_name,
    collector_title,
    orno,
    ordate,
    oramount,
    oramount,
    mode,
    paidby,
    paidbyaddress,
    partial,
    remarks
from rptledger_compromise_credit    
go 



insert into rptcompromise_item(
    objid,
    parentid,
    rptledgerfaasid,
    revtype,
    revperiod,
    year,
    amount,
    amtpaid,
    interest,
    interestpaid,
    priority,
    taxdifference
)
select 
    concat(min(rci.objid), '-basic') as objid,
    rci.rptcompromiseid as parentid,
    (select top 1 objid from rptledgerfaas where rptledgerid = rc.rptledgerid and rci.year >= fromyear and (rci.year <= toyear or toyear = 0) and state <> 'cancelled') as rptledgerfaasid,
    'basic' as revtype,
    'prior' as revperiod,
    year,
    sum(rci.basic) as amount,
    sum(rci.basicpaid) as amtpaid,
    sum(rci.basicint) as interest,
    sum(rci.basicintpaid) as interestpaid,
    10000 as priority,
    0 as taxdifference
from rptledger_compromise_item rci 
inner join rptledger_compromise rc on rci.rptcompromiseid = rc.objid 
where rci.basic > 0 
group by rc.rptledgerid, year, rptcompromiseid
go 



insert into rptcompromise_item(
    objid,
    parentid,
    rptledgerfaasid,
    revtype,
    revperiod,
    year,
    amount,
    amtpaid,
    interest,
    interestpaid,
    priority,
    taxdifference
)
select 
    concat(min(rci.objid), '-sef') as objid,
    rci.rptcompromiseid as parentid,
    (select top 1 objid from rptledgerfaas where rptledgerid = rc.rptledgerid and rci.year >= fromyear and (rci.year <= toyear or toyear = 0) and state <> 'cancelled') as rptledgerfaasid,
    'sef' as revtype,
    'prior' as revperiod,
    year,
    sum(rci.sef) as amount,
    sum(rci.sefpaid) as amtpaid,
    sum(rci.sefint) as interest,
    sum(rci.sefintpaid) as interestpaid,
    10000 as priority,
    0 as taxdifference
from rptledger_compromise_item rci 
inner join rptledger_compromise rc on rci.rptcompromiseid = rc.objid 
where rci.sef > 0
group by rc.rptledgerid, year, rptcompromiseid
go 


insert into rptcompromise_item(
    objid,
    parentid,
    rptledgerfaasid,
    revtype,
    revperiod,
    year,
    amount,
    amtpaid,
    interest,
    interestpaid,
    priority,
    taxdifference
)
select 
    concat(min(rci.objid), '-basicidle') as objid,
    rci.rptcompromiseid as parentid,
    (select top 1 objid from rptledgerfaas where rptledgerid = rc.rptledgerid and rci.year >= fromyear and (rci.year <= toyear or toyear = 0) and state <> 'cancelled') as rptledgerfaasid,
    'basicidle' as revtype,
    'prior' as revperiod,
    year,
    sum(rci.basicidle) as amount,
    sum(rci.basicidlepaid) as amtpaid,
    sum(rci.basicidleint) as interest,
    sum(rci.basicidleintpaid) as interestpaid,
    10000 as priority,
    0 as taxdifference
from rptledger_compromise_item rci 
inner join rptledger_compromise rc on rci.rptcompromiseid = rc.objid 
where rci.basicidle > 0
group by rc.rptledgerid, year, rptcompromiseid
go 




insert into rptcompromise_item(
    objid,
    parentid,
    rptledgerfaasid,
    revtype,
    revperiod,
    year,
    amount,
    amtpaid,
    interest,
    interestpaid,
    priority,
    taxdifference
)
select 
    concat(min(rci.objid), '-firecode') as objid,
    rci.rptcompromiseid as parentid,
    (select top 1 objid from rptledgerfaas where rptledgerid = rc.rptledgerid and rci.year >= fromyear and (rci.year <= toyear or toyear = 0) and state <> 'cancelled') as rptledgerfaasid,
    'firecode' as revtype,
    'prior' as revperiod,
    year,
    sum(rci.firecode) as amount,
    sum(rci.firecodepaid) as amtpaid,
    sum(0) as interest,
    sum(0) as interestpaid,
    10000 as priority,
    0 as taxdifference
from rptledger_compromise_item rci 
inner join rptledger_compromise rc on rci.rptcompromiseid = rc.objid 
where rci.basicidle > 0
group by rc.rptledgerid, year, rptcompromiseid
go 






/*====================================================================
*
* LANDTAX RPT DELINQUENCY UPDATE 
*
====================================================================*/


if exists(select * from information_schema.tables where table_name = N'report_rptdelinquency_error')
begin 
  drop table report_rptdelinquency_error
end 

if exists(select * from information_schema.tables where table_name = N'report_rptdelinquency_forprocess')
begin 
  drop table report_rptdelinquency_forprocess
end 

if exists(select * from information_schema.tables where table_name = N'report_rptdelinquency_item')
begin 
  drop table report_rptdelinquency_item
end 

if exists(select * from information_schema.tables where table_name = N'report_rptdelinquency_barangay')
begin 
  drop table report_rptdelinquency_barangay
end 

if exists(select * from information_schema.tables where table_name = N'report_rptdelinquency')
begin 
  drop table report_rptdelinquency
end 
go 



CREATE TABLE report_rptdelinquency (
  objid varchar(50) NOT NULL,
  state varchar(50) NOT NULL,
  dtgenerated datetime NOT NULL,
  dtcomputed datetime NOT NULL,
  generatedby_name varchar(255) NOT NULL,
  generatedby_title varchar(100) NOT NULL,
  PRIMARY KEY (objid)
) 
go 

CREATE TABLE report_rptdelinquency_item (
  objid varchar(50) NOT NULL,
  parentid varchar(50) NOT NULL,
  rptledgerid varchar(50) NOT NULL,
  barangayid varchar(50) NOT NULL,
  year integer NOT NULL,
  qtr integer DEFAULT NULL,
  revtype varchar(50) NOT NULL,
  amount decimal(16,2) NOT NULL,
  interest decimal(16,2) NOT NULL,
  discount decimal(16,2) NOT NULL,
  PRIMARY KEY (objid)
) 
go 

alter table report_rptdelinquency_item 
  add constraint fk_rptdelinquency_item_rptdelinquency foreign key(parentid)
  references report_rptdelinquency(objid)
go 

create index fk_rptdelinquency_item_rptdelinquency on report_rptdelinquency_item(parentid)  
go 


alter table report_rptdelinquency_item 
  add constraint fk_rptdelinquency_item_rptledger foreign key(rptledgerid)
  references rptledger(objid)
go 

create index fk_rptdelinquency_item_rptledger on report_rptdelinquency_item(rptledgerid)  
go 

alter table report_rptdelinquency_item 
  add constraint fk_rptdelinquency_item_barangay foreign key(barangayid)
  references barangay(objid)
go 

create index fk_rptdelinquency_item_barangay on report_rptdelinquency_item(barangayid)  
go 




CREATE TABLE report_rptdelinquency_barangay (
  objid varchar(50) not null, 
  parentid varchar(50) not null, 
  barangayid varchar(50) NOT NULL,
  count int not null,
  processed int not null, 
  errors int not null, 
  ignored int not null, 
  PRIMARY KEY (objid)
) 
go 


alter table report_rptdelinquency_barangay 
  add constraint fk_rptdelinquency_barangay_rptdelinquency foreign key(parentid)
  references report_rptdelinquency(objid)
go 

create index fk_rptdelinquency_barangay_rptdelinquency on report_rptdelinquency_item(parentid)  
go 


alter table report_rptdelinquency_barangay 
  add constraint fk_rptdelinquency_barangay_barangay foreign key(barangayid)
  references barangay(objid)
go 

create index fk_rptdelinquency_barangay_barangay on report_rptdelinquency_barangay(barangayid)  
go 


CREATE TABLE report_rptdelinquency_forprocess (
  objid varchar(50) NOT NULL,
  barangayid varchar(50) NOT NULL,
  PRIMARY KEY (objid)
)
go 

create index ix_barangayid on report_rptdelinquency_forprocess(barangayid)
go 
  


CREATE TABLE report_rptdelinquency_error (
  objid varchar(50) NOT NULL,
  barangayid varchar(50) NOT NULL,
  error text NULL,
  ignored integer NULL,
  PRIMARY KEY (objid)
)
go 

create index ix_barangayid on report_rptdelinquency_error(barangayid)
go
  




drop view vw_landtax_report_rptdelinquency_detail
go 

create view vw_landtax_report_rptdelinquency_detail 
as
select
  parentid, 
  rptledgerid,
  barangayid,
  year,
  qtr,
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
from report_rptdelinquency_item 
go




drop  view vw_landtax_report_rptdelinquency
go 

create view vw_landtax_report_rptdelinquency
as
select
  v.rptledgerid,
  v.barangayid,
  v.year,
  v.qtr,
  rr.dtgenerated,
  rr.generatedby_name,
  rr.generatedby_title,
  sum(v.basic) as basic,
  sum(v.basicint) as basicint,
  sum(v.basicdisc) as basicdisc,
  sum(v.basicdp) as basicdp,
  sum(v.basicnet) as basicnet,
  sum(v.basicidle) as basicidle,
  sum(v.basicidleint) as basicidleint,
  sum(v.basicidledisc) as basicidledisc,
  sum(v.basicidledp) as basicidledp,
  sum(v.basicidlenet) as basicidlenet,
  sum(v.sef) as sef,
  sum(v.sefint) as sefint,
  sum(v.sefdisc) as sefdisc,
  sum(v.sefdp) as sefdp,
  sum(v.sefnet) as sefnet,
  sum(v.firecode) as firecode,
  sum(v.firecodeint) as firecodeint,
  sum(v.firecodedisc) as firecodedisc,
  sum(v.firecodedp) as firecodedp,
  sum(v.firecodenet) as firecodenet,
  sum(v.sh) as sh,
  sum(v.shint) as shint,
  sum(v.shdisc) as shdisc,
  sum(v.shdp) as shdp,
  sum(v.shnet) as shnet,
  sum(v.total) as total
from report_rptdelinquency rr 
inner join vw_landtax_report_rptdelinquency_detail v on rr.objid = v.parentid 
group by 
  v.rptledgerid,
  v.barangayid,
  v.year,
  v.qtr,
  rr.dtgenerated,
  rr.generatedby_name,
  rr.generatedby_title
go


