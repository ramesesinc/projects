
[getStandardItems]
SELECT
  bt.objid, bt.parentid, bt.accountid, bt.code, bt.title, bt.type, sum(bt.amount) as amount
FROM (
  SELECT 
    'unmapped' AS objid,
    'unmapped' AS parentid,
    'unmapped' AS accountid,
    'unmapped' AS code,
    'unmapped' AS title,
    'unmapped' AS type,
     cri.amount 
  FROM  ( 
        select distinct cr.objid
        from cashreceipt cr 
          INNER JOIN remittance_cashreceipt rc on cr.objid = rc.objid 
          INNER JOIN liquidation_remittance lc on lc.objid = rc.remittanceid 
          INNER JOIN liquidation_cashier_fund lcf ON lcf.liquidationid = lc.liquidationid 
          INNER JOIN bankdeposit_liquidation bl ON lcf.objid = bl.objid
          inner join bankdeposit bd on bd.objid = bl.bankdepositid 
          LEFT JOIN cashreceipt_void vr ON cr.objid = vr.receiptid  
        where cr.receiptdate BETWEEN $P{fromdate} AND $P{todate} 
          and vr.objid is null 
      ) a 
      INNER JOIN cashreceiptitem cri ON cri.receiptid = a.objid
      INNER JOIN itemaccount ri ON ri.objid = cri.item_objid 
      LEFT JOIN ngas_revenue_mapping nrm ON nrm.revenueitemid=ri.objid 
      LEFT JOIN ngasaccount na ON na.objid=nrm.acctid
  WHERE na.objid is null     

  UNION ALL 
  
  SELECT 
    na.objid, na.parentid, na.objid as accountid, na.code, na.title, na.type, cri.amount
  FROM  ( 
        select distinct cr.objid
          from cashreceipt cr 
            INNER JOIN remittance_cashreceipt rc on cr.objid = rc.objid 
            INNER JOIN liquidation_remittance lc on lc.objid = rc.remittanceid 
            INNER JOIN liquidation_cashier_fund lcf ON lcf.liquidationid = lc.liquidationid 
            INNER JOIN bankdeposit_liquidation bl ON lcf.objid = bl.objid
            inner join bankdeposit bd on bd.objid = bl.bankdepositid 
            LEFT JOIN cashreceipt_void vr ON cr.objid = vr.receiptid  
          where cr.receiptdate BETWEEN $P{fromdate} AND $P{todate} 
            and vr.objid is null 
      ) a 
      inner join cashreceiptitem cri on cri.receiptid = a.objid
      inner join itemaccount ri on ri.objid = cri.item_objid 
      INNER JOIN ngas_revenue_mapping nrm ON nrm.revenueitemid=ri.objid 
      inner join ngasaccount na ON na.objid=nrm.acctid
  WHERE  na.type = 'detail'

  UNION ALL

  SELECT 
    p.objid, p.parentid, p.objid as accountid, p.code, p.title, p.type, cri.amount
  FROM  ( 
        select distinct cr.objid
          from cashreceipt cr 
            INNER JOIN remittance_cashreceipt rc on cr.objid = rc.objid 
            INNER JOIN liquidation_remittance lc on lc.objid = rc.remittanceid 
            INNER JOIN liquidation_cashier_fund lcf ON lcf.liquidationid = lc.liquidationid 
            INNER JOIN bankdeposit_liquidation bl ON lcf.objid = bl.objid
            inner join bankdeposit bd on bd.objid = bl.bankdepositid 
            LEFT JOIN cashreceipt_void vr ON cr.objid = vr.receiptid  
          where cr.receiptdate BETWEEN $P{fromdate} AND $P{todate} 
            and vr.objid is null 
      ) a 
      INNER JOIN cashreceiptitem cri ON cri.receiptid = a.objid
      INNER JOIN itemaccount ri ON ri.objid = cri.item_objid 
      INNER JOIN ngas_revenue_mapping nrm ON nrm.revenueitemid=ri.objid 
      INNER JOIN ngasaccount na ON na.objid=nrm.acctid
      inner join ngasaccount p ON p.objid=na.parentid
  WHERE na.type = 'subaccount'
   ) bt 
GROUP BY bt.objid,
    bt.parentid,
    bt.code,
    bt.title,
    bt.type


[getExtendedItems]
SELECT
  bt.objid, bt.parentid, bt.accountid, bt.code, bt.title, bt.type, sum(bt.amount) as amount
FROM (
  SELECT 
    'unmapped' AS objid,
    'unmapped' AS parentid,
    'unmapped' AS accountid,
    'unmapped' AS code,
    'unmapped' AS title,
    'unmapped' AS type,
     cri.amount 
  FROM  ( 
        select distinct cr.objid
          from cashreceipt cr 
            INNER JOIN remittance_cashreceipt rc on cr.objid = rc.objid 
            INNER JOIN liquidation_remittance lc on lc.objid = rc.remittanceid 
            INNER JOIN liquidation_cashier_fund lcf ON lcf.liquidationid = lc.liquidationid 
            INNER JOIN bankdeposit_liquidation bl ON lcf.objid = bl.objid
            inner join bankdeposit bd on bd.objid = bl.bankdepositid 
            LEFT JOIN cashreceipt_void vr ON cr.objid = vr.receiptid  
          where cr.receiptdate BETWEEN $P{fromdate} AND $P{todate} 
            and vr.objid is null 
      ) a 
      INNER JOIN cashreceiptitem cri ON cri.receiptid = a.objid
      INNER JOIN itemaccount ri ON ri.objid = cri.item_objid 
      LEFT JOIN ngas_revenue_mapping nrm ON nrm.revenueitemid=ri.objid 
      LEFT JOIN ngasaccount na ON na.objid=nrm.acctid
  WHERE na.objid is null 

  UNION ALL 
  
  SELECT 
    na.objid, na.parentid, na.objid as accountid, na.code, na.title, na.type, cri.amount
  FROM  ( 
        select distinct cr.objid
          from cashreceipt cr 
            INNER JOIN remittance_cashreceipt rc on cr.objid = rc.objid 
            INNER JOIN liquidation_remittance lc on lc.objid = rc.remittanceid 
            INNER JOIN liquidation_cashier_fund lcf ON lcf.liquidationid = lc.liquidationid 
            INNER JOIN bankdeposit_liquidation bl ON lcf.objid = bl.objid
            inner join bankdeposit bd on bd.objid = bl.bankdepositid 
            LEFT JOIN cashreceipt_void vr ON cr.objid = vr.receiptid  
          where cr.receiptdate BETWEEN $P{fromdate} AND $P{todate} 
            and vr.objid is null 
      ) a 
      inner join cashreceiptitem cri on cri.receiptid = a.objid
      inner join itemaccount ri on ri.objid = cri.item_objid 
      INNER JOIN ngas_revenue_mapping nrm ON nrm.revenueitemid=ri.objid 
      inner join ngasaccount na ON na.objid=nrm.acctid 
   ) bt 
GROUP BY bt.objid,
    bt.parentid,
    bt.code,
    bt.title,
    bt.type



[getNgasRootAccounts]
SELECT objid, code, title, type FROM ngasaccount WHERE parentid is null  ORDER BY code 


[getNgasSubAccounts]
SELECT objid, parentid, code, title, type 
FROM ngasaccount 
WHERE parentid = $P{parentid} 
  AND type IN ('group', 'detail')
ORDER BY code 

[getNgasExtendedSubAccounts]
SELECT objid, parentid, code, title, type 
FROM ngasaccount 
WHERE parentid = $P{parentid} 
ORDER BY code 
