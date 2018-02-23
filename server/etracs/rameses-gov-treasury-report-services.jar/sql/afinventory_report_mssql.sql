[getReport]
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
        select top 1 objid from af_control_detail 
        where controlid = tmp1.controlid and txntype in ('PURCHASE','BEGIN') 
        order by refdate, txndate 
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
            select top 1 objid from af_control_detail 
            where controlid = afd.controlid and refdate = max(afd.refdate) 
            order by refdate desc, txndate desc 
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
            select top 1 objid from af_control_detail 
            where controlid = afd.controlid and refdate = min(afd.refdate) and reftype = min(afd.reftype) 
            order by refdate, txndate 
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
            select top 1 objid from af_control_detail 
            where controlid = afd.controlid and txntype = afd.txntype 
            order by refdate, txndate 
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
            select top 1 objid from af_control_detail 
            where controlid = afd.controlid and refdate = min(afd.refdate) and reftype = min(afd.reftype)
            order by refdate, txndate 
          ) as detailid 
        from af_control_detail afd 
          inner join af_control afc on afc.objid = afd.controlid 
        where afd.refdate >= $P{startdate} 
          and afd.refdate <  $P{enddate} 
          and afd.reftype in ('ISSUE') 
        group by afd.controlid 
      )bt1 
        inner join af_control_detail afd on afd.objid = bt1.detailid 
        left join aftxnitem afi on afi.objid = afd.refitemid 

    )tmp1 
    group by tmp1.controlid 

  )tmp2 
    inner join af_control_detail afd on afd.objid = tmp2.detailid 
    left join aftxnitem afi on afi.objid = afd.refitemid 

)tmp3, af_control afc, af 
where afc.objid = tmp3.controlid 
  and af.objid = afc.afid 

order by afc.afid, afc.prefix, afc.suffix, afc.startseries 
