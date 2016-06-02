[getRestrictionAvSummary]
select 
	s.name as lguname,
	rt.objid as restriction_objid, 
	rt.name as restriction_name,
	rt.idx as restriction_idx,
	rt.isother as isother_idx,
	case when rt.isother = 0 then '' else 'OTHERS' end as isother_name,
	sum(r.totalav) as totalav
from faas f
	inner join realproperty rp on f.realpropertyid = rp.objid
	inner join rpu r on f.rpuid = r.objid 
	inner join sys_org s on f.lguid = s.objid 
	inner join faas_restriction_type rt on f.restrictionid = rt.objid 
where f.lguid like $P{lguid}
  and f.state = 'CURRENT' 
  ${periodfilter}
group by s.name, rt.name 
order by s.name, rt.isother, rt.idx