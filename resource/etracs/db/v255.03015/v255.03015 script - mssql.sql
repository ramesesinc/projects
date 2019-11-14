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
  