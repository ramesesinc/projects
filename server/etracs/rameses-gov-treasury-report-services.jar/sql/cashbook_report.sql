[findBeginBalance]
select sum(dr) as dr, sum(cr) as cr, sum(dr)-sum(cr) as balance 
from ( 
	select sum(dr) as dr, sum(cr) as cr 
	from vw_cashbook_cashreceipt 
	where refdate < $P{fromdate} 
		and collectorid = $P{accountid} 
		and fundid in (${fundfilter}) 
	union all 
	select sum(dr) as dr, sum(cr) as cr 
	from vw_cashbook_cashreceiptvoid 
	where refdate < $P{fromdate} 
		and collectorid = $P{accountid} 
		and fundid in (${fundfilter})
	union all 
	select sum(v.dr) as dr, sum(v.cr) as cr  
	from vw_cashbook_remittance v, remittance r  
	where v.refdate < $P{fromdate} 
		and v.collectorid = $P{accountid} 
		and v.fundid in (${fundfilter})
		and v.objid = r.objid 
		and r.liquidatingofficer_objid is not null 
)t1 

[findBeginBalance2]
select sum(dr) as dr, sum(cr) as cr, sum(dr)-sum(cr) as balance 
from ( 
	select sum(dr) as dr, sum(cr) as cr 
	from vw_cashbook_cashreceipt 
	where refdate >= $P{fromdate} 
		and refdate < $P{todate} 
		and collectorid = $P{accountid} 
		and fundid in (${fundfilter})
	union all 
	select sum(dr) as dr, sum(cr) as cr 
	from vw_cashbook_cashreceiptvoid 
	where refdate >= $P{fromdate} 
		and refdate < $P{todate} 
		and collectorid = $P{accountid} 
		and fundid in (${fundfilter})
	union all 
	select sum(v.dr) as dr, sum(v.cr) as cr  
	from vw_cashbook_remittance v, remittance r  
	where v.refdate >= $P{fromdate} 
		and v.refdate < $P{todate} 
		and v.collectorid = $P{accountid} 
		and v.fundid in (${fundfilter})
		and v.objid = r.objid 
		and r.liquidatingofficer_objid is not null 
)t1 

[findRevolvingFund]
select 
	year(controldate) as controlyear, 
	month(controldate) as controlmonth, 
	sum(amount) as amount, 
	((year(controldate)*12) + month(controldate)) as indexno 
from cashbook_revolving_fund 
where issueto_objid = $P{accountid} 
	and controldate <= $P{fromdate} 
	and fund_objid in (${fundfilter})
	and state = 'POSTED' 
group by year(controldate), month(controldate), ((year(controldate)*12) + month(controldate)) 
order by ((year(controldate)*12) + month(controldate)) desc 

[getDetails]
select * 
from ( 
	select refdate, refno, reftype, sum(dr) as dr, sum(cr) as cr, sortdate 
	from vw_cashbook_cashreceipt 
	where refdate >= $P{fromdate} 
		and refdate <  $P{todate} 
		and collectorid = $P{accountid} 
		and fundid in (${fundfilter})
	group by refdate, refno, reftype, sortdate 
	union all 
	select refdate, refno, reftype, sum(dr) as dr, sum(cr) as cr, sortdate 
	from vw_cashbook_cashreceiptvoid 
	where refdate >= $P{fromdate} 
		and refdate <  $P{todate} 
		and collectorid = $P{accountid} 
		and fundid in (${fundfilter})
	group by refdate, refno, reftype, sortdate 
	union all 
	select v.refdate, v.refno, v.reftype, v.dr, v.cr, v.sortdate  
	from vw_cashbook_remittance v, remittance r  
	where v.refdate >= $P{fromdate} 
		and v.refdate <  $P{todate} 
		and v.collectorid = $P{accountid} 
		and v.fundid in (${fundfilter})
		and r.objid = v.objid 
		and r.liquidatingofficer_objid is not null 
)t1 
order by sortdate, refdate 


