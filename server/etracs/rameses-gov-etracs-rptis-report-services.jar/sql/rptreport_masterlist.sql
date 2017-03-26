
[getMasterListing]
SELECT t.* FROM (  
	SELECT 
		f.state, f.owner_name, f.administrator_name, f.name, r.fullpin, f.tdno, f.titleno, rp.cadastrallotno,  
		r.rputype, pc.code AS classcode, r.totalareaha, r.totalareasqm, r.totalmv, r.totalav, f.effectivityyear, 
		f.prevtdno, NULL AS cancelledbytdnos, NULL AS cancelreason, canceldate
	FROM faas f
		INNER JOIN rpu r ON f.rpuid = r.objid 
		INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
		INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	WHERE f.state = 'CURRENT'  AND f.lguid LIKE $P{lguid} AND rp.barangayid LIKE $P{barangayid}
	  ${classidfilter} ${txntypefilter}

	UNION ALL

	SELECT 
		f.state, f.owner_name, f.administrator_name, f.name, r.fullpin, f.tdno, f.titleno, rp.cadastrallotno,  
		r.rputype, pc.code AS classcode, r.totalareaha, r.totalareasqm, r.totalmv, r.totalav, f.effectivityyear, 
		f.prevtdno, cancelledbytdnos, cancelreason, canceldate
	FROM faas f
		INNER JOIN rpu r ON f.rpuid = r.objid 
		INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
		INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	WHERE f.state = 'CANCELLED'  AND f.lguid LIKE $P{lguid} AND rp.barangayid LIKE $P{barangayid}
	  AND f.cancelledyear = $P{currentyear}  
	  ${classidfilter} ${txntypefilter}
) t 
${orderbyclause} 

