



[removeAFControlDetailByBatch]
delete from af_control_detail where controlid in ( 
	select objid from af_control 
	where batchno = $P{batchno} 
		and objid = af_control_detail.controlid 
)
