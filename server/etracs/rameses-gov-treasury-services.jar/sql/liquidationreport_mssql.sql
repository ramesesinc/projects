[getLiquidationFundlist]
select * from ( 
  select 
    fund_objid as fundid, fund_title as fundname, 
    case 
      when fund_objid='GENERAL' then 1 
      when fund_objid='TRUST' then 3 
      when fund_objid='SEF' then 2       
      else 100   
    end as fundsortorder 
  from liquidation_cashier_fund 
  where liquidationid = $P{liquidationid} 
)xx 
order by xx.fundsortorder, xx.fundname 


[findLiquidationInfo]
select 
  l.txnno, l.dtposted, l.liquidatingofficer_name, l.liquidatingofficer_title, 
  lc.fund_title, lc.cashier_name, su.jobtitle as cashier_title, lc.amount, 
  l.cashbreakdown
from liquidation l  
  inner join liquidation_cashier_fund lc on lc.liquidationid = l.objid 
  inner join sys_user su on su.objid = lc.cashier_objid  
where l.objid = $P{liquidationid} 
  and lc.fund_objid = $P{fundid} 


[getRCDRemittances]
select 
  collectorname, dtposted, 
  txnno, sum(amount) as amount  
from ( 
  select 
      r.collector_name as collectorname, r.txnno, 
      convert(DATE, r.remittancedate) as dtposted, rf.amount 
  from liquidation_remittance lr 
    inner join remittance r on lr.objid = r.objid 
    inner join remittance_fund rf ON r.objid = rf.remittanceid  
  where lr.liquidationid = $P{liquidationid} 
    and rf.fund_objid like $P{fundid}
)xx 
group by collectorname, dtposted, txnno 
order by collectorname, dtposted, txnno 


[getRCDRemittancesSummary]
select 
  collectorname, dtposted, 
  txnno, sum(amount) as amount  
from ( 
  select 
      r.collector_name as collectorname, r.txnno as txnno,
      convert(DATE, r.remittancedate) as dtposted, rf.amount as amount 
  from liquidation_remittance lr 
    inner join remittance r on lr.objid = r.objid 
    inner join remittance_fund rf ON r.objid = rf.remittanceid  
  where lr.liquidationid = $P{liquidationid} 
    and rf.fund_objid like $P{fundid} 
)xx 
group by collectorname, dtposted, txnno 
order by collectorname, dtposted, txnno 


[getRCDCollectionSummary]
select * from ( 
  select  
    lcf.fund_title as particulars, lcf.amount as amount, 
    case 
        when fund_objid='GENERAL' then 1 
        when fund_objid='SEF' then 2 
        when fund_objid='TRUST' then 3 
        else 100  
    end as fundsortorder
  from liquidation_cashier_fund  lcf 
  where lcf.liquidationid = $P{liquidationid} 
    and lcf.fund_objid like $P{fundid} 
)xx 
order by xx.fundsortorder, xx.particulars 


[getRCDRemittedForms]
select xx.*, 
  (xx.receivedendseries - xx.receivedstartseries)+1 as qtyreceived, 
  (xx.beginendseries - xx.beginstartseries)+1 as qtybegin, 
  (xx.issuedendseries - xx.issuedstartseries)+1 as qtyissued, 
  (xx.endingendseries - xx.endingstartseries)+1 as qtyending  
