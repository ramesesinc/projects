[getList]
SELECT 
	c.objid, c.state, c.txnno, c.txndate, c.rptledgerid, 
	c.secondpartyname, c.term, c.numofinstallment, 
	c.downpaymentrequired, c.downpayment, c.downpaymentorno,
	c.amount, c.amtpaid, c.enddate, c.cypaymentrequired, c.cypaymentorno, 
	rl.tdno, e.name AS taxpayer_name, e.address_text AS taxpayer_address, 
	rl.fullpin, rl.cadastrallotno
FROM rptledger_compromise c 
	INNER JOIN rptledger rl ON c.rptledgerid = rl.objid 
	INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
WHERE (c.txnno LIKE $P{searchtext} 
   OR rl.tdno LIKE $P{searchtext} 
   OR rl.owner_name LIKE $P{searchtext}
   OR rl.cadastrallotno LIKE $P{searchtext}
   OR rl.taxpayer_objid LIKE $P{searchtext})
ORDER BY c.txnno 


[findLedgerById]
SELECT rl.*, e.name AS taxpayer_name, e.address_text AS taxpayer_address  
FROM rptledger rl
	INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
WHERE rl.objid = $P{objid}


[getBillItems]
SELECT 
	li.year,
	li.qtr,
	li.rptledgerid,
	rl.faasid,
	rl.totalav AS assessedvalue,
	rl.tdno,
	rl.classcode,
	rl.classcode AS actualusecode,
	li.basic,
	0.0 AS basicpaid,
	li.basicint,
	0.0 AS basicintpaid,
	li.basicidle,
	0.0 AS basicidlepaid,
	li.basicidleint,
	0.0 AS basicidleintpaid,
	li.sef,
	0.0 AS sefpaid,
	li.sefint,
	0.0 AS sefintpaid,
	li.firecode,
	0.0 AS firecodepaid
FROM rptledger rl 
	INNER JOIN rptbill_ledger_item li ON rl.objid = li.rptledgerid
WHERE rl.objid = $P{rptledgerid}
  AND li.billid = $P{billid}
  AND ( li.year < $P{endyear} OR ( li.year = $P{endyear} AND li.qtr <= $P{endqtr} ) )
ORDER BY li.year, li.qtr   






[getLookupList]
SELECT c.* , rl.tdno, rl.cadastrallotno 
FROM rptledger_compromise c 
	INNER JOIN rptledger rl ON c.rptledgerid = rl.objid 
WHERE ${whereclause} 

[findRPTCompromiseById]
SELECT * 
FROM rptledger_compromise
WHERE objid = $P{objid} 

[getRPTCompromiseItems]
SELECT 
	f.* , 
	f.basicpaid + f.basicintpaid + f.basicidlepaid + f.basicidleintpaid + f.sefpaid + f.sefintpaid as payment
FROM rptledger_compromise_item f
WHERE f.rptcompromiseid  = $P{rptcompromiseid}
ORDER BY f.year, f.qtr 

[getRPTCompromiseInstallments]
SELECT * 
FROM rptledger_compromise_installment  
WHERE rptcompromiseid = $P{rptcompromiseid} 
ORDER BY installmentno


[getRPTCompromiseCredits]
SELECT 
	cr.remarks,
	cr.ordate,
	cr.orno,
	cr.amount,
	cr.collector_name AS collectorname,
	cr.paidby
FROM rptledger_compromise_credit cr	
WHERE cr.rptcompromiseid = $P{rptcompromiseid}	 
ORDER BY cr.ordate DESC, cr.orno DESC  

[findActiveCompromiseByLedgerId]
SELECT * 
FROM rptledger_compromise
WHERE rptledgerid = $P{rptledgerid} 
  AND state NOT IN ('CLOSED')

  
[setLedgerUnderCompromised]
UPDATE rptledger SET 
	undercompromise = 1 
WHERE objid = $P{objid} 

[resetLedgerUnderCompromised]
UPDATE rptledger SET 
	undercompromise = 0 
WHERE objid = $P{objid} 

[updateLastYearQtrPaid]
UPDATE rptledger SET	
	lastyearpaid = $P{lastyearpaid},
	lastqtrpaid = $P{lastqtrpaid} 
WHERE objid = $P{objid}	


	
[deleteItems]	
DELETE FROM rptledger_compromise_item 
WHERE rptcompromiseid = $P{objid} 

[deleteInstallments]	
DELETE FROM rptledger_compromise_installment
WHERE rptcompromiseid = $P{objid} 

