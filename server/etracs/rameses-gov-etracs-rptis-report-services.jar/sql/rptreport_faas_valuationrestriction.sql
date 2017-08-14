[getFaasValuationRestrictionSummary]
select 
	s.name as municipality,
	sum(r.totalmv) as totalmv,
	sum(r.totalav) as totalav,
	sum(r.totalav) as restrictionav,
	sum(r.totalmv) as restrictionmv
FROM faas_restriction fr 
	inner join faas_restriction_type frt on fr.restrictiontype_objid = frt.objid 
	inner join faas f on fr.parent_objid = f.objid 
	inner join sys_org s on f.lguid = s.objid 
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
where (
		(f.dtapproved < $P{enddate} AND f.state = 'CURRENT' ) OR 
		(f.canceldate >= $P{enddate} AND f.state = 'CANCELLED' )
  )
 ${filter}
group by s.name   
