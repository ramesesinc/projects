[getReportData]
select tt2.*, 
  case 
    when (tt2.receivedstartseries + tt2.receivedendseries) is null then 0 
    else tt2.receivedendseries - tt2.receivedstartseries + 1 
  end as qtyreceived, 
  case 
    when (tt2.beginendseries + tt2.beginendseries) is null then 0 
    else tt2.beginendseries - tt2.beginstartseries + 1 
  end as qtybegin,   
  case 
    when (tt2.issuedendseries + tt2.issuedendseries) is null then 0 
    else tt2.issuedendseries - tt2.issuedstartseries + 1 
  end as qtyissued, 
  case 
    when (tt2.endingendseries + tt2.endingendseries) is null then 0 
    else tt2.endingendseries - tt2.endingstartseries + 1 
  end as qtyending 
from ( 
  select 
    tt1.controlid, a.afid, a.afid as formno, af.formtype, af.serieslength, af.denomination, 
    a.stubno, a.prefix, a.suffix, a.startseries, a.endseries, a.startseries as sortseries, a.endseries+1 as nextseries, 
    min(tt1.receivedstartseries) as receivedstartseries, max(tt1.receivedendseries) as receivedendseries, 
    case when min(tt1.receivedstartseries) > 0 then null else min(tt1.beginstartseries) end as beginstartseries, 
    case when max(tt1.receivedendseries) > 0 then null else max(tt1.beginendseries) end as beginendseries, 
    min(tt1.issuedstartseries) as issuedstartseries, max(tt1.issuedendseries) as issuedendseries, 
    case 
      when max(tt1.issuedendseries) >= a.endseries then null 
      when max(tt1.issuedendseries) <  a.endseries then max(tt1.issuedendseries)+1 
      else max(tt1.endingstartseries) 
    end as endingstartseries, 
    case 
      when max(tt1.issuedendseries) >= a.endseries then null 
      when max(tt1.issuedendseries) <  a.endseries then a.endseries 
      else max(tt1.endingendseries) 
    end as endingendseries 
  from ( 

    select 
      d.controlid, null as receivedstartseries, null as receivedendseries, 
      d.endingstartseries as beginstartseries, d.endingendseries as beginendseries, 
      null as issuedstartseries, null as issuedendseries, d.endingstartseries, d.endingendseries 
    from ( 
      select t1.*, 
        (
          select top 1 objid from af_control_detail 
          where controlid = t1.controlid and refdate = t1.refdate 
          order by convert(date, refdate) desc, txndate desc, indexno desc 
        ) as detailid 
      from ( 
        select d.controlid, max(d.refdate) as refdate 
        from af_control_detail d 
        where d.issuedto_objid = $P{collectorid}  
          and d.refdate < $P{startdate} 
        group by d.controlid 
      )t1 
    )t2, af_control_detail d 
    where d.objid = t2.detailid 
      and d.qtyending > 0 

    union all 

    select 
      d.controlid, d.endingstartseries as receivedstartseries, d.endingendseries as receivedendseries, 
      null as beginstartseries, null as beginendseries, null as issuedstartseries, null as issuedendseries, 
      d.endingstartseries, d.endingendseries 
    from af_control_detail d 
    where d.issuedto_objid = $P{collectorid}  
      and d.refdate >= $P{startdate} 
      and d.refdate <  $P{enddate} 
      and d.reftype = 'ISSUE' 
      and d.qtyreceived > 0 

    union all 

    select 
      t1.controlid, null as receivedstartseries, null as receivedendseries, 
      case 
        when min(t1.issuedstartseries) > 0 then min(t1.issuedstartseries) else min(t1.beginstartseries) 
      end as beginstartseries, 
      case 
        when min(t1.issuedstartseries) > 0 then a.endseries else min(t1.beginendseries) 
      end as beginendseries, 
      min(t1.issuedstartseries) as issuedstartseries, max(t1.issuedendseries) as issuedendseries, 
      case 
        when max(t1.issuedendseries) >= a.endseries then null 
        when max(t1.issuedendseries) <  a.endseries then max(t1.issuedendseries)+1 
        else max(t1.beginstartseries) 
      end as endingstartseries, 
      case 
        when max(t1.issuedendseries) >= a.endseries then null 
        when max(t1.issuedendseries) <  a.endseries then a.endseries 
        else max(t1.beginendseries) 
      end as endingendseries 
    from ( 
      select 
        d.controlid, null as receivedstartseries, null as receivedendseries, 
        case when d.qtybegin > 0 then d.beginstartseries else null end as beginstartseries,  
        case when d.qtybegin > 0 then d.beginendseries else null end as beginendseries, 
        case when d.qtyissued > 0 then d.issuedstartseries else null end as issuedstartseries, 
        case when d.qtyissued > 0 then d.issuedendseries else null end as issuedendseries, 
        case when d.qtyending > 0 then d.endingstartseries else null end as endingstartseries, 
        case when d.qtyending > 0 then d.endingendseries else null end as endingendseries, 
        0 as qtyreceived, d.qtybegin, d.qtyissued, d.qtyending, 0 as qtycancelled 
      from af_control_detail d 
      where d.issuedto_objid = $P{collectorid}  
        and d.refdate >= $P{startdate} 
        and d.refdate <  $P{enddate} 
        and (d.qtybegin + d.qtyissued) > 0 
    )t1, af_control a 
    where a.objid = t1.controlid 
    group by t1.controlid, a.endseries  

  )tt1, af_control a, af  
  where a.objid = tt1.controlid 
    and a.afid = af.objid 
  group by tt1.controlid, a.afid, af.formtype, af.serieslength,  
    af.denomination, a.stubno, a.prefix, a.suffix, a.startseries, a.endseries 
)tt2 
order by formno, startseries 


