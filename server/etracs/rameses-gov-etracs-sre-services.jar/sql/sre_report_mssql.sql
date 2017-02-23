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

[getIncomeTargets]
select 
	a.objid, a.type, a.title, a.parentid, t.target    
from sreaccount_incometarget t 
	inner join sreaccount a on t.objid=a.objid 
	inner join sreaccount p on a.parentid=p.objid 
where t.year=$P{year} and t.target is not null 

[getIncomeSummary]
select 
	a.objid, a.type, a.code as account_code, a.title as account_title, 
	(case when a.parentid is null then 'ROOT' else a.parentid end) as parentid, 
	(select max(t.target) from sreaccount_incometarget t where objid=a.objid and t.year=YEAR($P{startdate})) as target, 
	xxb.amount 
from sreaccount a, ( 
		select objid, sum(amount) as amount 
		from ( 
			select objid, 0.0 as amount from sreaccount 
			union all 
			select a.objid, sum(inc.amount) as amount  
			from income_summary inc 
				inner join sre_revenue_mapping rm on inc.acctid=rm.revenueitemid 
				inner join itemaccount ia on rm.revenueitemid=ia.objid 
				inner join sreaccount a on rm.acctid=a.objid 
			where inc.refdate >= $P{startdate}  
				and inc.refdate < $P{enddate} 
				${filter} 
			group by a.objid 
		)xxa 
		group by objid 
	)xxb 
where a.objid=xxb.objid 
order by a.parentid, a.code 


[getIncomeSummaryByItemAccounts]
select 
	rm.acctid as parentid, rm.revenueitemid, 
	ia.code as account_code, ia.title as account_title, 
	sum(inc.amount) as amount 
from income_summary inc 
	inner join sre_revenue_mapping rm on inc.acctid=rm.revenueitemid 
	inner join itemaccount ia on rm.revenueitemid=ia.objid 
	inner join sreaccount a on rm.acctid=a.objid 
where inc.refdate >= $P{startdate} 
	and inc.refdate < $P{enddate} 
	${filter} 
group by rm.acctid, rm.revenueitemid, ia.code, ia.title 
order by rm.acctid, ia.code 


[getIncomeSummaryByLiquidationDate]
select 
	a.objid, a.type, a.code as account_code, a.title as account_title, 
	(case when a.parentid is null then 'ROOT' else a.parentid end) as parentid, 
	(select max(t.target) from sreaccount_incometarget t where objid=a.objid and t.year=YEAR($P{startdate})) as target, 
	xxb.amount 
from sreaccount a, ( 
		select objid, sum(amount) as amount 
		from ( 
			select objid, 0.0 as amount from sreaccount 
			union all 
			select a.objid, sum(inc.amount) as amount  
			from liquidation liq 
				inner join liquidation_remittance lrem on liq.objid=lrem.liquidationid 
				inner join remittance rem on lrem.objid=rem.objid 
				inner join income_summary inc on rem.objid=inc.refid 
				inner join sre_revenue_mapping rm on inc.acctid=rm.revenueitemid 
				inner join itemaccount ia on rm.revenueitemid=ia.objid 
				inner join sreaccount a on rm.acctid=a.objid 
			where liq.dtposted >= $P{startdate}  
				and liq.dtposted < $P{enddate} 
				${filter} 
			group by a.objid 
		)xxa 
		group by objid 
	)xxb 
where a.objid=xxb.objid 
order by a.parentid, a.code 


[getIncomeSummaryByItemAccountsByLiquidationDate]
select 
	rm.acctid as parentid, rm.revenueitemid, 
	ia.code as account_code, ia.title as account_title, 
	sum(inc.amount) as amount 
from liquidation liq 
	inner join liquidation_remittance lrem on liq.objid=lrem.liquidationid 
	inner join remittance rem on lrem.objid=rem.objid 
	inner join income_summary inc on rem.objid=inc.refid 
	inner join sre_revenue_mapping rm on inc.acctid=rm.revenueitemid 
	inner join itemaccount ia on rm.revenueitemid=ia.objid 
	inner join sreaccount a on rm.acctid=a.objid 
where liq.dtposted >= $P{startdate} 
	and liq.dtposted < $P{enddate} 
	${filter} 
group by rm.acctid, rm.revenueitemid, ia.code, ia.title 
order by rm.acctid, ia.code 
