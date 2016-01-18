[getFunds]
select fund_objid as objid, fund_title as title  
from liquidation_cashier_fund 
where liquidationid = $P{liquidationid} 
order by fund_title 


[getJevEntries]
select 
  ng.code AS account_code, ng.title AS account_title,
  0.0 AS debit, SUM(cri.amount) AS credit
from liquidation_remittance lrem 
  inner join remittance_cashreceipt remc on lrem.objid=remc.remittanceid 
  inner join cashreceipt cr on remc.objid=cr.objid 
  inner join cashreceiptitem cri on cr.objid=cri.receiptid 
  inner join itemaccount ia on cri.item_objid=ia.objid 
  left join ngas_revenue_mapping nrm on ia.objid=nrm.revenueitemid 
  left join ngasaccount ng on nrm.acctid=ng.objid 
where lrem.liquidationid = $P{liquidationid} 
  and cr.formno <> '56' 
  and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
  and ia.fund_objid = $P{fundid} 
group by ng.code, ng.title 
order by ng.code 


[findRPTReceivables]
select 
  '127' as account_code,
  '      RPT RECEIVABLE' AS account_title,
  0.0 as debit, sum(cri.amount) as credit
from liquidation_remittance lrem 
  inner join remittance_cashreceipt remc on lrem.objid=remc.remittanceid 
  inner join cashreceipt cr on remc.objid=cr.objid 
  inner join cashreceiptitem_rpt_account cri on cr.objid=cri.rptreceiptid 
  inner join itemaccount ia on cri.item_objid=ia.objid 
where lrem.liquidationid = $P{liquidationid} 
  and cr.formno = '56' 
  and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
  and ia.fund_objid = $P{fundid} 
  and cri.revtype like $P{revtype} 


[findRPTBasicDiscount]
select sum(cro.basicdisc) AS discount 
from liquidation_remittance lrem 
  inner join remittance_cashreceipt remc on lrem.objid=remc.remittanceid 
  inner join cashreceiptitem_rpt_online cro on remc.objid=cro.rptreceiptid 
where lrem.liquidationid = $P{liquidationid} 
  and remc.objid not in (select receiptid from cashreceipt_void where receiptid=remc.objid) 


[getRPTIncomes]
select 
  ng.code as account_code, ng.title as account_title,
  0.0 as debit, sum(cri.amount) as credit
from liquidation_remittance lrem 
  inner join remittance_cashreceipt remc on lrem.objid=remc.remittanceid 
  inner join cashreceipt cr on remc.objid=cr.objid 
  inner join cashreceiptitem cri on cr.objid=cri.receiptid 
  inner join itemaccount ia on cri.item_objid=ia.objid 
  left join ngas_revenue_mapping nrm on ia.objid=nrm.revenueitemid 
  left join ngasaccount ng on nrm.acctid=ng.objid 
where lrem.liquidationid = $P{liquidationid}  
  and cr.formno = '56' 
  and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
  and ia.fund_objid = $P{fundid}  
group by ng.code, ng.title 
order by ng.code


[getRPTShares]
select 
  cri.sharetype, sum(cri.amount) as share
from liquidation_remittance lrem 
  inner join remittance_cashreceipt remc on lrem.objid=remc.remittanceid 
  inner join cashreceipt cr on remc.objid=cr.objid 
  inner join cashreceiptitem_rpt_account cri on cr.objid=cri.rptreceiptid 
  inner join itemaccount ia on cri.item_objid=ia.objid 
where lrem.liquidationid = $P{liquidationid}  
  and cr.formno = '56' 
  and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
  and ia.fund_objid = $P{fundid}  
  and cri.revtype like $P{revtype} 
  and cri.sharetype <> $P{orgtype} 
group by cri.sharetype 
