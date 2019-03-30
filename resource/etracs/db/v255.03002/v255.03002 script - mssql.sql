/* 255-03001 */
alter table rptcertification add properties varchar(2000)
go 

	
alter table faas_signatory add reviewer_objid varchar(50)
go 
alter table faas_signatory add reviewer_name varchar(100)
go 
alter table faas_signatory add reviewer_title varchar(75)
go 
alter table faas_signatory add reviewer_dtsigned datetime
go 
alter table faas_signatory add reviewer_taskid varchar(50)
go 
alter table faas_signatory add assessor_name varchar(100)
go 
alter table faas_signatory add assessor_title varchar(100)
go 


alter table cancelledfaas_signatory add reviewer_objid varchar(50)
go 
alter table cancelledfaas_signatory add reviewer_name varchar(100)
go 
alter table cancelledfaas_signatory add reviewer_title varchar(75)
go 
alter table cancelledfaas_signatory add reviewer_dtsigned datetime
go 
alter table cancelledfaas_signatory add reviewer_taskid varchar(50)
go 
alter table cancelledfaas_signatory add assessor_name varchar(100)
go 
alter table cancelledfaas_signatory add assessor_title varchar(100)
go 



    

CREATE TABLE rptacknowledgement (
  objid varchar(50) NOT NULL,
  state varchar(25) NOT NULL,
  txnno varchar(25) NOT NULL,
  txndate datetime DEFAULT NULL,
  taxpayer_objid varchar(50) DEFAULT NULL,
  txntype_objid varchar(50) DEFAULT NULL,
  releasedate datetime DEFAULT NULL,
  releasemode varchar(50) DEFAULT NULL,
  receivedby varchar(255) DEFAULT NULL,
  remarks varchar(255) DEFAULT NULL,
  pin varchar(25) DEFAULT NULL,
  createdby_objid varchar(25) DEFAULT NULL,
  createdby_name varchar(25) DEFAULT NULL,
  createdby_title varchar(25) DEFAULT NULL,
  PRIMARY KEY (objid)
) 
go 

create UNIQUE index  ux_rptacknowledgement_txnno on rptacknowledgement(txnno)
go 
create index ix_rptacknowledgement_pin on rptacknowledgement(pin)
go 
create index ix_rptacknowledgement_taxpayerid on rptacknowledgement(taxpayer_objid)
go 


CREATE TABLE rptacknowledgement_item (
  objid varchar(50) NOT NULL,
  parent_objid varchar(50) NOT NULL,
  trackingno varchar(25) NULL,
  faas_objid varchar(50) DEFAULT NULL,
  newfaas_objid varchar(50) DEFAULT NULL,
  remarks varchar(255) DEFAULT NULL,
  PRIMARY KEY (objid)
)
go 

alter table rptacknowledgement_item 
  add constraint fk_rptacknowledgement_item_rptacknowledgement
  foreign key (parent_objid) references rptacknowledgement(objid)
go 

create index ix_rptacknowledgement_parentid on rptacknowledgement_item(parent_objid)
go 

create unique index ux_rptacknowledgement_itemno on rptacknowledgement_item(trackingno)
go 

create index ix_rptacknowledgement_item_faasid  on rptacknowledgement_item(faas_objid)
go 

create index ix_rptacknowledgement_item_newfaasid on rptacknowledgement_item(newfaas_objid)
go 


    

drop  view vw_faas_lookup
go 

CREATE view vw_faas_lookup AS 
select 
  fl.objid AS objid,
  fl.state AS state,
  fl.rpuid AS rpuid,
  fl.utdno AS utdno,
  fl.tdno AS tdno,
  fl.txntype_objid AS txntype_objid,
  fl.effectivityyear AS effectivityyear,
  fl.effectivityqtr AS effectivityqtr,
  fl.taxpayer_objid AS taxpayer_objid,
  fl.owner_name AS owner_name,
  fl.owner_address AS owner_address,
  fl.prevtdno AS prevtdno,
  fl.cancelreason AS cancelreason,
  fl.cancelledbytdnos AS cancelledbytdnos,
  fl.lguid AS lguid,
  fl.realpropertyid AS realpropertyid,
  fl.displaypin AS fullpin,
  fl.originlguid AS originlguid,
  e.name AS taxpayer_name,
  e.address_text AS taxpayer_address,
  pc.code AS classification_code,
  pc.code AS classcode,
  pc.name AS classification_name,
  pc.name AS classname,
  fl.ry AS ry,
  fl.rputype AS rputype,
  fl.totalmv AS totalmv,
  fl.totalav AS totalav,
  fl.totalareasqm AS totalareasqm,
  fl.totalareaha AS totalareaha,
  fl.barangayid AS barangayid,
  fl.cadastrallotno AS cadastrallotno,
  fl.blockno AS blockno,
  fl.surveyno AS surveyno,
  fl.pin AS pin,
  fl.barangay AS barangay_name,
  fl.trackingno
from faas_list fl
left join propertyclassification pc on fl.classification_objid = pc.objid
left join entity e on fl.taxpayer_objid = e.objid
go 

alter table faas alter column prevtdno varchar(800)
go 
  
alter table faas_list alter column prevtdno varchar(800)
go 
alter table faas_list alter column owner_name varchar(2000)
go 
alter table faas_list alter column cadastrallotno varchar(500)
go 


create index ix_faaslist_prevtdno on faas_list(prevtdno)
go 
create index ix_faaslist_cadastrallotno on faas_list(cadastrallotno)
go 
create index ix_faaslist_owner_name on faas_list(owner_name)
go 
create index ix_faaslist_txntype_objid on faas_list(txntype_objid)
go 



alter table rptledger alter column prevtdno varchar(800)
go 
create index ix_rptledger_prevtdno on rptledger(prevtdno)
go 
create index ix_rptledgerfaas_tdno on rptledgerfaas(tdno)
go 

  
