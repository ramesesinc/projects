/*============================================
*
* SUBLEDGER SUPPORT 
*
*=============================================*/

create table rptledger_subledger
(
	objid nvarchar(50) not null primary key,
	parent_objid nvarchar(50) not null, 
	subacctno nvarchar(10) not null,
	constraint ux_parentid_subacctno unique (parent_objid, subacctno)
)
go 


alter table rptledger_subledger 
add constraint FK_rptledger_subledger_rptldger foreign key(parent_objid)
references rptledger(objid)
go




/*============================================
*
* BLDG_LAND (bldg rpu located in multiple lands)
*
*=============================================*/
CREATE TABLE bldgrpu_land (
  objid varchar(50) NOT NULL,
  bldgrpuid nvarchar(50) NOT NULL,
  landfaas_objid nvarchar(50) NOT NULL,
  PRIMARY KEY  (objid),
  CONSTRAINT ux_bldgrpu_land_bldgrpu_landfaas UNIQUE(bldgrpuid,landfaas_objid)
)
go

create index ix_bldgrpu_land_bldgrpuid on bldgrpu_land(bldgrpuid)
go 
create index ix_bldgrpu_land_landfaasid on bldgrpu_land(landfaas_objid)
go 

ALTER TABLE bldgrpu_land 
	ADD CONSTRAINT FK_bldgrpu_land_bldgrpu 
	FOREIGN KEY (bldgrpuid) 
	REFERENCES bldgrpu (objid)
go 

ALTER TABLE bldgrpu_land 
	ADD CONSTRAINT FK_bldgrpu_land_landfaas 
	FOREIGN KEY (landfaas_objid) 
	REFERENCES faas (objid)
go 

	