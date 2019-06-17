[findBeginBalance]
select sum(dr) as dr, sum(cr) as cr, sum(dr)-sum(cr) as balance 
from ( 
	select sum(rf.amount) as dr, 0.0 as cr 
	from remittance r 
		inner join remittance_fund rf on rf.remittanceid = r.objid 
	where r.controldate < $P{startdate} 
		and r.liquidatingofficer_objid = $P{accountid} 
		and rf.fund_objid = $P{fundid} 
	union all 
	select 0.0 as dr, sum(cvf.amount) as cr 
	from collectionvoucher cv 
		inner join collectionvoucher_fund cvf on cvf.parentid = cv.objid 
	where cv.controldate < $P{startdate} 
		and cv.liquidatingofficer_objid = $P{accountid} 
		and cvf.fund_objid = $P{fundid} 
)t1 


[getDetails]
select * 
from ( 
	select 
		r.controldate as refdate, r.collector_objid as userid, r.collector_name as username, 
		r.controlno as refno, 'remittance' as reftype, rf.fund_objid as fundid, 
		rf.amount as dr, 0.0 as cr, rf.amount, r.dtposted as sortdate, 0 as idx   
	from remittance r 
		inner join remittance_fund rf on rf.remittanceid = r.objid 
	where r.controldate >= $P{startdate} 
		and r.controldate <  $P{enddate} 
		and r.liquidatingofficer_objid = $P{accountid} 
		and rf.fund_objid = $P{fundid} 
	union all 
	select 
		cv.controldate as refdate, cv.liquidatingofficer_objid as userid, cv.liquidatingofficer_name as username, 
		cv.controlno as refno, 'liquidation' as reftype, cvf.fund_objid as fundid, 
		0.0 as dr, cvf.amount as cr, cvf.amount, cv.dtposted as sortdate, 1 as idx  
	from collectionvoucher cv 
		inner join collectionvoucher_fund cvf on cvf.parentid = cv.objid 
	where cv.controldate >= $P{startdate} 
		and cv.controldate <  $P{enddate} 
		and cv.liquidatingofficer_objid = $P{accountid} 
		and cvf.fund_objid = $P{fundid} 
)t1 
order by refdate, idx, sortdate 
