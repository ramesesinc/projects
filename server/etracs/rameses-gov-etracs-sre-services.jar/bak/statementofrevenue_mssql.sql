[getNgasRootAccounts]
SELECT objid, code, title, type FROM account WHERE parentid is null  ORDER BY code 

[getSreRootAccounts]
SELECT objid, code, title, type FROM sreaccount WHERE parentid is null ORDER BY code


[getNgasSubAccounts]
SELECT objid, parentid, code, title, type 
FROM account 
WHERE parentid = $P{parentid} 
  AND type IN ('group', 'detail')
ORDER BY code 


[getSreSubAccounts]
SELECT objid, parentid, code, title, type , 
	(SELECT TOP 1 target FROM sreaccount_incometarget WHERE objid=s.objid AND year=$P{year} ) AS target
FROM sreaccount s
WHERE parentid = $P{parentid} 
  AND type IN ('group', 'detail')
ORDER BY code 


[getNgasExtendedSubAccounts]
SELECT objid, parentid, code, title, type 
FROM account 
WHERE parentid = $P{parentid} 
ORDER BY code 

[getSreExtendedSubAccounts]
SELECT objid, parentid, code, title, type , 
	(SELECT TOP 1 target FROM sreaccount_incometarget WHERE objid=s.objid AND year=$P{year} ) AS target
FROM sreaccount s 
WHERE parentid = $P{parentid} 
ORDER BY code 

[findAccountById]
SELECT * FROM account WHERE objid = $P{objid}


[getNgasStandardRevenueItemSummaries]
SELECT t.*,
	(SELECT TOP 1 target FROM account_incometarget WHERE objid=t.objid AND year=$P{year}) AS target
FROM (
	SELECT 
		CASE WHEN acct.objid IS NULL THEN 'unmapped' ELSE acct.objid END AS objid,
		CASE WHEN acct.parentid IS NULL THEN 'unmapped' ELSE acct.parentid END AS parentid,
		CASE WHEN acct.objid IS NULL THEN 'unmapped' ELSE acct.objid END AS accountid,
		CASE WHEN acct.code IS NULL THEN 'unmapped' ELSE acct.code END AS code,
		CASE WHEN acct.title IS NULL THEN 'unmapped' ELSE acct.title END AS title,
		CASE WHEN acct.type IS NULL THEN 'unmapped' ELSE acct.type END AS type,
		SUM(cri.amount) AS amount
	FROM (
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
		) cr 
		INNER JOIN cashreceiptitem cri ON cr.objid = cri.receiptid 
		INNER JOIN itemaccount ri ON cri.item_objid = ri.objid 
		LEFT JOIN revenueitem_attribute attr ON ri.objid = attr.revitemid  and attr.attribute_objid='ngasstandard'
		LEFT JOIN account acct on acct.objid = attr.account_objid 
	GROUP BY 
		acct.objid,
		acct.parentid,
		acct.code,
		acct.title,
		acct.type
		
	UNION ALL

	SELECT 
		CASE WHEN acct.objid IS NULL THEN 'unmapped' ELSE acct.objid END AS objid,
		CASE WHEN acct.parentid IS NULL THEN 'unmapped' ELSE acct.parentid END AS parentid,
		CASE WHEN acct.objid IS NULL THEN 'unmapped' ELSE acct.objid END AS accountid,
		CASE WHEN acct.code IS NULL THEN 'unmapped' ELSE acct.code END AS code,
		CASE WHEN acct.title IS NULL THEN 'unmapped' ELSE acct.title END AS title,
		CASE WHEN acct.type IS NULL THEN 'unmapped' ELSE acct.type END AS type,
		SUM(dci.amount) AS amount
	FROM directcash_collection dc
		INNER JOIN directcash_collection_item dci ON dc.objid = dci.parentid
		INNER JOIN itemaccount ri ON dci.item_objid = ri.objid 
		LEFT JOIN revenueitem_attribute attr ON ri.objid = attr.revitemid  and attr.attribute_objid='ngasstandard'
		LEFT JOIN account acct on acct.objid = attr.account_objid
	WHERE dc.refdate BETWEEN $P{fromdate} AND $P{todate}
	GROUP BY 
		acct.objid,
		acct.parentid,
		acct.code,
		acct.title,
		acct.type	
)t 
ORDER BY t.code   		



