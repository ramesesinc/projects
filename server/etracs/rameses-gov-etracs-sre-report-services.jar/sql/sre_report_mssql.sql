[getFunds]
select f.*, g.indexno as groupindexno 
from fund f, fundgroup g  
where f.groupid = g.objid 
order by g.indexno, g.title, f.code, f.title 

[getFundsByGroup]
select * 
from fund 
where groupid = $P{groupid} 
order by code, title 

[getAcctGroups]
select g.* 
from account_maingroup g 
where g.reporttype='SRE' 
order by g.version desc 

[getAcctTypes]
select distinct 
	ia.type as objid, ia.type as title 
from itemaccount ia 
where ia.type NOT IN ('CASH_IN_BANK','CASH_IN_TREASURY') 


[getIncomeSummary]
select 
	t1.objid, t1.code as account_code, t1.title as account_title, 
	t1.type, t1.leftindex, t1.rightindex, t1.level, 
	t1.maingroupid, sum(t1.amount) as amount, sum(t1.target) as target  
from ( 
	select 
		a.objid, a.code, a.title, a.type, 
		a.leftindex, a.rightindex, a.level, a.maingroupid, 
		sum(a.amount) as amount, null as target
	from vw_account_income_summary a 
	where a.maingroupid = $P{maingroupid} 
		and a.remittancedate >= $P{startdate} 
		and a.remittancedate <  $P{enddate} 
		${filter} 
	group by 
		a.objid, a.code, a.title, a.type, 
		a.leftindex, a.rightindex, a.level, 
		a.maingroupid

	union all 

	select 
		p.objid, p.code, p.title, p.type, 
		p.leftindex, p.rightindex, p.level, 
		p.maingroupid, 0.0 as amount, null as target  
	from account p 
		inner join ( 
			select a.maingroupid, max(a.leftindex) as leftindex 
			from vw_account_income_summary a 
			where a.maingroupid = $P{maingroupid} 
				and a.remittancedate >= $P{startdate} 
				and a.remittancedate <  $P{enddate} 
				${filter} 
			group by a.maingroupid 
		)m on p.maingroupid = m.maingroupid 
	where p.leftindex < m.leftindex 

	union all 

	select 
		ia.objid, ia.code, ia.title, 'itemaccount' as type, 
		a.leftindex, a.rightindex, (a.level+1) as [level], 
		a.maingroupid, sum(a.amount) as amount, null as target 
	from vw_account_income_summary a 
		inner join itemaccount ia on ia.objid = a.acctid 
	where a.maingroupid = $P{maingroupid} 
		and a.remittancedate >= $P{startdate} 
		and a.remittancedate <  $P{enddate} 
		and 'itemaccount' = $P{type} 
		${filter} 
	group by 
		ia.objid, ia.code, ia.title, 
		a.leftindex, a.rightindex, (a.level+1), 
		a.maingroupid 	

	union all 

	select 
		a.objid, a.code, a.title, a.type, 
		a.leftindex, a.rightindex, a.level, 
		a.maingroupid, null as amount, sum(ait.target) as target 
	from account a 
		inner join account_item_mapping aim on aim.acctid = a.objid 
		inner join account_incometarget ait on ait.itemid = aim.itemid 
	where a.maingroupid = $P{maingroupid} 
		and ait.year = YEAR($P{startdate}) 
	group by 
		a.objid, a.code, a.title, a.type, 
		a.leftindex, a.rightindex, a.level, 
		a.maingroupid 		
)t1 
group by 
	t1.objid, t1.code, t1.title, t1.type, 
	t1.leftindex, t1.rightindex, t1.level, 
	t1.maingroupid 
order by t1.leftindex, t1.level, t1.code  


[getIncomeSummaryByLiquidationDate]
select 
	t1.objid, t1.code as account_code, t1.title as account_title, 
	t1.type, t1.leftindex, t1.rightindex, t1.level, 
	t1.maingroupid, sum(t1.amount) as amount, sum(t1.target) as target  
from ( 
	select 
		a.objid, a.code, a.title, a.type, 
		a.leftindex, a.rightindex, a.level, a.maingroupid, 
		sum(a.amount) as amount, null as target
	from vw_account_income_summary a 
	where a.maingroupid = $P{maingroupid} 
		and a.refdate >= $P{startdate} 
		and a.refdate <  $P{enddate} 
		${filter} 
	group by 
		a.objid, a.code, a.title, a.type, 
		a.leftindex, a.rightindex, a.level, 
		a.maingroupid

	union all 

	select 
		p.objid, p.code, p.title, p.type, 
		p.leftindex, p.rightindex, p.level, 
		p.maingroupid, 0.0 as amount, null as target  
	from account p 
		inner join ( 
			select a.maingroupid, max(a.leftindex) as leftindex 
			from vw_account_income_summary a 
			where a.maingroupid = $P{maingroupid} 
				and a.refdate >= $P{startdate} 
				and a.refdate <  $P{enddate} 
				${filter} 
			group by a.maingroupid 
		)m on p.maingroupid = m.maingroupid 
	where p.leftindex < m.leftindex 

	union all 

	select 
		ia.objid, ia.code, ia.title, 'itemaccount' as type, 
		a.leftindex, a.rightindex, (a.level+1) as [level], 
		a.maingroupid, sum(a.amount) as amount, null as target 
	from vw_account_income_summary a 
		inner join itemaccount ia on ia.objid = a.acctid 
	where a.maingroupid = $P{maingroupid} 
		and a.refdate >= $P{startdate} 
		and a.refdate <  $P{enddate} 
		and 'itemaccount' = $P{type} 
		${filter} 
	group by 
		ia.objid, ia.code, ia.title, 
		a.leftindex, a.rightindex, (a.level+1), 
		a.maingroupid 	

	union all 

	select 
		a.objid, a.code, a.title, a.type, 
		a.leftindex, a.rightindex, a.level, 
		a.maingroupid, null as amount, sum(ait.target) as target 
	from account a 
		inner join account_item_mapping aim on aim.acctid = a.objid 
		inner join account_incometarget ait on ait.itemid = aim.itemid 
	where a.maingroupid = $P{maingroupid} 
		and ait.year = YEAR($P{startdate}) 
	group by 
		a.objid, a.code, a.title, a.type, 
		a.leftindex, a.rightindex, a.level, 
		a.maingroupid 		
)t1 
group by 
	t1.objid, t1.code, t1.title, t1.type, 
	t1.leftindex, t1.rightindex, t1.level, 
	t1.maingroupid 
order by t1.leftindex, t1.level, t1.code  
