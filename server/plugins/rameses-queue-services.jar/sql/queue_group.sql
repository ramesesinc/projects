[getSectionCounters]
select 
	c.*, cs.sectionid, 
	( 
		select n.seriesno from queue_number_counter nc 
			inner join queue_number n on nc.objid=n.objid 
		where nc.counterid=c.objid and n.sectionid=s.objid  
	) as seriesno  	
from queue_section s 
	inner join queue_counter_section cs on s.objid=cs.sectionid 
	inner join queue_counter c on cs.counterid=c.objid 
where s.objid=$P{sectionid} 
