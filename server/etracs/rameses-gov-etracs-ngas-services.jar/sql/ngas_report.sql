[getAcctGroups]
select ia.type as acctgroup 
from itemaccount ia 
group by ia.type 

[getUnmappedAccts]
select 
	ia.objid, 'unmapped' as parentid, ia.code, 
	ia.title, 'detail' as type, xx.amount, 
	ia.code as itemacctcode, ia.title as itemaccttitle, 
	ia.type as itemaccttype, f.objid as fundid 
from ( 
	select acctid, sum(amount) as amount   
	from income_summary inc, (select @rownum:=0)rn  
	where refdate >= $P{startdate} 
		and refdate < $P{enddate} 
		and fundid in (
			select objid from fund where objid like $P{fundid} 
			union 
			select objid from fund where parentid like $P{fundid} 
		) 
		and acctid not in (
			select revenueitemid from ngas_revenue_mapping 
			where revenueitemid=inc.acctid
		) 
	group by acctid 
)xx 
	inner join itemaccount ia on xx.acctid = ia.objid 
	inner join fund f on ia.fund_objid = f.objid 
where ia.type like $P{acctgroup} 
order by ia.code, ia.title  


[getUnmappedAcctsByLiquidationDate]
select 
	ia.objid, 'unmapped' as parentid, ia.code, 
	ia.title, 'detail' as type, xx.amount, 
	ia.code as itemacctcode, ia.title as itemaccttitle, 
	ia.type as itemaccttype, f.objid as fundid 
from ( 
	select inc.acctid, sum(inc.amount) as amount 
	from liquidation liq 
		inner join liquidation_remittance lrem on liq.objid=lrem.liquidationid 
		inner join remittance rem on lrem.objid=rem.objid 
		inner join income_summary inc on rem.objid=inc.refid 
	where inc.refdate >= $P{startdate} 
		and inc.refdate < $P{enddate} 
		and inc.fundid in (
			select objid from fund where objid like $P{fundid} 
			union 
			select objid from fund where parentid like $P{fundid} 
		) 
		and inc.acctid not in (
			select revenueitemid from ngas_revenue_mapping 
			where revenueitemid=inc.acctid
		) 
	group by inc.acctid 
)xx 
	inner join itemaccount ia on xx.acctid = ia.objid 
	inner join fund f on ia.fund_objid = f.objid 
where ia.type like $P{acctgroup} 
order by ia.code, ia.title  


[getReportData]
select bt.*, (@rownum:=@rownum+1) as itemindex
from ( 
	select 
		a.objid, ifnull(a.parentid,'root') as parentid, 
		a.code, a.title, a.type, a.acctgroup, xx.actualamount, 
		xx.itemacctcode, xx.itemaccttitle, xx.itemaccttype, 
		case 
			when a.parentid is null then 0 
			when a.type='group' then 1 
			when a.type='detail' then 2 
			else 3 
		end as catindex  
	from ngasaccount a 
		left join ( 
			select 
				nga.objid, sum( xx.amount ) as actualamount, 
				ia.code as itemacctcode, ia.title as itemaccttitle, 
				ia.type as itemaccttype 
			from ( 
				select acctid, sum(amount) as amount 
				from income_summary inc, (select @rownum:=0)rn  
				where refdate >= $P{startdate} 
					and refdate < $P{enddate} 
					and fundid in ( 
						select objid from fund where objid like $P{fundid} 
						union 
						select objid from fund where parentid like $P{fundid} 
					) 
				group by acctid 
			)xx inner join ngas_revenue_mapping rm on xx.acctid=rm.revenueitemid 
				inner join ngasaccount nga on rm.acctid = nga.objid 
				inner join itemaccount ia on rm.revenueitemid = ia.objid 
			where ia.type like $P{acctgroup} 
			group by nga.objid, ia.code, ia.title 
		)xx on a.objid = xx.objid 
)bt 
order by catindex, parentid, code 


[getReportDataByLiquidationDate]
select bt.*, (@rownum:=@rownum+1) as itemindex
from ( 
	select 
		a.objid, ifnull(a.parentid,'root') as parentid, 
		a.code, a.title, a.type, a.acctgroup, xx.actualamount, 
		xx.itemacctcode, xx.itemaccttitle, xx.itemaccttype, 
		case 
			when a.parentid is null then 0 
			when a.type='group' then 1 
			when a.type='detail' then 2 
			else 3 
		end as catindex  
	from ngasaccount a 
		left join ( 
			select 
				nga.objid, sum( xx.amount ) as actualamount, 
				ia.code as itemacctcode, ia.title as itemaccttitle, 
				ia.type as itemaccttype 
			from ( 
				select inc.acctid, sum(inc.amount) as amount 
				from liquidation liq 
					inner join liquidation_remittance lrem on liq.objid=lrem.liquidationid 
					inner join remittance rem on lrem.objid=rem.objid 
					inner join income_summary inc on rem.objid=inc.refid 
					inner join (select @rownum:=0) rn on 1=1   
				where inc.refdate >= $P{startdate} 
					and inc.refdate < $P{enddate} 
					and inc.fundid in ( 
						select objid from fund where objid like $P{fundid} 
						union 
						select objid from fund where parentid like $P{fundid} 
					) 
				group by inc.acctid 
			)xx 
				inner join ngas_revenue_mapping rm on xx.acctid=rm.revenueitemid 
				inner join ngasaccount nga on rm.acctid = nga.objid 
				inner join itemaccount ia on rm.revenueitemid = ia.objid 
			where ia.type like $P{acctgroup} 
			group by nga.objid, ia.code, ia.title 
		)xx on a.objid = xx.objid 
)bt 
order by catindex, parentid, code 


