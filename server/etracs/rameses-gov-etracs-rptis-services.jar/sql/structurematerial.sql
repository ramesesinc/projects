[getItems]
select sm.*, m.code as material_code, m.name as material_name 
from structurematerial sm 
	inner join material m on sm.material_objid = m.objid 
where structure_objid = $P{structureid}	
order by idx 


[deleteMaterial]
delete from structurematerial 
where structure_objid = $P{structureid}
and material_objid = $P{materialid}


[deleteAllMaterials]
delete from structurematerial 
where structure_objid = $P{structureid}


[updateMaterial]
update structurematerial set 
	display = $P{display},
	idx = $P{idx}
where structure_objid = $P{structureid}
and material_objid = $P{materialid}