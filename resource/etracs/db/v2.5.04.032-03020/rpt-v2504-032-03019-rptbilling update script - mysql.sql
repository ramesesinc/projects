
/*============================================================
*
* 254032-03020
*
=============================================================*/


alter table rptledger add lguid varchar(50);


update rptledger rl, barangay b, sys_org m set 
  rl.lguid = m.objid 
where rl.barangayid = b.objid 
and b.parentid = m.objid 
and m.orgclass = 'municipality';


update rptledger rl, barangay b, sys_org d, sys_org c set 
  rl.lguid = c.objid
where rl.barangayid = b.objid 
and b.parentid = d.objid 
and d.parent_objid = c.objid 
and d.orgclass = 'district';


create table `rptpayment` (
  `objid` varchar(100) not null,
  `type` varchar(50) default null,
  `refid` varchar(50) not null,
  `reftype` varchar(50) not null,
  `receiptid` varchar(50) default null,
  `receiptno` varchar(50) not null,
  `receiptdate` date not null,
  `paidby_name` longtext not null,
  `paidby_address` varchar(150) not null,
  `postedby` varchar(100) not null,
  `postedbytitle` varchar(50) not null,
  `dtposted` datetime not null,
  `fromyear` int(11) not null,
  `fromqtr` int(11) not null,
  `toyear` int(11) not null,
  `toqtr` int(11) not null,
  `amount` decimal(12,2) not null,
  `collectingagency` varchar(50) default null,
  `voided` int(11) not null,
  primary key(objid)
) engine=innodb default charset=utf8;

create index `fk_rptpayment_rptledger` on rptpayment(`refid`);
create index `fk_rptpayment_cashreceipt` on rptpayment(`receiptid`);
create index `ix_receiptno` on rptpayment(`receiptno`);

alter table rptpayment 
  add constraint `rptpayment_cashreceipt` 
  foreign key (`receiptid`) references `cashreceipt` (`objid`);


create table `rptpayment_item` (
  `objid` varchar(50) not null,
  `parentid` varchar(100) not null,
  `rptledgerfaasid` varchar(50) default null,
  `year` int(11) not null,
  `qtr` int(11) default null,
  `revtype` varchar(50) not null,
  `revperiod` varchar(25) default null,
  `amount` decimal(16,2) not null,
  `interest` decimal(16,2) not null,
  `discount` decimal(16,2) not null,
  `partialled` int(11) not null,
  primary key (`objid`)
) engine=innodb default charset=utf8;

create index `fk_rptpayment_item_parentid` on rptpayment_item (`parentid`);
create index `fk_rptpayment_item_rptledgerfaasid` on rptpayment_item (`rptledgerfaasid`);

alter table rptpayment_item
  add constraint `rptpayment_item_rptledgerfaas` foreign key (`rptledgerfaasid`) 
  references `rptledgerfaas` (`objid`);

alter table rptpayment_item
  add constraint `rptpayment_item_rptpayment` foreign key (`parentid`) 
  references `rptpayment` (`objid`);




create table `rptpayment_share` (
  `objid` varchar(50) not null,
  `parentid` varchar(100) default null,
  `revperiod` varchar(25) not null,
  `revtype` varchar(25) not null,
  `sharetype` varchar(25) not null,
  `item_objid` varchar(50) not null,
  `amount` decimal(16,4) not null,
  `discount` decimal(16,4) default null,
  primary key (`objid`)
) engine=innodb default charset=utf8;

create index `fk_rptpayment_share_parentid` on rptpayment_share(`parentid`);
create index `fk_rptpayment_share_item_objid` on  rptpayment_share(`item_objid`);

alter table rptpayment_share add constraint `rptpayment_share_itemaccount` 
  foreign key (`item_objid`) references `itemaccount` (`objid`);

alter table rptpayment_share add constraint `rptpayment_share_rptpayment` 
  foreign key (`parentid`) references `rptpayment` (`objid`);



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
from rptledger_payment;


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
from rptledger_payment_item;





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
from rptledger_payment_item;


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
where sh > 0;




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
;



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
;


update cashreceipt_rpt set txntype = 'online' where txntype = 'rptonline'
;
update cashreceipt_rpt set txntype = 'manual' where txntype = 'rptmanual'
;
update cashreceipt_rpt set txntype = 'compromise' where txntype = 'rptcompromise'
;

update rptpayment set type = 'online' where type = 'rptonline'
;
update rptpayment set type = 'manual' where type = 'rptmanual'
;
update rptpayment set type = 'compromise' where type = 'rptcompromise'
;





  
create table landtax_report_rptdelinquency (
  objid varchar(50) not null,
  rptledgerid varchar(50) not null,
  barangayid varchar(50) not null,
  year int not null,
  qtr int null,
  revtype varchar(50) not null,
  amount decimal(16,2) not null,
  interest decimal(16,2) not null,
  discount decimal(16,2) not null,
  dtgenerated datetime not null, 
  generatedby_name varchar(255) not null,
  generatedby_title varchar(100) not null,
  primary key (objid)
)engine=innodb default charset=utf8
;




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
;




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
;


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
;



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
;



create table `rptledger_item` (
  `objid` varchar(50) not null,
  `parentid` varchar(50) not null,
  `rptledgerfaasid` varchar(50) default null,
  `remarks` varchar(100) default null,
  `basicav` decimal(16,2) not null,
  `sefav` decimal(16,2) not null,
  `av` decimal(16,2) not null,
  `revtype` varchar(50) not null,
  `year` int(11) not null,
  `amount` decimal(16,2) not null,
  `amtpaid` decimal(16,2) not null,
  `priority` int(11) not null,
  `taxdifference` int(11) not null,
  `system` int(11) not null,
  primary key (`objid`)
) engine=innodb default charset=utf8
;

create index `fk_rptledger_item_rptledger` on rptledger_item (`parentid`)
; 

alter table rptledger_item 
  add constraint `fk_rptledger_item_rptledger` foreign key (`parentid`) 
  references `rptledger` (`objid`)
;



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
  ifnull(rli.basicav, rli.av),
  ifnull(rli.sefav, rli.av),
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
;




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
  ifnull(rli.basicav, rli.av),
  ifnull(rli.sefav, rli.av),
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
;




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
  ifnull(rli.basicav, rli.av),
  ifnull(rli.sefav, rli.av),
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
;



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
  ifnull(rli.basicav, rli.av),
  ifnull(rli.sefav, rli.av),
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
;


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
  ifnull(rli.basicav, rli.av),
  ifnull(rli.sefav, rli.av),
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
;






/*====================================================================================
*
* RPTLEDGER AND RPTBILLING RULE SUPPORT 
*
======================================================================================*/

INSERT INTO `sys_ruleset` (`name`, `title`, `packagename`, `domain`, `role`, `permission`) 
VALUES ('rptledger', 'Ledger Billing Rules', 'rptledger', 'LANDTAX', 'RULE_AUTHOR', NULL)
;

INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('INIT', 'rptledger', 'Init', '0')
;
INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('LEDGER_ITEM', 'rptledger', 'Ledger Item Posting', '1')
;
INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('TAX', 'rptledger', 'Tax Computation', '2')
;
INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('AFTER_TAX', 'rptledger', 'Post Tax Computation', '3')
;



set @ruleset = 'rptledger' 
;

delete from sys_rule_action_param where parentid in ( 
  select ra.objid 
  from sys_rule r, sys_rule_action ra 
  where r.ruleset=@ruleset and ra.parentid=r.objid 
)
;
delete from sys_rule_action where parentid in ( 
  select objid from sys_rule 
  where ruleset=@ruleset 
)
;
delete from sys_rule_condition_constraint where parentid in ( 
  select rc.objid 
  from sys_rule r, sys_rule_condition rc 
  where r.ruleset=@ruleset and rc.parentid=r.objid 
)
;
delete from sys_rule_condition_var where parentid in ( 
  select rc.objid 
  from sys_rule r, sys_rule_condition rc 
  where r.ruleset=@ruleset and rc.parentid=r.objid 
)
;
delete from sys_rule_condition where parentid in ( 
  select objid from sys_rule where ruleset=@ruleset 
)
;
delete from sys_rule_deployed where objid in ( 
  select objid from sys_rule where ruleset=@ruleset 
)
;
delete from sys_rule where ruleset=@ruleset 
;



