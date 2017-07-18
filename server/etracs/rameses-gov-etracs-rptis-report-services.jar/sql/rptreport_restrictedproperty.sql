[getRestrictedProperties]
select 
    b.name as barangay, 
    f.tdno, f.fullpin, f.owner_name, f.administrator_name, 
    rp.cadastrallotno, pc.code as classcode, 
    r.totalareaha, r.totalareasqm,
    r.totalmv, r.totalav,
    frt.name as restrictiontype,
    fr.remarks
from faas f 
    inner join realproperty rp on f.realpropertyid = rp.objid 
    inner join rpu r on f.rpuid = r.objid 
    inner join propertyclassification pc on r.classification_objid = pc.objid 
    inner join barangay b on rp.barangayid = b.objid 
    inner join faas_restriction fr on f.objid  = fr.parent_objid  
    inner join faas_restriction_type frt on fr.restrictiontype_objid = frt.objid 
where f.lguid like $P{lguid}
and rp.barangayid like $P{barangayid}
and rp.section like $P{section}
and fr.state = 'ACTIVE'
and fr.txndate >= $P{startdate} and fr.txndate < $P{enddate}
order by f.tdno 


[getUnrestrictedProperties]
select 
    b.name as barangay, 
    f.tdno, f.fullpin, f.owner_name, f.administrator_name, 
    rp.cadastrallotno, pc.code as classcode, 
    r.totalareaha, r.totalareasqm,
    r.totalmv, r.totalav,
    frt.name as restrictiontype,
    fr.remarks
from faas f 
    inner join realproperty rp on f.realpropertyid = rp.objid 
    inner join rpu r on f.rpuid = r.objid 
    inner join propertyclassification pc on r.classification_objid = pc.objid 
    inner join barangay b on rp.barangayid = b.objid 
    inner join faas_restriction fr on f.objid  = fr.parent_objid  
    inner join faas_restriction_type frt on fr.restrictiontype_objid = frt.objid 
where f.lguid like $P{lguid}
and rp.barangayid like $P{barangayid}
and rp.section like $P{section}
and fr.state = 'UNRESTRICTED'
and fr.txndate >= $P{startdate} and fr.txndate < $P{enddate}
order by f.tdno 
