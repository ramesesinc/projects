/* 255-03001 */

alter table rptcertification add properties text
go 



alter table rpttracking add dtfiled datetime
go 
alter table rpttracking add taxpayer_objid varchar(50)
go 
alter table rpttracking add txntype_objid varchar(50)
go 
alter table rpttracking add releasedate datetime
go 
alter table rpttracking add releasemode varchar(50)
go 
alter table rpttracking add receivedby varchar(255)
go 
alter table rpttracking add remarks varchar(255)
go 
alter table rpttracking add landcount int default 0
go 
alter table rpttracking add bldgcount int default 0
go 
alter table rpttracking add machcount int default 0
go 
alter table rpttracking add planttreecount int default 0
go 
alter table rpttracking add misccount int default 0
go 
alter table rpttracking add pin varchar(25)
go 
alter table rpttracking alter column filetype varchar(50) null
go 
	

create index ix_rpttracking_receivedby on rpttracking(receivedby)
go 
create index ix_rpttracking_remarks on rpttracking(remarks)
go 
create index ix_rpttracking_pin on rpttracking(pin)
go 

update rpttracking set remarks = msg
go 

alter table rpttracking drop column msg
go 


alter table faas_signatory add reviewer_objid varchar(50)
go 
alter table faas_signatory add reviewer_name varchar(100)
go 
alter table faas_signatory add reviewer_title varchar(75)
go 
alter table faas_signatory add reviewer_dtsigned datetime
go 
alter table faas_signatory add reviewer_taskid varchar(50)
go 