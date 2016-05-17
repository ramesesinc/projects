
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

ALTER TABLE `waterworks_account` DROP COLUMN `stuboutid`;
ALTER TABLE `waterworks_account` ADD COLUMN `stuboutnodeid` varchar(50) NULL;
ALTER TABLE `waterworks_account` ADD INDEX `ix_stuboutnodeid` (`stuboutnodeid`); 

update waterworks_account wa, waterworks_stubout_node wsn set 
	wa.stuboutnodeid = wsn.objid 
where wa.objid=wsn.acctid 
	and wa.stuboutnodeid is null  
;

drop view if exists vw_waterworks_stubout_account
;  
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


