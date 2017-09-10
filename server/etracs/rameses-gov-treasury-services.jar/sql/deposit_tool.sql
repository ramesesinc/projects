[unbindCashReceiptChecks]
update 
	deposit dep, liquidation_fund lf, 
	cashreceiptpayment_noncash nc, cashreceipt_check cc  
set 
	cc.depositrefid = null 
where 
	dep.batchid = $P{batchid} 
	and lf.depositid = dep.objid 
	and nc.liquidationfundid = lf.objid 
	and cc.objid = nc.refid 


[unbindLiquidationFunds]
update 
	deposit dep, liquidation_fund lf, liquidation l 
set 
	lf.depositid = null, 
	l.state = 'APPROVED' 
where 
	dep.batchid = $P{batchid} 
	and lf.depositid = dep.objid 
	and l.objid = lf.liquidationid 


[removeFundTransfers]
delete from deposit_fundtransfer where depositid in ( 
	select objid from deposit where batchid = $P{batchid} 
)

[removeBankDeposits]
delete from deposit_bankdeposit where depositid in ( 
	select objid from deposit where batchid = $P{batchid} 
)

[removeDeposit]
delete from deposit where batchid = $P{batchid}
