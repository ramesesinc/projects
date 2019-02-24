[getOpenChecks]
SELECT refno, receivedfrom, amount, amtused 
FROM checkpayment
WHERE objid IN ( 
   SELECT refid 
   FROM cashreceiptpayment_noncash npc
   INNER JOIN cashreceipt c ON c.objid=npc.receiptid
   LEFT JOIN cashreceipt_void cv ON cv.receiptid = c.objid 
   WHERE c.remittanceid =  $P{remittanceid} AND cv.objid IS NULL
)   
AND (amount - amtused) > 0 

[findInvalidCheck]
select 
	pc.refno, pc.amount, sum(nc.amount) as noncashamount 
from cashreceipt c 
	inner join cashreceiptpayment_noncash nc on nc.receiptid = c.objid 
	inner join checkpayment pc on pc.objid = nc.checkid 
where c.remittanceid = $P{remittanceid} 
	and c.objid not in (select receiptid from cashreceipt_void where receiptid=c.objid) 
group by pc.refno, pc.amount
having (pc.amount-sum(nc.amount)) > 0 

[insertRemittanceFund]
INSERT INTO remittance_fund ( 
	objid, controlno, remittanceid, fund_objid, fund_title, 
	amount, totalcash, totalcheck, totalcr, cashbreakdown 
)
select 
	concat( t1.remittanceid, '-', fund.objid ) as objid, 
	concat( r.controlno, '-', fund.code ) as controlno,
	t1.remittanceid, fund.objid as fund_objid, fund.title as fund_title,
	sum(t1.amount)-sum(t1.share) as amount, 0.0 as totalcash, 
	0.0 as totalcheck, 0.0 as totalcr, '[]' as cashbreakdown  
from ( 
	select remittanceid, fundid, sum(amount) as amount, 0.0 as share 
	from vw_remittance_cashreceiptitem 
	where remittanceid = $P{remittanceid} 
	group by remittanceid, fundid 

	union all 

	select t1.remittanceid, t1.fundid, 0.0 as amount, sum(cs.amount) as share 
	from ( 
		select remittanceid, receiptid, fundid, acctid 
		from vw_remittance_cashreceiptitem 
		where remittanceid = $P{remittanceid} 
		group by remittanceid, receiptid, fundid, acctid
	)t1, vw_remittance_cashreceiptshare cs 
	where cs.receiptid = t1.receiptid and cs.refacctid = t1.acctid 
	group by t1.remittanceid, t1.fundid 

	union all 

	select remittanceid, fundid, sum(amount) as amount, 0.0 as share  
	from vw_remittance_cashreceiptshare  
	where remittanceid = $P{remittanceid} 
	group by remittanceid, fundid 
)t1, fund, remittance r  
where fund.objid = t1.fundid 
	and r.objid = t1.remittanceid 
group by t1.remittanceid, r.controlno, fund.objid, fund.code, fund.title  
order by fund.code, fund.title 

[updateCashReceiptItemRemittanceFundId]
UPDATE cashreceiptitem cri, cashreceipt cr, remittance_fund rf
SET cri.remittancefundid = rf.objid, cri.remittanceid = rf.remittanceid
WHERE cri.receiptid = cr.objid
AND cr.remittanceid = rf.remittanceid 
AND  cri.item_fund_objid = rf.fund_objid
AND rf.remittanceid = $P{remittanceid}

[updateCashReceiptShareRemittanceFundId]
UPDATE cashreceipt_share crs, cashreceipt cr, itemaccount itm, remittance_fund rf
SET crs.remittancefundid = rf.objid, crs.remittanceid = rf.remittanceid
WHERE crs.receiptid = cr.objid
AND cr.remittanceid = rf.remittanceid 
AND crs.payableitem_objid = itm.objid 
AND  itm.fund_objid = rf.fund_objid
AND rf.remittanceid = $P{remittanceid}

