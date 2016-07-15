[getList]
SELECT * FROM stockrequest 
WHERE itemclass='AF' 
	AND (reqno LIKE $P{reqno} OR requester_name LIKE $P{requester})	
ORDER BY reqno desc 

[getLookup]
SELECT * FROM stockrequest 
WHERE itemclass='AF' 
	AND reqtype=$P{reqtype} 
	AND (reqno LIKE $P{reqno} OR requester_name LIKE $P{requester})	
	AND state='OPEN' 
ORDER BY reqno desc 
