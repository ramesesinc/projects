[findRevisedCount]
select count(*) as revisedcount 
from batchgr_items_forrevision 
where barangayid = $P{barangayid}
and rputype like $P{rputype}


[insertItemsForRevision]
insert into batchgr_items_forrevision(
  objid,
  rpuid,
  realpropertyid,
  barangayid,
  rputype,
  tdno,
  fullpin,
  pin,
  suffix
)
SELECT 
  f.objid,
  f.rpuid,
  f.realpropertyid,
  rp.barangayid,
  r.rputype,
  f.tdno,
  f.fullpin,
  rp.pin,
  r.suffix
FROM faas f 
    INNER JOIN rpu r ON f.rpuid = r.objid 
    INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
    INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
    INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE rp.barangayid = $P{barangayid}
  AND r.rputype LIKE $P{rputype}
  AND r.ry < $P{newry}
  AND f.state = 'CURRENT'
  AND NOT EXISTS(SELECT * FROM batchgr_error WHERE objid = f.objid)
  AND NOT EXISTS(SELECT * FROM batchgr_log WHERE objid = f.objid)
ORDER BY rp.pin, r.suffix 


[findFaasForRevision]
SELECT 
  f.*,
  pc.code AS classification_code, 
  pc.code AS classcode, 
  pc.name AS classification_name, 
  pc.name AS classname, 
  r.ry, f.realpropertyid, r.rputype, r.fullpin, r.totalmv, r.totalav,
  r.totalareasqm, r.totalareaha, 
  rp.barangayid, rp.cadastrallotno, rp.blockno, rp.surveyno, rp.lgutype, rp.pintype, 
  b.name AS barangay_name
  FROM faas f 
    INNER JOIN rpu r ON f.rpuid = r.objid 
    INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
    INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
    INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE f.objid = $P{objid}


[insertRevisedRealProperties]
insert into realproperty(
  objid,
  state,
  autonumber,
  pintype,
  pin,
  ry,
  claimno,
  section,
  parcel,
  cadastrallotno,
  blockno,
  surveyno,
  street,
  purok,
  north,
  south,
  east,
  west,
  barangayid,
  lgutype,
  previd,
  lguid,
  stewardshipno
)
select 
  replace(rp.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})) as objid,
  'INTERIM' as state,
  rp.autonumber,
  rp.pintype,
  rp.pin,
  $P{newry} as ry,
  rp.claimno,
  rp.section,
  rp.parcel,
  rp.cadastrallotno,
  rp.blockno,
  rp.surveyno,
  rp.street,
  rp.purok,
  rp.north,
  rp.south,
  rp.east,
  rp.west,
  rp.barangayid,
  rp.lgutype,
  rp.objid as previd,
  rp.lguid,
  rp.stewardshipno
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = 'land'
and r.ry < $P{newry}
and not exists(select objid from realproperty where objid=replace(rp.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})))


[insertRevisedRpus]
insert into rpu(
  objid,
  state,
  realpropertyid,
  rputype,
  ry,
  fullpin,
  suffix,
  subsuffix,
  classification_objid,
  exemptiontype_objid,
  taxable,
  totalareaha,
  totalareasqm,
  totalbmv,
  totalmv,
  totalav,
  hasswornamount,
  swornamount,
  useswornamount,
  previd,
  rpumasterid,
  reclassed,
  stewardparentrpumasterid
)
select 
  replace(r.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),${snewry})) as objid,
  'INTERIM' as state,
  (select objid from realproperty where objid = replace(r.realpropertyid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),${snewry}))) as realpropertyid,
  r.rputype,
  ${snewry} as ry,
  r.fullpin,
  r.suffix,
  r.subsuffix,
  r.classification_objid,
  r.exemptiontype_objid,
  r.taxable,
  r.totalareaha,
  r.totalareasqm,
  r.totalbmv,
  r.totalmv,
  r.totalav,
  r.hasswornamount,
  r.swornamount,
  r.useswornamount,
  r.previd,
  r.rpumasterid,
  r.reclassed,
  r.stewardparentrpumasterid
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = $P{rputype}
and r.ry < $P{newry}
and not exists(select objid from rpu where objid = replace(r.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),${snewry})))


[insertRevisedFaases]
insert into faas(
  objid,
  state,
  rpuid,
  datacapture,
  autonumber,
  utdno,
  tdno,
  txntype_objid,
  effectivityyear,
  effectivityqtr,
  titletype,
  titleno,
  titledate,
  taxpayer_objid,
  owner_name,
  owner_address,
  administrator_objid,
  administrator_name,
  administrator_address,
  beneficiary_objid,
  beneficiary_name,
  beneficiary_address,
  memoranda,
  backtaxyrs,
  prevtdno,
  lguid,
  name,
  dtapproved,
  realpropertyid,
  lgutype,
  signatories,
  fullpin,
  year,
  qtr,
  month,
  day,
  prevadministrator,
  originlguid,
  parentfaasid
)
select 
  replace(f.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})) as objid,
  'INTERIM' as state,
  replace(f.rpuid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})) as rpuid,
  1 as datacapture,
  f.autonumber,
  concat(utdno, '-', $P{newry}) as utdno,
  null as tdno,
  'GR' as txntype_objid,
  ($P{newry} + 1) as effectivityyear,
  1 as effectivityqtr,
  f.titletype,
  f.titleno,
  f.titledate,
  f.taxpayer_objid,
  f.owner_name,
  f.owner_address,
  f.administrator_objid,
  f.administrator_name,
  f.administrator_address,
  f.beneficiary_objid,
  f.beneficiary_name,
  f.beneficiary_address,
  $P{memoranda} AS memoranda,
  0 as backtaxyrs,
  f.tdno as prevtdno,
  f.lguid,
  f.name,
  f.dtapproved,
  replace(f.realpropertyid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})) as realpropertyid,
  f.lgutype,
  null as signatories,
  f.fullpin,
  0 as year,
  0 as qtr,
  0 as month,
  0 as day,
  f.prevadministrator,
  f.originlguid,
  f.parentfaasid
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = $P{rputype}
and r.ry < $P{newry}
and not exists(select objid from faas where objid = replace(f.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})))



