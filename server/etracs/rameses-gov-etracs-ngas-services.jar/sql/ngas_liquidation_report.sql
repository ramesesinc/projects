[getAccountGroups]
select 
  objid,
  case 
    when parentid is null then 'ROOT' 
    else parentid 
  end as parentid,
  code,
  title, type, 0.0 AS amount 
from ngasaccount 
${filter}
order by code


[getLiquidatedItems]
select * from ( 
  select 
    cri.objid, 
    case 
      when nrm.acctid is null then 'UNMAPPED' 
      else nrm.acctid 
    end as parentid, 
    ia.code, ia.title, 'item' AS type, sum(cri.amount) as amount,
    ia.fund_objid, ia.fund_title
  from ( 
    select a.*, 
      (select count(*) from cashreceipt_void where receiptid=a.objid) as voided 
    from remittance_cashreceipt a 
    where remittanceid in (select objid from liquidation_remittance where liquidationid='LIQ328fb949:14ddb7f79e4:1318') 
  )xx  
    inner join cashreceipt cr on xx.objid = cr.objid 
    inner join cashreceiptitem cri on cr.objid = cri.receiptid 
    inner join itemaccount ia on cri.item_objid = ia.objid 
    left join ngas_revenue_mapping nrm on cri.item_objid = nrm.revenueitemid 
  where xx.voided=0 ${filter} 
)xx 
order by code 



[getSummaryReport]
SELECT rem.objid AS remittanceid, cr.receiptdate, cr.receiptno, cr.paidby,
 na.code AS code, na.title AS title, CASE WHEN crv.objid IS NULL THEN cri.amount ELSE 0 END AS amount,
 CASE WHEN crv.objid IS NULL THEN 0 ELSE 1 END AS voided, 
ri.fund_objid, ri.fund_title
FROM cashreceiptitem cri
INNER JOIN itemaccount ri ON cri.item_objid=ri.objid
INNER JOIN cashreceipt cr ON cr.objid=cri.receiptid
INNER JOIN remittance_cashreceipt rem ON cri.receiptid=rem.objid
INNER JOIN liquidation_remittance lr ON lr.objid=rem.remittanceid
LEFT JOIN cashreceipt_void crv on crv.receiptid = cr.objid 
LEFT JOIN ngas_revenue_mapping nrm ON nrm.revenueitemid=cri.item_objid
LEFT JOIN ngasaccount na ON na.objid=nrm.acctid
WHERE lr.liquidationid=$P{liquidationid} AND crv.objid IS NULL
${filter}
ORDER BY cr.receiptno, na.code


