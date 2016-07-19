[getReportData]
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
    xx.saled, 
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
        select count(*) from af_inventory_detail 
        where controlid=t1.controlid and lineno <= t1.minlineno 
          and reftype='stocksale'
      ) as saled, 
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
        controlid, min(refdate) as minrefdate, 
        min(lineno) as minlineno, max(lineno) as maxlineno,     
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
  where afi.respcenter_objid = $P{collectorid}   
)xx 
order by xx.afid, xx.sortseries 


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
