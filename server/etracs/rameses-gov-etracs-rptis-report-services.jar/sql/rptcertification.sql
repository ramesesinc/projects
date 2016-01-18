[getList]
SELECT * 
FROM rptcertification
where 1=1 ${filters}
ORDER BY txnno DESC


[insertLandHoldingItems]
INSERT INTO rptcertificationitem (rptcertificationid,refid)
SELECT 
	$P{objid} as rptcertificationid,
	f.objid 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE f.taxpayer_objid	= $P{taxpayerid}
  AND r.rputype = 'land'
  ${asoffilter}


[insertLandHoldingWithImprovementItems]
INSERT INTO rptcertificationitem (rptcertificationid,refid)
SELECT 
	$P{objid} as rptcertificationid,
	f.objid 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE f.taxpayer_objid	= $P{taxpayerid}
  AND r.rputype = 'land'
  ${asoffilter}
  AND EXISTS( SELECT * 
  			  FROM faas f 
  			  	INNER JOIN rpu rpu ON f.rpuid = rpu.objid 
  			  WHERE f.realpropertyid = r.realpropertyid 
  			    AND f.state = 'CURRENT' 
  			    AND rpu.rputype <> 'land'
  			)

[insertLandHoldingWithNoImprovementItems]
INSERT INTO rptcertificationitem (rptcertificationid,refid)
SELECT 
	$P{objid} as rptcertificationid,
	f.objid 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE f.taxpayer_objid	= $P{taxpayerid}
  AND r.rputype = 'land'
  ${asoffilter}
  AND NOT EXISTS( SELECT * FROM rpu 
  			  WHERE realpropertyid = r.realpropertyid 
  			    AND state = 'CURRENT' 
  			    AND rputype <> 'land'
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
ORDER BY f.tdno, r.fullpin


[insertMultipleItems]
INSERT INTO rptcertificationitem (rptcertificationid,refid)
SELECT 
	$P{objid} as rptcertificationid,
	f.objid 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE f.taxpayer_objid	= $P{taxpayerid}
  ${asoffilter}



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
ORDER BY r.fullpin


[getFaasInfo]
SELECT 
	f.tdno, f.titleno, f.titledate, f.effectivityyear,
	f.owner_name, f.owner_address, 
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
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE f.taxpayer_objid = $P{taxpayerid}
   ${asoffilter}


[getLatestAndExistingItems]
SELECT 
	f.tdno,
	f.titleno,	
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



[getLandHoldingBirItems]
SELECT 
	f.objid as faasid, f.state, f.txntype_objid, f.year, rp.pin  
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
WHERE f.taxpayer_objid	= $P{taxpayerid}
  AND r.rputype = 'land'
  ${asoffilter}
order by rp.pin, f.year 