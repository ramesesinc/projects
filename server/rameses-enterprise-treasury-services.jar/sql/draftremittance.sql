[removeCashReceipts]
delete from draftremittance_cashreceipt where remittanceid = $P{remittanceid} 


[insertCashReceipts]
insert into draftremittance_cashreceipt ( 
	objid, remittanceid, controlid, amount, totalnoncash, totalcash, cancelled, voided 
) 
select 
	objid, $P{remittanceid}, controlid, amount, totalnoncash, totalcash, cancelled, voided  
from ( 
	select 
		c.objid, c.controlid, 
		c.amount, c.totalnoncash, (c.amount-c.totalnoncash) as totalcash, 
		(case when c.state = 'CANCELLED' then 1 else 0 end) as cancelled, 
		(select count(*) from cashreceipt_void where receiptid=c.objid) as voided 
	from cashreceipt c 
		inner join af_control afc on afc.objid = c.controlid 
		inner join af on (af.objid = afc.afid and af.formtype='serial') 
	where c.collector_objid = $P{collectorid} 
		and c.receiptdate < $P{txndate}  
		and c.state in ($P{txnstate}, 'CANCELLED', 'DELEGATED') 
		and c.objid not in (select objid from remittance_cashreceipt where objid=c.objid) 

	union all 

	select 
		c.objid, c.controlid, 
		c.amount, 0.0 as totalnoncash, c.amount as totalcash, 
		0 as cancelled, 0 as voided 
	from cashreceipt c 
		inner join af_control afc on afc.objid = c.controlid 
		inner join af on (af.objid = afc.afid and af.formtype <> 'serial') 
	where c.collector_objid = $P{collectorid}  
		and c.receiptdate < $P{txndate} 
		and c.state in ($P{txnstate}, 'DELEGATED')
		and c.objid not in (select objid from remittance_cashreceipt where objid=c.objid) 
		and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
)tmp1


[insertCashReceipt]
insert into draftremittance_cashreceipt ( 
	objid, remittanceid, controlid, amount, totalnoncash, totalcash, cancelled, voided 
) 
select 
	objid, $P{remittanceid}, controlid, amount, totalnoncash, totalcash, cancelled, voided  
from ( 
	select 
		c.objid, c.controlid, 
		c.amount, c.totalnoncash, (c.amount-c.totalnoncash) as totalcash, 
		(case when c.state = 'CANCELLED' then 1 else 0 end) as cancelled, 
		(select count(*) from cashreceipt_void where receiptid=c.objid) as voided 
	from cashreceipt c 
		inner join af_control afc on afc.objid = c.controlid 
		inner join af on (af.objid = afc.afid and af.formtype='serial') 
	where c.objid = $P{receiptid}  

	union all 

	select 
		c.objid, c.controlid, 
		c.amount, 0.0 as totalnoncash, c.amount as totalcash, 
		0 as cancelled, 0 as voided 
	from cashreceipt c 
		inner join af_control afc on afc.objid = c.controlid 
		inner join af on (af.objid = afc.afid and af.formtype <> 'serial') 
	where c.objid = $P{receiptid}  
		and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
)tmp1


[getCashReceiptSummary]
select  
  collector_objid, controlid, formno, formtype,  
  case when formtype='serial' then min(series) else null end as startseries, 
  case when formtype='serial' then max(series) else null end as endseries, 
  sum( qty ) as qty, sum( cqty ) as cqty, 
  sum( case when voided=0 then amount else 0.0 end ) AS amount,
  sum( case when voided=0 then totalcash-cashchange else 0.0 end ) AS totalcash,
  sum( case when voided=0 then totalnoncash else 0.0 end ) AS totalnoncash
from ( 
  select 
    c.objid, c.controlid, c.collector_objid, af.formtype, af.objid as formno, 0 as formindex, 
    c.series, c.amount, c.totalcash, c.totalnoncash, c.cashchange, 
    (case when c.state = 'CANCELLED' then 0 else 1 end) as qty, 
    (case when c.state = 'CANCELLED' then 1 else 0 end) as cqty, 
    (select count(*) from cashreceipt_void where receiptid=c.objid) as voided 
  from draftremittance_cashreceipt remc  
	inner join cashreceipt c on c.objid = remc.objid 
    inner join af_control afc on afc.objid = c.controlid 
    inner join af on (af.objid = afc.afid and af.formtype='serial') 
  where remc.remittanceid = $P{remittanceid}  

  union all 

  select 
    c.objid, c.controlid, c.collector_objid, af.formtype, af.objid as formno, 1 as formindex,  
    c.series, c.amount, c.totalcash, c.totalnoncash, c.cashchange, 
    convert((c.amount/af.denomination), signed) as qty, 0 as cqty, 0 as voided 
  from draftremittance_cashreceipt remc  
		inner join cashreceipt c on c.objid = remc.objid 
    inner join af_control afc on afc.objid = c.controlid 
    inner join af on (af.objid = afc.afid and af.formtype <> 'serial') 
  where remc.remittanceid = $P{remittanceid}  

)tmp1 
group by collector_objid, controlid, formno, formtype 