set @ruleset = 'rptbilling' 
;

delete from sys_rule_action_param where parentid in ( 
  select ra.objid 
  from sys_rule r, sys_rule_action ra 
  where r.ruleset=@ruleset and ra.parentid=r.objid 
)
;
delete from sys_rule_action where parentid in ( 
  select objid from sys_rule 
  where ruleset=@ruleset 
)
;
delete from sys_rule_condition_constraint where parentid in ( 
  select rc.objid 
  from sys_rule r, sys_rule_condition rc 
  where r.ruleset=@ruleset and rc.parentid=r.objid 
)
;
delete from sys_rule_condition_var where parentid in ( 
  select rc.objid 
  from sys_rule r, sys_rule_condition rc 
  where r.ruleset=@ruleset and rc.parentid=r.objid 
)
;
delete from sys_rule_condition where parentid in ( 
  select objid from sys_rule where ruleset=@ruleset 
)
;
delete from sys_rule_deployed where objid in ( 
  select objid from sys_rule where ruleset=@ruleset 
)
;
delete from sys_rule where ruleset=@ruleset 
;





/* REMOVE rptledger and rptbilling RULE DEFS */
set @ruleset='rptledger'
;
--
-- BEGIN 
-- 
delete from sys_rule_actiondef_param where parentid in ( 
  select ra.objid from sys_ruleset_actiondef rsa 
    inner join sys_rule_actiondef ra on ra.objid=rsa.actiondef 
  where rsa.ruleset=@ruleset
);
delete from sys_rule_actiondef where objid in ( 
  select actiondef from sys_ruleset_actiondef where ruleset=@ruleset 
);

delete from sys_rule_fact_field where parentid in ( 
  select rf.objid from sys_ruleset_fact rsf 
    inner join sys_rule_fact rf on rf.objid=rsf.rulefact 
  where rsf.ruleset=@ruleset 
);
delete from sys_rule_fact where objid in ( 
  select rulefact from sys_ruleset_fact where ruleset=@ruleset 
);

delete from sys_ruleset_fact where ruleset=@ruleset;
delete from sys_ruleset_actiondef where ruleset=@ruleset;
delete from sys_rulegroup where ruleset=@ruleset;
delete from sys_ruleset where name=@ruleset;
--
-- END 
-- 


set @ruleset='rptbilling'
;
--
-- BEGIN 
-- 
delete from sys_rule_actiondef_param where parentid in ( 
  select ra.objid from sys_ruleset_actiondef rsa 
    inner join sys_rule_actiondef ra on ra.objid=rsa.actiondef 
  where rsa.ruleset=@ruleset
);
delete from sys_rule_actiondef where objid in ( 
  select actiondef from sys_ruleset_actiondef where ruleset=@ruleset 
);

delete from sys_rule_fact_field where parentid in ( 
  select rf.objid from sys_ruleset_fact rsf 
    inner join sys_rule_fact rf on rf.objid=rsf.rulefact 
  where rsf.ruleset=@ruleset 
);
delete from sys_rule_fact where objid in ( 
  select rulefact from sys_ruleset_fact where ruleset=@ruleset 
);

delete from sys_ruleset_fact where ruleset=@ruleset;
delete from sys_ruleset_actiondef where ruleset=@ruleset;
delete from sys_rulegroup where ruleset=@ruleset;
delete from sys_ruleset where name=@ruleset;
--
-- END 
-- 



INSERT INTO `sys_ruleset` (`name`, `title`, `packagename`, `domain`, `role`, `permission`) VALUES ('rptbilling', 'RPT Billing Rules', 'rptbilling', 'LANDTAX', 'RULE_AUTHOR', NULL);
INSERT INTO `sys_ruleset` (`name`, `title`, `packagename`, `domain`, `role`, `permission`) VALUES ('rptledger', 'Ledger Billing Rules', 'rptledger', 'LANDTAX', 'RULE_AUTHOR', NULL);


INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('LEDGER_ITEM', 'rptledger', 'Ledger Item Posting', '1');
INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('TAX', 'rptledger', 'Tax Computation', '2');
INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('AFTER_TAX', 'rptledger', 'Post Tax Computation', '3');


INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('INIT', 'rptbilling', 'Init', '0');
INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('DISCOUNT', 'rptbilling', 'Discount Computation', '9');
INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('AFTER_DISCOUNT', 'rptbilling', 'After Discount Computation', '10');
INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('PENALTY', 'rptbilling', 'Penalty Computation', '7');
INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('AFTER_PENALTY', 'rptbilling', 'After Penalty Computation', '8');
INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('BEFORE_SUMMARY', 'rptbilling', 'Before Summary ', '19');
INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('SUMMARY', 'rptbilling', 'Summary', '20');
INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('AFTER_SUMMARY', 'rptbilling', 'After Summary', '21');
INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('BRGY_SHARE', 'rptbilling', 'Barangay Share Computation', '25');
INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('PROV_SHARE', 'rptbilling', 'Province Share Computation', '27');
INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('LGU_SHARE', 'rptbilling', 'LGU Share Computation', '26');




/*========================================================================
*
* UPDATE ON LANDTAX ACCOUNT MAPPING
*
*=========================================================================*/

drop procedure if exists buildRptAccounts;

