[getUsers]
select x.* 
from (
	select distinct u.objid, u.name, u.jobtitle  
	from sys_user u 
	where exists(select * from faas_task where assignee_objid = u.objid and state = 'examiner') 

	union 

	select distinct u.objid, u.name, u.jobtitle 
	from sys_user u 
	where exists(select * from consolidation_task where assignee_objid = u.objid and state = 'examiner') 

	union 

	select distinct u.objid, u.name, u.jobtitle  
	from sys_user u 
	where exists(select * from subdivision_task where assignee_objid = u.objid and state = 'examiner') 

	union 

	select distinct u.objid, u.name, u.jobtitle  
	from sys_user u 
	where exists(select * from cancelledfaas_task where assignee_objid = u.objid and state = 'examiner') 
) x 
order by x.name 


[getExaminationFindings]
select x.* 
from (
	select 
		ef.dtinspected, ef.findings, ef.recommendations, ef.notedby, ef.notedbytitle,
		f.tdno as refno, 
		(select assignee_objid from faas_task where refid = f.objid and state = 'examiner' order by enddate desc limit 1) as userid
	from examiner_finding ef
		inner join faas f on ef.parent_objid = f.objid 
	where ef.dtinspected >= $P{startdate} and ef.dtinspected < $P{enddate}

	union all 

	select 
		ef.dtinspected, ef.findings, ef.recommendations, ef.notedby, ef.notedbytitle,
		concat('SD#', s.txnno) as refno,
		(select assignee_objid from subdivision_task where refid = s.objid and state = 'examiner' order by enddate desc limit 1) as userid
	from examiner_finding ef
		inner join subdivision s on ef.parent_objid = s.objid 
	where ef.dtinspected >= $P{startdate} and ef.dtinspected < $P{enddate}

	union all 

	select 
		ef.dtinspected, ef.findings, ef.recommendations, ef.notedby, ef.notedbytitle,
		concat('CS#',c.txnno) as refno,
		(select assignee_objid from consolidation_task where refid = c.objid and state = 'examiner' order by enddate desc limit 1) as userid
	from examiner_finding ef
		inner join consolidation c on ef.parent_objid = c.objid 
	where ef.dtinspected >= $P{startdate} and ef.dtinspected < $P{enddate}
) x
where x.userid like $P{userid}
order by x.dtinspected



