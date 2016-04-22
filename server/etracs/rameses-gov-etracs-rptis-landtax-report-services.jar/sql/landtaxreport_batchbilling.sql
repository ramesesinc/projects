[getTaxpayerIds]
select distinct 
	rl.taxpayer_objid, 
	e.name as taxpayer_name, 
	e.address_text as taxpayer_address 
from rptledger rl
	inner join entity e on rl.taxpayer_objid = e.objid 
where rl.barangayid LIKE $P{barangayid}
	and rl.state = 'APPROVED'
	and (rl.lastyearpaid < $P{billtoyear} or 
		(rl.lastyearpaid = $P{billtoyear} and rl.lastqtrpaid = $P{billtoqtr})
	)
order by rl.tdno 