[updateCashReceiptNonCashPaymentRemittanceFundId]
UPDATE cashreceiptpayment_noncash crpn, cashreceipt cr, remittance_fund rf
SET crpn.remittancefundid = rf.objid, crpn.remittanceid = rf.remittanceid
WHERE crpn.receiptid = cr.objid
AND cr.remittanceid = rf.remittanceid 
AND  crpn.fund_objid = rf.fund_objid
AND rf.remittanceid = $P{remittanceid}

[getCashReceiptsForRemittance]
SELECT 
	CONCAT( cr.collector_objid, cr.remittanceid, afc.objid ) AS objid, 
	af.formtype, cr.remittanceid, cr.collector_objid, 
	afc.objid AS afcontrolid, afc.stubno, cr.formno,  
	MIN(cr.series) AS fromseries, MAX(cr.series) AS toseries, afc.endseries,
	COUNT(*) AS qty, SUM( CASE WHEN cv.objid IS NULL THEN cr.amount ELSE 0 END ) AS amount  
FROM ( 
	SELECT * FROM cashreceipt 
	WHERE remittanceid IS NULL 
		AND collector_objid = $P{collectorid} 
		AND receiptdate <= $P{remdate} 
)cr 
	INNER JOIN af_control afc ON cr.controlid=afc.objid 
	INNER JOIN af ON afc.afid = af.objid 
	LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid
GROUP BY 
	CONCAT( cr.collector_objid, cr.remittanceid, afc.objid ), 
	af.formtype, cr.remittanceid, cr.collector_objid, 
	afc.objid, afc.stubno, cr.formno, afc.endseries 


[getBuildRemittanceFunds]
select 
	remittanceid, controlno, fund_objid, fund_title, fund_code, 
	sum(amount) as amount, sum(totalcheck) as totalcheck, sum(totalcr) as totalcr, 
	(sum(amount)-sum(totalcheck)-sum(totalcr)) as totalcash 
from ( 
	select 
		c.remittanceid, r.controlno,  
		fund.objid as fund_objid, fund.title as fund_title, fund.code as fund_code, 
		SUM(ci.amount) as amount, 0.0 as totalcash, 0.0 as totalcheck, 0.0 as totalcr
	from remittance r 
		inner join cashreceipt c on c.remittanceid = r.objid 
		inner join cashreceiptitem ci on ci.receiptid = c.objid 
		inner join fund on fund.objid = ci.item_fund_objid 
		left join cashreceipt_void v on v.receiptid = c.objid 
	where r.objid = $P{remittanceid} 
		and v.objid is null 
	group by c.remittanceid, r.controlno, fund.objid, fund.title, fund.code

	union all 

	select 
		c.remittanceid, r.controlno,  
		fund.objid as fund_objid, fund.title as fund_title, fund.code as fund_code, 
		0.0 as amount, 0.0 as totalcash, sum(nc.amount) as totalcheck, 0.0 as totalcr 
	from remittance r 
		inner join cashreceipt c on c.remittanceid = r.objid 
		inner join cashreceiptpayment_noncash nc on nc.receiptid = c.objid 
		inner join fund on fund.objid = nc.fund_objid 
		left join cashreceipt_void v on v.receiptid = c.objid 
	where r.objid = $P{remittanceid} 
		and nc.reftype = 'CHECK'
		and v.objid is null 
	group by c.remittanceid, r.controlno, fund.objid, fund.title, fund.code

	union all 

	select 
		c.remittanceid, r.controlno,  
		fund.objid as fund_objid, fund.title as fund_title, fund.code as fund_code, 
		0.0 as amount, 0.0 as totalcash, 0.0 as totalcheck, sum(nc.amount) as totalcr 
	from remittance r 
		inner join cashreceipt c on c.remittanceid = r.objid 
		inner join cashreceiptpayment_noncash nc on nc.receiptid = c.objid 
		inner join fund on fund.objid = nc.fund_objid 
		left join cashreceipt_void v on v.receiptid = c.objid 
	where r.objid = $P{remittanceid} 
		and nc.reftype <> 'CHECK'
		and v.objid is null 
	group by c.remittanceid, r.controlno, fund.objid, fund.title, fund.code
)t1 
group by remittanceid, controlno, fund_objid, fund_title, fund_code 
