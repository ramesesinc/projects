[getOpenAFControls]
select 
	tmp2.remittanceid, tmp2.controlid, tmp2.formno, tmp2.formtype, tmp2.formtitle, tmp2.unit, 
	tmp2.serieslength, tmp2.denomination, tmp2.stubno, tmp2.startseries, tmp2.endseries, tmp2.qtycancelled, 
	(case when tmp2.received = 1 then tmp2.receivedstartseries else null end) as receivedstartseries, 
	(case when tmp2.received = 1 then tmp2.receivedendseries else null end) as receivedendseries, 
	(case when tmp2.received = 1 then tmp2.qtyreceived else 0 end) as qtyreceived, 
	case 
		when tmp2.received = 0 and tmp2.qtyreceived > 0 then tmp2.receivedstartseries 
		when tmp2.received = 0 and tmp2.qtybegin > 0 then tmp2.beginstartseries 
	end as beginstartseries, 
	case 
		when tmp2.received = 0 and tmp2.qtyreceived > 0 then tmp2.receivedendseries 
		when tmp2.received = 0 and tmp2.qtybegin > 0 then tmp2.beginendseries 
	end as beginendseries, 
	case 
		when tmp2.received = 0 and tmp2.qtyreceived > 0 then tmp2.qtyreceived
		when tmp2.received = 0 and tmp2.qtybegin > 0 then tmp2.qtybegin 
	end as qtybegin  
from ( 

	select 
		tmp1.remittanceid, 
		(case when af.formtype = 'serial' then 0 else 1 end) as formindex, 
		afc.afid as formno, af.formtype, af.title as formtitle, afc.unit, 
		af.serieslength, af.denomination, afc.stubno, afc.startseries, afc.endseries, 
		case 
			when afd.refdate >= convert(tmp1.controldate, date) and afd.refdate <= tmp1.controldate 
			then 1 else 0 
		end as received, 
		afd.refdate, afd.controlid, afd.qtycancelled, 
		afd.receivedstartseries, afd.receivedendseries, afd.qtyreceived, 
		afd.beginstartseries, afd.beginendseries, afd.qtybegin, 
		afd.endingstartseries, afd.endingendseries, afd.qtyending 
	from ( 
		select 
			rem.objid as remittanceid, rem.controldate, ( 
				select objid from af_control_detail 
				where controlid = afc.objid and refdate <= rem.controldate 
				order by refdate desc, txndate desc limit 1 
			) as detailid 
		from remittance rem 
			inner join af_control afc on afc.owner_objid = rem.collector_objid 
		where rem.objid = $P{remittanceid}  
			and afc.currentseries <= afc.endseries 
			and afc.dtfiled <= rem.controldate 
	)tmp1 
		inner join af_control_detail afd on afd.objid = tmp1.detailid 
		inner join af_control afc on afc.objid = afd.controlid 
		inner join af on af.objid = afc.afid 
	where afd.qtyending > 0 

)tmp2
order by tmp2.formindex, tmp2.formno, tmp2.startseries 


[getIssuedAFControls]
select 
	c.remittanceid, c.afcontrolid as controlid, 
	c.fromseries as issuedstartseries, c.toseries as issuedendseries, c.qty as qtyissued, 
	(case when c.toseries >= c.endseries then null else c.toseries+1 end) as endingstartseries, 
	(case when c.toseries >= c.endseries then null else c.endseries end) as endingendseries, 
 	(case when c.toseries >= c.endseries then 0 else (c.endseries-(c.toseries+1))+1 end) as qtyending 
from cashreceipt_af_summary c 
where c.remittanceid = $P{remittanceid}  


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
