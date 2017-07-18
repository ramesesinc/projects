delete from sys_wf_transition where processname = 'faas' order by idx;
delete from sys_wf_node where processname = 'faas' order by idx;
delete from sys_wf where name = 'faas';



INSERT INTO `sys_wf` (`name`, `title`) VALUES ('faas', 'FAAS Transaction Workflow');

INSERT INTO `sys_wf_node` (`name`, `processname`, `title`, `nodetype`, `idx`, `salience`, `domain`, `role`) VALUES ('start', 'faas', 'Start', 'start', '1', NULL, 'RPT', NULL);
INSERT INTO `sys_wf_node` (`name`, `processname`, `title`, `nodetype`, `idx`, `salience`, `domain`, `role`) VALUES ('assign-receiver', 'faas', 'For Review and Verification', 'state', '2', NULL, 'RPT', 'RECEIVER');
INSERT INTO `sys_wf_node` (`name`, `processname`, `title`, `nodetype`, `idx`, `salience`, `domain`, `role`) VALUES ('receiver', 'faas', 'Review and Verification', 'state', '5', NULL, 'RPT', 'RECEIVER');
INSERT INTO `sys_wf_node` (`name`, `processname`, `title`, `nodetype`, `idx`, `salience`, `domain`, `role`) VALUES ('assign-examiner', 'faas', 'For Examination', 'state', '10', NULL, 'RPT', 'EXAMINER');
INSERT INTO `sys_wf_node` (`name`, `processname`, `title`, `nodetype`, `idx`, `salience`, `domain`, `role`) VALUES ('examiner', 'faas', 'Examination', 'state', '15', NULL, 'RPT', 'EXAMINER');
INSERT INTO `sys_wf_node` (`name`, `processname`, `title`, `nodetype`, `idx`, `salience`, `domain`, `role`) VALUES ('assign-taxmapper', 'faas', 'For Taxmapping', 'state', '20', NULL, 'RPT', 'TAXMAPPER');
INSERT INTO `sys_wf_node` (`name`, `processname`, `title`, `nodetype`, `idx`, `salience`, `domain`, `role`) VALUES ('taxmapper', 'faas', 'Taxmapping', 'state', '25', NULL, 'RPT', 'TAXMAPPER');
INSERT INTO `sys_wf_node` (`name`, `processname`, `title`, `nodetype`, `idx`, `salience`, `domain`, `role`) VALUES ('assign-taxmapping-approval', 'faas', 'For Taxmapping Approval', 'state', '30', NULL, 'RPT', 'TAXMAPPER_CHIEF');
INSERT INTO `sys_wf_node` (`name`, `processname`, `title`, `nodetype`, `idx`, `salience`, `domain`, `role`) VALUES ('taxmapper_chief', 'faas', 'Taxmapping Approval', 'state', '35', NULL, 'RPT', 'TAXMAPPER_CHIEF');
INSERT INTO `sys_wf_node` (`name`, `processname`, `title`, `nodetype`, `idx`, `salience`, `domain`, `role`) VALUES ('assign-appraiser', 'faas', 'For Appraisal', 'state', '40', NULL, 'RPT', 'APPRAISER');
INSERT INTO `sys_wf_node` (`name`, `processname`, `title`, `nodetype`, `idx`, `salience`, `domain`, `role`) VALUES ('appraiser', 'faas', 'Appraisal', 'state', '45', NULL, 'RPT', 'APPRAISER');
INSERT INTO `sys_wf_node` (`name`, `processname`, `title`, `nodetype`, `idx`, `salience`, `domain`, `role`) VALUES ('assign-appraisal-chief', 'faas', 'For Appraisal Approval', 'state', '50', NULL, 'RPT', 'APPRAISAL_CHIEF');
INSERT INTO `sys_wf_node` (`name`, `processname`, `title`, `nodetype`, `idx`, `salience`, `domain`, `role`) VALUES ('appraiser_chief', 'faas', 'Appraisal Approval', 'state', '55', NULL, 'RPT', 'APPRAISAL_CHIEF');
INSERT INTO `sys_wf_node` (`name`, `processname`, `title`, `nodetype`, `idx`, `salience`, `domain`, `role`) VALUES ('assign-approver', 'faas', 'For Provincial Assessor Approval', 'state', '80', NULL, 'RPT', 'APPROVER,ASSESSOR');
INSERT INTO `sys_wf_node` (`name`, `processname`, `title`, `nodetype`, `idx`, `salience`, `domain`, `role`) VALUES ('approver', 'faas', 'Provincial Assessor Approval', 'state', '85', NULL, 'RPT', 'APPROVER,ASSESSOR');
INSERT INTO `sys_wf_node` (`name`, `processname`, `title`, `nodetype`, `idx`, `salience`, `domain`, `role`) VALUES ('end', 'faas', 'End', 'end', '1000', NULL, 'RPT', NULL);



INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('start', 'faas', NULL, 'assign-receiver', '1', NULL, NULL, NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('assign-receiver', 'faas', NULL, 'receiver', '2', NULL, '[caption:\'Assign To Me\', confirm:\'Assign task to you?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('receiver', 'faas', 'delete', 'end', '5', NULL, '[caption:\'Delete\', confirm:\'Delete record?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('receiver', 'faas', 'submit', 'assign-examiner', '6', NULL, '[caption:\'Submit For Examination\', confirm:\'Submit?\', showmessage:false]', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('assign-examiner', 'faas', NULL, 'examiner', '10', NULL, '[caption:\'Assign To Me\', confirm:\'Assign task to you?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('examiner', 'faas', 'return', 'receiver', '15', NULL, '[caption:\'Return to Receiver\',confirm:\'Return to receiver?\', messagehandler:\'default\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('examiner', 'faas', 'submit', 'assign-taxmapper', '16', NULL, '[caption:\'Submit for Taxmapping\', confirm:\'Submit for taxmapping?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('assign-taxmapper', 'faas', NULL, 'taxmapper', '20', NULL, '[caption:\'Assign To Me\', confirm:\'Assign task to you?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('taxmapper', 'faas', 'return_receiver', 'receiver', '25', NULL, '[caption:\'Return to Receiver\',confirm:\'Return to receiver?\',messagehandler:\'default\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('taxmapper', 'faas', 'return_examiner', 'examiner', '26', NULL, '[caption:\'Return to Examiner\',confirm:\'Return to examiner?\', messagehandler:\'default\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('taxmapper', 'faas', 'submit', 'assign-taxmapping-approval', '27', NULL, '[caption:\'Submit for Approval\', confirm:\'Submit for approval?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('assign-taxmapping-approval', 'faas', NULL, 'taxmapper_chief', '30', NULL, '[caption:\'Assign To Me\', confirm:\'Assign task to you?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('taxmapper_chief', 'faas', 'return_taxmapper', 'taxmapper', '35', NULL, '[caption:\'Return to Taxmapper\',confirm:\'Return to taxmapper?\', messagehandler:\'default\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('taxmapper_chief', 'faas', 'submit', 'assign-appraiser', '36', NULL, '[caption:\'Submit for Appraisal\', confirm:\'Submit for appraisal?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('assign-appraiser', 'faas', NULL, 'appraiser', '40', NULL, '[caption:\'Assign To Me\', confirm:\'Assign task to you?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('appraiser', 'faas', 'return_examiner', 'examiner', '45', NULL, '[caption:\'Return to Examiner\',confirm:\'Return to examiner?\', messagehandler:\'default\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('appraiser', 'faas', 'return_taxmapper', 'taxmapper', '46', NULL, '[caption:\'Return to Taxmapper\',confirm:\'Return to taxmapper?\', messagehandler:\'default\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('appraiser', 'faas', 'submit', 'assign-appraisal-chief', '47', NULL, '[caption:\'Submit for Approval\', confirm:\'Submit for approval?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('assign-appraisal-chief', 'faas', NULL, 'appraiser_chief', '50', NULL, '[caption:\'Assign To Me\', confirm:\'Assign task to you?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('appraiser_chief', 'faas', 'return_appraiser', 'appraiser', '55', NULL, '[caption:\'Return to Appraiser\',confirm:\'Return to appraiser?\', messagehandler:\'default\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('appraiser_chief', 'faas', 'submit', 'assign-approver', '56', NULL, '[caption:\'Submit for Assessor Approval\', confirm:\'Submit for assistant assessor approval?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('assign-approver', 'faas', NULL, 'approver', '70', NULL, '[caption:\'Assign To Me\', confirm:\'Assign task to you?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('approver', 'faas', NULL, 'end', '75', NULL, '[caption:\'Approve\', confirm:\'Approve FAAS?\', closeonend:false]', NULL);
