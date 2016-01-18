[getList]
SELECT wo.*, pc.chartno, p.lastname AS patient_lastname, p.firstname AS patient_firstname, 
	a.title AS activity_title, CONCAT('workorder:', LOWER(a.objid)) AS _filetype  
FROM patientchart_workorder wo
INNER JOIN patientchart pc ON pc.objid=wo.parentid
INNER JOIN patient p ON pc.patient_objid=p.objid
INNER JOIN activity a ON a.objid=wo.activitytype
WHERE wo.activitytype IN (${activityid}) AND wo.state=$P{state}


