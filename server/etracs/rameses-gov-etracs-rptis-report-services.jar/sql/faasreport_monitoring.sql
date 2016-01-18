[getTxnLogActions]
SELECT DISTINCT
	action 
FROM txnlog
WHERE ref = 'FAAS'



[findActionCountSummation]
SELECT 
	count(distinct refid) as sum
FROM txnlog
WHERE ref = 'FAAS'
AND action = $P{action}
AND userid = $P{userid}
AND txndate LIKE $P{date}



[getUsers]
SELECT DISTINCT
	userid, username
FROM txnlog
WHERE ref = 'FAAS'
AND userid LIKE $P{userid}


[getData]
SELECT 
username AS USER, ACTION 
FROM txnlog 
WHERE ref = 'FAAS'
AND userid LIKE $P{userid}
AND txndate LIKE $P{date}


