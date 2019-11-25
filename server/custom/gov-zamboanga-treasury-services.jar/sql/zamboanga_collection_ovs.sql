[getViolators]
select distinct Payee as violator
from dbo.AF51
where KindOfSystem = 'ORDINANCE'
and Payee like $P{violator}
order by Payee 


[getViolations]
select 
	x.ordinance,
	count(distinct or_id) as numoffense 
from (		
	select distinct 
		a.or_id,
		o.Description as ordinance 
	from dbo.AF51 a 
	inner join cto_bpls_user.AF51_Ordinance o on a.or_id = o.or_id 
	where a.KindOfSystem = 'ORDINANCE'
	and a.orstatus = '1'
	and a.Payee = $P{violator}
) x
group by x.ordinance 


[getApprehendingOffices]
select 
	apprehendingoffice
from dbo.ApprehendingOffice
order by ApprehendingOffice

[findAccount]
select * from dbo.zview_accounts where refid = $P{objid}