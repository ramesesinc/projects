[getList]
select 
	x.*,
	case when x.rputype = 'land' then x.totalav else null end as landav,
	case when x.rputype = 'mach' then x.totalav else null end as machav,
	case when x.rputype not in ('land','mach') then x.totalav else null end as improvav
from (
	select 
		e.name as owner, 
		case when rp.pin is null then rl.fullpin else rp.pin end as pin, 
		rl.fullpin,
		rl.tdno,
		rl.classcode,
		rl.cadastrallotno,
		b.name as barangay, 
		rl.rputype, 
		r.suffix, 
		(select max(assessedvalue) from rptledgerfaas 
		 where rptledgerid = rl.objid 
			 and $P{year} >= fromyear 
			 and ($P{year} <= toyear or toyear = 0)
			and state = 'APPROVED' 
			and taxable = 1 
		 ) as totalav
	from rptledger rl 
		inner join entity e on rl.taxpayer_objid = e.objid 
		inner join barangay b on rl.barangayid = b.objid 
		left join faas f on rl.faasid = f.objid 
		left join rpu r on f.rpuid = r.objid 
		left join realproperty rp on f.realpropertyid = rp.objid 
	where rl.state = 'APPROVED' 
	and rl.taxable = 1 
	and rp.barangayid like $P{barangayid}
	and not exists(select * from faas_restriction where ledger_objid = rl.objid and state='ACTIVE')

)x
where x.totalav > 0
order by x.pin, x.suffix 

