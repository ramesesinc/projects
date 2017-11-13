[getFundSummaries]
select 
	ia.fund_objid, ia.fund_code, ia.fund_title, ia.fund_title as fund_name,
	sum(cri.amount) as amount 
from cashreceiptitem cri 
inner join itemaccount ia on cri.item_objid = ia.objid 
where cri.receiptid = $P{objid}
group by ia.fund_objid, ia.fund_code, ia.fund_title
