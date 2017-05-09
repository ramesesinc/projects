/* 254032-030145*/

drop table batchgrerror
go 
alter table batchgr_error add barangayid varchar(50)
go 
alter table batchgr_error add barangay varchar(100)
go 
alter table batchgr_error add tdno varchar(30)
go 


create view vw_batchgr_error 
as 
select 
	err.*,
	f.fullpin as fullpin, 
	rp.pin as pin,
	o.name as lguname 
from batchgr_error err 
inner join faas f on err.objid = f.objid 
inner join realproperty rp on f.realpropertyid = rp.objid 
inner join barangay b on rp.barangayid = b.objid 
inner join sys_org o on f.lguid = o.objid 
go 


CREATE TABLE rptledger_forprocess (
  objid varchar(255) NOT NULL,
  PRIMARY KEY (objid)
)
go 


alter table rptledgeritem drop column basicintpaid
go 
alter table rptledgeritem drop column basicdisctaken
go 
alter table rptledgeritem drop column basicidledisctaken
go 
alter table rptledgeritem drop column basicidleintpaid
go 
alter table rptledgeritem drop column sefintpaid
go 
alter table rptledgeritem drop column sefdisctaken
go 



alter table rptledgeritem_qtrly drop column basicintpaid
go 
alter table rptledgeritem_qtrly drop column basicdisctaken
go 
alter table rptledgeritem_qtrly drop column basicidledisctaken
go 
alter table rptledgeritem_qtrly drop column basicidleintpaid
go 
alter table rptledgeritem_qtrly drop column sefintpaid
go 
alter table rptledgeritem_qtrly drop column sefdisctaken
go 


drop table rptledgeritem_qtrly_partial
go 

create index ix_dtapproved on faas(dtapproved)
go 


alter table faas_restriction add rpumaster_objid varchar(50)
go 

update fr set 
	fr.rpumaster_objid = r.rpumasterid
from faas_restriction fr, faas f, rpu r	
where fr.parent_objid = f.objid 
and f.rpuid = r.objid 
and fr.rpumaster_objid is null
go 



alter table cancelledfaas add cancelledbytdnos varchar(500)
go 
alter table cancelledfaas add cancelledbypins varchar(500)
go 



alter table subdivision_cancelledimprovement add cancelledbytdnos varchar(500)
go 
alter table subdivision_cancelledimprovement add cancelledbypins varchar(500)
go 

