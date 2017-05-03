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
	drop column basicintpaid,
	drop column basicdisctaken,
	drop column basicidledisctaken,
	drop column basicidleintpaid,
	drop column sefintpaid,
	drop column sefdisctaken;

alter table rptledgeritem_qtrly
	drop column basicintpaid,
	drop column basicdisctaken,
	drop column basicidledisctaken,
	drop column basicidleintpaid,
	drop column sefintpaid,
	drop column sefdisctaken;





drop table rptledgeritem_qtrly_partial;

