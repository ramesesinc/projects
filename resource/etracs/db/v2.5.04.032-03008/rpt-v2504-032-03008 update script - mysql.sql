/* v2.5.04.032-03008 */
/*========================================
*
* REPLACE etracs254_lguname with the 
* correct PRODUCTION Database Name
*=======================================*/

set @dbname := 'etracs254_lguname';

/*=======================================*/


drop procedure if exists alter_sys_rule_actiondef;

delimiter $$
create procedure alter_sys_rule_actiondef(in _dbname varchar(100))
begin
    declare _count int;
    set _count = (  select count(*) 
                    from information_schema.columns
                    where  table_schema = _dbname and 
                           table_name = 'sys_rule_actiondef' and 
                           column_name = 'actionclass');

    if _count = 0 then
        alter table sys_rule_actiondef
            add column actionclass varchar(100);
    end if;
end $$
delimiter ;

call alter_sys_rule_actiondef(@dbname);

drop procedure if exists alter_sys_rule_actiondef;


create table faas_restriction_type
(
	objid varchar(50),
	name varchar(100) not null,
	idx int not null,
	isother int not null,
	primary key(objid)
)engine=innodb default charset=utf8;


INSERT INTO `faas_restriction_type` (`objid`, `name`, `idx`, `isother`) VALUES ('BOUNDARY_CONFLICT', 'Boundary Conflict', '4', '1');
INSERT INTO `faas_restriction_type` (`objid`, `name`, `idx`, `isother`) VALUES ('BSP_GSP', 'BSP / GSP', '9', '1');
INSERT INTO `faas_restriction_type` (`objid`, `name`, `idx`, `isother`) VALUES ('CARP', 'Under CARP', '1', '0');
INSERT INTO `faas_restriction_type` (`objid`, `name`, `idx`, `isother`) VALUES ('RED_AREAS', 'Red Areas', '3', '1');
INSERT INTO `faas_restriction_type` (`objid`, `name`, `idx`, `isother`) VALUES ('RP_NIA', 'RP / NIA', '5', '1');
INSERT INTO `faas_restriction_type` (`objid`, `name`, `idx`, `isother`) VALUES ('TELECOM', 'Telecom', '6', '1');
INSERT INTO `faas_restriction_type` (`objid`, `name`, `idx`, `isother`) VALUES ('UNDER_LITIGATION', 'Under Litigation', '2', '0');
INSERT INTO `faas_restriction_type` (`objid`, `name`, `idx`, `isother`) VALUES ('UNDETERMINED', 'Undermined', '7', '1');
INSERT INTO `faas_restriction_type` (`objid`, `name`, `idx`, `isother`) VALUES ('UNLOCATED_OWNER', 'Unlocated Owner', '8', '1');
INSERT INTO `faas_restriction_type` (`objid`, `name`, `idx`, `isother`) VALUES ('RESTRICTED', 'Restricted', '9', '1');



INSERT INTO `sys_usergroup` (`objid`, `title`, `domain`, `userclass`, `orgclass`, `role`) 
VALUES ('RPT.REPORT', 'REPORT', 'RPT', 'usergroup', NULL, 'REPORT');

INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) 
VALUES ('RPT.REPORT-faas-titled-report-viewreport', 'RPT.REPORT', 'faas-titled-report', 'viewreport', 'View Report');



alter table subdivision_cancelledimprovement add lasttaxyear int;
alter table subdivision_cancelledimprovement add lguid varchar(50);
alter table subdivision_cancelledimprovement add reason_objid varchar(50);


/* RPLEDGER: add blockno info */
alter table rptledger add blockno varchar(50);

create index ix_rptledger_blockno on rptledger(blockno);

update rptledger rl, faas f, realproperty rp set 
    rl.blockno = rp.blockno
where rl.faasid = f.objid and f.realpropertyid = rp.objid;


/* PC and DT txn types */
INSERT INTO `faas_txntype` (`objid`, `name`, `newledger`, `newrpu`, `newrealproperty`, `displaycode`, `allowEditOwner`, `checkbalance`, `allowEditPin`, `allowEditPinInfo`, `allowEditAppraisal`, `opener`) VALUES ('PC', 'Physical Obsolence', '0', '1', '0', 'PC', '0', '0', '0', '0', '1', NULL);
INSERT INTO `faas_txntype` (`objid`, `name`, `newledger`, `newrpu`, `newrealproperty`, `displaycode`, `allowEditOwner`, `checkbalance`, `allowEditPin`, `allowEditPinInfo`, `allowEditAppraisal`, `opener`) VALUES ('DT', 'Destruction of Property', '0', '1', '0', 'DT', '0', '0', '0', '0', '1', NULL);



