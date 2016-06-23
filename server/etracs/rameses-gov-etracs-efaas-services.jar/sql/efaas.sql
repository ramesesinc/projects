[getLandRySetting]
SELECT * FROM landrysetting

[getLandAssessLevel]
SELECT * FROM landassesslevel

[getLandAssessLevelRange]
SELECT * FROM landassesslevelrange

[getLcuvSpecificClass]
SELECT * FROM lcuvspecificclass

[getLcuvSubClass]
SELECT * FROM lcuvsubclass

[getLcuvStripping]
SELECT * FROM lcuvstripping

[getLandAdjustmentType]
SELECT * FROM landadjustmenttype

[getFaasForAppraisal]
SELECT
	f.objid,
	f.state,
	f.rpuid,
	f.realpropertyid,
	f.owner_name,
	f.owner_address,
	f.tdno,
	f.fullpin,
	r.ry AS rpu_ry,
	r.suffix AS rpu_suffix,
	r.subsuffix AS rpu_subsuffix,
	r.classification_objid AS rpu_classification_objid,
	r.taxable AS rpu_taxable,
	r.totalareaha AS rpu_totalareaha,
	r.totalareasqm AS rpu_totalareasqm,
	r.totalbmv AS rpu_totalbmv,
	r.totalmv AS rpu_totalmv,
	r.totalav AS rpu_totalav,
	rp.cadastrallotno AS rp_cadastrallotno,
	rp.blockno AS rp_blockno,
	rp.surveyno AS rp_surveyno,
	rp.street AS rp_street,
	rp.purok AS rp_purok,
	rp.north AS rp_north,
	rp.south AS rp_south,
	rp.east AS rp_east,
	rp.west AS rp_west
FROM
	(
	SELECT
	ft.objid, ft.refid, state, MAX(startdate) AS startdate
	FROM faas_task ft
	WHERE ft.state = 'appraiser'
	AND ft.enddate IS NULL
	AND ft.assignee_objid = $P{userid}
	GROUP BY ft.refid
)t
	INNER JOIN faas f ON t.refid = f.objid
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
WHERE f.state = 'INTERIM';

[findFaas]
SELECT
	f.objid,
	f.state,
	f.rpuid,
	f.realpropertyid,
	f.owner_name,
	f.owner_address,
	f.tdno,
	f.fullpin,
	r.ry AS rpu_ry,
	r.suffix AS rpu_suffix,
	r.subsuffix AS rpu_subsuffix,
	r.classification_objid AS rpu_classification_objid,
	r.taxable AS rpu_taxable,
	r.totalareaha AS rpu_totalareaha,
	r.totalareasqm AS rpu_totalareasqm,
	r.totalbmv AS rpu_totalbmv,
	r.totalmv AS rpu_totalmv,
	r.totalav AS rpu_totalav,
	rp.cadastrallotno AS rp_cadastrallotno,
	rp.blockno AS rp_blockno,
	rp.surveyno AS rp_surveyno,
	rp.street AS rp_street,
	rp.purok AS rp_purok,
	rp.north AS rp_north,
	rp.south AS rp_south,
	rp.east AS rp_east,
	rp.west AS rp_west
FROM faas f 
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
WHERE f.state IN ('INTERIM','CURRENT')
AND f.tdno = $P{tdno}