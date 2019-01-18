/* 255-03001 */

-- create tables: resection and resection_item

if exists(select * from sysobjects where id = object_id('resectionaffectedrpu'))
begin 
	drop table resectionaffectedrpu
end 
go 


if exists(select * from sysobjects where id = object_id('resectionitem'))
begin 
	drop table resectionitem
end 
go 


if exists(select * from sysobjects where id = object_id('resection'))
begin 
	drop table resection
end 
go 


CREATE TABLE resection (
  objid varchar(50) NOT NULL,
  state varchar(25) NOT NULL,
  txnno varchar(25) NOT NULL,
  txndate datetime NOT NULL,
  lgu_objid varchar(50) NOT NULL,
  barangay_objid varchar(50) NOT NULL,
  pintype varchar(3) NOT NULL,
  section varchar(3) NOT NULL,
  originlgu_objid varchar(50) NOT NULL,
  memoranda varchar(255) DEFAULT NULL,
  taskid varchar(50) DEFAULT NULL,
  taskstate varchar(50) DEFAULT NULL,
  assignee_objid varchar(50) DEFAULT NULL,
  PRIMARY KEY (objid)
)
go 



create UNIQUE index ux_resection_txnno on resection(txnno)
go 

create index FK_resection_lgu_org on resection(lgu_objid)
go 
create index FK_resection_barangay_org on resection(barangay_objid)
go 
create index FK_resection_originlgu_org on resection(originlgu_objid)
go 
create index ix_resection_state on resection(state)
go 


  alter table resection 
    add CONSTRAINT FK_resection_barangay_org FOREIGN KEY (barangay_objid) 
    REFERENCES sys_org (objid)
go     
  alter table resection 
    add CONSTRAINT FK_resection_lgu_org FOREIGN KEY (lgu_objid) 
    REFERENCES sys_org (objid)
go     
  alter table resection 
    add CONSTRAINT FK_resection_originlgu_org FOREIGN KEY (originlgu_objid) 
    REFERENCES sys_org (objid)
go     




CREATE TABLE resection_item (
  objid varchar(50) NOT NULL,
  parent_objid varchar(50) NOT NULL,
  faas_objid varchar(50) NOT NULL,
  faas_rputype varchar(15) NOT NULL,
  faas_pin varchar(25) NOT NULL,
  faas_suffix int NOT NULL,
  newfaas_objid varchar(50) DEFAULT NULL,
  newfaas_rpuid varchar(50) DEFAULT NULL,
  newfaas_rpid varchar(50) DEFAULT NULL,
  newfaas_section varchar(3) DEFAULT NULL,
  newfaas_parcel varchar(3) DEFAULT NULL,
  newfaas_suffix int DEFAULT NULL,
  newfaas_tdno varchar(25) DEFAULT NULL,
  newfaas_fullpin varchar(50) DEFAULT NULL,
  PRIMARY KEY (objid)
)
go 

create UNIQUE index ux_resection_item_tdno on resection_item (newfaas_tdno)
go 

create index FK_resection_item_item on resection_item(parent_objid)
go   
create index FK_resection_item_faas on resection_item(faas_objid)
go   
create index FK_resection_item_newfaas on resection_item(newfaas_objid)
go   
create index ix_resection_item_fullpin on resection_item(newfaas_fullpin)
go   


alter table resection_item add CONSTRAINT FK_resection_item_faas FOREIGN KEY (faas_objid) 
  REFERENCES faas (objid)
go   
alter table resection_item add CONSTRAINT FK_resection_item_item FOREIGN KEY (parent_objid) 
  REFERENCES resection (objid)
go   
alter table resection_item add CONSTRAINT FK_resection_item_newfaas FOREIGN KEY (newfaas_objid) 
  REFERENCES faas (objid)
go     



CREATE TABLE resection_task (
  objid varchar(50) NOT NULL,
  refid varchar(50) DEFAULT NULL,
  parentprocessid varchar(50) DEFAULT NULL,
  state varchar(50) DEFAULT NULL,
  startdate datetime DEFAULT NULL,
  enddate datetime DEFAULT NULL,
  assignee_objid varchar(50) DEFAULT NULL,
  assignee_name varchar(100) DEFAULT NULL,
  assignee_title varchar(80) DEFAULT NULL,
  actor_objid varchar(50) DEFAULT NULL,
  actor_name varchar(100) DEFAULT NULL,
  actor_title varchar(80) DEFAULT NULL,
  message varchar(255) DEFAULT NULL,
  signature text,
  PRIMARY KEY (objid)
)
go 


create index  ix_assignee_objid on resection_task (assignee_objid)
go 
create index  ix_refid on resection_task (refid)
go 