[getOpenInstallments]
SELECT  * 
FROM rptledger_compromise_installment 
WHERE rptcompromiseid = $P{rptcompromiseid} 
  AND (fullypaid = 0 or amount - amtpaid > 0 )
ORDER BY installmentno 

[updateInstallmentPayment]
UPDATE rptledger_compromise_installment SET 
	fullypaid = CASE WHEN amount = amtpaid + $P{amtpaid} THEN 1 ELSE 0 END ,
	amtpaid = amtpaid + $P{amtpaid}
WHERE objid = $P{objid}	 

[updateCapturedInstallmentPayment]
UPDATE rptledger_compromise_installment SET 
	fullypaid = 1,
	amtpaid = amount 
WHERE objid = $P{objid}	 
	
	
[findFaasInfo]	
SELECT 
	rl.tdno, rl.fullpin, rl.cadastrallotno, b.name AS barangay, 
	rl.totalmv, rl.totalav AS assessedvalue,
	e.name AS taxpayer_name, e.address_text AS taxpayer_address 
FROM rptledger rl 
	INNER JOIN barangay b ON rl.barangayid = b.objid 
	INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
WHERE rl.objid = $P{objid}
  


[getCredits]
SELECT *
FROM rptledger_compromise_credit 
WHERE receiptid = $P{receiptid} 

	



[getPaidInstallmentsByReceipt]
SELECT i.*
FROM rptledger_compromise_installment  i 
	inner join rptledger_compromise_credit c on i.objid = c.installmentid 
WHERE c.rptreceiptid = $P{objid}
ORDER BY i.installmentno



[getUnpaidInstallments]
SELECT c.*, 
	c.amount - c.amtpaid AS balance,
	0.0 AS amtdue
FROM rptledger_compromise_installment  c 
WHERE rptcompromiseid = $P{rptcompromiseid} 
  AND fullypaid = 0
ORDER BY installmentno


[getUnpaidItems]
select 
	objid, rptcompromiseid, year, qtr, 
	(basic - basicpaid ) as basic,
	(basicint - basicintpaid ) as basicint,
	(basicidle - basicidlepaid ) as basicidle,
	(basicidleint - basicidleintpaid ) as basicidleint,
	(sef - sefpaid ) as sef,
	(sefint - sefintpaid ) as sefint,
	(firecode - firecodepaid ) as firecode
from rptledger_compromise_item
where rptcompromiseid = $P{objid}
  and fullypaid = 0
order by year, qtr  


[getItemsForPrinting]
SELECT 
	rl.objid AS rptledgerid,
	rl.tdno,
	rl.owner_name, 
	rl.rputype,
	rl.totalav, 
	rl.fullpin,
	rl.cadastrallotno,
	rl.classcode,
	rl.totalareaha * 10000 as totalareasqm,
	b.name AS barangay,
	md.name as munidistrict,
	pct.name as provcity, 
	cc.basic,
	0.0 AS basicdisc,
	cc.basicint,
	cc.basicint as basicdp,
	cc.basic + cc.basicint as basicnet,
	(cc.basicidle + cc.basicidleint) AS basicidle,
	cc.sef,
	0.0 as sefdisc,
	cc.sefint,
	cc.sefint as sefdp,
	cc.sef + cc.sefint as sefnet,
	cc.firecode,
	cc.basic + cc.basicint + cc.basicidle + cc.basicidleint + cc.sef + cc.sefint + cc.firecode as amount,
	cc.partial,
	ci.installmentno
FROM rptledger_compromise_credit cc 
	inner join rptledger_compromise_installment ci ON cc.installmentid = ci.objid 
	INNER JOIN rptledger_compromise c ON cc.rptcompromiseid = c.objid 
	INNER JOIN rptledger rl ON c.rptledgerid = rl.objid 
	INNER JOIN sys_org b ON rl.barangayid = b.objid
	inner join sys_org md on md.objid = b.parent_objid 
	inner join sys_org pct on pct.objid = md.parent_objid
WHERE cc.receiptid = $P{objid}
ORDER BY ci.installmentno 



[fullyPaidCompromiseItem]
UPDATE rptledger_compromise_item SET
	basicpaid = basic,
	basicintpaid = basicint,
	basicidlepaid = basicidle,
	basicidleintpaid = basicidleint,
	sefpaid = sef,
	sefintpaid = sefint,
	firecodepaid = firecode,
	fullypaid = 1
WHERE objid = $P{itemid}	


[partiallyPaidCompromiseItem]
UPDATE rptledger_compromise_item SET
	basicpaid = basicpaid + $P{basic},
	basicintpaid = basicintpaid + $P{basicint},
	basicidlepaid = basicidlepaid + $P{basicidle},
	basicidleintpaid = basicidleintpaid + $P{basicidleint},
	sefpaid = sefpaid + $P{sef},
	sefintpaid = sefintpaid + $P{sefint},
	firecodepaid = firecodepaid + $P{firecode},
	fullypaid = 0
