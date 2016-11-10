/*==================================
** V2.5.04.025
==================================*/

delete from sys_rule_deployed where objid in (
	select objid from sys_rule where ruleset = 'miscassessment'
)	
;

update sys_rule set state = 'APPROVED' where ruleset = 'miscassessment'
;

create table propertypayer(
	objid varchar(50),
	taxpayer_objid varchar(50) not null,
	primary key(objid)
)
;

alter table propertypayer 
	add constraint FK_propertypayer_entity foreign key(taxpayer_objid)
	references entity(objid)
;

create index ix_propertypayer_taxpayerid on propertypayer(taxpayer_objid)
;


create table propertypayer_item(
	objid varchar(50),
	parentid varchar(50) not null,
	rptledger_objid varchar(50) not null,
	primary key(objid)
)
;

alter table propertypayer_item 
	add constraint FK_propertypayeritem_propertypayer foreign key(parentid)
	references propertypayer(objid)
;

alter table propertypayer_item 
	add constraint FK_propertypayeritem_rptledger foreign key(rptledger_objid)
	references rptledger(objid)
;

create index ix_propertypayeritem_parentid on propertypayer_item(parentid)
;

create index ix_propertypayeritem_rptledgerid on propertypayer_item(rptledger_objid)
;


create unique index ux_propertypayeritem_rptledgerid on propertypayer_item(parentid, rptledger_objid)
;


