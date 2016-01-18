[getList]
SELECT b.*, 
	r.description AS route_description, r.area AS route_area, 
	x.code AS type_code, x.name AS type_name 
FROM ( 
	SELECT objid FROM loan_exemption WHERE borrower_name LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM loan_exemption WHERE loanapp_appno LIKE $P{searchtext} 
)bt 
	INNER JOIN loan_exemption b ON bt.objid=b.objid 
	INNER JOIN loan_route r ON b.route_code=r.code 
	INNER JOIN exemptiontype x ON b.type_objid=x.objid 
WHERE b.state=$P{state} 
ORDER BY b.dtcreated, b.borrower_name 

[getActiveList]
SELECT b.*, 
	r.description AS route_description, r.area AS route_area, 
	x.code AS type_code, x.name AS type_name   
FROM ( 
	SELECT objid FROM loan_exemption_active WHERE borrower_name LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM loan_exemption_active WHERE loanapp_appno LIKE $P{searchtext} 
)bt 
	INNER JOIN loan_exemption b ON bt.objid=b.objid 
	INNER JOIN loan_route r ON b.route_code=r.code 
	INNER JOIN exemptiontype x ON b.type_objid=x.objid 
WHERE b.state=$P{state} 
ORDER BY b.dtcreated, b.borrower_name 

[getActiveExpiredList]
SELECT np.objid FROM loan_exemption_active np 
	INNER JOIN loan_exemption n ON np.objid=n.objid AND n.dtend < CURRENT_DATE() 
WHERE 
	n.state='APPROVED' 

[getApprovedList]
SELECT n.* 
FROM loan_exemption_active a 
	INNER JOIN loan_exemption n ON a.objid=n.objid 
WHERE 
	n.state='APPROVED' AND n.dtend >= CURRENT_DATE() 


[getForExemptions]
SELECT * FROM loan_ledger_billing_detail
WHERE parentid=$P{parentid}
	AND txndate=$P{txndate}
	AND objid NOT IN(SELECT bpd.objid
						FROM batch_collectionsheet bp
						INNER JOIN batch_collectionsheet_detail bpd
						ON bp.objid=bpd.parentid
						WHERE bp.objid=$P{parentid}
							AND bp.txndate=$P{txndate})

[getExemptionsByStartdateAndEnddateAndLedgerid]
SELECT * FROM loan_exemption
WHERE txndate BETWEEN $P{startdate} AND $P{enddate}
	AND ledgerid=$P{ledgerid}
	AND state='APPROVED'

[findExemptionByDateAndLedgerid]
SELECT * FROM loan_exemption
WHERE txndate=$P{txndate}
	AND ledgerid=$P{ledgerid}
	AND state='APPROVED'