alter table waterworks_account add sectorid varchar(50) null;
alter table waterworks_account add zoneid varchar(50) null;
alter table waterworks_account add metersizeid varchar(50) null;
alter table waterworks_account add stuboutnodeid varchar(50) null;

create index ix_meterid on waterworks_account (meterid);
create index ix_metersizeid on waterworks_account (metersizeid);
create index ix_sectorid on waterworks_account (sectorid);
create index ix_zoneid on waterworks_account (zoneid);
create index ix_stuboutnodeid on waterworks_account (stuboutnodeid);

CREATE TABLE `waterworks_stubout_node` 
(
	`objid` varchar(50) NOT NULL,
	`stuboutid` varchar(50) DEFAULT NULL,
	`indexno` int(11) DEFAULT NULL,
	`acctid` varchar(50) DEFAULT NULL,   
	PRIMARY KEY (`objid`),
	UNIQUE KEY `uix_acctid` (`acctid`),
	KEY `ix_stuboutid` (`stuboutid`),
	CONSTRAINT `fk_stuboutnode_account` FOREIGN KEY (`acctid`) REFERENCES `waterworks_account` (`objid`),
	CONSTRAINT `fk_stuboutnode_stubout` FOREIGN KEY (`stuboutid`) REFERENCES `waterworks_stubout` (`objid`)
) 
ENGINE=InnoDB DEFAULT CHARSET=utf8
;

create table tmp_20160608_account  
select * from waterworks_account a where stuboutid not in (
	select objid from waterworks_stubout where objid=a.stuboutid 
); 
update waterworks_account a, tmp_20160608_account tmp set 
	a.stuboutid = null, 
	a.stuboutindex = null, 
	a.stuboutnodeid = null 
where a.objid=tmp.objid 
;

ALTER TABLE `waterworks_account` 
	ADD CONSTRAINT `fk_waterworks_account_metersize` 
	FOREIGN KEY (`metersizeid`) REFERENCES `waterworks_metersize` (`objid`);
alter table waterworks_account 
	add CONSTRAINT `fk_waterworks_account_sector` 
	FOREIGN KEY (`sectorid`) REFERENCES `waterworks_sector` (`objid`); 
alter table waterworks_account 
	add CONSTRAINT `fk_waterworks_account_sectorzone` 
	FOREIGN KEY (`zoneid`) REFERENCES `waterworks_sector_zone` (`objid`); 
alter table waterworks_account 
	add CONSTRAINT `fk_waterworks_account_stubout_node` 
	FOREIGN KEY (`stuboutnodeid`) REFERENCES `waterworks_stubout_node` (`objid`); 
alter table waterworks_account 
	add CONSTRAINT `fk_waterworks_account_stubout` 
	FOREIGN KEY (`stuboutid`) REFERENCES `waterworks_stubout` (`objid`); 


insert ignore into waterworks_stubout_node (
	objid, stuboutid, indexno, acctid 
) 
select 
	concat('STN-',wa.objid) as objid, 
	wa.stuboutid, wa.stuboutindex as indexno, 
	wa.objid as acctid 
from waterworks_account wa 
	inner join waterworks_stubout ws on wa.stuboutid=ws.objid 
where wa.stuboutid <> '-' 
;

update waterworks_account wa, waterworks_stubout_node wsn set 
	wa.stuboutnodeid = wsn.objid 
where wa.stuboutid=wsn.stuboutid 
	and wa.stuboutindex=wsn.indexno 
	and wa.objid = wsn.acctid 
	and wa.stuboutid <> '-' 
;

alter table waterworks_account drop column stuboutindex
;

create table tmp_20160608_account_stubout 
select 
	wa.objid as acctid, wa.stuboutnodeid, 
	wsn.stuboutid, ws.zoneid, wsz.sectorid 
from waterworks_account wa 
	inner join waterworks_stubout_node wsn on wa.stuboutnodeid=wsn.objid 
	inner join waterworks_stubout ws on wsn.stuboutid=ws.objid 
	inner join waterworks_sector_zone wsz on ws.zoneid = wsz.objid 
where wa.sectorid is null 
; 

create table tmp_20160608_account_meter  
select 
	wa.objid as acctid, wa.meterid, wm.sizeid as metersizeid 
from waterworks_account wa 
	inner join waterworks_meter wm on wa.meterid=wm.objid 
where wa.metersizeid is null 
; 

update waterworks_account a, tmp_20160608_account_meter tmp set 
	a.metersizeid = tmp.metersizeid 
where a.objid=tmp.acctid 
;
update waterworks_account a, tmp_20160608_account_stubout tmp set 
	a.sectorid = tmp.sectorid, 
	a.zoneid = tmp.zoneid, 
	a.stuboutid = tmp.stuboutid 
where a.objid=tmp.acctid 
;

drop table tmp_20160608_account_stubout;
drop table tmp_20160608_account_meter; 

