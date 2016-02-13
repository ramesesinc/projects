[getList]
select * from ( 
	select distinct cg.* 
	from collectiongroup cg 
		inner join collectiongroup_revenueitem cgr on cg.objid=cgr.collectiongroupid 
		inner join itemaccount ia on cgr.revenueitemid=ia.objid 
	where cg.name like $P{searchtext} ${filter} 
)a 
order by a.name 


[deleteRevenueItems]
delete from collectiongroup_revenueitem where collectiongroupid=$P{objid} 

[getRevenueItems]
select c.*, r.* from collectiongroup_revenueitem c 
	inner join itemaccount r on r.objid = c.revenueitemid 
where c.collectiongroupid = $P{objid} 
order by c.orderno 

[approve]
update collectiongroup set state='APPROVED' where objid=$P{objid} 

