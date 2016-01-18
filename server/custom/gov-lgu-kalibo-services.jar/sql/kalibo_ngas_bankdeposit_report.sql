[getFundlist]
select 
  distinct lcf.fund_objid as objid , lcf.fund_title as title 
from bankdeposit bd 
  inner join bankdeposit_liquidation bl on bd.objid = bl.bankdepositid 
  inner join liquidation_cashier_fund lcf on lcf.objid = bl.objid  
where bd.objid = $P{bankdepositid}

[getAccountGroups]
SELECT 
  objid,
  CASE WHEN parentid IS NULL THEN 'ROOT' ELSE parentid END AS parentid,
  code,
  title,
  type,
  0 AS amount 
FROM ngasaccount 
${filter}
ORDER BY code


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
        SELECT
          DISTINCT lf.liquidationid, lf.fund_objid
        FROM bankdeposit b 
          INNER JOIN bankdeposit_liquidation bl ON b.objid = bl.bankdepositid
          INNER JOIN liquidation_cashier_fund lf ON lf.objid = bl.objid 
         WHERE b.objid= $P{bankdepositid} 
          AND lf.fund_objid = $P{fundid} 
      ) a 
      INNER JOIN liquidation_remittance lr ON lr.liquidationid = a.liquidationid 
      INNER JOIN remittance_cashreceipt rc ON rc.remittanceid = lr.objid 
      INNER JOIN cashreceipt c ON c.objid = rc.objid 
      INNER JOIN cashreceiptitem cri ON cri.receiptid = c.objid
      INNER JOIN itemaccount ri ON ri.objid = cri.item_objid AND ri.fund_objid = a.fund_objid
      LEFT JOIN ngas_revenue_mapping nrm ON nrm.revenueitemid=ri.objid 
      LEFT JOIN ngasaccount na ON na.objid=nrm.acctid
      LEFT JOIN cashreceipt_void cv ON cv.receiptid = c.objid 
  WHERE ri.fund_objid = $P{fundid} 
      AND cv.objid IS NULL 
      AND na.objid is null 

  UNION ALL 
  
  SELECT 
    na.objid, na.parentid, na.objid as accountid, na.code, na.title, na.type, cri.amount
  FROM  ( 
        SELECT
          DISTINCT lf.liquidationid, lf.fund_objid
        FROM bankdeposit b 
          INNER JOIN bankdeposit_liquidation bl ON b.objid = bl.bankdepositid
          INNER JOIN liquidation_cashier_fund lf ON lf.objid = bl.objid 
        WHERE b.objid= $P{bankdepositid} 
        AND lf.fund_objid = $P{fundid} 
      ) a 
      inner join liquidation_remittance lr on lr.liquidationid = a.liquidationid 
      inner join remittance_cashreceipt rc on rc.remittanceid = lr.objid 
      inner join cashreceipt c on c.objid = rc.objid 
      inner join cashreceiptitem cri on cri.receiptid = c.objid
      inner join itemaccount ri on ri.objid = cri.item_objid and ri.fund_objid = a.fund_objid
      INNER JOIN ngas_revenue_mapping nrm ON nrm.revenueitemid=ri.objid 
      inner join ngasaccount na ON na.objid=nrm.acctid
      left join cashreceipt_void cv on cv.receiptid = c.objid 
  WHERE ri.fund_objid = $P{fundid} 
      and cv.objid is null 
      and na.type = 'detail'

  UNION ALL

  SELECT 
    p.objid, p.parentid, p.objid as accountid, p.code, p.title, p.type, cri.amount
  FROM  ( 
        SELECT
          DISTINCT lf.liquidationid, lf.fund_objid
        FROM bankdeposit b 
          INNER JOIN bankdeposit_liquidation bl ON b.objid = bl.bankdepositid
          INNER JOIN liquidation_cashier_fund lf ON lf.objid = bl.objid 
        WHERE b.objid= $P{bankdepositid} 
        AND lf.fund_objid = $P{fundid} 
      ) a 
      INNER JOIN liquidation_remittance lr ON lr.liquidationid = a.liquidationid 
      INNER JOIN remittance_cashreceipt rc ON rc.remittanceid = lr.objid 
      INNER JOIN cashreceipt c ON c.objid = rc.objid 
      INNER JOIN cashreceiptitem cri ON cri.receiptid = c.objid
      INNER JOIN itemaccount ri ON ri.objid = cri.item_objid AND ri.fund_objid = a.fund_objid
      INNER JOIN ngas_revenue_mapping nrm ON nrm.revenueitemid=ri.objid 
      INNER JOIN ngasaccount na ON na.objid=nrm.acctid
      inner join ngasaccount p ON p.objid=na.parentid
      LEFT JOIN cashreceipt_void cv ON cv.receiptid = c.objid 
  WHERE ri.fund_objid = $P{fundid} 
      AND cv.objid IS NULL 
      AND na.type = 'subaccount'
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
        SELECT
          DISTINCT lf.liquidationid, lf.fund_objid
        FROM bankdeposit b 
          INNER JOIN bankdeposit_liquidation bl ON b.objid = bl.bankdepositid
          INNER JOIN liquidation_cashier_fund lf ON lf.objid = bl.objid 
         WHERE b.objid= $P{bankdepositid} 
          AND lf.fund_objid = $P{fundid} 
      ) a 
      INNER JOIN liquidation_remittance lr ON lr.liquidationid = a.liquidationid 
      INNER JOIN remittance_cashreceipt rc ON rc.remittanceid = lr.objid 
      INNER JOIN cashreceipt c ON c.objid = rc.objid 
      INNER JOIN cashreceiptitem cri ON cri.receiptid = c.objid
      INNER JOIN itemaccount ri ON ri.objid = cri.item_objid AND ri.fund_objid = a.fund_objid
      LEFT JOIN ngas_revenue_mapping nrm ON nrm.revenueitemid=ri.objid 
      LEFT JOIN ngasaccount na ON na.objid=nrm.acctid
      LEFT JOIN cashreceipt_void cv ON cv.receiptid = c.objid 
  WHERE ri.fund_objid = $P{fundid} 
      AND cv.objid IS NULL 
      AND na.objid is null 

  UNION ALL 
  
  SELECT 
    na.objid, na.parentid, na.objid as accountid, na.code, na.title, na.type, cri.amount
  FROM  ( 
        SELECT
          DISTINCT lf.liquidationid, lf.fund_objid
        FROM bankdeposit b 
          INNER JOIN bankdeposit_liquidation bl ON b.objid = bl.bankdepositid
          INNER JOIN liquidation_cashier_fund lf ON lf.objid = bl.objid 
        WHERE b.objid= $P{bankdepositid} 
        AND lf.fund_objid = $P{fundid} 
      ) a 
      inner join liquidation_remittance lr on lr.liquidationid = a.liquidationid 
      inner join remittance_cashreceipt rc on rc.remittanceid = lr.objid 
      inner join cashreceipt c on c.objid = rc.objid 
      inner join cashreceiptitem cri on cri.receiptid = c.objid
      inner join itemaccount ri on ri.objid = cri.item_objid and ri.fund_objid = a.fund_objid
      INNER JOIN ngas_revenue_mapping nrm ON nrm.revenueitemid=ri.objid 
      inner join ngasaccount na ON na.objid=nrm.acctid
      left join cashreceipt_void cv on cv.receiptid = c.objid 
  WHERE ri.fund_objid = $P{fundid} 
      and cv.objid is null 
   ) bt 
