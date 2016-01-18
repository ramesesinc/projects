[findCashreceipt]
select * from cashreceipt where receiptno=$P{orno}

[updateCashreceiptNoncash]
update cashreceipt set 
	totalcash = 0.0,
	totalnoncash = amount,
	cashchange = 0.0 
where objid = $P{objid}

[insertCheckPayment] 
insert into cashreceiptpayment_noncash 
(
	objid, receiptid, bank, refno, refdate, reftype, 
	amount, particulars, bankid, deposittype 
)
values 
(
	$P{objid}, $P{receiptid}, $P{bank}, $P{checkno}, $P{checkdate}, 'CHECK', 
	$P{amount}, $P{particulars}, $P{bankid}, $P{deposittype} 
)


