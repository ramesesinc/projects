[findRemittanceItemByTxnno]
select ci.* from remittance r
 inner join remittance_cashreceipt rc on rc.remittanceid = r.objid 
 inner join cashreceipt c on rc.objid = c.objid
 inner join cashreceiptitem ci on c.objid = ci.receiptid 
where r.txnno=$P{txnno}
	and ci.item_code = $P{itemcode}

[updateItem]
update ci 
	set ci.item_code = $P{code}, 
		ci.item_objid = $P{objid},
		ci.item_title = $P{title}
from remittance r
 inner join remittance_cashreceipt rc on rc.remittanceid = r.objid 
 inner join cashreceipt c on rc.objid = c.objid
 inner join cashreceiptitem ci on c.objid = ci.receiptid 
where r.txnno=$P{txnno}
	and ci.item_code = $P{itemcode}


[getReceitpsByTxnno]
select c.* from remittance r
 inner join remittance_cashreceipt rc on rc.remittanceid = r.objid 
 inner join cashreceipt c on rc.objid = c.objid
 left join cashreceipt_void cv on cv.receiptid = c.objid 
where r.txnno=$P{txnno}
	and cv.objid is null 
	and c.collector_name like '%barangay%' 
	and c.txnmode = 'CAPTURE' 
	and c.formno = '0016'

[getReceiptItems]
select * from cashreceiptitem where receiptid=$P{objid}

[updateReceiptItemAmount]
update cashreceiptitem set amount = (amount / 2) where receiptid=$P{objid} 

[insertctcbrgyshare]
insert into cashreceiptitem 
(
	objid, receiptid, item_objid, item_code, item_title, amount
)
values
(
	$P{objid}, $P{receiptid}, $P{itemid}, $P{itemcode}, $P{itemtitle}, $P{amount} 
)


[getRemittanceList]
select * from remittance where dtposted > '2013-12-12 00:00:00' and state='OPEN' 

[deleteRemittanceFund]
delete from remittance_fund where  remittanceid=$P{remittanceid} 

[recreateRemittanceFund]
insert into remittance_fund ( objid, remittanceid, fund_objid, fund_title, amount)
VALUES ( $P{objid}, $P{remittanceid}, $P{fundid}, $P{fundtitle}, $P{amount})


[getRemittanceFund]
SELECT  ri.fund_objid as fundid, ri.fund_title as fundtitle, SUM(chi.amount) AS amount
FROM remittance_cashreceipt c
INNER JOIN cashreceipt ch on c.objid = ch.objid 
INNER JOIN cashreceiptitem chi on chi.receiptid = ch.objid
INNER JOIN itemaccount ri on ri.objid = chi.item_objid
LEFT JOIN cashreceipt_void cv ON c.objid = cv.receiptid 
WHERE c.remittanceid = $P{remittanceid}
AND cv.objid IS NULL
GROUP BY c.remittanceid, ri.fund_objid, ri.fund_title