from ( 
  select xx.*, 
    case 
      when xx.issuedstartseries > 0 then xx.issuedstartseries 
      when xx.beginstartseries > 0 then xx.beginstartseries 
      WHEN xx.receivedstartseries > 0 then xx.receivedstartseries 
      else xx.endingstartseries 
    end as sortseries 
  from ( 
    select 
      xx.controlid, afi.afid as formno, af.formtype, af.serieslength, af.denomination,  
      afi.respcenter_objid as ownerid, afi.respcenter_name as ownername, 
      (select top 1 receivedstartseries from af_inventory_detail 
        where controlid=xx.controlid and [lineno] between xx.minlineno and xx.maxlineno and receivedstartseries > 0 
          order by [lineno]) as receivedstartseries, 
      (select top 1 receivedendseries from af_inventory_detail 
        where controlid=xx.controlid and [lineno] between xx.minlineno and xx.maxlineno and receivedendseries > 0 
          order by [lineno]) as receivedendseries, 
      (select top 1 beginstartseries from af_inventory_detail 
        where controlid=xx.controlid and [lineno] between xx.minlineno and xx.maxlineno and beginstartseries > 0 
          order by [lineno]) as beginstartseries, 
      (select top 1 beginendseries from af_inventory_detail 
        where controlid=xx.controlid and [lineno] between xx.minlineno and xx.maxlineno and beginendseries > 0 
          order by [lineno] desc) as beginendseries, 
      (select top 1 issuedstartseries from af_inventory_detail 
        where controlid=xx.controlid and [lineno] between xx.minlineno and xx.maxlineno and issuedstartseries > 0 
          order by [lineno]) as issuedstartseries, 
      (select top 1 issuedendseries from af_inventory_detail 
        where controlid=xx.controlid and [lineno] between xx.minlineno and xx.maxlineno and issuedendseries > 0 
          order by [lineno] desc) as issuedendseries , 
      (select top 1 endingstartseries from af_inventory_detail 
        where controlid=xx.controlid and [lineno] between xx.minlineno and xx.maxlineno and endingstartseries > 0 
          order by [lineno] desc) as endingstartseries , 
      (select top 1 endingendseries from af_inventory_detail 
        where controlid=xx.controlid and endingendseries > 0 
          order by [lineno]) as endingendseries
    from ( 
      select ad.controlid, min(ad.[lineno]) as minlineno, max(ad.[lineno]) as maxlineno 
      from liquidation_remittance lr 
        inner join remittance_af r on lr.objid = r.remittanceid
        inner join af_inventory_detail ad on r.objid = ad.objid 
      where lr.liquidationid = $P{liquidationid}  
      group by ad.controlid 
    )xx 
      inner join af_inventory afi on xx.controlid=afi.objid 
      inner join af on afi.afid=af.objid 
  )xx 
)xx 
order by formno, sortseries 


[getRCDOtherPayments]
select 
  pc.reftype as paytype, pc.particulars, cri.amount as amount 
from ( 
  select lr.* from liquidation_remittance  lr 
    inner join remittance_cashreceipt rc on lr.objid = rc.remittanceid 
  where lr.liquidationid = $P{liquidationid}  
    and rc.objid not in (select receiptid from cashreceipt_void where receiptid=rc.objid) 
)xx 
  inner join cashreceiptpayment_noncash pc on xx.objid = pc.receiptid 
  inner join cashreceipt cr on xx.objid = cr.objid 
  inner join cashreceiptitem cri on cr.objid = cri.receiptid 
  inner join itemaccount ri on cri.item_objid = ri.objid 
where ri.fund_objid like $P{fundid} 


[getRevenueItemSummaryByFund]
select 
  ia.fund_title as fundname, cri.item_objid as acctid, cri.item_title as acctname,
  cri.item_code as acctcode, sum( cri.amount ) as amount 
from ( 
  select rc.* from liquidation_remittance  lr 
    inner join remittance_cashreceipt rc on lr.objid = rc.remittanceid 
  where lr.liquidationid = $P{liquidationid}  
    and rc.objid not in (select receiptid from cashreceipt_void where receiptid=rc.objid) 
)xx 
  inner join cashreceipt c on xx.objid = c.objid 
  inner join cashreceiptitem cri on c.objid = cri.receiptid 
  inner join itemaccount ia on cri.item_objid = ia.objid 
where ia.fund_objid like $P{fundid}  
group by ia.fund_title, cri.item_objid, cri.item_title, cri.item_code 
order by ia.fund_title, cri.item_code, cri.item_title 


[getFundSummaries]
SELECT * FROM liquidation_cashier_fund WHERE liquidationid = $P{liquidationid}

[getLiquidationCashierList]
select 
  distinct f.cashier_name as name, su.jobtitle 
from liquidation_cashier_fund f 
  inner join sys_user su on su.objid = f.cashier_objid 
where liquidationid = $P{liquidationid} 


