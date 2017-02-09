[getStandardReport]
select 
	t.classname,  min(t.special) as special , 
	sum( basiccurrent ) as basiccurrent, sum( basicdisc ) as basicdisc, sum( basicprev ) as basicprev, 
	sum( basiccurrentint ) as basiccurrentint, sum( basicprevint ) as basicprevint, sum( basicnet ) as basicnet, 
	sum( sefcurrent ) as sefcurrent, sum( sefdisc ) as sefdisc, sum( sefprev ) as sefprev, 
	sum( sefcurrentint ) as sefcurrentint, sum( sefprevint ) as sefprevint, sum( sefnet ) as sefnet, 
	sum( idlecurrent ) as idlecurrent, sum( idleprev ) as idleprev, sum( idledisc ) as idledisc, 
	sum( idleint ) as idleint, sum( idlenet ) as idlenet, sum( levynet ) as levynet 
from ( 
	select 
		pc.name as classname, pc.orderno, pc.special,  
		case when ri.revperiod='current' then ri.basic else 0.0 end  as basiccurrent,
		case when ri.revperiod='current' then ri.basicdisc else 0.0 end  as basicdisc,
		case when ri.revperiod in ('previous', 'prior') then ri.basic else 0.0 end  as basicprev,
		case when ri.revperiod='current' then ri.basicint else 0.0 end  as basiccurrentint,
		case when ri.revperiod in ('previous', 'prior') then ri.basicint else 0.0 end  as basicprevint,
		case when ri.revperiod in ('current', 'previous', 'prior') then (ri.basic - ri.basicdisc + ri.basicint) else 0.0 end as basicnet, 

		case when ri.revperiod='current' then ri.sef else 0.0 end  as sefcurrent,
		case when ri.revperiod='current' then ri.sefdisc else 0.0 end  as sefdisc,
		case when ri.revperiod in ('previous', 'prior') then ri.sef else 0.0 end  as sefprev,
		case when ri.revperiod='current' then ri.sefint else 0.0 end  as sefcurrentint,
		case when ri.revperiod in ('previous', 'prior') then ri.sefint else 0.0 end as sefprevint,
		case when ri.revperiod in ('current', 'previous', 'prior') then (ri.sef - ri.sefdisc + ri.sefint) else 0.0 end as sefnet,  

		case when ri.revperiod='current' then ri.basicidle else 0.0 end  as idlecurrent,
		case when ri.revperiod in ('previous', 'prior') then ri.basicidle else 0.0 end  as idleprev,
		case when ri.revperiod='current' then ri.basicidledisc else 0.0 end  as idledisc,
		case when ri.revperiod in ('current', 'previous', 'prior') then ri.basicidleint else 0.0 end  as idleint, 
		case when ri.revperiod in ('current', 'previous', 'prior') then (ri.basicidle-ri.basicidledisc+ri.basicidleint) else 0.0 end as idlenet, 
		0.0 as levynet 
	from remittance rem 
		inner join liquidation_remittance liqr on rem.objid = liqr.objid 
		inner join liquidation liq on liqr.liquidationid = liq.objid
		inner join remittance_cashreceipt remc on rem.objid = remc.remittanceid 
		inner join cashreceipt cr on remc.objid = cr.objid 
		inner join cashreceiptitem_rpt_online ri on cr.objid = ri.rptreceiptid 
		left join rptledger rl ON ri.rptledgerid = rl.objid  
		left join propertyclassification pc ON rl.classification_objid = pc.objid 
	where ${filter} 
		and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
)t 	
group by t.classname
order by min(t.orderno) 


[getAdvanceReport]
select 
	t.classname,  min(t.special) as special , 
	sum( basic)  as basic, sum( basicdisc ) as basicdisc, 
	sum( sef)  as sef, sum( sefdisc ) as sefdisc, 
	sum(basicnet) as basicnet, sum(sefnet ) as sefnet,
	sum( netgrandtotal ) as netgrandtotal, sum(idle) as idle 
