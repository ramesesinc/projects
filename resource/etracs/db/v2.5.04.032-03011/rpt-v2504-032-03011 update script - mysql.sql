/* v2.5.04.032-03011 */
alter table cashreceiptitem_rpt_online drop foreign key FK_cashreceiptitem_rpt_online_rptledgeritem;
alter table cashreceiptitem_rpt_online drop foreign key FK_cashreceiptitem_rpt_online_rptledgeritemqtrly;


/* subdivision */

alter table subdivisionaffectedrpu add isnew int;
update subdivisionaffectedrpu set isnew = 0 where isnew is null;