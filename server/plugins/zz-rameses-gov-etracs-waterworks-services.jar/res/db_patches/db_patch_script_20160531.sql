create index ix_meterid on waterworks_account (meterid);
create index ix_metersizeid on waterworks_account (metersizeid);
create index ix_sectorid on waterworks_account (sectorid);
create index ix_zoneid on waterworks_account (zoneid);
create index ix_stuboutid on waterworks_account (stuboutid);
create index ix_stuboutnodeid on waterworks_account (stuboutnodeid);

create table tmp_account_stubout 
select 
	wa.objid as acctid, wa.stuboutnodeid, 
	wsn.stuboutid, ws.zoneid, wsz.sectorid 
from waterworks_account wa 
	inner join waterworks_stubout_node wsn on wa.stuboutnodeid=wsn.objid 
	inner join waterworks_stubout ws on wsn.stuboutid=ws.objid 
	inner join waterworks_sector_zone wsz on ws.zoneid = wsz.objid 
where wa.sectorid is null 
; 

create table tmp_account_meter  
select 
	wa.objid as acctid, wa.meterid, wm.sizeid as metersizeid 
from waterworks_account wa 
	inner join waterworks_meter wm on wa.meterid=wm.objid 
where wa.metersizeid is null 
; 

update waterworks_account a, tmp_account_meter tmp set 
	a.metersizeid = tmp.metersizeid 
where a.objid=tmp.acctid 
;
update waterworks_account a, tmp_account_stubout tmp set 
	a.sectorid = tmp.sectorid, 
	a.zoneid = tmp.zoneid, 
	a.stuboutid = tmp.stuboutid 
where a.objid=tmp.acctid 
;

drop table tmp_account_stubout;
drop table tmp_account_meter; 
