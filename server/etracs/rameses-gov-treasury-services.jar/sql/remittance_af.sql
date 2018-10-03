[getBuildAFs]
select 
	t3.remittanceid, t3.controlid, af.objid as formno, af.formtype, af.title as formtitle, afc.unit, 
	af.serieslength, af.denomination, afc.stubno, afc.startseries, afc.endseries, 
	t3.receivedstartseries, t3.receivedendseries, t3.beginstartseries, t3.beginendseries, 
	t3.issuedstartseries, t3.issuedendseries, t3.endingstartseries, t3.endingendseries,
	case 
		when (t3.receivedendseries-t3.receivedstartseries+1) is null then 0 
		else (t3.receivedendseries-t3.receivedstartseries+1)
	end as qtyreceived, 
	case 
		when (t3.beginendseries-t3.beginstartseries+1) is null then 0 
		else (t3.beginendseries-t3.beginstartseries+1)
	end as qtybegin, 
	case 
		when (t3.issuedendseries-t3.issuedstartseries+1) is null then 0 
		else (t3.issuedendseries-t3.issuedstartseries+1)
	end as qtyissued, 
	case 
		when (t3.endingendseries-t3.endingstartseries+1) is null then 0 
		else (t3.endingendseries-t3.endingstartseries+1)
	end as qtyending, 
	0 as qtycancelled 
from ( 

	select 
		t2.remittanceid, t2.controlid, 
		min(receivedstartseries) as receivedstartseries, max(receivedendseries) as receivedendseries, 
		(
			case 
				when max(received) > 0 then null 
				when max(qtybegin) > 0 then min(beginstartseries) 
				else min(receivedstartseries) 
			end 
		) as beginstartseries, 
		(
			case 
				when max(received) > 0 then null 
				when max(qtybegin) > 0 then max(beginendseries) 
				else max(receivedendseries) 
			end 
		) as beginendseries, 
		min(issuedstartseries) as issuedstartseries, max(issuedendseries) as issuedendseries, 
		max(endingstartseries) as endingstartseries, max(endingendseries) as endingendseries, 
		max(received) as received 
	from ( 
		select 
			t1.remittanceid, afd.controlid, 
			(case when afd.receivedstartseries=0 then null else afd.receivedstartseries end) as receivedstartseries, 
			(case when afd.receivedendseries=0 then null else afd.receivedendseries end) as receivedendseries, 
			(case when afd.beginstartseries=0 then null else afd.beginstartseries end) as beginstartseries, 
			(case when afd.beginendseries=0 then null else afd.beginendseries end) as beginendseries,
			null as issuedstartseries, null as issuedendseries, 
			(case when afd.endingstartseries=0 then null else afd.endingstartseries end) as endingstartseries, 
			(case when afd.endingendseries=0 then null else afd.endingendseries end) as endingendseries,
			afd.qtyreceived, afd.qtybegin, afd.qtyissued, afd.qtyending, afd.qtycancelled, 
			(case 
				when t1.dtfiled >= convert(t1.controldate, date) and t1.dtfiled <= t1.controldate and afd.qtyreceived > 0 
				then 1 else 0 
			end) as received 
		from ( 
			select 
				rem.objid as remittanceid, rem.controldate, 
				afc.dtfiled, ( 
					select objid from af_control_detail 
					where controlid = afc.objid and refdate <= rem.controldate  
					order by refdate desc, txndate desc limit 1 
				) as detailid 
			from remittance rem 
				inner join af_control afc on afc.owner_objid = rem.collector_objid 
			where rem.objid = $P{remittanceid} 
				and afc.currentseries <= afc.endseries 
				and afc.dtfiled <= rem.controldate 
		)t1 
			inner join af_control_detail afd on afd.objid = t1.detailid 
		where afd.qtyending > 0 

		union all 

		select 
			c.remittanceid, c.controlid, 
			null as receivedstartseries, null as receivedendseries, 
			min(c.series) as beginstartseries, max(afc.endseries) as beginendseries, 
			min(c.series) as issuedstartseries, max(c.series) as issuedendseries, 
			case 
				when max(c.series) >= max(afc.endseries) then null else max(c.series)+1 
			end as endingstartseries,  
			case 
				when max(c.series) >= max(afc.endseries) then null else max(afc.endseries)  
			end as endingendseries, 
			0 as qtyreceived,   
			max(afc.endseries)-min(c.series)+1 as qtybegin, 
			max(c.series)-min(c.series)+1 as qtyissued, 
			case 
				when max(c.series) >= max(afc.endseries) then 0 
				else max(afc.endseries)-max(c.series)  
			end as qtyending, 
			0 as qtycancelled, 0 as received  
		from remittance rem 
			inner join cashreceipt c on c.remittanceid = rem.objid 
			inner join af_control afc on afc.objid = c.controlid 
		where rem.objid = $P{remittanceid} 
		group by c.remittanceid, c.controlid 
	)t2  
	group by t2.remittanceid, t2.controlid 

)t3 
	inner join af_control afc on afc.objid = t3.controlid 
	inner join af on af.objid = afc.afid 


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
