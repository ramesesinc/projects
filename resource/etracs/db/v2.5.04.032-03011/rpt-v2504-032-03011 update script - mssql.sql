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