alter table waterworks_meter 
	add stocktype varchar(50) null; 

alter table waterworks_billing_cycle 
	add billdate date null, 
	add oldid varchar(50) null,
	add newid varchar(50) null;  

update waterworks_billing_cycle set 
	oldid=objid, 
	newid = concat(sectorid,'-',year,'-',case when month < 10 then concat('0',month) else month end) 
;
set foreign_key_checks=0
; 
update waterworks_billing_cycle set objid=newid
;
update waterworks_account a, waterworks_billing_cycle bc set 
	a.billingcycleid = bc.newid 
where 
	a.billingcycleid = bc.oldid  
; 
set foreign_key_checks=1
;


alter table waterworks_billing_cycle drop column newid
; 
update waterworks_billing_cycle set 
	billdate=date_add(readingdate,interval 1 day) 
where 
	billdate is null
; 


alter table waterworks_account_consumption 
	add rate decimal(16,2) null, 
	add ledgerid varchar(50) null, 
	add billingcycleid varchar(50) null, 
	add index ix_ledgerid (ledgerid),
	add index ix_billingcycleid (billingcycleid);

alter table waterworks_account_consumption 
	add CONSTRAINT `fk_waterworks_account_consumption_billingcycle` 
	FOREIGN KEY (`billingcycleid`) REFERENCES `waterworks_billing_cycle` (`objid`); 

alter table waterworks_account_consumption 
	drop index uix_month_year; 
create unique index uix_account_billingcycle 
	on waterworks_account_consumption (acctid, billingcycleid); 


alter table waterworks_account_ledger 
	drop index uix_parentid_itemid_duedate, 
	drop column duedate;
alter table waterworks_account_ledger 
	add column billingcycleid varchar(50) null, 
	add index ix_billingcycleid (billingcycleid); 
create unique index uix_parent_item_billingcycle 
	on waterworks_account_ledger (parentid, item_objid, billingcycleid);


//
// Run the following scripts from web browser: 
//   http://localhost:8070/osiris3/json/etracs25/WaterworksBillingCycleService.generateByYear?year=2015&sectorid=C 
//   http://localhost:8070/osiris3/json/etracs25/WaterworksBillingCycleService.generateByYear?year=2016&sectorid=A 
//   http://localhost:8070/osiris3/json/etracs25/WaterworksBillingCycleService.generateByYear?year=2016&sectorid=B 
//   http://localhost:8070/osiris3/json/etracs25/WaterworksBillingCycleService.generateByYear?year=2016&sectorid=C 
//


update waterworks_account_consumption wac, waterworks_billing_cycle wbc, waterworks_account wa set 
	wac.billingcycleid = wbc.objid 
where wac.acctid = wa.objid 
	and wbc.sectorid = wa.sectorid 
	and wbc.`year` = wac.`year` 
	and wbc.`month` = wac.`month` 
;
alter table waterworks_account_consumption 
	drop column dtreading, 
	drop column `year`, 
	drop column `month`; 

alter table waterworks_account 
	add lastbilldate date null, 
	add index ix_lastbilldate (lastbilldate);


alter table waterworks_sector_reader 
	drop column name,  
	add index ix_assignee_objid (assignee_objid); 
create unique index uix_sector_reader_assignee 
	on waterworks_sector_reader (sectorid, assignee_objid);

alter table waterworks_payment_item 
	add index ix_parentid ( parentid ),
	add index ix_txnrefid ( txnrefid ),
	add index ix_item_objid ( item_objid );

alter table waterworks_payment 
	add txnmode varchar(50) null;

alter table waterworks_material 
  add installmentprice decimal(16,2) NULL,
  add unitcost decimal(16,2) NULL; 

alter table waterworks_installment 
	drop column amortization, 
	add enddate date null, 
	add acctid varchar(50) null, 
	add applicationid varchar(50) null, 
	add installmentamount decimal(16,2) null, 
	add lastbilldate date null, 
	add amtbilled decimal(16,2); 

alter table waterworks_installment 
	add index ix_acctid ( acctid ),
	add index ix_applicationid ( applicationid ),
	add index ix_startdate ( startdate ), 
	add index ix_enddate ( enddate ); 

update waterworks_installment set 
	enddate = date_add(startdate, interval term month)
;

update waterworks_account_consumption wac, waterworks_account_ledger wal set 
	wal.billingcycleid = wac.billingcycleid 
where wac.ledgerid is not null 
	and wac.ledgerid=wal.objid 
;

alter table waterworks_stubout_node 
	add applicationid varchar(50) null, 
	add index ix_applicationid (applicationid);

alter table waterworks_stubout_node 
	add index ix_acctid (acctid);

alter table waterworks_installment 
	drop column amount, 
	drop column amtpaid;

update waterworks_account_consumption wac, waterworks_account_ledger wal set 
	wal.billingcycleid = wac.billingcycleid 
where wac.ledgerid is not null 
	and wac.ledgerid=wal.objid;

