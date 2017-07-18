update rptledger set
	lastbilledyear = 0,
	lastbilledqtr = 0,
	partialbasic = 0,
	partialbasicint = 0,
	partialbasicdisc = 0,
	partialsef = 0,
	partialsefint = 0,
	partialsefdisc = 0,
	partialledyear = 0,
	partialledqtr = 0;

alter table rptledger add column taxpayer_objid varchar(50);
alter table rptledger add column fullpin varchar(25);
alter table rptledger add column tdno varchar(25);
alter table rptledger add column cadastrallotno varchar(500);
alter table rptledger add column rputype varchar(15);
alter table rptledger add column txntype_objid varchar(50);
alter table rptledger add column classification_objid varchar(50);
alter table rptledger add column classcode varchar(10);
alter table rptledger add column totalav decimal(16,2);
alter table rptledger add column totalmv decimal(16,2);
alter table rptledger add column totalareaha decimal(16,2);
alter table rptledger add column taxable int(11);
alter table rptledger add column owner_name varchar(2000);
alter table rptledger add column prevtdno varchar(1000);


alter table rptledger change column lastbilledyear lastbilledyear int(11) not null default 0;
alter table rptledger change column lastbilledqtr lastbilledqtr int(11) not null default 0;
alter table rptledger change column partialbasic partialbasic decimal(16,2) not null default 0;
alter table rptledger change column partialbasicint partialbasicint decimal(16,2) not null default 0;
alter table rptledger change column partialbasicdisc partialbasicdisc decimal(16,2) not null default 0;
alter table rptledger change column partialsef partialsef decimal(16,2) not null default 0;
alter table rptledger change column partialsefint partialsefint decimal(16,2) not null default 0;
alter table rptledger change column partialsefdisc partialsefdisc decimal(16,2) not null default 0;
alter table rptledger change column partialledyear partialledyear int(11) not null default 0;
alter table rptledger change column partialledqtr partialledqtr int(11) not null default 0;



update rptledger r, faas f, rpu rpu, realproperty rp, propertyclassification pc   set 
	r.tdno = f.tdno,
	r.fullpin = f.fullpin,
	r.rputype = rpu.rputype,
	r.cadastrallotno = rp.cadastrallotno,
	r.totalmv = rpu.totalmv, 
	r.totalav = rpu.totalav, 
	r.totalareaha = rpu.totalareaha,
	r.classcode = pc.code,
	r.classification_objid = pc.objid,
	r.taxpayer_objid = f.taxpayer_objid,
	r.txntype_objid = f.txntype_objid,
	r.taxable = rpu.taxable,
	r.owner_name = f.owner_name,
	r.prevtdno = f.prevtdno
where r.faasid = f.objid and f.rpuid = rpu.objid 
  and f.realpropertyid = rp.objid
  and rpu.classification_objid = pc.objid;


ALTER TABLE `cashreceiptitem_rpt_noledger` 
	DROP FOREIGN KEY `cashreceiptitem_rpt_noledger_ibfk_1`


ALTER TABLE cashreceiptitem_rpt_account
	MODIFY COLUMN `rptledgerid`  varchar(50) NULL;