/*==================================
** V2.5.04.025
==================================*/

delete from sys_rule_deployed where objid in (
	select objid from sys_rule where ruleset = 'miscassessment'
)
go

update sys_rule set state = 'APPROVED' where ruleset = 'miscassessment'
go



create table propertypayer(
	objid nvarchar(50),
	taxpayer_objid nvarchar(50) not null,
	primary key(objid)
)
go

alter table propertypayer 
	add constraint FK_propertypayer_entity foreign key(taxpayer_objid)
	references entity(objid)
go

create index ix_propertypayer_taxpayerid on propertypayer(taxpayer_objid)
go


create table propertypayer_item(
	objid nvarchar(50),
	parentid nvarchar(50) not null,
	rptledger_objid nvarchar(50) not null,
	primary key(objid)
)
go

alter table propertypayer_item 
	add constraint FK_propertypayeritem_propertypayer foreign key(parentid)
	references propertypayer(objid)
go

alter table propertypayer_item 
	add constraint FK_propertypayeritem_rptledger foreign key(rptledger_objid)
	references rptledger(objid)
go

create index ix_propertypayeritem_parentid on propertypayer_item(parentid)
go

create index ix_propertypayeritem_rptledgerid on propertypayer_item(rptledger_objid)
go

create unique index ux_propertypayeritem_rptledgerid on propertypayer_item(parentid, rptledger_objid)
go 



