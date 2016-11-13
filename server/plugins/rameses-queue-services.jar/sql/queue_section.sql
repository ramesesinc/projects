[getList]
SELECT 
	qs.*, qs.objid as sectionid, 
	qs.groupid as group_objid, 
	qg.title as group_title   
FROM queue_section qs 
	INNER JOIN queue_group qg on qs.groupid=qg.objid 
ORDER BY qs.groupid, qs.sortorder, qs.title 
