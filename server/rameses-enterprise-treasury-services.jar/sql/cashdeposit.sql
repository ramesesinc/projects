[insertLiquidationFunds]
insert into cashdeposit_liquidation_fund ( 
	objid, depositid, liquidationfundid 
)
select 
	concat($P{depositid},'-',lf.objid) as objid, 
	$P{depositid} as depositid, 
	lf.objid as liquidationfundid 
from liquidation liq 
	inner join liquidation_fund lf on lf.liquidationid=liq.objid 
where liq.cashier_objid = $P{cashierid}
	and liq.state = 'APPROVED' 


[insertFundsForDeposit]
insert into cashdeposit_deposit ( 
	objid, depositid, state, fund_objid, amount, 
	totalcash, totalnoncash, totalcashdeposited, totalnoncashdeposited 
)
select 
	concat('DEP-',cdf.depositid,'-',lf.fund_objid) as objid, 
	cdf.depositid, 'OPEN' as state, lf.fund_objid, 
	sum(lf.totalcash + lf.totalnoncash) as amount, 
	sum(lf.totalcash) as totalcash, sum(lf.totalnoncash) as totalnoncash, 
	0.0 as totalcashdeposited, 0.0 as totalnoncashdeposited 
from cashdeposit_liquidation_fund cdf 
	inner join liquidation_fund lf on lf.objid=cdf.liquidationfundid 
where cdf.depositid = $P{depositid}  
group by lf.fund_objid, lf.fund_title  


[insertFundsForTransfer]
insert into cashdeposit_fundtransfer ( 
	objid, depositid, state, fund_objid, bankaccountid, amount, amounttransferred 
)
select 
	concat('FT-',tmp1.depositid,'-',nc.fund_objid) as objid, 
	tmp1.depositid, 'OPEN' as state, nc.fund_objid, 
	nc.bankaccountid, sum(nc.amount) as amount, 0.0 as amounttransferred
from ( 
	select cdf.depositid, lnc.objid as noncashid 
	from cashdeposit_liquidation_fund cdf 
		inner join liquidation_fund lf on lf.objid=cdf.liquidationfundid 
		inner join liquidation_noncashpayment lnc on lnc.liquidationid=lf.liquidationid 
	where cdf.depositid = $P{depositid}  
	group by cdf.depositid, lnc.objid 
)tmp1 
	inner join cashreceiptpayment_noncash nc on nc.objid=tmp1.noncashid 
where nc.reftype='CREDITMEMO' 
group by tmp1.depositid, nc.fund_objid, nc.bankaccountid 


[getLiquidations]
select l.* 
from ( 
	select cdf.depositid, lf.liquidationid
	from cashdeposit_liquidation_fund cdf 
		inner join liquidation_fund lf on lf.objid=cdf.liquidationfundid 
	where cdf.depositid = $P{depositid}  
	group by cdf.depositid, lf.liquidationid 
)tmp1 
	inner join liquidation l on l.objid=tmp1.liquidationid 
order by l.dtposted 

