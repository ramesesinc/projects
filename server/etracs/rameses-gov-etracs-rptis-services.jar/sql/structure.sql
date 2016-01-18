[getList]
SELECT * 
FROM structure 
WHERE code LIKE $P{searchtext} OR name LIKE $P{searchtext}
ORDER BY indexno

[approve]
UPDATE structure SET state = 'APPROVED' WHERE objid = $P{objid}

[insertStructureMaterial]
INSERT INTO structurematerial 
	(structure_objid, material_objid)
VALUES	
	($P{structure_objid}, $P{material_objid})


[deleteStructureMaterial]
DELETE FROM structurematerial
WHERE structure_objid = $P{structure_objid} AND material_objid = $P{material_objid}


[deleteStructureMaterials]
DELETE FROM structurematerial WHERE structure_objid = $P{structure_objid} 


[getStructureMaterials]
SELECT 
	sm.*,
	m.code AS 'material_code',
	m.name AS 'material_name'
FROM structurematerial sm
	INNER JOIN material m ON sm.material_objid = m.objid 
WHERE sm.structure_objid = $P{structure_objid}
ORDER BY sm.idx, m.code 



#----------------------------------------------------------
#
# LOOKUP SUPPORT 
#
#----------------------------------------------------------

[getStructures]
SELECT *
FROM structure
WHERE state = 'APPROVED'
  AND code LIKE $P{searchtext} OR name LIKE $P{searchtext} 
ORDER BY indexno


[getMaterials]
SELECT 
	m.*
FROM structurematerial sm
	INNER JOIN material m ON sm.material_objid = m.objid
WHERE sm.structure_objid = $P{structure_objid}	
  AND ( m.code LIKE $P{searchtext} OR m.name LIKE $P{searchtext} ) 
ORDER BY m.name 