[insertRevisedFaasList]
insert into faas_list (
  objid,
  state,
  rpuid,
  realpropertyid,
  datacapture,
  ry,
  txntype_objid,
  tdno,
  utdno,
  prevtdno,
  displaypin,
  pin,
  taxpayer_objid,
  owner_name,
  owner_address,
  administrator_name,
  administrator_address,
  rputype,
  barangayid,
  barangay,
  classification_objid,
  classcode,
  cadastrallotno,
  blockno,
  surveyno,
  titleno,
  totalareaha,
  totalareasqm,
  totalmv,
  totalav,
  effectivityyear,
  effectivityqtr,
  cancelreason,
  cancelledbytdnos,
  lguid,
  originlguid,
  yearissued,
  taskid,
  taskstate,
  assignee_objid,
  trackingno,
  publicland
)
select 
  f.objid,
  f.state,
  f.rpuid,
  f.realpropertyid,
  f.datacapture,
  rp.ry,
  f.txntype_objid,
  f.tdno,
  f.utdno,
  f.prevtdno,
  f.fullpin as displaypin,
  case when r.rputype = 'land' then rp.pin else (rp.pin + '-' + convert(varchar(4),r.suffix)) end as pin,
  f.taxpayer_objid,
  f.owner_name,
  f.owner_address,
  f.administrator_name,
  f.administrator_address,
  r.rputype,
  rp.barangayid,
  b.name as barangay,
  r.classification_objid,
  pc.code as classcode,
  rp.cadastrallotno,
  rp.blockno,
  rp.surveyno,
  f.titleno,
  r.totalareaha,
  r.totalareasqm,
  r.totalmv,
  r.totalav,
  f.effectivityyear,
  f.effectivityqtr,
  null as cancelreason,
  null as cancelledbytdnos,
  f.lguid,
  f.originlguid,
  $P{newry} + 1 as yearissued,
  null as taskid,
  null as taskstate,
  null as assignee_objid,
  null as trackingno,
  0 as publicland
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join propertyclassification pc on r.classification_objid = pc.objid 
  inner join barangay b on rp.barangayid = b.objid 
where rp.barangayid = $P{barangayid}
and r.rputype = $P{rputype}
and r.ry = $P{newry}
and not exists(select objid from faas_list where objid = f.objid)





[insertRevisedSignatories]
insert into faas_signatory(
  objid,
  appraiser_name,
  appraiser_title,
  appraiser_dtsigned,
  recommender_name,
  recommender_title,
  recommender_dtsigned,
  approver_name,
  approver_title,
  approver_dtsigned,
  provapprover_name,
  provapprover_title,
  provapprover_dtsigned
)
select 
  replace(f.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})) as objid,
  $P{appraisername} as appraiser_name,
  $P{appraisertitle} as appraiser_title,
  $P{appraiserdtsigned} as appraiser_dtsigned,
  $P{recommendername} as recommender_name,
  $P{recommendertitle} as recommender_title,
  $P{recommenderdtsigned} as recommender_dtsigned,
  $P{approvername} as approver_name,
  $P{approvertitle} as approver_title,
  $P{approverdtsigned} as approver_dtsigned,
  $P{provapprovername} as provapprover_name,
  $P{provapprovertitle} as provapprover_title,
  $P{provapproverdtsigned} as provapprover_dtsigned
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = $P{rputype}
and r.ry < $P{newry}
and not exists(select objid from faas_signatory where objid = replace(f.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})))


[insertRevisedPreviousFaases]
insert into faas_previous(
  objid,
  faasid,
  prevfaasid,
  prevrpuid,
  prevtdno,
  prevpin,
  prevowner,
  prevadministrator,
  prevav,
  prevmv,
  prevareasqm,
  prevareaha,
  preveffectivity
)
select 
  replace(f.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})) as objid,
  replace(f.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})) as faasid,
  f.objid as prevfaasid,
  f.rpuid as prevrpuid,
  f.tdno as prevtdno,
  f.fullpin as prevpin,
  f.owner_name as prevowner,
  f.administrator_name as prevadministrator,
  r.totalav as prevav,
  r.totalmv as prevmv,
  r.totalareasqm as  prevareasqm,
  r.totalareaha as  prevareaha,
  f.effectivityyear preveffectivity
from faas f 
  inner join realproperty rp on f.realpropertyid = rp.objid
  inner join rpu r on f.rpuid = r.objid 
  inner join batchgr_items_forrevision xbi on f.objid = xbi.objid 
where rp.barangayid = $P{barangayid}
and f.state = 'current'
and r.rputype = $P{rputype}
and r.ry < $P{newry}
and not exists(select objid from faas_previous where objid = replace(f.objid, '-'+convert(varchar(4),rp.ry), '') + ('-' + convert(varchar(4),$P{newry})))



