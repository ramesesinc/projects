/* v2.5.04.032-03006 */
alter table cancelledfaas add txnno varchar(25) null;
create index ix_cancelledfaas_txnno on cancelledfaas(txnno);


delete from sys_wf_transition where processname = 'cancelledfaas';
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('start', 'cancelledfaas', NULL, 'receiver', '1', NULL, NULL, NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('receiver', 'cancelledfaas', 'delete', 'end', '5', NULL, '[caption:\'Delete\', confirm:\'Delete record?\', closeonend:true]', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('receiver', 'cancelledfaas', 'submit', 'assign-taxmapper', '16', NULL, '[caption:\'Submit for Taxmapping\', confirm:\'Submit for taxmapping?\', messagehandler:\'default\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('assign-taxmapper', 'cancelledfaas', NULL, 'taxmapper', '20', NULL, '[caption:\'Assign To Me\', confirm:\'Assign task to you?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('taxmapper', 'cancelledfaas', 'return_receiver', 'receiver', '25', NULL, '[caption:\'Return to Receiver\',confirm:\'Return to receiver?\',messagehandler:\'default\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('taxmapper', 'cancelledfaas', 'submit', 'assign-recommender', '27', NULL, '[caption:\'Submit for Recommending Approval\', confirm:\'Submit?\', messagehandler:\'rptmessage:create\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('assign-recommender', 'cancelledfaas', NULL, 'recommender', '70', NULL, '[caption:\'Assign To Me\', confirm:\'Assign task to you?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('recommender', 'cancelledfaas', 'return_appraiser', 'appraiser', '72', NULL, '[caption:\'Return to Appraiser\',confirm:\'Return to appraiser?\', messagehandler:\'default\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('recommender', 'cancelledfaas', 'return_taxmapper', 'taxmapper', '73', NULL, '[caption:\'Return to Taxmapper\',confirm:\'Return to taxmapper?\', messagehandler:\'default\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('recommender', 'cancelledfaas', 'submit_approver', 'assign-approver', '74', NULL, '[caption:\'Submit for Assessor Approval\', confirm:\'Submit to assessor approval?\', messagehandler:\'default\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('recommender', 'cancelledfaas', 'submit_to_province', 'approver', '75', NULL, '[caption:\'Submit to Province\', confirm:\'Submit to Province?\', messagehandler:\'rptmessage:create\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('assign-approver', 'cancelledfaas', NULL, 'approver', '80', NULL, '[caption:\'Assign To Me\', confirm:\'Assign task to you?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('approver', 'cancelledfaas', 'approve', 'end', '110', NULL, '[caption:\'Manually Approve\',confirm:\'Manually approve cancellation?\', visible:false]', NULL);


