/* v2.5.04.032-03011 */
alter table cashreceiptitem_rpt_online drop foreign key FK_cashreceiptitem_rpt_online_rptledgeritem;
alter table cashreceiptitem_rpt_online drop foreign key FK_cashreceiptitem_rpt_online_rptledgeritemqtrly;


/* subdivision */

alter table subdivisionaffectedrpu add isnew int;
update subdivisionaffectedrpu set isnew = 0 where isnew is null;


delete from rptbill_ledger_item where billid in (
	select objid from rptbill where expirydate < '2016-09-01'
);

delete from rptbill_ledger_account where billid in (
	select objid from rptbill where expirydate < '2016-09-01'
);


delete from rptbill_ledger where billid in (
	select objid from rptbill where expirydate < '2016-09-01'
);

delete from rptbill where expirydate < '2016-09-01';


