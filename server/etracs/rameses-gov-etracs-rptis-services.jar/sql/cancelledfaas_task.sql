[findOpenTask]
SELECT * FROM cancelledfaas_task WHERE refid = $P{objid} AND enddate IS NULL 
  

[findReturnToInfo]
select *
from cancelledfaas_task
where refid = $P{refid}
  and state = $P{state}
order by startdate desc 

[closeTask]
UPDATE cancelledfaas_task SET 
	enddate=$P{enddate},
	assignee_name = $P{assigneename},
	assignee_title = $P{assigneetitle}
WHERE refid = $P{refid} AND enddate IS NULL 


[updateTaskAssignee]
UPDATE cancelledfaas_task SET
	assignee_objid = $P{assigneeid},
	assignee_name = $P{assigneename},
	assignee_title = $P{assigneetitle}
WHERE objid = $P{objid}


[getAssignees]
SELECT distinct assignee_objid, assignee_name, assignee_title 
FROM cancelledfaas_task 
WHERE refid = $P{refid}
  AND assignee_objid IS NOT NULL 
  AND assignee_objid <> $P{assigneeid}
ORDER BY startdate DESC 

[deleteOpenTask]
delete from cancelledfaas_task where refid = $P{objid} and enddate is null 

[findRecommederTask]
select * 
from cancelledfaas_task 
where refid = $P{refid}
and state = 'recommender' 
order by startdate desc 

[getTasks]
select * from cancelledfaas_task where refid = $P{objid} order by startdate