[getCancelledCashReceipts]
select cs.*, c.series 
from draftremittance_cashreceipt remc 
	inner join cashreceipt c on c.objid = remc.objid 
	inner join af_control afc on afc.objid = c.controlid 
	inner join af on (af.objid = afc.afid and af.formtype='serial') 
	inner join cashreceipt_cancelseries cs on cs.receiptid = c.objid 
where remc.remittanceid = $P{remittanceid}  


[getCashReceiptChecks]
select 
	nc.objid, nc.refno, nc.particulars, nc.reftype, 
	(case when remc.voided=0 then nc.amount else 0.0 end) as amount,
	(case when remc.voided=0 then 0 else 1 end) as voided,
	c.subcollector_name 
from draftremittance_cashreceipt remc 
	inner join cashreceipt c on c.objid = remc.objid 
	inner join af_control afc on afc.objid = c.controlid 
	inner join af on (af.objid = afc.afid and af.formtype='serial') 
	inner join cashreceiptpayment_noncash nc on nc.receiptid = c.objid 
where remc.remittanceid = $P{remittanceid}  
	and remc.cancelled=0 
	and remc.voided=0 


[getTotals]
select 
	count(*) as itemcount, 
	sum( amount ) as amount, 
	sum( totalcash ) as totalcash, 
	sum( totalnoncash ) as totalnoncash 
from draftremittance_cashreceipt remc 
where remc.remittanceid = $P{remittanceid}  
	and (remc.voided+remc.cancelled)=0 


[getCashReceipts]
select 
  c.formno, c.receiptno, c.paidby, c.receiptdate,
  (case when remc.voided=0 then remc.amount else 0.0 end) as amount,
  (case when remc.voided=0 then 0 else 1 end) as voided, 
  c.subcollector_name
from draftremittance rem 
	inner join draftremittance_cashreceipt remc on remc.remittanceid = rem.objid 
	inner join cashreceipt c on c.objid = remc.objid 
where rem.objid = $P{remittanceid}  
order by c.receiptdate, c.receiptno 


[getFundSummary]
select 
	ia.fund_objid, ia.fund_title, sum(ci.amount) as amount 
from draftremittance rem 
	inner join draftremittance_cashreceipt remc on remc.remittanceid = rem.objid 
	inner join cashreceipt c on c.objid = remc.objid 
	inner join cashreceiptitem ci on ci.receiptid = c.objid 
	inner join itemaccount ia on ia.objid = ci.item_objid 
where rem.objid = $P{remittanceid}  
	and (remc.voided + remc.cancelled) = 0 
group by ia.fund_objid, ia.fund_title 


[postCashReceipts]
insert into remittance_cashreceipt ( 
	objid, remittanceid 
) 
select objid, remittanceid 
from draftremittance_cashreceipt 
where remittanceid = $P{remittanceid}


[postCashReceiptPayments]
insert into remittance_noncashpayment ( 
  objid, remittanceid, amount, voided 
) 
select 
	nc.objid, remc.remittanceid, nc.amount, 0 as voided 
from draftremittance rem 
	inner join draftremittance_cashreceipt remc on remc.remittanceid = rem.objid 
	inner join cashreceipt c on c.objid = remc.objid 
	inner join cashreceiptpayment_noncash nc on nc.receiptid = c.objid 
where rem.objid = $P{remittanceid}  
	and (remc.voided + remc.cancelled) = 0 


[findDelegatedReceipt]
select c.* 
from draftremittance rem 
	inner join draftremittance_cashreceipt remc on remc.remittanceid = rem.objid 
	inner join cashreceipt c on c.objid = remc.objid 
where rem.objid = $P{remittanceid} 
	and c.state = 'DELEGATED' 
