/* 254032-03019.02 */

/*==============================================
* EXAMINATION UPDATES
==============================================*/

alter table examiner_finding add inspectedby_objid varchar(50)
go 
alter table examiner_finding add inspectedby_name varchar(100)
go 
alter table examiner_finding add inspectedby_title varchar(50)
go 
alter table examiner_finding add doctype varchar(50)
go 

create index ix_examiner_finding_inspectedby_objid on examiner_finding(inspectedby_objid)
go 


update e set 
	e.inspectedby_objid = (select top 1 assignee_objid from faas_task where refid = f.objid and state = 'examiner' order by enddate desc),
	e.inspectedby_name = e.notedby,
	e.inspectedby_title = e.notedbytitle,
	e.doctype = 'faas'
from examiner_finding e, faas f
where e.parent_objid = f.objid
go 

update e set 
	e.inspectedby_objid = (select top 1 assignee_objid from subdivision_task where refid = s.objid and state = 'examiner' order by enddate desc),
	e.inspectedby_name = e.notedby,
	e.inspectedby_title = e.notedbytitle,
	e.doctype = 'subdivision'
from examiner_finding e, subdivision s	
where e.parent_objid = s.objid
go 

update e set 
	e.inspectedby_objid = (select top 1 assignee_objid from consolidation_task where refid = c.objid and state = 'examiner' order by enddate desc),
	e.inspectedby_name = e.notedby,
	e.inspectedby_title = e.notedbytitle,
	e.doctype = 'consolidation'
from  examiner_finding e, consolidation c	
where e.parent_objid = c.objid
go 

update e set 
	e.inspectedby_objid = (select top 1 assignee_objid from cancelledfaas_task where refid = c.objid and state = 'examiner' order by enddate desc),
	e.inspectedby_name = e.notedby,
	e.inspectedby_title = e.notedbytitle,
	e.doctype = 'cancelledfaas'
from examiner_finding e, cancelledfaas c	
where e.parent_objid = c.objid
go 



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
go 
*
================================================================*/

-- update inspectedby_userid field
update examiner_finding set 
	inspectedby_objid = (select objid from sys_user where firstname like 'mirasol%' and lastname like 'gaspar%')
where inspectedby_objid is null 
and notedby like 'MIRASOL%GASPAR%'
go 