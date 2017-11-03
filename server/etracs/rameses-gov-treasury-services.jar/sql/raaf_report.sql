[getReportData]
select tmp5.*, 
  case 
    when tmp5.saled > 0 then 'SALE' 
    when tmp5.consumed > 0 then 'CONSUMED' 
  end as remarks  
from ( 
  select tmp4.*, 
    (case when tmp4.receivedendseries-tmp4.receivedstartseries+1 > 0 then tmp4.receivedendseries-tmp4.receivedstartseries+1 else 0 end) as qtyreceived, 
    (case when tmp4.beginendseries-tmp4.beginstartseries+1 > 0 then tmp4.beginendseries-tmp4.beginstartseries+1 else 0 end) as qtybegin, 
    (case when tmp4.endingendseries-tmp4.endingstartseries+1 > 0 then tmp4.endingendseries-tmp4.endingstartseries+1 else 0 end) as qtyending, 
    (case when tmp4.issuedendseries >= tmp4.endseries then 1 else 0 end) as consumed, 
    (case when afi.respcenter_type='AFO' then 1 else 2 end) as respcenterlevel, 
    (case when tmp4.qtyissued > 0 then 0 else 1 end) as categoryindex, 
    afi.respcenter_type as respcentertype, afi.respcenter_name as name, afi.respcenter_objid as ownerid, 
    afi.afid as formno, af.formtype, af.denomination, af.serieslength 
  from ( 
    select 
      controlid, afid, startstub, endstub, startseries, endseries, endseries+1 as nextseries,  
      min(receivedstartseries) as receivedstartseries, min(receivedendseries) as receivedendseries, 
      min(beginstartseries) as beginstartseries, min(beginendseries) as beginendseries, 
      min(issuedstartseries) as issuedstartseries, max(issuedendseries) as issuedendseries, 
      case 
        when max(issuedendseries) >= endseries then null 
        when max(issuedendseries) is not null then max(issuedendseries)+1 
        when min(beginstartseries) is not null then min(beginstartseries) 
        else min(receivedstartseries) 
      end as endingstartseries, 
      case 
        when max(issuedendseries) >= endseries then null 
        when max(issuedendseries) is not null then endseries 
        when min(beginstartseries) is not null then min(beginendseries) 
        else min(receivedendseries) 
      end as endingendseries,
      sum(qtyissued) as qtyissued, sum(saled) as saled  
    from ( 
      select 
        controlid, afid, startstub, endstub, startseries, endseries, 
        receivedstartseries, receivedendseries, 
        (case when receivedstartseries is not null then null else beginstartseries end) as beginstartseries, 
        (case when receivedstartseries is not null then null else beginendseries end) as beginendseries, 
        issuedstartseries, issuedendseries, qtyissued, saled   
      from ( 

        select 
          afd.refdate, afd.controlid, afi.afid, afi.startstub, afi.endstub, afi.startseries, afi.endseries, 
          null as receivedstartseries, null as receivedendseries, 
          afd.endingstartseries as beginstartseries, afd.endingendseries as beginendseries, 
          null as issuedstartseries, null as issuedendseries, 0 as qtyissued, 0 as saled   
        from ( 
          select afd.controlid, max(afd.lineno) as xlineno  
          from af_inventory_detail afd 
            inner join af_inventory afi on afi.objid = afd.controlid 
          where afd.refdate < $P{startdate} 
            and afi.respcenter_objid = $P{collectorid} 
          group by afd.controlid 
        )tmp1 
          inner join af_inventory_detail afd on (afd.controlid = tmp1.controlid and afd.lineno = tmp1.xlineno) 
          inner join af_inventory afi on afi.objid = afd.controlid 
        where afd.qtyending > 0 
          and afd.endingstartseries <= afd.endingendseries 

        union all 

        select 
          afd.refdate, afd.controlid, afi.afid, afi.startstub, afi.endstub, afi.startseries, afi.endseries, 
          (case when afd.txntype='COLLECTOR BEG.BAL.' then null else tmp1.receivedstartseries end) as receivedstartseries, 
          (case when afd.txntype='COLLECTOR BEG.BAL.' then null else tmp1.receivedendseries end) as receivedendseries, 
          tmp1.beginstartseries, tmp1.beginendseries, tmp1.issuedstartseries, tmp1.issuedendseries, tmp1.qtyissued, 
          (case when afd.reftype='stocksale' then 1 else 0 end) as saled 
        from ( 
          select 
            afd.controlid, min(afd.lineno) as xlineno, 
            min(afd.receivedstartseries) as receivedstartseries, min(afd.receivedendseries) as receivedendseries, 
            min(afd.beginstartseries) as beginstartseries, min(afd.beginendseries) as beginendseries, 
            min(afd.issuedstartseries) as issuedstartseries, max(afd.issuedendseries) as issuedendseries, 
            sum(afd.qtyissued) as qtyissued 
          from af_inventory_detail afd 
          where afd.refdate >= $P{startdate} 
            and afd.refdate <  $P{enddate} 
            and afd.reftype <> 'TRANSFER' 
          group by afd.controlid 
        )tmp1 
          inner join af_inventory_detail afd on (afd.controlid = tmp1.controlid and afd.lineno = tmp1.xlineno) 
          inner join af_inventory afi on afi.objid = afd.controlid 
        where afi.respcenter_objid = $P{collectorid} 

      )tmp2 
    )tmp3 
    group by controlid, afid, startstub, endstub, startseries, endseries 
  )tmp4 
    inner join af_inventory afi on afi.objid = tmp4.controlid 
    inner join af on af.objid = afi.afid 
)tmp5 
order by tmp5.afid, tmp5.respcenterlevel, tmp5.categoryindex, tmp5.startseries 


