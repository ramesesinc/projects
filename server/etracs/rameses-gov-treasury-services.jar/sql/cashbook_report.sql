[xgetReport]
select 
	ce.refdate, ce.refno, ce.reftype, ce.dr, ce.cr, ce.runbalance 
from cashbook c 
	inner join cashbook_entry ce on ce.parentid = c.objid
where ce.refdate between $P{fromdate} and $P{todate} 
	and c.fund_objid=$P{fundid}	
	and c.subacct_objid = $P{accountid}
order by lineno


[getReport]
select 
	xx.refdate, xx.refno, xx.reftype, 
	xx.dr, xx.cr, xx.runbalance 
from ( 
	select xx.*, 
		(
			select sum(dr) from cashbook_entry 
			where parentid=xx.parentid and lineno between xx.minlineno and xx.maxlineno 
		) as dr, 
		(
			select sum(cr) from cashbook_entry 
			where parentid=xx.parentid and lineno between xx.minlineno and xx.maxlineno 
		) as cr, 
		(
			select runbalance from cashbook_entry 
			where parentid=xx.parentid and lineno=xx.maxlineno 
		) as runbalance 		
	from ( 
		select 
			ce.parentid, c.subacct_objid, c.fund_objid, 
			ce.refdate, ce.refno, ce.reftype, 
			min( ce.lineno ) as minlineno, 
			max( ce.lineno ) as maxlineno 
		from cashbook c 
			inner join cashbook_entry ce on ce.parentid = c.objid
		where ce.refdate between $P{fromdate} and $P{todate} 
			and c.fund_objid = $P{fundid}	
			and c.subacct_objid = $P{accountid}
		group by 	
			ce.parentid, c.subacct_objid, c.fund_objid, 
			ce.refdate, ce.refno, ce.reftype 
	)xx 
)xx 
order by xx.maxlineno 
