[getList]
SELECT 
a.objid, a.code as account_code, 
a.title AS account_title,
(SELECT TOP 1 target FROM sreaccount_incometarget WHERE objid=a.objid AND year=${year}) AS target 
FROM sreaccount a 
WHERE a.type in ('subaccount', 'detail') and a.code LIKE $P{code} 
ORDER BY a.code 