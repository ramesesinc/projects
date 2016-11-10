
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





alter table sys_usergroup_permission  modify column permission varchar(50) not null;

insert into sys_var 
  (name, value, description, datatype, category)
values
  ('rpt_compromise_downpayment_rate', '0.0', 'Compromise Agreement required downpayment rate.', 'decimal', 'LANDTAX');


alter table rptledger add column undercompromise int null;


create table rptledger_compromise
(
  objid varchar(50) not null, 
  state varchar(25) not null, 
  txnno varchar(25) not null, 
  txndate date not null, 
  faasid varchar(50), 
  rptledgerid varchar(50) not null, 
  lastyearpaid int not null, 
  lastqtrpaid int not null, 
  startyear int not null, 
  startqtr int not null, 
  endyear int not null, 
  endqtr int not null, 
  enddate date not null, 
  cypaymentrequired int, 
  cypaymentorno varchar(10), 
  cypaymentordate date, 
  cypaymentoramount decimal(10,2) null, 
  downpaymentrequired int not null, 
  downpaymentrate decimal(10,0) not null, 
  downpayment decimal(10,2) not null, 
  downpaymentorno varchar(50), 
  downpaymentordate date, 
  term int not null, 
  numofinstallment int not null, 
  amount decimal(16,2) not null, 
  amtforinstallment decimal(16,2) not null, 
  amtpaid decimal(16,2) not null, 
  firstpartyname varchar(100) not null, 
  firstpartytitle varchar(50) not null, 
  firstpartyaddress varchar(100) not null, 
  firstpartyctcno varchar(15) not null, 
  firstpartyctcissued varchar(100) not null, 
  firstpartyctcdate date not null, 
  firstpartynationality varchar(50) not null, 
  firstpartystatus varchar(50) not null, 
  firstpartygender varchar(10) not null, 
  secondpartyrepresentative varchar(100) not null, 
  secondpartyname varchar(100) not null, 
  secondpartyaddress varchar(100) not null, 
  secondpartyctcno varchar(15) not null, 
  secondpartyctcissued varchar(100) not null, 
  secondpartyctcdate date not null, 
  secondpartynationality varchar(50) not null, 
  secondpartystatus varchar(50) not null, 
  secondpartygender varchar(10) not null, 
  dtsigned date , 
  notarizeddate date , 
  notarizedby varchar(100) , 
  notarizedbytitle varchar(50) , 
  signatories varchar(1000) not null, 
  primary key (objid)
)engine=innodb;

create index ix_rptcompromise_faasid on rptledger_compromise(faasid);
create index ix_rptcompromise_ledgerid on rptledger_compromise(rptledgerid);

alter table rptledger_compromise 
  add constraint FK_rptleger_compromise_faas foreign key (faasid)
  references faas(objid);


alter table rptledger_compromise 
  add constraint FK_rptleger_compromise_rptledger foreign key (rptledgerid)
  references rptledger(objid);




create table rptledger_compromise_item
(
  objid varchar(50) not null,
  rptcompromiseid varchar(50) not null,
  year int not null,
  qtr int not null,
  faasid varchar(50),
  assessedvalue decimal(16,2) not null,
  tdno varchar(25) not null,
  classcode varchar(10) not null,
  actualusecode varchar(10) not null,
  basic decimal(16,2) not null,
  basicpaid decimal(16,2) not null,
  basicint decimal(16,2) not null,
  basicintpaid decimal(16,2) not null,
  basicidle decimal(16,2) not null,
  basicidlepaid decimal(16,2) not null,
  sef decimal(16,2) not null,
  sefpaid decimal(16,2) not null,
  sefint decimal(16,2) not null,
  sefintpaid decimal(16,2) not null,
  firecode decimal(16,2) not null,
  firecodepaid decimal(16,2) not null,
  total decimal(16,2) not null,
  fullypaid int not null,
  primary key(objid)
) engine=innodb;

create index ix_rptcompromise_item_rptcompromise on rptledger_compromise_item(rptcompromiseid);
create index ix_rptcompromise_item_faas  on rptledger_compromise_item(faasid);



alter table rptledger_compromise_item
  add constraint FK_rptleger_compromise_item_rptcompromise foreign key (rptcompromiseid)
  references rptledger_compromise(objid);

alter table rptledger_compromise_item
  add constraint FK_rptleger_compromise_item_faas  foreign key (faasid)
  references faas(objid);




create table rptledger_compromise_installment
(
  objid varchar(50) not null,
  rptcompromiseid varchar(50) not null,
  installmentno int not null,
  duedate date not null,
  amount decimal(16,2) not null,
  amtpaid decimal(16,2) not null,
  fullypaid int not null,
  primary key(objid)
)engine=innodb;

create index ix_rptcompromise_installment_rptcompromiseid  on rptledger_compromise_installment(rptcompromiseid);

alter table rptledger_compromise_installment
  add constraint FK_rptleger_compromise_installment_rptcompromise foreign key (rptcompromiseid)
  references rptledger_compromise(objid);



create table rptledger_compromise_credit
(
  objid varchar(50) not null,
  rptcompromiseid varchar(50) not null,
  rptreceiptid varchar(50) null,
  installmentid varchar(50) ,
  collector_name varchar(100) not null,
  collector_title varchar(50) not null,
  orno varchar(10) not null,
  ordate date not null,
  oramount decimal(16,2) not null,
  amount decimal(16,2) not null,
  mode varchar(50) not null,
  paidby varchar(150) not null,
  paidbyaddress varchar(100) not null,
  partial int null,
  remarks varchar(100),
  primary key(objid)
)engine=innodb;


create index ix_rptcompromise_credit_rptcompromiseid  on rptledger_compromise_credit(rptcompromiseid);
create index ix_rptcompromise_credit_receiptid  on rptledger_compromise_credit(rptreceiptid);
create index ix_rptcompromise_credit_installmentid  on rptledger_compromise_credit(installmentid);

alter table rptledger_compromise_credit
  add constraint FK_rptleger_compromise_credit_rptcompromise foreign key (rptcompromiseid)
  references rptledger_compromise(objid);

alter table rptledger_compromise_credit
  add constraint FK_rptleger_compromise_credit_receipt foreign key (rptreceiptid)
  references cashreceipt(objid);

alter table rptledger_compromise_credit
  add constraint FK_rptleger_compromise_credit_installment foreign key (installmentid)
  references rptledger_compromise_installment(objid);


  


create table rptledger_compromise_item_credit
(
  objid varchar(50) not null,
  rptcompromiseitemid varchar(50) not null,
  rptreceiptid varchar(50) null,
  year int not null,
  qtr int not null,
  basic decimal(16,2) not null,
  basicint decimal(16,2) not null,
  basicidle decimal(16,2) not null,
  sef decimal(16,2) not null,
  sefint decimal(16,2) not null,
  firecode decimal(16,2) not null,
  primary key(objid)
) engine=innodb;

create index ix_rptledger_compromise_item_credit_rptcompromiseitemid on rptledger_compromise_item_credit(rptcompromiseitemid);
create index ix_rptledger_compromise_item_credit_rptreceiptid on rptledger_compromise_item_credit(rptreceiptid);



alter table rptledger_compromise_item_credit
  add constraint FK_rptledger_compromise_item_credit_rptcompromise_item foreign key (rptcompromiseitemid)
  references rptledger_compromise_item(objid);


alter table rptledger_compromise_item_credit
  add constraint FK_rptledger_compromise_item_credit_rptreceipt foreign key (rptreceiptid)
  references cashreceipt_rpt(objid);





