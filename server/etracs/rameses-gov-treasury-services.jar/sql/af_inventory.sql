[getList]
select 
   objid, afid, respcenter_objid, respcenter_name, startseries, endseries, 
   startstub, endstub, prefix, suffix, ( endseries - startseries + 1 ) as qtyin, 
   ( endseries - currentseries + 1 ) as qtybalance, qtycancelled, 
   (( endseries - startseries + 1 ) -( endseries - currentseries + 1 )) as qtyout 
from af_inventory 
where respcenter_objid like $P{respcenterobjid} 
   and afid like $P{afid}  
order by respcenter_name, afid, startseries  


[getDetails]
select xx.*, 
   case  
      when xx.indexno = 1 then 'RECEIVED'
      when xx.qtyending > 0 then 'FORWARDED' 
      when xx.qtyissued > 0 and (xx.qtybegin-xx.qtyissued)=0 then 'CONSUMED' 
      else '' 
   end as remarks 
from ( 
   select 
      xx.refid, xx.refno, xx.reftype, xx.refdate, min(xx.indexno) as indexno,  
      min( xx.beginstartseries ) as beginstartseries, min( xx.beginendseries ) as beginendseries, 
      (min( xx.beginendseries ) - min( xx.beginstartseries ) + 1) as qtybegin, 
      min( xx.receivedstartseries ) as receivedstartseries, min( xx.receivedendseries ) as receivedendseries, 
      (min( xx.receivedendseries ) - min( xx.receivedstartseries ) + 1) as qtyreceived, 
      min( xx.issuedstartseries ) as issuedstartseries, min( xx.issuedendseries ) as issuedendseries, 
      (min( xx.issuedendseries ) - min( xx.issuedstartseries ) + 1) as qtyissued, 
      max( xx.endingstartseries ) as endingstartseries, max( xx.endingendseries ) as endingendseries, 
      (max( xx.endingendseries ) - max( xx.endingstartseries ) + 1) as qtyending  
   from ( 
      SELECT  
         d.lineno as indexno, d.refid, d.refno, d.reftype, convert(d.refdate,DATE) as refdate, 
         (SELECT endingstartseries FROM af_inventory_detail WHERE controlid=d.controlid AND lineno=(d.lineno-1)) AS beginstartseries,
         (SELECT endingendseries FROM af_inventory_detail WHERE controlid=d.controlid AND lineno=(d.lineno-1)) AS beginendseries,
         (SELECT qtyending FROM af_inventory_detail WHERE controlid=d.controlid AND lineno=(d.lineno-1)) AS qtybegin,
         d.receivedstartseries, d.receivedendseries, d.qtyreceived, 
         d.issuedstartseries, d.issuedendseries, d.qtyissued, 
         d.endingstartseries, d.endingendseries, d.qtyending, 
         d.cancelledstartseries, d.cancelledendseries, d.qtycancelled 
      FROM af_inventory_detail d
      WHERE d.controlid=$P{controlid}
      order by d.lineno 
   )xx 
   group by xx.refid, xx.refno, xx.reftype, xx.refdate 
   order by min(xx.indexno) 
)xx 


[getRespCenters]
select 
   respcenter_objid as objid, 
    min(respcenter_name) as name 
from af_inventory 
group by respcenter_objid 
order by min(respcenter_name)  


[getIssuancesOnHand]
SELECT 
   afid, 
   currentseries as startseries,
   endseries as endseries,
   qtybalance
FROM af_inventory 
WHERE respcenter_objid = $P{issuetoid}
AND qtybalance > 0 


[findAllAvailable]
select 
   tmpa.categoryindex, tmpa.txndate, 
   ac.objid AS controlid, ac.afid,
   ac.currentseries as startseries,
   ac.endseries as endseries,
   ac.currentstub as startstub,
   ac.endstub as endstub, 
   ac.prefix, ac.suffix,
   ac.unit, ac.qtybalance, ac.cost 