WHERE objid = $P{itemid}	


[postInstallmentPayment]
UPDATE rptledger_compromise_installment SET 
	fullypaid = CASE WHEN amount = amtpaid + $P{amtdue} 
					THEN 1
					ELSE 0
				END,
	amtpaid = amtpaid + $P{amtdue}
WHERE objid = $P{objid}


[updateCompromiseAmountPaid]
UPDATE rptledger_compromise SET 
	state = CASE WHEN amtpaid + $P{amtpaid} >= amount 
				THEN 'CLOSED'
				ELSE state 
			END,
	amtpaid = amtpaid + $P{amtpaid}
WHERE objid = $P{objid}	






[findCompromiseByReceiptForVoiding]
SELECT DISTINCT cr.rptcompromiseid, cr.rptreceiptid
FROM rptledger_compromise_credit cr 
WHERE rptreceiptid = $P{objid}


[voidCompromiseCredit]
UPDATE rc SET
	rc.amtpaid = rc.amtpaid - $P{debitamount},
	rc.state = case when rc.state = 'CLOSED' then 'APPROVED' else rc.state END 
from rptledger_compromise rc 
WHERE rc.objid = $P{rptcompromiseid}


[voidItemCredits]
UPDATE i SET 
	i.basicpaid = i.basicpaid - cr.basic,
	i.basicintpaid = i.basicintpaid - cr.basicint,
	i.basicidlepaid = i.basicidlepaid - cr.basicidle,
	i.basicidleintpaid = i.basicidleintpaid - cr.basicidleint,
	i.sefpaid = i.sefpaid - cr.sef,
	i.sefintpaid = i.sefintpaid - cr.sefint,
	i.firecodepaid = i.firecodepaid - cr.firecode,
	i.fullypaid = 0
from rptledger_compromise_item i, rptledger_compromise_item_credit cr 
WHERE i.objid = cr.rptcompromiseitemid 
  AND i.rptcompromiseid = $P{rptcompromiseid}
  AND cr.rptreceiptid = $P{rptreceiptid}


[voidInstallmentCredits]  
UPDATE ci SET
	ci.amtpaid = ci.amtpaid - cr.amount,
	ci.fullypaid = 0
from rptledger_compromise_installment ci, rptledger_compromise_credit cr 
WHERE ci.objid = cr.installmentid 
  AND ci.rptcompromiseid = $P{rptcompromiseid}
  AND cr.rptreceiptid = $P{rptreceiptid}


[deleteVoidedItemCredit]
DELETE FROM rptledger_compromise_item_credit WHERE rptreceiptid = $P{rptreceiptid}


[deleteVoidedCredit]
DELETE FROM rptledger_compromise_credit WHERE rptreceiptid = $P{rptreceiptid}


[updateDownpaymentPaymentInfo]
UPDATE rptledger_compromise SET 
	downpaymentreceiptid = $P{objid},
	downpaymentorno = $P{receiptno},
	downpaymentordate = $P{receiptdate}
WHERE objid = $P{rptcompromiseid}


[updateCurrentYearPaymentInfo]
UPDATE rptledger_compromise SET 
	cypaymentreceiptid = $P{objid},
	cypaymentorno = $P{receiptno},
	cypaymentordate = $P{receiptdate}
WHERE objid = $P{rptcompromiseid}



[getCurrentYearTaxes]
SELECT ba.*
FROM rptledger rl
		INNER JOIN rptbill_ledger_account ba ON rl.objid = ba.rptledgerid
WHERE rl.objid = $P{rptledgerid}  


[fullyPayLedgerItems]
update rptledgeritem set 
	fullypaid = 1,
	basicpaid = basic,
	basicintpaid = basicint,
	basicdisctaken = basicdisc,
	basicidlepaid = basicidle,
	basicidledisctaken = basicidledisc,
	basicidleintpaid = basicidleint,
	sefpaid = sef,
	sefintpaid = sefint,
	sefdisctaken = sefdisc,
	firecodepaid = firecode
where rptledgerid = $P{objid}
  and year <= $P{lastyearpaid}
  and fullypaid = 0 

[fullyPayQtrlyLedgerItems]
update rptledgeritem_qtrly set 
	fullypaid = 1,
	partialled = 0,
	basicpaid = basic,
	basicintpaid = basicint,
	basicdisctaken = basicdisc,
	basicidlepaid = basicidle,
	basicidledisctaken = basicidledisc,
	basicidleintpaid = basicidleint,
	sefpaid = sef,
	sefintpaid = sefint,
	sefdisctaken = sefdisc,
	firecodepaid = firecode
