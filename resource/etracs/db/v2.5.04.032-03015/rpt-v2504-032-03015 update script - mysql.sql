/* 254032-03015 */


drop table batchgrerror;
alter table batchgr_error add barangayid varchar(50);
alter table batchgr_error add barangay varchar(100);
alter table batchgr_error add tdno varchar(30);


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
inner join sys_org o on f.lguid = o.objid;


CREATE TABLE `rptledger_forprocess` (
  `objid` varchar(255) NOT NULL,
  PRIMARY KEY (`objid`)
) ;




alter table rptledgeritem
	modify column basicintpaid decimal(16,2) null,
	modify column basicdisctaken decimal(16,2) null,
	modify column basicidledisctaken decimal(16,2) null,
	modify column basicidleintpaid decimal(16,2) null,
	modify column sefintpaid decimal(16,2) null,
	modify column sefdisctaken decimal(16,2) null;

alter table rptledgeritem_qtrly
	modify column basicintpaid decimal(16,2) null,
	modify column basicdisctaken decimal(16,2) null,
	modify column basicidledisctaken decimal(16,2) null,
	modify column basicidleintpaid decimal(16,2) null,
	modify column sefintpaid decimal(16,2) null,
	modify column sefdisctaken decimal(16,2) null;





drop table rptledgeritem_qtrly_partial;


create index ix_dtapproved on faas(dtapproved);


alter table faas_restriction add rpumaster_objid varchar(50); 


update faas_restriction fr, faas f, rpu r set 
	fr.rpumaster_objid = r.rpumasterid
where fr.parent_objid = f.objid 
and f.rpuid = r.objid 
and fr.rpumaster_objid is null;


alter table cancelledfaas add cancelledbytdnos varchar(500);
alter table cancelledfaas add cancelledbypins varchar(500);


alter table subdivision_cancelledimprovement add cancelledbytdnos varchar(500);
alter table subdivision_cancelledimprovement add cancelledbypins varchar(500);



