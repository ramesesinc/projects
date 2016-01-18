[getMasterList]
select 
	m.acctno, m.dtcreated, m.owner_name as ownername, m.owner_address as owneraddress, 
	mrk.name as marketname, ms.name as sectionname, mr.name as rentalunitname, m.rate 
from  marketaccount m
	inner join market_rentalunit mr on mr.objid = m.rentalunit_objid 
	inner join market mrk on mr.marketid = mrk.objid 
	inner join marketsection ms on ms.objid = mr.section_objid 
where m.state = 'APPROVED'
order by m.dtcreated  