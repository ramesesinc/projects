[getList]
SELECT wo.*, a.title, CONCAT('workorder:', LOWER(a.objid)) AS _filetype 
FROM patientchart_workorder wo
INNER JOIN activity a ON a.objid=wo.activitytype
WHERE parentid = $P{parentid}

[updateState]
UPDATE patientchart_workorder SET state=$P{state} WHERE objid=$P{objid}