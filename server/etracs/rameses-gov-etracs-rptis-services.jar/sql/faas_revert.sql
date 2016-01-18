[findById]
select 
	f.objid, f.state, f.datacapture, f.rpuid, f.realpropertyid,
	r.rputype 
from faas f 
	inner join rpu r on f.rpuid = r.objid 
where f.objid = $P{objid}

[findLedgerByFaasId]
select * from rptledger where faasid = $P{objid}

[updateFaasState]
update faas set state = $P{state} where objid = $P{objid}

[updateRpuState]
update rpu set state = $P{state} where objid = $P{rpuid}

[updateRealPropertyState]
update realproperty set state = $P{state} where objid = $P{realpropertyid}

[clearLedgerFaasId]
update rptledger set faasid = null where objid = $P{objid}

[clearLedgerFaasIds]
update rptledgerfaas set faasid = null where rptledgerid = $P{objid}

