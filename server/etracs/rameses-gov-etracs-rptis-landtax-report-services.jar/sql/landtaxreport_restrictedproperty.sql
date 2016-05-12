[getRestrictedPropertyListing]
select 
    f.tdno, f.fullpin, f.owner_name, f.administrator_name, 
    rp.cadastrallotno, pc.code as classcode, 
    r.totalareaha, r.totalareasqm,
    r.totalmv, r.totalav,
    f.restrictionid,
    (select r.remarks from rptledger rl, rptledger_restriction r 
      where rl.faasid = f.objid and rl.objid = r.parentid and r.restrictionid = f.restrictionid) as restrictionreason 
from faas f
    inner join rpu r on f.rpuid = r.objid 
    inner join realproperty rp on f.realpropertyid = rp.objid 
    inner join barangay b on rp.barangayid = b.objid 
    inner join propertyclassification pc on r.classification_objid = pc.objid 
where f.lguid like $P{lguid}
and rp.barangayid like $P{barangayid}
and rp.section like $P{section}
and f.state = 'CURRENT'
and f.restrictionid is not null 
order by f.tdno 