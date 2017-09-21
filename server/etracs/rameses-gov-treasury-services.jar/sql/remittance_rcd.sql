[getReceiptForms]
select 
	formtypeindexno, controlid, formno, formtype, stubno, sum( amount ) as amount , 
	(case when formtype='serial' then min(receiptno) else null end) as fromseries, 
	(case when formtype='serial' then max(receiptno) else null end) as toseries 
from ( 
	select tmp2.*, 
		(case when tmp2.voided > 0 then 0.0 else tmp2.receiptamount end) as amount, 
		(case when tmp2.formtype='serial' then 1 else 2 end) as formtypeindexno 
	from ( 
		select 
			c.objid, c.controlid, c.series, c.receiptno, tmp1.receiptamount,  
			c.formno, af.formtype, c.stub as stubno, af.serieslength,  
			(select count(*) from cashreceipt_void where receiptid=c.objid) as voided 
		from ( 
			select 
				c.objid, sum(ci.amount) as receiptamount  
			from cashreceipt c 
				inner join cashreceiptitem ci on ci.receiptid = c.objid 
				inner join fund on fund.objid = ci.item_fund_objid 
			where c.remittanceid = $P{remittanceid} 
				and fund.objid like $P{fundid} 
			group by c.objid 
		)tmp1 
			inner join cashreceipt c on c.objid = tmp1.objid 
			inner join af on af.objid = c.formno 
	)tmp2 
)tmp3 
group by formtypeindexno, controlid, formno, formtype, stubno 
order by formtypeindexno, formno, min(receiptno) 


[getCollectionSummaries]
select particulars, sum(amount) as amount 
from ( 
  select tmp1.particulars, 
    (case when tmp1.voided > 0 then 0.0 else tmp1.itemamount end) as amount 
  from ( 
		select 
			concat('AF#', c.formno, ':', ct.title, '-', ia.fund_title) as particulars, 
			(select count(*) from cashreceipt_void where receiptid=c.objid) as voided, 
			ci.amount as itemamount 
		from cashreceipt c 
			inner join collectiontype ct on ct.objid = c.collectiontype_objid 
			inner join cashreceiptitem ci on ci.receiptid = c.objid 
			inner join itemaccount ia on ia.objid = ci.item_objid 
			inner join af on af.objid = c.formno 
		where c.remittanceid = $P{remittanceid}  
			and ia.fund_objid like $P{fundid} 
			and af.objid like $P{afid} 
  )tmp1 
)tmp2 
group by particulars 


[getAFSerials]
select tmp1.*, xxx.issuecount  
from ( 
	select remaf.*, afc.startseries, afc.endseries, afc.afid as formno,  
		(case when remaf.qtyissued > 0 then 0 else 1 end) as groupindex
	from remittance_af remaf 
		inner join af_control afc on afc.objid = remaf.controlid 
		inner join af on af.objid = afc.afid 
	where remaf.remittanceid = $P{remittanceid} 
		and af.formtype = 'serial' 
)tmp1 
	left join (
		select remittanceid, controlid, sum(icount) as issuecount  
		from ( 
			select remittanceid, controlid,  
				case when cancelled>0 or voided>0 then 0 else 1 end as icount 
			from ( 
				select 
					c.objid, c.remittanceid, c.controlid, 1 as icount, 
					(case when c.state='CANCELLED' then 1 else 0 end) as cancelled,  
					(select count(*) from cashreceipt_void where receiptid=c.objid) as voided 
				from cashreceipt c 
					inner join af_control afc on afc.objid = c.controlid 
					inner join af on (af.objid = afc.afid and af.formtype = 'serial') 
					inner join cashreceiptitem ci on ci.receiptid = c.objid 
					inner join fund on fund.objid = ci.item_fund_objid 
				where c.remittanceid = $P{remittanceid} 
					and fund.objid like $P{fundid} 
				group by c.objid, c.remittanceid, c.controlid, c.state 
			)tmp1 
		)tmp2 
		group by remittanceid, controlid 
	) xxx on (xxx.remittanceid = tmp1.remittanceid and xxx.controlid = tmp1.controlid) 
order by groupindex, formno, startseries  


[getAFNonSerials]
select tmp1.*, xxx.issuecount  
from ( 
	select remaf.*, afc.startseries, afc.endseries, afc.afid as formno,  
		(case when remaf.qtyissued > 0 then 0 else 1 end) as groupindex
	from remittance_af remaf 
		inner join af_control afc on afc.objid = remaf.controlid 
		inner join af on af.objid = afc.afid 
	where remaf.remittanceid = $P{remittanceid} 
		and af.formtype <> 'serial' 
)tmp1 
	left join (
		select 
			remittanceid, controlid, denomination, 
			convert(sum(amount)/denomination, signed) as issuecount  
		from ( 
			select remittanceid, controlid, denomination, 
				case when cancelled>0 or voided>0 then 0 else amount end as amount 
			from ( 
				select 
					c.objid, c.remittanceid, c.controlid, ci.amount, af.denomination, 
					(case when c.state='CANCELLED' then 1 else 0 end) as cancelled,  
					(select count(*) from cashreceipt_void where receiptid=c.objid) as voided 
				from cashreceipt c 
					inner join af_control afc on afc.objid = c.controlid 
					inner join af on (af.objid = afc.afid and af.formtype <> 'serial') 
					inner join cashreceiptitem ci on ci.receiptid = c.objid 
					inner join fund on fund.objid = ci.item_fund_objid 
				where c.remittanceid = $P{remittanceid} 
					and fund.objid like $P{fundid} 
			)tmpa  
		)tmpb  
		group by remittanceid, controlid, denomination  
	) xxx on (xxx.remittanceid = tmp1.remittanceid and xxx.controlid = tmp1.controlid) 
order by groupindex, formno, startseries  


[getPayments]
select nc.* 
from remittance rem 
	inner join remittance_noncashpayment remnc on remnc.remittanceid = rem.objid 
	inner join cashreceiptpayment_noncash nc on nc.objid = remnc.objid 
	inner join cashreceipt c on c.objid = nc.receiptid 
	inner join remittance_fund remf on remf.objid = nc.remittancefundid 
	inner join fund on fund.objid = remf.fund_objid 
where rem.objid = $P{remittanceid} 
	and fund.objid like $P{fundid} 
order by c.receiptdate, nc.particulars 