from ( 
	select 
		pc.name as classname, pc.orderno, pc.special,  
		ri.basic, ri.basicdisc, ( ri.basic - ri.basicdisc ) as basicnet,
		ri.sef, ri.sefdisc, (ri.sef - ri.sefdisc ) as sefnet,
		( ri.basic - ri.basicdisc + ri.sef - ri.sefdisc + ri.basicidle ) as netgrandtotal , ri.basicidle as idle 
	from remittance rem 
		inner join liquidation_remittance liqr on rem.objid = liqr.objid 
		inner join liquidation liq on liqr.liquidationid = liq.objid
		inner join remittance_cashreceipt remc on rem.objid = remc.remittanceid 
		inner join cashreceipt cr on remc.objid = cr.objid 
		inner join cashreceiptitem_rpt_online ri on cr.objid = ri.rptreceiptid 
		inner join rptledger rl ON ri.rptledgerid = rl.objid  
		inner join propertyclassification pc ON rl.classification_objid = pc.objid 
	where ${filter}  
		and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
		and ri.revperiod = 'advance'
)t 	
group by t.classname 
order by min(t.orderno)  


[findStandardDispositionReport]
select 
	sum( provcitybasicshare ) as provcitybasicshare, 
	sum( munibasicshare ) as munibasicshare, 
	sum( brgybasicshare ) as brgybasicshare, 
	sum( provcitysefshare ) as provcitysefshare, 
	sum( munisefshare ) as munisefshare, 
	sum( brgysefshare ) as brgysefshare 
from ( 
	select   
		case when ri.revtype in ('basic', 'basicint', 'basicidle') and ri.sharetype in ('province', 'city') then ri.amount else 0.0 end as provcitybasicshare,
		case when ri.revtype in ('basic', 'basicint', 'basicidle') and ri.sharetype in ('municipality') then ri.amount else 0.0 end as munibasicshare,
		case when ri.revtype in ('basic', 'basicint', 'basicidle') and ri.sharetype in ('barangay') then ri.amount else 0.0 end as brgybasicshare,
		case when ri.revtype in ('sef', 'sefint') and ri.sharetype in ('province', 'city') then ri.amount else 0.0 end as provcitysefshare,
		case when ri.revtype in ('sef', 'sefint') and ri.sharetype in ('municipality') then ri.amount else 0.0 end as munisefshare,
		case when ri.revtype in ('sef', 'sefint') and ri.sharetype in ('barangay') then ri.amount else 0.0 end as brgysefshare 
	from remittance rem 
		inner join liquidation_remittance liqr on rem.objid = liqr.objid 
		inner join liquidation liq on liqr.liquidationid = liq.objid
		inner join remittance_cashreceipt remc on rem.objid = remc.remittanceid 
		inner join cashreceipt cr on remc.objid = cr.objid 
		inner join cashreceiptitem_rpt_account ri ON cr.objid = ri.rptreceiptid 
	where ${filter}  
		and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
		and ri.revperiod != 'advance' 
)t 


[findAdvanceDispositionReport]
select 
	sum( provcitybasicshare ) as provcitybasicshare,
	sum( munibasicshare ) as munibasicshare,
	sum( brgybasicshare ) as brgybasicshare,
	sum( provcitysefshare ) as provcitysefshare,
	sum( munisefshare ) as munisefshare,
	sum( brgysefshare ) as brgysefshare
from ( 
	select 
		case when ri.revtype in ('basic', 'basicint', 'basicidle') and ri.sharetype in ('province', 'city') then ri.amount else 0.0 end as provcitybasicshare,
		case when ri.revtype in ('basic', 'basicint', 'basicidle') and ri.sharetype in ('municipality') then ri.amount else 0.0 end as munibasicshare,
		case when ri.revtype in ('basic', 'basicint', 'basicidle') and ri.sharetype in ('barangay') then ri.amount else 0.0 end as brgybasicshare,
		case when ri.revtype in ('sef', 'sefint') and ri.sharetype in ('province', 'city') then ri.amount else 0.0 end as provcitysefshare,
		case when ri.revtype in ('sef', 'sefint') and ri.sharetype in ('municipality') then ri.amount else 0.0 end as munisefshare,
		case when ri.revtype in ('sef', 'sefint') and ri.sharetype in ('barangay') then ri.amount else 0.0 end as brgysefshare 
	from remittance rem 
		inner join liquidation_remittance liqr on rem.objid = liqr.objid 
		inner join liquidation liq on liqr.liquidationid = liq.objid
		inner join remittance_cashreceipt remc on rem.objid = remc.remittanceid 
		inner join cashreceipt cr on remc.objid = cr.objid 
		inner join cashreceiptitem_rpt_account ri ON cr.objid = ri.rptreceiptid 
	where ${filter}  
		and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid)
		and ri.revperiod = 'advance' 
)t 
