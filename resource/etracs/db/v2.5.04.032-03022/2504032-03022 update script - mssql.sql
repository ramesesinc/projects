/* 03022 */

/*============================================
*
* SYNC PROVINCE AND REMOTE LEGERS
*
*============================================*/

drop table rptledger_remote
go 

CREATE TABLE remote_mapping (
  objid varchar(50) NOT NULL,
  remote_objid varchar(50) NULL,
  createdby_name varchar(255) NOT NULL,
  createdby_title varchar(100) DEFAULT NULL,
  dtcreated datetime NOT NULL,
  orgcode varchar(10) DEFAULT NULL,
  remote_orgcode varchar(10) DEFAULT NULL,
  PRIMARY KEY (objid)
)
go 



create table sync_data (
  objid varchar(50) not null,
  refid varchar(50) not null,
  reftype varchar(50) not null,
  orgid varchar(50) null,
  action varchar(50) not null,
  dtfiled datetime not null,
  primary key (objid)
)
go 

create index ix_sync_data_refid on sync_data(refid)
go 

create index ix_sync_data_reftype on sync_data(reftype)
go 

create index ix_sync_data_orgid on sync_data(orgid)
go 

create index ix_sync_data_dtfiled on sync_data(dtfiled)
go 



CREATE TABLE sync_data_forprocess (
  objid varchar(50) NOT NULL,
  PRIMARY KEY (objid)
) 
go 

alter table sync_data_forprocess add constraint fk_sync_data_forprocess_sync_data 
  foreign key (objid) references sync_data (objid)
go 

CREATE TABLE sync_data_pending (
  objid varchar(50) NOT NULL,
  error text,
  expirydate datetime,
  PRIMARY KEY (objid)
) 
go 


alter table sync_data_pending add constraint fk_sync_data_pending_sync_data 
  foreign key (objid) references sync_data (objid)
go 

create index ix_expirydate on sync_data_pending(expirydate)
go 


