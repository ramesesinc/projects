/* 255-03015 */

create table rptcertification_online (
  objid varchar(50) not null,
  state varchar(25) not null,
  reftype varchar(25) not null,
  refid varchar(50) not null,
  refno varchar(50) not null,
  refdate date not null,
  orno varchar(25) default null,
  ordate date default null,
  oramount decimal(16,2) default null,
  primary key (objid)
)
go 

alter table rptcertification_online 
	add constraint fk_rptcertification_online_rptcertification foreign key (objid) references rptcertification (objid)
go 
 
create index ix_state on rptcertification_online(state)
go 
 
create index ix_refid on rptcertification_online(refid)
go 
 
create index ix_refno on rptcertification_online(refno)
go 
 
create index ix_orno on rptcertification_online(orno)
go 
  



CREATE TABLE assessmentnotice_online (
  objid varchar(50) NOT NULL,
  state varchar(25) NOT NULL,
  reftype varchar(25) NOT NULL,
  refid varchar(50) NOT NULL,
  refno varchar(50) NOT NULL,
  refdate date NOT NULL,
  orno varchar(25) DEFAULT NULL,
  ordate date DEFAULT NULL,
  oramount decimal(16,2) DEFAULT NULL,
  PRIMARY KEY (objid)
) 
go 

create index ix_state on assessmentnotice_online (state)
go 
create index ix_refid on assessmentnotice_online (refid)
go 
create index ix_refno on assessmentnotice_online (refno)
go 
create index ix_orno on assessmentnotice_online (orno)
go 
  
alter table assessmentnotice_online 
  add CONSTRAINT fk_assessmentnotice_online_assessmentnotice 
  FOREIGN KEY (objid) REFERENCES assessmentnotice (objid)
go   



/*===============================================================
**
** FAAS ANNOTATION
**
===============================================================*/
CREATE TABLE faasannotation_faas (
  objid varchar(50) NOT NULL,
  parent_objid varchar(50) NOT NULL,
  faas_objid varchar(50) NOT NULL,
  PRIMARY KEY (objid)
) 
go 


alter table faasannotation_faas 
add constraint fk_faasannotationfaas_faasannotation foreign key(parent_objid)
references faasannotation (objid)
go

alter table faasannotation_faas 
add constraint fk_faasannotationfaas_faas foreign key(faas_objid)
references faas (objid)
go

create index ix_parent_objid on faasannotation_faas(parent_objid)
go

create index ix_faas_objid on faasannotation_faas(faas_objid)
go


create unique index ux_parent_faas on faasannotation_faas(parent_objid, faas_objid)
go

alter table faasannotation alter column faasid varchar(50) null
go



-- insert annotated faas
insert into faasannotation_faas(
  objid, 
  parent_objid,
  faas_objid 
)
select 
  objid, 
  objid as parent_objid,
  faasid as faas_objid 
from faasannotation
go
  


/*============================================
*
*  LEDGER FAAS FACTS
*
=============================================*/
INSERT INTO sys_var ([name], [value], [description], [datatype], [category]) 
VALUES ('rptledger_rule_include_ledger_faases', '0', 'Include Ledger FAASes as rule facts', 'checkbox', 'LANDTAX')
go

INSERT INTO sys_var ([name], [value], [description], [datatype], [category]) 
VALUES ('rptledger_post_ledgerfaas_by_actualuse', '0', 'Post by Ledger FAAS by actual use', 'checkbox', 'LANDTAX')
go 


