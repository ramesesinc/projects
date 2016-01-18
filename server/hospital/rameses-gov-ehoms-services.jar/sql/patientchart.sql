[getList]
SELECT pc.*, 
p.lastname AS patient_lastname, 
p.firstname AS patient_firstname, 
p.birthdate AS patient_birthdate,
ph.lastname AS physician_lastname,  
ph.firstname AS physician_firstname
FROM patientchart pc 
INNER JOIN patient p ON pc.patient_objid=p.objid
LEFT JOIN physician ph ON pc.attendingphysician_objid=ph.objid
WHERE pc.section=$P{section} AND state=$P{state}

[getListByActivity]
SELECT pc.*, 
p.lastname AS patient_lastname, 
p.firstname AS patient_firstname, 
ph.lastname AS physician_lastname,  
ph.firstname AS physician_firstname,
pa.objid AS activityid,
pc.objid AS chartid
FROM patientchart pc 
INNER JOIN patient p ON pc.patient_objid=p.objid
LEFT JOIN physician ph ON pc.attendingphysician_objid=ph.objid
INNER JOIN patientchart_activity pa ON pa.parentid=pc.objid 
WHERE pa.activity_objid = $P{activity}

[updateState]
UPDATE patientchart SET state=$P{state} WHERE objid=$P{objid}

[updateDisposition]
UPDATE patientchart SET disposition=$P{disposition}, state=$P{state}, dtclosed=$P{dtclosed} WHERE objid=$P{objid}

[updateAdmission]
UPDATE patientchart SET disposition=$P{disposition}, state=$P{state}, section=$P{section} WHERE objid=$P{objid}

[updatePhysician]
UPDATE patientchart 
SET attendingphysician_objid=$P{id}, 
attendingphysician_name=$P{name} 
WHERE objid=$P{objid}

[getHistory]
SELECT * FROM patientchart WHERE patient_objid=$P{objid}

[getListByPhysician]
SELECT pc.*, 
p.lastname AS patient_lastname, 
p.firstname AS patient_firstname, 
p.birthdate AS patient_birthdate,
p.gender AS patient_gender,
ph.lastname AS physician_lastname,  
ph.firstname AS physician_firstname,
pc.objid AS chartid
FROM patientchart pc 
INNER JOIN patient p ON pc.patient_objid=p.objid
INNER JOIN physician ph ON pc.attendingphysician_objid=ph.objid
WHERE pc.attendingphysician_objid = $P{physicianid}
AND pc.state = $P{state}

[getListAdmitted]
SELECT pc.*, 
p.lastname AS patient_lastname, 
p.firstname AS patient_firstname, 
ph.lastname AS physician_lastname,  
ph.firstname AS physician_firstname
FROM patientchart pc 
INNER JOIN patient p ON pc.patient_objid=p.objid
LEFT JOIN physician ph ON pc.attendingphysician_objid=ph.objid
INNER JOIN patientchart_admission pa ON pa.chartid=pc.objid
WHERE pc.state=$P{state}

[getTodoList]
SELECT w.objid, w.state, w.controlno as refno, a.title AS activity, w.requester_name,  w.section, w.dtfiled, w.activitytype
FROM patientchart_workorder w
INNER JOIN activity a ON w.activitytype=a.objid
LEFT JOIN patientchart_activity pa ON pa.workorderid=w.objid
WHERE w.parentid=$P{objid} AND pa.objid IS NULL 
ORDER BY w.dtfiled DESC

[getActivityList]
SELECT pa.objid, w.state, w.controlno as refno, a.title AS activity, w.requester_name,  w.section, w.dtfiled, w.activitytype,
pa.startdate, pa.enddate, pa.remarks 
FROM patientchart_workorder w
INNER JOIN activity a ON w.activitytype=a.objid
INNER JOIN patientchart_activity pa ON pa.workorderid=w.objid
WHERE w.parentid=$P{objid}  
ORDER BY w.dtfiled DESC