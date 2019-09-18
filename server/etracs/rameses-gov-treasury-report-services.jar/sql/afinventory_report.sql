[getReport]
select 
  t1.afid, t1.formtype, t1.denomination, t1.serieslength, 
  t1.controlid, t1.prefix, t1.suffix, t1.cost, t1.startseries, t1.endseries, t1.nextseries, 
  min(t1.receivedstartseries) as receivedstartseries, max(t1.receivedendseries) as receivedendseries, sum(t1.qtyreceived) as qtyreceived, 
  min(t1.beginstartseries) as beginstartseries, max(t1.beginendseries) as beginendseries, sum(t1.qtybegin) as qtybegin, 
  min(t1.issuedstartseries) as issuedstartseries, max(t1.issuedendseries) as issuedendseries, sum(t1.qtyissued) as qtyissued, 
  min(t1.endingstartseries) as endingstartseries, max(t1.endingendseries) as endingendseries, sum(t1.qtyending) as qtyending, 
  sum(t1.qtycancelled) as qtycancelled 
from ( 
  select 
    a.afid, af.formtype, af.denomination, af.serieslength, 
    d.controlid, a.prefix, a.suffix, a.cost, 
    a.startseries, a.endseries, a.endseries+1 as nextseries, 
    null as receivedstartseries, null as receivedendseries, 0 as qtyreceived, 
    case when d.reftype='PURCHASE_RECEIPT' then d.receivedstartseries else d.beginstartseries end as beginstartseries, 
    case when d.reftype='PURCHASE_RECEIPT' then d.receivedendseries else d.beginendseries end as beginendseries,
    case when d.reftype='PURCHASE_RECEIPT' then d.qtyreceived else d.qtybegin end as qtybegin, 
    null as issuedstartseries, null as issuedendseries, 0 as qtyissued, 
    null as endingstartseries, null as endingendseries, 0 as qtyending, 
    d.qtycancelled 
  from ( 
    select controlid, sum(iflag) as iflag   
    from ( 
      select d.controlid, 
        case when d.reftype in ('PURCHASE_RECEIPT','BEGIN_BALANCE') then 1 else -1 end as iflag 
      from af_control_detail d 
      where d.refdate < $P{startdate} 
        and d.reftype in ('PURCHASE_RECEIPT','BEGIN_BALANCE','ISSUE','MANUAL_ISSUE')
    )t1 
    group by controlid 
    having sum(iflag) > 0 
  )t2 
    inner join af_control_detail d on (d.controlid = t2.controlid and d.reftype in ('PURCHASE_RECEIPT','BEGIN_BALANCE')) 
    inner join af_control a on a.objid = d.controlid 
    inner join af on af.objid = a.afid 

  union all 

  select 
    a.afid, af.formtype, af.denomination, af.serieslength, 
    d.controlid, a.prefix, a.suffix, a.cost, 
    a.startseries, a.endseries, a.endseries+1 as nextseries, 
    null as receivedstartseries, null as receivedendseries, 0 as qtyreceived, 
    d.beginstartseries, d.beginendseries, d.qtybegin, 
    null as issuedstartseries, null as issuedendseries, 0 as qtyissued, 
    null as endingstartseries, null as endingendseries, 0 as qtyending, 
    d.qtycancelled 
  from af_control_detail d 
    inner join af_control a on a.objid = d.controlid 
    inner join af on af.objid = a.afid 
  where d.refdate >= $P{startdate} 
    and d.refdate <  $P{enddate} 
    and d.reftype = 'BEGIN_BALANCE'

  union all 

  select 
    a.afid, af.formtype, af.denomination, af.serieslength, 
    d.controlid, a.prefix, a.suffix, a.cost, 
    a.startseries, a.endseries, a.endseries+1 as nextseries, 
    d.receivedstartseries, d.receivedendseries, d.qtyreceived, 
    null as beginstartseries, null as beginendseries, 0 as qtybegin, 
    null as issuedstartseries, null as issuedendseries, 0 as qtyissued, 
    null as endingstartseries, null as endingendseries, 0 as qtyending, 
    d.qtycancelled 
  from af_control_detail d 
    inner join af_control a on a.objid = d.controlid 
    inner join af on af.objid = a.afid 
  where d.refdate >= $P{startdate} 
    and d.refdate <  $P{enddate} 
    and d.reftype = 'PURCHASE_RECEIPT'

  union all 

  select 
    a.afid, af.formtype, af.denomination, af.serieslength, 
    d.controlid, a.prefix, a.suffix, a.cost, 
    a.startseries, a.endseries, a.endseries+1 as nextseries, 
    null as receivedstartseries, null as receivedendseries, 0 as qtyreceived, 
    null as beginstartseries, null as beginendseries, 0 as qtybegin, 
    d.receivedstartseries as issuedstartseries, d.receivedendseries as issuedendseries, d.qtyreceived as qtyissued, 
    null as endingstartseries, null as endingendseries, 0 as qtyending, 
    d.qtycancelled 
  from af_control_detail d 
    inner join af_control a on a.objid = d.controlid 
    inner join af on af.objid = a.afid 
  where d.refdate >= $P{startdate} 
    and d.refdate <  $P{enddate} 
    and d.reftype in ('ISSUE','MANUAL_ISSUE') 
)t1 
group by 
  t1.afid, t1.formtype, t1.denomination, t1.serieslength, 
  t1.controlid, t1.prefix, t1.suffix, t1.cost, 
  t1.startseries, t1.endseries, t1.nextseries 