where rptledgerid = $P{objid}
  and year <= $P{lastyearpaid}
  and fullypaid = 0 



[getDefaultedCompromises]
select objid, rptledgerid, txnno, startyear, endyear   
from rptledger_compromise
where DATEADD(D, 1, enddate) < $P{currentdate}
  and state = 'APPROVED' 


[setDefaultedCompromise]
update rptledger_compromise set 
	state = 'DEFAULTED'
where objid = $P{objid}	


[setDefaultedLedger]
update rptledger set 
	nextbilldate = null,
	undercompromise = 0,
	lastyearpaid = $P{lastyearpaid},
	lastqtrpaid = $P{lastqtrpaid}
where objid = $P{rptledgerid}

[findLastPaidCompromiseItem]
select * 
from rptledger_compromise_item
where rptcompromiseid = $P{objid}
  and fullypaid = 1
order by year desc, qtr desc 

[findFirstUnpaidCompromiseItem]
select * 
from rptledger_compromise_item
where rptcompromiseid = $P{objid}
  and fullypaid = 0
order by year, qtr




[resetLedgerItemPaidInfo]
update rptledgeritem set 
	fullypaid = 0,
	basicpaid = 0.0,
	basicintpaid = 0.0,
	basicdisctaken = 0.0,
	basicidlepaid = 0.0,
	basicidledisctaken = 0.0,
	basicidleintpaid = 0.0,
	sefpaid = 0.0,
	sefintpaid = 0.0,
	sefdisctaken = 0.0,
	firecodepaid = 0.0
where rptledgerid = $P{rptledgerid}
  and year >= $P{fromyear} 
  and year <= $P{toyear}

[resetLedgerItemQtrlyPaidInfo]  
update rptledgeritem_qtrly set 
	fullypaid = 0,
	basicpaid = 0.0,
	basicintpaid = 0.0,
	basicdisctaken = 0.0,
	basicidlepaid = 0.0,
	basicidledisctaken = 0.0,
	basicidleintpaid = 0.0,
	sefpaid = 0.0,
	sefintpaid = 0.0,
	sefdisctaken = 0.0,
	firecodepaid = 0.0
where rptledgerid = $P{rptledgerid}
  and year >= $P{fromyear} 
  and year <= $P{toyear}

  
[getPartialledQtrlyItems]
select * 
from rptledgeritem_qtrly 
where rptledgerid = $P{rptledgerid} 
and year = $P{partialledyear} 
order by year, qtr 

[findLedgerItem]
select * from rptledgeritem where rptledgerid = $P{rptledgerid} and year = $P{partialledyear}



[findDefaultedInstallment]
select * 
from rptledger_compromise c 
	inner join rptledger_compromise_installment ci on c.objid = ci.rptcompromiseid
where c.objid = $P{objid}
  and c.state = 'APPROVED' 
  and DATEADD(D, 1, ci.duedate) < $P{currentdate}
  and ci.amount > ci.amtpaid 
  

[fullyPaidItem]	
update rptledger_compromise_item set 
	fullypaid = 1,
	basicpaid = basic,
	basicintpaid = basicint,
	basicidlepaid = basicidle,
	basicidleintpaid = basicidleint,
	sefpaid = sef,
	sefintpaid = sefint,
	firecodepaid = firecode
where objid = $P{objid}


[findCompromiseReferenceByLedger]
select objid, state, txnno
from rptledger_compromise 
where rptledgerid = $P{objid}
and state not in ('DEFAULTED', 'CLOSED')


[deleteLedgerCompromiseCredit]
delete from rptledger_credit 
where rptledgerid = $P{objid} 
  and type = 'COMPROMISE'


[resetLedgerItemsPaidFlag]  
update rptledgeritem set 
	fullypaid = 0
where rptledgerid = $P{objid}
and year >= $P{startyear}


[updateLedgerItemStartYearQtrPaid]
update rptledgeritem set 
	fullypaid = 0
where rptledgerid = $P{objid}
  and year = $P{startyear}


[findCompromiseByReceipt]
select c.*
from rptledger_compromise c 
	inner join rptledger_compromise_credit cr on c.objid = cr.rptcompromiseid  
where cr.rptreceiptid = $P{objid}


[updateLedgerLastYearQtrPaid]
update rptledger set 
	lastyearpaid = $P{endyear},
	lastqtrpaid = $P{endqtr}
where objid = 	$P{rptledgerid}

