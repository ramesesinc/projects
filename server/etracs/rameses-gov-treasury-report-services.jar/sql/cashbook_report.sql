[findBeginBalance]
select sum(dr) as dr, sum(cr) as cr, sum(dr)-sum(cr) as balance 
from ( 
	select sum(dr) as dr, sum(cr) as cr 
	from vw_cashbook_cashreceipt 
	where refdate < $P{fromdate} 
		and collectorid = $P{accountid} 
		and fundid = $P{fundid} 
	union all 
	select sum(dr) as dr, sum(cr) as cr 
	from vw_cashbook_cashreceiptvoid 
	where refdate < $P{fromdate} 
		and collectorid = $P{accountid} 
		and fundid = $P{fundid} 
	union all 
	select sum(v.dr) as dr, sum(v.cr) as cr  
	from vw_cashbook_remittance v, remittance r  
	where v.refdate < $P{fromdate} 
		and v.collectorid = $P{accountid} 
		and v.fundid = $P{fundid} 
		and v.objid = r.objid 
		and r.liquidatingofficer_objid is not null 
)t1 


[getDetails]
select * 
from ( 
	select refdate, refno, reftype, sum(dr) as dr, sum(cr) as cr, sortdate 
	from vw_cashbook_cashreceipt 
	where refdate >= $P{fromdate} 
		and refdate <  $P{todate} 
		and collectorid = $P{accountid} 
		and fundid = $P{fundid} 
	group by refdate, refno, reftype, sortdate 
	union all 
	select refdate, refno, reftype, sum(dr) as dr, sum(cr) as cr, sortdate 
	from vw_cashbook_cashreceiptvoid 
	where refdate >= $P{fromdate} 
		and refdate <  $P{todate} 
		and collectorid = $P{accountid} 
		and fundid = $P{fundid} 
	group by refdate, refno, reftype, sortdate 
	union all 
	select v.refdate, v.refno, v.reftype, v.dr, v.cr, v.sortdate  
	from vw_cashbook_remittance v, remittance r  
	where v.refdate >= $P{fromdate} 
		and v.refdate <  $P{todate} 
		and v.collectorid = $P{accountid} 
		and v.fundid = $P{fundid} 
		and r.objid = v.objid 
		and r.liquidatingofficer_objid is not null 
)t1 
order by sortdate 


[getSummaries]
select refdate, particulars, refno, dr, cr 
from ( 
	select 
		c.refdate, c.controlid, c.formno, min(c.series) as minseries, max(c.series) as maxseries, 
		(case when c.formno = '51' then 'VARIOUS TAXES AND FEES' else af.title end) as particulars, 
		concat('AF ',c.formno,'#',min(c.refno),'-',max(c.refno)) as refno, 
		sum(c.dr) as dr, 0.0 as cr, min(c.sortdate) as sortdate 
	from vw_cashbook_cashreceipt c  
		inner join af on af.objid = c.formno 
	where c.refdate >= $P{fromdate} 
		and c.refdate <  $P{todate} 
		and c.collectorid = $P{accountid} 
		and c.fundid = $P{fundid} 
		and af.formtype = 'serial' 
	group by 
		c.refdate, c.formno, c.controlid, 
		(case when c.formno = '51' then 'VARIOUS TAXES AND FEES' else af.title end) 

	union all 

	select 
		c.refdate, c.objid as controlid, c.formno, min(c.series) as minseries, min(c.series) as maxseries, 
		min((case when c.formno = '51' then 'VARIOUS TAXES AND FEES' else af.title end)) as particulars,
		min(concat('*** VOIDED - AF ',c.formno,'#',c.refno,' ***')) as refno, sum(c.dr) as dr, sum(c.cr) as cr, 
		min(c.sortdate) as sortdate 
	from vw_cashbook_cashreceiptvoid c 
		inner join af on af.objid = c.formno 
	where c.refdate >= $P{fromdate} 
		and c.refdate <  $P{todate} 
		and c.collectorid = $P{accountid} 
		and c.fundid = $P{fundid} 
		and af.formtype = 'serial' 
	group by c.refdate, c.objid, c.formno, c.fundid  	

	union all 

	select 
		c.refdate, null as controlid, c.formno, null as minseries, null as maxseries, 
		af.title as particulars, c.formno as refno, sum(c.dr) as dr, 0.0 as cr, 
		min(c.sortdate) as sortdate 
	from vw_cashbook_cashreceipt c 
		inner join af on af.objid = c.formno 
	where c.refdate >= $P{fromdate} 
		and c.refdate <  $P{todate} 
		and c.collectorid = $P{accountid} 
		and c.fundid = $P{fundid} 
		and af.formtype = 'cashticket' 
	group by c.refdate, c.formno, af.title 

	union all 

	select 
		v.refdate, null as controlid, v.reftype as formno, null as minseries, null as maxseries, 
		concat('REMITTANCE - ', r.liquidatingofficer_name) as particulars, v.refno, 
		v.dr, v.cr, v.sortdate 
	from vw_cashbook_remittance v, remittance r 
	where v.refdate >= $P{fromdate} 
		and v.refdate <  $P{todate} 
		and v.collectorid = $P{accountid } 
		and v.fundid = $P{fundid} 
		and r.objid = v.objid 
		and r.liquidatingofficer_objid is not null 
)t1 
order by refdate, sortdate 
