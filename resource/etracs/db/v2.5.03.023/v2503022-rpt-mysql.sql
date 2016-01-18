/*=================================================
* RPT Billing Rules Update
*=================================================*/

INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) 
VALUES ('INIT', 'rptbilling', 'Init', '0');


INSERT INTO `sys_usergroup` (`objid`, `title`, `domain`, `userclass`, `orgclass`, `role`) 
VALUES ('LANDTAX.RULE_AUTHOR', 'RULE_AUTHOR', 'LANDTAX', 'usergroup', NULL, 'RULE_AUTHOR');



/*=================================================
* Add rptledgeritem support
*=================================================*/

alter table rptbill_ledger_item add column rptledgeritemid varchar(50);

alter table rptbill_ledger_item 
	add constraint FK_rptbill_ledger_item_rptledgeritem
	foreign key (rptledgeritemid) references rptledgeritem(objid);


alter table cashreceiptitem_rpt_online add column rptledgeritemid varchar(50);

alter table cashreceiptitem_rpt_online 
	add constraint FK_cashreceiptitem_rpt_online_rptledgeritem
	foreign key (rptledgeritemid) references rptledgeritem(objid);


/*=================================================
*
* Sharing: modify amount to use decimal(16,4)
*          to handle non-equal sharing
*
*=================================================*/

alter table rptbill_ledger_account modify column amount decimal(16,4) not null;

alter table cashreceiptitem_rpt_account modify column amount decimal(16,4) not null;

alter table cashreceiptitem modify column amount decimal(16,4) not null;


/*=================================================
*
* Liquidation updates
*=================================================*/
alter table liquidation add column posted int;

create table income_summary
(
	remittanceno varchar(25) not null, 
	liquidationno varchar(25) not null, 
	receiptdate date not null, 
	acctid varchar(50) not null, 
	fundid varchar(50) not null, 
	liquidationdate date not null, 
	remittancedate date not null,
	amount decimal(16,2),
	reftype varchar(50),
	orgid varchar(50)
)




/*=================================================
*
* Save Discount Information
*=================================================*/
alter table rptbill_ledger_account add column discount decimal(16,4) null;
update rptbill_ledger_account  set discount = 0 where discount is null;

alter table cashreceiptitem_rpt_account add column discount decimal(16,4) null;
update cashreceiptitem_rpt_account  set discount = 0 where discount is null;

	

/*=================================================
*
* IDLE LAND: support disc and int
*
*=================================================*/

alter table rptbill_ledger_item add column basicidledisc decimal(16,2);
alter table rptbill_ledger_item add column basicidleint decimal(16,2);
update rptbill_ledger_item set basicidledisc = 0, basicidleint = 0;	

alter table cashreceiptitem_rpt_online add column basicidledisc decimal(16,2);
alter table cashreceiptitem_rpt_online add column basicidleint decimal(16,2);
update cashreceiptitem_rpt_online set basicidledisc = 0, basicidleint = 0;	

alter table municipality_taxaccount_mapping add column basicidlecurracct_objid varchar(50);
alter table municipality_taxaccount_mapping add column basicidlecurrintacct_objid varchar(50);
alter table municipality_taxaccount_mapping add column basicidleprevacct_objid varchar(50);
alter table municipality_taxaccount_mapping add column basicidleprevintacct_objid varchar(50);
alter table municipality_taxaccount_mapping add column basicidleadvacct_objid varchar(50);


alter table brgy_taxaccount_mapping drop column basicadvintacct_objid	
alter table brgy_taxaccount_mapping change column advacct_objid basicadvacct_objid varchar(50);
alter table brgy_taxaccount_mapping change column prevacct_objid basicprevacct_objid varchar(50);
alter table brgy_taxaccount_mapping change column previntacct_objid basicprevintacct_objid varchar(50);
alter table brgy_taxaccount_mapping change column prioracct_objid basicprioracct_objid varchar(50);
alter table brgy_taxaccount_mapping change column priorintacct_objid basicpriorintacct_objid varchar(50);
alter table brgy_taxaccount_mapping change column curracct_objid basiccurracct_objid varchar(50);
alter table brgy_taxaccount_mapping change column currintacct_objid basiccurrintacct_objid varchar(50);


CREATE TABLE province_taxaccount_mapping (
`objid`  varchar(50)  NOT NULL ,
`basicadvacct_objid`  varchar(50)  NULL DEFAULT NULL ,
`basicprevacct_objid`  varchar(50)  NULL DEFAULT NULL ,
`basicprevintacct_objid`  varchar(50)  NULL DEFAULT NULL ,
`basicprioracct_objid`  varchar(50)  NULL DEFAULT NULL ,
`basicpriorintacct_objid`  varchar(50)  NULL DEFAULT NULL ,
`basiccurracct_objid`  varchar(50)  NULL DEFAULT NULL ,
`basiccurrintacct_objid`  varchar(50)  NULL DEFAULT NULL ,
`sefadvacct_objid`  varchar(50)  NULL DEFAULT NULL ,
`sefprevacct_objid`  varchar(50)  NULL DEFAULT NULL ,
`sefprevintacct_objid`  varchar(50)  NULL DEFAULT NULL ,
`sefprioracct_objid`  varchar(50)  NULL DEFAULT NULL ,
`sefpriorintacct_objid`  varchar(50)  NULL DEFAULT NULL ,
`sefcurracct_objid`  varchar(50)  NULL DEFAULT NULL ,
`sefcurrintacct_objid`  varchar(50)  NULL DEFAULT NULL ,
`basicidlecurracct_objid`  varchar(50)  NULL DEFAULT NULL ,
`basicidlecurrintacct_objid`  varchar(50)  NULL DEFAULT NULL ,
`basicidleprevacct_objid`  varchar(50)  NULL DEFAULT NULL ,
`basicidleprevintacct_objid`  varchar(50)  NULL DEFAULT NULL ,
`basicidleadvacct_objid`  varchar(50)  NULL DEFAULT NULL ,
PRIMARY KEY (`objid`)
)
ENGINE=InnoDB;






/*=================================================
*
* TAX DIFFERENCE
*
*=================================================*/

alter table rptledgeritem add column taxdifference int;
update rptledgeritem set taxdifference = 0 where taxdifference is null;

alter table rptbill_ledger_item add column taxdifference int;
update rptbill_ledger_item set taxdifference = 0 where taxdifference is null;



/*=================================================
*
* ESIGNATURE
*
*=================================================*/
alter table faas_task add column signature text null;	
alter table subdivision_task add column signature text null;	
alter table consolidation_task add column signature text null;	






/*=================================================
*
* RPT COMPROMISE UPDATES
*
*=================================================*/

alter table rptledger_compromise_item add column basicidleint decimal(16,4) not null;
alter table rptledger_compromise_item add column basicidleintpaid decimal(16,4) not null;
alter table rptledger_compromise_item_credit add column basicidleint decimal(16,4) not null;


update rptledger_compromise_item set basicidleint = 0, basicidleintpaid = 0 where basicidleint is null;
update rptledger_compromise_item_credit set basicidleint = 0 where basicidleint is null;

	
