/* v2.5.04.032-03006 */
alter table cancelledfaas add txnno varchar(25) null
go
create index ix_cancelledfaas_txnno on cancelledfaas(txnno)
go 



delete from sys_wf_transition where processname = 'cancelledfaas'
go 

INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('start', 'cancelledfaas', NULL, 'receiver', '1', NULL, NULL, NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('receiver', 'cancelledfaas', 'delete', 'end', '5', NULL, '[caption:''Delete'', confirm:''Delete record?'', closeonend:true]', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('receiver', 'cancelledfaas', 'submit', 'assign-taxmapper', '16', NULL, '[caption:''Submit for Taxmapping'', confirm:''Submit for taxmapping?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('assign-taxmapper', 'cancelledfaas', NULL, 'taxmapper', '20', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('taxmapper', 'cancelledfaas', 'return_receiver', 'receiver', '25', NULL, '[caption:''Return to Receiver'',confirm:''Return to receiver?'',messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('taxmapper', 'cancelledfaas', 'submit', 'assign-recommender', '27', NULL, '[caption:''Submit for Recommending Approval'', confirm:''Submit?'', messagehandler:''rptmessage:create'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('assign-recommender', 'cancelledfaas', NULL, 'recommender', '70', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('recommender', 'cancelledfaas', 'return_appraiser', 'appraiser', '72', NULL, '[caption:''Return to Appraiser'',confirm:''Return to appraiser?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('recommender', 'cancelledfaas', 'return_taxmapper', 'taxmapper', '73', NULL, '[caption:''Return to Taxmapper'',confirm:''Return to taxmapper?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('recommender', 'cancelledfaas', 'submit_approver', 'assign-approver', '74', NULL, '[caption:''Submit for Assessor Approval'', confirm:''Submit to assessor approval?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('recommender', 'cancelledfaas', 'submit_to_province', 'approver', '75', NULL, '[caption:''Submit to Province'', confirm:''Submit to Province?'', messagehandler:''rptmessage:create'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('assign-approver', 'cancelledfaas', NULL, 'approver', '80', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('approver', 'cancelledfaas', 'approve', 'end', '110', NULL, '[caption:''Manually Approve'',confirm:''Manually approve cancellation?'', visible:false]', NULL)
go 



update fl set 
	fl.state = f.state,
	fl.tdno = f.tdno,
	fl.utdno = f.utdno, 
	fl.prevtdno = f.prevtdno, 
	fl.displaypin = f.fullpin, 
	fl.pin = case when r.rputype = 'land' then rp.pin else (rp.pin + '-' + convert(varchar(4),r.suffix)) end, 
	fl.taxpayer_objid = f.taxpayer_objid,
	fl.owner_name = f.owner_name,
	fl.owner_address = f.taxpayer_objid,
	fl.administrator_name = f.administrator_name,
	fl.administrator_address = f.administrator_address,
	fl.barangayid = b.objid, 
	fl.barangay = b.name, 
	fl.classification_objid = r.classification_objid,
	fl.classcode = pc.code, 
	fl.cadastrallotno = rp.cadastrallotno, 
	fl.blockno = rp.blockno,
	fl.surveyno = rp.surveyno,
	fl.titleno = f.titleno,
	fl.totalareaha = r.totalareaha,
	fl.totalareasqm = r.totalareasqm,
	fl.totalmv = r.totalmv,
	fl.totalav = r.totalav,
	fl.effectivityyear = f.effectivityyear,
	fl.effectivityqtr = f.effectivityqtr,
	fl.cancelreason = f.cancelreason,
	fl.cancelledbytdnos = f.cancelledbytdnos,
	fl.taskid = (select top 1 objid from faas_task where refid = f.objid and enddate is null) ,
	fl.taskstate = (select top 1 state from faas_task where refid = f.objid and enddate is null) ,
	fl.assignee_objid = (select top 1 assignee_objid from faas_task where refid = f.objid and enddate is null) ,
	fl.trackingno = (select top 1 trackingno from rpttracking where objid = f.objid) 
from faas_list fl, faas f, rpu r, propertyclassification pc, realproperty rp, barangay b 
where fl.objid = f.objid 
and f.rpuid = r.objid 
and f.realpropertyid = rp.objid 
and rp.barangayid = b.objid 
and r.classification_objid = pc.objid
go 


