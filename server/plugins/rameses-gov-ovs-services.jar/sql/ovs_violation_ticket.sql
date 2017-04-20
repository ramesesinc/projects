[getViolations]
SELECT
v2.*
FROM ovs_violation_ticket v1
INNER JOIN ovs_violation v2 on v1.violationid = v2.objid
WHERE parentid = $P{objid}

[getViolationsPerTicket]
SELECT
v.*,
vte.*
FROM ovs_violation_ticket_entry vte 
INNER JOIN ovs_violation v ON vte.violationid = v.objid
WHERE vte.parentid = $P{objid}

[findVehiclePerTicket]
SELECT
v.*,
vtv.*
FROM ovs_violation_ticket_vehicle vtv
INNER JOIN ovs_vehicle v ON vtv.vehicleid = v.objid 
WHERE vtv.parentid = $P{objid}

[getViolationTicketsByPaymentRefid]
select distinct 
	vt.objid, vt.ticketno, 
	vt.apprehensionofficer_name as officername  
from ovs_payment op 
	inner join ovs_payment_item opi on opi.parentid=op.objid 
	inner join ovs_violation_ticket_entry vte on vte.objid=opi.refid 
	inner join ovs_violation_ticket vt on vt.objid=vte.parentid 
where op.refid=$P{refid} 
order by vt.ticketno 
