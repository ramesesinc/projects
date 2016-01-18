[getList]
SELECT 
	cf.*,
	f.tdno AS faas_tdno,
	f.owner_name AS faas_owner_name,
	r.fullpin AS faas_fullpin,
	r.rputype AS faas_rputype,
	r.totalareasqm AS faas_totalareasqm,
	r.totalareaha AS faas_totalareaha,
	r.totalmv AS faas_totalmv,
	r.totalav AS faas_totalav,
	ctr.code AS reason_code,
	ctr.name AS reason_name
FROM cancelledfaas cf 
	INNER JOIN faas f ON cf.faasid = f.objid 
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN canceltdreason ctr ON cf.reason_objid = ctr.objid 
where 1=1 ${filters}	
ORDER BY txndate DESC 

[findById]
SELECT 
	cf.*,
	f.objid as faas_objid,
	f.tdno AS faas_tdno,
	f.owner_name AS faas_owner_name,
	f.rpuid AS faas_rpuid,
	r.fullpin AS faas_fullpin,
	r.rputype AS faas_rputype,
	r.realpropertyid AS faas_realpropertyid,
	r.totalareasqm AS faas_totalareasqm,
	r.totalareaha AS faas_totalareaha,
	r.totalmv AS faas_totalmv,
	r.totalav AS faas_totalav,
	ctr.code AS reason_code,
	ctr.name AS reason_name
FROM cancelledfaas cf 
	INNER JOIN faas f ON cf.faasid = f.objid 
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN canceltdreason ctr ON cf.reason_objid = ctr.objid 
WHERE cf.objid = $P{objid}


[cancelFaas]
UPDATE faas SET state = 'CANCELLED' WHERE objid = $P{objid} 

[cancelRpu]
UPDATE rpu SET state = 'CANCELLED' WHERE objid = $P{objid}

[cancelRealProperty]
UPDATE realproperty SET state = 'CANCELLED' WHERE objid = $P{objid}


[getNonCancelledImprovements]
SELECT f.tdno 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
WHERE r.realpropertyid = $P{realpropertyid}
  AND r.rputype <> 'land'
  AND f.state <> 'CANCELLED'	
ORDER BY f.tdno   

