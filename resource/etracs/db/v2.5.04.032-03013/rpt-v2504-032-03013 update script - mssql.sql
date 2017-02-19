#254032-03013

create table memoranda_template
(
	objid varchar(50) primary key,
	code varchar(25) not null,
	template varchar(500) not null
)
go 


alter table rpu_assessment alter column classification_objid varchar(50)  null
go 
alter table rpu_assessment alter column actualuse_objid varchar(50)  null
go 