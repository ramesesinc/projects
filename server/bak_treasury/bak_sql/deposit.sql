[bindCashReceipChecks]
update 
	liquidation_fund lf, deposit dep, cashreceiptpayment_noncash nc, paymentcheck cc 
set 
	cc.depositid = lf.depositid 
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


[unbindCashReceiptChecksByLiquidationFund]
update 
	liquidation_fund lf, cashreceiptpayment_noncash nc, paymentcheck cc  
set 
	cc.depositid = null 
where 
	lf.depositid = $P{depositid} 
	and lf.objid = $P{liquidationfundid} 
	and nc.liquidationfundid = lf.objid 
	and cc.objid = nc.refid 


[unbindLiquidationFund]
update 
	liquidation_fund lf, liquidation l 
set 
	lf.depositid = null 
where 
	lf.depositid = $P{depositid} 
	and lf.objid = $P{liquidationfundid} 
	and l.objid = lf.liquidationid 

