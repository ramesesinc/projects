[getRemittanceFunds]
select fund.objid, fund.code, fund.title 
from vw_remittance_cashreceiptitem ci, fund 
where ci.remittanceid = $P{refid}  
	and fund.objid = ci.fundid 
group by fund.objid, fund.code, fund.title


[getLiquidationFunds]
select fund.objid, fund.code, fund.title 
from vw_remittance_cashreceiptitem ci, fund 
where ci.collectionvoucherid = $P{refid}  
	and fund.objid = ci.fundid 
group by fund.objid, fund.code, fund.title


[getAccounts]
select 
	upper(a.code) as acctcode, upper(a.title) as accttitle, 
	a.[level], a.leftindex, a.objid, a.groupid, a.type, 
	0.0 as amount 
from account a 
where a.maingroupid = $P{maingroupid} 
order by a.[level], a.leftindex  


[getRemittanceReport]
select t1.*, m.objid as acctid 
from ( 
	select 
		ci.fundid, ci.acctid as itemid, 
		ci.acctcode as itemcode, ci.acctname as itemtitle, 
		sum(ci.amount) as amount 
	from vw_remittance_cashreceiptitem ci 
	where ci.remittanceid = $P{refid} ${filters} 
		and ci.voided = 0 
	group by ci.fundid, ci.acctid, ci.acctcode, ci.acctname 
)t1 
	left join vw_account_mapping m on (m.itemid = t1.itemid and m.maingroupid = $P{maingroupid}) 
order by m.objid, t1.itemcode, t1.itemtitle 


[getLiquidationReport]
select t1.*, m.objid as acctid 
from ( 
	select 
		ci.fundid, ci.acctid as itemid, 
		ci.acctcode as itemcode, ci.acctname as itemtitle, 
		sum(ci.amount) as amount 
	from vw_remittance_cashreceiptitem ci 
	where ci.collectionvoucherid = $P{refid} ${filters} 
		and ci.voided = 0 
	group by ci.fundid, ci.acctid, ci.acctcode, ci.acctname 
)t1 
	left join vw_account_mapping m on (m.itemid = t1.itemid and m.maingroupid = $P{maingroupid}) 
order by m.objid, t1.itemcode, t1.itemtitle 

