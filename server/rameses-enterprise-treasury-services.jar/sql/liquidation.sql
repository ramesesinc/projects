[getRemittanceFundCashbreakdowns] 
select remf.cashbreakdown 
from remittance rem 
	inner join remittance_fund remf on remf.remittanceid=rem.objid 
where rem.liquidationid = $P{liquidationid} 


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
	amount, totalcash, totalcheck, totalcr
) 
select 
	concat(rem.liquidationid,'-',remf.fund_objid) as objid, 
	rem.liquidationid, remf.fund_objid, fund.title as fund_title, 
	sum(remf.amount) as amount, sum(remf.totalcash) as totalcash, 
	sum(remf.totalcheck) as totalcheck, sum(remf.totalcr) as totalcr 
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
