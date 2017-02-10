[getCraafData]
select xxc.*, af.formtype, af.denomination, af.serieslength, 
  afi.respcenter_objid as ownerid, afi.respcenter_name as name, afi.respcenter_type as respcentertype, 
  (select count(*) from af_inventory_detail where controlid=xxc.controlid and [lineno]=1 and reftype='stocksale') as saled, 
  (case when afi.respcenter_type='AFO' then 0 else 1 end) as categoryindex, afi.afid, af.formtype as aftype, xxc.startseries as sortseries  
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
    (case when min(issuedendseries) >= afi.endseries then 1 else 0 end) as consumed 
  from ( 

    select 
      afd.controlid, 
      afd.endingstartseries as beginstartseries, afd.endingendseries as beginendseries, 
      null as receivedstartseries, null as receivedendseries, 
      null as issuedstartseries, null as issuedendseries, 
      afd.endingstartseries, afd.endingendseries 
    from ( 
      select afd.controlid, max(afd.[lineno]) as maxlineno 
      from af_inventory afi, af_inventory_detail afd 
      where afd.controlid = afi.objid 
        and afd.refdate < $P{startdate}    
      group by afd.controlid 
    )xxa, af_inventory_detail afd 
    where afd.controlid = xxa.controlid 
      and afd.[lineno] = xxa.maxlineno 
      and afd.endingstartseries > 0 

    union all 

    select 
      afd.controlid, 
      afd.endingstartseries as beginstartseries, afd.endingendseries as beginendseries, 
      null as receivedstartseries, null as receivedendseries, 
      null as issuedstartseries, null as issuedendseries, 
      null as endingstartseries, null as endingendseries 
    from ( 
      select afd.controlid, max(afd.[lineno]) as maxlineno 
      from ( 
        select b.controlid 
        from af_inventory a, af_inventory_detail b 
        where b.controlid = a.objid 
          and b.refdate >= $P{startdate}  
          and b.refdate <  $P{enddate}   
        group by b.controlid 
      )xxa, af_inventory_detail afd 
      where afd.controlid=xxa.controlid 
        and afd.refdate < $P{startdate} 
      group by afd.controlid 
    )xxa, af_inventory_detail afd 
    where afd.controlid=xxa.controlid 
      and afd.[lineno]=xxa.maxlineno 

    union all 

    select 
      afd.controlid, 
      null as beginstartseries, null as beginendseries, 
      afd.receivedstartseries, afd.receivedendseries, 
      null as issuedstartseries, null as issuedendseries, 
      null as endingstartseries, null as endingendseries 
    from ( 
      select b.controlid, min(b.[lineno]) as maxlineno
      from af_inventory a, af_inventory_detail b 
      where b.controlid = a.objid 
        and b.refdate >= $P{startdate}    
        and b.refdate <  $P{enddate}  
        and b.receivedstartseries > 0 
      group by b.controlid 
    )xxa, af_inventory_detail afd 
    where afd.controlid=xxa.controlid 
      and afd.[lineno]=xxa.maxlineno 

    union all 

    select 
      b.controlid, 
      null as beginstartseries, null as beginendseries, 
      null as receivedstartseries, null as receivedendseries,  
      min(b.issuedstartseries) as issuedstartseries, max(b.issuedendseries) as issuedendseries, 
      null as endingstartseries, null as endingendseries 
    from af_inventory a, af_inventory_detail b 
    where b.controlid = a.objid 
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
      select b.controlid, max(b.[lineno]) as maxlineno 
      from af_inventory a, af_inventory_detail b 
      where b.controlid = a.objid 
        and b.refdate >= $P{startdate}    
        and b.refdate <  $P{enddate}    
        and b.endingstartseries > 0 
      group by b.controlid 
    )xxa, af_inventory_detail afd 
    where afd.controlid=xxa.controlid 
      and afd.[lineno]=xxa.maxlineno 

  )xxb, af_inventory afi 
  where afi.objid=xxb.controlid 
  group by 
    afi.respcenter_type, xxb.controlid, afi.afid, 
    afi.startstub, afi.endstub, afi.startseries, afi.endseries 
)xxc, af, af_inventory afi  
where xxc.formno = af.objid 
    and afi.objid = xxc.controlid 
order by xxc.formno, xxc.respcenterlevel, xxc.startseries 


[getCraafData_bak1]
select * from ( 
  select 
    'A' as idx, '' as type, xx.controlid, af.formtype as aftype, afi.afid, af.denomination, af.serieslength, 
    afi.respcenter_objid as ownerid, afi.respcenter_name as name, afi.respcenter_type as respcentertype, 
    (case when afi.respcenter_type='AFO' then 0 else 1 end) as categoryindex, afi.startstub, afi.endstub, 
    xx.prevendingstartseries, xx.prevendingendseries, 
    xx.receivedstartseries, xx.receivedendseries, 
    (case when xx.beginstartseries>0 and xx.prevendingstartseries>0 then xx.prevendingstartseries else xx.beginstartseries end) as beginstartseries, 
    (case when xx.beginendseries>0 and xx.prevendingendseries>0 then xx.prevendingendseries else xx.beginendseries end) as beginendseries, 
    xx.issuedstartseries, xx.issuedendseries, xx.issuednextseries, 
    (case when xx.issuednextseries>xx.endingendseries then null else xx.endingstartseries end) as endingstartseries, 
    (case when xx.issuednextseries>xx.endingendseries then null else xx.endingendseries end) as endingendseries, 
    xx.saled, 
    case 
      when xx.beginstartseries > 0 then xx.beginstartseries 
      when xx.issuedstartseries > 0 then xx.issuedstartseries 
      when xx.receivedstartseries > 0 then xx.receivedstartseries 
      else xx.endingstartseries 
    end as sortseries 
  from ( 
    select t1.*, 
      (
        select count(*) from af_inventory_detail 
        where controlid=t1.controlid and [lineno] <= t1.minlineno 
          and reftype='stocksale'
      ) as saled, 
      (
        select max(endingstartseries) from af_inventory_detail 
        where controlid=t1.controlid and [lineno]<t1.minlineno 
      ) as prevendingstartseries, 
      (
        select max(endingendseries) from af_inventory_detail 
        where controlid=t1.controlid and [lineno]<t1.minlineno 
      ) as prevendingendseries  
    from ( 
      select 
        controlid, min(refdate) as minrefdate, 
        min([lineno]) as minlineno, max([lineno]) as maxlineno,     
        min(receivedstartseries) as receivedstartseries, 
        max(receivedendseries) as receivedendseries, 
        min(beginstartseries) as beginstartseries, 
        max(beginendseries) as beginendseries, 
        min(issuedstartseries) as issuedstartseries, 
        max(issuedendseries) as issuedendseries, 
        max(issuedendseries)+1 as issuednextseries, 
        max(endingstartseries) as endingstartseries, 
        max(endingendseries) as endingendseries   
      from af_inventory_detail 
      where year(refdate)=$P{year} and month(refdate)=$P{month}  
      group by controlid 
    )t1  
  )xx inner join af_inventory afi on xx.controlid=afi.objid 
      inner join af on afi.afid = af.objid 
)xx 
order by xx.afid, xx.sortseries  


[getAFOUnforwardedAF]
select  
  afd.controlid,
  afd.endingstartseries as startseries, 
  afd.endingendseries as endseries  
from af_inventory_detail afd
  inner join af_inventory ai on ai.objid = afd.controlid 
where ai.respcenter_type ='AFO' 
  and ai.currentseries <= ai.endseries 
  and afd.[lineno] = ai.currentlineno
  and afd.refdate < $P{firstdate} 