order by t1.afid, t1.prefix, t1.suffix, t1.startseries 


[getReport_bak1]
select tmp3.*,  
  (case when tmp3.qtyreceived > 0 then tmp3.costperstub else 0.0 end) as qtyreceivedcost, 
  (case when tmp3.qtybegin > 0 then tmp3.costperstub else 0.0 end) as qtybegincost, 
  (case when tmp3.salecost > 0 then tmp3.salecost - tmp3.costperstub else 0.0 end) as gaincost, 
  afc.afid, afc.afid as formno, af.formtype, af.serieslength, af.denomination, afc.unit, 
  afc.startseries, afc.endseries, afc.endseries+1 as nextseries, afc.prefix, afc.suffix  
from ( 

  select tmp2.*, 
    (case when afi.cost > 0 then afi.cost else 0.0 end) as costperstub, 
    case 
      when tmp2.issuedcost > 0 then tmp2.issuedcost 
      when tmp2.salecost > 0 then tmp2.salecost 
      else 0.0 
    end as qtyissuedcost  
  from ( 

    select 
      tmp1.controlid, 
      min(tmp1.receivedstartseries) as receivedstartseries, max(tmp1.receivedendseries) as receivedendseries, 
      min(tmp1.beginstartseries) as beginstartseries, max(tmp1.beginendseries) as beginendseries, 
      min(tmp1.issuedstartseries) as issuedstartseries, max(tmp1.issuedendseries) as issuedendseries, 
      sum(tmp1.issuedcost) as issuedcost, sum(tmp1.salecost) as salecost, 
      (case when (max(tmp1.receivedendseries)-min(tmp1.receivedstartseries)) is null then 0 else 1 end) as qtyreceived, 
      (case when (max(tmp1.beginendseries)-min(tmp1.beginstartseries)) is null then 0 else 1 end) as qtybegin, 
      (case when (max(tmp1.issuedendseries)-min(tmp1.issuedstartseries)) is null then 0 else 1 end) as qtyissued, 
      (
        select objid from af_control_detail 
        where controlid = tmp1.controlid and txntype in ('PURCHASE','BEGIN') 
        order by refdate, txndate limit 1 
      ) as detailid 
    from ( 

      /* previous AF */
      select 
        afd.controlid, null as receivedstartseries, null as receivedendseries, 
        afd.endingstartseries as beginstartseries, afd.endingendseries as beginendseries, 
        null as issuedstartseries, null as issuedendseries, 
        null as issuedcost, null as salecost 
      from ( 
        select afd.controlid, max(afd.refdate) as refdate, (
            select objid from af_control_detail 
            where controlid = afd.controlid and refdate = max(afd.refdate) 
            order by refdate desc, txndate desc limit 1 
          ) as detailid 
        from af_control_detail afd 
        where afd.refdate < $P{startdate} 
        group by afd.controlid 
      )bt1 
        inner join af_control_detail afd on afd.objid = bt1.detailid 
      where afd.issuedto_objid is null 
        and afd.txntype in ('PURCHASE','BEGIN') 
        and afd.qtyending > 0 

      union all 

      /* currently issued using previous AF */
      select 
        afd.controlid, 
        null as receivedstartseries, null as receivedendseries, 
        case 
          when afd.qtyissued > 0 then afd.issuedstartseries 
          when afd.qtyreceived > 0 then afd.receivedstartseries 
        end as beginstartseries, 
        case 
          when afd.qtyissued > 0 then afd.issuedendseries 
          when afd.qtyreceived > 0 then afd.receivedendseries 
        end as beginendseries, 
        null as issuedstartseries, null as issuedendseries,  
        null as issuedcost, null as salecost 
      from ( 
        select afd.controlid, min(afd.refdate) as refdate, (
            select objid from af_control_detail 
            where controlid = afd.controlid and refdate = min(afd.refdate) and reftype = afd.reftype 
            order by refdate, txndate limit 1 
          ) as detailid 
        from af_control_detail afd 
          inner join af_control afc on afc.objid = afd.controlid 
        where afd.refdate >= $P{startdate} 
          and afd.refdate <  $P{enddate} 
          and afd.reftype in ('ISSUE') 
          and afc.dtfiled < $P{startdate} 
        group by afd.controlid 
      )bt1 
        inner join af_control_detail afd on afd.objid = bt1.detailid 

      union all 

      /* currently received AF */
      select 
        afd.controlid, 
        afd.receivedstartseries, afd.receivedendseries, 
        null as beginstartseries, null as beginendseries, 
        null as issuedstartseries, null as issuedendseries, 
        null as issuedcost, null as salecost 
      from ( 
        select afd.controlid, ( 
            select objid from af_control_detail 
            where controlid = afd.controlid and txntype = afd.txntype 
            order by refdate, txndate limit 1 
          ) as detailid 
        from af_control_detail afd 
        where afd.refdate >= $P{startdate} 
          and afd.refdate <  $P{enddate} 
          and afd.txntype in ('PURCHASE') 
          and afd.qtyreceived > 0 
      )bt1 
        inner join af_control_detail afd on afd.objid = bt1.detailid 
        inner join af_control afc on afc.objid = afd.controlid 

      union all 

      /* currently issued AF */ 
      select 
        afd.controlid, 
        null as receivedstartseries, null as receivedendseries, 
        null as beginstartseries, null as beginendseries, 
        case 
          when afd.qtyissued > 0 then afd.issuedstartseries 
          when afd.qtyreceived > 0 then afd.receivedstartseries 
        end as issuedstartseries, 
        case 
          when afd.qtyissued > 0 then afd.issuedendseries 
          when afd.qtyreceived > 0 then afd.receivedendseries 
        end as issuedendseries, 
        (case when afd.qtyreceived > 0 then afi.cost else 0.0 end) as issuedcost, 
        (case when afd.qtyissued > 0 then afi.cost else 0.0 end) as salecost 
      from ( 
        select afd.controlid, min(afd.refdate) as refdate, (
            select objid from af_control_detail 
            where controlid = afd.controlid and refdate = min(afd.refdate) and reftype = afd.reftype 
            order by refdate, txndate limit 1 
          ) as detailid 
        from af_control_detail afd 
          inner join af_control afc on afc.objid = afd.controlid 
        where afd.refdate >= $P{startdate} 
          and afd.refdate <  $P{enddate} 
          and afd.reftype in ('ISSUE') 
        group by afd.controlid 
      )bt1 
        inner join af_control_detail afd on afd.objid = bt1.detailid 
        left join aftxnitem afi on afi.objid = afd.aftxnitemid 

    )tmp1 
    group by tmp1.controlid 

  )tmp2 
    inner join af_control_detail afd on afd.objid = tmp2.detailid 
    left join aftxnitem afi on afi.objid = afd.aftxnitemid 

)tmp3, af_control afc, af 
where afc.objid = tmp3.controlid 
  and af.objid = afc.afid 

order by afc.afid, afc.prefix, afc.suffix, afc.startseries 
