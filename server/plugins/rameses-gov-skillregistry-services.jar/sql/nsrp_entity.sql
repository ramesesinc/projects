[getSearchResult]
	SELECT
	ei.objid
	FROM entityindividual ei 
	INNER JOIN entity e 
		ON ei.objid = e.objid
	INNER JOIN entity_address ea 
		ON ei.objid = ea.parentid
	WHERE ei.gender = $P{gender}
	OR ea.barangay_name = $P{barangay}

UNION
	
	SELECT
	ei.objid
	FROM entityindividual ei
	INNER JOIN entity e 
		ON ei.objid = e.objid
	WHERE ei.objid in ${entityids}
	OR ei.profession LIKE $P{searchtext}
