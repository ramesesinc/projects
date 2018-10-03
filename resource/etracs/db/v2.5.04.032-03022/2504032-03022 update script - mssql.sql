/* 03022 */

/*============================================
*
* SYNC PROVINCE AND REMOTE LEGERS
*
*============================================*/

drop table rptledger_remote
go 

CREATE TABLE remote_mapping (
  objid varchar(50) NOT NULL,
  doctype varchar(50) NOT NULL,
  remote_objid varchar(50) NULL,
  createdby_name varchar(255) NOT NULL,
  createdby_title varchar(100) DEFAULT NULL,
  dtcreated datetime NOT NULL,
  orgcode varchar(10) DEFAULT NULL,
  remote_orgcode varchar(10) DEFAULT NULL,
  PRIMARY KEY (objid)
)
go 


create index ix_doctype on remote_mapping(doctype)
go 
create index ix_orgcode on remote_mapping(orgcode)
go 
create index ix_remote_orgcode on remote_mapping(remote_orgcode)
go 
create index ix_remote_objid on remote_mapping(remote_objid)
go 


create table sync_data (
  objid varchar(50) not null,
  refid varchar(50) not null,
  reftype varchar(50) not null,
  orgid varchar(50) null,
  action varchar(50) not null,
  dtfiled datetime not null,
  primary key (objid)
)
go 

create index ix_sync_data_refid on sync_data(refid)
go 

create index ix_sync_data_reftype on sync_data(reftype)
go 

create index ix_sync_data_orgid on sync_data(orgid)
go 

create index ix_sync_data_dtfiled on sync_data(dtfiled)
go 



CREATE TABLE sync_data_forprocess (
  objid varchar(50) NOT NULL,
  PRIMARY KEY (objid)
) 
go 

alter table sync_data_forprocess add constraint fk_sync_data_forprocess_sync_data 
  foreign key (objid) references sync_data (objid)
go 

CREATE TABLE sync_data_pending (
  objid varchar(50) NOT NULL,
  error text,
  expirydate datetime,
  PRIMARY KEY (objid)
) 
go 


alter table sync_data_pending add constraint fk_sync_data_pending_sync_data 
  foreign key (objid) references sync_data (objid)
go 

create index ix_expirydate on sync_data_pending(expirydate)
go 


alter table faas alter column prevtdno varchar(1000)
go 



create view vw_txn_log 
as 
select 
  distinct
  u.objid as userid, 
  u.name as username, 
  txndate, 
  ref,
  action, 
  1 as cnt 
from txnlog t
inner join sys_user u on t.userid = u.objid 

union 

select 
  u.objid as userid, 
  u.name as username,
  t.enddate as txndate, 
  'faas' as ref,
  case 
    when t.state like '%receiver%' then 'receive'
    when t.state like '%examiner%' then 'examine'
    when t.state like '%taxmapper_chief%' then 'approve taxmap'
    when t.state like '%taxmapper%' then 'taxmap'
    when t.state like '%appraiser%' then 'appraise'
    when t.state like '%appraiser_chief%' then 'approve appraisal'
    when t.state like '%recommender%' then 'recommend'
    when t.state like '%approver%' then 'approve'
    else t.state 
  end action, 
  1 as cnt 
from faas_task t 
inner join sys_user u on t.actor_objid = u.objid 
where t.state not like '%assign%'

union 

select 
  u.objid as userid, 
  u.name as username,
  t.enddate as txndate, 
  'subdivision' as ref,
  case 
    when t.state like '%receiver%' then 'receive'
    when t.state like '%examiner%' then 'examine'
    when t.state like '%taxmapper_chief%' then 'approve taxmap'
    when t.state like '%taxmapper%' then 'taxmap'
    when t.state like '%appraiser%' then 'appraise'
    when t.state like '%appraiser_chief%' then 'approve appraisal'
    when t.state like '%recommender%' then 'recommend'
    when t.state like '%approver%' then 'approve'
    else t.state 
  end action, 
  1 as cnt 
from subdivision_task t 
inner join sys_user u on t.actor_objid = u.objid 
where t.state not like '%assign%'

union 

select 
  u.objid as userid, 
  u.name as username,
  t.enddate as txndate, 
  'consolidation' as ref,
  case 
    when t.state like '%receiver%' then 'receive'
    when t.state like '%examiner%' then 'examine'
    when t.state like '%taxmapper_chief%' then 'approve taxmap'
    when t.state like '%taxmapper%' then 'taxmap'
    when t.state like '%appraiser%' then 'appraise'
    when t.state like '%appraiser_chief%' then 'approve appraisal'
    when t.state like '%recommender%' then 'recommend'
    when t.state like '%approver%' then 'approve'
    else t.state 
  end action, 
  1 as cnt 
from subdivision_task t 
inner join sys_user u on t.actor_objid = u.objid 
where t.state not like '%consolidation%'

union 


select 
  u.objid as userid, 
  u.name as username,
  t.enddate as txndate, 
  'cancelledfaas' as ref,
  case 
    when t.state like '%receiver%' then 'receive'
    when t.state like '%examiner%' then 'examine'
    when t.state like '%taxmapper_chief%' then 'approve taxmap'
    when t.state like '%taxmapper%' then 'taxmap'
    when t.state like '%appraiser%' then 'appraise'
    when t.state like '%appraiser_chief%' then 'approve appraisal'
    when t.state like '%recommender%' then 'recommend'
    when t.state like '%approver%' then 'approve'
    else t.state 
  end action, 
  1 as cnt 
from subdivision_task t 
inner join sys_user u on t.actor_objid = u.objid 
where t.state not like '%cancelledfaas%'
go 



/*===================================================
* DELINQUENCY UPDATE 
====================================================*/


alter table report_rptdelinquency_barangay add idx int
go 

update report_rptdelinquency_barangay set idx = 0 where idx is null
go 


create view vw_faas_lookup
as 
SELECT 
f.*,
e.name as taxpayer_name, 
e.address_text as taxpayer_address,
pc.code AS classification_code, 
pc.code AS classcode, 
pc.name AS classification_name, 
pc.name AS classname, 
r.ry, r.rputype, r.totalmv, r.totalav,
r.totalareasqm, r.totalareaha, r.suffix, r.rpumasterid, 
rp.barangayid, rp.cadastrallotno, rp.blockno, rp.surveyno, rp.pintype, 
rp.section, rp.parcel, rp.stewardshipno, rp.pin, 
b.name AS barangay_name 
FROM faas f 
INNER JOIN faas_list fl on f.objid = fl.objid 
INNER JOIN rpu r ON f.rpuid = r.objid 
INNER JOIN realproperty rp ON f.realpropertyid = rp.objid 
INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
INNER JOIN barangay b ON rp.barangayid = b.objid 
INNER JOIN entity e on f.taxpayer_objid = e.objid
go 