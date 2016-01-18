[getList]
SELECT 
	f.*,
	rpu.rputype,
	rpu.ry,
	rpu.taxable,
	rpu.totalareaha,
	rpu.totalareasqm,
	rpu.totalbmv,
	rpu.totalmv,
	rpu.totalav,
	rp.section,
	rp.parcel,
	rp.surveyno,
	rp.cadastrallotno,
	rp.blockno,
	rp.claimno,
	b.name AS barangay_name,
	pc.code AS classification_code,
	t.trackingno,
	tsk.objid AS taskid,
	tsk.state AS taskstate,
	tsk.assignee_objid 
FROM faas f
	INNER JOIN rpu rpu ON f.rpuid = rpu.objid
	INNER  JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN barangay b ON rp.barangayid = b.objid 
	LEFT JOIN propertyclassification pc ON rpu.classification_objid = pc.objid 
	LEFT JOIN faas_task tsk ON f.objid = tsk.refid AND tsk.enddate IS NULL
	LEFT JOIN rpttracking t ON f.objid = t.objid 
WHERE 1=1 
${filters}



[findById]
SELECT  
	f.*,
	rpu.rputype AS rpu_rputype,
	rpu.ry AS rpu_ry,
	rpu.objid AS rpu_objid, 
	rpu.suffix AS rpu_suffix, 
	rpu.fullpin AS rpu_fullpin,
	rpu.taxable AS rpu_taxable,
	rpu.totalmv AS rpu_totalmv,
	rpu.totalav AS rpu_totalav,
	rpu.totalareaha AS rpu_totalareaha,
	rpu.totalareasqm AS rpu_totalareasqm,
	rpu.reclassed AS rpu_reclassed,
	rpu.realpropertyid AS rpu_realpropertyid,
	pc.objid AS rpu_classification_objid,
	pc.code AS rpu_classification_code,
	rp.objid AS rp_objid,
	rp.ry AS rp_ry,
	rp.pin AS rp_pin,
	rp.surveyno AS rp_surveyno,
	rp.cadastrallotno AS rp_cadastrallotno,
	rp.blockno AS rp_blockno,
	rp.purok AS rp_purok,
	rp.street AS rp_street,
	rp.north AS rp_north,
	rp.south AS rp_south,
	rp.east AS rp_east,
	rp.west AS rp_west,
	b.name AS rp_barangay_name,
	b.objid AS rp_barangay_objid,
	b.parentid AS rp_barangay_parentid,
	tsk.objid AS taskid,
	tsk.state AS taskstate,
	tsk.message AS taskmsg,
	tsk.assignee_objid
FROM faas f
	LEFT JOIN rpu rpu ON f.rpuid = rpu.objid
	LEFT JOIN propertyclassification pc ON rpu.classification_objid = pc.objid 
	LEFT JOIN faas_task tsk ON f.objid = tsk.refid AND tsk.enddate IS NULL
	INNER  JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE f.objid = $P{objid}



[findByTdNo]
SELECT  
	f.*,
	rpu.rputype AS rpu_rputype,
	rpu.ry AS rpu_ry,
	rpu.objid AS rpu_objid, 
	rpu.suffix AS rpu_suffix, 
	rpu.fullpin AS rpu_fullpin,
	rpu.taxable AS rpu_taxable,
	rpu.totalmv AS rpu_totalmv,
	rpu.totalav AS rpu_totalav,
	rpu.totalareaha AS rpu_totalareaha,
	rpu.totalareasqm AS rpu_totalareasqm,
	rpu.reclassed AS rpu_reclassed,
	rpu.realpropertyid AS rpu_realpropertyid,
	pc.objid AS rpu_classification_objid,
	pc.code AS rpu_classification_code,
	rp.objid AS rp_objid,
	rp.pin AS rp_pin,
	rp.surveyno AS rp_surveyno,
	rp.cadastrallotno AS rp_cadastrallotno,
	rp.blockno AS rp_blockno,
	rp.purok AS rp_purok,
	rp.street AS rp_street,
	rp.north AS rp_north,
	rp.south AS rp_south,
	rp.east AS rp_east,
	rp.west AS rp_west,
	b.name AS rp_barangay_name,
	b.objid AS rp_barangay_objid,
	b.parentid AS rp_barangay_parentid,
	tsk.objid AS taskid,
	tsk.state AS taskstate,
	tsk.message AS taskmsg,
	tsk.assignee_objid
