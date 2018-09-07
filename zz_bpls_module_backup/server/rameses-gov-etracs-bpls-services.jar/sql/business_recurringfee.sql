[getList]
SELECT rf.*, r.code AS account_code 
FROM business_recurringfee rf
INNER JOIN itemaccount r ON r.objid=rf.account_objid
WHERE rf.businessid=$P{businessid}