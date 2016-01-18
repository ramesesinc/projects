CREATE TABLE misccollectiontype(
	objid varchar(50) NOT NULL,
	fund_objid varchar(50) NULL,
	primary key (objid) 
)
go

alter table afserial_control 
	add  fund_objid varchar(50) null
	
go

alter table afserial_control 	
	add  fund_title varchar(200) null 
	
go
