[getTransmittalItems]
select * from rpttransmittal_item where transmittalid = $P{objid}

[getRequirements]
select * from rpt_requirement where refid = $P{refid}

[getImageHeaders]
select * from image_header where refid = $P{refid}

[getImageChunks]
select *
from etracs_image.image_chunk 
where parentid = $P{objid}
order by fileno

[getSignatories]
select * from faas_task where refid = $P{refid}
union 
select * from subdivision_task where refid = $P{refid}
union 
select * from consolidation_task where refid = $P{refid}

