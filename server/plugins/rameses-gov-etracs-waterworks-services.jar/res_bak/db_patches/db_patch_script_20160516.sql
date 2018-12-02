
ALTER TABLE `waterworks_material`
	ADD COLUMN `installmentprice` decimal(16,2) NULL,
	ADD COLUMN `unitcost`  decimal(16,2) NULL
; 

ALTER TABLE `waterworks_stubout_node`
	ADD COLUMN `applicationid`  varchar(50) NULL
;
ALTER TABLE `waterworks_stubout_node`
	ADD INDEX `ix_applicationid` (`applicationid`)
; 
ALTER TABLE `waterworks_stubout_node`
	ADD INDEX `ix_indexno` (`indexno`) 
; 

ALTER TABLE `waterworks_account` DROP COLUMN `stuboutid`;
ALTER TABLE `waterworks_account` ADD COLUMN `stuboutnodeid` varchar(50) NULL;
ALTER TABLE `waterworks_account` ADD INDEX `ix_stuboutnodeid` (`stuboutnodeid`); 

update waterworks_account wa, waterworks_stubout_node wsn set 
	wa.stuboutnodeid = wsn.objid 
where wa.objid=wsn.acctid 
	and wa.stuboutnodeid is null  
;

ALTER TABLE `waterworks_application` DROP COLUMN `stuboutindex`;
ALTER TABLE `waterworks_application` DROP COLUMN `stuboutid`;
ALTER TABLE `waterworks_application` ADD `stuboutnodeid` varchar(50) NULL;
ALTER TABLE `waterworks_application` ADD INDEX ix_classificationid (classificationid);
ALTER TABLE `waterworks_application` ADD INDEX ix_appno (appno);
ALTER TABLE `waterworks_application` ADD INDEX ix_acctid (acctid);
ALTER TABLE `waterworks_application` ADD INDEX ix_owner_objid (owner_objid);
ALTER TABLE `waterworks_application` ADD INDEX ix_meterid (meterid);
ALTER TABLE `waterworks_application` ADD INDEX ix_installer_objid (installer_objid);
ALTER TABLE `waterworks_application` ADD INDEX ix_dtinstalled (dtinstalled);
ALTER TABLE `waterworks_application` ADD INDEX ix_stuboutnodeid (stuboutnodeid);

ALTER TABLE `waterworks_application` 
	ADD CONSTRAINT `fk_waterworks_application_stuboutnodeid` 
	FOREIGN KEY (`stuboutnodeid`) REFERENCES `waterworks_stubout_node` (`objid`)
;
ALTER TABLE `waterworks_application` 
	ADD CONSTRAINT `fk_waterworks_application_meterid` 
	FOREIGN KEY (`meterid`) REFERENCES `waterworks_meter` (`objid`)
;


drop view if exists vw_waterworks_account_ledger;
create view vw_waterworks_account_ledger 
as 
select
	waf.objid, wa.acctid AS parentid, waf.txntype,
	waf.duedate, waf.item_objid, waf.item_code, waf.item_title,
	waf.remarks, waf.amount, waf.amtpaid, waf.installmentid 
from waterworks_application_fee waf 
	inner join waterworks_application wa ON wa.objid=waf.parentid
where ((waf.amount - waf.amtpaid) > 0) 

union 

select
	wal.objid, wal.parentid, wal.txntype,
	wal.duedate, wal.item_objid, wal.item_code, wal.item_title,
	wal.remarks, wal.amount, wal.amtpaid, wal.installmentid 
from waterworks_account_ledger wal 
where ((wal.amount - wal.amtpaid) > 0) 
;


drop view if exists vw_waterworks_account_notin_stubout;  
create view vw_waterworks_account_notin_stubout 
as 
select 
	wa.objid AS objid, wa.acctno AS acctno, wa.acctname AS acctname, 
	wa.owner_name AS owner_name, wa.address_text AS address_text, 
	wm.serialno AS meter_serialno 
from waterworks_account wa 
	inner join waterworks_meter wm on wa.meterid = wm.objid 
where wa.stuboutnodeid not in (
	select objid from waterworks_stubout_node 
	where objid = wa.stuboutnodeid 
) 
;  

drop view if exists vw_waterworks_meter_account;  
create view vw_waterworks_meter_account 
as 
select
  wm.objid AS objid, wm.serialno AS serialno, wm.brand AS brand,
  wm.capacity AS capacity, wms.title AS size_title,
  wa.objid AS account_objid, wa.acctname AS account_acctname,
  wa.address_text AS account_address_text
from waterworks_meter wm
	inner join waterworks_metersize wms on wms.objid = wm.sizeid 
   	left join waterworks_account wa on wa.meterid = wm.objid 
order by wm.serialno 
;

drop view if exists vw_waterworks_meter_application;  
create view vw_waterworks_meter_application 
as 
select
  wm.objid AS objid, wm.serialno AS serialno,
  wm.brand AS brand, wm.capacity AS capacity,
  wms.title AS size_title, 
  wp.objid AS application_objid,
  wp.acctname AS application_acctname, 
	wm.currentacctid 
from waterworks_meter wm
	inner join waterworks_metersize wms on wms.objid = wm.sizeid 
	left join waterworks_application wp on wp.meterid = wm.objid 
order by wm.serialno 
;

drop view if exists vw_waterworks_sector_zone;  
create view vw_waterworks_sector_zone 
as 
select 
	wsz.objid AS objid, wsz.code AS code, wsz.description AS description, 
	ws.objid AS sector_objid, ws.code AS sector_code 
from waterworks_sector_zone wsz 
	inner join waterworks_sector ws on ws.objid = wsz.sectorid 
order by ws.code,wsz.code 
;

drop view if exists vw_waterworks_stubout;  
create view vw_waterworks_stubout 
as 
select 
	wst.objid, wst.code, wst.description, wsz.objid AS zone_objid,
	wsz.code AS zone_code, wsz.description AS zone_description,
	ws.objid AS sector_objid, ws.code AS sector_code, 
	wsr.objid AS reader_objid, wsr.name AS reader_name,
	wsr.assignee_objid, wsr.assignee_name  
from waterworks_stubout wst 
	inner join waterworks_sector_zone wsz on wst.zoneid = wsz.objid 
	inner join waterworks_sector ws on wsz.sectorid = ws.objid 
	inner join waterworks_sector_reader wsr on wsz.readerid = wsr.objid 
;

drop view if exists vw_waterworks_stubout_account;  
create view vw_waterworks_stubout_account 
as 
select 
	wa.objid, wsn.stuboutid, wsn.indexno as stuboutindex, 
	wa.acctno AS account_acctno, wa.acctname AS account_acctname, 
	wa.address_text AS account_address_text,
	wm.serialno AS account_meter_serialno 
from waterworks_account wa 
	inner join waterworks_stubout_node wsn on wa.stuboutnodeid=wsn.objid 
	inner join waterworks_meter wm on wm.objid = wa.meterid 
;

drop view if exists vw_waterworks_stubout_node;  
create view vw_waterworks_stubout_node 
as 
SELECT 
	sn.objid,
	sn.stuboutid,
	sn.indexno, 
	wa.objid AS account_objid,
	wa.acctno AS account_acctno,
	wa.acctname AS account_acctname,
	wa.currentreading AS account_currentreading,
	wm.serialno AS account_meter_serialno
FROM waterworks_stubout_node sn
	LEFT JOIN waterworks_account wa ON sn.acctid=wa.objid
	LEFT JOIN waterworks_meter wm ON wa.meterid=wm.objid 
;