create procedure buildRptAccounts()
BEGIN
  select orgclass into @orgclass  from sys_org where root = 1;



    /*REVENUE PARENT ACCOUNTS  */

    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASIC_ADVANCE', 'APPROVED', '455-049', 'RPT BASIC ADVANCE', 'RPT BASIC ADVANCE', 'REVENUE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASIC_CURRENT', 'APPROVED', '455-049', 'RPT BASIC CURRENT', 'RPT BASIC CURRENT', 'REVENUE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICINT_CURRENT', 'APPROVED', '455-049', 'RPT BASIC PENALTY CURRENT', 'RPT BASIC PENALTY CURRENT', 'REVENUE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASIC_PREVIOUS', 'APPROVED', '455-049', 'RPT BASIC PREVIOUS', 'RPT BASIC PREVIOUS', 'REVENUE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICINT_PREVIOUS', 'APPROVED', '455-049', 'RPT BASIC PENALTY PREVIOUS', 'RPT BASIC PENALTY PREVIOUS', 'REVENUE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASIC_PRIOR', 'APPROVED', '455-049', 'RPT BASIC PRIOR', 'RPT BASIC PRIOR', 'REVENUE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICINT_PRIOR', 'APPROVED', '455-049', 'RPT BASIC PENALTY PRIOR', 'RPT BASIC PENALTY PRIOR', 'REVENUE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);

    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEF_ADVANCE', 'APPROVED', '455-050', 'RPT SEF ADVANCE', 'RPT SEF ADVANCE', 'REVENUE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEF_CURRENT', 'APPROVED', '455-050', 'RPT SEF CURRENT', 'RPT SEF CURRENT', 'REVENUE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEFINT_CURRENT', 'APPROVED', '455-050', 'RPT SEF PENALTY CURRENT', 'RPT SEF PENALTY CURRENT', 'REVENUE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEF_PREVIOUS', 'APPROVED', '455-050', 'RPT SEF PREVIOUS', 'RPT SEF PREVIOUS', 'REVENUE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEFINT_PREVIOUS', 'APPROVED', '455-050', 'RPT SEF PENALTY PREVIOUS', 'RPT SEF PENALTY PREVIOUS', 'REVENUE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEF_PRIOR', 'APPROVED', '455-050', 'RPT SEF PRIOR', 'RPT SEF PRIOR', 'REVENUE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEFINT_PRIOR', 'APPROVED', '455-050', 'RPT SEF PENALTY PRIOR', 'RPT SEF PENALTY PRIOR', 'REVENUE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);

    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_BASICIDLE_ADVANCE', 'APPROVED', '455-050', 'RPT BASICIDLE ADVANCE', 'RPT BASICIDLE ADVANCE', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_BASICIDLE_CURRENT', 'APPROVED', '455-050', 'RPT BASICIDLE CURRENT', 'RPT BASICIDLE CURRENT', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_BASICIDLEINT_CURRENT', 'APPROVED', '455-050', 'RPT BASICIDLE PENALTY CURRENT', 'RPT BASICIDLE PENALTY CURRENT', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_BASICIDLE_PREVIOUS', 'APPROVED', '455-050', 'RPT BASICIDLE PREVIOUS', 'RPT BASICIDLE PREVIOUS', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_BASICIDLEINT_PREVIOUS', 'APPROVED', '455-050', 'RPT BASICIDLE PENALTY PREVIOUS', 'RPT BASICIDLE PENALTY PREVIOUS', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_BASICIDLE_PRIOR', 'APPROVED', '455-050', 'RPT BASICIDLE PRIOR', 'RPT BASICIDLE PRIOR', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_BASICIDLEINT_PRIOR', 'APPROVED', '455-050', 'RPT BASICIDLE PENALTY PRIOR', 'RPT BASICIDLE PENALTY PRIOR', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);

    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_SH_ADVANCE', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING ADVANCE', 'RPT SOCIAL HOUSING ADVANCE', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_SH_CURRENT', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING CURRENT', 'RPT SOCIAL HOUSING CURRENT', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_SHINT_CURRENT', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING PENALTY CURRENT', 'RPT SOCIAL HOUSING PENALTY CURRENT', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_SH_PREVIOUS', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING PREVIOUS', 'RPT SOCIAL HOUSING PREVIOUS', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_SHINT_PREVIOUS', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING PENALTY PREVIOUS', 'RPT SOCIAL HOUSING PENALTY PREVIOUS', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_SH_PRIOR', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING PRIOR', 'RPT SOCIAL HOUSING PRIOR', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_SHINT_PRIOR', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING PENALTY PRIOR', 'RPT SOCIAL HOUSING PENALTY PRIOR', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);

    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_FIRECODE_ADVANCE', 'APPROVED', '455-050', 'RPT FIRECODE ADVANCE', 'RPT FIRECODE ADVANCE', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_FIRECODE_CURRENT', 'APPROVED', '455-050', 'RPT FIRECODE CURRENT', 'RPT FIRECODE CURRENT', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_FIRECODEINT_CURRENT', 'APPROVED', '455-050', 'RPT FIRECODE PENALTY CURRENT', 'RPT FIRECODE PENALTY CURRENT', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_FIRECODE_PREVIOUS', 'APPROVED', '455-050', 'RPT FIRECODE PREVIOUS', 'RPT FIRECODE PREVIOUS', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_FIRECODEINT_PREVIOUS', 'APPROVED', '455-050', 'RPT FIRECODE PENALTY PREVIOUS', 'RPT FIRECODE PENALTY PREVIOUS', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_FIRECODE_PRIOR', 'APPROVED', '455-050', 'RPT FIRECODE PRIOR', 'RPT FIRECODE PRIOR', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) 
    VALUES ('RPT_FIRECODEINT_PRIOR', 'APPROVED', '455-050', 'RPT FIRECODE PENALTY PRIOR', 'RPT FIRECODE PENALTY PRIOR', 'REVENUE', 'GENERAL', '02', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);

    insert into itemaccount_tag (objid, acctid, tag)
    select  'RPT_BASIC_ADVANCE' as objid, 'RPT_BASIC_ADVANCE' as acctid, 'rpt_basic_advance' as tag
    union 
    select  'RPT_BASIC_CURRENT' as objid, 'RPT_BASIC_CURRENT' as acctid, 'rpt_basic_current' as tag
    union 
    select  'RPT_BASICINT_CURRENT' as objid, 'RPT_BASICINT_CURRENT' as acctid, 'rpt_basicint_current' as tag
    union 
    select  'RPT_BASIC_PREVIOUS' as objid, 'RPT_BASIC_PREVIOUS' as acctid, 'rpt_basic_previous' as tag
    union 
    select  'RPT_BASICINT_PREVIOUS' as objid, 'RPT_BASICINT_PREVIOUS' as acctid, 'rpt_basicint_previous' as tag
    union 
    select  'RPT_BASIC_PRIOR' as objid, 'RPT_BASIC_PRIOR' as acctid, 'rpt_basic_prior' as tag
    union 
    select  'RPT_BASICINT_PRIOR' as objid, 'RPT_BASICINT_PRIOR' as acctid, 'rpt_basicint_prior' as tag
    union 
    select  'RPT_SEF_ADVANCE' as objid, 'RPT_SEF_ADVANCE' as acctid, 'rpt_sef_advance' as tag
    union 
    select  'RPT_SEF_CURRENT' as objid, 'RPT_SEF_CURRENT' as acctid, 'rpt_sef_current' as tag
    union 
    select  'RPT_SEFINT_CURRENT' as objid, 'RPT_SEFINT_CURRENT' as acctid, 'rpt_sefint_current' as tag
    union 
    select  'RPT_SEF_PREVIOUS' as objid, 'RPT_SEF_PREVIOUS' as acctid, 'rpt_sef_previous' as tag
    union 
    select  'RPT_SEFINT_PREVIOUS' as objid, 'RPT_SEFINT_PREVIOUS' as acctid, 'rpt_sefint_previous' as tag
    union 
    select  'RPT_SEF_PRIOR' as objid, 'RPT_SEF_PRIOR' as acctid, 'rpt_sef_prior' as tag
    union 
    select  'RPT_SEFINT_PRIOR' as objid, 'RPT_SEFINT_PRIOR' as acctid, 'rpt_sefint_prior' as tag
    union
    select  'RPT_BASICIDLE_ADVANCE' as objid, 'RPT_BASICIDLE_ADVANCE' as acctid, 'rpt_basicidle_advance' as tag
    union 
    select  'RPT_BASICIDLE_CURRENT' as objid, 'RPT_BASICIDLE_CURRENT' as acctid, 'rpt_basicidle_current' as tag
    union 
    select  'RPT_BASICIDLEINT_CURRENT' as objid, 'RPT_BASICIDLEINT_CURRENT' as acctid, 'rpt_basicidleint_current' as tag
    union 
    select  'RPT_BASICIDLE_PREVIOUS' as objid, 'RPT_BASICIDLE_PREVIOUS' as acctid, 'rpt_basicidle_previous' as tag
    union 
    select  'RPT_BASICIDLEINT_PREVIOUS' as objid, 'RPT_BASICIDLEINT_PREVIOUS' as acctid, 'rpt_basicidleint_previous' as tag
    union 
    select  'RPT_BASICIDLE_PRIOR' as objid, 'RPT_BASICIDLE_PRIOR' as acctid, 'rpt_basicidle_prior' as tag
    union 
    select  'RPT_BASICIDLEINT_PRIOR' as objid, 'RPT_BASICIDLEINT_PRIOR' as acctid, 'rpt_basicidleint_prior' as tag
    union
    select  'RPT_SH_ADVANCE' as objid, 'RPT_SH_ADVANCE' as acctid, 'rpt_sh_advance' as tag
    union 
    select  'RPT_SH_CURRENT' as objid, 'RPT_SH_CURRENT' as acctid, 'rpt_sh_current' as tag
    union 
    select  'RPT_SHINT_CURRENT' as objid, 'RPT_SHINT_CURRENT' as acctid, 'rpt_shint_current' as tag
    union 
    select  'RPT_SH_PREVIOUS' as objid, 'RPT_SH_PREVIOUS' as acctid, 'rpt_sh_previous' as tag
    union 
    select  'RPT_SHINT_PREVIOUS' as objid, 'RPT_SHINT_PREVIOUS' as acctid, 'rpt_shint_previous' as tag
    union 
    select  'RPT_SH_PRIOR' as objid, 'RPT_SH_PRIOR' as acctid, 'rpt_sh_prior' as tag
    union 
    select  'RPT_SHINT_PRIOR' as objid, 'RPT_SHINT_PRIOR' as acctid, 'rpt_shint_prior' as tag
    union
    select  'RPT_FIRECODE_ADVANCE' as objid, 'RPT_FIRECODE_ADVANCE' as acctid, 'rpt_firecode_advance' as tag
    union 
    select  'RPT_FIRECODE_CURRENT' as objid, 'RPT_FIRECODE_CURRENT' as acctid, 'rpt_firecode_current' as tag
    union 
    select  'RPT_FIRECODEINT_CURRENT' as objid, 'RPT_FIRECODEINT_CURRENT' as acctid, 'rpt_firecodeint_current' as tag
    union 
    select  'RPT_FIRECODE_PREVIOUS' as objid, 'RPT_FIRECODE_PREVIOUS' as acctid, 'rpt_firecode_previous' as tag
    union 
    select  'RPT_FIRECODEINT_PREVIOUS' as objid, 'RPT_FIRECODEINT_PREVIOUS' as acctid, 'rpt_firecodeint_previous' as tag
    union 
    select  'RPT_FIRECODE_PRIOR' as objid, 'RPT_FIRECODE_PRIOR' as acctid, 'rpt_firecode_prior' as tag
    union 
    select  'RPT_FIRECODEINT_PRIOR' as objid, 'RPT_FIRECODEINT_PRIOR' as acctid, 'rpt_firecodeint_prior' as tag    
    ;


  if @orgclass = 'province' then 
    /* MUNICIPALITY SHARE PAYABLE */

    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASIC_ADVANCE_MUNICIPALITY_SHARE', 'APPROVED', '455-049', 'RPT BASIC ADVANCE MUNICIPALITY SHARE', 'RPT BASIC ADVANCE MUNICIPALITY SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASIC_CURRENT_MUNICIPALITY_SHARE', 'APPROVED', '455-049', 'RPT BASIC CURRENT MUNICIPALITY SHARE', 'RPT BASIC CURRENT MUNICIPALITY SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICINT_CURRENT_MUNICIPALITY_SHARE', 'APPROVED', '455-049', 'RPT BASIC CURRENT PENALTY MUNICIPALITY SHARE', 'RPT BASIC CURRENT PENALTY MUNICIPALITY SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASIC_PREVIOUS_MUNICIPALITY_SHARE', 'APPROVED', '455-049', 'RPT BASIC PREVIOUS MUNICIPALITY SHARE', 'RPT BASIC PREVIOUS MUNICIPALITY SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICINT_PREVIOUS_MUNICIPALITY_SHARE', 'APPROVED', '455-049', 'RPT BASIC PREVIOUS PENALTY MUNICIPALITY SHARE', 'RPT BASIC PREVIOUS PENALTY MUNICIPALITY SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASIC_PRIOR_MUNICIPALITY_SHARE', 'APPROVED', '455-049', 'RPT BASIC PRIOR MUNICIPALITY SHARE', 'RPT BASIC PRIOR MUNICIPALITY SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICINT_PRIOR_MUNICIPALITY_SHARE', 'APPROVED', '455-049', 'RPT BASIC PRIOR PENALTY MUNICIPALITY SHARE', 'RPT BASIC PRIOR PENALTY MUNICIPALITY SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);

    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEF_ADVANCE_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT SEF ADVANCE MUNICIPALITY SHARE', 'RPT SEF ADVANCE MUNICIPALITY SHARE', 'PAYABLE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEF_CURRENT_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT SEF CURRENT MUNICIPALITY SHARE', 'RPT SEF CURRENT MUNICIPALITY SHARE', 'PAYABLE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEFINT_CURRENT_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT SEF CURRENT PENALTY MUNICIPALITY SHARE', 'RPT SEF CURRENT PENALTY MUNICIPALITY SHARE', 'PAYABLE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEF_PREVIOUS_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT SEF PREVIOUS MUNICIPALITY SHARE', 'RPT SEF PREVIOUS MUNICIPALITY SHARE', 'PAYABLE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEFINT_PREVIOUS_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT SEF PREVIOUS PENALTY MUNICIPALITY SHARE', 'RPT SEF PREVIOUS PENALTY MUNICIPALITY SHARE', 'PAYABLE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEF_PRIOR_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT SEF PRIOR MUNICIPALITY SHARE', 'RPT SEF PRIOR MUNICIPALITY SHARE', 'PAYABLE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEFINT_PRIOR_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT SEF PRIOR PENALTY MUNICIPALITY SHARE', 'RPT SEF PRIOR PENALTY MUNICIPALITY SHARE', 'PAYABLE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);

    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICIDLE_ADVANCE_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT BASICIDLE ADVANCE MUNICIPALITY SHARE', 'RPT BASICIDLE ADVANCE MUNICIPALITY SHARE', 'PAYABLE', 'BASICIDLE', '02', 'BASICIDLE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICIDLE_CURRENT_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT BASICIDLE CURRENT MUNICIPALITY SHARE', 'RPT BASICIDLE CURRENT MUNICIPALITY SHARE', 'PAYABLE', 'BASICIDLE', '02', 'BASICIDLE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICIDLEINT_CURRENT_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT BASICIDLE CURRENT PENALTY MUNICIPALITY SHARE', 'RPT BASICIDLE CURRENT PENALTY MUNICIPALITY SHARE', 'PAYABLE', 'BASICIDLE', '02', 'BASICIDLE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICIDLE_PREVIOUS_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT BASICIDLE PREVIOUS MUNICIPALITY SHARE', 'RPT BASICIDLE PREVIOUS MUNICIPALITY SHARE', 'PAYABLE', 'BASICIDLE', '02', 'BASICIDLE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICIDLEINT_PREVIOUS_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT BASICIDLE PREVIOUS PENALTY MUNICIPALITY SHARE', 'RPT BASICIDLE PREVIOUS PENALTY MUNICIPALITY SHARE', 'PAYABLE', 'BASICIDLE', '02', 'BASICIDLE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICIDLE_PRIOR_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT BASICIDLE PRIOR MUNICIPALITY SHARE', 'RPT BASICIDLE PRIOR MUNICIPALITY SHARE', 'PAYABLE', 'BASICIDLE', '02', 'BASICIDLE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICIDLEINT_PRIOR_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT BASICIDLE PRIOR PENALTY MUNICIPALITY SHARE', 'RPT BASICIDLE PRIOR PENALTY MUNICIPALITY SHARE', 'PAYABLE', 'BASICIDLE', '02', 'BASICIDLE', '0.00', 'ANY', NULL, NULL, NULL);

    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_FIRECODE_ADVANCE_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT FIRECODE ADVANCE MUNICIPALITY SHARE', 'RPT FIRECODE ADVANCE MUNICIPALITY SHARE', 'PAYABLE', 'FIRECODE', '02', 'FIRECODE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_FIRECODE_CURRENT_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT FIRECODE CURRENT MUNICIPALITY SHARE', 'RPT FIRECODE CURRENT MUNICIPALITY SHARE', 'PAYABLE', 'FIRECODE', '02', 'FIRECODE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_FIRECODEINT_CURRENT_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT FIRECODE CURRENT PENALTY MUNICIPALITY SHARE', 'RPT FIRECODE CURRENT PENALTY MUNICIPALITY SHARE', 'PAYABLE', 'FIRECODE', '02', 'FIRECODE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_FIRECODE_PREVIOUS_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT FIRECODE PREVIOUS MUNICIPALITY SHARE', 'RPT FIRECODE PREVIOUS MUNICIPALITY SHARE', 'PAYABLE', 'FIRECODE', '02', 'FIRECODE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_FIRECODEINT_PREVIOUS_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT FIRECODE PREVIOUS PENALTY MUNICIPALITY SHARE', 'RPT FIRECODE PREVIOUS PENALTY MUNICIPALITY SHARE', 'PAYABLE', 'FIRECODE', '02', 'FIRECODE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_FIRECODE_PRIOR_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT FIRECODE PRIOR MUNICIPALITY SHARE', 'RPT FIRECODE PRIOR MUNICIPALITY SHARE', 'PAYABLE', 'FIRECODE', '02', 'FIRECODE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_FIRECODEINT_PRIOR_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT FIRECODE PRIOR PENALTY MUNICIPALITY SHARE', 'RPT FIRECODE PRIOR PENALTY MUNICIPALITY SHARE', 'PAYABLE', 'FIRECODE', '02', 'FIRECODE', '0.00', 'ANY', NULL, NULL, NULL);

    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SH_ADVANCE_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING ADVANCE MUNICIPALITY SHARE', 'RPT SOCIAL HOUSING ADVANCE MUNICIPALITY SHARE', 'PAYABLE', 'SOCIAL HOUSING', '02', 'SOCIAL HOUSING', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SH_CURRENT_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING CURRENT MUNICIPALITY SHARE', 'RPT SOCIAL HOUSING CURRENT MUNICIPALITY SHARE', 'PAYABLE', 'SOCIAL HOUSING', '02', 'SOCIAL HOUSING', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SHINT_CURRENT_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING CURRENT PENALTY MUNICIPALITY SHARE', 'RPT SOCIAL HOUSING CURRENT PENALTY MUNICIPALITY SHARE', 'PAYABLE', 'SOCIAL HOUSING', '02', 'SOCIAL HOUSING', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SH_PREVIOUS_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING PREVIOUS MUNICIPALITY SHARE', 'RPT SOCIAL HOUSING PREVIOUS MUNICIPALITY SHARE', 'PAYABLE', 'SOCIAL HOUSING', '02', 'SOCIAL HOUSING', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SHINT_PREVIOUS_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING PREVIOUS PENALTY MUNICIPALITY SHARE', 'RPT SOCIAL HOUSING PREVIOUS PENALTY MUNICIPALITY SHARE', 'PAYABLE', 'SOCIAL HOUSING', '02', 'SOCIAL HOUSING', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SH_PRIOR_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING PRIOR MUNICIPALITY SHARE', 'RPT SOCIAL HOUSING PRIOR MUNICIPALITY SHARE', 'PAYABLE', 'SOCIAL HOUSING', '02', 'SOCIAL HOUSING', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SHINT_PRIOR_MUNICIPALITY_SHARE', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING PRIOR PENALTY MUNICIPALITY SHARE', 'RPT SOCIAL HOUSING PRIOR PENALTY MUNICIPALITY SHARE', 'PAYABLE', 'SOCIAL HOUSING', '02', 'SOCIAL HOUSING', '0.00', 'ANY', NULL, NULL, NULL);



    update itemaccount ia, landtax_lgu_account_mapping m, municipality b set 
      ia.parentid = case 
        when m.revtype = 'basic' and revperiod = 'advance' then 'RPT_BASIC_ADVANCE_MUNICIPALITY_SHARE'
        when m.revtype = 'basic' and revperiod = 'current' then 'RPT_BASIC_CURRENT_MUNICIPALITY_SHARE'
        when m.revtype = 'basicint' and revperiod = 'current' then 'RPT_BASICINT_CURRENT_MUNICIPALITY_SHARE'
        when m.revtype = 'basic' and revperiod = 'previous' then 'RPT_BASIC_PREVIOUS_MUNICIPALITY_SHARE'
        when m.revtype = 'basicint' and revperiod = 'previous' then 'RPT_BASICINT_PREVIOUS_MUNICIPALITY_SHARE'
        when m.revtype = 'basic' and revperiod = 'prior' then 'RPT_BASIC_PRIOR_MUNICIPALITY_SHARE'
        when m.revtype = 'basicint' and revperiod = 'prior' then 'RPT_BASICINT_PRIOR_MUNICIPALITY_SHARE'

        when m.revtype = 'sef' and revperiod = 'advance' then 'RPT_SEF_ADVANCE_MUNICIPALITY_SHARE'
        when m.revtype = 'sef' and revperiod = 'current' then 'RPT_SEF_CURRENT_MUNICIPALITY_SHARE'
        when m.revtype = 'sefint' and revperiod = 'current' then 'RPT_SEFINT_CURRENT_MUNICIPALITY_SHARE'
        when m.revtype = 'sef' and revperiod = 'previous' then 'RPT_SEF_PREVIOUS_MUNICIPALITY_SHARE'
        when m.revtype = 'sefint' and revperiod = 'previous' then 'RPT_SEFINT_PREVIOUS_MUNICIPALITY_SHARE'
        when m.revtype = 'sef' and revperiod = 'prior' then 'RPT_SEF_PRIOR_MUNICIPALITY_SHARE'
        when m.revtype = 'sefint' and revperiod = 'prior' then 'RPT_SEFINT_PRIOR_MUNICIPALITY_SHARE'

        when m.revtype = 'basicidle' and revperiod = 'advance' then 'RPT_BASICIDLE_ADVANCE_MUNICIPALITY_SHARE'
        when m.revtype = 'basicidle' and revperiod = 'current' then 'RPT_BASICIDLE_CURRENT_MUNICIPALITY_SHARE'
        when m.revtype = 'basicidleint' and revperiod = 'current' then 'RPT_BASICIDLEINT_CURRENT_MUNICIPALITY_SHARE'
        when m.revtype = 'basicidle' and revperiod = 'previous' then 'RPT_BASICIDLE_PREVIOUS_MUNICIPALITY_SHARE'
        when m.revtype = 'basicidleint' and revperiod = 'previous' then 'RPT_BASICIDLEINT_PREVIOUS_MUNICIPALITY_SHARE'
        when m.revtype = 'basicidle' and revperiod = 'prior' then 'RPT_BASICIDLE_PRIOR_MUNICIPALITY_SHARE'
        when m.revtype = 'basicidleint' and revperiod = 'prior' then 'RPT_BASICIDLEINT_PRIOR_MUNICIPALITY_SHARE'

        when m.revtype = 'sh' and revperiod = 'advance' then 'RPT_SH_ADVANCE_MUNICIPALITY_SHARE'
        when m.revtype = 'sh' and revperiod = 'current' then 'RPT_SH_CURRENT_MUNICIPALITY_SHARE'
        when m.revtype = 'shint' and revperiod = 'current' then 'RPT_SHINT_CURRENT_MUNICIPALITY_SHARE'
        when m.revtype = 'sh' and revperiod = 'previous' then 'RPT_SH_PREVIOUS_MUNICIPALITY_SHARE'
        when m.revtype = 'shint' and revperiod = 'previous' then 'RPT_SHINT_PREVIOUS_MUNICIPALITY_SHARE'
        when m.revtype = 'sh' and revperiod = 'prior' then 'RPT_SH_PRIOR_MUNICIPALITY_SHARE'
        when m.revtype = 'shint' and revperiod = 'prior' then 'RPT_SHINT_PRIOR_MUNICIPALITY_SHARE'

        when m.revtype = 'firecode' and revperiod = 'advance' then 'RPT_FIRECODE_ADVANCE_MUNICIPALITY_SHARE'
        when m.revtype = 'firecode' and revperiod = 'current' then 'RPT_FIRECODE_CURRENT_MUNICIPALITY_SHARE'
        when m.revtype = 'firecodeint' and revperiod = 'current' then 'RPT_FIRECODEINT_CURRENT_MUNICIPALITY_SHARE'
        when m.revtype = 'firecode' and revperiod = 'previous' then 'RPT_FIRECODE_PREVIOUS_MUNICIPALITY_SHARE'
        when m.revtype = 'firecodeint' and revperiod = 'previous' then 'RPT_FIRECODEINT_PREVIOUS_MUNICIPALITY_SHARE'
        when m.revtype = 'firecode' and revperiod = 'prior' then 'RPT_FIRECODE_PRIOR_MUNICIPALITY_SHARE'
        when m.revtype = 'firecodeint' and revperiod = 'prior' then 'RPT_FIRECODEINT_PRIOR_MUNICIPALITY_SHARE'
        end,
      ia.org_objid = b.objid, 
      ia.org_name = b.name,
      ia.type = 'PAYABLE'
    where ia.objid = m.item_objid
    and m.lgu_objid = b.objid
    ;


  elseif @orgclass = 'municipality' then 


    /* PROVINCE SHARE PAYABLE */

    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASIC_ADVANCE_PROVINCE_SHARE', 'APPROVED', '455-049', 'RPT BASIC ADVANCE PROVINCE SHARE', 'RPT BASIC ADVANCE PROVINCE SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASIC_CURRENT_PROVINCE_SHARE', 'APPROVED', '455-049', 'RPT BASIC CURRENT PROVINCE SHARE', 'RPT BASIC CURRENT PROVINCE SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICINT_CURRENT_PROVINCE_SHARE', 'APPROVED', '455-049', 'RPT BASIC PENALTY CURRENT PROVINCE SHARE', 'RPT BASIC PENALTY CURRENT PROVINCE SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASIC_PREVIOUS_PROVINCE_SHARE', 'APPROVED', '455-049', 'RPT BASIC PREVIOUS PROVINCE SHARE', 'RPT BASIC PREVIOUS PROVINCE SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICINT_PREVIOUS_PROVINCE_SHARE', 'APPROVED', '455-049', 'RPT BASIC PENALTY PREVIOUS PROVINCE SHARE', 'RPT BASIC PENALTY PREVIOUS PROVINCE SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASIC_PRIOR_PROVINCE_SHARE', 'APPROVED', '455-049', 'RPT BASIC PRIOR PROVINCE SHARE', 'RPT BASIC PRIOR PROVINCE SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICINT_PRIOR_PROVINCE_SHARE', 'APPROVED', '455-049', 'RPT BASIC PENALTY PRIOR PROVINCE SHARE', 'RPT BASIC PENALTY PRIOR PROVINCE SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);

    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEF_ADVANCE_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT SEF ADVANCE PROVINCE SHARE', 'RPT SEF ADVANCE PROVINCE SHARE', 'PAYABLE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEF_CURRENT_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT SEF CURRENT PROVINCE SHARE', 'RPT SEF CURRENT PROVINCE SHARE', 'PAYABLE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEFINT_CURRENT_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT SEF PENALTY CURRENT PROVINCE SHARE', 'RPT SEF PENALTY CURRENT PROVINCE SHARE', 'PAYABLE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEF_PREVIOUS_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT SEF PREVIOUS PROVINCE SHARE', 'RPT SEF PREVIOUS PROVINCE SHARE', 'PAYABLE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEFINT_PREVIOUS_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT SEF PENALTY PREVIOUS PROVINCE SHARE', 'RPT SEF PENALTY PREVIOUS PROVINCE SHARE', 'PAYABLE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEF_PRIOR_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT SEF PRIOR PROVINCE SHARE', 'RPT SEF PRIOR PROVINCE SHARE', 'PAYABLE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SEFINT_PRIOR_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT SEF PENALTY PRIOR PROVINCE SHARE', 'RPT SEF PENALTY PRIOR PROVINCE SHARE', 'PAYABLE', 'SEF', '02', 'SEF', '0.00', 'ANY', NULL, NULL, NULL);

    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICIDLE_ADVANCE_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT BASICIDLE ADVANCE PROVINCE SHARE', 'RPT BASICIDLE ADVANCE PROVINCE SHARE', 'PAYABLE', 'BASICIDLE', '02', 'BASICIDLE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICIDLE_CURRENT_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT BASICIDLE CURRENT PROVINCE SHARE', 'RPT BASICIDLE CURRENT PROVINCE SHARE', 'PAYABLE', 'BASICIDLE', '02', 'BASICIDLE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICIDLEINT_CURRENT_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT BASICIDLE PENALTY CURRENT PROVINCE SHARE', 'RPT BASICIDLE PENALTY CURRENT PROVINCE SHARE', 'PAYABLE', 'BASICIDLE', '02', 'BASICIDLE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICIDLE_PREVIOUS_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT BASICIDLE PREVIOUS PROVINCE SHARE', 'RPT BASICIDLE PREVIOUS PROVINCE SHARE', 'PAYABLE', 'BASICIDLE', '02', 'BASICIDLE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICIDLEINT_PREVIOUS_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT BASICIDLE PENALTY PREVIOUS PROVINCE SHARE', 'RPT BASICIDLE PENALTY PREVIOUS PROVINCE SHARE', 'PAYABLE', 'BASICIDLE', '02', 'BASICIDLE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICIDLE_PRIOR_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT BASICIDLE PRIOR PROVINCE SHARE', 'RPT BASICIDLE PRIOR PROVINCE SHARE', 'PAYABLE', 'BASICIDLE', '02', 'BASICIDLE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICIDLEINT_PRIOR_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT BASICIDLE PENALTY PRIOR PROVINCE SHARE', 'RPT BASICIDLE PENALTY PRIOR PROVINCE SHARE', 'PAYABLE', 'BASICIDLE', '02', 'BASICIDLE', '0.00', 'ANY', NULL, NULL, NULL);

    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_FIRECODE_ADVANCE_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT FIRECODE ADVANCE PROVINCE SHARE', 'RPT FIRECODE ADVANCE PROVINCE SHARE', 'PAYABLE', 'FIRECODE', '02', 'FIRECODE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_FIRECODE_CURRENT_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT FIRECODE CURRENT PROVINCE SHARE', 'RPT FIRECODE CURRENT PROVINCE SHARE', 'PAYABLE', 'FIRECODE', '02', 'FIRECODE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_FIRECODEINT_CURRENT_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT FIRECODE PENALTY CURRENT PROVINCE SHARE', 'RPT FIRECODE PENALTY CURRENT PROVINCE SHARE', 'PAYABLE', 'FIRECODE', '02', 'FIRECODE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_FIRECODE_PREVIOUS_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT FIRECODE PREVIOUS PROVINCE SHARE', 'RPT FIRECODE PREVIOUS PROVINCE SHARE', 'PAYABLE', 'FIRECODE', '02', 'FIRECODE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_FIRECODEINT_PREVIOUS_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT FIRECODE PENALTY PREVIOUS PROVINCE SHARE', 'RPT FIRECODE PENALTY PREVIOUS PROVINCE SHARE', 'PAYABLE', 'FIRECODE', '02', 'FIRECODE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_FIRECODE_PRIOR_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT FIRECODE PRIOR PROVINCE SHARE', 'RPT FIRECODE PRIOR PROVINCE SHARE', 'PAYABLE', 'FIRECODE', '02', 'FIRECODE', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_FIRECODEINT_PRIOR_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT FIRECODE PENALTY PRIOR PROVINCE SHARE', 'RPT FIRECODE PENALTY PRIOR PROVINCE SHARE', 'PAYABLE', 'FIRECODE', '02', 'FIRECODE', '0.00', 'ANY', NULL, NULL, NULL);

    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SH_ADVANCE_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING ADVANCE PROVINCE SHARE', 'RPT SOCIAL HOUSING ADVANCE PROVINCE SHARE', 'PAYABLE', 'SOCIAL HOUSING', '02', 'SOCIAL HOUSING', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SH_CURRENT_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING CURRENT PROVINCE SHARE', 'RPT SOCIAL HOUSING CURRENT PROVINCE SHARE', 'PAYABLE', 'SOCIAL HOUSING', '02', 'SOCIAL HOUSING', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SHINT_CURRENT_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING PENALTY CURRENT PROVINCE SHARE', 'RPT SOCIAL HOUSING PENALTY CURRENT PROVINCE SHARE', 'PAYABLE', 'SOCIAL HOUSING', '02', 'SOCIAL HOUSING', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SH_PREVIOUS_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING PREVIOUS PROVINCE SHARE', 'RPT SOCIAL HOUSING PREVIOUS PROVINCE SHARE', 'PAYABLE', 'SOCIAL HOUSING', '02', 'SOCIAL HOUSING', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SHINT_PREVIOUS_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING PENALTY PREVIOUS PROVINCE SHARE', 'RPT SOCIAL HOUSING PENALTY PREVIOUS PROVINCE SHARE', 'PAYABLE', 'SOCIAL HOUSING', '02', 'SOCIAL HOUSING', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SH_PRIOR_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING PRIOR PROVINCE SHARE', 'RPT SOCIAL HOUSING PRIOR PROVINCE SHARE', 'PAYABLE', 'SOCIAL HOUSING', '02', 'SOCIAL HOUSING', '0.00', 'ANY', NULL, NULL, NULL);
    INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_SHINT_PRIOR_PROVINCE_SHARE', 'APPROVED', '455-050', 'RPT SOCIAL HOUSING PENALTY PRIOR PROVINCE SHARE', 'RPT SOCIAL HOUSING PENALTY PRIOR PROVINCE SHARE', 'PAYABLE', 'SOCIAL HOUSING', '02', 'SOCIAL HOUSING', '0.00', 'ANY', NULL, NULL, NULL);

    update itemaccount ia, landtax_lgu_account_mapping m, province b set 
      ia.parentid = case 
        when m.revtype = 'basic' and revperiod = 'advance' then 'RPT_BASIC_ADVANCE_PROVINCE_SHARE'
        when m.revtype = 'basic' and revperiod = 'current' then 'RPT_BASIC_CURRENT_PROVINCE_SHARE'
        when m.revtype = 'basicint' and revperiod = 'current' then 'RPT_BASICINT_CURRENT_PROVINCE_SHARE'
        when m.revtype = 'basic' and revperiod = 'previous' then 'RPT_BASIC_PREVIOUS_PROVINCE_SHARE'
        when m.revtype = 'basicint' and revperiod = 'previous' then 'RPT_BASICINT_PREVIOUS_PROVINCE_SHARE'
        when m.revtype = 'basic' and revperiod = 'prior' then 'RPT_BASIC_PRIOR_PROVINCE_SHARE'
        when m.revtype = 'basicint' and revperiod = 'prior' then 'RPT_BASICINT_PRIOR_PROVINCE_SHARE'

        when m.revtype = 'sef' and revperiod = 'advance' then 'RPT_SEF_ADVANCE_PROVINCE_SHARE'
        when m.revtype = 'sef' and revperiod = 'current' then 'RPT_SEF_CURRENT_PROVINCE_SHARE'
        when m.revtype = 'sefint' and revperiod = 'current' then 'RPT_SEFINT_CURRENT_PROVINCE_SHARE'
        when m.revtype = 'sef' and revperiod = 'previous' then 'RPT_SEF_PREVIOUS_PROVINCE_SHARE'
        when m.revtype = 'sefint' and revperiod = 'previous' then 'RPT_SEFINT_PREVIOUS_PROVINCE_SHARE'
        when m.revtype = 'sef' and revperiod = 'prior' then 'RPT_SEF_PRIOR_PROVINCE_SHARE'
        when m.revtype = 'sefint' and revperiod = 'prior' then 'RPT_SEFINT_PRIOR_PROVINCE_SHARE'

        when m.revtype = 'basicidle' and revperiod = 'advance' then 'RPT_BASICIDLE_ADVANCE_PROVINCE_SHARE'
        when m.revtype = 'basicidle' and revperiod = 'current' then 'RPT_BASICIDLE_CURRENT_PROVINCE_SHARE'
        when m.revtype = 'basicidleint' and revperiod = 'current' then 'RPT_BASICIDLEINT_CURRENT_PROVINCE_SHARE'
        when m.revtype = 'basicidle' and revperiod = 'previous' then 'RPT_BASICIDLE_PREVIOUS_PROVINCE_SHARE'
        when m.revtype = 'basicidleint' and revperiod = 'previous' then 'RPT_BASICIDLEINT_PREVIOUS_PROVINCE_SHARE'
        when m.revtype = 'basicidle' and revperiod = 'prior' then 'RPT_BASICIDLE_PRIOR_PROVINCE_SHARE'
        when m.revtype = 'basicidleint' and revperiod = 'prior' then 'RPT_BASICIDLEINT_PRIOR_PROVINCE_SHARE'

        when m.revtype = 'sh' and revperiod = 'advance' then 'RPT_SH_ADVANCE_PROVINCE_SHARE'
        when m.revtype = 'sh' and revperiod = 'current' then 'RPT_SH_CURRENT_PROVINCE_SHARE'
        when m.revtype = 'shint' and revperiod = 'current' then 'RPT_SHINT_CURRENT_PROVINCE_SHARE'
        when m.revtype = 'sh' and revperiod = 'previous' then 'RPT_SH_PREVIOUS_PROVINCE_SHARE'
        when m.revtype = 'shint' and revperiod = 'previous' then 'RPT_SHINT_PREVIOUS_PROVINCE_SHARE'
        when m.revtype = 'sh' and revperiod = 'prior' then 'RPT_SH_PRIOR_PROVINCE_SHARE'
        when m.revtype = 'shint' and revperiod = 'prior' then 'RPT_SHINT_PRIOR_PROVINCE_SHARE'

        when m.revtype = 'firecode' and revperiod = 'advance' then 'RPT_FIRECODE_ADVANCE_PROVINCE_SHARE'
        when m.revtype = 'firecode' and revperiod = 'current' then 'RPT_FIRECODE_CURRENT_PROVINCE_SHARE'
        when m.revtype = 'firecodeint' and revperiod = 'current' then 'RPT_FIRECODEINT_CURRENT_PROVINCE_SHARE'
        when m.revtype = 'firecode' and revperiod = 'previous' then 'RPT_FIRECODE_PREVIOUS_PROVINCE_SHARE'
        when m.revtype = 'firecodeint' and revperiod = 'previous' then 'RPT_FIRECODEINT_PREVIOUS_PROVINCE_SHARE'
        when m.revtype = 'firecode' and revperiod = 'prior' then 'RPT_FIRECODE_PRIOR_PROVINCE_SHARE'
        when m.revtype = 'firecodeint' and revperiod = 'prior' then 'RPT_FIRECODEINT_PRIOR_PROVINCE_SHARE'
        end,
      ia.org_objid = b.objid, 
      ia.org_name = b.name,
      ia.type = 'PAYABLE'
    where ia.objid = m.item_objid
    and m.lgu_objid = b.objid
    ;

  end if;




  /* BARANGAY SHARE PAYABLE */

  INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASIC_ADVANCE_BRGY_SHARE', 'APPROVED', '455-049', 'RPT BASIC ADVANCE BARANGAY SHARE', 'RPT BASIC ADVANCE BARANGAY SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
  INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASIC_CURRENT_BRGY_SHARE', 'APPROVED', '455-049', 'RPT BASIC CURRENT BARANGAY SHARE', 'RPT BASIC CURRENT BARANGAY SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
  INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICINT_CURRENT_BRGY_SHARE', 'APPROVED', '455-049', 'RPT BASIC PENALTY CURRENT BARANGAY SHARE', 'RPT BASIC PENALTY CURRENT BARANGAY SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
  INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASIC_PREVIOUS_BRGY_SHARE', 'APPROVED', '455-049', 'RPT BASIC PREVIOUS BARANGAY SHARE', 'RPT BASIC PREVIOUS BARANGAY SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
  INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICINT_PREVIOUS_BRGY_SHARE', 'APPROVED', '455-049', 'RPT BASIC PENALTY PREVIOUS BARANGAY SHARE', 'RPT BASIC PENALTY PREVIOUS BARANGAY SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
  INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASIC_PRIOR_BRGY_SHARE', 'APPROVED', '455-049', 'RPT BASIC PRIOR BARANGAY SHARE', 'RPT BASIC PRIOR BARANGAY SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);
  INSERT INTO `itemaccount` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`, `parentid`) VALUES ('RPT_BASICINT_PRIOR_BRGY_SHARE', 'APPROVED', '455-049', 'RPT BASIC PENALTY PRIOR BARANGAY SHARE', 'RPT BASIC PENALTY PRIOR BARANGAY SHARE', 'PAYABLE', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL, NULL);


    insert into itemaccount_tag (objid, acctid, tag)
    select  'RPT_BASIC_ADVANCE_BRGY_SHARE' as objid, 'RPT_BASIC_ADVANCE_BRGY_SHARE' as acctid, 'rpt_basic_advance' as tag
    union 
    select  'RPT_BASIC_CURRENT_BRGY_SHARE' as objid, 'RPT_BASIC_CURRENT_BRGY_SHARE' as acctid, 'rpt_basic_current' as tag
    union 
    select  'RPT_BASICINT_CURRENT_BRGY_SHARE' as objid, 'RPT_BASICINT_CURRENT_BRGY_SHARE' as acctid, 'rpt_basicint_current' as tag
    union 
    select  'RPT_BASIC_PREVIOUS_BRGY_SHARE' as objid, 'RPT_BASIC_PREVIOUS_BRGY_SHARE' as acctid, 'rpt_basic_previous' as tag
    union 
    select  'RPT_BASICINT_PREVIOUS_BRGY_SHARE' as objid, 'RPT_BASICINT_PREVIOUS_BRGY_SHARE' as acctid, 'rpt_basicint_previous' as tag
    union 
    select  'RPT_BASIC_PRIOR_BRGY_SHARE' as objid, 'RPT_BASIC_PRIOR_BRGY_SHARE' as acctid, 'rpt_basic_prior' as tag
    union 
    select  'RPT_BASICINT_PRIOR_BRGY_SHARE' as objid, 'RPT_BASICINT_PRIOR_BRGY_SHARE' as acctid, 'rpt_basicint_prior' as tag
    union 
    select  'RPT_SEF_ADVANCE_BRGY_SHARE' as objid, 'RPT_SEF_ADVANCE_BRGY_SHARE' as acctid, 'rpt_sef_advance' as tag
    union 
    select  'RPT_SEF_CURRENT_BRGY_SHARE' as objid, 'RPT_SEF_CURRENT_BRGY_SHARE' as acctid, 'rpt_sef_current' as tag
    union 
    select  'RPT_SEFINT_CURRENT_BRGY_SHARE' as objid, 'RPT_SEFINT_CURRENT_BRGY_SHARE' as acctid, 'rpt_sefint_current' as tag
    union 
    select  'RPT_SEF_PREVIOUS_BRGY_SHARE' as objid, 'RPT_SEF_PREVIOUS_BRGY_SHARE' as acctid, 'rpt_sef_previous' as tag
    union 
    select  'RPT_SEFINT_PREVIOUS_BRGY_SHARE' as objid, 'RPT_SEFINT_PREVIOUS_BRGY_SHARE' as acctid, 'rpt_sefint_previous' as tag
    union 
    select  'RPT_SEF_PRIOR_BRGY_SHARE' as objid, 'RPT_SEF_PRIOR_BRGY_SHARE' as acctid, 'rpt_sef_prior' as tag
    union 
    select  'RPT_SEFINT_PRIOR_BRGY_SHARE' as objid, 'RPT_SEFINT_PRIOR_BRGY_SHARE' as acctid, 'rpt_sefint_prior' as tag
    ;


  update itemaccount ia, landtax_lgu_account_mapping m, barangay b set 
    ia.parentid = case 
      when m.revtype = 'basic' and revperiod = 'advance' then 'RPT_BASIC_ADVANCE_BRGY_SHARE'
      when m.revtype = 'basic' and revperiod = 'current' then 'RPT_BASIC_CURRENT_BRGY_SHARE'
      when m.revtype = 'basicint' and revperiod = 'current' then 'RPT_BASICINT_CURRENT_BRGY_SHARE'
      when m.revtype = 'basic' and revperiod = 'previous' then 'RPT_BASIC_PREVIOUS_BRGY_SHARE'
      when m.revtype = 'basicint' and revperiod = 'previous' then 'RPT_BASICINT_PREVIOUS_BRGY_SHARE'
      when m.revtype = 'basic' and revperiod = 'prior' then 'RPT_BASIC_PRIOR_BRGY_SHARE'
      when m.revtype = 'basicint' and revperiod = 'prior' then 'RPT_BASICINT_PRIOR_BRGY_SHARE'
      end,
    ia.org_objid = b.objid, 
    ia.org_name = b.name,
      ia.type = 'PAYABLE'
  where ia.objid = m.item_objid
  and m.lgu_objid = b.objid;

  update itemaccount a, landtax_lgu_account_mapping m, sys_org o set 
    a.parentid = case 
        when m.revtype = 'basic' and revperiod = 'advance' then 'RPT_BASIC_ADVANCE'
        when m.revtype = 'basic' and revperiod = 'current' then 'RPT_BASIC_CURRENT'
        when m.revtype = 'basicint' and revperiod = 'current' then 'RPT_BASICINT_CURRENT'
        when m.revtype = 'basic' and revperiod = 'previous' then 'RPT_BASIC_PREVIOUS'
        when m.revtype = 'basicint' and revperiod = 'previous' then 'RPT_BASICINT_PREVIOUS'
        when m.revtype = 'basic' and revperiod = 'prior' then 'RPT_BASIC_PRIOR'
        when m.revtype = 'basicint' and revperiod = 'prior' then 'RPT_BASICINT_PRIOR'

        when m.revtype = 'sef' and revperiod = 'advance' then 'RPT_SEF_ADVANCE'
      when m.revtype = 'basic' and revperiod = 'advance' then 'RPT_BASIC_ADVANCE'
      when m.revtype = 'basic' and revperiod = 'current' then 'RPT_BASIC_CURRENT'
      when m.revtype = 'basicint' and revperiod = 'current' then 'RPT_BASICINT_CURRENT'
      when m.revtype = 'basic' and revperiod = 'previous' then 'RPT_BASIC_PREVIOUS'
      when m.revtype = 'basicint' and revperiod = 'previous' then 'RPT_BASICINT_PREVIOUS'
      when m.revtype = 'basic' and revperiod = 'prior' then 'RPT_BASIC_PRIOR'
      when m.revtype = 'basicint' and revperiod = 'prior' then 'RPT_BASICINT_PRIOR'

      when m.revtype = 'sef' and revperiod = 'advance' then 'RPT_SEF_ADVANCE'
        when m.revtype = 'sef' and revperiod = 'current' then 'RPT_SEF_CURRENT'
        when m.revtype = 'sefint' and revperiod = 'current' then 'RPT_SEFINT_CURRENT'
        when m.revtype = 'sef' and revperiod = 'previous' then 'RPT_SEF_PREVIOUS'
        when m.revtype = 'sefint' and revperiod = 'previous' then 'RPT_SEFINT_PREVIOUS'
        when m.revtype = 'sef' and revperiod = 'prior' then 'RPT_SEF_PRIOR'
        when m.revtype = 'sefint' and revperiod = 'prior' then 'RPT_SEFINT_PRIOR'

        when m.revtype = 'basicidle' and revperiod = 'advance' then 'RPT_BASICIDLE_ADVANCE'
        when m.revtype = 'basicidle' and revperiod = 'current' then 'RPT_BASICIDLE_CURRENT'
        when m.revtype = 'basicidleint' and revperiod = 'current' then 'RPT_BASICIDLEINT_CURRENT'
        when m.revtype = 'basicidle' and revperiod = 'previous' then 'RPT_BASICIDLE_PREVIOUS'
        when m.revtype = 'basicidleint' and revperiod = 'previous' then 'RPT_BASICIDLEINT_PREVIOUS'
        when m.revtype = 'basicidle' and revperiod = 'prior' then 'RPT_BASICIDLE_PRIOR'
        when m.revtype = 'basicidleint' and revperiod = 'prior' then 'RPT_BASICIDLEINT_PRIOR'

        when m.revtype = 'firecode' and revperiod = 'advance' then 'RPT_FIRECODE_ADVANCE'
        when m.revtype = 'firecode' and revperiod = 'current' then 'RPT_FIRECODE_CURRENT'
        when m.revtype = 'firecodeint' and revperiod = 'current' then 'RPT_FIRECODEINT_CURRENT'
        when m.revtype = 'firecode' and revperiod = 'previous' then 'RPT_FIRECODE_PREVIOUS'
        when m.revtype = 'firecodeint' and revperiod = 'previous' then 'RPT_FIRECODEINT_PREVIOUS'
        when m.revtype = 'firecode' and revperiod = 'prior' then 'RPT_FIRECODE_PRIOR'
        when m.revtype = 'firecodeint' and revperiod = 'prior' then 'RPT_FIRECODEINT_PRIOR'

        when m.revtype = 'sh' and revperiod = 'advance' then 'RPT_SH_ADVANCE'
        when m.revtype = 'sh' and revperiod = 'current' then 'RPT_SH_CURRENT'
        when m.revtype = 'shint' and revperiod = 'current' then 'RPT_SHINT_CURRENT'
        when m.revtype = 'sh' and revperiod = 'previous' then 'RPT_SH_PREVIOUS'
        when m.revtype = 'shint' and revperiod = 'previous' then 'RPT_SHINT_PREVIOUS'
        when m.revtype = 'sh' and revperiod = 'prior' then 'RPT_SH_PRIOR'
        when m.revtype = 'shint' and revperiod = 'prior' then 'RPT_SHINT_PRIOR'
      end,
    a.type = 'REVENUE',
    a.org_objid = o.objid,
    a.org_name = o.name
  where m.item_objid = a.objid
  and m.lgu_objid = o.objid 
  and o.root = 1;

  
  insert into itemaccount_tag (objid, acctid, tag)
  select 
    concat(a.objid, ':', m.revtype, '_', m.revperiod) as objid,
    a.objid as acctid, 
    concat('rpt_', m.revtype, '_', m.revperiod) as tag 
  from landtax_lgu_account_mapping m, itemaccount a
  where m.item_objid = a.objid 



end ;




call buildRptAccounts();


drop view if exists vw_landtax_lgu_account_mapping
; 

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
  ia.type as item_type,
  pt.tag as item_tag
from itemaccount ia
inner join itemaccount p on ia.parentid = p.objid 
inner join itemaccount_tag pt on p.objid = pt.acctid
inner join sys_org o on ia.org_objid = o.objid 
where p.state = 'APPROVED'
; 
