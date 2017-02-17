[findBldgInfoById]
select
	br.landrpuid, 
	br.houseno,
	br.psic,
	br.permitno,
	br.permitdate,
	br.permitissuedby,
	br.basevalue,
	br.dtcompleted,
	br.dtoccupied,
	br.floorcount,
	br.depreciation,
	br.depreciationvalue,
	br.totaladjustment,
	br.additionalinfo,
	br.bldgage,
	br.percentcompleted,
	br.condominium,
	br.bldgclass,
	br.predominant,
	br.effectiveage,
	br.condocerttitle,
	br.dtcertcompletion,
	br.dtcertoccupancy
from bldgrpu br 
where br.objid = $P{objid}	


[getStructures]
SELECT objid, name, indexno FROM structure where showinfaas=1 ORDER BY indexno;

[getStructureMaterials]
SELECT m.objid, m.name
FROM structurematerial sm 
	INNER JOIN material m ON sm.material_objid = m.objid 
WHERE sm.structure_objid = $P{objid} 
  AND sm.display = 1 
ORDER BY sm.idx 


[getDetailedStructureMaterials]
SELECT 
  s.code AS structurecode,
  s.name AS structurename,
  m.code AS materialcode,
  m.name AS materialname 
FROM bldgstructure bs 
  LEFT JOIN structure s ON bs.structure_objid = s.objid 
  LEFT JOIN material m ON bs.material_objid = m.objid 
WHERE bs.bldgrpuid = $P{objid}  
ORDER BY bs.floor, s.indexno


[getStructuralTypes]
select 
	stt.objid, 
	stt.floorcount,
	stt.basefloorarea,
	stt.totalfloorarea,
	stt.basevalue,
	stt.unitvalue,
	stt.classification_objid,
	bt.code as bldgtype_code,
	bt.name as bldgtype_name,
	bk.code as bldgkindbucc_bldgkind_code,
	bk.name as bldgkindbucc_bldgkind_name,
	pc.code as classification_code, 
	pc.name as classification_name
from bldgrpu_structuraltype stt
  inner join bldgtype bt on stt.bldgtype_objid = bt.objid 
  inner join bldgkindbucc bucc on stt.bldgkindbucc_objid = bucc.objid 
  inner join bldgkind bk on bucc.bldgkind_objid = bk.objid 
  inner join propertyclassification pc on stt.classification_objid = pc.objid 
where stt.bldgrpuid = $P{objid}


[getBldgUses]
select 
	bu.objid,
	bu.basevalue,
	bu.area,
	bu.basemarketvalue,
	bu.depreciationvalue,
	bu.adjustment,
	bu.marketvalue,
	bu.assesslevel,
	bu.assessedvalue,
	bu.actualuse_objid,
	bal.code as  actualuse_code,
	bal.name as actualuse_name
from bldguse bu
  inner join bldgassesslevel bal on bu.actualuse_objid = bal.objid 
where bu.structuraltype_objid = $P{objid}


[getFloors]
select
	f.objid, 
	f.floorno,
	f.area,
	f.storeyrate,
	f.basevalue,
	f.unitvalue,
	f.basemarketvalue,
	f.adjustment,
	f.marketvalue
from bldgfloor f 
where f.bldguseid = $P{objid}



[getAdditionalItems]
SELECT 
	bfa.objid, 
	bfa.amount,
	bfa.expr,
	bi.code AS additionalitem_code,
	bi.name AS additionalitem_name,
	bi.expr AS additionalitem_expr,
	bi.unit AS additionalitem_unit
FROM bldgflooradditional bfa 
  INNER JOIN bldgadditionalitem bi ON bfa.additionalitem_objid = bi.objid 
WHERE bfa.bldgfloorid = $P{objid}


[getAdditionalItemParams]
SELECT 
	a.param_objid,
	a.intvalue,
	a.decimalvalue,
	p.name AS param_name,
	p.caption AS param_caption,
	p.paramtype AS param_paramtype,
	p.maxvalue AS param_maxvalue,
	p.minvalue AS param_minvalue
FROM bldgflooradditionalparam a
  INNER JOIN rptparameter p ON a.param_objid = p.objid 
WHERE a.bldgflooradditionalid = $P{objid}


[getBldgPropertyAssessments]
SELECT 
	bra.rpuid,
	bal.objid as actualuseid,
	bal.code AS actualuse,
	bal.name AS actualusename,
	bra.marketvalue,
	bra.assesslevel / 100 as assesslevel,
	bra.assessedvalue AS assessedvalue,
	bra.taxable
FROM rpu_assessment bra 
	INNER JOIN bldgassesslevel bal ON bra.actualuse_objid = bal.objid 
WHERE bra.rpuid = $P{objid}	


[getBldgStructureMaterials]
SELECT 
	bs.floor, 
	bs.structure_objid AS structureid, 
	bs.material_objid AS materialid, 
	m.name AS material, 
	'X' as checked,
	sm.display
FROM bldgstructure bs 
	INNER JOIN structurematerial sm ON bs.structure_objid = sm.structure_objid AND bs.material_objid = sm.material_objid
	INNER JOIN material m ON bs.material_objid = m.objid 
	inner join structure s on sm.structure_objid = s.objid 
WHERE bldgrpuid = $P{objid}
and s.showinfaas = 1 


[findTotalAdditionalArea]
select sum(bfa.decimalvalue) AS area 
from bldgflooradditionalparam bfa
	inner join rptparameter p on bfa.param_objid = p.objid 
	inner join bldgflooradditional ba on bfa.bldgflooradditionalid = ba.objid 
	inner join bldgadditionalitem bi on ba.additionalitem_objid = bi.objid 
where bfa.bldgrpuid = $P{objid}
and bi.addareatobldgtotalarea =  1 
and p.name like '%AREA%'


[findBldgUseAdditionalInfo]
select distinct addlinfo 
from bldguse 
where bldgrpuid = $P{rpuid} 
and actualuse_objid = $P{actualuseid}
and addlinfo is not null 