[getNgasExtendedRevenueItemSummaries]
SELECT t.*,
	(SELECT TOP 1 target FROM account_incometarget WHERE objid=t.accountid AND year=$P{year} ) AS target
FROM (
	SELECT 
		CASE 
			WHEN subacct.parentid IS NOT NULL THEN subacct.parentid 
			WHEN acct.parentid IS NOT NULL THEN acct.parentid
			ELSE 'unmapped' 
		END AS parentid,
		CASE 
			WHEN subacct.objid IS NOT NULL THEN subacct.objid 
			WHEN acct.objid IS NOT NULL THEN acct.objid
			ELSE 'unmapped' 
		END AS accountid,
		CASE 
			WHEN acct.code IS NOT NULL THEN acct.code
			ELSE 'unmapped' 
		END AS parentcode,
		CASE 
			WHEN subacct.code IS NOT NULL THEN subacct.code 
			WHEN acct.code IS NOT NULL THEN acct.code
			ELSE 'unmapped' 
		END AS code,
		CASE 
			WHEN subacct.title IS NOT NULL THEN subacct.title 
			WHEN acct.title IS NOT NULL THEN acct.title
			ELSE 'unmapped' 
		END AS title,
		CASE 
			WHEN subacct.type IS NOT NULL THEN subacct.type 
			WHEN acct.type IS NOT NULL THEN acct.type
			ELSE 'unmapped' 
		END AS type,
		SUM(cri.amount) AS amount
	FROM (
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
		) cr 
		INNER JOIN cashreceiptitem cri ON cr.objid = cri.receiptid 
		INNER JOIN itemaccount ri ON cri.item_objid = ri.objid 
		LEFT JOIN revenueitem_attribute attr ON ri.objid = attr.revitemid and attr.attribute_objid = 'ngasstandard'
		LEFT JOIN sreaccount acct ON attr.account_objid = acct.objid 
		LEFT JOIN revenueitem_attribute subattr ON ri.objid = subattr.revitemid and subattr.attribute_objid='ngassubaccount'
		LEFT JOIN sreaccount subacct ON subattr.account_objid = subacct.objid   
	GROUP BY 
		acct.objid,
		acct.parentid,
		acct.code,
		acct.title,
		acct.type,
		subacct.objid,
		subacct.parentid,
		subacct.code,
		subacct.title,
		subacct.type 

	UNION 

	SELECT 
		CASE 
			WHEN subacct.parentid IS NOT NULL THEN subacct.parentid 
			WHEN acct.parentid IS NOT NULL THEN acct.parentid
			ELSE 'unmapped' 
		END AS parentid,
		CASE 
			WHEN subacct.objid IS NOT NULL THEN subacct.objid 
			WHEN acct.objid IS NOT NULL THEN acct.objid
			ELSE 'unmapped' 
		END AS accountid,
		CASE 
			WHEN acct.code IS NOT NULL THEN acct.code
			ELSE 'unmapped' 
		END AS parentcode,
		CASE 
			WHEN subacct.code IS NOT NULL THEN subacct.code 
			WHEN acct.code IS NOT NULL THEN acct.code
			ELSE 'unmapped' 
		END AS code,
		CASE 
			WHEN subacct.title IS NOT NULL THEN subacct.title 
			WHEN acct.title IS NOT NULL THEN acct.title
			ELSE 'unmapped' 
		END AS title,
		CASE 
			WHEN subacct.type IS NOT NULL THEN subacct.type 
			WHEN acct.type IS NOT NULL THEN acct.type
			ELSE 'unmapped' 
		END AS type,
		SUM(dci.amount) AS amount
	FROM directcash_collection dc
		INNER JOIN directcash_collection_item dci ON dc.objid = dci.parentid
		INNER JOIN itemaccount ri ON dci.item_objid = ri.objid 
		LEFT JOIN revenueitem_attribute attr ON ri.objid = attr.revitemid and attr.attribute_objid = 'ngasstandard'
		LEFT JOIN sreaccount acct ON attr.account_objid = acct.objid 
		LEFT JOIN revenueitem_attribute subattr ON ri.objid = subattr.revitemid and subattr.attribute_objid='ngassubaccount'
		LEFT JOIN sreaccount subacct ON subattr.account_objid = subacct.objid   
	WHERE dc.refdate BETWEEN $P{fromdate} AND $P{todate}
	GROUP BY 
		acct.objid,
		acct.parentid,
		acct.code,
		acct.title,
		acct.type
) t
ORDER BY t.parentcode, t.code
  

