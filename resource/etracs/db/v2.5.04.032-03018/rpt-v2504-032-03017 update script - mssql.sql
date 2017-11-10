/* 254032-03018*/

alter table faasbacktax alter column tdno varchar(25) null
go 



alter table landdetail alter column subclass_objid varchar(50) null
go 
alter table landdetail alter column specificclass_objid varchar(50) null
go 
alter table landdetail alter column actualuse_objid varchar(50) null
go 
alter table landdetail alter column landspecificclass_objid varchar(50) null
go 




/* RYSETTING ORDINANCE INFO */
alter table landrysetting add ordinanceno varchar(25)
go 
alter table landrysetting add ordinancedate date
go 


alter table bldgrysetting add ordinanceno varchar(25)
go 
alter table bldgrysetting add ordinancedate date
go 


alter table machrysetting add ordinanceno varchar(25)
go 
alter table machrysetting add ordinancedate date
go 


alter table miscrysetting add ordinanceno varchar(25)
go 
alter table miscrysetting add ordinancedate date
go 


alter table planttreerysetting add ordinanceno varchar(25)
go 
alter table planttreerysetting add ordinancedate date
go 


delete from sys_var where name in ('gr_ordinance_date','gr_ordinance_no')
go





drop TABLE bldgrpu_land
go 

create table bldgrpu_land (
  objid varchar(50) not null,
  rpu_objid varchar(50) not null,
  landfaas_objid varchar(50)not null,
  landrpumaster_objid varchar(50),
  primary key (objid)
)
go 


create index ix_bldgrpu_land_bldgrpuid on bldgrpu_land(rpu_objid)
go 
create index ix_bldgrpu_land_landfaasid on bldgrpu_land(landfaas_objid)
go 
create index ix_bldgrpu_land_landrpumasterid on bldgrpu_land(landrpumaster_objid)
go 


alter table bldgrpu_land 
	add constraint fk_bldgrpu_land_bldgrpu foreign key (rpu_objid) 
	references bldgrpu (objid)
go 	

alter table bldgrpu_land 
	add constraint fk_bldgrpu_land_rpumaster foreign key (landrpumaster_objid) 
	references rpumaster objid)
go 

alter table bldgrpu_land 
	add constraint fk_bldgrpu_land_landfaas foreign key (landfaas_objid) 
	references faas(objid)
go 




create table batchgr_log (
	objid varchar(50) not null,
	primary key (objid)
)
go 



alter table bldgrpu_structuraltype alter column bldgkindbucc_objid nvarchar(50) null
go 



alter table bldgadditionalitem add idx int
go 

update bldgadditionalitem set idx = 0 where idx is null
go 





/*=================================================	
*
*  PROVINCE-MUNI LEDGER SYNCHRONIZATION SUPPORT 
*
====================================================*/
CREATE TABLE rptledger_remote (
  objid nvarchar(50) NOT NULL,
  remote_objid nvarchar(50) NOT NULL,
  createdby_name nvarchar(255) NOT NULL,
  createdby_title nvarchar(100) DEFAULT NULL,
  dtcreated datetime NOT NULL,
  PRIMARY KEY (objid)
)
go 

alter table rptledger_remote 
add CONSTRAINT FK_rptledgerremote_rptledger FOREIGN KEY (objid) REFERENCES rptledger (objid)
go 
