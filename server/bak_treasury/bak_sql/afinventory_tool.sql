[findAFInventory]
select 
   afi.*, af.formtype, af.denomination   
from af_inventory afi 
   inner join af on afi.afid=af.objid 
where afi.objid = $P{objid}  


[updateAFInventory]
update af_inventory set 
   currentseries = $P{currentseries}, 
   qtybalance = $P{qtybalance}, 
   qtyout = $P{qtyout} 
where 
   objid = $P{objid} 


[updateAFControl]
update af_control set 
   currentseries = $P{currentseries} 
where 
   objid = $P{objid} 


[findAFSerial]
select 
   objid, startseries, endseries, 
   (
      select count(objid) from cashreceipt 
      where controlid = afc.objid  
   ) as qtyissued, 
   (
      select count(cr.objid) from cashreceipt cr 
         inner join remittance_cashreceipt remc on cr.objid=remc.objid 
      where cr.controlid = afc.objid 
   ) as qtyremitted 
from af_control afc 
where objid = $P{objid} 


[findCashticket]
select 
   afc.objid, afc.startseries, afc.endseries, af.denomination,  
   (
      select sum(cr.amount) from cashreceipt cr 
      where cr.controlid=afc.objid 
         and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
   ) as amtissued, 
   (
      select sum(cr.amount) from cashreceipt cr 
         inner join remittance_cashreceipt remc on cr.objid=remc.objid 
      where cr.controlid=afc.objid 
         and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
   ) as amtremitted 
from af_control afc 
   inner join af on afc.afid = af.objid 
where afc.objid = $P{objid} 


[getUnremittedAFs]
select distinct xx.controlid as objid, xx.formno 
from ( 
   select formno, controlid from cashreceipt cr 
   where collector_objid = $P{collectorid}  
      and objid not in (select objid from remittance_cashreceipt where objid=cr.objid) 
   union 
   select formno, controlid from cashreceipt cr 
   where subcollector_objid = $P{collectorid} 
      and objid not in (select objid from remittance_cashreceipt where objid=cr.objid) 
)xx 
order by xx.formno, xx.controlid 
