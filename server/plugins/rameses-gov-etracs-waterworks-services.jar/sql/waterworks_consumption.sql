[findLastConsumption]
select c.*, bs.year, bs.month, 
	case 
		when bb.readingdate is null then bs.readingdate else bb.readingdate 
	end as readingdate  
from waterworks_consumption c 
	inner join waterworks_billing_schedule bs on bs.objid = c.scheduleid 
	left join waterworks_batch_billing bb on bb.objid = c.batchid 
where c.acctid = $P{acctid} ${filter} 
order by bs.year desc, bs.month desc 


[getBatchConsumption]
select c.*, bs.year, bs.month, 
	case 
		when bb.readingdate is null then bs.readingdate else bb.readingdate 
	end as readingdate  
from waterworks_consumption c 
	inner join waterworks_billing_schedule bs on bs.objid = c.scheduleid 
	left join waterworks_batch_billing bb on bb.objid = c.batchid 
where c.batchid = $P{batchid} 
