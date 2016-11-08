[findRemittanceReceipt]
select * from remittance_cashreceipt where objid=$P{objid}

[removeRemittanceReceipt]
delete from remittance_cashreceipt where objid=$P{objid} 
 
[getRemittanceReceipts]
select 
	remc.objid, remc.remittanceid 
from cashreceipt cr 
	inner join remittance_cashreceipt remc on cr.objid=remc.objid 
	inner join remittance rem on remc.remittanceid=rem.objid 
where cr.controlid=$P{controlid} 


[removeFunds]
delete from remittance_fund where remittanceid=$P{remittanceid}  

[insertFund]
insert into remittance_fund (
	objid, remittanceid, fund_objid, fund_title, amount 
) values ( 
	$P{objid}, $P{remittanceid}, $P{fundid}, $P{fundtitle}, $P{amount} 
) 

[getRebuildFunds]
select xx.*, fund.title as fund_title 
from ( 
	select 
		remc.remittanceid, ia.fund_objid, 
		sum( cri.amount ) as amount  
	from remittance_cashreceipt remc 
		inner join cashreceipt cr on remc.objid=cr.objid 
		inner join cashreceiptitem cri on cr.objid=cri.receiptid 
		inner join itemaccount ia on cri.item_objid=ia.objid 
	where remc.remittanceid=$P{remittanceid} 
		and remc.objid not in ( 
			select receiptid from cashreceipt_void 
			where receiptid=remc.objid 
		) 
	group by remc.remittanceid, ia.fund_objid 
)xx, fund  
where xx.fund_objid=fund.objid  

[getCashReceiptNonCash]
select objid from cashreceiptpayment_noncash 
where receiptid=$P{receiptid} 

[removeNonCashPayments]
delete from remittance_noncashpayment where remittanceid=$P{remittanceid} 

[removeNonCashPayment]
delete from remittance_noncashpayment where objid=$P{objid} 

[insertNonCashPayments]
insert into remittance_noncashpayment (
	objid, remittanceid, amount, voided 
) 
select 
  	nc.objid, remc.remittanceid, nc.amount,    
	(case when cv.objid is null then 0 else 1 end) as voided 
from remittance_cashreceipt remc 
  	inner join cashreceipt cr ON remc.objid=cr.objid 
  	inner join cashreceiptpayment_noncash nc ON cr.objid=nc.receiptid 
	left join cashreceipt_void cv on cr.objid=cv.receiptid 
where remc.remittanceid = $P{remittanceid} 

[updateTotals]
update 
	remittance rem, ( 
		select 
			remittanceid, 
			sum(totalamount) as totalamount, 
			sum(totalnoncash) as totalnoncash, 
			(sum(totalamount)-sum(totalnoncash)) as totalcash 
		from ( 
			select 
				remittanceid, 0.0 as totalamount, 
				(case when sum(amount) >= 0.0 then sum(amount) else 0.0 end) as totalnoncash  
			from remittance_noncashpayment 
			where remittanceid=$P{remittanceid} and voided=0 
			group by remittanceid 

			union all 
			
			select
				remittanceid,  
				(case when sum(amount) >= 0.0 then sum(amount) else 0.0 end) as totalamount, 
				0.0 as totalnoncash 
			from remittance_fund 
			where remittanceid=$P{remittanceid} 
			group by remittanceid 
		)t1 
		group by remittanceid 
	)t2 
set 
	rem.amount = t2.totalamount, 
	rem.totalcash = t2.totalcash, 
	rem.totalnoncash = t2.totalnoncash 
where rem.objid=t2.remittanceid 

