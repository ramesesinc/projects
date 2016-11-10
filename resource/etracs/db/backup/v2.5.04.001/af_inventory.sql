CREATE TABLE `af` (
  `objid` VARCHAR(50) NOT NULL,
  `title` VARCHAR(255) DEFAULT NULL,
  `usetype` VARCHAR(50) DEFAULT NULL,
  `serieslength` INT(11) DEFAULT NULL,
  `system` INT(11) DEFAULT NULL,
  `denomination` DECIMAL(10,2) DEFAULT NULL,
  `formtype` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `af_control` (
  `objid` VARCHAR(50) NOT NULL,
  `afid` VARCHAR(50) DEFAULT NULL,
  `txnmode` VARCHAR(10) DEFAULT NULL,
  `assignee_objid` VARCHAR(50) DEFAULT NULL,
  `assignee_name` VARCHAR(50) DEFAULT NULL,
  `startseries` INT(11) DEFAULT NULL,
  `currentseries` INT(11) DEFAULT NULL,
  `endseries` INT(11) DEFAULT NULL,
  `active` INT(11) DEFAULT NULL,
  `org_objid` VARCHAR(50) DEFAULT NULL,
  `org_name` VARCHAR(50) DEFAULT NULL,
  `fund_objid` VARCHAR(50) DEFAULT NULL,
  `fund_title` VARCHAR(200) DEFAULT NULL,
  `stubno` INT(11) DEFAULT NULL,
  `owner_objid` VARCHAR(50) DEFAULT NULL,
  `owner_name` VARCHAR(255) DEFAULT NULL,
  `prefix` VARCHAR(10) DEFAULT NULL,
  `suffix` VARCHAR(10) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_org_objid` (`org_objid`),
  KEY `ix_org_name` (`org_name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `af_inventory` (
  `objid` VARCHAR(50) NOT NULL,
  `afid` VARCHAR(50) DEFAULT NULL,
  `respcenter_objid` VARCHAR(50) DEFAULT NULL,
  `respcenter_name` VARCHAR(50) DEFAULT NULL,
  `respcenter_type` VARCHAR(25) DEFAULT NULL,
  `startseries` INT(11) DEFAULT NULL,
  `endseries` INT(11) DEFAULT NULL,
  `currentseries` INT(11) DEFAULT NULL,
  `startstub` INT(11) DEFAULT NULL,
  `endstub` INT(11) DEFAULT NULL,
  `currentstub` INT(11) DEFAULT NULL,
  `prefix` VARCHAR(10) DEFAULT NULL,
  `suffix` VARCHAR(10) DEFAULT NULL,
  `unit` VARCHAR(10) DEFAULT NULL,
  `qtyin` INT(11) DEFAULT NULL,
  `qtyout` INT(11) DEFAULT NULL,
  `qtycancelled` INT(11) DEFAULT NULL,
  `qtybalance` INT(11) DEFAULT NULL,
  `currentlineno` INT(11) DEFAULT NULL,
  `cost` DECIMAL(10,2) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `ix_afid` (`afid`),
  KEY `ix_respcenter_objid` (`respcenter_objid`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `af_inventory_detail` (
  `objid` VARCHAR(50) NOT NULL,
  `controlid` VARCHAR(50) DEFAULT NULL,
  `lineno` INT(11) DEFAULT NULL,
  `refid` VARCHAR(50) DEFAULT NULL,
  `refno` VARCHAR(50) DEFAULT NULL,
  `reftype` VARCHAR(50) DEFAULT NULL,
  `refdate` DATETIME DEFAULT NULL,
  `txndate` DATETIME DEFAULT NULL,
  `txntype` VARCHAR(25) DEFAULT NULL,
  `receivedstartseries` INT(11) DEFAULT NULL,
  `receivedendseries` INT(11) DEFAULT NULL,
  `beginstartseries` INT(11) DEFAULT NULL,
  `beginendseries` INT(11) DEFAULT NULL,
  `issuedstartseries` INT(11) DEFAULT NULL,
  `issuedendseries` INT(11) DEFAULT NULL,
  `endingstartseries` INT(11) DEFAULT NULL,
  `endingendseries` INT(11) DEFAULT NULL,
  `cancelledstartseries` INT(11) DEFAULT NULL,
  `cancelledendseries` INT(11) DEFAULT NULL,
  `qtyreceived` INT(11) DEFAULT NULL,
  `qtybegin` INT(11) DEFAULT NULL,
  `qtyissued` INT(11) DEFAULT NULL,
  `qtyending` INT(11) DEFAULT NULL,
  `qtycancelled` INT(11) DEFAULT NULL,
  `remarks` VARCHAR(255) DEFAULT NULL,
  `cost` DECIMAL(10,2) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  KEY `FK_afcontrol_detail_afcontrol` (`controlid`),
  KEY `ix_refid` (`refid`),
  KEY `ix_refno` (`refno`),
  KEY `ix_refdate` (`refdate`),
  KEY `ix_txndate` (`txndate`),
  CONSTRAINT `af_inventory_detail_ibfk_1` FOREIGN KEY (`controlid`) REFERENCES `af_inventory` (`objid`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `af_inventory_detail_cancelseries` (
  `objid` VARCHAR(50) NOT NULL,
  `series` INT(11) NOT NULL,
  `controlid` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`objid`,`series`),
  KEY `ix_controlid` (`controlid`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


insert into af
( objid, title, usetype, serieslength, system, denomination, formtype)

select 
   cf.objid, cf.title, 'collection', af.serieslength, 1, 0.0, cf.formtype
from collectionform cf 
    inner join afserial af on af.objid = cf.objid 
union 
select 
   cf.objid, cf.title, 'collection', 0, 1, af.denomination, cf.formtype
from collectionform cf 
    inner join cashticket af on af.objid = cf.objid ;
    
Alter table cashticket_inventory drop foreign key cashticket_inventory_ibfk_1;    
    
drop table afserial;
drop table cashticket;
drop table collectionform ;

insert into af_inventory
select * from afserial_inventory;

insert into af_inventory_detail
select * from afserial_inventory_detail;

insert into af_inventory_detail_cancelseries
select * from afserial_inventory_detail_cancelseries;

drop table afserialcapture;

insert into af_control
(    objid,
     afid,
     txnmode,
     assignee_objid,
     assignee_name,
     startseries,
     currentseries,
     endseries,
     active,
     org_objid,
     org_name,
     fund_objid,
     fund_title,
     stubno,
     owner_objid,
     owner_name,
     prefix,
     suffix )
select
    ctr.controlid, ai.afid, ctr.txnmode, ifnull(ctr.assignee_objid, ai.respcenter_objid) as assignee_objid,
    ifnull(ctr.assignee_name, ai.respcenter_name) as assignee_name, ai.startseries, ctr.currentseries, ai.endseries,
    ctr.active, ctr.org_objid, ctr.org_name, ctr.fund_objid, ctr.fund_title, ai.startstub, ai.respcenter_objid, ai.respcenter_name,
    ai.prefix, ai.suffix
from afserial_control ctr
    inner join afserial_inventory ai on ai.objid = ctr.controlid ;
    
Alter table remittance_afserial drop foreign key remittance_afserial_ibfk_1;


drop table afserial_inventory_detail;
drop table afserial_inventory_detail_cancelseries;
drop table afserial_control;
drop table afserial_inventory;
drop table afstockcontrol;
    

insert into af_inventory
( 
    objid, afid, respcenter_objid, respcenter_name, respcenter_type, startseries, endseries, currentseries,
    startstub, endstub, currentstub, prefix, suffix, unit, qtyin, qtyout, qtycancelled, qtybalance, currentlineno, cost 
)
select 
	objid, afid, respcenter_objid, respcenter_name, respcenter_type, 1, qtyin, (( qtyin - qtybalance) + 1) as currentseries, 
	startstub, endstub, currentstub, null, null, 'PAD', qtyin, qtyout, qtycancelled, qtybalance, currentlineno, cost 
from cashticket_inventory ;


insert into af_inventory_detail
(
    objid, controlid, lineno, refid, refno, reftype, refdate, txndate, txntype, receivedstartseries, receivedendseries,
    beginstartseries, beginendseries, issuedstartseries, issuedendseries, endingstartseries, endingendseries, 
    cancelledstartseries, cancelledendseries, qtyreceived, qtybegin, qtyissued, qtyending, qtycancelled, remarks, cost 
)
select 
    objid, controlid, lineno, refid, refno, reftype, refdate, txndate, txntype, 
    case when qtyreceived > 0 then 1 else null end as receivedstartseries,
    case when qtyreceived > 0 then qtyreceived  else null end as receivedendseries, 
    case when qtybegin > 0 then 1 else null end as beginstartseries, 
    case when qtybegin > 0 then qtybegin else null end as beginendseries, 
    case when qtyissued > 0 then 1 else null end as issuedstartseries, 
    case when qtyissued > 0 then qtyissued else null end as issuedendseries, 
    case when qtyending > 0 then 1 else null end as endingstartseries, 
    case when qtyending > 0 then qtyending else null end as endingendseries, 
    null as cancelledstartseries, null as cancelledendseries, 
    qtyreceived, qtybegin, qtyissued, qtyending, qtycancelled, remarks, cost 
from cashticket_inventory_detail;


insert into af_control
(  
  objid, afid, txnmode, assignee_objid, assignee_name, startseries, currentseries, endseries,
  active, org_objid, org_name, fund_objid, fund_title, stubno, owner_objid, owner_name, prefix, suffix 
)
select
    ctr.controlid, ai.afid, 'ONLINE' AS txnmode, ifnull(ctr.assignee_objid, ai.respcenter_objid) as assignee_objid,
    ifnull(ctr.assignee_name, ai.respcenter_name) as assignee_name, 1 AS startseries, (( ai.qtyin - ai.qtybalance) + 1) as currentseries , ai.qtyin,
    0 as active, ctr.org_objid, ctr.org_name, null as fund_objid, null as fund_title, ai.startstub, ai.respcenter_objid, ai.respcenter_name,
    null as prefix, null as suffix
from cashticket_control ctr
    inner join cashticket_inventory ai on ai.objid = ctr.controlid ;
    
Alter table remittance_cashticket drop foreign key remittance_cashticket_ibfk_1;


drop table cashticket_inventory_detail;
drop table cashticket_control;
drop table cashticket_inventory;

update stockitem set 
 type ='SERIAL'
Where type ='AFSERIAL';