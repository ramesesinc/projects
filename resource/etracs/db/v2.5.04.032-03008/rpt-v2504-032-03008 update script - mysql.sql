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

