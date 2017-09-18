[getReceiptForms]
select 
	formtypeindexno, controlid, formno, formtype, stubno, sum( amount ) as amount , 
	(case when formtype='serial' then min(receiptno) else null end) as fromseries, 
	(case when formtype='serial' then max(receiptno) else null end) as toseries 
from ( 
  select tmp1.*, 
		(case when tmp1.voided > 0 then 0.0 else tmp1.receiptamount end) as amount, 
		(case when formtype='serial' then 1 else 2 end) as formtypeindexno 
  from ( 
		select 
			c.objid, c.controlid, c.series, c.receiptno, c.amount as receiptamount, 
			c.formno, af.formtype, c.stub as stubno, af.serieslength,  
			(select count(*) from cashreceipt_void where receiptid=c.objid) as voided 
		from cashreceipt c 
			inner join af on af.objid = c.formno 
		where c.remittanceid = $P{remittanceid} 
  )tmp1 
)tmp2 
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
		where c.remittanceid = 'REM-1f7db4bc:15e7a8cd772:-7fbe' 
			and ia.fund_objid like '%' 
			and af.objid like '%' 
  )tmp1 
)tmp2 
group by particulars 
