[getList]
SELECT * FROM fundrequest 
WHERE reqno LIKE $P{searchtext} 
ORDER BY dtcreated 

[getPendingList]
SELECT r.* 
FROM fundrequest_pending p 
	INNER JOIN fundrequest r ON p.objid=r.objid 
WHERE r.state='PENDING' 
ORDER BY r.dtcreated 

[getListByState]
SELECT * FROM fundrequest 
WHERE reqno LIKE $P{searchtext} AND state=$P{state} 
ORDER BY dtcreated DESC 

[getReportList]
SELECT 
	req.dtcreated, req.author_objid, req.author_username, 
	req.amount, req.remarks, req.reqno, req.posting_date, 
	req.posting_userid, req.posting_username, 
	ur.name AS requester, ua.name AS approver, 
	req.posting_date AS dtposted  
FROM fundrequest req 
	INNER JOIN sys_user ur ON req.author_objid=ur.objid 
	INNER JOIN sys_user ua ON req.posting_userid=ua.objid 
WHERE req.posting_date BETWEEN $P{startdate} AND $P{enddate} 
	AND req.state='APPROVED' 
ORDER BY req.posting_date 