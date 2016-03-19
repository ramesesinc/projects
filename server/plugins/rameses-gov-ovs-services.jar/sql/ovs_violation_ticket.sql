[getViolations]
SELECT
v2.*
FROM violation_ticket v1
INNER JOIN violation v2 on v1.violationid = v2.objid
WHERE parentid = $P{objid}

[getViolationsPerTicket]
SELECT
v.*,
vte.*
FROM violation_ticket_entry vte 
INNER JOIN violation v ON vte.violationid = v.objid
WHERE vte.parentid = $P{objid}

[findVehiclePerTicket]
SELECT
v.*,
vtv.*
FROM violation_ticket_vehicle vtv
INNER JOIN vehicle v ON vtv.vehicleid = v.objid 
WHERE vtv.parentid = $P{objid}
