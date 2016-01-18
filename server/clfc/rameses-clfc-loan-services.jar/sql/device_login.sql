[findFieldCollectorByUsername]
SELECT u.objid, u.pwd, u.username, u.name
FROM sys_user u
 INNER JOIN sys_usergroup_member ugm ON u.objid=ugm.user_objid
WHERE u.username=$P{username} AND
 ugm.usergroupid='LOAN_FIELD_COLLECTOR'