[getNgasDetailedRevenueItemSummaries]
SELECT t.*,
	(SELECT TOP 1 target FROM account_incometarget WHERE objid=t.accountid AND year=$P{year} ) AS target
FROM (
	SELECT 
		ri.objid,
		CASE 
			WHEN subacct.objid IS NOT NULL THEN subacct.objid 
			WHEN acct.objid IS NOT NULL THEN acct.objid
			ELSE 'unmapped'
		END AS parentid,
		CASE 
			WHEN subacct.objid IS NOT NULL THEN subacct.objid 
			WHEN acct.objid IS NOT NULL THEN acct.objid
			ELSE 'unmapped'
		END AS accountid,

		CASE 
			WHEN acct.code IS NOT NULL THEN acct.code
			ELSE 'unmapped'
		END AS acctcode,
		CASE 
			WHEN subacct.code IS NOT NULL THEN subacct.code 
			ELSE 'unmapped'
		END AS subacctcode,

		ri.code,
		ri.title,
		'revenueitem' AS type,
		SUM(cri.amount) AS amount
	FROM (
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
		) cr 
		INNER JOIN cashreceiptitem cri ON cr.objid = cri.receiptid 
		INNER JOIN itemaccount ri ON cri.item_objid = ri.objid 
		LEFT JOIN revenueitem_attribute attr ON ri.objid = attr.revitemid and attr.attribute_objid = 'ngasstandard'
		LEFT JOIN sreaccount acct ON attr.account_objid = acct.objid 
		LEFT JOIN revenueitem_attribute subattr ON ri.objid = subattr.revitemid and subattr.attribute_objid='ngassubaccount'
		LEFT JOIN sreaccount subacct ON subattr.account_objid = subacct.objid   
	GROUP BY 
		acct.objid,
		acct.parentid,
		acct.code,
		subacct.objid,
		subacct.parentid,
		subacct.code,
		ri.objid,
		ri.code,
		ri.title 
	  
	UNION ALL

	SELECT 
		ri.objid,
		CASE 
			WHEN subacct.objid IS NOT NULL THEN subacct.objid 
			WHEN acct.objid IS NOT NULL THEN acct.objid
			ELSE 'unmapped'
		END AS parentid,
		CASE 
			WHEN subacct.objid IS NOT NULL THEN subacct.objid 
			WHEN acct.objid IS NOT NULL THEN acct.objid
			ELSE 'unmapped'
		END AS accountid,
		CASE 
			WHEN acct.code IS NOT NULL THEN acct.code
			ELSE 'unmapped'
		END AS acctcode,
		CASE 
			WHEN subacct.code IS NOT NULL THEN subacct.code 
			ELSE 'unmapped'
		END AS subacctcode,
		ri.code,
		ri.title,
		'revenueitem' AS type,
		SUM(dci.amount) AS amount
	FROM directcash_collection dc
		INNER JOIN directcash_collection_item dci ON dc.objid = dci.parentid
		INNER JOIN itemaccount ri ON dci.item_objid = ri.objid 
		LEFT JOIN revenueitem_attribute attr ON ri.objid = attr.revitemid and attr.attribute_objid = 'ngasstandard'
		LEFT JOIN sreaccount acct ON attr.account_objid = acct.objid 
		LEFT JOIN revenueitem_attribute subattr ON ri.objid = subattr.revitemid and subattr.attribute_objid='ngassubaccount'
		LEFT JOIN sreaccount subacct ON subattr.account_objid = subacct.objid   
	WHERE dc.refdate BETWEEN $P{fromdate} AND $P{todate}
	GROUP BY 
		acct.objid,
		acct.parentid,
		acct.code,
		subacct.objid,
		subacct.parentid,
		subacct.code,
		ri.objid,
		ri.code,
		ri.title 
) t
ORDER BY t.acctcode, t.subacctcode, t.code 



