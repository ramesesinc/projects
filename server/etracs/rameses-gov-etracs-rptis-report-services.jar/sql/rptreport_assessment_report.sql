[getPreceedingList]
select 
	b.objid,
	b.name as barangay,
	sum(case when r.taxable = 1 then 1 else 0 end) as pretaxcnt,
	sum(case when r.taxable = 1 then ${valuefield} else 0 end) as pretaxvalue,
	sum(case when r.taxable = 0 then 1 else 0 end) as preexemptcnt,
	sum(case when r.taxable = 0 then ${valuefield} else 0 end) as preexemptvalue
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
where f.lguid = $P{lguid}
and f.state = 'CURRENT'
and (f.year < $P{year} or (f.year = $P{year} and f.month < $P{monthid}))
group by b.objid, b.name 


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
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
where f.lguid = $P{lguid}
and f.state = 'CURRENT'
and f.year = $P{year} 
and f.month = $P{monthid}
group by b.objid, b.name 



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
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
where f.lguid = $P{lguid}
and f.state = 'CANCELLED'
and f.cancelledyear = $P{year} 
and f.cancelledmonth = $P{monthid}
group by b.objid, b.name 


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
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
where f.lguid = $P{lguid}
and f.state = 'CURRENT'
and (f.year < $P{year} or (f.year = $P{year} and f.month <= $P{monthid}))
group by b.objid, b.name 






