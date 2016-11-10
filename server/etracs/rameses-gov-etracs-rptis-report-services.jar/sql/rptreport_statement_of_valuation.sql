[getList]
select 
	x.isitem,
	x.idx,
	x.title,
	sum(x.rpucount) as rpucount,
	sum(x.totalav) as totalav, 
	sum(x.totalmv) as totalmv
from (
	select 
		0 as isitem, 
		1 as idx,
		'A. NEW DISCOVERY' as title,
		null as rpucount,
		null as totalav, 
		null as totalmv 

	union all 

	select 
		1 as isitem, 
		case 
			when r.rputype = 'land' then 2
			when r.rputype = 'bldg' and r.totalmv <= 175000 then 3
			when r.rputype = 'bldg' and r.totalmv > 175000 then 4
			when r.rputype = 'mach' and r.totalmv > 175000 then 5
			when r.rputype = 'misc' and r.totalmv > 175000 then 6
			else  7
		end as idx, 
		case 
			when r.rputype = 'land' then 'LAND' 
			when r.rputype = 'bldg' and r.totalmv <= 175000 then 'BLDG. LESS THAN OR EQUAL 175000' 
			when r.rputype = 'bldg' and r.totalmv > 175000 then 'BLDG. GREATER THAN 175000' 
			when r.rputype = 'mach' and r.totalmv > 175000 then 'MACHINERY' 
			when r.rputype = 'misc' and r.totalmv > 175000 then 'MISCELLANEOUS' 
			else 'PLANTS' 
		end as title,
		1 as rpucount,
		r.totalav,
		r.totalmv 
	from faas f 
		inner join rpu r on f.rpuid = r.objid
		inner join realproperty rp on f.realpropertyid = rp.objid 
	where f.lguid = $P{lguid}
		and f.state = 'CURRENT' 
		and f.txntype_objid = 'ND' 
		and f.year = $P{year}
		and f.month = $P{monthid}


	union all 

	select 
		0 as isitem, 
		21 as idx, 
		'B. SUBDIVISION' as title,
		sum(1) as rpucount,
		sum(r.totalav) as totalav,
		sum(r.totalmv) as totalmv 
	from subdivision s 
		inner join subdividedland sl on s.objid = sl.subdivisionid
		inner join faas f on sl.newfaasid = f.objid 
		inner join rpu r on f.rpuid = r.objid
		inner join realproperty rp on f.realpropertyid = rp.objid 
	where s.lguid = $P{lguid}
		and s.state = 'APPROVED' 
		and f.state = 'CURRENT'
		and f.year = $P{year}
		and f.month = $P{monthid}
		
	union all 

	select 
		0 as isitem, 
		31 as idx, 
		'C. TRANSFER' as title,
		1 as rpucount,
		r.totalav,
		r.totalmv 
	from faas f 
		inner join rpu r on f.rpuid = r.objid
		inner join realproperty rp on f.realpropertyid = rp.objid 
	where f.lguid = $P{lguid}
		and f.state = 'CURRENT' 
		and f.txntype_objid in ('TR', 'TRE', 'TRC') 
		and f.year = $P{year}
		and f.month = $P{monthid}


	union all 

	select 
		0 as isitem, 
		41 as idx, 
		'D. CONSOLIDATION' as title,
		sum(1) as rpucount,
		sum(r.totalav) as totalav,
		sum(r.totalmv) as totalmv 
	from consolidation c
		inner join faas f on c.newfaasid = f.objid 
		inner join rpu r on f.rpuid = r.objid
		inner join realproperty rp on f.realpropertyid = rp.objid 
	where c.lguid = $P{lguid}
		and c.state = 'APPROVED' 
		and f.state = 'CURRENT'
		and f.year = $P{year}
		and f.month = $P{monthid}

	union all 

	select 
		0 as isitem, 
		50 as idx,
		'E. REASSESSMENT' as title,
		null as rpucount,
		null as totalav, 
		null as totalmv 

	union all 

	select 
		1 as isitem, 
		51 as idx, 
		ft.name as title,
		1 as rpucount,
		r.totalav,
		r.totalmv 
	from faas f 
		inner join rpu r on f.rpuid = r.objid
		inner join realproperty rp on f.realpropertyid = rp.objid 
		inner join faas_txntype ft on f.txntype_objid = ft.objid 
	where f.lguid = $P{lguid}
		and f.state = 'CURRENT' 
		and f.txntype_objid not in ('ND', 'TR', 'TRC', 'TRE', 'SD', 'CS', 'GR')
		and f.year = $P{year}
		and f.month = $P{monthid}
) x 
group by x.isitem, x.idx, x.title 
order by x.idx 
