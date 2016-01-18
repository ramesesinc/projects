
/* v2.5.04.030b */

alter table rptledger
  modify column firstqtrpaidontime int null,
  modify column qtrlypaymentavailed int null,
  modify column qtrlypaymentpaidontime int null,
  modify column lastitemyear int null,
  modify column lastreceiptid varchar(50) null,
  modify column advancebill int null,
  modify column lastbilledyear int null,
  modify column lastbilledqtr int null,
  modify column partialbasic decimal(16,2) null,
  modify column partialbasicint decimal(16,2) null,
  modify column partialbasicdisc decimal(16,2) null,
  modify column partialsef decimal(16,2) null,
  modify column partialsefint decimal(16,2) null,
  modify column partialsefdisc decimal(16,2) null,
  modify column partialledyear int null,
  modify column partialledqtr int null,
  add updateflag varchar(50),
  add forcerecalcbill int;


update rptledger set nextbilldate = null, forcerecalcbill = 1, updateflag = objid where forcerecalcbill is null;


alter table rptledgeritem
  add basic decimal(16,2) null,
  add basicpaid decimal(16,2) null,
  add basicint  decimal(16,2) null,
  add basicintpaid  decimal(16,2) null,
  add basicdisc decimal(16,2) null,
  add basicdisctaken  decimal(16,2) null,
  add basicidle decimal(16,2) null,
  add basicidlepaid decimal(16,2) null,
  add basicidledisc decimal(16,2) null,
  add basicidledisctaken  decimal(16,2) null,
  add basicidleint  decimal(16,2) null,
  add basicidleintpaid  decimal(16,2) null,
  add sef decimal(16,2) null,
  add sefpaid decimal(16,2) null,
  add sefint  decimal(16,2) null,
  add sefintpaid  decimal(16,2) null,
  add sefdisc decimal(16,2) null,
  add sefdisctaken  decimal(16,2) null,
  add firecode  decimal(16,2) null,
  add firecodepaid  decimal(16,2) null,
  add revperiod varchar(50) null,
  add qtrly int(11) null,
  add fullypaid int(11) null;


update rptledgeritem set 
  fullypaid = paid,
  basic = 0.0,
  basicpaid = 0.0,
  basicint = 0.0,
  basicintpaid = 0.0,
  basicdisc = 0.0,
  basicdisctaken = 0.0,
  basicidle = 0.0,
  basicidlepaid = 0.0,
  basicidledisc = 0.0,
  basicidledisctaken = 0.0,
  basicidleint = 0.0,
  basicidleintpaid = 0.0,
  sef = 0.0,
  sefpaid = 0.0,
  sefint = 0.0,
  sefintpaid = 0.0,
  sefdisc = 0.0,
  sefdisctaken = 0.0,
  firecode = 0.0,
  firecodepaid = 0.0
where basic is null;



alter table rptledgeritem
  drop column paidqtr,
  drop column paid;



  
create table rptledgeritem_qtrly (
  objid varchar(50) not null,
  parentid varchar(50) not null,
  rptledgerid varchar(50) not null,
  basicav decimal(16,2) not null,
  sefav decimal(16,2) not null,
  av decimal(16,2) not null,
  year int(11) not null,
  qtr int(11) not null,
  basic decimal(16,2) not null,
  basicpaid decimal(16,2) not null,
  basicint decimal(16,2) not null,
  basicintpaid decimal(16,2) not null,
  basicdisc decimal(16,2) not null,
  basicdisctaken decimal(16,2) not null,
  basicidle decimal(16,2) not null,
  basicidlepaid decimal(16,2) not null,
  basicidledisc decimal(16,2) not null,
  basicidledisctaken decimal(16,2) not null,
  basicidleint decimal(16,2) not null,
  basicidleintpaid decimal(16,2) not null,
  sef decimal(16,2) not null,
  sefpaid decimal(16,2) not null,
  sefint decimal(16,2) not null,
  sefintpaid decimal(16,2) not null,
  sefdisc decimal(16,2) not null,
  sefdisctaken decimal(16,2) not null,
  firecode decimal(16,2) not null,
  firecodepaid decimal(16,2) not null,
  revperiod varchar(50) default null,
  partialled int(11) not null,
  fullypaid int(11) not null,
  primary key  (objid),
  key FK_rptledgeritemqtrly_rptledgeritem (parentid),
  key FK_rptledgeritemqtrly_rptledger (rptledgerid),
  key ix_rptledgeritemqtrly_year (year),
  constraint FK_rptledgeritemqtrly_rptledger foreign key (rptledgerid) references rptledger (objid),
  constraint FK_rptledgeritemqtrly_rptledgeritem foreign key (parentid) references rptledgeritem (objid)
) engine=innodb  DEFAULT CHARSET=utf8;



alter table cashreceiptitem_rpt_online 
  add rptledgeritemqtrlyid varchar(50);

alter table cashreceiptitem_rpt_online 
  add constraint FK_cashreceiptitem_rpt_online_rptledgeritemqtrly foreign key(rptledgeritemqtrlyid)
  references rptledgeritem_qtrly(objid);



set foreign_key_checks = 0;

DROP TABLE rptbill_ledger_account;
DROP TABLE rptbill_ledger_item;
DROP TABLE rptbill_ledger;