FROM faas f
	INNER JOIN rpu rpu ON f.rpuid = rpu.objid
	INNER JOIN propertyclassification pc ON rpu.classification_objid = pc.objid 
	INNER  JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN barangay b ON rp.barangayid = b.objid 
	LEFT JOIN faas_task tsk ON f.objid = tsk.refid AND tsk.enddate IS NULL
WHERE f.tdno = $P{tdno}


[getFaasIds]
select
  f.objid, f.tdno
from faas f  
  inner join rpu r on r.objid = f.rpuid 
  inner join realproperty rp on rp.objid = r.realpropertyid 
where f.state like $P{state}
	and r.ry = $P{revisionyear} 
	and rp.barangayid = $P{barangayid}
	and rp.section like $P{section} 
order by r.fullpin 


[getPreviousFaases]
SELECT pf.*, f.rpuid AS prevrpuid
FROM previousfaas pf
INNER JOIN faas f ON pf.prevfaasid = f.objid 
WHERE faasid = $P{faasid}


[getBackTaxes]
SELECT * FROM faasbacktax WHERE faasid = $P{faasid} ORDER BY effectivityyear DESC 


[cancelFaas]
UPDATE faas SET 
	state = 'CANCELLED',
	cancelreason = $P{cancelreason},
	canceldate   = $P{canceldate},
	cancelledbytdnos = $P{cancelledbytdnos},
	cancelledtimestamp = $P{cancelledtimestamp},
	cancelledyear = $P{cancelledyear},
	cancelledqtr = $P{cancelledqtr},
	cancelledmonth = $P{cancelledmonth},
	cancelledday = $P{cancelledday}
WHERE objid = $P{objid}


[cancelRpu]
UPDATE rpu SET state = 'CANCELLED' WHERE objid = $P{objid}




#---------------------------------------------------------
#
#  LOOKUP SUPPORT
#
#---------------------------------------------------------
[lookupFaas]
SELECT 
	f.*,
	pc.code AS classification_code, 
	pc.code AS classcode, 
	pc.name AS classification_name, 
	pc.name AS classname, 
	r.ry, r.realpropertyid, r.rputype, r.fullpin, r.totalmv, r.totalav,
	r.totalareasqm, r.totalareaha, r.suffix, 
	rp.barangayid, rp.cadastrallotno, rp.blockno, rp.surveyno, rp.lgutype, rp.pintype, 
	rp.section, rp.parcel, 
	b.name AS barangay_name,
	t.trackingno
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
	LEFT JOIN rpttracking t ON f.objid = t.objid 
where 1=1  AND f.state <> 'PENDING' ${filters}	
ORDER BY f.tdno 


[getLandImprovementIds]
SELECT fi.objid 
FROM faas fl 
	INNER JOIN rpu lr ON fl.rpuid = lr.objid 
	INNER JOIN rpu ri ON lr.realpropertyid = ri.realpropertyid
	INNER JOIN faas fi ON ri.objid = fi.rpuid 
WHERE fl.objid = $P{landfaasid}
  AND ri.rputype <> 'land' 
  AND fi.state = 'CURRENT'


[findLguIndexInfo]  
SELECT
	b.indexno as barangayindex,
	case when c.objid is not null then c.indexno else p.indexno end as provcityindex,
	case when d.objid is not null then d.indexno else m.indexno end as munidistrictindex
