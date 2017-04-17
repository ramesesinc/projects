#254032-03014a

CREATE TABLE rptledgeritem_qtrly_partial (
  objid varchar(50) NOT NULL,
  rptledgerid varchar(50) NOT NULL,
  year integer NOT NULL,
  qtr integer NOT NULL,
  basicpaid decimal(16,2) NOT NULL,
  basicintpaid decimal(16,2) NOT NULL,
  basicdisctaken decimal(16,2) NOT NULL,
  basicidlepaid decimal(16,2) NOT NULL,
  basicidledisctaken decimal(16,2) NOT NULL,
  sefpaid decimal(16,2) NOT NULL,
  sefintpaid decimal(16,2) NOT NULL,
  sefdisctaken decimal(16,2) NOT NULL,
  firecodepaid decimal(16,2) NOT NULL,
  PRIMARY KEY (objid)
)
go 

create index FK_rptledgeritemqtrlypartial_rptledger on rptledgeritem_qtrly_partial(rptledgerid)
go 
  
alter table rptledgeritem_qtrly_partial
add CONSTRAINT FK_rptledgeritemqtrlypartial_rptledger 
FOREIGN KEY (rptledgerid) REFERENCES rptledger (objid)
go 



alter table faas drop column taxpayer_name
go 
alter table faas drop column taxpayer_address
go 