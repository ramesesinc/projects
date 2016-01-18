[getListByActivity]
SELECT pc.*, IFNULL(t.description, '-') AS groupname, it.range AS item_range, it.unit AS item_unit, it.datatype
FROM patientchart_labresult pc
INNER JOIN labtest_item it ON it.objid=pc.item_objid
LEFT JOIN labtest_template t ON pc.groupid=t.objid
WHERE pc.activityid = $P{activityid}
ORDER BY t.description

[getListByWorkOrder]
SELECT pc.*, IFNULL(t.description, '-') AS groupname, it.range AS item_range, it.unit AS item_unit, it.datatype
FROM patientchart_labresult pc
INNER JOIN labtest_item it ON it.objid=pc.item_objid
LEFT JOIN labtest_template t ON pc.groupid=t.objid
WHERE pc.workorderid = $P{workorderid}
ORDER BY t.description
