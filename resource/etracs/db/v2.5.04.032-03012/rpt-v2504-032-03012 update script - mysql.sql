/* v2.5.04.032-03012 */
INSERT INTO `sys_usergroup` (`objid`, `title`, `domain`, `userclass`, `orgclass`, `role`) VALUES ('RPT.MANAGEMENT', 'MANAGEMENT', 'RPT', NULL, NULL, 'MANAGEMENT');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('PER-41ea09c:157ef7851c2:-7df4', 'RPT.MANAGEMENT', 'report', 'txn-monitoring', 'Transaction Monitoring');

create index ix_realproperty_claimno on realproperty(claimno);

create index ix_dtinspected on examiner_finding(dtinspected);


create index ix_assignee_objid on faas_task(assignee_objid);
create index ix_assignee_objid on subdivision_task(assignee_objid);
create index ix_assignee_objid on consolidation_task(assignee_objid);
create index ix_assignee_objid on cancelledfaas_task(assignee_objid);

alter table faas_txntype add allowannotated int;
update faas_txntype set allowannotated = 0 where allowannotated is null;			
