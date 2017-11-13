[getFundSummaries]
select 
	ia.fund_objid, ia.fund_code, ia.fund_title, ia.fund_title as fund_name,
	sum(poi.amount) as amount 
from paymentorder_item poi  
inner join itemaccount ia on poi.item_objid = ia.objid 
where poi.parent_objid = $P{txnid}
group by ia.fund_objid, ia.fund_code, ia.fund_title



[getPaymentOrderItems]
select 
	poi.*, 
	ia.fund_objid as item_fund_objid, 
	ia.fund_code as item_fund_code, 
	ia.fund_title as item_fund_title 
from paymentorder_item poi  
inner join itemaccount ia on poi.item_objid = ia.objid 
where poi.parent_objid = $P{txnid}
