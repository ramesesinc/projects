[getTDInfo]
SELECT
	f.objid as faasid,
	f.*,
	rp.*,
	r.*,
	f.fullpin as displaypin,
	CASE WHEN p.objid IS NOT NULL THEN p.name ELSE c.name END AS parentlguname, 
	CASE WHEN p.objid IS NOT NULL THEN p.indexno ELSE c.indexno END AS parentlguindex,   
	CASE WHEN m.objid IS NOT NULL THEN m.name ELSE '' END AS lguname, 
	CASE WHEN m.objid IS NOT NULL THEN m.indexno ELSE d.indexno END AS lguindex,  
	b.name AS barangay,  
	b.indexno AS barangayindex, 
	et.code AS legalbasis, 
	ry.ordinanceno, ry.ordinancedate, ry.sangguniangname,
	(select trackingno from rpttracking t where objid = f.objid) as trackingno
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
	LEFT JOIN exemptiontype et ON r.exemptiontype_objid = et.objid 
	LEFT JOIN municipality m ON b.parentid = m.objid  
	LEFT JOIN district d ON b.parentid = d.objid 
	LEFT JOIN province p ON m.parentid = p.objid 
	LEFT JOIN city c ON d.parentid = c.objid 
	LEFT JOIN rysettinginfo ry on ry.ry = r.ry 
WHERE f.objid = $P{faasid}


[getAnnotationMemoranda]
SELECT memoranda, memoranda as annotationtext
FROM faasannotation 
WHERE faasid = $P{faasid} 
  AND state = 'APPROVED'


[getStandardLandAssessment]
SELECT 
	pc.code AS classcode,
	pc.name AS classification,
	case when lal.objid is not null then lal.code else ptl.code end AS actualusecode,
	case when lal.objid is not null then lal.name else ptl.name end AS actualuse,
	SUM(case when pc.name = 'AGRICULTURAL' then r.areaha else r.areasqm end ) AS area,
	case when pc.name = 'AGRICULTURAL' then 'HA' else 'SQM' end as areatype,
	SUM(r.marketvalue) AS marketvalue,
	r.assesslevel,
	SUM(r.assessedvalue) AS assessedvalue,
	SUM(r.areasqm) AS areasqm,
	SUM(r.areaha) AS areaha 
FROM faas f
	INNER JOIN rpu_assessment r ON f.rpuid = r.rpuid
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	LEFT JOIN landassesslevel lal ON r.actualuse_objid = lal.objid 
	LEFT JOIN planttreeassesslevel ptl ON r.actualuse_objid = ptl.objid 
WHERE f.objid = $P{faasid}
GROUP BY 
	pc.code, pc.name, 
	case when lal.objid is not null then lal.code else ptl.code end,
	case when lal.objid is not null then lal.name else ptl.name end,
	r.assesslevel 


[getDetailedLandAssessment]
SELECT 
	'land' AS propertytype,
	dc.code AS dominantclasscode,
	dc.name AS dominantclassification,
	pc.code AS classcode,
	pc.name AS classification,
	lal.name AS actualuse,
	ld.areatype,
	ld.taxable,
	ld.assesslevel,
	lspc.name AS specificclass,
	sub.name AS subclass,
	SUM(ld.area) AS area,	
	SUM(ld.marketvalue) AS marketvalue,
	SUM(ld.assessedvalue) AS assessedvalue,
	SUM(ld.areasqm) AS areasqm,
	SUM(ld.areaha) AS areaha 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN landrpu lr ON r.objid = lr.objid
	INNER JOIN landdetail ld ON lr.objid = ld.landrpuid 
	INNER JOIN landspecificclass lspc ON ld.landspecificclass_objid = lspc.objid 
	INNER JOIN landassesslevel lal ON ld.actualuse_objid = lal.objid 
	INNER JOIN lcuvsubclass sub ON ld.subclass_objid = sub.objid 
	INNER JOIN lcuvspecificclass spc ON ld.specificclass_objid = spc.objid 
	INNER JOIN propertyclassification pc ON spc.classification_objid = pc.objid 
	INNER JOIN propertyclassification dc ON r.classification_objid = dc.objid 
WHERE f.objid = $P{faasid}
GROUP BY dc.code, dc.name, pc.code, pc.name, lal.code, lal.name, ld.areatype, ld.assesslevel,
	lspc.code, lspc.name, sub.code, sub.name 


