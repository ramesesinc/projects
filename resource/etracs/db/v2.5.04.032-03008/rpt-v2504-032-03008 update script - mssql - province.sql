/* v2.5.04.032-03008 */
alter table sys_rule_actiondef add actionclass varchar(100)
go 

create table faas_restriction_type
(
	objid varchar(50),
	name varchar(100) not null,
	idx int not null,
	isother int not null,
	primary key(objid)
)
go 


INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('BOUNDARY_CONFLICT', 'Boundary Conflict', '4', '1')
go 
INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('BSP_GSP', 'BSP / GSP', '9', '1')
go 
INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('CARP', 'Under CARP', '1', '0')
go 
INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('RED_AREAS', 'Red Areas', '3', '1')
go 
INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('RP_NIA', 'RP / NIA', '5', '1')
go 
INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('TELECOM', 'Telecom', '6', '1')
go 
INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('UNDER_LITIGATION', 'Under Litigation', '2', '0')
go 
INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('UNDETERMINED', 'Undermined', '7', '1')
go 
INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('UNLOCATED_OWNER', 'Unlocated Owner', '8', '1')
go 
INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('RESTRICTED', 'Restricted', '9', '1')
go 



INSERT INTO sys_usergroup (objid, title, domain, userclass, orgclass, role) 
VALUES ('RPT.REPORT', 'REPORT', 'RPT', 'usergroup', NULL, 'REPORT')
go 

INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('RPT.REPORT-faas-titled-report-viewreport', 'RPT.REPORT', 'faas-titled-report', 'viewreport', 'View Report')
go 


alter table subdivision_cancelledimprovement add lasttaxyear int
go 
alter table subdivision_cancelledimprovement add lguid varchar(50)
go 
alter table subdivision_cancelledimprovement add reason_objid varchar(50)
go 



/* RPTLEDGER: add blockno info */
alter table rptledger add blockno varchar(50)
go 

create index ix_rptledger_blockno on rptledger(blockno)
go 

update rl set 
	rl.blockno = rp.blockno
from rptledger rl, faas f, realproperty rp
where rl.faasid = f.objid and f.realpropertyid = rp.objid
go 




/* PC and DT txn types */
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode, allowEditOwner, checkbalance, allowEditPin, allowEditPinInfo, allowEditAppraisal, opener) VALUES ('PC', 'Physical Obsolence', '0', '1', '0', 'PC', '0', '0', '0', '0', '1', NULL)
go 
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode, allowEditOwner, checkbalance, allowEditPin, allowEditPinInfo, allowEditAppraisal, opener) VALUES ('DT', 'Destruction of Property', '0', '1', '0', 'DT', '0', '0', '0', '0', '1', NULL)
go 




alter table faas_txntype add reconcileledger int 
GO 
update faas_txntype set reconcileledger = 1 where reconcileledger is null
go 

INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode, allowEditOwner, checkbalance, allowEditPin, allowEditPinInfo, allowEditAppraisal, opener, reconcileledger) VALUES ('UK', 'Unknown to Known', '1', '1', '1', 'DP', '1', '0', '0', '0', '1', NULL, '0')
go 
update faas_txntype set reconcileledger = 0 where objid = 'UK'
go 

drop index rptledger.ux_rptledger_fullpin
go 




/* CANCELLED FAAS */

alter table cancelledfaas add originlguid varchar(50)
go 

update cancelledfaas set originlguid = lguid where originlguid is null
go 


