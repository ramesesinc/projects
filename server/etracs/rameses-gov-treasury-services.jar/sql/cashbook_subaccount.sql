[getList]
SELECT 
ugm.user_objid as objid, 
ugm.user_username as username, 
ugm.user_lastname as lastname,
ugm.user_firstname as firstname,
ug.role as subaccttype,
ugm.org_name,
u.jobtitle as title
FROM sys_usergroup_member ugm
INNER JOIN sys_usergroup ug ON ug.objid=ugm.usergroup_objid
INNER JOIN sys_user u ON u.objid=ugm.user_objid 
WHERE ugm.user_lastname LIKE $P{searchtext}
AND ug.role IN ('COLLECTOR', 'LIQUIDATING_OFFICER', 'CASHIER')