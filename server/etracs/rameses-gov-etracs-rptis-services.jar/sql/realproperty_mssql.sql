[findRealProperty]
SELECT rp.*, f.objid AS faasid, f.tdno
FROM realproperty rp 
	LEFT JOIN faas f ON rp.objid = f.realpropertyid 
WHERE rp.pin = $P{pin} 
  AND rp.ry = $P{ry}
  AND (rp.claimno IS NULL OR rp.claimno LIKE $P{claimno} )




[getList]
SELECT t.* FROM (
	SELECT rp.*, b.objid AS barangay_objid, b.name AS barangay_name
	FROM realproperty rp
		INNER JOIN barangay b ON rp.barangayid = b.objid 
	WHERE rp.pin LIKE $P{searchtext}
	${filters}

	UNION 

	SELECT rp.*, b.objid AS barangay_objid, b.name AS barangay_name
	FROM realproperty rp
		INNER JOIN barangay b ON rp.barangayid = b.objid 
	WHERE rp.cadastrallotno = $P{cadastrallotno}
	${filters}

	UNION

	SELECT rp.*, b.objid AS barangay_objid, b.name AS barangay_name
	FROM realproperty rp
		INNER JOIN barangay b ON rp.barangayid = b.objid 
	WHERE rp.surveyno = $P{surveyno}
	${filters}

	UNION

	SELECT rp.*, b.objid AS barangay_objid, b.name AS barangay_name
	FROM realproperty rp
		INNER JOIN barangay b ON rp.barangayid = b.objid 
	WHERE b.name = $P{barangay}
	${filters}
) t
ORDER BY barangay_name, pin 


[changeState]
UPDATE realproperty SET state = $P{state} WHERE objid = $P{objid}


[approve]
UPDATE realproperty SET  state = 'CURRENT' WHERE objid = $P{objid} AND state <> 'CANCELLED'


[disapprove]
UPDATE realproperty SET  state = 'INTERIM' WHERE objid = $P{objid} AND state NOT IN ('CANCELLED', 'CURRENT')


[getLandRevisionYears]
SELECT ry FROM landrysetting ORDER BY ry 

[delete]
DELETE FROM realproperty WHERE objid = $P{objid}

[findFaasByRealPropertyId]
SELECT objid, tdno FROM faas 
WHERE realpropertyid = $P{realpropertyid} 
  AND objid <> $P{objid}
  AND state <> 'CANCELLED'


[cancelPreviousRealProperty]
UPDATE prevrp SET
	prevrp.state = 'CANCELLED'
FROM realproperty rp 
	INNER JOIN realproperty prevrp ON rp.previd = prevrp.objid 
WHERE rp.objid = $P{objid}




[findDuplicatePin]
SELECT * 
FROM realproperty 
WHERE objid <> $P{objid}
AND pin = $P{pin} 
AND ry = $P{ry}


[updateBoundaries]
update realproperty set 
	north = $P{north},
	east = $P{east},
	west = $P{west},
	south = $P{south}
where objid = $P{objid}