#******************************************
# SRE Report
# Note: tempory add union to migrated tracs data 
#*****************************************

[getSREAcctGroups]
select distinct acctgroup from sreaccount where parentid is null 

[getSreRootAccountsByAccountGroup]
SELECT objid, code, title, type FROM sreaccount WHERE acctgroup=$P{acctgroup} ORDER BY code

[getSREStandardRevenueItemSummaries]
SELECT t.*,
	(SELECT TOP 1 target FROM sreaccount_incometarget WHERE objid=t.objid AND year=$P{year}) AS target
FROM (
	SELECT 
	CASE WHEN acct.objid IS NULL THEN 'unmapped' ELSE acct.objid END AS objid,
	CASE WHEN acct.parentid IS NULL THEN 'unmapped' ELSE acct.parentid END AS parentid,
	CASE WHEN acct.objid IS NULL THEN 'unmapped' ELSE acct.objid END AS accountid,
	CASE WHEN acct.code IS NULL THEN 'unmapped' ELSE acct.code END AS code,
	CASE WHEN acct.title IS NULL THEN 'unmapped' ELSE acct.title END AS title,
	CASE WHEN acct.type IS NULL THEN 'unmapped' ELSE acct.type END AS type,
	SUM(cri.amount) AS amount 
	FROM (
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
		) cc 
		INNER JOIN cashreceiptitem cri ON cc.objid = cri.receiptid 
		INNER JOIN itemaccount ri ON cri.item_objid = ri.objid 
		LEFT JOIN revenueitem_attribute attr ON ri.objid = attr.revitemid  and attr.attribute_objid='srestandard'
		LEFT JOIN sreaccount acct on acct.objid = attr.account_objid 
	GROUP BY 
		acct.objid,
		acct.parentid,
		acct.code,
		acct.title,
		acct.type
		
	UNION ALL

	SELECT 
		CASE WHEN acct.objid IS NULL THEN 'unmapped' ELSE acct.objid END AS objid,
		CASE WHEN acct.parentid IS NULL THEN 'unmapped' ELSE acct.parentid END AS parentid,
		CASE WHEN acct.objid IS NULL THEN 'unmapped' ELSE acct.objid END AS accountid,
		CASE WHEN acct.code IS NULL THEN 'unmapped' ELSE acct.code END AS code,
		CASE WHEN acct.title IS NULL THEN 'unmapped' ELSE acct.title END AS title,
		CASE WHEN acct.type IS NULL THEN 'unmapped' ELSE acct.type END AS type,
		SUM(dci.amount) AS amount
	FROM directcash_collection dc
		INNER JOIN directcash_collection_item dci ON dc.objid = dci.parentid
		INNER JOIN itemaccount ri ON dci.item_objid = ri.objid 
		LEFT JOIN revenueitem_attribute attr ON ri.objid = attr.revitemid  and attr.attribute_objid='srestandard'
		LEFT JOIN sreaccount acct on acct.objid = attr.account_objid 
	WHERE dc.refdate BETWEEN $P{fromdate} AND $P{todate} 
	GROUP BY 
		acct.objid,
		acct.parentid,
		acct.code,
		acct.title,
		acct.type	
	
	) t


[getSREExtendedRevenueItemSummaries]
SELECT t.*,
	(SELECT TOP 1 target FROM sreaccount_incometarget WHERE objid=t.accountid AND year=$P{year}) AS target
