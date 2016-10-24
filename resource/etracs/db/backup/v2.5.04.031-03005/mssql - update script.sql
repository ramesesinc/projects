/* v2.5.04.032 */
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode, allowEditOwner, checkbalance, allowEditPin, allowEditPinInfo, allowEditAppraisal, opener) VALUES ('CA', 'Change Administrator', '0', '0', '0', 'DP', '0', '1', '0', '0', '1', NULL)
go 


CREATE TABLE cancelledfaas_task (
  objid varchar(50) NOT NULL primary key,
  refid varchar(50) NOT NULL,
  parentprocessid varchar(50) NULL,
  state varchar(50) NULL,
  startdate datetime  NULL,
  enddate datetime  NULL,
  assignee_objid varchar(50)  NULL,
  assignee_name varchar(100)  NULL,
  assignee_title varchar(80)  NULL,
  actor_objid varchar(50)  NULL,
  actor_name varchar(100)  NULL,
  actor_title varchar(80)  NULL,
  message varchar(255)  NULL,
  signature text
)
go 

create index ix_refid  on cancelledfaas_task(refid)
go

alter table cancelledfaas_task 
  add CONSTRAINT FK_cancelledfaas_task_cancelledfaas 
	FOREIGN KEY (refid) REFERENCES cancelledfaas (objid)
go 


create table cancelledfaas_signatory (
  objid varchar(50) not null,
  taxmapper_objid varchar(50) null,
  taxmapper_name varchar(100) null,
  taxmapper_title varchar(50) null,
  taxmapper_dtsigned datetime null,
  taxmapper_taskid varchar(50) null,
  taxmapperchief_objid varchar(50) null,
  taxmapperchief_name varchar(100) null,
  taxmapperchief_title varchar(50) null,
  taxmapperchief_dtsigned datetime null,
  taxmapperchief_taskid varchar(50) null,
  appraiser_objid varchar(50) null,
  appraiser_name varchar(150) null,
  appraiser_title varchar(50) null,
  appraiser_dtsigned datetime null,
  appraiser_taskid varchar(50) null,
  appraiserchief_objid varchar(50) null,
  appraiserchief_name varchar(100) null,
  appraiserchief_title varchar(50) null,
  appraiserchief_dtsigned datetime null,
  appraiserchief_taskid varchar(50) null,
  recommender_objid varchar(50) null,
  recommender_name varchar(100) null,
  recommender_title varchar(50) null,
  recommender_dtsigned datetime null,
  recommender_taskid varchar(50) null,
  provtaxmapper_objid varchar(50) null,
  provtaxmapper_name varchar(100) null,
  provtaxmapper_title varchar(50) null,
  provtaxmapper_dtsigned datetime null,
  provtaxmapper_taskid varchar(50) null,
  provtaxmapperchief_objid varchar(50) null,
  provtaxmapperchief_name varchar(100) null,
  provtaxmapperchief_title varchar(50) null,
  provtaxmapperchief_dtsigned datetime null,
  provtaxmapperchief_taskid varchar(50) null,
  provappraiser_objid varchar(50) null,
  provappraiser_name varchar(100) null,
  provappraiser_title varchar(50) null,
  provappraiser_dtsigned datetime null,
  provappraiser_taskid varchar(50) null,
  provappraiserchief_objid varchar(50) null,
  provappraiserchief_name varchar(100) null,
  provappraiserchief_title varchar(50) null,
  provappraiserchief_dtsigned datetime null,
  provappraiserchief_taskid varchar(50) null,
  approver_objid varchar(50) null,
  approver_name varchar(100) null,
  approver_title varchar(50) null,
  approver_dtsigned datetime null,
  approver_taskid varchar(50) null,
  provapprover_objid varchar(50) null,
  provapprover_name varchar(100) null,
  provapprover_title varchar(50) null,
  provapprover_dtsigned datetime null,
  provapprover_taskid varchar(50) null,
  provrecommender_objid varchar(50) null,
  provrecommender_name varchar(100) null,
  provrecommender_title varchar(50) null,
  provrecommender_dtsigned datetime null,
  provrecommender_taskid varchar(50) null,
  primary key  (objid)
) 
go 


