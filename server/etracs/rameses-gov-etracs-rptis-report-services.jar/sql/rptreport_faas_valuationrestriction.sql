[getFaasValuationRestrictionSummary]
select 
	s.name as municipality,
	sum(case when f.restrictionid is null then r.totalmv else 0 end ) as totalmv,
	sum(case when f.restrictionid is null then r.totalav else 0 end ) as totalav,
	sum(case when f.restrictionid is not null then r.totalav else 0 end ) as restrictionav,
	sum(case when f.restrictionid is not null then r.totalmv else 0 end ) as restrictionmv
from faas f
	inner join rpu r on f.rpuid = r.objid 
	inner join realproperty rp on f.realpropertyid = rp.objid
	inner join sys_org s on f.lguid = s.objid 
where f.lguid like $P{lguid}
  and f.state = 'CURRENT' 
  and r.taxable = 1 
  ${periodfilter}
group by s.name   