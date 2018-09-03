

/*=============================================================== 	
* Use the script below to manually update the inspectedby_objid
* field for inspection done after the record has been approved.
*
* This query returns the list of examiners 
* with inspectedby_objid field not yet set.
* For each result, update the inspectedby_objid 
* by setting the firstname, lastname and notedby values
* in the query "-- update inspectedby_userid field"
*
*
*  select distinct notedby from examiner_finding 
*  where inspectedby_objid is null
*  order by notedby;
*
================================================================*/

-- update inspectedby_userid field
update examiner_finding set 
	inspectedby_objid = (select objid from sys_user where firstname like 'mirasol%' and lastname like 'gaspar%')
where inspectedby_objid is null 
and notedby like 'MIRASOL%GASPAR%'
;
