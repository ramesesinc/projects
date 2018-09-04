[getUsers]
select distinct 
	u.objid, 
	u.name
from txnlog l
inner join sys_user u on l.userid = u.objid 
order by u.name 

[getRefTypes]
select distinct 
	ref
from txnlog 
order by ref 


[getList]
select 
	x.username,
	x.ref,
	x.action, 
	sum(x.cnt) as cnt 
from (
	select 
		y.username,
		y.ref,
		y.action,
		sum(y.cnt) as cnt 
	from (
		select
			distinct 
			objid,
			username, 
			ref,
			action,	
			1 as cnt 
		from txnlog
		where txndate >= $P{startdate} and txndate < $P{enddate}
		${filter}
	) y 
	group by y.username, y.ref, y.action 
) x 
group by x.username, x.ref, x.action
order by x.username, x.ref, x.action