[getLandPlantTreeAssessment]
SELECT 
	'PLANT/TREE' AS propertytype,
	pc.code AS classcode,
	pc.name AS classification,
	ptal.code AS actualusecode,
	ptal.name AS actualuse,
	bra.assesslevel, 
	bra.marketvalue,
	bra.assessedvalue,
	r.totalareasqm AS area,
	'SQM' AS areatype
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN rpu_assessment bra ON r.objid = bra.rpuid 
	INNER JOIN planttreeassesslevel ptal ON bra.actualuse_objid = ptal.objid 
WHERE f.objid = $P{faasid}	



[getBldgInfo]
SELECT 
	br.*
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN bldgrpu br ON r.objid = br.objid 
WHERE f.objid = $P{faasid}

[getBldgKindInfo]
SELECT DISTINCT
	bk.name AS bldgdescription,
	rys.predominant
FROM faas f
	INNER JOIN bldgrpu_structuraltype bs ON f.rpuid = bs.bldgrpuid
	INNER JOIN bldgkindbucc bucc ON bs.bldgkindbucc_objid = bucc.objid 
	INNER JOIN bldgkind bk ON bucc.bldgkind_objid = bk.objid 
	INNER JOIN bldgtype bt ON bs.bldgtype_objid = bt.objid 
	INNER JOIN bldgrysetting rys ON bt.bldgrysettingid = rys.objid 
WHERE f.objid = $P{faasid}




[getBldgAssessments]
SELECT 
	'BLDG' as propertytype, 
	pc.code AS classcode,
	pc.name AS classification,
	bal.code AS actualusecode,
	bal.name AS actualuse,
	r.areasqm AS area,
	'SQM' as areatype,
	r.marketvalue AS marketvalue,
	r.assesslevel,
	r.assessedvalue AS assessedvalue,
	r.areasqm AS areasqm,
	r.areaha AS areaha 
FROM faas f
	INNER JOIN rpu_assessment r ON f.rpuid = r.rpuid
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN bldgassesslevel bal ON r.actualuse_objid = bal.objid 
WHERE f.objid = $P{faasid}	


[getMachines]
SELECT 
	m.name AS machinename
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN machrpu mr ON r.objid = mr.objid 
	INNER JOIN machuse mu ON mr.objid = mu.machrpuid 
	INNER JOIN machdetail md ON mu.objid = md.machuseid 
	INNER JOIN machine m ON md.machine_objid = m.objid 
WHERE f.objid = $P{faasid}	



[getMachineAssessment]
SELECT 
	'MACH' AS propertytype,
	pc.code AS classcode,
	pc.name AS classification,
	mal.code AS actualusecode,
	mal.name AS actualuse,
	r.areasqm AS area,
	'SQM' as areatype,
	r.marketvalue AS marketvalue,
	r.assesslevel,
	r.assessedvalue AS assessedvalue,
	r.areasqm AS areasqm,
	r.areaha AS areaha 
FROM faas f
	INNER JOIN rpu_assessment r ON f.rpuid = r.rpuid
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN machassesslevel mal ON r.actualuse_objid = mal.objid 
WHERE f.objid = $P{faasid}	


[getMachineDetailedAssessment]
SELECT 
	'MACH' AS propertytype,
	pc.name AS classification,
	m.name AS machine,
	mal.name AS actualuse,
	md.depreciation,
	md.marketvalue,
	md.basemarketvalue,
	md.assesslevel,
	md.assessedvalue,
	mal.objid AS actualuseid,
	0.0 AS area,
	md.operationyear,
	md.replacementcost,
	md.brand,
	md.model
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN machrpu mr ON r.objid = mr.objid 
	INNER JOIN machuse mu ON mr.objid = mu.machrpuid 
	INNER JOIN machassesslevel mal ON mu.actualuse_objid = mal.objid 
	INNER JOIN machdetail md ON mu.objid = md.machuseid 
	INNER JOIN machine m ON md.machine_objid = m.objid
WHERE f.objid = $P{faasid}	


[getPlantTreeAssessment]
SELECT 
	'PLANT/TREE' AS propertytype,
	pc.code AS classcode,
	pc.name AS classification,
	ptal.code AS actualusecode,
	ptal.name AS actualuse,
	bra.assesslevel, 
	bra.marketvalue,
	bra.assessedvalue,
	r.totalareasqm AS area,
	'SQM' AS areatype
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN rpu_assessment bra ON r.objid = bra.rpuid 
	INNER JOIN planttreeassesslevel ptal ON bra.actualuse_objid = ptal.objid 
WHERE f.objid = $P{faasid}	


[getMiscItems]
SELECT 
	mi.name 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN miscrpu mr ON r.objid = mr.objid 
	INNER JOIN miscrpuitem mri ON mr.objid = mri.miscrpuid 
	INNER JOIN miscitem mi ON mri.miscitem_objid = mi.objid 
WHERE f.objid = $P{faasid}