[findRemittance]
select * from remittance where objid = $P{objid} 

[getUnmappedAcctsByRemittance]
select 
	ia.objid, 'unmapped' as parentid, ia.code, 
	ia.title, 'detail' as type, xx.amount, 
	ia.code as itemacctcode, ia.title as itemaccttitle, 
	ia.type as itemaccttype, f.objid as fundid, 
	1 as unmapped  
from ( 
	select xx.* from ( 
		select cri.item_objid as acctid, sum( cri.amount ) as amount  
		from ( 
			select a.*, 
				(select count(*) from cashreceipt_void where receiptid=a.objid) as voided 
			from remittance_cashreceipt a, (select @rownum:=0)rn  
			where remittanceid = $P{remittanceid} 
		)xx  
			inner join cashreceipt cr on xx.objid = cr.objid 
			inner join cashreceiptitem cri on cr.objid = cri.receiptid 
		where xx.voided=0 
		group by cri.item_objid 
	)xx 
	where xx.acctid not in (select revenueitemid from ngas_revenue_mapping where revenueitemid=xx.acctid) 
)xx 	 
	inner join itemaccount ia on xx.acctid = ia.objid 
	inner join fund f on ia.fund_objid = f.objid 
order by ia.code, ia.title 


[getReportDataByRemittance]
select bt.*, (@rownum:=@rownum+1) as itemindex
from ( 
	select 
		a.objid, ifnull(a.parentid,'root') as parentid, 
		a.code, a.title, a.type, a.acctgroup, 
		ifnull(xx.actualamount,0.0) as actualamount, 
		0 as unmapped, 
		case 
			when a.parentid is null then 0 
			when a.type='group' then 1 
			when a.type='detail' then 2 
			else 3 
		end as catindex 
	from ngasaccount a 
		left join ( 
			select nga.objid, sum( xx.amount ) as actualamount  
			from ( 
				select cri.item_objid as acctid, sum( cri.amount ) as amount  
				from ( 
					select a.*, 
						(select count(*) from cashreceipt_void where receiptid=a.objid) as voided 
					from remittance_cashreceipt a, (select @rownum:=0)rn   
					where remittanceid = $P{remittanceid} 
				)xx  
					inner join cashreceipt cr on xx.objid = cr.objid 
					inner join cashreceiptitem cri on cr.objid = cri.receiptid 
				where xx.voided=0 
				group by cri.item_objid  
			)xx 
				inner join ngas_revenue_mapping rm on xx.acctid = rm.revenueitemid 
				inner join ngasaccount nga on rm.acctid = nga.objid 
				inner join itemaccount ia on rm.revenueitemid = ia.objid 
			group by nga.objid 
		)xx on a.objid = xx.objid 
)bt 
order by catindex, parentid, code 


[findLiquidation]
select * from liquidation where objid = $P{objid} 

[getReportDataByLiquidation]
select bt.*, (@rownum:=@rownum+1) as itemindex
from ( 
	select 
		a.objid, ifnull(a.parentid,'root') as parentid, 
		a.code, a.title, a.type, a.acctgroup, 
		ifnull(xx.actualamount,0.0) as actualamount, 
		0 as unmapped, 
		case 
			when a.parentid is null then 0 
			when a.type='group' then 1 
			when a.type='detail' then 2 
			else 3 
		end as catindex 
	from ngasaccount a 
		left join ( 
			select nga.objid, sum( xx.amount ) as actualamount 
			from ( 
				select t1.* from ( 
					select inc.acctid, sum(inc.amount) as amount 
					from liquidation_remittance lr 
						inner join income_summary inc on lr.objid = inc.refid 
					where lr.liquidationid = $P{liquidationid} 
					group by inc.acctid 
				)t1, (select @rownum:=0)rn 
			)xx inner join ngas_revenue_mapping rm on xx.acctid=rm.revenueitemid 
				inner join ngasaccount nga on rm.acctid = nga.objid 
				inner join itemaccount ia on rm.revenueitemid = ia.objid 
			group by nga.objid 
		)xx on a.objid = xx.objid 
)bt 
order by catindex, parentid, code 


[getUnmappedAcctsByLiquidation]
select 
	ia.objid, 'unmapped' as parentid, ia.code, 
	ia.title, 'detail' as type, xx.amount, 
	ia.code as itemacctcode, ia.title as itemaccttitle, 
	ia.type as itemaccttype, f.objid as fundid, 
	1 as unmapped  
from ( 
	select t1.* from ( 
		select inc.acctid, sum(inc.amount) as amount 
		from liquidation_remittance lr 
			inner join income_summary inc on lr.objid = inc.refid 
		where lr.liquidationid = $P{liquidationid} 
		group by inc.acctid 
	)t1, (select @rownum:=0)rn  
	where acctid not in (
		select revenueitemid from ngas_revenue_mapping 
		where revenueitemid=t1.acctid 
	) 
)xx inner join itemaccount ia on xx.acctid = ia.objid 
	inner join fund f on ia.fund_objid = f.objid 
order by ia.code, ia.title 
