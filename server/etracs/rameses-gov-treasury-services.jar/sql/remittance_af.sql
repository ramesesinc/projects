[getBuildAFs]

select 
    remittanceid, controlid, formno, formtype, formtitle, unit, 
    serieslength, denomination, stubno, startseries, endseries, nextseries, 
    receivedstartseries, receivedendseries, 
    case when qtyreceived > 0 then null else beginstartseries end as beginstartseries, 
    case when qtyreceived > 0 then null else beginendseries end as beginendseries, 
    issuedstartseries, issuedendseries, qtyreceived, qtyissued, 
    case when qtyreceived > 0 then null else qtybegin end as qtybegin, 
    case 
        when issuedendseries >= endseries then null 
        when issuedendseries < endseries then issuedendseries+1 
        when beginstartseries > 0 then beginstartseries 
        else receivedstartseries 
    end as endingstartseries, 
    case 
        when issuedendseries >= endseries then null 
        when issuedendseries < endseries then endseries  
        when beginendseries > 0 then beginendseries 
        else receivedendseries 
    end as endingendseries, 
    case 
        when issuedendseries >= endseries then null 
        when issuedendseries < endseries then endseries-issuedendseries
        when beginstartseries > 0 then beginendseries-beginstartseries+1 
        else receivedendseries-receivedstartseries+1 
    end as qtyending
