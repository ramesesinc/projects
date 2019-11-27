/* 255-03016 */

/*================================================================
*
* RPTLEDGER REDFLAG
*
================================================================*/

CREATE TABLE rptledger_redflag (
  objid varchar(50) NOT NULL,
  parent_objid varchar(50) NOT NULL,
  state varchar(25) NOT NULL,
  caseno varchar(25) NULL,
  dtfiled datetime NULL,
  type varchar(25) NOT NULL,
  finding text,
  remarks text,
  blockaction varchar(25) DEFAULT NULL,
  filedby_objid varchar(50) DEFAULT NULL,
  filedby_name varchar(255) DEFAULT NULL,
  filedby_title varchar(50) DEFAULT NULL,
  resolvedby_objid varchar(50) DEFAULT NULL,
  resolvedby_name varchar(255) DEFAULT NULL,
  resolvedby_title varchar(50) DEFAULT NULL,
  dtresolved datetime NULL,
  PRIMARY KEY (objid)
) 
go

create index ix_parent_objid on rptledger_redflag(parent_objid)
go
create index ix_state on rptledger_redflag(state)
go
create unique index ux_caseno on rptledger_redflag(caseno)
go
create index ix_type on rptledger_redflag(type)
go
create index ix_filedby_objid on rptledger_redflag(filedby_objid)
go
create index ix_resolvedby_objid on rptledger_redflag(resolvedby_objid)
go

alter table rptledger_redflag 
add constraint fk_rptledger_redflag_rptledger foreign key (parent_objid)
references rptledger(objid)
go

alter table rptledger_redflag 
add constraint fk_rptledger_redflag_filedby foreign key (filedby_objid)
references sys_user(objid)
go

alter table rptledger_redflag 
add constraint fk_rptledger_redflag_resolvedby foreign key (resolvedby_objid)
references sys_user(objid)
go
