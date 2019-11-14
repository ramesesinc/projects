[getFunds]
select 
	fund_objid as objid, 
	fund_code as code, 
	fund_title as title  
from eor_remittance_fund 
where remittanceid = $P{remittanceid} 
order by fund_code, fund_title  

[getReceipts]
select 
	t1.receiptid, eor.receiptno, eor.receiptdate, eor.remarks, 
	t1.fund_objid as fundid, t1.fund_title as fundname, eor.paidby, 
	(select item_title from eor_item where parentid = t1.receiptid and item_objid = t1.item_objid limit 1) as particulars, 
	t1.amount 
from ( 
	select 
		eor.objid as receiptid, rf.fund_objid, rf.fund_title, 
		eori.item_objid, sum( eori.amount ) as amount 
	from eor_remittance_fund rf 
		inner join eor on eor.remittanceid = rf.remittanceid  
		inner join eor_item eori on (eori.parentid = eor.objid and eori.item_fund_objid = rf.fund_objid) 
	where rf.remittanceid = $P{remittanceid} 
		and rf.fund_objid like $P{fundid} 
	group by 
		eor.objid, rf.fund_objid, rf.fund_title, eori.item_objid 
)t1, eor 
where eor.objid = t1.receiptid 
order by eor.receiptno, eor.paidby 

[getReceiptItemSummary]
select 
	rf.fund_objid as fundid, rf.fund_title as fundname, 
	eori.item_objid as acctid, eori.item_code as acctcode, eori.item_title as acctname, 
	sum( eori.amount ) as amount 
from eor_remittance_fund rf 
	inner join eor on eor.remittanceid = rf.remittanceid  
	inner join eor_item eori on (eori.parentid = eor.objid and eori.item_fund_objid = rf.fund_objid) 
where rf.remittanceid = $P{remittanceid} 
	and rf.fund_objid like $P{fundid} 
group by 
	rf.fund_objid, rf.fund_title, eori.item_objid, eori.item_code, eori.item_title 
order by 
	rf.fund_title, eori.item_code, eori.item_title 


[getDetailReceipts]
select * 
from ( 
	select 
		t1.receiptid, eor.receiptno, eor.receiptdate, t1.fund_objid as fundid, t1.fund_title as fundname, eor.paidby, 
		(select item_title from eor_item where parentid = t1.receiptid and item_objid = t1.item_objid limit 1) as particulars, 
		t1.amount 
	from ( 
		select 
			eor.objid as receiptid, rf.fund_objid, rf.fund_title, 
			eori.item_objid, sum( eori.amount ) as amount 
		from eor_remittance_fund rf 
			inner join eor on eor.remittanceid = rf.remittanceid  
			inner join eor_item eori on (eori.parentid = eor.objid and eori.item_fund_objid = rf.fund_objid) 
		where rf.remittanceid = $P{remittanceid} 
			and rf.fund_objid like $P{fundid} 
		group by 
			eor.objid, rf.fund_objid, rf.fund_title, eori.item_objid 
	)t1, eor 
	where eor.objid = t1.receiptid 
)t2 
order by particulars, receiptno 


[getReportByReceipts]
select * 
from ( 
	select 
		t1.receiptid, eor.receiptno, eor.receiptdate, 
		t1.fund_objid as fundid, t1.fund_title as fundname, 
		eor.paidby, t1.amount 
	from ( 
		select 
			eor.objid as receiptid, rf.fund_objid, rf.fund_title, 
			eori.item_objid, sum( eori.amount ) as amount 
		from eor_remittance_fund rf 
			inner join eor on eor.remittanceid = rf.remittanceid  
			inner join eor_item eori on (eori.parentid = eor.objid and eori.item_fund_objid = rf.fund_objid) 
		where rf.remittanceid = $P{remittanceid} 
			and rf.fund_objid like $P{fundid} 
		group by 
			eor.objid, rf.fund_objid, rf.fund_title, eori.item_objid 
	)t1, eor 
	where eor.objid = t1.receiptid 
)t2 
order by fundname, receiptno 
