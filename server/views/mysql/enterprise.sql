drop view if exists sys_user_role
;
create view sys_user_role as 
select 
	`u`.`objid` AS `objid`,
	`u`.`lastname` AS `lastname`,
	`u`.`firstname` AS `firstname`,
	`u`.`middlename` AS `middlename`,
	`u`.`username` AS `username`,
	concat(`u`.`lastname`,', ',`u`.`firstname`,(case when isnull(`u`.`middlename`) then '' else concat(' ',`u`.`middlename`) end)) AS `name`,
	`ug`.`role` AS `role`,
	`ug`.`domain` AS `domain`,
	`ugm`.`org_objid` AS `orgid`,
	`u`.`txncode` AS `txncode`,
	`u`.`jobtitle` AS `jobtitle`,
	`ugm`.`objid` AS `usergroupmemberid`,
	`ugm`.`usergroup_objid` AS `usergroup_objid` 
from `sys_usergroup_member` `ugm` 
	inner join `sys_usergroup` `ug` on `ug`.`objid` = `ugm`.`usergroup_objid`
	inner join `sys_user` `u` on `u`.`objid` = `ugm`.`user_objid` 
;