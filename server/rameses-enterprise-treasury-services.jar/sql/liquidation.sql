[bindRemittances]
update remittance set 
	liquidationid = $P{liquidationid} 
where 
	liquidationid is null 
	and liquidatingofficer_objid = $P{liquidatingofficerid} 
	and state = 'APPROVED' 


[bindRemittanceFunds]
update 
	remittance_fund a, 
	( 
		select 
			remf.objid as remittancefundid, lf.objid as liquidationfundid
		from remittance rem 
			inner join remittance_fund remf on remf.remittanceid = rem.objid 
			inner join liquidation_fund lf on (lf.liquidationid = rem.liquidationid and lf.fund_objid = remf.fund_objid) 
		where rem.liquidationid = $P{liquidationid} 
	)b 
set 
	a.liquidationfundid = b.liquidationfundid 
where 
	a.objid = b.remittancefundid 


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


[postJev]
insert into jev (
	objid, jevno, jevdate, fundid, dtposted, 
	txntype, refid, refno, reftype, amount 
) 
select 
	lf.objid, null, null, lf.fund_objid, l.dtposted, 
	'COLLECTION', lf.objid, l.txnno, 'liquidation', lf.amount 
from liquidation l 
	inner join liquidation_fund lf on lf.liquidationid = l.objid 
where l.objid = $P{liquidationid} 


[postJevItem]
insert into jevitem ( 
	objid, jevid, ledgerid, ledgertype, acctid, dr, cr, particulars 
) 
select  
	concat(lf.objid,'-',ct.acctid) as objid, 
	lf.objid as jevid, ct.objid as ledgerid,
	'cashbook_treasury' as ledgertype, 
	ct.acctid, (lf.totalcash + lf.totalcheck) as dr, 0.0 as cr, 
	null as particulars 
from liquidation l 
	inner join liquidation_fund lf on lf.liquidationid = l.objid 
	inner join cashbook_treasury ct on ct.fundid=lf.fund_objid  
where l.objid = $P{liquidationid} 
	and (lf.totalcash + lf.totalcheck) > 0 


[postJevItemForBankAccount]
insert into jevitem ( 
	objid, jevid, ledgerid, ledgertype, acctid, dr, cr, particulars 
) 
select 
	concat(lf.objid,'-',ba.acctid) as objid, 
	lf.objid as jevid, ba.objid as ledgerid,
	'bankaccount' as ledgertype, 
	ba.acctid, sum(nc.amount) as dr, 
	0.0 as cr, null as particulars 
from liquidation_fund lf 
	inner join liquidation l on l.objid=lf.liquidationid
	inner join remittance_fund remf on remf.liquidationfundid = lf.objid 
	inner join cashreceiptpayment_noncash nc on nc.remittancefundid = remf.objid 
	inner join cashreceipt c on c.objid = nc.receiptid 
	inner join creditmemo cm on cm.objid = nc.refid 
	inner join bankaccount ba on ba.objid = cm.bankaccount_objid 
where l.objid = $P{liquidationid} 
	and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
	and c.state <> 'CANCELLED' 
	and nc.reftype = 'CREDITMEMO' 
group by lf.objid, ba.acctid, ba.objid 


[postJevItemForRevenue]
insert into jevitem ( 
	objid, jevid, ledgerid, ledgertype, acctid, dr, cr, particulars 
) 
select  
	concat(lf.objid,'-',ci.item_objid) as objid, 
	lf.objid as jevid, null as ledgerid,
	'revenue' as ledgertype, 
	ci.item_objid, 0.0 as dr, sum(ci.amount) as cr, 
	null as particulars 
from liquidation_fund lf 
	inner join liquidation l on l.objid=lf.liquidationid
	inner join remittance rem on rem.liquidationid=l.objid 
	inner join cashreceipt c on c.remittanceid=rem.objid 	
	inner join cashreceiptitem ci on (ci.receiptid=c.objid and ci.item_fund_objid=lf.fund_objid)
where l.objid = $P{liquidationid} 
	and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
	and c.state <> 'CANCELLED' 
	and ci.item_objid not in ( 
		select refacctid from cashreceipt_share 
		where receiptid=c.objid and refacctid=ci.item_objid 
	) 
group by lf.objid, ci.item_objid 
