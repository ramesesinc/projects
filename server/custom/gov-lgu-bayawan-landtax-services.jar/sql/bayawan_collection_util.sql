[findReceipt]
select * 
from cashreceipt 
where receiptno = $P{receiptno}
and formno = '56' 


[findItemTotal]
select 
	sum(basic + basicint - basicdisc + 
	    sef + sefint - sefdisc + firecode + 
	    basicidle + basicidleint - basicidledisc) as amount 
from cashreceiptitem_rpt_online 
where rptreceiptid = $P{objid}


[findAccountTotal]
select sum(amount) as amount 
from cashreceiptitem_rpt_account 
where rptreceiptid = $P{objid}



[getLedgers]
select * from rptledger where tdno = $P{tdno}

[findLedgerFaas]
select * 
from rptledgerfaas 
where rptledgerid = $P{rptledgerid}
and $P{year} >= fromyear 
and ($P{year} <= toyear or toyear = 0)


[deletePostedItems]
delete from cashreceiptitem_rpt_online
where rptreceiptid = $P{rptreceiptid}
and year = $P{year}
and objid like 'SYSI-%'