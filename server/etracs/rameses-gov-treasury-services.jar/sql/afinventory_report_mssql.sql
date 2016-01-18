[getReport]
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
          min([lineno]) as minlineno, max([lineno]) as maxlineno 
        from ( select objid from af_inventory where respcenter_type='AFO' )xai 
          inner join af_inventory_detail afd on xai.objid=afd.controlid  
        where year(afd.refdate)=$P{year} and month(afd.refdate)=$P{month} 
        group by controlid 
      )xx 
        inner join af_inventory_detail afd on xx.controlid=afd.controlid 
      where afd.[lineno] < xx.minlineno 
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