[getAbstractNGASReport]
select  
  na.code as account_code, na.title as account_title, na.type as account_type, 
  na.parentid as account_parentid, f.code as fund_code, f.title as fund_title, 
  sum( inc.amount ) as amount 
from ( 
  select objid from liquidation_remittance 
  where liquidationid = $P{liquidationid}  
)xx 
  inner join income_summary inc on xx.objid = inc.refid 
  inner join fund f ON inc.fundid = f.objid  
  inner join ngas_revenue_mapping nrm ON inc.acctid = nrm.revenueitemid 
  inner join ngasaccount na ON nrm.acctid = na.objid 
where f.objid = $P{fundname}   
group by na.type, na.code, na.title, na.parentid, f.code, f.title 
order by na.code  


[getUnmappedNGASReport]
select  
  ia.code as account_code, ia.title as account_title, 'unmapped' as account_type, 
  null as account_parentid, f.code as fund_code, f.title as fund_title, 
  sum( inc.amount ) as amount 
from ( 
  select objid from liquidation_remittance 
  where liquidationid = $P{liquidationid} 
)xx 
  inner join income_summary inc on xx.objid = inc.refid 
  inner join itemaccount ia on inc.acctid = ia.objid 
  inner join fund f on inc.fundid = f.objid 
where inc.fundid = $P{fundname}  
  and inc.acctid not in (select revenueitemid from ngas_revenue_mapping where revenueitemid=inc.acctid)
group by ia.code, ia.title, f.code, f.title 
order by ia.code  


[findCreditMemoByFund]
select sum(pc.amount) as amount 
from ( 
  select remc.* from liquidation_remittance lr 
    inner join remittance_cashreceipt remc on lr.objid = remc.remittanceid 
  where lr.liquidationid = $P{liquidationid}  
    and remc.objid not in (select receiptid from cashreceipt_void where receiptid=remc.objid) 
)xx 
  inner join cashreceiptpayment_noncash pc on xx.objid = pc.receiptid 
where pc.account_fund_objid = $P{fundid}  
  and pc.reftype='CREDITMEMO'


[getReceipts]
select 
  cr.formno as afid, cr.receiptno as serialno, cr.receiptdate as txndate, 
  ai.fund_title as fundname, cr.remarks as remarks, 
  case when xx.voided=0 then cr.paidby else '***VOIDED***' END AS payer,
  case when xx.voided=0 then cri.item_title else '***VOIDED***' END AS particulars,
  case when xx.voided=0 then cr.paidbyaddress else '' END AS payeraddress,
  case when xx.voided=0 then cri.amount else 0.0 END AS amount 
from ( 
  select remc.*, 
    (select count(*) from cashreceipt_void where receiptid=remc.objid) as voided 
  from liquidation_remittance lrem 
    inner join remittance_cashreceipt remc on lrem.objid = remc.remittanceid 
  where lrem.liquidationid = $P{liquidationid}  
)xx 
  inner join cashreceipt cr on xx.objid = cr.objid 
  inner join cashreceiptitem cri on cr.objid = cri.receiptid 
  inner join itemaccount ai on cri.item_objid = ai.objid 
where ai.fund_objid like $P{fundid} 
order by afid, serialno, payer 


[getReceiptItemAccounts]
select 
  ai.fund_title as fundname, cri.item_objid as acctid, 
  cri.item_title as acctname, cri.item_code as acctcode, 
  sum( cri.amount ) as amount 
from ( 
  select remc.*, 
    (select count(*) from cashreceipt_void where receiptid=remc.objid) as voided 
  from liquidation_remittance lrem 
    inner join remittance_cashreceipt remc on lrem.objid = remc.remittanceid 
  where lrem.liquidationid = $P{liquidationid} 
)xx 
  inner join cashreceipt cr on xx.objid = cr.objid 
  inner join cashreceiptitem cri on cr.objid = cri.receiptid 
  inner join itemaccount ai on cri.item_objid = ai.objid 
where ai.fund_objid like $P{fundid} and xx.voided=0
group by 
  ai.fund_title, cri.item_objid,  
  cri.item_title, cri.item_code 
order by fundname, acctcode 

