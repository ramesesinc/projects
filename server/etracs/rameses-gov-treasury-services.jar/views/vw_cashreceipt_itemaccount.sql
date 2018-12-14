DROP VIEW IF EXISTS vw_cashreceipt_itemaccount
;
CREATE VIEW vw_cashreceipt_itemaccount AS 
SELECT 
  objid, state, code, title, description, type, fund_objid, fund_code, fund_title, 
  defaultvalue, valuetype, sortorder, org_objid AS orgid, hidefromlookup 
FROM itemaccount 
WHERE state='ACTIVE' 
  AND type IN ('REVENUE','NONREVENUE','PAYABLE') 
  AND (generic = 0 OR generic IS NULL)
