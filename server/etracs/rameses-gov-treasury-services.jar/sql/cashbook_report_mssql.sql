[getReport]
select 
	ce.refdate, ce.refno, ce.reftype, ce.dr, ce.cr, ce.runbalance 
from cashbook c 
	inner join cashbook_entry ce on ce.parentid = c.objid
where ce.refdate between $P{fromdate} and $P{todate} 
	and c.fund_objid=$P{fundid}	
	and c.subacct_objid = $P{accountid}
order by [lineno] 
