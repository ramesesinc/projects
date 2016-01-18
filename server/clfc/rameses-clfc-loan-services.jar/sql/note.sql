[getList]
SELECT b.*, 
	r.description AS route_description, r.area AS route_area  
FROM ( 
	SELECT objid FROM note WHERE borrower_name LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM note WHERE loanapp_appno LIKE $P{searchtext} 
)bt 
	INNER JOIN note b ON bt.objid=b.objid 
	INNER JOIN loan_route r ON b.route_code=r.code 
WHERE b.state=$P{state} 
ORDER BY borrower_name 

[getActiveList]
SELECT b.*, 
	r.description AS route_description, r.area AS route_area  
FROM ( 
	SELECT objid FROM note_active WHERE borrower_name LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM note_active WHERE loanapp_appno LIKE $P{searchtext} 
)bt 
	INNER JOIN note b ON bt.objid=b.objid 
	INNER JOIN loan_route r ON b.route_code=r.code 
WHERE b.state=$P{state} 
ORDER BY borrower_name 

[getActiveExpiredList]
SELECT np.objid FROM note_active np 
	INNER JOIN note n ON np.objid=n.objid AND IFNULL(n.dtend,CURRENT_DATE()) < CURRENT_DATE() 
WHERE 
	n.state='APPROVED' 

[getApprovedList]
SELECT n.* 
FROM note_active a 
	INNER JOIN note n ON a.objid=n.objid 
WHERE 
	n.state='APPROVED' AND IFNULL(n.dtend,CURRENT_DATE()) >= CURRENT_DATE() 

[getListByLedgerid]
SELECT n.*
FROM note n 
WHERE n.ledger_objid = $P{ledgerid}
ORDER BY n.dtcreated DESC

[getListByLedgeridAndState]
SELECT n.*
FROM note n 
WHERE n.ledger_objid = $P{ledgerid}
	AND n.state = $P{state}
ORDER BY n.dtcreated DESC

[getApprovedListByLedger]
SELECT n.* 
FROM note_active a 
	INNER JOIN note n ON a.objid=n.objid 
WHERE 
	n.ledger_objid = $P{ledgerid}
	AND n.state='APPROVED' AND IFNULL(n.dtend,CURRENT_DATE()) >= CURRENT_DATE() 
ORDER BY n.dtcreated DESC
LIMIT 3 