
DROP VIEW IF EXISTS vw_cashreceipt_itemaccount_collectiontype;
CREATE VIEW vw_cashreceipt_itemaccount_collectiontype AS 

SELECT 
ia.objid,ia.state,ia.code,ia.title,ia.description,ia.type,ia.fund_objid,ia.fund_code,ia.fund_title,
CASE WHEN ca.defaultvalue =0 THEN ia.defaultvalue ELSE ca.defaultvalue END AS defaultvalue,
CASE WHEN ca.valuetype IS NULL THEN ia.valuetype ELSE ca.valuetype END AS valuetype, 
CASE WHEN ca.sortorder=0 THEN ia.sortorder ELSE ca.sortorder END AS sortorder,
null AS orgid, ca.collectiontypeid,
ia.hidefromlookup
FROM itemaccount ia
INNER JOIN collectiontype_account ca ON ca.account_objid=ia.objid
WHERE ia.parentid is NULL AND (ia.generic = 0 OR ia.generic IS NULL) AND ia.state='ACTIVE' AND ia.type IN ('REVENUE','NONREVENUE','PAYABLE')

UNION 

SELECT  
ia.objid,ia.state,ia.code,ia.title,ia.description,ip.type,ia.fund_objid,ia.fund_code,ia.fund_title,ia.defaultvalue,ia.valuetype, ia.sortorder, ia.org_objid AS orgid, ca.collectiontypeid 
,ia.hidefromlookup
FROM itemaccount ia 
INNER JOIN itemaccount ip ON ia.parentid = ip.objid
INNER JOIN collectiontype_account ca ON ca.account_objid=ip.objid
WHERE ip.generic = 1 AND ia.state='ACTIVE' AND ip.type IN ('REVENUE','NONREVENUE','PAYABLE')

UNION 

SELECT  
ia.objid,ia.state,ia.code,ia.title,ia.description,ia.type,ia.fund_objid,ia.fund_code,ia.fund_title,
CASE WHEN ca.defaultvalue =0 THEN ia.defaultvalue ELSE ca.defaultvalue END AS defaultvalue,
CASE WHEN ca.valuetype IS NULL THEN ia.valuetype ELSE ca.valuetype END AS valuetype, 
CASE WHEN ca.sortorder=0 THEN ia.sortorder ELSE ca.sortorder END AS sortorder,
ia.org_objid AS orgid, ca.collectiontypeid,
ia.hidefromlookup
FROM itemaccount ia 
INNER JOIN collectiontype_account ca ON ca.account_objid=ia.objid
WHERE NOT(ia.org_objid IS NULL) AND ia.state='ACTIVE' AND ia.type IN ('REVENUE','NONREVENUE','PAYABLE');
