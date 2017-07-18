[getViolationsByViolator]
SELECT
v.*
FROM violation v 
INNER JOIN violation_ticket_entry vte ON v.objid = vte.violationid 
INNER JOIN violation_ticket vt ON vte.parentid = vt.objid 
WHERE v.objid = $P{violationid} 
AND vt.violator_objid = $P{violatorid}