[getList]
SELECT pa.*,a.title 
FROM patientchart_activity pa
INNER JOIN activity a ON pa.activity_objid=a.objid
WHERE pa.parentid=$P{objid}

[updateState]
UPDATE patientchart_activity SET state=$P{state},enddate=$P{enddate} WHERE objid=$P{objid}

[findHasPendingActivities]
SELECT COUNT(*) AS count 
FROM patientchart_activity 
WHERE parentid=$P{objid} AND state IS NULL OR state='OPEN'