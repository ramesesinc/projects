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

[findDetail]
select * from af_inventory_detail where objid=$P{objid} 

[removeDetail]
delete from af_inventory_detail where objid=$P{objid} 

[findInventory]
select * from af_inventory where objid=$P{objid} 

[syncInventorySeries]
update afi set 
	afi.currentseries = x.minseries 
from af_inventory afi, ( 
	select 
		cr.controlid,  
		min(cr.series) as minseries,  
		max(cr.series) as maxseries 
	from cashreceipt cr 
	where cr.controlid=$P{controlid} 
		and cr.objid not in (select objid from remittance_cashreceipt where objid=cr.objid) 
		and cr.state='POSTED' 
	group by cr.controlid 
) x 
where x.controlid=afi.objid 

[syncInventoryBalances]
update afi set 
	afi.qtyout = (afi.currentseries - afi.startseries), 
	afi.qtybalance = afi.qtyin - (afi.currentseries - afi.startseries) 
from af_inventory afi 
where afi.objid=$P{controlid} 

[syncInventoryLineNo]
update afi set 
	afi.currentlineno = x.maxlineno 
from af_inventory afi, ( 
		select max([lineno]) as maxlineno 
		from af_inventory_detail 
		where controlid=$P{controlid} 
 	)x  
where afi.objid=$P{controlid} 

[logDetailAsForward]
insert into af_inventory_detail ( 
	objid, controlid, [lineno], refid, refno, reftype, refdate, 
	txndate, txntype, beginstartseries, beginendseries, endingstartseries, endingendseries, 
	qtyreceived, qtybegin, qtyissued, qtyending, qtycancelled, remarks 
) 
select 
	$P{objid}, afi.objid, afi.currentlineno+1, 'FORWARD', 'FORWARD', 'FORWARD', $P{txndate}, 
	$P{txndate}, 'FORWARD', afi.currentseries, afi.endseries, afi.currentseries, afi.endseries, 
	0, (afi.endseries-afi.currentseries)+1, 0, (afi.endseries-afi.currentseries)+1, 0, 'FORWARDED DUE TO REVERT REMITTANCE' 
from af_inventory afi 
where afi.objid=$P{controlid}
