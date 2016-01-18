[findAfControl]
SELECT ac.* ,
  case when af.formtype = 'serial' then ac.startseries else null end as sstartseries,
  case when af.formtype = 'serial' then ac.endseries else null end as sendseries
FROM  af_control ac 
  inner join af on af.objid = ac.afid 
 WHERE ac.objid = $P{controlid}

[findAfInventory]
SELECT * FROM af_inventory WHERE objid = $P{controlid}

[getAfInventoryDetails]
SELECT d.* FROM af_inventory_detail d
WHERE d.controlid = $P{controlid}
  AND NOT EXISTS(
    SELECT * FROM remittance_af WHERE objid = d.objid 
  )


[updateAfModeToRemote]
UPDATE af_control SET txnmode = 'REMOTE' WHERE objid = $P{controlid}


