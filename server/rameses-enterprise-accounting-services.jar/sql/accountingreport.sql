[getAccountSummary]
select tmp1.*, inc.amount 
from ( 
	select 
		objid, maingroupid, code as acctcode, title as accttitle, 'group' as type, groupid as parentid 
	from account_group 
	where maingroupid=$P{maingroupid} 
	union all 
	select 
		objid, maingroupid, code as acctcode, title as accttitle, 'detail' as type, groupid as parentid  
	from account 
	where maingroupid=$P{maingroupid} and acctid is null 
	union all 
	select 
		objid, maingroupid, code as acctcode, title as accttitle, 'subaccount' as type, acctid as parentid  
	from account 
	where maingroupid=$P{maingroupid} and acctid is not null 
)tmp1
	left join ( 
		select m.acctid, sum(inc.amount) as amount 
		from account_item_mapping m 
			inner join income_summary inc on m.itemid=inc.acctid 
		where m.maingroupid=$P{maingroupid} ${filter} 
		group by acctid 
	)inc on inc.acctid=tmp1.objid 
order by tmp1.parentid, tmp1.acctcode 


[getItemAccountSummary]
select 
	m.itemid as objid, a.maingroupid, ia.code as acctcode, ia.title as accttitle, 
	'itemaccount' as type, m.acctid as parentid, sum(inc.amount) as amount  
from account_item_mapping m 
	inner join income_summary inc on m.itemid=inc.acctid 
	inner join itemaccount ia on ia.objid=m.itemid 
	inner join account a on a.objid=m.acctid 
where m.maingroupid = $P{maingroupid} 
	and a.maingroupid = m.maingroupid 
	${filter} 
group by 
	m.itemid, a.maingroupid, ia.code, ia.title, m.acctid 
order by m.acctid, ia.code 
