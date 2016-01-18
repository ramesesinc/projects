[getList]
SELECT pc.*, vs.title
FROM patientchart_vitalsign pc
INNER JOIN vitalsign vs ON pc.vitalsignid=vs.objid
WHERE activityid = $P{activityid}

[getColumns]
SELECT objid, name, title FROM vitalsign WHERE objid NOT IN ('W','BMI', 'H')

[getAllVitalSigns]
SELECT dttaken, vitalsignid AS name, value FROM patientchart_vitalsign 
WHERE chartid=$P{objid}
