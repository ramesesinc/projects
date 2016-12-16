[getList]
select 
	l.objid, l.dtposted, l.txnno, l.liquidatingofficer_name, 
	l.totalcash, l.totalnoncash, l.amount, l.state 
from liquidation l 
where YEAR(l.dtposted)=2016 ${filter} 
order by l.dtposted 

[getUndepositedLiquidations]
select 
	l.liquidatingofficer_name, l.txnno, l.dtposted, 
	lcf.fund_title, lcf.amount, lcf.cashier_name 
from liquidation_cashier_fund lcf 
	inner join liquidation l on lcf.liquidationid=l.objid 
where lcf.objid not in ( 
		select objid from bankdeposit_liquidation 
		where objid=lcf.objid 
	)
order by l.liquidatingofficer_name, l.dtposted 
