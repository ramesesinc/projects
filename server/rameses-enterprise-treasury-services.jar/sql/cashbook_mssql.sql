[getList]
select c.* from ( 
	select objid as xid from cashbook where code like $P{searchtext} 
	union 
	select objid as xid from cashbook where subacct_name like $P{searchtext} 
	union 
	select objid as xid from cashbook where fund_objid in (select objid from fund where title like $P{searchtext}) 
)xx inner join cashbook c on xx.xid=c.objid 
order by subacct_name, fund_code 

[getListBySubacct]
select c.* from ( 
	select objid as xid from cashbook 
	where subacct_objid=$P{subacctid} and code like $P{searchtext} 
	union 
	select objid as xid from cashbook 
	where subacct_objid=$P{subacctid} 
		and fund_objid in (select objid from fund where title like $P{searchtext}) 
)xx inner join cashbook c on xx.xid=c.objid 
order by subacct_name, fund_code 

[approve]
UPDATE cashbook SET state='APPROVED' WHERE objid=$P{objid}

[findBySubAcctFund] 
SELECT * FROM cashbook WHERE fund_objid=$P{fundid} AND subacct_objid=$P{subacctid} AND type = $P{type} 

[getEntries]
SELECT refdate,refno,reftype,particulars,dr,cr,runbalance,[lineno] 
FROM cashbook_entry 
WHERE parentid=$P{objid} 
order by [lineno] 
