[getRemittances]
select 
	remittanceid, collectorname, controlno, controldate, sum(amount) as amount 
from ( 
	select 
		remf.remittanceid, rem.collector_name as collectorname, 
		rem.controlno, convert(date, rem.controldate) as controldate, remf.amount
	from remittance rem 
		inner join remittance_fund remf on remf.remittanceid = rem.objid 
	where rem.collectionvoucherid = $P{collectionvoucherid} 
		and remf.fund_objid like $P{fundid}  
)tmp1 
group by remittanceid, collectorname, controlno, controldate
order by controldate, collectorname, controlno 


[getCollectionSummaries]
select 
	cvf.fund_title as particulars, cvf.amount
from collectionvoucher_fund cvf 
	inner join collectionvoucher cv on cv.objid = cvf.parentid 
	inner join fund on fund.objid = cvf.fund_objid 
where cvf.parentid = $P{collectionvoucherid} 
	and cvf.fund_objid like $P{fundid}  
order by cvf.parentid, fund.code, fund.title  


[getOtherPayments]
select 
	pc.bank_name, nc.reftype, nc.particulars, 
	sum(nc.amount) as amount, min(nc.refdate) as refdate  
from remittance rem 
	inner join cashreceipt c on c.remittanceid = rem.objid 
	inner join cashreceiptpayment_noncash nc on nc.receiptid = c.objid 
	left join checkpayment pc on (pc.objid = nc.refid and nc.reftype='CHECK') 
where rem.collectionvoucherid = $P{collectionvoucherid} 
	and nc.fund_objid like $P{fundid} 
	and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
group by pc.bank_name, nc.reftype, nc.particulars 
order by pc.bank_name, min(nc.refdate), sum(nc.amount) 