delete from sys_wf_transition where processname = 'cancelledfaas'
go 	

    INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('start', 'cancelledfaas', '', 'assign-receiver', '1', NULL, NULL, NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('assign-receiver', 'cancelledfaas', '', 'receiver', '2', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'', immediate:true]', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('receiver', 'cancelledfaas', 'submit_examiner', 'assign-examiner', '5', NULL, '[caption:''Submit For Examination'', confirm:''Submit?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('receiver', 'cancelledfaas', 'submit_taxmapper', 'assign-provtaxmapper', '6', NULL, '[caption:''Submit For Taxmapping'', confirm:''Submit?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('receiver', 'cancelledfaas', 'disapprove', 'end', '7', '#{data.lguid.replaceAll(''-'','') == data.originlguid.replaceAll(''-'','')}', '[caption:''Disapprove'', confirm:''Disapprove?'', messagehandler:''default'', closeonend:true]', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('receiver', 'cancelledfaas', 'delete', 'end', '8', '#{data.originlguid.replaceAll(''-'','') == env.ORGID.replaceAll(''-'','')}', '[caption:''Delete'', confirm:''Delete?'', messagehandler:''default'', closeonend:true]', '')
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('assign-examiner', 'cancelledfaas', '', 'examiner', '10', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('examiner', 'cancelledfaas', 'return_receiver', 'receiver', '15', NULL, '[caption:''Return to Receiver'',confirm:''Return to receiver?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('examiner', 'cancelledfaas', 'submit', 'assign-provtaxmapper', '16', NULL, '[caption:''Submit for Taxmapping'', confirm:''Submit for taxmapping?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('assign-provtaxmapper', 'cancelledfaas', '', 'provtaxmapper', '20', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('provtaxmapper', 'cancelledfaas', 'return_receiver', 'receiver', '25', NULL, '[caption:''Return to Receiver'',confirm:''Return to receiver?'',messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('provtaxmapper', 'cancelledfaas', 'return_examiner', 'examiner', '26', NULL, '[caption:''Return to Examiner'',confirm:''Return to examiner?'',messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('provtaxmapper', 'cancelledfaas', 'submit', 'assign-provtaxmapperchief', '27', NULL, '[caption:''Submit for Approval'', confirm:''Submit for approval?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('assign-provtaxmapperchief', 'cancelledfaas', '', 'provtaxmapperchief', '30', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('provtaxmapperchief', 'cancelledfaas', 'return_receiver', 'receiver', '31', NULL, '[caption:''Return to Receiver'',confirm:''Return to receiver?'',messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('provtaxmapperchief', 'cancelledfaas', 'return_taxmapper', 'provtaxmapper', '32', NULL, '[caption:''Return to Taxmapper'',confirm:''Return to taxmapper?'',messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('provtaxmapperchief', 'cancelledfaas', 'submit', 'assign-provrecommender', '33', NULL, '[caption:''Submit for Recommending Approval'', confirm:''Submit?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('assign-provrecommender', 'cancelledfaas', '', 'provrecommender', '60', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('provrecommender', 'cancelledfaas', 'return_taxmapper', 'taxmapper', '62', NULL, '[caption:''Return to Taxmapper'',confirm:''Return to taxmapper?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('provrecommender', 'cancelledfaas', 'submit_approver', 'assign-approver', '63', NULL, '[caption:''Submit for Assessor Approval'', confirm:''Submit to assessor approval?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('assign-approver', 'cancelledfaas', '', 'approver', '80', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('approver', 'cancelledfaas', 'return_receiver', 'receiver', '83', NULL, '[caption:''Return to Receiver'',confirm:''Return to receiver?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('approver', 'cancelledfaas', 'return_provtaxmapper', 'provtaxmapper', '85', NULL, '[caption:''Return to Taxmapper'',confirm:''Return to Taxmapper?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('approver', 'cancelledfaas', 'approve', 'end', '100', NULL, '[caption:''Approve'', confirm:''Approve FAAS?'', messagehandler:\"default\", closeonend:false]', NULL)
go 



update sys_wf_transition set action = '' where action is null
go 


alter table sys_wf_transition alter column action  varchar(50) not null
go 

-- recreate sys_wf_transition primary key
DECLARE @table NVARCHAR(512)
declare @sql NVARCHAR(MAX)

SELECT @table = N'sys_wf_transition'

SELECT @sql = 'ALTER TABLE ' + @table 
    + ' DROP CONSTRAINT ' + name + ';'
    FROM sys.key_constraints
    WHERE [type] = 'PK'
    AND [parent_object_id] = OBJECT_ID(@table)

EXEC sp_executeSQL @sql
go 


alter table sys_wf_transition add primary key(processname, parentid, [action], [to])
go 
