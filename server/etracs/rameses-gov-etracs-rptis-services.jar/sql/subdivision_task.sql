[findOpenTask]
SELECT * FROM subdivision_task WHERE refid = $P{objid} AND enddate IS NULL 

[findReturnToInfo]
select *
from subdivision_task
where refid = $P{refid}
  and state = $P{state}
order by startdate desc 

[updateTaskAssignee]
UPDATE subdivision_task SET
	assignee_objid = $P{assigneeid},
	assignee_name = $P{assigneename},
	assignee_title = $P{assigneetitle}
WHERE objid = $P{objid}

[closeTask]
UPDATE subdivision_task SET 
	enddate=$P{enddate},
	assignee_name = $P{assigneename},
	assignee_title = $P{assigneetitle},
	actor_name = $P{assigneename},
	actor_title = $P{assigneetitle}
WHERE refid = $P{refid} AND enddate IS NULL 

[getTasks]
select * from subdivision_task where refid = $P{objid}

[removeOpenTask]
delete from subdivision_task where refid = $P{objid} and enddate is null


[findRecommederTask]
select * 
from subdivision_task 
where refid = $P{refid}
and state = 'recommender' 
order by startdate desc 
