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

[modifyPin]
update rpu set 
  fullpin=$P{newpin}, suffix=$P{suffix}, 
  realpropertyid = $P{realpropertyid}
where objid=$P{rpuid}


[modifyLedgerPin]
update rptledger set 
  fullpin=$P{newpin}
where faasid in (
	select objid from faas where rpuid=$P{rpuid} and state <> 'CANCELLED'
)

[modifyFaasPin]
update faas set 
  fullpin=$P{newpin},
  realpropertyid = $P{realpropertyid}
where rpuid=$P{rpuid}

