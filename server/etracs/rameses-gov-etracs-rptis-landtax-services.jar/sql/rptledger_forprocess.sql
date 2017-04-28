[insertItemsForProcess]
insert into rptledger_forprocess (objid)
select objid 
from rptledger rl 
where state = 'APPROVED'
and lastyearpaid = $P{lastyearpaid}
and lastqtrpaid = $P{lastqtrpaid}
and not exists(select * from rptledger_compromise where rptledgerid = rl.objid and state='APPROVED')


[deleteLedgerQtrlyItems]
delete from rptledgeritem_qtrly where parentid in (
	select objid from rptledgeritem where rptledgerid = $P{objid} and year <= $P{toyear} and fullypaid = 1
)


[deleteLedgerItems]
delete from rptledgeritem where rptledgerid = $P{objid} and year <= $P{toyear} and fullypaid = 1

