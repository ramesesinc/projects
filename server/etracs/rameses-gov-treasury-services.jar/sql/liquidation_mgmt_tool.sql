[findLiquidatedRemittance]
select * from liquidation_remittance where objid=$P{objid} 

[removeLiquidatedRemittance]
delete from liquidation_remittance where objid=$P{objid} 

[getLiquidatedRemittances]
select * from liquidation_remittance 
where objid in (${remittanceids}) 

[getLiquidatedFunds]
select * from liquidation_cashier_fund  
where liquidationid in (${liquidationids}) 

[removeNonCashPayments]
delete from liquidation_noncashpayment where liquidationid=$P{liquidationid}

[insertNonCashPayments]
insert into liquidation_noncashpayment ( objid, liquidationid ) 
select distinct remnc.objid, lrem.liquidationid  
from liquidation_remittance lrem 
	inner join remittance_noncashpayment remnc on lrem.objid=remnc.remittanceid 
where lrem.liquidationid=$P{liquidationid} 

[getFunds]
select * from liquidation_cashier_fund 
where liquidationid=$P{liquidationid} 

[getRebuildFunds]
select 
	xx.liquidationid, xx.fund_objid, fund.title as fund_title, xx.amount 
from ( 
	select 
		lrem.liquidationid, remf.fund_objid, sum(remf.amount) as amount 
	from liquidation_remittance lrem 
		inner join remittance_fund remf on lrem.objid=remf.remittanceid 
	where lrem.liquidationid=$P{liquidationid} 
	group by lrem.liquidationid, remf.fund_objid 
)xx, fund 
where xx.fund_objid=fund.objid 

[removeFund]
select * from liquidation_cashier_fund where objid=$P{objid} 

[updateFund]
update liquidation_cashier_fund set 
	amount = $P{amount}, 
	fund_objid = $P{fundid}, 
	fund_title = $P{fundtitle} 
where 
	objid=$P{objid} 

[insertFund]
insert into liquidation_cashier_fund ( 
	objid, liquidationid, fund_objid, fund_title, cashier_objid, cashier_name, amount 
) values ( 
	$P{objid}, $P{liquidationid}, $P{fundid}, $P{fundtitle}, $P{cashierid}, $P{cashiername}, $P{amount}  
)

[updateTotals]
update liq set 
	liq.amount = xx2.amount, 
	liq.totalcash = xx2.totalcash, 
	liq.totalnoncash = xx2.totalnoncash 
from liquidation liq, ( 
			select 
				liquidationid, 
				sum(isnull(totalamount,0.0)) as amount, 
				sum(isnull(totalnoncash,0.0)) as totalnoncash, 
				(sum(isnull(totalamount,0.0))-sum(isnull(totalnoncash,0.0))) as totalcash 
			from ( 
				select 
					liquidationid, sum(amount) as totalamount, 0.0 as totalnoncash 
				from liquidation_cashier_fund 
				where liquidationid=$P{liquidationid}  
				group by liquidationid 
				union all 
				select 
					lnc.liquidationid, 0.0 as totalamount, sum(nc.amount) as totalnoncash 
				from liquidation_noncashpayment lnc 
					inner join cashreceiptpayment_noncash nc on lnc.objid=nc.objid 	
				where lnc.liquidationid=$P{liquidationid} 
					and nc.receiptid not in ( select receiptid from cashreceipt_void where receiptid=nc.receiptid ) 		
				group by lnc.liquidationid 
			)xx1  
			group by liquidationid 
		)xx2  
where 
	liq.objid=xx2.liquidationid 