FROM (
	SELECT 
		CASE 
			WHEN subacct.parentid IS NOT NULL THEN subacct.parentid 
			WHEN acct.parentid IS NOT NULL THEN acct.parentid
			ELSE 'unmapped' 
		END AS parentid,
		CASE 
			WHEN subacct.objid IS NOT NULL THEN subacct.objid 
			WHEN acct.objid IS NOT NULL THEN acct.objid
			ELSE 'unmapped' 
		END AS accountid,
		CASE 
			WHEN acct.code IS NOT NULL THEN acct.code
			ELSE 'unmapped' 
		END AS parentcode,
		CASE 
			WHEN subacct.code IS NOT NULL THEN subacct.code 
			WHEN acct.code IS NOT NULL THEN acct.code
			ELSE 'unmapped' 
		END AS code,
		CASE 
			WHEN subacct.title IS NOT NULL THEN subacct.title 
			WHEN acct.title IS NOT NULL THEN acct.title
			ELSE 'unmapped' 
		END AS title,
		CASE 
			WHEN subacct.type IS NOT NULL THEN subacct.type 
			WHEN acct.type IS NOT NULL THEN acct.type
			ELSE 'unmapped' 
		END AS type,
		SUM(cri.amount) AS amount 
	FROM (
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
		) cc
		INNER JOIN cashreceiptitem cri ON cc.objid = cri.receiptid 
		INNER JOIN itemaccount ri ON cri.item_objid = ri.objid 
		LEFT JOIN revenueitem_attribute attr ON ri.objid = attr.revitemid and attr.attribute_objid = 'srestandard'
		LEFT JOIN sreaccount acct ON attr.account_objid = acct.objid 
		LEFT JOIN revenueitem_attribute subattr ON ri.objid = subattr.revitemid and subattr.attribute_objid='sresubaccount'
		LEFT JOIN sreaccount subacct ON subattr.account_objid = subacct.objid   
	GROUP BY 
		acct.objid,
		acct.parentid,
		acct.code,
		acct.title,
		acct.type,
		subacct.objid,
		subacct.parentid,
		subacct.code,
		subacct.title,
		subacct.type 

	UNION ALL

	SELECT 
		CASE 
			WHEN subacct.parentid IS NOT NULL THEN subacct.parentid 
			WHEN acct.parentid IS NOT NULL THEN acct.parentid
			ELSE 'unmapped' 
		END AS parentid,
		CASE 
			WHEN subacct.objid IS NOT NULL THEN subacct.objid 
			WHEN acct.objid IS NOT NULL THEN acct.objid
			ELSE 'unmapped' 
		END AS accountid,
		CASE 
			WHEN acct.code IS NOT NULL THEN acct.code
			ELSE 'unmapped' 
		END AS parentcode,
		CASE 
			WHEN subacct.code IS NOT NULL THEN subacct.code 
			WHEN acct.code IS NOT NULL THEN acct.code
			ELSE 'unmapped' 
		END AS code,
		CASE 
			WHEN subacct.title IS NOT NULL THEN subacct.title 
			WHEN acct.title IS NOT NULL THEN acct.title
			ELSE 'unmapped' 
		END AS title,
		CASE 
			WHEN subacct.type IS NOT NULL THEN subacct.type 
			WHEN acct.type IS NOT NULL THEN acct.type
			ELSE 'unmapped' 
		END AS type,
		SUM(dci.amount) AS amount
	FROM directcash_collection dc
		INNER JOIN directcash_collection_item dci ON dc.objid = dci.parentid
		INNER JOIN itemaccount ri ON dci.item_objid = ri.objid 
		LEFT JOIN revenueitem_attribute attr ON ri.objid = attr.revitemid and attr.attribute_objid = 'srestandard'
		LEFT JOIN sreaccount acct ON attr.account_objid = acct.objid 
		LEFT JOIN revenueitem_attribute subattr ON ri.objid = subattr.revitemid and subattr.attribute_objid='sresubaccount'
		LEFT JOIN sreaccount subacct ON subattr.account_objid = subacct.objid 
	WHERE dc.refdate BETWEEN $P{fromdate} AND $P{todate}  
	GROUP BY 
		acct.objid,
		acct.parentid,
		acct.code,
		acct.title,
		acct.type,
		subacct.objid,
		subacct.parentid,
		subacct.code,
		subacct.title,
		subacct.type 
	) t


