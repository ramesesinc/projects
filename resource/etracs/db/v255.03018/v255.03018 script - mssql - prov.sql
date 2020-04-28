DELETE FROM sys_wf_transition WHERE processname = 'batchgr'
go
DELETE FROM sys_wf_node WHERE processname = 'batchgr'
go


INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('start', 'batchgr', 'Start', 'start', '1', NULL, 'RPT', NULL, NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('assign-receiver', 'batchgr', 'For Review and Verification', 'state', '2', NULL, 'RPT', 'RECEIVER', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('receiver', 'batchgr', 'Review and Verification', 'state', '5', NULL, 'RPT', 'RECEIVER', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('assign-examiner', 'batchgr', 'For Examination', 'state', '10', NULL, 'RPT', 'EXAMINER', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('examiner', 'batchgr', 'Examination', 'state', '15', NULL, 'RPT', 'EXAMINER', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('assign-taxmapper', 'batchgr', 'For Taxmapping', 'state', '20', NULL, 'RPT', 'TAXMAPPER', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('assign-provtaxmapper', 'batchgr', 'For Taxmapping', 'state', '20', NULL, 'RPT', 'TAXMAPPER', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('taxmapper', 'batchgr', 'Taxmapping', 'state', '25', NULL, 'RPT', 'TAXMAPPER', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('provtaxmapper', 'batchgr', 'Taxmapping', 'state', '25', NULL, 'RPT', 'TAXMAPPER', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('assign-taxmapping-approval', 'batchgr', 'For Taxmapping Approval', 'state', '30', NULL, 'RPT', 'TAXMAPPER_CHIEF', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('taxmapper_chief', 'batchgr', 'Taxmapping Approval', 'state', '35', NULL, 'RPT', 'TAXMAPPER_CHIEF', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('assign-appraiser', 'batchgr', 'For Appraisal', 'state', '40', NULL, 'RPT', 'APPRAISER', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('assign-provappraiser', 'batchgr', 'For Appraisal', 'state', '40', NULL, 'RPT', 'APPRAISER', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('appraiser', 'batchgr', 'Appraisal', 'state', '45', NULL, 'RPT', 'APPRAISER', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('provappraiser', 'batchgr', 'Appraisal', 'state', '45', NULL, 'RPT', 'APPRAISER', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('assign-appraisal-chief', 'batchgr', 'For Appraisal Approval', 'state', '50', NULL, 'RPT', 'APPRAISAL_CHIEF', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('appraiser_chief', 'batchgr', 'Appraisal Approval', 'state', '55', NULL, 'RPT', 'APPRAISAL_CHIEF', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('assign-recommender', 'batchgr', 'For Recommending Approval', 'state', '70', NULL, 'RPT', 'RECOMMENDER,ASSESSOR', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('recommender', 'batchgr', 'Recommending Approval', 'state', '75', NULL, 'RPT', 'RECOMMENDER,ASSESSOR', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('forprovsubmission', 'batchgr', 'For Province Submission', 'state', '80', NULL, 'RPT', 'RECOMMENDER,ASSESSOR', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('forprovapproval', 'batchgr', 'For Province Approval', 'state', '81', NULL, 'RPT', 'RECOMMENDER,ASSESSOR', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('forapproval', 'batchgr', 'Provincial Assessor Approval', 'state', '85', NULL, 'RPT', 'APPROVER,ASSESSOR', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('assign-approver', 'batchgr', 'For Provincial Assessor Approval', 'state', '90', NULL, 'RPT', 'APPROVER,ASSESSOR', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('approver', 'batchgr', 'Provincial Assessor Approval', 'state', '95', NULL, 'RPT', 'APPROVER,ASSESSOR', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('provapprover', 'batchgr', 'Approved By Province', 'state', '96', NULL, 'RPT', 'APPROVER', NULL, NULL, NULL)
go
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role], [properties], [ui], [tracktime]) VALUES ('end', 'batchgr', 'End', 'end', '1000', NULL, 'RPT', NULL, NULL, NULL, NULL)
go

INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission], [caption], [ui]) VALUES ('start', 'batchgr', '', 'assign-receiver', '1', NULL, NULL, NULL, NULL, NULL)
go
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission], [caption], [ui]) VALUES ('assign-receiver', 'batchgr', '', 'receiver', '2', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL, NULL, NULL)
go
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission], [caption], [ui]) VALUES ('receiver', 'batchgr', 'submit', 'assign-provtaxmapper', '5', NULL, '[caption:''Submit For Taxmapping'', confirm:''Submit?'']', NULL, NULL, NULL)
go
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission], [caption], [ui]) VALUES ('assign-examiner', 'batchgr', '', 'examiner', '10', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL, NULL, NULL)
go
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission], [caption], [ui]) VALUES ('examiner', 'batchgr', 'returnreceiver', 'receiver', '15', NULL, '[caption:''Return to Receiver'', confirm:''Return to receiver?'', messagehandler:''default'']', NULL, NULL, NULL)
go
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission], [caption], [ui]) VALUES ('examiner', 'batchgr', 'submit', 'assign-provtaxmapper', '16', NULL, '[caption:''Submit for Approval'', confirm:''Submit?'']', NULL, NULL, NULL)
go
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission], [caption], [ui]) VALUES ('assign-provtaxmapper', 'batchgr', '', 'provtaxmapper', '20', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL, NULL, NULL)
go
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission], [caption], [ui]) VALUES ('provtaxmapper', 'batchgr', 'returnexaminer', 'examiner', '25', NULL, '[caption:''Return to Examiner'', confirm:''Return to examiner?'', messagehandler:''default'']', NULL, NULL, NULL)
go
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission], [caption], [ui]) VALUES ('provtaxmapper', 'batchgr', 'submit', 'assign-provappraiser', '26', NULL, '[caption:''Submit for Approval'', confirm:''Submit?'', messagehandler:''rptmessage:sign'']', NULL, NULL, NULL)
go
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission], [caption], [ui]) VALUES ('assign-provappraiser', 'batchgr', '', 'provappraiser', '40', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL, NULL, NULL)
go
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission], [caption], [ui]) VALUES ('provappraiser', 'batchgr', 'returntaxmapper', 'provtaxmapper', '45', NULL, '[caption:''Return to Taxmapper'', confirm:''Return to taxmapper?'', messagehandler:''default'']', NULL, NULL, NULL)
go
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission], [caption], [ui]) VALUES ('provappraiser', 'batchgr', 'returnexaminer', 'examiner', '46', NULL, '[caption:''Return to Examiner'', confirm:''Return to examiner?'', messagehandler:''default'']', NULL, NULL, NULL)
go
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission], [caption], [ui]) VALUES ('provappraiser', 'batchgr', 'submit', 'assign-approver', '47', NULL, '[caption:''Submit for Approval'', confirm:''Submit?'', messagehandler:''rptmessage:sign'']', NULL, NULL, NULL)
go
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission], [caption], [ui]) VALUES ('assign-approver', 'batchgr', '', 'approver', '70', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL, NULL, NULL)
go
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission], [caption], [ui]) VALUES ('approver', 'batchgr', 'approve', 'provapprover', '90', NULL, '[caption:''Approve'', confirm:''Approve record?'', messagehandler:''rptmessage:sign'']', NULL, NULL, NULL)
go
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission], [caption], [ui]) VALUES ('provapprover', 'batchgr', 'backforprovapproval', 'approver', '95', NULL, '[caption:''Cancel Posting'', confirm:''Cancel posting record?'']', NULL, NULL, NULL)
go
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission], [caption], [ui]) VALUES ('provapprover', 'batchgr', 'completed', 'end', '100', NULL, '[caption:''Approved'', visible:false]', NULL, NULL, NULL)
go