GROUP BY bt.objid,
    bt.parentid,
    bt.code,
    bt.title,
    bt.type



[getDetailedItems]
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
        SELECT
          DISTINCT lf.liquidationid, lf.fund_objid
        FROM bankdeposit b 
          INNER JOIN bankdeposit_liquidation bl ON b.objid = bl.bankdepositid
          INNER JOIN liquidation_cashier_fund lf ON lf.objid = bl.objid 
         WHERE b.objid= $P{bankdepositid} 
          AND lf.fund_objid = $P{fundid} 
      ) a 
      INNER JOIN liquidation_remittance lr ON lr.liquidationid = a.liquidationid 
      INNER JOIN remittance_cashreceipt rc ON rc.remittanceid = lr.objid 
      INNER JOIN cashreceipt c ON c.objid = rc.objid 
      INNER JOIN cashreceiptitem cri ON cri.receiptid = c.objid
      INNER JOIN itemaccount ri ON ri.objid = cri.item_objid AND ri.fund_objid = a.fund_objid
      LEFT JOIN ngas_revenue_mapping nrm ON nrm.revenueitemid=ri.objid 
      LEFT JOIN ngasaccount na ON na.objid=nrm.acctid
      LEFT JOIN cashreceipt_void cv ON cv.receiptid = c.objid 
  WHERE ri.fund_objid = $P{fundid} 
      AND cv.objid IS NULL 
      AND na.objid is null 

  UNION ALL 
  
  SELECT 
    na.objid, na.parentid, na.objid as accountid,  ri.code, ri.title , "revenueitem" as type, cri.amount 
  FROM  ( 
        SELECT
          DISTINCT lf.liquidationid, lf.fund_objid
        FROM bankdeposit b 
          INNER JOIN bankdeposit_liquidation bl ON b.objid = bl.bankdepositid
          INNER JOIN liquidation_cashier_fund lf ON lf.objid = bl.objid 
        WHERE b.objid= $P{bankdepositid} 
        AND lf.fund_objid = $P{fundid} 
      ) a 
      inner join liquidation_remittance lr on lr.liquidationid = a.liquidationid 
      inner join remittance_cashreceipt rc on rc.remittanceid = lr.objid 
      inner join cashreceipt c on c.objid = rc.objid 
      inner join cashreceiptitem cri on cri.receiptid = c.objid
      inner join itemaccount ri on ri.objid = cri.item_objid and ri.fund_objid = a.fund_objid
      INNER JOIN ngas_revenue_mapping nrm ON nrm.revenueitemid=ri.objid 
      inner join ngasaccount na ON na.objid=nrm.acctid
      left join cashreceipt_void cv on cv.receiptid = c.objid 
  WHERE ri.fund_objid = $P{fundid} 
      and cv.objid is null 
   ) bt 
