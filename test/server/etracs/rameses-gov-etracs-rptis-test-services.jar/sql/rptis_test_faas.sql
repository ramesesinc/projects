[findFaasById]
select objid from faas where objid = $P{objid}

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

[setFaasRequirementsAsComplied]
update rpt_requirement set 
	complied = 1
where refid = $P{objid}

[fullyPaidLedger]
update rptledger set lastyearpaid = year(now()), lastqtrpaid = 4 where objid = $P{objid}

