[getSubdividedLands]
select 
	f.tdno,
	f.titleno, 
	f.fullpin,
	rp.cadastrallotno,
	rp.surveyno,
	r.totalareaha,
	r.totalareasqm,
	r.totalmv,
	r.totalav 
from subdividedland sl 
	inner join faas f on sl.newfaasid = f.objid 
	inner join rpu r on f.rpuid = r.objid 
	inner join realproperty rp on f.realpropertyid = rp.objid 
where sl.subdivisionid = $P{objid}
order by cadastrallotno


[getMotherLandsSummary]
select 
	sum(r.totalareaha) as totalareaha,
	sum(r.totalareasqm) as totalareasqm,
	sum(r.totalmv) as totalmv,
	sum(r.totalav) as totalav
from subdivision_motherland m
	inner join faas f on m.landfaasid = f.objid 
	inner join rpu r on f.rpuid = r.objid 
	inner join realproperty rp on f.realpropertyid = rp.objid 
where m.subdivisionid = $P{objid}
