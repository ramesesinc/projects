[getList]
SELECT *
FROM assessmentnotice
where 1=1 ${filters}
ORDER BY txnno DESC 


[getItems]
SELECT 
	ni.*,
	${fields}
FROM assessmentnoticeitem ni 
	INNER JOIN faas f ON ni.faasid = f.objid 
	LEFT JOIN txnsignatory ts on ts.refid = f.objid and ts.type='APPROVER'
	INNER JOIN rpu rpu ON f.rpuid = rpu.objid
	INNER JOIN propertyclassification pc ON rpu.classification_objid = pc.objid
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE ni.assessmentnoticeid = $P{objid}

[getApprovedFaasList]
SELECT ${fields}
FROM faas f 
	INNER JOIN rpu rpu ON f.rpuid = rpu.objid
	INNER JOIN propertyclassification pc ON rpu.classification_objid = pc.objid
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE f.taxpayer_objid = $P{taxpayerid}
  AND f.state = 'CURRENT'


[findFaasById]
SELECT ${fields}
FROM faas f 
	INNER JOIN rpu rpu ON f.rpuid = rpu.objid
	INNER JOIN propertyclassification pc ON rpu.classification_objid = pc.objid
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE f.objid = $P{faasid}
  AND f.state = 'CURRENT'

[getTaxpayerList]  
select distinct f.taxpayer_objid as objid,
	 f.taxpayer_name as name, 
	f.taxpayer_address as address 
from faas f 
 inner join rpu r on f.rpuid = r.objid 
 inner join realproperty rp on rp.objid = r.realpropertyid 
where f.state='CURRENT' 
	and r.ry=$P{revisionyear}
	and rp.barangayid like $P{barangayid}
	and rp.section like $P{section}  

[getTaxpayerPropertyListForBatch]  
select f.objid 
from faas f 
 inner join rpu r on f.rpuid = r.objid 
 inner join realproperty rp on rp.objid = r.realpropertyid 
where f.state='CURRENT' 
	and r.ry=$P{revisionyear}
	and rp.barangayid like $P{barangayid}
	and rp.section like $P{section}


[getBldgTypes]	
select bt.code, bk.name as bldgkind 
from bldgrpu_structuraltype st
	inner join bldgtype bt on st.bldgtype_objid = bt.objid 
	inner join bldgkindbucc bucc on st.bldgkindbucc_objid = bucc.objid
	inner join bldgkind bk on bucc.bldgkind_objid = bk.objid 
where st.bldgrpuid = $P{rpuid}

