[getFunds]
select * from remittance_fund where remittanceid=$P{remittanceid} 

[getAFs]
select * from remittance_af where remittanceid=$P{remittanceid} 

[removeFunds]
delete from remittance_fund where remittanceid=$P{remittanceid}

[removeAFs]
delete from remittance_af where remittanceid=$P{remittanceid}

[removeCashReceipts]
delete from remittance_cashreceipt where remittanceid=$P{remittanceid}

[removeCreditMemoPayments]
delete from remittance_creditmemopayment where remittanceid=$P{remittanceid}

[removeNonCashPayments]
delete from remittance_noncashpayment where remittanceid=$P{remittanceid}

[removeRemittance]
delete from remittance where objid=$P{remittanceid}

[findRemittance]
select rem.*, 
	(select count(*) from liquidation_remittance where objid=rem.objid) as liquidated  
from remittance rem 
where rem.objid=$P{remittanceid} 
