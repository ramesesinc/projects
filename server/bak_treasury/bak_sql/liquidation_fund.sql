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


[bindNonCashPayments]
update 
	cashreceiptpayment_noncash a, 
	( 
		select remf.objid as remittancefundid, remf.liquidationfundid 
		from liquidation_fund lf 
			inner join remittance_fund remf on remf.liquidationfundid = lf.objid 
		where lf.liquidationid = $P{liquidationid} 
	)b 
set 
	a.liquidationfundid = b.liquidationfundid 
where 
	a.remittancefundid = b.remittancefundid 


[unbindNonCashPayments]
update 
	cashreceiptpayment_noncash a, 
	( 
		select remf.objid as remittancefundid, remf.liquidationfundid 
		from liquidation_fund lf 
			inner join remittance_fund remf on remf.liquidationfundid = lf.objid 
		where lf.liquidationid = $P{liquidationid} 
	)b 
set 
	a.liquidationfundid = null 
where 
	a.remittancefundid = b.remittancefundid 


[unbindRemittanceFunds]
update 
	remittance_fund a,
	liquidation_fund b 
set 
	a.liquidationfundid = null 
where 
	b.liquidationid = $P{liquidationid} 
	and a.liquidationfundid = b.objid 


[removeFunds]
delete from liquidation_fund where liquidationid = $P{liquidationid} 
 