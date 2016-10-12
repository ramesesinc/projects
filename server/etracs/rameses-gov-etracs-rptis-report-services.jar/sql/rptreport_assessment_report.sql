[getPreceedingList]
select 
	b.objid,
	b.name as barangay,
	sum(case when r.taxable = 1 then 1 else 0 end) as pretaxcnt,
	sum(case when r.taxable = 1 then ${valuefield} else 0 end) as pretaxvalue,
	sum(case when r.taxable = 0 then 1 else 0 end) as preexemptcnt,
	sum(case when r.taxable = 0 then ${valuefield} else 0 end) as preexemptvalue
from faas f 
	inner join rpu r on f.rpuid = r.objid 
	inner join realproperty rp on f.realpropertyid = rp.objid 
	inner join barangay b on rp.barangayid = b.objid 
where f.state = 'CURRENT'
and (f.year < $P{year} or (f.year = $P{year} and f.month < $P{monthid}))



[getCurrentList]
select 
	b.objid,
	b.name as barangay,
	sum(case when r.taxable = 1 then 1 else 0 end) as currtaxcnt,
	sum(case when r.taxable = 1 then ${valuefield} else 0 end) as currtaxvalue,
	sum(case when r.taxable = 0 then 1 else 0 end) as currexemptcnt,
	sum(case when r.taxable = 0 then ${valuefield} else 0 end) as currexemptvalue
from faas f 
	inner join rpu r on f.rpuid = r.objid 
	inner join realproperty rp on f.realpropertyid = rp.objid 
	inner join barangay b on rp.barangayid = b.objid 
where f.state = 'CURRENT'
and f.year = $P{year} 
and f.month = $P{monthid}



[getCancelledList]
select 
	b.objid,
	b.name as barangay,
	sum(case when r.taxable = 1 then 1 else 0 end) as cancelledtaxcnt,
	sum(case when r.taxable = 1 then ${valuefield} else 0 end) as cancelledtaxvalue,
	sum(case when r.taxable = 0 then 1 else 0 end) as cancelledexemptcnt,
	sum(case when r.taxable = 0 then ${valuefield} else 0 end) as cancelledexemptvalue
from faas f 
	inner join rpu r on f.rpuid = r.objid 
	inner join realproperty rp on f.realpropertyid = rp.objid 
	inner join barangay b on rp.barangayid = b.objid 
where f.state = 'CANCELLED'
and f.year = $P{year} 
and f.month = $P{monthid}



[getEndingList]
select 
	b.objid,
	b.name as barangay,
	sum(case when r.taxable = 1 then 1 else 0 end) as endtaxcnt,
	sum(case when r.taxable = 1 then ${valuefield} else 0 end) as endtaxvalue,
	sum(case when r.taxable = 0 then 1 else 0 end) as endexemptcnt,
	sum(case when r.taxable = 0 then ${valuefield} else 0 end) as endexemptvalue
from faas f 
	inner join rpu r on f.rpuid = r.objid 
	inner join realproperty rp on f.realpropertyid = rp.objid 
	inner join barangay b on rp.barangayid = b.objid 
where f.state = 'CURRENT'
and (f.year < $P{year} or (f.year = $P{year} and f.month <= $P{monthid}))