GROUP BY bt.objid,
    bt.parentid,
    bt.accountid,
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

[getItems]
SELECT a.* FROM 
( 
  SELECT 
	ri.objid,
	CASE WHEN nrm.acctid IS NULL THEN 'UNMAPPED' ELSE nrm.acctid END AS parentid, 
	case when ri.code is null then '' else ri.code end as code, 
	ri.title,
	'item' AS type,
	SUM(cri.amount)  AS amount,
	ri.fund_objid,
	ri.fund_title
FROM bankdeposit bd 
  INNER JOIN bankdeposit_liquidation bl ON bd.objid = bl.bankdepositid
  INNER JOIN liquidation_cashier_fund lf ON bl.objid = lf.objid 
  INNER JOIN liquidation_remittance lr ON lf.liquidationid = lr.liquidationid
  INNER JOIN remittance rem ON lr.objid = rem.objid 
  INNER JOIN remittance_cashreceipt rc ON rem.objid = rc.remittanceid
  INNER JOIN cashreceipt cr ON rc.objid = cr.objid 
  INNER JOIN cashreceiptitem cri ON cr.objid = cri.receiptid
  INNER JOIN itemaccount ri ON cri.item_objid = ri.objid AND ri.fund_objid = lf.fund_objid
  LEFT JOIN ngas_revenue_mapping nrm ON nrm.revenueitemid=cri.item_objid
  LEFT JOIN ngasaccount na ON na.objid=nrm.acctid
  LEFT JOIN cashreceipt_void cv ON cv.receiptid = cr.objid  
WHERE bd.objid = $P{bankdepositid}
  AND ri.fund_objid = $P{fundid} 
GROUP BY ri.objid, nrm.acctid, ri.fund_objid, ri.fund_title, ri.code, ri.title
) a
ORDER BY a.code 


