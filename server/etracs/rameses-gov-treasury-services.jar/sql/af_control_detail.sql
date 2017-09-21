
[getRemittanceAF]
select 
  afc.startseries, afc.endseries, afc.afid as formno, 
  af.formtype, af.denomination, af.serieslength, tmp2.* 
from ( 
  select 
    tmp1.controlid, 
    min(tmp1.receivedstartseries) as receivedstartseries, min(tmp1.receivedendseries) as receivedendseries, 
    min(tmp1.beginstartseries) as beginstartseries, min(tmp1.beginendseries) as beginendseries, 
    min(tmp1.issuedstartseries) as issuedstartseries, max(tmp1.issuedendseries) as issuedendseries, 
    case when min(tmp1.receivedstartseries) is null then 0 else (min(tmp1.receivedendseries)-min(tmp1.receivedstartseries))+1 end as qtyreceived, 
    case when min(tmp1.beginstartseries) is null then 0 else (min(tmp1.beginendseries)-min(tmp1.beginstartseries))+1 end as qtybegin, 
    sum( tmp1.qtyissued ) as qtyissued, 
    case when sum(tmp1.qtyissued) > 0 then 0 else 1 end as groupindex  
  from ( 
    select 
      afd.controlid, afd.indexno, 
      afd.receivedstartseries, afd.receivedendseries, afd.beginstartseries, afd.beginendseries, 
      afd.issuedstartseries, afd.issuedendseries, afd.endingstartseries, afd.endingendseries, 
      afd.qtyreceived, afd.qtybegin, afd.qtyissued, afd.qtyending, afd.qtycancelled 
    from af_control_detail afd 
    where refid = $P{remittanceid} 
    union all 
    select 
      afd.controlid, afd.indexno, 
      afd.receivedstartseries, afd.receivedendseries, afd.beginstartseries, afd.beginendseries, 
      null as issuedstartseries, null as issuedendseries, afd.endingstartseries, afd.endingendseries, 
      afd.qtyreceived, afd.qtybegin, 0 as qtyissued, afd.qtyending, 0 as qtycancelled 
    from ( 
      select controlid, convert(min(refdate),date) as refdate 
      from af_control_detail where refid = $P{remittanceid} 
      group by controlid 
    )tmp1, af_control_detail afd 
    where afd.controlid = tmp1.controlid 
      and afd.indexno = 1 
      and convert(afd.refdate,date)=tmp1.refdate 
  )tmp1
  group by tmp1.controlid 
)tmp2 
  inner join af_control afc on afc.objid = tmp2.controlid 
  inner join af on af.objid = afc.afid 
order by tmp2.groupindex, afc.afid, afc.startseries 
