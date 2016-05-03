drop view if exists vw_waterworks_account_ledger;
create view vw_waterworks_account_ledger as 
select
	waterworks_application_fee.objid         AS objid,
	wa.acctid  AS parentid,
	waterworks_application_fee.txntype       AS txntype,
	waterworks_application_fee.duedate       AS duedate,
	waterworks_application_fee.item_objid    AS item_objid,
	waterworks_application_fee.item_code     AS item_code,
	waterworks_application_fee.item_title    AS item_title,
	waterworks_application_fee.remarks       AS remarks,
	waterworks_application_fee.amount        AS amount,
	waterworks_application_fee.amtpaid       AS amtpaid,
	waterworks_application_fee.installmentid AS installmentid
from waterworks_application_fee
	inner join waterworks_application wa ON wa.objid=waterworks_application_fee.parentid
where ((waterworks_application_fee.amount - waterworks_application_fee.amtpaid) > 0)
union 
select
	waterworks_account_ledger.objid           AS objid,
	waterworks_account_ledger.parentid        AS parentid,
	waterworks_account_ledger.txntype         AS txntype,
	waterworks_account_ledger.duedate         AS duedate,
	waterworks_account_ledger.item_objid      AS item_objid,
	waterworks_account_ledger.item_code       AS item_code,
	waterworks_account_ledger.item_title      AS item_title,
	waterworks_account_ledger.remarks         AS remarks,
	waterworks_account_ledger.amount          AS amount,
	waterworks_account_ledger.amtpaid         AS amtpaid,
	waterworks_account_ledger.installmentid   AS installmentid
from waterworks_account_ledger
where ((waterworks_account_ledger.amount - waterworks_account_ledger.amtpaid) > 0) 
;

drop view if exists vw_waterworks_account_notin_stubout;
create view vw_waterworks_account_notin_stubout as 
select 
	wa.objid AS objid, wa.acctno AS acctno, wa.acctname AS acctname, 
	wa.owner_name AS owner_name, wa.address_text AS address_text, 
	wm.serialno AS meter_serialno 
from waterworks_account wa 
	inner join waterworks_meter wm on wa.meterid = wm.objid 
where wa.stuboutid not in (
	select objid from waterworks_stubout 
	where objid = wa.stuboutid 
)
; 

drop view if exists vw_waterworks_meter_account;
create view vw_waterworks_meter_account as 
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
create view vw_waterworks_meter_application as 
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
create view vw_waterworks_sector_zone as 
select 
	wsz.objid AS objid, wsz.code AS code, wsz.description AS description, 
	ws.objid AS sector_objid, ws.code AS sector_code 
from waterworks_sector_zone wsz 
	inner join waterworks_sector ws on ws.objid = wsz.sectorid 
order by ws.code,wsz.code 
;

drop view if exists vw_waterworks_stubout; 
create view vw_waterworks_stubout as 
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
create view vw_waterworks_stubout_account as 
select 
	wa.objid, wa.stuboutid, 
	wa.acctno AS account_acctno, wa.acctname AS account_acctname, 
	wa.address_text AS account_address_text,
	wm.serialno AS account_meter_serialno 
from waterworks_stubout ws 
	inner join waterworks_account wa on wa.stuboutid = ws.objid 
	inner join waterworks_meter wm on wm.objid = wa.meterid  
;
