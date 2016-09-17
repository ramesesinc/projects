[getList]
SELECT * 
FROM rptcertification
where 1=1 ${filters}
ORDER BY txnno DESC


[getItemsTest]
SELECT 
	$P{objid} as rptcertificationid,
	f.objid 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE (f.taxpayer_objid	= $P{taxpayerid}
	or exists(select * from entitymember where member_objid = $P{taxpayerid})
  )
  AND r.rputype = 'land'
  ${asoffilter}



[insertLandHoldingItems]
INSERT INTO rptcertificationitem (rptcertificationid,refid)
SELECT 
	$P{objid} as rptcertificationid,
	f.objid 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
WHERE f.state = 'CURRENT'
 and f.taxpayer_objid	= $P{taxpayerid}
  AND r.rputype = 'land'
  
union 

SELECT 
	$P{objid} as rptcertificationid,
	f.objid 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
    inner join entitymember m on f.taxpayer_objid = m.entityid 
WHERE f.state = 'CURRENT'
 and m.member_objid = $P{taxpayerid}
  AND r.rputype = 'land'



[insertLandHoldingWithImprovementItems]
INSERT INTO rptcertificationitem (rptcertificationid,refid)
SELECT 
	$P{objid} as rptcertificationid,
	f.objid 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
WHERE f.state = 'CURRENT'
  and f.taxpayer_objid	= $P{taxpayerid}
  AND r.rputype = 'land'
  AND EXISTS( SELECT * 
  			  FROM faas fx
  			  	INNER JOIN rpu rpu ON fx.rpuid = rpu.objid 
  			  WHERE fx.realpropertyid = f.realpropertyid 
  			    AND fx.state = 'CURRENT' 
  			    AND rpu.rputype <> 'land'
  			)

union

SELECT 
	$P{objid} as rptcertificationid,
	f.objid 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	inner join entitymember m on f.taxpayer_objid = m.entityid 
WHERE f.state = 'CURRENT' 
  and m.member_objid = $P{taxpayerid}
  AND r.rputype = 'land'
  AND EXISTS( SELECT * 
  			  FROM faas fx 
  			  	INNER JOIN rpu rpu ON fx.rpuid = rpu.objid 
  			  WHERE fx.realpropertyid = f.realpropertyid
  			    AND fx.state = 'CURRENT' 
  			    AND rpu.rputype <> 'land'
  			)



[insertLandHoldingWithNoImprovementItems]
INSERT INTO rptcertificationitem (rptcertificationid,refid)
SELECT 
	$P{objid} as rptcertificationid,
	f.objid 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
WHERE f.state = 'CURRENT'
  and f.taxpayer_objid	= $P{taxpayerid}
  AND r.rputype = 'land'
  AND NOT EXISTS( SELECT * 
  			  FROM faas fx 
  			  	INNER JOIN rpu rpu ON fx.rpuid = rpu.objid 
  			  WHERE fx.realpropertyid = f.realpropertyid
  			    AND fx.state = 'CURRENT' 
  			    AND rpu.rputype <> 'land'
  			)

union 

SELECT 
	$P{objid} as rptcertificationid,
	f.objid 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	inner join entitymember m on f.taxpayer_objid = m.entityid 
WHERE f.state = 'CURRENT' 
  and m.member_objid = $P{taxpayerid}
  AND r.rputype = 'land'
  AND NOT EXISTS( SELECT * 
  			  FROM faas fx 
  			  	INNER JOIN rpu rpu ON fx.rpuid = rpu.objid 
  			  WHERE fx.realpropertyid = f.realpropertyid
  			    AND fx.state = 'CURRENT' 
  			    AND rpu.rputype <> 'land'
  			)





[getLandHoldingItems]
SELECT 
	f.tdno,
	f.taxpayer_name, 
	f.owner_name, 
	f.titleno,	
	f.rpuid, 
	pc.code AS classcode, 
	pc.name AS classname,
	rp.cadastrallotno,
	CASE WHEN op.parent_orgclass = 'MUNICIPALITY' THEN op.name ELSE ogp.name END AS lguname,
	b.name AS barangay, 
	r.rputype, 
	r.totalareaha AS totalareaha,
	r.totalareasqm AS totalareasqm,
	r.totalav,
	r.totalmv, 
	rp.street,
	rp.cadastrallotno,
	rp.surveyno
FROM rptcertificationitem rci 
	INNER JOIN faas f ON rci.refid = f.objid 
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN sys_org b ON rp.barangayid = b.objid 
	INNER JOIN sys_org op ON b.parent_objid = op.objid 
	INNER JOIN sys_org ogp ON op.parent_objid = ogp.objid 
WHERE rci.rptcertificationid = $P{objid}
ORDER BY f.tdno


[insertMultipleItems]
INSERT INTO rptcertificationitem (rptcertificationid,refid)
SELECT 
	$P{objid} as rptcertificationid,
	f.objid 
FROM faas f
WHERE f.state ='CURRENT' 
  and f.taxpayer_objid	= $P{taxpayerid}

union 

SELECT 
	$P{objid} as rptcertificationid,
	f.objid 
FROM faas f
	inner join entitymember m on f.taxpayer_objid = m.entityid 
WHERE f.state ='CURRENT' 
 and m.member_objid = $P{taxpayerid}


[getMultipleItems]
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
ORDER BY f.tdno 


