[getCollectionByFund]
select 
  ri.fund_objid as fundid, ri.fund_title as fundname, fund.code as fundcode, 
  cri.item_objid as acctid, cri.item_title as acctname, cri.item_code as acctcode, 
  sum( cri.amount ) as amount, fund.parentid as fundparentid  
from ( 
  select remc.objid 
  from remittance r 
    inner join remittance_cashreceipt remc on r.objid=remc.remittanceid  
  where r.remittancedate >= $P{fromdate} 
    and r.remittancedate < $P{todate} 
    and remc.objid not in (select receiptid from cashreceipt_void where receiptid=remc.objid) 
)xx1 
  inner join cashreceipt cr on cr.objid=xx1.objid 
  inner join cashreceiptitem cri on cri.receiptid=cr.objid 
  inner join itemaccount ri on cri.item_objid=ri.objid 
  inner join fund on ri.fund_objid=fund.objid 
where ri.fund_objid in ( 
    select objid from fund where objid like $P{fundid} 
    union 
    select objid from fund where parentid=$P{fundparentid} 
  ) 
group by 
  ri.fund_objid, ri.fund_title, fund.code, fund.parentid, 
  cri.item_objid, cri.item_title, cri.item_code 
order by 
  fund.code, ri.fund_title, cri.item_code, cri.item_title  


[getCollectionByFundByLiquidation]
select 
  fund.parentid as fundparentid, fund.objid as fundid, 
  fund.code as fundcode, fund.title as fundname, 
  ia.objid as acctid, ia.code as acctcode, ia.title as acctname, 
  xx2.amount 
from ( 
  select 
    inc.fundid, inc.acctid, sum(inc.amount) as amount 
  from ( 
    select lrem.objid as refid 
    from liquidation l 
      inner join liquidation_remittance lrem on l.objid=lrem.liquidationid 
    where l.dtposted >= $P{fromdate} 
      and l.dtposted < $P{todate} 
  )xx1, income_summary inc 
  where inc.refid = xx1.refid  
  group by inc.fundid, inc.acctid 
)xx2 
  inner join fund on xx2.fundid=fund.objid 
  inner join itemaccount ia on xx2.acctid=ia.objid 
where fund.objid in ( 
    select objid from fund where objid like $P{fundid} 
    union 
    select objid from fund where parentid = $P{fundparentid} 
  ) 
order by 
  fund.code, fund.title, ia.code, ia.title 


[getAbstractOfCollection]
select 
  cr.controlid, cr.formno, cr.receiptno, cr.receiptdate, cr.formtype, 
  CASE WHEN vr.objid is null THEN cr.paidby ELSE '*** VOIDED ***' END AS payorname, 
  CASE WHEN vr.objid is null THEN cr.paidbyaddress ELSE '' END AS payoraddress, 
  CASE WHEN vr.objid is null  THEN cri.item_title ELSE '' END AS accttitle, 
  CASE WHEN vr.objid is null  THEN ri.fund_title ELSE '' END AS fundname, 
  CASE WHEN vr.objid is null  THEN cri.amount ELSE 0.0 END AS amount, 
  cr.collector_name as collectorname, 
  cr.collector_title as collectortitle 
from ( 
  select r.objid 
  from remittance r 
    inner join liquidation_remittance lr on lr.objid = r.objid 
    inner join liquidation l on l.objid = lr.liquidationid 
  where 'BY_REMITTANCE' = $P{postingtypeid} 
    and r.remittancedate >= $P{fromdate} 
    and r.remittancedate <  $P{todate} 

  union 

  select r.objid 
  from liquidation l 
    inner join liquidation_remittance lr on lr.liquidationid = l.objid 
    inner join remittance r on r.objid = lr.objid 
  where 'BY_LIQUIDATION' = $P{postingtypeid} 
    and l.liqdate >= $P{fromdate} 
    and l.liqdate <  $P{todate} 
)tmp 
  INNER JOIN remittance r on r.objid = tmp.objid 
  INNER JOIN remittance_cashreceipt rc on rc.remittanceid = r.objid 
  INNER JOIN cashreceipt cr on cr.objid = rc.objid 
  INNER join cashreceiptitem cri on cri.receiptid = cr.objid
  INNER join itemaccount ri on ri.objid = cri.item_objid 
  LEFT JOIN cashreceipt_void vr ON cr.objid = vr.receiptid  
where 1=1 ${filter} 
order by cr.formno, cr.controlid, cr.receiptno


[getFunds]
select * from fund order by code 

[getSubFunds]
select * from fund where parentid = $P{objid} order by code 
