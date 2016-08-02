[getList]
select a.* 
from ( 
	select objid as reqid from stockrequest 
	where reqno like $P{reqno} 
	union 
	select objid as reqid from stockrequest 
	where requester_name like $P{requester} 
)xx, stockrequest a 
where xx.reqid=a.objid 
	and a.itemclass='AF' 
order by a.dtfiled desc 


[getLookup]
select a.* 
from ( 
	select objid as reqid from stockrequest 
	where reqno like $P{reqno} 
		and reqtype=$P{reqtype} 
	union 
	select objid as reqid from stockrequest 
	where requester_name like $P{requester} 
		and reqtype=$P{reqtype} 
)xx, stockrequest a 
where xx.reqid=a.objid 
	and a.itemclass='AF' 
	and a.state='OPEN' 
order by a.dtfiled desc 
