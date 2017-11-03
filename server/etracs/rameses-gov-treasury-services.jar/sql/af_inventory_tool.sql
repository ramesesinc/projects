[getDetails]
select * from af_inventory_detail 
where controlid=$P{controlid} 
order by lineno  

[getDetailsByRef]
select * from af_inventory_detail 
where refid=$P{refid}  
order by controlid 

[removeDetailsByRef]
delete from af_inventory_detail 
where refid=$P{refid}  

[findLastDetailByControl]
select * 
from af_inventory_detail 
where controlid=$P{controlid} 
order by lineno desc, refdate desc 
limit 1

[findDetail]
select * from af_inventory_detail where objid=$P{objid} 

[removeDetail]
delete from af_inventory_detail where objid=$P{objid} 

[findInventory]
select * from af_inventory where objid=$P{objid} 

[syncInventory]
update af_inventory afi set 
	afi.qtyout = (afi.currentseries - afi.startseries), 
	afi.qtybalance = afi.qtyin - (afi.currentseries - afi.startseries), 
	afi.currentlineno = ( select max(lineno) from af_inventory_detail where controlid=afi.objid ), 
	afi.qtycancelled = (select sum(qtycancelled) from af_inventory_detail where controlid=afi.objid) 
where 
	afi.objid=$P{controlid} 

[syncInventoryBalance]
update af_inventory afi set 
	afi.qtyout = (afi.currentseries - afi.startseries), 
	afi.qtybalance = afi.qtyin - (afi.currentseries - afi.startseries) - afi.qtycancelled 
where 
	afi.objid=$P{controlid} 
