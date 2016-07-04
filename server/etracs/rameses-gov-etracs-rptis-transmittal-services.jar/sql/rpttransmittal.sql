[findActiveTransmittalItem]
select t.objid, t.txnno 
from rpttransmittal t 
	inner join rpttransmittal_item ti on t.objid = ti.parentid
where t.state <> 'APPROVED'
  and ti.refid = $P{refid}
  and ti.objid <> $P{objid}