[getCraafData]
select tmp5.* 
from ( 

  select tmp4.*, 
    tmp4.ownername as name, tmp4.ownertype as respcentertype, 
    (case when tmp4.ownertype='AFO' then 1 else 2 end) as respcenterlevel, 
    (case when tmp4.qtyissued > 0 then 0 else 1 end) as categoryindex 
    
  from ( 

    select 
      tmp3.controlid, tmp3.minrefdate, tmp3.maxrefdate, 
      afc.afid, af.formtype as aftype, afc.afid as formno, af.formtype, af.denomination, af.serieslength, 
      afc.dtfiled, afc.prefix, afc.suffix, afc.stubno as startstub, afc.stubno as endstub, 
      afc.startseries, afc.endseries, afc.endseries+1 as nextseries, afc.startseries as sortseries, 
      (case when afd.issuedto_objid is null then 0 else 1 end) as ownerlevel, 
      (case when afd.issuedto_objid is null then 'AFO' else 'COLLECTOR' end) as ownertype, 
      (case when afd.issuedto_objid is null then 'AFO' else afd.issuedto_objid end) as ownerid, 
      (case when afd.issuedto_objid is null then 'AFO' else afd.issuedto_name end) as ownername, 
      (case when afd.txntype = 'SALE' then 1 else 0 end) as saled, 
      (case when tmp3.beginstartseries > 0 then null else tmp3.receivedstartseries end) as receivedstartseries, 
      (case when tmp3.beginstartseries > 0 then null else tmp3.receivedendseries end) as receivedendseries, 
      case 
        when tmp3.beginstartseries > 0 then 0  
        else (tmp3.receivedendseries-tmp3.receivedstartseries)+1 
      end as qtyreceived, 
      tmp3.beginstartseries, tmp3.beginendseries, 
      case 
        when tmp3.beginstartseries > 0 
        then (tmp3.beginendseries-tmp3.beginstartseries)+1 else 0 
      end as qtybegin, 
      tmp3.issuedstartseries, tmp3.issuedendseries, tmp3.qtyissued, 
      tmp3.endingstartseries, tmp3.endingendseries, 
      case 
        when tmp3.endingstartseries > 0 
        then (tmp3.endingendseries-tmp3.endingstartseries)+1 else 0 
      end as qtyending, 
      tmp3.consumed, 
      case 
        when afd.txntype = 'SALE' then 'SALE'  
        when tmp3.consumed > 0 then 'CONSUMED' 
        else null 
      end as remarks 
    from ( 

      select tmp2.*, (
          select objid from af_control_detail 
          where controlid = tmp2.controlid and refdate = tmp2.maxrefdate 
          order by refdate desc, txndate desc limit 1 
        ) as detailid 
      from ( 

        select 
          tmp1.controlid, min(tmp1.refdate) as minrefdate, max(tmp1.refdate) as maxrefdate, 
          min(tmp1.receivedstartseries) as receivedstartseries, min(tmp1.receivedendseries) as receivedendseries, 
          min(tmp1.beginstartseries) as beginstartseries, min(tmp1.beginendseries) as beginendseries, 
          min(tmp1.issuedstartseries) as issuedstartseries, max(tmp1.issuedendseries) as issuedendseries, 
          sum(tmp1.qtyissued) as qtyissued, 
          case 
            when max(tmp1.issuedendseries) >= tmp1.endseries then null 
            when max(tmp1.issuedendseries) < tmp1.endseries then max(tmp1.issuedendseries)+1 
            when min(tmp1.beginstartseries) > 0 then min(tmp1.beginstartseries) 
            else min(tmp1.receivedstartseries) 
          end as endingstartseries, 
          case 
            when max(tmp1.issuedendseries) >= tmp1.endseries then null 
            else tmp1.endseries 
          end as endingendseries, 
          case 
            when max(tmp1.issuedendseries) >= tmp1.endseries then 1 else 0 
          end as consumed  
        from ( 

          select 
            afd.controlid, afd.refdate, bt1.endseries, 
            null as receivedstartseries, null as receivedendseries, 
            afd.endingstartseries as beginstartseries, afd.endingendseries as beginendseries, 
            null as issuedstartseries, null as issuedendseries, 0 as qtyissued 
          from ( 
            select afd.controlid, max(afd.refdate) as refdate, afc.endseries 
            from af_control_detail afd 
              inner join af_control afc on afc.objid = afd.controlid 
            where afd.refdate < $P{startdate} 
            group by afd.controlid, afc.endseries  
          )bt1 
            inner join af_control_detail afd on (afd.controlid = bt1.controlid and afd.refdate = bt1.refdate)
          where afd.qtyending > 0 

          union all 

          select 
            afd.controlid, max(afd.refdate) as refdate, afc.endseries, 
            min(case when afd.receivedstartseries > 0 then afd.receivedstartseries else null end) as receivedstartseries, 
            min(case when afd.receivedendseries > 0 then afd.receivedendseries else null end) as receivedendseries, 
            min(case when afd.beginstartseries > 0 then afd.beginstartseries else null end) as beginstartseries, 
            min(case when afd.beginendseries > 0 then afd.beginendseries else null end) as beginendseries, 
            min(case when afd.issuedstartseries > 0 then afd.issuedstartseries else null end) as issuedstartseries, 
            min(case when afd.issuedendseries > 0 then afd.issuedendseries else null end) as issuedendseries, 
            sum(afd.qtyissued) as qtyissued 
          from af_control_detail afd, af_control afc  
          where afd.refdate >= $P{startdate} 
            and afd.refdate <  $P{enddate}  
            and afc.objid = afd.controlid 
          group by afd.controlid, afc.endseries  

        )tmp1 
        group by tmp1.controlid, tmp1.endseries 

      )tmp2 

    )tmp3, af_control_detail afd, af_control afc, af  
    where afd.objid = tmp3.detailid 
      and afc.objid = afd.controlid 
      and af.objid = afc.afid 

  )tmp4
)tmp5
order by tmp5.afid, tmp5.respcenterlevel, tmp5.categoryindex, tmp5.dtfiled, tmp5.startseries 