alter table cancelledfaas_signatory 
  add constraint FK_cancelledfaas_signatory_cancelled_faas 
  foreign key (objid) references cancelledfaas (objid)
go 


alter table cancelledfaas alter column reason_objid varchar(50) null
go 

alter table cancelledfaas alter column remarks text null
go 


alter table cancelledfaas add [online] int null
go 

update cancelledfaas set [online] = 0 where [online] is null
go 

alter table cancelledfaas add lguid varchar(50)
go 
alter table cancelledfaas add lasttaxyear int
go 


delete from sys_wf_transition where processname = 'cancelledfaas';
delete from sys_wf_node where processname = 'cancelledfaas';

INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('start', 'cancelledfaas', 'Start', 'start', '1', NULL, 'RPT', NULL);
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-receiver', 'cancelledfaas', 'Assign Receiver', 'state', '2', '0', 'RPT', 'RECEIVER');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('receiver', 'cancelledfaas', 'Review and Verification', 'state', '5', NULL, 'RPT', 'RECEIVER');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-examiner', 'cancelledfaas', 'For Examination', 'state', '10', NULL, 'RPT', 'EXAMINER');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('examiner', 'cancelledfaas', 'Examination', 'state', '15', NULL, 'RPT', 'EXAMINER');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-taxmapper', 'cancelledfaas', 'For Taxmapping', 'state', '20', NULL, 'RPT', 'TAXMAPPER');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('taxmapper', 'cancelledfaas', 'Taxmapping', 'state', '25', NULL, 'RPT', 'TAXMAPPER');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-provtaxmapperchief', 'cancelledfaas', 'For Taxmapping Approval', 'state', '25', '0', 'RPT', 'TAXMAPPER_CHIEF');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-taxmapping-approval', 'cancelledfaas', 'For Taxmapper Chief Approval', 'state', '30', NULL, 'RPT', 'TAXMAPPER_CHIEF');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('provtaxmapperchief', 'cancelledfaas', 'Taxmapping Chief Approval', 'state', '35', NULL, 'RPT', 'TAXMAPPER_CHIEF');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('taxmapper_chief', 'cancelledfaas', 'Taxmapping Approval', 'state', '35', NULL, 'RPT', 'TAXMAPPER_CHIEF');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-appraiser', 'cancelledfaas', 'For Appraisal', 'state', '40', NULL, 'RPT', 'APPRAISER');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('appraiser', 'cancelledfaas', 'Appraisal', 'state', '45', NULL, 'RPT', 'APPRAISER');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-appraisal-chief', 'cancelledfaas', 'For Appraisal Chief Approval', 'state', '50', NULL, 'RPT', 'APPRAISAL_CHIEF');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('appraiser_chief', 'cancelledfaas', 'Appraisal Chief Approval', 'state', '55', NULL, 'RPT', 'APPRAISAL_CHIEF');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('provappraiserchief', 'cancelledfaas', 'Appraisal Chief Approval', 'state', '55', NULL, 'RPT', 'APPRAISAL_CHIEF');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-recommender', 'cancelledfaas', 'For Recommending Approval', 'state', '70', NULL, 'RPT', 'RECOMMENDER');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-provrecommender', 'cancelledfaas', 'For Recommending Approval', 'state', '71', NULL, 'RPT', 'RECOMMENDER');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('recommender', 'cancelledfaas', 'Recommending Approval', 'state', '75', NULL, 'RPT', 'RECOMMENDER');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('provrecommender', 'cancelledfaas', 'Recommending Approval', 'state', '76', NULL, 'RPT', 'RECOMMENDER');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-approver', 'cancelledfaas', 'For Assessor Approval', 'state', '80', NULL, 'RPT', 'APPROVER,ASSESSOR');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('approver', 'cancelledfaas', 'Assessor Approval', 'state', '85', NULL, 'RPT', 'APPROVER,ASSESSOR');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-provtaxmapper', 'cancelledfaas', 'For Taxmapping', 'state', '200', '0', 'RPT', 'TAXMAPPER');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-provappraiserchief', 'cancelledfaas', 'For Appraisal Chief Approval', 'state', '201', '0', 'RPT', 'APPRAISAL_CHIEF');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('provtaxmapper', 'cancelledfaas', 'Taxmapping', 'state', '205', '0', 'RPT', 'TAXMAPPER');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-provappraiser', 'cancelledfaas', 'For Appraisal', 'state', '210', '0', 'RPT', 'APPRAISER');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('provappraiser', 'cancelledfaas', 'Appraisal', 'state', '215', '0', 'RPT', 'APPRAISER');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-provapprover', 'cancelledfaas', 'For Provincial Assessor Approval', 'state', '220', '0', 'RPT', 'APPROVER');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('provapprover', 'cancelledfaas', 'Provincial Assessor Approval', 'state', '230', NULL, 'RPT', 'APPROVER,ASSESSOR');
INSERT INTO [sys_wf_node] ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('end', 'cancelledfaas', 'End', 'end', '1000', NULL, 'RPT', NULL);