[getSummaries]
select refdate, particulars, refno, dr, cr 
from ( 
	select 
		refdate, controlid, formno, min(series) as minseries, max(series) as maxseries, 
		(case when formno = '51' then 'VARIOUS TAXES AND FEES' else af_title end) as particulars, 
		concat('AF ', formno, '#', min(refno), '-', max(refno)) as refno, 
		sum(dr) as dr, 0.0 as cr, min(sortdate) as sortdate 
	from ( 
		select 
			c.refdate, c.controlid, c.formno, c.series, 
			af.title as af_title, c.refno, c.dr, c.cr, c.sortdate 
		from vw_cashbook_cashreceipt c  
			inner join af on af.objid = c.formno 
		where c.refdate >= $P{fromdate} 
			and c.refdate <  $P{todate} 
			and c.collectorid = $P{accountid} 
			and c.fundid in (${fundfilter})
			and af.formtype = 'serial' 

		union all 

		select 
			t1.refdate, t1.controlid, t1.formno, t1.series, 
			t1.af_title, t1.refno, -cs.dr as dr, 0.0 as cr, t1.sortdate 
		from ( 
			select 
				c.receiptid, c.controlid, c.formno, c.series, 
				af.title as af_title, c.refdate, c.refno, c.sortdate 
			from vw_cashbook_cashreceipt c  
				inner join af on af.objid = c.formno 
			where c.refdate >= $P{fromdate} 
				and c.refdate <  $P{todate} 
				and c.collectorid = $P{accountid} 
				and c.fundid in (${fundfilter}) 
				and af.formtype = 'serial' 
			group by 
				c.receiptid, c.controlid, c.formno, c.series, 
				af.title, c.refdate, c.refno, c.sortdate 
		)t1, vw_cashbook_cashreceipt_share cs 
		where cs.receiptid = t1.receiptid 

		union all 

		select 
			c.refdate, c.controlid, c.formno, c.series, 
			af.title as af_title, c.refno, c.dr, 0.0 as cr, c.sortdate 
		from vw_cashbook_cashreceipt_share c  
			inner join af on af.objid = c.formno 
		where c.refdate >= $P{fromdate} 
			and c.refdate <  $P{todate} 
			and c.collectorid = $P{accountid} 
			and c.fundid in (${fundfilter}) 
			and af.formtype = 'serial' 
	)t0 
	group by refdate, formno, controlid, 
		(case when formno = '51' then 'VARIOUS TAXES AND FEES' else af_title end) 

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
		and c.fundid in (${fundfilter})
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
		and c.fundid in (${fundfilter})
		and af.formtype = 'cashticket' 
	group by c.refdate, c.formno, af.title 

	union all 

	select 
		r.controldate as refdate, null as controlid, 
		'remittance' as reftype, null as minseries, null as maxseries, 
		concat('REMITTANCE - ', r.liquidatingofficer_name) as particulars, 
		r.controlno as refno, 0.0 as dr, sum(t2.amount)-sum(t2.share) as cr, 
		r.dtposted as sortdate 
	from ( 
	  select v.remittanceid, v.fundid, sum(v.amount) as amount, 0.0 as share   
	  from vw_remittance_cashreceiptitem v 
	  where v.remittance_controldate >= $P{fromdate} 
			and v.remittance_controldate <  $P{todate} 
			and v.collectorid = $P{accountid} 
		group by v.remittanceid, v.fundid 
		union all 
		select cs.remittanceid, t1.fundid, 0.0 as amount, sum(cs.amount) as share 
		from ( 
			select receiptid, fundid, acctid 
			from vw_remittance_cashreceiptitem v 
			where v.remittance_controldate >= $P{fromdate} 
				and v.remittance_controldate <  $P{todate} 
				and v.collectorid = $P{accountid} 
			group by receiptid, fundid, acctid
		)t1, vw_remittance_cashreceiptshare cs 
		where cs.receiptid = t1.receiptid 
			and cs.refacctid = t1.acctid 
		group by cs.remittanceid, t1.fundid 
		union all 
	  select remittanceid, fundid, sum(amount) as amount, 0.0 as share  
	  from vw_remittance_cashreceiptshare v 
	  where v.remittance_controldate >= $P{fromdate} 
			and v.remittance_controldate <  $P{todate} 
			and v.collectorid = $P{accountid} 
	  group by remittanceid, fundid 
	)t2, remittance r, fund  
	where r.objid = t2.remittanceid 
		and fund.objid = t2.fundid 
		and fund.objid in (${fundfilter}) 
	group by 
		r.controldate, r.controlno, r.dtposted, r.liquidatingofficer_name
 
)t11  
order by refdate, sortdate 
