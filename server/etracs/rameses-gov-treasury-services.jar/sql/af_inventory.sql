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
SELECT  
   MIN( d.refdate) AS refdate, MIN(d.refno) AS refno, MIN( d.reftype) AS reftype,
   MIN( d.receivedstartseries ) AS receivedstartseries, 
   MAX( d.receivedendseries ) AS receivedendseries, 
   MIN( d.qtyreceived) AS qtyreceived,
   (SELECT endingstartseries FROM af_inventory_detail 
      WHERE controlid = d.controlid 
         AND lineno = (MIN(d.lineno) - 1) ) AS beginstartseries,
   (SELECT endingendseries FROM af_inventory_detail 
      WHERE controlid = d.controlid 
         AND lineno = (MIN(d.lineno) - 1) ) AS beginendseries,
   (SELECT qtyending FROM af_inventory_detail 
      WHERE controlid = d.controlid 
         AND lineno = (MIN(d.lineno) - 1) ) AS qtybegin,
   MAX( d.issuedstartseries ) AS issuedstartseries,
   MAX( d.issuedendseries ) AS issuedendseries,
   MAX( d.qtyissued) AS qtyissued, 
   MAX( d.endingstartseries ) AS endingstartseries,
   MAX( d.endingendseries ) AS endingendseries,    
   MAX( d.qtyending) AS qtyending, 
   MAX( d.cancelledstartseries ) AS cancelledstartseries,
   MAX( d.cancelledendseries ) AS cancelledendseries, 
   MAX( d.qtycancelled) AS qtycancelled, 
   MIN( d.remarks ) AS remarks
FROM af_inventory_detail d
WHERE controlid=$P{controlid}
GROUP BY refid
ORDER BY MIN(txndate)

[getRespCenters]
select distinct 
   respcenter_objid as objid, respcenter_name as name 
from af_inventory 
order by respcenter_name 

[getIssuancesOnHand]
SELECT 
   afid, 
   currentseries as startseries,
   endseries as endseries,
   qtybalance
FROM 
   af_inventory 
WHERE respcenter_objid = $P{issuetoid}
AND qtybalance > 0

[findAllAvailable]
SELECT 
   ac.objid AS controlid,  
   ac.afid,
   ac.currentseries as startseries,
   ac.endseries as endseries,
   ac.currentstub as startstub,
   ac.endstub as endstub,
   ac.prefix,
   ac.suffix,
   ac.unit,
   ac.qtybalance,
   ac.cost 
FROM af_inventory ac
   inner join af_inventory_detail ad on ad.controlid = ac.objid and ad.lineno=1
WHERE ac.afid=$P{afid} 
   AND ac.respcenter_type = 'AFO' 
   AND ac.qtybalance > 0
ORDER BY ad.txndate, ac.currentseries


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
select * from af_inventory 
where afid=$P{afid} and unit=$P{unit} 
   and startseries <= $P{startseries} 
   and endseries >= $P{startseries} 

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
