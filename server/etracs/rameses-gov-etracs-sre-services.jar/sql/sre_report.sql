[getAcctGroups]
SELECT DISTINCT ia.type AS acctgroup FROM itemaccount ia 


[getSreAccounts]
SELECT 
	a.objid, a.type, a.code AS account_code, a.title AS account_title, 
	CASE WHEN a.parentid IS NULL THEN 'ROOT' ELSE a.parentid END AS parentid 
FROM sreaccount a 
	LEFT JOIN sre_revenue_mapping rm ON a.objid=rm.acctid 
	LEFT JOIN itemaccount ia ON rm.revenueitemid=ia.objid 
GROUP BY a.objid, a.type, a.code, a.title, a.parentid 
ORDER BY a.parentid, a.code 


[getSreAccountsByFund]
select distinct 
	a.objid, a.parentid, a.code  
from (
	select objid from fund where objid LIKE $P{fundid} 
	union 
	select objid from fund where parentid LIKE $P{fundid} 
)xf 
	inner join itemaccount ia on xf.objid=ia.fund_objid 
	inner join sre_revenue_mapping rm on ia.objid=rm.revenueitemid 
	inner join sreaccount a on rm.acctid=a.objid 
where ia.type LIKE $P{acctgroup} 
order by a.parentid, a.code 


[getIncomeSummary]
select 
	x.objid, x.type, x.title, sum(x.amount) as amount 
from ( 
	select 
		a.objid, a.type, a.title, 
		sum(amount) as amount, 
		rm.revenueitemid  
	from income_summary inc 
		inner join sre_revenue_mapping rm on inc.acctid=rm.revenueitemid 
		inner join sreaccount a on rm.acctid=a.objid 
	where inc.refdate between $P{startdate} AND $P{enddate} 
		and inc.fundid in (
			select objid from fund where objid LIKE $P{fundid} 
			union 
			select objid from fund where parentid LIKE $P{fundid} 
		) 
	group by a.objid, a.type, a.title, rm.revenueitemid  
)x 
	inner join itemaccount ia on x.revenueitemid=ia.objid
where ia.type LIKE $P{acctgroup} 
group by x.objid, x.type, x.title 


[getIncomeTargets]
select 
	a.objid, a.type, a.title, a.parentid, t.target    
from sreaccount_incometarget t 
	inner join sreaccount a on t.objid=a.objid 
	inner join sreaccount p on a.parentid=p.objid 
where t.year=$P{year} and t.target is not null 


[getIncomeSummaryByItemAccounts]
select x.*, ia.code, ia.title  
from ( 
	select 
		a.parentid, sum(amount) as amount, rm.revenueitemid  
	from income_summary inc 
		inner join sre_revenue_mapping rm on inc.acctid=rm.revenueitemid 
		inner join sreaccount a on rm.acctid=a.objid 
	where inc.refdate between $P{startdate} AND $P{enddate}  
		and inc.fundid in (
			select objid from fund where objid LIKE $P{fundid} 
			union 
			select objid from fund where parentid LIKE $P{fundid} 
		) 
	group by a.parentid, rm.revenueitemid  
)x 
	inner join itemaccount ia on x.revenueitemid=ia.objid
where ia.type LIKE $P{acctgroup}  
order by x.parentid, ia.code 
