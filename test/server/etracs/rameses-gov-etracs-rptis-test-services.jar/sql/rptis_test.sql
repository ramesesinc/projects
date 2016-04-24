[findFaasTaskById]
select * from faas_task where objid = $P{objid}

[findFaasById]
select objid, state from faas where objid = $P{objid}

[findCancelledFaasById]
select objid, state from cancelledfaas where objid = $P{objid}

[findRpuById]
select objid from rpu where objid = $P{objid}

[findRealPropertyById]
select objid from realproperty where objid = $P{objid}

[findNewFaasId]
select distinct f.objid 
from faas f
	inner join previousfaas pf on f.objid = pf.faasid
where pf.prevfaasid = $P{objid}


[findLedgerByFaasId]
select objid, state from rptledger where faasid = $P{objid}

[findCurrentTask]
select * 
from faas_task 
where refid = $P{objid} and enddate is null


[setRequirementsAsComplied]
update rpt_requirement set 
	complied = 1
where refid = $P{objid}

[fullyPaidLedger]
update rptledger set lastyearpaid = year(now()), lastqtrpaid = 4 where objid = $P{objid}




[findSubdivisionById]
select objid, state from subdivision where objid = $P{objid}


[getSubdivisionTasks]
select 
	objid,
	state,
	startdate,
	enddate,
	assignee_objid,
	assignee_name,
	assignee_title,
	actor_objid,
	actor_name,
	actor_title
from subdivision_task 
where refid = $P{objid}




[findConsolidationById]
select objid, state from consolidation where objid = $P{objid}


[getConsolidationTasks]
select 
	objid,
	state,
	startdate,
	enddate,
	assignee_objid,
	assignee_name,
	assignee_title,
	actor_objid,
	actor_name,
	actor_title
from consolidation_task 
where refid = $P{objid}