[getMiscAssessment]
SELECT 
	'MISC' AS propertytype,
	pc.code AS classcode,
	pc.name AS classification,
	mal.code AS actualusecode,
	mal.name AS actualuse,
	bra.assesslevel, 
	bra.marketvalue,
	bra.assessedvalue,
	r.totalareasqm AS area,
	'SQM' AS areatype
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN rpu_assessment bra ON r.objid = bra.rpuid 
	INNER JOIN miscassesslevel mal ON bra.actualuse_objid = mal.objid 
WHERE f.objid = $P{faasid}	

[findLandReference]
select
	f.tdno, f.owner_name, 
	rp.cadastrallotno, 
	r.totalareasqm, r.totalareaha
 from rpu r
	inner join faas f on r.objid = f.rpuid  
	inner join realproperty rp on f.realpropertyid = rp.objid,
	(
		select 
			case 
				when br.landrpuid is not null then br.landrpuid
				when mr.landrpuid is not null then mr.landrpuid
				when pr.landrpuid is not null then pr.landrpuid
				else mir.landrpuid 
			end as landrpuid 
		from faas f
			left join bldgrpu br on f.rpuid = br.objid 
			left join machrpu mr on f.rpuid = mr.objid 
			left join planttreerpu pr on f.rpuid = pr.objid 
			left join miscrpu mir on f.rpuid = mir.objid 
		where f.objid = $P{faasid}
	) x 
where r.objid = x.landrpuid 
  and f.state <> 'CANCELLED' 



[getBldgLands]
SELECT 
  bl.*, 
  f.owner_name AS landfaas_owner_name, 
  f.tdno AS landfaas_tdno, f.fullpin AS landfaas_fullpin
FROM bldgrpu_land bl 
  INNER JOIN faas f ON bl.landfaas_objid = f.objid 
WHERE bl.bldgrpuid = $P{objid}  


[findEntityContactInfo]
select 
	case 
		when e.mobileno is not null then mobileno 
		when e.phoneno is not null then phoneno 
		else e.email 
	end as contactno,
	case 
		when i.objid is not null then i.tin
		when j.objid is not null then j.tin
		else null 
	end as tin
from entity e 
	left join entityindividual i on e.objid = i.objid 
	left join entityjuridical j on e.objid = j.objid 
where e.objid = $P{objid}



[getCancellingFaasInfo]
select 
	f.tdno,
	f.fullpin,
	f.effectivityyear,
	f.effectivityqtr
from previousfaas pf 
	inner join faas f on pf.faasid = f.objid 
where pf.prevfaasid = $P{objid}

[getFaasIds]
select
  f.objid, f.tdno
from faas f  
  inner join rpu r on r.objid = f.rpuid 
  inner join realproperty rp on rp.objid = r.realpropertyid 
where rp.barangayid LIKE $P{barangayid}
	and r.ry = $P{revisionyear} 
	and f.state LIKE $P{state}
	${sectionfilter}
	${starttdnofilter}
	${endtdnofilter}
order by f.tdno 




[findCancelFaasReason]
select 
	ctd.name as reason,
	cf.remarks, 
	cf.txndate
from cancelledfaas cf 
	inner join canceltdreason ctd on cf.reason_objid = ctd.objid 
where cf.faasid = $P{objid}
and cf.state = 'APPROVED' 


[getSignatories]
select 
	ft.objid,
	ft.state,
	ft.actor_name,
	ft.actor_title,
	ft.signature,
	ft.enddate as dtsigned
from faas_task ft,
	(
		select 
			refid, 
			state, 
			max(enddate) as enddate 
		from faas_task 
		where refid = $P{objid}
			and state not like 'assign%'
			and enddate is not null 
		group by refid, state 
	) t 
where ft.refid = $P{objid}
  and ft.refid = t.refid 
  and ft.state = t.state 
  and ft.enddate = t.enddate



[findAdjustmentFactor]
select sum(la.adjustment) / sum(la.basemarketvalue) as adjfactor
from faas f
	inner join landadjustment la on f.rpuid = la.landrpuid 
where f.objid = $P{faasid}



[findTotalAdditionlItemArea]
select 
	SUM( case when decimalvalue is not null then decimalvalue else intvalue end ) as additionalarea 
from faas f 
	inner join bldgflooradditional bfa on f.rpuid = bfa.bldgrpuid
	inner join bldgadditionalitem bi on bfa.additionalitem_objid = bi.objid 
	inner join bldgflooradditionalparam p on bfa.objid = p.bldgflooradditionalid
where f.objid = $P{objid}
and bi.addareatobldgtotalarea = 1
and param_objid = 'AREA_SQM'
