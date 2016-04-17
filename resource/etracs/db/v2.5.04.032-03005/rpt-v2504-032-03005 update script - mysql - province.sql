/* v2.5.04.032-03005 */

alter table rpt_changeinfo add refid varchar(50);

update rpt_changeinfo set refid = faasid where refid is null;


	
-- workflow: disapprove support 	
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('receiver', 'faas', 'disapprove', 'end', '7', NULL, '[caption:\'Disapprove\', confirm:\'Disapprove FAAS?\', messagehandler:\'default\']', '');

INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) 
VALUES ('receiver', 'subdivision', 'disapprove', 'end', '7', NULL, '[caption:\'Disapprove\', confirm:\'Disapprove Subdivision?\', messagehandler:\'default\']', '');

INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) 
VALUES ('receiver', 'consolidation', 'disapprove', 'end', '7', NULL, '[caption:\'Disapprove\', confirm:\'Disapprove Consolidation?\', messagehandler:\'default\']', '');

