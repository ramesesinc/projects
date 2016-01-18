[getEncodingStatistics]
SELECT 
	username AS name,
	SUM(CASE WHEN ref='IndividualEntity' and action = 'create' then 1 else 0 end) as individualcnt,
	SUM(CASE WHEN ref='JuridicalEntity' and action = 'create' then 1 else 0 end) as juridicalcnt,
	SUM(CASE WHEN ref='MultipleEntity' and action = 'create' then 1 else 0 end) as multiplecnt,
	SUM(CASE WHEN action = 'create' then 1 else 0 end) as total
FROM  txnlog 
WHERE ref in ('individualentity', 'juridicalentity', 'multipleentity')
  AND txndate BETWEEN $P{fromdate} AND $P{todate}
GROUP BY username 