UPDATE `faas_txntype` SET `objid`='CC', `name`='Change Classification', `newledger`='0', `newrpu`='1', `newrealproperty`='0', `displaycode`='RC', `allowEditOwner`='0', `checkbalance`='1', `allowEditPin`='0', `allowEditPinInfo`='0', `allowEditAppraisal`='1', `opener`=NULL WHERE (`objid`='CC');
UPDATE `faas_txntype` SET `objid`='CD', `name`='Change Depreciation', `newledger`='0', `newrpu`='1', `newrealproperty`='0', `displaycode`='DP', `allowEditOwner`='0', `checkbalance`='1', `allowEditPin`='0', `allowEditPinInfo`='0', `allowEditAppraisal`='1', `opener`=NULL WHERE (`objid`='CD');
UPDATE `faas_txntype` SET `objid`='CE', `name`='Correction of Entry', `newledger`='0', `newrpu`='1', `newrealproperty`='1', `displaycode`='DP', `allowEditOwner`='1', `checkbalance`='1', `allowEditPin`='0', `allowEditPinInfo`='1', `allowEditAppraisal`='1', `opener`=NULL WHERE (`objid`='CE');
UPDATE `faas_txntype` SET `objid`='CS', `name`='Consolidation', `newledger`='1', `newrpu`='1', `newrealproperty`='1', `displaycode`='CS', `allowEditOwner`='1', `checkbalance`='1', `allowEditPin`='0', `allowEditPinInfo`='1', `allowEditAppraisal`='1', `opener`='consolidation' WHERE (`objid`='CS');
UPDATE `faas_txntype` SET `objid`='CS/SD', `name`='Consolidation/Subdivision', `newledger`='1', `newrpu`='1', `newrealproperty`='1', `displaycode`='CS/SD', `allowEditOwner`='0', `checkbalance`='1', `allowEditPin`='0', `allowEditPinInfo`='0', `allowEditAppraisal`='1', `opener`='subdivision' WHERE (`objid`='CS/SD');
UPDATE `faas_txntype` SET `objid`='CT', `name`='Change Taxability', `newledger`='0', `newrpu`='1', `newrealproperty`='0', `displaycode`='DP', `allowEditOwner`='0', `checkbalance`='1', `allowEditPin`='0', `allowEditPinInfo`='0', `allowEditAppraisal`='1', `opener`=NULL WHERE (`objid`='CT');
UPDATE `faas_txntype` SET `objid`='CTD', `name`='Cancellation', `newledger`='0', `newrpu`='0', `newrealproperty`='0', `displaycode`='DP', `allowEditOwner`='0', `checkbalance`='1', `allowEditPin`='0', `allowEditPinInfo`='0', `allowEditAppraisal`='0', `opener`=NULL WHERE (`objid`='CTD');
UPDATE `faas_txntype` SET `objid`='DC', `name`='Data Capture', `newledger`='1', `newrpu`='1', `newrealproperty`='1', `displaycode`='DC', `allowEditOwner`='1', `checkbalance`='0', `allowEditPin`='1', `allowEditPinInfo`='1', `allowEditAppraisal`='1', `opener`=NULL WHERE (`objid`='DC');
UPDATE `faas_txntype` SET `objid`='GR', `name`='General Revision', `newledger`='0', `newrpu`='1', `newrealproperty`='1', `displaycode`='GR', `allowEditOwner`='0', `checkbalance`='0', `allowEditPin`='0', `allowEditPinInfo`='1', `allowEditAppraisal`='1', `opener`=NULL WHERE (`objid`='GR');
UPDATE `faas_txntype` SET `objid`='MC', `name`='Multiple Claim', `newledger`='1', `newrpu`='1', `newrealproperty`='1', `displaycode`='DP', `allowEditOwner`='1', `checkbalance`='0', `allowEditPin`='1', `allowEditPinInfo`='1', `allowEditAppraisal`='1', `opener`=NULL WHERE (`objid`='MC');
UPDATE `faas_txntype` SET `objid`='MCS', `name`='Multiple Claim Settlement', `newledger`='0', `newrpu`='1', `newrealproperty`='1', `displaycode`='DP', `allowEditOwner`='0', `checkbalance`='0', `allowEditPin`='0', `allowEditPinInfo`='0', `allowEditAppraisal`='0', `opener`='mcsettlement' WHERE (`objid`='MCS');
UPDATE `faas_txntype` SET `objid`='ND', `name`='New Discovery', `newledger`='1', `newrpu`='1', `newrealproperty`='1', `displaycode`='DC', `allowEditOwner`='1', `checkbalance`='0', `allowEditPin`='1', `allowEditPinInfo`='1', `allowEditAppraisal`='1', `opener`=NULL WHERE (`objid`='ND');
UPDATE `faas_txntype` SET `objid`='RE', `name`='Reassessment', `newledger`='0', `newrpu`='1', `newrealproperty`='0', `displaycode`='DP', `allowEditOwner`='0', `checkbalance`='1', `allowEditPin`='1', `allowEditPinInfo`='0', `allowEditAppraisal`='1', `opener`=NULL WHERE (`objid`='RE');
UPDATE `faas_txntype` SET `objid`='RS', `name`='Resection', `newledger`='0', `newrpu`='1', `newrealproperty`='1', `displaycode`='DP', `allowEditOwner`='0', `checkbalance`='0', `allowEditPin`='0', `allowEditPinInfo`='0', `allowEditAppraisal`='1', `opener`=NULL WHERE (`objid`='RS');
UPDATE `faas_txntype` SET `objid`='RV', `name`='Revision', `newledger`='0', `newrpu`='1', `newrealproperty`='1', `displaycode`='RV', `allowEditOwner`='1', `checkbalance`='1', `allowEditPin`='1', `allowEditPinInfo`='1', `allowEditAppraisal`='1', `opener`=NULL WHERE (`objid`='RV');
UPDATE `faas_txntype` SET `objid`='SD', `name`='Subdivision', `newledger`='1', `newrpu`='1', `newrealproperty`='1', `displaycode`='SD', `allowEditOwner`='1', `checkbalance`='1', `allowEditPin`='0', `allowEditPinInfo`='1', `allowEditAppraisal`='1', `opener`='subdivision' WHERE (`objid`='SD');
UPDATE `faas_txntype` SET `objid`='TR', `name`='Transfer of Ownership', `newledger`='0', `newrpu`='1', `newrealproperty`='1', `displaycode`='TR', `allowEditOwner`='1', `checkbalance`='1', `allowEditPin`='0', `allowEditPinInfo`='1', `allowEditAppraisal`='1', `opener`=NULL WHERE (`objid`='TR');
UPDATE `faas_txntype` SET `objid`='TRC', `name`='Transfer with Correction', `newledger`='0', `newrpu`='1', `newrealproperty`='1', `displaycode`='TR', `allowEditOwner`='1', `checkbalance`='1', `allowEditPin`='0', `allowEditPinInfo`='1', `allowEditAppraisal`='1', `opener`=NULL WHERE (`objid`='TRC');
UPDATE `faas_txntype` SET `objid`='TRE', `name`='Transfer with Reassessment', `newledger`='0', `newrpu`='1', `newrealproperty`='0', `displaycode`='TR', `allowEditOwner`='1', `checkbalance`='1', `allowEditPin`='0', `allowEditPinInfo`='1', `allowEditAppraisal`='1', `opener`=NULL WHERE (`objid`='TRE');


CREATE TABLE faas_affectedrpu (
  objid varchar(50) NOT NULL,
  faasid varchar(50) NOT NULL,
  prevfaasid varchar(50) NOT NULL,
  newfaasid varchar(50)  NULL,
  newsuffix int  NULL,
  PRIMARY KEY(objid)
);

alter table faas_affectedrpu
  add constraint FK_faasaffectedrpu_faas foreign key(faasid)
  references faas (objid);
 
alter table faas_affectedrpu
  add constraint FK_faasaffectedrpu_prevfaas foreign key(prevfaasid)
  references faas (objid);

alter table faas_affectedrpu
  add constraint FK_faasaffectedrpu_newfaas foreign key(newfaasid)
  references faas (objid);  

create index FK_faasaffectedrpu_faas on faas_affectedrpu(faasid) ;
create index FK_faasaffectedrpu_prevfaas on faas_affectedrpu(prevfaasid) ;
create index FK_faasaffectedrpu_newfaas on faas_affectedrpu(newfaasid) ;

create unique index ux_faasaffectedrpu_faasprevfaas on faas_affectedrpu(faasid, prevfaasid);


