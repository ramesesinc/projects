[getReportData]
select xxc.*, af.formtype, af.denomination, af.serieslength, 
  afi.respcenter_objid as ownerid, afi.respcenter_name as name, afi.respcenter_type as respcentertype, 
  (select count(*) from af_inventory_detail where controlid=xxc.controlid and lineno=1 and reftype='stocksale') as saled, 
  (case when afi.respcenter_type='AFO' then 0 else 1 end) as categoryindex, afi.afid 
from ( 
  select 
    (case when afi.respcenter_type='AFO' then 1 else 2 end) as respcenterlevel,  
    xxb.controlid, afi.afid as formno, afi.startstub, afi.endstub, 
    afi.startseries, afi.endseries, afi.endseries+1 as nextseries, 
    min(beginstartseries) as beginstartseries, min(beginendseries) as beginendseries, 
    min(receivedstartseries) as receivedstartseries, min(receivedendseries) as receivedendseries, 
    min(issuedstartseries) as issuedstartseries, min(issuedendseries) as issuedendseries, 
    (case 
        when min(issuedendseries) >= afi.endseries then null 
        when min(issuedendseries) > 0 then min(issuedendseries)+1 
        when min(beginstartseries) > 0 then min(beginstartseries)
        when min(receivedstartseries) > 0 then min(receivedstartseries)  
        else min(endingstartseries) 
     end) as endingstartseries, 
    (case when min(issuedendseries) >= afi.endseries then null else afi.endseries end) as endingendseries, 
    (case when min(issuedendseries) >= afi.endseries then 1 else 0 end) as consumed, 
    (case when min(issuedstartseries) > 0 then 0 else 1 end) as groupindex   
  from ( 

    select 
      afd.controlid, 
      afd.endingstartseries as beginstartseries, afd.endingendseries as beginendseries, 
      null as receivedstartseries, null as receivedendseries, 
      null as issuedstartseries, null as issuedendseries, 
      afd.endingstartseries, afd.endingendseries 
    from ( 
      select afd.controlid, max(afd.lineno) as maxlineno 
      from af_inventory afi, af_inventory_detail afd 
      where afi.respcenter_objid = $P{collectorid}  
        and afd.controlid = afi.objid 
        and afd.refdate < $P{startdate}  
      group by afd.controlid 
    )xxa, af_inventory_detail afd 
    where afd.controlid = xxa.controlid 
      and afd.lineno = xxa.maxlineno 
      and afd.endingstartseries > 0 

    union all 

    select 
      afd.controlid, 
      afd.endingstartseries as beginstartseries, afd.endingendseries as beginendseries, 
      null as receivedstartseries, null as receivedendseries, 
      null as issuedstartseries, null as issuedendseries, 
      null as endingstartseries, null as endingendseries 
    from ( 
      select afd.controlid, max(afd.lineno) as maxlineno 
      from ( 
        select b.controlid 
        from af_inventory a, af_inventory_detail b 
        where a.respcenter_objid = $P{collectorid}  
          and b.controlid = a.objid 
          and b.refdate >= $P{startdate}  
          and b.refdate <  $P{enddate}   
        group by b.controlid 
      )xxa, af_inventory_detail afd 
      where afd.controlid=xxa.controlid 
        and afd.refdate < $P{startdate}  
      group by afd.controlid 
    )xxa, af_inventory_detail afd 
    where afd.controlid=xxa.controlid 
      and afd.lineno=xxa.maxlineno 

    union all 

    select 
      afd.controlid, 
      null as beginstartseries, null as beginendseries, 
      afd.receivedstartseries, afd.receivedendseries, 
      null as issuedstartseries, null as issuedendseries, 
      null as endingstartseries, null as endingendseries 
    from ( 
      select b.controlid, min(b.lineno) as maxlineno
      from af_inventory a, af_inventory_detail b 
      where a.respcenter_objid = $P{collectorid}  
        and b.controlid = a.objid 
        and b.refdate >= $P{startdate}   
        and b.refdate <  $P{enddate}  
        and b.receivedstartseries > 0 
      group by b.controlid 
    )xxa, af_inventory_detail afd 
    where afd.controlid=xxa.controlid 
      and afd.lineno=xxa.maxlineno 

    union all 

    select 
      b.controlid, 
      null as beginstartseries, null as beginendseries, 
      null as receivedstartseries, null as receivedendseries,  
      min(b.issuedstartseries) as issuedstartseries, max(b.issuedendseries) as issuedendseries, 
      null as endingstartseries, null as endingendseries 
    from af_inventory a, af_inventory_detail b 
    where a.respcenter_objid = $P{collectorid}  
      and b.controlid = a.objid 
      and b.refdate >= $P{startdate}   
      and b.refdate <  $P{enddate}   
    group by b.controlid 

    union all 

    select 
      afd.controlid, 
      null as beginstartseries, null as beginendseries, 
      null as receivedstartseries, null as receivedendseries,  
      null as issuedstartseries, null as issuedendseries, 
      afd.endingstartseries, afd.endingendseries 
    from ( 
      select b.controlid, max(b.lineno) as maxlineno 
      from af_inventory a, af_inventory_detail b 
      where a.respcenter_objid = $P{collectorid}  
        and b.controlid = a.objid 
        and b.refdate >= $P{startdate}   
        and b.refdate <  $P{enddate}   
        and b.endingstartseries > 0 
      group by b.controlid 
    )xxa, af_inventory_detail afd 
    where afd.controlid=xxa.controlid 
      and afd.lineno=xxa.maxlineno 

  )xxb, af_inventory afi 
  where afi.objid=xxb.controlid 
  group by 
    afi.respcenter_type, xxb.controlid, afi.afid, 
    afi.startstub, afi.endstub, afi.startseries, afi.endseries 
)xxc, af, af_inventory afi  
where xxc.formno = af.objid 
    and afi.objid = xxc.controlid 
