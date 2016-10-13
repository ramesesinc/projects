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
	and e.[lineno] >= $P{startlineno} 
	and e.[lineno] < $P{endlineno} 

[getCashBookEntries]
select * from cashbook_entry e 
where e.parentid=$P{cashbookid} 
	and e.[lineno] > $P{lineno}   
order by e.[lineno] 

[updateCashBookEntryBalance]
update ce set 
	ce.runbalance = $P{runbalance}, 
	ce.[lineno] = $P{lineno}  
from cashbook_entry ce 
where ce.objid=$P{objid}

[updateCashBookBalance]
update a set 
	a.totaldr = b.dr,
	a.totalcr = b.cr,
	a.endbalance = b.balance, 
	a.currentlineno = b.maxlineno+1
from cashbook a, ( 
	select 
		parentid, 
		sum(dr) as dr, sum(cr) as cr, 
		sum(dr-cr) as balance, 
		max([lineno]) as maxlineno 
	from cashbook_entry 
	where parentid=$P{cashbookid} 
	group by parentid 
)b  
where a.objid=b.parentid 


[getEntriesFromIndex]
select * 
from cashbook_entry 
where parentid = $P{cashbookid} 
	and [lineno] >= $P{indexno}   
order by [lineno]  