[getReportDataByRef]
select * 
from ( 
  select 
    'A' as idx, '' as type, afi.afid, af.formtype,
    afi.afid as formno, af.denomination, af.serieslength, 
    afi.owner_objid as ownerid, afi.owner_name as name, 
    'COLLECTOR' as respcentertype, 1 as categoryindex, 
    afi.stubno as startstub, afi.stubno as endstub, 
    case 
      when tmp.beginstartseries > 0 then tmp.beginstartseries 
      when tmp.issuedstartseries > 0 then tmp.issuedstartseries 
      when tmp.receivedstartseries > 0 then tmp.receivedstartseries 
      else tmp.endingstartseries 
    end as sortseries, 
    tmp.* 
  from ( 
    select 
      controlid, 
      min(case when receivedstartseries=0 then null else receivedstartseries end) as receivedstartseries, 
      max(case when receivedendseries=0 then null else receivedendseries end) as receivedendseries, 
      min(case when beginstartseries=0 then null else beginstartseries end) as beginstartseries, 
      max(case when beginendseries=0 then null else beginendseries end) as beginendseries, 
      min(case when issuedstartseries=0 then null else issuedstartseries end) as issuedstartseries, 
      max(case when issuedendseries=0 then null else issuedendseries end) as issuedendseries, 
      max(case when issuedendseries=0 then null else issuedendseries end)+1 as issuednextseries, 
      max(case when endingstartseries=0 then null else endingstartseries end) as endingstartseries, 
      max(case when endingendseries=0 then null else endingendseries end) as endingendseries 
    from remittance_af 
    where remittanceid = $P{refid} 
    group by controlid

    union 

    select 
      raf.controlid, 
      min(case when raf.receivedstartseries=0 then null else raf.receivedstartseries end) as receivedstartseries, 
      max(case when raf.receivedendseries=0 then null else raf.receivedendseries end) as receivedendseries, 
      min(case when raf.beginstartseries=0 then null else raf.beginstartseries end) as beginstartseries, 
      max(case when raf.beginendseries=0 then null else raf.beginendseries end) as beginendseries, 
      min(case when raf.issuedstartseries=0 then null else raf.issuedstartseries end) as issuedstartseries, 
      max(case when raf.issuedendseries=0 then null else raf.issuedendseries end) as issuedendseries, 
      max(case when raf.issuedendseries=0 then null else raf.issuedendseries end)+1 as issuednextseries, 
      max(case when raf.endingstartseries=0 then null else raf.endingstartseries end) as endingstartseries, 
      max(case when raf.endingendseries=0 then null else raf.endingendseries end) as endingendseries 
    from remittance rem 
      inner join remittance_af raf on raf.remittanceid = rem.objid 
    where rem.collectionvoucherid = $P{refid} 
    group by raf.controlid 

  )tmp 
    inner join af_control afi on afi.objid = tmp.controlid 
    inner join af on af.objid = afi.afid 
)t2 
where t2.formno like $P{formno}  
order by t2.formno, t2.sortseries 