[getFaasInfo]
SELECT 
	f.tdno, f.titleno, f.titledate, f.effectivityyear,
	f.owner_name, f.owner_address, 
	f.administrator_name, f.administrator_address, 
	pc.code AS classcode, 
	pc.name AS classname, 
	r.ry, r.realpropertyid, r.rputype, r.fullpin, r.totalmv, r.totalav,
	r.totalareasqm, r.totalareaha,
	rp.barangayid, rp.cadastrallotno, rp.blockno, rp.surveyno, rp.street,
	b.name AS barangay_name
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE f.objid = $P{faasid}


[getAnnotation]
SELECT objid, txnno
FROM faasannotation
WHERE faasid = $P{faasid} 
  AND STATE = 'APPROVED'


[getProperties]
SELECT objid FROM faas WHERE taxpayer_objid = $P{taxpayerid} AND state = 'CURRENT'


[findImprovementCount]
SELECT 
	COUNT(*) AS improvcount
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
WHERE f.state IN ('CURRENT')
  AND f.year <= $P{asofyear}
  AND r.rputype <> 'land'
  AND f.realpropertyid IN (
		select realpropertyid from faas where objid = $P{faasid}
  )


[findBldgLandCount]
SELECT COUNT(*) AS improvcount
FROM bldgrpu_land bl 
	INNER JOIN faas bf ON bl.bldgrpuid = bf.rpuid 
WHERE bl.landfaas_objid = $P{faasid}
  AND bf.state IN ('CURRENT')
  AND bf.year <= $P{asofyear}


[findPlantTreeCount]  
select count(*) as improvcount
from faas f 
inner join planttreedetail ptd on f.rpuid = ptd.landrpuid
where f.objid = $P{faasid}



[getLandItems]
SELECT 
	f.tdno,
	f.taxpayer_name, 
	f.owner_name, 
	f.titleno,	
	f.effectivityyear,
	pc.code AS classcode, 
	pc.name AS classname,
	rp.cadastrallotno,
	CASE WHEN  op.parent_orgclass = 'MUNICIPALITY' THEN op.name ELSE ogp.name END AS lguname,
	b.name AS barangay, 
	r.totalareaha AS totalareaha,
	r.totalareasqm AS totalareasqm,
	r.totalav,
	r.totalmv, 
	rp.surveyno,
	rp.street,
	r.rputype
FROM rptcertificationitem rci 
	INNER JOIN faas f ON rci.refid = f.objid 
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN sys_org b ON rp.barangayid = b.objid 
	INNER JOIN sys_org op ON b.parent_objid = op.objid 
	INNER JOIN sys_org ogp ON op.parent_objid = ogp.objid 
WHERE rci.rptcertificationid = $P{objid}
ORDER BY f.tdno 





[insertLandWithNoImprovement]
INSERT INTO rptcertificationitem (rptcertificationid,refid)
SELECT 
	$P{objid} as rptcertificationid,
	f.objid 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE f.objid = $P{faasid} 


[insertLandImprovements]
INSERT INTO rptcertificationitem (rptcertificationid,refid)
SELECT 
	$P{objid} as rptcertificationid,
	f.objid
FROM faas f 
	INNER JOIN rpu r on f.rpuid = r.objid 
WHERE r.rputype <> 'land' 
  ${asoffilter}
  AND f.realpropertyid IN (
		select realpropertyid from faas where objid = $P{faasid}
  )


[insertLandImprovementFromBldgLand]
INSERT INTO rptcertificationitem (rptcertificationid,refid)
SELECT 
	$P{objid} as rptcertificationid,
	f.objid 
FROM bldgrpu_land bl 
	INNER JOIN faas f ON bl.bldgrpuid = f.rpuid 
WHERE bl.landfaas_objid = $P{faasid}
and f.objid <>  $P{faasid}
  ${asoffilter}




[findFaasById]
SELECT 
	f.tdno,
	f.owner_name,
	b.name AS barangay
FROM faas f 
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE f.objid = $P{faasid}



[insertLastAndExistingItems]
INSERT INTO rptcertificationitem (rptcertificationid,refid)
SELECT 
	$P{objid} as rptcertificationid,
	f.objid as refid
FROM faas f 
WHERE f.state = 'CURRENT' 
  and f.taxpayer_objid	= $P{taxpayerid}

union 

SELECT 
	$P{objid} as rptcertificationid,
	f.objid as refid
FROM faas f 
	inner join entitymember m on f.taxpayer_objid = m.entityid 
WHERE f.state = 'CURRENT' 
  and m.member_objid = $P{taxpayerid}


[getLatestAndExistingItems]
SELECT 
	f.tdno,
	f.titleno,	
	f.taxpayer_name, 
	f.owner_name, 
	pc.code AS classcode,
	pc.name AS classname,
	rp.cadastrallotno,
	b.name AS barangay, 
	r.totalareaha AS totalareaha,
	r.totalareasqm AS totalareasqm,
	r.totalav,
	r.totalmv, 
	rp.surveyno,
	rp.street,
	r.objid as rpuid,
	r.rputype
FROM rptcertificationitem rci 
	INNER JOIN faas f ON rci.refid = f.objid 
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN sys_org b ON rp.barangayid = b.objid 
WHERE rci.rptcertificationid = $P{objid}
ORDER BY r.fullpin


[getBldgTypes]	
select bt.code
from bldgrpu_structuraltype st
	inner join bldgtype bt on st.bldgtype_objid = bt.objid 
where st.bldgrpuid = $P{rpuid}


[getNoPropertyTaxpayers]
select e.name
from rptcertificationitem rci 
	inner join entity e on rci.refid = e.objid 
where rci.rptcertificationid = $P{objid}
order by e.name 


