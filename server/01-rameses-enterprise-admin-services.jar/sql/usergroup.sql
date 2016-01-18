[getLookup]
SELECT * FROM sys_usergroup 
WHERE objid LIKE $P{searchtext}

[getRootNodes]
SELECT DISTINCT
	ug.domain as caption, ug.domain as domain, '' as usergroupid, 'domain' as filetype 
FROM sys_usergroup ug 

[getChildNodes]
SELECT DISTINCT
	ug.title as caption, ug.domain as domain, ug.objid as usergroupid, 
	'usergroup-folder' as filetype, ug.orgclass 
FROM sys_usergroup ug 
WHERE 
	ug.domain=$P{domain} 

[getList]
SELECT DISTINCT
	ugm.objid, ugm.user_username, ugm.user_lastname, ugm.user_firstname, 
	ugm.org_name, sg.name AS securitygroup_name 
FROM sys_usergroup ug 
	INNER JOIN sys_usergroup_member ugm ON ug.objid=ugm.usergroup_objid ${usergroupfilter} 
	LEFT JOIN sys_securitygroup sg ON ugm.securitygroup_objid=sg.objid 
WHERE 
	ug.domain=$P{domain} 
ORDER BY 
	ugm.user_lastname, ugm.user_firstname 

[getAdminList]
SELECT uga.* FROM sys_usergroup_admin uga
WHERE uga.usergroupid=$P{usergroupid}

[search]
SELECT ugm.objid, su.username, su.name, sg.name AS securitygroup_name, so.name as org_name
FROM sys_usergroup_member ugm
INNER JOIN sys_user su ON su.objid=ugm.user_objid
INNER JOIN sys_securitygroup sg ON ugm.securitygroup_objid=sg.objid 
LEFT JOIN sys_org so ON ugm.org_objid=so.objid
WHERE su.name like $P{name}  

[changeState-approved]
UPDATE sys_usergroup_member SET state='APPROVED' WHERE objid=$P{objid} AND state='DRAFT' 

[getPermissions]
SELECT * FROM sys_usergroup_permission WHERE usergroup_objid=$P{objid}

[removePermissions]
delete FROM sys_usergroup_permission WHERE usergroup_objid=$P{objid}

[findPermission]
SELECT * FROM sys_usergroup_permission 
WHERE object=$P{object} 
	 and permission=$P{permission}
	 and usergroup_objid=$P{usergroupid} 

[findDuplicateWithoutOrg]
SELECT * FROM sys_usergroup_member 
WHERE user_objid=$P{userid} AND usergroup_objid=$P{usergroupid} AND org_objid IS NULL
	
[findDuplicateWithOrg]
SELECT * FROM sys_usergroup_member 
WHERE user_objid=$P{userid} AND usergroup_objid=$P{usergroupid} AND org_objid=$P{orgid}

