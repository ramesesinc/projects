/* v2.5.04.032-03005 */

alter table rpt_changeinfo add refid varchar(50);

update rpt_changeinfo set refid = faasid where refid is null;


	
-- workflow: disapprove support 	
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('receiver', 'faas', 'disapprove', 'end', '7', NULL, '[caption:\'Disapprove\', confirm:\'Disapprove FAAS?\', messagehandler:\'default\']', '');

INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) 
VALUES ('receiver', 'subdivision', 'disapprove', 'end', '7', NULL, '[caption:\'Disapprove\', confirm:\'Disapprove Subdivision?\', messagehandler:\'default\']', '');

INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) 
VALUES ('receiver', 'consolidation', 'disapprove', 'end', '7', NULL, '[caption:\'Disapprove\', confirm:\'Disapprove Consolidation?\', messagehandler:\'default\']', '');



/* LEDGER RESTRICTION SUPPORT */
create table rptledger_restriction
(
	objid varchar(50) not null,
	parentid varchar(50) not null, 
	restrictionid varchar(50) not null,
	remarks varchar(150),
	primary key (objid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table rptledger_restriction 
	add constraint FK_rptledger_restriction_rptledger 
	foreign key(parentid) references rptledger(objid);

create unique index ux_rptledger_restriction on rptledger_restriction(parentid, restrictionid);


	