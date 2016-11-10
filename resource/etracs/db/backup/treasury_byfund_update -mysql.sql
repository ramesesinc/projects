CREATE TABLE misccollectiontype(
	objid varchar(50) NOT NULL,
	fund_objid varchar(50) NULL,
	primary key (objid) 
);

alter table afserial_control 
	add column fund_objid varchar(50) null,
	add column fund_title varchar(200) null ;