[getReportDataByRef]
select * from ( 
  select 
    'A' as idx, '' as type, t2.controlid, af.formtype, afi.afid as formno, af.denomination, af.serieslength, 
    afi.respcenter_objid as ownerid, afi.respcenter_name as name, afi.respcenter_type as respcentertype, 
    (case when afi.respcenter_type='AFO' then 0 else 1 end) as categoryindex, afi.startstub, afi.endstub, 
    t2.prevendingstartseries, t2.prevendingendseries, 
    t2.receivedstartseries, t2.receivedendseries, 
    (case when t2.beginstartseries>0 and t2.prevendingstartseries>0 then t2.prevendingstartseries else t2.beginstartseries end) as beginstartseries, 
    (case when t2.beginendseries>0 and t2.prevendingendseries>0 then t2.prevendingendseries else t2.beginendseries end) as beginendseries, 
    t2.issuedstartseries, t2.issuedendseries, t2.issuednextseries, 
    (case when t2.issuednextseries>t2.endingendseries then null else t2.endingstartseries end) as endingstartseries, 
    (case when t2.issuednextseries>t2.endingendseries then null else t2.endingendseries end) as endingendseries, 
    case 
      when t2.beginstartseries > 0 then t2.beginstartseries 
      when t2.issuedstartseries > 0 then t2.issuedstartseries 
      when t2.receivedstartseries > 0 then t2.receivedstartseries 
      else t2.endingstartseries 
    end as sortseries, 
    afi.afid, t2.qtycancelled 
  from ( 
    select t1.*, 
      (
        select max(endingstartseries) from af_inventory_detail 
        where controlid=t1.controlid and lineno < t1.minlineno 
      ) as prevendingstartseries, 
      (
        select max(endingendseries) from af_inventory_detail 
        where controlid=t1.controlid and lineno < t1.minlineno 
      ) as prevendingendseries  
    from ( 
      select 
        afd.controlid, min(afd.lineno) as minlineno, max(afd.lineno) as maxlineno,   
        min(afd.receivedstartseries) as receivedstartseries, 
        max(afd.receivedendseries) as receivedendseries, 
        min(afd.beginstartseries) as beginstartseries, 
        max(afd.beginendseries) as beginendseries, 
        min(afd.issuedstartseries) as issuedstartseries, 
        max(afd.issuedendseries) as issuedendseries, 
        max(afd.issuedendseries)+1 as issuednextseries, 
        max(afd.endingstartseries) as endingstartseries, 
        max(afd.endingendseries) as endingendseries, 
        sum(afd.qtycancelled) as qtycancelled  
      from remittance_af raf 
        inner join af_inventory_detail afd on raf.objid=afd.objid 
      where raf.remittanceid = $P{refid} 
      group by afd.controlid 
      union 
      select 
        afd.controlid, min(afd.lineno) as minlineno, max(afd.lineno) as maxlineno,   
        min(afd.receivedstartseries) as receivedstartseries, 
        max(afd.receivedendseries) as receivedendseries, 
        min(afd.beginstartseries) as beginstartseries, 
        max(afd.beginendseries) as beginendseries, 
        min(afd.issuedstartseries) as issuedstartseries, 
        max(afd.issuedendseries) as issuedendseries, 
        max(afd.issuedendseries)+1 as issuednextseries, 
        max(afd.endingstartseries) as endingstartseries, 
        max(afd.endingendseries) as endingendseries, 
        sum(afd.qtycancelled) as qtycancelled 
      from liquidation_remittance lr 
        inner join remittance_af raf on lr.objid = raf.remittanceid 
        inner join af_inventory_detail afd on raf.objid=afd.objid 
      where lr.liquidationid = $P{refid}     
      group by afd.controlid 
    )t1  
  )t2 
    inner join af_inventory afi on t2.controlid=afi.objid 
    inner join af on afi.afid = af.objid 
)t3 
where t3.formno like $P{formno}   
order by t3.formno, t3.sortseries 
