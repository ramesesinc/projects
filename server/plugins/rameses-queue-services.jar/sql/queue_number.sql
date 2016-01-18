[findNextAvailableNumber]
select * from queue_number qn 
where sectionid=$P{sectionid} 
	and state = 'PENDING' 
	and not exists ( 
		select objid from queue_number_counter where objid=qn.objid 
	) 
order by seriesno 
