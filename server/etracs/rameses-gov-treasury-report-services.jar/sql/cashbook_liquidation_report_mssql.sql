[getReport]
select * 
from ( 
	select 
		convert(DATE, cv.controldate) as refdate, cv.liquidatingofficer_name as username, 
		cv.controlno as refno, 'liquidation' as reftype, 0.0 as dr, cvf.amount as cr, 
		(cvf.amount * -1.0) as amount, 1 as sortindex 
	from collectionvoucher cv  
		inner join collectionvoucher_fund cvf on cvf.parentid = cv.objid 
	where cv.liquidatingofficer_objid = $P{accountid}  
		and cv.controldate >= $P{startdate} 
		and cv.controldate <  $P{enddate} 
		and cvf.fund_objid = $P{fundid} 
	
	union all 

	select 
		convert(DATE, cv.controldate) as refdate, rem.collector_name as username, 
		rem.controlno as refno, 'remittance' as reftype, remf.amount as dr, 0.0 as cr, 
		remf.amount as amount, 0 as sortindex 
	from collectionvoucher cv 
		inner join remittance rem on rem.collectionvoucherid = cv.objid 
		inner join remittance_fund remf on remf.remittanceid = rem.objid 
	where cv.liquidatingofficer_objid = $P{accountid}  
		and cv.controldate >= $P{startdate}  
		and cv.controldate <  $P{enddate} 
		and remf.fund_objid = $P{fundid}  
)tmp1 
order by refdate, sortindex, username, refno  