FROM barangay b
	left join district d on b.parentid = d.objid
	left join city c on d.parentid = c.objid 
	left join municipality m on b.parentid = m.objid 
	left join province p on m.parentid = p.objid 
where b.objid = $P{barangayid}	



[updateFaasState]
UPDATE faas SET state = $P{state} WHERE objid = $P{objid} 

[submitForApproval]
UPDATE faas SET state = $P{state} WHERE objid = $P{objid}

[approveFaas]
UPDATE faas SET 
	state = $P{state}, utdno = $P{utdno}, tdno = $P{tdno}, 
	dtapproved = $P{dtapproved}, fullpin = $P{fullpin},
	year = $P{year}, 
	qtr = $P{qtr}, 
	month = $P{month}, 
	day = $P{day},
	txntimestamp = $P{txntimestamp}
WHERE objid = $P{objid}  
  AND state IN ('FORAPPROVAL', 'PENDING', 'INTERIM')


[updateRpuMasterInfo]
UPDATE rm SET 
	rm.currentfaasid = f.objid,
	rm.currentrpuid = f.rpuid 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN rpumaster rm ON r.rpumasterid = rm.objid 
WHERE f.objid = $P{objid}


[findRealProperty]
SELECT rp.*, f.objid AS faasid, f.tdno
FROM realproperty rp 
	LEFT JOIN faas f ON rp.objid = f.realpropertyid 
WHERE rp.pin = $P{pin} 
  AND rp.ry = $P{ry}
  AND (rp.claimno IS NULL OR rp.claimno LIKE $P{claimno} )
  

[findRpu]  
SELECT r.*, f.objid AS faasid, f.tdno
FROM rpu r
	LEFT JOIN faas f ON r.objid = f.rpuid
WHERE r.fullpin = $P{fullpin} 
  AND r.ry = $P{ry}
  AND r.rputype = $P{rputype}

[getLandReference]
select
	r.fullpin, r.totalareasqm, f.owner_name, f.tdno, rp.cadastrallotno 
 from rpu r 
	inner join faas f on f.rpuid = r.objid 
	inner join realproperty rp on rp.objid = r.realpropertyid 
where r.objid=$P{landrpuid} and r.rputype ='land'


[findFaasObjid]   
SELECT objid FROM faas WHERE objid = $P{objid}


[findState]   
SELECT state FROM faas WHERE objid = $P{objid}


[findOpenRealProperty]
SELECT rp.objid 
FROM realproperty rp
	LEFT JOIN faas f ON rp.objid = f.realpropertyid
WHERE rp.pin = $P{pin}
  AND rp.state NOT IN ('CURRENT', 'CANCELLED')
  AND rp.claimno = $P{claimno}
  AND f.objid IS NULL


[findOpenRpu]
SELECT rpu.objid 
FROM rpu rpu
	LEFT JOIN faas f ON rpu.objid = f.rpuid
WHERE rpu.realpropertyid = $P{realpropertyid}
  AND rpu.state NOT IN ('CURRENT', 'CANCELLED')
  AND rpu.rputype = $P{rputype}
  AND f.objid IS NULL



[getHistory]
SELECT 
	f.*,
	prpu.rputype,
	prpu.ry,
	prpu.fullpin ,
	prpu.taxable,
	prpu.totalareaha,
	prpu.totalareasqm,
	prpu.totalbmv,
	prpu.totalmv,
	prpu.totalav,
	rp.section,
	rp.parcel,
	rp.surveyno,
	rp.cadastrallotno,
	rp.blockno,
	rp.claimno,
	b.name AS barangay_name,
	pc.code AS classification_code
FROM faas cf
	INNER JOIN rpu rpu ON cf.rpuid = rpu.objid
	INNER JOIN rpu prpu ON rpu.rpumasterid = prpu.rpumasterid
	INNER JOIN faas f ON prpu.objid = f.rpuid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN barangay b ON rp.barangayid = b.objid 
	INNER JOIN propertyclassification pc ON rpu.classification_objid = pc.objid 