order by xxc.formno, xxc.respcenterlevel, xxc.groupindex, xxc.startseries 


[getReportDataByRef]
select * from ( 
  select 
    'A' as idx, '' as type, xx.controlid, af.formtype, afi.afid as formno, af.denomination, af.serieslength, 
    afi.respcenter_objid as ownerid, afi.respcenter_name as name, afi.respcenter_type as respcentertype, 
    (case when afi.respcenter_type='AFO' then 0 else 1 end) as categoryindex, afi.startstub, afi.endstub, 
    xx.prevendingstartseries, xx.prevendingendseries, 
    xx.receivedstartseries, xx.receivedendseries, 
    (case when xx.beginstartseries>0 and xx.prevendingstartseries>0 then xx.prevendingstartseries else xx.beginstartseries end) as beginstartseries, 
    (case when xx.beginendseries>0 and xx.prevendingendseries>0 then xx.prevendingendseries else xx.beginendseries end) as beginendseries, 
    xx.issuedstartseries, xx.issuedendseries, xx.issuednextseries, 
    (case when xx.issuednextseries>xx.endingendseries then null else xx.endingstartseries end) as endingstartseries, 
    (case when xx.issuednextseries>xx.endingendseries then null else xx.endingendseries end) as endingendseries, 
    case 
      when xx.beginstartseries > 0 then xx.beginstartseries 
      when xx.issuedstartseries > 0 then xx.issuedstartseries 
      when xx.receivedstartseries > 0 then xx.receivedstartseries 
      else xx.endingstartseries 
    end as sortseries, 
    afi.afid 
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
        max(afd.endingendseries) as endingendseries 
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
        max(afd.endingendseries) as endingendseries 
      from liquidation_remittance lr 
        inner join remittance_af raf on lr.objid = raf.remittanceid 
        inner join af_inventory_detail afd on raf.objid=afd.objid 
      where lr.liquidationid = $P{refid}  
      group by afd.controlid 
    )t1 
  )xx inner join af_inventory afi on xx.controlid=afi.objid 
      inner join af on afi.afid = af.objid 
)xx 
where xx.formno like $P{formno} 
order by xx.formno, xx.sortseries  