from ( 
   select 
      0 as categoryindex, a.objid, a.dtfiled as txndate 
   from af_inventory_return a   
      inner join af_inventory b on b.objid=a.objid 
   where b.afid=$P{afid} 
      and b.respcenter_type = 'AFO' 
      and b.qtybalance > 0 
   union 
   select 
      1 as categoryindex, ai.objid, ad.txndate  
   from af_inventory ai  
      inner join af_inventory_detail ad on (ad.controlid=ai.objid and ad.lineno=1)
   where ai.afid=$P{afid} 
      and ai.respcenter_type = 'AFO' 
      and ai.qtybalance > 0 
      and ai.objid not in (select objid from af_inventory_return where objid=ai.objid) 
)tmpa, af_inventory ac  
where ac.objid=tmpa.objid 
order by tmpa.categoryindex, tmpa.txndate, ac.currentseries 

[getAFSaleDetails]
select tmp1.*, d.cost, 
   (tmp1.endseries-tmp1.startseries)+1 as qtyissued, 
   (tmp1.endstub-tmp1.startstub)+1 as qty 
from ( 
   select 
      afd.controlid, afi.afid, afi.unit, afd.refid, afd.reftype, 
      afi.startseries, afi.endseries, afi.startstub, afi.endstub, 
      af.serieslength, af.formtype as aftype, afi.prefix, afi.suffix 
   from af_inventory_detail afd 
      inner join af_inventory afi on afi.objid = afd.controlid 
      inner join af on af.objid = afi.afid 
   where afd.refid = $P{stockissueid} 
      and afd.reftype = 'stocksale' 
      and afd.txntype = 'SALE-RECEIPT' 
      and afi.afid = $P{afid} 
      and afi.unit = $P{unit} 
)tmp1 
   left join af_inventory_detail d on d.refid=tmp1.refid 
   left join af_inventory a on a.objid = d.controlid 
where d.reftype = tmp1.reftype 
   and d.txntype = 'SALE' 
   and a.afid = tmp1.afid 
   and a.unit = tmp1.unit 
   and d.issuedstartseries = tmp1.startseries 
   and d.issuedendseries = tmp1.endseries 

[getAFDetails]
select 
   min(afi.startseries) as startseries, max(afi.endseries) as endseries, 
   min(afi.startstub) as startstub, max(afi.endstub) as endstub,
   (max(afi.endseries) - min(afi.startseries)) + 1 as qtyissued, 
   (max(afi.endstub) - min(afi.startstub)) + 1 as qty,
   af.serieslength, af.formtype as aftype, afi.prefix, afi.suffix, 
   (
      select sum(xafid.cost) from af_inventory_detail xafid
         inner join af_inventory xafi on xafid.controlid = xafi.objid 
      where xafid.refid = $P{stockissueid} and xafi.afid = $P{afid}  
         and xafi.unit = $P{unit} and xafi.respcenter_type='AFO' 
   ) as cost 
from af_inventory_detail afid 
   inner join af_inventory afi on afid.controlid=afi.objid 
   inner join af on afi.afid = af.objid 
where afid.refid = $P{stockissueid} 
   and afi.afid = $P{afid} and afi.unit = $P{unit} 
   and afi.respcenter_type = $P{respcentertype} 
group by af.serieslength, af.formtype, afi.prefix, afi.suffix 


[findOverlappingAF]
select afi.*  
from af_inventory afi, ( 
   select 
      $P{afid} as afid, 
      $P{unit} as unit, 
      $P{startseries} as startseries 
)xx  
where afi.afid=xx.afid and afi.unit=xx.unit 
   and xx.startseries between afi.startseries and afi.endseries 
   and afi.respcenter_type = 'COLLECTOR' 


[getIssuanceReceipts]
select 
   afi.objid, afi.afid, afi.startstub as stubno, 
   afi.startseries, afi.endseries, afi.qtyin as qty 
from af_inventory_detail afd 
   inner join af_inventory afi on afd.controlid=afi.objid 
where afd.refid=$P{refid} 
   and afd.txntype='ISSUANCE-RECEIPT' 
   and afi.afid = $P{afid} 
order by afi.startseries 

[findFirstDetail]
select * from af_inventory_detail 
where controlid=$P{controlid} and lineno=1 
