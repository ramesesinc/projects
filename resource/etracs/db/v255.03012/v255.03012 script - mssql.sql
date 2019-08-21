/* 255-03012 */

/*=====================================
* LEDGER TAG
=====================================*/
CREATE TABLE rptledger_tag (
  objid varchar(50) NOT NULL,
  parent_objid varchar(50) NOT NULL,
  tag varchar(255) NOT NULL,
  PRIMARY KEY (objid)
)
go 

create UNIQUE index ux_rptledger_tag on rptledger_tag(parent_objid,tag)
go 

create index FK_rptledgertag_rptledger on rptledger_tag(parent_objid)
go 
  
alter table rptledger_tag 
    add CONSTRAINT FK_rptledgertag_rptledger 
    FOREIGN KEY (parent_objid) REFERENCES rptledger (objid)
go     

