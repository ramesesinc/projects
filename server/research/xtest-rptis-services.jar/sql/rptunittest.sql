[findDataByCode]
select * from testdata where code = $P{code}


[findLedgerByFaasId]
select * from rptledger where faasid = $P{objid}

[findCurrentSubdivisionTask]
select * 
from subdivision_task 
where refid = $P{objid} and enddate is null

[findCurrentConsolidationTask]
select * 
from consolidation_task 
where refid = $P{objid} and enddate is null


[findCurrentFaasTask]
select * 
from faas_task 
where refid = $P{objid} and enddate is null

[setFaasRequirementsAsComplied]
update rpt_requirement set 
	complied = 1
where refid = $P{objid}


[updateDataByCode]
update testdata set 
	objid = $P{objid},
	data = $P{data}
where code = $P{code}


