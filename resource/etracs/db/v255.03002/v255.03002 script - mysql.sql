/* 255-03001 */
alter table rptcertification add properties text;


alter table rpttracking 
	add dtfiled datetime,
	add pin varchar(25),
	add taxpayer_objid varchar(50),
	add txntype_objid varchar(50),
	add releasedate datetime,
	add releasemode varchar(50),
	add receivedby varchar(255),
	add remarks varchar(255),
	add landcount int default 0,
	add bldgcount int default 0,
	add machcount int default 0,
	add planttreecount int default 0,
	add misccount int default 0
;
alter table rpttracking modify column filetype varchar(50) null
;
	

create index ix_rpttracking_receivedby on rpttracking(receivedby)
;
create index ix_rpttracking_remarks on rpttracking(remarks)
;
create index ix_rpttracking_pin on rpttracking(pin)
;

update rpttracking set remarks = msg
;

alter table rpttracking drop column msg
;	

	
alter table faas_signatory 
    add reviewer_objid varchar(50),
    add reviewer_name varchar(100),
    add reviewer_title varchar(75),
    add reviewer_dtsigned datetime,
    add reviewer_taskid varchar(50);

    