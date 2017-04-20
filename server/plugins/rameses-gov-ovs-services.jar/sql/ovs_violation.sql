[getViolationsByViolator]
SELECT
v.*
FROM ovs_violation v 
INNER JOIN ovs_violation_ticket_entry vte ON v.objid = vte.violationid 
INNER JOIN ovs_violation_ticket vt ON vte.parentid = vt.objid 
WHERE v.objid = $P{violationid} 
AND vt.violator_objid = $P{violatorid}