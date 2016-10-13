[removeCashBookEntry]
delete from cashbook_entry 
where objid=$P{objid} 

[getEntriesByRef]
select cbe.*, 
from cashbook_entry cbe  
	inner join cashbook cb on cbe.parentid=cb.objid 
where cbe.refid=$P{refid} 

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
	a.currentlineno = b.maxlineno+1 
where a.objid=$P{cashbookid} 


[getEntriesFromIndex]
select * 
from cashbook_entry 
where parentid=$P{cashbookid} 
	and lineno>=$P{indexno}   
order by lineno  
