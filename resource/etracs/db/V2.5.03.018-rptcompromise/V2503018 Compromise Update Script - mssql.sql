

alter table sys_usergroup_permission  alter column permission nvarchar(50) not null
go 

insert into sys_var 
  (name, value, description, datatype, category)
values
  ('rpt_compromise_downpayment_rate', '0.0', 'Compromise Agreement required downpayment rate.', 'decimal', 'LANDTAX')

 go 







create table rptledger_compromise
(
  objid nvarchar(50) not null, 
  state nvarchar(25) not null, 
  txnno nvarchar(25) not null, 
  txndate date not null, 
  faasid nvarchar(50), 
  rptledgerid nvarchar(50) not null, 
  lastyearpaid int not null, 
  lastqtrpaid int not null, 
  startyear int not null, 
  startqtr int not null, 
  endyear int not null, 
  endqtr int not null, 
  enddate date not null, 
  cypaymentrequired int, 
  cypaymentorno nvarchar(10), 
  cypaymentordate date, 
  cypaymentoramount decimal(10,2) null, 
  downpaymentrequired int not null, 
  downpaymentrate decimal(10,0) not null, 
  downpayment decimal(10,2) not null, 
  downpaymentorno nvarchar(50), 
  downpaymentordate date, 
  term int not null, 
  numofinstallment int not null, 
  amount decimal(16,2) not null, 
  amtforinstallment decimal(16,2) not null, 
  amtpaid decimal(16,2) not null, 
  firstpartyname nvarchar(100) not null, 
  firstpartytitle nvarchar(50) not null, 
  firstpartyaddress nvarchar(100) not null, 
  firstpartyctcno nvarchar(15) not null, 
  firstpartyctcissued nvarchar(100) not null, 
  firstpartyctcdate date not null, 
  firstpartynationality nvarchar(50) not null, 
  firstpartystatus nvarchar(50) not null, 
  firstpartygender nvarchar(10) not null, 
  secondpartyrepresentative nvarchar(100) not null, 
  secondpartyname nvarchar(100) not null, 
  secondpartyaddress nvarchar(100) not null, 
  secondpartyctcno nvarchar(15) not null, 
  secondpartyctcissued nvarchar(100) not null, 
  secondpartyctcdate date not null, 
  secondpartynationality nvarchar(50) not null, 
  secondpartystatus nvarchar(50) not null, 
  secondpartygender nvarchar(10) not null, 
  dtsigned date , 
  notarizeddate date , 
  notarizedby nvarchar(100) , 
  notarizedbytitle nvarchar(50) , 
  signatories nvarchar(1000) not null, 
  primary key (objid)
)
go 


create index ix_rptcompromise_faasid on rptledger_compromise(faasid)
go 
create index ix_rptcompromise_ledgerid on rptledger_compromise(rptledgerid)
go 

alter table rptledger_compromise 
  add constraint FK_rptleger_compromise_faas foreign key (faasid)
  references faas(objid)
 go 


alter table rptledger_compromise 
  add constraint FK_rptleger_compromise_rptledger foreign key (rptledgerid)
  references rptledger(objid)
 go 






create table rptledger_compromise_item
(
  objid nvarchar(50) not null,
  rptcompromiseid nvarchar(50) not null,
  year int not null,
  qtr int not null,
  faasid nvarchar(50),
  assessedvalue decimal(16,2) not null,
  tdno nvarchar(25) not null,
  classcode nvarchar(10) not null,
  actualusecode nvarchar(10) not null,
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
) 
go 

create index ix_rptcompromise_item_rptcompromise on rptledger_compromise_item(rptcompromiseid)
go 
create index ix_rptcompromise_item_faas  on rptledger_compromise_item(faasid)
go 



alter table rptledger_compromise_item
  add constraint FK_rptleger_compromise_item_rptcompromise foreign key (rptcompromiseid)
  references rptledger_compromise(objid)
 go 

alter table rptledger_compromise_item
  add constraint FK_rptleger_compromise_item_faas  foreign key (faasid)
  references faas(objid)
 go 






create table rptledger_compromise_installment
(
  objid nvarchar(50) not null,
  rptcompromiseid nvarchar(50) not null,
  installmentno int not null,
  duedate date not null,
  amount decimal(16,2) not null,
  amtpaid decimal(16,2) not null,
  fullypaid int not null,
  primary key(objid)
)
go 

create index ix_rptcompromise_installment_rptcompromiseid  on rptledger_compromise_installment(rptcompromiseid)
	go

alter table rptledger_compromise_installment
  add constraint FK_rptleger_compromise_installment_rptcompromise foreign key (rptcompromiseid)
  references rptledger_compromise(objid)
  go



create table rptledger_compromise_credit
(
  objid nvarchar(50) not null,
  rptcompromiseid nvarchar(50) not null,
  rptreceiptid nvarchar(50) null,
  installmentid nvarchar(50) ,
  collector_name nvarchar(100) not null,
  collector_title nvarchar(50) not null,
  orno nvarchar(10) not null,
  ordate date not null,
  oramount decimal(16,2) not null,
  amount decimal(16,2) not null,
  mode nvarchar(50) not null,
  paidby nvarchar(150) not null,
  paidbyaddress nvarchar(100) not null,
  partial int null,
  remarks nvarchar(100),
  primary key(objid)
)
go 


create index ix_rptcompromise_credit_rptcompromiseid  on rptledger_compromise_credit(rptcompromiseid)
	go
create index ix_rptcompromise_credit_receiptid  on rptledger_compromise_credit(rptreceiptid)
	go
create index ix_rptcompromise_credit_installmentid  on rptledger_compromise_credit(installmentid)
	go



alter table rptledger_compromise_credit
  add constraint FK_rptleger_compromise_credit_rptcompromise foreign key (rptcompromiseid)
  references rptledger_compromise(objid)
 go 

alter table rptledger_compromise_credit
  add constraint FK_rptleger_compromise_credit_receipt foreign key (rptreceiptid)
  references cashreceipt(objid)
 go 

alter table rptledger_compromise_credit
  add constraint FK_rptleger_compromise_credit_installment foreign key (installmentid)
  references rptledger_compromise_installment(objid)
 go 


  


create table rptledger_compromise_item_credit
(
  objid nvarchar(50) not null,
  rptcompromiseitemid nvarchar(50) not null,
  rptreceiptid nvarchar(50) null,
  year int not null,
  qtr int not null,
  basic decimal(16,2) not null,
  basicint decimal(16,2) not null,
  basicidle decimal(16,2) not null,
  sef decimal(16,2) not null,
  sefint decimal(16,2) not null,
  firecode decimal(16,2) not null,
  primary key(objid)
) 
go 

create index ix_rptledger_compromise_item_credit_rptcompromiseitemid on rptledger_compromise_item_credit(rptcompromiseitemid)
go 
create index ix_rptledger_compromise_item_credit_rptreceiptid on rptledger_compromise_item_credit(rptreceiptid)
go 



alter table rptledger_compromise_item_credit
  add constraint FK_rptledger_compromise_item_credit_rptcompromise_item foreign key (rptcompromiseitemid)
  references rptledger_compromise_item(objid)
 go 


alter table rptledger_compromise_item_credit
  add constraint FK_rptledger_compromise_item_credit_rptreceipt foreign key (rptreceiptid)
  references cashreceipt_rpt(objid)
 go 


