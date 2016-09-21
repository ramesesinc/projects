/* v2.5.04.032-03011 */
alter table cashreceiptitem_rpt_online drop constraint FK_cashreceiptitem_rpt_online_rptledgeritem
go 
alter table cashreceiptitem_rpt_online drop constraint FK_cashreceiptitem_rpt_online_rptledgeritemqtrly
go 



/* subdivision */

alter table subdivisionaffectedrpu add isnew int
go 
update subdivisionaffectedrpu set isnew = 0 where isnew is null
go 


delete from rptbill_ledger_item where billid in (
	select objid from rptbill where expirydate < '2016-09-01'
)
go 

delete from rptbill_ledger_account where billid in (
	select objid from rptbill where expirydate < '2016-09-01'
)
go 


delete from rptbill_ledger where billid in (
	select objid from rptbill where expirydate < '2016-09-01'
)
go 

delete from rptbill where expirydate < '2016-09-01'
go 