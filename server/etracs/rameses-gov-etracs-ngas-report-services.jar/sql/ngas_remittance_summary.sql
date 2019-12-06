[getFunds]
select fund.objid, fund.code, fund.title 
from remittance r 
	inner join cashreceipt c on c.remittanceid = r.objid 
	inner join cashreceiptitem ci on ci.receiptid = c.objid 
	inner join fund on fund.objid = ci.item_fund_objid 
where r.objid = $P{remittanceid} 
group by fund.objid, fund.code, fund.title


[getReport]
select t1.*, a.code, a.title  
from ( 
	SELECT 
		c.receiptdate, c.receiptno, c.paidby, ci.item_objid, 
		CASE WHEN v.objid IS NULL THEN ci.amount ELSE 0.0 END AS amount,
		CASE WHEN v.objid IS NULL THEN 0 ELSE 1 END AS voided, 
		fund.objid as fund_objid, fund.title as fund_title 
	FROM remittance r 
		inner join cashreceipt c on c.remittanceid = r.objid 
		inner join cashreceiptitem ci on ci.receiptid = c.objid 
		inner join fund on fund.objid = ci.item_fund_objid 
		left join cashreceipt_void v on v.receiptid = c.objid 
	where r.objid = $P{remittanceid} ${filters}
)t1 
	left join ( 
		select a.objid, a.code, a.title, m.itemid 
		from account a, vw_account_mapping m 
		where a.maingroupid = $P{maingroupid} 
			and m.acctid = a.objid 
	)a on a.itemid = t1.item_objid 
order by t1.receiptno, a.code 
