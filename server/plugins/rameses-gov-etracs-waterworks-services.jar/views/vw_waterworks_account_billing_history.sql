drop view if exists vw_waterworks_account_billing_history
;
create view vw_waterworks_account_billing_history as 
select 
	b.acctid, a.acctno, bs.year, bs.month, ((bs.year * 12) + bs.month) as periodindexno, 
	case when bb.readingdate is null then bs.readingdate else bb.readingdate end as readingdate, 
	c.prevreading, c.reading, c.volume, c.amount, b.arrears, b.surcharge, b.interest, b.credits, 
	b.consumptionid, b.batchid, b.objid as billid, bb.scheduleid, bb.zoneid, c.meterid  
from waterworks_account a 
	inner join waterworks_billing b on b.acctid = a.objid 
	inner join waterworks_batch_billing bb on bb.objid = b.batchid 
	inner join waterworks_consumption c on c.objid = b.consumptionid 
	inner join waterworks_billing_schedule bs on bs.objid = bb.scheduleid 
;