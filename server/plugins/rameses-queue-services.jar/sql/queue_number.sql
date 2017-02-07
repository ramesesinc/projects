[findNextAvailableNumber]
select * from queue_number qn 
where sectionid=$P{sectionid} 
	and state = 'PENDING' 
	and not exists ( 
		select objid from queue_number_counter where objid=qn.objid 
	) 
order by seriesno 

[getPendingList]
select * from ( 
	select qn.*, qs.title as sectiontitle  
	from queue_number qn 
		inner join queue_section qs on qn.sectionid=qs.objid 
	where qn.sectionid=$P{sectionid} 
		and qn.state='PENDING' 
)xxx 
order by groupid, sectionid, seriesno 

[getPendingSections]
select distinct 
	qs.objid, qs.title, qs.groupid, qg.title as grouptitle 
from queue_number qn 
	inner join queue_section qs on qn.sectionid=qs.objid 
	inner join queue_group qg on qs.groupid=qg.objid 
where qn.groupid=$P{groupid} 
	and qn.state='PENDING' 

[removeExpiredTickets]
delete from queue_number 
where ( txndate IS NULL or txndate < $P{txndate} ) 
	and objid not in ( 
		select objid from queue_number_counter 
		where objid=queue_number.objid 
	) 

