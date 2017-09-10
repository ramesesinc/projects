[getAvailableFundsForDeposit]
select 
	lf.fund_objid as fundid, fund.code as fundcode, fund.title as fundtitle  
from liquidation_fund lf 
	inner join liquidation l on l.objid = lf.liquidationid 
	inner join fund on fund.objid = lf.fund_objid 
where lf.depositid is null 
	and l.state = 'APPROVED' 
group by lf.fund_objid, fund.code, fund.title  


[bindLiquidationFunds]
update 
	liquidation_fund lf, liquidation l 
set 
	lf.depositid = $P{depositid}, 
	l.state = 'CLOSED' 
where 
	lf.depositid is null 
	and lf.liquidationid = l.objid 
	and lf.fund_objid = $P{fundid} 
	and l.state = 'APPROVED' 


[bindCashReceipChecks]
update 
	liquidation_fund lf, deposit dep, cashreceiptpayment_noncash nc, cashreceipt_check cc 
set 
	cc.depositrefid = lf.depositid 
where 
	lf.depositid = $P{depositid} 
	and dep.objid = lf.depositid 
	and nc.liquidationfundid = lf.objid 
	and cc.objid = nc.refid 


[insertBankDeposits]
insert into deposit_bankdeposit (
	objid, depositid, amount, totalcash, totalcheck 
) 
select 
	dep.objid, lf.depositid, sum(totalcash + totalcheck) as amount, 
	sum(totalcash) as totalcash, sum(totalcheck) as totalcheck 
from deposit dep 
	inner join liquidation_fund lf on (lf.depositid = dep.objid and lf.fund_objid = dep.fundid) 
where dep.objid = $P{depositid} 
group by dep.objid, lf.depositid


[getBuildFundTransfers]
select 
	lf.depositid, cm.bankaccount_objid as bankaccountid, 
	sum(nc.amount) as amount, 0.0 as amounttransferred 
from liquidation_fund lf
	inner join deposit dep on dep.objid = lf.depositid 
	inner join cashreceiptpayment_noncash nc on nc.liquidationfundid = lf.objid 
	inner join creditmemo cm on cm.objid = nc.refid 
where lf.depositid = $P{depositid} 
group by cm.bankaccount_objid 

	


[xxx]
update 
	deposit a, 
	(
		select depositid, sum(totalcash+totalcheck) as amount, sum(totalcr) as cramount 
		from liquidation_fund where depositid = 'DEP-2f2b2bc4:15e69f72b85:-7fd9-GENERAL' 
		group by depositid 
	)b 
set 
	a.amount = b.amount, 
	a.cramount = b.cramount 
where 
	a.objid = b.depositid 
