[findBillTotal]
select sum(
	rliq.basic - rliq.basicpaid - rliq.basicdisc + rliq.basicint +
	rliq.basicidle - rliq.basicidlepaid - rliq.basicidledisc + rliq.basicidleint +
	rliq.sef - rliq.sefpaid - rliq.sefdisc + rliq.sefint + 
	rliq.firecode - rliq.firecodepaid 
) as amount 
from rptbill b
inner join rptbill_ledger bl on b.objid = bl.billid 
inner join rptledgeritem_qtrly rliq on bl.rptledgerid = rliq.rptledgerid
where b.objid = $P{objid}
and (rliq.year < b.billtoyear  or 
	(rliq.year = b.billtoyear and rliq.qtr <= b.billtoqtr)
)
and rliq.fullypaid = 0

