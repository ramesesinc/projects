[getPropertiesForBIR]
select
	f.objid, f.state, f.tdno, f.fullpin, 
	r.rputype, r.totalmv, r.totalav
from faas f
	inner join rpu r on f.rpuid = r.objid 
where f.taxpayer_objid = $P{taxpayerid}
  and f.state in ('CURRENT', 'CANCELLED')
  and f.year <= $P{asofyear}
  and r.ry = $P{ry}
order by f.tdno   


[getLandHoldingBirItems]
SELECT 
	f.objid, f.state, f.tdno, f.fullpin, 
	r.rputype, r.totalmv, r.totalav
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
WHERE f.taxpayer_objid	= $P{taxpayerid}
  AND r.rputype = 'land'
  and f.state in ('CURRENT', 'CANCELLED')
  and f.year <= $P{asofyear}
  and r.ry = $P{ry}
order by f.tdno 



[getLandHoldingWithImprovementBirItems]
SELECT 
	f.objid, f.state, f.tdno, f.fullpin, 
	r.rputype, r.totalmv, r.totalav
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE f.taxpayer_objid	= $P{taxpayerid}
  AND r.rputype = 'land'
  and f.state in ('CURRENT', 'CANCELLED')
  and f.year <= $P{asofyear}
  and r.ry = $P{ry}
  AND EXISTS( SELECT * 
  			  FROM faas fx 
  			  	INNER JOIN rpu rx ON fx.rpuid = rx.objid 
  			  WHERE fx.realpropertyid = r.realpropertyid 
  			    AND fx.state in ('CURRENT', 'CANCELLED')
  			    AND rx.rputype <> 'land'
  			    and fx.year <= $P{asofyear}
  				and rx.ry = $P{ry}
  			)
order by f.tdno   


[getLandHoldingWithNoImprovementBirItems]
SELECT 
	f.objid, f.state, f.tdno, f.fullpin, 
	r.rputype, r.totalmv, r.totalav
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE f.taxpayer_objid	= $P{taxpayerid}
  AND r.rputype = 'land'
  and f.state in ('CURRENT', 'CANCELLED')
  and f.year <= $P{asofyear}
  and r.ry = $P{ry}
  AND NOT EXISTS( 
  				SELECT * 
				FROM faas fx 
			  inner join rpu  rx on fx.rpuid = rx.objid 
  			  WHERE rx.realpropertyid = f.realpropertyid 
  			    AND fx.state in ('CURRENT', 'CANCELLED')
  			    AND rx.rputype <> 'land'
  			    and fx.year = $P{asofyear}
  			  	and rx.ry = $P{ry}
  			)



[findRyForAsOfYear]
select r.ry 
from faas f
	inner join rpu r on f.rpuid = r.objid 
where f.year = $P{asofyear}




[createItem]
insert into rptcertificationitem (rptcertificationid,refid)
values ($P{objid}, $P{refid})



[getItems]
SELECT 
	f.tdno,
	f.taxpayer_name, 
	f.owner_name, 
	f.titleno,	
	f.rpuid, 
	pc.code AS classcode, 
	pc.name AS classname,
	rp.cadastrallotno,
	CASE WHEN  op.parent_orgclass = 'MUNICIPALITY' THEN op.name ELSE ogp.name END AS lguname,
	b.name AS barangay, 
	r.rputype, 
	r.totalareaha AS totalareaha,
	r.totalareasqm AS totalareasqm,
	r.totalav,
	r.totalmv, 
	rp.surveyno,
	rp.street
FROM rptcertificationitem rci 
	INNER JOIN faas f ON rci.refid = f.objid 
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN sys_org b ON rp.barangayid = b.objid 
	INNER JOIN sys_org op ON b.parent_objid = op.objid 
	INNER JOIN sys_org ogp ON op.parent_objid = ogp.objid 
WHERE rci.rptcertificationid = $P{objid}  
ORDER BY r.fullpin


[getBldgTypes]	
select bt.code
from bldgrpu_structuraltype st
	inner join bldgtype bt on st.bldgtype_objid = bt.objid 
where st.bldgrpuid = $P{rpuid}
