[findDuplicateRpuPIN]
select tdno 
from faas f 
	inner join rpu r on f.rpuid = r.objid
where f.realpropertyid = $P{realpropertyid}	
  and r.suffix = $P{suffix}
  and f.state <> 'CANCELLED'

[getLandRpusByRealProperty]
select r.objid, r.state  
from realproperty rp 
	inner join rpu r on rp.objid = r.realpropertyid
where rp.pin = $P{pin}
  and rp.ry = $P{ry}
  and r.rputype = 'land' 
  and r.state <> 'CANCELLED'   


[getAffectedRpus]	
select 
	arpu.*,
	f.owner_name, f.owner_address,
	f.prevtdno, f.fullpin as prevpin 
from faas_affectedrpu arpu
	inner join faas f on arpu.prevfaasid = f.objid 
where arpu.faasid = $P{objid}
order by f.fullpin 


[getAffectedRpusByLandFaasId]
select bf.objid as prevfaasid
from faas lf 
	inner join bldgrpu br on br.landrpuid = lf.rpuid 
	inner join faas bf on br.objid = bf.rpuid 
where lf.objid = $P{objid}
  and bf.state = 'CURRENT'

union 
  
select bf.objid as prevfaasid
from faas lf 
	inner join machrpu br on br.landrpuid = lf.rpuid 
	inner join faas bf on br.objid = bf.rpuid 
where lf.objid = $P{objid}
  and bf.state = 'CURRENT'

union 
  
select bf.objid as prevfaasid
from faas lf 
	inner join planttreerpu br on br.landrpuid = lf.rpuid 
	inner join faas bf on br.objid = bf.rpuid 
where lf.objid = $P{objid}
  and bf.state = 'CURRENT'

union 

select bf.objid as prevfaasid
from faas lf 
	inner join miscrpu br on br.landrpuid = lf.rpuid 
	inner join faas bf on br.objid = bf.rpuid 
where lf.objid = $P{objid}
  and bf.state = 'CURRENT'


