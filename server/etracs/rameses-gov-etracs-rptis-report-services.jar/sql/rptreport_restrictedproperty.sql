[getRestrictedProperties]
select 
    b.name as barangay, 
    rl.tdno, rl.fullpin, rl.owner_name, rl.administrator_name, 
    rl.cadastrallotno, rl.classcode, 
    rl.totalareaha, rl.totalareaha * 10000 as totalareasqm,
    rl.totalmv, rl.totalav,
    frt.name as restrictiontype,
    fr.remarks
from rptledger rl 
    inner join faas f on rl.faasid = f.objid 
    inner join realproperty rp on f.realpropertyid = rp.objid 
    inner join barangay b on rl.barangayid = b.objid 
    inner join faas_restriction fr on rl.objid = fr.ledger_objid 
    inner join faas_restriction_type frt on fr.restrictiontype_objid = frt.objid 
where f.lguid like $P{lguid}
and rl.barangayid like $P{barangayid}
and rp.section like $P{section}
and fr.state = 'ACTIVE'
and fr.txndate >= $P{startdate} and fr.txndate < $P{enddate}
order by rl.tdno 


[getUnrestrictedProperties]
select 
    b.name as barangay, 
    rl.tdno, rl.fullpin, rl.owner_name, rl.administrator_name, 
    rl.cadastrallotno, rl.classcode, 
    rl.totalareaha, rl.totalareaha * 10000 as totalareasqm,
    rl.totalmv, rl.totalav,
    frt.name as restrictiontype,
    fr.remarks,
    fr.receipt_receiptno as receiptno,
    fr.receipt_receiptdate as receiptdate, 
    fr.receipt_lastyearpaid as lastyearpaid,
    fr.receipt_lastqtrpaid as lastqtrpaid
from rptledger rl 
    inner join faas f on rl.faasid = f.objid 
    inner join realproperty rp on f.realpropertyid = rp.objid 
    inner join barangay b on rl.barangayid = b.objid 
    inner join faas_restriction fr on rl.objid = fr.ledger_objid 
    inner join faas_restriction_type frt on fr.restrictiontype_objid = frt.objid 
where f.lguid like $P{lguid}
and rl.barangayid like $P{barangayid}
and rp.section like $P{section}
and fr.state = 'UNRESTRICTED'
and fr.txndate >= $P{startdate} and fr.txndate < $P{enddate}
order by rl.tdno 