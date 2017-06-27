/*=================================================
* RPT Billing Rules Update
*=================================================*/

INSERT INTO sys_rulegroup ([name], [ruleset], [title], [sortorder]) 
VALUES ('INIT', 'rptbilling', 'Init', '0')
go 


INSERT INTO sys_usergroup ([objid], [title], [domain], [userclass], [orgclass], [role]) 
VALUES ('LANDTAX.RULE_AUTHOR', 'RULE_AUTHOR', 'LANDTAX', 'usergroup', NULL, 'RULE_AUTHOR')
go 


/*=================================================
* Add rptledgeritem support
*=================================================*/

alter table rptbill_ledger_item add rptledgeritemid varchar(50)
go 

alter table rptbill_ledger_item 
	add constraint FK_rptbill_ledger_item_rptledgeritem
	foreign key (rptledgeritemid) references rptledgeritem(objid)
go 	


alter table cashreceiptitem_rpt_online add rptledgeritemid varchar(50)
go 

alter table cashreceiptitem_rpt_online 
	add constraint FK_cashreceiptitem_rpt_online_rptledgeritem
	foreign key (rptledgeritemid) references rptledgeritem(objid)
go 




/*=================================================
*
* Sharing: modify amount to use decimal(16,4)
*          to handle non-equal sharing
*
*=================================================*/

alter table rptbill_ledger_account alter column amount decimal(16,4) not null
go

alter table cashreceiptitem_rpt_account alter column amount decimal(16,4) not null
go

alter table cashreceiptitem alter column amount decimal(16,4) not null
go



/*=================================================
*
* Liquidation updates
*=================================================*/
alter table liquidation add posted int
go 

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
go 



/*=================================================
*
* Save Discount Information
*=================================================*/
alter table rptbill_ledger_account add discount decimal(16,4) null
go 

update rptbill_ledger_account  set discount = 0 where discount is null 
go

alter table cashreceiptitem_rpt_account add discount decimal(16,4) null
go

update cashreceiptitem_rpt_account  set discount = 0 where discount is null 
go





/*=================================================
*
* IDLE LAND: support disc and int
*
*=================================================*/

alter table rptbill_ledger_item add basicidledisc decimal(16,2)
go
alter table rptbill_ledger_item add basicidleint decimal(16,2)
go
update rptbill_ledger_item set basicidledisc = 0, basicidleint = 0 where basicidledisc is null
go



alter table cashreceiptitem_rpt_online add basicidledisc decimal(16,2)
go 
alter table cashreceiptitem_rpt_online add basicidleint decimal(16,2)
go 
update cashreceiptitem_rpt_online set basicidledisc = 0, basicidleint = 0 where basicidledisc is null
go 



alter table municipality_taxaccount_mapping add  basicidlecurracct_objid varchar(50)
go
alter table municipality_taxaccount_mapping add  basicidlecurrintacct_objid varchar(50)
go 
alter table municipality_taxaccount_mapping add  basicidleprevacct_objid varchar(50)
go 
alter table municipality_taxaccount_mapping add  basicidleprevintacct_objid varchar(50)
go 
alter table municipality_taxaccount_mapping add  basicidleadvacct_objid varchar(50)
go 


alter table brgy_taxaccount_mapping drop column advintacct_objid
go 


alter table brgy_taxaccount_mapping add basicadvacct_objid varchar(50)
go

alter table brgy_taxaccount_mapping add basicprevacct_objid varchar(50)
go

alter table brgy_taxaccount_mapping add basicprevintacct_objid varchar(50)
go

alter table brgy_taxaccount_mapping add basicprioracct_objid varchar(50)
go

alter table brgy_taxaccount_mapping add basicpriorintacct_objid varchar(50)
go

alter table brgy_taxaccount_mapping add basiccurracct_objid varchar(50)
go

alter table brgy_taxaccount_mapping add basiccurrintacct_objid varchar(50)
go



update brgy_taxaccount_mapping set 
	basicadvacct_objid = advacct_objid,
	basicprevacct_objid = prevacct_objid,
	basicprevintacct_objid = previntacct_objid,
	basicprioracct_objid = prioracct_objid,
	basicpriorintacct_objid = priorintacct_objid,
	basiccurracct_objid = curracct_objid,
	basiccurrintacct_objid = currintacct_objid
go 	

alter table brgy_taxaccount_mapping drop column advacct_objid
go
alter table brgy_taxaccount_mapping drop column prevacct_objid
go
alter table brgy_taxaccount_mapping drop column previntacct_objid
go
alter table brgy_taxaccount_mapping drop column prioracct_objid
go
alter table brgy_taxaccount_mapping drop column priorintacct_objid
go
alter table brgy_taxaccount_mapping drop column curracct_objid
go
alter table brgy_taxaccount_mapping drop column currintacct_objid
go




CREATE TABLE province_taxaccount_mapping (
objid  varchar(50)  NOT NULL ,
basicadvacct_objid  varchar(50)  NULL DEFAULT NULL ,
basicprevacct_objid  varchar(50)  NULL DEFAULT NULL ,
basicprevintacct_objid  varchar(50)  NULL DEFAULT NULL ,
basicprioracct_objid  varchar(50)  NULL DEFAULT NULL ,
basicpriorintacct_objid  varchar(50)  NULL DEFAULT NULL ,
basiccurracct_objid  varchar(50)  NULL DEFAULT NULL ,
basiccurrintacct_objid  varchar(50)  NULL DEFAULT NULL ,
sefadvacct_objid  varchar(50)  NULL DEFAULT NULL ,
sefprevacct_objid  varchar(50)  NULL DEFAULT NULL ,
sefprevintacct_objid  varchar(50)  NULL DEFAULT NULL ,
sefprioracct_objid  varchar(50)  NULL DEFAULT NULL ,
sefpriorintacct_objid  varchar(50)  NULL DEFAULT NULL ,
sefcurracct_objid  varchar(50)  NULL DEFAULT NULL ,
sefcurrintacct_objid  varchar(50)  NULL DEFAULT NULL ,
basicidlecurracct_objid  varchar(50)  NULL DEFAULT NULL ,
basicidlecurrintacct_objid  varchar(50)  NULL DEFAULT NULL ,
basicidleprevacct_objid  varchar(50)  NULL DEFAULT NULL ,
basicidleprevintacct_objid  varchar(50)  NULL DEFAULT NULL ,
basicidleadvacct_objid  varchar(50)  NULL DEFAULT NULL ,
PRIMARY KEY (objid)
)
go






/*=================================================
*
* TAX DIFFERENCE
*
*=================================================*/

alter table rptledgeritem add taxdifference int
go
update rptledgeritem set taxdifference = 0 where taxdifference is null 
go

alter table rptbill_ledger_item add taxdifference int
go
update rptbill_ledger_item set taxdifference = 0 where taxdifference is null;
go




/*=================================================
*
* ESIGNATURE
*
*=================================================*/
alter table faas_task add signature text null
go 
alter table subdivision_task add column signature text null
go 
alter table consolidation_task add column signature text null
go 





/*=================================================
*
* RPT COMPROMISE UPDATES
*
*=================================================*/

alter table rptledger_compromise_item add basicidleint decimal(16,4) null;
alter table rptledger_compromise_item add  basicidleintpaid decimal(16,4) null;
alter table rptledger_compromise_item_credit add basicidleint decimal(16,4) null;


update rptledger_compromise_item set basicidleint = 0, basicidleintpaid = 0 where basicidleint is null;
update rptledger_compromise_item_credit set basicidleint = 0 where basicidleint is null;

	

	