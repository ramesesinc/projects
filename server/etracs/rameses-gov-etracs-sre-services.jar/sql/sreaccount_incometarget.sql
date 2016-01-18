[getList]
SELECT 
a.objid, a.code as account_code, 
CASE WHEN a.parentid IS NULL THEN 'ROOT' ELSE a.parentid END AS parentid,
a.type, 
a.title AS account_title,
(SELECT target FROM sreaccount_incometarget WHERE objid=a.objid AND year=${year} limit 1) AS target 
FROM sreaccount a 
WHERE a.code LIKE $P{code} 
ORDER BY a.code




