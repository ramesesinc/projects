[dropXLedgerTable]
drop table xrptledger


[createXLedgerTable]
create table xrptledger(
	objid varchar(50),
	primary key(objid)
)

[createLedgerMigrateTable]
create table etracs25_migrate_rptledger
(
	objid varchar(50),
	primary key(objid)
)

[createLedgerMigrateLogTable]
create table etracs25_migrate_rptledger_log
(
	objid varchar(50),
	log text, 
	primary key(objid)
)


[logMigratedLedger]
insert into etracs25_migrate_rptledger
	(objid)
values 
	($P{objid})


[logMigrateError]
insert into etracs25_migrate_rptledger_log
	(objid, log)
values
	($P{objid}, $P{log})

[deleteXLedger]
delete from xrptledger where objid = $P{objid}




[insertLedgers]
insert into xrptledger
select objid 
from rptledger rl 
where not exists(select * from etracs25_migrate_rptledger where objid = rl.objid)
  and not exists(select * from etracs25_migrate_rptledger_log where objid = rl.objid)

[findLedgerForMigrationCount]
select count(*) as count from xrptledger 


[getLedgersForMigration]
select * from xrptledger


[findLedgerById]
select 
  rl.objid, 
  rl.docstate as state, 
  rl.faasid, 
  null as nextbilldate, 
  rl.lastyearpaid, 
  rl.lastqtrpaid, 
  rl.firstqtrpaidontime, 
  0 as qtrlypaymentavailed, 
  rl.quarterlyinstallmentpaidontime as qtrlypaymentpaidontime, 
  0 as lastitemyear, 
  0 as lastreceiptid, 
  0 as advancebill, 
  0 as lastbilledyear, 
  0 as lastbilledqtr, 
  rl.partialbasic, 
  rl.partialbasicint, 
  0 as partialbasicdisc, 
  rl.partialsef, 
  rl.partialsefint, 
  0 as partialsefdisc, 
  0 as partialledyear, 
  0 as partialledqtr, 
  0 as undercompromise
from rptledger rl 
where objid = $P{objid}
  
[findLedgerByTdno]
select 
  rl.objid, 
  rl.docstate as state, 
  rl.faasid, 
  null as nextbilldate, 
  rl.lastyearpaid, 
  rl.lastqtrpaid, 
  rl.firstqtrpaidontime, 
  0 as qtrlypaymentavailed, 
  rl.quarterlyinstallmentpaidontime as qtrlypaymentpaidontime, 
  0 as lastitemyear, 
  0 as lastreceiptid, 
  0 as advancebill, 
  0 as lastbilledyear, 
  0 as lastbilledqtr, 
  rl.partialbasic, 
  rl.partialbasicint, 
  0 as partialbasicdisc, 
  rl.partialsef, 
  rl.partialsefint, 
  0 as partialsefdisc, 
  0 as partialledyear, 
  0 as partialledqtr, 
  0 as undercompromise
from rptledger rl 
where tdno = $P{tdno}

[findFaasById]
select 
  f.taxpayer_objid, 
  f.fullpin, 
  f.tdno, 
  rp.cadastrallotno, 
  r.rputype, 
  f.txntype_objid, 
  pc.code as classcode, 
  r.totalav, 
  r.totalmv, 
  r.totalareaha, 
  r.taxable, 
  f.owner_name, 
  f.prevtdno, 
  r.classification_objid, 
  f.titleno, 
  0 as undercompromise
from faas f 
  inner join rpu r on f.rpuid = r.objid 
  inner join realproperty rp on f.realpropertyid = rp.objid 
  inner join propertyclassification pc on r.classification_objid = pc.objid 
where f.objid = $P{objid}

[getLedgerFaases]
select
  rli.objid, 
  rli.docstate as state, 
  rli.parentid as rptledgerid, 
  rli.faasid, 
  rli.tdno, 
  rli.txntype as txntype_objid, 
  rli.classid as classification_objid, 
  rli.actualuseid as actualuse_objid, 
  rli.taxable, 
  rli.backtax, 
  rli.fromyear, 
  1 as fromqtr, 
  rli.toyear, 
  4 as toqtr, 
  rli.assessedvalue, 
  rli.systemcreated, 
  0 as reclassed, 
  0 as idleland
from rptledgeritem rli 
where rli.parentid = $P{objid}


[getLedgerCredits]
select
  p.objid, 
  p.rptledgerid, 
  p.mode as type, 
  p.receiptno as refno, 
  p.receiptdate as refdate, 
  r.payorid, 
  r.payorname as paidby_name, 
  r.payoraddress as  paidby_address, 
  r.collectorname as collector, 
  case when r.objid is null then p.capturedby else r.collectorname end as postedby, 
  case when r.objid is null then '-' else r.collectortitle end as postedbytitle, 
  case when r.objid is null then p.receiptdate else r.txndate end as dtposted, 
  p.fromyear, 
  p.fromqtr, 
  p.toyear, 
  p.toqtr, 
  p.basic, 
  p.basicint, 
  p.basicdisc, 
  0.0 as basicidle, 
  p.sef, 
  p.sefint, 
  p.sefdisc, 
  0.0 firecode, 
  (p.basic + p.basicint - p.basicdisc + p.sef + p.sefint - p.sefdisc) as amount, 
  '-' as collectingagency
from rptpayment p 
  inner join receiptlist r on p.receiptid = r.objid
where p.rptledgerid = $P{objid}


