[findBillTotal]
select sum(
    rliq.basic - rliq.basicpaid - rliq.basicdisc + rliq.basicint +
    rliq.basicidle - rliq.basicidlepaid - rliq.basicidledisc + rliq.basicidleint +
    rliq.sef - rliq.sefpaid - rliq.sefdisc + rliq.sefint +
    rliq.sh - rliq.shpaid - rliq.shdisc + rliq.shint +
    rliq.firecode - rliq.firecodepaid 
) as amount
from rptbill b
inner join rptbill_ledger bl on b.objid = bl.billid
inner join rptledgeritem_qtrly rliq on bl.rptledgerid = rliq.rptledgerid
where b.objid = $P{objid}
and rliq.fullypaid = 0 
and (rliq.year < b.billtoyear or (rliq.year = b.billtoyear and rliq.qtr <= b.billtoqtr))