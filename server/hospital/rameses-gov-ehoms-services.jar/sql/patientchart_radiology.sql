[getListByActivity]
SELECT pr.*, (
	SELECT  COUNT(*) FROM patientchart_radiology_attachment WHERE parentid=pr.objid ) AS attachmentcount
FROM patientchart_radiology pr
WHERE pr.activityid = $P{activityid}

[getListByWorkOrder]
SELECT pr.*, (
	SELECT  COUNT(*) FROM patientchart_radiology_attachment WHERE parentid=pr.objid ) AS attachmentcount 
FROM patientchart_radiology pr
WHERE pr.workorderid = $P{workorderid}

[getAttachments]
SELECT objid, thumbnail, title 
FROM patientchart_radiology_attachment WHERE parentid=$P{objid}

[findAttachment]
SELECT title, image FROM patientchart_radiology_attachment 
WHERE objid=$P{objid}