WHERE cf.objid = $P{faasid}
  AND ISNULL(cf.tdno,'') <> f.tdno 
ORDER BY f.tdno DESC 


[getTxnTypes]
SELECT * FROM faas_txntype



[findTrackingNo]
SELECT trackingno FROM rpttracking WHERE objid = $P{objid}



[getSignatories]
SELECT  DISTINCT
	ft.actor_name as assignee_name, ft.actor_title as assignee_title,
	x.assignee_dtsigned, x.type 
FROM faas_task ft, 
(
	SELECT refid, state AS type, max(enddate) AS assignee_dtsigned
	FROM faas_task 
	WHERE refid = $P{objid}
		AND actor_name IS NOT NULL
	GROUP BY refid, state
) x
where ft.refid = x.refid
and ft.state = x.type 
and ft.enddate = x.assignee_dtsigned


[deleteTasks]
DELETE FROM faas_task WHERE refid = $P{objid}

[insertTask]
INSERT INTO faas_task 
	(	objid, refid, state, startdate, enddate, 
		assignee_objid, assignee_name, assignee_title, 
		actor_objid, actor_name, actor_title
	)
VALUES 
	($P{objid}, $P{refid}, $P{state}, $P{startdate}, $P{enddate}, 
		$P{assigneeid}, $P{assigneename}, $P{assigneetitle},
		$P{actorid}, $P{actorname}, $P{actortitle}
	)	


[findLandOwner]	
SELECT f.taxpayer_objid AS objid 
FROM faas f, 
	(
		SELECT landrpuid FROM bldgrpu WHERE objid = $P{rpuid}
		UNION 
		SELECT landrpuid FROM machrpu WHERE objid = $P{rpuid}
		UNION 
		SELECT landrpuid FROM planttreerpu WHERE objid = $P{rpuid}
		UNION 
		SELECT landrpuid FROM miscrpu WHERE objid = $P{rpuid}
	) x
WHERE f.rpuid = x.landrpuid 
  AND f.state <> 'CANCELLED'  



[findLandFaasUnderTransaction]
SELECT f.objid, tdno, utdno 
FROM faas f
	inner join rpu r on f.rpuid = r.objid 
where f.fullpin = $P{pin}
  and f.state NOT IN ( 'CURRENT', 'CANCELLED')
  and r.rputype = 'land' 










[findFaasById]
SELECT  
	f.*,
	tsk.objid AS taskid,
	tsk.state AS taskstate,
	tsk.message AS taskmsg,
	tsk.assignee_objid
FROM faas f
	LEFT JOIN faas_task tsk ON f.objid = tsk.refid AND tsk.enddate IS NULL
WHERE f.objid = $P{objid}


[findRpuById]
SELECT  
	rpu.rputype,
	rpu.ry,
	rpu.objid,
	rpu.suffix,
	rpu.fullpin,
	rpu.taxable,
	rpu.totalmv,
	rpu.totalav,
	rpu.totalareaha,
	rpu.totalareasqm,
	rpu.reclassed,
	rpu.realpropertyid,
	pc.objid AS classification_objid,
	pc.code AS classification_code
FROM rpu rpu
	LEFT JOIN propertyclassification pc ON rpu.classification_objid = pc.objid 
WHERE rpu.objid = $P{objid}


[findRealPropertyById]
select
	rp.objid,
	rp.ry,
	rp.pin,
	rp.surveyno,
	rp.cadastrallotno,
	rp.blockno,
	rp.purok,
	rp.street,
	rp.north,
	rp.south,
	rp.east,
	rp.west,
	b.name AS barangay_name,
	b.objid AS barangay_objid,
	b.parentid AS barangay_parentid
from realproperty rp 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
where rp.objid = $P{objid}	


[findIndividualEntity]
select lastname, firstname, middlename 
from entityindividual
where objid = $P{objid}


[getTxnLogs]
select
	action,
	username,
	txndate 
from txnlog l	
where refid = $P{objid}
order by txndate 

