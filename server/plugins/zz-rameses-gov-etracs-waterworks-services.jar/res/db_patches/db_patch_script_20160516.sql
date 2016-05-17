
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

ALTER TABLE `waterworks_account` 
	DROP COLUMN stuboutnodeid
;


drop view if exists vw_waterworks_stubout_account;  
create view vw_waterworks_stubout_account 
select 
	wa.objid, wsn.stuboutid, wsn.indexno as stuboutindex, 
	wa.acctno AS account_acctno, wa.acctname AS account_acctname, 
	wa.address_text AS account_address_text,
	wm.serialno AS account_meter_serialno 
from waterworks_account wa 
	inner join waterworks_stubout_node wsn on (wa.objid=wsn.acctid and wa.stuboutid=wsn.stuboutid) 
	inner join waterworks_meter wm on wm.objid = wa.meterid 
;

