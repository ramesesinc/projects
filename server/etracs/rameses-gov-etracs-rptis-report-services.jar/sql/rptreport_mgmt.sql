[getList]
select t.* 
from (
	select 
		1 as task_typeid,
		case when ft.assignee_name is null then 0 else 1 end as task_assigneeid,
		sn.idx as task_idx,
		'FAAS' as type,
		case when ft.assignee_name is null then 'UN-ASSIGNED' else ft.assignee_name end as assignee_name,
		sn.title as taskstate,
		count(*) as count
	from faas_list fl
		inner join faas_task ft on fl.taskid = ft.objid 
		inner join sys_wf_node sn on ft.state = sn.name
	where fl.lguid = $P{lguid}
		and fl.state not in ('current', 'cancelled')
		and sn.processname = 'faas'
	group by 
		ft.assignee_name,
		sn.title,
		sn.idx 

	union all 

	select 
		2 as task_typeid,
		case when st.assignee_name is null then 0 else 1 end as task_assigneeid,
		sn.idx as task_idx,
		'Subdivision' as type,
		case when st.assignee_name is null then 'UN-ASSIGNED' else st.assignee_name end as assignee_name,
		sn.title as taskstate,
		count(*) as count
	from subdivision s
		inner join subdivision_task st on s.objid = st.refid 
		inner join sys_wf_node sn on st.state = sn.name 
	where s.lguid = $P{lguid}
	and s.state not in ('approved')
	and sn.processname = 'subdivision'
	and st.enddate is null 
	group by 
		st.assignee_name,
		st.assignee_title,
		sn.title,
		sn.idx

	union all 


	select 
		3 as task_typeid,
		case when st.assignee_name is null then 0 else 1 end as task_assigneeid,
		sn.idx as task_idx,
		'Consolidation' as type,
		case when st.assignee_name is null then 'UN-ASSIGNED' else st.assignee_name end as assignee_name,
		sn.title as taskstate,
		count(*) as count
	from consolidation c
		inner join consolidation_task st on c.objid = st.refid 
		inner join sys_wf_node sn on st.state = sn.name 
	where c.lguid = $P{lguid}
	and c.state not in ('approved')
	and sn.processname = 'consolidation'
	and st.enddate is null 
	group by 
		st.assignee_name,
		sn.title,
		sn.idx

	union all 

	
	select 
		4 as task_typeid,
		case when st.assignee_name is null then 0 else 1 end as task_assigneeid,
		sn.idx as task_idx,
		'Cancelled FAAS' as type,
		case when st.assignee_name is null then 'UN-ASSIGNED' else st.assignee_name end as assignee_name,
		sn.title as taskstate,
		count(*) as count
	from cancelledfaas c
		inner join cancelledfaas_task st on c.objid = st.refid 
		inner join sys_wf_node sn on st.state = sn.name 
	where c.lguid = $P{lguid}
	and c.state not in ('approved')
	and sn.processname = 'cancelledfaas'
	and st.enddate is null 
	group by 
		st.assignee_name,
		sn.title,
		sn.idx
)t 
order by 
	t.task_typeid,
	t.task_assigneeid,
	t.assignee_name,
	t.task_idx
