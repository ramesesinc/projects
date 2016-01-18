[getAgingList]
SELECT a.objid AS loanapp_objid, a.appno AS loanapp_appno, a.loantype AS loanapp_loantype, a.apptype AS loanapp_apptype,
	a.loanamount AS loanapp_loanamount, ac.dtreleased AS loanapp_dtreleased, l.dtmatured AS loanapp_dtmatured,
	l.balance AS loanapp_balance, a.borrower_objid AS borrower_objid, a.borrower_name AS borrower_name, r.code AS route_code, 
	r.description AS route_description, r.area AS route_area,  DATEDIFF(CURDATE(), ac.dtreleased) AS daysaged
FROM loan_ledger l
INNER JOIN loanapp a ON l.appid = a.objid
INNER JOIN loanapp_capture ac ON a.objid = ac.objid
INNER JOIN loan_route r ON a.route_code = r.code
WHERE l.state = 'OPEN'
	AND l.dtmatured IS NOT NULL

[getOverdueList]
SELECT a.objid AS loanapp_objid, a.appno AS loanapp_appno, a.loantype AS loanapp_loantype, a.apptype AS loanapp_apptype,
	a.loanamount AS loanapp_loanamount, ac.dtreleased AS loanapp_dtreleased, l.dtmatured AS loanapp_dtmatured,
	l.balance AS loanapp_balance, a.borrower_objid AS borrower_objid, a.borrower_name AS borrower_name, r.code AS route_code, 
	r.description AS route_description, r.area AS route_area
FROM loan_ledger l
INNER JOIN loanapp a ON l.appid = a.objid
INNER JOIN loanapp_capture ac ON a.objid = ac.objid
INNER JOIN loan_route r ON a.route_code = r.code
WHERE l.state = 'OPEN'
	AND l.dtmatured IS NOT NULL

[getUserList]
SELECT 
	u.objid, 
	u.lastname, 
	u.firstname, 
	u.middlename, 
	u.jobtitle, 
	u.jobtitle AS title, 
	ug.domain,
	ug.role,
	ugm.objid AS usergroupmemberid, 
	ugm.usergroup_objid,
	u.txncode  
FROM (
	SELECT objid FROM sys_user WHERE lastname LIKE $P{searchtext}
	UNION
	SELECT objid FROM sys_user WHERE firstname LIKE $P{searchtext}
	UNION
	SELECT objid FROM sys_user WHERE middlename LIKE $P{searchtext}
) q INNER JOIN sys_user u ON q.objid = u.objid
	INNER JOIN sys_usergroup_member ugm ON u.objid = ugm.user_objid
	INNER JOIN sys_usergroup ug ON ug.objid=ugm.usergroup_objid 
WHERE ug.role IN (${roles})  
	AND ug.domain IN (${domains})
ORDER BY u.lastname 