from ( 

    select 
        remittanceid, controlid, formno, formtype, formtitle, unit, serieslength, 
        denomination, stubno, startseries, endseries, endseries+1 as nextseries, 
        min(receivedstartseries) as receivedstartseries, max(receivedendseries) as receivedendseries, 
        min(beginstartseries) as beginstartseries, max(beginendseries) as beginendseries, 
        min(issuedstartseries) as issuedstartseries, max(issuedendseries) as issuedendseries, 
        max(receivedendseries)-min(receivedstartseries)+1 as qtyreceived, 
        max(beginendseries)-min(beginstartseries)+1 as qtybegin, 
        max(issuedendseries)-min(issuedstartseries)+1 as qtyissued 
    from ( 

        select 
            t2.remittanceid, t2.controlid, a.afid as formno, af.formtype, af.title as formtitle, a.unit, 
            af.serieslength, af.denomination, a.stubno, a.startseries, a.endseries, 
            case when t2.reftype = 'ISSUE' then t2.beginstartseries else null end as receivedstartseries, 
            case when t2.reftype = 'ISSUE' then t2.beginendseries else null end as receivedendseries, 
            case when t2.reftype = 'ISSUE' then null else t2.beginstartseries end as beginstartseries, 
            case when t2.reftype = 'ISSUE' then null else t2.beginendseries end as beginendseries, 
            null as issuedstartseries, null as issuedendseries, 
            case when t2.reftype = 'ISSUE' then t2.qtybegin else 0 end as qtyreceived, 
            case when t2.reftype = 'ISSUE' then 0 else t2.qtybegin end as qtybegin, 
            t2.qtyissued, t2.qtycancelled 
        from ( 
            select 
                d.controlid, remittanceid, d.reftype, 
                null as receivedstartseries, null as receivedendseries, 
                case when d.qtyending = 0 then null else d.endingstartseries end beginstartseries, 
                case when d.qtyending = 0 then null else d.endingendseries end beginendseries, 
                null as issuedstartseries, null as issuedendseries, 
                0 as qtyreceived, d.qtyending as qtybegin, 0 as qtyissued, 0 as qtycancelled 
            from ( 
                select r.objid as remittanceid,
                    afc.owner_objid, afc.objid as controlid, 
                    (
                        select objid from af_control_detail 
                        where controlid = afc.objid and refdate <= r.dtposted 
                        order by convert(refdate, date) desc, txndate desc, indexno desc 
                        limit 1 
                    ) as detailid 
                from remittance r 
                    inner join af_control afc on afc.owner_objid = r.collector_objid 
                where r.objid = $P{remittanceid}  
                    and afc.currentseries <= afc.endseries 
                    and afc.dtfiled <= r.dtposted 
            )t1 
                inner join af_control_detail d on d.objid = t1.detailid 
            where d.issuedto_objid = t1.owner_objid 
                and d.qtyending > 0 
        )t2, af_control a, af 
        where a.objid = t2.controlid 
            and af.objid = a.afid 

        union all 

        select 
            t2.remittanceid, t2.controlid, afc.afid as formno, af.formtype, af.title as formtitle, afc.unit, 
            af.serieslength, af.denomination, afc.stubno, afc.startseries, afc.endseries, 
            null as receivedstartseries, null as receivedendseries, 
            d.endingstartseries as beginstartseries, d.endingendseries as beginendseries, 
            null as issuedstartseries, null as issuedendseries,
            0 as qtyreceived, d.endingendseries-d.endingstartseries+1 as qtybegin, 
            0 as qtyissued, 0 as qtycancelled 
        from ( 
            select t1.*, ( 
                    select objid from af_control_detail 
                    where controlid = t1.controlid and refdate <= t1.dtposted 
                    order by convert(refdate, date) desc, txndate desc, indexno desc 
                    limit 1 
                ) as detailid 
            from ( 
                select c.remittanceid, c.controlid, r.dtposted, count(*) as icount 
                from remittance r 
                    inner join cashreceipt c on c.remittanceid = r.objid 
                    inner join af_control afc on afc.objid = c.controlid 
                    inner join af on af.objid = afc.afid 
                where r.objid = $P{remittanceid}   
                group by c.remittanceid, c.controlid, r.dtposted
            )t1 
        )t2 
            inner join af_control_detail d on d.objid = t2.detailid 
            inner join af_control afc on afc.objid = d.controlid 
            inner join af on af.objid = afc.afid 

        union all 

        select 
            c.remittanceid, afc.objid as controlid, afc.afid as formno, af.formtype, af.title as formtitle, 
            afc.unit, af.serieslength, af.denomination, afc.stubno, afc.startseries, afc.endseries, 
            null as receivedstartseries, null as receivedendseries, 
            null as beginstartseries, null as beginendseries, 
            min(c.series) as issuedstartseries, max(c.series) as issuedendseries, 
            0 as qtyreceived, 0 as qtybegin, max(c.series)-min(c.series)+1 as qtyissued, 
            0 as qtycancelled 
        from remittance r 
            inner join cashreceipt c on c.remittanceid = r.objid 
            inner join af_control afc on afc.objid = c.controlid 
            inner join af on (af.objid = afc.afid and af.formtype = 'serial') 
        where r.objid = $P{remittanceid}   
        group by c.remittanceid, afc.objid, afc.afid, af.formtype, af.title, 
            afc.unit, af.serieslength, af.denomination, afc.stubno, afc.startseries, afc.endseries 

        union all 

        select 
            t2.remittanceid, t2.controlid, afc.afid as formno, af.formtype, af.title as formtitle, 
            afc.unit, af.serieslength, af.denomination, afc.stubno, afc.startseries, afc.endseries,
            null as receivedstartseries, null as receivedendseries, 
            d.endingstartseries as beginstartseries, d.endingendseries as beginendseries, 
            d.endingstartseries as issuedstartseries, d.endingstartseries+t2.qtyissued-1 as issuedendseries, 
            0 as qtyreceived, d.qtyending as qtybegin, t2.qtyissued, 0 as qtycancelled 
        from ( 
            select t1.*, ( 
                    select objid from af_control_detail 
                    where controlid = t1.controlid and refdate <= t1.dtposted 
                    order by convert(refdate, date) desc, txndate desc, indexno desc 
                    limit 1 
                ) as detailid 
            from ( 
                select c.remittanceid, r.dtposted, afc.objid as controlid, 
                    convert((sum(c.amount) / af.denomination), signed) as qtyissued  
                from remittance r 
                    inner join cashreceipt c on c.remittanceid = r.objid 
                    inner join af_control afc on afc.objid = c.controlid 
                    inner join af on (af.objid = afc.afid and af.formtype <> 'serial') 
                    left join cashreceipt_void v on v.receiptid = c.objid 
                where r.objid = $P{remittanceid}   
                    and c.state = 'POSTED' 
                    and v.objid is null 
                group by c.remittanceid, r.dtposted, afc.objid, af.denomination 
            )t1 
        )t2 
            inner join af_control_detail d on d.objid = t2.detailid 
            inner join af_control afc on afc.objid = d.controlid 
            inner join af on af.objid = afc.afid 

    )tt1 
    group by remittanceid, controlid, formno, formtype, formtitle, unit, 
        serieslength, denomination, stubno, startseries, endseries 

)tt2 
order by formno, startseries 


[getCancelledSeries]
select 
	c.remittanceid, c.controlid, af.formtype, afc.afid, 
	c.series, c.receiptno as refno, c.objid as refid 
from cashreceipt c 
	inner join af_control afc on afc.objid = c.controlid 
	inner join af on af.objid = afc.afid 
where c.remittanceid = $P{remittanceid}  
	and c.state = 'CANCELLED' 
	and af.formtype = 'serial' 
