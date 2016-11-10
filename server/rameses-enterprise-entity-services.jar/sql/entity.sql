[getList]
SELECT 
	e.objid, e.entityno, e.name, e.address_text, e.address_objid, e.type	 
FROM entity e 
WHERE e.entityname LIKE $P{searchtext}  
ORDER BY e.entityname 

[getLookup]
SELECT 
	e.objid, e.entityno, e.name, e.address_text, e.address_objid, e.type, 
	(SELECT bldgno FROM entity_address WHERE objid=e.address_objid) as address_bldgno, 
	(SELECT bldgname FROM entity_address WHERE objid=e.address_objid) as address_bldgname,  
	(SELECT unitno FROM entity_address WHERE objid=e.address_objid) as address_unitno , 
	(SELECT street FROM entity_address WHERE objid=e.address_objid) as address_street ,  
	(SELECT subdivision FROM entity_address WHERE objid=e.address_objid) as address_subdivision, 
	(SELECT barangay_name FROM entity_address WHERE objid=e.address_objid) as address_barangay_name, 
	(SELECT city FROM entity_address WHERE objid=e.address_objid) as address_city, 
	(SELECT municipality FROM entity_address WHERE objid=e.address_objid) as address_municipality, 
	(SELECT province FROM entity_address WHERE objid=e.address_objid) as address_province 
FROM entity e 
WHERE e.entityname LIKE $P{searchtext} 
 ${filter} 
ORDER BY e.entityname 

[updateName]
UPDATE entity 
SET name=$P{name}
WHERE objid=$P{objid}


[findEntity]
select * from entity where objid = $P{objid}

[findIndividual]
select * from entityindividual where objid = $P{objid}

[findJuridical]
select * from entityjuridical where objid = $P{objid}

[findMultiple]
select * from entitymultiple where objid = $P{objid}


[insertIndividual]
insert into entityindividual(
	objid,
	lastname,
	firstname,
	middlename,
	birthdate,
	birthplace,
	citizenship,
	gender,
	civilstatus,
	profession,
	tin,
	sss,
	height,
	weight,
	acr,
	religion,
	photo,
	thumbnail
)
values (
	$P{objid},
	$P{lastname},
	$P{firstname},
	$P{middlename},
	$P{birthdate},
	$P{birthplace},
	$P{citizenship},
	$P{gender},
	$P{civilstatus},
	$P{profession},
	$P{tin},
	$P{sss},
	$P{height},
	$P{weight},
	$P{acr},
	$P{religion},
	$P{photo},
	$P{thumbnail}
)


[insertJuridical]
insert into entityjuridical(
	objid,
	tin,
	dtregistered,
	orgtype,
	nature,
	placeregistered,
	administrator_name,
	administrator_address,
	administrator_position
)
values(
	$P{objid},
	$P{tin},
	$P{dtregistered},
	$P{orgtype},
	$P{nature},
	$P{placeregistered},
	$P{administrator_name},
	$P{administrator_address},
	$P{administrator_position}
)


[insertMultiple]
insert into entitymultiple(
	objid,
	fullname
)
values(
	$P{objid},
	$P{fullname}
)
