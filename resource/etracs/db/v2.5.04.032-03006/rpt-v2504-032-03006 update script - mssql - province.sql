/* v2.5.04.032-03006 */
alter table cancelledfaas add txnno varchar(25) null
go
create index ix_cancelledfaas_txnno on cancelledfaas(txnno)
go 


delete from sys_wf_transition where processname = 'cancelledfaas'
go 

INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('start', 'cancelledfaas', NULL, 'assign-receiver', '1', NULL, NULL, NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('assign-receiver', 'cancelledfaas', NULL, 'receiver', '2', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'', immediate:true]', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('receiver', 'cancelledfaas', 'submit_examiner', 'assign-examiner', '5', NULL, '[caption:''Submit For Examination'', confirm:''Submit?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('receiver', 'cancelledfaas', 'submit_taxmapper', 'assign-provtaxmapper', '6', NULL, '[caption:''Submit For Taxmapping'', confirm:''Submit?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('assign-examiner', 'cancelledfaas', NULL, 'examiner', '10', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('examiner', 'cancelledfaas', 'return_receiver', 'receiver', '15', NULL, '[caption:''Return to Receiver'',confirm:''Return to receiver?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('examiner', 'cancelledfaas', 'submit', 'assign-provtaxmapper', '16', NULL, '[caption:''Submit for Taxmapping'', confirm:''Submit for taxmapping?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('assign-provtaxmapper', 'cancelledfaas', NULL, 'provtaxmapper', '20', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL)
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
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('assign-approver', 'cancelledfaas', NULL, 'approver', '80', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('approver', 'cancelledfaas', 'return_receiver', 'receiver', '83', NULL, '[caption:''Return to Receiver'',confirm:''Return to receiver?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('approver', 'cancelledfaas', 'return_provtaxmapper', 'provtaxmapper', '85', NULL, '[caption:''Return to Taxmapper'',confirm:''Return to Taxmapper?'', messagehandler:''default'']', NULL)
go 
INSERT INTO sys_wf_transition (parentid, processname, action, [to], idx, eval, properties, permission) VALUES ('approver', 'cancelledfaas', 'approve', 'end', '100', NULL, '[caption:''Approve'', confirm:''Approve FAAS?'', messagehandler:\"default\", closeonend:false]', NULL)
go 