UPDATE faas_txntype SET objid='CC', name='Change Classification', newledger='0', newrpu='1', newrealproperty='0', displaycode='RC', allowEditOwner='0', checkbalance='1', allowEditPin='0', allowEditPinInfo='0', allowEditAppraisal='1', opener=NULL WHERE (objid='CC')
go 
UPDATE faas_txntype SET objid='CD', name='Change Depreciation', newledger='0', newrpu='1', newrealproperty='0', displaycode='DP', allowEditOwner='0', checkbalance='1', allowEditPin='0', allowEditPinInfo='0', allowEditAppraisal='1', opener=NULL WHERE (objid='CD')
go 
UPDATE faas_txntype SET objid='CE', name='Correction of Entry', newledger='0', newrpu='1', newrealproperty='1', displaycode='DP', allowEditOwner='1', checkbalance='1', allowEditPin='0', allowEditPinInfo='1', allowEditAppraisal='1', opener=NULL WHERE (objid='CE')
go 
UPDATE faas_txntype SET objid='CS', name='Consolidation', newledger='1', newrpu='1', newrealproperty='1', displaycode='CS', allowEditOwner='1', checkbalance='1', allowEditPin='0', allowEditPinInfo='1', allowEditAppraisal='1', opener='consolidation' WHERE (objid='CS')
go 
UPDATE faas_txntype SET objid='CS/SD', name='Consolidation/Subdivision', newledger='1', newrpu='1', newrealproperty='1', displaycode='CS/SD', allowEditOwner='0', checkbalance='1', allowEditPin='0', allowEditPinInfo='0', allowEditAppraisal='1', opener='subdivision' WHERE (objid='CS/SD')
go 
UPDATE faas_txntype SET objid='CT', name='Change Taxability', newledger='0', newrpu='1', newrealproperty='0', displaycode='DP', allowEditOwner='0', checkbalance='1', allowEditPin='0', allowEditPinInfo='0', allowEditAppraisal='1', opener=NULL WHERE (objid='CT')
go 
UPDATE faas_txntype SET objid='CTD', name='Cancellation', newledger='0', newrpu='0', newrealproperty='0', displaycode='DP', allowEditOwner='0', checkbalance='1', allowEditPin='0', allowEditPinInfo='0', allowEditAppraisal='0', opener=NULL WHERE (objid='CTD')
go 
UPDATE faas_txntype SET objid='DC', name='Data Capture', newledger='1', newrpu='1', newrealproperty='1', displaycode='DC', allowEditOwner='1', checkbalance='0', allowEditPin='1', allowEditPinInfo='1', allowEditAppraisal='1', opener=NULL WHERE (objid='DC')
go 
UPDATE faas_txntype SET objid='GR', name='General Revision', newledger='0', newrpu='1', newrealproperty='1', displaycode='GR', allowEditOwner='0', checkbalance='0', allowEditPin='0', allowEditPinInfo='1', allowEditAppraisal='1', opener=NULL WHERE (objid='GR')
go 
UPDATE faas_txntype SET objid='MC', name='Multiple Claim', newledger='1', newrpu='1', newrealproperty='1', displaycode='DP', allowEditOwner='1', checkbalance='0', allowEditPin='1', allowEditPinInfo='1', allowEditAppraisal='1', opener=NULL WHERE (objid='MC')
go 
UPDATE faas_txntype SET objid='MCS', name='Multiple Claim Settlement', newledger='0', newrpu='1', newrealproperty='1', displaycode='DP', allowEditOwner='0', checkbalance='0', allowEditPin='0', allowEditPinInfo='0', allowEditAppraisal='0', opener='mcsettlement' WHERE (objid='MCS')
go 
UPDATE faas_txntype SET objid='ND', name='New Discovery', newledger='1', newrpu='1', newrealproperty='1', displaycode='DC', allowEditOwner='1', checkbalance='0', allowEditPin='1', allowEditPinInfo='1', allowEditAppraisal='1', opener=NULL WHERE (objid='ND')
go 
UPDATE faas_txntype SET objid='RE', name='Reassessment', newledger='0', newrpu='1', newrealproperty='0', displaycode='DP', allowEditOwner='0', checkbalance='1', allowEditPin='1', allowEditPinInfo='0', allowEditAppraisal='1', opener=NULL WHERE (objid='RE')
go 
UPDATE faas_txntype SET objid='RS', name='Resection', newledger='0', newrpu='1', newrealproperty='1', displaycode='DP', allowEditOwner='0', checkbalance='0', allowEditPin='0', allowEditPinInfo='0', allowEditAppraisal='1', opener=NULL WHERE (objid='RS')
go 
UPDATE faas_txntype SET objid='RV', name='Revision', newledger='0', newrpu='1', newrealproperty='1', displaycode='RV', allowEditOwner='1', checkbalance='1', allowEditPin='1', allowEditPinInfo='1', allowEditAppraisal='1', opener=NULL WHERE (objid='RV')
go 
UPDATE faas_txntype SET objid='SD', name='Subdivision', newledger='1', newrpu='1', newrealproperty='1', displaycode='SD', allowEditOwner='1', checkbalance='1', allowEditPin='0', allowEditPinInfo='1', allowEditAppraisal='1', opener='subdivision' WHERE (objid='SD')
go 
UPDATE faas_txntype SET objid='TR', name='Transfer of Ownership', newledger='0', newrpu='1', newrealproperty='1', displaycode='TR', allowEditOwner='1', checkbalance='1', allowEditPin='0', allowEditPinInfo='1', allowEditAppraisal='1', opener=NULL WHERE (objid='TR')
go 
UPDATE faas_txntype SET objid='TRC', name='Transfer with Correction', newledger='0', newrpu='1', newrealproperty='1', displaycode='TR', allowEditOwner='1', checkbalance='1', allowEditPin='0', allowEditPinInfo='1', allowEditAppraisal='1', opener=NULL WHERE (objid='TRC')
go 
UPDATE faas_txntype SET objid='TRE', name='Transfer with Reassessment', newledger='0', newrpu='1', newrealproperty='0', displaycode='TR', allowEditOwner='1', checkbalance='1', allowEditPin='0', allowEditPinInfo='1', allowEditAppraisal='1', opener=NULL WHERE (objid='TRE')
go 



CREATE TABLE faas_affectedrpu (
  objid varchar(50) NOT NULL,
  faasid varchar(50) NOT NULL,
  prevfaasid varchar(50) NOT NULL,
  newfaasid varchar(50)  NULL,
  newsuffix int  NULL,
  PRIMARY KEY(objid)
)
go 

alter table faas_affectedrpu
  add constraint FK_faasaffectedrpu_faas foreign key(faasid)
  references faas (objid)
  go 
 
alter table faas_affectedrpu
  add constraint FK_faasaffectedrpu_prevfaas foreign key(prevfaasid)
  references faas (objid)
  go 

alter table faas_affectedrpu
  add constraint FK_faasaffectedrpu_newfaas foreign key(newfaasid)
  references faas (objid)
  go   

create index FK_faasaffectedrpu_faas on faas_affectedrpu(faasid) 
	go 
create index FK_faasaffectedrpu_prevfaas on faas_affectedrpu(prevfaasid) 
	go 
create index FK_faasaffectedrpu_newfaas on faas_affectedrpu(newfaasid) 
	go 

create unique index ux_faasaffectedrpu_faasprevfaas on faas_affectedrpu(faasid, prevfaasid)
	go 

