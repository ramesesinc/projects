[getUnpaidLedgers]
select 
	rl.objid, 
	rl.tdno, 
	e.name as ownername, 
	e.mobileno as phoneno
from rptledger rl 
	inner join entity e on rl.taxpayer_objid = e.objid 
where rl.state = 'APPROVED' 
  and rl.taxable = 1
  and (rl.lastyearpaid < $P{cy} or (rl.lastyearpaid = $P{cy} and rl.lastqtrpaid < 4))
  and e.mobileno is not null

UNION ALL 

select 
	rl.objid, 
	rl.tdno, 
	e.name as ownername, 
	e.mobileno as phoneno
from rptledger rl 
	inner join propertypayer_item ppi on rl.objid = ppi.rptledger_objid
	inner join propertypayer p on ppi.parentid = p.objid 
	inner join entity e on p.taxpayer_objid = e.objid 
where rl.state = 'APPROVED' 
  and rl.taxable = 1
  and (rl.lastyearpaid < $P{cy} or (rl.lastyearpaid = $P{cy} and rl.lastqtrpaid < 4))
  and e.mobileno is not null