[bindRemittances]
update remittance set 
	liquidationid = $P{liquidationid} 
where 
	liquidationid is null 
	and liquidatingofficer_objid = $P{liquidatingofficerid} 
	and state = 'APPROVED' 


[insertFunds]
insert into liquidation_fund ( 
	objid, liquidationid, fund_objid, fund_title, 
	amount, totalcash, totalcheck, totalcr, cashbreakdown 
) 
select 
	concat(rem.liquidationid,'-',remf.fund_objid) as objid, 
	rem.liquidationid, remf.fund_objid, fund.title as fund_title, 
	sum(remf.amount) as amount, sum(remf.totalcash) as totalcash, 
	sum(remf.totalcheck) as totalcheck, sum(remf.totalcr) as totalcr, 
	'[]' as cashbreakdown 
from remittance rem 
	inner join remittance_fund remf on remf.remittanceid = rem.objid 
	inner join fund on remf.fund_objid = fund.objid 
where rem.liquidationid = $P{liquidationid} 
group by rem.liquidationid, remf.fund_objid, fund.title


[insertNonCashPayments]
insert into liquidation_noncashpayment (
	objid, liquidationid 
)
select 
	remnc.objid, rem.liquidationid 
from remittance rem 
	inner join remittance_noncashpayment remnc on remnc.remittanceid = rem.objid 
where rem.liquidationid = $P{liquidationid} 


[updateFundControlNo]
update 
  liquidation_fund liqf, liquidation liq, fund 
set 
  liqf.controlno = concat(liq.txnno,'-',fund.code) 
where liq.objid = $P{liquidationid} 
  and liqf.liquidationid = liq.objid 
  and fund.objid = liqf.fund_objid 


[postToCollectionCash]
insert into collection_cash ( 
	objid, liquidationfundid, controlno, fundid, amount 
) 
select 
	lf.objid, lf.objid, lf.controlno, lf.fund_objid, (lf.totalcash + lf.totalcheck) 
from liquidation l 
	inner join liquidation_fund lf on lf.liquidationid = l.objid 
where l.objid = $P{liquidationid} 
	and ( lf.totalcash + lf.totalcheck) > 0  


[postJev]
insert into jev (
	objid, jevno, jevdate, fundid, dtposted, 
	txntype, refid, refno, reftype, amount,
	state, postedby_objid, postedby_name
) 
select 
	lf.objid, null, null, lf.fund_objid, l.dtposted, 
	'COLLECTION', lf.objid, lf.controlno, 'liquidation', lf.amount,
	'OPEN', l.liquidatingofficer_objid,l.liquidatingofficer_name   
from liquidation l 
	inner join liquidation_fund lf on lf.liquidationid = l.objid 
where l.objid = $P{liquidationid} 


[postJevItem]
insert into jevitem ( 
	objid, jevid, accttype, acctid, dr, cr, particulars 
) 
select 
	c.objid, c.objid, ia.accttype, ia.acctid, c.amount, 0.0, null 
from liquidation_fund lf 
	inner join collection_cash c on c.objid = lf.objid 
	inner join ( 
		select fund_objid, min(type) as accttype, min(objid) as acctid 
		from itemaccount 
		where type = 'CASH_IN_TREASURY' 
		group by fund_objid  
	) ia on ia.fund_objid = c.fundid  
where lf.liquidationid = $P{liquidationid} 


[postJevItemForBankAccount]
insert into jevitem ( 
	objid, jevid, accttype, acctid, dr, cr, particulars  
) 
select 
	concat(lf.objid,'-',ba.acctid) as objid, 
	lf.objid as jevid, ia.type, 
	ba.acctid, sum(nc.amount) as dr, 
	0.0 as cr, null as particulars 
from liquidation_fund lf 
	inner join liquidation l on l.objid=lf.liquidationid
	inner join remittance_fund remf on remf.liquidationfundid = lf.objid 
	inner join cashreceiptpayment_noncash nc on nc.remittancefundid = remf.objid 
	inner join cashreceipt c on c.objid = nc.receiptid 
	inner join creditmemo cm on cm.objid = nc.refid 
	inner join bankaccount ba on ba.objid = cm.bankaccount_objid 
	inner join itemaccount ia on ba.acctid=ia.objid
