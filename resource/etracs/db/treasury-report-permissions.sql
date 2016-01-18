
delete from sys_usergroup_permission where usergroup_objid='TREASURY.REPORT';

insert into sys_usergroup_permission (objid, usergroup_objid, object, permission, title) values('TREASURY-REPORT-abstractofcollection-viewreport','TREASURY.REPORT','abstractofcollection','viewreport','View Abstract of Collection Report');
insert into sys_usergroup_permission (objid, usergroup_objid, object, permission, title) values('TREASURY-REPORT-cashbook-viewreport','TREASURY.REPORT','cashbook','viewreport','View Cashbook Report');
insert into sys_usergroup_permission (objid, usergroup_objid, object, permission, title) values('TREASURY-REPORT-collectionbyfund-viewreport','TREASURY.REPORT','collectionbyfund','viewreport','View Collection By Fund Report');
insert into sys_usergroup_permission (objid, usergroup_objid, object, permission, title) values('TREASURY-REPORT-craaf-viewreport','TREASURY.REPORT','craaf','viewreport','View CRAAF');
insert into sys_usergroup_permission (objid, usergroup_objid, object, permission, title) values('TREASURY-REPORT-dailycollection-viewreport','TREASURY.REPORT','dailycollection','viewreport','View Dialy Collection Report');
insert into sys_usergroup_permission (objid, usergroup_objid, object, permission, title) values('TREASURY-REPORT-raaf-viewreport','TREASURY.REPORT','raaf','viewreport','View RAAF');
insert into sys_usergroup_permission (objid, usergroup_objid, object, permission, title) values('TREASURY-REPORT-statementofrevenue-viewreport','TREASURY.REPORT','srs','viewreport','View SRS Report');
