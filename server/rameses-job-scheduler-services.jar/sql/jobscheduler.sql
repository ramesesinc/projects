[collectExpiredJobs]
INSERT INTO sys_jobscheduler_task_processing (objid)
SELECT objid
FROM sys_jobscheduler_task WHERE (expirydate IS NULL OR expirydate <= $P{serverDate}) 
AND state='ACTIVE'
AND NOT objid IN (SELECT objid FROM sys_jobscheduler_task_processing )
AND NOT objid IN (SELECT objid FROM sys_jobscheduler_task_completed )
AND NOT objid IN (SELECT objid FROM sys_jobscheduler_task_error )

[getForProcessingJobs]
SELECT info.* 
FROM sys_jobscheduler_task info
INNER JOIN sys_jobscheduler_task_processing j ON info.objid=j.objid

[getCompletedJobs]
SELECT info.* 
FROM sys_jobscheduler_task info
INNER JOIN sys_jobscheduler_task_completed j ON info.objid=j.objid

[removeProcessingLock]
DELETE FROM sys_jobscheduler_task_processing WHERE objid=$P{objid}


[transferToCompleted]
INSERT INTO sys_jobscheduler_task_completed (objid) VALUES ($P{objid})

[removeCompletedLock]
DELETE FROM sys_jobscheduler_task_completed WHERE objid=$P{objid}

[updateExpirydate]
UPDATE sys_jobscheduler_task SET expirydate=$P{expirydate} WHERE objid=$P{objid}

[addError]
INSERT INTO sys_jobscheduler_task_error (objid,msg) VALUES ($P{objid},$P{msg})

[getList]
SELECT * FROM sys_jobscheduler_task

[updateState]
UPDATE sys_jobscheduler_task SET state=$P{state} WHERE objid=$P{objid}

[activate]
UPDATE sys_jobscheduler_task SET state='ACTIVE', expirydate = $P{expirydate} WHERE objid=$P{objid}
