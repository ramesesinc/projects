[getList]
SELECT t.* FROM roletemplate t
ORDER BY t.dtcreated DESC

[getLookupList]
SELECT t.* FROM roletemplate t
ORDER BY t.dtcreated DESC

[getLookupListByState]
SELECT t.* FROM roletemplate t
WHERE t.txnstate = $P{state}
ORDER BY t.dtcreated DESC

[getRoles]
SELECT r.*, s.title, s.domain, s.role
FROM roletemplate_usergroup r
INNER JOIN sys_usergroup s ON r.usergroupid = s.objid
WHERE r.parentid = $P{objid}

[getTemplatesByUserid]
SELECT r.*, u.objid AS usertemplateid
FROM roletemplate_user u
INNER JOIN roletemplate r ON u.roletemplate_objid = r.objid
WHERE u.user_objid = $P{userid}

[findUsergroupMemberByUseridAndUsergroupid]
SELECT * FROM sys_usergroup_member 
WHERE user_objid=$P{userid} AND usergroup_objid=$P{usergroupid}

[changeState]
UPDATE roletemplate SET txnstate = $P{txnstate}
WHERE objid = $P{objid}