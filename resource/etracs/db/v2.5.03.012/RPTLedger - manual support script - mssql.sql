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

alter table rptledger add taxpayer_objid varchar(50);
alter table rptledger add fullpin varchar(25);
alter table rptledger add tdno varchar(25);
alter table rptledger add cadastrallotno varchar(500);
alter table rptledger add rputype varchar(15);
alter table rptledger add txntype_objid varchar(50);
alter table rptledger add classcode varchar(10);
alter table rptledger add totalav decimal(16,2);
alter table rptledger add totalmv decimal(16,2);
alter table rptledger add totalareaha decimal(16,2);
alter table rptledger add taxable int;
alter table rptledger add owner_name varchar(2000);
alter table rptledger add prevtdno varchar(1000);



update r set 
	r.tdno = f.tdno,
	r.fullpin = f.fullpin,
	r.rputype = rpu.rputype,
	r.cadastrallotno = rp.cadastrallotno,
	r.totalmv = rpu.totalmv, 
	r.totalav = rpu.totalav, 
	r.totalareaha = rpu.totalareaha,
	r.classcode = pc.code,
	r.taxpayer_objid = f.taxpayer_objid,
	r.txntype_objid = f.txntype_objid,
	r.taxable = rpu.taxable,
	r.owner_name = f.owner_name,
	r.prevtdno = f.prevtdno
from rptledger r, faas f, rpu rpu, realproperty rp, propertyclassification pc   	
where r.faasid = f.objid and f.rpuid = rpu.objid 
  and f.realpropertyid = rp.objid
  and rpu.classification_objid = pc.objid;