alter table faas_txntype add reconcileledger int ;
update faas_txntype set reconcileledger = 1 where reconcileledger is null;

INSERT INTO `faas_txntype` (`objid`, `name`, `newledger`, `newrpu`, `newrealproperty`, `displaycode`, `allowEditOwner`, `checkbalance`, `allowEditPin`, `allowEditPinInfo`, `allowEditAppraisal`, `opener`, `reconcileledger`) VALUES ('UK', 'Unknown to Known', '1', '1', '1', 'DP', '1', '0', '0', '0', '1', NULL, '0');
update faas_txntype set reconcileledger = 0 where objid = 'UK';    
    
alter table rptledger drop key ux_rptledger_fullpin;    



/* CANCELLED FAAS */

alter table cancelledfaas add originlguid varchar(50);

update cancelledfaas set originlguid = lguid where originlguid is null;


delete from sys_wf_transition where processname = 'cancelledfaas';
    
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('start', 'cancelledfaas', '', 'receiver', '1', NULL, NULL, NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('receiver', 'cancelledfaas', 'delete', 'end', '5', NULL, '[caption:\'Delete\', confirm:\'Delete record?\', closeonend:true]', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('receiver', 'cancelledfaas', 'submit', 'assign-taxmapper', '16', NULL, '[caption:\'Submit for Taxmapping\', confirm:\'Submit for taxmapping?\', messagehandler:\'default\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('assign-taxmapper', 'cancelledfaas', '', 'taxmapper', '20', NULL, '[caption:\'Assign To Me\', confirm:\'Assign task to you?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('taxmapper', 'cancelledfaas', 'return_receiver', 'receiver', '25', NULL, '[caption:\'Return to Receiver\',confirm:\'Return to receiver?\',messagehandler:\'default\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('taxmapper', 'cancelledfaas', 'submit', 'assign-recommender', '27', NULL, '[caption:\'Submit for Recommending Approval\', confirm:\'Submit?\', messagehandler:\'rptmessage:create\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('assign-recommender', 'cancelledfaas', '', 'recommender', '70', NULL, '[caption:\'Assign To Me\', confirm:\'Assign task to you?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('recommender', 'cancelledfaas', 'return_receiver', 'receiver', '71', NULL, '[caption:\'Return to Receiver\',confirm:\'Return to receiver?\', messagehandler:\'default\']', '');
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('recommender', 'cancelledfaas', 'return_appraiser', 'appraiser', '72', NULL, '[caption:\'Return to Appraiser\',confirm:\'Return to appraiser?\', messagehandler:\'default\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('recommender', 'cancelledfaas', 'return_taxmapper', 'taxmapper', '73', NULL, '[caption:\'Return to Taxmapper\',confirm:\'Return to taxmapper?\', messagehandler:\'default\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('recommender', 'cancelledfaas', 'submit_approver', 'assign-approver', '74', NULL, '[caption:\'Submit for Assessor Approval\', confirm:\'Submit to assessor approval?\', messagehandler:\'default\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('recommender', 'cancelledfaas', 'submit_to_province', 'approver', '75', NULL, '[caption:\'Submit to Province\', confirm:\'Submit to Province?\', messagehandler:\'rptmessage:create\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('assign-approver', 'cancelledfaas', '', 'approver', '80', NULL, '[caption:\'Assign To Me\', confirm:\'Assign task to you?\']', NULL);
INSERT INTO `sys_wf_transition` (`parentid`, `processname`, `action`, `to`, `idx`, `eval`, `properties`, `permission`) VALUES ('approver', 'cancelledfaas', 'approve', 'end', '110', NULL, '[caption:\'Manually Approve\',confirm:\'Manually approve cancellation?\']', NULL);

update sys_wf_transition set action = '' where action is null;


ALTER TABLE `sys_wf_transition`
MODIFY COLUMN `action`  varchar(50) not null,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`parentid`, `processname`, `to`, `action`);


/* insert rptledger restrictions */
insert into rptledger_restriction(objid, parentid, restrictionid, remarks)
select distinct f.objid, rlf.rptledgerid, f.restrictionid, null 
from faas f 
    inner join rptledgerfaas rlf on f.objid = rlf.faasid 
where f.restrictionid is not null 
and not exists(select * from rptledger_restriction where parentid = rlf.rptledgerid and restrictionid = f.restrictionid);