[getSREDetailedRevenueItemSummaries]
SELECT t.*, 
	(SELECT TOP 1 target FROM sreaccount_incometarget WHERE objid=t.objid AND year=$P{year}) AS target
FROM (
	SELECT 
		ri.objid,
		CASE 
			WHEN subacct.objid IS NOT NULL THEN subacct.objid 
			WHEN acct.objid IS NOT NULL THEN acct.objid
			ELSE 'unmapped'
		END AS parentid,
		CASE 
			WHEN subacct.objid IS NOT NULL THEN subacct.objid 
			WHEN acct.objid IS NOT NULL THEN acct.objid
			ELSE 'unmapped'
		END AS accountid,

		CASE 
			WHEN acct.code IS NOT NULL THEN acct.code
			ELSE 'unmapped'
		END AS acctcode,
		CASE 
			WHEN subacct.code IS NOT NULL THEN subacct.code 
			ELSE 'unmapped'
		END AS subacctcode,

		ri.code,
		ri.title,
		'revenueitem' AS type,
		SUM(cri.amount) AS amount 
	FROM (
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
		) cc
		INNER JOIN cashreceiptitem cri ON cc.objid = cri.receiptid 
		INNER JOIN itemaccount ri ON cri.item_objid = ri.objid 
		LEFT JOIN revenueitem_attribute attr ON ri.objid = attr.revitemid and attr.attribute_objid = 'srestandard'
		LEFT JOIN sreaccount acct ON attr.account_objid = acct.objid 
		LEFT JOIN revenueitem_attribute subattr ON ri.objid = subattr.revitemid and subattr.attribute_objid='sresubaccount'
		LEFT JOIN sreaccount subacct ON subattr.account_objid = subacct.objid 
	GROUP BY 
		acct.objid,
		acct.parentid,
		acct.code,
		acct.title,
		subacct.objid,
		subacct.parentid,
		subacct.code,
		ri.objid,
		ri.code,
		ri.title 
	  
	UNION ALL

	SELECT 
		ri.objid,
		CASE 
			WHEN subacct.objid IS NOT NULL THEN subacct.objid 
			WHEN acct.objid IS NOT NULL THEN acct.objid
			ELSE 'unmapped'
		END AS parentid,
		CASE 
			WHEN subacct.objid IS NOT NULL THEN subacct.objid 
			WHEN acct.objid IS NOT NULL THEN acct.objid
			ELSE 'unmapped'
		END AS accountid,
		CASE 
			WHEN acct.code IS NOT NULL THEN acct.code
			ELSE 'unmapped'
		END AS acctcode,
		CASE 
			WHEN subacct.code IS NOT NULL THEN subacct.code 
			ELSE 'unmapped'
		END AS subacctcode,
		ri.code,
		ri.title,
		'revenueitem' AS type,
		SUM(dci.amount) AS amount
	FROM directcash_collection dc
		INNER JOIN directcash_collection_item dci ON dc.objid = dci.parentid
		INNER JOIN itemaccount ri ON dci.item_objid = ri.objid 
		LEFT JOIN revenueitem_attribute attr ON ri.objid = attr.revitemid and attr.attribute_objid = 'srestandard'
		LEFT JOIN sreaccount acct ON attr.account_objid = acct.objid 
		LEFT JOIN revenueitem_attribute subattr ON ri.objid = subattr.revitemid and subattr.attribute_objid='sresubaccount'
		LEFT JOIN sreaccount subacct ON subattr.account_objid = subacct.objid 
	WHERE dc.refdate BETWEEN $P{fromdate} AND $P{todate}
	GROUP BY 
		acct.objid,
		acct.parentid,
		acct.code,
		acct.title,
		subacct.objid,
		subacct.parentid,
		subacct.code,
		ri.objid,
		ri.code,
		ri.title
	) t	