where l.objid = $P{liquidationid} 
	and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
	and c.state <> 'CANCELLED' 
group by lf.objid, ba.acctid, ia.type  


[postJevItemFromEP]
insert into jevitem ( 
	objid, jevid, accttype, acctid, dr, cr, particulars 
)
select * from (
	select  
		concat(lf.objid,'-',ia.objid) as objid, 
		lf.objid as jevid, ia.type, ia.objid as acctid, 
		sum(nc.amount) as dr, 0.0 as cr, null as particulars 
	from liquidation_fund lf 
		inner join liquidation l on l.objid=lf.liquidationid
		inner join remittance_fund remf on remf.liquidationfundid = lf.objid 
		inner join cashreceiptpayment_noncash nc on nc.remittancefundid = remf.objid 
		inner join cashreceipt c on c.objid = nc.receiptid 
		inner join epayment ep on ep.objid = nc.refid 
		inner join paymentpartner cm on cm.objid = ep.partnerid 
		inner join itemaccount ia on ia.objid = cm.receivableacctid 
	where l.state in ('POSTED','CLOSED') 
		and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
		and c.state <> 'CANCELLED' 
	group by lf.objid, ia.objid, ia.type
)tmp1 
where objid not in (select objid from jevitem where objid=tmp1.objid) 
	and acctid is not null 


[postJevItemForRevenue]
insert into jevitem ( 
	objid, jevid, accttype, acctid, dr, cr, particulars 
) 
select  
	concat(lf.objid,'-',ci.item_objid) as objid, 
	lf.objid as jevid, ia.type, 
	ci.item_objid as acctid, 0.0 as dr, sum(ci.amount) as cr, 
	null as particulars 
from liquidation_fund lf 
	inner join liquidation l on l.objid=lf.liquidationid
	inner join remittance rem on rem.liquidationid=l.objid 
	inner join cashreceipt c on c.remittanceid=rem.objid 	
	inner join cashreceiptitem ci on (ci.receiptid=c.objid and ci.item_fund_objid=lf.fund_objid)
	inner join itemaccount ia on ci.item_objid=ia.objid
where l.objid = $P{liquidationid} 
	and c.objid not in (select receiptid from cashreceipt_share where receiptid=c.objid) 
	and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
	and c.state <> 'CANCELLED' 
group by lf.objid, ci.item_objid, ia.type  


[postJevItemForShare]
insert into jevitem ( 
	objid, jevid, accttype, acctid, dr, cr, particulars 
) 
select * from ( 
	select  
		concat(lf.objid,'-',ia.objid,'-revenue') as objid, 
		lf.objid as jevid, ia.type, 
		ia.objid as acctid, 0.0 as dr, sum(cs.amount) as cr, 
		null as particulars 
	from liquidation_fund lf 
		inner join liquidation l on l.objid = lf.liquidationid
		inner join remittance rem on rem.liquidationid = l.objid 
		inner join cashreceipt c on c.remittanceid = rem.objid 	
		inner join cashreceipt_share cs on (cs.receiptid = c.objid and cs.payableacctid is null)
		inner join itemaccount ia on (ia.objid = cs.refacctid and ia.fund_objid = lf.fund_objid) 
	where l.objid = $P{liquidationid} 
		and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
		and c.state <> 'CANCELLED' 
	group by lf.objid, ia.objid, ia.type 

	union all 

	select  
		concat(lf.objid,'-',ia.objid,'-payable') as objid, 
		lf.objid as jevid, ia.type, 
		ia.objid as acctid, 0.0 as dr, sum(cs.amount) as cr, 
		null as particulars 
	from liquidation_fund lf 
		inner join liquidation l on l.objid = lf.liquidationid
		inner join remittance rem on rem.liquidationid = l.objid 
		inner join cashreceipt c on c.remittanceid = rem.objid 	
		inner join cashreceipt_share cs on cs.receiptid = c.objid 
		inner join itemaccount ia on (ia.objid = cs.payableacctid and ia.fund_objid = lf.fund_objid) 
	where l.objid = $P{liquidationid} 
		and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
		and c.state <> 'CANCELLED' 
	group by lf.objid, ia.objid, ia.type  
)tmp1 


[unbindJev]
delete from jev where objid = $P{jevid} 

[unbindJevItem]
delete from jevitem where jevid = $P{jevid} 
