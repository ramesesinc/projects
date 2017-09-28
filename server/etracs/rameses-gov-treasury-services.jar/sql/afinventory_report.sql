[getReport]
select 
  controlid, receivedstartseries, receivedendseries, beginstartseries, beginendseries, 
  issuedstartseries, issuedendseries, endingstartseries, endingendseries, 
  (case when qtyreceived > 0 then qtyreceived/afqty else 0 end) as qtyreceived, 
  (case when qtybegin > 0 then qtybegin/afqty else 0 end) as qtybegin, 
  (case when qtyissued > 0 then qtyissued/afqty else 0 end) as qtyissued, 
  (case when qtyending > 0 then qtyending/afqty else 0 end) as qtyending, 
  formtype, afid, startseries, endseries, unit, denomination, serieslength, 
  costperstub, afqty, afindex, groupindex, gaincost, 
  (case when qtybegin > 0 then (qtybegin/afqty)*costperstub else 0.0 end) as qtybegincost,  
  (case when qtyissued > 0 then (qtyissued/afqty)*costperstub else 0.0 end) as qtyissuedcost, 
  (case when qtyending > 0 then (qtyending/afqty)*costperstub else 0.0 end) as qtyendingcost 
from ( 
  select tmp2.*, 
    case when (receivedendseries-receivedstartseries)+1 is null then 0 else (receivedendseries-receivedstartseries)+1 end as qtyreceived, 
    case when (beginendseries-beginstartseries)+1 is null then 0 else (beginendseries-beginstartseries)+1 end as qtybegin,
    case when (endingendseries-endingstartseries)+1 is null then 0 else (endingendseries-endingstartseries)+1 end as qtyending, 
    0 as qtycancelled, a.afid, a.unit, (case when a.cost is null then 0 else a.cost end) as costperstub, 
    a.startseries, a.endseries, af.formtype, af.denomination, af.serieslength, siu.qty as afqty, 
    (case when af.formtype='serial' then 0 else 1 end) as afindex, 
    (case when tmp2.qtyissued > 0 then 0 else 1 end) as groupindex  
  from ( 
    select controlid, 
      min(receivedstartseries) as receivedstartseries, min(receivedendseries) as receivedendseries, 
      min(beginstartseries) as beginstartseries, min(beginendseries) as beginendseries, 
      min(issuedstartseries) as issuedstartseries, max(issuedendseries) as issuedendseries, 
      case 
        when max(issuedendseries) >= endseries then null 
        when max(issuedendseries) < endseries then max(issuedendseries)+1 
        when min(beginstartseries) is not null then min(beginstartseries) 
        else min(receivedstartseries) 
      end as endingstartseries, 
      case 
        when max(issuedendseries) >= endseries then null 
        when max(issuedendseries) < endseries then endseries 
        when min(beginendseries) is not null then min(beginendseries) 
        else min(receivedendseries) 
      end as endingendseries, 
      sum(qtyissued) as qtyissued, sum(salecost)-sum(purchasecost) as gaincost 
    from ( 

      select d.controlid, tmp1.endseries, 
        null as receivedstartseries, null as receivedendseries, 
        d.endingstartseries as beginstartseries, d.endingendseries as beginendseries, 
        null as issuedstartseries, null as issuedendseries, 
        0 as qtyissued, 0.0 as issuedcost, 0.0 as salecost, 0.0 as purchasecost     
      from ( 
        select d.controlid, a.endseries, max(d.lineno) as maxlineno  
        from af_inventory_detail d 
          inner join af_inventory a on a.objid = d.controlid 
        where a.respcenter_type = 'AFO' 
          and d.refdate < $P{startdate} 
        group by d.controlid, a.endseries  
      )tmp1, af_inventory_detail d 
      where d.controlid = tmp1.controlid 
        and d.lineno = tmp1.maxlineno 
        and d.qtyending > 0 

      union all 

      select d.controlid, a.endseries, 
        min(d.receivedstartseries) as receivedstartseries, min(d.receivedendseries) as receivedendseries, 
        case when min(d.receivedstartseries) is not null then null else min(d.beginstartseries) end as beginstartseries, 
        case when min(d.receivedstartseries) is not null then null else min(d.beginendseries) end as beginendseries, 
        null as issuedstartseries, null as issuedendseries, 0 as qtyissued, 0.0 as issuedcost, 0.0 as salecost, 0.0 as purchasecost    
      from af_inventory_detail d 
        inner join af_inventory a on a.objid = d.controlid 
      where a.respcenter_type = 'AFO' 
        and d.refdate >= $P{startdate} 
        and d.refdate <  $P{enddate} 
        and d.qtyissued = 0 
      group by d.controlid, a.endseries  

      union all 

      select d.controlid, a.endseries, 
        null as receivedstartseries, null as receivedendseries, 
        min(d.issuedstartseries) as beginstartseries, a.endseries as beginendseries, 
        min(d.issuedstartseries) as issuedstartseries, max(d.issuedendseries) as issuedendseries, 
        sum(d.qtyissued) as qtyissued, sum(d.cost) as issuedcost, 0.0 as salecost, 0.0 as purchasecost  
      from af_inventory_detail d 
        inner join af_inventory a on a.objid = d.controlid 
      where a.respcenter_type = 'AFO' 
        and d.refdate >= $P{startdate} 
        and d.refdate <  $P{enddate} 
        and d.qtyissued > 0 
      group by d.controlid, a.endseries 

      union all 

      select d.controlid, a.endseries, 
        null as receivedstartseries, null as receivedendseries, 
        null as beginstartseries, null as beginendseries, 
        null as issuedstartseries, null as issuedendseries, 
        0 as qtyissued, 0.0 as issuedcost, sum(d.cost) as salecost, 
        (sum(d.qtyissued)/siu.qty)*a.cost as purchasecost 
      from af_inventory_detail d 
        inner join af_inventory a on a.objid = d.controlid 
        inner join af on af.objid = a.afid 
        inner join stockitem_unit siu on (siu.itemid = af.objid and siu.unit = a.unit) 
      where a.respcenter_type = 'AFO' 
        and d.refdate >= $P{startdate} 
        and d.refdate <  $P{enddate} 
        and d.reftype = 'stocksale' 
        and d.txntype = 'SALE' 
        and d.qtyissued > 0 
      group by d.controlid, a.endseries, siu.qty, a.cost 

    )tmp1 
    group by controlid, endseries 
  )tmp2 
    inner join af_inventory a on a.objid = tmp2.controlid 
    inner join af on af.objid = a.afid 
    inner join stockitem_unit siu on (siu.itemid = af.objid and siu.unit = a.unit) 
)tmp3 
order by afindex, afid, startseries 


