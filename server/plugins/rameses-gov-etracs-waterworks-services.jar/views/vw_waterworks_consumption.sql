drop view if exists vw_waterworks_consumption
;
create view vw_waterworks_consumption as 
select wc.*, 
	(wc.amount - wc.amtpaid - wc.discount) as balance, 
	s.year, s.month, s.fromperiod, s.toperiod, 
	s.readingdate, s.readingduedate, s.billingduedate, 
	s.discdate, s.duedate, sn.indexno, a.acctno, a.acctname, 
	((s.year * 12) + s.month) as periodindexno, 
	bb.objid as batch_objid, bb.readingdate as batch_readingdate, 
	b.objid as bill_objid, b.otherfees as bill_otherfees, 
	b.arrears as bill_arrears, b.credits as bill_credits, 
	b.surcharge as bill_surcharge, b.interest as bill_interest 
from waterworks_consumption wc 
	inner join waterworks_account a on a.objid = wc.acctid 
	inner join waterworks_stubout_node sn on sn.objid = a.stuboutnodeid 
	left join waterworks_billing_schedule s on s.objid = wc.scheduleid 
	left join waterworks_batch_billing bb on bb.objid = wc.batchid 
	left join waterworks_billing b on b.consumptionid = wc.objid 
;