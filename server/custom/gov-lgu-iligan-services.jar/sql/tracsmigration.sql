[getList]
select * from tracs_remittance 
where txnno like $P{txnno}
order by dtposted desc  

[findRemittance]
select * from tracs_remittance 
where objid like $P{objid} 

[getRemittanceEntries]
select min(formno) as formno, 
	min(receiptno) as startseries,
	max(receiptno) as endseries,
	sum(amount) as amount 
from tracs_cashreceipt
where remittanceid=$P{objid}
group by formno 

[getCashreceipts]
select
	o.ObjID as objid, o.strORNo as receiptno, o.dtORDate as receiptdate, o.strORNo as series,
	'batchcapture:misc' as _filetype, o.strPayor as paidby, 
	o.strPayorAddress as paidbyaddress, o.intVoid as voided, o.strRemarks as remarks,
	case when o.intVoid = 0 then o.curAmount else 0.0 end as amount 
from tblOR o
	left join tracs_etracs..cashreceipts c on c.objid = o.ObjID 
where o.strORNo between '${startseries}' and '${endseries}'
	and c.objid is null  
order by o.strORNo 

[getCashreceiptsItem]
select 
	oi.ID as objid, oi.ParentID as receiptid,  m.*, oi.curAmount as amount, 
	tf.strDescription as taxfeedesc, tf.ObjID as ttaxfeeid  
from tblORItem oi 
	inner join tblTaxFeeAccount tf on tf.ObjID = oi.strAcctID 
	left join tracs_etracs..tbletracsaccountmapping m on m.taxfeeid = tf.ObjID 
where oi.ParentID='${objid}' 	

[getCheckPayments]
select 
	c.ObjID as objid, c.ParentID as receiptid, c.strCheckNO as checkno, 'CHECK' as type, 
	b.strBankCode as bank, c.dtDateIssued as checkdate, c.curAmount as amount
from tblIncomingCheck c
 inner join tblBank b on c.strBankID = b.ObjID 
where c.ParentID = $P{objid} 

[findMappedItem]
select * from tracs_etracs..tbletracsaccountmapping where taxfeeid =$P{ttaxfeeid}


[createAccountMapping]
insert into tracs_etracs..tbletracsaccountmapping
(
	taxfeeid, item_objid, item_code, item_title, 
	fund_objid, fund_code, fund_title
)
values 
(
	$P{taxfeeid}, $P{itemid}, $P{itemcode}, $P{itemtitle}, 
	$P{fundid}, $P{fundcode}, $P{fundtitle}	

)

[createTRACSRemittance]
insert into tracs_remittance 
(
	objid, txnno, dtposted, collector_objid, collector_name, collector_title, 
	amount, postedby 
)
values 
(
	$P{objid}, $P{txnno}, $P{dtposted},  $P{collectorid}, $P{collectorname}, $P{collectortitle},
	$P{amount}, $P{postedby}
)

[createTRACSCashreceipt]
insert into tracs_cashreceipt
(
	objid, formno, formtype, receiptno, receiptdate, paidby, paidbyaddress,
	amount, collector_objid, collector_name, collector_title,
	collectiontype_objid, collectiontype_name, remittanceid 
)
values 
(
		$P{objid}, $P{formno}, $P{formtype}, $P{receiptno}, $P{receiptdate}, $P{paidby}, $P{paidbyaddress},
		$P{amount}, $P{collectorid}, $P{collectorname}, $P{collectortitle},
		$P{collectiontypeid}, $P{collectiontypename}, $P{remittanceid} 
)

[createTRACSCashreceiptitem]
insert into tracs_cashreceiptitem 
(
	objid, receiptid, item_objid, item_code,
	item_title, amount 
)
values
(
	$P{objid}, $P{receiptid}, $P{itemid}, $P{itemcode},
	$P{itemtitle}, $P{amount}  		
)

[createCashreceipt]
insert into tracs_etracs..cashreceipts 
(
	objid
)
values 
(
	$P{objid}  
)

[getFundlist]
select 
	distinct fu.objid, fu.code, fu.title 
from tracs_remittance r 
	inner join tracs_cashreceipt tc on r.objid = tc.remittanceid
	inner join tracs_cashreceiptitem tci on tci.receiptid = tc.objid
	inner join itemaccount ru on ru.objid = tci.item_objid 
	inner join fund fu on ru.fund_objid = fu.objid
where r.objid=$P{objid}

[getRevenueItemSummaryByFund]
select 
  fu.title as fundname, tci.item_objid as acctid, tci.item_title as acctname,
  tci.item_code as acctcode, sum( tci.amount ) as amount 
from tracs_remittance r 
	inner join tracs_cashreceipt cr on r.objid = cr.remittanceid
	inner join tracs_cashreceiptitem tci on tci.receiptid = cr.objid
	inner join itemaccount ru on ru.objid = tci.item_objid 
	inner join fund fu on ru.fund_objid = fu.objid
where r.objid=$P{objid} and fu.objid like $P{fundid}
	and cr.amount > 0.0 
group by fu.title, tci.item_objid , tci.item_code, tci.item_title 
order by fu.title, tci.item_title 

[getBrgyShares]
select 
  min(b.name) as barangayname, SUM( tci.amount ) as netbasic, 
  SUM( tci.lgushare ) as lgushare, sum( tci.brgyshare ) as brgyshare 
from tracs_remittance r 
	inner join tracs_cashreceipt cr on r.objid = cr.remittanceid
	inner join tracs_cashreceiptitem tci on tci.receiptid = cr.objid
	inner join barangay b on b.objid = tci.barangayid
where r.objid=$P{objid} and cr.amount > 0.0  
	and tci.item_title like '%RPT%BASIC%'
group by b.objid 


[getReceiptsByRemittanceFund]
select 
	bt.* 
from (
	select 
	  min(cr.formno) as afid, 
	  min(cr.receiptdate) as txndate, min(ri.fund_title) as fundname, null as remarks, 
	  min(cr.receiptno) as serialno,  min(cr.paidby) AS payer,
	  min(cri.item_title)  AS particulars,min(cr.paidbyaddress) AS payeraddress,
	  sum(cri.amount) as amount 
	from tracs_remittance rem 
	   inner join tracs_cashreceipt cr on cr.remittanceid = rem.objid 
	   inner join tracs_cashreceiptitem cri on cri.receiptid = cr.objid 
	   inner join itemaccount ri on ri.objid = cri.item_objid
	   inner join fund fu on ri.fund_objid = fu.objid
	where rem.objid=$P{objid} and fu.objid like $P{fundid} 
		and cr.amount > 0.0 
	group by cr.formno, cr.receiptno, cri.item_code 

	union

	select 
	  cr.formno as afid, cr.receiptdate as txndate, null as fundname, null as remarks, 
	  cr.receiptno as serialno,  '***VOIDED***' AS payer, '***VOIDED***' AS particulars,
	  '' AS payeraddress,  cr.amount
	from tracs_remittance rem 
	   inner join tracs_cashreceipt cr on cr.remittanceid = rem.objid 
	where rem.objid=$P{objid} 
		and cr.amount  = 0.0
   ) bt 
ORDER BY afid, serialno, payer 


