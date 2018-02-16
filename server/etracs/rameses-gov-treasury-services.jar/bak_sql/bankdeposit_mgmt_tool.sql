[findLiquidatedFund]
select * from bankdeposit_liquidation where objid=$P{objid} 

[removeLiquidatedFund]
delete from bankdeposit_liquidation where objid=$P{objid} 

