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

[getViolationTicketsByPaymentRefid]
select distinct 
	vt.objid, vt.ticketno, 
	vt.apprehensionofficer_name as officername  
from payment op 
	inner join payment_item opi on opi.parentid=op.objid 
	inner join violation_ticket_entry vte on vte.objid=opi.refid 
	inner join violation_ticket vt on vt.objid=vte.parentid 
where op.refid=$P{refid} 
order by vt.ticketno 