INSERT INTO [sys_wf_transition] ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('start', 'cancelledfaas', NULL, 'receiver', '1', NULL, NULL, NULL);
INSERT INTO [sys_wf_transition] ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('receiver', 'cancelledfaas', 'delete', 'end', '5', NULL, '[caption:''Delete'', confirm:''Delete record?'', closeonend:true]', NULL);
INSERT INTO [sys_wf_transition] ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('receiver', 'cancelledfaas', 'submit', 'assign-taxmapper', '16', NULL, '[caption:''Submit for Taxmapping'', confirm:''Submit for taxmapping?'', messagehandler:''default'']', NULL);
INSERT INTO [sys_wf_transition] ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('assign-taxmapper', 'cancelledfaas', NULL, 'taxmapper', '20', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL);
INSERT INTO [sys_wf_transition] ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('taxmapper', 'cancelledfaas', 'return_receiver', 'receiver', '25', NULL, '[caption:''Return to Receiver'',confirm:''Return to receiver?'',messagehandler:''default'']', NULL);
INSERT INTO [sys_wf_transition] ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('taxmapper', 'cancelledfaas', 'submit', 'assign-recommender', '27', NULL, '[caption:''Submit for Recommending Approval'', confirm:''Submit?'', messagehandler:''rptmessage:create'']', NULL);
INSERT INTO [sys_wf_transition] ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('assign-recommender', 'cancelledfaas', NULL, 'recommender', '70', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL);
INSERT INTO [sys_wf_transition] ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('recommender', 'cancelledfaas', 'return_appraiser', 'appraiser', '72', NULL, '[caption:''Return to Appraiser'',confirm:''Return to appraiser?'', messagehandler:''default'']', NULL);
INSERT INTO [sys_wf_transition] ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('recommender', 'cancelledfaas', 'return_taxmapper', 'taxmapper', '73', NULL, '[caption:''Return to Taxmapper'',confirm:''Return to taxmapper?'', messagehandler:''default'']', NULL);
INSERT INTO [sys_wf_transition] ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('recommender', 'cancelledfaas', 'submit_approver', 'assign-approver', '74', NULL, '[caption:''Submit for Assessor Approval'', confirm:''Submit to assessor approval?'', messagehandler:''default'']', NULL);
INSERT INTO [sys_wf_transition] ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('recommender', 'cancelledfaas', 'submit_to_province', 'approver', '75', NULL, '[caption:''Submit to Province'', confirm:''Submit to Province?'', messagehandler:''rptmessage:create'']', NULL);
INSERT INTO [sys_wf_transition] ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('approver', 'cancelledfaas', 'approve', 'end', '110', NULL, '[caption:''Manually Approve'',confirm:''Manually approve cancellation?'', visible:false]', NULL);


