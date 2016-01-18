[updateLedgerLastYearPaid]
update rptledger set 
	state = 'APPROVED',
	lastyearpaid = $P{lastyearpaid},
	lastqtrpaid = $P{lastqtrpaid}
where objid = $P{objid}	


[approveLedgerFaases]
update rptledgerfaas set 
	state = 'APPROVED' 
where rptledgerid = $P{objid}


[findFaasByTdNo]
select objid from faas where tdno = $P{tdno}

[findLedgerByFaasId]
select * from rptledger where faasid = $P{faasid}