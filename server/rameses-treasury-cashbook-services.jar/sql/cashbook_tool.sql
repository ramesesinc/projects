[removeCashBookEntry]
delete from cashbook_entry 
where objid=$P{objid} 

[getEntriesByRef]
select * from cashbook_entry 
where refid=$P{refid} 

[findRunningBalance]
select sum(e.dr - e.cr) as balance 
from cashbook_entry e 
where e.parentid=$P{cashbookid} 
	and e.lineno >= $P{startlineno} 
	and e.lineno < $P{endlineno} 

[getCashBookEntries]
select * from cashbook_entry e 
where e.parentid=$P{cashbookid} 
	and e.lineno > $P{lineno}   
order by e.lineno 

[updateCashBookEntryBalance]
update cashbook_entry set 
	runbalance = $P{runbalance}, 
	lineno = $P{lineno}  
where 
	objid=$P{objid}

[updateCashBookBalance]
update cashbook a, ( 
	select 
		sum(dr) as dr, sum(cr) as cr, 
		sum(dr-cr) as balance, 
		max(lineno) as maxlineno 
	from cashbook_entry 
	where parentid=$P{cashbookid} 
)b set 
	a.totaldr = b.dr,
	a.totalcr = b.cr,
	a.endbalance = b.balance, 
	a.currentlineno = b.maxlineno 
where a.objid=$P{cashbookid} 
