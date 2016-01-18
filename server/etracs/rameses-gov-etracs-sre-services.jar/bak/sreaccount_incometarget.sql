[getList]
SELECT 
a.objid, a.code as account_code, 
a.title AS account_title,
(SELECT target FROM sreaccount_incometarget WHERE objid=a.objid AND year=${year} limit 1) AS target 
FROM sreaccount a 
WHERE a.type in ('subaccount', 'detail') and a.code LIKE $P{code} 
ORDER BY a.code  