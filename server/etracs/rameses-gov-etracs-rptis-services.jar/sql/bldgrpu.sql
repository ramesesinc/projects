[getStructures]
SELECT 
  bs.*,
  s.code AS structure_code,
  s.name AS structure_name,
  m.code AS material_code,
  m.name AS material_name 
FROM bldgstructure bs 
  LEFT JOIN structure s ON bs.structure_objid = s.objid 
  LEFT JOIN material m ON bs.material_objid = m.objid 
WHERE bs.bldgrpuid = $P{objid}  
ORDER BY bs.floor, s.indexno


[getBldgUses]
SELECT 
  bu.*,
  bal.code AS  actualuse_code,
  bal.name AS actualuse_name
FROM bldguse bu
  INNER JOIN bldgassesslevel bal ON bu.actualuse_objid = bal.objid 
WHERE bu.structuraltype_objid = $P{structuraltypeid}


[getFloors]
SELECT * FROM bldgfloor WHERE bldguseid = $P{bldguseid}  ORDER BY floorno 

[getAdditionalItems]
SELECT bfa.*,
  bi.code AS additionalitem_code,
  bi.name AS additionalitem_name,
  bi.expr AS additionalitem_expr,
  bi.unit AS additionalitem_unit,
  bi.type AS additionalitem_type 
FROM bldgflooradditional bfa 
  INNER JOIN bldgadditionalitem bi ON bfa.additionalitem_objid = bi.objid 
WHERE bfa.bldgfloorid = $P{bldgfloorid}


[getAdditionalItemParams]
SELECT a.*,
  p.name AS param_name,
  p.caption AS param_caption,
  p.paramtype AS param_paramtype,
  p.maxvalue AS param_maxvalue,
  p.minvalue AS param_minvalue
FROM bldgflooradditionalparam a
  INNER JOIN rptparameter p ON a.param_objid = p.objid 
WHERE a.bldgflooradditionalid = $P{bldgflooradditionalid}

[getAssessments]
SELECT 
  ba.*,
  bal.code AS  actualuse_code,
  bal.name AS actualuse_name,
  pc.code AS classification_code, 
  pc.name AS classification_name
FROM rpu_assessment ba
  INNER JOIN bldgassesslevel bal ON ba.actualuse_objid = bal.objid 
  INNER JOIN propertyclassification pc ON ba.classification_objid = pc.objid 
WHERE ba.rpuid = $P{objid}



[deleteAllParams]
DELETE FROM bldgflooradditionalparam WHERE bldgrpuid = $P{objid}

[deleteAllFloorAdditionals]
DELETE FROM bldgflooradditional WHERE bldgrpuid = $P{objid}

[deleteAllFloors]
DELETE FROM bldgfloor WHERE bldgrpuid = $P{objid}

[deleteAllUses]
DELETE FROM bldguse WHERE bldgrpuid = $P{objid}

[deleteAllStructures]
DELETE FROM bldgstructure WHERE bldgrpuid = $P{objid}


[deleteAllStructuralTypes]
DELETE FROM bldgrpu_structuraltype WHERE bldgrpuid = $P{objid}


[getStructuralTypes]
SELECT 
  stt.*,
  bt.objid AS bldgtype_objid,
  bucc.objid AS bldgkindbucc_objid,
  pc.code AS classification_code, 
  pc.name AS classification_name
FROM bldgrpu_structuraltype stt
  INNER JOIN bldgtype bt ON stt.bldgtype_objid = bt.objid 
  INNER JOIN bldgkindbucc bucc ON stt.bldgkindbucc_objid = bucc.objid 
  INNER JOIN propertyclassification pc ON stt.classification_objid = pc.objid 
WHERE stt.bldgrpuid = $P{bldgrpuid}


[getBldgLands]
SELECT 
  bl.*, 
  f.owner_name AS landfaas_owner_name, 
  f.tdno AS landfaas_tdno, f.fullpin AS landfaas_fullpin
FROM bldgrpu_land bl 
  INNER JOIN faas f ON bl.landfaas_objid = f.objid 
WHERE bl.bldgrpuid = $P{objid}  