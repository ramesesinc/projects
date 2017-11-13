[getReport]
select * 
from ( 
	select 
		convert(l.dtposted, date) as refdate, l.liquidatingofficer_name as username, 
		l.txnno as refno, 'liquidation' as reftype, 0.0 as dr, lf.amount as cr, 
		(lf.amount * -1.0) as amount, 1 as sortindex 
	from liquidation l 
		inner join liquidation_cashier_fund lf on lf.liquidationid = l.objid 
	where l.liquidatingofficer_objid = $P{accountid}  
		and l.dtposted >= $P{startdate}  
		and l.dtposted < $P{enddate} 
		and lf.fund_objid = $P{fundid}  
	
	union all 

	select 
		convert(l.dtposted, date) as refdate, rem.collector_name as username, 
		rem.txnno as refno, 'remittance' as reftype, remf.amount as dr, 0.0 as cr, 
		remf.amount as amount, 0 as sortindex 
	from liquidation l 
		inner join liquidation_remittance lrem on lrem.liquidationid = l.objid 
		inner join remittance rem on rem.objid = lrem.objid 
		inner join remittance_fund remf on remf.remittanceid = rem.objid 
	where l.liquidatingofficer_objid = $P{accountid}  
		and l.dtposted >= $P{startdate}  
		and l.dtposted < $P{enddate} 
		and remf.fund_objid = $P{fundid}  
)tmp1 
order by refdate, sortindex, username, refno  