[getReport_bak1]
select xx.* from ( 
  select 
    aaa.objid, aaa.afid, aaa.cost as costperstub, af.formtype, 
    af.denomination, af.serieslength, siu.qty as qtyunit, saleinfo.gaincost, 
    (prev.qtybalance/siu.qty) as qtybegin, (prev.qtybalance/siu.qty)*aaa.cost as qtybegincost, 
    (case when prev.qtybalance>0 then prev.endingstartseries end) as beginstartseries, 
    (case when prev.qtybalance>0 then prev.endingendseries end) as beginendseries, 
    (curr.qtyreceived/siu.qty) as qtyreceived, (curr.qtyreceived/siu.qty)*aaa.cost as qtyreceivedcost,  
    curr.receivedstartseries, curr.receivedendseries, 
    (curr.qtyissued/siu.qty) as qtyissued, (curr.qtyissued/siu.qty)*aaa.cost as qtyissuedcost,  
    curr.issuedstartseries, curr.issuedendseries, aaa.endseries, 
    (case when af.formtype='serial' then 0 else 1 end) as afindex 
  from ( 
    select objid, afid, cost, unit, endseries 
    from af_inventory where respcenter_type='AFO' 
  ) aaa 
    inner join af on aaa.afid=af.objid 
    inner join stockitem_unit siu on (af.objid=siu.itemid and aaa.unit=siu.unit) 
    left join ( 
      select 
        xx.controlid, 
        max(afd.issuedendseries) as issuedendseries, 
        max(afd.endingstartseries) as endingstartseries, 
        max(afd.endingendseries) as endingendseries, 
        case 
          when max(afd.issuedendseries)>0 then max(afd.endingendseries)-max(afd.issuedendseries) 
          else max(afd.endingendseries)-max(afd.endingstartseries)+1  
        end as qtybalance 
      from ( 
        select 
          controlid, min(refdate) as minrefdate, 
          min(lineno) as minlineno, max(lineno) as maxlineno 
        from ( select objid from af_inventory where respcenter_type='AFO' )xai 
          inner join af_inventory_detail afd on xai.objid=afd.controlid  
        where year(afd.refdate)=$P{year} and month(afd.refdate)=$P{month} 
        group by controlid 
      )xx 
        inner join af_inventory_detail afd on xx.controlid=afd.controlid 
      where afd.lineno < xx.minlineno 
      group by xx.controlid  
    ) prev on aaa.objid=prev.controlid 

    left join ( 
      select 
        controlid, 
        max(receivedendseries)-min(receivedstartseries)+1 as qtyreceived, 
        min(receivedstartseries) as receivedstartseries, max(receivedendseries) as receivedendseries, 
        max(issuedendseries)-min(issuedstartseries)+1 as qtyissued, 
        min(issuedstartseries) as issuedstartseries, max(issuedendseries) as issuedendseries 
      from ( select objid from af_inventory where respcenter_type='AFO' )xai 
        inner join af_inventory_detail afd on xai.objid=afd.controlid  
      where year(afd.refdate)=$P{year} and month(afd.refdate)=$P{month} 
      group by afd.controlid 
    ) curr on aaa.objid=curr.controlid 

    left join (
      select 
        xx.controlid, (sum(salecost)-sum(purchasecost)) as gaincost 
      from ( 
        select 
          afd.controlid, afi.cost, siu.qty as qtyunit, 
          afd.issuedstartseries, afd.issuedendseries, afd.cost as salecost, 
          ((afd.issuedendseries-afd.issuedstartseries+1)/siu.qty)*afi.cost as purchasecost  
        from af_inventory_detail afd 
          inner join af_inventory afi on afd.controlid=afi.objid 
          inner join af on afi.afid=af.objid 
          inner join stockitem_unit siu on (af.objid=siu.itemid and afi.unit=siu.unit) 
        where year(afd.refdate)=$P{year} and month(afd.refdate)=$P{month}  
          and afd.reftype='stocksale' and txntype='SALE' 
      )xx 
      group by controlid 
    ) saleinfo on aaa.objid=saleinfo.controlid 
  where (prev.controlid is not null or curr.controlid is not null)  
)xx 
order by afindex, serieslength, denomination, afid  

