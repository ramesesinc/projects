
CREATE TABLE rpttransmittal (
  objid varchar(50) NOT NULL,
  state varchar(50) NOT NULL,
  type varchar(15) NOT NULL,
  txnno varchar(15) NOT NULL,
  txndate datetime NOT NULL,
  lgu_objid varchar(50) NOT NULL,
  lgu_name varchar(50) NOT NULL,
  lgu_type varchar(50) NOT NULL,
  tolgu_objid varchar(50) NOT NULL,
  tolgu_name varchar(50) NOT NULL,
  tolgu_type varchar(50) NOT NULL,
  createdby_objid varchar(50) NOT NULL,
  createdby_name varchar(100) NOT NULL,
  createdby_title varchar(50) NOT NULL,
  remarks varchar(500) NULL,
  PRIMARY KEY  (objid) 
)
go 

create UNIQUE index ux_txnno on rpttransmittal(txnno)
go

create index ix_state on rpttransmittal(state)
go 

create index ix_createdby_name on rpttransmittal(createdby_name)
go 
  
create index ix_lguname on rpttransmittal (lgu_name)
go 




CREATE TABLE rpttransmittal_item (
  objid varchar(50) NOT NULL,
  transmittalid varchar(50) NOT NULL,
  state varchar(50) NOT NULL,
  refid varchar(50) NOT NULL,
  filetype varchar(50) NOT NULL,
  txntype_objid varchar(50) NOT NULL,
  tdno varchar(50) NOT NULL,
  owner_name varchar(1000) NOT NULL,
  owner_address varchar(100) NOT NULL,
  fullpin varchar(50) NOT NULL,
  cadastrallotno varchar(500) NOT NULL,
  totalareaha decimal(16,6) NOT NULL,
  totalareasqm decimal(16,2) NOT NULL,
  totalmv decimal(16,2) NOT NULL,
  totalav decimal(16,2) NOT NULL,
  remarks text,
  PRIMARY KEY  (objid)
)
go 

create UNIQUE index ux_transmittalid_refid on rpttransmittal_item(transmittalid,refid)
go 

create index ix_refid on rpttransmittal_item(refid)
go 
create index FK_rpttransmittal_item_rpttransmittal on rpttransmittal_item(transmittalid)
go 
create index FK_rpttransmittal_item_data_rpttransmittal on rpttransmittal_item(transmittalid)
go 

alter table rpttransmittal_item
 add CONSTRAINT FK_rpttransmittal_item_rpttransmittal 
 FOREIGN KEY (transmittalid) REFERENCES rpttransmittal (objid)
go



CREATE TABLE rpttransmittal_item_data (
  objid varchar(50) NOT NULL,
  transmittalid varchar(50) NOT NULL,
  parentid varchar(50) NOT NULL,
  itemno int NOT NULL,
  itemtype varchar(50) NOT NULL,
  data text NOT NULL,
  PRIMARY KEY  (objid)
)
go 

create UNIQUE index ux_rpttransmittal_itemno on rpttransmittal_item_data(parentid,itemno)
go 

create index FK_rpttransmittal_item_data_rpttransmittal on rpttransmittal_item_data (transmittalid)
go 

create index FK_rpttransmittal_item_data_rpttransmittalitem on rpttransmittal_item_data (parentid)
go 

alter table rpttransmittal_item_data 
  add CONSTRAINT FK_rpttransmittal_item_data_rpttransmittal 
  FOREIGN KEY (transmittalid) REFERENCES rpttransmittal (objid)
go 

alter table rpttransmittal_item_data 
  add CONSTRAINT FK_rpttransmittal_item_data_rpttransmittalitem 
  FOREIGN KEY (parentid) REFERENCES rpttransmittal_item (objid)
go 



CREATE TABLE rpttransmittal_log (
  objid varchar(50) NOT NULL,
  transmittalid varchar(50) NOT NULL,
  refid varchar(50) NOT NULL,
  txndate datetime NOT NULL,
  error text,
  remarks varchar(500) default NULL,
  PRIMARY KEY  (objid),
  CONSTRAINT FK_rpttransmittal_log_rpttransmittal FOREIGN KEY (transmittalid) REFERENCES rpttransmittal (objid)
)
go 

create index FK_rpttransmittal_log_rpttransmittal on rpttransmittal_log(transmittalid)
go

create index ix_refid on rpttransmittal_log(refid)
go


alter table rptledgeritem add classification_objid varchar(50)
go   
alter table rptledgeritem add actualuse_objid varchar(50)
go 
alter table rptledgeritem add basicav decimal(16,2) null
go 
alter table rptledgeritem add sefav decimal(16,2) null
go 

update rptledgeritem set basicav = av, sefav = av where basicav is null
go 


update rli set
  rli.classification_objid = rlf.classification_objid,
  rli.actualuse_objid = rlf.actualuse_objid
from rptledgeritem rli, rptledgerfaas rlf
where rli.rptledgerfaasid = rlf.objid
go 

update rli set
  rli.actualuse_objid = rlf.classification_objid 
from rptledgeritem rli, rptledgerfaas rlf 
where rli.rptledgerfaasid = rlf.objid
and rli.actualuse_objid is null
go 


alter table cashreceiptitem_rpt_account add discount decimal(16,2)
go
update cashreceiptitem_rpt_account  set discount = 0 where discount is null
go 





