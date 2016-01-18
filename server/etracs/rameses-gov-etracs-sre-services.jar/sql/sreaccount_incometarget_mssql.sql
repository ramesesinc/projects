[getList]
SELECT 
a.objid, a.code as account_code, 
CASE WHEN a.parentid IS NULL THEN 'ROOT' ELSE a.parentid END AS parentid,
a.type, 
a.title AS account_title,
(SELECT TOP 1 target FROM sreaccount_incometarget WHERE objid=a.objid AND year=${year}) AS target 
FROM sreaccount a 
WHERE a.code LIKE $P{code} 
ORDER BY a.code




