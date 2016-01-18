[getCollectionByFund]
select 
    ri.fund_title as fundname, cri.item_objid as acctid, cri.item_title as acctname,
    cri.item_code as acctcode, sum( cri.amount ) as amount 
from cashreceipt cr 
  INNER JOIN remittance_cashreceipt rc on cr.objid = rc.objid 
  INNER JOIN remittance r on r.objid = rc.remittanceid 
  INNER join cashreceiptitem cri on cri.receiptid = cr.objid
  INNER join itemaccount ri on ri.objid = cri.item_objid 
  LEFT JOIN cashreceipt_void vr ON cr.objid = vr.receiptid  
where r.remittancedate BETWEEN $P{fromdate} AND $P{todate} 
  and vr.objid is null  ${filter} 
group by ri.fund_title, cri.item_objid, cri.item_code, cri.item_title 
order by fundname, acctcode, acctname  


[getAbstractOfCollection]
select 
    cr.formno, 
    cr.receiptno, 
    cr.receiptdate, 
    cr.formtype, 
    CASE WHEN vr.objid is null THEN cr.paidby ELSE '*** VOIDED ***' END AS payorname, 
    CASE WHEN vr.objid is null THEN cr.paidbyaddress ELSE '' END AS payoraddress, 
    CASE WHEN vr.objid is null  THEN cri.item_title ELSE '' END AS accttitle, 
    CASE WHEN vr.objid is null  THEN ri.fund_title ELSE '' END AS fundname, 
    CASE WHEN vr.objid is null  THEN cri.amount ELSE 0.0 END AS amount, 
    cr.collector_name as collectorname, 
    cr.collector_title as collectortitle  
from cashreceipt cr 
  INNER JOIN remittance_cashreceipt rc on cr.objid = rc.objid 
  INNER JOIN remittance r on r.objid = rc.remittanceid 
  INNER join cashreceiptitem cri on cri.receiptid = cr.objid
  INNER join itemaccount ri on ri.objid = cri.item_objid 
  LEFT JOIN cashreceipt_void vr ON cr.objid = vr.receiptid  
where r.remittancedate BETWEEN $P{fromdate} AND $P{todate}  
    ${filter} 
order by cr.formno, cr.receiptno


  

[getFunds]
select * from fund 

[getSubFunds]
select * from fund where parentid = $P{objid}