CREATE TABLE rptbill_ledger (
  rptledgerid varchar(50) NOT NULL default '',
  billid varchar(50) NOT NULL default '',
  updateflag varchar(50),
  PRIMARY KEY  (rptledgerid,billid),
  KEY rptbillid (billid),
  KEY rptledgerid (rptledgerid),
  CONSTRAINT FK_rptbillledger_rptledger FOREIGN KEY (rptledgerid) REFERENCES rptledger (objid),
  CONSTRAINT FK_rptbillledger_rptbill FOREIGN KEY (billid) REFERENCES rptbill(objid)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


CREATE TABLE rptbill_ledger_account (
  objid varchar(50) NOT NULL,
  billid varchar(50) NOT NULL,
  rptledgerid varchar(50) NOT NULL,
  revperiod varchar(25) NOT NULL,
  revtype varchar(25) NOT NULL,
  item_objid varchar(50) NOT NULL,
  amount decimal(16,4) NOT NULL,
  sharetype varchar(25) NOT NULL,
  discount decimal(16,4) default NULL,
  PRIMARY KEY  (objid),
  KEY ix_rptbill_ledger_account_rptledger (rptledgerid),
  KEY ix_rptbillledgeraccount_revenueitem (item_objid),
  KEY FK_rptbillledgeraccount_rptbill (billid),
  CONSTRAINT FK_rptbillledgeraccount_rptbill FOREIGN KEY (billid) REFERENCES rptbill (objid),
  CONSTRAINT rptbill_ledger_account_ibfk_1 FOREIGN KEY (item_objid) REFERENCES itemaccount (objid),
  CONSTRAINT rptbill_ledger_account_ibfk_2 FOREIGN KEY (rptledgerid) REFERENCES rptledger (objid)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE rptbill_ledger_item (
  objid varchar(75) NOT NULL,
  billid varchar(50) NOT NULL,
  rptledgerid varchar(50) NOT NULL,
  rptledgeritemid varchar(50) NULL,
  rptledgeritemqtrlyid varchar(50) NULL,
  rptledgerfaasid varchar(50) NOT NULL,
  av decimal(16,2) default NULL,
  basicav decimal(16,2) default NULL,
  sefav decimal(16,2) default NULL,
  year int(11) NOT NULL,
  qtr int(11) NOT NULL,
  basic decimal(16,2) NOT NULL,
  basicint decimal(16,2) NOT NULL,
  basicdisc decimal(16,2) NOT NULL,
  sef decimal(16,2) NOT NULL,
  sefint decimal(16,2) NOT NULL,
  sefdisc decimal(16,2) NOT NULL,
  firecode decimal(10,2) default NULL,
  revperiod varchar(25) default NULL,
  basicnet decimal(16,2) default NULL,
  sefnet decimal(16,2) default NULL,
  total decimal(16,2) default NULL,
  partialled int(11) NOT NULL,
  basicidle decimal(16,2) default NULL,
  basicidledisc decimal(16,2) default NULL,
  basicidleint decimal(16,2) default NULL,
  taxdifference int(11) default NULL,
  PRIMARY KEY  (objid),
  KEY FK_rptbillledgeritem_rptledger (rptledgerid),
  KEY FK_rptbillledgeritem_rptledgerfaas (rptledgerfaasid),
  KEY FK_rptbillledgeritem_rptledgeritem (rptledgeritemid),
  KEY FK_rptbillledgeritem_rptledgeritemqtrly (rptledgeritemqtrlyid),
  KEY FK_rptbillledgeritem_rptbill (billid),
  CONSTRAINT FK_rptbillledgeritem_rptbill FOREIGN KEY (billid) REFERENCES rptbill (objid),
  CONSTRAINT FK_rptbillledgeritem_rptledger FOREIGN KEY (rptledgerid) REFERENCES rptledger (objid),
  CONSTRAINT FK_rptbillledgeritem_rptledgerfaas FOREIGN KEY (rptledgerfaasid) REFERENCES rptledgerfaas (objid),
  CONSTRAINT FK_rptbillledgeritem_rptledgeritem FOREIGN KEY (rptledgeritemid) REFERENCES rptledgeritem(objid),
  CONSTRAINT FK_rptbillledgeritem_rptledgeritemqtrly FOREIGN KEY (rptledgeritemqtrlyid) REFERENCES rptledgeritem_qtrly (objid)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


set foreign_key_checks = 1;



/*

delete from sys_rule_deployed where objid in (
  select objid from sys_rule where name = 'SPLIT_ADVANCE_PARTIALLED'
);

update sys_rule set state = 'DRAFT' where name = 'SPLIT_ADVANCE_PARTIALLED';


*/



INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('LANDTAX.ADMIN-add_new_ledger_faas', 'LANDTAX.ADMIN', 'rptledger', 'add_new_ledger_faas', 'Add New Ledger FAAS');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('LANDTAX.ADMIN-change_faas_reference', 'LANDTAX.ADMIN', 'rptledger', 'change_faas_reference', 'Change FAAS Reference');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('LANDTAX.ADMIN-fix_ledger_faas', 'LANDTAX.ADMIN', 'rptledger', 'fix_ledger_faas', 'Fix Ledger FAAS');
