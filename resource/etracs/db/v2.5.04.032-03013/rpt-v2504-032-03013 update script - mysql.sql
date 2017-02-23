#254032-03013

create table memoranda_template
(
	objid varchar(50) primary key,
	code varchar(25) not null,
	template varchar(500) not null
);


alter table rpu_assessment modify column classification_objid varchar(50)  null;
alter table rpu_assessment modify column actualuse_objid varchar(50)  null;


INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-bldgkind-create', 'RPT.MASTER', 'bldgkind', 'create', 'Create Kind of Building');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-bldgkind-delete', 'RPT.MASTER', 'bldgkind', 'delete', 'Delete Kind of Building');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-bldgkind-edit', 'RPT.MASTER', 'bldgkind', 'edit', 'Edit Kind of Building');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-bldgkind-read', 'RPT.MASTER', 'bldgkind', 'read', 'Open Kind of Building');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-landspecificclass-delete', 'RPT.MASTER', 'landspecificclass', 'delete', 'Delete Land Specific Class');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-landspecificclass-create', 'RPT.MASTER', 'landspecificclass', 'create', 'Create Land Specific Class');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-landspecificclass-read', 'RPT.MASTER', 'landspecificclass', 'read', 'Open Land Specific Class');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-landspecificclass-edit', 'RPT.MASTER', 'landspecificclass', 'edit', 'Edit Land Specific Class');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-machine-create', 'RPT.MASTER', 'machine', 'create', 'Create Machine');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-machine-read', 'RPT.MASTER', 'machine', 'read', 'Open Machine');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-machine-edit', 'RPT.MASTER', 'machine', 'edit', 'Edit Machine');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-machine-delete', 'RPT.MASTER', 'machine', 'delete', 'Delete Machine');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-approve', 'RPT.MASTER', 'master', 'approve', 'Approve Memoranda Template');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-sync', 'RPT.MASTER', 'master', 'sync', 'Synchronize Master Files');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-import', 'RPT.MASTER', 'master', 'import', 'Import Master Files');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-disapprove', 'RPT.MASTER', 'master', 'disapprove', 'Disapprove Memoranda Template');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-viewlist', 'RPT.MASTER', 'master', 'viewlist', 'View Master Files');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-export', 'RPT.MASTER', 'master', 'export', 'Export Master Files');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-material-delete', 'RPT.MASTER', 'material', 'delete', 'Delete Material');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-material-edit', 'RPT.MASTER', 'material', 'edit', 'Edit Material');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-material-read', 'RPT.MASTER', 'material', 'read', 'Open Material');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-material-create', 'RPT.MASTER', 'material', 'create', 'Create Material');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-plant-read', 'RPT.MASTER', 'planttree', 'read', 'Open Plant/Tree');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-plant-edit', 'RPT.MASTER', 'planttree', 'edit', 'Edit Plant/Tree');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-plant-create', 'RPT.MASTER', 'planttree', 'create', 'Create Plant/Tree');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-plant-delete', 'RPT.MASTER', 'planttree', 'delete', 'Delete Plant/Tree');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-memoranda-read', 'RPT.MASTER', 'rptis_memoranda_template', 'read', 'Open Memoranda Template');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-memoranda-delete', 'RPT.MASTER', 'rptis_memoranda_template', 'delete', 'Delete Memoranda Template');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-memoranda-create', 'RPT.MASTER', 'rptis_memoranda_template', 'create', 'Create Memoranda Template');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-memoranda-edit', 'RPT.MASTER', 'rptis_memoranda_template', 'edit', 'Edit Memoranda Template');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-miscitem-read', 'RPT.MASTER', 'miscitem', 'read', 'Open Miscellaneous Item');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-miscitem-edit', 'RPT.MASTER', 'miscitem', 'edit', 'Edit Miscellaneous Item');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-miscitem-create', 'RPT.MASTER', 'miscitem', 'create', 'Create Miscellaneous Item');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-miscitem-delete', 'RPT.MASTER', 'miscitem', 'delete', 'Delete Miscellaneous Item');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-exemptiontype-create', 'RPT.MASTER', 'exemptiontype', 'create', 'Create Exemption Type');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-exemptiontype-delete', 'RPT.MASTER', 'exemptiontype', 'delete', 'Delete Exemption Type');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-exemptiontype-edit', 'RPT.MASTER', 'exemptiontype', 'edit', 'Edit Exemption Type');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-exemptiontype-read', 'RPT.MASTER', 'exemptiontype', 'read', 'Open Exemption Type');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-rptparameter-create', 'RPT.MASTER', 'rptparameter', 'create', 'Create Parameter');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-rptparameter-delete', 'RPT.MASTER', 'rptparameter', 'delete', 'Delete Parameter');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-rptparameter-edit', 'RPT.MASTER', 'rptparameter', 'edit', 'Edit Parameter');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-rptparameter-read', 'RPT.MASTER', 'rptparameter', 'read', 'Open Parameter');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-classification-create', 'RPT.MASTER', 'propertyclassification', 'create', 'Create Property Classification');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-classification-delete', 'RPT.MASTER', 'propertyclassification', 'delete', 'Delete Property Classification');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-classification-edit', 'RPT.MASTER', 'propertyclassification', 'edit', 'Edit Property Classification');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-classification-read', 'RPT.MASTER', 'propertyclassification', 'read', 'Open Property Classification');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-structure-create', 'RPT.MASTER', 'structure', 'create', 'Create Structure');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-structure-delete', 'RPT.MASTER', 'structure', 'delete', 'Delete Structure');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-structure-edit', 'RPT.MASTER', 'structure', 'edit', 'Edit Structure');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-structure-read', 'RPT.MASTER', 'structure', 'read', 'Open Structure');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-requirementtype-create', 'RPT.MASTER', 'requirementtype', 'create', 'Create Requirement Type');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-requirementtype-delete', 'RPT.MASTER', 'requirementtype', 'delete', 'Delete Requirement Type');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-requirementtype-edit', 'RPT.MASTER', 'requirementtype', 'edit', 'Edit Requirement Type');
INSERT INTO `sys_usergroup_permission` (`objid`, `usergroup_objid`, `object`, `permission`, `title`) VALUES ('RPT.MASTER-requirementtype-read', 'RPT.MASTER', 'requirementtype', 'read', 'Open Requirement Type');
