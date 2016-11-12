[getPin]
SELECT * FROM pin WHERE pin = $P{pin}

[updateLedgerPin]
UPDATE rptledger SET 
	fullpin = $P{newpin},
	municityid = $P{municityid},
	municityname = $P{municityname},
	barangay = $P{barangay} 
WHERE faasid = $P{faasid} 

[updateRealProperty]
UPDATE realproperty SET
	munidistrict = $P{munidistrict},
	barangay = $P{barangay},
	barangayid = $P{barangayid},
	pintype = $P{pintype},
	pin = $P{pin},
	munidistrictindex = $P{munidistrictindex},
	barangayindex = $P{barangayindex},
	section = $P{section},
	parcel = $P{parcel}
WHERE landfaasid = $P{faasid}



[insertPin]
INSERT INTO pin (pin, claimno, docstate) 
VALUES($P{pin}, '-', $P{docstate}) 

[deletePin]
DELETE FROM pin 
WHERE pin = $P{pin} AND claimno = '-'



[findByPinRy]
SELECT * FROM realproperty WHERE pin = $P{pin} AND ry = $P{ry} AND state <> 'CANCELLED'

[modifyRpuPin]
update rpu set 
  fullpin=$P{newpin}, suffix=$P{suffix}, 
  realpropertyid = $P{realpropertyid}
where objid=$P{rpuid}


[modifyLedgerPin]
update rptledger set 
  fullpin=$P{newpin}
where faasid = $P{faasid}

[modifyFaasPin]
update faas set 
  fullpin=$P{newpin},
  realpropertyid = $P{realpropertyid}
where objid = $P{faasid}


[findFaasInfo]
select objid, fullpin, rpuid, realpropertyid, taxpayer_objid from faas where objid = $P{faasid}


[findLandFaas]
select f.objid, f.realpropertyid, f.rpuid, f.fullpin
from faas f 
	inner join realproperty rp on f.realpropertyid = rp.objid 
	inner join rpu r on f.rpuid = r.objid 
where f.objid = $P{landfaasid}


[findLandRpuByPinRy]
SELECT r.objid 
FROM faas f 
	inner join rpu r on f.rpuid = r.objid 
where r.fullpin = $P{pin}	 and r.ry = $P{ry}
and r.rputype = 'land'
and f.state <> 'CANCELLED'
order by r.state 


[modifyBldgLandRpu]
update bldgrpu set landrpuid = $P{landrpuid} where objid = $P{rpuid}

[modifyMachLandRpu]
update machrpu set landrpuid = $P{landrpuid} where objid = $P{rpuid}	

[modifyPlantTreeLandRpu]
update planttreerpu set landrpuid = $P{landrpuid} where objid = $P{rpuid}		

[modifyMiscLandRpu]
update miscrpu set landrpuid = $P{landrpuid} where objid = $P{rpuid}		


[getImprovements]
select x.* 
from (
	select r.objid, r.suffix, r.rputype, f.fullpin, f.objid as faasid
	from faas f 
	inner join bldgrpu br on f.rpuid = br.objid 
	inner join rpu r on br.objid = r.objid 
	where landrpuid = $P{landrpuid}
	and f.state <> 'CANCELLED' 

	union 

	select r.objid, r.suffix, r.rputype, f.fullpin, f.objid as faasid
	from faas f 
	inner join machrpu br on f.rpuid = br.objid 
	inner join rpu r on br.objid = r.objid 
	where landrpuid = $P{landrpuid}
	and f.state <> 'CANCELLED' 

	union 

	select r.objid, r.suffix, r.rputype, f.fullpin, f.objid as faasid
	from faas f 
	inner join planttreerpu br on f.rpuid = br.objid 
	inner join rpu r on br.objid = r.objid 
	where landrpuid = $P{landrpuid}
	and f.state <> 'CANCELLED' 

	union 

	select r.objid, r.suffix, r.rputype, f.fullpin, f.objid as faasid
	from faas f 
	inner join miscrpu br on f.rpuid = br.objid 
	inner join rpu r on br.objid = r.objid 
	where landrpuid = $P{landrpuid}
	and f.state <> 'CANCELLED' 
) x