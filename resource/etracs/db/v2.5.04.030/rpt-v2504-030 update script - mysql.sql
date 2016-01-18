/*==================================
** V2.5.04.030
==================================*/
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-SUMMARY', 'landassessment', 'After Summary Computation', '105');
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-SUMMARY', 'bldgassessment', 'After Summary Computation', '105');
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-SUMMARY', 'machassessment', 'After Summary Computation', '105');
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-SUMMARY', 'miscassessment', 'After Summary Computation', '105');
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-SUMMARY', 'planttreeassessment', 'After Summary Computation', '105');
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('MUASSESSLEVEL', 'machassessment', 'Actual Use Assess Level Computation', '50');
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('MUASSESSEDVALUE', 'machassessment', 'Actual Use Assessed Value Computation', '55');

UPDATE sys_rulegroup SET name='INITIAL', ruleset='machassessment', title='Initial Computation', sortorder='0' WHERE (name='INITIAL') AND (ruleset='machassessment');
UPDATE sys_rulegroup SET name='BASEMARKETVALUE', ruleset='machassessment', title='Machine Base Market Value Computation', sortorder='5' WHERE (name='BASEMARKETVALUE') AND (ruleset='machassessment');
UPDATE sys_rulegroup SET name='AFTER-BASEMARKETVALUE', ruleset='machassessment', title='After Machine Base Market Value Computation', sortorder='10' WHERE (name='AFTER-BASEMARKETVALUE') AND (ruleset='machassessment');
UPDATE sys_rulegroup SET name='DEPRECIATION', ruleset='machassessment', title='Machine Depreciation Computation', sortorder='11' WHERE (name='DEPRECIATION') AND (ruleset='machassessment');
UPDATE sys_rulegroup SET name='AFTER-DEPRECIATION', ruleset='machassessment', title='After Machine Depreciation Computation', sortorder='12' WHERE (name='AFTER-DEPRECIATION') AND (ruleset='machassessment');
UPDATE sys_rulegroup SET name='MARKETVALUE', ruleset='machassessment', title='Machine Market Value Computation', sortorder='25' WHERE (name='MARKETVALUE') AND (ruleset='machassessment');
UPDATE sys_rulegroup SET name='AFTER-MARKETVALUE', ruleset='machassessment', title='After Machine Market Value Computation', sortorder='30' WHERE (name='AFTER-MARKETVALUE') AND (ruleset='machassessment');
UPDATE sys_rulegroup SET name='ASSESSLEVEL', ruleset='machassessment', title='Machine Assess Level Computation', sortorder='35' WHERE (name='ASSESSLEVEL') AND (ruleset='machassessment');
UPDATE sys_rulegroup SET name='AFTER-ASSESSLEVEL', ruleset='machassessment', title='After Machine Assess Level Computation', sortorder='36' WHERE (name='AFTER-ASSESSLEVEL') AND (ruleset='machassessment');
UPDATE sys_rulegroup SET name='ASSESSEDVALUE', ruleset='machassessment', title='Machine Assessed Value Computation', sortorder='40' WHERE (name='ASSESSEDVALUE') AND (ruleset='machassessment');
UPDATE sys_rulegroup SET name='AFTER-ASSESSEDVALUE', ruleset='machassessment', title='After Machine Assessed Value Computation', sortorder='45' WHERE (name='AFTER-ASSESSEDVALUE') AND (ruleset='machassessment');


alter table rpt_redflag add info text;

alter table bldguse add addlinfo varchar(255);


alter table faas_signatory 
	add provrecommender_objid	varchar(50),
	add provrecommender_name	varchar(100),
	add provrecommender_title	varchar(50),
	add provrecommender_dtsigned	datetime,
	add provrecommender_taskid	varchar(50);

	
alter table subdividedland modify column itemno	varchar(10)	null;
alter table subdividedland modify column newtdno	varchar(50)	null;
alter table subdividedland modify column newutdno	varchar(50)	null;
alter table subdividedland modify column newtitletype	varchar(50)	null;
alter table subdividedland modify column newtitleno	varchar(50)	null;
alter table subdividedland modify column newtitledate	varchar(50)	null;
alter table subdividedland modify column areasqm	decimal(16,6)	null;
alter table subdividedland modify column areaha	decimal(16,6)	null;
alter table subdividedland modify column memoranda	varchar(500)	null;
alter table subdividedland modify column administrator_objid	varchar(50)	null;
alter table subdividedland modify column administrator_name	varchar(200)	null;
alter table subdividedland modify column administrator_address	varchar(200)	null;
alter table subdividedland modify column taxpayer_objid	varchar(50)	null;
alter table subdividedland modify column taxpayer_name	varchar(200)	null;
alter table subdividedland modify column taxpayer_address	varchar(200)	null;
alter table subdividedland modify column owner_name	varchar(200)	null;
alter table subdividedland modify column owner_address	varchar(200)	null;
alter table subdividedland modify column newrpuid	varchar(50)	null;
alter table subdividedland modify column newfaasid	varchar(50)	null;



alter table consolidation modify column txndate	datetime null;
alter table consolidation modify column txntype_objid	varchar(50) null;
alter table consolidation modify column autonumber	int(11) null;
alter table consolidation modify column effectivityyear	int(11) null;
alter table consolidation modify column effectivityqtr	int(11) null;
alter table consolidation modify column newtdno	varchar(50) null;
alter table consolidation modify column newutdno	varchar(50) null;
alter table consolidation modify column newtitletype	varchar(50) null;
alter table consolidation modify column newtitleno	varchar(50) null;
alter table consolidation modify column newtitledate	varchar(50) null;
alter table consolidation modify column memoranda	text null;
alter table consolidation modify column lguid	varchar(50) null;
alter table consolidation modify column lgutype	varchar(50) null;
alter table consolidation modify column newrpid	varchar(50) null;
alter table consolidation modify column newrpuid	varchar(50) null;
alter table consolidation modify column newfaasid	varchar(50) null;
alter table consolidation modify column taxpayer_objid	varchar(50) null;
alter table consolidation modify column taxpayer_name	text null;
alter table consolidation modify column taxpayer_address	varchar(200) null;
alter table consolidation modify column owner_name	text null;
alter table consolidation modify column owner_address	varchar(200) null;
alter table consolidation modify column administrator_objid	varchar(50) null;
alter table consolidation modify column administrator_name	varchar(500) null;
alter table consolidation modify column administrator_address	varchar(200) null;
alter table consolidation modify column administratorid	varchar(50) null;
alter table consolidation modify column administratorname	varchar(500) null;
alter table consolidation modify column administratoraddress	varchar(200) null;
alter table consolidation modify column signatories	varchar(500) null;
alter table consolidation modify column originlguid	varchar(50) null;
