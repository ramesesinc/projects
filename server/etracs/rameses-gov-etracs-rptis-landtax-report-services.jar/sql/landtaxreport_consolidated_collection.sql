[getConsolidatedCollections]
select 
	b.pin,
	b.name as barangay, 
	count(distinct rl.objid) as rpucount,
	sum(ri.basic) as basic, 
	sum(ri.basicint) as basicint,
	sum(ri.basicdisc) as basicdisc, 
	sum(ri.sef) as sef, 
	sum(ri.sefint) as sefint,
	sum(ri.sefdisc) as sefdisc,
	sum(ri.basic + ri.basicint - ri.basicdisc + ri.sef + ri.sefint - ri.sefdisc) as total 
from remittance rem 
	inner join liquidation_remittance liqr on rem.objid = liqr.objid 
	inner join liquidation liq on liqr.liquidationid = liq.objid
	inner join remittance_cashreceipt remc on rem.objid = remc.remittanceid 
	inner join cashreceipt cr on remc.objid = cr.objid 
	inner join cashreceiptitem_rpt_online ri on cr.objid = ri.rptreceiptid 
	inner join rptledger rl ON ri.rptledgerid = rl.objid  
	inner join barangay b on rl.barangayid = b.objid 
where ${filter} 
    and rl.classification_objid = $P{classid}
    and rl.rputype = $P{type}
	and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
group by b.pin, b.name 
order by b.pin 