[getTasks]
select * from faas_task where refid = $P{objid} order by startdate 


[updateSignatoryInfo]
update faas_signatory set 
	${updatefields}
where objid = $P{objid}


[updateDataCaptureFlag]
update faas set datacapture = $P{datacapture} where objid = $P{objid}

[deleteFaasUpdate]
delete from faasupdate where faasid = $P{objid}


[getAffectedRpus]	
select 
	arpu.*,
	f.owner_name, f.owner_address,
	f.prevtdno, f.fullpin as prevpin 
from faas_affectedrpu arpu
	inner join faas f on arpu.prevfaasid = f.objid 
where arpu.faasid = $P{objid}
order by f.fullpin 


[getTxnTypeAttributes]
select  0 as selected, attribute
from faas_txntype_attribute 
where txntype_objid = $P{objid} 
order by idx


[getOpenRedFlags]
select objid from rpt_redflag where refid = $P{objid} and state  = 'OPEN'


[findOpenRedFlagCount]
select count(*) as count from rpt_redflag where refid = $P{objid} and state  = 'OPEN'


[findPreviousLandRpu]
select pf.rpuid as objid 
from faas f 
	inner join previousfaas p on f.objid = p.faasid
	inner join faas pf on p.prevfaasid = pf.objid 
where f.objid = $P{objid}
  and pf.state  = 'CANCELLED'


[updateBldgRpuLandRpuId]  
update r set 
	r.landrpuid = $P{objid}
from bldgrpu r, faas f
where r.landrpuid = $P{objid}
  and r.objid = f.rpuid 
  and f.state = 'CURRENT' 


[updateMachRpuLandRpuId]  
update r set 
	r.landrpuid = $P{objid}
from machrpu r, faas f
where r.landrpuid = $P{objid}
  and r.objid = f.rpuid 
  and f.state = 'CURRENT' 


[updatePlantTreeRpuLandRpuId]  
update r set 
	r.landrpuid = $P{objid}
from planttreerpu r, faas f
where r.landrpuid = $P{objid}
  and r.objid = f.rpuid 
  and f.state = 'CURRENT' 


[updateMiscRpuLandRpuId]  
update r set 
	r.landrpuid = $P{objid}
from miscrpu r, faas f
where r.landrpuid = $P{objid}
  and r.objid = f.rpuid 
  and f.state = 'CURRENT' 



[updateBldgRpuLandRpuIdByFaas]
update r set 
	r.landrpuid = lm.currentrpuid
from bldgrpu r, faas f, rpu lr, rpumaster lm
where r.objid = f.rpuid 
  and r.landrpuid = lr.objid 
	and lr.rpumasterid = lm.objid 
  and lr.rputype = 'land'
  and f.objid = $P{objid}
  

[updateMachRpuLandRpuIdByFaas]
update r set 
	r.landrpuid = lm.currentrpuid
from machrpu r, faas f, rpu lr, rpumaster lm 	
where r.objid = f.rpuid 
  and r.landrpuid = lr.objid 
	and lr.rpumasterid = lm.objid 
  and lr.rputype = 'land'
  and f.objid = $P{objid}
  

[updatePlantTreeRpuLandRpuIdByFaas]
update r set 
	r.landrpuid = lm.currentrpuid
from planttreerpu r, faas f, rpu lr, rpumaster lm 
where r.objid = f.rpuid 
  and r.landrpuid = lr.objid 
	and lr.rpumasterid = lm.objid 
  and lr.rputype = 'land'
  and f.objid = $P{objid}
  

[updateMiscRpuLandRpuIdByFaas]
update r set 
	r.landrpuid = lm.currentrpuid
from miscrpu r, faas f, rpu lr, rpumaster lm 
where r.objid = f.rpuid 
  and r.landrpuid = lr.objid 
	and lr.rpumasterid = lm.objid 
  and lr.rputype = 'land'
  and f.objid = $P{objid}
  
  