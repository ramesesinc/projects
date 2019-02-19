
drop view if exists vw_waterworks_account_payment
;
create view vw_waterworks_account_payment as 
select 
	wp.objid, wp.refid, wp.refno, wp.reftype, wp.refdate, 
	wp.amount, wp.txnmode, wp.voided, wp.remarks, 
	c.acctid, a.acctno, c.scheduleid, c.objid as consumptionid,  
	wpi.parentid as paymentid, wpi.objid as paymentitemid  
from waterworks_account a 
	inner join waterworks_consumption c on c.acctid = a.objid 
	inner join waterworks_payment_item wpi on (wpi.refid = c.objid and wpi.reftype='waterworks_consumption') 
	inner join waterworks_payment wp on wp.objid = wpi.parentid 
;