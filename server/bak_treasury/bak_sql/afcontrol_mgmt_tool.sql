[findAFControl]
select * from af_control where objid=$P{controlid} 

[findAFInventory]
select * from af_inventory where objid=$P{controlid} 

[getAFInventoryItems]
select * from af_inventory_detail where controlid=$P{controlid}

[getCashReceipts]
select * from cashreceipt where controlid=$P{controlid} order by series 

[getCashReceiptItems]
select cri.* 
from cashreceipt cr 
	inner join cashreceiptitem cri on cr.objid=cri.receiptid 
where cr.controlid=$P{controlid}  

[getVoidCashReceipts]
select crv.* 
from cashreceipt_void crv 
	inner join cashreceipt cr on crv.receiptid=cr.objid 
where cr.controlid=$P{controlid}  

[getCashReceiptFunds]
select distinct 
	cr.collector_objid, ia.fund_objid  
from cashreceipt cr 
	inner join cashreceiptitem cri on cr.objid=cri.receiptid 
	inner join itemaccount ia on cri.item_objid=ia.objid 
where cr.controlid=$P{controlid}  

[getRemittances]
select rem.* 
from ( 
	select distinct remc.remittanceid  
	from cashreceipt cr 
		inner join remittance_cashreceipt remc on cr.objid=remc.objid 
	where cr.controlid=$P{controlid} 
)xxa, remittance rem 
where rem.objid=xxa.remittanceid 

[getLiquidations]
select distinct liq.* 
from cashreceipt cr 
	inner join remittance_cashreceipt remc on cr.objid=remc.objid 
	inner join liquidation_remittance lrem on remc.remittanceid=lrem.objid 
	inner join liquidation liq on lrem.liquidationid=liq.objid 
where cr.controlid=$P{controlid} 

[getLiquidationFunds]
select * from liquidation_fund 
where liquidationid in (${liquidationids}) 

[getBankDeposits]
select distinct bd.*  
from liquidation_fund lcf 
	inner join bankdeposit_liquidation bdl on lcf.objid=bdl.objid 
	inner join bankdeposit bd on bdl.bankdepositid=bd.objid 
where lcf.objid in (${liquidationfundids}) 

[getBankDepositLiquidations]
select distinct bdl.* 
from liquidation_fund lcf 
	inner join bankdeposit_liquidation bdl on lcf.objid=bdl.objid 
where lcf.objid in (${liquidationfundids}) 
